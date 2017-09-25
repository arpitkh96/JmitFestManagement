package database;

import java.io.*;
import java.sql.*;
import java.util.Vector;

public class CacheConnection {
  private static boolean verbose           = false;
  private static int     numberConnections = 0;
  private static Vector  cachedConnections = new Vector(  );
  private static Thread  monitor           = null;
  private static long    MAX_IDLE          = 1000*60*60;

  synchronized public static Connection checkOut() {
    return checkOut("Database");
  }

  synchronized public static Connection checkOut(String baseName) {
    boolean          found  = false;
    CachedConnection cached = null; 

    if (verbose) {
      System.out.println("There are " + 
       Integer.toString(numberConnections) + 
       " connections in the cache");
      System.out.println("Searching for a connection not in use...");
    }
    for (int i=0;!found && i<numberConnections;i++) {
      if (verbose) {
        System.out.println("Vector entry " + Integer.toString(i));
      }
      cached = (CachedConnection)cachedConnections.get(i);
      if (!cached.isInUse() && cached.getBaseName().equals(baseName)) {
        if (verbose) {
          System.out.println("found cached entry " + 
           Integer.toString(i) + 
           " for " + baseName);
        }
        found = true;
      }
    }
    if (found) {
      cached.setInUse(true);
    }
    else {
      if (verbose) {
        System.out.println("Cached entry not found ");
        System.out.println("Allocating new entry for " + baseName);
      }
      cached = new CachedConnection(
       JDBCMySQLConnection.getConnection(), true, baseName);
      cachedConnections.add(cached);
      numberConnections++;
    }

    if (monitor == null) {
      monitor = new Thread( 

       new Runnable(  ) { 
         public void run(  ) { 
           while(numberConnections > 0) { 
             runMonitor(  ); 
           } 
           monitor = null; 
           if (verbose) {
             System.out.println("CacheConnection monitor stopped"); 
           }
         } 
       } 
      );
      monitor.setDaemon(true);
      monitor.start(  );
    }
    return cached.getConnection(  );
  }

  synchronized public static void checkIn(Connection c) {
    boolean          found  = false;
    boolean          closed = false;
    CachedConnection cached = null; 
    Connection       conn   = null;
    int              i      = 0;

    if (verbose) {
      System.out.println("Searching for connection to set not in use...");
    }
    for (i=0;!found && i<numberConnections;i++) {
      if (verbose) {
        System.out.println("Vector entry " + Integer.toString(i));
      }
      cached = (CachedConnection)cachedConnections.get(i);
      conn = cached.getConnection(  );
      if (conn == c) {
        if (verbose) {
          System.out.println("found cached entry " + Integer.toString(i));
        }
        found = true; 
      }
    }
    if (found) {
      try { 
        closed = conn.isClosed(  );
      }
      catch(SQLException ignore) {
        closed = true;
      }
      if (!closed) 
        cached.setInUse(false);
      else {
        cachedConnections.remove(i);    
        numberConnections--;
      }
    }
    else if (verbose) {
      System.out.println("In use Connection not found!!!");
    }
  }

  synchronized private static void checkUse(  ) {
    CachedConnection cached = null; 
    Connection       conn   = null;
    int              i      = 0;
    long             now    = System.currentTimeMillis(  );
    long             then   = 0;

    for (i=numberConnections-1;i>-1;i--) {
      if (verbose) {
        System.out.println(
         "CacheConnection monitor checking vector entry " + 
         Integer.toString(i) + 
         " for use...");
      }
      cached = (CachedConnection)cachedConnections.get(i);
      if (!cached.isInUse(  )) {
        then = cached.getLastUsed(  );
        if ((now - then) > MAX_IDLE) {
          if (verbose) {
            System.out.println("Cached entry " + 
             Integer.toString(i) + 
             " idle too long, being destroyed");
          }
          conn = cached.getConnection(  );
          try { conn.close(  ); } catch (SQLException e) { 
          System.err.println("Unable to close connection: " + 
           e.getMessage(  )); }
          cachedConnections.remove(i);    
          numberConnections--;
        }
      }
    }
  }

  private static void runMonitor(  ) {
    checkUse(  );
    if (numberConnections > 0) {
      if (verbose) {
        System.out.println("CacheConnection monitor going to sleep");
      }
      try {
        // 1000 milliseconds/second x 60 seconds/minute x 5 minutes 
        monitor.sleep(1000*60*5);
      }
      catch (InterruptedException ignore) {
        if (verbose) {
          System.out.println(
           "CacheConnection monitor's sleep was interrupted");
        }
      }
    }
  }

  public void finalize(  ) throws Throwable {
    CachedConnection cached = null; 
    for(int i=0;i<numberConnections;i++) {
      cached = (CachedConnection)cachedConnections.get(i);
      if (cached.getConnection(  ) != null) {
        if (verbose) {
          System.out.println(
           "Closing connection on Vector entry " + 
           Integer.toString(i));
        }
        try { 
          cached.getConnection().close(  ); 
        } 
        catch(SQLException ignore) { 
          System.err.println("Can't close connection!!!"); 
        }
      }
    }
    numberConnections = 0;
  }

  public static void setVerbose(boolean v) {
    verbose = v;
  }
}
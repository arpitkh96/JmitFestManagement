package database;

import java.sql.*;

public class CachedConnection {
  private boolean    inUse;
  private Connection conn;
  private long       lastUsed;
  private String     baseName;

  public CachedConnection(  ) { 
    conn     = null;
    inUse    = false;
    lastUsed = System.currentTimeMillis(  );
    baseName = "Database";
  }

  public CachedConnection(Connection conn, boolean inUse) { 
    this.conn     = conn;
    this.inUse    = inUse;
    this.lastUsed = System.currentTimeMillis(  );
    this.baseName = "Database";
  }

  public CachedConnection(Connection conn, boolean inUse, String baseName) { 
    this.conn     = conn;
    this.inUse    = inUse;
    this.lastUsed = System.currentTimeMillis(  );
    this.baseName = baseName;
  }

  public Connection getConnection() { 
    return conn; 
  }

  public void setConnection(Connection conn) { 
    this.conn = conn; 
  }

  public boolean getInUse() {
    return inUse;
  }

  

  public void setInUse(boolean inUse) {
    if (!inUse)
      lastUsed = System.currentTimeMillis(  );
    this.inUse = inUse;
  }

  public String getBaseName() {
    return baseName;
  }

  public void setBaseName(String baseName) {
    this.baseName = baseName;
  }

  public long getLastUsed() {
    return lastUsed;
  }

public boolean isInUse() {
	return inUse;
}

}
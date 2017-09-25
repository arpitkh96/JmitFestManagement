package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Logger {
	static final String path = "/usr/share/tomcat7/logs/mylog.txt";
	
	public static void log(Exception e) {

		StringWriter sw = new StringWriter();
    	PrintWriter pw = new PrintWriter(sw);
    	e.printStackTrace(pw);
    	Logger.log(sw.toString());
    	e.printStackTrace();
    
	}
	public static void log(final String a) {
		System.out.println(a);
		new Thread(new Runnable(){
	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if(new File(path).exists())new File(path).createNewFile();
					StringBuilder b = new StringBuilder();
					FileReader inpt = new FileReader(path);
					BufferedReader reader = new BufferedReader(inpt);
					String line = reader.readLine();
					while (line != null) {
						b.append(line+"\n");
						line = reader.readLine();
					}
					b.append(a);
					FileWriter output = new FileWriter(path);
					BufferedWriter writer = new BufferedWriter(output);
					writer.write(b.toString());
					writer.close();
					output.close();
				} catch (FileNotFoundException e) {
					
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}			
			}}).run();
		}
}

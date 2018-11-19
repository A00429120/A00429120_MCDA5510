package mysql.connections.com;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connections {
	 private static Connections obj;
	 
	 public static Connections getCon() {
		 obj = new Connections();
		 return obj;
	 }
	// Initializes the connection to the database and return the Connection option
		public Connection setupConnection() throws Exception {

			Connection connect = null;
			try {
				// This will load the MySQL driver, each DB has its own driver
				// Class.forName("com.mysql.jdbc.Driver");
				Class.forName("com.mysql.cj.jdbc.Driver");
				// Setup the connection with the DB

				connect = DriverManager.getConnection("jdbc:mysql://dev.cs.smu.ca:3306/v_govindan?"
						+ "user=v_govindan&password=A00429120" + "&useSSL=false"
						+ "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");

			} catch (Exception e) {
				throw e;
			} finally {

			}
			return connect;
		}
		
	
}

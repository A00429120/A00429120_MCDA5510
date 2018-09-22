import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.relique.jdbc.csv.CsvDriver;

/* documentation Again : This is the first type where i use the DB method to read the CSV  */
public class DirWalker2 {
	private static int fineRecords = 0;
	private static int incompleteRecords = 0;
	SimpleLogging log = new SimpleLogging();
	static String pathSeperator = System.getProperty("os.name").startsWith("Windows") ? "\\" : "/";
	
	/*Input : (path of the file to be parsed,
	 *  output file to which its results are to written, 
	 *  date from the dir structure)*/
	public void fileParser(String filePath, String outputFile, String date) {
		Reader fr = null;
		PrintWriter pw = null;
		if(! filePath.endsWith(".csv")) {
			log.logIt(Level.INFO,"Not a .csv file hence skipping it !!");
			return;
		}
		
		try {
			// Load the driver.
			Class.forName("org.relique.jdbc.csv.CsvDriver");
			
			pw = new PrintWriter(new FileOutputStream(new File(outputFile), true));
			
			int err = 0;
			String CSVDir = filePath.substring(0,filePath.lastIndexOf("\\"));
			String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
			fileName = fileName.substring(0, fileName.indexOf("."));

			Connection conn = DriverManager.getConnection("jdbc:relique:csv:" + CSVDir);

			// Create a Statement object to execute the query with.
			// A Statement is not thread-safe.
			Statement stmt = conn.createStatement();

			// Select the ID and NAME columns from sample.csv
			ResultSet results;

			results = stmt.executeQuery("SELECT * FROM "+fileName);

			
			while(results.next()) {
				String firstName = results.getString("First Name");
			    String lastName = results.getString("Last Name");
			    /*Expensive operations that increase the process time 
			     * Checking to see if there is any comma in between my data which was previously enclosed in ""
			     */
			    Integer streetNo = results.getInt("Street Number");
			    String streetName = results.getString("Street").indexOf(",") >= 0 ? "\""+results.getString("Street")+"\"" : results.getString("Street");
			    String city = results.getString("City").indexOf(",") >= 0 ? "\""+results.getString("City")+"\"" : results.getString("City");
			    String province = results.getString("Province").indexOf(",") >= 0 ? "\""+results.getString("Province")+"\"" : results.getString("Province");
			    String pin = results.getString("Postal Code");
			    String country = results.getString("Country");
			    Integer phNo = results.getInt("Phone Number");
			    String email = results.getString("email Address");
			    
			    //this region to be commented out in case this is just to check if the csv is complete or not  
			    if(firstName.isEmpty() || lastName.isEmpty() || streetNo == 0 ||
			    		streetName.isEmpty() || city.isEmpty() || province.isEmpty() || pin.isEmpty() ||
			    		country.isEmpty() || phNo == 0 || email.isEmpty()) {
			    	log.logIt(Level.INFO,"Incomplete Record Hence Skipping it 1");
			    	incompleteRecords++;
			    	continue;
			    } 
			    
			    pw.write(date+","+firstName+","+lastName+","+streetNo+","+streetName+","+city+
			    		","+province+","+pin+","+country+","+phNo+","+email+"\n"); 
			    fineRecords++; 
			    
			}
			
			// Clean up
			conn.close();
		} catch (SQLException e) {
			log.logIt(Level.WARNING,e.getMessage()+ filePath);
			incompleteRecords++;
		} catch (IOException e) {
			log.logIt(Level.WARNING,e.getMessage());			
		} catch (Exception e) {
			log.logIt(Level.WARNING,e.getMessage());
			e.printStackTrace();
		} 
		
		finally {
			try {
			if(fr != null) fr.close();
			if(pw != null) pw.close();
			} catch (Exception e) {
				log.logIt(Level.SEVERE,e.getMessage());
			}
		}
	}
	
	public void walk(String path, String outputFile) {
		File root = new File(path);
		File[] files = root.listFiles();
		
		if(files.length == 0) return;
		
		for(File file : files) {
			String fileName = file.getAbsolutePath();
			if(file.isDirectory()) walk(fileName, outputFile);
			else {
				//fetching the dates using the folder structure
				 String folder = fileName.substring(0,fileName.lastIndexOf("\\"));
				String day = folder.substring(folder.lastIndexOf("\\")+1);
				//System.out.println(day);
				String month = folder.substring(0,folder.lastIndexOf("\\"));
				month = month.substring(month.lastIndexOf("\\")+1);
				String year = folder.substring(0,folder.lastIndexOf("\\"+month+"\\"));
				year = year.substring(year.lastIndexOf("\\")+1);
				month = month.length() == 2 ? month:"0"+month ;
				day = day.length() == 2 ? day:"0"+day ;
				String date = year+"\\"+month+"\\"+day; 
				fileParser(file.getAbsolutePath(), outputFile, date); //to be replace with the parse code
			}
		}
	}
	
	public int getValidRecords() {
		return fineRecords;
	}

	public int getInvalidRecords() {
		return incompleteRecords;
	}
	
	
	public static void main(String[] args) {
		
		String basePath = System.getProperty("user.dir");
		final long startTime = System.currentTimeMillis();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
		LocalDateTime time = LocalDateTime.now();
		String now = dtf.format(time);
		int validRecords, invalidRecords;
		String outputDir = basePath+"/Output";
		File output = new File(outputDir);
		File outputFile = new File(outputDir+"/output"+now+".csv");
		DirWalker2 obj = new DirWalker2();
		
		if (! output.exists()) {
			output.mkdirs(); 
		}
		if (! outputFile.exists()) {
			try {
				outputFile.createNewFile();
				PrintWriter pwr = new PrintWriter(outputFile.getAbsolutePath());
				pwr.write("Date,First Name,Last Name,Street Number,Street Name,City,Province,Postal Code,Country,Phone Number,E-Mail\n");
				pwr.close();
			} catch (FileNotFoundException e) {
				obj.log.logIt(Level.SEVERE,e.getMessage());
			} catch (IOException e) {
				obj.log.logIt(Level.SEVERE,e.getMessage());
			} 
		}
		
		
		
		obj.walk(basePath+"/Sample Data",outputFile.getAbsolutePath());
		final long endTime = System.currentTimeMillis();
		Long exeTime = endTime-startTime;
		validRecords = obj.getValidRecords();
		invalidRecords = obj.getInvalidRecords();
		obj.log.logIt(Level.INFO,"Total Execution time in milliseconds is : "+exeTime.toString()+","+validRecords+","+invalidRecords);
		
	}

}

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;


/*  Yay documentation : This is the first type where i use the parser to read the CSV  */
public class DirWalker {
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
		int count = 0;
		if(! filePath.endsWith(".csv")) {
			log.logIt(Level.INFO,"Not a .csv file hence skipping it !!");
			return;
		}

		try {
			fr = new FileReader(filePath);
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(fr);
			pw = new PrintWriter(new FileOutputStream(new File(outputFile), true));
			for (CSVRecord record : records) {
				count++;
				if (count == 1) continue;
				try {
					String firstName = record.get(0);
					String lastName = record.get(1);
					String streetNo = record.get(2) ;
					/*Expensive operations that increase the process time 
					 * Checking to see if there is any comma in between my data which was previously enclosed in ""
					 */
					String streetName = record.get(3).indexOf(",") >= 0 ? "\""+record.get(3)+"\"" : record.get(3);
					String city = record.get(4).indexOf(",") >= 0 ? "\""+record.get(4)+"\"" : record.get(4);
					String province = record.get(5).indexOf(",") >= 0 ? "\""+record.get(5)+"\"" : record.get(5);
					String pin = record.get(6);
					String country = record.get(7);
					String phNo = record.get(8);
					String email = record.get(9);

					//TODO : see if this required or this is redundant, checking for the pattern
					if(Pattern.matches("[a-zA-Z]+", streetNo) == true || Pattern.matches("[a-zA-Z]+", phNo) == true) {
						log.logIt(Level.INFO,"Record with improper fields");
						incompleteRecords++;
						continue;
					}


					//this region to be commented out in case this is just to check if the csv is complete or not 
					if(firstName.isEmpty() || lastName.isEmpty() || streetNo.isEmpty() ||
							streetName.isEmpty() || city.isEmpty() || province.isEmpty() || pin.isEmpty() ||
							country.isEmpty() || phNo.isEmpty() || email.isEmpty()) {
						//log.logIt(Level.INFO,"Incomplete Record Hence Skipping it 1");
						incompleteRecords++;
						continue;
					} 


					pw.write(date+","+firstName+","+lastName+","+streetNo+","+streetName+","+city+
							","+province+","+pin+","+country+","+phNo+","+email+"\n");
					fineRecords++;
				} catch (IndexOutOfBoundsException e) {
					incompleteRecords++;
					//log.logIt(Level.WARNING,"The record dosen't have all the data hence skipping it"+e.getMessage());
				}

			}

		} catch (IOException e) {
			log.logIt(Level.WARNING,e.getMessage());			
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
				String folder = fileName.substring(0,fileName.lastIndexOf(pathSeperator));
				String day = folder.substring(folder.lastIndexOf(pathSeperator)+1);
				//System.out.println(day);
				String month = folder.substring(0,folder.lastIndexOf(pathSeperator));
				month = month.substring(month.lastIndexOf(pathSeperator)+1);
				String year = folder.substring(0,folder.lastIndexOf(pathSeperator+month+pathSeperator));
				year = year.substring(year.lastIndexOf(pathSeperator)+1);
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
		DirWalker obj = new DirWalker();

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
				obj.log.logIt(Level.WARNING,e.getMessage());
			} catch (IOException e) {
				obj.log.logIt(Level.WARNING,e.getMessage());
			} 
		}



		obj.walk(basePath+"/Sample Data",outputFile.getAbsolutePath());
		final long endTime = System.currentTimeMillis();
		Long exeTime = endTime-startTime;
		validRecords = obj.getValidRecords();
		invalidRecords = obj.getInvalidRecords();
		obj.log.logIt(Level.INFO,"Total Execution time in milliseconds is : "+exeTime.toString()+",Valid Records :"+validRecords+", Invalid Records:"+invalidRecords);

	}

}

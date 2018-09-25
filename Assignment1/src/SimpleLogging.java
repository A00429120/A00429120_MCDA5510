import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SimpleLogging {

	static final String basePath = System.getProperty("user.dir");
	static final String outputDir = basePath+"/../log";
	FileHandler fh; 

	
	public void logIt(Level level,String logMessage) {

		final Logger logging = Logger.getLogger(SimpleLogging.class.getName()); 

		try {  

			fh = new FileHandler(outputDir+"/java.log",true);  
			logging.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  
			logging.log(level,logMessage); 
			fh.close();
		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  



	}


}



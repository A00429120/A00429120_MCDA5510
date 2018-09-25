import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleLogging {

	private static final Logger logging = Logger.getLogger(SimpleLogging.class.getName());
	static final String basePath = System.getProperty("user.dir");
	static final String outputDir = basePath+"/../Output";
	File logFile = new File(outputDir+"/assignment1.log");
	
	
	public void logIt(Level level,String logMessage) {
		
		//This manual log creation is increasing the exec time by 4 *FACEPALM* 
		//must be a better way to handle this
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss ");
		LocalDateTime time = LocalDateTime.now();
		String now = dtf.format(time);
		
		
		try {
			if (! logFile.exists()) {
				logFile.createNewFile();
			}
			PrintWriter pwr = new PrintWriter(new FileOutputStream(logFile, true));
			pwr.write(now +" : "+ logMessage +"\n");
			pwr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		//This is logging using simple logger just because i can :P
		logging.log(level, logMessage);

	}
	
}

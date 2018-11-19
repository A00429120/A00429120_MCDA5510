package mysql.com.web.service;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SimpleLogging {

	static final String basePath = System.getProperty("user.dir");
	static final String outputDir = basePath + "/../log";
	static final File opDir = new File(outputDir);
	FileHandler fh;

	public void logIt(Level level, String logMessage) {

		final Logger logging = Logger.getLogger(SimpleLogging.class.getName());

		try {
			if (!opDir.exists()) {
				opDir.mkdirs();
			}

			fh = new FileHandler(outputDir + "/transaction.log", true);
			logging.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			logging.log(level, logMessage);
			fh.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

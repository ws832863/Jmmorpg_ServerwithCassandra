package game.utils;
//
//import game.database.DatabaseCon;
//import game.database.DbConect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * class responsible for loading the server configuration file 
 * 
 * @author antonio junior
 * 
 */
public class Configures {
	/**
	 * configure the directory contains the properties file
	 */
	public static String dirconfig = "conf/";
    private static final Logger logger =
        Logger.getLogger(Configures.class.getName());
	

	/**
	 * read the properties file
	 * 
	 * @param prop
	 * @return
	 */
	public static Properties properties(String prop) {
		File file = new File(dirconfig + prop + ".properties");
		Properties return_string = null;

		if (file.exists()) {
			Properties props = new Properties();

			try {
				props.load(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return_string = props;
		}

		return return_string;
	}

	/**
	 * file loaded
	 */
	public Configures() {
//		try {
//			logger.info("Initializing Configurations... ");
//			logger.info("Initializing DataBase... ");
//			new DatabaseCon();
//			DbConect.getInstance();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
}

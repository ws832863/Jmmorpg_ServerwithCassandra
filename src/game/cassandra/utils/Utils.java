package game.cassandra.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

public class Utils {

	public static String CurrentDateString = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss").format(new java.util.Date()).toString();

	private final static Logger logger = Logger
			.getLogger(Utils.class.getName());

	// transform a String to java.sql.date. in order to pass the old database
	public static java.sql.Date dateFromString(String dateString) {
		//System.out.println(dateString + "length " +dateString.length());
		logger.debug(dateString + "length " +dateString.length());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if(dateString.length()==10){
			sdf=new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			
			sdf.parse(dateString);
		} catch (ParseException e) {
			logger.warn("prase string to date error " + e.toString());
		}
		return new java.sql.Date(sdf.YEAR_FIELD, sdf.MONTH_FIELD,
				sdf.DATE_FIELD);
	}

	// transform a java.sql.date to String, in order to pass the old database
	public static String stringFromDate(java.sql.Date Stringdate) {
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Stringdate);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

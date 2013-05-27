package game.cassandra.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.apache.log4j.Logger;

public class Utils {

	//get the String form Date now
	public static String CurrentDateString = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss").format(new java.util.Date()).toString();

	private static final String MESSAGE_CHARSET = "UTF-8";

	private final static Logger logger = Logger
			.getLogger(Utils.class.getName());

	// transform a String to java.sql.date. in order to pass the old database
	public static java.sql.Date dateFromString(String dateString) {
		// System.out.println(dateString + "length " +dateString.length());
		logger.debug(dateString + "length " + dateString.length());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (dateString.length() == 10) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
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

	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static int getRandomInt(int num) {
		SecureRandom random = new SecureRandom();
		return random.nextInt(num) + 1;
	}

	/**
	 * get a ramdom email address
	 * 
	 * @return
	 */
	public static String getRandomEmail() {
		StringBuilder email = new StringBuilder();
		email.append(Utils.getRandomString(Utils.getRandomInt(5) + 4));
		email.append("@");
		email.append("sample.email");
		return email.toString();

	}

	public static String getRandomHeroClass() {
		int i = Utils.getRandomInt(4);
		String hc;
		switch (i) {
		case 1:
			hc = "Assisin";
			break;
		case 2:
			hc = "Wizzard";
			break;
		case 3:
			hc = "Archer";
			break;
		case 4:
			hc = "Warrior";
			break;
		default:
			hc = "DefaultClass";
			break;
		}
		return hc;
	}

	public static String getRandomRace() {
		int i = Utils.getRandomInt(2);

		String race;
		switch (i) {
		case 1:
			race = "Humano";
			break;
		case 2:
			race = "Elf";
			break;
		default:
			race = "defaultrace";
			break;

		}
		return race + " " + i;
	}

	/**
	 * get a random birthday string from 1950 to 2000 for simple, days not
	 * bigger than 28
	 * 
	 * @return
	 */
	public static String getRandomBirth() {
		int year;
		int month;
		int day;

		year = Utils.getRandomInt(50) + 1950;
		month = Utils.getRandomInt(12);
		day = Utils.getRandomInt(28);

		StringBuilder sb = new StringBuilder();
		sb.append(year);
		sb.append("-");
		if (month < 10)
			sb.append(0);

		sb.append(month);

		sb.append("-");
		if (day < 10)
			sb.append(0);
		sb.append(day);
		return sb.toString();

	}

	/**
	 * Encodes a {@code String} into a {@link ByteBuffer}.
	 * 
	 * @param s
	 *            the string to encode
	 * @return the {@code ByteBuffer} which encodes the given string
	 */
	public static ByteBuffer encodeStringToByteBuffer(String s) {
		try {
			return ByteBuffer.wrap(s.getBytes(MESSAGE_CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET
					+ " not found", e);
		}
	}

	/**
	 * Decodes a {@link ByteBuffer} into a {@code String}.
	 * 
	 * @param buf
	 *            the {@code ByteBuffer} to decode
	 * @return the decoded string
	 */
	public static String decodeByteBufferToString(ByteBuffer buf) {
		try {
			byte[] bytes = new byte[buf.remaining()];
			buf.get(bytes);
			return new String(bytes, MESSAGE_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new Error("Required character set " + MESSAGE_CHARSET
					+ " not found", e);
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {
			String ss = Utils.getRandomHeroClass();
			System.out.println(ss);
		}
	}

}

package game.utils;

import java.util.Calendar;

/**
 * Classe responsavel para manipular o horario e data.
 * 
 * @author antonio junior
 * 
 */
public class DateManager {
	static Calendar date = Calendar.getInstance();

	public static String getYear() {
		String year = String.valueOf(date.get(Calendar.YEAR));

		return year;
	}
	
	public static long CalendarGetTime() {
		return System.currentTimeMillis();
	}

	public static String getDate() {
		String date = DateManager.getDay() + "/" + DateManager.getMonth() + "/"
				+ DateManager.getYear() + " " + DateManager.getTime() + ":"
				+ DateManager.getMinute() + ":" + DateManager.getSecond();
		return date;
	}

	public static String getDateFormat() {

		String date = DateManager.getYear() + "-" + DateManager.getMonth()
				+ "-" + DateManager.getDay() + " " + DateManager.getTime()
				+ "-" + DateManager.getMinute() + "-" + DateManager.getSecond();
		return date;
	}

	public static String getDay() {
		String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1) {
			day = '0' + day;
		}
		return day;
	}

	public static String getTime() {
		String time = String.valueOf(date.get(Calendar.HOUR_OF_DAY));
		if (time.length() == 1) {
			time = '0' + time;
		}
		return time;
	}

	public static String getMonth() {
		String month = String.valueOf(date.get(Calendar.MONTH));
		if (month.length() == 1) {
			month = '0' + month;
		}
		return month;
	}

	public static String getMinute() {
		String minute = String.valueOf(date.get(Calendar.MINUTE));
		if (minute.length() == 1) {
			minute = '0' + minute;
		}
		return minute;
	}

	public static String getSecond() {
		String second = String.valueOf(date.get(Calendar.SECOND));
		if (second.length() == 1) {
			second = '0' + second;
		}
		return second;
	}

	public void getThreadDate(Calendar date) {
		DateManager.date = date;
	}
}

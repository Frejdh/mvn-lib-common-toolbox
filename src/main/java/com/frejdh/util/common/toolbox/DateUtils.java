package com.frejdh.util.common.toolbox;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class DateUtils {

	/**
	 * Returns a date based on a string
	 * @param date String to format
	 * @param dateFormat Format to save it in
	 * @return A new instance of a {@link Calendar} date (or null if failed)
	 */
	public static Calendar stringToCalendar(String date, String dateFormat) {
		try {
			date = date.replace(".", "").replace("\"", "").trim();
			Calendar retval = Calendar.getInstance();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
			retval.setTime(simpleDateFormat.parse(date));
			return retval;
		} catch (ParseException e) { return null; }
	}

	/**
	 * Same as {@link #stringToCalendar(String, String)} with the second argument as "yyyy-MM-dd"
	 * @param date String to convert
	 * @return A {@link Calendar} instance
	 */
	public static Calendar stringToCalendar(String date) {
		return stringToCalendar(date, "yyyy-MM-dd");
	}

    /**
	 * Get the elapsed time between then and now in a string format.
	 * The timezone is automatically chosen based of the parameter.
	 * @param then Time to compare with.
	 * @return A string, etc. '5 years ago'
	 */
	public static String getRelativeTimeSpanString(Calendar then) {
		Calendar now = Calendar.getInstance(then.getTimeZone());

		int years = now.get(Calendar.YEAR) - then.get(Calendar.YEAR);
		int months = now.get(Calendar.MONTH) - then.get(Calendar.MONTH);
		int weeks = now.get(Calendar.WEEK_OF_YEAR) - then.get(Calendar.WEEK_OF_YEAR);
		int days = now.get(Calendar.DAY_OF_YEAR) - then.get(Calendar.DAY_OF_YEAR);
		int hours = now.get(Calendar.HOUR_OF_DAY) - then.get(Calendar.HOUR_OF_DAY);
		int minutes = now.get(Calendar.MINUTE) - then.get(Calendar.MINUTE);
		int seconds = now.get(Calendar.SECOND) - then.get(Calendar.SECOND);

        if (years >= 1)
			return years + " years ago";
		if (months >= 1)
			return months + " months ago";
		if (weeks >= 1)
			return weeks + " weeks ago";
		if (days >= 1)
			return days + " days ago";
		if (hours >= 1)
			return hours + " hours ago";
		if (minutes >= 1)
			return minutes + " minutes ago";
		if (seconds >= 1)
			return seconds + " seconds ago";
		return "Just now";
	}

	/**
	 * Get the elapsed time between then and now in a string format
	 * @param then Time to compare with (milliseconds)
	 * @param timeZone Timezone to use
	 * @return A string, etc. '5 years ago'
	 */
	public static String getRelativeTimeSpanString(long then, TimeZone timeZone) {
		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.setTimeInMillis(then);
		return getRelativeTimeSpanString(calendar);
	}

	/**
	 * Get the elapsed time between then and now in a string format.
	 * This method uses the UTC timezone
	 * @param thenUTC Time to compare with (milliseconds in UTC)
	 * @return A string, etc. '5 years ago'
	 */
	public static String getRelativeTimeSpanString(long thenUTC) {
		return getRelativeTimeSpanString(thenUTC, TimeZone.getTimeZone("UTC"));
	}

	/**
	 * Converts UNIX epoch to milliseconds
	 * @param epoch Epoch value
	 * @return Time in milliseconds
	 */
	public static long epochToToMilliseconds(long epoch) {
		return epoch * 1000;
	}

	/**
	 *  A deserializer for the Gregorian calender. Meant to be used along with Google's GsonBuilder class.
	 *  Usage example:<i><br>
	 *  Gson gson = new GsonBuilder()<br>
 	 *	    .registerTypeAdapter({@link Calendar Calendar.class}, new DateUtils.GregorianCalendarDeserializer())<br>
 	 *	    .create();
	 *	</i>
	 */
	@SuppressWarnings({"InnerClassMayBeStatic", "MagicConstant"})
	public static class GregorianCalendarDeserializer implements JsonDeserializer<Calendar> {
		@Override
		public Calendar deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
		{
			String strDate = je.getAsString().replaceAll("\"", "");
			try {
				int year = Integer.parseInt(strDate.split("-")[0]);
				int month = Integer.parseInt(strDate.split("-")[1]);
				int day = Integer.parseInt(strDate.split("-")[2]);
				return new GregorianCalendar(year, month - 1, day);
			} catch (Exception e) {
				throw new JsonParseException("Could not parse Gregorian date");
			}
		}

	}
}

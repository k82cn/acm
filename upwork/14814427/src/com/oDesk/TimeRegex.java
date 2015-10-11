package com.oDesk;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeRegex {

	private Pattern pattern;

	private int year;
	private int month;
	private int date;
	private int hour;
	private int minute;
	private int second;

	public TimeRegex(String regex, int year, int month, int date) {
		this(regex, year, month, date, -1, -1, -1);
	}
	
	public TimeRegex(String regex, int year, int month, int date, int hour, int minute) {
		this(regex, year, month, date, hour, minute, -1);
	}

	public TimeRegex(String regex, int year, int month, int date, int hour,
			int minute, int second) {
		pattern = Pattern.compile(regex);
		this.year = year;
		this.month = month;
		this.date = date;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public String getRegex()
	{
		return pattern.pattern();
	}
	
	public Date getDate(String dateStr) {
		Matcher m = pattern.matcher(dateStr);
		if (m.find()) {
			Calendar c = Calendar.getInstance();
			try {
				if (this.year > 0)
					c.set(Calendar.YEAR, Integer.parseInt(m.group(this.year)));
				if (this.month > 0) {
					String monthStr = m.group(this.month);
					int month = -1;
					// if it is word, convert it into integer
					for (int i = 0; i < Dictionary.monthWords.length; i++) {
						if (monthStr.equalsIgnoreCase(Dictionary.monthWords[i])) {
							month = i;
						}
					}
					// otherwise, it should be a number
					if (month < 0)
						month = Integer.parseInt(monthStr);
					c.set(Calendar.MONTH, month);
				}
				if (this.date > 0)
					c.set(Calendar.DATE, Integer.parseInt(m.group(this.date)));
				if (this.hour > 0)
					c.set(Calendar.HOUR, Integer.parseInt(m.group(this.hour)));
				if (this.minute > 0)
					c.set(Calendar.MINUTE,
							Integer.parseInt(m.group(this.minute)));
				if (this.second > 0)
					c.set(Calendar.SECOND,
							Integer.parseInt(m.group(this.second)));
				return c.getTime();
			} catch (Exception e) {
				e.printStackTrace();
				// if any exception to convert time to integer, return null;
			}
		}

		return null;
	}

}

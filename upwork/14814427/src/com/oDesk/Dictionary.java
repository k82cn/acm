package com.oDesk;

import java.util.ArrayList;

public class Dictionary {

	public static final String[] numberWords = { "[Zz]ero", "[Oo]ne", "[Tt]wo",
			"[Tt]hree", "[Ff]our", "[Ff]ive", "[Ss]ix", "[Ss]even", "[Ee]ight",
			"[Nn]ine", "[Tt]en", "[Ee]leven", "[Tt]welve", "[Tt]hirteen",
			"[Ff]ourteen", "[Ff]ifteen", "[Ss]ixteen", "[Ss]eventeen",
			"[Ee]ighteen", "[Nn]ineteen", "[Tt]wenty", "[Tt]wenty [Oo]ne",
			"[Tt]wenty [Tt]wo", "[Tt]wenty [Tt]hree", "[Tt]wenty [Ff]our" };
	public static final String[] monthWords = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sept", "Oct", "Nov", "Dec" };
	public static final String[] dayNames = { "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday", "Sunday" };

	public static final String[] words = { "published", "submitted", "updated",
			"authored", "posted", "reserved", "related", "modified", "powered",
			"designed", "edited" };

	// init the list of years
	public static ArrayList<String> years = new ArrayList<String>();

	static {
		for (int i = 1900; i <= 2100; i++) {
			years.add(Integer.toString(i, 10));
		}
	}

	// initialize the regex list for date
	public static ArrayList<TimeRegex> regexList = new ArrayList<TimeRegex>();

	static {
		TimeRegex tr = null;

		tr = new TimeRegex("([\\d]+) - ([\\d]+) - ([\\d]+) ([\\d]+) : ([\\d]+)", 1, 2, 3,
				4, 5);
		regexList.add(tr);
	}

}

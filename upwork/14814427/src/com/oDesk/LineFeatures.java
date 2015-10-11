package com.oDesk;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineFeatures {

	// Constructor of LineFeatures, built from line
	public LineFeatures(String line) {

		setLine(line);
		setDate();
		// Those info will be updated by LineHandler
//		setFirstDate();
//		setLastDate();
//		setEarlistDate();
//		setLatestDate();
		setTense();

		// setRegexRule();
		setLocation();
		setTags();
		setDepth();
		setTop();
		setLow();
		setLetters();
		setNumbers();

		setFreqNeighbor();
		setNeighbor();

		setBeforeTitle();

		setAfterTitle();

		setMonthWords();
		setYear();
		setNumberWords();
		setDayName();

	}

	// Convert feature into String.
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(line).append("\t");
		sb.append(this.isFirstDate).append("\t");
		sb.append(this.isLastDate).append("\t");
		sb.append(this.isEarliestDate).append("\t");
		sb.append(this.isLatestDate).append("\t");
		sb.append(this.tense).append("\t");
		sb.append(this.regexRule).append("\t");
		sb.append(this.location).append("\t");
		sb.append(this.tags).append("\t");
		sb.append(this.depth).append("\t");
		sb.append(this.top).append("\t");
		sb.append(this.low).append("\t");
		sb.append(this.letters).append("\t");
		sb.append(this.numbers).append("\t");
		sb.append(this.lineLen).append("\t");
		sb.append(this.freqNeighbor).append("\t");
		sb.append(this.neighbor).append("\t");
		sb.append(this.beforeTitle).append("\t");
		sb.append(this.afterTitle).append("\t");
		sb.append(this.includeMonthWords).append("\t");
		sb.append(this.includeYear).append("\t");
		sb.append(this.includeNumberWords).append("\t");
		sb.append(this.includeDayName);

		return sb.toString();
	}

	private Date date;

	private String line;
	private String[] lineWords;

	public void setLine(String line) {
		this.line = line;
		this.lineLen = line.length();
		this.lineWords = this.line.split("[\\s]+");
	}

	// matching date regular expression? [0,1] ( I give you a set of matching
	// RE)
	private boolean isDate;

	public boolean isDate() {
		return this.isDate;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate() {
		for (TimeRegex tr : Dictionary.regexList) {
			date = tr.getDate(this.line);
			if (date != null) {
				this.setRegexRule(tr.getRegex());
				isDate = true;
			}
		}
	}

	// First date in the document [0,1]
	private int isFirstDate;

	public void setFirstDate() {
		this.isFirstDate = 1;
	}

	// last date in the document [0,1]
	private int isLastDate;

	public void setLastDate() {
		this.isLastDate = 1;
	}

	// Earliest date in page [0,1] --> we should normalize the date only for
	// dates that are absolute (example: Nov, 12 2014 --> 12-11-2014)
	private int isEarliestDate;

	public void setEarlistDate() {
		this.isEarliestDate = 1;
	}

	// latest date in page [0,1] --> we should normalize the date only for dates
	// that are absolute
	private int isLatestDate;

	public void setLatestDate() {
		this.isLatestDate = 1;
	}

	// Tense of sentence containing date
	// (http://stackoverflow.com/questions/19966345/identifying-verb-tenses-in-python)
	private String tense;

	public void setTense() {
		// TODO
	}

	// Regex rule matching date
	// (I have a set of regular expressions that I send you)
	private String regexRule;

	public void setRegexRule(String regexRule) {
		this.regexRule = regexRule;
	}

	// Location of date (header/body) (you can check it based on the html files
	// in source_files_all folder)
	private String location;

	public void setLocation() {
		// TODO
	}

	// tag surrounding date (you can have a set of tags and then use 0,1 values
	private int tags;

	public void setTags() {
		// TODO
	}

	// depth in HTML tree of date
	private int depth;

	public void setDepth() {
		// TODO
	}

	// if it is in the top 20% of the page
	private int top;

	public void setTop() {
		// TODO
	}

	// if it is in the lowest 20% of the page
	private int low;

	public void setLow() {
		// TODO
	}

	// percentage of letters
	private double letters;

	public void setLetters() {
		int cnt = 0;
		for (int i = 0; i < this.lineLen; i++) {
			if (Character.isAlphabetic(this.line.charAt(i))) {
				cnt++;
			}
		}
		this.numbers = ((double) cnt) / (double) this.lineLen;
	}

	// percentage of numbers
	private double numbers;

	public void setNumbers() {
		int cnt = 0;
		for (int i = 0; i < this.lineLen; i++) {
			if (Character.isDigit(this.line.charAt(i))) {
				cnt++;
			}
		}
		this.numbers = ((double) cnt) / (double) this.lineLen;
	}

	// length of the string
	private int lineLen;
	// does it have a neighbor word (window of +3 and -3) any of the words you
	// can find in the first column of freq_ngrams_neighbor [0,1]

	private int freqNeighbor;

	public void setFreqNeighbor() {
		// TODO
	}

	// does it have a neighbor word including any of [published, submitted,
	// updated, authored, posted, reserved, related, modified, powered,
	// designed, edited]

	private int neighbor;

	public void setNeighbor() {
		// TODO
	}

	// Position of Unit before the title of the page

	private int beforeTitle;

	public void setBeforeTitle() {
		// TODO
	}

	// Position of Unit aftre the title of the page

	private int afterTitle;

	public void setAfterTitle() {
		// TODO
	}

	// includes monthWords?
	private int includeMonthWords;

	public void setMonthWords() {
		for (String n : Dictionary.monthWords) {
			for (String w : lineWords) {
				if (n.equalsIgnoreCase(w)) {
					this.includeMonthWords = 1;
					return;
				}
			}
		}
	}

	// includes year (1900 to 2100)?
	private int includeYear;

	public void setYear() {
		for (String y : Dictionary.years) {
			for (String w : lineWords) {
				if (y.equalsIgnoreCase(w)) {
					this.includeYear = 1;
					return;
				}
			}
		}
	}

	// includes numberWords?
	private int includeNumberWords;

	public void setNumberWords() {
		for (String n : Dictionary.numberWords) {
			Pattern p = Pattern.compile(n);
			for (String w : lineWords) {
				Matcher m = p.matcher(w);
				if (m.matches()) {
					this.includeNumberWords = 1;
					return;
				}
			}
		}
	}

	// include day name?
	private int includeDayName;

	public void setDayName() {
		for (String n : Dictionary.dayNames) {
			for (String w : lineWords) {
				if (w.equalsIgnoreCase(n)) {
					this.includeDayName = 1;
					return;
				}
			}
		}
	}

}

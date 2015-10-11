package com.oDesk.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.oDesk.TimeRegex;

public class LineFeaturesTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSetDate() {

		Pattern p = Pattern
				.compile("([\\d]+) - ([\\d]+) - ([\\d]+) ([\\d]+) : ([\\d]+)");
		Matcher m = p.matcher("2014 - 11 - 11 10 : 47");

		if (m.find()) {
			for (int i = 1; i <= m.groupCount(); i++) {
				System.out.println(m.group(i));
			}
		}

		TimeRegex rt = new TimeRegex(
				"([\\d]+) - ([\\d]+) - ([\\d]+) ([\\d]+) : ([\\d]+)", 1, 2, 3,
				4, 5);
		Date date = rt.getDate("2014 - 11 - 11 10 : 47");
		if (date != null)
			System.out.println(date.toString());
	}

	@After
	public void tearDown() throws Exception {
	}

}

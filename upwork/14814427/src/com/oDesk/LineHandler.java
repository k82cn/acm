package com.oDesk;

import java.util.Date;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LineHandler extends DefaultHandler {

	private boolean isInTimeX3;

	ArrayList<LineFeatures> featureList = new ArrayList<LineFeatures>();

	int firstDate = -1;
	int lastDate = -1;
	int earliestDate = -1;
	int latestDate = -1;

	StringBuilder msg;

	public LineHandler() {
		isInTimeX3 = false;
		msg = new StringBuilder();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("TIMEX3")) {
			isInTimeX3 = true;
			msg.setLength(0);
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equalsIgnoreCase("TIMEX3")) {
			isInTimeX3 = false;
			LineFeatures lf = new LineFeatures(msg.toString().trim());
			if (lf.isDate()) {
				this.setEarliestDate(lf);	
				this.setFirstDate(lf);
				this.setLastDate(lf);
				this.setLatestDate(lf);
			}
			this.featureList.add(lf);
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {
		if (isInTimeX3) {
			msg.append(ch, start, length);
		}
	}

	@Override
	public void endDocument() {
		// update date info
		if (this.earliestDate > 0)
			this.featureList.get(this.earliestDate).setEarlistDate();
		if (this.latestDate > 0)
			this.featureList.get(this.latestDate).setLatestDate();
		if (this.firstDate > 0)
			this.featureList.get(this.firstDate).setFirstDate();
		if (this.lastDate > 0)
			this.featureList.get(this.lastDate).setLastDate();

		// flush the features into files
		for (LineFeatures lf : this.featureList)
		{
			System.out.println("===========================================");
			System.out.println(lf.toString());;
		}
	}

	private void setEarliestDate(LineFeatures lf)
	{
		int i = this.featureList.size();
		if (this.earliestDate < 0)
		{
			this.earliestDate = i;
		}
		else
		{
			Date earliestDate = this.featureList.get(this.earliestDate).getDate();
			Date currentDate = lf.getDate();
			if (currentDate.before(earliestDate))
			{
				this.earliestDate = i;
			}
		}
	}
	

	private void setLatestDate(LineFeatures lf)
	{
		int i = this.featureList.size();
		if (this.latestDate < 0)
		{
			this.latestDate = i;
		}
		else
		{
			Date latestDate = this.featureList.get(this.latestDate).getDate();
			Date currentDate = lf.getDate();
			if (currentDate.after(latestDate))
			{
				this.latestDate = i;
			}
		}
	}
	
	private void setFirstDate(LineFeatures lf)
	{
		if (this.firstDate < 0)
		{
			this.firstDate = this.featureList.size();
		}
	}
	
	private void setLastDate(LineFeatures lf)
	{
		if (lf.isDate())
		{
			this.lastDate = this.featureList.size();
		}
	}
}

package com.oDesk;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class Main {

	public static void main(String[] args) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);

		factory.setXIncludeAware(false);

		try {
			factory.newSAXParser()
					.parse(new File(
							"/Users/dma/Downloads/0b82d0ed-755e-31e2-8c55-7af0e1e98fa1.txt.tml"),
							new LineHandler());
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}

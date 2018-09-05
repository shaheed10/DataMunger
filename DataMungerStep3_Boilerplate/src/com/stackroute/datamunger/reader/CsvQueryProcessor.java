package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;

public class CsvQueryProcessor extends QueryProcessingEngine {
	File filename = new File("data/ipl.csv");

	// Parameterized constructor to initialize filename
	public CsvQueryProcessor(String fileName) throws FileNotFoundException {
	
		FileInputStream fis = new FileInputStream(fileName);

	}

	/*
	 * Implementation of getHeader() method. We will have to extract the headers
	 * from the first line of the file.
	 * Note: Return type of the method will be Header
	 */

	@Override
	public Header getHeader() throws IOException {
		Header getHeader = new Header();
        BufferedReader getHeaders = new BufferedReader(new FileReader(filename));
        String text = getHeaders.readLine();
        getHeader.setHeaders(text.split(","));
        getHeaders.close();
        return getHeader;

	}
	/**
	 * getDataRow() method will be used in the upcoming assignments
	 */
	
	@Override
	public void getDataRow() {

	}

	/*
	 * Implementation of getColumnType() method. To find out the data types, we will
	 * read the first line from the file and extract the field values from it. If a
	 * specific field value can be converted to Integer, the data type of that field
	 * will contain "java.lang.Integer", otherwise if it can be converted to Double,
	 * then the data type of that field will contain "java.lang.Double", otherwise,
	 * the field is to be treated as String. 
	 * Note: Return Type of the method will be DataTypeDefinitions
	 */
	
	@Override
	public DataTypeDefinitions getColumnType() throws IOException {
	      DataTypeDefinitions getColumn = new DataTypeDefinitions();
	      BufferedReader reader = new BufferedReader(new FileReader(filename));
	      String str1[] = reader.readLine().split(",");
	      String str2 = reader.readLine();
	      String splitData[] = str2.split(",",str1.length);
	      String type[] = new String[str1.length];
	     
	      for(int i=0;i<splitData.length;i++) {
	          if(splitData[i].matches("[0-9]+") ){
	              Integer n = Integer.parseInt(splitData[i]);
	              type[i] = n.getClass().getName();
	          }
	          else {
	          type[i] = splitData[i].getClass().getName();
	      }
	      }
	      System.out.println(Arrays.toString(type));
	      getColumn.setDataTypes(type);
	      reader.close();
	       return getColumn;
	   }
	
}

package com.stackroute.datamunger.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.io.BufferedReader;

import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Header;

public class CsvQueryProcessor extends QueryProcessingEngine {
	File filename = new File("data/ipl.csv");

	

	/*
	 * parameterized constructor to initialize filename. As you are trying to
	 * perform file reading, hence you need to be ready to handle the IO Exceptions.
	 */
	public CsvQueryProcessor(String fileName) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(fileName);
	
	}

	/*
	 * implementation of getHeader() method. We will have to extract the headers
	 * from the first line of the file.
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
	 * This method will be used in the upcoming assignments
	 */
	@Override
	public void getDataRow() {

	}

	/*
	 * implementation of getColumnType() method. To find out the data types, we will
	 * read the first line from the file and extract the field values from it. In
	 * the previous assignment, we have tried to convert a specific field value to
	 * Integer or Double. However, in this assignment, we are going to use Regular
	 * Expression to find the appropriate data type of a field. Integers: should
	 * contain only digits without decimal point Double: should contain digits as
	 * well as decimal point Date: Dates can be written in many formats in the CSV
	 * file. However, in this assignment,we will test for the following date
	 * formats('dd/mm/yyyy',
	 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm-dd')
	 */
	@Override
	public DataTypeDefinitions getColumnType() throws IOException {
		// TODO Auto-generated method stub
		
		// checking for Integer
		
		// checking for floating point numbers
				
		// checking for date format dd/mm/yyyy
		
		// checking for date format mm/dd/yyyy
		
		// checking for date format dd-mon-yy
		
		// checking for date format dd-mon-yyyy
		
		// checking for date format dd-month-yy
		
		// checking for date format dd-month-yyyy
		
		// checking for date format yyyy-mm-dd
		
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
	          else if(splitData[i].matches("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$"))
              {
                  DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                 try {
                     Date date = format.parse(splitData[i]);
                     type[i] = date.getClass().getName();
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }}
	          else if(splitData[i].matches("^((0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-(19|20)\\d\\d)$"))
              {
                  DateFormat format = new SimpleDateFormat("dd-mm-yyyy");
                 try {
                     Date date = format.parse(splitData[i]);
                     type[i] = date.getClass().getName();
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }}	 
	          else if(splitData[i].matches("^((0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])-(19|20)\\\\d\\\\d)$"))
                 {
                     DateFormat format = new SimpleDateFormat("mm-dd-yyyy");
                    try {
                        Date date = format.parse(splitData[i]);
                        type[i] = date.getClass().getName();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }}	        
	          else if(splitData[i].matches("^((0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-(19|20)\\\\d\\\\d)$"))
                    {
                        DateFormat format = new SimpleDateFormat("dd-mm-yy");
                       try {
                           Date date = format.parse(splitData[i]);
                           type[i] = date.getClass().getName();
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }}
               
   	          else if(splitData[i].matches("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$"))
              {
                  DateFormat format = new SimpleDateFormat("dd-mon-yyyy");
                 try {
                     Date date = format.parse(splitData[i]);
                     type[i] = date.getClass().getName();
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
	        	  
	          }
   	          else if(splitData[i].matches("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$"))
              {
                  DateFormat format = new SimpleDateFormat("dd-mon-yyyy");
                 try {
                     Date date = format.parse(splitData[i]);
                     type[i] = date.getClass().getName();
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
	        	  
	          }
   	          else if(splitData[i].matches("^((0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-(19|20)\\\\d\\\\d)$"))
              {
                  DateFormat format = new SimpleDateFormat("dd-month-yyyy");
                 try {
                     Date date = format.parse(splitData[i]);
                     type[i] = date.getClass().getName();
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
	        	  
	          }
	          
   	          else if (splitData[i].isEmpty()) {
	        	  type[i] = "java.lang.Object";
	          }
	          else {
	          type[i] = splitData[i].getClass().getName();
	      }
	      }
	      System.out.println(Arrays.toString(type));
	      getColumn.setDataTypes(type);
	      reader.close();
	       return getColumn;
	   
	}}
	
	

	
	



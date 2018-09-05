package com.stackroute.datamunger.query.parser;

import java.util.*;

/*There are total 4 DataMungerTest file:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 * 
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		queryParameter.setFileName(getFileName(queryString));
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setOrderByFields(getOrderByFields(queryString));
		queryParameter.setGroupByFields(getGroupByFields(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
		queryParameter.setRestrictions(getRestriction(queryString));
		queryParameter.setLogicalOperators(getLogicalOperators(queryString));

		return queryParameter;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
	public String getFileName(String queryString) {
		queryString = queryString.toLowerCase();
        String getFile = queryString.split("from")[1].trim().split(" ")[0];
        return getFile;
		
	}

	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */
	public String getBaseQuery(String queryString) {
		queryString = queryString.toLowerCase();
		String getBase ="";
		
		
		if(queryString.contains("where"))
  		 getBase =queryString.split("where")[0].trim();
		
		else if(queryString.contains("group by"))
		getBase=queryString.split(" group by ")[0].trim();
		

		return getBase;
}
	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */
	public List<String> getOrderByFields(String queryString) {
		queryString = queryString.toLowerCase();
		if(!queryString.contains("order by")) {
			return null;
		}
			String [] s =queryString.split("order by")[1].trim().split("\\s");
			LinkedList<String> getorderBy = new LinkedList<>(Arrays.asList(s));
			return getorderBy;	
		}
		
	
	
	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
	
	
	public List<String> getGroupByFields(String queryString) {
		queryString = queryString.toLowerCase();
		if(queryString.contains("group by")) {
			String [] m =queryString.split("group by")[1].trim().split("order by")[0].trim().split("\\s");
			List<String> getorderBy = Arrays.asList(m);
			return getorderBy;	
		}
		else {
		

		return null;
	}
	}

	/*
	 * Extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */
	
	
	public List<String> getFields(String queryString) {
		queryString = queryString.toLowerCase();
		String [] n  = queryString.split(" ")[1].trim().split(",");
		List<String> getFields = Arrays.asList(n);


		return getFields;
	}

	/*
	 * Extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 */
	public List<Restriction> getRestriction(final String queryString) {
	       if(!queryString.contains("where")) {
	           return null;
	       }
	       else {
	           String str[] =  queryString.split("where")[1].trim().split("group by|order by")[0].trim().split(" and | or ");
	            final List<Restriction> list = new ArrayList<Restriction>();
	            for(String item:str) {
	                String s1[] = item.split("=|<|>");
	                String s[] = {s1[0] , "=", s1[1]};
	                if(item.contains("<")) {
	                    s[1] = "<";
	                }
	                else if(item.contains(">")) {
	                    s[1] = ">";
	                }
	                if(s[2].contains("'")) {
	                    s[2] = s[2].substring(s[2].indexOf("'")+1, s[2].lastIndexOf("'"));
	                }
	                Restriction resfunc = new Restriction(s[0].trim(), s[2].trim(), s[1].trim());
	                list.add(resfunc);
	            }
	            return list;
	       }
	   }
	
	

	/*
	 * Extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * The query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
public List<String> getLogicalOperators(String queryString) {
		
		queryString = queryString.toLowerCase();
		if(!queryString.contains("where")) {
            return null;
       }
       int count = 0;
      String str[] = queryString.split("where")[1].trim().split("\\s");
      for(int i=0;i<str.length;i++) {
          if(str[i].equals("and") || str[i].equals("or")) {
              count++;
          }
      }
      String str1[] = new String[count];
      count = 0;
      for(int i=0;i<str.length;i++) {
         if(str[i].equals("and")) {
             str1[count] = "and";
             count++;
         }
         else if(str[i].equals("or")) {
             str1[count] = "or";
             count++;
         }
      }
      List<String> list=Arrays.asList(str1);
      
       return list;
      
    }

	/*
	 * Extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied.
	 * 
	 * Please note that more than one aggregate function can be present in a query.
	 * 
	 * 
	 */
	public List<AggregateFunction> getAggregateFunctions( final String queryString){
	if(queryString.contains("*"))
	{
		return null;
	}
	String str[] = queryString.split("\\s")[1].split(",");
	final List<AggregateFunction> list = new ArrayList<AggregateFunction>();
	
	for(String string:str) {
		if(string.contains("(")) {
			String afuncs[] = string.split("\\(|\\)");
			AggregateFunction aggregteFunction = new AggregateFunction(afuncs[1],afuncs[0]);
			list.add(aggregteFunction);
		}
	}
	return list;
}
}
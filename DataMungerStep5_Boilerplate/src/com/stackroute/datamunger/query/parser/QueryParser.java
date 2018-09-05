package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();
	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setFileName(getFileName(queryString));
		queryParameter.setOrderByFields(getOrderByFields(queryString));
		queryParameter.setGroupByFields(getGroupByFields(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
		queryParameter.setLogicalOperators(getLogicalOperators(queryString));
		queryParameter.setRestrictions(getRestrictions(queryString));
		return queryParameter;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
	
	private String getFileName(String queryString) {
		String arr[] = queryString.toLowerCase().split(" ");
		for(int i=0; i<arr.length;i++) {
			if(arr[i].equals("from")) {
				return arr[i+1];
			}
		}
		return null;
	}

	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */
	
	private String getBaseQuery(String queryString) {
		int indexOfWhere = queryString.indexOf(" where ");
		if(indexOfWhere<0) {
			return queryString.split(" group by | order by ")[0];
		}		
		else {
			return queryString.substring(0, indexOfWhere);
		}
			
	}

	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */

	private List<String> getOrderByFields(String queryString) {
		int indexOfOrderBy = queryString.indexOf(" order by ");
		if(indexOfOrderBy<0)
			return null;
		else {
			String str = queryString.split(" order by ")[1].split(" ")[0];
			return new LinkedList<String>(Arrays.asList(str.split(" ,")));
		}
	}
	
	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
	
	private List<String> getGroupByFields(String queryString) {
		int indexOfGroupBy = queryString.indexOf(" group by ");
		if(indexOfGroupBy<0)
			return null;
		else {
			String str = queryString.split(" group by ")[1].split(" ")[0];
			return new LinkedList<String>(Arrays.asList(str.split(" ,")));
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
	
	private List<String> getFields(String queryString) {
		String arr[] = queryString.toLowerCase().split("select | from");
		String fieldString = arr[1];
		String fields[] = fieldString.split(",");
		LinkedList<String> fieldsList = new LinkedList<String>();
		for(int i=0; i<fields.length;i++) {
			fieldsList.add(fields[i].trim());
		}
		return fieldsList;
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
	
	private List<Restriction> getRestrictions(String queryString) {
		int indexOfGroupBy = queryString.indexOf(" group by ");
		int indexOfOrderBy = queryString.indexOf(" order by ");
		int indexOfWhere = queryString.indexOf(" where ");
		if(indexOfWhere < 0) {
			return null;
		}
		String conditionString = "";
		if(indexOfGroupBy<0&&indexOfOrderBy<0) {
			conditionString = queryString.substring(queryString.indexOf(" where ")).replace(" where ", "");
		} else {
			int lastIndex = queryString.length();
			if(indexOfGroupBy<0) {
				lastIndex = indexOfOrderBy;
			} else if(indexOfOrderBy<0) {
				lastIndex = indexOfGroupBy;
			} else if(indexOfGroupBy>indexOfOrderBy) {
				lastIndex = indexOfOrderBy;
			} else {
				lastIndex = indexOfGroupBy;
			}
			conditionString = queryString.substring(indexOfWhere, lastIndex).replace(" where ", "");
		}
		ArrayList<Restriction> list = new ArrayList<>();
		for(String str: conditionString.split(" and | or ")) {
			String condition = "";
			if(str.contains(">=")) {
				condition = ">=";
			} else if(str.contains("<=")) {
				condition = "<=";
			} else if(str.contains("!=")) {
				condition = "!=";
			} else if(str.contains(">")) {
				condition = ">";
			} else if(str.contains("<")) {
				condition = "<";
			} else if(str.contains("=")) {
				condition = "=";
			}
			String name = str.split(condition)[0].trim();
			String value = str.split(condition)[1].trim().replaceAll("'", "");
			Restriction restrictionInstance = new Restriction(name, value, condition);
			list.add(restrictionInstance);
		}
		return list;
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

	private List<String> getLogicalOperators(String queryString) {
		if(!queryString.toLowerCase().contains("where"))
			return null;
		ArrayList<String> operatorsArr = new ArrayList<>();
		String arr[] = queryString.split(" ");
		for(String str: arr) {
			if(str.equals("and"))
				operatorsArr.add("and");
			else if(str.equals("or"))
				operatorsArr.add("or");
		}
		return operatorsArr;
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
	
	private List<AggregateFunction> getAggregateFunctions(String queryString) {
		String splitstrings[] = queryString.split(" ");
		ArrayList<String> aggFunctions = new ArrayList<String>();
		for(String string: splitstrings) {
			if(string.contains("sum(")||string.contains("count(")||string.contains("min(")||string.contains("max(")||string.contains("avg(")) {
				aggFunctions = new ArrayList<String>(Arrays.asList(string.split(",")));
			}
		}
		if(aggFunctions.size() == 0)
			return null;
		else {
			ArrayList<AggregateFunction> functionList = new ArrayList<AggregateFunction>();
			for(String aggFun: aggFunctions) {
				if(!aggFun.contains("(")) {
					continue;
				} else {
					String field = aggFun.trim().split("\\(")[1].replace(")", "");
					String function = aggFun.trim().split("\\(")[0];
					AggregateFunction fun = new AggregateFunction(field, function);
					functionList.add(fun);
				}
			}
			return functionList;
		}
	}
	
	
}

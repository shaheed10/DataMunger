package com.stackroute.datamunger.query.parser;

import java.util.List;

/* 
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */

public class QueryParameter {
	private String FileName;
	private String BaseQuery;
	private List<Restriction> Restriction;
	private List<String> LogicalOperators;
	private List<String> Fields;
	private List<AggregateFunction> AggregateFunctions;
	private List<String> GroupByFields;
	private List<String> OrderByFields;
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getBaseQuery() {
		return BaseQuery;
	}
	public void setBaseQuery(String baseQuery) {
		BaseQuery = baseQuery;
	}
	public List<Restriction> getRestrictions() {
		return Restriction;
	}
	public void setRestrictions(List<Restriction> restriction) {
		Restriction = restriction;
	}
	@Override
	public String toString() {
		return "QueryParameter [FileName=" + FileName + ", BaseQuery=" + BaseQuery + ", Restriction=" + Restriction
				+ ", LogicalOperators=" + LogicalOperators + ", Fields=" + Fields + ", AggregateFunctions="
				+ AggregateFunctions + ", GroupByFields=" + GroupByFields + ", OrderByFields=" + OrderByFields + "]";
	}
	public List<String> getLogicalOperators() {
		return LogicalOperators;
	}
	public void setLogicalOperators(List<String> logicalOperators) {
		LogicalOperators = logicalOperators;
	}
	public List<String> getFields() {
		return Fields;
	}
	public void setFields(List<String> fields) {
		Fields = fields;
	}
	public List<AggregateFunction> getAggregateFunctions() {
		return AggregateFunctions;
	}
	public void setAggregateFunctions(List<AggregateFunction> aggregateFunctions) {
		AggregateFunctions = aggregateFunctions;
	}
	public List<String> getGroupByFields() {
		return GroupByFields;
	}
	public void setGroupByFields(List<String> groupByFields) {
		GroupByFields = groupByFields;
	}
	public List<String> getOrderByFields() {
		return OrderByFields;
	}
	public void setOrderByFields(List<String> orderByFields) {
		OrderByFields = orderByFields;
	}

	
}
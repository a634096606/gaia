package org.apel.gaia.commons.jqgrid;

public class RuleItem {
	private String field;
	private String op;
	private Object data;
	private String cusType;
	private boolean prefixBrackets;
	private boolean suffixBrackets;
	private String groupOp;
	
	public RuleItem() {
		
	}

	public RuleItem(String field, String op, Object data, String cusType,
			boolean prefixBrackets, boolean suffixBrackets, String groupOp) {
		super();
		this.field = field;
		this.op = op;
		this.data = data;
		this.cusType = cusType;
		this.prefixBrackets = prefixBrackets;
		this.suffixBrackets = suffixBrackets;
		this.groupOp = groupOp;
	}

	public String getCusType() {
		return cusType;
	}

	public void setCusType(String cusType) {
		this.cusType = cusType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isPrefixBrackets() {
		return prefixBrackets;
	}

	public void setPrefixBrackets(boolean prefixBrackets) {
		this.prefixBrackets = prefixBrackets;
	}

	public boolean isSuffixBrackets() {
		return suffixBrackets;
	}

	public void setSuffixBrackets(boolean suffixBrackets) {
		this.suffixBrackets = suffixBrackets;
	}

	public String getGroupOp() {
		return groupOp;
	}

	public void setGroupOp(String groupOp) {
		this.groupOp = groupOp;
	}
	
	

}

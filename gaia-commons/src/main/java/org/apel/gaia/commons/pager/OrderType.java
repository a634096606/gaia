package org.apel.gaia.commons.pager;

/**
 * QL排序类型
 * 
 * @version 0.0.1
 */
public enum OrderType{
	ASC("ASC"), DESC("DESC");

	private String desc;

	private OrderType(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return desc;
	}
}

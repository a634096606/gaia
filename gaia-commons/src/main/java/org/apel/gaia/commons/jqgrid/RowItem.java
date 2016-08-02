package org.apel.gaia.commons.jqgrid;

public class RowItem {
	private int id;
	private Object[] cell;

	public RowItem(int id, Object[] cell) {
		super();
		this.id = id;
		this.cell = cell;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object[] getCell() {
		return cell;
	}

	public void setCell(Object[] cell) {
		this.cell = cell;
	}

}

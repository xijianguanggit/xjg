package com.tedu.base.common.model;

public class UploadConfig {
    private String tableName;

    private String tableCol;

    private String diaplayName;
    private String type;
    private int length;
    private String regular;
    private String foreignKey;

    public String getDiaplayName() {
        return diaplayName;
    }

    public void setDiaplayName(String diaplayName) {
        this.diaplayName = diaplayName == null ? null : diaplayName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public String getTableCol() {
        return tableCol;
    }

    public void setTableCol(String tableCol) {
        this.tableCol = tableCol == null ? null : tableCol.trim();
    }

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getRegular() {
		return regular;
	}

	public void setRegular(String regular) {
		this.regular = regular;
	}

	public String getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(String foreignKey) {
		this.foreignKey = foreignKey;
	}
    
}
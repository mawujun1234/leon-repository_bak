
package com.mawujun.generator.db;

import com.mawujun.repository.mybatis.dialect.DBAlias;


public class DB2Query extends AbstractDbQuery {


    @Override
    public DBAlias dbType() {
        return DBAlias.db2;
    }


    @Override
    public String tablesSql() {
        return "SELECT * FROM SYSCAT.TABLES where tabschema=current schema";
    }


    @Override
    public String tableFieldsSql() {
        return "SELECT *  FROM syscat.columns WHERE tabschema=current schema AND tabname='%s'";
    }


    @Override
    public String tableName() {
        return "TABNAME";
    }


    @Override
    public String tableComment() {
        return "REMARKS";
    }


    @Override
    public String fieldName() {
        return "COLNAME";
    }


    @Override
    public String fieldType() {
        return "TYPENAME";
    }


    @Override
    public String fieldComment() {
        return "REMARKS";
    }


    @Override
    public String fieldKey() {
        return "IDENTITY";
    }

}

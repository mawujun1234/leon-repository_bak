package com.mawujun.generator.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDbQuery implements IDbQuery {


    @Override
    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return false;
    }


    @Override
    public String[] fieldCustom() {
        return null;
    }
    
	@Override
	public String defaultKey() {
		// TODO Auto-generated method stub
		return "DEFAULT";
	}


	@Override
	public String nullableKey() {
		// TODO Auto-generated method stub
		return "NULL";
	}
	
	
	
}
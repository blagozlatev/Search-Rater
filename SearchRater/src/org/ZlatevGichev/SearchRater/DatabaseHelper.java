package org.ZlatevGichev.SearchRater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	static final String dbName="demoDB";
	static final String employeeTable="Employees";
	static final String colID="EmployeeID";
	static final String colName="EmployeeName";
	static final String colAge="Age";
	static final String colDept="Dept";

	static final String wordTable="Word";
	static final String colWordID="WordID";
	static final String colWord="Word";

	static final String viewEmps="ViewEmps";
	
	
	public DatabaseHelper(Context context) {
		  super(context, dbName, null,33); 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE "+wordTable+" ("+colWordID+ " INTEGER PRIMARY KEY , "+
			    colWord+ " TEXT)");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}

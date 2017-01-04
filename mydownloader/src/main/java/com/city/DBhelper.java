package com.city;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBhelper {

	private SQLiteDatabase db;
	private Context context;
	private DBManager dbm;
	
	public DBhelper(Context context) {
		super();
		this.context = context;
		dbm = new DBManager(context);
	}

	public ArrayList<Area> getCity(String pcode) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		ArrayList<Area> list = new ArrayList<Area>();
		
	 	try {    
	        String sql = "select * from city where pcode='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	String code=cursor.getString(cursor.getColumnIndex("code"));
				String pcodeArea=cursor.getString(cursor.getColumnIndex("pcode"));
		        String name=cursor.getString(cursor.getColumnIndex("name"));
		        Area area=new Area();
		        area.setName(name);
		        area.setCode(code);
		        area.setPcode(pcodeArea);
		        list.add(area);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("code"));
			String name=cursor.getString(cursor.getColumnIndex("name"));
			String pcodeArea=cursor.getString(cursor.getColumnIndex("pcode"));
	        Area area=new Area();
	        area.setName(name);
	        area.setCode(code);
	        area.setPcode(pcodeArea);
	        list.add(area);
	        
	    } catch (Exception e) {  
	    	return null;
	    } 
	 	dbm.closeDatabase();
	 	db.close();	

		return list;

	}


	public ArrayList<Area> getProvince() {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<Area> list = new ArrayList<Area>();
		
	 	try {    
	        String sql = "select * from province";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	String code=cursor.getString(cursor.getColumnIndex("code"));
		        String name=cursor.getString(cursor.getColumnIndex("name"));
				String pcode=cursor.getString(cursor.getColumnIndex("pcode"));
		        Area area=new Area();
		        area.setName(name);
		        area.setCode(code);
				area.setPcode(pcode);
		        list.add(area);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("code"));
			String name=cursor.getString(cursor.getColumnIndex("name"));
			String pcode=cursor.getString(cursor.getColumnIndex("pcode"));
	        Area area=new Area();
	        area.setName(name);
	        area.setCode(code);
			area.setPcode(pcode);
	        list.add(area);
	        
	    } catch (Exception e) {  
	    	return null;
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
		
	}
	public ArrayList<Area> getDistrict(String pcode) {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<Area> list = new ArrayList<Area>();
	 	try {    
	        String sql = "select * from district where pcode='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);
	        if (cursor.moveToFirst()) {
				while (!cursor.isLast()) {
					String code = cursor.getString(cursor.getColumnIndex("code"));
					String name=cursor.getString(cursor.getColumnIndex("name"));
					String pcodeArea=cursor.getString(cursor.getColumnIndex("pcode"));
					Area Area = new Area();
					Area.setName(name);
					Area.setCode(code);
					Area.setPcode(pcodeArea);
					list.add(Area);
					cursor.moveToNext();
				}
				String code = cursor.getString(cursor.getColumnIndex("code"));
				String name=cursor.getString(cursor.getColumnIndex("name"));
				String pcodeArea=cursor.getString(cursor.getColumnIndex("pcode"));
				Area Area = new Area();
				Area.setName(name);
				Area.setCode(code);
				Area.setPcode(pcodeArea);
				list.add(Area);
			}
	        
	    } catch (Exception e) { 
	    	Log.i("wer", e.toString());
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
		
	}

	public Area getCityArea(String code) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		Area Area = new Area();
		try {
			String sql = "select * from city where code='"+code+"'";
			Cursor cursor = db.rawQuery(sql,null);
			if (cursor.moveToFirst()) {

				code = cursor.getString(cursor.getColumnIndex("code"));
				String name=cursor.getString(cursor.getColumnIndex("name"));
				String pcodeArea=cursor.getString(cursor.getColumnIndex("pcode"));

				Area.setName(name);
				Area.setCode(code);
				Area.setPcode(pcodeArea);

			}

		} catch (Exception e) {
			Log.i("wer", e.toString());
		}
		dbm.closeDatabase();
		db.close();
		return Area;

	}

	public Area getProvinceArea(String code) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		Area Area = new Area();
		try {
			String sql = "select * from province where code='"+code+"'";
			Cursor cursor = db.rawQuery(sql,null);
			if (cursor.moveToFirst()) {

				code = cursor.getString(cursor.getColumnIndex("code"));
				String name=cursor.getString(cursor.getColumnIndex("name"));
				String pcodeArea=cursor.getString(cursor.getColumnIndex("pcode"));

				Area.setName(name);
				Area.setCode(code);
				Area.setPcode(pcodeArea);

			}

		} catch (Exception e) {
			Log.i("wer", e.toString());
		}
		dbm.closeDatabase();
		db.close();
		return Area;

	}

	public Area getDistrictArea(String code) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		Area Area = new Area();
		try {
			String sql = "select * from district where code='"+code+"'";
			Cursor cursor = db.rawQuery(sql,null);
			if (cursor.moveToFirst()) {

				code = cursor.getString(cursor.getColumnIndex("code"));
				String name=cursor.getString(cursor.getColumnIndex("name"));
				String pcodeArea=cursor.getString(cursor.getColumnIndex("pcode"));

				Area.setName(name);
				Area.setCode(code);
				Area.setPcode(pcodeArea);

			}

		} catch (Exception e) {
			Log.i("wer", e.toString());
		}
		dbm.closeDatabase();
		db.close();
		return Area;

	}
}

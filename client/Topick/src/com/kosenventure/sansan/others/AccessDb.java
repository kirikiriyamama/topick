package com.kosenventure.sansan.others;

import com.kosenventure.sansan.topick.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AccessDb {

	private Context mContext;
	private SQLiteOpenHelper mHelper;
	private SQLiteDatabase mWritableDb;

	public final static String DATABESE_NAME = "topickdb.sqlite3";
	public final static CursorFactory DATABASE_FACTORY = null;
	public final static int DATABASE_VERSION = 1;
	
	public AccessDb(Context context) {
		mContext = context;
		initializeDb();
	}

	private void initializeDb(){
		mHelper = new LocusHelper(mContext, DATABESE_NAME, DATABASE_FACTORY, DATABASE_VERSION);
		try{
			mWritableDb = mHelper.getWritableDatabase();
		}catch(SQLException e){
			log(e.getMessage());
		}
	}
	
	public Cursor readDb(String table_name, String[] column, String where, String[] answer, String order_by) {
        Cursor cur = null;
 
        try {
            String groupBy = null;
            String having = null;
 
            cur = mWritableDb.query(table_name, column,
                    where, answer, groupBy, having,
                    order_by);
        }catch(SQLException e){
        	log(e.getMessage());
        }
        
        if (cur != null && cur.moveToFirst()) { return cur; } 
        else { return null; }
    }
	
	public void writeDb(String table_name, String phrase) {
		// ContentValuesインスタンスに
        // データベースへ書き込む情報をセット
        ContentValues values = new ContentValues();
        values.put("phrase", phrase);
 
        mWritableDb.beginTransaction();
        
        try {
            mWritableDb.insert(table_name, null, values);
            mWritableDb.setTransactionSuccessful();
        } finally {
            mWritableDb.endTransaction();
        }
	}
	
	public void deleteDb(String table_name, String id){
    	int count = 0;
    	try{
    		count = mWritableDb.delete(table_name, "id = ?", new String[]{id});
    	}catch(SQLException e){
    		log(e.getMessage());
    	}
    	if(count == 0) log("delete missed");
    }
	
    public void updateDb(String table_name, String id,ContentValues values){
    	int count = 0;
    	try{
    		count = mWritableDb.update(table_name, values, "id = ?", new String[]{id});
    	}catch(SQLException e){
    		log(e.getMessage());
    	}
    	if(count == 0) log("update missed");    	
    }
    
    public void closeDb(){
    	mWritableDb.close();
    	mHelper.close();
    }
	
	protected void log(String msg){
		Log.d("AccessDb", msg);
	}
	
	public class LocusHelper extends SQLiteOpenHelper {

		private Context mContext;
		
		public LocusHelper(Context context, String name, CursorFactory factory,int version) {
			super(context, name, factory, version);
			mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
	        // データベース処理開始
			db.beginTransaction();
	        try {
	            // テーブル作成を実行
	            db.execSQL(getStr(R.string.create_keyphrase_table_sql));
	            db.setTransactionSuccessful();
	        } finally {
	            // データベース終了処理
	            db.endTransaction();
	        }
		}

	    private String getStr(int resourceId) {
			return mContext.getResources().getString(resourceId);
		}
	    
		@Override
	    public void onOpen(SQLiteDatabase db) {
	        // データベースが開かれたときに実行される
	        // これの実装は任意

	        super.onOpen(db);
	    }
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

}

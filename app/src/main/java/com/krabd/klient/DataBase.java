package com.krabd.klient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "lec_database.db";
	private static final int DATABASE_VERSION = 1;

	public DataBase(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void dropTable(SQLiteDatabase db, String table_name) {
		db.execSQL("DROP TABLE IF EXISTS " + table_name);
	}

	public void delete(long rowId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TEST_TABLE, testID + "=" + rowId, null);
		db.close();
	}


	public static final String STUD_TABLE = "student";

	public static final String UIDs = "_id";
	public static final String studID = "id";
	public static final String studNAME = "studname";
	public static final String studSURN = "studsurn";
	public static final String studAUTH = "studauth";
	public static final String studGROUP = "studgroup";

	private static final String SQL_CREATE_ENTRIES3 = "CREATE TABLE "
			+ STUD_TABLE + " (" + UIDs + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ studID + " VARCHAR(255)," + studNAME + " VARCHAR(255),"
			+ studSURN + " VARCHAR(255)," + studAUTH + " VARCHAR(255),"
			+ studGROUP + " VARCHAR(255));";

	public void createStudTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES3);
	}

	public Cursor getAllStudData() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.query(STUD_TABLE, new String[]{UIDs, studID, studNAME,
						studSURN, studAUTH, studGROUP}, null, // �������, �� ��������
				// ���������� �������
				null, // ��������, �� �������� ���������� �������
				null, null, null // �������, �� �������� �����������
		);
	}

	void insertStudTable(String stID, String stname, String stsurn,
						 String auth, String stgr) {
		SQLiteDatabase sqdb = this.getWritableDatabase();
		dropTable(sqdb, STUD_TABLE);
		createStudTable(sqdb);
		ContentValues values = new ContentValues();
		values.put(studID, stID);
		values.put(studNAME, stname);
		values.put(studSURN, stsurn);
		values.put(studAUTH, auth);
		values.put(studGROUP, stgr);
		sqdb.insert(STUD_TABLE, studID, values);
	}


	public static final String LEC_TABLE = "lec_table";

	public static final String lecID = "_id";
	public static final String lecNAME = "lecname";
	public static final String lecDISC = "lecdisc";
	public static final String lecURL = "lecurl";

	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ LEC_TABLE + " (" + lecID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ lecNAME + " VARCHAR(255)," + lecDISC + " VARCHAR(255)," + lecURL
			+ " VARCHAR(255));";

	public void createLecTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	public Cursor getAllLecData() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.query(LEC_TABLE, new String[]{lecID, lecNAME, lecDISC,
				lecURL}, null, null, null, null, null);
	}

	void insertLecTable(String lecname, String lecdisc, String lecurl) {
		SQLiteDatabase sqdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(lecNAME, lecname);
		values.put(lecDISC, lecdisc);
		values.put(lecURL, lecurl);
		sqdb.insert(LEC_TABLE, lecNAME, values);
	}

	public static final String TEST_TABLE = "test_table";


	public static final String testID = "_id";
	public static final String testNAME = "testname";
	public static final String testDISC = "testdisc";

	private static final String SQL_CREATE_ENTRIES2 = "CREATE TABLE "
			+ TEST_TABLE + " (" + testID + " VARCHAR(255)," + testNAME
			+ " VARCHAR(255)," + testDISC + " VARCHAR(255));";

	public void createTestTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES2);
	}

	public Cursor getAllTestData() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.query(TEST_TABLE,
				new String[]{testID, testNAME, testDISC}, null, null, null,
				null, null);
	}

	void insertTestTable(String testid, String testname, String testdisc) {
		SQLiteDatabase sqdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(testID, testid);
		values.put(testNAME, testname);
		values.put(testDISC, testdisc);
		sqdb.insert(TEST_TABLE, testNAME, values);
	}


	public static final String QUEST_TABLE = "vopr_table";

	public static final String questID = "_id";
	public static final String UIDt = "_idt";
	public static final String questTYPE = "voprtype";
	public static final String questTEXT = "voprtext";
	public static final String VAR1 = "var1";
	public static final String VAR2 = "var2";
	public static final String VAR3 = "var3";
	public static final String VAR4 = "var4";
	public static final String imageURL = "imageURL";
	public static final String questANSWER = "answer";
	public static final String urlhit = "urlhit";
	public static final String quid = "quid";

	private static final String SQL_CREATE_ENTRIES1 = "CREATE TABLE "
			+ QUEST_TABLE + " (" + questID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + UIDt + " VARCHAR(255),"
			+ questTYPE + " VARCHAR(255)," + questTEXT + " VARCHAR(255),"
			+ VAR1 + " VARCHAR(255)," + VAR2 + " VARCHAR(255)," + VAR3
			+ " VARCHAR(255)," + VAR4 + " VARCHAR(255)," + imageURL
			+ " VARCHAR(255)," + questANSWER + " VARCHAR(255)," + urlhit + " VARCHAR(255),"+ quid +" VARCHAR(255));";

	public void createQuestTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES1);
	}

	public Cursor getAllQuestData(String selection, String[] selectionArgs) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.query(QUEST_TABLE, new String[]{questID, UIDt, questTYPE,
						questTEXT, VAR1, VAR2, VAR3, VAR4, imageURL,
						DataBase.questANSWER,urlhit,quid}, selection, selectionArgs, null,
				null, null);
	}


	void insertQuestTable(String testid, String questtype, String questtext,
			String var1, String var2, String var3, String var4, String imgurl,
			String questanswer,String hiturl,String quids) {
		SQLiteDatabase sqdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UIDt, testid);
		values.put(questTYPE, questtype);
		values.put(questTEXT, questtext);
		values.put(VAR1, var1);
		values.put(VAR2, var2);
		values.put(VAR3, var3);
		values.put(VAR4, var4);
		values.put(imageURL, imgurl);
		values.put(questANSWER, questanswer);
		values.put(urlhit, hiturl);
		values.put(quid, quids);
		sqdb.insert(QUEST_TABLE, UIDt, values);
	}

	/*--------------------------------------------------------------------------------------------------*/
	/* Statist Table */
	/*
	 * �������� ������� ��� ������ ������ ������ ��������.
	 */
	public static final String STATIST_TABLE = "statist";

	/*
	 * ������� ������� � ������� �������. statistID: ���������� ����� � �������;
	 * statistQUEST: �������� �����; statistRES: ���������.
	 */
	public static final String statistID = "_id";
	public static final String statistQUEST = "quest";
	public static final String statistRES = "res";

	/*
	 * ������ � �������� �� �������� ������� � ������� ������.
	 */
	private static final String SQL_CREATE_ENTRIES4 = "CREATE TABLE "
			+ STATIST_TABLE + " (" + statistID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + statistQUEST
			+ " VARCHAR(255)," + statistRES + " VARCHAR(255));";

	/*
	 * ����� ��� �������� ������� � ������� ������. ������� ������: db: ����
	 * ������.
	 */
	public void createStatistTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES4);
	}

	/*
	 * ����� ��� ��������� ���� ������� �� ������� � ������� ������. ��������
	 * ������: Cursor: ������, ���������� ��� ������.
	 */
	public Cursor getAllStatistData() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.query(STATIST_TABLE, new String[] { statistID, statistQUEST,
				statistRES }, null, null, null, null, statistID + " DESC");
	}

	/*
	 * ����� ��� ���������� ������ � ������� � ������� ������. ������� ������:
	 * statistquest: �������� �����; statistres: ���������.
	 */
	void insertStatistTable(String statistquest, String statistres) {
		SQLiteDatabase sqdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(statistQUEST, statistquest);
		values.put(statistRES, statistres);
		sqdb.insert(STATIST_TABLE, statistQUEST, values);
	}

	/*--------------------------------------------------------------------------------------------------*/

	public static final String Rez_TABLE = "rez";

	/*
	 * ������� ������� � ������� �������. statistID: ���������� ����� � �������;
	 * statistQUEST: �������� �����; statistRES: ���������.
	 */
	public static final String Rezz = "_Rez";

	/*
	 * ������ � �������� �� �������� ������� � ������� ������.
	 */
	private static final String SQL_CREATE_ENTRIES5 = "CREATE TABLE  if not exists "
			+ Rez_TABLE +" ("+  Rezz + " VARCHAR(255));";

	/*
	 * ����� ��� �������� ������� � ������� ������. ������� ������: db: ����
	 * ������.
	 */
	public void createRezTable(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES5);
	}

	/*
	 * ����� ��� ��������� ���� ������� �� ������� � ������� ������. ��������
	 * ������: Cursor: ������, ���������� ��� ������.
	 */
	public Cursor getAllRezData() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.query(Rez_TABLE,null,null,null,null,null,null);
	}

	/*
	 * ����� ��� ���������� ������ � ������� � ������� ������. ������� ������:
	 * statistquest: �������� �����; statistres: ���������.
	 */
	void insertRezTable(String Rez) {
		SQLiteDatabase sqdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Rezz, Rez);
		sqdb.insert(Rez_TABLE, Rezz, values);
	}

	public static final String Time_TABLE = "time_table";

	public static final String awsID = "_id";
	public static final String qustid = "qust";
	public static final String idtest1 = "test";
	public static final String iduser = "user";

	private static final String SQL_CREATE_ENTRIES8 = "CREATE TABLE "
			+ Time_TABLE + " (" + awsID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ qustid + " int(255)," + idtest1 + " int(255)," + iduser
			+ " int(255));";

	public void createTimetab(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES8);
	}

	public Cursor getAllTimedate() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.query(Time_TABLE, new String[]{awsID, qustid, idtest1,
				iduser}, null, null, null, null, null);

	}

	void insertTimeDate(int questID, int idtest, int iduser1) {
		SQLiteDatabase sqdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(qustid, questID);
		values.put(idtest1, idtest);
		values.put(iduser, iduser1);
		sqdb.insert(LEC_TABLE,awsID, values);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
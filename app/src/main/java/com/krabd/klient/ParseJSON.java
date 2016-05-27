package com.krabd.klient;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

/*
 * Класс для анализа ответа сервера в формате JSON.
 */
public class ParseJSON {

	/*--------------------------------------------------------------------------------------------------*/
	/* Stud Parse */
	/*
	 * Метод для анализа ответа сервера на запрос информации о студенте и записи
	 * расшифрованой строки в БД. Входные данные: stringresponse: Ответ сервера;
	 * context: Активность, в которой вызывается метод; group: Группа студента.
	 */
	public static void parseStud(String stringresponse, Context context,
								 String group) {
		DataBase sqh = new DataBase(context);
		String OutputData = "";
		String OutputData1 = "";
		String OutputData3 = "";
		JSONObject jsonResponse;
		try {
			jsonResponse = new JSONObject(stringresponse);
			OutputData3 = jsonResponse.getString("id_st");
			String stid = new String(OutputData3.getBytes("ISO-8859-1"),
					"UTF-8");
			OutputData = jsonResponse.getString("name");
			String stname = new String(OutputData.getBytes("ISO-8859-1"),
					"UTF-8");
			OutputData1 = jsonResponse.getString("surname");
			String stsurn = new String(OutputData1.getBytes("ISO-8859-1"),
					"UTF-8");
			sqh.insertStudTable(stid, stname, stsurn, "true", group);
		}
		catch (Exception e) {
		}
	}

	/*--------------------------------------------------------------------------------------------------*/
	/* Lec Parse */
	/*
	 * Метод для анализа ответа сервера на запрос информации о лекциях и записи
	 * расшифрованых строк в БД. Входные данные: stringresponse: Ответ сервера;
	 * context: Активность, в которой вызывается метод.
	 */
	public static void parseLec(String stringresponse, Context context) {
		DataBase sqh = new DataBase(context);
		String OutputData = "";
		String OutputData1 = "";
		String OutputData2 = "";
		try {
			JSONArray jr = new JSONArray(stringresponse);
			for (int i = 0; i < jr.length(); i++) {
				JSONObject jb = jr.getJSONObject(i);
				OutputData = jb.getString("name");
				String lecname = new String(OutputData.getBytes("ISO-8859-1"),
						"UTF-8");
				OutputData1 = jb.getString("description");
				String lecdisc = new String(OutputData1.getBytes("ISO-8859-1"),
						"UTF-8");
				OutputData2 = jb.getString("url");
				String lecurl = new String(OutputData2.getBytes("ISO-8859-1"),
						"UTF-8");
				sqh.insertLecTable(lecname, lecdisc, lecurl);
			}
		}
		catch (Exception e) {
		}
	}

	/*--------------------------------------------------------------------------------------------------*/
	/* Test Parse */
	/*
	 * Метод для анализа ответа сервера на запрос информации о тестах и записи
	 * расшифрованых строк в БД. Входные данные: stringresponse: Ответ сервера;
	 * context: Активность, в которой вызывается метод.
	 */
	public static void parseTest(String stringresponse, Context context) {
		DataBase sqh = new DataBase(context);
		String OutputData = "";
		String OutputData1 = "";
		String OutputData2 = "";
		try {
			JSONArray jr = new JSONArray(stringresponse);
			for (int i = 0; i < jr.length(); i++) {
				JSONObject jb = jr.getJSONObject(i);
				OutputData = jb.getString("name");
				String testname = new String(OutputData.getBytes("ISO-8859-1"),
						"UTF-8");
				OutputData1 = jb.getString("recom");
				String testdisc = new String(
						OutputData1.getBytes("ISO-8859-1"), "UTF-8");
				OutputData2 = jb.getString("id_ts");
				String testid = new String(OutputData2.getBytes("ISO-8859-1"),
						"UTF-8");
				sqh.insertTestTable(testid, testname, testdisc);
			}
		}
		catch (Exception e) {
		}
	}

	/*--------------------------------------------------------------------------------------------------*/
	/* Quest Parse */
	/*
	 * Метод для анализа ответа сервера на запрос информации о вопросах к тестам
	 * и записи расшифрованых строк в БД. Входные данные: stringresponse: Ответ
	 * сервера; context: Активность, в которой вызывается метод.
	 */
	public static void parseQuest(String stringresponse, Context context) {
		DataBase sqh = new DataBase(context);
		String OutputData = "";
		String OutputData1 = "";
		String OutputData2 = "";
		String OutputData3 = "";
		String OutputData4 = "";
		String OutputData5 = "";
		String OutputData6 = "";
		String OutputData7 = "";
		String OutputData8 = "";
		String OutputData9 = "";
		try {
			JSONArray jr = new JSONArray(stringresponse);
			for (int i = 0; i < jr.length(); i++) {
				JSONObject jb = jr.getJSONObject(i);
				OutputData = jb.getString("id_ts");
				String testid = new String(OutputData.getBytes("ISO-8859-1"),
						"UTF-8");
				OutputData1 = jb.getString("type");
				String questtype = new String(
						OutputData1.getBytes("ISO-8859-1"), "UTF-8");
				OutputData2 = jb.getString("question");
				String questtext = new String(
						OutputData2.getBytes("ISO-8859-1"), "UTF-8");
				OutputData3 = jb.getString("var1");
				String var1 = new String(OutputData3.getBytes("ISO-8859-1"),
						"UTF-8");
				OutputData4 = jb.getString("var2");
				String var2 = new String(OutputData4.getBytes("ISO-8859-1"),
						"UTF-8");
				OutputData5 = jb.getString("var3");
				String var3 = new String(OutputData5.getBytes("ISO-8859-1"),
						"UTF-8");
				OutputData6 = jb.getString("var4");
				String var4 = new String(OutputData6.getBytes("ISO-8859-1"),
						"UTF-8");
				OutputData7 = jb.getString("imgUrl");
				String imgurl = new String(OutputData7.getBytes("ISO-8859-1"),
						"UTF-8");
				OutputData8 = jb.getString("answ");
				String questanswer = new String(
						OutputData8.getBytes("ISO-8859-1"), "UTF-8");
				OutputData9 = jb.getString("hits");
				String urlhit = new String(
						OutputData9.getBytes("ISO-8859-1"), "UTF-8");
				sqh.insertQuestTable(testid, questtype, questtext, var1, var2,
						var3, var4, imgurl, questanswer, urlhit);
			}
		}
		catch (Exception e) {
		}
	}

	/*--------------------------------------------------------------------------------------------------*/
	/* Statist Parse */
	/*
	 * Метод для анализа ответа сервера на запрос информации об оценках студента
	 * и записи расшифрованых строк в БД. Входные данные: stringresponse: Ответ
	 * сервера; context: Активность, в которой вызывается метод.
	 */
	public static void parseStatist(String stringresponse, Context context) {
		DataBase sqh = new DataBase(context);
		String OutputData = "";
		try {
			JSONObject actor = new JSONObject(stringresponse);
			JSONObject progress = actor.getJSONObject("progress");
			Iterator<?> keys = progress.keys();
			int j = progress.length();
			for (int i = 0; i < j; i++) {
				String key = (String) keys.next();
				OutputData = progress.getString(key);
				String statistres = new String(
						OutputData.getBytes("ISO-8859-1"), "UTF-8");
				sqh.insertStatistTable(key, statistres);
			}
		}
		catch (Exception e) {
		}
	}

	/*--------------------------------------------------------------------------------------------------*/
	/* One Res Parse */
	/*
	 * Метод для анализа ответа сервера на запрос информации об оценке за
	 * пройденный тест и записи расшифрованых строк в БД. Входные данные:
	 * stringresponse: Ответ сервера; context: Активность, в которой вызывается
	 * метод.
	 */
	public static String parseRes(String stringresponse, Context context) {
		String OutputData = "";
		String proc = "exc";
		try {
			JSONObject jb = new JSONObject(stringresponse);
			OutputData = jb.getString("proc");
			proc = new String(OutputData.getBytes("ISO-8859-1"), "UTF-8");
		}
		catch (Exception e) {
		}
		return proc;
	}
}
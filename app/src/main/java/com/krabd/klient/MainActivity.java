package com.krabd.klient;

import java.io.File;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.krabd.klient.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public class MainActivity extends Activity {

	AuthTask Auth_T;
	LectTask Lect_T;
	QuestTask Quest_T;
	TestTask Test_T;
	StatistTask Statist_T;
	LectDownloadTask downloadTask;
	LastLectDownloadTask downloadTask1;
	ImgDownloadTask imagedownloadTask;
	Button but;
	int progress;
	boolean flav;
	boolean flagauth;
	EditText logi, pas, gro;
	int myProgress = 0;
	float per, per1;
	private ProgressBar pb;
	DataBase sqh = new DataBase(this);
	AlertDialog.Builder ad;
	Context context;
	int length;
	String studid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		logi = (EditText) findViewById(R.id.log);
		pas = (EditText) findViewById(R.id.pass);
		gro = (EditText) findViewById(R.id.group);
		but = (Button)  findViewById(R.id.button1);
		pb = (ProgressBar) findViewById(R.id.progressBar2);
		pb.setVisibility(View.GONE);
		try {
			Cursor cursor = sqh.getAllStudData();
			int length = cursor.getCount();
			final String[] lecnm = new String[length];
			int i = 0;
			while (cursor.moveToNext()) {
				// GET COLUMN INDICES + VALUES OF THOSE COLUMNS
				String name = cursor.getString(cursor
						.getColumnIndex(DataBase.studAUTH));
				lecnm[i] = name;
				i++;
			}
			cursor.close();
			if (lecnm[0].equals("true")) {
				Intent intent = new Intent(MainActivity.this,
						MenuActivity.class);
				startActivity(intent);
			}
		}
		catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick1(View v) {
		Intent intent = new Intent(MainActivity.this, RegActivity.class);
		startActivity(intent);
	}

	public void onClick(View v) throws IOException {
		myProgress = 0;
		pb.setMax(100);
		but.setEnabled(false);
		pb.setVisibility(View.VISIBLE);
		myProgress++;
		pb.setProgress(myProgress);
		Variable.lgnm = logi.getText().toString();
		Variable.group = gro.getText().toString();
		Variable.pssw = pas.getText().toString();
		if (Variable.lgnm.equals("") || Variable.pssw.equals("")
				|| Variable.group.equals("")) {
			but.setEnabled(true);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setTitle("Ошибка")
					.setMessage("Необходимо заполнить все поля")
					.setCancelable(false)
					.setNegativeButton("Продолжить заполнение",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
													int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
		else {
			boolean checkCon;
			checkCon = checkInternetConnection();
			if (checkCon) {
				Auth_T = new AuthTask();
				Auth_T.execute(Variable.group, Variable.lgnm, Variable.pssw,
						Variable.stringresponse_stud);
			}
			else {
				but.setEnabled(true);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("Ошибка")
						.setMessage("Нет подключения к интернету")
						.setCancelable(false)
						.setNegativeButton("Попробовать позже",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}

	private boolean checkInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Закрытие приложения");
		alertDialog.setMessage("Вы действительно хотите закрыть приложение?");
		alertDialog.setButton("Да", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		alertDialog.setButton2("Нет", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		// Set the Icon for the Dialog
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.show();
	}

	private class AuthTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("gr", params[0]));
			nameValuePairs.add(new BasicNameValuePair("lg", params[1]));
			nameValuePairs.add(new BasicNameValuePair("pss", params[2]));
			Variable.stringresponse_stud = POSTRequest.POST_Data(
					nameValuePairs, Variable.URL_stud);
			return Variable.stringresponse_stud;
		}

		protected void onPostExecute(String result) {
			myProgress = myProgress + 10;
			pb.setProgress(myProgress);
			String ae, qw;
			ae = Variable.stringresponse_stud;
			qw = ae.substring(1, 2);
			switch (qw) {
				case "O":
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MainActivity.this);
					builder.setTitle("Ошибка")
							.setMessage("Пользователь не найден")
							.setCancelable(false)
							.setNegativeButton("Ввести заново",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
					break;
				case "\"":
					flagauth = true;
					try {
						context = MainActivity.this;
						ParseJSON.parseStud(Variable.stringresponse_stud, context,
								Variable.group);
						Cursor cursorst = sqh.getAllStudData();
						while (cursorst.moveToNext()) {
							studid = cursorst.getString(cursorst
									.getColumnIndex(DataBase.studID));
						}
						cursorst.close();
						Lect_T = new LectTask();
						Lect_T.execute(Variable.group, Variable.stringresponse_lect);
						Test_T = new TestTask();
						Test_T.execute(Variable.group, studid,
								Variable.stringresponse_test);
						Statist_T = new StatistTask();
						Statist_T.execute(studid, Variable.group,
								Variable.stringresponse_statist);
						File checkDir = new File(getFilesDir() + "/mpeiClient/imge");
						if (checkDir.exists()) {
						}
						else {
							checkDir.mkdirs();
						}
						String ds, gh;
						imagedownloadTask = new ImgDownloadTask(MainActivity.this);
						ds = "ava";
						gh = Variable.URL_avatar + Variable.lgnm + ".JPG";
						File sourceFile = new File(getFilesDir()
								+ "/mpeiClient/imge/" + ds + ".jpeg");
						if (sourceFile.isFile()) {
							myProgress = (int) (myProgress + per1);
							pb.setProgress(myProgress);
						}
						else {
							flav = true;
							imagedownloadTask.execute(gh, ds);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					but.setEnabled(true);
					AlertDialog.Builder builder2 = new AlertDialog.Builder(
							MainActivity.this);
					builder2.setTitle("Ошибка")
							.setMessage("Внутренняя ошибка сервера")
							.setCancelable(false)
							.setNegativeButton("ОК",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert2 = builder2.create();
					alert2.show();
					break;
			}
		}

		protected void onProgressUpdate(Integer... progress) {

		}
	}

	private class LectTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gr", params[0]));
			Variable.stringresponse_lect = POSTRequest.POST_Data(
					nameValuePairs, Variable.URL_lect);
			return Variable.stringresponse_lect;
		}

		protected void onPostExecute(String result) {

			myProgress = myProgress + 10;
			pb.setProgress(myProgress);
			try {
				SQLiteDatabase sqdb = sqh.getWritableDatabase();
				sqh.dropTable(sqdb, DataBase.LEC_TABLE);
				sqh.createLecTable(sqdb);
				context = MainActivity.this;
				ParseJSON.parseLec(Variable.stringresponse_lect, context);
				final Cursor cursor = sqh.getAllLecData();
				final int length = cursor.getCount();
				final String[] lecnm = new String[length];
				per = (int) Math.floor(40 / length);
				String title = "Предупреждение";
				String message = "Сейчас произойдет проверка обновлений лекций. Возможно будут скачаны новые материалы."
						+ " Вы согласны?";
				String button1String = "Да";
				String button2String = "Нет";
				ad = new AlertDialog.Builder(context);
				ad.setTitle(title);
				ad.setMessage(message);
				ad.setPositiveButton(button1String, new OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						int i = 0;
						while (cursor.moveToNext()) {
							// GET COLUMN INDICES + VALUES OF THOSE COLUMNS
							String name = cursor.getString(cursor
									.getColumnIndex(DataBase.lecURL));
							lecnm[i] = name;
							i++;
						}
						cursor.close();
						String gh, ds;
						File checkDir = new File("sdcard/mpeiClient/lect");
						if (checkDir.exists()) {
						}
						else {
							checkDir.mkdirs();
						}
						for (int j = 0; j < length - 1; j++) {
							ds = Integer.toString(j + 1);
							File sourceFile = new File(
									"sdcard/mpeiClient/lect/lect" + ds + ".pdf");
							if (sourceFile.isFile()) {
								myProgress = (int) (myProgress + per);
								pb.setProgress(myProgress);
							}
							else {
								downloadTask = new LectDownloadTask(context);
								gh = lecnm[j];
								downloadTask.execute(gh, ds);
							}
						}
						downloadTask1 = new LastLectDownloadTask(
								MainActivity.this);
						ds = Integer.toString(length);
						gh = lecnm[length - 1];
						downloadTask1.execute(gh, ds);
					}
				});
				ad.setNegativeButton(button2String, new OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						ad.show();
					}
				});
				ad.setCancelable(true);
				ad.setOnCancelListener(new OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						Toast.makeText(context, "Вы ничего не выбрали",
								Toast.LENGTH_LONG).show();
					}
				});
				ad.show();
				sqdb.close();
				sqh.close();
			}
			catch (Exception e) {
				Intent intent = new Intent(MainActivity.this,
						MenuActivity.class);
				startActivity(intent);
				e.printStackTrace();
			}
		}

		protected void onProgressUpdate(Integer... progress) {

		}
	}

	private class TestTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("gr", params[0]));
			nameValuePairs.add(new BasicNameValuePair("id", params[1]));
			Variable.stringresponse_test = POSTRequest.POST_Data(
					nameValuePairs, Variable.URL_test);
			return Variable.stringresponse_test;
		}

		protected void onPostExecute(String result) {
			myProgress = myProgress + 10;
			pb.setProgress(myProgress);
			try {
				SQLiteDatabase sqdb = sqh.getWritableDatabase();
				sqh.dropTable(sqdb, DataBase.TEST_TABLE);
				sqh.createTestTable(sqdb);
				sqh.dropTable(sqdb, DataBase.QUEST_TABLE);
				sqh.createQuestTable(sqdb);
				ParseJSON.parseTest(Variable.stringresponse_test, context);
				Cursor cursoridt = sqh.getAllTestData();
				String[] idt = new String[cursoridt.getCount()];
				int i = 0;
				while (cursoridt.moveToNext()) {
					idt[i] = cursoridt.getString(cursoridt
							.getColumnIndex(DataBase.testID));
					i++;
				}
				cursoridt.close();
				sqdb.close();
				sqh.close();
				for (i = 0; i < cursoridt.getCount(); i++) {
					Quest_T = new QuestTask();
					Quest_T.execute(idt[i], Variable.stringresponse_quest);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		protected void onProgressUpdate(Integer... progress) {

		}
	}

	private class QuestTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", params[0]));
			Variable.stringresponse_quest = POSTRequest.POST_Data(
					nameValuePairs, Variable.URL_quest);
			return Variable.stringresponse_quest;
		}

		protected void onPostExecute(String result) {
			myProgress = myProgress + 10;
			pb.setProgress(myProgress);
			try {
				ParseJSON.parseQuest(Variable.stringresponse_quest, context);
				String selection = "imageURL <> ?";
				String[] selectionArgs = new String[] { "none" };
				final Cursor cursor = sqh.getAllQuestData(selection,
						selectionArgs);
				final int length = cursor.getCount();
				final String[] lecnm = new String[length];
				final String[] lecnm1 = new String[length];
				final String[] lecnm2 = new String[length];
				per1 = (int) Math.floor(20 / length);
				int i = 0;
				while (cursor.moveToNext()) {
					// GET COLUMN INDICES + VALUES OF THOSE COLUMNS
					String name = cursor.getString(cursor
							.getColumnIndex(DataBase.imageURL));
					lecnm[i] = name;
					String name1 = cursor.getString(cursor
							.getColumnIndex(DataBase.questID));
					lecnm1[i] = name1;
					String name2 = cursor.getString(cursor
							.getColumnIndex(DataBase.UIDt));
					lecnm2[i] = name2;
					i++;
				}
				cursor.close();
				String gh, ds;
				File checkDir = new File(getFilesDir() + "/mpeiClient/imge");
				if (checkDir.exists()) {
				}
				else {
					checkDir.mkdirs();
				}
				for (int j = 0; j <= length - 1; j++) {
					imagedownloadTask = new ImgDownloadTask(MainActivity.this);
					ds = lecnm2[j] + lecnm1[j];
					gh = lecnm[j];
					File sourceFile = new File(getFilesDir()
							+ "/mpeiClient/imge/img" + ds + ".jpeg");
					if (sourceFile.isFile()) {
						myProgress = (int) (myProgress + per1);
						pb.setProgress(myProgress);
					}
					else {
						flav = true;
						imagedownloadTask.execute(gh, ds);
					}
				}
				sqh.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		protected void onProgressUpdate(Integer... progress) {

		}
	}

	private class LectDownloadTask extends AsyncTask<String, Integer, String> {

		private Context context;

		public LectDownloadTask(Context context) {
			this.context = context;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(String result) {
			myProgress = (int) (myProgress + per);
			pb.setProgress(myProgress);
		}

		@Override
		protected String doInBackground(String... sUrl) {
			String filepath;
			filepath = "sdcard/mpeiClient/lect/lect" + sUrl[1] + ".pdf";
			return File_Server.download(sUrl[0], filepath);
		}
	}

	private class LastLectDownloadTask extends
			AsyncTask<String, Integer, String> {

		private Context context;

		public LastLectDownloadTask(Context context) {
			this.context = context;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(String result) {
			myProgress = (int) (myProgress + per);
			pb.setProgress(myProgress);
			Intent intent = new Intent(MainActivity.this, MenuActivity.class);
			startActivity(intent);
		}

		@Override
		protected String doInBackground(String... sUrl) {
			String filepath;
			filepath = "sdcard/mpeiClient/lect/lect" + sUrl[1] + ".pdf";
			return File_Server.download(sUrl[0], filepath);
		}
	}

	private class ImgDownloadTask extends AsyncTask<String, Integer, String> {

		private Context context;

		public ImgDownloadTask(Context context) {
			this.context = context;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(String result) {
			if (flav == false) {
				myProgress = (int) (myProgress + per1);
				pb.setProgress(myProgress);
			}
		}

		@Override
		protected String doInBackground(String... sUrl) {
			String filepath;
			filepath = getFilesDir() + "/mpeiClient/imge/img" + sUrl[1]
					+ ".jpeg";
			return File_Server.download(sUrl[0], filepath);
		}
	}

	class StatistTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("id", params[0]));
			nameValuePairs.add(new BasicNameValuePair("gr", params[1]));
			Variable.stringresponse_statist = POSTRequest.POST_Data(
					nameValuePairs, Variable.URL_statist);
			return Variable.stringresponse_statist;
		}

		protected void onPostExecute(String result) {

			try {
				SQLiteDatabase sqdb = sqh.getWritableDatabase();
				sqh.dropTable(sqdb, DataBase.STATIST_TABLE);
				sqh.createStatistTable(sqdb);
				ParseJSON
						.parseStatist(Variable.stringresponse_statist, context);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		protected void onProgressUpdate(Integer... progress) {

		}
	}
}
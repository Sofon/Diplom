package com.krabd.klient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TestsActivity extends ListActivity implements OnRefreshListener {

	QuestTask Quest_T;
	TestTask Test_T;
	ImgDownloadTask imagedownloadTask;
	LastImgDownloadTask lastimagedownloadTask;
	ListView lv;
	SwipeRefreshLayout swipeLayout;
	DataBase sqh = new DataBase(this);
	Cursor cursor;

	@Override
	protected void onResume() {
		super.onResume();
		fillData();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getResources().getLayout(R.layout.tests);
		setContentView(R.layout.tests);
		if (checkInternetConnection()) {
			swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
			swipeLayout.setOnRefreshListener(TestsActivity.this);
			registerForContextMenu(getListView());
		}
			fillData();


	}



	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		boolean checkCon;
		checkCon = checkInternetConnection();
		if (checkCon) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Test_T = new TestTask();
					Test_T.execute(Variable.group, Variable.id,
							Variable.stringresponse_test);
				}
			}, 4000);
		}
		else {
			swipeLayout.setRefreshing(false);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					TestsActivity.this);
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

	private boolean checkInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		sqh.close();
		Intent intent = new Intent(TestsActivity.this, MenuActivity.class);
		startActivity(intent);
	}

	private class TestTask extends AsyncTask<String, Integer, String> {
		private String delegate=null;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("gr", params[0]));
			nameValuePairs.add(new BasicNameValuePair("id", params[1]));
			delegate = POSTRequest.POST_Data(
					nameValuePairs, Variable.URL_test);
			return delegate;
		}

		protected void onPostExecute(String result) {
			try {
				SQLiteDatabase sqdb = sqh.getWritableDatabase();
				sqh.dropTable(sqdb, DataBase.TEST_TABLE);
				sqh.createTestTable(sqdb);
				sqh.dropTable(sqdb, DataBase.QUEST_TABLE);
				sqh.createQuestTable(sqdb);
				Context context = TestsActivity.this;
				ParseJSON.parseTest(delegate, context);
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
					//wait(500);
					Quest_T.execute(idt[i], Variable.stringresponse_quest);

				}
				lv.setEnabled(true);
				swipeLayout.setRefreshing(false);
			}
			catch (Exception e) {
				e.printStackTrace();
				swipeLayout.setRefreshing(false);
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
			try {
				Context context = TestsActivity.this;
				ParseJSON.parseQuest(Variable.stringresponse_quest, context);
				String selection = "imageURL <> ?";
				String[] selectionArgs = new String[] { "none" };

				final Cursor cursor = sqh.getAllQuestData(selection,
						selectionArgs);
				final int length = cursor.getCount();
				final String[] lecnm = new String[length];
				final String[] lecnm1 = new String[length];
				final String[] lecnm2 = new String[length];
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
				fillData();
				registerForContextMenu(getListView());
				swipeLayout.setRefreshing(false);
				String gh, ds;
				File checkDir = new File(getFilesDir() + "/mpeiClient/imge");
				if (checkDir.exists()) {
				}
				else {
					checkDir.mkdirs();
				}
				for (int j = 0; j < length - 1; j++) {
					imagedownloadTask = new ImgDownloadTask(TestsActivity.this);
					ds = lecnm2[j] + lecnm1[j];
					gh = lecnm[j];
					File sourceFile = new File(getFilesDir()
							+ "/mpeiClient/imge/img" + ds + ".jpeg");
					if (sourceFile.isFile()) {
					}
					else {
						imagedownloadTask.execute(gh, ds);
					}
				}
				lastimagedownloadTask = new LastImgDownloadTask(
						TestsActivity.this);
				ds = lecnm2[length - 1] + lecnm1[length - 1];
				gh = lecnm[length - 1];
				lastimagedownloadTask.execute(gh, ds);
				sqh.close();
				lv.setEnabled(true);
			}
			catch (Exception e) {
				swipeLayout.setRefreshing(false);
				e.printStackTrace();
			}
		}

		protected void onProgressUpdate(Integer... progress) {

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
			if (result != null)
				Toast.makeText(context, "Download error: " + result,
						Toast.LENGTH_LONG).show();
		}

		@Override
		protected String doInBackground(String... sUrl) {
			String filepath;
			filepath = getFilesDir() + "/mpeiClient/imge/img" + sUrl[1]
					+ ".jpeg";
			return File_Server.download(sUrl[0], filepath);
		}
	}

	private class LastImgDownloadTask extends
			AsyncTask<String, Integer, String> {

		private Context context;

		public LastImgDownloadTask(Context context) {
			this.context = context;
		}

		protected void onProgressUpdate(Integer... progress) {

		}

		protected void onPostExecute(String result) {
			swipeLayout.setRefreshing(false);

		}

		@Override
		protected String doInBackground(String... sUrl) {
			String filepath;
			filepath = getFilesDir() + "/mpeiClient/imge/img" + sUrl[1]
					+ ".jpeg";
			return File_Server.download(sUrl[0], filepath);
		}
	}

	@SuppressWarnings("deprecation")
	public void fillData() {
		DataBase sqh = new DataBase(TestsActivity.this);
		Cursor cursor = sqh.getAllTestData();
		startManagingCursor(cursor);
		String[] from = new String[] { DataBase.testNAME,
				DataBase.testDISC };
		int[] to = new int[] { R.id.lname, R.id.ldisc };
		// Теперь создадим адаптер массива и установим его для отображения наших
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.list_item, cursor, from, to, 0);
		setListAdapter(notes);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Cursor cursor = sqh.getAllTestData();
		int length = cursor.getCount();
		final String[] testid = new String[length];
		int i = 0;
		while (cursor.moveToNext()) {
			String name1 = cursor.getString(cursor
					.getColumnIndex(DataBase.testID));
			testid[i] = name1;
			i++;
		}
		Intent intent = new Intent(TestsActivity.this, TestActivity.class);
		intent.putExtra("num", testid[position]);
		startActivity(intent);
	}
}
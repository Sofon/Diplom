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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

public class LekciiActivity extends ListActivity implements OnRefreshListener {

	SwipeRefreshLayout swipeLayout;
	LectTask Lect_T;
	LectDownloadTask downloadTask;
	LastLectDownloadTask downloadTask1;
	AlertDialog.Builder ad;
	Context context;
	Cursor cursor;
	ListView lv;
	String[] lecnm;
	SimpleCursorAdapter notes;
	int clonum;
	DataBase sqh = new DataBase(this);

	@Override
	protected void onResume() {
		super.onResume();
		fillData();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lects);
		this.getListView();
	    lv = getListView();
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(LekciiActivity.this);
		//swipeLayout.setColorScheme(Color.RED, Color.GREEN, Color.BLUE,
		//		Color.CYAN);
		boolean checkCon;
		checkCon = checkInternetConnection();
		if (checkCon) {
			this.getListView();
			fillData();
		}
	}

	@Override
	public void onRefresh() {
		boolean checkCon;
		checkCon = checkInternetConnection();
		if (checkCon) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Lect_T = new LectTask();
					Lect_T.execute(Variable.group, Variable.stringresponse_lect);
				}
			}, 4000);

		}
		else {
			swipeLayout.setRefreshing(false);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LekciiActivity.this);
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

	public void onBackPressed() {
		sqh.close();
		Intent intent = new Intent(LekciiActivity.this, MenuActivity.class);
		startActivity(intent);
	}

	private class LectTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gr", params[0]));
			Variable.stringresponse_lect = POSTRequest.POST_Data(
					nameValuePairs, Variable.URL_lect);
			return Variable.stringresponse_lect;

		}

		protected void onPostExecute(String result) {
			try {
				SQLiteDatabase sqdb = sqh.getWritableDatabase();
				sqh.dropTable(sqdb, DataBase.LEC_TABLE);
				sqh.createLecTable(sqdb);
				context = LekciiActivity.this;

				ParseJSON.parseLec(Variable.stringresponse_lect, context);
				final Cursor cursor = sqh.getAllLecData();
				final int length = cursor.getCount();
				final String[] lecnm = new String[length];
				int i = 0;
				while (cursor.moveToNext()) {
					// GET COLUMN INDICES + VALUES OF THOSE COLUMNS
					String name = cursor.getString(cursor
							.getColumnIndex(DataBase.lecURL));
					lecnm[i] = name;
					i++;
				}
				cursor.close();
				fillData();
				swipeLayout.setRefreshing(false);
				String gh, ds;
				File checkDir = new File(Environment.getExternalStoragePublicDirectory("DIRECTORY_DOCUMENTS").getPath()+"/mpeiClient/");
				if (checkDir.exists()) {
				}
				else {
					checkDir.mkdirs();
				}
				for (int j = 0; j < length - 1; j++) {
					downloadTask = new LectDownloadTask(LekciiActivity.this);
					ds = Integer.toString(j + 1);
					gh = lecnm[j];
					File sourceFile = new File(Environment.getExternalStoragePublicDirectory("DIRECTORY_DOCUMENTS").getPath()+"/mpeiClient/"
							+ ds + ".pdf");
					if (sourceFile.isFile()) {
					}
					else {
						downloadTask.execute(gh, ds);
					}
				}
				downloadTask1 = new LastLectDownloadTask(LekciiActivity.this);
				ds = Integer.toString(length);
				gh = lecnm[length - 1];
				downloadTask1.execute(gh, ds);
				sqdb.close();
				lv.setEnabled(true);
				sqh.close();

			}
			catch (Exception e) {
				swipeLayout.setRefreshing(false);
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
			if (result != null)
				Toast.makeText(context, "Download error: " + result,
						Toast.LENGTH_LONG).show();
		}

		@Override
		protected String doInBackground(String... sUrl) {
			String filepath;
			filepath = Environment.getExternalStoragePublicDirectory("DIRECTORY_DOCUMENTS").getPath()+"/mpeiClient/" + sUrl[1] + ".pdf";
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
			swipeLayout.setRefreshing(false);
			if (result != null)
				Toast.makeText(context, "Download error: " + result,
						Toast.LENGTH_LONG).show();
			else {

			}
		}

		@Override
		protected String doInBackground(String... sUrl) {
			String filepath;
			filepath = Environment.getExternalStoragePublicDirectory("DIRECTORY_DOCUMENTS").getPath()+"/mpeiClient/" + sUrl[1] + ".pdf";
			return File_Server.download(sUrl[0], filepath);
		}
	}

	@SuppressWarnings("deprecation")
	private void fillData() {
		DataBase sqh = new DataBase(LekciiActivity.this);
		cursor = sqh.getAllLecData();
		startManagingCursor(cursor);
		String[] from = new String[] { DataBase.lecNAME, DataBase.lecDISC };
		int[] to = new int[] { R.id.lname, R.id.ldisc };
		notes = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from,
				to, 0);
		lv.setEnabled(true);
		setListAdapter(notes);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String pos;
		pos = String.valueOf(position + 1);
		File checkDir = new File(Environment.getExternalStoragePublicDirectory("DIRECTORY_DOCUMENTS").getPath()+"/mpeiClient/");
		if (checkDir.exists()) {
			File file = new File(Environment.getExternalStoragePublicDirectory("DIRECTORY_DOCUMENTS").getPath()+"/mpeiClient/" + pos + ".pdf");
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file), "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		}
		else {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LekciiActivity.this);
			builder.setTitle("Ошибка")
					.setMessage("Нет лекции. Попробуйте обновить контент")
					.setCancelable(false)
					.setNegativeButton("ОК",
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
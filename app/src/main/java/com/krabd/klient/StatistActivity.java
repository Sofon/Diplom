package com.krabd.klient;

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
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

public class StatistActivity extends ListActivity implements OnRefreshListener {

	ListView lv;
	DataBase sqh = new DataBase(this);
	Cursor cursor;
	SwipeRefreshLayout swipeLayout;
	SimpleCursorAdapter notes;
	StatistTask Statist_T;
	protected static String[] namet;
	protected static String[] rest;
	SQLiteDatabase sqdb;
	int length;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statist);
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(StatistActivity.this);
		//swipeLayout.setColorScheme(Color.RED, Color.GREEN, Color.BLUE,
		//		Color.CYAN);
		this.getListView();
		sqdb = sqh.getWritableDatabase();
		fillData();
		registerForContextMenu(getListView());
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
					Statist_T = new StatistTask();
					Statist_T.execute(Variable.id, Variable.group,
							Variable.stringresponse_statist);
				}
			}, 4000);
		}
		else {
			swipeLayout.setRefreshing(false);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					StatistActivity.this);
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
				sqh.dropTable(sqdb, DataBase.STATIST_TABLE);
				sqh.createStatistTable(sqdb);
				Context context = StatistActivity.this;
				ParseJSON
						.parseStatist(Variable.stringresponse_statist, context);
				sqdb.close();
				sqh.close();
				fillData();
				swipeLayout.setRefreshing(false);
			}
			catch (Exception e) {
				fillData();
				e.printStackTrace();
			}
		}

		protected void onProgressUpdate(Integer... progress) {

		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {


		sqh.close();
		Intent intent = new Intent(StatistActivity.this, MenuActivity.class);
		startActivity(intent);
	}

	@SuppressWarnings("deprecation")
	private void fillData() {
		DataBase sqh = new DataBase(StatistActivity.this);
		cursor = sqh.getAllStatistData();
		startManagingCursor(cursor);
		String[] from = new String[] { DataBase.statistQUEST,
				DataBase.statistRES };
		int[] to = new int[] { R.id.lname, R.id.ldisc };
		// Теперь создадим адаптер массива и установим его для отображения наших
		// данных
		notes = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from,
				to, 0);
		setListAdapter(notes);
	}
}
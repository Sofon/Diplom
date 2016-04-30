package com.krabd.klient;

import java.io.File;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity {

	private TextView ns;
	Context context;
	DataBase sqh = new DataBase(this);
//	ImageView img;
	String name;
	String surname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		//img = (ImageView) findViewById(R.id.img);
		//ImageLoader imageLoader = ImageLoader.getInstance();
		//imageLoader.init(ImageLoaderConfiguration
		//		.createDefault(getBaseContext()));
		//String sd = "" + getFilesDir() + "/mpeiClient/imge/imgava.jpeg";
		//File sourceFile = new File(sd);
		//if (sourceFile.isFile()) {
		//	sd = "file://" + sd;
		//	imageLoader.displayImage(sd, img);
		//}
		Cursor cursor = sqh.getAllStudData();
		while (cursor.moveToNext()) {
			// GET COLUMN INDICES + VALUES OF THOSE COLUMNS
			Variable.id = cursor.getString(cursor.getColumnIndex(DataBase.studID));
			name = cursor
					.getString(cursor.getColumnIndex(DataBase.studNAME));
			surname = cursor.getString(cursor
					.getColumnIndex(DataBase.studSURN));
			Variable.group = cursor.getString(cursor
					.getColumnIndex(DataBase.studGROUP));
		}
		cursor.close();

		ns = (TextView) findViewById(R.id.about);
		ns.setText("Добро пожаловать "+name +" "+ surname);
	}

	public void onClick1(View v) {
		Intent intent = new Intent(MenuActivity.this, LekciiActivity.class);
		startActivity(intent);
	}
	public void onClick2(View v) {
		Intent intent = new Intent(MenuActivity.this, TestsActivity.class);
		startActivity(intent);
	}

	public void onClick3(View v) {
		Intent intent = new Intent(MenuActivity.this, StatistActivity.class);
		startActivity(intent);
	}
	void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);
		fileOrDirectory.delete();
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Выход");
		alertDialog.setMessage("Вы действительно хотите выйти?");
		alertDialog.setButton("Да", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				deleteDatabase("lec_database.db");
				File sourceFile = new File("sdcard/mpeiClient");
				DeleteRecursive(sourceFile);
				File sourceFile1 = new File(getFilesDir() + "/mpeiClient");
				DeleteRecursive(sourceFile1);
				Intent intent = new Intent(MenuActivity.this,
						MainActivity.class);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("InflateParams")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case R.id.action_settings:
				Intent intent = new Intent(MenuActivity.this,
						SettingsActivity.class);
				startActivity(intent);
				return true;
			case R.id.action_about:
				// Inflate the about message contents
				View messageView = getLayoutInflater().inflate(R.layout.rez, null,
						false);
				// When linking text, force to always use default color. This works
				// around a pressed color state bug.
				TextView textView = (TextView) messageView
						.findViewById(R.id.about_credits);
				int defaultColor = textView.getTextColors().getDefaultColor();
				textView.setTextColor(defaultColor);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle(R.string.app_name);
				builder.setView(messageView);
				builder.create();
				builder.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
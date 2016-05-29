package com.krabd.klient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class OfflineActivity extends Activity {

	private TextView ns;
	Context context;
	DataBase sqh = new DataBase(this);
	String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.offlinemenu);
		ns = (TextView) findViewById(R.id.about);
		ns.setText("Оффлайн режим!");
	}

	public void onClick(View v) {
		Intent intent = new Intent(OfflineActivity.this, LekciiActivity.class);
		startActivity(intent);
	}
	public void onClick1(View v) {
		Intent intent = new Intent(OfflineActivity.this, MenuActivity.class);
		startActivity(intent);
	}
}
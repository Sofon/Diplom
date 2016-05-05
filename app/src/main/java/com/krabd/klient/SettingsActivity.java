package com.krabd.klient;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;

public class SettingsActivity extends Activity {
	Button but;
	DataBase sqh = new DataBase(this);
	AlertDialog.Builder ad;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
	}

	public void onClick(View v) {
		but = (Button)  findViewById(R.id.button);
		but.setEnabled(false);
		context = SettingsActivity.this;
		String title = "Предупреждение";
		String message = "Данная операция удалит все лекции и картинки."
				+ " Вы согласны?";
		String button1String = "Да";
		String button2String = "Нет";
		ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(message);
		ad.setPositiveButton(button1String, new OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {
				File sourceFile = new File(getFilesDir().getPath());
				DeleteRecursive(sourceFile);
				File sourceFile1 = new File(Environment.getExternalStoragePublicDirectory("DIRECTORY_DOCUMENTS").getPath() + "/mpeiClient");
				DeleteRecursive(sourceFile1);
				deleteDatabase("lec_database.db");
				SQLiteDatabase sqdb = sqh.getWritableDatabase();
				sqh.dropTable(sqdb, DataBase.LEC_TABLE);
				sqh.createLecTable(sqdb);
				sqh.dropTable(sqdb, DataBase.TEST_TABLE);
				sqh.createTestTable(sqdb);
				sqh.dropTable(sqdb, DataBase.QUEST_TABLE);
				sqh.createQuestTable(sqdb);
				AlertDialog.Builder builder2 = new AlertDialog.Builder(
						SettingsActivity.this);
				builder2.setTitle("Успех")
						.setMessage("Очистка проведена успешно")
						.setCancelable(false)
						.setNegativeButton("ОК",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
										but.setEnabled(true);
										startActivity(intent);
										dialog.cancel();
									}
								});
				AlertDialog alert2 = builder2.create();
				alert2.show();



			}

		});
		ad.setNegativeButton(button2String, new OnClickListener() {
			public void onClick(DialogInterface dialog, int arg1) {
			}
		});
		ad.setCancelable(true);
		ad.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Toast.makeText(context, "Вы ничего не выбрали",
						Toast.LENGTH_LONG).show();
				but.setEnabled(true);
			}
		});
		ad.show();
	}

        void DeleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				DeleteRecursive(child);
		fileOrDirectory.delete();
	}
}
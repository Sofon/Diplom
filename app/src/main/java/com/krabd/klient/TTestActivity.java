package com.krabd.klient;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class TTestActivity extends TabActivity {

	DataBase sqh = new DataBase(this);
	protected static int[] clo = new int[100];
	protected static String[] type;
	protected static String[] vopr;
	protected static String[] var1;
	protected static String[] var2;
	protected static String[] var3;
	protected static String[] var4;
	protected static String[] quenm;
	protected static String[] hit;
	private EditText an;
	private TextView qu;
	int y;
	private Button btn, rezbtn;
	private RadioButton r1, r2, r3, r4;
	private RadioGroup rad, rad2;
	ImageView img;
	Bitmap bitmap;
	static ImageLoader imageLoader;
	static String put;
	ResTask Res_T;
	static String jsonanswer;
	static String[] answ = new String[100];
	String num;
	String typequest;
	int typeint;
	static int tabnum;
	String aw;
	int length;

	public void hideSoftKeyboard() {
		if(getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		img = (ImageView) findViewById(R.id.img);
		qu = (TextView) findViewById(R.id.quest);
		an = (EditText) findViewById(R.id.answ);
		rad = (RadioGroup) findViewById(R.id.radioGroup1);
		rad2 = (RadioGroup) findViewById(R.id.radioGroup2);
		r1 = (RadioButton) findViewById(R.id.radioButton1);
		r2 = (RadioButton) findViewById(R.id.radioButton2);
		r3 = (RadioButton) findViewById(R.id.radioButton3);
		r4 = (RadioButton) findViewById(R.id.radioButton4);
		btn = (Button) findViewById(R.id.answer);
		rezbtn = (Button) findViewById(R.id.rez);
		for (int o = 0; o < 10; o++) {
			clo[o] = 0;
		}
		num = getIntent().getStringExtra("num");
		String selection = "_idt = ?";
		String[] selectionArgs = new String[] { num };
		put = "file://" + getFilesDir() + "/mpeiClient/imge/img" + num;
		Cursor curstext = sqh.getAllQuestData(selection, selectionArgs);
		length = curstext.getCount();
		vopr = new String[length];
		var1 = new String[length];
		var2 = new String[length];
		var3 = new String[length];
		var4 = new String[length];
		type = new String[length];
		quenm = new String[length];
		hit = new String[length];
		int i = 0;
		while (curstext.moveToNext()) {
			// GET COLUMN INDICES + VALUES OF THOSE COLUMNS
			String name = curstext.getString(curstext
					.getColumnIndex(DataBase.questTEXT));
			vopr[i] = name;
			String name1 = curstext.getString(curstext
					.getColumnIndex(DataBase.VAR1));
			var1[i] = name1;
			String name2 = curstext.getString(curstext
					.getColumnIndex(DataBase.VAR2));
			var2[i] = name2;
			String name3 = curstext.getString(curstext
					.getColumnIndex(DataBase.VAR3));
			var3[i] = name3;
			String name4 = curstext.getString(curstext
					.getColumnIndex(DataBase.VAR4));
			var4[i] = name4;
			String name5 = curstext.getString(curstext
					.getColumnIndex(DataBase.questTYPE));
			type[i] = name5;
			String name6 = curstext.getString(curstext
					.getColumnIndex(DataBase.questID));
			quenm[i] = name6;
			String name7 = curstext.getString(curstext
					.getColumnIndex(DataBase.urlhit));
			hit[i] = name7;
			i++;
		}
		curstext.close();
		int j = i;
		final TabHost tabHost = getTabHost();
		TabHost.TabSpec tabSpec;
		for (i = 1; i <= j + 1; i++) {
			tabSpec = tabHost.newTabSpec("" + i);
			tabSpec.setContent(TabFactory);
			tabSpec.setIndicator("" + i);
			tabHost.addTab(tabSpec);
		}
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(getBaseContext()));
		typequest = type[0];
		typeint = Integer.parseInt(typequest);
		tabnum = 0;
		swtype(typeint, 0, vopr, var1, var2, var3, var4, imageLoader);
		rad.setOnCheckedChangeListener(listener1);
		rad2.setOnCheckedChangeListener(listener2);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				tabnum = Integer.parseInt(tabId) - 1;
				if (tabnum >= length) {
					swtype(5555, 0, null, null, null, null, null, null);
				} else {
					typequest = type[tabnum];
					typeint = Integer.parseInt(typequest);
					qu.setVisibility(View.VISIBLE);
					btn.setVisibility(View.VISIBLE);
					rad.clearCheck();
					rad2.clearCheck();
					if (clo[tabnum] == 1) {
						btn.setEnabled(false);
					} else {
						btn.setEnabled(true);
					}
					swtype(typeint, tabnum, vopr, var1, var2, var3, var4,
							imageLoader);
				}
			}
		});

		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if ((!(an.length()==0)  || aw == "1" || aw == "2" || aw == "3" || aw == "4")) {

					hideSoftKeyboard();
					if (clo[tabHost.getCurrentTab()] == 1) {
						btn.setEnabled(false);
					} else {
						btn.setEnabled(true);
					}

					hideSoftKeyboard();
					clo[tabnum] = 1;

					switch (typeint) {
						case 1:
							answ[tabnum] = aw;
							int y = tabHost.getCurrentTab();
							tabHost.setCurrentTab(y+1);
							aw = "0";
							break;
						case 2:
							answ[tabnum] = an.getText().toString().toLowerCase().trim();
							y = tabHost.getCurrentTab();
							tabHost.setCurrentTab(y+1);
							an.setText("0");
							break;
						case 3:
							answ[tabnum] = aw;
							y = tabHost.getCurrentTab();
							tabHost.setCurrentTab(y+1);
							aw = "0";
							break;
						case 4:
							answ[tabnum] = an.getText().toString().toLowerCase().trim();
							y = tabHost.getCurrentTab();
							tabHost.setCurrentTab(y+1);
							an.setText("0");
							break;


					}
				}
				else
				{   clo[tabnum] = 0;
					AlertDialog.Builder builder7 = new AlertDialog.Builder(
							TTestActivity.this);
					builder7.setTitle("Ошибка")
							.setMessage("Вы нечего не ответили!")
							.setCancelable(false)
							.setNegativeButton("ОК",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert7 = builder7.create();
					alert7.show();
					an.setText("");
				}
			}
		});

		rezbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
                if (!(rezbtn.getText() == "Проверить!")) {
					if (checkInternetConnection()) {
						Intent intent = new Intent(TTestActivity.this, PDFShow.class);
						intent.putExtra("URL", hit[tabnum]);
						startActivity(intent);
					}
					else
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(
								TTestActivity.this);
						builder.setTitle("Ошибка")
								.setMessage("Без подключение к интернету подсказки не работают!")
								.setCancelable(false)
								.setNegativeButton("Попробовать позже",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog, int id) {
												dialog.cancel();
											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					}

				}
				else {
					jsonanswer = "{\"id_ts\":" + num + ",\"answ\":{";
					for (int i = 1; i <= length; i++) {
						if (i == length)
							jsonanswer = jsonanswer + "\"" + i + "\":\""
									+ answ[i - 1] + "\"}}";
						else
							jsonanswer = jsonanswer + "\"" + i + "\":\""
									+ answ[i - 1] + "\",";
					}
					boolean checkCon;
					aw = "0";
					an.setText("0");
					checkCon = checkInternetConnection();
					if (checkCon) {
						rezbtn.setEnabled(false);
						Res_T = new ResTask();
						Res_T.execute(Variable.id, Variable.group, jsonanswer,
								Variable.stringresponse_oneres);
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								TTestActivity.this);
						builder.setTitle("Ошибка")
								.setMessage("Нет подключения к интернету")
								.setCancelable(false)
								.setNegativeButton("Попробовать позже",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog, int id) {
												dialog.cancel();
											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					}
				}
			}
		});
	}

	public void swtype(int v, int t, String[] vopr, String[] var1,
					   String[] var2, String[] var3, String[] var4, ImageLoader imageLoader) {
		switch (v) {
			case 1:
				an.setVisibility(View.INVISIBLE);
				img.setVisibility(View.INVISIBLE);
				rad.setVisibility(View.VISIBLE);
				rad2.setVisibility(View.VISIBLE);
				r1.setVisibility(View.VISIBLE);
				r2.setVisibility(View.VISIBLE);
				r3.setVisibility(View.VISIBLE);
				r4.setVisibility(View.VISIBLE);
				rezbtn.setVisibility(View.VISIBLE);
				rezbtn.setText("Подсказка");
				qu.setText(vopr[t]);
				r1.setText(var1[t]);
				r2.setText(var2[t]);
				r3.setText(var3[t]);
				r4.setText(var4[t]);
				break;
			case 2:
				an.setVisibility(View.VISIBLE);
				img.setVisibility(View.INVISIBLE);
				rad.setVisibility(View.INVISIBLE);
				rad2.setVisibility(View.INVISIBLE);
				r1.setVisibility(View.INVISIBLE);
				r2.setVisibility(View.INVISIBLE);
				r3.setVisibility(View.INVISIBLE);
				r4.setVisibility(View.INVISIBLE);
				rezbtn.setVisibility(View.VISIBLE);
				rezbtn.setText("Подсказка");
				an.setText("");
				qu.setText(vopr[t]);
				break;
			case 3:
				an.setVisibility(View.INVISIBLE);
				rad.setVisibility(View.VISIBLE);
				rad2.setVisibility(View.VISIBLE);
				r1.setVisibility(View.VISIBLE);
				r2.setVisibility(View.VISIBLE);
				r3.setVisibility(View.VISIBLE);
				r4.setVisibility(View.VISIBLE);
				img.setVisibility(View.VISIBLE);
				rezbtn.setVisibility(View.VISIBLE);
				rezbtn.setText("Подсказка");
				qu.setText(vopr[t]);
				r1.setText(var1[t]);
				r2.setText(var2[t]);
				r3.setText(var3[t]);
				r4.setText(var4[t]);
				imageLoader.displayImage(put + quenm[t] + ".jpeg", img);
				break;
			case 4:
				an.setVisibility(View.VISIBLE);
				img.setVisibility(View.VISIBLE);
				rad.setVisibility(View.INVISIBLE);
				rad2.setVisibility(View.INVISIBLE);
				r1.setVisibility(View.INVISIBLE);
				r2.setVisibility(View.INVISIBLE);
				r3.setVisibility(View.INVISIBLE);
				r4.setVisibility(View.INVISIBLE);
				rezbtn.setVisibility(View.VISIBLE);
				rezbtn.setText("Подсказка");
				an.setText("");
				qu.setText(vopr[t]);
				imageLoader.displayImage(put + quenm[t] + ".jpeg", img);
				break;
			case 5555:
				an.setVisibility(View.INVISIBLE);
				img.setVisibility(View.INVISIBLE);
				rad.setVisibility(View.INVISIBLE);
				rad2.setVisibility(View.INVISIBLE);
				r1.setVisibility(View.INVISIBLE);
				r2.setVisibility(View.INVISIBLE);
				r3.setVisibility(View.INVISIBLE);
				r4.setVisibility(View.INVISIBLE);
				btn.setVisibility(View.INVISIBLE);
				qu.setVisibility(View.VISIBLE);
				rezbtn.setVisibility(View.VISIBLE);
				rezbtn.setText("Проверить!");
				qu.setText("Нажмите на кнопку, чтобы завершить тест");
				break;
		}
	}

	private OnCheckedChangeListener listener1 = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
				case R.id.radioButton1:
					rad2.setOnCheckedChangeListener(null);
					rad2.clearCheck();
					rad2.setOnCheckedChangeListener(listener2);
					aw = "1";
					break;
				case R.id.radioButton2:
					rad2.setOnCheckedChangeListener(null);
					rad2.clearCheck();
					rad2.setOnCheckedChangeListener(listener2);
					aw = "2";
					break;
				default:
					break;
			}
		}
	};

	private OnCheckedChangeListener listener2 = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
				case R.id.radioButton3:
					rad.setOnCheckedChangeListener(null);
					rad.clearCheck();
					rad.setOnCheckedChangeListener(listener1);
					aw = "3";
					break;
				case R.id.radioButton4:
					rad.setOnCheckedChangeListener(null);
					rad.clearCheck();
					rad.setOnCheckedChangeListener(listener1);
					aw = "4";
					break;
				default:
					break;
			}
		}
	};

	private class ResTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("id_st", params[0]));
			nameValuePairs.add(new BasicNameValuePair("gr", params[1]));
			nameValuePairs.add(new BasicNameValuePair("res", params[2]));
			Variable.stringresponse_oneres = POSTRequest.POST_Data(
					nameValuePairs, Variable.URL_proverka);
			return Variable.stringresponse_oneres;
		}

		protected void onPostExecute(String result) {
			Context context = TTestActivity.this;
			String qw;
			qw = Variable.stringresponse_oneres.substring(2, 3);
			switch (qw) {
				case "p":
					rezbtn.setBackgroundColor(Color.GRAY);
					String oneres = ParseJSON.parseRes(
							Variable.stringresponse_oneres, context);
					qu.setText("Вы прошли тест на " + oneres + "%!");
					break;
				case "T":
					rezbtn.setBackgroundColor(Color.GRAY);
					AlertDialog.Builder builder2 = new AlertDialog.Builder(
							TTestActivity.this);
					builder2.setTitle("Ошибка!!!")
							.setMessage("Вы проходили этот тест!")
							.setPositiveButton("ОК",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
											Intent intent = new Intent(
													TTestActivity.this,
													TestsActivity.class);
											startActivity(intent);

											dialog.cancel();
										}
									});
					AlertDialog alert2 = builder2.create();
					alert2.show();
					break;
				default:
					rezbtn.setEnabled(true);
					AlertDialog.Builder builder3 = new AlertDialog.Builder(
							TTestActivity.this);
					builder3.setTitle("Ошибка")
							.setMessage("Внутренняя ошибка сервера")
							.setPositiveButton("ОК",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert3 = builder3.create();
					alert3.show();
					break;
			}
		}

		protected void onProgressUpdate(Integer... progress) {

		}
	}

	TabHost.TabContentFactory TabFactory = new TabHost.TabContentFactory() {
		@Override
		public View createTabContent(String tag) {
			TextView tv = new TextView(TTestActivity.this);
			return tv;

		}
	};

	private boolean checkInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null;
	}
}
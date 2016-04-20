package com.krabd.klient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

/*
 * Класс, реализующий процесс регистрации в приложении.
 */
public class RegActivity extends Activity implements OnClickListener {

	/*
	 * Элементы управления на форме. lgn: поле, для ввода логина, под которым хочет зарегистрироваться;
	 * pss: поле для ввода желаемого пароля; pss1: поля для повторного ввода пароля; grp: поле для ввода группы студента;
	 * nm: поле для ввода имени студента; srnm: поле для ввода фамилии студента;
	 * pb: элемент отображения прогресса регистрации;
	 * chb: элемент отображения согласия пользователя на обработку его персональных данных.
	 */
	private EditText lgn, pss, pss1, grp, nm, srnm;
	private Button btn, btnimg;
	private ProgressBar pb;
	private CheckBox chb;

	ProgressDialog dialog = null; //Отображение прогресса загрузки файла с фотографией студента на сервер
	RgTask Rg_T;
	String fileName;
	Bitmap galleryPic = null;
	ImageView image;
	int serverResponseCode = 0;
	String uploadFilePath = "0";
	private static final int PICK_IMAGE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg);
		lgn = (EditText) findViewById(R.id.logi);
		pss = (EditText) findViewById(R.id.pas);
		pss1 = (EditText) findViewById(R.id.pas1);
		grp = (EditText) findViewById(R.id.gr);
		nm = (EditText) findViewById(R.id.nam);
		srnm = (EditText) findViewById(R.id.sur);
		//btnimg = (Button) findViewById(R.id.loadimg);
		btn = (Button) findViewById(R.id.regg);
		pb = (ProgressBar) findViewById(R.id.progressBar);
		chb = (CheckBox) findViewById(R.id.checkBox);
		image = (ImageView) findViewById(R.id.img);
		pb.setVisibility(View.GONE);
		btn.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		btn.setEnabled(false);
		pb.setProgress(20);
		if (pss.getText().toString().equals("")
				|| pss1.getText().toString().equals("")
				|| lgn.getText().toString().equals("")
				|| grp.getText().toString().equals("")
				|| nm.getText().toString().equals("")
				|| srnm.getText().toString().equals("")) {
			btn.setEnabled(true);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					RegActivity.this);
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
		else
		{
			if (pss.getText().toString().equals(pss1.getText().toString())) {
				if (chb.isChecked()) {
					if((pss.getText().length()==0) && (lgn.getText().length()==0))
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(
								RegActivity.this);
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
					boolean checkCon;
					pb.setProgress(40);
					checkCon = checkInternetConnection();
					if (checkCon) {
						if (uploadFilePath.equals("0")) {
							pb.setVisibility(View.VISIBLE);
							Rg_T = new RgTask();
							Rg_T.execute(nm.getText().toString(), srnm
											.getText().toString(), grp.getText()
											.toString(), lgn.getText().toString(), pss
											.getText().toString(),
									Variable.stringresponse_reg);
						}
						else {
							dialog = ProgressDialog.show(RegActivity.this, "",
									"Uploading file...", true);
							new Thread(new Runnable() {
								public void run() {
									fileName = lgn.getText().toString()
											+ ".JPG";
									File sourceFile = new File(uploadFilePath);
									if (!sourceFile.isFile()) {
										dialog.dismiss();
										runOnUiThread(new Runnable() {
											public void run() {
												AlertDialog.Builder builder = new AlertDialog.Builder(
														RegActivity.this);
												builder.setTitle("Ошибка")
														.setMessage(
																"Вы не загрузили фото")
														.setCancelable(false)
														.setNegativeButton(
																"ОК",
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int id) {
																		dialog.cancel();
																	}
																});
												AlertDialog alert = builder
														.create();
												alert.show();
												btn.setEnabled(true);
											}
										});
									}
									else {
										serverResponseCode = File_Server
												.uploadFile(
														Variable.upLoadServerUri,
														sourceFile, fileName);
										if (serverResponseCode == 200) {
											runOnUiThread(new Runnable() {
												public void run() {
													dialog.dismiss();
													pb.setProgress(80);
													pb.setVisibility(View.VISIBLE);
													Rg_T = new RgTask();
													pb.setProgress(100);
													Rg_T.execute(
															nm.getText()
																	.toString(),
															srnm.getText()
																	.toString(),
															grp.getText()
																	.toString(),
															lgn.getText()
																	.toString(),
															pss.getText()
																	.toString(),
															Variable.stringresponse_reg);
													btn.setEnabled(true);
										}
									});
										}
									}
								}
							}).start();
						}
					}
					else {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								RegActivity.this);
						builder.setTitle("Ошибка")
								.setMessage("Нет подключения к интернету")
								.setCancelable(false)
								.setNegativeButton("Попробовать позже",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});
						AlertDialog alert = builder.create();
						alert.show();
						btn.setEnabled(true);
					}
				}
				else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RegActivity.this);
					builder.setTitle("Ошибка")
							.setMessage(
									"Вы не потвердили согласие на обработку Ваших персональных данных")
							.setCancelable(false)
							.setNegativeButton("ОК",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
					btn.setEnabled(true);
				}
			}
			else {
				btn.setEnabled(true);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						RegActivity.this);
				builder.setTitle("Ошибка")
						.setMessage("Пароли не совпадают")
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
				pss.setText("");
				pss1.setText("");
			}
		}
	}

	public void onClick1(View v) {
		selectImageFromGallery();
	}

	public void selectImageFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				PICK_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
			case PICK_IMAGE:
				if (resultCode == RESULT_OK) {
					Uri selectedImage = data.getData();
					try {
						galleryPic = Media.getBitmap(getContentResolver(),
								selectedImage);
					}
					catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					image.setImageBitmap(galleryPic);
					String FilePath = getRealPathFromURI(selectedImage);
					uploadFilePath = FilePath;
				}
				break;
		}
	}

	private String getRealPathFromURI(Uri contentURI) {
		String result;
		Cursor cursor = getContentResolver().query(contentURI, null, null,
				null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
			// path
			result = contentURI.getPath();
		}
		else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

	private class RgTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("us", params[0]));
			nameValuePairs.add(new BasicNameValuePair("sn", params[1]));
			nameValuePairs.add(new BasicNameValuePair("gr", params[2]));
			nameValuePairs.add(new BasicNameValuePair("lg", params[3]));
			nameValuePairs.add(new BasicNameValuePair("pss", params[4]));
			Variable.stringresponse_reg = POSTRequest.POST_Data(nameValuePairs, Variable.URL_reg);
			return Variable.stringresponse_reg;
		}

		protected void onPostExecute(String result) {
			pb.setVisibility(View.GONE);
			switch (Variable.stringresponse_reg) {
				case "NO":
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RegActivity.this);
					builder.setTitle("Ошибка")
							.setMessage("Такого человека нет в группе")
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
					lgn.setText("");
					nm.setText("");
					srnm.setText("");
					grp.setText("");
					pss.setText("");
					pss1.setText("");
					btn.setEnabled(true);
					break;
				case"PassLose":
					AlertDialog.Builder builder4 = new AlertDialog.Builder(
							RegActivity.this);
					builder4.setTitle("Ошибка")
							.setMessage("Пароль должен содержать :" +
									"Минимум 6 символов " +
									"Маленькие и большие буквы латинского алфавита" +
									"А также цифры")
							.setCancelable(false)
							.setNegativeButton("ОК",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert4 = builder4.create();
					alert4.show();
					btn.setEnabled(true);
					break;
				case"LogLose":
					AlertDialog.Builder builder5 = new AlertDialog.Builder(
							RegActivity.this);
					builder5.setTitle("Ошибка")
							.setMessage("Логин слишком короткий или содержит не латинские символы")
							.setCancelable(false)
							.setNegativeButton("ОК",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert5 = builder5.create();
					alert5.show();
					btn.setEnabled(true);
					break;
				case "OK":
					AlertDialog.Builder builder1 = new AlertDialog.Builder(
							RegActivity.this);
					builder1.setTitle("Завершение регистрации")
							.setMessage("Вы успешно зарегистрировались")
							.setCancelable(false)
							.setNegativeButton("ОК",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
											Intent intent = new Intent(
													RegActivity.this,
													MainActivity.class);
											startActivity(intent);
											dialog.cancel();
										}
									});
					AlertDialog alert1 = builder1.create();
					alert1.show();
					btn.setEnabled(true);

					break;
				case "USER_EXIST":
					AlertDialog.Builder builder3 = new AlertDialog.Builder(
							RegActivity.this);
					builder3.setTitle("Ошибка")
							.setMessage("Вы уже зарегистрированы")
							.setCancelable(false)
							.setNegativeButton("ОК",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
															int id) {
											dialog.cancel();
										}
									});
					AlertDialog alert3 = builder3.create();
					alert3.show();
					btn.setEnabled(true);

					break;
				default:
					AlertDialog.Builder builder2 = new AlertDialog.Builder(
							RegActivity.this);
					builder2.setTitle("Ошибка")
							.setMessage("Такой логин уже занят, либо произошла внутренняя ошибка сервера")
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
					btn.setEnabled(true);
					break;
			}
		}

		protected void onProgressUpdate(Integer... progress) {
			pb.setProgress(progress[0]);
		}
	}

	private boolean checkInternetConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null;
	}
}
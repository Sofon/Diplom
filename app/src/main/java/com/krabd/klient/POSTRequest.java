package com.krabd.klient;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/*
 * Класс для отправления запросов к серверу.
 */
public class POSTRequest {

	/*
	 * Метод для отправления запросов к серверу методом POST.
	 * Входные данные: nameValuePairs: Структура пар "ключ", "значение", которые необходимо отправить;
	 * URL: URL на которой необходимо отправить запрос.
	 * Выходные данные: stringresponse: Строка - ответ сервера в формате JSON.
	 */
	public static String POST_Data(List<NameValuePair> nameValuePairs,
								   String URL) {
		String stringresponse = "";
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(URL);
		try {
			// Add your data
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			stringresponse = EntityUtils.toString(entity);
		}
		catch (ClientProtocolException e) {
		}
		catch (IOException e) {
		}
		return stringresponse;
	}
}
package com.krabd.klient;

/*
 * Класс для глобальных переменных.
 */
public class Variable {

	/*
	 * Необходимые URL. URL_stud: URL, на который необходимо отправлять запрос
	 * для получения данных о студенте; URL_lect: URL, на который необходимо
	 * отправлять запрос для получения данных о лекциях; URL_test: URL, на
	 * который необходимо отправлять запрос для получения данных о тестах;
	 * URL_quest: URL, на который необходимо отправлять запрос для получения
	 * данных о вопросах к тестам; URL_statist: URL, на который необходимо
	 * отправлять запрос для получения данных об оценках студента; URL_oneres:
	 * URL, на который необходимо отправлять запрос для получения данных об
	 * оценке за только что пройденный тест; URL_reg: URL, на который необходимо
	 * отправлять запрос на регистрацию; upLoadServerUri: URL, на который
	 * необходимо отправлять фото студента.
	 */
	static String URL_stud = "http://kushelev.ru/action/auth.php";
	static String URL_lect = "http://kushelev.ru/action/lect.php";
	static String URL_test = "http://kushelev.ru/action/tests.php";
	static String URL_quest = "http://kushelev.ru/action/quest.php";
	static String URL_statist = "http://kushelev.ru/action/st_progress.php";
	static String URL_oneres = "http://kushelev.ru/action/answer.php";
	static String URL_proverka = "http://kushelev.ru/action/proverka.php";
	static String URL_reg = "http://kushelev.ru/action/reg.php";
	static String upLoadServerUri = "http://kushelev.ru/action/imageBuffer.php";
	static String URL_avatar = "http://kushelev.ru/action/files/avatars/";

	/*
	 * Ответы сервера в формате JSON. stringresponse_stud: Ответ сервера, на
	 * запрос данных о студенте; stringresponse_lect: Ответ сервера, на запрос
	 * данных о лекциях; stringresponse_test: Ответ сервера, на запрос данных они
	 * тестах; stringresponse_quest: Ответ сервера, на запрос данных о вопросах
	 * к тестам; stringresponse_statist: Ответ сервера, на запрос данных об
	 * оценках студента; stringresponse_oneres: Ответ сервера, на запрос данных
	 * об оценке за только что пройденный тест; stringresponse_reg: Ответ
	 * сервера, на запрос о регистрации.
	 */
	static String stringresponse_stud;
	static String stringresponse_lect;
	static String stringresponse_test;
	static String stringresponse_quest;
	static String stringresponse_statist;
	static String stringresponse_oneres;
	static String stringresponse_reg;

	/*
	 * Данные студента. id: Идентификатор студента на сервере; lgnm: Логин
	 * студента; group: Группа студента; pssw: Пароль студента.
	 */
	static String id;
	static String lgnm;
	static String group;
	static String pssw;
}
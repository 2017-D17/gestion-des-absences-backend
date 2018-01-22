package dev.gda.api.service;

import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Service
public class HttpService {

	private CollabService collabSvc;
	private OkHttpClient.Builder httpClient;

	HttpService() {
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		// set your desired log level
		logging.setLevel(Level.BASIC);
		httpClient = new OkHttpClient.Builder();
		httpClient.addInterceptor(logging);
	}

	public CollabService getCollabService() {
		if (collabSvc == null) {
			Retrofit retrofit = new Retrofit.Builder().baseUrl("https://collab-json-api.herokuapp.com")
					.addConverterFactory(JacksonConverterFactory.create())
					.addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(httpClient.build()).build();
			collabSvc = retrofit.create(CollabService.class);
		}
		return collabSvc;
	}
}
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

	HttpService() {

	}

	public CollabService getService() {
		if (collabSvc == null) {
			HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
			// set your desired log level
			logging.setLevel(Level.BASIC);
			OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
			httpClient.addInterceptor(logging);
			Retrofit retrofit = new Retrofit.Builder().baseUrl("https://collab-json-api.herokuapp.com/")
					.addConverterFactory(JacksonConverterFactory.create())
					.addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(httpClient.build()).build();
			collabSvc = retrofit.create(CollabService.class);
		}
		return collabSvc;
	}
}
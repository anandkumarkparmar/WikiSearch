package in.anandkparmar.wikisearch.network.core;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import in.anandkparmar.wikisearch.network.responses.SearchListResponse;
import in.anandkparmar.wikisearch.utils.AppConstants;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIService {
    private static APIInterface apiInterface;

    private static OkHttpClient getOkHttpClientBuilder() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(AppConstants.CONNECTION_TIMEOUT_IN_MINS, TimeUnit.MINUTES)
                .readTimeout(AppConstants.READ_TIMEOUT_IN_MINS, TimeUnit.MINUTES)
                .writeTimeout(AppConstants.WRITE_TIMEOUT_IN_MINS, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false);

        if (AppConstants.IS_LOGGING_ENABLE) {
            okHttpClientBuilder
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        return okHttpClientBuilder.build();
    }

    private static void providesService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.SERVER_BASE_URL)
                .client(getOkHttpClientBuilder())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiInterface = retrofit.create(APIInterface.class);
    }

    public static void getSearchList(String query, Observer<SearchListResponse> observer) {
        if (apiInterface == null) {
            providesService();
        }

        apiInterface.getSearchList(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

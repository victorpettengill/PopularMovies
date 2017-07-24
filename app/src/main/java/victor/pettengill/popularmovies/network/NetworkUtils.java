package victor.pettengill.popularmovies.network;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by victorfernandes on 04/03/17.
 */

public class NetworkUtils {

    public static String getResponseFromUrl(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
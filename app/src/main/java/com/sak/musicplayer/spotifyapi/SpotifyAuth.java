package com.sak.musicplayer.spotifyapi;

import java.io.IOException;
import java.util.Base64;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SpotifyAuth {

    private static final String CLIENT_ID = "4027ac607f134780abe9225e8dcfe328";
    private static final String CLIENT_SECRET = "94f422f2dc54479789f9a33edcd7a293";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";

    public static void getAccessToken(final AccessTokenCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Prepare Basic Auth
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

        // Prepare the request body
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(body)
                .addHeader("Authorization", basicAuth)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    // Parse the response to get the access token
                    String responseBody = response.body().string();
                    String accessToken = parseAccessToken(responseBody);
                    callback.onSuccess(accessToken);
                } else {
                    // Handle unsuccessful response
                    callback.onError(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    private static String parseAccessToken(String json) {
        // Simple parsing logic (consider using a JSON library like Gson or Moshi for production code)
        int startIndex = json.indexOf("access_token\":\"") + 15; // Length of the token key
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }

    // Callback interface for accessing the token
    public interface AccessTokenCallback {
        void onSuccess(String accessToken);
        void onError(Exception e);
    }
}

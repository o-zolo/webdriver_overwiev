import com.google.gson.Gson;
import com.softserve.academy.GreenCityLogin;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HttpTest {



    @Test
    public void checkGreenCity() {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();
        RequestBody body;
        Request request;
        Response response;
        GreenCityLogin greenCityLogin;
        String resultJson;
        String token;

        //Login

        String jsonBody = new StringBuilder()
                .append("{")
                .append("\"email\":\"box.zolotar@gmail.com\",")
                .append("\"password\":\"Qwerty1234!\"")
                .append("}").toString();
        System.out.println("JSON body -> " + jsonBody);
        body = RequestBody.create(jsonBody,
                MediaType.parse("application/json; charset=utf-8"));
        request = new Request.Builder()
                .url("https://greencity-user.greencity.social/ownSecurity/signIn")
                .post(body)
                .build();
        System.out.println("Request -> " + request);
        try {
            response = client.newCall(request).execute();
            resultJson = response.body().string();
            greenCityLogin = gson.fromJson(resultJson, GreenCityLogin.class);
            token = greenCityLogin.getAccessToken();
            System.out.println("Token -> " + token);
            Assertions.assertTrue(response.isSuccessful());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    //get all active comments
    @Test
    public void GreenCityComments() throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://greencity.greencity.social/econews/comments/active").newBuilder();
        urlBuilder.addQueryParameter("ecoNewsId", "1246");
        urlBuilder.addQueryParameter("page", "0");
        urlBuilder.addQueryParameter("size", "0");
        String url = urlBuilder.build().toString();
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("Accept", "*/*")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        String resultJson = response.body().string();
        System.out.println("Result JSON: " + resultJson);
    }
}
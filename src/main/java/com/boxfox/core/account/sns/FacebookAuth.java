package com.boxfox.core.account.sns;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FacebookAuth {
    private static final String myAppId = "XXX";
    private static final String myAppSecret = "XXX";
    private static final String redirectURI = "http://localhost:8080/";

    private void test(){
        String token = "";
        String url = "https://graph.facebook.com/me?access_token=" + token;
        /*request(path, function (error, response, body) {
            var data = JSON.parse(body);
            if (!error && response && response.statusCode && response.statusCode == 200) {
                var user = {
                        facebookUserId: data.id,
                        username: data.username,
                        firstName: data.first_name,
                        lastName: data.last_name,
                        email: data.email
			};
                deferred.resolve(user);
            }
            else {
                console.log(data.error);
                //console.log(response);
                deferred.reject({code: response.statusCode, message: data.error.message});
            }
        });*/
    }
}

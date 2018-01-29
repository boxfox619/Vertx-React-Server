package com.boxfox.core.account.sns;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import org.json.JSONObject;

public class FacebookAuth {

    public static SNSAuthDTO verification(String token) {
        SNSAuthDTO result = null;
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://graph.facebook.com/me")
                    .queryString("access_token", token)
                    .asJson();
            if (response.getStatus() == HttpStatus.SC_OK) {
                JSONObject data = response.getBody().getObject();
                String facebookUserId = data.getString("id");
                String username = data.getString("username");
                String firstName = data.getString("first_name");
                String lastName = data.getString("last_name");
                String email = data.getString("email");
                result = new SNSAuthDTO(email, username);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return result;
    }
}

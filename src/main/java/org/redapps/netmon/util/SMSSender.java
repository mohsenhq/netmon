package org.redapps.netmon.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SMSSender {
    private static final Logger logger = LoggerFactory.getLogger(SMSSender.class);

    private static String username;
    private static String password;
    private static String authenticateURL;
    private static String sendURL;

    /*
     * Read sms configuration from properties file.
    */
    @Value("${sms.Username}")
    public void setUsername(String value) {
        username = value;
    }

    @Value("${sms.Password}")
    public void setPassword(String value) {
        password = value;
    }

    @Value("${sms.AuthenticateURL}")
    public void setAuthenticateURL(String value) {
        authenticateURL = value;
    }

    @Value("${sms.SendURL}")
    public void setSendURL(String value) {
        sendURL = value;
    }

    private String signin() throws java.io.IOException{
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);

        String token = null;

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(authenticateURL);
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            String ResultString = EntityUtils.toString(response.getEntity());
            logger.info(ResultString);

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(ResultString);

            JSONObject jsonObject = (JSONObject) obj;

            token = jsonObject.get("token").toString();
            logger.info(token);

        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return token;
    }

    /**
     *  Sending a sms to mobile number.
     * @param msg the messae to send
     * @param mobileNumber
     * @throws java.io.IOException
     */
    private void sendSMS(String msg, String mobileNumber) throws java.io.IOException {
        String token = signin();
        JSONObject json = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(mobileNumber);


        json.put("Mobile", jsonArray);
        json.put("Message", msg);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(sendURL);
            StringEntity params = new StringEntity(json.toString());
            request.addHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            request.addHeader("cache-control", "no-cache");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            logger.info(request.toString());
            String ResultString = EntityUtils.toString(response.getEntity());

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(ResultString);

            JSONObject jsonObject = (JSONObject) obj;
            logger.info(jsonObject.toString());

        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    /**
     * This method get information of sms and email, then call send method.
     * @param mobileNumber
     * @param emailAddress
     * @param msg the body of sms or email
     * @param smsIsEnabled sending sms or not
     * @param emailIsEnabled sending email or not
     * @return OK or error description
     */
    public static String sendMessage(String mobileNumber, String emailAddress, String msg,
                                     boolean smsIsEnabled, boolean emailIsEnabled){
        if (smsIsEnabled) {
            try {
                SMSSender smsSender = new SMSSender();
                smsSender.sendSMS(msg, mobileNumber);
            } catch (Exception ex) {
                logger.error("The SMS was not sent.");
                logger.error("Error message: " + ex.getMessage());
                return ex.getMessage();
            }
        }
        else {
            logger.info("Sending SMS is Disabled");
        }

        if (emailIsEnabled) {
            try {
                EmailSender emailSender = new EmailSender();
                emailSender.sendMail(msg, emailAddress);
            } catch (Exception ex) {
                logger.error("The email was not sent.");
                logger.error("Error message: " + ex.getMessage());
                return ex.getMessage();
            }
        } else {
            logger.info("Sending Email is Disabled");
        }

        return "OK";
    }
}

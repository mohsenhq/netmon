package org.redapps.netmon.util;

import com.google.gson.Gson;
import org.redapps.netmon.payload.CallAPIBillingRequest;
import org.redapps.netmon.payload.UpdateBillingOrderIdResponse;
import org.redapps.netmon.payload.UpdateBillingReferenceIdResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class PaymentStatusService {

    private static String url;

    /*
     * Read payment configuration from properties file.
     */
    @Value("${payment.url}")
    public void setUrl(String value) {
        url = value;
    }

    /**
     * Call payment service and get order id.
     * @param callAPIBillingRequest the api information object to pay
     * @return order id and message
     */
    public static UpdateBillingOrderIdResponse callBillingAPIGetOrderID(
            CallAPIBillingRequest callAPIBillingRequest) {

        try{
            String response = callBillingAPI(callAPIBillingRequest);
            Gson gson = new Gson();

            return gson.fromJson(response, UpdateBillingOrderIdResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
            return  new UpdateBillingOrderIdResponse(-1, e.toString(), -1);
        }
    }

    /**
     * Call payment service and get order id.
     * @param callAPIBillingRequest the api information object to pay
     * @return reference id and message
     */
    public static UpdateBillingReferenceIdResponse callBillingAPIGetReferenceID(
            CallAPIBillingRequest callAPIBillingRequest) {

        try{
            String response = callBillingAPI(callAPIBillingRequest);
            Gson gson = new Gson();
            return gson.fromJson(response, UpdateBillingReferenceIdResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
            return  new UpdateBillingReferenceIdResponse(-1, e.toString(), -1);
        }
    }

    /**
     * Prepare message body and cal payment api
     * @param callAPIBillingRequest the api information object to pay
     * @return result message
     */
    private static String callBillingAPI(CallAPIBillingRequest callAPIBillingRequest) {
        RestTemplate restTemplate = new RestTemplate();
        Gson gson = new Gson();
        String stringJson = "'"+gson.toJson(callAPIBillingRequest)+"'";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new  HttpEntity<>(stringJson, headers);


        String result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
        return result.substring(1, result.length()-1).replace("\\", "");
    }
}

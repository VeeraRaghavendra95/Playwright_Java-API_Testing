package com.qa.api.tests;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class GETapiCall {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;

    @BeforeTest
    public void setup(){

        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();

    }


    @Test
    public void getUsersApiTest() throws IOException {

        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");

       int statusCode = apiResponse.status();
       System.out.println("Response Status Code is : " + statusCode);
       //validating the statusCode
       Assert.assertEquals(statusCode, 200);
       Assert.assertEquals(apiResponse.ok(), true);

       String statusResText = apiResponse.statusText();
       System.out.println("Response Status Text is : " + statusResText);

        System.out.println(" ----- Print api response with plain text ----- ");
        System.out.println(apiResponse.text());


       //If we use the below method we are getting the json response body in the form of byte[]
        // So to get proper json response body in Playwright we use 3rdparty API - Jackson Api
       // Need to use inbuilt ObjectMapper class, readTree method from Jackson Api to get proper json Response body

        apiResponse.body();
        System.out.println(" ----- Print Api json Response ----- ");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(apiResponse.body());
        String expectedjsonResponse = jsonResponse.toPrettyString();
        System.out.println(expectedjsonResponse);

        System.out.println(" ----- Print Api Url ----- ");
        System.out.println(apiResponse.url());

        System.out.println(" ----- Print response headers ----- ");
        Map<String,String> headersMap =  apiResponse.headers();
        System.out.println(headersMap);
        // validating  headers
        Assert.assertEquals(headersMap.get("content-type"), "application/json; charset=utf-8");
        Assert.assertEquals(headersMap.get("x-download-options"), "noopen");

    }

    // Test for getting specificUser with QueryParam
    @Test
    public void getSpecificUserApiTest(){
        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setQueryParam("gender", "male")
                        .setQueryParam("status","active")
        );

        int statusCode = apiResponse.status();
        System.out.println("Response Status Code is : " + statusCode);

        String statusResText = apiResponse.statusText();
        System.out.println("Response Status Text is : " + statusResText);

        System.out.println(" ----- Print api response with plain text ----- ");
        System.out.println(apiResponse.text());

    }


    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}








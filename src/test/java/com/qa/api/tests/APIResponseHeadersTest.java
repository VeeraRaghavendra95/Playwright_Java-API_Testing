package com.qa.api.tests;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.HttpHeader;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class APIResponseHeadersTest {

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
    public void getHeadersTest(){

        APIResponse apiResponse = requestContext.get("https://gorest.co.in/public/v2/users");

        int statusCode = apiResponse.status();
        System.out.println("Response Status Code is : " + statusCode);
        //validating the statusCode
        Assert.assertEquals(statusCode, 200);

        // Using Map
        Map<String, String> headeraMap = apiResponse.headers();
        headeraMap.forEach((k,v) -> System.out.println(k + ":" + v));

        System.out.println("Total Response Headers : " + headeraMap.size());

        Assert.assertEquals(headeraMap.get("server"), "cloudflare");
        Assert.assertEquals(headeraMap.get("content-type"), "application/json; charset=utf-8");

        System.out.println("--------------------------------------------------------------");

        //Using List
        List<HttpHeader> headersList =  apiResponse.headersArray();
        for(HttpHeader e : headersList){
            System.out.println(e.name + " : " + e.value);
        }



    }




    @AfterTest
    public void tearDown(){
        playwright.close();
    }
}

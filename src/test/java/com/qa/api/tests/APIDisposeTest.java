package com.qa.api.tests;
import com.microsoft.playwright.*;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;



// Disposes the body of this response. If not called then body will stay in memory until the context closes.

public class APIDisposeTest {

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
    public void disposeResponseTest(){

        // Request-1
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

        //Dispose method will dispose only response body but status code, url, status text will remain same.

        apiResponse.dispose();
        System.out.println(" ----- Print api response after dispose method with plain text ----- ");

        try {
            System.out.println(apiResponse.text());
        } catch (PlaywrightException e) {
            System.out.println("Api response body is disposed");
        }


        int statusCode1 = apiResponse.status();
        System.out.println("Response Status Code after dispose method is : " + statusCode1);

        String statusResText1 = apiResponse.statusText();
        System.out.println("Response Status Text is : " + statusResText1);

        System.out.println("Response url : " + apiResponse.url());

        //Request-2

        APIResponse apiResponse2 = requestContext.get("https://reqres.in/api/users/2");

        System.out.println("Status code of 2nd request is : " + apiResponse2.status());
        System.out.println("Response body of 2nd request is : " + apiResponse2.text());

        requestContext.dispose();

        System.out.println("Response2 body : " + apiResponse2.text());


    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }

}

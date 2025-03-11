package com.qa.api.tests;
import com.api.data.POJOUser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class CreateUserPOJOTestPostCall {

    Playwright playwright;
    APIRequest request;
    APIRequestContext requestContext;
    static String emailId;

    @BeforeTest
    public void setup(){

        playwright = Playwright.create();
        request = playwright.request();
        requestContext = request.newContext();

    }

    public String getRandomEmail(){
        emailId = "TestPW" + System.currentTimeMillis() + "@gmail.com";
        return emailId;
    }

    @Test
    public void createUserTest() throws IOException {

        POJOUser user = new POJOUser("Veera", getRandomEmail(),"male","active");

        // POST Call : Creating a User
        APIResponse apiPostResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization","Bearer 4a506f0d5bdb7c94dc825914263c8ec2c04f5422eb5418e51ae5bfeaf1b6643b")
                        .setData(user)
        );

        System.out.println("API Response Status : " + apiPostResponse.status());
        Assert.assertEquals(apiPostResponse.status(),201);
        Assert.assertEquals(apiPostResponse.statusText(), "Created");

        String responseText =  apiPostResponse.text();
        System.out.println(responseText);

        //Convert the text/json to POJO --- Deserialization
        ObjectMapper objectMapper = new ObjectMapper();
        POJOUser actUser = objectMapper.readValue(responseText, POJOUser.class);

        System.out.println(" ----- Actual User data from the resonse ----- ");
        System.out.println(actUser);

        System.out.println(actUser.getEmail());

        Assert.assertEquals(actUser.getName(), user.getName());
        Assert.assertEquals(actUser.getGender(), user.getGender());
        Assert.assertEquals(actUser.getStatus(), user.getStatus());


    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }


}

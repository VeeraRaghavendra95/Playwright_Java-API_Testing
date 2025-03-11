package com.qa.api.tests;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CreateUserWithJsonFileTest {

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

        // Get json File

        File file = new File("./src/test/Data/user.json");
        byte[] fileBytes =  Files.readAllBytes(file.toPath());


        // POST Call : Creating a User
        APIResponse apiPostResponse = requestContext.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization","Bearer 4a506f0d5bdb7c94dc825914263c8ec2c04f5422eb5418e51ae5bfeaf1b6643b")
                        .setData(fileBytes)
        );

        System.out.println("API Response Status : " + apiPostResponse.status());
        Assert.assertEquals(apiPostResponse.status(),201);
        Assert.assertEquals(apiPostResponse.statusText(), "Created");

        System.out.println(apiPostResponse.text());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResponse = objectMapper.readTree(apiPostResponse.body());
        System.out.println(postJsonResponse.toPrettyString());

        //Capturing an id from the post json response to use it in the get call
        String userId = postJsonResponse.get("id").asText();
        System.out.println("User Id is : " + userId);

        //GET Call : Fetch the same user by id
        APIResponse apiGetResponse =
                requestContext.get("https://gorest.co.in/public/v2/users/" + userId,
                        RequestOptions.create()
                                .setHeader("Authorization","Bearer 4a506f0d5bdb7c94dc825914263c8ec2c04f5422eb5418e51ae5bfeaf1b6643b")
                );

        Assert.assertEquals(apiGetResponse.status(), 200);
        Assert.assertEquals(apiGetResponse.statusText(),"OK");

        System.out.println(apiGetResponse.text());

        Assert.assertTrue(apiGetResponse.text().contains(userId));
        Assert.assertTrue(apiGetResponse.text().contains("Veera"));


    }

    @AfterTest
    public void tearDown(){
        playwright.close();
    }

}

package step.definitions;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;

import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import utils.LogHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RestAPIPostSteps {
    private final String BASE_URL = "https://reqres.in/api";
    private static final Logger LOGGER = LogHelper.getLogger(RestAPIPostSteps.class);

    private Response response;
    private Scenario scenario;

    @Before
    public void before(Scenario scenario){
        this.scenario = scenario;
    }

    @Given("Get call to {string}")
    public void getCallToUrl(String url) throws URISyntaxException {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification requestSpecification = RestAssured.given();
        response = requestSpecification.when().get(new URI(url));
    }

    @Then("Response is {string}")
    public void responseIsStatusCode(String statusCode){
        LOGGER.log(Level.INFO, "Getting Status Code");
        LOGGER.log(Level.WARNING, "Getting Status Code");
        LOGGER.log(Level.SEVERE, "Getting Status Code");
        int actualResponseCode = response.then().extract().statusCode();
        Assert.assertEquals(statusCode,actualResponseCode+"");
    }

    @And("Response email is {string}, first name is {string} and last name is {string}")
    public void responseEmailIsFirstNameIsAndLastNameIs(String email, String first_name, String last_name) {
        String actualEmail = response.then().extract().path("data.email");
        String actualFirstName = response.then().extract().path("data.first_name");
        String actualLastName = response.then().extract().path("data.last_name");

        LOGGER.log(Level.INFO,"\n Actual email: "+actualEmail+"\n"+"Actual First Name: "+actualFirstName+"\n"+"Actual Last Name:"+ actualLastName);

        Assert.assertEquals(email, actualEmail);
        Assert.assertEquals(first_name, actualFirstName);
        Assert.assertEquals(last_name, actualLastName);
    }

    @And("{string} is on the list")
    public void isOnTheList(String name) {
        ArrayList dataResponse = response.then().extract().path("data.name");
        LOGGER.log(Level.INFO, "dataResponse: "+dataResponse);

        Assert.assertTrue(dataResponse.contains(name));

    }

    @And("Matches the schema {string}")
    public void matchesTheSchema(String file) {
        LOGGER.log(Level.INFO,"Schema: "+file);
        response.then().assertThat().body(matchesJsonSchemaInClasspath(file));
    }
}

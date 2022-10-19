package step.definitions;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

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

    private String credentials;
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

    @Given("Get Delete to {string}")
    public void getDeleteTo(String url) throws URISyntaxException {
        LOGGER.log(Level.INFO,"Delete request");
        RestAssured.baseURI = BASE_URL;
        RequestSpecification requestSpecification = RestAssured.given();
        response = requestSpecification.when().delete(new URI(url));
    }

    @And("Response body is empty")
    public void resposeBodyIsEmpty() {
        LOGGER.log(Level.INFO,"Validating empty body");
        String responseStr = response.then().extract().asString();

        Assert.assertEquals(responseStr, "");
    }

    @Given("Credentials {string} and {string}")
    public String credentialsAnd(String email, String password) {
        RestAssured.baseURI = BASE_URL;
        //Map<String, String> credentials = new HashMap<>();
        //credentials.put("email", email);
        //credentials.put("password", password);
        String credentials = "{ \"email\":\""  +  email  +  "\", \"password\":\""  +  password  +  "\"}";
        this.credentials = credentials;
        LOGGER.log(Level.INFO, "Credentials => "+ this.credentials);
        return credentials;
    }


    @When("Post {string}")
    public void post(String uri) throws URISyntaxException {
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(this.credentials).log().all();

        response = requestSpecification.when().post(new URI(uri));
    }


    @And("Response has token on body")
    public void responseHasTokenOnBody() {
        String body  = response.getBody().asString();
        
        LOGGER.log(Level.INFO,"BODY :" + body);
        Assert.assertTrue(body.contains("token"));
    }


}

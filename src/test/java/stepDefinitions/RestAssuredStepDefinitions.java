package stepDefinitions;

import implementation.RestAssuredImplementation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RestAssuredStepDefinitions {

    @Given("^post user data using POST request$")
    public static void post(){
        RestAssuredImplementation.post();
    }
    @Then("^update user data with authentication using PATCH request$")
    public static void patch(){
        RestAssuredImplementation.patch();
    }
    @Then("^update user data with authentication using PUT request$")
    public static void put(){
        RestAssuredImplementation.put();
    }
    @When("^deleting user data with authentication using DELETE request$")
    public static void delete(){
        RestAssuredImplementation.delete();
    }
    @Then("^validate whether data deleted or not using GET request$")
    public static void validateDelete(){
        RestAssuredImplementation.validateDelete();
    }

}
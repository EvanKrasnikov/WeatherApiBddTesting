package steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import mappings.WeatherApiMappingLoader;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class WeatherApiTestSteps {

    private final static WireMockServer wiremock = new WireMockServer(options().dynamicPort());

    private RequestSpecification specification;
    private Response response;

    @BeforeAll
    public static void before_all() {
        wiremock.start();
        WireMock.configureFor(wiremock.port());

        wiremock.loadMappingsUsing(new WeatherApiMappingLoader());
    }

    @AfterAll
    static void afterAll() {
        wiremock.shutdown();
    }


    @Step
    @Given("user prepares request specification")
    public void user_prepares_request_specification() {
        specification = new RequestSpecBuilder()
                .setBaseUri(wiremock.baseUrl())
                .setPort(wiremock.port())
                .setBasePath("/v1")
                .build();
    }

    @Step
    @Given("query parameters {string} and {string}")
    public void queryParameters(String q, String key) {
        specification.queryParam("q", q)
                .queryParam("key", key);
    }

    @Step
    @When("user sends GET request")
    public void user_sends_get_request() {
        response = given(specification).get("/current.json");
    }

    @Step
    @Then("response status code should be {int}")
    public void response_status_code_should_be(Integer statusCode) {
        assertThat(response.getStatusCode())
                .as("Unexpected response status code")
                .isEqualTo(statusCode);
    }

    @Step
    @Then("response body should contain {string}")
    public void response_body_should_contain_city_name(String cityName) {
        assertThat(response.jsonPath().getString("location.name"))
                .as("Unexpected city name")
                .isEqualTo(cityName);
    }

    @Step
    @Then("response body should contain current weather information")
    public void response_body_should_contain_current_weather_information() {
        assertThatNoException()
                .as("No current weather was found")
                .isThrownBy(() -> response.jsonPath().getString("current"));
    }

    @Step
    @Then("response body should contain error code {int}")
    public void response_body_should_contain_error_code(Integer errorCode) {
        assertThat(response.jsonPath().getInt("error.code"))
                .as("Unexpected error code")
                .isEqualTo(errorCode);
    }

    @Step
    @Then("response body should contain error message")
    public void response_body_should_contain_error_message(String errorMessage) {
        assertThat(response.jsonPath().getString("error.message"))
                .as("Unexpected error message")
                .isEqualTo(errorMessage);
    }

    @Step
    @Given("query parameter q is {string}")
    public void query_parameter_q_is(String q) {
        specification.queryParam("q", q);
    }

    @Step
    @Given("query parameter key is {string}")
    public void query_parameter_key_is(String key) {
        specification.queryParam("key", key);
    }

    @Step
    @Given("invalid json body")
    public void invalid_json_body(String json) {
        specification.body(json.stripIndent())
                .header("Content-type", "application/json");
    }

    @Step
    @When("user sends POST request")
    public void user_sends_post_request() {
        response = given(specification).post("/current.json");
    }

}

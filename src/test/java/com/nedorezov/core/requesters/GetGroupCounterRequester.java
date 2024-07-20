package com.nedorezov.core.requesters;

import com.nedorezov.core.config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.AllArgsConstructor;

import java.util.Map;

import static io.restassured.RestAssured.given;

@AllArgsConstructor
public class GetGroupCounterRequester {

    private static final Method httpMethod = Method.GET;
    private final Config config;
    private final Map<String, String> pathParams;

    /***
     * метод для отправки запроса
     * @return Response
     */
    public Response sendRequest() {
        return given()
                .spec(buildRequestSpec())
                .request(String.valueOf(httpMethod), config.getUrl())
                .then()
                .spec(buildResponseSpec())
                .extract()
                .response();
    }

    /***
     * Метод который конфигурирует RequestSpecification
     * @return RequestSpecification
     */
    private RequestSpecification buildRequestSpec() {
        return new RequestSpecBuilder()
                .log(LogDetail.ALL)
                .addHeader("Content-Type", "application/json")
                .addQueryParams(pathParams)
                .addQueryParam("application_key", config.getApplicationKey())
                .addQueryParam("access_token", config.getAccessToken())
                .addQueryParam("sig", config.getSig())
                .build();
    }

    /***
     * Метод который конфигурирует ResponseSpecification
     * @return ResponseSpecification
     */
    private ResponseSpecification buildResponseSpec() {
        return new ResponseSpecBuilder()
                .log(LogDetail.BODY).build();

    }
}

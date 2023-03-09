package apiTest;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import static io.restassured.RestAssured.given;

public class tesData {
    @Test
    public void postFailUser(){
        RestAssured.baseURI = "https://reqres.in/";
        int name = 5;
        int gender = 3;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",name);
        jsonObject.put("gender",gender);

        given().log().all()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .body(jsonObject)
                .post("api/users")
                .then()
                .assertThat().statusCode(201)
                .assertThat().body("name",Matchers.equalTo(name))
                .assertThat().body("gender",Matchers.equalTo(gender))
                .assertThat().body("$",Matchers.hasKey("id"))
                .assertThat().body("$",Matchers.hasKey("createdAt"));
    }
    @Test
    public void getUser(){
    RestAssured.baseURI = "https://reqres.in/";
    given().when().get("api/users?page=2")
            .then()
            .log().all()
            .assertThat().statusCode(200)
            .assertThat().body("total", Matchers.equalTo(12))
            .assertThat().body("data.email", Matchers.hasSize(6));


    }
    @Test
    public void postUser(){
        RestAssured.baseURI = "https://reqres.in/";
        String name = "Siti";
        String gender = "woman";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",name);
        jsonObject.put("gender",gender);

        given().log().all()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .body(jsonObject.toString())
                .post("api/users")
                .then()
                .assertThat().statusCode(201)
                .assertThat().body("name",Matchers.equalTo(name))
                .assertThat().body("gender",Matchers.equalTo(gender))
                .assertThat().body("$",Matchers.hasKey("id"))
                .assertThat().body("$",Matchers.hasKey("createdAt"));
    }
    @Test
    public void putUser(){
        RestAssured.baseURI = "https://reqres.in/";
        int userId = 5;
        String newName = "jhosua";
        String fname = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.first_name");
        String lname = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.last_name");
        String avatar = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.avatar");
        String email = given().when().get("api/users/"+userId).getBody().jsonPath().get("data.email");
        System.out.println("name before = "+lname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", userId);
        bodyMap.put("email", email);
        bodyMap.put("first_name", fname);
        bodyMap.put("last_name", newName);
        bodyMap.put("avatar", avatar);
        JSONObject jsonObject = new JSONObject(bodyMap);

        given().log().all()
                .header("Content-Type","application/json")
                .body(jsonObject.toString())
                .put("api/users/"+userId)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("last_name", Matchers.equalTo(newName));
    }
    @Test
    public void patchUser(){
        RestAssured.baseURI = "https://reqres.in/";
        int userId = 5;
        String newName = "nurbaidah";
        String lname = given().when().get("api/users/"+userId)
                .getBody().jsonPath().get("data.last_name");
        System.out.println("name before = "+lname);

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("last_name", newName);
        JSONObject jsonObject = new JSONObject(bodyMap);

        given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("api/users/"+userId)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("last_name", Matchers.equalTo(newName));
    }
    @Test
    public void deleteUser(){
        RestAssured.baseURI = "https://reqres.in/";
        int userDelete = 5;
        given().log().all()
                .when().delete("api/users/"+userDelete)
                .then()
                .log().all()
                .assertThat().statusCode(204);
    }
    @Test
    public void validateSchemaSingle(){
        RestAssured.baseURI = "https://reqres.in/";
        int userGet = 5;
        File file = new File("src/test/resources/JsonSchema/GetSingleUserSchema.json");
        given().log().all()
                .when().get("api/users/"+userGet)
                .then().log().all()
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(file));
    }

    @Test
    public void getUserFail(){
        RestAssured.baseURI = "https://reqres.in/";
        given().when().get("api/unknown/23")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .assertThat().body("total", Matchers.equalTo(12))
                .assertThat().body("data.email", Matchers.hasSize(6));
    }
}

package apiauto;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import org.json.JSONObject;
import static io.restassured.RestAssured.given;
import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestReqres {
    @Test
    public void testGetListUserspositive() {

        File jsonSchema = new File("src/test/resources/jsonSchema/getListUsersSchema.json");

        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
    }

    @Test
    public void testGetSingleUserBorder1() {

        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users/1") //memilih user paling awal
                .then().log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void testGetSingleUserBorder12() {

        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users/12") //memilih user paling akhir
                .then().log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void testGetSingleUserBorder13Negatif() {

        RestAssured
                .given()
                .when()
                .get("https://reqres.in/api/users/13") //memilih user diatas 12
                .then().log().all()
                .assertThat().statusCode(404);
    }

    @Test
    public void testPostCreateUser() {
        String valueName = "Habibi";
        String valueJob = "pengangguran";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("name", valueName);
        bodyObj.put("job", valueJob);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(valueName));
    }

    @Test
    public void testPostRegisterPositif() {
        String valueEmail = "eve.holt@reqres.in";
        String valuePassword = "pistol";

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("email", valueEmail);
        bodyObj.put("password", valuePassword);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://reqres.in/api/register")
                .then().log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void testPostRegisterNegatif() {
        String valueEmail = "eve.holt@reqres.in";
        String valuePassword = ""; //tanpa password

        JSONObject bodyObj = new JSONObject();

        bodyObj.put("email", valueEmail);
        bodyObj.put("password", valuePassword);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(bodyObj.toString())
                .when()
                .post("https://reqres.in/api/register")
                .then().log().all()
                .assertThat().statusCode(400);
    }


    @Test
    public void testPutUser() {
        RestAssured.baseURI = "https://reqres.in/";
        int userId = 2;


        Response response = given().when().get("api/users/" + userId);
        String fname = response.getBody().jsonPath().get("data.first_name");
        String lname = response.getBody().jsonPath().get("data.last_name");
        String avatar = response.getBody().jsonPath().get("data.avatar");
        String email = response.getBody().jsonPath().get("data.email");
        System.out.println("name before = " + fname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", userId);
        bodyMap.put("email", email);
        bodyMap.put("first_name", "updatedUser");
        bodyMap.put("last_name", lname);
        bodyMap.put("avatar", avatar);

        JSONObject jsonObject = new JSONObject(bodyMap);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("api/users/" + userId)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo("updatedUser"));
    }

    @Test
    public void testPatchUser() {
        RestAssured.baseURI = "https://reqres.in/";
        int userId = 2;

        Response response = given().when().get("api/users/" + userId);
        String fname = response.getBody().jsonPath().get("data.first_name");
        String lname = response.getBody().jsonPath().get("data.last_name");
        String avatar = response.getBody().jsonPath().get("data.avatar");
        String email = response.getBody().jsonPath().get("data.email");
        System.out.println("name before = " + fname);

        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("first_name", "updatedUser"); // Hanya mengupdate first_name
        JSONObject jsonObject = new JSONObject(bodyMap);

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("api/users/" + userId)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo("updatedUser"));
    }


    @Test
    public void testDeleteUser() {
        RestAssured.baseURI = "https://reqres.in/";
        int userId = 2;


        given()
                .when()
                .delete("api/users/" + userId)
                .then()
                .log().all()
                .assertThat().statusCode(204);  // Mengharapkan status code 204 (No Content) setelah penghapusan

    }

}

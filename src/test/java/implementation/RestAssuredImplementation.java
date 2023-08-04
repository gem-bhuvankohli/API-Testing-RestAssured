package implementation;

import globalvars.GlobalVars;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.json.JSONObject;
import org.junit.Assert;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import static io.restassured.RestAssured.given;

public class RestAssuredImplementation {

    static String bookingID;

    //POST REQUEST
    public static void post() {
        try {
//          Fetching payload from data folder
            FileReader fileReader = new FileReader("src/test/resources/data/bookingDetails.json");
            int i;
            StringBuilder payload = new StringBuilder();
            while ((i = fileReader.read()) != -1)
                payload.append((char) i);
            fileReader.close();

            RequestSpecification req = given()
                    .baseUri(GlobalVars.baseUri)
                    .log()
                    .all()
                    .header("Content-Type", "application/json");
            Log.info("**********************POST REQUEST**********************");
            Response res = req.body(payload.toString())
                    .post()
                    .then()
                    .log()
                    .all()
                    .extract().
                    response();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            Date date = new Date();

            File postRes = new File("src/test/resources/responseData/postReq/postReq-" + formatter.format(date) + ".json");
            FileWriter fileWriter = new FileWriter(postRes);
            fileWriter.write(res.asPrettyString());
            fileWriter.close();

//      Fetching Booking ID
            bookingID = res.prettyPrint().split(":")[1].split(",")[0];
            int statusCode = res.getStatusCode();
            Assert.assertEquals(200, statusCode);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //      PATCH REQUEST
    public static void patch() {
        try {
//      PATCH Payload
            JSONObject payload = new JSONObject();
            payload.put("firstname", "changedName");
            System.out.println(payload.get("firstname"));
//      Authenticating by successfully updating the payload using PATCH method of RestAssured
            RequestSpecification req = given()
                    .baseUri(GlobalVars.baseUri)
                    .log()
                    .all()
                    .header("Content-Type", "application/json")
                    .header("Authorization", GlobalVars.authToken);
            Log.info("**********************PATCH REQUEST**********************");
            Log.info("Authentication Successful!");
            Log.info("Authentication Token : " + new String(Base64.getEncoder().encode(payload.toString().getBytes())));


            Response res = req
                    .when()
                    .body(payload.toString())
                    .patch(bookingID)
                    .then()
                    .log()
                    .all()
                    .extract()
                    .response();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            Date date = new Date();

            File patchRes = new File("src/test/resources/responseData/patchReq/patchReq-" + formatter.format(date) + ".json");
            FileWriter fileWriter = new FileWriter(patchRes);
            fileWriter.write(res.asPrettyString());
            fileWriter.close();
            int statusCode = res.getStatusCode();
            Assert.assertEquals(200, statusCode);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    //PUT REQUEST
    public static void put() {
        try {
//      PUT Payload
            JSONObject payload = new JSONObject();
            payload.put("firstname", "changedName");
            payload.put("lastname", "changedLastName");
            payload.put("totalprice", 222);
            payload.put("depositpaid", true);
            JSONObject innerList = new JSONObject();
            innerList.put("checkin", "2018-01-01");
            innerList.put("checkout", "2019-01-01");
            payload.put("bookingdates", innerList);
            payload.put("additionalneeds", "Breakfast");

//      Authenticating by successfully updating the payload using PUT method of RestAssured
            RequestSpecification req = given().baseUri(GlobalVars.baseUri).log().all()
                    .header("Content-Type", "application/json")
                    .header("Authorization", GlobalVars.authToken);
            Log.info("**********************PUT REQUEST**********************");
            Log.info("Authentication Successful!");
            Log.info("Authentication Token : " + new String(Base64.getEncoder().encode(payload.toString().getBytes())));

            Response res = req.body(payload.toString())
                    .put(bookingID)
                    .then()
                    .log()
                    .all()
                    .extract()
                    .response();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
            Date date = new Date();

            File putRes = new File("src/test/resources/responseData/putReq/putReq-" + formatter.format(date) + ".json");
            FileWriter fileWriter = new FileWriter(putRes);
            fileWriter.write(res.asPrettyString());
            fileWriter.close();


            int statusCode = res.getStatusCode();
            Assert.assertEquals(200, statusCode);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    //DELETE REQUEST
    public static void delete() {
        try {
//      Authenticating by successfully updating the payload using DELETE method of RestAssured
            RequestSpecification req = given().baseUri(GlobalVars.baseUri).log().all()
                    .header("Content-Type", "application/json")
                    .header("Authorization", GlobalVars.authToken);
            Log.info("**********************DELETE REQUEST**********************");
            Log.info("Authentication Successful!");

            Response res = req.delete(bookingID)
                    .then()
                    .log()
                    .all()
                    .extract()
                    .response();
            int statusCode = res.getStatusCode();
            Assert.assertEquals(201, statusCode);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }


    //  VALIDATING DELETE USING GET REQUEST

    public static void validateDelete() {
        try {
            RequestSpecification req = given()
                    .baseUri(GlobalVars.baseUri)
                    .log()
                    .all()
                    .header("Content-Type", "application/json");
            Log.info("****************VALIDATING DELETE USING GET REQUEST****************");
            Response res = req.get("625")
                    .then()
                    .log()
                    .all()
                    .extract()
                    .response();
            int statusCode = res.getStatusCode();
            JsonPath jp = new JsonPath(res.asString());
            System.out.println(jp.getString("bookingdates.checkin"));
            System.out.println(res.path("bookingdates.checkin").toString());

/*     Note*:
       If status code shows 404 it means the requested data is not
       found hence, delete operation was successful.
 */
            Assert.assertEquals(404, statusCode);
            Log.info("REQUESTS VALIDATED SUCCESSFULLY!");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
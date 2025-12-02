package clients;

import baseClass.BaseClass;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.WhatsAppConsentRequest;

import static io.restassured.RestAssured.given;

public class WhatsAppConsentClient extends BaseClass {

    public static Response getWhatsAppConsentStatus(String authToken){
        return given()
                .baseUri(getBaseUrl())
                .header("accept","application/json")
                .header("Authorization","Bearer " + authToken)
                .contentType(ContentType.JSON)
                .queryParam("random_id", 1234)
                .when()
                .get("/api/user-service/v1/user/whatsapp-consent/status")
                .then()
                .extract().response();
    }

    public static Response submitWhatsAppConsent(String authToken){

        WhatsAppConsentRequest request = WhatsAppConsentRequest.builder()
                .whatsAppNotification(true)
                .build();

        return given()
                .baseUri(getBaseUrl())
//                .header("accept","application/json")
                .header("Authorization","Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/user-service/v1/user/whatsapp-consent/")
                .then()
                .extract().response();
    }

    public static Response updateWhatsAppConsent(String authToken,String clientId){

        String requestBody = "{\n" +
                "  \"customer_id\": \"" + clientId + "\",\n" +
                "  \"attributes\": {\n" +
                "    \"moe_wa_subscription\": true\n" +
                "  }\n" +
                "}";

        return given()
                .baseUri(getBaseUrl())
                .header("accept","application/json")
                .header("Authorization","Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/user-service/v1/user/whatsapp-consent/update-status")
                .then()
                .extract().response();
    }
}

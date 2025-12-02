package ManageProfiles;

import clients.ManageProfileClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class AvatarsListTests {
    private static Response response;

    @BeforeClass
    public static void setUp() {
        response = ManageProfileClient.getAvatarList();
    }

    @Test(priority = 1)
    public static void verifyStatusCode() {
        Assert.assertEquals(response.getStatusCode(), 200, "Invalid Status Code");
    }

    @Test(priority = 2)
    public static void verifyResponseStatus() {
        boolean status = response.jsonPath().getBoolean("status");
        Assert.assertTrue(status, "Status is not true");
    }

    @Test(priority = 3)
    public static void verifyResponseMessage() {
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(message, "Success","Not Successful");
    }

    @Test(priority = 4)
    public static void verifyDataIsNotNullAndContainsIdAndAvatarUrl() {

        // Extract the data list
        List<Map<String, Object>> dataList = response.jsonPath().getList("data");

        // Assert that data is not null and not empty
        Assert.assertNotNull(dataList, "Data list is null");
        Assert.assertFalse(dataList.isEmpty(), "Data list is empty");

        // Loop through each item and check _id and avatar_url
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> item = dataList.get(i);

            // Extract and validate _id
            String id = (String) item.get("_id");
            Assert.assertNotNull(id, "Item " + i + " has null _id");
            Assert.assertFalse(id.trim().isEmpty(), "Item " + i + " has empty _id");

            // Extract and validate avatar_url
            String avatarUrl = (String) item.get("avatar_url");
            Assert.assertNotNull(avatarUrl, "Item " + i + " has null avatar_url");
            Assert.assertFalse(avatarUrl.trim().isEmpty(), "Item " + i + " has empty avatar_url");
        }
    }
}

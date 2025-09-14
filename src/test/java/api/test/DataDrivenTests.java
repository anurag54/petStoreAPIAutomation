package api.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class DataDrivenTests {
	
	@Test(priority=1, dataProvider="Data", dataProviderClass=DataProviders.class)
	public void testPostUser(String userID, String userName, String firstName, String lastName, String email, String password, String phone) 
	{
		if (userID == null || userID.trim().isEmpty()) {
			System.out.println("Skipping test: userID is empty");
			return;
		}
		int id;
		try {
			id = Integer.parseInt(userID);
		} catch (NumberFormatException e) {
			System.out.println("Skipping test: userID is not a valid integer: " + userID);
			return;
		}
		User userPayload = new User();
		userPayload.setId(id);
		userPayload.setUsername(userName);
		userPayload.setFirstName(firstName);
		userPayload.setLastName(lastName);
		userPayload.setEmail(email);
		userPayload.setPassword(password);
		userPayload.setPhone(phone);
		Response response = UserEndPoints.createUser(userPayload);
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority=2, dataProvider="UserNames", dataProviderClass=DataProviders.class)
	public void testDeleteUserByName(String userName) 
	{
		if (userName == null || userName.trim().isEmpty()) {
			System.out.println("Skipping test: userName is empty");
			return;
		}
		Response response = UserEndPoints.deleteUser(userName);
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	

}
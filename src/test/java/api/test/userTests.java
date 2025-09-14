package api.test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {

	Faker faker;
	User userPayload;

	@BeforeClass
	public void setupData() {

		faker = new Faker();
		userPayload = new User();

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setUsername(faker.name().username());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());

	}


	@Test(priority=1)
	public void testPostUser() {
		Response response = UserEndPoints.createUser(userPayload);
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);
	}

	@Test(priority=2)
	public void testGetUserByName() {
		Response response = UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);
	}

	@Test(priority=3)
	public void testUpdateUserByName() {
		// Update userPayload fields
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPhone(faker.phoneNumber().cellPhone());

		// Send update request
		Response response = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);

		// Verify update by fetching user again
		Response getResponse = UserEndPoints.readUser(this.userPayload.getUsername());
		getResponse.then().log().all();

		Assert.assertEquals(getResponse.getStatusCode(), 200);
		Assert.assertEquals(getResponse.jsonPath().getString("firstName"), userPayload.getFirstName());
		Assert.assertEquals(getResponse.jsonPath().getString("lastName"), userPayload.getLastName());
		Assert.assertEquals(getResponse.jsonPath().getString("email"), userPayload.getEmail());
		Assert.assertEquals(getResponse.jsonPath().getString("phone"), userPayload.getPhone());
	}

	@Test(priority=4)
	public void testDeleteUserByName() {
		Response response = UserEndPoints.deleteUser(this.userPayload.getUsername());
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);

		// Verify deletion by attempting to fetch the user again
		Response getResponse = UserEndPoints.readUser(this.userPayload.getUsername());
		getResponse.then().log().all();

		Assert.assertEquals(getResponse.getStatusCode(), 404); // Assuming 404 is returned for not found
	}


}
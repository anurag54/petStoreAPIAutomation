package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints2;
import api.payload.User;
import io.restassured.response.Response;

public class userTests2 {

	Faker faker;
	User userPayload;
	
	public Logger logger;  //For logs 

	@BeforeClass
	public void setup() {

		faker = new Faker();
		userPayload = new User();

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setUsername(faker.name().username());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		logger = LogManager.getLogger(this.getClass()); //For logs

	}


	@Test(priority=1)
	public void testPostUser() {
		
		logger.info("********** Creating User **********"); //For logs
		Response response = UserEndPoints2.createUser(userPayload);
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("********** User Created **********"); //For logs
	}

	@Test(priority=2)
	public void testGetUserByName() {
		
		logger.info("********** Reading User Info **********"); //For logs
		Response response = UserEndPoints2.readUser(this.userPayload.getUsername());
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("********** User Info Displayed **********"); //For logs
	}

	@Test(priority=3)
	public void testUpdateUserByName() {
		
		logger.info("********** Updating User Info **********"); //For logs
		// Update userPayload fields
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPhone(faker.phoneNumber().cellPhone());

		// Send update request
		Response response = UserEndPoints2.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);

		// Verify update by fetching user again
		Response getResponse = UserEndPoints2.readUser(this.userPayload.getUsername());
		getResponse.then().log().all();

		Assert.assertEquals(getResponse.getStatusCode(), 200);
		Assert.assertEquals(getResponse.jsonPath().getString("firstName"), userPayload.getFirstName());
		Assert.assertEquals(getResponse.jsonPath().getString("lastName"), userPayload.getLastName());
		Assert.assertEquals(getResponse.jsonPath().getString("email"), userPayload.getEmail());
		Assert.assertEquals(getResponse.jsonPath().getString("phone"), userPayload.getPhone());
		
		logger.info("********** User Info Updated **********"); //For logs
	}

	@Test(priority=4)
	public void testDeleteUserByName() {
		
		logger.info("********** Deleting User **********"); //For logs
		Response response = UserEndPoints2.deleteUser(this.userPayload.getUsername());
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);

		// Verify deletion by attempting to fetch the user again
		Response getResponse = UserEndPoints2.readUser(this.userPayload.getUsername());
		getResponse.then().log().all();

		Assert.assertEquals(getResponse.getStatusCode(), 404); // Assuming 404 is returned for not found
		
		logger.info("********** User Deleted **********"); //For logs
	}


}
package net.degiere.authorizenet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Properties;

import net.authorize.data.cim.CustomerProfile;
import net.authorize.data.cim.PaymentProfile;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerManagerIntegrationTest {

	static Logger log = LoggerFactory.getLogger(CustomerManagerIntegrationTest.class);	
	
	MerchantManager merchantManager;
	CustomerManager customerManager;
	String baseEmail;
	
	public CustomerManagerIntegrationTest() {
		Properties props = PropertiesLoader.load("authorizenet.properties");
		MerchantManager merchantManager = new MerchantManager(props.getProperty("login"), props.getProperty("transactionKey"));
		customerManager = new CustomerManager();
		customerManager.setMerchantManager(merchantManager);
		baseEmail = props.getProperty("test.email");
	}
	
	@Test
	public void createsNewCustomerProfile() {
		String profileId = customerManager.createCustomerProfile(testCustomerProfile());
		assertNotNull(profileId);
		customerManager.deleteCustomerProfile(profileId);
	}
	
	@Test
	public void errorsWhenCustomerProfileAlreadyExists() {
		CustomerProfile profile = testCustomerProfile();
		String profileId = customerManager.createCustomerProfile(profile);
		try {
			customerManager.createCustomerProfile(profile);
			fail("shouldn't have created duplicate");
		} catch (AuthorizeNetRuntimeException e) {
			assertEquals("E00039", e.getResultMessage().getCode());
		} catch (Exception e) {
			fail("threw wrong exception");
		}
		customerManager.deleteCustomerProfile(profileId);
	}

	@Test
	public void getsCustomerProfileThatExists() {
		String id = createCustomerProfileForTest();
		CustomerProfile profile = customerManager.getCustomerProfile(id);
		assertNotNull(profile);
		customerManager.deleteCustomerProfile(id);
	}

	@Test(expected = AuthorizeNetRuntimeException.class)
	public void errorsWhenCustomerProfileDoesNotExist() {
		customerManager.getCustomerProfile("11111111");
	}
	
	@Test
	public void deletesCustomerProfileWhenExists() {
		String id = createCustomerProfileForTest();
		customerManager.deleteCustomerProfile(id);
	}
	
	@Test
	public void createsPaymentProfile() {
		CustomerProfile customer = TestData.testCustomerProfile(baseEmail);
		String customerId = customerManager.createCustomerProfile(customer);
		PaymentProfile paymentProfile = TestData.testPaymentProfile(customer.getMerchantCustomerId());
		String paymentProfileId = customerManager.createCustomerPaymentProfile(customerId, paymentProfile);
		assertNotNull(paymentProfileId);
		customerManager.deleteCustomerProfile(customerId); 
	}

	private CustomerProfile testCustomerProfile() {
		return TestData.testCustomerProfile(baseEmail);
	}
	
	private String createCustomerProfileForTest() {
		return customerManager.createCustomerProfile(testCustomerProfile());
	}
	
}

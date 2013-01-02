package net.degiere.authorizenet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import net.authorize.Environment;
import net.authorize.TransactionType;
import net.authorize.data.Order;
import net.authorize.data.cim.CustomerProfile;
import net.authorize.data.cim.PaymentProfile;
import net.authorize.data.cim.PaymentTransaction;

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
		Environment env = MerchantManager.environment(props.getProperty("environment"));
		merchantManager = new MerchantManager(props.getProperty("login"), props.getProperty("transactionKey"), env);
		customerManager = new CustomerManager();
		customerManager.setMerchantManager(merchantManager);
		baseEmail = props.getProperty("test.email");
	}
	
//	createCustomerProfileRequest – Create a new customer profile along with any customer payment profiles and customer shipping addresses for the customer profile.

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

//	createCustomerPaymentProfileRequest—Create a new customer payment profile for an existing customer profile.
	
	@Test
	public void createsPaymentProfile() {
		CustomerProfile customer = TestData.testCustomerProfile(baseEmail);
		String customerId = customerManager.createCustomerProfile(customer);
		PaymentProfile paymentProfile = TestData.testPaymentProfile(customer.getMerchantCustomerId());
		String paymentProfileId = customerManager.createCustomerPaymentProfile(customerId, paymentProfile);
		assertNotNull(paymentProfileId);
		customerManager.deleteCustomerProfile(customerId); 
	}

//	createCustomerShippingAddressRequest—Create a new customer shipping address for an existing customer profile.
	
//	@Test
	public void createsCustomerShippingAddress() {
		customerManager.createCustomerShippingAddress();
	}
		
//	createCustomerProfileTransactionRequest—Create a new payment transaction from an existing customer profile.

	@Test
	public void createsCustomerProfileTransaction() {
		// setup customer profile and payment profile
		CustomerProfile customer = TestData.testCustomerProfile(baseEmail);
		String customerId = customerManager.createCustomerProfile(customer);
		PaymentProfile paymentProfile = TestData.testPaymentProfile(customer.getMerchantCustomerId());
		String paymentProfileId = customerManager.createCustomerPaymentProfile(customerId, paymentProfile);
		// post transaction
		PaymentTransaction transaction = PaymentTransaction.createPaymentTransaction();
		transaction.setTransactionType(TransactionType.AUTH_CAPTURE);
//		transaction.setApprovalCode(approvalCode); // for previously authorized transaction
		transaction.setCardCode("123");
		Order order = Order.createOrder();
		order.setTotalAmount(new BigDecimal("9.99"));
		transaction.setOrder(order);
		customerManager.createCustomerProfileTransaction(customerId, paymentProfileId, transaction);
		customerManager.deleteCustomerProfile(customerId); 
	}

//	deleteCustomerProfileRequest—Delete an existing customer profile along with all associated customer payment profiles and customer shipping addresses.

	@Test
	public void deletesCustomerProfileWhenExists() {
		String id = createCustomerProfileForTest();
		customerManager.deleteCustomerProfile(id);
	}
	
//	deleteCustomerPaymentProfileRequest—Delete a customer payment profile from an existing customer profile.

	@Test
	public void deleteCustomerPaymentProfile() {
		CustomerProfile customer = TestData.testCustomerProfile(baseEmail);
		String customerId = customerManager.createCustomerProfile(customer);
		PaymentProfile paymentProfile = TestData.testPaymentProfile(customer.getMerchantCustomerId());
		String paymentProfileId = customerManager.createCustomerPaymentProfile(customerId, paymentProfile);
		customerManager.deleteCustomerPaymentProfile(customerId, paymentProfileId);
		customerManager.deleteCustomerProfile(customerId); 
	}
	
//	deleteCustomerShippingAddressRequest—Delete a customer shipping address from an existing customer profile.

//	@Test
	public void deleteCustomerShippingAddress() {
		customerManager.deleteCustomerShippingAddress();
	}

//	getCustomerProfileIdsRequest—Retrieve all customer profile IDs you have previously created.
	
	@Test
	public void getCustomerProfileIds() {
		List<String> ids = customerManager.getCustomerProfileIds();
		assertNotNull(ids);
	}
	
//	getCustomerProfileRequest—Retrieve an existing customer profile along with all the associated customer payment profiles and customer shipping addresses.

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
	
//	getCustomerPaymentProfileRequest—Retrieve a customer payment profile for an existing customer profile.
	
	@Test
	public void getsCustomerPaymentProfile() {
		CustomerProfile customer = TestData.testCustomerProfile(baseEmail);
		String customerId = customerManager.createCustomerProfile(customer);
		PaymentProfile paymentProfile = TestData.testPaymentProfile(customer.getMerchantCustomerId());
		String paymentProfileId = customerManager.createCustomerPaymentProfile(customerId, paymentProfile);
		PaymentProfile actual = customerManager.getCustomerPaymentProfile(customerId, paymentProfileId);
		customerManager.deleteCustomerProfile(customerId); 
		assertEquals(paymentProfileId, actual.getCustomerPaymentProfileId());
	}
	
//	getCustomerShippingAddressRequest—Retrieve a customer shipping address for an existing customer profile.
	
//	@Test
	public void getsCustomerShippingAddress() {
		customerManager.getShippingAddress();
	}
	
//	getHostedProfilePageRequest—sends a request for access to the hosted CIM page. The response includes a token that enables customers to update their information directly on the Authorize.Net website.
//	updateCustomerProfileRequest – Update an existing customer profile.
//	updateCustomerPaymentProfileRequest – Update a customer payment profile for an existing customer profile.
//	updateCustomerShippingAddressRequest – Update a shipping address for an existing customer profile.
	
	private CustomerProfile testCustomerProfile() {
		return TestData.testCustomerProfile(baseEmail);
	}
	
	private String createCustomerProfileForTest() {
		return customerManager.createCustomerProfile(testCustomerProfile());
	}
	
}

package net.degiere.authorizenet;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.Random;

import net.authorize.Environment;
import net.authorize.aim.Result;
import net.authorize.aim.Transaction;
import net.authorize.data.Customer;
import net.authorize.data.creditcard.CreditCard;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PaymentManagerIntegrationTest {
	
	static Logger log = LoggerFactory.getLogger(PaymentManagerIntegrationTest.class);	
	
	MerchantManager merchantManager;
	PaymentManager paymentService;
	
	public PaymentManagerIntegrationTest() {
		Properties props = PropertiesLoader.load("authorizenet.properties");
		Environment env = MerchantManager.environment(props.getProperty("environment"));
		merchantManager = new MerchantManager(props.getProperty("login"), props.getProperty("transactionKey"), env);
		paymentService = new PaymentManager();
		paymentService.setMerchantManager(merchantManager);
	}

	@Test
	public void priorauth() {
		Result<Transaction> result = paymentService.auth(testCustomer(), testCreditCard(), random());
		debug(result);
		assertTrue(result.isApproved());
		result = paymentService.priorauth(result.getTarget().getTransactionId());
		debug(result);
		assertTrue(result.isApproved());
	}

	@Test
	public void capture() {
		Result<Transaction> result = paymentService.capture(testCustomer(), testCreditCard(), random());
		debug(result);
		assertTrue(result.isApproved());
	}

	private CreditCard testCreditCard() {
		CreditCard creditCard = CreditCard.createCreditCard();
		creditCard.setCreditCardNumber("4111 1111 1111 1111");
		creditCard.setExpirationMonth("12");
		creditCard.setExpirationYear("2015");
		creditCard.setCardCode("123");
		return creditCard;
	}
	
	private Customer testCustomer() {
		Customer customer = Customer.createCustomer();
		customer.setFirstName("John");
		customer.setLastName("Doe");
		customer.setAddress("555 Easy Street");
		customer.setCity("Happy Valley");
		customer.setState("CA");
		customer.setZipPostalCode("95555");
		return customer;
	}
	
	private void debug(Result<Transaction> result) {
		if (result.isApproved()) {
			Transaction target = result.getTarget();
			log.debug("Approved! Transaction Id: {}. Authorization Code: {}", target.getTransactionId(), target.getAuthorizationCode());
		} else if (result.isDeclined()) {
			log.debug("Declined!");
		} else {
			log.debug("Error!");
		}
		log.debug("Response: {}", result.getReasonResponseCode() + " : " + result.getResponseText());
	}
	
	private BigDecimal random() {
		return new BigDecimal(new Random().nextDouble());
	}
	
}

package net.degiere.authorizenet;

import net.authorize.data.cim.CustomerProfile;
import net.authorize.data.cim.PaymentProfile;
import net.authorize.data.creditcard.CreditCard;
import net.authorize.data.xml.Address;
import net.authorize.data.xml.CustomerType;
import net.authorize.data.xml.Payment;

public class TestData {

	public static Address testAddress(String id) {
		Address address = Address.createAddress();
		address.setAddress(suffix(id, 4) + " Some Str");
		address.setCity("Citiesville");
//		address.setCompany("Company Co.");
		address.setCountry("US");
		address.setFirstName("Firsty");
		address.setLastName("Lastnamington");
		address.setState("CA");
		address.setZipPostalCode(suffix(id, 5));
//		address.setFaxNumber("555-555-" + suffix(id, 4));
		address.setPhoneNumber("555-555-" + suffix(id, 4));
		return address;
	}
	
	public static CreditCard testCard() {
		CreditCard card = CreditCard.createCreditCard();
		card.setCardCode("123");
		card.setCreditCardNumber("4007000000027"); // visa
		card.setExpirationMonth("12");
		card.setExpirationYear("2020");
		return card;
	}
	
	public static Payment testPayment() {
		return Payment.createPayment(testCard());
	}
	
	public static PaymentProfile testPaymentProfile(String id) {
		Address address = testAddress(id);
		Payment payment = testPayment();
		PaymentProfile paymentProfile = PaymentProfile.createPaymentProfile();
		paymentProfile.setBillTo(address);
		paymentProfile.setPaymentList(CustomerManager.asList(payment));
		paymentProfile.setCustomerType(CustomerType.INDIVIDUAL);
		return paymentProfile;
	}
	
	private static String suffix(String id, int amount) {
		return id.substring(id.length() - amount, id.length());
	}
	
	public static String testEmail(String email, String suffix) {
		String[] parts = email.split("@");
		return parts[0] + "+" + suffix + "@" + parts[1];
	}

	public static CustomerProfile testCustomerProfile(String baseEmail) {
		String merchantCustomerId = MerchantHelper.generateMerchantId();
		String testEmail = testEmail(baseEmail, merchantCustomerId);
		CustomerProfile profile = CustomerHelper.createCustomerProfile(merchantCustomerId, testEmail, "test");
		return profile;
	}
	
}

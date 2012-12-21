package net.degiere.authorizenet;

import net.authorize.data.cim.CustomerProfile;

public class CustomerProfileHelper {

	public static CustomerProfile createCustomerProfile(String email) {
		return createCustomerProfile(null, email, null);
	}
	
	public static CustomerProfile createCustomerProfile(String merchantCustomerId, String email, String description) {
		CustomerProfile cp = CustomerProfile.createCustomerProfile();
		cp.setMerchantCustomerId(merchantCustomerId);
		cp.setEmail(email);
		cp.setDescription(description);
		return cp;
	}

	public static CustomerProfile testCustomerProfile(String baseEmail) {
		String merchantCustomerId = MerchantHelper.generateMerchantId();
		String testEmail = CustomerProfileHelper.testEmail(baseEmail, merchantCustomerId);
		CustomerProfile profile = CustomerProfileHelper.createCustomerProfile(testEmail);
		return profile;
	}
	
	public static String testEmail(String email, String suffix) {
		String[] parts = email.split("@");
		return parts[0] + "+" + suffix + "@" + parts[1];
	}

}

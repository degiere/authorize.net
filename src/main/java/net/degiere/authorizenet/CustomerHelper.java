package net.degiere.authorizenet;

import net.authorize.data.cim.CustomerProfile;

public class CustomerHelper {

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

}

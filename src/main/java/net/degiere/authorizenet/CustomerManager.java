package net.degiere.authorizenet;

import net.authorize.Merchant;
import net.authorize.cim.Result;
import net.authorize.cim.Transaction;
import net.authorize.cim.TransactionType;
import net.authorize.data.cim.CustomerProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerManager {
	
	static final Logger log = LoggerFactory.getLogger(CustomerManager.class);
	
	MerchantManager merchantManager;
	
	public void setMerchantManager(MerchantManager merchantManager) {
    	this.merchantManager = merchantManager;
    }

	/**
	 * Create a new customer profile
	 * @param customerProfile
	 * @return profile id
	 */
	public String createCustomerProfile(CustomerProfile customerProfile) {
		log.debug("creating customer profile for email: {}", customerProfile.getEmail());
		Merchant merchant = merchantManager.getMerchant();
		Transaction tran = merchant.createCIMTransaction(TransactionType.CREATE_CUSTOMER_PROFILE);
		tran.setCustomerProfile(customerProfile);
        Result<Transaction> result = post(merchant, tran);
		if (isSuccessful(result)) {
			return result.getCustomerProfileId();
		} else {
			throw new AuthorizeNetRuntimeException(result.getMessages().iterator().next());
		}
	}

	/**
	 * Get customer profile by id
	 * @param customerProfileId
	 * @return customer profile
	 */
	public CustomerProfile getCustomerProfile(String customerProfileId) {
		log.info("getting profile for id: {}", customerProfileId);
		Merchant merchant = merchantManager.getMerchant();
		Transaction tran = merchant.createCIMTransaction(TransactionType.GET_CUSTOMER_PROFILE);
		tran.setCustomerProfileId(customerProfileId);
        Result<Transaction> result = post(merchant, tran);
		if (isSuccessful(result)) {
			return result.getCustomerProfile();
		} else {
			throw new AuthorizeNetRuntimeException(result.getMessages().iterator().next());
		}
	}
	
	/**
	 * delete a customer profile
	 * @param customerProfileId
	 */
	public void deleteCustomerProfile(String customerProfileId) {
		log.info("deleting profile for id: {}", customerProfileId);
		Merchant merchant = merchantManager.getMerchant();
		Transaction tran = merchant.createCIMTransaction(TransactionType.DELETE_CUSTOMER_PROFILE);
		tran.setCustomerProfileId(customerProfileId);
        Result<Transaction> result = post(merchant, tran);
		if (isSuccessful(result)) {
			return;
		} else {
			throw new AuthorizeNetRuntimeException(result.getMessages().iterator().next());
		}
	}

	private Result<Transaction> post(Merchant merchant, Transaction transaction) {
		return (Result<Transaction>) merchant.postTransaction(transaction);
	}
	
	private static boolean isSuccessful(Result<Transaction> result) {
		return "Ok".equals(result.getResultCode());
	}

}
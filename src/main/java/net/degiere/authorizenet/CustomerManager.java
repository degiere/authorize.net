package net.degiere.authorizenet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.authorize.Merchant;
import net.authorize.cim.Result;
import net.authorize.cim.Transaction;
import net.authorize.cim.TransactionType;
import net.authorize.data.ShippingAddress;
import net.authorize.data.cim.CustomerProfile;
import net.authorize.data.cim.PaymentProfile;
import net.authorize.data.cim.PaymentTransaction;
import net.authorize.data.xml.Payment;

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
	 * Create a new customer payment profile
	 * @param customerProfileId
	 * @param paymentProfile
	 * @return
	 */
	public String createCustomerPaymentProfile(String customerProfileId, PaymentProfile paymentProfile) {
		log.info("creating payment profile: {}", paymentProfile);
		Merchant merchant = merchantManager.getMerchant();
		Transaction tran = merchant.createCIMTransaction(TransactionType.CREATE_CUSTOMER_PAYMENT_PROFILE);
		tran.setCustomerProfileId(customerProfileId);
		tran.setPaymentProfileList(asList(paymentProfile));
        Result<Transaction> result = post(merchant, tran);
		if (isSuccessful(result)) {
			return result.getCustomerPaymentProfileIdList().iterator().next();
		} else {
			throw new AuthorizeNetRuntimeException(result.getMessages().iterator().next());
		}
	}
	
	public String createCustomerShippingAddress() {
		throw new RuntimeException("not implemented yet");
	}
	
	public void createCustomerProfileTransaction(String customerProfileId, String paymentProfileId, PaymentTransaction transaction) {
		log.info("creating transaction: {}", transaction);
		Merchant merchant = merchantManager.getMerchant();
		Transaction tran = merchant.createCIMTransaction(TransactionType.CREATE_CUSTOMER_PROFILE_TRANSACTION);
		tran.setCustomerProfileId(customerProfileId);
		tran.setCustomerPaymentProfileId(paymentProfileId);
		tran.setPaymentTransaction(transaction);
        Result<Transaction> result = post(merchant, tran);
		if (isSuccessful(result)) {
			return;
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
	
	/**
	 * delete a customer payment profile
	 * @param paymentProfileId
	 */
	public void deleteCustomerPaymentProfile(String customerProfileId, String paymentProfileId) {
		log.info("deleting payment profile for id: {}", paymentProfileId);
		Merchant merchant = merchantManager.getMerchant();
		Transaction tran = merchant.createCIMTransaction(TransactionType.DELETE_CUSTOMER_PAYMENT_PROFILE);
		tran.setCustomerProfileId(customerProfileId);
		tran.setCustomerPaymentProfileId(paymentProfileId);
        Result<Transaction> result = post(merchant, tran);
		if (isSuccessful(result)) {
			return;
		} else {
			throw new AuthorizeNetRuntimeException(result.getMessages().iterator().next());
		}
	}
	
	public void deleteCustomerShippingAddress() {
		throw new RuntimeException("not implemented yet");
	}
	
	public List<String> getCustomerProfileIds() {
		log.info("getting customer profile ids");
		Merchant merchant = merchantManager.getMerchant();
		Transaction tran = merchant.createCIMTransaction(TransactionType.GET_CUSTOMER_PROFILE_IDS);
        Result<Transaction> result = post(merchant, tran);
		if (isSuccessful(result)) {
			return result.getCustomerProfileIdList();
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
	 * Get customer payment profile
	 * @param paymentProfileId
	 * @return payment profile
	 */
	public PaymentProfile getCustomerPaymentProfile(String customerProfileId, String paymentProfileId) {
		log.info("getting payment profile for id: {}", paymentProfileId);
		Merchant merchant = merchantManager.getMerchant();
		Transaction tran = merchant.createCIMTransaction(TransactionType.GET_CUSTOMER_PAYMENT_PROFILE);
		tran.setCustomerProfileId(customerProfileId);
		tran.setCustomerPaymentProfileId(paymentProfileId);
        Result<Transaction> result = post(merchant, tran);
		if (isSuccessful(result)) {
			return result.getCustomerPaymentProfile();
		} else {
			throw new AuthorizeNetRuntimeException(result.getMessages().iterator().next());
		}
	}
	
	public void updateCustomerProfile(CustomerProfile customerProfile) {
		throw new RuntimeException("not implemented yet");
	}
	
	public void updatePaymentProfile(PaymentProfile paymentProfile) {
		throw new RuntimeException("not implemented yet");
	}
	
	public void updateShippingAddress(ShippingAddress shippingAddress) {
		throw new RuntimeException("not implemented yet");
	}

	public ShippingAddress getShippingAddress() {
		throw new RuntimeException("not implemented yet");
	}

	public static ArrayList<PaymentProfile> asList(PaymentProfile paymentProfile) {
		return new ArrayList<PaymentProfile>(Arrays.asList(new PaymentProfile[] { paymentProfile }));
	}

	public static ArrayList<Payment> asList(Payment payment) {
		return new ArrayList<Payment>(Arrays.asList(new Payment[] { payment }));
	}
	
	private Result<Transaction> post(Merchant merchant, Transaction transaction) {
		return (Result<Transaction>) merchant.postTransaction(transaction);
	}
	
	private static boolean isSuccessful(Result<Transaction> result) {
		return "Ok".equals(result.getResultCode());
	}

}

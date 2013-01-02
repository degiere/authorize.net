package net.degiere.authorizenet;

import java.math.BigDecimal;

import net.authorize.Merchant;
import net.authorize.TransactionType;
import net.authorize.aim.Result;
import net.authorize.aim.Transaction;
import net.authorize.data.Customer;
import net.authorize.data.creditcard.CreditCard;

public class PaymentManager {
	
	MerchantManager merchantManager;
	
	public void setMerchantManager(MerchantManager merchantManager) {
    	this.merchantManager = merchantManager;
    }
	
	/**
	 * Authorize a transaction for settlement later. Checks if card is valid, has funds, matches CVV, matches address.
	 * @param customer
	 * @param creditCard
	 * @param amount
	 * @return result
	 */
	public Result<Transaction> auth(Customer customer, CreditCard creditCard, BigDecimal amount) {
		return transaction(customer, creditCard, TransactionType.AUTH_ONLY, null, amount);
	}
	
	/**
	 * Submit a previously authorized transaction for settlement.
	 * @param customer
	 * @param creditCard
	 * @param amount
	 * @return
	 */
	public Result<Transaction> priorauth(String transactionId) {
		return transaction(null, null, TransactionType.PRIOR_AUTH_CAPTURE, transactionId, null);
	}
	
	/**
	 * Authorize and submit a transaction for settlement.
	 * @param customer
	 * @param creditCard
	 * @param amount
	 * @return
	 */
	public Result<Transaction> capture(Customer customer, CreditCard creditCard, BigDecimal amount) {
		return transaction(customer, creditCard, TransactionType.AUTH_CAPTURE, null, amount);
	}

	private Result<Transaction> transaction(Customer customer, CreditCard creditCard, TransactionType type, String transactionId, BigDecimal amount) {
		Merchant merchant = merchantManager.getMerchant();
		Transaction transaction = merchant.createAIMTransaction(type, amount);
		transaction.setCreditCard(creditCard);
		transaction.setCustomer(customer);
		transaction.setTransactionId(transactionId);
        Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(transaction);
		return result;
	}
	
}

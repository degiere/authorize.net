package net.degiere.authorizenet;

import net.authorize.Environment;
import net.authorize.Merchant;

public class MerchantManager {

	public String login;
	public String transactionKey;
	public Merchant merchant;
	
	public MerchantManager(String login, String transactionKey) {
		this.login = login;
		this.transactionKey = transactionKey;
		// TODO: revisit environment
		this.merchant = Merchant.createMerchant(Environment.SANDBOX_TESTMODE, login, transactionKey);
	}
	
	public Merchant getMerchant() {
		return merchant;
	}

}

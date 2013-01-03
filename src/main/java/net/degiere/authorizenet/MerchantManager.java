package net.degiere.authorizenet;

import net.authorize.Environment;
import net.authorize.Merchant;

public class MerchantManager {

	public String login;
	public String transactionKey;
	public Merchant merchant;
	
	public MerchantManager(String login, String transactionKey, Environment env) {
		this.login = login;
		this.transactionKey = transactionKey;
		this.merchant = Merchant.createMerchant(env, login, transactionKey);
	}
	
	public static Environment environment(String environment) {
		if ("production".equals(environment)) {
			return Environment.PRODUCTION;
		} else {
			return Environment.SANDBOX;
		}
	}
	
	public Merchant getMerchant() {
		return merchant;
	}

}

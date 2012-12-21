package net.degiere.authorizenet;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import net.authorize.Merchant;

import org.junit.Test;


public class MerchantManagerTest {
	
	@Test
	public void getManager() {
		Properties props = PropertiesLoader.load("authorizenet.properties");
		MerchantManager merchantManager = new MerchantManager(props.getProperty("login"), props.getProperty("transactionKey"));
		Merchant merchant = merchantManager.getMerchant();
		assertNotNull(merchant);
		assertNotNull(merchant.getLogin());
		assertNotNull(merchant.getTransactionKey());
	}

}

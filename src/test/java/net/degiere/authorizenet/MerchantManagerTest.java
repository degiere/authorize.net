package net.degiere.authorizenet;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import net.authorize.Environment;
import net.authorize.Merchant;

import org.junit.Test;


public class MerchantManagerTest {
	
	MerchantManager merchantManager;
	
	@Test
	public void getManager() {
		Properties props = PropertiesLoader.load("authorizenet.properties");
		Environment env = MerchantManager.environment(props.getProperty("environment"));
		merchantManager = new MerchantManager(props.getProperty("login"), props.getProperty("transactionKey"), env);
		Merchant merchant = merchantManager.getMerchant();
		assertNotNull(merchant);
		assertNotNull(merchant.getLogin());
		assertNotNull(merchant.getTransactionKey());
	}

}

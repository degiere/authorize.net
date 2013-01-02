package net.degiere.authorizenet;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Test;


public class PropertiesLoaderTest {
	
	@Test
	public void load() {
		Properties props = PropertiesLoader.load("authorizenet.properties");
		assertNotNull(props);
		assertNotNull(props.getProperty("login"));
		assertNotNull(props.getProperty("transactionKey"));
		assertNotNull(props.getProperty("test.email"));
		assertNotNull(props.getProperty("mode"));
	}

}

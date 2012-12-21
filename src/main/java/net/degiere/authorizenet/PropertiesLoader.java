package net.degiere.authorizenet;

import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
	
	public static Properties load(String name) {
		Properties props = new Properties();
		try {
			props.load(PropertiesLoader.class.getClassLoader().getResourceAsStream(name));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return props;
	}

}

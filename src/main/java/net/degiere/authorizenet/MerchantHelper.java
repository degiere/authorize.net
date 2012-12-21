package net.degiere.authorizenet;

import java.util.Random;

public class MerchantHelper {

	/**
	 * Authorize.net merchant provided ids can can only be 20 characters,
	 * depending on database ids, those may work, UUIDs are too long, use a
	 * random long instead of truncated UUID
	 * 
	 * @return 20 characters of random numbers
	 */
	public static String generateMerchantId() {
		return new Long(Math.abs(new Random().nextLong())).toString();
	}

}

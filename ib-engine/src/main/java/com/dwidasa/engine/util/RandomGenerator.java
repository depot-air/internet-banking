package com.dwidasa.engine.util;

import java.util.Random;

/**
 * Convenience class to help us generating random number.
 *
 * @author rk
 */
public class RandomGenerator {
    /**
     * Generate hexadecimal random number with specified size.
     * @param size size of random number
     * @return random number
     */
    public static String generateHex(Integer size) {
        String result = "";
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            result += Integer.toHexString(random.nextInt(16));
        }

        return result;
    }

	public static String generateChallenge(String custRef) {
		String firstSix = custRef.substring(custRef.length() - 6);
    	
    	Random randomGenerator = new Random();
        int randomFirst = randomGenerator.nextInt(10);
        int randomSecond = randomGenerator.nextInt(10);
        return firstSix + randomFirst + randomSecond;
	}
    
}

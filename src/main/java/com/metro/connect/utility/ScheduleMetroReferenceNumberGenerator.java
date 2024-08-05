package com.metro.connect.utility;

import java.util.Random;

public class ScheduleMetroReferenceNumberGenerator {
	
	public static String generate() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            // Generate a random number between 0 and 15
            int randomNumber = random.nextInt(16);

            // Convert the random number to a hexadecimal digit
            String hexDigit = Integer.toHexString(randomNumber);

            // Append the hexadecimal digit to the string
            sb.append(hexDigit);
        }

        return sb.toString();
    }

}

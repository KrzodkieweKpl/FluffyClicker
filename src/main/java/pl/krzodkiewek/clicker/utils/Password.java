package pl.krzodkiewek.clicker.utils;

import java.security.SecureRandom;

public class Password {
    public static String generateRandomPassword(int len)
    {
        //All letters of the alphabet
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        //Constructors
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        //For loop, to generate a password
        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        //Return the final password
        return sb.toString();
    }

}

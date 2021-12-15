package it.cnr.isti.labsedc.concern;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encrypt {
     
    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    	 //Creating a Signature object
        Signature sign = Signature.getInstance("SHA256withRSA");
        
        //Creating KeyPair generator object
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        
        //Initializing the key pair generator
        keyPairGen.initialize(2048);
        
        //Generating the pair of keys
        KeyPair pair = keyPairGen.generateKeyPair();      
  	
        //Creating a Cipher object
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
          
        //Initializing a Cipher object
        cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
  	  
        //Adding data to the cipher
        byte[] input = "4342.377,01025.914".getBytes();	  
        cipher.update(input);
  	  
        //encrypting the data
        byte[] cipherText = cipher.doFinal();	 
        System.out.println(new String(cipherText, "UTF8"));
             
        System.out.println("DECRYPT");
                             
        //Getting the public key from the key pair
        PublicKey publicKey = pair.getPublic();  

        //Initializing a Cipher object
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
  	  
        cipher.update(cipherText);

        //Initializing the same cipher for decryption
        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
        
        //Decrypting the text
        byte[] decipheredText = cipher.doFinal(cipherText);
        System.out.println(new String(decipheredText));
    }
 
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

/**
 *
 * @author Edwin
 */
public class VigenereCipher {
    
    public String Encrypt(String plaintext, String key, int charSize){
        String res="";
        int j=0;
        int keyLength = key.length();
        int offsetK = (charSize==26)? 97:0;
        for(int i=0; i<plaintext.length(); i++){
            if(plaintext.charAt(i) != ' '){
                int offsetP = (charSize==26)? ((int)plaintext.charAt(i)>=65 &&(int)plaintext.charAt(i)<=90? 65:97):0;
                res += (char)((((int)plaintext.charAt(i)-offsetP + (int)key.charAt(j)-offsetK) % charSize)+offsetP);
                j = (j+1) % keyLength;
            }
            else{
                res += plaintext.charAt(i);
            }
        }
        return res;
    }
    
    public String Decrypt(String ciphertext, String key, int charSize){
        String res="";
        int j=0;
        int keyLength = key.length();
        int offsetK = (charSize==26)? 97:0;
        for(int i=0; i<ciphertext.length(); i++){
            if(ciphertext.charAt(i) != ' '){
                int offsetP = (charSize==26)? ((int)ciphertext.charAt(i)>=65 &&(int)ciphertext.charAt(i)<=90? 65:97):0;
                res += (char)(Math.floorMod(((int)ciphertext.charAt(i)-offsetP - ((int)key.charAt(j)-offsetK)),charSize)+offsetP);
                j = (j+1) % keyLength;
            }
            else{
                res += ciphertext.charAt(i);
            }
        }
        return res;
    }
    
}

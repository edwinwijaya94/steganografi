/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegano;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Edwin
 */
public class MessageLoader {
    //attr
    String message;
    static String[] byteMessage;
    
    public MessageLoader(){
        byteMessage = new String[10000];
        for(int i=0;i<10000;i++){
            byteMessage[i] = "";
        }
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    // convert message to matrix of byte
    public static void toByteMessage(String message){
        int idxSegmen = 0;
	byte[] valuesDefault = message.getBytes();
        
        for(int i=0; i<valuesDefault.length; i++){
            String s = String.format("%8s", Integer.toBinaryString(valuesDefault[i] & 0xFF)).replace(' ', '0');
            if ((i != 0) && (i % 8 == 0)){
                idxSegmen++;
                byteMessage[idxSegmen] += s;
            } else {
                byteMessage[idxSegmen] += s;
            }
        }
        for(int i=0;i<=idxSegmen;i++){
            System.out.println(byteMessage[i]);
        }
    }
    
    public String[] getByteMessage(){
        return this.byteMessage;
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {

        MessageLoader m = new MessageLoader();
	// The string we want to convert.
	String letters = "abcdefghijklmnopqrstuvwxyzas";
	System.out.println(letters);
        
        m.toByteMessage(letters);
    }
}

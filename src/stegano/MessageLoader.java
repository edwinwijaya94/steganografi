/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegano;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Edwin
 */
public class MessageLoader {
    //attr
    String message;
    ArrayList<ArrayList<Byte>> byteMessage;
    
    public MessageLoader(){
        
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    // convert message to matrix of byte
    private void toByteMessage(){
        
    }
    
    private ArrayList<ArrayList<Byte>> getByteMessage(){
        return this.byteMessage;
    }
    
}

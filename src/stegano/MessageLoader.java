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
    static ArrayList<String> byteMessage;
    static ArrayList<ArrayList<String>> regions; // 8x8 pixel region
    
    public MessageLoader(){
        byteMessage = new ArrayList<String>();
        regions = new ArrayList<ArrayList<String>>();
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    // convert message to matrix of byte
    public static ArrayList<String> toByteMessage(String message){
	byte[] valuesDefault = message.getBytes();
        
        for(int i=0; i<valuesDefault.length; i++){
            String s = String.format("%8s", Integer.toBinaryString(valuesDefault[i] & 0xFF)).replace(' ', '0');
            byteMessage.add(s);
        }
        return byteMessage;
    }
    
    public static void toRegions(ArrayList<String> byteMessage){
        int idxSegmen = 0;
        regions.add(new ArrayList<String>());
        for(int i=0; i<byteMessage.size(); i++){
            if ((i != 0) && (i % 8 == 0)){
                idxSegmen++;
                regions.add(new ArrayList<String>());
                regions.get(idxSegmen).add(byteMessage.get(i));
            } else {
                regions.get(idxSegmen).add(byteMessage.get(i));
            }
        }
        for(int i=0;i<=idxSegmen;i++){
            System.out.println(regions.get(i));
        }
    }
    
    public int complexity(ArrayList<String> byteMessage){
        int k = 0;
        for(int i=0;i<byteMessage.size();i++){
            for(int j=0;j<byteMessage.get(i).length();j++){
                if (byteMessage.get(i).charAt(j) == '0'){
                    if ((j < byteMessage.get(i).length()-1) && (i < byteMessage.size()-1)){
                        if (byteMessage.get(i).charAt(j+1) == '1'){
                            k++;
                        } else if (byteMessage.get(i+1).charAt(j) == '1'){
                            k++;
                        }
                    } else if ((j < byteMessage.get(i).length()-1) && (i == byteMessage.size()-1)){
                        if (byteMessage.get(i).charAt(j+1) == '1'){
                            k++;
                        }
                    } else if ((j == byteMessage.get(i).length()-1) && (i < byteMessage.size()-1)){
                        if (byteMessage.get(i+1).charAt(j) == '1'){
                            k++;
                        }
                    }
                } else {
                    if ((j < byteMessage.get(i).length()-1) && (i < byteMessage.size()-1)){
                        if (byteMessage.get(i).charAt(j+1) == '0'){
                            k++;
                        } else if (byteMessage.get(i+1).charAt(j) == '0'){
                            k++;
                        }
                    } else if ((j < byteMessage.get(i).length()-1) && (i == byteMessage.size()-1)){
                        if (byteMessage.get(i).charAt(j+1) == '0'){
                            k++;
                        }
                    } else if ((j == byteMessage.get(i).length()-1) && (i < byteMessage.size()-1)){
                        if (byteMessage.get(i+1).charAt(j) == '0'){
                            k++;
                        }
                    }
                }
            }
        }
        return k;
    }
    
    public boolean isNoiseLikeRegion(ArrayList<String> byteMessage){
        double n = 112;
        double alpha;
        double threshold = 0.3;
        alpha = (double)complexity(byteMessage)/n;
        //System.out.println(alpha);
        
        if (alpha >= threshold){
            return true;
        } else {
            return false;
        }
    }
    
    public ArrayList<String> getByteMessage(){
        return this.byteMessage;
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {

        MessageLoader m = new MessageLoader();
        ArrayList<String> al = new ArrayList<String>();
	// The string we want to convert.
	String letters = "VincentTheophilusCiputraABC";
	System.out.println(letters);
        
        al = m.toByteMessage(letters);
        System.out.println(al);
        m.toRegions(al);
        for(int i=0;i<regions.size();i++){
            System.out.println(m.complexity(regions.get(i)));
            System.out.println(m.isNoiseLikeRegion(regions.get(i)));
        }
    }
}

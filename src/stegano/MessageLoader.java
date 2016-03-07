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
    ArrayList<String> byteMessage;
    ArrayList<String> wcPattern;
    ArrayList<Integer> conjugationMap;
    public ArrayList<ArrayList<String>> regions; // 8x8 pixel region
    public static double threshold;
            
    public MessageLoader(){
        byteMessage = new ArrayList<String>();
        regions = new ArrayList<ArrayList<String>>();
        conjugationMap = new ArrayList<Integer>();
        wcPattern = new ArrayList<String>();
        for(int i=0;i<8;i++){
            if (i % 2 == 0){
                wcPattern.add("01010101");
            } else {
                wcPattern.add("10101010");
            }
        }
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    // convert message to matrix of byte
    public ArrayList<String> toByteMessage(String message){
	byte[] valuesDefault = message.getBytes();
        
        for(int i=0; i<valuesDefault.length; i++){
            String s = String.format("%8s", Integer.toBinaryString(valuesDefault[i] & 0xFF)).replace(' ', '0');
            byteMessage.add(s);
        }
        return byteMessage;
    }
    
    public String toStringMessage(ArrayList<String> segmen){
        String output = "";
        for(int i=0;i<segmen.size();i++){
            for(int j=0;j<=segmen.get(i).length()-8;j+=8){
                int k = Integer.parseInt(segmen.get(i).substring(j, j+8), 2);
                output += (char) k;
            } 
        }
        
        return output;
    }
    
    public void printMessage(ArrayList<ArrayList<String>> region){
        String s = "";
        for(int i=0;i<region.size();i++){
            s += toStringMessage(region.get(i));
        }
        System.out.println(s);
    }
    
    public void toRegions(ArrayList<String> byteMessage){
        int idxSegmen = 0;
        int size = byteMessage.size();
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
        while (size % 8 != 0){
            regions.get(idxSegmen).add("00000000");
            size++;
        }
        for(int i=0;i<=idxSegmen;i++){
            System.out.println(regions.get(i));
        }
    }
    
    public static int complexity(ArrayList<String> byteMessage){
        int k = 0;
        for(int i=0;i<byteMessage.size();i++){
            for(int j=0;j<byteMessage.get(i).length();j++){
                if (byteMessage.get(i).charAt(j) == '0'){
                    if ((j < byteMessage.get(i).length()-1) && (i < byteMessage.size()-1)){
                        if (byteMessage.get(i).charAt(j+1) == '1'){
                            k++;
                        }
                        if (byteMessage.get(i+1).charAt(j) == '1'){
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
                        }
                        if (byteMessage.get(i+1).charAt(j) == '0'){
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
    
    public static boolean isNoiseLikeRegion(ArrayList<String> byteMessage, double threshold){
        double n = 112;
        double alpha;
//        double threshold = 0.3;
        alpha = (double)complexity(byteMessage)/n;
//        System.out.println("alpha");
//        System.out.println(alpha);
        if (alpha >= threshold){
            return true;
        } else {
            return false;
        }
    }
    
    public static String xor(String a, String b){
        String res = new String();
        for(int i=0;i<a.length();i++){
            if (a.charAt(i) == b.charAt(i)){
                res += "0";
            } else {
                res += "1";
            }
        }
        
        return res;
    }
    
    public void conjugateRegion(){
        for(int i=0;i<regions.size();i++){
            if(!isNoiseLikeRegion(regions.get(i),threshold)){
                conjugationMap.add(i);
                for(int j=0;j<8;j++){
                    regions.get(i).set(j, xor(regions.get(i).get(j),wcPattern.get(j)));
                }
            }
        }
    }
    
    public void reverseConjugateRegion(){
        int idxConjugate = 0;
        if(!conjugationMap.isEmpty()){
            for(int i=0;i<regions.size();i++){
                if(i == conjugationMap.get(idxConjugate)){
                    for(int j=0;j<8;j++){
                        regions.get(i).set(j, xor(regions.get(i).get(j),wcPattern.get(j)));
                    }
                    idxConjugate++;
                }
            }
        }
        
    }
    
    public ArrayList<String> getByteMessage(){
        return this.byteMessage;
    }
    
    public void main(String[] args) {

//        MessageLoader m = new MessageLoader();
//        ArrayList<String> al = new ArrayList<String>();
//	// The string we want to convert.
//	String letters = "Vincent Theophilus Ciputra";
//	System.out.println(letters);
//        
//        al = m.toByteMessage(letters);
//        System.out.println(al);
//        m.toRegions(al);
//        for(int i=0;i<regions.size();i++){
//            System.out.println(m.complexity(regions.get(i)));
//            System.out.println(m.isNoiseLikeRegion(regions.get(i)));
//        }
//        printMessage(regions);
//        System.out.println();
//        System.out.println("Conjugate Region:");
//        m.conjugateRegion();
//        for(int i=0;i<regions.size();i++){
//            System.out.println(m.complexity(regions.get(i)));
//            System.out.println(m.isNoiseLikeRegion(regions.get(i)));
//        }
//        printMessage(regions);
//        System.out.println();
//        m.reverseConjugateRegion();
//        System.out.println();
//        System.out.println("Reverse Conjugate Region:");
//        for(int i=0;i<regions.size();i++){
//            System.out.println(m.complexity(regions.get(i)));
//            System.out.println(m.isNoiseLikeRegion(regions.get(i)));
//        }
//        printMessage(regions);
    }
}

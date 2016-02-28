/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegano;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.util.Pair;

/**
 *
 * @author Edwin
 */
public class BPCS {
    //attr
    float threshold;
    byte[] stegoByteArray;
    ArrayList<ArrayList<ArrayList<String>>> imageMtxBitPlane; // image bit planes from all regions
    ArrayList<Pair<Integer,Integer>> imageTargetBitPlane; // target bit plane
    public ArrayList<ArrayList<String>> messageRegions; // message in region format
    int imageWidth;
    
    // temp
    ArrayList<ArrayList<String>> region;
    ArrayList<ArrayList<ArrayList<String>>> arrRegion;
    ArrayList<ArrayList<ArrayList<String>>> stegoRegions; // one dimensional regions
    
    
    public BPCS(){
        this.imageMtxBitPlane  = new ArrayList<>(); 
        this.imageTargetBitPlane  = new ArrayList<>();
        this.messageRegions  = new ArrayList<>();
        
        //temp
        region = new ArrayList<>();
        arrRegion = new ArrayList<>();
        stegoRegions = new ArrayList<>();
    }
    
    public void setThreshold(float threshold){
        this.threshold = threshold;
    }
    
    public float getThreshold(){
        return this.threshold;
    }
    
    public void setImageMtxBitPlane(ArrayList<ArrayList<ArrayList<String>>> imageMtxBitPlane, int imageWidth){
        this.imageMtxBitPlane = imageMtxBitPlane;
        this.imageWidth = imageWidth;
    }
    
    public ArrayList<ArrayList<ArrayList<String>>> getImageMtxBitPlane(){
        return this.imageMtxBitPlane;
    }
    
    public void setImageTargetBitPlane(ArrayList<Pair<Integer,Integer>> imageTargetBitPlane){
        this.imageTargetBitPlane = imageTargetBitPlane;
    }
    
    public ArrayList<Pair<Integer,Integer>> getImageTargetBitPlane(){
        return this.imageTargetBitPlane;
    }
    
    // insert message to byteImage then put to stegoImage
    public int doStegano(){
        if(messageRegions.size() > imageTargetBitPlane.size()){
            return 0; // over payload
        }
        else{
            for(int i=0; i<messageRegions.size(); i++){
                int a = imageTargetBitPlane.get(i).getKey(); // get region idx
                int b = imageTargetBitPlane.get(i).getValue(); // get region's bit plane idx
                imageMtxBitPlane.get(a).set(b, messageRegions.get(i)); // replace bit plane with message region
            }
        }
        return 1; // OK
    }
    
    public void toStegoByteArray(){
        System.out.println("bitplane " +imageMtxBitPlane.size());
        for(int i=0; i<imageMtxBitPlane.size(); i++){
            
            if(i%(Math.ceil((float)imageWidth/8)) == 0){ // allocate new image row
                initArrRegion();
                stegoRegions.addAll(arrRegion);
            }
            
            // convert arr bit plane to byte region
            String[][] tempReg = new String[8][8];
            for(int x=0; x<8; x++){
                for(int y=0; y<8; y++)
                    tempReg[x][y]="";
            }
//            System.out.println("img bit i" +imageMtxBitPlane.get(i).size());
            for(int j=0; j<imageMtxBitPlane.get(i).size(); j++){
                ArrayList<String>curBitPlane = imageMtxBitPlane.get(i).get(j); // current bit plane
                //System.out.println(curBitPlane.get(0));
                // append cur bit plane to temp region
                for(int k=0; k<8; k++){
                    for(int l=0; l<8; l++){
                        tempReg[k][l] += curBitPlane.get(k).charAt(l);
                        //System.out.println("temp reg " +tempReg[k][l].length());
                    }
                }
            }
            //put to region
            ArrayList<ArrayList<String>> Reg = new ArrayList<>();
            
            for(int k=0; k<8; k++){
                ArrayList<String> dummy = new ArrayList<String>();
                for(int p = 0;p<8;p++){
                    dummy.add("");
                }
                Reg.add(dummy);
            }
            
            for(int k=0; k<8; k++){
                for(int l=0; l<8; l++){
                    //System.out.println("temp reg "+tempReg[k][l].length());
                  Reg.get(k).set(l,tempReg[k][l]);
                }
            }
            stegoRegions.set(i, Reg);
        }
        
        // convert regions to byte array
        ArrayList<Byte> tempByteArray= new ArrayList<>();
        System.out.println("stego regions " +stegoRegions.size());
        for(int i=0; i<stegoRegions.size(); i++){
            //get from a region
            for(int j=0; j<8; j++){
                for(int k=0; k<8; k++){
                    //System.out.println("stegoreg" + stegoRegions.get(i).get(j).get(k));
                    tempByteArray.add((byte)Integer.parseInt(stegoRegions.get(i).get(j).get(k),2)); //binary string to byte
                }
            }
        }
        
        stegoByteArray = new byte[tempByteArray.size()];
        for (int i = 0; i < tempByteArray.size(); i++) {
            stegoByteArray[i] = tempByteArray.get(i);
        }
        
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(stegoByteArray.length);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        
    }
    
    public byte[] getStegoByteArray(){
        return this.stegoByteArray;
    }
    
    public void initRegion(){
        
        for(int i = 0;i<8;i++){
            ArrayList<String> tempInput= new ArrayList<>();
            for (int j = 0;j<8;j++){
                tempInput.add("00000000000000000000000000000000");
            }
            region.add(tempInput);
            System.out.println(tempInput.size());
        }
    }
    
    public void initArrRegion(){
        ArrayList<ArrayList<String>> tempInput= new ArrayList<ArrayList<String>>();
        initRegion();
        for(int i = 0;i<Math.ceil((float)imageWidth/8);i++){
            arrRegion.add(region);
        }
    }
    
    
    // count PSNR val
    public float getPSNR(){
        
        return 0;
    }
    
}

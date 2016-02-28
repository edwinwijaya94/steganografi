/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegano;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author Edwin
 */
public class BPCS {
    //attr
    float threshold;
    ArrayList<ArrayList<Byte>> stegoImage;
    ArrayList<ArrayList<ArrayList<String>>> imageMtxBitPlane; // image bit planes from all regions
    ArrayList<Pair<Integer,Integer>> imageTargetBitPlane; // target bit plane
    static ArrayList<ArrayList<String>> messageRegions; // message in region format
    public BPCS(){
    
    }
    
    public void setThreshold(float threshold){
        this.threshold = threshold;
    }
    
    public float getThreshold(){
        return this.threshold;
    }
    
    public void setImageMtxBitPlane(ArrayList<ArrayList<ArrayList<String>>> imageMtxBitPlane){
        this.imageMtxBitPlane = imageMtxBitPlane;
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
    
    public ArrayList<ArrayList<Byte>> getStegoImage(){
        return this.stegoImage;
    }
            
    // count PSNR val
    public float getPSNR(){
        
        return 0;
    }
    
}

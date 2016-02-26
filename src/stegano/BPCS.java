/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegano;

import java.util.ArrayList;

/**
 *
 * @author Edwin
 */
public class BPCS {
    //attr
    ArrayList<ArrayList<Byte>> byteImage;
    float threshold;
    ArrayList<ArrayList<ArrayList<Byte>>> regions; // 8x8 pixel region
    ArrayList<ArrayList<Byte>> stegoImage;
    
    public BPCS(){
    
    }
    
    private void setThreshold(float threshold){
        this.threshold = threshold;
    }
    
    private void setByteImage(ArrayList<ArrayList<Byte>> byteImage){
        this.byteImage = byteImage;
    }
    
    private float getThreshold(){
        return this.threshold;
    }
    
    private ArrayList<ArrayList<Byte>> getByteImage(ArrayList<ArrayList<Byte>> byteImage){
        return this.byteImage;
    }
    
    //add pixels if image size not multiple of 8x8 pixels
    private void addPixels(){
        
    }
        
    
    //set image regions, size 8x8 pixels
    private void setRegions(){
        
    }
    
    //get region complexity
    private float getComplexity(ArrayList<ArrayList<Byte>> region){
                
        return 0;
    }
    
    //get region index that pass threshold value
    private ArrayList<Integer> getTargetRegionsIndex(){
        
        return null;
    }
    
    //conjugate region to increase complexity
    private void conjugateRegion(ArrayList<ArrayList<Byte>> region){
        
    }
   
    
    private void toCGC(){
        
    }
    
    private void toPBC(){
        
    }
    
    // insert message to byteImage then put to stegoImage
    private void doStegano(){
        
    }
    
    private ArrayList<ArrayList<Byte>> getStegoImage(){
        return this.stegoImage;
    }
            
    // count PSNR val
    private float getPSNR(){
        
        return 0;
    }
    
}

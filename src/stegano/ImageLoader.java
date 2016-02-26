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
public class ImageLoader {
    //attr
    BufferedImage image;
    ArrayList<ArrayList<Byte>> byteImage;
    
    public ImageLoader(){
        
    }
    
    private void setImage(BufferedImage image){
        this.image = image;
    }
    
    private BufferedImage getImage(){
        return this.image;
    }
    
    // convert image to matrix of byte
    private void toByteImage(){
        
    }
    
    private ArrayList<ArrayList<Byte>> getByteImage(){
        return this.byteImage;
    }
    
}

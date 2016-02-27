/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegano;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 *
 * @author Edwin
 */
public class ImageLoader {
    //attr
    BufferedImage image;
    ArrayList<ArrayList<Byte>> byteImage;
    int width;
    int height;
    
    public ImageLoader(){
        byteImage = new ArrayList<ArrayList<Byte>>();
    }
    
    public void setImage(BufferedImage image){
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        System.out.println("width" + width);
        System.out.println("height" + height);
    }
    
    public BufferedImage getImage(){
        return this.image;
    }
    
    // convert image to matrix of byte
    public void toByteImage(){
        ArrayList<Byte> tempByte = null;
//        for (int x = 0; x < width; x++)
//        {
//            for (int y = 0; y < height; y++)
//            {
//                int color = image.getRGB(x, y);
//                System.out.println(color >> 8);
//                tempByte = new ArrayList<Byte>();
//                tempByte.add((byte)(color >> 8));
//            }
//            byteImage.add(tempByte);
//            tempByte.clear();
//        }
//        
//        for (int x = 0; x < width; x++)
//        {
//            for (int y = 0; y < height; y++)
//            {
//                int color = image.getRGB(x, y);
//                System.out.print(byteImage.get(x).get(y));
//            }
//            System.out.println();
//        }
        byte[] imageBytes = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        System.out.println(imageBytes.length);
        
        int count = 0;
        for (int i = 0;i<height;i++){
            tempByte = new ArrayList<>();
            for (int j = 0;j<width*3;j++){
                tempByte.add(imageBytes[count]);
//                System.out.println(imageBytes[i][j]);
                String s = String.format("%32s", Integer.toBinaryString(imageBytes[count] & 0xFF)).replace(' ', '0');
//                System.out.print(s+ " ");
                count++;
            }
//            System.out.println(tempByte);
            byteImage.add(tempByte);
            tempByte.clear();
        }
    }
    
    public ArrayList<ArrayList<Byte>> getByteImage(){
        return this.byteImage;
    }
    
}

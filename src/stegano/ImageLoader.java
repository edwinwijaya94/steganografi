/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegano;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.util.Pair;
import javax.imageio.ImageIO;

/**
 *
 * @author Edwin
 */
public class ImageLoader {
    //attr
    BufferedImage image;
    ArrayList<ArrayList<Byte>> byteImage;
    public ArrayList<ArrayList<String>> binaryImage;
    ArrayList<ArrayList<ArrayList<String>>> arrRegion;
    ArrayList<ArrayList<String>> region;
    ArrayList<ArrayList<ArrayList<ArrayList<String>>>> imageRegions;
    ArrayList<String> bitPlane;
    ArrayList<ArrayList<String>> arrBitPlane;
    public ArrayList<ArrayList<ArrayList<String>>> mtxBitPlane;
    public ArrayList<ArrayList<ArrayList<String>>> mtxBitPlaneCGC;
    ArrayList<Pair<Integer,Integer>> targetBitPlane;
    public int width;
    public int height;
    byte[] imageBytes;
    
    
    public ImageLoader(){
        byteImage = new ArrayList<ArrayList<Byte>>();
        binaryImage = new ArrayList<ArrayList<String>>();
        imageRegions = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>();
        arrRegion = new ArrayList<ArrayList<ArrayList<String>>>();
        region = new ArrayList<ArrayList<String>>();
        bitPlane = new ArrayList<String>();
        arrBitPlane = new ArrayList<ArrayList<String>>();
        mtxBitPlane = new ArrayList<ArrayList<ArrayList<String>>>();
        mtxBitPlaneCGC = new ArrayList<ArrayList<ArrayList<String>>>();
        targetBitPlane = new ArrayList<Pair<Integer,Integer>>();
    }
    
    public void setImage(BufferedImage image){
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        System.out.println("width" + width);
        System.out.println("height" + height);
    }
    
    public void setImageBytes(byte[] imgBytes){
        imageBytes = imgBytes;
    }
   
    
    public BufferedImage getImage(){
        return this.image;
    }
    
    // convert image to matrix of byte
    public void toByteImage() throws IOException{
        ArrayList<Byte> tempByte = null;
        ArrayList<String> tempBinary = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "bmp", baos);
        byte[] bytes = baos.toByteArray();
        imageBytes = new byte[bytes.length];
        imageBytes = bytes;
        //System.out.println(imageBytes.length);
        
        int count = 0;
        int countBin = 0;
        for (int i = 0;i<height;i++){
            tempByte = new ArrayList<>();
            tempBinary = new ArrayList<>();
            for (int j = 0;j<width*3;j++){
                tempByte.add(imageBytes[count]);
                count++;
            }
            for (int j = 0;j<width;j++){
//                if(j%4 == 0){
//                    String s= "11111111"; // alpha
//                    tempBinary.add(s);
//                }
                String s = String.format("%8s", Integer.toBinaryString(imageBytes[countBin] & 0xFF)).replace(' ', '0');
                tempBinary.add(s);
                countBin++;
            }
            binaryImage.add(tempBinary);
            
            byteImage.add(tempByte);
        }
//        this.width = binaryImage.get(0).size();
//        this.height = binaryImage.size();
    }
    
    public void toRegions(){
        ArrayList<ArrayList<ArrayList<String>>> tempArrRegion = new ArrayList<>();
        System.out.println("bin img size "+binaryImage.size());
        for (int i = 0;i<binaryImage.size();i++){
            if (i%8==0){
                initArrRegion();
                System.out.println("arrRegSizeBefore" + arrRegion.size());
                
                imageRegions.add(arrRegion);
                arrRegion = new ArrayList<>();
                //System.out.println("arrRegSizeAfter" + arrRegion.size());
            }
            
            for(int j=0;j<binaryImage.get(i).size();j++){
                imageRegions.get(i/8).get(j/8).get(i%8).set(j%8,binaryImage.get(i).get(j));
            }
        }
        System.out.println("region size" +imageRegions.get(1).size() +" " +imageRegions.size());
    }
    
    public void printRegion(ArrayList<ArrayList<String>> inputArr){
        for (int i =0;i<inputArr.size();i++){
            for(int j=0;j<inputArr.get(i).size();j++){
                System.out.print(inputArr.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
   
    public void initRegion(){
        
        for(int i = 0;i<8;i++){
            ArrayList<String> tempInput= new ArrayList<>();
            for (int j = 0;j<8;j++){
                tempInput.add("00000000");
            }
            region.add(tempInput);
//            System.out.println(tempInput.size());
        }
        
//        System.out.println("region size"+region.size());
        
    }
    
    public void initArrRegion(){
        region = new ArrayList<>();
        initRegion();
        
        for(int i = 0;i<Math.ceil((float)(width*3)/8);i++){
            arrRegion.add(region);
        }
        
        //System.out.println("arrreg i"+arrRegion.get(1).size());
    }
    
    public void makeBitPlane(ArrayList<ArrayList<String>> reg,int bitPlaneIdx){
        bitPlane =  new ArrayList<>();
        for (int i =0;i<reg.size();i++){
            String temp="";
            for (int j = 0;j<reg.get(i).size();j++){
                temp+= reg.get(i).get(j).charAt(bitPlaneIdx);
            }
            bitPlane.add(temp);
        }
        arrBitPlane.add(bitPlane);
    }
    
    public void makeArrBitPlane(ArrayList<ArrayList<String>> reg){
        arrBitPlane = new ArrayList<>();
        for (int i = 0;i<8;i++){
            makeBitPlane(reg,i);
        }
    }
    
    public void allRegionBitPlanes(){
        for (int i = 0;i<imageRegions.size();i++){
            for (int j = 0;j<imageRegions.get(i).size();j++){
                makeArrBitPlane(imageRegions.get(i).get(j));
                //System.out.println("img reg i j size"+ imageRegions.get(i).get(j).size());
                mtxBitPlane.add(arrBitPlane);
                mtxBitPlaneCGC.add(arrBitPlane);
            }
        }
    }
    
    public BufferedImage createImageFromBytes(byte[] imageData) throws IOException {

        BufferedImage newImg = null;
        newImg = new BufferedImage(height,width,BufferedImage.TYPE_3BYTE_BGR);
        newImg.setData(Raster.createRaster(newImg.getSampleModel(), new DataBufferByte(imageData,imageData.length), new Point()));
        ImageIO.write(newImg, "bmp",new File("newPict.bmp"));
        return newImg;
    }
    
    public void countComplexity(){
        for(int i = 0;i<mtxBitPlane.size();i++){
            for (int j = 0;j<mtxBitPlane.get(i).size();j++){
                if (MessageLoader.isNoiseLikeRegion(mtxBitPlane.get(i).get(j))){
                    targetBitPlane.add(new Pair(i,j));
                }
            }
        }
    }
    
    public void toCGC(){
        for (int i = 0;i<mtxBitPlane.size();i++){
            for (int j = 0;j<mtxBitPlane.get(i).size();j++){
                for (int k = 0;k<8;k++){
                    String tempStr = new String();
                    tempStr += mtxBitPlane.get(i).get(j).get(k).charAt(0);
                    tempStr += MessageLoader.xor(mtxBitPlane.get(i).get(j).get(k).substring(1, 8), mtxBitPlane.get(i).get(j).get(k).substring(0, 7));
                    mtxBitPlaneCGC.get(i).get(j).set(k, tempStr);
                }                
            }
        }
    }
    
    public void toPBC(){
        for (int i = 0;i<mtxBitPlane.size();i++){
            for (int j = 0;j<mtxBitPlane.get(i).size();j++){
                System.out.println("[");
                for (int k = 0;k<8;k++){
                    String tempStr = new String();
                    tempStr += mtxBitPlane.get(i).get(j).get(k).charAt(0);
                    tempStr += MessageLoader.xor(mtxBitPlaneCGC.get(i).get(j).get(k).substring(1, 8), mtxBitPlane.get(i).get(j).get(k).substring(0, 7));
                    System.out.println(mtxBitPlaneCGC.get(i).get(j).get(k).substring(1, 8) + " XOR " + mtxBitPlane.get(i).get(j).get(k).substring(0, 7));
                    System.out.println("tempStr " + tempStr);
                    mtxBitPlane.get(i).get(j).set(k, tempStr);
                }                
                System.out.println("]");
            }
        }
    }
    
    public void printComplexity(){
        System.out.println(targetBitPlane);
    }

    public void printMTXBitPlane(){
        System.out.println(mtxBitPlane);
    }
    
    public void printMTXBitPlaneCGC(){
        System.out.println(mtxBitPlaneCGC);
    }
    
    public ArrayList<ArrayList<Byte>> getByteImage(){
        return this.byteImage;
    }
    
    public ArrayList<ArrayList<String>> getBinaryImage(){
        return this.binaryImage;
    }
    
    public byte[] getImageBytes(){
        return imageBytes;
    } 
    
    
}

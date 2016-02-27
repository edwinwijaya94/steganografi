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
    ArrayList<ArrayList<String>> binaryImage;
    ArrayList<ArrayList<ArrayList<String>>> arrRegion;
    ArrayList<ArrayList<String>> region;
    ArrayList<ArrayList<ArrayList<ArrayList<String>>>> imageRegions;
    ArrayList<String> bitPlane;
    ArrayList<ArrayList<String>> arrBitPlane;
    ArrayList<ArrayList<ArrayList<String>>> mtxBitPlane;
    int width;
    int height;
    
    
    public ImageLoader(){
        byteImage = new ArrayList<ArrayList<Byte>>();
        binaryImage = new ArrayList<ArrayList<String>>();
        imageRegions = new ArrayList<ArrayList<ArrayList<ArrayList<String>>>>();
        arrRegion = new ArrayList<ArrayList<ArrayList<String>>>();
        region = new ArrayList<ArrayList<String>>();
        bitPlane = new ArrayList<String>();
        arrBitPlane = new ArrayList<ArrayList<String>>();
        mtxBitPlane = new ArrayList<ArrayList<ArrayList<String>>>();
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
        ArrayList<String> tempBinary = null;
        byte[] imageBytes = ((DataBufferByte) image.getData().getDataBuffer()).getData();
        System.out.println(imageBytes.length);
        
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
                String s = String.format("%32s", Integer.toBinaryString(imageBytes[countBin] & 0xFF)).replace(' ', '0');
                tempBinary.add(s);
                countBin++;
            }
//            System.out.println(tempBinary);
//            System.out.println("before" + binaryImage.size());
            binaryImage.add(tempBinary);
//            System.out.println(tempBinary);
//            System.out.println("after" + binaryImage.size());
            byteImage.add(tempByte);
//            System.out.println(binaryImage.size());
//            System.out.println(binaryImage.get(1).size());
//            tempByte.clear();
//            tempBinary.clear();
        }
//        System.out.println(binaryImage);
    }
    
    public void toRegions(){
        ArrayList<ArrayList<ArrayList<String>>> tempArrRegion = new ArrayList<>();
        ArrayList<ArrayList<String>> tempRegion = null;
//         System.out.println(binaryImage.get(1).size());
        for (int i = 0;i<binaryImage.size();i++){
            if (i%8==0){
                initArrRegion();
//                System.out.println("Arr Reg" +arrRegion.size());
                imageRegions.add(arrRegion);
                arrRegion = new ArrayList<>();
            }
//            System.out.println(imageRegions.get(i/8).size());
            
            for(int j=0;j<binaryImage.get(i).size();j++){
//                if (j%8==0){
//                    tempRegion = initRegion(tempRegion);
//                    imageRegions.get(i).add(tempRegion);
//                }
//                System.out.println(binaryImage.get(i).get(j));
//                System.out.println("i,j " + i/8 + " " + j/8);
//                System.out.println(binaryImage.get(i).get(j));
                imageRegions.get(i/8).get(j/8).get(i%8).set(j%8,binaryImage.get(i).get(j));
//                System.out.println("img reg" + imageRegions.get(i/8).get(j/8).get(i%8).get(j%8));
//                printRegion(imageRegions.get(i/8).get(j/8));
            }
            
        }
        System.out.println(imageRegions.get(1).size());
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
                tempInput.add("00000000000000000000000000000000");
            }
            region.add(tempInput);
            System.out.println(tempInput.size());
        }
        
    }
    
    public void initArrRegion(){
        ArrayList<ArrayList<String>> tempInput= new ArrayList<ArrayList<String>>();
        initRegion();
        for(int i = 0;i<Math.ceil((float)width/8);i++){
            arrRegion.add(region);
        }
    }
    
    public void makeBitPlane(ArrayList<ArrayList<String>> reg,int bitPlaneIdx){
        bitPlane =  new ArrayList();
        for (int i =0;i<reg.size();i++){
            String temp="";
//            System.out.println(reg.get(i).size());
            for (int j = 0;j<reg.get(i).size();j++){
                temp+= reg.get(i).get(j).charAt(bitPlaneIdx);
            }
            bitPlane.add(temp);
//            System.out.println(temp);
        }
        arrBitPlane.add(bitPlane);
    }
    
    public void makeArrBitPlane(ArrayList<ArrayList<String>> reg){
        arrBitPlane = new ArrayList();
        for (int i = 0;i<32;i++){
            makeBitPlane(reg,i);
        }
    }
    
    public void allRegionBitPlanes(){
//        System.out.println(imageRegions.size());
//        System.out.println(imageRegions.get(1).size());
//        System.out.println(imageRegions.get(0).get(0));
        for (int i = 0;i<imageRegions.size();i++){
            for (int j = 0;j<imageRegions.get(i).size();j++){
//                System.out.println("i "+ i + "j " + j);
                makeArrBitPlane(imageRegions.get(i).get(j));
                mtxBitPlane.add(arrBitPlane);
//                System.out.println(imageRegions.get(i).get(j).get(0).size());
            }
        }
    }

    public void printArrBitPlane(){
        System.out.println(mtxBitPlane);
    }
    
    public ArrayList<ArrayList<Byte>> getByteImage(){
        return this.byteImage;
    }
    
    public ArrayList<ArrayList<String>> getBinaryImage(){
        return this.binaryImage;
    }
    
}
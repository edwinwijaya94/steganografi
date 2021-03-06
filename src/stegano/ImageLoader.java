/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stegano;

import gui.SteganoGUI;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

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
    public ArrayList<Pair<Integer,Integer>> targetBitPlane;
    public int width;
    public int height;
    public byte[] header;
    public byte[] imageBytes;
    public byte[] oriBytes;
    public byte[] OutImage;
    public String imagePath;
    
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
        this.width = (int) (72 *Math.ceil((float)(this.width)/24)); //resize image width for RGB image
        this.height = image.getHeight();
        this.height = (int) (8 *Math.ceil((float)(this.height)/8)); //resize image height
        System.out.println("new width" + width);
        System.out.println("new height" + height);
    }
    
    public void setImageBytes(byte[] imgBytes){
        imageBytes = imgBytes;
    }
   
    
    public BufferedImage getImage(){
        return this.image;
    }
    
    // convert image to matrix of byte
    public void toByteImage() throws IOException{
        
        if (imagePath.substring(imagePath.length() - 3).equals("png")){
            BufferedImage bufferedImage;
            try {	
                //read image file
                bufferedImage = ImageIO.read(new File(imagePath));
                // create a blank, RGB, same width and height, and a white background
                BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                              bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
                // write to bmp file
                ImageIO.write(newBufferedImage, "bmp", new File("asd.bmp"));
                System.out.println("Done");
              } catch (IOException e) {
                e.printStackTrace();
              }

            BufferedImage myPicture = null;
            myPicture = ImageIO.read(new File("asd.bmp"));
            setImage(myPicture);
        }
        
        
        ArrayList<Byte> tempByte = null;
        ArrayList<String> tempBinary = null;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "bmp", baos);
        baos.flush();
        byte[] bytes = baos.toByteArray();
        oriBytes = bytes.clone();
        baos.close();
       header = Arrays.copyOfRange(bytes,0,54);
        
        imageBytes = Arrays.copyOfRange(bytes,54,bytes.length);
//        for(int i=0; i<100; i++){
//            System.out.println("ori "+imageBytes[i]);
//        }
        System.out.println("ori bytes length "+imageBytes.length);
        System.out.println("img bytes");
//        System.out.println(Arrays.copyOfRange(imageBytes, 0, 100));
        int count = 0;
        int countBin = 0;
        for (int i = 0;i<image.getHeight();i++){
            tempByte = new ArrayList<>();
            tempBinary = new ArrayList<>();
            for (int j = 0;j<image.getWidth()*3;j++){
                tempByte.add(imageBytes[count]);
                count++;
            }
            for (int j = 0;j<image.getWidth()*3;j++){
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
        
    }
    
    public void toRegions(){
        ArrayList<ArrayList<ArrayList<String>>> tempArrRegion;
        System.out.println("bin img size "+binaryImage.size() + " " + binaryImage.get(0).size());
        int count=0;
//        System.out.println("binary image before");
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++)
//                System.out.print(binaryImage.get(i).get(j) + " ");
//            System.out.println();
//        }
        for (int i = 0;i<binaryImage.size();i++){
            initArrRegion();
            if (i%8==0){
//                System.out.println("arrRegSizeBefore" + arrRegion.size());
                tempArrRegion = new ArrayList<>((ArrayList<ArrayList<ArrayList<String>>>) arrRegion.clone());
//                tempArrRegion = ;
                imageRegions.add(tempArrRegion);
                //System.out.println("arrRegSizeAfter" + arrRegion.size());
            }
            
            for(int j=0;j<binaryImage.get(i).size();j++){

//                System.out.println("img reg size, i size :"+ imageRegions.size()+" "+imageRegions.get(0).size());
//                System.out.println("i/8 j/8 i%8 j%8 : "+i/8+" "+j/8+" "+i%8+" "+j%8);
                imageRegions.get(i/8).get(j/8).get(i%8).set(j%8,binaryImage.get(i).get(j));
            }
        }
//        System.out.println("count " + count);
//        System.out.println("binary image");
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++)
//                System.out.print(binaryImage.get(i).get(j) + " ");
//            System.out.println();
//        }
//        System.out.println("region");
//        for(int i=0; i<8; i++){
//            for(int j=0; j<8; j++)
//                System.out.print(imageRegions.get(0).get(1).get(i).get(j) + " ");
//            System.out.println();
//        }
        
        //System.out.println("region size" +imageRegions.get(1).size() +" " +imageRegions.size());
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
        region = new ArrayList<>();
        ArrayList<String> input= new ArrayList<>();
        for (int j = 0;j<8;j++){
            input.add("00000000");
        }
        
        for(int i = 0;i<8;i++){
            
            ArrayList<String> tempInput = new ArrayList<>((ArrayList<String>)input.clone());
            region.add(tempInput);
//            System.out.println(tempInput.size());
        }
        
//        System.out.println("region size"+region.size());
        
    }
    
    public void initArrRegion(){
        arrRegion = new ArrayList<>();
        
        
        for(int i = 0;i<width/8;i++){
            initRegion();
            ArrayList<ArrayList<String>>tempReg;
            tempReg = new ArrayList<>();
            tempReg = (ArrayList<ArrayList<String>>) region.clone();
            arrRegion.add(tempReg);
        }
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
//                if(i==0 && j==1){
//                    System.out.println("arrbitplane");
//                    System.out.println(arrBitPlane);
//                }
                mtxBitPlane.add(arrBitPlane);
                mtxBitPlaneCGC.add(arrBitPlane);
            }
        }
    }
    
    public void test(ArrayList<ArrayList<ArrayList<ArrayList<String>>>> a){
        for (int i = 0;i<a.size();i++){
            for (int j = 0;j<a.get(i).size();j++){
                makeArrBitPlane(a.get(i).get(j));
                //System.out.println("img reg i j size"+ imageRegions.get(i).get(j).size());
                mtxBitPlane.add(arrBitPlane);
                mtxBitPlaneCGC.add(arrBitPlane);
            }
        }
    }
    
    public byte[] concatByte(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c= new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
    
    public BufferedImage createImageFromBytes(byte[] imageData) throws IOException {
        FileOutputStream out = null;
        System.out.println("image data size "+ imageData.length);
        try {
            out = new FileOutputStream("newPict2.bmp");
            header[18] = (byte)(width/3); //resize width
            header[22] = (byte)(height); // resize height
            System.out.println(Arrays.copyOfRange(imageData, 0, 100));
            OutImage = concatByte(header, imageData);
            out.write(OutImage);
            out.flush();
            System.out.println("inputfile");
        } finally {
            if (out != null) out.close();
        }
        BufferedImage newImg = new BufferedImage(width/3, height, BufferedImage.TYPE_3BYTE_BGR);
        try{
            newImg = ImageIO.read(new File("newPict2.bmp"));
        }
        catch (IOException e) { }
        
        if (imagePath.substring(imagePath.length() - 3).equals("png")){
            BufferedImage bufferedImage;
            try {	
                //read image file
                bufferedImage = ImageIO.read(new File("newPict2.bmp"));
                // create a blank, RGB, same width and height, and a white background
                BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                              bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
                // write to bmp file
                ImageIO.write(newBufferedImage, "png", new File("out.png"));
                System.out.println("Done");
              } catch (IOException e) {
                e.printStackTrace();
              }
            newImg = ImageIO.read(new File("out.png"));
        }
        
        
        return newImg;
        
    }
    
    public void countComplexity(){
        for(int i = 0;i<mtxBitPlane.size();i++){
            for (int j = 0;j<mtxBitPlane.get(i).size();j++){
                System.out.println("ML thres: " + MessageLoader.threshold);
                if (MessageLoader.isNoiseLikeRegion(mtxBitPlane.get(i).get(j),MessageLoader.threshold)){
//                    System.out.println("masuk");
                    System.out.println("complexity: "+MessageLoader.complexity(mtxBitPlane.get(i).get(j)));
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

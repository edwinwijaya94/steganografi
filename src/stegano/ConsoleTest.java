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
public class ConsoleTest {
    public static void main(String[] args){
        ImageLoader IL = new ImageLoader();
        BPCS bpcs = new BPCS();
        
        ArrayList<ArrayList<String>> dummy = new ArrayList<>();
        for (int i = 0;i<8;i++){
            ArrayList<String> temp = new ArrayList();
            for (int j = 0;j<8;j++){
                temp.add("10001000");
            }
            dummy.add(temp);
        }
            
        ArrayList<ArrayList<String>> dummy2 = new ArrayList<>();
        for (int i = 0;i<8;i++){
            ArrayList<String> temp = new ArrayList();
            for (int j = 0;j<8;j++){
                temp.add("11110000");
            }
            dummy2.add(temp);
        }
        
        ArrayList<ArrayList<String>> dummy3 = new ArrayList<>();
        for (int i = 0;i<8;i++){
            ArrayList<String> temp = new ArrayList();
            for (int j = 0;j<8;j++){
                temp.add("00011000");
            }
            dummy3.add(temp);
        }
        
        ArrayList<ArrayList<String>> dummy4 = new ArrayList<>();
        for (int i = 0;i<8;i++){
            ArrayList<String> temp = new ArrayList();
            for (int j = 0;j<8;j++){
                temp.add("10010010");
            }
            dummy4.add(temp);
        }
        
        ArrayList<ArrayList<ArrayList<ArrayList<String>>>> mtx = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> arr = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> arr2 = new ArrayList<>();
        arr.add(dummy);arr.add(dummy2);
        mtx.add(arr);
        arr2.add(dummy3);arr2.add(dummy4);
        mtx.add(arr2);
        
//        System.out.println("ArrBitPlane "+IL.arrBitPlane);
        IL.test(mtx);
        
        bpcs.imageMtxBitPlane = IL.mtxBitPlane;
        bpcs.toStegoByteArray();
        System.out.println("mtx "+bpcs.stegoByteArray);
        for(int i =0;i<bpcs.stegoByteArray.length;i++){
            System.out.println("bytee " + bpcs.stegoByteArray[i]);
        }
//        BPCS bpcs = new BPCS();
//        ArrayList<ArrayList<ArrayList<String>>> mtx = new ArrayList<>();
//        ArrayList<ArrayList<String>> arrBP = new ArrayList<>();
//        ArrayList<String>BP = new ArrayList<>();
//        for(int i=0; i<8; i++)
//            BP.add("00000000");
//        ArrayList<String>BP2 = new ArrayList<>();
//        for(int i=0; i<8; i++)
//            BP2.add("11111111");
//        for(int i=0; i<8; i++){
//            if(i<4 ){
//                arrBP.add(BP);
//            }
//            else
//                arrBP.add(BP2);
//        }
//        mtx.add(arrBP);
//        System.out.println("BP");
//        System.out.println(BP);
//        System.out.println("arrBP");
//        System.out.println(arrBP);
//        System.out.println("mtx");
//        System.out.println(mtx);
//        System.out.println("init "+bpcs.getImageMtxBitPlane().size());
//        bpcs.setImageMtxBitPlane(mtx,8);
//        bpcs.toStegoByteArray();
    }
}

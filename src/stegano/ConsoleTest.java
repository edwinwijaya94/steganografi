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
        BPCS bpcs = new BPCS();
        ArrayList<ArrayList<ArrayList<String>>> mtx = new ArrayList<>();
        ArrayList<ArrayList<String>> arrBP = new ArrayList<>();
        ArrayList<String>BP = new ArrayList<>();
        for(int i=0; i<8; i++)
            BP.add("00000000");
        ArrayList<String>BP2 = new ArrayList<>();
        for(int i=0; i<8; i++)
            BP2.add("11111111");
        for(int i=0; i<8; i++){
            if(i<4 ){
                arrBP.add(BP);
            }
            else
                arrBP.add(BP2);
        }
        mtx.add(arrBP);
        System.out.println("BP");
        System.out.println(BP);
        System.out.println("arrBP");
        System.out.println(arrBP);
        System.out.println("mtx");
        System.out.println(mtx);
        System.out.println("init "+bpcs.getImageMtxBitPlane().size());
        bpcs.setImageMtxBitPlane(mtx,8);
        bpcs.toStegoByteArray();
    }
}

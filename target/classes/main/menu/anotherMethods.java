/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.menu;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.RowFilter.Entry;
//import java.util.Map.Entry;
/**
 *
 * @author hardi
 */
public class anotherMethods extends menuUtama{
    PreparedStatement pst = null;
    ResultSet rs = null;
    Statement st =null;
    ResultSetMetaData rsmd = null;

    public static void main(String[] args){
        
    }
    public void pemilih(ArrayList<Integer> loopPem, ArrayList<Integer> arrPem){
    Integer objPem = loopPem.size();
    for(Integer pemilih : objPem){
        Integer counPem = arrPem.get(pemilih);
        if(arrPem.add(pemilih)){
            System.out.println("Pemilih : " + pemObj);
        }
    }
}
    public void pilihan1(ArrayList<Object> loopPil1,ArrayList<Object> arrPil1){
    Object[] objPil1 = loopPil1.toArray();
    for(Object pilihan1 : objPil1){
        if(arrPil1.add(pilihan1)){
            Integer pil1Obj = arrPil1.toString();
            System.out.println("Pilihan 1 :" + pil1Obj);
            }
        }
    }
    public void pilihan2(ArrayList<Object> loopPil2,ArrayList<Object> arrPil2){
    Object[] objPil2 = loopPil2.toArray();
    for(Object pilihan2 : objPil2){
        if(arrPil2.add(pilihan2)){
            String pil2Obj = arrPil2.toString();
            System.out.println("Pilihan 1 :" + pil2Obj);
            }
        }
    }
    
    
//    public void Pilihan1(ArrayList<Integer> pil1Arr, Map<Object, Integer> pil1AndCount, Map<Object, Integer> pemArr){
//    //Looping Pilihan 1
//        Object[] arrObjPil1 = pil1Arr.toArray();
//        for(Object pilihan1 : arrObjPil1){
//            Integer countPil1 = pil1AndCount.get(pilihan1);
//            if(countPil1 == null){
//                pil1AndCount.put(pilihan1, 1);
//            }else{
//                pil1AndCount.put(pilihan1, ++countPil1);
//            }
//        }
//        Set<Map.Entry<Object, Integer>> entryPil1 = pil1AndCount.entrySet();
//        for(Map.Entry<Object, Integer> ePilihan1 : entryPil1){
//            //Looping Pemilih
//            if(ePilihan1.getValue() >= 1){
//                System.out.println("\nKey :" + ePilihan1.getKey() + "\nValue:" + ePilihan1.getValue());
//            }
//        }
//    }
//    public void Pilihan2(ArrayList<Integer> pil2Arr, Map<Object, Integer> pil2AndCount){
//    //Looping Pilihan 2
//        Object[] arrObjPil2 = pil2Arr.toArray();
//        for(Object pilihan2 : arrObjPil2){
//            Integer countPil2 = pil2AndCount.get(pilihan2);
//            if(countPil2 == null){
//                pil2AndCount.put(pilihan2, 1);
//            }else{
//                pil2AndCount.put(pilihan2, ++countPil2);
//                System.out.println(pil2AndCount);
//            }
//        }
//        Set<Map.Entry<Object, Integer>> entryPil2 = pil2AndCount.entrySet();
//        for(Map.Entry<Object, Integer> entry : entryPil2){
//            if(entry.getValue() >= 1){
//                System.out.println("\nKey :" + entry.getKey() + "\nValue:" + entry.getValue());
//            }
//        }
//    }
}

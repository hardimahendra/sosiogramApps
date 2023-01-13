/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.menu;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
/**
 *
 * @author hardi
 */
public class sqliteDB {
    Connection conn = null;
    public static Connection connect(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:sosiometri.db");
            System.out.println("Connect");
            return conn;
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
    }
    public static void main(String[] args){
        connect();
    }
}

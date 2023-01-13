/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package main.menu;

import java.awt.Cursor;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static jdk.internal.org.jline.keymap.KeyMap.key;
import org.h2.util.JdbcUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Writer;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import static java.util.Map.entry;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import static sun.util.locale.LocaleUtils.isEmpty;
/**
 *
 * @author Hardi Mahendra
 */
public class menuUtama extends javax.swing.JFrame {
    public PreparedStatement pst;
    public ResultSet rs;
    public Statement st;
    public ResultSetMetaData rsmd ;
    public Connection conn = sqliteDB.connect();
    /**
     * Creates new form JavaJFrame
     */
    public menuUtama() {
        initComponents();
        tampilkanData();
        selectTabelJumlah();
        reset();
        tabelTambahData.setAutoCreateRowSorter(true);

        //disable all button kecuali input data
        btnTambah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSimpan.setEnabled(false);
        btnTabulasi.setEnabled(false);
        btnSosiogram.setEnabled(false);

        // Menyembunyikan/disable all field
        fieldJudul.setEnabled(false);
        fieldJumlahData.setEnabled(false);

    }    
    
    // ==============================Validasi untuk field cek duplikat=============================
    public void validasiFieldEmpty(){
        //fungsi untuk memvalidasi field ketika tombol tambah ditekan
        if(fieldJumlahData.getText().trim().isEmpty() && fieldNama.getText().trim().isEmpty() && 
            fieldPemilih.getText().trim().isEmpty()&& fieldPilihan1.getText().trim().isEmpty() && 
            fieldPilihan2.getText().trim().isEmpty()){
                JOptionPane.showMessageDialog(null , 
                "Form Input Tidak Boleh Kosong, kecuali Judul", "Peringatan!!", JOptionPane.PLAIN_MESSAGE);
         return;
        }else
        if(fieldJumlahData.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null , 
                    "Masukan Jumlah Data", "Peringatan!!", JOptionPane.PLAIN_MESSAGE);
            return;
        }else
        if(fieldNama.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null , 
            "Masukan Nama", "Peringatan!!", JOptionPane.PLAIN_MESSAGE);
            return;
        }else
        if(fieldPemilih.getText().isEmpty()){            
            JOptionPane.showMessageDialog(null , 
            "Masukan No Pemilih", "Peringatan!!", JOptionPane.PLAIN_MESSAGE);
            return;
        }else
        if(fieldPilihan1.getText().isEmpty()){
            JOptionPane.showMessageDialog(null , 
            "Masukan No Pilihan 1", "Peringatan!!", JOptionPane.PLAIN_MESSAGE);
            return;
        }else
        if(fieldPilihan2.getText().isEmpty()){
            JOptionPane.showMessageDialog(null , 
            "Masukan No Pilihan 2", "Peringatan!!", JOptionPane.PLAIN_MESSAGE);
            return;
        }else if(true){
            validasiField();
            addTabelData();
            enableProses();
        }
    }
    public void validasiField(){
    String pilihan1 = fieldPilihan1.getText().trim();
    String pilihan2 = fieldPilihan2.getText().trim();
     
    if(fieldPemilih.getText().trim().equals(pilihan1) ||
        fieldPemilih.getText().trim().equals(pilihan2)){
            JOptionPane.showMessageDialog(null , 
                "No Pemilih tidak boleh sama dengan No Pilihan 1 atau Pilihan 2", "Duplikat Data!!", JOptionPane.PLAIN_MESSAGE);
                fieldPilihan1.setText(null);
                fieldPilihan2.setText(null);
            return;
        }else 
            if(pilihan1.equals(pilihan2)){
                JOptionPane.showMessageDialog(null , 
                "No pilihan 1 atau Pilihan 2 tidak boleh sama", "Duplikat Data!!", JOptionPane.PLAIN_MESSAGE);
                fieldPilihan1.setText(null);
        return;
        }
        // ============validasi agar field pemilih tidak duplikat di database================
        try{
            String noPemilih = fieldPemilih.getText();
            int intPemilih = Integer.parseInt(noPemilih);
            String sql = "SELECT * FROM data_angket where pemilih="+intPemilih+"";
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()){
                JOptionPane.showMessageDialog(this, 
                "Nomor "+noPemilih+", Sudah tersedia");
                fieldPemilih.setText(null);
            return;
        }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        return;
        }finally{
            try{
                st.close();
                rs.close();
            }catch(Exception e){}
        }

    }
    
    //================Validasi untuk membuka tombol tabulasi========================    
    public void enableProses(){
    String jumD = "SELECT jumlah_data from data_jumlah";
    String getData = "SELECT COUNT(*) FROM data_angket";
    try{
        PreparedStatement jumlahD = conn.prepareStatement(jumD);
        ResultSet rSJD = jumlahD.executeQuery();
        pst = conn.prepareStatement(getData);
        rs = pst.executeQuery();
        String pemilih = rs.getString(1);
        String jumlahData = rSJD.getString(1);
        if(pemilih.equals(jumlahData)){
            fieldJudul.setText(null);
            fieldJumlahData.setText(null);
            btnTambah.setEnabled(false);
            btnTabulasi.setEnabled(true);
        return;
        }
    }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Pastikan Pemilih tidak kosong!");
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){}
        }
    }
    
    //=================== Untuk menambahkan data dari field ke database ==============================
    public void addTabelData(){
        try{
            String jD = "SELECT judul, jumlah_data FROM data_jumlah";
            String sql3 = "INSERT INTO data_angket (pemilih, nama, pilihan_1, pilihan_2) VALUES(?,?,?,?)";
            String insJudJum = "INSERT INTO data_jumlah (judul, jumlah_data) VALUES (?,?)";
            rs = st.executeQuery(jD);
            String noPemilih = fieldPemilih.getText();
            String noPilihan1 = fieldPilihan1.getText();
            String noPilihan2 = fieldPilihan2.getText();
            String judul = rs.getString(1);
            String jumlah = rs.getString(2);
            int intPemilih = Integer.parseInt(noPemilih);
            int intPilihan1 = Integer.parseInt(noPilihan1);
            int intPilihan2 = Integer.parseInt(noPilihan2);
            int getJumlah = rs.getInt(2);
                if(judul == null && jumlah == null){
                    pst = conn.prepareStatement(insJudJum);
                    pst.setString(1, fieldJudul.getText());
                    pst.setString(2, fieldJumlahData.getText());
                    pst.executeUpdate();
                    fieldJudul.setEnabled(false);
                    fieldJumlahData.setEnabled(false);
                    selectTabelJumlah();
                return;
            }else 
                if(judul != null && jumlah != null){
                    //============= validasi agar data yang dimasukan field pemilih, pilihan1 dan pilihan 2 tidak boleh melebihi jumlah data =============
                    if(intPemilih > getJumlah){
                        JOptionPane.showMessageDialog(null, 
                        "Jumlah dari Pemilih tidak boleh melebihi jumlah data", "Peringatan", JOptionPane.OK_OPTION);
                        fieldPemilih.setText(null);
                    return;
                    }else 
                        if((intPilihan1 > getJumlah) || (intPilihan2 > getJumlah)){
                            JOptionPane.showMessageDialog(null, 
                            "Jumlah dari Pilihan 1 dan Pilihan 2 tidak boleh melebihi jumlah data", "Peringatan", JOptionPane.OK_OPTION);
                            fieldPilihan1.setText(null);
                            fieldPilihan2.setText(null);
                    return;
                    }else{
                    pst = conn.prepareStatement(sql3);
                    pst.setString(1, fieldPemilih.getText());
                    pst.setString(2, fieldNama.getText());
                    pst.setString(3, fieldPilihan1.getText());
                    pst.setString(4, fieldPilihan2.getText());
                    pst.executeUpdate();
                    tampilkanData();
                    reset();
                return;
                }
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }finally{
            try{
                pst.close();
                st.close();
                rs.close();
            }catch(Exception e){}
        }
    }
    //====================== Select and delete data from tabel======================
    //Untuk menampilkan data pada database di tabel aplikasi 
    public void tampilkanData(){
        try{
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM data_angket");
            tabelTambahData.setModel(DbUtils.resultSetToTableModel(rs));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }finally{
            try{
                rs.close();
                st.close();
            }catch(Exception e){}
        }
    }
    
    //Menampilkan nilai pada Judul & jumlah data
    public void selectTabelJumlah(){
        String sql = "SELECT judul, jumlah_data FROM data_jumlah";
        try{
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            labelJudul.setText(rs.getString(1));
            labelJumlahData.setText(rs.getString(2));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }finally{
            try{
                rs.close();
                pst.close();
            }catch(Exception e){}
        }
    }
    //Method edit data
    public void editSelectedData(){
    try{
        st = conn.createStatement();
        st.executeUpdate("UPDATE data_angket SET nama='" + fieldNama.getText()+ "', pilihan_1='" + fieldPilihan1.getText()+"', pilihan_2='" + fieldPilihan2.getText()+"' WHERE pemilih='"+fieldPemilih.getText()+"'");
        tampilkanData();
        JOptionPane.showMessageDialog(null, "Update Berhasil");
        reset();
    }catch(Exception e){
        e.printStackTrace();
        }finally{
            try{
                st.close();
            }catch(Exception e){}
        }
    }
    //Method untuk menghapus data yang dipilih
    public void deleteSelectdData(){
    int row = tabelTambahData.getSelectedRow();
    String select = tabelTambahData.getModel().getValueAt(row, 0).toString();
    try{
        String sql = "DELETE FROM data_angket WHERE pemilih =" + select;
        pst = conn.prepareStatement(sql);
        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data Dihapus");
        tampilkanData();
        reset();
      }catch(Exception e){
        JOptionPane.showMessageDialog(null, e);
      }finally{
        try{
            pst.close();
        }catch(Exception e){}
        }
    }
    
    //Method untuk menghapus data ketika keluar aplikasi
    public void deleteTabelDatabase(){
    try{
        String sql = "DELETE FROM data_jumlah";
        pst = conn.prepareStatement(sql);
        pst.executeUpdate();
        selectTabelJumlah();
        fieldJudul.setEnabled(true);
        fieldJumlahData.setEnabled(true);
        fieldJudul.setText(null);
        fieldJumlahData.setText(null);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
    }finally{
        try{
            pst.close();
        }catch(Exception e){}
    }
    try{
        String sql = "DELETE FROM data_angket";
        pst = conn.prepareStatement(sql);
        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Semua Data Telah Dihapus");
        reset();
        tampilkanData();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }finally{
            try{
                pst.close();
            }catch(Exception e){}
        }
    }
    // ==================Methods pendukung===========================
    public void reset(){
        fieldNama.setText(null);
        fieldPemilih.setText(null);
        fieldPilihan1.setText(null);
        fieldPilihan2.setText(null);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSimpan.setEnabled(false);
    }
    
    public void close(){
    WindowEvent closeWindow = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
    deleteTabelDatabase();
    }
    public void getSelectedRow(){
        int row = tabelTambahData.getSelectedRow();
        TableModel modelT = tabelTambahData.getModel();
        fieldPemilih.setText(modelT.getValueAt(row,0).toString());
        fieldNama.setText(modelT.getValueAt(row,1).toString());
        fieldPilihan1.setText(modelT.getValueAt(row,2).toString());
        fieldPilihan2.setText(modelT.getValueAt(row,3).toString());
    }
    
    //================ Untuk tabel tabulasi =====================

    
//    public int getValue(String pemilih){
//    int getRow = tabelTambahData.getRowCount();
//        for(int i=0; i < getRow; i++){
//            if(pemilih.equals(tabelTambahData.getValueAt(i, 1).toString()))
//            return i;
//        }
//    return -1;
//    }   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        panelSosiogram = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panelInput = new javax.swing.JPanel();
        btnInput = new javax.swing.JButton();
        btnTabulasi = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnSosiogram = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        fieldJudul = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        JumlahData = new javax.swing.JLabel();
        fieldJumlahData = new javax.swing.JTextField();
        fieldNama = new javax.swing.JTextField();
        Nama = new javax.swing.JLabel();
        btnTambah = new javax.swing.JButton();
        fieldPilihan1 = new javax.swing.JTextField();
        pemilih = new javax.swing.JLabel();
        fieldPilihan2 = new javax.swing.JTextField();
        pilihan1 = new javax.swing.JLabel();
        pilihan2 = new javax.swing.JLabel();
        fieldPemilih = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        labelJumlahData = new javax.swing.JLabel();
        labelJudul = new javax.swing.JLabel();
        btnTest = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        panelTabel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelTambahData = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelTabulasi = new javax.swing.JTable();
        labelTabelData = new javax.swing.JLabel();
        labelTabelIndeks = new javax.swing.JLabel();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 255));
        setResizable(false);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setText("Sosiometri");

        panelSosiogram.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Sosiogram");

        javax.swing.GroupLayout panelSosiogramLayout = new javax.swing.GroupLayout(panelSosiogram);
        panelSosiogram.setLayout(panelSosiogramLayout);
        panelSosiogramLayout.setHorizontalGroup(
            panelSosiogramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSosiogramLayout.createSequentialGroup()
                .addGap(180, 180, 180)
                .addComponent(jLabel4)
                .addContainerGap(194, Short.MAX_VALUE))
        );
        panelSosiogramLayout.setVerticalGroup(
            panelSosiogramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSosiogramLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnInput.setText("Input Data");
        btnInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInputActionPerformed(evt);
            }
        });

        btnTabulasi.setText("Indeks Tabulasi");
        btnTabulasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabulasiActionPerformed(evt);
            }
        });

        btnExit.setText("Keluar");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnSosiogram.setText("Sosiogram");

        btnHapus.setText("Hapus Data");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        fieldJudul.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Judul");

        JumlahData.setText("Jumlah Data");

        fieldJumlahData.setBackground(new java.awt.Color(255, 255, 255));
        fieldJumlahData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldJumlahDataKeyTyped(evt);
            }
        });

        fieldNama.setBackground(new java.awt.Color(255, 255, 255));

        Nama.setText("Nama");

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        fieldPilihan1.setBackground(new java.awt.Color(255, 255, 255));
        fieldPilihan1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldPilihan1KeyTyped(evt);
            }
        });

        pemilih.setText("Pemilih");

        fieldPilihan2.setBackground(new java.awt.Color(255, 255, 255));
        fieldPilihan2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldPilihan2KeyTyped(evt);
            }
        });

        pilihan1.setText("Pilihan 1");

        pilihan2.setText("Pilihan 2");

        fieldPemilih.setBackground(new java.awt.Color(255, 255, 255));
        fieldPemilih.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldPemilihKeyTyped(evt);
            }
        });

        jLabel6.setText("Judul             :");

        jLabel7.setText("Jumlah Data :");

        labelJumlahData.setText("-");

        labelJudul.setText("-");

        btnTest.setText("Test");
        btnTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit Data");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInputLayout = new javax.swing.GroupLayout(panelInput);
        panelInput.setLayout(panelInputLayout);
        panelInputLayout.setHorizontalGroup(
            panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelInputLayout.createSequentialGroup()
                            .addGap(1, 1, 1)
                            .addComponent(jLabel1))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInputLayout.createSequentialGroup()
                            .addComponent(fieldJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(JumlahData)))
                    .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(fieldJumlahData, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fieldNama, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelInputLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(26, 26, 26)
                                .addComponent(labelJudul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(panelInputLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(25, 25, 25)
                                .addComponent(labelJumlahData, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(Nama)
                    .addGroup(panelInputLayout.createSequentialGroup()
                        .addComponent(btnInput, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTabulasi))
                    .addComponent(btnSosiogram))
                .addGap(18, 18, 18)
                .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInputLayout.createSequentialGroup()
                        .addComponent(btnExit)
                        .addGap(18, 18, 18)
                        .addComponent(btnSimpan))
                    .addGroup(panelInputLayout.createSequentialGroup()
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnTest)
                            .addGroup(panelInputLayout.createSequentialGroup()
                                .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelInputLayout.createSequentialGroup()
                                        .addComponent(pemilih)
                                        .addGap(28, 28, 28))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInputLayout.createSequentialGroup()
                                        .addComponent(fieldPemilih, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelInputLayout.createSequentialGroup()
                                        .addComponent(pilihan1)
                                        .addGap(23, 23, 23)
                                        .addComponent(pilihan2))
                                    .addGroup(panelInputLayout.createSequentialGroup()
                                        .addComponent(fieldPilihan1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(fieldPilihan2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInputLayout.createSequentialGroup()
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panelInputLayout.setVerticalGroup(
            panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInputLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInputLayout.createSequentialGroup()
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnInput)
                            .addComponent(btnTabulasi))
                        .addGap(18, 18, 18)
                        .addComponent(btnSosiogram)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnExit)
                        .addComponent(btnSimpan))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInputLayout.createSequentialGroup()
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelInputLayout.createSequentialGroup()
                                .addComponent(JumlahData)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(panelInputLayout.createSequentialGroup()
                                .addComponent(btnEdit)
                                .addGap(19, 19, 19)))
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldJumlahData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTambah)
                            .addComponent(btnHapus))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInputLayout.createSequentialGroup()
                        .addComponent(Nama)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldPemilih, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldPilihan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldPilihan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelJudul)
                            .addComponent(jLabel6))
                        .addGap(31, 31, 31)
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(labelJumlahData))
                        .addGap(24, 24, 24))
                    .addGroup(panelInputLayout.createSequentialGroup()
                        .addGroup(panelInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pemilih)
                            .addComponent(pilihan1)
                            .addComponent(pilihan2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(btnTest)
                .addGap(14, 14, 14))
        );

        panelTabel.setBackground(new java.awt.Color(204, 204, 204));

        tabelTambahData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pemilih", "Nama", "Pilihan 1", "Pilihan 2"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelTambahData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelTambahDataMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelTambahData);
        if (tabelTambahData.getColumnModel().getColumnCount() > 0) {
            tabelTambahData.getColumnModel().getColumn(0).setResizable(false);
            tabelTambahData.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabelTambahData.getColumnModel().getColumn(1).setResizable(false);
            tabelTambahData.getColumnModel().getColumn(1).setPreferredWidth(210);
            tabelTambahData.getColumnModel().getColumn(2).setResizable(false);
            tabelTambahData.getColumnModel().getColumn(2).setPreferredWidth(50);
            tabelTambahData.getColumnModel().getColumn(3).setResizable(false);
            tabelTambahData.getColumnModel().getColumn(3).setPreferredWidth(50);
        }

        tabelTabulasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pemilih", "Nama", "Bobot", "Nilai"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelTabulasi.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setViewportView(tabelTabulasi);
        if (tabelTabulasi.getColumnModel().getColumnCount() > 0) {
            tabelTabulasi.getColumnModel().getColumn(0).setResizable(false);
            tabelTabulasi.getColumnModel().getColumn(1).setResizable(false);
            tabelTabulasi.getColumnModel().getColumn(1).setPreferredWidth(210);
            tabelTabulasi.getColumnModel().getColumn(2).setResizable(false);
            tabelTabulasi.getColumnModel().getColumn(3).setResizable(false);
        }

        labelTabelData.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelTabelData.setForeground(new java.awt.Color(0, 0, 0));
        labelTabelData.setText("Tabel Tambah Data");

        labelTabelIndeks.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        labelTabelIndeks.setForeground(new java.awt.Color(0, 0, 0));
        labelTabelIndeks.setText("Tabel Indeks Tabulasi");

        javax.swing.GroupLayout panelTabelLayout = new javax.swing.GroupLayout(panelTabel);
        panelTabel.setLayout(panelTabelLayout);
        panelTabelLayout.setHorizontalGroup(
            panelTabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane2)
                .addGap(30, 30, 30))
            .addGroup(panelTabelLayout.createSequentialGroup()
                .addGap(178, 178, 178)
                .addComponent(labelTabelData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelTabelIndeks)
                .addGap(161, 161, 161))
        );
        panelTabelLayout.setVerticalGroup(
            panelTabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabelLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(panelTabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTabelData)
                    .addComponent(labelTabelIndeks))
                .addGap(18, 18, 18)
                .addGroup(panelTabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panelTabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(panelInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(panelSosiogram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(459, 459, 459))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSosiogram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(panelTabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInputActionPerformed
        btnTambah.setEnabled(true);
        btnInput.setEnabled(false);

        
        fieldJudul.setEnabled(true);
        fieldJumlahData.setEnabled(true);
        fieldNama.setEnabled(true);
        fieldPemilih.setEnabled(true);
        fieldPilihan1.setEnabled(true);
        fieldPilihan2.setEnabled(true);
    }//GEN-LAST:event_btnInputActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        validasiFieldEmpty();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
    if(JOptionPane.showConfirmDialog(null, 
        "Hapus Data yang dipilih ?", 
        "Apakah anda yakin ?", 
        JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
        deleteSelectdData();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
       if (JOptionPane.showConfirmDialog(null, 
            "Apakah Anda Yakin Ingin Keluar ?, Jika Keluar Semua Data akan ikut dihapus", "Terima Kasih ", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
           deleteTabelDatabase();
            System.exit(0);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    private void fieldJumlahDataKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldJumlahDataKeyTyped
    char karakter = evt.getKeyChar();
        if(!(((karakter >= '0') && (karakter <= '9') || 
                (karakter == KeyEvent.VK_BACK_SPACE) || 
                (karakter == KeyEvent.VK_DELETE)))){
        getToolkit().beep();
        evt.consume();
        }
    }//GEN-LAST:event_fieldJumlahDataKeyTyped

    private void fieldPemilihKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldPemilihKeyTyped
       char karakter = evt.getKeyChar();
        if(!(((karakter >= '0') && (karakter <= '9') || 
                (karakter == KeyEvent.VK_BACK_SPACE) || 
                (karakter == KeyEvent.VK_DELETE)))){
        getToolkit().beep();
        evt.consume();
        }
    }//GEN-LAST:event_fieldPemilihKeyTyped

    private void fieldPilihan1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldPilihan1KeyTyped
       char karakter = evt.getKeyChar();
        if(!(((karakter >= '0') && (karakter <= '9') || 
                (karakter == KeyEvent.VK_BACK_SPACE) || 
                (karakter == KeyEvent.VK_DELETE)))){
        getToolkit().beep();
        evt.consume();
        }
    }//GEN-LAST:event_fieldPilihan1KeyTyped

    private void fieldPilihan2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldPilihan2KeyTyped
       char karakter = evt.getKeyChar();
        if(!(((karakter >= '0') && (karakter <= '9') || 
                (karakter == KeyEvent.VK_BACK_SPACE) || 
                (karakter == KeyEvent.VK_DELETE)))){
        getToolkit().beep();
        evt.consume();
        }
    }//GEN-LAST:event_fieldPilihan2KeyTyped

    private void btnTabulasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabulasiActionPerformed
        
    }//GEN-LAST:event_btnTabulasiActionPerformed

    private void btnTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestActionPerformed
    try{
        anotherMethods aM = new anotherMethods();
        //ArrayList Object
        ArrayList<Object> pemArr = new ArrayList();
        ArrayList<Object> pemCount = new ArrayList();
        ArrayList<Object> pil1Arr = new ArrayList();
        ArrayList<Object> pil2Arr = new ArrayList();
        ArrayList<Object> arrPil1 = new ArrayList();
        ArrayList<Object> arrPil2 = new ArrayList();
        //Hash Method
        Map<Object, Integer> pemAndCount = new HashMap<>();
        Map<Object, Integer> pil1AndCount = new HashMap<>();
        Map<Object, Integer> pil2AndCount = new HashMap<>();
//        String sql = "INSERT INTO tabulasi (pemilih, nama, bobot_pemilih, nilai) VALUES(?,?,?,?)";
//        pst = conn.prepareStatement(sql);
        st = conn.createStatement();
        rs = st.executeQuery("SELECT * FROM data_angket");
        while(rs.next()){
            pemArr.add(rs.getInt(1)); 
            pil1Arr.add(rs.getInt(3)); //mengambil value dari database dan memasukannya ke dalam array
            pil2Arr.add(rs.getInt(4));
        }
        aM.pemilih(pemArr, pil1Arr, pil2Arr, pemCount, arrPil1, arrPil2);//pemArr merupakan variabel dari method void pada class anotherMethods
//        aM.Pilihan1(pil1Arr, pil1AndCount, pemArr);
        System.out.println("========================================================");
//        aM.Pilihan2(pil2Arr, pil2AndCount);
//        pil1AndCount.containsKey(pilihan1);
//        pil1AndCount.containsValue(count);
//        pil1AndCount.remove(pilihan1);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }finally{
            try{
                pst.close();
                st.close();
            }catch(Exception e){}
        }
    }//GEN-LAST:event_btnTestActionPerformed

    private void tabelTambahDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTambahDataMouseClicked
        getSelectedRow();
        btnHapus.setEnabled(true);
        btnEdit.setEnabled(true);
        btnSimpan.setEnabled(true);
    }//GEN-LAST:event_tabelTambahDataMouseClicked

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    if(JOptionPane.showConfirmDialog(null, 
        "Ubah Data yang dipilih ?", 
        "Apakah anda yakin ?", 
        JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
         editSelectedData();
        }                    
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSimpanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menuUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JumlahData;
    private javax.swing.JLabel Nama;
    public javax.swing.JButton btnEdit;
    public javax.swing.JButton btnExit;
    public javax.swing.JButton btnHapus;
    public javax.swing.JButton btnInput;
    public javax.swing.JButton btnSimpan;
    public javax.swing.JButton btnSosiogram;
    public javax.swing.JButton btnTabulasi;
    public javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTest;
    public javax.swing.JTextField fieldJudul;
    public javax.swing.JTextField fieldJumlahData;
    public javax.swing.JTextField fieldNama;
    public javax.swing.JTextField fieldPemilih;
    public javax.swing.JTextField fieldPilihan1;
    public javax.swing.JTextField fieldPilihan2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel labelJudul;
    public javax.swing.JLabel labelJumlahData;
    private javax.swing.JLabel labelTabelData;
    private javax.swing.JLabel labelTabelIndeks;
    private javax.swing.JPanel panelInput;
    private javax.swing.JPanel panelSosiogram;
    private javax.swing.JPanel panelTabel;
    private javax.swing.JLabel pemilih;
    private javax.swing.JLabel pilihan1;
    private javax.swing.JLabel pilihan2;
    private javax.swing.JTable tabelTabulasi;
    public javax.swing.JTable tabelTambahData;
    // End of variables declaration//GEN-END:variables

}

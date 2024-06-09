package id.rezka.tuprak9.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import id.rezka.tuprak9.config.DatabaseConnection;


public class DbManager {

    //Metode ini membuat tabel bernama Pengingat di database jika belum ada.
    public static void buatTabel(){

        //Deklarasi variabel tablesql yang berisi query SQL untuk membuat tabel Pengingat.
        String tablesql = "CREATE TABLE IF NOT EXISTS Pengingat ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " judul TEXT NOT NULL,"
                + " jenis_prioritas TEXT NOT NULL,"
                + " tanggal TEXT NOT NULL,"
                + " waktu TEXT NOT NULL,"
                + " deskripsi TEXT,"
                + " selesai BOOLEAN DEFAULT 0"
                + ");";
        try (Connection connection = DatabaseConnection.connect();
			Statement stmt = connection.createStatement()) {
			stmt.execute(tablesql);
			System.out.println("Tabel berhasil dibuat.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
    }
    
    //menandai tugas selesa
    public static void tandaSelesai(int id) {
        String sql = "UPDATE Pengingat SET selesai = 1 WHERE id = ?";
        try (Connection connection = DatabaseConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 

    //Metode statis saveData() yang digunakan untuk menyimpan data ke tabel Pengingat.
    public static void saveData(String judul, String jenisPrioritas, LocalDate tanggal, LocalTime waktu, String deskripsi, boolean selesai) {
        buatTabel();

        //Deklarasi variabel sql yang berisi query SQL untuk menyimpan data ke tabel Pengingat.
        String sql = "INSERT INTO Pengingat (judul, jenis_prioritas, tanggal, waktu, deskripsi, selesai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, judul); //Atur nilai parameter pertama dengan nilai judul.
            statement.setString(2, jenisPrioritas); //Atur nilai parameter kedua dengan nilai jenisPrioritas.
            statement.setString(3, tanggal.toString()); //
            statement.setString(4, waktu.toString());
            statement.setString(5, deskripsi);
            statement.setBoolean(6, selesai);
            //Jalankan query SQL untuk menyimpan data ke tabel Pengingat.
            statement.executeUpdate();
        //Blok kode untuk menangani pengecualian SQLException.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Definisikan metode static bernama loadData yang digunakan untuk memuat semua data dari tabel Pengingat.
    public static List<String[]> loadData() { // load data seluruhan yg blm selesai
        List<String[]> dataList = new ArrayList<>();
        String sql = "SELECT * FROM Pengingat WHERE selesai = 0";
        try (Connection connection = DatabaseConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = resultSet.getString(i + 1);
                }
                dataList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    //Metode statis loadDataForDate() yang digunakan untuk memuat data dari tabel Pengingat berdasarkan tanggal
    public static List<String[]> loadDataForDate(LocalDate tanggal) { //load data menurut tgl
        List<String[]> dataList = new ArrayList<>();
        String sql = "SELECT * FROM Pengingat WHERE tanggal = ? AND selesai = 0";
        try (Connection connection = DatabaseConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tanggal.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                while (resultSet.next()) {
                    String[] row = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row[i] = resultSet.getString(i + 1);
                    }
                    dataList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    // Load untuk completed tasks
    public static List<String[]> loadCompletedTasks() {
        List<String[]> dataList = new ArrayList<>();
        String sql = "SELECT * FROM Pengingat WHERE selesai = 1";
        try (Connection connection = DatabaseConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = resultSet.getString(i + 1);
                }
                dataList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    //Metode ini menghapus catatan dari tabel Pengingat di database berdasarkan ID-nya.
    public static void removeData(int id) {
        String sql = "DELETE FROM Pengingat WHERE id = ?";
        try (Connection connection = DatabaseConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Metode ini mencari catatan di tabel Pengingat di database berdasarkan kata kunci dan mengembalikannya sebagai daftar array.
    public static List<String[]> cariData(String keyword){
    List<String[]> resultList = new ArrayList<>();

    //Definisikan variabel sql dengan query SQL untuk memuat semua data dari tabel Pengingat.
    String sql = "SELECT * FROM Pengingat WHERE judul LIKE?";
    try (Connection connection = DatabaseConnection.connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            String cariKeyword = "%" + keyword + "%";
            statement.setString(1, cariKeyword);
            ResultSet resultSet = statement.executeQuery();

            int cekColumn =  resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                String[] baris = new String[cekColumn];
                for (int i = 0; i < cekColumn; i++){
                    baris[i] = resultSet.getString(i + 1);
                }
                resultList.add(baris);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultList;
    }

    //mengupdate informasi yang di edit
    public static void updateData(int id, String judul, String jenisPrioritas, LocalDate tanggal, LocalTime waktu, String deskripsi) {
        String sql = "UPDATE Pengingat SET judul = ?, jenis_prioritas = ?, tanggal = ?, waktu = ?, deskripsi = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, judul);
            statement.setString(2, jenisPrioritas);
            statement.setString(3, tanggal.toString());
            statement.setString(4, waktu.toString());
            statement.setString(5, deskripsi);
            statement.setInt(6, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metode untuk menghapus semua item
    public static void deleteAllItems() {
        String sql = "DELETE FROM Pengingat WHERE selesai = 0";
        try (Connection connection = DatabaseConnection.connect();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
}
}
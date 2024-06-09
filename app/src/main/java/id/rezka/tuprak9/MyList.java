package id.rezka.tuprak9;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import id.rezka.tuprak9.controller.DbManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyList {
    private static VBox list;
    // Metode createScene membuat dan mengembalikan sebuah Scene yang menampilkan daftar jadwal.
    public static Scene createScene(Stage primaryStage, App app) {
        
        // Membuat label untuk judul "My List"
        Label myListLabel = new Label("My List");
        myListLabel.setId("search-label");
    
        // Membuat ScrollPane untuk menampung daftar jadwal. ScrollPane memungkinkan konten untuk digulir jika lebih besar dari ukuran tampilan.
        ScrollPane scroll= new ScrollPane();
        scroll.setId("scroll-pane");

        // Membuat VBox untuk menampung Label yang berisi jadwal. VBox akan menampilkan komponen secara vertikal dengan jarak 10 piksel.
        list = new VBox(10);
        list.setPadding(new Insets(10)); // Menetapkan padding di sekitar VBox
        list.setId("list-box"); // Menetapkan ID untuk keperluan styling dengan CSS

        // upadate data terbaru
        updateList(primaryStage);

        // Menetapkan konten dari ScrollPane dengan VBox yang berisi daftar jadwal
        scroll.setContent(list);
        scroll.setFitToWidth(true); // Menetapkan agar konten sesuai dengan lebar ScrollPane

        // Membuat tombol kembali untuk kembali ke scene utama
        Button backButton = new Button();
        backButton.setMaxWidth(40);
        backButton.setMaxHeight(40);
        backButton.setId("bck-bttn");
        backButton.setOnAction(e -> {
            primaryStage.setScene(app.createMainScene(primaryStage)); ;
        }); // Kembali ke scene sebelumnyn
        try {
             // Setel ikon tombol kembali
            FileInputStream iconStream = new FileInputStream("src/main/resources/image/back-arrow.png");
            Image icon = new Image(iconStream);

            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            backButton.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        

        // Mengatur layout menggunakan BorderPane
        BorderPane layout = new BorderPane(); 
        layout.setTop(myListLabel); // Menetapkan label "My List" di bagian atas
        layout.setCenter(scroll); // Menetapkan ScrollPane di bagian tengah
        layout.setBottom(backButton); // Menetapkan tombol kembali di bagian bawah
        BorderPane.setAlignment(myListLabel, Pos.TOP_CENTER);
        BorderPane.setAlignment(scroll, Pos.TOP_CENTER);
        BorderPane.setMargin(myListLabel, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(scroll, new Insets(0, 10, 0, 10));
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(10, 10, 10, 20));
        layout.setId("lyt-list");// Menetapkan ID untuk keperluan styling dengan CSS

         // Membuat Scene baru dengan layout yang sudah dibuat, dan menetapkan ukuran serta stylesheet
        Scene scene = new Scene(layout, 500, 600);
        scene.getStylesheets().add("/styles/stylesMyList.css");
        scene.getStylesheets().add("/styles/stylesDetail.css");

        return scene;
        
    }

    public static void updateList(Stage primaryStage){
        if (list != null) {
            list.getChildren().clear();
            // Mengambil semua jadwal dari database menggunakan DbManager
            List<String[]> allSchedule = DbManager.loadData();
            //Memuat data jadwal secara berurutan menurut prioritas yg trrtingi
            allSchedule.sort((a, b) -> {
                String priorityA = a[2];
                String priorityB = b[2];
                if ("High".equals(priorityA) && !"High".equals(priorityB)) return -1;
                if ("Medium".equals(priorityA) && "Low".equals(priorityB)) return -1;
                if ("Low".equals(priorityA) && !"Low".equals(priorityB)) return 1;
                return 0;
            });

                // Untuk setiap jadwal, buat Label yang menampilkan tanggal dan judul jadwal
            for (String[] jadwal : allSchedule) {
                Label scheduLabel = new Label("\t" + jadwal[3] + "\t\t" + jadwal[1]); // Membuat sebuah label dengan teks berisi waktu dan judul jadwal.
                scheduLabel.setPrefWidth(460);
                scheduLabel.setPrefHeight(40);
                //Menentukan warna label berdasarkan tingkat prioritas jadwal.
                if ("Low".equals(jadwal[2])) {
                    scheduLabel.setId("rendah-label");
                } else if ("Medium".equals(jadwal[2])) {
                    scheduLabel.setId("sedang-label");
                } else if ("High".equals(jadwal[2])) {
                    scheduLabel.setId("tinggi-label");
                }
    
                // Menetapkan event handler untuk Label, ketika diklik, akan menampilkan detail dari jadwal tersebut
                scheduLabel.setOnMouseClicked(e -> {
                    Scene detailScene = DaftarPengingatHarian.detailScene(primaryStage, jadwal, primaryStage.getScene());
                    primaryStage.setScene(detailScene);
                });
                list.getChildren().add(scheduLabel); // Menambahkan Label ke dalam VBox
            }
        }
    }
}
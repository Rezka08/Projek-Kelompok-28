package id.rezka.tuprak9;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import id.rezka.tuprak9.controller.DbManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

public class MyList {
    private static VBox list;
    private static List<CheckBox> checkBoxes;
    private static List<String[]> allSchedule; // List to store all schedules

    // Metode createScene membuat dan mengembalikan sebuah Scene yang menampilkan daftar jadwal.
    public static Scene createScene(Stage primaryStage, App app) {

        // Membuat label untuk judul "My List"
        Label myListLabel = new Label("My List");
        myListLabel.setId("search-label");

        // Membuat ScrollPane untuk menampung daftar jadwal. ScrollPane memungkinkan konten untuk digulir jika lebih besar dari ukuran tampilan.
        ScrollPane scroll = new ScrollPane();
        scroll.setId("scroll-pane");

        // Membuat VBox untuk menampung Label yang berisi jadwal. VBox akan menampilkan komponen secara vertikal dengan jarak 10 piksel.
        list = new VBox(10);
        list.setPadding(new Insets(10)); // Menetapkan padding di sekitar VBox
        list.setId("list-box"); // Menetapkan ID untuk keperluan styling dengan CSS

        // update data terbaru
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
            primaryStage.setScene(app.createMainScene(primaryStage));
        }); // Kembali ke scene sebelumnya

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

        // Tombol untuk menghapus item yang dipilih
        Button deleteSelectedButton = new Button("Delete Selected");
        deleteSelectedButton.setId("deleteselect");
        deleteSelectedButton.setOnAction(e -> deleteSelectedItems());

        // Tombol untuk menghapus semua item
        Button deleteAllButton = new Button("Delete All");
        deleteAllButton.setId("deleteall");
        deleteAllButton.setOnAction(e -> deleteAllItems());

            // Membuat VBox untuk menampung tombol-tombol delete
        VBox deleteButtonsBox = new VBox(10, deleteSelectedButton, deleteAllButton);
        deleteButtonsBox.setAlignment(Pos.CENTER_RIGHT);
        deleteButtonsBox.setPadding(new Insets(10));
        
        // Membuat HBox untuk menampung label dan tombol-tombol delete
        HBox topBox = new HBox(10, myListLabel, deleteButtonsBox);
        topBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(deleteButtonsBox, Priority.ALWAYS);



        

        // Mengatur layout menggunakan BorderPane
        BorderPane layout = new BorderPane();
        layout.setTop(topBox); // Menetapkan label "My List" di bagian atas
        layout.setCenter(scroll); // Menetapkan ScrollPane di bagian tengah
        layout.setBottom(backButton); // Menetapkan tombol di bagian bawah
        BorderPane.setAlignment(topBox, Pos.TOP_CENTER);
        BorderPane.setAlignment(scroll, Pos.TOP_CENTER);
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        BorderPane.setMargin(topBox, new Insets(10, 10, 10, 10));
        BorderPane.setMargin(scroll, new Insets(0, 10, 0, 10));
        BorderPane.setMargin(backButton, new Insets(10, 10, 10, 10));
        layout.setId("lyt-list"); // Menetapkan ID untuk keperluan styling dengan CSS

        // Membuat Scene baru dengan layout yang sudah dibuat, dan menetapkan ukuran serta stylesheet
        Scene scene = new Scene(layout, 500, 600);
        scene.getStylesheets().add("/styles/stylesMyList.css");
        scene.getStylesheets().add("/styles/stylesDetail.css");

        return scene;

    }

    public static void updateList(Stage primaryStage) {
        if (list != null) {
            list.getChildren().clear();
            checkBoxes = new ArrayList<>();

            // Mengambil semua jadwal dari database menggunakan DbManager
            allSchedule = DbManager.loadData();
            // Memuat data jadwal secara berurutan menurut prioritas yang tertinggi
            allSchedule.sort((a, b) -> {
                String priorityA = a[2];
                String priorityB = b[2];
                if ("High".equals(priorityA) && !"High".equals(priorityB)) return -1;
                if ("Medium".equals(priorityA) && "Low".equals(priorityB)) return -1;
                if ("Low".equals(priorityA) && !"Low".equals(priorityB)) return 1;
                return 0;
            });

            // Untuk setiap jadwal, buat HBox yang berisi CheckBox dan Label yang menampilkan tanggal dan judul jadwal
            for (String[] jadwal : allSchedule) {
                CheckBox checkBox = new CheckBox();
                checkBoxes.add(checkBox);

                Label scheduLabel = new Label("\t" + jadwal[3] + "\t\t" + jadwal[1]); // Membuat sebuah label dengan teks berisi waktu dan judul jadwal.
                scheduLabel.setPrefWidth(460);
                scheduLabel.setPrefHeight(40);
                // Menentukan warna label berdasarkan tingkat prioritas jadwal.
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

                HBox itemBox = new HBox(10, checkBox, scheduLabel);
                list.getChildren().add(itemBox); // Menambahkan HBox ke dalam VBox
            }
        }
    }

    private static void deleteSelectedItems() {
        List<Integer> selectedIndexes = checkBoxes.stream()
            .filter(CheckBox::isSelected)
            .map(checkBoxes::indexOf)
            .map(allSchedule::get)
            .map(data -> Integer.parseInt(data[0]))
            .collect(Collectors.toList());

        DbManager.deleteItems(selectedIndexes); //  method menghapus item dari database melalui ID
        updateList(new Stage()); // me refresh list
    }

    private static void deleteAllItems() {
        DbManager.deleteAllItems(); //  method untuk menghapus semua item daridatabase
        updateList(new Stage()); // me refresh  list
    }
}

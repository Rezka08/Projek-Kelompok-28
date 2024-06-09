package id.rezka.tuprak9;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;


import id.rezka.tuprak9.controller.DbManager;
import id.rezka.tuprak9.utils.NotifInputJadwal;
import id.rezka.tuprak9.utils.TambahDeskripsi;
import id.rezka.tuprak9.utils.TambahWaktu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class InputJadwal {
    private static String jenisPrioritas = null;
    
    public static Button rendahPrio;
    public static Button sedangPrio;
    public static Button tinggiPrio;
    private static boolean isRendahPressed = false;
    private static boolean isSedangPressed = false;
    private static boolean isTinggiPressed = false;
    
    public static String getJenisPrioritas() {
        return jenisPrioritas;
    }

    public static void setJenisPrioritas(String jenisPrioritas) {
        InputJadwal.jenisPrioritas = jenisPrioritas;
    }

    public static boolean isRendahPressed() {
        return isRendahPressed;
    }

    public static void setRendahPressed(boolean isRendahPressed) {
        InputJadwal.isRendahPressed = isRendahPressed;
    }

    public static boolean isSedangPressed() {
        return isSedangPressed;
    }

    public static void setSedangPressed(boolean isSedangPressed) {
        InputJadwal.isSedangPressed = isSedangPressed;
    }

    public static boolean isTinggiPressed() {
        return isTinggiPressed;
    }

    public static void setTinggiPressed(boolean isTinggiPressed) {
        InputJadwal.isTinggiPressed = isTinggiPressed;
    }

    public static void resetPrioritasPressed(){
        setRendahPressed(false);
        setSedangPressed(false);
        setTinggiPressed(false);
    }

    public static Scene createScene(Stage primaryStage, App app, Scene sceneSebelumnya) {
        setJenisPrioritas(null);
        resetPrioritasPressed();
        TambahWaktu.reset();
        TambahDeskripsi.resetDeskripsi();
        
        // Label untuk judul form "Add New Schedule"
        Label catatanLabel = new Label("Add New Schedule");
        catatanLabel.setId("cttn-label");
        
        // Label untuk "Title"
        Label judulLabel = new Label("Title");
        judulLabel.setId("judul-label");
        
        // TextField untuk memasukkan judul jadwal
        TextField jadwalField = new TextField();
        jadwalField.setPromptText("Enter The Title");
        jadwalField.setId("jdwl-field");
        jadwalField.setPrefWidth(400);
        jadwalField.setPrefHeight(10);

        // Membatasi panjang input judul hingga 50 karakter
        jadwalField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 50) {
                jadwalField.setText(oldValue);
            }
        });

        // Label untuk "Type Of Priority"
        Label jPLabel = new Label("Type Of Priority");
        jPLabel.setId("prioritas-label");

        HBox titlebox = new HBox(judulLabel);
        titlebox.setAlignment(Pos.TOP_LEFT);
        // VBox untuk menampung label dan input judul
        VBox labelBox = new VBox(10, catatanLabel,titlebox, jadwalField, jPLabel);
        
        labelBox.setAlignment(Pos.TOP_CENTER);

        // Button untuk prioritas rendah
        rendahPrio = new Button("Low");
        rendahPrio.setPrefWidth(100);
        rendahPrio.setId("btn-rendah");
        rendahPrio.setOnMouseClicked(e -> {
            if (isRendahPressed()) {
                rendahPrio.setId("btn-rendah");
                setRendahPressed(false);
                setJenisPrioritas(null);
            } else {
                rendahPrio.setId("click-rendah");
                sedangPrio.setId("btn-sedang");
                tinggiPrio.setId("btn-tinggi");
                setRendahPressed(true);
                setSedangPressed(false);
                setTinggiPressed(false);
                setJenisPrioritas("Low");
            }
        });
            
        // Button untuk prioritas sedang
        sedangPrio = new Button("Medium");
        sedangPrio.setPrefWidth(100);
        sedangPrio.setId("btn-sedang");
        sedangPrio.setOnMouseClicked(e -> {
            if (isSedangPressed()) {
                sedangPrio.setId("btn-sedang");
                setSedangPressed(false);
                setJenisPrioritas(null);
            } else {
                rendahPrio.setId("btn-rendah");
                sedangPrio.setId("click-sedang");
                tinggiPrio.setId("btn-tinggi");
                setRendahPressed(false);
                setSedangPressed(true);
                setTinggiPressed(false);
                setJenisPrioritas("Medium");

            }
        });
            
        // Button untuk prioritas tinggi        
        tinggiPrio = new Button("High");
        tinggiPrio.setPrefWidth(100);
        tinggiPrio.setId("btn-tinggi");
        tinggiPrio.setOnMouseClicked(e -> {
            if (isTinggiPressed()) {
                tinggiPrio.setId("btn-tinggi");
                setTinggiPressed(false);
                setJenisPrioritas(null);
            } else {
                rendahPrio.setId("btn-rendah");
                sedangPrio.setId("btn-sedang");
                tinggiPrio.setId("click-tinggi");
                setRendahPressed(false);
                setSedangPressed(false);
                setTinggiPressed(true);
                setJenisPrioritas("High");
            }
        });

        // HBox untuk menampung tombol prioritas
        HBox jPbutton = new HBox(10,rendahPrio, sedangPrio, tinggiPrio);
        jPbutton.setAlignment(Pos.CENTER);
        jPbutton.setSpacing(10);

        // Label untuk "Details"
        Label detailLabel = new Label("Details");
        detailLabel.setId("detail-label");

        // Button untuk menambahkan waktu
        Button tambahWaktu = new Button("Add Time");
        tambahWaktu.setId("btn-addtimeanddesc");
        tambahWaktu.setPrefWidth(500);
        // tambahWaktu.setId("btn-tmbhwkt");

        tambahWaktu.setOnAction(e -> {
            TambahWaktu.tambahWaktuTanggal(primaryStage).showAndWait();
        });

        // Button untuk menambahkan deskripsi
        Button tambahDeskrip = new Button("Add a description");
        tambahDeskrip.setPrefWidth(500);
        tambahDeskrip.setId("btn-addtimeanddesc");
        tambahDeskrip.setOnAction(e ->{
            TambahDeskripsi.tambahDeskrip(primaryStage).showAndWait();
        });        

        // VBox untuk menampung detail label, tambah waktu, dan tambah deskripsi
        VBox detailVBox = new VBox(10, detailLabel,tambahWaktu, tambahDeskrip);

        // Button untuk menyimpan jadwal
        Button saveButton = new Button("");
        saveButton.setId("sve-bttn");
        saveButton.setPrefWidth(80);
        saveButton.setPrefSize(70, 20);
        saveButton.setMinSize(70, 10);
        saveButton.setMaxHeight(40);
        saveButton.setMaxWidth(40);
        try {
            // Setel ikon tombol kembali
            FileInputStream iconStream = new FileInputStream("src/main/resources/image/save.png");
            Image icon = new Image(iconStream);

            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            saveButton.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Button untuk kembali ke scene sebelumnya
        Button backButton = new Button("");
        backButton.setId("bck-bttn");
        backButton.setMaxHeight(40);
        backButton.setMaxWidth(40);
        backButton.setPrefSize(70, 20);
        backButton.setMinSize(70, 10);
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
        
        // HBox untuk menampung tombol kembali dan tombol simpan
        HBox tombol = new HBox(340, backButton, saveButton);
        tombol.setAlignment(Pos.CENTER);

        // VBox untuk mengatur seluruh layout
        VBox layout = new VBox(15, labelBox, jPbutton, detailVBox, tombol);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setId("lyt-jadwal");
        layout.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        
        // VBox untuk menampilkan notifikasi
        VBox notifBox = new VBox( layout);
        notifBox.setTranslateY(NotifInputJadwal.NOTIFICATION_HEIGHT);
        NotifInputJadwal.slideAtas(notifBox, sceneSebelumnya);

        // Event handler untuk tombol save
        saveButton.setOnAction(e ->{
            String judul = jadwalField.getText().trim();
            LocalDate tanggal = TambahWaktu.getTanggal();
            LocalTime waktu = TambahWaktu.getWaktu();
            String deskripsi = TambahDeskripsi.getDeskripsi();

            // Validasi input
            String pesanError = null;
            if (judul.isEmpty() && jenisPrioritas == null && tanggal == null && waktu == null) {
                pesanError = "Title, Priority Type, Date and Time must not be empty.";
            } else if (judul.isEmpty()) {
                pesanError = "The Title cannot be empty.";
            } else if (jenisPrioritas == null) {
                pesanError = "Priority type cannot be empty.";
            } else if (tanggal == null && waktu == null) {
                pesanError = "Date and Time cannot be empty.";
            }

            // Menampilkan pesan error atau menyimpan data ke database
            if (pesanError != null) {
                NotifInputJadwal.showErrorPopup(primaryStage, pesanError);
            } else {
                DbManager.saveData(judul, jenisPrioritas, tanggal, waktu, deskripsi, false);
                NotifInputJadwal.slidekeBawah(notifBox, sceneSebelumnya);
            }
        });
        
         // Event handler untuk tombol back
        backButton.setOnAction(e -> {
            NotifInputJadwal.slidekeBawah(notifBox,sceneSebelumnya);
        });

         // Menonaktifkan elemen-elemen di scene sebelumnya
        StackPane rootSebelumnya = (StackPane) sceneSebelumnya.getRoot();
        rootSebelumnya.getChildren().forEach(node -> node.setDisable(true));

        // StackPane untuk menampung scene sebelumnya dan notifikasi
        StackPane mainLayoutAddSch = new StackPane();
        mainLayoutAddSch.getChildren().addAll(sceneSebelumnya.getRoot(), notifBox);
        

        // Membuat scene baru dengan layout yang sudah diatur dan menambahkan stylesheet
        Scene scene = new Scene(mainLayoutAddSch, 500, 600);
        scene.getStylesheets().add("/styles/stylesInputJadwal&App.css");
        scene.getStylesheets().add("/styles/stylesDetail.css");
        scene.getStylesheets().add("/styles/stylesMyList.css");
        return scene;
    }
}
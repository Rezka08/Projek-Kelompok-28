package id.rezka.tuprak9.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TambahDeskripsi {
    
    private static String deskripsi;

    // Metode untuk menampilkan pop-up menambahkan deskripsi
    public static Stage tambahDeskrip(Stage primaryStage) {
        // Membuat stage baru untuk pop-up
        Stage popUpDescribe = new Stage();
        // Mengatur owner dari pop-up
        popUpDescribe.initOwner(primaryStage);
        // Mengatur modality agar pop-up bersifat modal
        popUpDescribe.initModality(Modality.APPLICATION_MODAL);
        // Mengatur style dari pop-up
        popUpDescribe.initStyle(StageStyle.UTILITY);
        // Mengatur judul dari pop-up
        popUpDescribe.setTitle("Add a description");

        // Membuat TextArea untuk input deskripsi
        TextArea desArea = new TextArea();
        desArea.setPromptText("Enter a description");
        desArea.setId("description-area");
        desArea.setPrefWidth(300);
        desArea.setPrefHeight(100);
        desArea.setWrapText(true);

        // Membuat tombol Save
        Button saveButton = new Button("Save");
        saveButton.setId("save-button");
        saveButton.setPrefWidth(100);
        // Mengatur aksi tombol Save
        saveButton.setOnAction(e -> {
            // Menyimpan teks dari desArea ke variabel deskripsi
            deskripsi = desArea.getText();
            // Menutup pop-up
            popUpDescribe.close();
        });

        // Membuat tombol Back
        Button backButton = new Button("Back");
        backButton.setId("back-button");
        backButton.setPrefWidth(100);
        // Mengatur aksi tombol Back
        backButton.setOnAction(e -> {
            // Menutup pop-up
            popUpDescribe.close();
        });

        // Membuat HBox untuk menampung tombol Save dan Back
        HBox tombolDesc = new HBox(190, backButton, saveButton);
        tombolDesc.setAlignment(Pos.CENTER);

        // Membuat VBox untuk layout pop-up
        VBox layout = new VBox(10, desArea, tombolDesc);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setId("popup-lyt");

        // Membuat scene untuk pop-up 
        Scene popupScene = new Scene(layout, 400, 200);
        // Menambahkan stylesheet untuk styling
        popupScene.getStylesheets().add("/styles/stylesTambahDeskripsi.css");
        // Mengatur scene dari stage pop-up
        popUpDescribe.setScene(popupScene);

        // Mengembalikan objek stage pop-up
        return popUpDescribe;
    }

    // Metode untuk mendapatkan deskripsi yang tersimpan
    public static String getDeskripsi() {
        return deskripsi;
    }
    
    public static void resetDeskripsi() {
        deskripsi = null;
    }
}


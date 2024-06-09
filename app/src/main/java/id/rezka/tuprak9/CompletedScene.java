package id.rezka.tuprak9;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import id.rezka.tuprak9.controller.DbManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CompletedScene {
    private static VBox completedList = new VBox();

    public static Scene createScene(Stage primaryStage, App app) {
        // Membuat scene yang menampilkan daftar tugas yang telah diselesaikan.
        Label completedLabel = new Label("Completed List");
        completedLabel.setId("completed-Label");

        completedList = new VBox(10);
        completedList.setPadding(new Insets(10));
        completedList.setAlignment(Pos.TOP_LEFT);

        updateCompletedSchedule(primaryStage);

        // ScrollPane digunakan untuk menampung label "Completed List".
        // ScrollPane akan berguna jika daftar tugas yang diselesaikan panjang.
        ScrollPane scrollPane = new ScrollPane(completedList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));
        scrollPane.setId("scroll-pane");

        // Layout BorderPane digunakan sebagai layout utama untuk menempatkan label dan tombol.
        BorderPane completedLayout = new BorderPane();
        completedLayout.setTop(completedLabel);
        BorderPane.setAlignment(completedLabel, Pos.TOP_CENTER);
        completedLayout.setCenter(scrollPane);
        BorderPane.setMargin(completedLabel, new Insets(20, 0, 20, 0));
        BorderPane.setMargin(scrollPane, new Insets(0, 20, 0, 20));
        completedLayout.setId("completed-lyt");

        // Tombol "Back" ditambahkan untuk kembali ke halaman utama.
        Button backButton = new Button("");
        backButton.setMaxWidth(40);
        backButton.setMaxHeight(40);
        backButton.setId("bck-bttn");
        backButton.setOnAction(e -> primaryStage.setScene(app.createMainScene(primaryStage)));
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

        completedLayout.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(10));

        Scene scene = new Scene(completedLayout, 500, 600);
        scene.getStylesheets().add("/styles/stylesCompleted.css");
        scene.getStylesheets().add("/styles/stylesDetail.css");

        return scene;
    }

    public static void updateCompletedSchedule(Stage primaryStage) {
        completedList.getChildren().clear();
        // menampilkan semua tugas yang selesai
        List<String[]> completedTasks = DbManager.loadCompletedTasks();
        // Untuk setiap jadwal, buat Label yang menampilkan tanggal dan judul jadwal
        for (String[] schedule : completedTasks) {
            Label scheduLabel = new Label("\t" + schedule[3] + "\t\t" + schedule[1]);
            scheduLabel.setPrefWidth(460);
            scheduLabel.setPrefHeight(40);
            scheduLabel.setId("schedule-label");

            // Menetapkan event handler untuk Label, ketika diklik, akan menampilkan detail dari jadwal tersebut
            scheduLabel.setOnMouseClicked(e -> {
                Scene detailScene = DaftarPengingatHarian.detailScene(primaryStage, schedule, primaryStage.getScene());
                primaryStage.setScene(detailScene);
            });

            // Menetapkan event handler untuk mengubah kursor menjadi tangan ketika mouse berada di atas Label
            scheduLabel.setOnMouseEntered(event -> primaryStage.getScene().setCursor(Cursor.HAND));
            scheduLabel.setOnMouseExited(event -> primaryStage.getScene().setCursor(Cursor.DEFAULT));

            // Add the schedule label to the completed list
            completedList.getChildren().add(scheduLabel);
        }
    }
}

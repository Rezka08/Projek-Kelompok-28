package id.rezka.tuprak9;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

import id.rezka.tuprak9.controller.DbManager;
import id.rezka.tuprak9.utils.EditScene;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.control.ListCell;
import id.rezka.tuprak9.utils.CustomItem;; 

public class DaftarPengingatHarian {

    private static boolean popupmuncul = false;
    private static Stage popup;
    private static Label scheduleLabel;

    // Metode untuk membuat scene detail untuk sebuah jadwal
    public static Scene detailScene(Stage primaryStage, String[] scheduleDetails, Scene previousScene) {
        
        // Membuat kontainer VBox untuk menempatkan komponen-komponen UI
        VBox detailJadwal = new VBox(10);
        detailJadwal.setAlignment(Pos.TOP_CENTER);
        detailJadwal.setId("box-detail"); // Setel ID untuk CSS styling

        // Label judul untuk menandakan bagian detail
        Label titleLabel = new Label("Schedule Details");
        titleLabel.setId("label-detail");

         // Informasi jadwal yang akan ditampilkan
        scheduleLabel = new Label();
        updateDetails(scheduleDetails);
        //.setId("label-schedule"); // Setel ID untuk CSS styling
        scheduleLabel.setPrefWidth(400);
        scheduleLabel.setPrefHeight(400);
        scheduleLabel.setWrapText(true);
        scheduleLabel.setAlignment(Pos.TOP_LEFT);
        scheduleLabel.setPadding(new Insets(10));
        scheduleLabel.setId("label-schedule");  // Setel ID untuk CSS styling

        // Tombol kembali untuk kembali ke scene sebelumnya
        Button backButton = new Button();
        backButton.setMaxWidth(40);
        backButton.setMaxHeight(40);
        backButton.setId("bck-bttn");
        backButton.setOnAction(e -> {
            primaryStage.setScene(previousScene); ; // Kembali ke scene sebelumnya
            MyList.updateList(primaryStage); // Perbarui daftar jadwal
        });
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

        VBox centerBox = new VBox(10, titleLabel, scheduleLabel);
        VBox.setMargin(titleLabel, new Insets(0,0,10,0));
        VBox.setMargin(scheduleLabel, new Insets(0,0,10,0));
        centerBox.setAlignment(Pos.TOP_CENTER);
        
        //menandai tugas selesai
        String taskStatus = scheduleDetails[6]; 
        
        // Menambahkan ComboBox untuk tindakan delete dan edit
        ComboBox<CustomItem> actionComboBox = new ComboBox<>();
        actionComboBox.setId("for-combox");
        try {
            Image editImage = new Image(new FileInputStream("src/main/resources/image/edit.png"));
            Image deleteImage = new Image(new FileInputStream("src/main/resources/image/deletee.png"));

            if ("1".equals(taskStatus)) {
                actionComboBox.getItems().add(new CustomItem("Delete", deleteImage));
            } else {
                actionComboBox.getItems().addAll(
                    new CustomItem("Edit",editImage),
                    new CustomItem("Delete", deleteImage)
                );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        actionComboBox.setCellFactory(param  -> new ListCell<CustomItem>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(CustomItem item, boolean empty){
                super.updateItem(item, empty);
                if(empty || item == null ){
                    setGraphic(null);
                    setText(null);
                }else {
                    imageView.setImage(item.getImage());
                    imageView.setFitHeight(13);
                    imageView.setFitWidth(13);
                    setAlignment(Pos.CENTER);
                    setGraphic(imageView);
                    setText(null);
                }
            }
            });

            actionComboBox.setButtonCell(new ListCell<CustomItem>(){
                private final ImageView imageView = new ImageView();
                
                @Override
                protected void updateItem(CustomItem item, boolean empty){
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setText(null);
                    }else {
                        imageView.setImage(item.getImage());
                        setGraphic(imageView);
                        setText(item.getText());
                    }
                }

                });
        actionComboBox.setOnMouseEntered(e -> actionComboBox.show());
        
        actionComboBox.setOnAction(e -> {
            CustomItem action = actionComboBox.getValue();
            if ("Delete".equals(action.getText())) {
                showConfirmationDialog("Delete", () -> {
                    int id = Integer.parseInt(scheduleDetails[0]);
                    DbManager.removeData(id);
                    primaryStage.setScene(previousScene);
                    MyList.updateList(primaryStage);
                    CompletedScene.updateCompletedSchedule(primaryStage);
                    String rootid = previousScene.getRoot().getId();
                    if (rootid != null && rootid.equals("search-box")) {
                        VBox searchBox = (VBox) ((ScrollPane) previousScene.lookup("#scroll-pane")).getContent();
                        SearchScene.updateSearchResults(searchBox, "", primaryStage);
                    }
                });
            } else if ("Edit".equals(action.getText())) {
                Scene editScene = EditScene.createEditScene(primaryStage, scheduleDetails, DaftarPengingatHarian.detailScene(primaryStage, scheduleDetails, previousScene));
                primaryStage.setScene(editScene);
            }
        });
          // Kotak untuk menempatkan tombol-tombol

        Button completeButton = new Button("Mark as Completed");
        completeButton.setMaxWidth(400);
        completeButton.setMaxHeight(100);
        completeButton.setId("complete-btn");
        completeButton.setOnAction(e -> {
            showConfirmationDialog("Mark as Completed", () -> {
                int id = Integer.parseInt(scheduleDetails[0]);
                DbManager.tandaSelesai(id);
                primaryStage.setScene(previousScene);
                MyList.updateList(primaryStage);
                String rootid = previousScene.getRoot().getId();
                if (rootid != null && rootid.equals("search-box")) {
                    VBox searchBox = (VBox) ((ScrollPane) previousScene.lookup("#scroll-pane")).getContent();
                    SearchScene.updateSearchResults(searchBox, "", primaryStage);
                }
            });
        });

        HBox buttonBox = new HBox(400, backButton, actionComboBox);
        buttonBox.setMaxSize(50, 30);
        // Layout BorderPane yang menempatkan semua komponen
        BorderPane layout = new BorderPane();
        layout.setTop(buttonBox);
        layout.setCenter(centerBox);
        BorderPane.setAlignment(buttonBox, Pos.TOP_LEFT);
        BorderPane.setMargin(buttonBox, new Insets(20, 20, 0, 20));
        BorderPane.setMargin(centerBox, new Insets(0, 20, 20, 20));

        //kondisi untuk menampilkan button  meyelesaikan tugas
        if (!"1".equals(taskStatus)) {
            layout.setBottom(completeButton);
            BorderPane.setAlignment(completeButton, Pos.BOTTOM_CENTER);
            BorderPane.setMargin(buttonBox, new Insets(20, 20, 20, 20));

        }
        detailJadwal.getChildren().add(layout); // Tambahkan layout ke VBox utama


        // Buat scene dengan VBox sebagai root
        Scene scene = new Scene(detailJadwal, 500, 600);
        scene.getStylesheets().add("styles/stylesDetail.css"); // Tambahkan CSS styling
        scene.getStylesheets().add("styles/stylesDaftarPengingat.css"); // Tambahkan CSS styling
        scene.getStylesheets().add("styles/stylesForComboBox.css"); // Tambahkan CSS styling

        return scene;
    }

    // buat tampilan isi detail
    public static void updateDetails(String[] updatedDetails) {
        String description = updatedDetails[5];
        if (description == null || description.isEmpty()) {
            description = "No Description";
        }
        scheduleLabel.setText(
            "Details\n---------------------------------------------------------------------------\n" +
            "Date\t\t\t\t: " + updatedDetails[3] + "\n" + 
            "Time\t\t\t\t: " + updatedDetails[4] + "\n" +
            "Title\t\t\t\t: " + updatedDetails[1] + "\n" +
            "Type Of Priority\t: " + updatedDetails[2] + "\n" + 
            "Description\t\t: " + description
            
        );
    }
    

    // Metode untuk menampilkan daftar jadwal harian dalam jendela popup
    public static void tampilkanDaftarHarian(Stage primaryStage, Button triggerButton) {
        if (popupmuncul) {
            if (popup != null) {
                popup.hide();
                popupmuncul = false;
            }
            return;
        }

        popupmuncul = true;
        popup = new Stage();
        popup.initOwner(primaryStage);
        popup.initModality(Modality.NONE);
        popup.initStyle(StageStyle.TRANSPARENT);

        // Kotak untuk menempatkan daftar jadwal harian
        VBox daftar = new VBox(10);
        daftar.setPadding(new Insets(10));
        daftar.setAlignment(Pos.TOP_LEFT);
        daftar.setPrefSize(360, 300);
        daftar.setId("box-daftar");

        // Label judul untuk menandakan bagian daftar
        Label titleLabel = new Label("Today's Schedule");
        titleLabel.setId("label-title");
        daftar.getChildren().addAll(titleLabel);

        // Mendapatkan tanggal hari ini.
        LocalDate today = LocalDate.now();

        //Memuat data jadwal dari database untuk tanggal hari ini.
        List<String[]> todaySchedules = DbManager.loadDataForDate(today);
        todaySchedules.sort((a, b) -> {
            String priorityA = a[2];
            String priorityB = b[2];
            if ("High".equals(priorityA) && !"High".equals(priorityB)) return -1;
            if ("Medium".equals(priorityA) && "Low".equals(priorityB)) return -1;
            if ("Low".equals(priorityA) && !"Low".equals(priorityB)) return 1;
            return 0;
        });

        //// Membuat sebuah scroll pane dan menambahkan daftar jadwal ke dalamnya
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(daftar);
        scrollPane.setPrefSize(380, 300);
        scrollPane.setFitToWidth(true);

        //Menambahkan style dan scene ke popup yang akan menampilkan daftar jadwal.
        Scene popupScene = new Scene(scrollPane);
        popupScene.setFill(null);
        popupScene.getStylesheets().add("/styles/stylesDaftarPengingat.css");
        popup.setScene(popupScene);

        //Perulangan untuk setiap jadwal pada tanggal hari ini.
        for (String[] jadwal : todaySchedules) {
            Label scheduleLabel = new Label(jadwal[4] + "\t" + jadwal[1]); // Membuat sebuah label dengan teks berisi waktu dan judul jadwal.
            scheduleLabel.setPrefWidth(340);
            //Menentukan warna label berdasarkan tingkat prioritas jadwal.
            if ("Low".equals(jadwal[2])) {
                scheduleLabel.setId("rendah-label");
            } else if ("Medium".equals(jadwal[2])) {
                scheduleLabel.setId("sedang-label");
            } else if ("High".equals(jadwal[2])) {
                scheduleLabel.setId("tinggi-label");
            }


            //Menambahkan event listener untuk menangani pergeseran ke scene detail jadwal ketika label diklik.
            scheduleLabel.setOnMouseClicked(e -> {
                Scene detailScene = detailScene(primaryStage, jadwal, primaryStage.getScene()); //Menambahkan label ke VBox yang berisi daftar jadwal.
                primaryStage.setScene(detailScene);
                popup.hide();
                popupmuncul = false;
            });

            daftar.getChildren().add(scheduleLabel);
        }

        //Mendapatkan posisi trigger button dalam koordinat layar.
        Bounds bounds = triggerButton.localToScreen(triggerButton.getBoundsInLocal());

        //Menetukan posisi popup yang berisi daftar jadwal.
        popup.setX(bounds.getMinX());
        popup.setY(bounds.getMaxY() + 5);
        popup.show();////Menampilkan popup.


        ////Membuat scroll pane bergeser ke atas sebelum ditampilkan.
        scrollPane.setTranslateY(-300);
        ////Membuat transisi bergeser untuk scroll pane.
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), scrollPane);
        slide.setFromY(-300);
        slide.setToY(0);
        slide.play();

        // //Menangani pergeseran jendela aplikasi dan menggeser popup sesuai dengan posisi trigger button.
        primaryStage.xProperty().addListener((obs, oldVal, newVal) -> {
            Bounds newBounds = triggerButton.localToScreen(triggerButton.getBoundsInLocal());
            if (newBounds != null) {
                popup.setX(newBounds.getMinX());
            }
        });

        primaryStage.yProperty().addListener((obs, oldVal, newVal) -> {
            Bounds newBounds = triggerButton.localToScreen(triggerButton.getBoundsInLocal());
            if (newBounds != null) {
                popup.setY(newBounds.getMaxY() + 5);
            }
        });

        ////Menyembunyikan popup ketika mouse ditekan di luar area popup.
        Bounds bounds3 = primaryStage.getScene().getRoot().getLayoutBounds();
        primaryStage.getScene().addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (popup.isShowing() && !bounds3.contains(event.getScreenX(), event.getScreenY())) {
                popup.hide();
                popupmuncul = true;
            }
        });
    }

    private static void showConfirmationDialog(String action, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to " + action + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                onConfirm.run();
            }
        });
    }    
}
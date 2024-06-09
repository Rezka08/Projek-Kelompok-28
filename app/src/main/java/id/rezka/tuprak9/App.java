package id.rezka.tuprak9;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.text.LabelView;

public class App extends Application {
    private App appInstance;
    
    @Override
    public void start(Stage primaryStage) {
        appInstance = this;
        primaryStage.setTitle("Evernote");
        primaryStage.setResizable(false);
        try {
            FileInputStream iconStream = new FileInputStream("src/main/resources/image/IMG_1370.jpg");
            Image icon = new Image(iconStream);
            primaryStage.getIcons().add(icon);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        // Create and set loading scene
        Scene loadingScene = createLoadingScene(primaryStage);
        primaryStage.setScene(loadingScene);
        primaryStage.show();

        // Run loading thread
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> primaryStage.setScene(createMainScene(primaryStage)));
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Scene createLoadingScene(Stage primaryStage) {
        // Load the loading image
        ImageView loadingImageView = null;
        try {
            FileInputStream loadingImageStream = new FileInputStream("src/main/resources/image/IMG_1327(1).JPG");
            Image loadingImage = new Image(loadingImageStream);
            loadingImageView = new ImageView(loadingImage);
            loadingImageView.setFitWidth(500);
            loadingImageView.setFitHeight(600);
            loadingImageView.setPreserveRatio(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Create layout for loading scene
        StackPane loadingLayout = new StackPane();
        if (loadingImageView != null) {
            loadingLayout.getChildren().add(loadingImageView);
        }

        return new Scene(loadingLayout, 500, 600);
    }

    public Scene createMainScene(Stage primaryStage) {
        Button searchbar = new Button("Search");
        searchbar.setId("btn-search");
        searchbar.setMaxWidth(470);
        searchbar.setMinHeight(30);
        searchbar.setId("for-outer");
        

        // Action untuk Search
        searchbar.setOnAction(e -> {
            try {
                primaryStage.setScene(SearchScene.createScene(primaryStage, appInstance));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        StackPane stackPane1 = new StackPane(searchbar);
        stackPane1.setAlignment(Pos.TOP_CENTER);
        stackPane1.setPadding(new javafx.geometry.Insets(20));

        try {
            FileInputStream iconStream = new FileInputStream("src/main/resources/image/search.png");
            Image icon = new Image(iconStream);

            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(10);
            imageView.setFitWidth(10);
            searchbar.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Tombol Calendar
        Button calendarButton = new Button("");
        calendarButton.setMaxWidth(150);
        calendarButton.setMinHeight(100);
        calendarButton.setId("btn-calendar");

        // Action untuk gambar Calendar(Daftar List) Button
        calendarButton.setOnAction(e -> {
            try {
                primaryStage.setScene(MyList.createScene(primaryStage, appInstance));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        try {
            FileInputStream iconStream = new FileInputStream("src/main/resources/image/calendar.png");
            Image icon = new Image(iconStream);

            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(90);
            imageView.setFitWidth(90);
            calendarButton.setGraphic(imageView);

            Label lblmylst = new Label("My-List");
            lblmylst.setId("mylist-font");
            VBox vbox = new VBox();
            vbox.getChildren().addAll(imageView, lblmylst);
            vbox.setAlignment(Pos.CENTER);


            calendarButton.setGraphic(vbox);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Tombol Completed
        Button completedButton = new Button("");
        completedButton.setMaxWidth(150);
        completedButton.setMinHeight(100);
        completedButton.setId("btn-completed");

        // Action Untuk Completed Button
        completedButton.setOnAction(e -> {
            try {
                primaryStage.setScene(CompletedScene.createScene(primaryStage, appInstance));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        try {
            FileInputStream iconStream = new FileInputStream("src/main/resources/image/completed(1).png");
            Image icon = new Image(iconStream);

            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(90);
            imageView.setFitWidth(90);
            completedButton.setGraphic(imageView);

            Label lblcmpltd = new Label("Completed-List");
            lblcmpltd.setId("mylist-font");

            VBox vboxcmpltd = new VBox();
            vboxcmpltd.getChildren().addAll(imageView,lblcmpltd);
            vboxcmpltd.setAlignment(Pos.CENTER);

            completedButton.setGraphic(vboxcmpltd);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HBox hBoxButtons = new HBox(130);
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxButtons.getChildren().addAll(calendarButton, completedButton);

        StackPane buttonPane = new StackPane(hBoxButtons);
        buttonPane.setAlignment(Pos.TOP_CENTER);
        buttonPane.setPadding(new javafx.geometry.Insets(30, 0, 0, 0));
        
        // button menampilkan jadwal harian
        Button jadwalHarian = new Button("Today's Task List");
        jadwalHarian.setId("btn-jadwalHari");
        jadwalHarian.setPrefWidth(380);
        jadwalHarian.setOnAction(e -> {
            DaftarPengingatHarian.tampilkanDaftarHarian(primaryStage, jadwalHarian);
        });

        StackPane btnDaftarHarian = new StackPane(jadwalHarian);
        btnDaftarHarian.setAlignment(Pos.CENTER);
        buttonPane.setPadding(new javafx.geometry.Insets(0, 20, 0, 20));

        //button add schedule
        Button jadwalButton = new Button(" Add Schedule ");
        jadwalButton.setMaxHeight(100);
        jadwalButton.setMinHeight(10);
        jadwalButton.setAlignment(Pos.BASELINE_RIGHT);
        jadwalButton.setId("btn-jadwal");


        // Action untuk jadwal
        jadwalButton.setOnAction(e -> {
            try {
                primaryStage.setScene(InputJadwal.createScene(primaryStage, appInstance, createMainScene(primaryStage)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        StackPane jadwalPane = new StackPane(jadwalButton);
        jadwalPane.setAlignment(Pos.BOTTOM_LEFT);
        jadwalPane.setPadding(new javafx.geometry.Insets(330, 0, 0, 360));

        try {
            FileInputStream iconStream = new FileInputStream("src/main/resources/image/plus.png");
            Image icon = new Image(iconStream);
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(10);
            imageView.setFitWidth(10);
            jadwalButton.setGraphic(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Menambahkan ImageView sebagai background
        ImageView backgroundImageView = null;
        try {
            FileInputStream backgroundStream = new FileInputStream("src/main/resources/image/IMG_1458.PNG");
            Image backgroundImage = new Image(backgroundStream);
            backgroundImageView = new ImageView(backgroundImage);
            backgroundImageView.setFitWidth(300);
            backgroundImageView.setFitHeight(300);
            backgroundImageView.setPreserveRatio(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Membuat StackPane untuk menampung background dan elemen lainnya
        StackPane root = new StackPane();
        if (backgroundImageView != null) {
            root.getChildren().add(backgroundImageView);
            StackPane.setAlignment(backgroundImageView, Pos.CENTER);
            StackPane.setMargin(backgroundImageView, new Insets(150, 0, 0, 25));
            
            
        }

        // Menambahkan elemen-elemen ke StackPane
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(stackPane1, buttonPane, btnDaftarHarian, jadwalPane);
        root.getChildren().add(vBox);
        root.setId("lyt-BG");
        
        

        Scene scene = new Scene(root, 500, 620);
        scene.getStylesheets().add("/styles/stylesInputJadwal&App.css");
        scene.getStylesheets().add("/styles/stylesMyList.css");
        

        primaryStage.setScene(scene);

        return scene;
    }
}
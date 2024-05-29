package id.rezka.tuprak9;

import id.rezka.tuprak9.controller.DbManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class SearchScene {

    public static Scene createScene(Stage primaryStage, App appInstance) {

        // Membuat tombol untuk kembali ke scene  dan Mengatur gaya tombol kembali
        Button backButton = new Button("Back");
        backButton.setId("back-button");
        backButton.setOnAction(e -> primaryStage.setScene(appInstance.createMainScene(primaryStage)));

        // Membuat kolom pencarian, Mengatur teks petunjuk dalam kolom pencarian, Mengatur gaya kolom pencarian, Mengatur lebar preferensi kolom pencarian
        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        searchField.setId("search-field");
        searchField.setPrefWidth(400);

        // Membuat ScrollPanel agar konten scroll menyesuaikan dengan lebar panel
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setId("scroll-pane");

        //Membuat kotak vertikal untuk menampung hasil pencarian, Mengatur padding (jarak dalam) kotak vertikal
        VBox searchBox = new VBox(10);
        searchBox.setPadding(new javafx.geometry.Insets(10));
        searchBox.setId("main-layout");
        scroll.setContent(searchBox); //Menetapkan kotak vertikal sebagai konten panel scroll

        // Mengatur tata letak utama scene, Mengatur padding tata letak utama, 
        VBox layout2 = new VBox(10, backButton, searchField, scroll);
        layout2.setPadding(new javafx.geometry.Insets(20));
        layout2.setAlignment(Pos.TOP_LEFT); //Mengatur perataan elemen dalam tata letak utama
        layout2.setId("search-box");

        // Memuat data awal
        updateSearchResults(searchBox, "", primaryStage);

        // Penanganan acara untuk perubahan teks dalam kolom pencarian
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSearchResults(searchBox, newValue, primaryStage);
        });

        //Mengembalikan objek Scene yang telah dibuat
        Scene scene = new Scene(layout2, 500, 600);
        scene.getStylesheets().add("/styles/stylesSearch.css");
        return scene;
    }


    // Metode untuk memperbarui hasil pencarian
    public static void updateSearchResults(VBox searchBox, String keyword, Stage primaryStage) {

        // Mendapatkan hasil pencarian dari database
        List<String[]> searchResults = DbManager.cariData(keyword);

        // Menghapus hasil pencarian sebelumnya
        searchBox.getChildren().clear();

        // Menampilkan hasil pencarian dalam VBox di dalam ScrollPane
        for (String[] result : searchResults) {
            Label resultLabel = new Label(result[3] + " - " + result[1]);
            resultLabel.setPrefWidth(400);
            resultLabel.setId("result-label");
            
            // Mengatur aksi ketika label diklik
            resultLabel.setOnMouseClicked(ev -> {
                Scene detailScene = DaftarPengingatHarian.detailScene(primaryStage, result, primaryStage.getScene());
                primaryStage.setScene(detailScene);
            });
            searchBox.getChildren().add(resultLabel);
        }
    }
}

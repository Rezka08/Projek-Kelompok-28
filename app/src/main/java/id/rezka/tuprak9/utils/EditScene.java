package id.rezka.tuprak9.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import id.rezka.tuprak9.controller.DbManager;
import id.rezka.tuprak9.DaftarPengingatHarian;
import id.rezka.tuprak9.InputJadwal;
import id.rezka.tuprak9.MyList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditScene extends InputJadwal {

    public static Scene createEditScene(Stage primaryStage, String[] scheduleDetails, Scene previousScene) {

        //untuk main layout pda edit scene
        VBox editForm = new VBox(10);
        editForm.setAlignment(Pos.TOP_CENTER);
        editForm.setPadding(new Insets(20));
        editForm.setId("edit-form");

        //judul pda scene
        Label editLabel = new Label("Edit Schedule");
        editLabel.setId("label-edit");

        // TextField untuk Title
        Label judulLabel = new Label("Title:");
        TextField isijudul = new TextField(scheduleDetails[1]);
        // pembatasan input isi judul tdk boleh lebih dri 50 characters
        isijudul.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 50) {
                isijudul.setText(oldValue);
            }
        });

        setJenisPrioritas(scheduleDetails[2]);

        Label priorityFieldLabel = new Label("Priority Type:");
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

        // Sesuaikan id tombol prioritas sesuai dengan jenisPrioritas
        if ("Low".equals(getJenisPrioritas())) {
            rendahPrio.setId("click-rendah");
        } else if ("Medium".equals(getJenisPrioritas())) {
            sedangPrio.setId("click-sedang");
        } else if ("High".equals(getJenisPrioritas())) {
            tinggiPrio.setId("click-tinggi");
        }

        // HBox untuk menampung tombol prioritas
        HBox jPbutton = new HBox(10, rendahPrio, sedangPrio, tinggiPrio);
        jPbutton.setAlignment(Pos.CENTER);
        jPbutton.setSpacing(10);

        VBox labelBox = new VBox(10, editLabel, judulLabel, isijudul, priorityFieldLabel, jPbutton);
        labelBox.setAlignment(Pos.CENTER_LEFT);
        editForm.getChildren().add(labelBox);

        // DatePicker for Date
        Label dateFieldLabel = new Label("Date:");
        TextField datePicker = new TextField(scheduleDetails[3]);
        TextField timeField = new TextField(scheduleDetails[4]);
        datePicker.setPrefWidth(400);
        datePicker.setOnMouseClicked(e -> {
            Stage timePopup = TambahWaktu.tambahWaktuTanggal(primaryStage);
            timePopup.showAndWait();
            LocalDate selectedDate = TambahWaktu.getTanggal();
            LocalTime selectedTime = TambahWaktu.getWaktu();
            if (selectedDate != null) {
                datePicker.setText(selectedDate.toString());
            }
            if (selectedTime != null) {
                timeField.setText(selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            }
        });

        // TextField for Time
        Label timeFieldLabel = new Label("Time:");
        timeField.setPrefWidth(400);
        timeField.setOnMouseClicked(e -> {
            Stage timePopup = TambahWaktu.tambahWaktuTanggal(primaryStage);
            timePopup.showAndWait();
            LocalDate selectedDate = TambahWaktu.getTanggal();
            LocalTime selectedTime = TambahWaktu.getWaktu();
            if (selectedDate != null) {
                datePicker.setText(selectedDate.toString());
            }
            if (selectedTime != null) {
                timeField.setText(selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            }
        });

        VBox waktunyaBox = new VBox(10, dateFieldLabel, datePicker, timeFieldLabel, timeField);
        waktunyaBox.setAlignment(Pos.CENTER_LEFT);
        editForm.getChildren().addAll(waktunyaBox);

        // TextArea for Description
        Label descriptionFieldLabel = new Label("Description:");
        TextArea descriptionArea = new TextArea(scheduleDetails[5]);
        descriptionArea.setWrapText(true);
        descriptionArea.setOnMouseClicked(e -> {
            Stage descPopup = TambahDeskripsi.tambahDeskrip(primaryStage);
            descPopup.showAndWait();
            String newDescription = TambahDeskripsi.getDeskripsi();
            if (newDescription != null && !newDescription.isEmpty()) {
                descriptionArea.setText(newDescription);
            }
        });
        waktunyaBox.getChildren().addAll(descriptionFieldLabel, descriptionArea);

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(100);
        saveButton.setOnAction(e -> {
            String judul = isijudul.getText().trim();
            String priority = getJenisPrioritas();

            // menggunakan inisiasi tanggal dan waktu jika blm diubah user
            String dateString = datePicker.getText().trim().isEmpty() ? scheduleDetails[3] : datePicker.getText().trim();
            String timeString = timeField.getText().trim().isEmpty() ? scheduleDetails[4] : timeField.getText().trim();

            // menggunakan inisiasi deskripsi yang sblmnya jika blm diubah oleh user
            String deskripsi = (descriptionArea.getText() != null) ? descriptionArea.getText().trim() : "";

            String pesanError = null;
            if (judul.isEmpty() || priority == null || dateString.isEmpty() || timeString.isEmpty()) {
                pesanError = "Title, Priority Type, Date and Time must not be empty.";
            } else if (judul.isEmpty()) {
                pesanError = "The Title cannot be empty.";
            } else if (priority == null) {
                pesanError = "Priority type cannot be empty.";
            } else if (dateString.isEmpty() || timeString.isEmpty()) {
                pesanError = "Date and Time cannot be empty.";
            }

            if (pesanError != null) {
                NotifInputJadwal.showErrorPopup(primaryStage, pesanError);
            } else {
                int id = Integer.parseInt(scheduleDetails[0]);
                LocalDate tanggal = LocalDate.parse(dateString);
                LocalTime waktu = LocalTime.parse(timeString);
                DbManager.updateData(id, judul, priority, tanggal, waktu, deskripsi);
                MyList.updateList(primaryStage);

                // Update scheduleDetails array
                scheduleDetails[1] = judul;
                scheduleDetails[2] = priority;
                scheduleDetails[3] = tanggal.toString();
                scheduleDetails[4] = waktu.toString();
                scheduleDetails[5] = deskripsi;

                DaftarPengingatHarian.updateDetails(scheduleDetails);
                primaryStage.setScene(previousScene);
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(100);
        cancelButton.setOnAction(e -> {
            primaryStage.setScene(previousScene);
        });

        HBox buttonBox = new HBox(300, cancelButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER);
        editForm.getChildren().add(buttonBox);

        Scene scene = new Scene(editForm, 500, 600);
        scene.getStylesheets().add("styles/stylesEdit.css");
        return scene;
    }
}
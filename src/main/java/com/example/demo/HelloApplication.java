package com.example.demo;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HelloApplication extends Application {
    private final Map<String, Integer> wordFrequencyMap = new HashMap<>();
    private final TextArea textArea = new TextArea();
    private final TableView<Map.Entry<String, Integer>> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Word Frequency Counter");

        Button openFileButton = new Button("Open Text File");
        openFileButton.setOnAction(e -> openFile());

        Button calculateButton = new Button("Calculate Word Frequency");
        calculateButton.setOnAction(e -> calculateFrequency());

        TableColumn<Entry<String, Integer>, String> wordColumn = new TableColumn<>("Word");
        wordColumn.setCellValueFactory(param -> new Label(param.getValue().getKey()).textProperty());

        TableColumn<Entry<String, Integer>, Number> frequencyColumn = new TableColumn<>("Frequency");
        frequencyColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValue()));

        tableView.getColumns().addAll(wordColumn, frequencyColumn);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(openFileButton, textArea, calculateButton);

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(vbox, tableView);

        Scene scene = new Scene(hbox, 600, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            readFile(selectedFile);
        }
    }

    private void readFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            textArea.setText(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateFrequency() {
        wordFrequencyMap.clear();

        String[] words = textArea.getText().toLowerCase().split("\\s+");

        for (String word : words) {
            word = word.replaceAll("[^a-zA-Zа-яА-Я]", ""); // Remove non-alphabetic characters
            wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
        }

        // Populate the table view
        tableView.getItems().setAll(wordFrequencyMap.entrySet());
    }
}

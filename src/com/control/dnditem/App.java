package com.control.dnditem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.List;

public class App extends Application {

    private ImageView delete;
    private StackPane pane;

    @Override
    public void start(Stage primaryStage) throws Exception {

        pane = getUI();
        attachListeners();

        primaryStage.setScene(new Scene(pane, 300, 300));
        primaryStage.setTitle("Drag and Drop Item");
        primaryStage.show();
    }

    private void attachListeners() {
        delete.setOnDragOver(this::dragOver);
        delete.setOnDragDropped(this::dragDropped);
    }

    private void dragOver(DragEvent event){
        Dragboard dragboard = event.getDragboard();

        if(dragboard.hasFiles() || dragboard.hasImage()){
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }

        event.consume();
    }

    private void dragDropped(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        List<File> files = dragboard.getFiles();
        if(files.size() >= 1){
            showDeleteDialog(event);
            event.setDropCompleted(true);
        }
        event.consume();
    }

    public boolean showDeleteDialog(DragEvent event){
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Button ok = new Button("Ok");
        Button cancel = new Button("Cancel");
        HBox line2 = new HBox(5, ok, cancel);
        layout.getChildren().addAll(new Label("Delete File?"), line2);

        Stage dialog = new Stage(StageStyle.DECORATED);
        dialog.setScene(new Scene(layout));
        ok.setOnAction(event1 -> deleteFile(event.getDragboard().getFiles()));
        cancel.setOnAction(event1 -> dialog.close());
        dialog.showAndWait();

        return true;
    }

    private boolean deleteFile(List<File> files){

        for(File file : files){
            if(!file.delete()){
                return false;
            }
        }

        return true;
    }

    public StackPane getUI() {

        StackPane ui = new StackPane();
        VBox view = new VBox(5);
        view.setAlignment(Pos.CENTER);
        delete = new ImageView("resources/images/delete.png");
        String text = "This idiot allows you delete files\nwithout even confirming with you\nJust Drag and Drop with CAUTION";
        view.getChildren().addAll(new Text(text), delete);
        delete.setFitWidth(100);
        delete.setFitHeight(100);
        ui.getChildren().addAll(view);

        return ui;
    }
}

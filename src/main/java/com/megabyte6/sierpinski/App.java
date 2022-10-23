package com.megabyte6.sierpinski;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    private BorderPane root = new BorderPane();
    private Scene mainScene = new Scene(root);
    SierpinskiTrianglePane trianglePane = new SierpinskiTrianglePane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initStage(primaryStage);
        primaryStage.show();
    }

    private void initStage(Stage primaryStage) {
        primaryStage.setTitle("Sierpinski's Triangle");
        primaryStage.setScene(mainScene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        root.setCenter(trianglePane);

        trianglePane.widthProperty().addListener(ov -> trianglePane.paint());
        trianglePane.heightProperty().addListener(ov -> trianglePane.paint());

        trianglePane.paint();

        trianglePane.setOrder(5);
    }

}

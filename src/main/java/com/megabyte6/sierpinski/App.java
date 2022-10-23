package com.megabyte6.sierpinski;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class App extends Application {

    private Pane root = new Pane();
    private Scene mainScene = new Scene(root);

    private boolean reloadKeyHeld = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        // Add event listeners.
        root.widthProperty().addListener(ob -> render(root));
        root.heightProperty().addListener(ob -> render(root));
        mainScene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                render(root);
            }
        });
        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.R && reloadKeyHeld == false) {
                    reloadKeyHeld = true;
                    render(root);
                }
            }
        });
        mainScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.R)
                    reloadKeyHeld = false;
            }
        });

        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sierpinski's Triangle");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.setScene(mainScene);

        // Show initialized window.
        primaryStage.show();
        render(root);
    }

    public void render(Pane root) {
        // Select three points in proportion to the pane size
        Point2D p1 = new Point2D(root.getWidth() / 2, 0);
        Point2D p2 = new Point2D(10, root.getHeight());
        Point2D p3 = new Point2D(root.getWidth() - 10, root.getHeight());

        root.getChildren().clear(); // Clear the pane before redisplay

        displayTriangles(5, p1, p2, p3);
    }

    private void displayTriangles(int levels, Point2D p1, Point2D p2, Point2D p3) {
        if (levels == 0) {
            // Draw a triangle to connect three points
            Polygon triangle = new Polygon();
            triangle.getPoints().addAll(p1.getX(), p1.getY(), p2.getX(),
                    p2.getY(), p3.getX(), p3.getY());
            triangle.setStroke(Color.BLACK);
            triangle.setFill(Color.WHITE);

            root.getChildren().add(triangle);
        } else {
            // Get the midpoint on each edge in the triangle
            Point2D p12 = p1.midpoint(p2);
            Point2D p23 = p2.midpoint(p3);
            Point2D p31 = p3.midpoint(p1);

            // Recursively display three triangles
            displayTriangles(levels - 1, p1, p12, p31);
            displayTriangles(levels - 1, p12, p2, p23);
            displayTriangles(levels - 1, p31, p23, p3);
        }
    }

}

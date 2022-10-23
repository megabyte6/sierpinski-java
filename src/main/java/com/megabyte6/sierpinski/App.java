package com.megabyte6.sierpinski;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class App extends Application {

    private final Background BACKGROUND_COLOR = new Background(
            new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY));
    private final Color FILL_COLOR = Color.BEIGE;
    private final Color STROKE_COLOR = Color.GRAY;

    private final Pane root = new Pane();
    private final Scene scene = new Scene(root);

    private boolean reloadKeyHeld = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        root.setBackground(BACKGROUND_COLOR);

        // Add event listeners.
        scene.widthProperty().addListener(ob -> render(root));
        scene.heightProperty().addListener(ob -> render(root));
        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                render(root);
            }
        });
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.R && reloadKeyHeld == false) {
                    reloadKeyHeld = true;
                    render(root);
                }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
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
        primaryStage.setScene(scene);

        // Show initialized window.
        primaryStage.show();
        render(root);
    }

    public void render(Pane root) {
        Point2D p1 = new Point2D(root.getWidth() / 2, 0);
        Point2D p2 = new Point2D(0, root.getHeight());
        Point2D p3 = new Point2D(root.getWidth(), root.getHeight());

        root.getChildren().clear(); // Clear the pane before redisplay

        displayTriangles(5, p1, p2, p3);
    }

    private void displayTriangles(int levels, Point2D p1, Point2D p2, Point2D p3) {
        if (levels == 0) {
            // Draw a triangle to connect three points
            Polygon triangle = new Polygon();
            triangle.getPoints().addAll(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
            triangle.setStroke(STROKE_COLOR);
            triangle.setFill(FILL_COLOR);

            root.getChildren().add(triangle);
        } else {
            // Get the midpoint of the side of the triangle
            Point2D p1_2 = p1.midpoint(p2);
            Point2D p2_3 = p2.midpoint(p3);
            Point2D p3_1 = p3.midpoint(p1);

            // Recursively draw the inner triangles.
            displayTriangles(levels - 1, p1, p1_2, p3_1);
            displayTriangles(levels - 1, p1_2, p2, p2_3);
            displayTriangles(levels - 1, p3_1, p2_3, p3);
        }
    }

}

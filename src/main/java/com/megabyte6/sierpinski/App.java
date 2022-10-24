package com.megabyte6.sierpinski;

import static java.lang.Math.sqrt;

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

    private final int WIDTH = 600;
    private final int HEIGHT = 400;

    private final int SCROLL_SPEED = 30;
    private final int DETAIL_LEVEL = 6;
    private final Background BACKGROUND_COLOR = new Background(
            new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY));
    private final Color FILL_COLOR = Color.BEIGE;
    private final Color STROKE_COLOR = Color.GREY;

    private final Pane root = new Pane();
    private final Scene scene = new Scene(root);

    private int triangleHeight = HEIGHT;

    private boolean reloadKeyHeld = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        root.setBackground(BACKGROUND_COLOR);

        // Add event listeners.
        scene.widthProperty().addListener(ob -> render());
        scene.heightProperty().addListener(ob -> render());
        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    triangleHeight += SCROLL_SPEED;
                } else {
                    triangleHeight -= SCROLL_SPEED;
                }
                render();
            }
        });
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.R && reloadKeyHeld == false) {
                    reloadKeyHeld = true;
                    render();
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
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setScene(scene);

        // Show initialized window.
        primaryStage.show();
        render();
    }

    private void render() {
        // Check if the triangle is too small or too big.
        if (triangleHeight < root.getHeight()) {
            triangleHeight *= 2;
        } else if (triangleHeight > root.getHeight() * 2) {
            triangleHeight /= 2;
        }

        // Use the equation h = (√3 / 2) * a
        // Where h is the height, a is the side length.

        // Calculate side length.
        final double sideLength = triangleHeight / (sqrt(3) / 2);
        // Find points of outermost triangle.
        Point2D p1 = new Point2D(
                root.getWidth() / 2,
                0);
        Point2D p2 = new Point2D(
                (root.getWidth() / 2) - (sideLength / 2),
                triangleHeight);
        Point2D p3 = new Point2D(
                (root.getWidth() / 2) + (sideLength / 2),
                triangleHeight);

        // Clear the pane before rerender
        root.getChildren().clear();

        displayTriangles(DETAIL_LEVEL, p1, p2, p3);
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

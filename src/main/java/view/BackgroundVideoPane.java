package view;

import javafx.geometry.Insets;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class BackgroundVideoPane extends StackPane {

    public BackgroundVideoPane() {
        getStyleClass().add("background-media-pane");
        setPickOnBounds(false);

        Rectangle baseLayer = new Rectangle();
        baseLayer.widthProperty().bind(widthProperty());
        baseLayer.heightProperty().bind(heightProperty());
        baseLayer.setFill(new LinearGradient(
                0, 0, 1, 1, true, null,
                new Stop(0, Color.web("#0F172A")),
                new Stop(1, Color.web("#0B1220"))
        ));
        getChildren().add(baseLayer);

        getChildren().add(createNetworkLayer());
        getChildren().add(createSoftGlowLayer());

        Pane overlay = new Pane();
        overlay.setMouseTransparent(true);
        overlay.getStyleClass().add("background-overlay");
        overlay.setPadding(new Insets(0));
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        getChildren().add(overlay);
    }

    private Pane createNetworkLayer() {
        Pane networkPane = new Pane();
        networkPane.setMouseTransparent(true);
        networkPane.prefWidthProperty().bind(widthProperty());
        networkPane.prefHeightProperty().bind(heightProperty());

        double[][] points = {
                {140, 120}, {380, 80}, {700, 140}, {980, 80}, {1180, 160},
                {180, 420}, {420, 520}, {740, 480}, {1060, 540}, {320, 260},
                {860, 320}, {560, 620}
        };

        int[][] connections = {
                {0, 1}, {1, 2}, {2, 3}, {3, 4}, {0, 5}, {1, 6}, {2, 9}, {3, 10}, {4, 11}, {5, 6}, {6, 7}, {7, 8}
        };

        for (int[] link : connections) {
            double[] from = points[link[0]];
            double[] to = points[link[1]];
            Line line = new Line(from[0], from[1], to[0], to[1]);
            line.setStroke(Color.web("#38BDF8", 0.16));
            line.setStrokeWidth(1.2);
            line.setSmooth(true);
            networkPane.getChildren().add(line);
        }

        for (double[] point : points) {
            Circle node = new Circle(point[0], point[1], 4.5, Color.web("#38BDF8", 0.24));
            node.setEffect(new DropShadow(10, Color.web("#38BDF8", 0.18)));
            networkPane.getChildren().add(node);

            Circle halo = new Circle(point[0], point[1], 10, Color.web("#38BDF8", 0.08));
            halo.setEffect(new BoxBlur(8, 8, 2));
            networkPane.getChildren().add(halo);
        }

        Circle anchor = new Circle(1140, 520, 22, Color.web("#38BDF8", 0.10));
        anchor.setEffect(new BoxBlur(20, 20, 3));
        networkPane.getChildren().add(anchor);

        return networkPane;
    }

    private Pane createSoftGlowLayer() {
        Pane glowPane = new Pane();
        glowPane.setMouseTransparent(true);
        glowPane.prefWidthProperty().bind(widthProperty());
        glowPane.prefHeightProperty().bind(heightProperty());

        Rectangle glow = new Rectangle();
        glow.widthProperty().bind(widthProperty());
        glow.heightProperty().bind(heightProperty());
        glow.setFill(new LinearGradient(
                0, 0, 1, 1, true, null,
                new Stop(0, Color.web("#0F172A", 0.00)),
                new Stop(0.35, Color.web("#38BDF8", 0.04)),
                new Stop(0.6, Color.web("#0F172A", 0.00)),
                new Stop(1, Color.web("#0F172A", 0.00))
        ));
        glow.setMouseTransparent(true);
        glowPane.getChildren().add(glow);

        return glowPane;
    }

    /**
     * Stops any long-running or background effects used by the background pane.
     * This method is included to support clean application shutdown.
     */
    public void stop() {
        // No active media process in the current visual background implementation,
        // but this method exists so the app can safely release future resources.
    }
}

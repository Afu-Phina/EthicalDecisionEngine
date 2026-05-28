package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class BackgroundVideoPane extends StackPane {
    private static final String VIDEO_PATH = "/visuals/dashboard-background.mp4";
    private final MediaPlayer mediaPlayer;

    public BackgroundVideoPane() {
        getStyleClass().add("background-media-pane");
        setPickOnBounds(false);

        Rectangle baseLayer = new Rectangle();
        baseLayer.widthProperty().bind(widthProperty());
        baseLayer.heightProperty().bind(heightProperty());
        baseLayer.setFill(new LinearGradient(
                0, 0, 1, 1, true, null,
                new Stop(0, Color.web("#07101f")),
                new Stop(1, Color.web("#081627"))
        ));

        getChildren().add(baseLayer);

        MediaPlayer player = null;
        URL videoUrl = getClass().getResource(VIDEO_PATH);
        if (videoUrl != null) {
            try {
                Media media = new Media(videoUrl.toExternalForm());
                player = new MediaPlayer(media);
                MediaView mediaView = new MediaView(player);
                mediaView.setPreserveRatio(false);
                mediaView.fitWidthProperty().bind(widthProperty());
                mediaView.fitHeightProperty().bind(heightProperty());
                mediaView.setOpacity(0.28);
                mediaView.setSmooth(true);
                mediaView.setCache(true);
                mediaView.setCacheHint(CacheHint.SPEED);
                getChildren().add(0, mediaView);
                player.setCycleCount(MediaPlayer.INDEFINITE);
                player.setMute(true);
                player.setAutoPlay(true);
            } catch (MediaException exception) {
                System.err.println("Failed to load background video: " + exception.getMessage());
                player = null;
            }
        }

        if (player == null) {
            getChildren().add(createFallbackAnimation());
        }

        Pane overlay = new Pane();
        overlay.setMouseTransparent(true);
        overlay.getStyleClass().add("background-overlay");
        overlay.setPadding(new Insets(0));
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        getChildren().add(overlay);

        mediaPlayer = player;
    }

    private Pane createFallbackAnimation() {
        Pane particleLayer = new Pane();
        particleLayer.setMouseTransparent(true);
        particleLayer.prefWidthProperty().bind(widthProperty());
        particleLayer.prefHeightProperty().bind(heightProperty());

        List<Circle> nodes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Circle particle = new Circle(18 + Math.random() * 20, Color.web("#5ed0ff", 0.08));
            particle.setCenterX(120 + Math.random() * 1000);
            particle.setCenterY(80 + Math.random() * 620);
            particle.setEffect(new BoxBlur(10, 10, 3));
            particleLayer.getChildren().add(particle);
            nodes.add(particle);
        }

        Timeline timeline = new Timeline();
        for (Circle particle : nodes) {
            double targetX = 80 + Math.random() * 1080;
            double targetY = 60 + Math.random() * 760;
            KeyFrame frame = new KeyFrame(Duration.seconds(18 + Math.random() * 12),
                    new KeyValue(particle.centerXProperty(), targetX),
                    new KeyValue(particle.centerYProperty(), targetY));
            timeline.getKeyFrames().add(frame);
        }
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Rectangle shimmer = new Rectangle();
        shimmer.widthProperty().bind(widthProperty());
        shimmer.heightProperty().bind(heightProperty());
        shimmer.setFill(new LinearGradient(
                0, 0, 1, 1, true, null,
                new Stop(0, Color.rgb(255, 255, 255, 0.00)),
                new Stop(0.5, Color.rgb(94, 208, 255, 0.04)),
                new Stop(1, Color.rgb(255, 255, 255, 0.00))
        ));
        shimmer.setMouseTransparent(true);

        particleLayer.getChildren().add(shimmer);
        return particleLayer;
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}

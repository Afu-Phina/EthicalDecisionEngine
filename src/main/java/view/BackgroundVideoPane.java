package view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

public class BackgroundVideoPane extends StackPane {
    private final ParallelTransition backgroundAnimation;

    public BackgroundVideoPane() {
        getStyleClass().add("background-media-pane");
        setPickOnBounds(false);

        Rectangle baseLayer = new Rectangle();
        baseLayer.widthProperty().bind(widthProperty());
        baseLayer.heightProperty().bind(heightProperty());
        baseLayer.setFill(new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#050812")),
                new Stop(0.38, Color.web("#071227")),
                new Stop(1, Color.web("#02050d"))
        ));
        baseLayer.setMouseTransparent(true);
        getChildren().add(baseLayer);

        Pane waveLayer = createWaveLayer();
        Pane networkLayer = createNetworkLayer();
        Pane pulseLayer = createDataFlowLayer();
        Pane particleLayer = createParticleField();

        getChildren().addAll(waveLayer, networkLayer, pulseLayer, particleLayer);

        Pane overlay = new Pane();
        overlay.setMouseTransparent(true);
        overlay.getStyleClass().add("background-overlay");
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        getChildren().add(overlay);

        backgroundAnimation = createBackgroundAnimation(waveLayer, particleLayer, pulseLayer);
        backgroundAnimation.play();
    }

    private Pane createWaveLayer() {
        Pane wavePane = new Pane();
        wavePane.setMouseTransparent(true);
        wavePane.prefWidthProperty().bind(widthProperty());
        wavePane.prefHeightProperty().bind(heightProperty());

        Rectangle wave = new Rectangle();
        wave.widthProperty().bind(widthProperty());
        wave.heightProperty().bind(heightProperty());
        wave.setFill(new LinearGradient(
                0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#041224", 0.00)),
                new Stop(0.18, Color.web("#0c2a55", 0.08)),
                new Stop(0.44, Color.web("#0d315f", 0.14)),
                new Stop(0.68, Color.web("#060b1a", 0.16)),
                new Stop(1, Color.web("#041225", 0.00))
        ));
        wave.setMouseTransparent(true);
        wavePane.getChildren().add(wave);

        Circle pulse = new Circle(1420, 180, 178, Color.web("#22d3ee", 0.06));
        pulse.setEffect(new BoxBlur(36, 36, 3));
        pulse.setMouseTransparent(true);
        wavePane.getChildren().add(pulse);

        Circle pulse2 = new Circle(240, 160, 148, Color.web("#a78bfa", 0.04));
        pulse2.setEffect(new BoxBlur(28, 28, 2));
        pulse2.setMouseTransparent(true);
        wavePane.getChildren().add(pulse2);

        return wavePane;
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
            line.setStrokeWidth(1.15);
            line.setStrokeLineCap(StrokeLineCap.ROUND);
            networkPane.getChildren().add(line);
        }

        for (double[] point : points) {
            Circle halo = new Circle(point[0], point[1], 12, Color.web("#38BDF8", 0.10));
            halo.setEffect(new BoxBlur(10, 10, 2));
            halo.setMouseTransparent(true);
            networkPane.getChildren().add(halo);

            Circle node = new Circle(point[0], point[1], 4.2, Color.web("#38BDF8", 0.32));
            node.setEffect(new DropShadow(10, Color.web("#38BDF8", 0.18)));
            node.setMouseTransparent(true);
            networkPane.getChildren().add(node);

            Circle core = new Circle(point[0], point[1], 2.2, Color.web("#ffffff", 0.90));
            core.setMouseTransparent(true);
            networkPane.getChildren().add(core);
        }

        Circle anchor = new Circle(1140, 520, 22, Color.web("#38BDF8", 0.10));
        anchor.setEffect(new BoxBlur(22, 22, 2));
        anchor.setMouseTransparent(true);
        networkPane.getChildren().add(anchor);

        return networkPane;
    }

    private Pane createDataFlowLayer() {
        Pane flowPane = new Pane();
        flowPane.setMouseTransparent(true);
        flowPane.prefWidthProperty().bind(widthProperty());
        flowPane.prefHeightProperty().bind(heightProperty());

        Path flow = new Path();
        flow.setStroke(Color.web("#7dd3fc", 0.16));
        flow.setStrokeWidth(1.6);
        flow.setStrokeLineCap(StrokeLineCap.ROUND);
        flow.getStrokeDashArray().addAll(10.0, 12.0);
        flow.setFill(Color.TRANSPARENT);
        flow.getElements().addAll(
                new MoveTo(80, 760),
                new CubicCurveTo(260, 620, 500, 740, 680, 620),
                new CubicCurveTo(810, 540, 920, 720, 1120, 640)
        );

        Path flow2 = new Path();
        flow2.setStroke(Color.web("#c084fc", 0.12));
        flow2.setStrokeWidth(1.4);
        flow2.getStrokeDashArray().addAll(8.0, 10.0);
        flow2.setStrokeLineCap(StrokeLineCap.ROUND);
        flow2.setFill(Color.TRANSPARENT);
        flow2.getElements().addAll(
                new MoveTo(320, 820),
                new CubicCurveTo(420, 680, 700, 780, 860, 660),
                new CubicCurveTo(960, 600, 1080, 740, 1280, 700)
        );

        flowPane.getChildren().addAll(flow, flow2);
        return flowPane;
    }

    private Pane createParticleField() {
        Pane particlePane = new Pane();
        particlePane.setMouseTransparent(true);
        particlePane.prefWidthProperty().bind(widthProperty());
        particlePane.prefHeightProperty().bind(heightProperty());

        particlePane.getChildren().add(createParticle(310, 190, 5, Color.web("#38bdf8", 0.34)));
        particlePane.getChildren().add(createParticle(950, 130, 4, Color.web("#88f7ff", 0.28)));
        particlePane.getChildren().add(createParticle(520, 320, 4.5, Color.web("#a855f7", 0.26)));
        particlePane.getChildren().add(createParticle(1180, 240, 3.8, Color.web("#38bdf8", 0.28)));
        particlePane.getChildren().add(createParticle(180, 560, 4.2, Color.web("#8b5cf6", 0.22)));
        particlePane.getChildren().add(createParticle(760, 600, 5.4, Color.web("#22d3ee", 0.26)));

        return particlePane;
    }

    private Circle createParticle(double x, double y, double radius, Color color) {
        Circle particle = new Circle(x, y, radius, color);
        particle.setEffect(new BoxBlur(6, 6, 2));
        particle.setMouseTransparent(true);
        return particle;
    }

    private ParallelTransition createBackgroundAnimation(Pane waveLayer, Pane particleLayer, Pane pulseLayer) {
        TranslateTransition waveMove = new TranslateTransition(Duration.seconds(34), waveLayer);
        waveMove.setFromX(-160);
        waveMove.setToX(160);
        waveMove.setAutoReverse(true);
        waveMove.setCycleCount(Animation.INDEFINITE);
        waveMove.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition particleMove1 = createParticleTransition(particleLayer.getChildren().get(0), 24, 12, 18);
        TranslateTransition particleMove2 = createParticleTransition(particleLayer.getChildren().get(1), -28, 16, 22);
        TranslateTransition particleMove3 = createParticleTransition(particleLayer.getChildren().get(2), 14, -18, 20);
        TranslateTransition particleMove4 = createParticleTransition(particleLayer.getChildren().get(3), -16, 14, 24);
        TranslateTransition particleMove5 = createParticleTransition(particleLayer.getChildren().get(4), 18, -12, 26);
        TranslateTransition particleMove6 = createParticleTransition(particleLayer.getChildren().get(5), -22, 18, 30);

        ParallelTransition parallel = new ParallelTransition(
                waveMove,
                particleMove1,
                particleMove2,
                particleMove3,
                particleMove4,
                particleMove5,
                particleMove6
        );
        parallel.setCycleCount(Animation.INDEFINITE);
        return parallel;
    }

    private TranslateTransition createParticleTransition(javafx.scene.Node node, double x, double y, double durationSeconds) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(durationSeconds), node);
        transition.setByX(x);
        transition.setByY(y);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        return transition;
    }

    public void stop() {
        if (backgroundAnimation != null) {
            backgroundAnimation.stop();
        }
    }
}

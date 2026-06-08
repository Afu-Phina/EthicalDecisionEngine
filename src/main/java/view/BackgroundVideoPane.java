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

// creates a decorative animated background for the application UI.
public class BackgroundVideoPane extends StackPane {
    private final ParallelTransition backgroundAnimation;

// This method performs one part of the class behavior.
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

        Pane gridLayer = createTechGridLayer();
        Pane networkLayer = createNetworkLayer();
        Pane pulseLayer = createDataFlowLayer();
        Pane particleLayer = createParticleField();
        Pane nodePulseLayer = createNodePulseLayer();

        getChildren().addAll(gridLayer, networkLayer, pulseLayer, particleLayer, nodePulseLayer);

        Pane overlay = new Pane();
        overlay.setMouseTransparent(true);
        overlay.getStyleClass().add("background-overlay");
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());
        getChildren().add(overlay);

        backgroundAnimation = createBackgroundAnimation(gridLayer, particleLayer, pulseLayer);
        backgroundAnimation.play();
    }

// This method performs one part of the class behavior.
    private Pane createTechGridLayer() {
        Pane gridPane = new Pane();
        gridPane.setMouseTransparent(true);
        gridPane.prefWidthProperty().bind(widthProperty());
        gridPane.prefHeightProperty().bind(heightProperty());

// Repeat the code inside this block for each item or while the loop condition remains true.
        for (int index = 1; index <= 12; index++) {
            double y = index * 76;
            Line line = new Line(0, y, 1600, y);
            line.setStroke(Color.web("#38BDF8", 0.04));
            line.setStrokeWidth(1);
            line.setMouseTransparent(true);
            gridPane.getChildren().add(line);
        }

// Repeat the code inside this block for each item or while the loop condition remains true.
        for (int index = 1; index <= 18; index++) {
            double x = index * 88;
            Line line = new Line(x, 0, x, 920);
            line.setStroke(Color.web("#a855f7", 0.03));
            line.setStrokeWidth(1);
            line.setMouseTransparent(true);
            gridPane.getChildren().add(line);
        }

// Return this value to the method caller so the result can be used elsewhere.
        return gridPane;
    }

// This method performs one part of the class behavior.
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

// Repeat the code inside this block for each item or while the loop condition remains true.
        for (int[] link : connections) {
            double[] from = points[link[0]];
            double[] to = points[link[1]];
            Line line = new Line(from[0], from[1], to[0], to[1]);
            line.setStroke(Color.web("#38BDF8", 0.16));
            line.setStrokeWidth(1.15);
            line.setStrokeLineCap(StrokeLineCap.ROUND);
            networkPane.getChildren().add(line);
        }

// Repeat the code inside this block for each item or while the loop condition remains true.
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

// Return this value to the method caller so the result can be used elsewhere.
        return networkPane;
    }

// This method performs one part of the class behavior.
    private Pane createDataFlowLayer() {
        Pane flowPane = new Pane();
        flowPane.setMouseTransparent(true);
        flowPane.prefWidthProperty().bind(widthProperty());
        flowPane.prefHeightProperty().bind(heightProperty());

        Path flow = new Path();
        flow.setStroke(Color.web("#7dd3fc", 0.18));
        flow.setStrokeWidth(1.8);
        flow.setStrokeLineCap(StrokeLineCap.ROUND);
        flow.getStrokeDashArray().addAll(10.0, 12.0);
        flow.setFill(Color.TRANSPARENT);
        flow.getElements().addAll(
                new MoveTo(80, 760),
                new CubicCurveTo(260, 620, 500, 740, 680, 620),
                new CubicCurveTo(810, 540, 920, 720, 1120, 640)
        );

        Path flow2 = new Path();
        flow2.setStroke(Color.web("#c084fc", 0.14));
        flow2.setStrokeWidth(1.6);
        flow2.getStrokeDashArray().addAll(8.0, 10.0);
        flow2.setStrokeLineCap(StrokeLineCap.ROUND);
        flow2.setFill(Color.TRANSPARENT);
        flow2.getElements().addAll(
                new MoveTo(320, 820),
                new CubicCurveTo(420, 680, 700, 780, 860, 660),
                new CubicCurveTo(960, 600, 1080, 740, 1280, 700)
        );

        flowPane.getChildren().addAll(flow, flow2);
// Return this value to the method caller so the result can be used elsewhere.
        return flowPane;
    }

// This method performs one part of the class behavior.
    private Pane createNodePulseLayer() {
        Pane pulsePane = new Pane();
        pulsePane.setMouseTransparent(true);
        pulsePane.prefWidthProperty().bind(widthProperty());
        pulsePane.prefHeightProperty().bind(heightProperty());

        Circle beacon1 = createGlowingNode(240, 180, 32, Color.web("#38bdf8", 0.08));
        Circle beacon2 = createGlowingNode(980, 140, 42, Color.web("#a855f7", 0.06));
        Circle beacon3 = createGlowingNode(1180, 520, 52, Color.web("#7dd3fc", 0.06));
        Circle beacon4 = createGlowingNode(520, 540, 36, Color.web("#8b5cf6", 0.07));

        pulsePane.getChildren().addAll(beacon1, beacon2, beacon3, beacon4);
// Return this value to the method caller so the result can be used elsewhere.
        return pulsePane;
    }

// This method performs one part of the class behavior.
    private Circle createGlowingNode(double x, double y, double radius, Color color) {
        Circle node = new Circle(x, y, radius, color);
        node.setMouseTransparent(true);
        node.setEffect(new BoxBlur(20, 20, 3));
        node.setOpacity(0.68);
// Return this value to the method caller so the result can be used elsewhere.
        return node;
    }

// This method performs one part of the class behavior.
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

// Return this value to the method caller so the result can be used elsewhere.
        return particlePane;
    }

// This method performs one part of the class behavior.
    private Circle createParticle(double x, double y, double radius, Color color) {
        Circle particle = new Circle(x, y, radius, color);
        particle.setEffect(new BoxBlur(6, 6, 2));
        particle.setMouseTransparent(true);
// Return this value to the method caller so the result can be used elsewhere.
        return particle;
    }

// This method performs one part of the class behavior.
    private ParallelTransition createBackgroundAnimation(Pane gridLayer, Pane particleLayer, Pane pulseLayer) {
        TranslateTransition gridMove = new TranslateTransition(Duration.seconds(48), gridLayer);
        gridMove.setFromX(-90);
        gridMove.setToX(90);
        gridMove.setAutoReverse(true);
        gridMove.setCycleCount(Animation.INDEFINITE);
        gridMove.setInterpolator(Interpolator.EASE_BOTH);

        TranslateTransition particleMove1 = createParticleTransition(particleLayer.getChildren().get(0), 24, 12, 18);
        TranslateTransition particleMove2 = createParticleTransition(particleLayer.getChildren().get(1), -28, 16, 22);
        TranslateTransition particleMove3 = createParticleTransition(particleLayer.getChildren().get(2), 14, -18, 20);
        TranslateTransition particleMove4 = createParticleTransition(particleLayer.getChildren().get(3), -16, 14, 24);
        TranslateTransition particleMove5 = createParticleTransition(particleLayer.getChildren().get(4), 18, -12, 26);
        TranslateTransition particleMove6 = createParticleTransition(particleLayer.getChildren().get(5), -22, 18, 30);

        TranslateTransition pulseMove = new TranslateTransition(Duration.seconds(60), pulseLayer);
        pulseMove.setFromY(-20);
        pulseMove.setToY(20);
        pulseMove.setAutoReverse(true);
        pulseMove.setCycleCount(Animation.INDEFINITE);
        pulseMove.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition parallel = new ParallelTransition(
                gridMove,
                pulseMove,
                particleMove1,
                particleMove2,
                particleMove3,
                particleMove4,
                particleMove5,
                particleMove6
        );
        parallel.setCycleCount(Animation.INDEFINITE);
// Return this value to the method caller so the result can be used elsewhere.
        return parallel;
    }

// This method performs one part of the class behavior.
    private TranslateTransition createParticleTransition(javafx.scene.Node node, double x, double y, double durationSeconds) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(durationSeconds), node);
        transition.setByX(x);
        transition.setByY(y);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setInterpolator(Interpolator.EASE_BOTH);
// Return this value to the method caller so the result can be used elsewhere.
        return transition;
    }

// This method performs one part of the class behavior.
    public void stop() {
// If the condition inside the parentheses is true, the code inside the block will run.
        if (backgroundAnimation != null) {
            backgroundAnimation.stop();
        }
    }
}

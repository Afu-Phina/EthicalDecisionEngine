package view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public final class TransitionUtils {
    private TransitionUtils() {
    }

    public static void applySlideFadeIn(Node node) {
        node.setOpacity(0);
        node.setTranslateY(18);

        FadeTransition fade = new FadeTransition(Duration.millis(360), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(360), node);
        slide.setFromY(18);
        slide.setToY(0);

        ParallelTransition transition = new ParallelTransition(fade, slide);
        transition.play();
    }
}

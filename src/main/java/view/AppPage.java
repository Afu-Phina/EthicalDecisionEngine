package view;

import javafx.scene.Node;

public interface AppPage {
    Node getView();
    String getTitle();
    default void refresh() {}
}

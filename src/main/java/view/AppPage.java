package view;

import javafx.scene.Node;

// defines the methods every page in the application must provide.
public interface AppPage {
    Node getView();
    String getTitle();
    default void refresh() {}
}

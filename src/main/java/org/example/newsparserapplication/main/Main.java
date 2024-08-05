/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package org.example.newsparserapplication.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.newsparserapplication.app.NewsApp;

/**
 * Main class for launching the NewsParser JavaFX application.
 * */
public class Main extends Application {

    /**
     * The entry point for the JavaFX application.
     * This method is called when the application is launched.
     * It initializes the main stage of the application, creates an instance
     * of the NewsApp class, and sets up the scene with a specified width and height.
     * Finally, it displays the primary stage with the title "News".
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        NewsApp newsApp = new NewsApp();
        Scene scene = new Scene(newsApp, 800, 600);

        primaryStage.setTitle("News");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
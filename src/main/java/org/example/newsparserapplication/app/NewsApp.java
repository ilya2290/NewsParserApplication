/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package org.example.newsparserapplication.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.example.newsparserapplication.news.News;
import org.example.newsparserapplication.service.NewsService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The NewsApp class provides a user interface for displaying news articles in a JavaFX application.
 * It manages UI components, interacts with the news service, filters news by date, and handles navigation.
 */
public class NewsApp extends BorderPane {
    private final Label headlineLabel = new Label();
    private final Label publicationDateLabel = new Label();
    private final NewsService newsService = new NewsService();
    private final TextArea descriptionTextArea = new TextArea();

    private final List<LocalDate> requestedDates = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();
    private List<News> filteredNewsList = new ArrayList<>();
    private int currentIndex;
    boolean showNews = false;
    private DatePicker datePicker;
    private Button nextButton;
    private Button prevButton;
    private Button morningButton;
    private Button afternoonButton;
    private Button eveningButton;
    private HBox navigationBox;
    private HBox publicationDateBox;



    public NewsApp() {
        this.saveNews(LocalDate.now());
        this.initAppElements();
    }

    /**
     * Checks if news for the specified date has already been requested.
     * If not, fetches news from the service and updates the news list.
     * @param selectedDate The date to check.
     */
    private void checkNewsExistence(LocalDate selectedDate) {
        if (!this.requestedDates.contains(selectedDate)) {
            requestedDates.add(selectedDate);
            Optional<List<News>> newsByAnotherDay = this.newsService.getNewsByPostedDate(selectedDate);
            this.newsList.addAll(newsByAnotherDay.orElse(Collections.emptyList()));
        }
    }

    /**
     * Filters the list of news items by the specified date.
     * @param date The date to filter news by.
     * @return A list of news items published on the specified date.
     */
    private List<News> filterNewsByDate(LocalDate date) {
        return this.newsList.stream()
                .filter(news -> news.getPublicationTime().toLocalDate().isEqual(date))
                .toList();
    }

    /**
     * Filters news based on the selected date from the DatePicker and updates the display.
     */
    private void filterNewsByDate() {
        LocalDate selectedDate = datePicker.getValue();

        this.checkNewsExistence(selectedDate);

        filteredNewsList = filterNewsByDate(selectedDate);

        if (filteredNewsList.isEmpty()) {
            this.noNewsSet();
            currentIndex = 0;
        } else {
            for (int i = 0; i < filteredNewsList.size(); i++) {
                currentIndex = i;

                this.showNews = true;

                this.showNews();

                News currentNews = filteredNewsList.get(currentIndex);

                headlineLabel.setText(currentNews.getHeadline());
                descriptionTextArea.setText(currentNews.getDescription());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String str = currentNews.getPublicationTime().format(formatter);

                publicationDateLabel.setText(str);
            }
        }
    }

    /**
     * Initializes the application elements including the DatePicker, buttons, and layout.
     */
        private void initAppElements() {
        this.initDatePicker();
        this.initButtons();
        this.initNavigationBox();
        this.initTextArea();
        this.initLabel();
        this.initPostedDateBox();
        this.initFilterButtons();

        this.initContentBox();
    }
    /**
     *
     * Initializes the navigation buttons for the application.
     * Specifically, this method sets up the "Previous" and "Next" buttons,
     * which are used to navigate through the list of news items.
     * It invokes the methods `previousButton()` and `nextButton()` to create
     * and configure these buttons, and set their respective event handlers.
     */
    private void initButtons() {
        this.previousButton();
        this.nextButton();
    }


    /**
     * Initializes the content box which contains all primary UI elements.
     */
    private void initContentBox() {
        VBox contentBox = new VBox(20, datePicker, headlineLabel, descriptionTextArea, publicationDateBox, navigationBox);
        contentBox.setPadding(new Insets(10));
        this.setCenter(contentBox);
    }

    /**
     * Initializes the DatePicker with the current date and restricts future dates.
     */
    private void initDatePicker() {
        this.datePicker = new DatePicker(LocalDate.now());

        datePicker.setDayCellFactory(new Callback<>() {
            @Override
            public javafx.scene.control.DateCell call(DatePicker datePicker) {
                return new javafx.scene.control.DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);

                        // Disable future dates
                        if (date != null && date.isAfter(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #f5f5f5;");
                        }
                    }
                };
            }
        });

        this.initNews();

        this.datePicker.setOnAction(_ -> filterNewsByDate());
    }

    /**
     * Initializes the filter buttons for filtering news by different times of day.
     */
    private void initFilterButtons() {
        this.morningButton = new Button("Morning");
        this.afternoonButton = new Button("Afternoon");
        this.eveningButton = new Button("Evening");

        this.morningButton.setOnAction(e -> filterNewsByTime("morning"));
        this.afternoonButton.setOnAction(e -> filterNewsByTime("afternoon"));
        this.eveningButton.setOnAction(e -> filterNewsByTime("evening"));

        // Add buttons to navigation box or create a new VBox
        HBox filterBox = new HBox(10, morningButton, afternoonButton, eveningButton);
        filterBox.setPadding(new Insets(10));

        // Add filterBox to contentBox or another appropriate container
        this.setTop(filterBox);
    }

    /**
     * Initializes the navigation box which contains buttons for navigating between news items.
     */
    private void initNavigationBox() {
        this.navigationBox = new HBox(10, prevButton, nextButton);
    }

    /**
     * Initializes the label for displaying the headline of the news item.
     */
    private void initLabel() {
        headlineLabel.setWrapText(true);
        headlineLabel.setPrefWidth(800);
    }

    /**
     * Initializes the news list based on the current date.
     */
    private void initNews() {
        this.filterNewsByDate();
    }

    /**
     * Initializes the previous button for navigating to the previous news item.
     */
    private void previousButton() {
        this.prevButton = new Button("Previous");
        this.prevButton.setOnAction(_ -> showPreviousNews());
    }

    /**
     * Initializes the next button for navigating to the next news item.
     */
    private void nextButton() {
        this.nextButton = new Button("Next");
        this.nextButton.setOnAction(_ -> showNextNews());
    }

    /**
     * Initializes the label for displaying the publication date of the news item.
     */
    private void initPostedDateBox() {
        this.publicationDateBox = new HBox();

        publicationDateBox.setPadding(new Insets(10));
        publicationDateBox.setAlignment(Pos.BOTTOM_RIGHT);
        publicationDateBox.getChildren().add(this.publicationDateLabel);
    }

    /**
     * Initializes the text area for displaying the news description.
     */
    private void initTextArea() {
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.setEditable(false);
    }

    /**
     * Shows the current news item based on the index and filtered list.
     */
    private void showNews() {
        if (currentIndex >= 0 && currentIndex < filteredNewsList.size()) {
            News currentNews = filteredNewsList.get(currentIndex);
            headlineLabel.setText(currentNews.getHeadline());
            descriptionTextArea.setText(currentNews.getDescription());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String str = currentNews.getPublicationTime().format(formatter);

            publicationDateLabel.setText(str);
        }
    }

    /**
     * Shows the next news item in the filtered list.
     */
    private void showNextNews() {
        if (currentIndex < filteredNewsList.size() - 1) {
            currentIndex++;

            if (showNews) {
                showNews();
            }
        }
    }

    /**
     * Shows the previous news item in the filtered list.
     */
    private void showPreviousNews() {
        if (currentIndex > 0) {
            currentIndex--;

            if (showNews) {
                showNews();
            }
        }
    }

    /**
     * Updates the UI when no news is available for the selected date.
     */
    private void noNewsSet() {
        this.headlineLabel.setText("No new news.");
        this.descriptionTextArea.clear();
        this.publicationDateLabel.setText("");
    }

    /**
     * Saves news for the specified date by requesting it from the news service.
     * @param date The date for which to save news.
     */
    public void saveNews(LocalDate date) {
        if (this.newsService.getNewsByPostedDate(date).isPresent()) {
            this.newsList = this.newsService.getNewsByPostedDate(date).get();
        }
        this.requestedDates.add(date);
    }

    /**
     * Filters news based on the selected date and time of day.
     *
     * @param timeOfDay The time period to filter by: "morning", "afternoon", or "evening".
     * If the selected date is null, or if no news matches the criteria, appropriate actions are taken
     * to handle these cases.
     */
    private void filterNewsByTime(String timeOfDay) {
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate == null) {
            return;
        }

        this.checkNewsExistence(selectedDate);
        filteredNewsList = filterNewsByDate(selectedDate);

        switch (timeOfDay) {
            case "morning":
                filteredNewsList = filteredNewsList.stream()
                        .filter(news -> isMorning(news.getPublicationTime()))
                        .toList();
                break;
            case "afternoon":
                filteredNewsList = filteredNewsList.stream()
                        .filter(news -> isAfternoon(news.getPublicationTime()))
                        .toList();
                break;
            case "evening":
                filteredNewsList = filteredNewsList.stream()
                        .filter(news -> isEvening(news.getPublicationTime()))
                        .toList();
                break;
            default:
                filteredNewsList = Collections.emptyList();
        }

        if (filteredNewsList.isEmpty()) {
            noNewsSet();
        } else {
            currentIndex = 0;
            showNews();
        }
    }

    /**
     * Checks if the given time is during the morning (6:00 - 11:59).
     *
     * @param dateTime The LocalDateTime to check.
     * @return true if the time is morning, false otherwise.
     */
    private boolean isMorning(LocalDateTime dateTime) {
        return dateTime.getHour() >= 6 && dateTime.getHour() < 12;
    }

    /**
     * Checks if the given time is during the afternoon (12:00 - 17:59).
     *
     * @param dateTime The LocalDateTime to check.
     * @return true if the time is afternoon, false otherwise.
     */
    private boolean isAfternoon(LocalDateTime dateTime) {
        return dateTime.getHour() >= 12 && dateTime.getHour() < 18;
    }

    /**
     * Checks if the given time is during the evening (18:00 and later).
     *
     * @param dateTime The LocalDateTime to check.
     * @return true if the time is evening, false otherwise.
     */
    private boolean isEvening(LocalDateTime dateTime) {
        return dateTime.getHour() >= 18;
    }
}

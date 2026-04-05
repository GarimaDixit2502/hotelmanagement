import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    Hotel hotel = new Hotel();

    // ── Color Palette ─────────────────────────────────────────────────────────
    private static final String NAVY       = "#1A1A2E";
    private static final String GOLD       = "#C9A96E";
    private static final String CREAM      = "#F8F6F2";
    private static final String WHITE      = "#FFFFFF";
    private static final String BORDER     = "#E0DDD6";
    private static final String TEXT_MAIN  = "#1A1A2E";
    private static final String TEXT_MUTED = "#888780";
    private static final String GREEN_FG   = "#3B6D11";
    private static final String AMBER_BG   = "#FDF8EE";
    private static final String AMBER_FG   = "#BA7517";
    private static final String RED_FG     = "#A32D2D";

    // ── Style helpers ─────────────────────────────────────────────────────────

    private String cardStyle() {
        return "-fx-background-color:" + WHITE + ";"
                + "-fx-background-radius:10;"
                + "-fx-border-color:" + BORDER + ";"
                + "-fx-border-radius:10;"
                + "-fx-border-width:1;";
    }

    private Button primaryBtn(String text) {
        Button b = new Button(text);
        String base  = "-fx-background-color:" + NAVY + ";-fx-text-fill:white;"
                + "-fx-font-size:12px;-fx-font-weight:bold;"
                + "-fx-background-radius:7;-fx-padding:9 22 9 22;-fx-cursor:hand;";
        String hover = "-fx-background-color:#2A2A4E;-fx-text-fill:white;"
                + "-fx-font-size:12px;-fx-font-weight:bold;"
                + "-fx-background-radius:7;-fx-padding:9 22 9 22;-fx-cursor:hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }

    private Button goldBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + GOLD + ";-fx-text-fill:white;"
                + "-fx-font-size:12px;-fx-font-weight:bold;"
                + "-fx-background-radius:7;-fx-padding:9 22 9 22;-fx-cursor:hand;");
        return b;
    }

    private Button outlineBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:" + TEXT_MAIN + ";"
                + "-fx-font-size:12px;-fx-font-weight:bold;-fx-background-radius:7;"
                + "-fx-border-color:" + BORDER + ";-fx-border-radius:7;-fx-border-width:1;"
                + "-fx-padding:8 18 8 18;-fx-cursor:hand;");
        return b;
    }

    private <T> ComboBox<T> styledCombo(String prompt) {
        ComboBox<T> c = new ComboBox<>();
        c.setPromptText(prompt);
        c.setStyle("-fx-background-color:#FAFAF8;-fx-border-color:" + BORDER + ";"
                + "-fx-border-radius:7;-fx-background-radius:7;"
                + "-fx-font-size:12px;-fx-pref-height:36px;");
        c.setMaxWidth(Double.MAX_VALUE);
        return c;
    }

    private Label sectionLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + TEXT_MUTED + ";-fx-font-size:10px;-fx-font-weight:bold;");
        return l;
    }

    private Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + TEXT_MUTED + ";-fx-font-size:11px;-fx-font-weight:bold;");
        return l;
    }

    private Label pageTitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill:" + TEXT_MAIN + ";-fx-font-size:17px;-fx-font-weight:bold;");
        return l;
    }

    // ── Dynamic dropdown refreshers ───────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private void refreshBookDropdown(Pane page) {
        ComboBox<Integer> combo = (ComboBox<Integer>) page.lookup("#bookCombo");
        if (combo == null) return;
        combo.getItems().clear();
        for (Room r : hotel.getAvailableRooms()) combo.getItems().add(r.roomNumber);
        combo.setValue(null);
    }

    @SuppressWarnings("unchecked")
    private void refreshCheckoutDropdown(Pane page) {
        ComboBox<Integer> combo = (ComboBox<Integer>) page.lookup("#checkoutCombo");
        if (combo == null) return;
        combo.getItems().clear();
        for (Room r : hotel.getBookedRooms()) combo.getItems().add(r.roomNumber);
        combo.setValue(null);
    }

    // ── SIDEBAR ───────────────────────────────────────────────────────────────

    private VBox buildSidebar(StackPane contentArea, Pane[] pages) {
        // Brand
        Label hotelIcon = new Label("\uD83C\uDFE8");
        hotelIcon.setStyle("-fx-font-size:22px;");
        Label hotelName = new Label("Grand Vista");
        hotelName.setStyle("-fx-text-fill:white;-fx-font-size:14px;-fx-font-weight:bold;");
        Label hotelSub = new Label("HOTEL SYSTEM");
        hotelSub.setStyle("-fx-text-fill:rgba(255,255,255,0.4);-fx-font-size:9px;");
        VBox brand = new VBox(3, hotelIcon, hotelName, hotelSub);
        brand.setPadding(new Insets(22, 20, 20, 20));
        brand.setStyle("-fx-border-color:rgba(255,255,255,0.08);-fx-border-width:0 0 1 0;");

        String[] icons  = { "\uD83D\uDCE5", "\uD83D\uDDBC", "\uD83D\uDECF", "\uD83D\uDEAA" };
        String[] labels = { "Add Room", "View Rooms", "Book Room", "Checkout" };

        HBox[]  navItems  = new HBox[labels.length];
        Label[] navLabels = new Label[labels.length];
        int[]   selected  = { 0 };

        VBox nav = new VBox(2);
        nav.setPadding(new Insets(14, 0, 0, 0));

        for (int i = 0; i < labels.length; i++) {
            final int idx = i;
            HBox item = new HBox(10);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(10, 20, 10, 20));
            item.setCursor(javafx.scene.Cursor.HAND);

            Label ico = new Label(icons[i]);
            ico.setStyle("-fx-font-size:13px;");
            Label lbl = new Label(labels[i]);
            lbl.setStyle("-fx-text-fill:rgba(255,255,255,0.5);-fx-font-size:12.5px;-fx-font-weight:bold;");

            item.getChildren().addAll(ico, lbl);
            navItems[i]  = item;
            navLabels[i] = lbl;

            String activeItemStyle = "-fx-background-color:rgba(201,169,110,0.08);"
                    + "-fx-border-color:" + GOLD + ";-fx-border-width:0 0 0 2;";
            String activeLblStyle  = "-fx-text-fill:" + GOLD + ";-fx-font-size:12.5px;-fx-font-weight:bold;";
            String inactiveLblStyle = "-fx-text-fill:rgba(255,255,255,0.5);-fx-font-size:12.5px;-fx-font-weight:bold;";
            String hoverItemStyle   = "-fx-background-color:rgba(255,255,255,0.04);";
            String hoverLblStyle    = "-fx-text-fill:rgba(255,255,255,0.8);-fx-font-size:12.5px;-fx-font-weight:bold;";

            item.setOnMouseEntered(e -> {
                if (selected[0] != idx) {
                    item.setStyle(hoverItemStyle);
                    lbl.setStyle(hoverLblStyle);
                }
            });
            item.setOnMouseExited(e -> {
                if (selected[0] != idx) {
                    item.setStyle("-fx-background-color:transparent;");
                    lbl.setStyle(inactiveLblStyle);
                }
            });
            item.setOnMouseClicked(e -> {
                // Deactivate old
                navItems[selected[0]].setStyle("-fx-background-color:transparent;");
                navLabels[selected[0]].setStyle(inactiveLblStyle);
                // Activate new
                selected[0] = idx;
                item.setStyle(activeItemStyle);
                lbl.setStyle(activeLblStyle);
                // Refresh dynamic lists and switch page
                if (idx == 2) refreshBookDropdown(pages[2]);
                if (idx == 3) refreshCheckoutDropdown(pages[3]);
                contentArea.getChildren().setAll(pages[idx]);
            });

            nav.getChildren().add(item);
        }

        // Activate first by default
        navItems[0].setStyle("-fx-background-color:rgba(201,169,110,0.08);"
                + "-fx-border-color:" + GOLD + ";-fx-border-width:0 0 0 2;");
        navLabels[0].setStyle("-fx-text-fill:" + GOLD + ";-fx-font-size:12.5px;-fx-font-weight:bold;");

        VBox sidebar = new VBox();
        sidebar.setPrefWidth(200);
        sidebar.setMinWidth(200);
        sidebar.setStyle("-fx-background-color:" + NAVY + ";");
        VBox.setVgrow(nav, Priority.ALWAYS);
        sidebar.getChildren().addAll(brand, nav);
        return sidebar;
    }

    // ── PAGE: ADD ROOM ────────────────────────────────────────────────────────

    private Pane buildAddPage() {
        Integer[] roomNumbers = {101,102,103,201,202,203,301,302,303};
        String[] roomTypes = {
                "Standard Non AC","Standard AC",
                "Deluxe Non AC","Deluxe AC",
                "Suite Non AC","Suite AC"
        };

        ComboBox<Integer> roomNo = styledCombo("Select Room Number");
        roomNo.getItems().addAll(roomNumbers);
        ComboBox<String> type = styledCombo("Select Room Type");
        type.getItems().addAll(roomTypes);

        Label priceChip = new Label("Price: \u20B9 \u2014");
        priceChip.setStyle("-fx-background-color:" + AMBER_BG + ";-fx-text-fill:" + AMBER_FG + ";"
                + "-fx-font-size:12px;-fx-font-weight:bold;-fx-background-radius:20;"
                + "-fx-border-color:#FAC775;-fx-border-radius:20;-fx-border-width:1;"
                + "-fx-padding:4 14 4 14;");

        type.setOnAction(e -> {
            String s = type.getValue();
            if (s != null) priceChip.setText("Price: \u20B9" + hotel.getPrice(s));
        });

        Label msgLbl = new Label();
        Button addBtn = primaryBtn("Add Room");
        addBtn.setOnAction(e -> {
            if (roomNo.getValue() == null || type.getValue() == null) {
                msgLbl.setStyle("-fx-text-fill:" + RED_FG + ";-fx-font-size:12px;");
                msgLbl.setText("Please select all fields before adding.");
                return;
            }
            boolean added = hotel.addRoom(roomNo.getValue(), type.getValue());
            if (added) {
                msgLbl.setStyle("-fx-text-fill:" + GREEN_FG + ";-fx-font-size:12px;");
                msgLbl.setText("Room " + roomNo.getValue() + " added successfully.");
            } else {
                msgLbl.setStyle("-fx-text-fill:" + RED_FG + ";-fx-font-size:12px;");
                msgLbl.setText("Room " + roomNo.getValue() + " already exists.");
            }
        });

        VBox roomRow = new VBox(4, fieldLabel("Room Number"), roomNo);
        VBox typeRow = new VBox(4, fieldLabel("Room Type"), type);
        HBox formRow = new HBox(14, roomRow, typeRow);
        HBox.setHgrow(roomRow, Priority.ALWAYS);
        HBox.setHgrow(typeRow, Priority.ALWAYS);

        VBox formCard = new VBox(16, sectionLabel("ROOM DETAILS"), formRow, priceChip, addBtn, msgLbl);
        formCard.setPadding(new Insets(22));
        formCard.setStyle(cardStyle());
        formCard.setMaxWidth(560);

        VBox content = new VBox(18, pageTitle("Add New Room"), formCard);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color:" + CREAM + ";");

        StackPane page = new StackPane(content);
        StackPane.setAlignment(content, Pos.TOP_LEFT);
        page.setStyle("-fx-background-color:" + CREAM + ";");
        return page;
    }

    // ── PAGE: VIEW ROOMS ──────────────────────────────────────────────────────

    private Pane buildViewPage() {
        ListView<String> listView = new ListView<>();
        listView.setStyle("-fx-background-color:" + WHITE + ";-fx-border-color:" + BORDER + ";"
                + "-fx-border-radius:8;-fx-background-radius:8;-fx-font-size:12px;");

        Button showAll   = outlineBtn("All Rooms");
        Button available = goldBtn("Available");
        Button booked    = primaryBtn("Booked");

        showAll.setOnAction(e -> {
            listView.getItems().clear();
            for (Room r : hotel.getRooms()) listView.getItems().add(r.toString());
        });
        available.setOnAction(e -> {
            listView.getItems().clear();
            for (Room r : hotel.getAvailableRooms()) listView.getItems().add(r.toString());
        });
        booked.setOnAction(e -> {
            listView.getItems().clear();
            for (Room r : hotel.getBookedRooms()) listView.getItems().add(r.toString());
        });

        HBox filterBar = new HBox(10, showAll, available, booked);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(14, sectionLabel("FILTER ROOMS"), filterBar, listView);
        VBox.setVgrow(listView, Priority.ALWAYS);
        card.setPadding(new Insets(22));
        card.setStyle(cardStyle());

        VBox content = new VBox(18, pageTitle("Room Overview"), card);
        VBox.setVgrow(card, Priority.ALWAYS);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color:" + CREAM + ";");

        VBox page = new VBox(content);
        VBox.setVgrow(content, Priority.ALWAYS);
        page.setStyle("-fx-background-color:" + CREAM + ";");
        return page;
    }

    // ── PAGE: BOOK ROOM ───────────────────────────────────────────────────────

    private Pane buildBookPage() {
        ComboBox<Integer> bookCombo = styledCombo("Select an available room");
        bookCombo.setId("bookCombo");

        Label hint = new Label("Only available rooms are listed below.");
        hint.setStyle("-fx-text-fill:" + TEXT_MUTED + ";-fx-font-size:11px;");

        Label msgLbl = new Label();
        Button bookBtn = goldBtn("Confirm Booking");
        bookBtn.setOnAction(e -> {
            if (bookCombo.getValue() == null) {
                msgLbl.setStyle("-fx-text-fill:" + RED_FG + ";-fx-font-size:12px;");
                msgLbl.setText("Please select a room first.");
                return;
            }
            int roomNum = bookCombo.getValue();
            if (hotel.bookRoom(roomNum)) {
                msgLbl.setStyle("-fx-text-fill:" + GREEN_FG + ";-fx-font-size:12px;");
                msgLbl.setText("Room " + roomNum + " booked successfully.");
                bookCombo.getItems().remove(Integer.valueOf(roomNum));
                bookCombo.setValue(null);
            } else {
                msgLbl.setStyle("-fx-text-fill:" + RED_FG + ";-fx-font-size:12px;");
                msgLbl.setText("Room not available or does not exist.");
            }
        });

        VBox roomRow = new VBox(4, fieldLabel("Room Number"), bookCombo);

        VBox formCard = new VBox(16, sectionLabel("BOOKING DETAILS"), hint, roomRow, bookBtn, msgLbl);
        formCard.setPadding(new Insets(22));
        formCard.setStyle(cardStyle());
        formCard.setMaxWidth(520);

        VBox content = new VBox(18, pageTitle("Book a Room"), formCard);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color:" + CREAM + ";");

        StackPane page = new StackPane(content);
        StackPane.setAlignment(content, Pos.TOP_LEFT);
        page.setStyle("-fx-background-color:" + CREAM + ";");
        return page;
    }

    // ── PAGE: CHECKOUT ────────────────────────────────────────────────────────

    private Pane buildCheckoutPage() {
        ComboBox<Integer> checkoutCombo = styledCombo("Select a booked room");
        checkoutCombo.setId("checkoutCombo");

        Label hint = new Label("Only currently booked rooms are listed below.");
        hint.setStyle("-fx-text-fill:" + TEXT_MUTED + ";-fx-font-size:11px;");

        Label billLabel = new Label();
        Label msgLbl    = new Label();
        Button checkoutBtn = primaryBtn("Process Checkout");
        checkoutBtn.setOnAction(e -> {
            if (checkoutCombo.getValue() == null) {
                msgLbl.setStyle("-fx-text-fill:" + RED_FG + ";-fx-font-size:12px;");
                msgLbl.setText("Please select a room first.");
                billLabel.setText("");
                return;
            }
            int roomNum = checkoutCombo.getValue();
            double bill = hotel.checkoutRoom(roomNum);
            if (bill > 0) {
                msgLbl.setStyle("-fx-text-fill:" + GREEN_FG + ";-fx-font-size:12px;");
                msgLbl.setText("Checkout successful for Room " + roomNum + ".");
                billLabel.setStyle("-fx-background-color:" + AMBER_BG + ";-fx-text-fill:" + AMBER_FG + ";"
                        + "-fx-font-size:14px;-fx-font-weight:bold;-fx-background-radius:8;"
                        + "-fx-border-color:#FAC775;-fx-border-radius:8;-fx-border-width:1;"
                        + "-fx-padding:10 18 10 18;");
                billLabel.setText("Total Bill: \u20B9" + bill);
                checkoutCombo.getItems().remove(Integer.valueOf(roomNum));
                checkoutCombo.setValue(null);
            } else {
                msgLbl.setStyle("-fx-text-fill:" + RED_FG + ";-fx-font-size:12px;");
                msgLbl.setText("Room is not booked or does not exist.");
                billLabel.setText("");
            }
        });

        VBox roomRow = new VBox(4, fieldLabel("Room Number"), checkoutCombo);

        VBox formCard = new VBox(16, sectionLabel("CHECKOUT DETAILS"), hint, roomRow, checkoutBtn, msgLbl, billLabel);
        formCard.setPadding(new Insets(22));
        formCard.setStyle(cardStyle());
        formCard.setMaxWidth(520);

        VBox content = new VBox(18, pageTitle("Checkout Room"), formCard);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color:" + CREAM + ";");

        StackPane page = new StackPane(content);
        StackPane.setAlignment(content, Pos.TOP_LEFT);
        page.setStyle("-fx-background-color:" + CREAM + ";");
        return page;
    }

    // ── START ─────────────────────────────────────────────────────────────────

    @Override
    public void start(Stage stage) {
        Pane addPage      = buildAddPage();
        Pane viewPage     = buildViewPage();
        Pane bookPage     = buildBookPage();
        Pane checkoutPage = buildCheckoutPage();

        Pane[] pages = { addPage, viewPage, bookPage, checkoutPage };

        StackPane contentArea = new StackPane(addPage);
        contentArea.setStyle("-fx-background-color:" + CREAM + ";");
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        VBox sidebar = buildSidebar(contentArea, pages);

        HBox root = new HBox(sidebar, contentArea);
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        Scene scene = new Scene(root, 800, 560);
        stage.setTitle("Grand Vista \u2014 Hotel Management");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package necoapi;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import necoapi.domain.*;
import necoapi.helpers.*;
import necoapi.models.Role;
import necoapi.services.Service;
import necoapi.uicomponents.HomePage;
import necoapi.uicomponents.LoginPage;
import necoapi.uicomponents.TopupPage;
import necoapi.uicomponents.UsersPage;
import necoapi.uicomponents.pdf.PDFHelpers;
import necoapi.uicomponents.uihelpers.ActionsHelpers;
import necoapi.uicomponents.uihelpers.TableViewHelpers;
import necoapi.uicomponents.uihelpers.TreeViewHelpers;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.Text;

import java.util.*;

@SpringBootApplication
public class NecoApplication extends Application {

    @Autowired
    Service service;
    public static ConfigurableApplicationContext applicationContext;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        applicationContext = SpringApplication.run(NecoApplication.class);

        HomePage homePage = new HomePage();
        HBox newAccount = homePage.getHBoxNewAccount();
        HBox transactions = homePage.getHBoxTransactions();
        HBox hBoxProducts = homePage.getHBoxProducts();
        HBox hBoxAllItems = homePage.getHBoxAllItems();
        hBoxAllItems.setPadding(new Insets(0));
        hBoxAllItems.setId("items-holder");

        Panel panelAccounts = new Panel("NECO Fuel Accounts");
        panelAccounts.getStyleClass().addAll("panel", "panel-primary");
        panelAccounts.setMaxWidth(400);
        panelAccounts.setBody(newAccount);

        Panel panelProducts = new Panel("NECO Fuel Products");
        panelProducts.getStyleClass().addAll("panel", "panel-primary");
        panelProducts.setBody(hBoxProducts);


        ComboBox comboBoxProduct = homePage.getComboBoxProduct();
        comboBoxProduct.setMinWidth(250);
        RESTCallHelpers.populateProductsComboBox(comboBoxProduct);
        comboBoxProduct.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    Optional<ProductResponse> value = Selected.getInstance().getProductResponses().stream().filter(item -> item.getProductName().equalsIgnoreCase(newValue.toString())).findFirst();
                    if (value.isPresent()) {
                        Selected.getInstance().setSelectedProduct(value.get());
                    }
                }
            }
        });

        TextField accountName = homePage.getTextFieldAccountName();
        TextField openingBalance = homePage.getTextFieldOpeningBalance();
        DatePicker openingDate = homePage.getTextFieldOpeningDate();
        Button submitAccountButton = homePage.getButtonSubmitAccount();
        Label accountId = homePage.getAccountId();
        Button clearAndCancel = homePage.getClearAndCancel();
        accountId.setVisible(false);

        Label transactionId = homePage.getTransactionId();
        Button clearAndCancelTransaction = homePage.getClearAndCancelTransaction();
        transactionId.setVisible(false);

        accountName.setMinWidth(200);
        openingBalance.setMinWidth(200);
        openingDate.setMinWidth(200);

        CheckBox allowOverdraft = homePage.getAllowOverdraft();
        allowOverdraft.setMinWidth(200);

        Panel panelTransactions = new Panel("");
        panelTransactions.getStyleClass().addAll("panel", "panel-primary");
        panelTransactions.setBody(transactions);

        Panel panelUsers = new Panel("Application Users");
        panelUsers.getStyleClass().addAll("panel", "panel-primary");
        panelUsers.setBody(new UsersPage().getHBoxUsers());

        TreeItem rootItem = homePage.getRootItem();

        TreeItem accounts = homePage.getAccounts();
        RESTCallHelpers.populateAccounts(accounts, APIConstants.FETCH_ACCOUNTS);
        rootItem.getChildren().add(accounts);

        TreeItem productItems = homePage.getProducts();
        RESTCallHelpers.populateProducts(productItems);
        rootItem.getChildren().add(productItems);

        TreeItem usersNode = homePage.getUsers();
        rootItem.getChildren().add(usersNode);

        TreeView treeView = homePage.getTreeView();
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
        treeView.setMinSize(300, homePage.getHeight() - 150);

        Label transactionAccountTitle = homePage.getLabelTitle();
        transactionAccountTitle.setId("transaction-title");

        submitAccountButton.setId("button-save-edit");
        submitAccountButton.getStyleClass().addAll("btn", "btn-primary");

        homePage.getHBox().setSpacing(20);
        homePage.getHBox().setPadding(new Insets(20, 20, 20, 20));
        homePage.getHBoxAllItems().setSpacing(20);
        homePage.getHBoxAllItems().setPadding(new Insets(0, 20, 0, 20));
        homePage.getHBoxNewAccount().setSpacing(10);

        Button printCustomers = homePage.getPrintCustomers();
        printCustomers.setId("button");
        printCustomers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PDFHelpers.printCustomers();
            }
        });

        GridPane gridPaneAccount = homePage.getGridPaneAccount();
        gridPaneAccount.setHgap(20);
        gridPaneAccount.setVgap(20);

        Label labelAccountName = homePage.getLabelAccountName();
        labelAccountName.setMinWidth(130);

        Label labelOpeningBalance = homePage.getLabelOpeningBalance();
        labelOpeningBalance.setMinWidth(130);

        Label labelOpeningDate = homePage.getLabelOpeningDate();
        labelOpeningDate.setMinWidth(130);

        GridPane gridPaneTransaction = homePage.getGridPaneTransactions();
        gridPaneTransaction.setHgap(20);
        gridPaneTransaction.setVgap(20);

        DatePicker datePickerTransactionDate = homePage.getTransactionDate();
        datePickerTransactionDate.setMinWidth(200);

        Button submitTransactionButton = homePage.getButtonSubmitTransaction();
        submitTransactionButton.setId("button-save-edit");
        submitTransactionButton.getStyleClass().addAll("btn", "btn-primary");
        submitTransactionButton.setMinWidth(200);

        TextField vehicleNumber = homePage.getVehicleNumber();
        TextField orderNumber = homePage.getFieldOrderNumber();
        TextField transactionQuantity = homePage.getFieldQuantity();

        TableView tableViewTransactions = TableViewHelpers.getTableViewTransactions(
                homePage, submitTransactionButton, clearAndCancelTransaction,
                transactionId, vehicleNumber, orderNumber, datePickerTransactionDate,
                comboBoxProduct, transactionQuantity, panelTransactions);
        TableView tableViewAccounts = TableViewHelpers.getTableViewAccounts(
                homePage,
                accountName,
                openingBalance,
                openingDate,
                accountId,
                submitAccountButton,
                clearAndCancel,
                allowOverdraft,
                hBoxAllItems,
                comboBoxProduct,
                panelTransactions,
                homePage.getVehicleNumber(),
                homePage.getFieldOrderNumber(),
                homePage.getFieldQuantity(),
                homePage.getTransactionDate(),
                tableViewTransactions);

        submitAccountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ActionsHelpers.accountSubmission(accountName, openingBalance,
                        openingDate, allowOverdraft, submitAccountButton,
                        accountId, clearAndCancel, tableViewAccounts, accounts);
            }
        });

        gridPaneAccount.add(accountId, 0,0,1,1);
        gridPaneAccount.add(labelAccountName, 2,0, 1,1);
        gridPaneAccount.add(accountName, 3,0, 1,1);
        gridPaneAccount.add(labelOpeningBalance, 4,0, 1,1);
        gridPaneAccount.add(openingBalance, 5,0, 1,1);
        gridPaneAccount.add(allowOverdraft, 6,0, 1,1);
        gridPaneAccount.add(labelOpeningDate, 2,1, 1,1);
        gridPaneAccount.add(openingDate, 3,1, 1,1);
        gridPaneAccount.add(clearAndCancel, 4,1,1,1);
        gridPaneAccount.add(submitAccountButton, 5,1, 1,1);
        gridPaneAccount.add(printCustomers, 6,1, 1,1);

        TextField searchAccountField = new TextField();
        searchAccountField.setPromptText("Search Accounts...");
        searchAccountField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                RESTCallHelpers.updateTableAccountsAfterSearch(tableViewAccounts, searchAccountField.getText());
            }
        });
        gridPaneAccount.add(searchAccountField, 2, 4,2,1);

        ComboBox otherFields = new ComboBox();
        otherFields.getItems().clear();
        otherFields.getItems().add(0, "Select Fields To Show");
        otherFields.getItems().add(1,"Created By");
        otherFields.getItems().add(2,"Created Date");
        otherFields.getItems().add(3,"Last Modified By");
        otherFields.getItems().add(4,"Last modified Date");
        otherFields.getSelectionModel().select(0);
        otherFields.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TableViewHelpers.showMoreFields(newValue, tableViewAccounts);
            }
        });

        gridPaneAccount.add(otherFields, 5, 4, 2,1);

        gridPaneAccount.add(tableViewAccounts, 0, 7, 7,2);

        transactionQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    transactionQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        submitTransactionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ActionsHelpers.transactionSubmission(datePickerTransactionDate, transactionQuantity,
                        orderNumber, vehicleNumber, submitTransactionButton,
                        comboBoxProduct, tableViewTransactions, transactionId,
                        clearAndCancelTransaction, panelTransactions);
            }
        });

        Button printTransactions = homePage.getPrintTransactions();

        gridPaneTransaction.add(transactionAccountTitle, 0,0,8,1);
        gridPaneTransaction.add(transactionId, 0,1,1,1);
        gridPaneTransaction.add(homePage.getLabelTxnDate(), 2, 1, 1, 1);
        gridPaneTransaction.add(datePickerTransactionDate, 3, 1, 1, 1);
        gridPaneTransaction.add(homePage.getLabelProduct(), 4, 1, 1, 1);
        gridPaneTransaction.add(comboBoxProduct, 5, 1, 1, 1);
        gridPaneTransaction.add(homePage.getLabelVehicleNumber(), 2, 2, 1, 1);
        gridPaneTransaction.add(vehicleNumber, 3, 2, 1, 1);
        gridPaneTransaction.add(homePage.getLabelOrderNumber(), 4, 2, 1, 1);
        gridPaneTransaction.add(orderNumber, 5, 2, 1, 1);
        gridPaneTransaction.add(homePage.getLabelQuantity(), 2, 3, 1, 1);
        gridPaneTransaction.add(transactionQuantity, 3, 3, 1, 1);
        gridPaneTransaction.add(clearAndCancelTransaction, 4,3,1,1);
        gridPaneTransaction.add(submitTransactionButton, 5, 3, 1, 1);
        gridPaneTransaction.add(printTransactions, 6, 3, 1, 1);

        Button show = new Button("Top Up");
        show.getStyleClass().addAll("btn", "btn-secondary");

        show.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                TopupPage topupPage = new TopupPage(panelTransactions);
                RESTCallHelpers.updateTableTopUps(topupPage.getTableViewTopups());
                topupPage.getPopupWindow().showAndWait();
            }
        });
        gridPaneTransaction.add(show, 2, 6,1,1);

        gridPaneTransaction.add(homePage.getTableViewTransactions(), 0, 7,8,1);

        GridPane gridPaneProducts = homePage.getGridPaneProducts();
        gridPaneProducts.setHgap(20);
        gridPaneProducts.setVgap(20);

        TextField productName = new TextField();
        productName.setMinWidth(280);

        TextField productDescription = new TextField();
        productDescription.setMinWidth(280);

        TextField productUnitPrice = new TextField();
        productUnitPrice.setMinWidth(280);

        Button submitProduct = new Button("Save Product");
        Button clearProductFields = new Button("Clear");
        submitProduct.setMinWidth(280);
        submitProduct.setId("button-save-edit");
        submitProduct.getStyleClass().addAll("btn", "btn-primary");

        Label productNameLabel = new Label("Product Name");
        productNameLabel.setMinWidth(120);

        Label productDescriptionLabel = new Label("Product Description");
        productDescriptionLabel.setMinWidth(120);

        gridPaneProducts.add(productNameLabel, 2,0,1,1);
        gridPaneProducts.add(productName, 3,0,1,1);

        gridPaneProducts.add(productDescriptionLabel, 4,0,1,1);
        gridPaneProducts.add(productDescription, 5,0,1,1);

        gridPaneProducts.add(new Label("Product Unit Price"), 2,1,1,1);
        gridPaneProducts.add(productUnitPrice, 3,1,1,1);

        gridPaneProducts.add(clearProductFields, 4,1,1,1);
        gridPaneProducts.add(submitProduct, 5,1,1,1);

        clearAndCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (clearAndCancel.getText().equalsIgnoreCase("Cancel")) {
                  submitAccountButton.setText("Save Account");
                    clearAndCancel.setText("Clear");
                }
                ControlsHelpers.clearAccountFields(accountName, openingBalance, openingDate, allowOverdraft);
            }
        });
        clearAndCancel.setId("button-cancel-clear");
        clearAndCancel.getStyleClass().addAll("btn", "btn-default");

        clearAndCancelTransaction.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (clearAndCancelTransaction.getText().equalsIgnoreCase("Cancel")) {
                    submitTransactionButton.setText("Save Transaction");
                    clearAndCancelTransaction.setText("Clear");
                }
                ControlsHelpers.clearTransactions(vehicleNumber, orderNumber, comboBoxProduct, datePickerTransactionDate, transactionQuantity);
            }
        });
        clearAndCancelTransaction
                .setId("button-cancel-clear");

        clearProductFields.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (clearProductFields.getText().equalsIgnoreCase("Cancel")) {
                    clearProductFields.setText("Clear");
                    submitProduct.setText("Save Product");
                }
                ControlsHelpers.clearProductsFields(productName, productDescription, productUnitPrice);
            }
        });
        clearProductFields.setId("button-cancel-clear");

        submitProduct.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ActionsHelpers.productSubmission(productName, productDescription,
                        productUnitPrice, submitProduct, productItems);
            }
        });

        hBoxAllItems.getChildren().add(panelAccounts);

        TableView tableViewUsers = TableViewHelpers.getTableViewUsers();
        List<UserResponse> userResponses = RESTCallHelpers.getUsers();
        if (userResponses == null || userResponses.isEmpty()) {
            RESTCallHelpers.registerAdminUser(tableViewUsers);
        }
        RESTCallHelpers.updateTableUsers(tableViewUsers);

        new UsersPage().getButtonSaveUser().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableViewUsers.getItems().clear();
            }
        });

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem>() {
            @Override
            public void changed(ObservableValue observable, TreeItem oldValue, TreeItem newValue) {
                TreeViewHelpers.treeViewSelectionHelper(newValue, hBoxAllItems, treeView, panelAccounts, accounts, tableViewAccounts,
                        panelTransactions, tableViewTransactions, comboBoxProduct, vehicleNumber, orderNumber,
                        datePickerTransactionDate, transactionQuantity, submitTransactionButton, clearAndCancelTransaction,
                        panelProducts, clearProductFields, submitProduct, productName, productDescription, productUnitPrice,
                        submitAccountButton, clearAndCancel, panelUsers, tableViewUsers);
            }
        });

        Scene scene = homePage.getScene();
        HBox hBox = homePage.getHBox();
        String necoLogo = getClass().getResource("/neco-logo.jpg").toString();
        BackgroundImage myBI = new BackgroundImage(new Image(necoLogo,100,100,true,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        gridPaneAccount.setBackground(new Background(myBI));
        gridPaneTransaction.setBackground(new Background(myBI));
        gridPaneProducts.setBackground(new Background(myBI));

        hBox.setId("all-items");
        printTransactions.setId("button-print");
        openingDate.setId("opening-balance");
        String  style = getClass().getResource("/css/home.css").toExternalForm();
        scene.getStylesheets().add(style);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        printTransactions.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PDFHelpers.printTransaction();
            }
        });
        primaryStage.setTitle("NECO Station Management Application");

        LoginPage loginPage = new LoginPage();
        Scene loginPageScene = loginPage.getScene();
        loginPage.getButtonSignIn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginPage.loginUser(primaryStage, scene);
            }
        });

        primaryStage.setScene(loginPageScene);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                applicationContext.close();
            }
        });
        primaryStage.show();
    }
}

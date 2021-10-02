package necoapi.uicomponents.uihelpers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import necoapi.domain.AccountResponse;
import necoapi.domain.TransactionResponse;
import necoapi.helpers.ControlsHelpers;
import necoapi.helpers.RESTCallHelpers;
import necoapi.helpers.Selected;
import necoapi.uicomponents.HomePage;
import necoapi.uicomponents.TopupPage;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class TableViewHelpers {
    static List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
    static {
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd"));
        knownPatterns.add(new SimpleDateFormat("yyyy-dd-MM"));
        knownPatterns.add(new SimpleDateFormat("dd-MM-yyy"));
        knownPatterns.add(new SimpleDateFormat("MM-dd-yyy"));
        knownPatterns.add(new SimpleDateFormat("dd/MM/yyyy"));
        knownPatterns.add(new SimpleDateFormat("MM/dd/yyyy"));
    }
    public static void addButtonToAccountTable(
            TableView tableView,
            TextField accountName,
            TextField openingBalance,
            DatePicker openingDate,
            Label accountId,
            Button saveButton,
            Button clearAndCancel,
            CheckBox allowedOverdraft,
            HBox hBoxAllItems,
            ComboBox comboBoxProducts,
            Panel panelTransactions,
            TextField vehicleNumber,
            TextField orderNumber,
            TextField txnQuantity,
            DatePicker txnDate,
            TreeView treeView,
            TableView tableViewTxns) {
        TableColumn<AccountResponse, Void> colBtn = new TableColumn("Action");
        colBtn.setMinWidth(350);
        Callback<TableColumn<AccountResponse, Void>, TableCell<AccountResponse, Void>> cellFactory = new Callback<TableColumn<AccountResponse, Void>, TableCell<AccountResponse, Void>>() {
            @Override
            public TableCell<AccountResponse, Void> call(final TableColumn<AccountResponse, Void> param) {
                final TableCell<AccountResponse, Void> cell = new TableCell<AccountResponse, Void>() {

                    private final Button btnViewTxn = new Button("View Txns");
                    private final Button btnEdit = new Button("Edit");
                    private final Button btnDelete = new Button("Delete");
                    HBox hBox = new HBox(btnViewTxn, btnEdit, btnDelete);

                    {

                        btnViewTxn.setId("button-edit");
                        btnViewTxn.getStyleClass().setAll("btn","btn-default");
                        btnViewTxn.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Map<String, Object> accountItem = (Map<String, Object>) getTableView().getItems().get(getIndex());
                                handleSelectedAccountTransactions(
                                        accountItem.get("accountName").toString(),
                                        hBoxAllItems,
                                        treeView,
                                        panelTransactions,
                                        tableViewTxns,
                                        comboBoxProducts,
                                        vehicleNumber,
                                        orderNumber,
                                        txnDate,
                                        txnQuantity,
                                        saveButton,
                                        clearAndCancel);
                            }
                        });

                        hBox.setSpacing(10);
                        hBox.setAlignment(Pos.BASELINE_CENTER);
                        btnEdit.setId("button-edit");
                        btnEdit.getStyleClass().setAll("btn","btn-primary");
                        btnEdit.setOnAction((ActionEvent event) -> {
                            Map<String, Object> accountItem = (Map<String, Object>) getTableView().getItems().get(getIndex());
                            accountName.setText(accountItem.get("accountName").toString());
                            Double doubleValue = Double
                                    .parseDouble(accountItem.get("openingBalance").toString()
                                            .replaceAll("[^0-9]", ""));
                            openingBalance.setText(doubleValue.toString());
                            Optional<AccountResponse> accountResponse = Selected.getInstance()
                                    .getAccountResponses().stream().filter(account ->
                                            account.getAccountName().equalsIgnoreCase(accountItem
                                                    .get("accountName").toString())).findFirst();
                            if (accountResponse.isPresent()) {
                                allowedOverdraft.setSelected(accountResponse.get().getBankOverdraftAllowed());
                            }

                            LocalDate theFinalDate = null;

                            for (SimpleDateFormat pattern : knownPatterns) {
                                try {
                                    String value = accountItem.get("openingDate").toString();
                                    Date myDate = pattern.parse(value);
                                    theFinalDate = myDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                    openingDate.setValue(theFinalDate);
                                    break;
                                } catch (ParseException pe) {
                                    // Loop on
                                }
                            }
                            accountId.setText(accountItem.get("accountId").toString());
                            saveButton.setText("Edit Account");
                            clearAndCancel.setText("Cancel");
                        });
                        btnDelete.getStyleClass().setAll("btn","btn-danger");
                        btnDelete.setId("button-delete");
                        btnDelete.setOnAction((ActionEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Deleting Account Will Delete All Its Transactions",
                                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                            alert.setTitle("Confirm Delete");
                            alert.setHeaderText("Are you sure to delete");
                            alert.showAndWait();
                            if (alert.getResult() == ButtonType.YES) {
                                Map<String, Object> accountItem = (Map<String, Object>) getTableView().getItems().get(getIndex());
                                Long accountId = Long.parseLong(accountItem.get("accountId").toString());
                                String url = "http://localhost:8080/neco/v1/account/"+accountId;
                                ResponseEntity<Object> responseEntity = RESTCallHelpers.deleteRequest(url);
                                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                                    Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                                    alertSuccess.setTitle("Success");
                                    alertSuccess.setHeaderText("Account Successfully Deleted");
                                    alertSuccess.setContentText("All Related Transactions Deleted");
                                    alertSuccess.showAndWait();
                                    RESTCallHelpers.updateTableAccounts(tableView);
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        tableView.getColumns().add(colBtn);
    }

    public static void addButtonToTransactionTable(
            TableView tableViewTransactions,
            Button saveTransaction,
            Button clearAndCancel,
            Label transactionId,
            TextField vehicleNumber,
            TextField orderNumber,
            DatePicker transactionDate,
            ComboBox productName,
            TextField transactionQuantity,
            Panel panel) {
        TableColumn<TransactionResponse, Void> colBtn = new TableColumn("Action");
        colBtn.setMinWidth(190);
        Callback<TableColumn<TransactionResponse, Void>, TableCell<TransactionResponse, Void>> cellFactory = new Callback<TableColumn<TransactionResponse, Void>, TableCell<TransactionResponse, Void>>() {
            @Override
            public TableCell<TransactionResponse, Void> call(final TableColumn<TransactionResponse, Void> param) {
                final TableCell<TransactionResponse, Void> cell = new TableCell<TransactionResponse, Void>() {

                    private final Button btnEdit = new Button("Edit");
                    private final Button btnDelete = new Button("Delete");
                    HBox hBox = new HBox(btnEdit, btnDelete);

                    {
                        hBox.setSpacing(10);
                        hBox.setAlignment(Pos.BASELINE_CENTER);
                        btnEdit.setId("button-edit");
                        btnEdit.getStyleClass().setAll("btn","btn-primary");
                        btnEdit.setOnAction((ActionEvent event) -> {
                            saveTransaction.setText("Edit Transaction");
                            clearAndCancel.setText("Cancel");
                            Map<String, Object> transaction = (Map<String, Object>) getTableView().getItems().get(getIndex());
                            Long txnId = Long.parseLong(transaction.get("transactionId").toString());
                            transactionId.setText(txnId.toString());
                            vehicleNumber.setText(transaction.get("transactionVehicleNumber").toString());
                            orderNumber.setText(transaction.get("transactionOrderNumber").toString());
                            transactionQuantity.setText(transaction.get("transactionQuantity").toString());
                            productName.getSelectionModel().select(transaction.get("transactionProductName").toString());
                            LocalDate theFinalDate = null;
                            for (SimpleDateFormat pattern : knownPatterns) {
                                try {
                                    String value = transaction.get("transactionDate").toString();
                                    Date myDate = pattern.parse(value);
                                    theFinalDate = myDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                    transactionDate.setValue(theFinalDate);
                                    break;
                                } catch (ParseException pe) {
                                    // Loop on
                                }
                            }
                        });

                        btnDelete.getStyleClass().setAll("btn","btn-danger");
                        btnDelete.setId("button-delete");
                        btnDelete.setOnAction((ActionEvent event) -> {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Delete cannot be undone",
                                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                            alert.setTitle("Confirm Delete");
                            alert.setHeaderText("Are you sure to delete this transaction");
                            alert.showAndWait();
                            if (alert.getResult() == ButtonType.YES) {
                                Map<String, Object> transaction = (Map<String, Object>) getTableView().getItems().get(getIndex());
                                Long txnId = Long.parseLong(transaction.get("transactionId").toString());
                                String url = "http://localhost:8080/neco/v1/transaction/"+txnId;
                                ResponseEntity<Object> responseEntity = RESTCallHelpers.deleteRequest(url);
                                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                                    Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                                    alertSuccess.setTitle("Success");
                                    alertSuccess.setHeaderText("Transaction Successfully Deleted");
                                    alertSuccess.showAndWait();
                                    RESTCallHelpers.updateTableTransactions(tableViewTransactions, Selected.getInstance().getSelectedAccount().getAccountName());
                                    RESTCallHelpers.updateBalancesInHeading(panel);
                                }
                            }
                        });

                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hBox);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        tableViewTransactions.getColumns().add(colBtn);
    }

    public static TableView getTableViewTransactions(
            HomePage homePage,
            Button saveTransactionButton,
            Button clearAndCancelButton,
            Label transactionId,
            TextField vehicleNumber,
            TextField orderNumber,
            DatePicker transactionDate,
            ComboBox productName,
            TextField transactionQuantity,
            Panel panelTransaction) {
        TableView tableViewTransactions = homePage.getTableViewTransactions();

        TableColumn<Map, String> transactionIdColumn = new TableColumn<>("#Id");
        transactionIdColumn.setCellValueFactory(new MapValueFactory<>("transactionId"));
        transactionIdColumn.setMinWidth(50);

        TableColumn<Map, String> transactionDateColumn = new TableColumn<>("Transaction Date");
        transactionDateColumn.setCellValueFactory(new MapValueFactory<>("transactionDate"));
        transactionDateColumn.setMinWidth(100);

        TableColumn<Map, String> transactionAccountNameColumn = new TableColumn<>("Account Name");
        transactionAccountNameColumn.setCellValueFactory(new MapValueFactory<>("transactionAccountName"));
        transactionAccountNameColumn.setMinWidth(140);

        TableColumn<Map, String> transactionProductNameColumn = new TableColumn<>("Product");
        transactionProductNameColumn.setCellValueFactory(new MapValueFactory<>("transactionProductName"));
        transactionProductNameColumn.setMinWidth(100);

        TableColumn<Map, String> transactionVehicleNumberColumn = new TableColumn<>("Vehicle Number");
        transactionVehicleNumberColumn.setCellValueFactory(new MapValueFactory<>("transactionVehicleNumber"));
        transactionVehicleNumberColumn.setMinWidth(100);

        TableColumn<Map, String> transactionQuantityColumn = new TableColumn<>("Quantity");
        transactionQuantityColumn.setCellValueFactory(new MapValueFactory<>("transactionQuantity"));
        transactionQuantityColumn.setMinWidth(70);

        TableColumn<Map, String> transactionAmountColumn = new TableColumn<>("Amount");
        transactionAmountColumn.setCellValueFactory(new MapValueFactory<>("transactionAmount"));
        transactionAmountColumn.setMinWidth(80);

        TableColumn<Map, String> transactionCurrentBalanceColumn = new TableColumn<>("Current Balance");
        transactionCurrentBalanceColumn.setCellValueFactory(new MapValueFactory<>("transactionCurrentBalance"));
        transactionCurrentBalanceColumn.setMinWidth(100);

        TableColumn<Map, String> transactionOrderNumberColumn = new TableColumn<>("Order Number");
        transactionOrderNumberColumn.setCellValueFactory(new MapValueFactory<>("transactionOrderNumber"));
        transactionOrderNumberColumn.setMinWidth(100);

        tableViewTransactions.getColumns().add(transactionIdColumn);
        tableViewTransactions.getColumns().add(transactionDateColumn);
        tableViewTransactions.getColumns().add(transactionAccountNameColumn);
        tableViewTransactions.getColumns().add(transactionProductNameColumn);
        tableViewTransactions.getColumns().add(transactionVehicleNumberColumn);
        tableViewTransactions.getColumns().add(transactionQuantityColumn);
        tableViewTransactions.getColumns().add(transactionAmountColumn);
        tableViewTransactions.getColumns().add(transactionCurrentBalanceColumn);
        tableViewTransactions.getColumns().add(transactionOrderNumberColumn);

        TableViewHelpers.addButtonToTransactionTable(
                tableViewTransactions,
                saveTransactionButton,
                clearAndCancelButton,
                transactionId,
                vehicleNumber,
                orderNumber,
                transactionDate,
                productName,
                transactionQuantity,
                panelTransaction);
        return tableViewTransactions;
    }

    public static TableView getTableViewAccounts(
            HomePage homePage,
            TextField accountName,
            TextField openingBalance,
            DatePicker openingDate,
            Label accountId,
            Button saveButton,
            Button clearAndCancel,
            CheckBox checkBoxBankOverdraft,
            HBox hBoxAllItems,
            ComboBox comboBoxProducts,
            Panel panelTransactions,
            TextField vehicleNumber,
            TextField orderNumber,
            TextField txnQuantity,
            DatePicker txnDate,
            TableView tableViewTxns) {

        TableView tableViewAccounts = homePage.getTableViewAccounts();

        TableColumn<Map, String> idColumn = new TableColumn<>("#Id");
        idColumn.setCellValueFactory(new MapValueFactory<>("accountId"));
        idColumn.setMinWidth(50);

        TableColumn<Map, String> accountNameColumn = new TableColumn<>("Account Name");
        accountNameColumn.setCellValueFactory(new MapValueFactory<>("accountName"));
        accountNameColumn.setMinWidth(200);

        TableColumn<Map, String> openingDateColumn = new TableColumn<>("Opening Date");
        openingDateColumn.setCellValueFactory(new MapValueFactory<>("openingDate"));
        openingDateColumn.setMinWidth(100);

        TableColumn<Map, String> openingBalanceColumn = new TableColumn<>("Opening Balance");
        openingBalanceColumn.setCellValueFactory(new MapValueFactory<>("openingBalance"));
        openingBalanceColumn.setMinWidth(150);

        TableColumn<Map, String> createdBy = new TableColumn<>("Created By");
        createdBy.setCellValueFactory(new MapValueFactory<>("createdBy"));
        createdBy.setMinWidth(100);

        TableColumn<Map, String> createdDate = new TableColumn<>("Created Date");
        createdDate.setCellValueFactory(new MapValueFactory<>("createdDate"));
        createdDate.setMinWidth(100);

        TableColumn<Map, String> lastModifiedBy = new TableColumn<>("Last Mod By");
        lastModifiedBy.setCellValueFactory(new MapValueFactory<>("lastModifiedBy"));
        lastModifiedBy.setMinWidth(100);

        TableColumn<Map, String> lastModifiedDate = new TableColumn<>("Last Mod Date");
        lastModifiedDate.setCellValueFactory(new MapValueFactory<>("lastModifiedDate"));
        lastModifiedDate.setMinWidth(100);

        tableViewAccounts.getColumns().add(idColumn);
        tableViewAccounts.getColumns().add(accountNameColumn);
        tableViewAccounts.getColumns().add(openingDateColumn);
        tableViewAccounts.getColumns().add(openingBalanceColumn);
        tableViewAccounts.getColumns().add(createdBy);
        tableViewAccounts.getColumns().add(createdDate);
        tableViewAccounts.getColumns().add(lastModifiedBy);
        tableViewAccounts.getColumns().add(lastModifiedDate);

        createdBy.setVisible(false);
        createdDate.setVisible(false);
        lastModifiedBy.setVisible(false);
        lastModifiedDate.setVisible(false);

        TableViewHelpers.addButtonToAccountTable(
                tableViewAccounts,
                accountName,
                openingBalance,
                openingDate,
                accountId,
                saveButton,
                clearAndCancel,
                checkBoxBankOverdraft,
                hBoxAllItems,
                comboBoxProducts,
                panelTransactions,
                vehicleNumber,
                orderNumber,
                txnQuantity,
                txnDate,
                homePage.getTreeView(),
                tableViewTxns);

        RESTCallHelpers.updateTableAccounts(tableViewAccounts);
        return tableViewAccounts;
    }

    public static void handleSelectedAccountTransactions(
            String accountName,
            HBox hBoxAllItems,
            TreeView treeView,
            Panel panelTransactions,
            TableView tableViewTransactions,
            ComboBox comboBoxProduct,
            TextField vehicleNumber,
            TextField orderNumber,
            DatePicker datePickerTransactionDate,
            TextField transactionQuantity,
            Button submitTransactionButton,
            Button clearAndCancelTransaction) {
        Selected.getInstance().setAccountResponses(RESTCallHelpers.getAccounts());
        hBoxAllItems.getChildren().clear();
        hBoxAllItems.getChildren().addAll(treeView, panelTransactions);
        Optional<AccountResponse> accountResponse = Selected.getInstance().getAccountResponses()
                .stream().filter(account -> account.getAccountName()
                        .equalsIgnoreCase(accountName)).findFirst();
        if (accountResponse.isPresent()) {
            Selected.getInstance().setSelectedAccount(accountResponse.get());
        }
        RESTCallHelpers.updateTableTransactions(tableViewTransactions, accountName);
        RESTCallHelpers.populateProductsComboBox(comboBoxProduct);
        ControlsHelpers.clearTransactions(vehicleNumber, orderNumber, comboBoxProduct, datePickerTransactionDate, transactionQuantity);
        submitTransactionButton.setText("Save Transaction");
        clearAndCancelTransaction.setText("Clear");
        Selected.getInstance().setAccountResponses(RESTCallHelpers.getAccounts());
        RESTCallHelpers.updateBalancesInHeading(panelTransactions);
        AccountResponse theSelected = Selected.getInstance().getSelectedAccount();
        if (theSelected != null) {
            String selectedCompany = theSelected.getAccountName();
            tableViewTransactions.setPlaceholder(new Label("No transaction for " + selectedCompany));
        }
    }

    public static <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns())
            if (col.getText().equals(name)) return col ;
        return null ;
    }

    public static void showMoreFields(Object newValue, TableView tableViewAccounts) {
        TableColumn createdByColumn = getTableColumnByName(tableViewAccounts, "Created By");
        TableColumn createdDateColumn = getTableColumnByName(tableViewAccounts, "Created Date");
        TableColumn lastModifiedByColumn = getTableColumnByName(tableViewAccounts, "Last Mod By");
        TableColumn lastModifiedDateColumn = getTableColumnByName(tableViewAccounts, "Last Mod Date");

        if (newValue.toString().equalsIgnoreCase("Created By")) {
            createdByColumn.setVisible(true);
            createdDateColumn.setVisible(false);
            lastModifiedByColumn.setVisible(false);
            lastModifiedDateColumn.setVisible(false);
        }
        if (newValue.toString().equalsIgnoreCase("Created Date")) {
            createdByColumn.setVisible(false);
            createdDateColumn.setVisible(true);
            lastModifiedByColumn.setVisible(false);
            lastModifiedDateColumn.setVisible(false);
        }
        if (newValue.toString().equalsIgnoreCase("Last Modified By")) {
            createdByColumn.setVisible(false);
            createdDateColumn.setVisible(false);
            lastModifiedByColumn.setVisible(true);
            lastModifiedDateColumn.setVisible(false);
        }
        if (newValue.toString().equalsIgnoreCase("Last Modified Date")) {
            createdByColumn.setVisible(false);
            createdDateColumn.setVisible(false);
            lastModifiedByColumn.setVisible(false);
            lastModifiedDateColumn.setVisible(true);
        }

        if (newValue.toString().equalsIgnoreCase("Select Fields To Show")) {
            createdByColumn.setVisible(false);
            createdDateColumn.setVisible(false);
            lastModifiedByColumn.setVisible(false);
            lastModifiedDateColumn.setVisible(false);
        }
    }

    public static TableView getTableViewTopups() {

        TableView tableViewTopups = new TableView();

        TableColumn<Map, String> idColumn = new TableColumn<>("#TopUpId");
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        idColumn.setMinWidth(50);

        TableColumn<Map, String> accountNameColumn = new TableColumn<>("Prev Bal");
        accountNameColumn.setCellValueFactory(new MapValueFactory<>("previousBalance"));
        accountNameColumn.setMinWidth(100);

        TableColumn<Map, String> openingDateColumn = new TableColumn<>("TopUp Amt");
        openingDateColumn.setCellValueFactory(new MapValueFactory<>("amount"));
        openingDateColumn.setMinWidth(100);

        TableColumn<Map, String> openingBalanceColumn = new TableColumn<>("Current Bal");
        openingBalanceColumn.setCellValueFactory(new MapValueFactory<>("currentBalance"));
        openingBalanceColumn.setMinWidth(100);

        TableColumn<Map, String> createdBy = new TableColumn<>("Created By");
        createdBy.setCellValueFactory(new MapValueFactory<>("createdBy"));
        createdBy.setMinWidth(100);

        TableColumn<Map, String> createdDate = new TableColumn<>("Created Date");
        createdDate.setCellValueFactory(new MapValueFactory<>("createdDate"));
        createdDate.setMinWidth(100);

        TableColumn<Map, String> lastModifiedBy = new TableColumn<>("Last Mod By");
        lastModifiedBy.setCellValueFactory(new MapValueFactory<>("lastModifiedBy"));
        lastModifiedBy.setMinWidth(100);

        TableColumn<Map, String> lastModifiedDate = new TableColumn<>("Last Mod Date");
        lastModifiedDate.setCellValueFactory(new MapValueFactory<>("lastModifiedDate"));
        lastModifiedDate.setMinWidth(100);

        tableViewTopups.getColumns().add(idColumn);
        tableViewTopups.getColumns().add(accountNameColumn);
        tableViewTopups.getColumns().add(openingDateColumn);
        tableViewTopups.getColumns().add(openingBalanceColumn);
        tableViewTopups.getColumns().add(createdBy);
        tableViewTopups.getColumns().add(createdDate);
        tableViewTopups.getColumns().add(lastModifiedBy);
        tableViewTopups.getColumns().add(lastModifiedDate);

        return tableViewTopups;
    }

    public static TableView getTableViewUsers() {

        TableView tableViewUsers = new TableView();

        TableColumn<Map, String> idColumn = new TableColumn<>("#UserId");
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        idColumn.setMinWidth(50);

        TableColumn<Map, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new MapValueFactory<>("username"));
        usernameColumn.setMinWidth(150);

        TableColumn<Map, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new MapValueFactory<>("email"));
        emailColumn.setMinWidth(100);

        TableColumn<Map, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new MapValueFactory<>("address"));
        addressColumn.setMinWidth(100);

        TableColumn<Map, String> rolesColumn = new TableColumn<>("Roles");
        rolesColumn.setCellValueFactory(new MapValueFactory<>("roles"));
        rolesColumn.setMinWidth(100);

        TableColumn<Map, String> createdBy = new TableColumn<>("Created By");
        createdBy.setCellValueFactory(new MapValueFactory<>("createdBy"));
        createdBy.setMinWidth(100);

        TableColumn<Map, String> createdDate = new TableColumn<>("Created Date");
        createdDate.setCellValueFactory(new MapValueFactory<>("createdDate"));
        createdDate.setMinWidth(100);

        TableColumn<Map, String> lastModifiedBy = new TableColumn<>("Last Mod By");
        lastModifiedBy.setCellValueFactory(new MapValueFactory<>("lastModifiedBy"));
        lastModifiedBy.setMinWidth(100);

        TableColumn<Map, String> lastModifiedDate = new TableColumn<>("Last Mod Date");
        lastModifiedDate.setCellValueFactory(new MapValueFactory<>("lastModifiedDate"));
        lastModifiedDate.setMinWidth(100);

        tableViewUsers.getColumns().add(idColumn);
        tableViewUsers.getColumns().add(usernameColumn);
        tableViewUsers.getColumns().add(emailColumn);
        tableViewUsers.getColumns().add(addressColumn);
        tableViewUsers.getColumns().add(rolesColumn);
        tableViewUsers.getColumns().add(createdBy);
        tableViewUsers.getColumns().add(createdDate);
        tableViewUsers.getColumns().add(lastModifiedBy);
        tableViewUsers.getColumns().add(lastModifiedDate);

        RESTCallHelpers.updateTableUsers(tableViewUsers);

        return tableViewUsers;
    }

}
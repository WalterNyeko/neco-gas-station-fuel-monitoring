package necoapi.uicomponents;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomePage {
    int width = (int) Screen.getPrimary().getBounds().getWidth();
    int height = (int) Screen.getPrimary().getBounds().getHeight() - 80;

    TreeItem rootItem = new TreeItem("Tutorials");

    TreeItem accounts = new TreeItem("Accounts");

    TreeItem products = new TreeItem("Products");

    TreeItem users = new TreeItem("App Users");

    TreeView treeView = new TreeView();

    Label labelAccountName = new Label("Account Name");

    TextField textFieldAccountName = new TextField();

    Label labelOpeningBalance = new Label("Opening Balance");

    Label accountId = new Label("");
    Label transactionId = new Label("");
    Label productId = new Label("");

    TextField textFieldOpeningBalance = new TextField();

    Label labelOpeningDate = new Label("Opening Date");

    DatePicker textFieldOpeningDate = new DatePicker();

    Button buttonSubmitAccount = new Button("Save Account");

    Button clearAndCancel = new Button("Clear");
    Button clearAndCancelTransaction = new Button("Clear");

    GridPane gridPaneAccount = new GridPane();

    GridPane gridPaneTransactions = new GridPane();

    TableView tableViewAccounts = new TableView();

    TableView tableViewTransactions = new TableView();

    HBox hBoxNewAccount = new HBox(gridPaneAccount);

    HBox hBoxTransactions = new HBox(gridPaneTransactions);

    GridPane gridPaneProducts = new GridPane();

    HBox hBoxProducts = new HBox(gridPaneProducts);

    HBox hBoxAllItems = new HBox(treeView, hBoxNewAccount);

    /**
     * UI Components For New Transaction Record
     */
    Label labelTitle = new Label("");

    Label labelVehicleNumber = new Label("Vehicle Number");

    TextField vehicleNumber = new TextField();

    Label labelTxnDate = new Label("Transaction Date");

    DatePicker transactionDate = new DatePicker();

    Label labelOrderNumber = new Label("Order Number");

    TextField fieldOrderNumber = new TextField();

    Label labelQuantity = new Label("Quantity");

    TextField fieldQuantity = new TextField();

    Label labelProduct = new Label("Product");

    ComboBox comboBoxProduct = new ComboBox();

    Button buttonSubmitTransaction = new Button("Save Transaction");

    CheckBox allowOverdraft = new CheckBox("Allow Overdraft");

    Button printCustomers = new Button("Print");

    Button printTransactions = new Button("Print Transactions");

    MenuBar menuBar = new MenuBar();

    HBox hBox = new HBox(hBoxAllItems);

    Scene scene = new Scene(hBox,width,height);
}

package necoapi.uicomponents.uihelpers;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import necoapi.domain.AccountResponse;
import necoapi.domain.ProductResponse;
import necoapi.helpers.APIConstants;
import necoapi.helpers.ControlsHelpers;
import necoapi.helpers.RESTCallHelpers;
import necoapi.helpers.Selected;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Optional;

public class TreeViewHelpers {

    public static void treeViewSelectionHelper(
            TreeItem newValue, HBox hBoxAllItems,
            TreeView treeView, Panel panelAccounts,
            TreeItem accounts, TableView tableViewAccounts,
            Panel panelTransactions, TableView tableViewTransactions,
            ComboBox comboBoxProduct, TextField vehicleNumber,
            TextField orderNumber, DatePicker datePickerTransactionDate,
            TextField transactionQuantity, Button submitTransactionButton,
            Button clearAndCancelTransaction, Panel panelProducts,
            Button clearProductFields, Button submitProduct, TextField productName,
            TextField productDescription, TextField productUnitPrice,
            Button submitAccount, Button cancelAccount,
            Panel panelUsers, TableView tableViewUsers) {
        if (newValue.getValue().toString().equalsIgnoreCase("Accounts")) {
            hBoxAllItems.getChildren().clear();
            hBoxAllItems.getChildren().addAll(treeView, panelAccounts);
            RESTCallHelpers.populateAccounts(accounts, APIConstants.FETCH_ACCOUNTS);
            RESTCallHelpers.updateTableAccounts(tableViewAccounts);
            tableViewAccounts.setPlaceholder(new Label("No accounts to display at this time"));
            submitAccount.setText("Save Account");
            cancelAccount.setText("Clear");
        }else if (newValue.getParent().getValue().toString().equalsIgnoreCase("Accounts")){
            showSelectedAccountTransactions(newValue, hBoxAllItems, treeView, panelTransactions, tableViewTransactions,
                    comboBoxProduct, vehicleNumber, orderNumber, datePickerTransactionDate, transactionQuantity,
                    submitTransactionButton, clearAndCancelTransaction);
        }else if (newValue.getParent().getValue().toString().equalsIgnoreCase("Products")){
            hBoxAllItems.getChildren().clear();
            hBoxAllItems.getChildren().addAll(treeView, panelProducts);
            clearProductFields.setText("Cancel");
            submitProduct.setText("Edit Product");
            Optional<ProductResponse> productResponse = Selected.getInstance().getProductResponses()
                    .stream().filter(product -> product.getProductName()
                            .equalsIgnoreCase(newValue.getValue().toString())).findFirst();
            if (productResponse.isPresent()) {
                ProductResponse theProduct = productResponse.get();
                Selected.getInstance().setSelectedProduct(theProduct);
                productName.setText(theProduct.getProductName());
                productDescription.setText(theProduct.getProductDescription());
                productUnitPrice.setText(theProduct.getProductUnitPrice().toString());
            }
        }else if (newValue.getValue().toString().equalsIgnoreCase("Products")){
            hBoxAllItems.getChildren().clear();
            hBoxAllItems.getChildren().addAll(treeView, panelProducts);
            clearProductFields.setText("Clear");
            submitProduct.setText("Save Product");
            ControlsHelpers.clearProductsFields(productName, productDescription, productUnitPrice);
        }else if (newValue.getValue().toString().equalsIgnoreCase("App Users")){
            hBoxAllItems.getChildren().clear();
            hBoxAllItems.getChildren().addAll(treeView, panelUsers);
            RESTCallHelpers.updateTableUsers(tableViewUsers);
        }
    }

    public static void showSelectedAccountTransactions(
            TreeItem newValue,
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
                        .equalsIgnoreCase(newValue.getValue().toString())).findFirst();
        if (accountResponse.isPresent()) {
            Selected.getInstance().setSelectedAccount(accountResponse.get());
        }
        RESTCallHelpers.updateTableTransactions(tableViewTransactions, newValue.getValue().toString());
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

}

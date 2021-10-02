package necoapi.uicomponents.uihelpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.*;
import necoapi.domain.AccountRequest;
import necoapi.domain.ProductRequest;
import necoapi.domain.TransactionRequest;
import necoapi.helpers.*;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public class ActionsHelpers {

    public static void productSubmission(TextField productName, TextField productDescription, TextField productUnitPrice, Button submitProduct, TreeItem productItems) {
        ProductRequest productRequest = ProductRequest.builder()
                .productName(productName.getText())
                .productDescription(productDescription.getText())
                .productUnitPrice(Double.parseDouble(productUnitPrice.getText()))
                .build();
        if (submitProduct.getText().equalsIgnoreCase("Save Product")) {
            ResponseEntity<Object> responseResponseEntity = RESTCallHelpers.submitProduct(productRequest);
            if (responseResponseEntity.getStatusCode().is2xxSuccessful()) {
                ControlsHelpers.clearProductsFields(productName, productDescription, productUnitPrice);
                RESTCallHelpers.populateProducts(productItems);
            }else {
                APIError apiError = new ObjectMapper().convertValue(responseResponseEntity.getBody(), APIError.class);
                Alert alert = new Alert(Alert.AlertType.ERROR, apiError.getMessage());
                alert.setTitle("Error Occurred");
                alert.setHeaderText(apiError.getReason());
                alert.showAndWait();
            }
        }else {
            ResponseEntity<Object> responseResponseEntity = RESTCallHelpers.editProduct(productRequest, Selected.getInstance().getSelectedProduct().getProductId());
            if (responseResponseEntity.getStatusCode().is2xxSuccessful()) {
                ControlsHelpers.clearProductsFields(productName, productDescription, productUnitPrice);
                RESTCallHelpers.populateProducts(productItems);
            }else {
                APIError apiError = new ObjectMapper().convertValue(responseResponseEntity.getBody(), APIError.class);
                Alert alert = new Alert(Alert.AlertType.ERROR, apiError.getMessage());
                alert.setTitle("Error Occurred");
                alert.setHeaderText(apiError.getReason());
                alert.showAndWait();
            }
        }
    }

    public static void transactionSubmission(DatePicker datePickerTransactionDate, TextField transactionQuantity, TextField orderNumber, TextField vehicleNumber, Button submitTransactionButton, ComboBox comboBoxProduct, TableView tableViewTransactions, Label transactionId, Button clearAndCancelTransaction, Panel panelTransactions) {
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .transactionDate(datePickerTransactionDate.getValue().toString())
                .transactionQuantity(Double.parseDouble(transactionQuantity.getText()))
                .orderNumber(orderNumber.getText())
                .accountId(Selected.getInstance().getSelectedAccount().getAccountId().toString())
                .productId(Selected.getInstance().getSelectedProduct().getProductId().toString())
                .vehicleNumber(vehicleNumber.getText())
                .build();
        if (submitTransactionButton.getText().equalsIgnoreCase("Save Transaction")) {

            ResponseEntity<Object> responseResponseEntity = RESTCallHelpers.submitTransaction(transactionRequest);
            if (responseResponseEntity.getStatusCode().is2xxSuccessful()) {
                ControlsHelpers.clearTransactions(vehicleNumber, orderNumber, comboBoxProduct, datePickerTransactionDate, transactionQuantity);
                RESTCallHelpers.updateTableTransactions(tableViewTransactions, Selected.getInstance().getSelectedAccount().getAccountName());
            }else {
                APIError apiError = new ObjectMapper().convertValue(responseResponseEntity.getBody(), APIError.class);
                Alert alert = new Alert(Alert.AlertType.ERROR, apiError.getMessage());
                alert.setTitle("Error Occurred");
                alert.setHeaderText(apiError.getReason());
                alert.showAndWait();
            }
        }else {
            ResponseEntity<Object> responseResponseEntity = RESTCallHelpers.editTransaction(transactionRequest, Long.parseLong(transactionId.getText()));
            if (responseResponseEntity.getStatusCode().is2xxSuccessful()) {
                ControlsHelpers.clearTransactions(vehicleNumber, orderNumber, comboBoxProduct, datePickerTransactionDate, transactionQuantity);
                RESTCallHelpers.updateTableTransactions(tableViewTransactions, Selected.getInstance().getSelectedAccount().getAccountName());
                submitTransactionButton.setText("Save Transaction");
                clearAndCancelTransaction.setText("Clear");
            }else {
                APIError apiError = new ObjectMapper().convertValue(responseResponseEntity.getBody(), APIError.class);
                Alert alert = new Alert(Alert.AlertType.ERROR, apiError.getMessage());
                alert.setTitle("Error Occurred");
                alert.setHeaderText(apiError.getReason());
                alert.showAndWait();
            }
        }
        RESTCallHelpers.updateBalancesInHeading(panelTransactions);
    }

    public static void accountSubmission(TextField accountName, TextField openingBalance, DatePicker openingDate, CheckBox allowOverdraft, Button submitAccountButton, Label accountId, Button clearAndCancel, TableView tableViewAccounts, TreeItem accounts) {
        AccountRequest accountRequest = AccountRequest.builder()
                .accountName(accountName.getText())
                .openingBalance(Double.parseDouble(openingBalance.getText()))
                .openingDate(openingDate.getValue().toString())
                .bankOverdraftAllowed(allowOverdraft.isSelected()? true : false)
                .build();
        if (submitAccountButton.getText().equalsIgnoreCase("Save Account")) {
            ResponseEntity<Object> responseResponseEntity = RESTCallHelpers.submitAccount(accountRequest);
            if (responseResponseEntity.getStatusCode().is2xxSuccessful()) {
                ControlsHelpers.clearAccountFields(accountName, openingBalance, openingDate, allowOverdraft);
            }else {
                APIError apiError = new ObjectMapper().convertValue(responseResponseEntity.getBody(), APIError.class);
                Alert alert = new Alert(Alert.AlertType.ERROR, apiError.getMessage());
                alert.setTitle("Error Occurred");
                alert.setHeaderText(apiError.getReason());
                alert.showAndWait();
            }
        }else {
            Long accountIdValue = Long.parseLong(accountId.getText());
            ResponseEntity<Object> responseResponseEntity = RESTCallHelpers.editAccount(accountRequest, accountIdValue);
            if (responseResponseEntity.getStatusCode().is2xxSuccessful()) {
                ControlsHelpers.clearAccountFields(accountName, openingBalance, openingDate, allowOverdraft);
                accountId.setText(null);
                submitAccountButton.setText("Save Account");
                clearAndCancel.setText("Clear");
            }else {
                APIError apiError = new ObjectMapper().convertValue(responseResponseEntity.getBody(), APIError.class);
                Alert alert = new Alert(Alert.AlertType.ERROR, apiError.getMessage());
                alert.setTitle("Error Occurred");
                alert.setHeaderText(apiError.getReason());
                alert.showAndWait();
            }
        }
        RESTCallHelpers.updateTableAccounts(tableViewAccounts);
        RESTCallHelpers.populateAccounts(accounts, APIConstants.FETCH_ACCOUNTS);
    }
}

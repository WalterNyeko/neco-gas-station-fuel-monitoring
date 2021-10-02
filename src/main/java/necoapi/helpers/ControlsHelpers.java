package necoapi.helpers;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ControlsHelpers {

    public static void clearProductsFields(TextField productName, TextField productDescription, TextField productUnitPrice) {
        productName.clear();
        productDescription.clear();
        productUnitPrice.clear();
    }

    public static void clearTransactions(TextField vehicleNumber, TextField orderNumber, ComboBox comboBoxProduct, DatePicker datePickerTransactionDate, TextField transactionQuantity) {
        vehicleNumber.clear();
        orderNumber.clear();
        comboBoxProduct.getSelectionModel().select(0);
        datePickerTransactionDate.setValue(null);
        transactionQuantity.clear();
    }

    public static void clearAccountFields(TextField accountName, TextField openingBalance, DatePicker openingDate, CheckBox allowOverdraft) {
        accountName.setText(null);
        openingBalance.setText(null);
        openingDate.setValue(null);
        allowOverdraft.setSelected(false);
    }

}

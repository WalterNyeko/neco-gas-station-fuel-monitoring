package necoapi.uicomponents;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class GridPaneBuilder {

    static HomePage homePage = new HomePage();
    static GridPane gridPaneAccount = homePage.getGridPaneAccount();
    static {
        TextField accountName = homePage.getTextFieldAccountName();
        TextField openingBalance = homePage.getTextFieldOpeningBalance();
        DatePicker openingDate = homePage.getTextFieldOpeningDate();
        Button submitAccountButton = homePage.getButtonSubmitAccount();
        Label accountId = homePage.getAccountId();
        Button clearAndCancel = homePage.getClearAndCancel();
        TableView tableViewAccounts = homePage.getTableViewAccounts();
        gridPaneAccount.setHgap(20);
        gridPaneAccount.setVgap(20);

        gridPaneAccount.add(accountId, 0,0,1,1);
        gridPaneAccount.add(homePage.getLabelAccountName(), 2,0, 1,1);
        gridPaneAccount.add(accountName, 3,0, 1,1);
        gridPaneAccount.add(homePage.getLabelOpeningBalance(), 4,0, 1,1);
        gridPaneAccount.add(openingBalance, 5,0, 1,1);
        gridPaneAccount.add(homePage.getLabelOpeningDate(), 2,1, 1,1);
        gridPaneAccount.add(openingDate, 3,1, 1,1);
        gridPaneAccount.add(clearAndCancel, 4,1,1,1);
        gridPaneAccount.add(submitAccountButton, 5,1, 1,1);
        gridPaneAccount.add(tableViewAccounts, 0, 2, 7,2);

    }

}

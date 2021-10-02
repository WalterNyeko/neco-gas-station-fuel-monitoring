package necoapi.uicomponents;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import necoapi.domain.AccountResponse;
import necoapi.domain.TopUp;
import necoapi.helpers.*;
import necoapi.uicomponents.uihelpers.TableViewHelpers;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.kordamp.bootstrapfx.scene.layout.Panel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopupPage {

    Panel panel;

    public TopupPage(Panel panel) {
        this.panel = panel;
    }

    Stage popupWindow=new Stage();

    Label amount = new Label("Top Up Amount:");

    TextField fieldAmount = new TextField();

    Button buttonSubmit = new Button("Save");

    Button buttonCancel = new Button("Cancel");

    Button buttonPrint = new Button("Print Top Ups");

    TableView tableViewTopups = TableViewHelpers.getTableViewTopups();

    VBox layout = new VBox();

    Scene scene = new Scene(layout, 900, 650);

    GridPane gridPane = new GridPane();

    {
        //Grid pane
        gridPane.setVgap(20);
        gridPane.setHgap(20);
        gridPane.setId("pop-up-top-up");
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(amount, 0,0,1,1);
        gridPane.add(fieldAmount, 1,0,1,1);
        gridPane.add(buttonSubmit, 2,0,1,1);
        gridPane.add(buttonCancel, 0,1,1,1);
        gridPane.add(buttonPrint, 1,1,1,1);
        gridPane.add(tableViewTopups, 0,3,4,8);

        buttonSubmit.getStyleClass().addAll("btn", "btn-primary");
        buttonCancel.getStyleClass().addAll("btn", "btn-default");
        buttonPrint.getStyleClass().addAll("btn", "btn-secondary");
        buttonSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @SneakyThrows
            @Override
            public void handle(ActionEvent event) {
                TopUp topUp = new TopUp();
                try {
                    topUp.setAmount(Double.parseDouble(fieldAmount.getText()));
                }catch (Exception exception) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Error saving top up record");
                    alert.setHeaderText(exception.getMessage());
                    alert.setContentText(exception.getCause()!= null ? exception.getCause().getMessage(): "Parse exception occurred");
                    alert.showAndWait();
                }
                AccountResponse accountResponse = Selected.getInstance().getSelectedAccount();
                HttpEntity<necoapi.models.TopUp> httpEntity = new HttpEntity<>(
                        ObjectMappers.mapTopUp(topUp, ObjectMappers.mapAccountEntity(accountResponse)),
                        RESTCallHelpers.createHeaders(Selected.getInstance().getToken()));
                ResponseEntity<Object> results = RESTCallHelpers.saveTopUp(APIConstants.SAVE_TOP_UP.replace("{accountId}",
                        accountResponse.getAccountId().toString()), HttpMethod.POST, httpEntity);
                if (!results.getStatusCode().is2xxSuccessful()) {
                    APIError apiError = new ObjectMapper().convertValue(results.getBody(), APIError.class);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error saving top up record");
                    alert.setHeaderText(apiError.getReason());
                    alert.setContentText(apiError.getMessage());
                    alert.showAndWait();
                }else {
                    RESTCallHelpers.updateTableTopUps(tableViewTopups);
                    fieldAmount.clear();
                    RESTCallHelpers.updateBalancesInHeading(panel);
                }
            }
        });
        buttonCancel.setOnAction(e -> popupWindow.close());
        buttonPrint.setOnAction(e -> popupWindow.close());

        layout.getChildren().addAll(gridPane);
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(20);
        layout.setPadding(new Insets(15,10,10,10));

        String  style = getClass().getResource("/css/home.css").toExternalForm();
        scene.getStylesheets().add(style);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Top Up Account Balance For "+ Selected.getInstance().getSelectedAccount().getAccountName());
        popupWindow.setScene(scene);
    }
}

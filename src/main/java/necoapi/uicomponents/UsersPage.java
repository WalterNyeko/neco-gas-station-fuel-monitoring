package necoapi.uicomponents;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import necoapi.domain.UserRequest;
import necoapi.helpers.APIConstants;
import necoapi.helpers.APIError;
import necoapi.helpers.RESTCallHelpers;
import necoapi.helpers.Selected;
import necoapi.models.Role;
import necoapi.uicomponents.uihelpers.TableViewHelpers;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersPage {

    GridPane gridPaneUsers = new GridPane();

    Label labelUsername = new Label("Username");
    Label labelPassword = new Label("Password");
    Label labelEmail = new Label("Email");
    Label labelAddress = new Label("Address");
    Label labelRoles = new Label("Roles");

    TextField fieldUsername = new TextField();
    PasswordField fieldPassword = new PasswordField();
    TextField fieldEmail = new TextField();
    TextField fieldAddress = new TextField();
    TextField fieldRoles = new TextField();

    Button buttonSaveUser = new Button("Save User");
    Button buttonClear = new Button("Clear");

    HBox hBoxUsers = new HBox(gridPaneUsers);

    TableView tableViewUsers = TableViewHelpers.getTableViewUsers();

    {
        gridPaneUsers.setVgap(20);
        gridPaneUsers.setHgap(20);
        gridPaneUsers.setPadding(new Insets(10));

        gridPaneUsers.add(labelUsername, 0,0,1,1);
        gridPaneUsers.add(fieldUsername, 1,0,1,1);
        gridPaneUsers.add(labelPassword, 2,0,1,1);
        gridPaneUsers.add(fieldPassword, 3,0,1,1);
        gridPaneUsers.add(labelEmail, 4,0,1,1);
        gridPaneUsers.add(fieldEmail, 5,0,1,1);
        gridPaneUsers.add(labelAddress, 0,1,1,1);
        gridPaneUsers.add(fieldAddress, 1,1,1,1);
        gridPaneUsers.add(labelRoles, 2,1,1,1);
        gridPaneUsers.add(fieldRoles, 3,1,1,1);
        gridPaneUsers.add(buttonSaveUser, 4,1,1,1);
        gridPaneUsers.add(buttonClear, 5,1,1,1);
        gridPaneUsers.add(TableViewHelpers.getTableViewUsers(), 0,3,8,2);

        buttonClear.getStyleClass().addAll("btn", "btn-secondary");
        buttonSaveUser.getStyleClass().addAll("btn", "btn-primary");
        buttonSaveUser.setId("button-save-edit");
        buttonSaveUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UserRequest userRequest = new UserRequest();
                userRequest.setUsername(fieldUsername.getText());
                userRequest.setPassword(fieldPassword.getText());
                userRequest.setEmail(fieldEmail.getText());
                userRequest.setAddress(fieldAddress.getText());
                Set<Role> roleList = new HashSet<>();
                List<String> roles = Arrays.asList(fieldRoles.getText().split(","));
                for (String role: roles) {
                    Role roleModel = new Role();
                    roleModel.setRole(role);
                    roleList.add(roleModel);
                }
                userRequest.setRoles(roleList);

                HttpEntity<UserRequest> httpEntity = new HttpEntity<>(userRequest,
                        RESTCallHelpers.createHeaders(Selected.getInstance().getToken()));
                ResponseEntity<Object> responseEntity = RESTCallHelpers
                        .usersPostAndPut(APIConstants.POST_USER, httpEntity, HttpMethod.POST);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    tableViewUsers.refresh();
                    RESTCallHelpers.updateTableUsers(tableViewUsers);
                    clearFields();
                }else {
                    System.out.println("Something is not working well...");
                    APIError apiError = new ObjectMapper().convertValue(responseEntity.getBody(), APIError.class);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error saving user");
                    alert.setHeaderText(apiError.getReason());
                    alert.setContentText(apiError.getMessage());
                    alert.showAndWait();
                }
            }
        });

        buttonClear.setId("button-cancel-clear");
        buttonClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearFields();
            }
        });

        hBoxUsers.setSpacing(20);
        hBoxUsers.setPadding(new Insets(20));
    }

    private void clearFields() {
        fieldAddress.clear();
        fieldEmail.clear();
        fieldPassword.clear();
        fieldRoles.clear();
        fieldUsername.clear();
    }
}

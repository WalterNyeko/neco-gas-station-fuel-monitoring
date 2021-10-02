package necoapi.uicomponents;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import necoapi.domain.CustomUserDetails;
import necoapi.domain.LoginResponse;
import necoapi.domain.UserRequest;
import necoapi.helpers.APIError;
import necoapi.helpers.ObjectMappers;
import necoapi.helpers.RESTCallHelpers;
import necoapi.helpers.Selected;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPage {
    int width = (int) Screen.getPrimary().getBounds().getWidth() / 2;
    int height = (int) Screen.getPrimary().getBounds().getHeight() / 2;

    GridPane gridPane = new GridPane();

    Label labelUserName = new Label("Username");
    TextField fieldUsername = new TextField();

    Label labelPassword = new Label("Password");
    PasswordField passwordField = new PasswordField();

    Button buttonSignIn = new Button("Login In");

    {
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.add(labelUserName, 0,0,1,1);
        gridPane.add(fieldUsername, 1,0,1,1);
        gridPane.add(labelPassword, 0,1,1,1);
        gridPane.add(passwordField, 1,1,1,1);
        gridPane.add(buttonSignIn, 0, 2, 2,1);
        buttonSignIn.getStyleClass().addAll("btn-sign-in");
        buttonSignIn.setId("button-save-edit");
        buttonSignIn.setMinWidth(250);
        buttonSignIn.setDefaultButton(true);
    }

    HBox hBox = new HBox(gridPane);

    {
        hBox.setPadding(new Insets(height / 3,20,20,width / 3));
        hBox.setSpacing(20);
    }

    String  style = getClass().getResource("/css/home.css").toExternalForm();
    Scene scene = new Scene(hBox,width,height);

    {
        scene.getStylesheets().add(style);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        String necoLogo = getClass().getResource("/neco-logo.jpg").toString();
        BackgroundImage myBI = new BackgroundImage(new Image(necoLogo,150,150,true,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        hBox.setBackground(new Background(myBI));
    }

    public void loginUser(Stage stage, Scene scene) {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(fieldUsername.getText());
        userRequest.setPassword(passwordField.getText());
        HttpEntity<UserRequest> httpEntity = new HttpEntity<>(userRequest);
        ResponseEntity<Object> responseEntity = RESTCallHelpers.post("http://localhost:8080/neco/v1/login",  httpEntity, HttpMethod.POST);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            LoginResponse loginResponse = new ObjectMapper().convertValue(responseEntity.getBody(), LoginResponse.class);
            System.out.println(loginResponse.getToken());
            Selected.getInstance().setToken(loginResponse.getToken());
            stage.setScene(scene);
            // Date format
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

            String title = stage.getTitle();
            title += "        [Logged In User: "+fieldUsername.getText()+", Current Time: ";

            String finalTitle = title;
            EventHandler<ActionEvent> eventHandler = e -> {
            stage.setTitle(finalTitle +df.format(new Date())+"]");
            };
            Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
            animation.setCycleCount(Timeline.INDEFINITE);
            animation.play();
            stage.centerOnScreen();
        }else {
            APIError apiError = new ObjectMapper().convertValue(responseEntity.getBody(), APIError.class);
            System.out.println(apiError.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(apiError.getReason());
            alert.setTitle("Login Error");
            alert.setHeaderText(apiError.getMessage());
            alert.show();

        }
    }
}

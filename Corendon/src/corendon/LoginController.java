/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corendon;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author alexa
 */
public class LoginController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passField;
    @FXML
    private Button loginBtn;

    @FXML
    private void handleButtonAction(ActionEvent event) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException, IOException {

        //succesvol ingelogd...door naar nieuw scherm
        if (login(usernameField.getText(), passField.getText())) {
            Stage stage = (Stage) loginBtn.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage home = new Stage();

            home.setScene(new Scene(root));
            home.show();

            stage.close();
        } else {
            label.setText("Wrong credentials.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public boolean login(String username, String wachtwoord) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        final String USERNAME = "root";
        final String PASSWORD = "Welkom123!";
        final String CONN_STRING = "jdbc:mysql://localhost:3306/corendon";
        boolean ingelogd = false;

        Connection conn = null;
        Statement stmt = null;
        ResultSet result = null;
        int teller = 0;
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            result = stmt.executeQuery("SELECT * FROM medewerker WHERE Username = '" + username + "'  AND Wachtwoord = '" + wachtwoord + "'");

            System.out.println("Connected ");

            while (result.next()) {
                teller++;
            }
            if (teller == 1) {
                ingelogd = true;
            } else {
                teller = 0;
                ingelogd = false;
            }

        } catch (SQLException ex) {
            System.err.print(ex);
        } finally {
            if (conn != null || stmt != null) {
                stmt.close();
                conn.close();
            }
        }
        return ingelogd;
    }
}

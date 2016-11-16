/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corendon;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javax.naming.spi.DirStateFactory;

/**
 * FXML Controller class Regelt alle acties op de home pagina van de Corendon
 * App
 *
 * @author alex chorak
 */
public class HomeController implements Initializable {

    //database gegevens
    static Connection conn = null;
    static PreparedStatement stmt = null;
    static ResultSet rs = null;

    static final String USERNAME = "root";
    static final String PASSWORD = "Welkom123!";
    static final String CONN_STRING = "jdbc:mysql://localhost:3306/corendon";
    //einde database gegevens 

    @FXML
    private ChoiceBox gewichtCb;
    @FXML
    ChoiceBox reizigerCb;

    //begin reiziger gegevens
    @FXML
    private TextField naamTb;
    @FXML
    private TextField adresTb;
    @FXML
    private TextField woonplaatsTb;
    @FXML
    private TextField postcodeTb;
    @FXML
    private TextField landTb;
    @FXML
    private TextField telefoonTb;
    @FXML
    private TextField emailTb;
    //einde reiziger gegevens

    @FXML
    private TabPane mainTab;

    @Override //choicebox koffergewicht vullen met waardes

    public void initialize(URL url, ResourceBundle rb) {
        gewichtCb.getItems().addAll(
                "0-10KG",
                "10-20KG",
                "20KG of meer");

        //choidebox vullen met alle reiziger namen....misschien even aanpassen op datum...laatste dag ofzo alleen
        try {
            reizigerCb.getItems().addAll(reizigerCombobox());

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //haal een lijst met namen op uit de database 
    private List<String> reizigerCombobox() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        List<String> reizigers = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            stmt = conn.prepareStatement("SELECT Reizigerid, naam FROM Reiziger ORDER BY reizigerid ASC");
            rs = stmt.executeQuery();

            while (rs.next()) {
                reizigers.add(rs.getInt("reizigerid") + rs.getString("Naam"));
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return reizigers;
    }

    @FXML //button event om de reiziger op te slaan
    private void opslaanButtonReiziger(ActionEvent event) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        //kijken of alles ingevuld is
        if (!naamTb.getText().isEmpty() && !adresTb.getText().isEmpty() && !woonplaatsTb.getText().isEmpty()
                && !postcodeTb.getText().isEmpty() && !landTb.getText().isEmpty() && !telefoonTb.getText().isEmpty() && !emailTb.getText().isEmpty()) {
            //als alles klopt wordt de methode 'OpslaanReiziger' aangeroepen
            if (opslaanReiziger(naamTb.getText(), adresTb.getText(), woonplaatsTb.getText(),
                    postcodeTb.getText(), landTb.getText(), telefoonTb.getText(), emailTb.getText())) {

                //alle velden weer leegmaken
                naamTb.clear();
                adresTb.clear();
                woonplaatsTb.clear();
                postcodeTb.clear();
                landTb.clear();
                telefoonTb.clear();
                emailTb.clear();

                //succesvol opgeslagen
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Corendon - Reiziger Opslaan");
                alert.setHeaderText("Succesvol opgeslagen!");
                alert.showAndWait();
            }
        } else {
            //error weergeven bij niet ingevulde velden
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Corendon - Reiziger Opslaan");
            alert.setHeaderText("Vul alle velden in!");
            alert.showAndWait();
        }

    }

    //reiziger opslaanin de database
    public boolean opslaanReiziger(String naam, String adres, String woonplaats, String postcode, String land, String telefoon, String email) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            //ingevoerde gegevens opslaan in de database
            stmt = conn.prepareStatement("INSERT INTO Reiziger (Naam, Adres, Woonplaats, Postcode, Land, Telefoon, Email) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, naam);
            stmt.setString(2, adres);
            stmt.setString(3, woonplaats);
            stmt.setString(4, postcode);
            stmt.setString(5, land);
            stmt.setString(6, telefoon);
            stmt.setString(7, email);

            stmt.executeUpdate();

            stmt.close();
            return true;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            conn.close();
        }
    }

}

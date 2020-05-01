/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
//import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<String> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	String mese = boxMese.getValue();
    	int nmese = Integer.parseInt(mese);
    	
    	txtResult.appendText("\n\nLa sequenza migliore e': "+model.trovaSequenza(nmese));
    	
    	//txtResult.appendText("\nIl costo relativo e': "+model.calcoloCosto(model.trovaSequenza(nmese)));
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	//calcolo l'umidità media nel mese condiderato
    	txtResult.clear();
    	
    	//int mese = boxMese.getValue();
    	String mese = boxMese.getValue();
    	int m = Integer.parseInt(mese);
    	
    	String to = "Torino";
    	String mi = "Milano";
    	String ge = "Genova";
    	
    	String mediaTo = this.model.getUmiditaMedia(m, to);
    	String mediaMi = this.model.getUmiditaMedia(m, mi);
    	String mediaGe = this.model.getUmiditaMedia(m, ge);
    	
    	txtResult.appendText("La media del mese a Genova e': "+mediaGe);
    	txtResult.appendText("\nLa media del mese a Milano e': "+mediaMi);
    	txtResult.appendText("\nLa media del mese a Torino e': "+mediaTo);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setmodel(Model model) {
    	this.model= model;
    	boxMese.getItems().addAll(this.model.getElencoMesi());
    }
}


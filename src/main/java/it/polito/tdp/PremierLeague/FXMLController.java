/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.SquadraPunti;
import it.polito.tdp.PremierLeague.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnClassifica"
    private Button btnClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbSquadra"
    private ComboBox<Team> cmbSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doClassifica(ActionEvent event) {
    	this.txtResult.clear();
    	if(this.model.getGrafo() == null) {
    		this.txtResult.setText("Creare grafo!");
    		return;
    	}
    	Team t = this.cmbSquadra.getValue();
    	if(t == null) {
    		this.txtResult.setText("Scegliere una squadra!");
    		return;
    	}
    	List<SquadraPunti> migliori = this.model.miglioriDi(t); 
    	List<SquadraPunti> peggiori = this.model.peggioriDi(t); 
    	
    	this.txtResult.appendText("SQUADRE MIGLIORI:");
    	for(SquadraPunti s : migliori)
    		this.txtResult.appendText("\n" + s.getT().getName() + "(" + s.getPunti() + ")");
    	
    	this.txtResult.appendText("\n\nSQUADRE PEGGIORI:");
    	for(SquadraPunti s : peggiori)
    		this.txtResult.appendText("\n" + s.getT().getName() + "(" + s.getPunti() + ")");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String msg = this.model.creaGrafo();
    	
    	this.txtResult.setText(msg);
    	
    	this.cmbSquadra.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	if(this.model.getGrafo() == null) {
    		this.txtResult.setText("Creare grafo!");
    		return;
    	}
    	
    	String nS = this.txtN.getText();
    	String xS = this.txtX.getText();
    	
    	try {
    		int n = Integer.parseInt(nS);
    		if(n < 0) {
    			this.txtResult.setText("N deve essere maggiore di 0!");
        		return;
    		}
    		int x = Integer.parseInt(xS);
    		if(x < 0) {
    			this.txtResult.setText("X deve essere maggiore di 0!");
        		return;
    		}
    		
    		this.model.init(n, x);
    		this.txtResult.appendText("Reporter medi per partita: " + this.model.getRepMedi());
    		this.txtResult.appendText("\n\nPartite sotto la soglia X: " + this.model.getPartiteSottoX());
    	}
    	catch(NumberFormatException nfe) {
    		this.txtResult.setText("N ed X devono essere interi!");
    		return;
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnClassifica != null : "fx:id=\"btnClassifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbSquadra != null : "fx:id=\"cmbSquadra\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    }
}

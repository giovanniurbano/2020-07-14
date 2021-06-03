/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Simulator;
import it.polito.tdp.PremierLeague.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private Simulator sim;

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
    	Team s = this.cmbSquadra.getValue();
    	
    	if(this.model.getGrafo() == null){
    		this.txtResult.setText("Creare prima il grafo!");
    		return;
    	}
    	
    	if(s == null) {
    		this.txtResult.setText("Scegliere una squadra!");
    		return;
    	}
    	
    	this.txtResult.appendText("\nSquadre migliori: ");
    	for(Team t : this.model.getMigliori(s).keySet()) {
    		this.txtResult.appendText(t.getName() + "(" + this.model.getMigliori(s).get(t) + ")\n");
    	}
    	
    	this.txtResult.appendText("\nSquadre peggiori: ");
    	for(Team t : this.model.getPeggiori(s).keySet()) {
    		this.txtResult.appendText(t.getName() + " (" + this.model.getPeggiori(s).get(t) + ")\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String msg = this.model.creaGrafo();
    	this.txtResult.setText(msg);
    	
    	this.cmbSquadra.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doSimula(ActionEvent event) {
    	int n = Integer.parseInt(this.txtN.getText());
    	int x = Integer.parseInt(this.txtX.getText());
    	
    	this.sim.init(n, x, this.model.getGrafo());
    	this.sim.run();
    	
    	this.txtResult.setText("#ReporterMedioPerPartita: " + this.sim.getReporterMediPerPartita());
    	this.txtResult.appendText("\n#PartiteSottoSoglia: " + this.sim.getnPartiteSottoSoglia());
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
    	this.sim = new Simulator(this.model);
    }
}

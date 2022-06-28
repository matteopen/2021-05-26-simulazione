/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
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

    @FXML // fx:id="btnLocaleMigliore"
    private Button btnLocaleMigliore; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	txtResult.clear();
    	String result = "Miglior cammino: \n";
    	Business partenza = this.cmbLocale.getValue();
    	if(partenza==null) {
    		this.txtResult.setText("Selezionare business");
    	}
    	Double soglia = Double.parseDouble(this.txtX.getText());
    	if(soglia==null || soglia<0.0 || soglia>1.0) {
    		this.txtResult.setText("Inserire soglia corretta");
    	}
    	List<Business> best = this.model.cerca(partenza, soglia);
    	for(Business b : best) {
    		result+=b+"\n";
    	}
    	txtResult.appendText(result);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	String city = this.cmbCitta.getValue();
    	if(city == null) {
    		this.txtResult.setText("Selezionare città!");
    		return;
    	}
    	Integer year = this.cmbAnno.getValue();
    	if(year == null) {
    		this.txtResult.setText("Selezionare anno!");
    		return;
    	}
    	this.model.creaGrafo(city, year);
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("#Vertici: "+this.model.nVertices()+"\n");
    	this.txtResult.appendText("#Archi: "+this.model.nEdges()+"\n");
    	
    	this.cmbLocale.getItems().addAll(this.model.businesses());
    }

    @FXML
    void doLocaleMigliore(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	Business result = this.model.best();
    	
    	this.txtResult.appendText("Miglior locale: "+result);

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnLocaleMigliore != null : "fx:id=\"btnLocaleMigliore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.cmbAnno.getItems().addAll(this.model.getYears());
    	this.cmbCitta.getItems().addAll(this.model.getCities());
    }
}

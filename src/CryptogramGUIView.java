

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CryptogramGUIView extends Application implements Observer {
	
	public   List<Object> encodedGrid;
	public   List<Object> decodedGrid;
	public GridPane grid;
	
	public CryptogramModel m;
	
	@Override
	public void start(Stage stage) throws Exception {
		m = new CryptogramModel();
		CryptogramController c = new CryptogramController(this, m);
		m.addObserver(this);
		grid = new GridPane(); 
		c.setGrids(grid);
		BorderPane p = new BorderPane();p.setCenter(grid);
		c.setButtons(p);
        Scene scene = new Scene(p, 900, 400);  
		stage.setScene(scene);
		stage.setTitle("Cryptograms");
        stage.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println(arg);
		Map<Character, Character> record = (Map<Character, Character>) arg;
		int gridSize = grid.getChildren().size();
		for (int i = 0; i < gridSize; i++) {
			VBox vbox = (VBox) grid.getChildren().get(i);
			Label label = (Label) vbox.getChildren().get(1);
			TextField text = (TextField) vbox.getChildren().get(0);
			
			if (record.containsKey(label.getText().charAt(0))) {
				if (!(record.get(label.getText().charAt(0)) == null)) {
					text.setText(record.get(label.getText().charAt(0)) + "");
				}
			}
			
		}
		
		if (m.original.equals(m.decoded)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Message");
			alert.setContentText("You won!\n");
			alert.showAndWait();
			for (int i = 0; i < gridSize; i++) {
				VBox vbox = (VBox) grid.getChildren().get(i);
				TextField text = (TextField) vbox.getChildren().get(0);
				text.setDisable(true);
				
				
			}
		}
		
		
		
		
	}

}

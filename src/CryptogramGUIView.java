

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CryptogramGUIView extends Application implements Observer {
	
	public   List<Object> encodedGrid;
	public   List<Object> decodedGrid;
	public GridPane grid;
	
	@Override
	public void start(Stage stage) throws Exception {
		CryptogramModel m = new CryptogramModel();
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
		List<Character> decode = (List<Character>) arg;
		int gridSize = grid.getChildren().size();
		for (int i = 0; i < gridSize; i++) {
			VBox vbox = (VBox) grid.getChildren().get(i);
			TextField text = (TextField) vbox.getChildren().get(0);
			text.setText(decode.get(i) + "");
		}
		
		
	}

}

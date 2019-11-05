

import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CryptogramController {

	private CryptogramModel model;
	private CryptogramGUIView view;

	public CryptogramController(CryptogramGUIView view, CryptogramModel model){
		this.model = model;
		this.view = view;
	}

	public Button setStart(BorderPane p) {
		Button start = new Button("New Puzzle ");
		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					p.getScene().getWindow().hide();
					view.start(new Stage());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return start;
	}
	
	public Button setHint(BorderPane p) {
		Button hint = new Button("Hint");
		hint.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Map<Character, Character> record = 
						model.getHint(model.encryption, model.record, 
								model.encoded);
				model.decode(record, model.encoded, model.decoded);
			}
		});
		return hint;
	}
	

	private HBox getFreqBox(Map<Character, Integer> freq, char start, 
			char end) {
		VBox vbox1 = new VBox(); VBox vbox2 = new VBox();
		for (char i = start; i <= end; i++) {
			Label label1 = new Label(i + " ");
			vbox1.getChildren().add(label1);
			Label label2 = new Label(freq.get(i).toString());
			vbox2.getChildren().add(label2);
		}
		return new HBox(vbox1, vbox2);
	}
	
	public void setButtons(BorderPane p) {
		Button start = setStart(p); Button hint = setHint(p);
		CheckBox checkBox = new CheckBox("Show Freq");
		HBox hbox1 = getFreqBox(model.getFreq(model.encoded), 'A', 'M'); 
		HBox hbox2 = getFreqBox(model.getFreq(model.encoded), 'N', 'Z'); 
		HBox hbox = new HBox(hbox1, hbox2);
		hbox.setVisible(false); hbox.setSpacing(15);
		checkBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (checkBox.isSelected()) {
					hbox.setVisible(true);
				}else {
					hbox.setVisible(false);
				}		
			}
		});
		VBox buttons = new VBox(start, hint, checkBox, hbox);
		p.setRight(buttons);
	}

	public void setGrids(GridPane grid) {
		for (int i = 0; i < model.encodeInitial.size(); i++) {
			for (int j = 0; j < model.encodeInitial.get(i).trim().length(); j++) {
				TextField textField = new TextField(); 
				textField.setAlignment(Pos.CENTER);  
				textField.setPrefColumnCount(1);
				char cur = model.encodeInitial.get(i).charAt(j);
				String temp = String.valueOf(cur);
				Label label = new Label(temp);
				if (cur < 'A' || cur > 'Z') {
					textField.setText(temp);
					textField.setDisable(true);
				}
				textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
				    public void handle(KeyEvent ke) {
				    	if (textField.getLength() > 1) {
				    		textField.setText(ke.getText());
				    	}
				        System.out.println("Key Pressed: " + textField.getText().toUpperCase());// note
				        String command = cur + " = " + textField.getText().toUpperCase();
				        char current = ' ';
				        if (!ke.getText().isEmpty()) {
				        	current = ke.getText().toUpperCase().charAt(0);
				        }
				        if (!ke.getText().isEmpty() && current >= 'A' 
				        		&& current <= 'Z')
				        	model.process(command.toUpperCase());
				        
				        
				        
				    }
				});
				VBox vBox = new VBox();
				vBox.setAlignment(Pos.CENTER);
				vBox.getChildren().addAll(textField, label);
				grid.add(vBox, j, i);
				
			}
		}

	}





}

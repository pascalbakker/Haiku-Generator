import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JavaFX extends Application{
	public String source;
	HaikuGenerator hg;
	public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Haiku Code
		hg = new HaikuGenerator(new File("navyseal.txt"));
		Random r = new Random();

		
		//Javafx code
		BorderPane root = new BorderPane();
        Button btn = new Button("Generate Haiku");
        Text haikuText = new Text(150,400,"");
        haikuText.setFont(Font.font("Courrier New",20));
        //Generate new haiku every button press
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        		List<String> keys  = new ArrayList<String>(hg.markov.keySet());
        		String currentWord = keys.get( r.nextInt(keys.size()) );
                haikuText.setText(hg.generateHaiku());
            }
        });
        //Observable list
        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList("Going 100 West","Alice&Wonderland","Navy Seal","Trump");
        list.setItems(items);
        list.setPrefWidth(100);
        list.setPrefHeight(70);
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + list.getSelectionModel().getSelectedItem());
                String clickedon = list.getSelectionModel().getSelectedItem();
                try{
	                switch(clickedon){
		                case "Going 100 West": hg.setNewFile("100west.txt");
		                break;
		                case "Alice&Wonderland":hg.setNewFile("sample.txt");
		                break;
		                case "Navy Seal": hg.setNewFile("navyseal.txt");
		                break;
		                case "Trump": hg.setNewFile("trump.txt");
		                break;
		                default: System.out.println("Error changing values");
	                }
                }catch(IOException ioe){
                	System.out.println(ioe);
                	}
            }
        });
        //Add all elements to borderpane
        Pane newpane = new Pane();
        newpane.getChildren().add(list);
        BorderPane.setAlignment(list, Pos.BOTTOM_CENTER);
        root.setCenter(list);
        BorderPane.setAlignment(btn, Pos.TOP_CENTER);
        root.setTop(btn);
        BorderPane.setAlignment(haikuText, Pos.CENTER);
        root.setBottom(haikuText);
		
        primaryStage.setScene(new Scene(root, 500, 225));
        primaryStage.show();
	}
}

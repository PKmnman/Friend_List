package com.friend.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

public class MainMenu extends BorderPane {

	private static final URL FXML_FILE = MainMenu.class.getResource("com/friend/gui/fxml/MainMenu.fxml");

	private AddDialog addDialog;


	public MainMenu(){
		FXMLLoader loader = new FXMLLoader(FXML_FILE);
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.addDialog = new AddDialog(loader);
	}

	public void init(){

	}


}

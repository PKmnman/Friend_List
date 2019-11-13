package com.friend.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

class AddDialog extends VBox {

	private static final URL FXML_FILE = AddDialog.class.getResource("/com/friend/gui/fxml/AddDialog.fxml");

	AddDialog(){
		try {
			//Create a FXMLLoader and set "this" as it's root and controller
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/friend/gui/fxml/AddDialog.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			//Load the UI
			loader.load();
		} catch (IOException e) {
			//Handle any load exceptions and exit
			System.err.printf("[%s] <SEVERE> Failed to load FXML file \"%s\"%n", getClass().getSimpleName(), FXML_FILE.getFile());
			System.err.printf("[%s] <SEVERE> Caused by: \"%s\"%n", getClass().getSimpleName(), e.getCause());
			Platform.exit();
			System.exit(-1);
		}
	}



}

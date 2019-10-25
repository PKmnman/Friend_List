package com.friend.gui;

import com.friend.Friend;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.net.URL;

class AddDialog extends Dialog<Friend> {

	private static final URL FXML_FILE = AddDialog.class.getResource("com/friend/gui/fxml/AddDialog.fxml");

	private FXMLLoader loader;

	AddDialog(FXMLLoader loader){
		this.loader = loader;
	}

	void init(){
		loader.setLocation(FXML_FILE);
		loader.setController(this);

		try {
			this.setDialogPane(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}

		ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);

		this.getDialogPane().getButtonTypes().add(confirmButton);
	}



}

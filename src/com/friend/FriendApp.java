package com.friend;

import com.friend.gui.MainMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FriendApp extends Application {

	//This is the main method for the final program using the GUI
	//
	//DO NOT USE THIS MAIN METHOD TO TEST THE PROGRAM
	//
	//Use the main method of the com.friend.TestDriver class for testing
	public static void main(String[] args) {
		//DO NOT MODIFY
		Application.launch(FriendApp.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainMenu mainMenu = new MainMenu();
		mainMenu.init();

		Scene appScene = new Scene(mainMenu);

		primaryStage.setOnShowing(this::loadFriendList);
		primaryStage.setScene(appScene);
		primaryStage.sizeToScene();
		primaryStage.setMinWidth(640);
		primaryStage.setMinHeight(448);
		primaryStage.setResizable(true);
		primaryStage.show();
	}

	private void loadFriendList(WindowEvent e){
		//TODO: Load the list of friends from a file
	}
}

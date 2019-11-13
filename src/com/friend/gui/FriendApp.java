package com.friend.gui;

import com.friend.FriendFileHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class FriendApp extends Application {

	static File friendFile;
	static FriendFileHandler fileHandler;
	
	MainMenu mainMenu;
	
	//This is the main method for the final program using the GUI
	//
	//DO NOT USE THIS MAIN METHOD TO TEST THE PROGRAM
	//
	//Use the main method of the com.friend.TestDriver class for testing
	public static void main(String[] args) {
		initFriendData();
		
		//DO NOT MODIFY
		Application.launch(FriendApp.class, args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		mainMenu = new MainMenu();

		Scene appScene = new Scene(mainMenu);

		primaryStage.setTitle("Friend List");
		primaryStage.setOnShowing(this::loadFriendList);
		primaryStage.setScene(appScene);
		primaryStage.sizeToScene();
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * Loads the list of Friends from the program's storage file
	 * @param e the OnShowing event
	 */
	private void loadFriendList(WindowEvent e){
		if(e.getEventType().equals(WindowEvent.WINDOW_SHOWING)){
			mainMenu.loadRecords();
		}
	}
	
	private static void initFriendData() {
		URL friendDataFile = FriendApp.class.getResource("/files/testFile.dat");
		if(friendDataFile == null){
			friendFile = new File("/files/friends.bdf");
			if(friendFile.exists()){
				friendFile.delete();
			}
			try {
				friendFile.createNewFile();
			} catch (IOException e) {
				System.err.println("Failed to init friend data");
				System.exit(-1);
			}
		}else{
			try {
				friendFile = new File(friendDataFile.toURI());
			} catch (URISyntaxException e) {
				System.err.println("Failed to load friend data");
				System.exit(-1);
			}
		}
		
		try {
			fileHandler = new FriendFileHandler(friendFile.toPath().toString());
		} catch (IOException e) {
			System.err.println("Failed to initialize FriendFileHandler");
			System.exit(-1);
		}
	}
	
}

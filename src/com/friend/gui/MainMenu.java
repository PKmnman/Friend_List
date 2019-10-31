package com.friend.gui;

import com.friend.Friend;
import com.friend.PhoneNumber;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The {@code MainMenu} class serves as the JavaFX {@code Controller}
 * for the main gui of the {@code FriendApp} program.
 * <p>
 * {@code MainMenu} uses a {@link javafx.scene.control.TableView TableView} to list
 * and display the {@link com.friend.Friend} objects stored in a binary file
 */
public class MainMenu extends BorderPane {

	private static final URL FXML_FILE = MainMenu.class.getResource("/com/friend/gui/fxml/MainMenu.fxml");

	private AddDialog addDialog;

	@FXML private TableView<Friend> displayTable;
	@FXML private TableColumn<Friend, String> firstNameColumn;
	@FXML private TableColumn<Friend, String> lastNameColumn;
	@FXML private TableColumn<Friend, PhoneNumber> phoneNumberColumn;
	
	/**
	 * Default constructor for that constructs and loads the main menu UI.
	 * <p>
	 * This constructor should only be called within the {@link Application#start(Stage)} start(Stage)}
	 * method of any class extending the JavaFX {@link Application} or on
	 * the JavaFX Application thread.
	 */
	public MainMenu(){
		//Checks that this object is being created on the FX Application Thread
		if(!Platform.isFxApplicationThread()){
			//The program should terminate if this constructor is not called on
			//the FX Application Thread.
			System.err.printf("[%s] <ERROR> GUI objects must be created on the JavaFX Application Thread%n", getClass().getSimpleName());
			//End the FX Application first
			Platform.exit();
			System.exit(-1);
		}
		
		try {
			//Create a FXMLLoader and set "this" as it's root and controller
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/friend/gui/fxml/MainMenu.fxml"));
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
		
		//TODO: Load the list of friends
		
		//Setup the table display
		firstNameColumn.setCellValueFactory(new PropertyValueFactory("firstName"));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory("lastName"));
		
		//This is a test list
		ObservableList<Friend> friends = FXCollections.observableArrayList();
		friends.addAll(new Friend("Gary", "Reeves", "2037367606"), new Friend("Ted", "Test", "2036365421"));
		
		displayTable.setItems(friends);
		
		//TODO: Load the "Add/Edit Friend" dialog
		
		//this.addDialog = new AddDialog(loader);
		//this.addDialog.init();
	}


}

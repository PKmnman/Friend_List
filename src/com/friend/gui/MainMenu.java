package com.friend.gui;

import com.friend.Block;
import com.friend.Friend;
import com.friend.PhoneNumber;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * The {@code MainMenu} class serves as the JavaFX {@code Controller}
 * for the main gui of the {@code FriendApp} program. The majority of the
 * UI is defined in the "MainMenu.fxml" file packaged with the program.
 * <p>
 * {@code MainMenu} uses a {@link javafx.scene.control.TableView TableView} to list
 * and display the {@link com.friend.Friend} objects stored in a binary file that is
 * structured appropriately.
 */
public class MainMenu extends BorderPane {

	//This is the location of the FXML file, always constant
	private static final URL FXML_FILE = MainMenu.class.getResource("/com/friend/gui/fxml/MainMenu.fxml");

	private AddDialog addDialog;

	//Fields defined in FXML
	
	@FXML private TableView<Friend> displayTable;
	@FXML private TableColumn<Friend, String> firstNameColumn;
	@FXML private TableColumn<Friend, String> lastNameColumn;
	@FXML private TableColumn<Friend, PhoneNumber> phoneNumberColumn;
	
	@FXML private ContextMenu tableContextMenu;
	@FXML private ListProperty<Friend> friends;
	
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
			System.err.println();
			e.printStackTrace();
			Platform.exit();
			System.exit(-1);
		}
		
		
		//This adds the context menu to each row
		displayTable.setRowFactory(p -> {
			TableRow<Friend> row = new TableRow<>();
			row.setContextMenu(tableContextMenu);
			return row;
		});
		
		loadRecords();
		displayTable.itemsProperty().bind(friends);
		
		//This listener will update the friend file whenever a change is made
		friends.addListener((ListChangeListener<Friend>) c -> {
			while(c.next()){
				if(c.wasRemoved()){
					FriendApp.fileHandler.deleteFriend(c.getRemoved().get(0));
				}
			}
		});
		
		displayTable.refresh();
		//TODO: Load the "Add/Edit Friend" dialog
	}
	
	/**
	 * Performs delete operation on friend object. Linked in fxml.
	 * @param e the event fired
	 */
	public void onDeleteAction(ActionEvent e){
		if(!friends.isEmpty()){
			Friend f = displayTable.getSelectionModel().getSelectedItem();
			System.out.println("Deleting " + f.getFirstName() + " " + f.getLastName());
			friends.remove(f);
			
			displayTable.refresh();
		}
	}

	public void onAddAction(ActionEvent event){
		AddDialog dialog = new AddDialog(this);
		dialog.showAndWait();
		
		//Only add if the friend being added is valid
		if(dialog.isComplete()){
			Friend f = new Friend(dialog.getFirstName(), dialog.getLastName(), dialog.getPhoneNumber());
			FriendApp.fileHandler.addFriend(f);
			friends.add(f);
		}
		
	}
	
	private ObservableList<Friend> loadRecords() {
		friends = new SimpleListProperty<>(this, "friends", FXCollections.observableArrayList());
		
		Block b = new Block();
		while (b != null) {
			b = FriendApp.fileHandler.read();
			
			if (b == null || b.isDeleted()) {
				break;
			}
			
			friends.add(b.getData());
		}
		
		return friends;
	}
	
	public ObservableList<Friend> getFriends(){
		return friends;
	}
	
}

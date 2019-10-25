package com.friend.gui;

import com.friend.Friend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;

public class MainMenu extends BorderPane {

	private static final URL FXML_FILE = MainMenu.class.getResource("/com/friend/gui/fxml/MainMenu.fxml");

	private AddDialog addDialog;

	@FXML private TableView<Friend> displayTable;
	@FXML private TableColumn<Friend, String> firstNameColumn;
	@FXML private TableColumn<Friend, String> lastNameColumn;

	public MainMenu(){
		if(FXML_FILE == null){
			System.err.printf("[%s] FXML File is null", this.getClass().getSimpleName());
			System.exit(-1);
		}
	}

	public void init(){
		FXMLLoader loader = new FXMLLoader(FXML_FILE);
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		firstNameColumn.setCellValueFactory(new PropertyValueFactory("firstName"));

		lastNameColumn.setCellValueFactory(new PropertyValueFactory("lastName"));

		ObservableList<Friend> friends = FXCollections.observableArrayList();
		friends.addAll(new Friend("Gary", "Reeves", "2037367606"), new Friend("Ted", "Test", "2036365421"));

		displayTable.setItems(friends);

		this.addDialog = new AddDialog(loader);
		this.addDialog.init();
	}


}

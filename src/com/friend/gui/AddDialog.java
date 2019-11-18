package com.friend.gui;

import com.friend.PhoneNumber;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

class AddDialog extends VBox {

	private static final URL FXML_FILE = AddDialog.class.getResource("/com/friend/gui/fxml/AddDialog.fxml");
	
	/**
	 * Describes the pattern in which phone numbers should be input. This is meant to be used in
	 * the validation of phone numbers.
	 * @implNote  This pattern will match any {@code String} formatted as "###-###-####" or
	 * "(###)-###-####".
	 */
	private static final Pattern NUM_PATTERN = Pattern.compile("((\\(\\d{3}\\))|(\\d{3}))-\\d{3}-\\d{4}");
	
	@FXML private TextField firstNameField;
	@FXML private TextField lastNameField;
	@FXML private TextField phoneNumberField;
	
	private ObjectProperty<MainMenu> parentMenu;
	
	private BooleanProperty complete;
	
	private Stage stage;
	
	AddDialog(MainMenu parent){
		parentMenu = new SimpleObjectProperty<>(this, "parent menu", parent);
		
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
		
		Scene owner  = new Scene(this);
		
		stage = new Stage();
		stage.setScene(owner);
		stage.setTitle("Add Friend...");
		stage.setResizable(false);
		stage.initOwner(parent.getScene().getWindow());
		stage.initModality(Modality.APPLICATION_MODAL);
		
		complete = new SimpleBooleanProperty(this, "complete");
		
		complete.bind(firstNameField.textProperty().isNotEmpty().and(lastNameField.textProperty().isNotEmpty()).and(phoneNumberField.textProperty().isNotEmpty()));
		
		TextFormatter<PhoneNumber> formatter = new TextFormatter<PhoneNumber>(
				new StringConverter<PhoneNumber>() {
					@Override
					public String toString(PhoneNumber object) {
						return object.toString();
					}
					
					@Override
					public PhoneNumber fromString(String string) {
						return new PhoneNumber(string);
					}
				},
				null
		);
		
		phoneNumberField.setTextFormatter(formatter);
	}
	
	public void showAndWait(){
		stage.showAndWait();
		
	}
	
	@FXML private void onCancel(ActionEvent e){
		complete.setValue(false);
		stage.hide();
	}
	
	@FXML private void onConfirm(ActionEvent e){
		if(isComplete()){
			stage.hide();
		}
	}
	
	public boolean isComplete() {
		return this.complete.getValue();
	}
	
	public String getFirstName(){
		return firstNameField.textProperty().getValueSafe();
	}
	
	public String getLastName(){
		return lastNameField.textProperty().getValueSafe();
	}
	
	public PhoneNumber getPhoneNumber(){
		return ((TextFormatter<PhoneNumber>) phoneNumberField.getTextFormatter()).getValue();
	}

}

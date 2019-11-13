package com.friend.gui;

import com.friend.Friend;
import com.friend.PhoneNumber;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
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
	
	private Friend newFriend;
	
	@FXML private TextField firstNameField;
	@FXML private TextField lastNameField;
	@FXML private TextField phoneNumberField;
	
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
				new PhoneNumber("0000000000")
		);
	}
	
	private TextFormatter.Change filterInput(TextFormatter.Change change){
		if (!change.isContentChange() && !change.getControlNewText().isEmpty()) {
			return change;
		}
		
		String text = change.getControlNewText();
		int start = change.getRangeStart();
		int end = change.getRangeEnd();
		int anchor = change.getAnchor();
		
		StringBuffer newText = new StringBuffer(text);
		
		
		
		return change;
	}



}

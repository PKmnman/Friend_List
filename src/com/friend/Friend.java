package com.friend;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Friend {

	private static final String DEFAULT_NAME = "NIL";
	private static final PhoneNumber DEFAULT_NUMBER = new PhoneNumber("(000)-000-0000");
	private static final int MAX_NAME_LENGTH = 15;

	public static final int BYTES = ((Character.BYTES * MAX_NAME_LENGTH) * 2) + PhoneNumber.BYTES;

	private String lastName, firstName;
	private PhoneNumber phoneNumber;

	public Friend(String firstName, String lastName, PhoneNumber phoneNumber){
		this.firstName = firstName.trim();
		this.lastName = lastName.trim();
		this.phoneNumber = phoneNumber;
	}

	public Friend(String firstName, String lastName, String phoneNumber){
		this(firstName, lastName, new PhoneNumber(phoneNumber));
	}

	public Friend(){
		this(DEFAULT_NAME, DEFAULT_NAME, DEFAULT_NUMBER);
	}

	/**
	 * Reads and returns a new {@link Friend} object from a {@link RandomAccessFile}.
	 * @param file the file to read from
	 * @return the friend object contained in the {@code RandomAccessFile}
	 * @throws IOException
	 */
	public void read(RandomAccessFile file) throws IOException{
		char[] lastName = new char[MAX_NAME_LENGTH];
		char[] firstName = new char[MAX_NAME_LENGTH];
		
		//Read and store the first name
		for(int i = 0; i < MAX_NAME_LENGTH; i++){
			firstName[i] = file.readChar();
		}
		this.firstName = new String(firstName, 0, MAX_NAME_LENGTH).trim();
		
		//Read and store the last name
		for(int i = 0; i < MAX_NAME_LENGTH; i++){
			lastName[i] = file.readChar();
		}
		this.lastName = new String(lastName, 0, MAX_NAME_LENGTH).trim();

		this.phoneNumber = PhoneNumber.read(file);
	}

	public void write(RandomAccessFile file) throws IOException{
		//Write first name
		if (firstName != null){
			for (int i = 0; i < MAX_NAME_LENGTH; i++) {
				if(i < firstName.length()){
					file.writeChar(firstName.charAt(i));
				}else{
					file.writeChar(' ');
				}
			}
		}else {
			for (int i = 0; i < MAX_NAME_LENGTH; i++) {
				file.writeChar(' ');
			}
		}
		
		//Write last name
		if(lastName != null){
			for (int i = 0; i < MAX_NAME_LENGTH; i++) {
				if(i < lastName.length()){
					file.writeChar(lastName.charAt(i));
				}else{
					file.writeChar(' ');
				}
			}
		}else {
			for (int i = 0; i < MAX_NAME_LENGTH; i++) {
				file.writeChar(' ');
			}
		}
		
		//Write phone number
		phoneNumber.write(file);
	}

	public int size(){
		return BYTES;
	}

	public String getFirstName(){
		return this.firstName;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getLastName(){
		return this.lastName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public PhoneNumber getPhoneNumber(){
		return this.phoneNumber;
	}

	public void setPhoneNumber (PhoneNumber number){
		this.phoneNumber = number;
	}

	public String toString(){
		return String.format("Name: %s %s %s", firstName, lastName, phoneNumber);
	}

	public boolean equals(Object o){
		if(o instanceof Friend){
			Friend f = (Friend)o;
			return (f.getFirstName().equalsIgnoreCase(this.firstName) && f.getLastName().equalsIgnoreCase(this.lastName) && f.getPhoneNumber().equals(this.phoneNumber));
		}
		return false;
	}

	public boolean compareNames(String nameFirst, String nameLast){
		return (this.firstName.trim().equalsIgnoreCase(nameFirst) && this.lastName.trim().equalsIgnoreCase(nameLast));
	}
}

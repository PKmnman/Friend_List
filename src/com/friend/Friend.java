package com.friend;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Friend {

	private static final String DEFAULT_NAME = "NIL";
	private static final PhoneNumber DEFAULT_NUMBER = new PhoneNumber("(000)-000-0000");
	private static final int MAX_NAME_LENGTH = 15;

	private static final int BYTE_SIZE = 30 + PhoneNumber.NUMBER_SIZE;

	private String lastName, firstName;
	private PhoneNumber phoneNumber;

	public Friend(){
		lastName = DEFAULT_NAME;
		firstName = DEFAULT_NAME;
		phoneNumber = new PhoneNumber("2031001000");
	}

	public Friend(String firstName, String lastName, PhoneNumber phoneNumber){
		this.lastName = lastName;
		this.firstName = firstName;
		this.phoneNumber = phoneNumber;
	}

	public Friend(String firstName, String lastName, String phoneNumber){
		this(firstName, lastName, new PhoneNumber(phoneNumber));
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

	/**
	 * Reads and returns a new {@link Friend} object from a {@link RandomAccessFile}.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Friend read(RandomAccessFile file) throws IOException {
		char[] lastName = new char[15];
		char[] firstName = new char[15];

		for (int i = 0; i < lastName.length; i++) {
			lastName[i] = file.readChar();
		}

		for (int i = 0; i < firstName.length; i++) {
			firstName[i] = file.readChar();
		}

		PhoneNumber phone= PhoneNumber.read(file);

		String first = new String(firstName);
		String last = new String(lastName);

		return new Friend(first, last, phone);
	}

	public void write(RandomAccessFile file){

	}

	public static int size(){
		return BYTE_SIZE;
	}

}

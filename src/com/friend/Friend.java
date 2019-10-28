package com.friend;

import com.sun.javafx.binding.StringFormatter;
import com.sun.xml.internal.ws.util.StringUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Formattable;
import java.util.Formatter;
import java.util.Locale;

public class Friend {

	private static final String DEFAULT_NAME = "NIL";
	private static final PhoneNumber DEFAULT_NUMBER = new PhoneNumber("(000)-000-0000");
	private static final int MAX_NAME_LENGTH = 15;

	public static final int BYTES = ((Character.BYTES * MAX_NAME_LENGTH) * 2) + PhoneNumber.BYTES;

	private String lastName, firstName;
	private PhoneNumber phoneNumber;

	public Friend(String firstName, String lastName, PhoneNumber phoneNumber){
		StringBuffer buff = new StringBuffer(firstName);
		buff.setLength(15);

		this.firstName = buff.toString();

		buff = new StringBuffer(lastName);
		buff.setLength(15);

		this.lastName = buff.toString();

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
	public static Friend readObject(RandomAccessFile file) throws IOException {
		byte[] lastName = new byte[Character.BYTES * 15];
		byte[] firstName = new byte[Character.BYTES * 15];

		file.read(firstName);
		String first = new String(firstName, 0, firstName.length, StandardCharsets.US_ASCII);

		file.read(lastName);
		String last = new String(lastName, 0, lastName.length, StandardCharsets.US_ASCII);

		PhoneNumber phone = PhoneNumber.read(file);

		return new Friend(first, last, phone);
	}

	public void read(RandomAccessFile file) throws IOException{
		byte[] lastName = new byte[Character.BYTES * 15];
		byte[] firstName = new byte[Character.BYTES * 15];

		file.read(firstName);
		this.firstName = new String(firstName);

		file.read(lastName);
		this.lastName = new String(lastName);

		this.phoneNumber = PhoneNumber.read(file);
	}

	public void write(RandomAccessFile file) throws IOException{
		//TODO: Implement write functionality

		StringBuffer buf;
		if (firstName != null){
			buf = new StringBuffer(this.firstName);
		}
		else {
			buf = new StringBuffer(MAX_NAME_LENGTH);
		}

		buf.setLength(MAX_NAME_LENGTH);
		file.writeChars(buf.toString());

		if (lastName != null){
			buf = new StringBuffer(this.lastName);
		}
		else {
			buf = new StringBuffer(MAX_NAME_LENGTH);
		}

		buf.setLength(MAX_NAME_LENGTH);
		file.writeChars(buf.toString());

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
		return String.format("Name: %-16s%-16s%s", firstName, lastName, phoneNumber);
	}

}

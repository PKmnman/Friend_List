package com.friend;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * The {@code com.friend.PhoneNumber} class represents a standard, American 10-digit telephone number and
 * stores it as an array of bytes.
 */
public class PhoneNumber {

	/**
	 * This is the size of all {@link PhoneNumber} objects in bytes.
	 */
	public static final int BYTES = 5;

	private static final int BIT_MASK = 0x000F;
	private static final short DIGIT_SIZE = 4;
	private static final short ARRAY_SIZE = 5;

	private static final int MAX_LENGTH = 10;

	private final byte[] DIGITS;

	public PhoneNumber(String phoneNumber){
		DIGITS = new byte[ARRAY_SIZE];
		encode(phoneNumber);
	}

	private PhoneNumber(byte[] digits){
		DIGITS = Arrays.copyOfRange(digits, 0, 10);
	}

	public PhoneNumber(){
		DIGITS = new byte[ARRAY_SIZE];
	}

	/**
	 * Converts a {@code String} object into a series of hexadecimal bytes representing each digit of
	 * this {@code com.friend.PhoneNumber} and stores them to the underlying byte array.
	 *
	 * @implNote This method should only be called once within the constructor of the {@link PhoneNumber} class
	 *
	 * @param phoneNumber the {@code String} to be encoded
	 */
	private void encode(String phoneNumber){
		char[] number = phoneNumber.replaceAll("\\D+", "").toCharArray();

		if(number.length > MAX_LENGTH){
			throw new IllegalArgumentException("Phone number length must no exceed 10 digits");
		}

		for (int i = 0; i < number.length; i += 2) {
			byte d1 = (byte)Character.getNumericValue(number[i]);
			byte d2 = (byte)Character.getNumericValue(number[i + 1]);

			DIGITS[i/2] = (byte)((d1 << DIGIT_SIZE) | d2);
		}
	}

	private int getDigit(int index){
		if(index % 2 != 0){
			return DIGITS[index / 2] & BIT_MASK;
		}else{
			return (DIGITS[index / 2] >> DIGIT_SIZE) & BIT_MASK;
		}
	}

	public void write(RandomAccessFile file) throws IOException {
		file.write(DIGITS);
	}

	public static PhoneNumber read(RandomAccessFile file) throws IOException{
		byte[] digits = new byte[BYTES];
		file.read(digits);
		return new PhoneNumber(digits);
	}

	public String toString(){
		StringBuffer buff = new StringBuffer();

		for (int i = 0; i < MAX_LENGTH; i++) {
			buff.append(getDigit(i));
		}

		buff.insert(0, '(').insert(4, ")-").insert(9,'-');

		return buff.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PhoneNumber){
			for (int i = 0; i < ((PhoneNumber) obj).DIGITS.length; i++){
				if(this.DIGITS[i] != ((PhoneNumber) obj).DIGITS[i]){
					return false;
				}
				return true;
			}
		}
		return false;
	}
	
}

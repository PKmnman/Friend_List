package com.friend;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class TestDriver {

	public static File TEST_FILE;

	static{
		try {
			TEST_FILE = new File(TestDriver.class.getResource("/files/testFile.dat").toURI());
		} catch (URISyntaxException e) {
			System.err.println("Error loading file");
			System.exit(-1);
		}
	}

	public static void main(String[] args) throws IOException{

		PhoneNumber myNumber = new PhoneNumber("(203)-736-7606");

		RandomAccessFile file = new RandomAccessFile(TEST_FILE, "rw");;

		//Testing writing multiple blocks
		Friend[] friends = new Friend[]{
				new Friend("Gary", "Reeves", myNumber),
				new Friend(),
				new Friend("John", "Doe", "2037772424"),
				new Friend("Blah", "Blah", "7245556983")
		};
		
		file.writeLong(-1L);
		file.writeLong(16L);
		
		System.out.printf("Writing %d Blocks to \"%s\"...%n", 100, TEST_FILE);
		pause(2000);
		
		Friend f = new Friend();
		Block b = new Block(f, -1, Block.BYTES);
		
		long prev = file.getFilePointer();
		
		//Initialize the blocks
		for (int i = 0; i < 100; i++) {
			b.writeObject(file);
			b.setPrev(prev);
			if(i < (100 - 1)){
				b.setNext(file.getFilePointer() + Block.BYTES);
			}else{
				b.setNext(-1);
			}
			prev += Block.BYTES;
		}

		

		System.out.println("Blocks written to file!\n");
		
		//Close the file
		file.close();

		file = new RandomAccessFile(TEST_FILE, "r");

		System.out.printf("Reading \"%s\"...%n%n", TEST_FILE);
		pause(1500);
		read(file);

		file.close();

		System.out.println("\nDone!!!");

	}

	public static void read(RandomAccessFile file) throws IOException{
		long a = file.readLong();
		System.out.printf("[Offset = %#010x]: %#010x%n", file.getFilePointer(), a);
		long b = file.readLong();
		System.out.printf("[Offset = %#010x]: %#010x%n", file.getFilePointer(), a);
		System.out.println("--------------------------------------------------------------------------------------------------");
		Block block = new Block();
		
		boolean eof = false;
		
		while(!eof){
			try{
				long loc = file.getFilePointer();
				block.readObject(file);
				
				System.out.printf("OFFSET = %d%n", loc);
				System.out.printf("%s%n", block.getData());
				System.out.printf("Prev = %-5d Next = %-5d%n", block.getPrev(), block.getNext());
				System.out.println("------------------------------------------");
				
			}catch (IOException e){
				eof = true;
			}
		}
	}

	public static void pause(long milli){
		try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

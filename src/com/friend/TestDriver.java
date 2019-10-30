package com.friend;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

public class TestDriver {

	public static File TEST_FILE;
	
	private static final int NUM_OF_FRIENDS = 10;

	static{
		try {
			TEST_FILE = new File(TestDriver.class.getResource("/files/testFile.dat").toURI());
		} catch (URISyntaxException e) {
			System.err.println("Error loading file");
			System.exit(-1);
		}
	}

	public static void main(String[] args) throws IOException{

		File output = new File("C:/Users/ksrot/Dropbox/Java Programs/Friend_List/output.txt");
		
		if(!output.createNewFile()){
			output.delete();
			output.createNewFile();
		}
		
		OutputStream fOut = Files.newOutputStream(output.toPath(), StandardOpenOption.WRITE);
		
		System.setOut(new PrintStream(fOut));
		
		RandomAccessFile file = new RandomAccessFile(TEST_FILE, "rw");;
		
		file.writeLong(-1L);
		file.writeLong(16L);
		
		System.out.printf("Writing %d Blocks to \"%s\"...%n", 100, TEST_FILE);
		pause(2000);
		
		Friend f = new Friend();
		Block b = new Block(f, -1, Block.BYTES);
		
		long prev = file.getFilePointer();
		
		//Initialize the blocks
		for (int i = 0; i < NUM_OF_FRIENDS; i++) {
			b.writeObject(file);
			b.setPrev(prev);
			if(i < (NUM_OF_FRIENDS - 1)){
				b.setNext(file.getFilePointer() + Block.BYTES);
			}else{
				b.setNext(-1);
			}
			prev += Block.BYTES;
		}

		

		System.out.println("Blocks written to file!\n");
		
		//Close the file
		file.close();

		file = new RandomAccessFile(TEST_FILE, "rw");

		System.out.printf("Reading \"%s\"...%n%n", TEST_FILE);
		pause(1500);
		read(file);

		file.seek(0);
		
		System.out.println("\nAdding new friend\n");
		
		addFriend(file, new Friend("Gary", "Reeves", "2037367606"));
		addFriend(file, new Friend("Jayne", "Doe", "2031122200"));
		
		
		file.close();
		
		
		file = new RandomAccessFile(TEST_FILE, "r");
		read(file);
		
		System.out.println("\nDone!!!");

	}

	public static void read(RandomAccessFile file) throws IOException{
		long loc = file.getFilePointer();
		long a = file.readLong();
		System.out.printf("[Offset = %#010x]: %#010x%n", loc, a);
		loc = file.getFilePointer();
		long b = file.readLong();
		System.out.printf("[Offset = %#010x]: %#010x%n", loc, b);
		System.out.println("--------------------------------------------------------------------------------------------------");
		Block block = new Block();
		
		boolean eof = false;
		
		while(!eof){
			try{
				loc = file.getFilePointer();
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
	
	public static void addFriend(RandomAccessFile file, Friend friend){
		
		try {
			file.seek(8);
			long open = file.readLong();
			
			file.seek(open);
			
			Block b = new Block();
			b.readObject(file);
			
			b.setData(friend);
			
			long newOpen = file.getFilePointer();
			
			file.seek(0);
			file.writeLong(open);
			file.writeLong(newOpen);
			
			file.seek(open);
			
			b.writeObject(file);
		} catch (IOException e) {
			e.printStackTrace();
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

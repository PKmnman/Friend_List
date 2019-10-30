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

		File output = new File("C:/Users/heric/IdeaProjects/Friend_List/output.txt");
		
		if(!output.createNewFile()){
			output.delete();
			output.createNewFile();
		}
		
		OutputStream fOut = Files.newOutputStream(output.toPath(), StandardOpenOption.WRITE);
		
		//System.setOut(new PrintStream(fOut));
		
		RandomAccessFile file = new RandomAccessFile(TEST_FILE, "rw");;
		
		file.writeLong(-1L);
		file.writeLong(16L);
		
		createFile(file);

		System.out.println("Blocks written to file!\n");
		
		//Close the file
		file.close();

		//Re-open File
		file = new RandomAccessFile(TEST_FILE, "rw");
		
		//Read and print to std::out
		read(file);
		//Seek to beginning
		file.seek(0);
		
		System.out.println("\nAdding new friend\n");
		
		addFriend(file, new Friend("Gary", "Reeves", "2037367606"));
		addFriend(file, new Friend("Jayne", "Doe", "2031122200"));
		addFriend(file, new Friend("Ricky", "He", "2153596726"));
		
		file.close();
		
		
		file = new RandomAccessFile(TEST_FILE, "r");
		read(file);
		
		System.out.println("\nDone!!!");

	}
	
	private static void createFile(RandomAccessFile file) throws IOException {
		System.out.printf("Writing %d Blocks to \"%s\"...%n", NUM_OF_FRIENDS, TEST_FILE);
		Friend f = new Friend();
		Block b = new Block(f, -1, Block.BYTES + 16);
		
		long prev = file.getFilePointer();
		
		b.writeObject(file);
		
		//Write The blocks to the file
		for (int i = 0; i < NUM_OF_FRIENDS - 1; i++) {
			b.setPrev(prev);
			b.setNext(file.getFilePointer() + Block.BYTES);
			b.writeObject(file);
			prev = file.getFilePointer();
		}
		
		b.setNext(-1);
		b.setPrev(prev);
		b.writeObject(file);
	}
	
	public static void read(RandomAccessFile file) throws IOException{
		System.out.printf("Reading \"%s\"...%n%n", TEST_FILE);
		long loc = file.getFilePointer();
		long a = file.readLong();
		System.out.printf("[Offset = %d]: %d%n", loc, a);
		loc = file.getFilePointer();
		long b = file.readLong();
		System.out.printf("[Offset = %d]: %d%n", loc, b);
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

	public static void printFile(RandomAccessFile file) {
		long v;
		try {
			v = file.readLong();
			System.out.println(v);
			v = file.readLong();
			System.out.println(v);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			while (true) {
				long loc = file.getFilePointer();
				Block b = new Block();
				b.readObject(file);
				System.out.printf("OFFSET %d %n", loc);
				System.out.printf("%s %n", b.getData());
				System.out.printf("PREV = %d \t NEXT = %d %n", b.getPrev(), b.getNext());
			}
		} catch (EOFException eof){
			System.err.println(eof.getMessage());
		}catch (IOException ioe){
			System.err.println(ioe.getMessage());
		}

	}

}

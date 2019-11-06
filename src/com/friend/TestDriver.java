package com.friend;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class TestDriver {

	public static final URL TEST_FILE = TestDriver.class.getResource("/files/testFile.dat");
	
	private static final int NUM_OF_FRIENDS = 10;
	
	public static void main(String[] args) throws IOException{
		//Open binary file for read/write operations
		File test = null;
		try {
			test = new File(TEST_FILE.toURI());
		} catch (URISyntaxException e) {
			System.err.println("Failed to load file");
			System.exit(-1);
		}
		
		RandomAccessFile file = new RandomAccessFile(test, "rw");
			
		//Create the file and populate it with blocks
		createFile(file);
		System.out.println("Blocks written to file!\n");
		
		//Close the file
		file.close();

		//Re-open File
		file = new RandomAccessFile(test, "rw");
		
		//Read and print to std::out
		read(file);
		//Seek to beginning
		file.seek(0);
		
		System.out.println("\nAdding new friend\n");
		
		//Add friends
		addFriend(file, new Friend("Gary", "Reeves", "2037367606"));
		addFriend(file, new Friend("Jayne", "Doe", "2031122200"));
		addFriend(file, new Friend("Ricky", "He", "2153596726"));
		
		//Close the file
		file.close();
		
		//Re-open the file for reading
		file = new RandomAccessFile(test, "r");
		read(file);
		
		System.out.println("\nDone!!!");

	}
	
	private static void setFileOut(File output) throws IOException {
		if(!output.createNewFile()){
			output.delete();
			output.createNewFile();
		}
		
		OutputStream fOut = Files.newOutputStream(output.toPath(), StandardOpenOption.WRITE);
		
		System.setOut(new PrintStream(fOut));
	}
	
	private static void createFile(RandomAccessFile file) throws IOException {
		file.writeLong(-1L);
		file.writeLong(16L);
		
		System.out.printf("Writing %d Blocks to \"%s\"...%n", NUM_OF_FRIENDS, TEST_FILE);
		Friend f = new Friend();
		Block b = new Block(f, -1, Block.BYTES + 16);
		
		long prev = file.getFilePointer();
		
		b.writeObject(file);
		
		//Write The blocks to the file
		for (int i = 0; i < NUM_OF_FRIENDS - 1; i++) {
			b.setPrev(prev);
			b.setNext(file.getFilePointer() + Block.BYTES);
			prev = file.getFilePointer();
			b.writeObject(file);
		}
		
		b.setNext(-1);
		b.setPrev(prev);
		b.writeObject(file);
	}
	
	public static void read(RandomAccessFile file) throws IOException{
		System.out.printf("Reading \"%s\"...%n%n", TEST_FILE);
		
		long loc = file.getFilePointer();
		//Print the DP and FP as well as their offsets
		long dp = file.readLong();
		System.out.printf("[Offset = %d]: %d%n", loc, dp);
		loc = file.getFilePointer();
		long fp = file.readLong();
		System.out.printf("[Offset = %d]: %d%n", loc, fp);
		System.out.println("--------------------------------------------------------------------------------------------------");
		
		//Init variables for reading
		Block block = new Block();
		boolean eof = false;
		
		//Read every block in the file
		while(!eof){
			try{
				loc = file.getFilePointer();
				block.readObject(file);
				//Print statements (Maybe should make into a method)
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
			//Seek to FP
			file.seek(8);
			long open = file.readLong();
			//Seek to next free block
			file.seek(open);
			
			Block b = new Block();
			//Read the block
			b.readObject(file);
			//Set the data of the block
			b.setData(friend);
			//Store the free pointer to the next block
			//TODO: Should change this to a search for the next free block
			long newOpen = file.getFilePointer();
			
			//Write Block
			file.seek(open);
			b.writeObject(file);
			
			//Update DP and FP
			file.seek(8);
			//TODO: DP shouldn't necessarily change on every insert
			file.writeLong(newOpen);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public  static void deleteRecord(){
	
	}
	public static void deleteFriend(RandomAccessFile file, String firstName, String lastName){
		try{
			file.seek(16);
			Block b = new Block();
			Friend f = new Friend();
			while(true){
			    ///If never found then while loop goes forever
				long loc = file.getFilePointer();
				b.readObject(file);
				f = b.getData();
				if (f.compareNames(firstName,lastName)){
					long prev = b.getPrev();
					//Previous block location
					long next = b.getNext();
					//Next block location
					long curr = loc;
					//Current block location


					file.seek(prev);
					//Go back to prev block to change next location
					Block bPrev = new Block();
					bPrev.readObject(file);
					bPrev.setNext(next);
					file.seek(prev);
					bPrev.writeObject(file);

					file.seek(next);
					//Go to next block to change prev location
					Block bNext = new Block();
					bNext.readObject(file);
					bNext.setPrev(prev);
					file.seek(next);
					bNext.writeObject(file);

					file.seek(8);
					//Change FP to current block
					file.writeLong(curr);

				}

			}
		}catch (EOFException e){
			e.printStackTrace();
		} catch (IOException e) {
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

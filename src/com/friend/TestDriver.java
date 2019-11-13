package com.friend;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

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
		
		FriendFileHandler file = new FriendFileHandler(test.toPath().toString());
			
		//Create the file and populate it with blocks
		//createFile(file);
		System.out.println("Blocks written to file!\n");
		
		//Close the file
		file.close();

		//Re-open File
		file = new FriendFileHandler(test.toPath().toString());
		
		//Read and print to std::out
		//read(file);
		//Seek to beginning
		
		
		System.out.println("\nAdding new friend\n");
		Friend f = new Friend("Jayne", "Doe", "2031122200");
		
		//Add friends
		file.addFriend(new Friend("Gary", "Reeves", "2037367606"));
		file.addFriend(f);
		file.addFriend(new Friend("Ricky", "He", "2153596726"));

		file.deleteFriend(f);


		//Close the file
		file.close();
		
		//Re-open the file for reading
		file = new FriendFileHandler(test.toPath().toString());
		//read(file);
		
		System.out.println("\nDone!!!");

	}
	
	private static void createFile(RandomAccessFile file) throws IOException {
		file.writeLong(-1L);
		file.writeLong(16L);
		
		System.out.printf("Writing %d Blocks to \"%s\"...%n", NUM_OF_FRIENDS, TEST_FILE);
		Friend f = new Friend();
		Block b = new Block(f, -1, Block.BYTES + 16);
		
		long prev = file.getFilePointer();
		
		b.write(file);
		
		//Write The blocks to the file
		for (int i = 0; i < NUM_OF_FRIENDS - 1; i++) {
			b.setPrev(prev);
			b.setNext(file.getFilePointer() + Block.BYTES);
			prev = file.getFilePointer();
			b.write(file);
		}
		
		b.setNext(-1);
		b.setPrev(prev);
		b.write(file);
	}
	
	public static void read(FriendFileHandler file) throws IOException{
		System.out.printf("Reading \"%s\"...%n%n", TEST_FILE);
		
		
		//Print the DP and FP as well as their offsets
		
		System.out.printf("[Offset = %d]: %d%n", 0, file.getDataPointer());
		System.out.printf("[Offset = %d]: %d%n", 8, file.getFreePointer());
		System.out.println("--------------------------------------------------------------------------------------------------");
		
		//Init variables for reading
		Block block = new Block();
		
		//Read every block in the file
		while(block != null){
			long loc = file.getFilePointer();
			block = file.read();
			//Print statements (Maybe should make into a method)
			System.out.printf("OFFSET = %d%n", loc);
			System.out.printf("%s%n", block.getData());
			System.out.printf("Prev = %-5d Next = %-5d%n", block.getPrev(), block.getNext());
			System.out.println("------------------------------------------");
		}
	}
	

	public static long searchNextFree(RandomAccessFile file){
		try{
			file.seek(16);
			Block b = new Block();
			long loc;
			while (true){
				loc = file.getFilePointer();
				b.read(file);
				if (b.isDeleted()){
					return loc;
				}
			}
		}catch (IOException e) {

		}
		return -1;
	}
}

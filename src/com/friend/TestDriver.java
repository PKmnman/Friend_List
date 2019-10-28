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

		ArrayList<Block> blocks = new ArrayList<>(4);

		//Initialize the blocks
		for (long i = 0; i < friends.length * Block.BYTES; i += Block.BYTES) {
			Block b = new Block(friends[(int)(i / Block.BYTES)]);
			if(i == 0L){
				b.setPrev(0L);
			}else{
				b.setPrev(i - Block.BYTES);
			}

			if(i == ((friends.length - 1) * Block.BYTES)){
				b.setNext(0x0000000000000000L);
			}else{
				b.setNext(i + Block.BYTES + 1);
			}

			blocks.add(b);
		}

		System.out.printf("Writing %d Blocks to \"%s\"...%n", blocks.size(), TEST_FILE);
		pause(2000);

		//Write blocks to file
		for (int i = 0; i < blocks.size(); i++) {
			blocks.get(i).writeObject(file);
		}

		/*for (int i = 0; i < friends.length; i++) {
			friends[i].write(file);
		}*/

		System.out.println("Blocks written to file!\n");

		//Close the file
		file.close();

		file = new RandomAccessFile(TEST_FILE, "r");

		System.out.printf("Reading \"%s\"...%n%n", TEST_FILE);
		pause(1500);
		read(file);

		/*for (int i = 0; i < 4; i++) {
			System.out.println(Friend.readObject(file));
		}*/

		file.close();

		System.out.println("\nDone!!!");

	}

	public static void read(RandomAccessFile file) throws IOException{
		Block[] blocks = Block.readFile(file);

		for (int i = 0; i < blocks.length; i++) {
			System.out.print(blocks[i] + " ");
			System.out.print(blocks[i].getData());
			System.out.println();
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

package com.friend;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FriendFileHandler implements Closeable {
	
	private static final int NUM_OF_FRIENDS = 20;
	private final long FP_OFFSET = 8;
	private final long DP_OFFSET = 0;
	
	private long freePointer;
	private long dataPointer;
	
	private RandomAccessFile raf;
	private Block curr;
	private long loc;
	
	public FriendFileHandler(String path) throws IOException {
		raf = new RandomAccessFile(path, "rw");
		
		if(raf.length() == 0){
			populateFile();
			raf.seek(0);
		}
		
		curr = null;
		dataPointer = raf.readLong();
		freePointer = raf.readLong();
		loc = dataPointer;
	}
	
	private void populateFile() throws IOException{
		raf.writeLong(-1L);
		raf.writeLong(16L);
		
		Friend f = new Friend();
		Block b = new Block(f, -1, Block.BYTES + 16);
		
		long prev = raf.getFilePointer();
		
		b.write(raf);
		
		//Write The blocks to the file
		for (int i = 0; i < NUM_OF_FRIENDS - 1; i++) {
			b.setPrev(prev);
			b.setNext(raf.getFilePointer() + Block.BYTES);
			prev = raf.getFilePointer();
			b.write(raf);
		}
		
		b.setNext(-1);
		b.setPrev(prev);
		b.write(raf);
	}
	
	public Block read(){
		Block b = new Block();
		
		try {
			if(raf.getFilePointer() < 16){
				raf.seek(16);
			}
			b.read(raf);
		} catch (EOFException e) {
			return null;
		} catch (IOException e) {
			System.err.println("Error reading file");
		}
		
		return b;
	}
	
	public void deleteFriend(Friend target){
		try{
			raf.seek(16);
			Block b = new Block();
			Friend f;
			while(true){
				///If never found then while loop goes forever
				long loc = raf.getFilePointer();
				b.read(raf);
				f = b.getData();
				if (f.equals(target)){
					long prev = b.getPrev();
					//Previous block location
					long next = b.getNext();
					//Next block location
					long curr = loc;
					//Current block location
					
					raf.seek(b.getPrev());
					//Go back to prev block to change next location
					Block bp = new Block();
					bp.read(raf);
					bp.setNext(next);
					//write the block to the file
					raf.seek(b.getPrev());
					bp.write(raf);
					
					//Change the curr block to null
					raf.seek(curr);
					bp.read(raf);
					bp.setData(Friend.DEFAULT);
					bp.write(raf);
					
					
					raf.seek(b.getNext());
					//Go to next block to change prev location
					bp.read(raf);
					bp.setPrev(prev);
					raf.seek(next);
					bp.write(raf);
					
					raf.seek(8);
					//Change FP to current block
					raf.writeLong(curr);
					
					break;
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void addFriend(Friend f){
		//TODO: COpy over add method
		try {
			//Seek to FP
			//Seek to next free block
			raf.seek(freePointer);

			Block b = new Block();
			//Read the block
			b.read(raf);
			//Set the data of the block
			b.setData(f);
			//Store the free pointer to the next block
			//TODO: Should change this to a search for the next free block
			long newOpen = raf.getFilePointer();

			//Write Block
			raf.seek(freePointer);
			long loc = raf.getFilePointer();
			long prev = b.getPrev();
			long next = b.getNext();
			b.write(raf);

			//Edit the prev block to update its next data
			if (prev > -1) {
				raf.seek(prev);
				b.read(raf);
				b.setNext(loc);
				raf.seek(prev);
				b.write(raf);
			}

			//Edit the next Block to update its prev data
			if (prev > -1) {
				raf.seek(next);
				b.read(raf);
				b.setPrev(loc);
				b.write(raf);
			}


			//Locate next free block
			long fP = searchNextFree();
			//Update DP and FP
			//TODO: DP shouldn't necessarily change on every insert
			freePointer = fP;


		} catch (IOException e) {

		}
	}
	//Gets the first Data block
	private long getDataPointer(){
		try{
			raf.seek(16);
			while (true){
				loc = raf.getFilePointer();
				curr.read(raf);
				if (!curr.isDeleted()){
					return loc;
				}
			}
		}catch (EOFException e){
			return -1;
		}catch (IOException e){
			System.err.println("Error reading file");
			System.exit(-1);
		}
		return -1;
	}
	//Searches for the next Free block offset
	private long searchNextFree(){
		try{
			raf.seek(16);
			while (true){
				loc = raf.getFilePointer();
				curr.read(raf);
				if (curr.isDeleted()){
					freePointer = loc;
					return loc;
				}
			}
		} catch (EOFException e){
			return -1;
		} catch (IOException e) {
			System.err.println("Error reading file");
			System.exit(-1);
		}
		return -1;
	}

	//Prints out only valid friends
	private void printFriends(){
		try{
			raf.seek(16);
			curr.read(raf);
			while (true){
				if(!curr.isDeleted()){
					System.out.println(curr);
				}
			}
		}catch (EOFException e){

		}catch (IOException e){

		}
	}
	
	private void seekNext() throws IOException{
		loc = raf.getFilePointer() + Block.BYTES;
		raf.seek(loc);
	}
	
	private void seekToFreePointer() throws IOException{
		loc = FP_OFFSET;
		raf.seek(loc);
	}
	
	private void seekToFree() throws IOException{
		raf.seek(freePointer);
		loc = raf.getFilePointer();
	}
	
	private void seekToData() throws IOException{
		raf.seek(dataPointer);
		loc = raf.getFilePointer();
	}
	
	public void forcePointerUpdate() throws IOException{
		raf.seek(DP_OFFSET);
		raf.writeLong(dataPointer);
		raf.writeLong(freePointer);
	}
	
	@Override
	public void close() throws IOException {
		forcePointerUpdate();
		raf.close();
	}
	
}

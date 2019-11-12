package com.friend;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FriendFileHandler implements Closeable {
	
	private final long FP_OFFSET = 8;
	private final long DP_OFFSET = 0;
	private final long DATA_START = 16;
	
	private long freePointer;
	private long dataPointer;
	
	private RandomAccessFile raf;
	private Block curr;
	private long loc;
	
	public FriendFileHandler(String path) throws IOException {
		raf = new RandomAccessFile(path, "rw");
		curr = new Block(null);
		dataPointer = raf.readLong();
		freePointer = raf.readLong();
		loc = DATA_START;
		
		if(dataPointer != -1){
			seekToData();
			curr.readObject(raf);
		}
	}
	
	public Block readCurr(){
		return curr;
	}
	
	private Block readPrev(){
		if(curr != null && curr.getPrev() >= 16){
			Block prev = new Block();
			try {
				loc = raf.getFilePointer();
				raf.seek(curr.getPrev());
				prev.readObject(raf);
				raf.seek(loc);
			}catch (IOException e){
				System.err.println("Error reading Block");
				System.exit(-1);
			}
			return prev;
		}
		return null;
	}
	
	private Block readNext(){
		if(curr != null && curr.getNext() >= (16 + Block.BYTES)){
			Block next = new Block();
			try {
				loc = curr.getNext();
				raf.seek(loc);
				next.readObject(raf);
				
				if(next.getData().equals(new Friend())){
				
				}
				
			}catch (IOException e){
				System.err.println("Error reading Block");
				System.exit(-1);
			}
			return next;
		}
		return null;
	}
	
	public void deleteFriend(String first, String last){
		try{
			raf.seek(16);
			Block b = new Block();
			Friend f;
			while(true){
				///If never found then while loop goes forever
				loc = raf.getFilePointer();
				curr.readObject(raf);
				f = b.getData();
				if (f.compareNames(first,last)){
					long prev = b.getPrev();
					//Previous block location
					long next = b.getNext();
					
					//Current block location
					raf.seek(prev);
					//Go back to prev block to change next location
					Block bPrev = new Block();
					bPrev.readObject(raf);
					bPrev.setNext(next);
					raf.seek(prev);
					bPrev.writeObject(raf);
					
					raf.seek(next);
					//Go to next block to change prev location
					Block bNext = new Block();
					bNext.readObject(raf);
					bNext.setPrev(prev);
					raf.seek(next);
					bNext.writeObject(raf);
					
					raf.seek(8);
					//Change FP to current block
					freePointer = loc;
				}
			}
		}catch (EOFException e){
			System.err.println("Block doesn't exist");
		}catch (IOException e){
			System.err.println("Error reading file");
		}
	}
	
	public void addFriend(Friend f){
		//Seek to FP
		//Seek to next free block
		try {
			seekToFree();
			
			//Read the block
			curr.readObject(raf);
			//Set the friend data of the block
			curr.setData(f);
			//Store the free pointer to the next block
			//TODO: Should change this to a search for the next free block
			
			
			//Write Block
			seekToFree();
			curr.writeObject(raf);
			
			if (dataPointer == -1) {
				dataPointer = freePointer;
			}
			
		} catch(IOException e){
			System.err.println("Error adding friend");
		}
	}
	
	private long searchNextFree(){
		return 0;
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
	
	@Override
	public void close() throws IOException {
		raf.seek(DP_OFFSET);
		raf.writeLong(dataPointer);
		raf.writeLong(freePointer);
		raf.close();
	}
	
}

package com.friend;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FriendFileHandler {
	
	private RandomAccessFile raf;
	private Block curr;
	private long loc;
	
	public FriendFileHandler(String path) throws IOException {
		raf = new RandomAccessFile(path, "rw");
		curr = null;
		loc = 0;
	}
	
	public Block read(){
		return null;
	}
	
	private Block readPrev(){
		return null;
	}
	
	private Block readNext(){
		return null;
	}
	
	public void deleteFriend(String first, String last){
	
	}
	
	public void addFriend(Friend f){
	
	}
	
	private void seekToFP() throws IOException{
		raf.seek(getFP());
	}
	
	private void seekToData() throws IOException{
		raf.seek(getDP());
	}
	
	private long getDP() throws IOException{
		raf.seek(0);
		long dp = raf.readLong();
		raf.seek(loc);
		
		return dp;
	}
	
	private long getFP() throws IOException {
		raf.seek(8);
		long fp = raf.readLong();
		raf.seek(loc);
		return fp;
	}
	
	private void setFP(long fp) throws IOException{
		raf.seek(8);
		raf.writeLong(fp);
		raf.seek(loc);
	}
}

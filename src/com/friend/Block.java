package com.friend;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Block {

	public static final int BYTES = Friend.BYTES + (2 * Long.BYTES);

    private Friend friendObject;
    private long prev;
    private long next;


    public Block(Friend friend, long prev, long next){
        this.friendObject = friend;
	    this.prev = prev;
	    this.next = next;
    }

    public Block(Friend friend, long prev){
    	this(friend, 0, 0);
    }

    public Block(Friend friend){
    	this(friend, 0L, 0L);
    }

    public Block(String firstName, String lastName, String phoneNumber){
        this(new Friend(firstName,lastName,phoneNumber));
    }

	public Block(){
		this(new Friend());
	}

    public void write(RandomAccessFile file) throws IOException {
		if(friendObject == null){
			//Using a static instance to avoid creating throwaway objects
			friendObject = Friend.DEFAULT;
		}
    	
    	friendObject.write(file);
    	file.writeLong(prev);
    	file.writeLong(next);
    }

    public void read(RandomAccessFile file) throws IOException{
		this.friendObject.read(file);
    	this.prev = file.readLong();
	    this.next = file.readLong();
    }
    //Data to see if the block was deleted or not
    //Checks if the block is a null block
    public boolean isDeleted(){
        return this.friendObject == null;
    }


    public void setPrev(long prevNum){
        this.prev = prevNum;
    }

    public long getPrev(){
        return this.prev;
    }

    public void setNext(long nextNum){
        this.next = nextNum;
    }

    public long getNext(){
        return this.next;
    }

    public Friend getData(){
    	return friendObject;
    }

    public void setData(Friend friend){
    	this.friendObject = friend;
    }

    public String toString(){
    	return String.format("[Block p=%d n=%d]", prev, next);
    }

}

package com.friend;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

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
    	this(friend, prev, prev + (BYTES * 2));
    }

    public Block(Friend friend){
    	this(friend, -1L);
    }

    public Block(String firstName, String lastName, String phoneNumber){
        this(new Friend(firstName,lastName,phoneNumber));
    }

	public Block(){
		this(new Friend());
	}

    public void writeObject(RandomAccessFile file) throws IOException {
    	/*long mask = 0x0000000F;

    	byte[] prevPointer = new byte[8];
    	byte[] nextPointer = new byte[8];

    	//Write the pointers to the next block
	    for (int i = 56; i > 0; i -= 8) {
		    prevPointer[(56 - i)/8] = ((byte) ((prev >> i) & mask));
	    }

	    for (int i = 56; i > 0; i -= 8) {
		    nextPointer[(56 - i)/8] = ((byte) ((next >> i) & mask));
	    }

	    file.write(prevPointer);
	    file.write(nextPointer);*/

    	file.writeLong(this.prev);
    	file.writeLong(this.next);

	    //Writes a default friend in the event this block doesn't have a friend object
    	if(friendObject != null){
		    friendObject.write(file);
	    }else{
    		new Friend().write(file);
	    }
    }

    public static Block[] readFile(RandomAccessFile file) throws IOException{
    	//Create an ArrayList to store the blocks being read
	    ArrayList<Block> blocks = new ArrayList<>();

	    //Read each block until EOF is reached
	    do{
	    	Block b = new Block();
	    	b.readObject(file);
	    	blocks.add(b);
	    }while (file.getFilePointer() < file.length());

	    blocks.trimToSize();

    	return blocks.toArray(new Block[0]);
    }

    public void readObject(RandomAccessFile file) throws IOException{
	    this.prev = 0L;
	    this.next = 0L;

	    this.prev = file.readLong();
	    this.next = file.readLong();
    	this.friendObject.read(file);
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
    	return String.format("[Block p=%#018x n=%#018x]", prev, next);
    }

}

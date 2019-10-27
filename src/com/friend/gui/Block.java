package com.friend.gui;

import com.friend.Friend;

public class Block {
    private static Friend friendObject;
    private static long prev;
    private static long next;
    private static final int BYTE_SIZE = 16;



    public Block(){
        friendObject = new Friend();
        this.prev = -1;
        this.next = -1;
    }

    public Block(Friend person){
        this.friendObject.setFirstName(person.getFirstName());
        this.friendObject.setLastName(person.getLastName());
        this.friendObject.setPhoneNumber(person.getPhoneNumber());

    }

    public Block(String firstName,String lastName, String phoneNumber){
        friendObject = new Friend(firstName,lastName,phoneNumber);
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

    public int getByteSize(){return  this.getByteSize();}


}

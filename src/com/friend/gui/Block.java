package com.friend.gui;

import com.friend.Friend;

public class Block {
    private static Friend friendObject;
    private static int prev;
    private static int next;



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

    public void setPrev(int prevNum){
        this.prev = prevNum;
    }

    public int getPrev(){
        return this.prev;
    }

    public void setNext(int nextNum){
        this.next = nextNum;
    }

    public int getNext(){
        return this.next;
    }


}

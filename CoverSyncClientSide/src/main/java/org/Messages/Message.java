package org.Messages;

public class Message {
  String type;
  String input;
  int number;

  public Message() {
    this.type = "";
    this.input = "";
    this.number = 0;
    System.out.println("client-side message created");
  }

  public Message(String type, String input, int number) {
    this.type = type;
    this.input = input;
    this.number = number;
    System.out.println("client-side message created");
  }
}
package com.xtwy.client.param;

import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest {
 
   private final long id;
   private Object content;
   private final AtomicLong aid = new AtomicLong(1);
   private String command;
   
   
public String getCommand() {
	return command;
}
public void setCommand(String command) {
	this.command = command;
}
public Object getContent() {
	return content;
   }

public void setContent(Object content) {
	this.content = content;
	//System.out.println("setContent finish");
}
   public ClientRequest(){
     id = aid.incrementAndGet();
	}   
   public long getId() {
	   //System.out.println(" getId() ");
	return id;
	
   }
   }

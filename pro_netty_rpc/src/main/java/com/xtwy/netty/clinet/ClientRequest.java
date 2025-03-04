package com.xtwy.netty.clinet;

import java.util.concurrent.atomic.AtomicLong;

public class ClientRequest {
 
   private final long id;
   private Object content;
   private final AtomicLong aid = new AtomicLong(1);
   private String commad;
   
   
   public String getCommad() {
	return commad;
}
public void setCommad(String commad) {
	this.commad = commad;
}
public Object getContent() {
	return content;
   }
   public void setContent(Object content) {
	this.content = content;
   }
   public ClientRequest(){
     id = aid.incrementAndGet();
	}   
   public long getId() {
	return id;
   }
   }

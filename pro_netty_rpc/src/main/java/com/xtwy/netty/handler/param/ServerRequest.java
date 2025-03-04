package com.xtwy.netty.handler.param;

public class ServerRequest {
   private Long id;
   private Object content;
   private String commad;
   
   public String getCommad() {
	return commad;
}
public void setCommad(String commad) {
	this.commad = commad;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Object getContent() {
	return content;
}
public void setContent(Object content) {
	this.content = content;
}

}

/*
 * package com.xtwy.pro_netty_rpc;
 * 
 * import java.util.ArrayList; import java.util.List;
 * 
 * import org.junit.Test;
 * 
 * import com.xtwy.netty.clinet.ClientRequest; import
 * com.xtwy.netty.clinet.TcpClient; import com.xtwy.netty.util.Response; import
 * com.xtwy.user.bean.User;
 * 
 * public class TestTcp {
 * 
 * @Test public void testGetResponse() { ClientRequest request = new
 * ClientRequest(); request.setContent("测试TCP长连接请求"); Response resp =
 * TcpClient.send(request ); System.out.println(resp.getResult()); } public void
 * testSaveUser() { ClientRequest request = new ClientRequest(); User u = new
 * User(); u.setId(1); u.setName("张三");
 * request.setCommad("com.xtwy.user.controller.UserController.saveUser");
 * request.setContent(u); Response resp = TcpClient.send(request );
 * System.out.println(resp.getResult()); }
 * 
 * @Test public void testSaveUsers() { ClientRequest request = new
 * ClientRequest(); List<User> users = new ArrayList<User>(); User u = new
 * User(); u.setId(1); u.setName("张三"); users.add(u);
 * request.setCommad("com.xtwy.user.controller.UserController.saveUser");
 * request.setContent(users); Response resp = TcpClient.send(request );
 * System.out.println(resp.getResult()); } }
 */

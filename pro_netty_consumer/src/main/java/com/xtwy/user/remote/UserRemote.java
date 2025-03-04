package com.xtwy.user.remote;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xtwy.client.param.Response;
import com.xtwy.user.bean.User;

@Component
public interface UserRemote {

	public Response saveUser(User user);
	public Response saveUsers(List<User> users);
}

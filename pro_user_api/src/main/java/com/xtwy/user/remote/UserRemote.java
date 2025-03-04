package com.xtwy.user.remote;

import java.util.List;

import com.xtwy.user.model.User;

public interface UserRemote {
	public Object saveUser(User user);
	public Object saveUsers(List<User> users);
}

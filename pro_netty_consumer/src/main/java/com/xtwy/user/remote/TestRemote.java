package com.xtwy.user.remote;

import org.springframework.stereotype.Component;

import com.xtwy.client.param.Response;
import com.xtwy.user.bean.User;


@Component
public interface TestRemote {
	public void testUser();
}
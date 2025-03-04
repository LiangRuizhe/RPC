package com.xtwy.client.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import com.xtwy.client.annotation.RemoteInvoke;
import com.xtwy.client.core.TcpClient;
import com.xtwy.client.param.ClientRequest;
import com.xtwy.client.param.Response;



@Component
public class InvokeProxy implements BeanPostProcessor{
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Field[] fields = bean.getClass().getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(RemoteInvoke.class)) {
				field.setAccessible(true);
				final Map<Method, Class> methodClassMap = new HashMap<Method,Class>();
				putMethodClass(methodClassMap,field);
				Enhancer enhancer = new Enhancer();
				enhancer.setInterfaces(new Class[] {field.getType()});
				enhancer.setCallback(new MethodInterceptor() {		
					@Override
					public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy)
							throws Throwable {
						// TODO 自动生成的方法存根
						ClientRequest request = new ClientRequest();
						//System.out.println(args.toString());
						//System.out.println(methodClassMap.get(method).getName()+"."+method.getName());
						request.setCommand(methodClassMap.get(method).getName()+"."+method.getName());
						request.setContent(args[0]);
						Response resp = TcpClient.send(request);
						return resp;
					}
					});
						try {
							field.set(bean, enhancer.create());
						} catch (Exception e) {
							 //TODO 自动生成的 catch 块
							e.printStackTrace();
						} 
		}
			}
		//System.out.println("finish");
		return bean;
	}
    //对属性的所有方法和属性接口类型放入到一个map
	private void putMethodClass(Map<Method, Class> methodClassMap, Field field) {
	    Method[] methods = field.getType().getDeclaredMethods();
	    for(Method m : methods) {
	    	methodClassMap.put(m, field.getType());
	    }	
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		//System.out.println("bbb");
		return bean;
	}

}

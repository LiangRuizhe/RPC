package com.xtwy.netty.medium;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.xtwy.netty.annotation.Remote;


@Component
public class IntialMedium implements BeanPostProcessor{
//中介，处理数据
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean.getClass().isAnnotationPresent(Remote.class)) {
		//System.out.println(bean.getClass().getName());	
		Method[] methods = bean.getClass().getDeclaredMethods();
		for(Method m:methods) {
			String key = bean.getClass().getInterfaces()[0].getName()+"."+m.getName();
            Map<String, BeanMethod> beanMap = Media.beanMap;
            BeanMethod beanMethod = new BeanMethod();
            beanMethod.setBean(bean);
            beanMethod.setMethod(m);
            beanMap.put(key, beanMethod);
            
		}
		
		}
			return bean;
    }
}

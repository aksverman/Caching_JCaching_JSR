package com.zetainteractive.security.keygenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component (value = "customkey")
public class CustomKeyGenerator implements CacheKeyGenerator {

	/*
	 * Logger 
	 */
	private static final Logger logger = Logger.getLogger(CustomKeyGenerator.class);
	
	@Override
	public GeneratedCacheKey generateCacheKey(
			CacheKeyInvocationContext<? extends Annotation> cacheKeyInvocationContext) {
		
		String methodName = null;
		CacheInvocationParameter[] para1= cacheKeyInvocationContext.getAllParameters();
		methodName = cacheKeyInvocationContext.getMethod().getName();
		Object[] params = new Object[para1.length];
		for(int i=0;i<para1.length;i++)
			params[0] = para1[0].getValue();
		GeneratedCacheKey customkey = new GeneratedCacheKeyImpl(params, methodName, cacheKeyInvocationContext);
		//GeneratedCacheKey customkey = new GeneratedCacheKeyImpl(params, methodName);
		logger.info("cutom key hashcode : "  + customkey.hashCode());
		return customkey;
	}
	
	
	
	/*@Override
	public Object generate(Object target, Method method, Object... params) {
		//logger.info("Generating Custome Key ... ");
		String key = null;
		String methodName = method.getName();
		StringBuilder sb = new StringBuilder();
        if ( "getUserById".equals(methodName) || "updateUser".equals(methodName))
        	key = "key_userid";
        else if ("listAllUser".equals(methodName)) 
			key = "key_list_all";
        else if ( "saveUser".equals(methodName))
        	key = "unique_key_save";
        else {
        	sb.append(method.getName());
	        for (Object param : params) 
	        	key = sb.append(param.toString()).toString();
	    }
        //logger.info("Custom Key Generatored : " + key);
        return key;
	}*/


}

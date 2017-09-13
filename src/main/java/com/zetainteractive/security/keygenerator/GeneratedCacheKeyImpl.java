package com.zetainteractive.security.keygenerator;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;

import org.apache.log4j.Logger;

import com.zetainteractive.security.bo.UserBO;

public class GeneratedCacheKeyImpl implements GeneratedCacheKey {
	  
		/*
		 * logger
		 */
		private static final Logger logger = Logger.getLogger(GeneratedCacheKeyImpl.class);
		
		private static final long serialVersionUID = 1L;

	  
	    private Object[] parameters = null;
	    private int hashCode = 0;
	    private String methodName = null;
		private CacheKeyInvocationContext<? extends Annotation> invokeContext;

	    	    
	    /**
	     * Constructs a default cache key.
	     *
	     * @param parameters the list of invoked method's parameters
	     * @param methodname of which cache was invoked
	     */
	    public GeneratedCacheKeyImpl(Object[] parameters, String methodName) {
	        this.parameters = parameters;
	        this.methodName = methodName;
	    }

	    /**
	     * constructs
	     * @param params 
	     * 
	     * @param methodName 
	     * @param cacheKeyInvocationContext 
	     */
	    public GeneratedCacheKeyImpl(Object[] params, String methodName, CacheKeyInvocationContext<? extends Annotation> cacheKeyInvocationContext) {
	    	this.parameters = params;
	    	this.methodName = methodName;
	    	this.invokeContext = cacheKeyInvocationContext;
		}
	    
	    
	    /**
	     * Override hashCode() for various methods called
	     * @imp  generated hashCode must be unique based on method name & **parameters**
	     * method being invoked with same params must generate same hashcode, otherwise cache will not work efficiently.
	     * 
	     * Adding method parameters to hashcode accordingly as
	     * 
	     *  @return hashcode of created GeneratedCacheKeyImpl object for every method called.
	     */
	    @Override
	    public int hashCode() {
	    	int tmphash = hashCode;
	    	try {
				if(tmphash == 0 ) {
					/*
					 * adding parameters value to hashcode to uniquely distinguish same method
					 * being invoked with different parameters
					 */
					if ( "getUserById".equals(methodName) || "updateUser".equals(methodName) || "getUserCustom".equals(methodName)) {
						UserBO userBO = invokeContext.getValueParameter() != null ? (UserBO) invokeContext.getValueParameter().getValue() : null;
						parameters[0] = userBO != null ? (long)userBO.getUserID() : parameters[0];
						for (int i = 0; i < "customcache".length(); i++) 
							tmphash = 31 * tmphash + "customcache".charAt(i);
				        tmphash += (long)parameters[0];
					}
					else  
						for(int i=0; i< methodName.length(); i++)
							tmphash += methodName.charAt(i);
					
					hashCode = tmphash;
				}
			} catch (Exception e) {
				logger.error("Exception while generating hashcode for method : " + methodName + e,e);
				e.printStackTrace();
			}
	        return tmphash;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        if (this.hashCode != obj.hashCode())
	            return false;
	        GeneratedCacheKeyImpl other = (GeneratedCacheKeyImpl) obj;
	        return Arrays.deepEquals(this.parameters, other.parameters);
	    }
}

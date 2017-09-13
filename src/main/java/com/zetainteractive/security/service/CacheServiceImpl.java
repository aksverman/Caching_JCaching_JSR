package com.zetainteractive.security.service;

import java.util.List;

import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zetainteractive.security.bo.UserBO;
import com.zetainteractive.security.dao.CacheUserDAO;
import com.zetainteractive.security.keygenerator.CustomKeyGenerator;

/**
 * 
 * @author Ankush.Verman
 * @imp the methods generating same key or declaring same key, must have same return type.
 * else it will throw @exception as ClassCastException
 * 
 * @imp	two caches are used here : customcache   :- to used for single record retrieval  [getUserById, getUserCustom, updateUser]
 * 								&  savelistcache :- to save a userbo or list records [listAllUser, listUserWithCriteria, saveUser]
 * 
 * based on above usage, @CacheRemoveAll {savelistcache} & @CachePut {customcache} are used.
 */

@Service
public class CacheServiceImpl implements CacheService {

	/*
	 * The Logger
	 */
	private static final Logger logger = Logger.getLogger(CacheServiceImpl.class);
	
	@Autowired
	CacheUserDAO cacheUserDao;
	
	/**
	 * @Cacheable is used to demarcate methods that are cacheable - 
	 * i.e, methods for whom the result is stored into the cache so on subsequent invocations (with the same arguments),
	 * the value in the cache is returned without having to actually execute the method.
	 * 
	 * @see 	https://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html#cache-annotations-cacheable
	 */
	@Override
	@CacheResult( cacheName = "customcache" , cacheKeyGenerator = CustomKeyGenerator.class)
	public UserBO getUserById(long userId) {
		logger.info("Begin : " + getClass().getName() + " :getUserById()");
		
		UserBO userBO = null;
		userBO = cacheUserDao.getUserById(userId);
		logger.info("End : " + getClass().getName() + " :getUserById()");
		return userBO;
	}
	
	/* Used SpEL as key 
	 * Each SpEL expression evaluates again a dedicated context, click below link to explore more meta data for Cache SpEL
	 *  
	 * @see		https://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html#cache-spel-context
	 */
	@Override
	@CacheResult( cacheName = "customcache"/*key = "{#userId, #userName}" ,key = "#userId + #userName"*/)
	public UserBO getUserCustom(@CacheKey long userId, String userName) {
		logger.info("Begin : " + getClass().getName() + " :getUserCustom()");
		
		UserBO userBO = null;
		userBO = cacheUserDao.getUserCustom(userId, userName);
		logger.info("End : " + getClass().getName() + " :getUserCustom()");
		return userBO;
	}

	/** *Conditional caching**
	 * a method might not be suitable for caching all the time,
	 * If SpEL expression that is evaluated to either true the method is cached - if not, it behaves as if the method is not cached
	 * In below case, if #criteria (in our case UserBO.createdBy) is 'testaks', then method is cached only.
	 * 
	 * @see 	https://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html#cache-annotations-cacheable-condition
	 */
	@Override
	@CacheResult( cacheName = "savelistcache", cacheKeyGenerator = CustomKeyGenerator.class)
	public List<UserBO> listUserWithCriteria(String criteria) {
		logger.info("Begin : " + getClass().getName() + " : listUserWithCriteria");
		List<UserBO>	userList = null;
		userList = cacheUserDao.listUserWithCriteria(criteria);
		logger.info("End : " + getClass().getName() + " : listUserWithCriteria");
		return userList;
	}

	/* Custom Key Generation**
	 * 	If the algorithm responsible to generate the key is too specific or
	 *  if it needs to be shared, you may define a custom 'keyGenerator' on the operation
	 *  specify the name of the KeyGenerator bean implementation to use:
	 *  
	 * @see 	https://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html#cache-annotations-cacheable-key
	 */
	@Override
	@CacheResult( cacheName= "savelistcache", cacheKeyGenerator = CustomKeyGenerator.class)
	public List<UserBO> listAllUser() {
		logger.info("Begin : " + getClass().getName() + " : listAllUser()");
		List<UserBO>	userList = null;
		userList = cacheUserDao.listAllUser();
		logger.info("End : " + getClass().getName() + " : listAllUser()");
		return userList;
	}

	
	/** @CacheEvict annotation
	 * useful for removing stale or unused data from the cache. 
	 * Opposed to @Cacheable, annotation @CacheEvict demarcates methods that perform cache eviction, 
	 * i.e., methods that act as triggers for removing data from the cache. 
	 * 
	 * {@link https://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html#cache-annotations-evict}
	 */
	@Override
	//@Cacheable( value= "customCache", key="#userBO.userName + #userBO.firstName")
	@CacheRemoveAll ( cacheName = "savelistcache")
	public long saveUser(UserBO userBO) {
		logger.info("Begin : " + getClass().getName() + " : saveUser()");
		long userid = cacheUserDao.saveUser(userBO);
		logger.info("End : " + getClass().getName() + " : saveUser()");
		return userid;
	}

	/* @Caching annotation
	 * There are cases when multiple annotations of the same type, such as @CacheEvict or @CachePut need to be specified.
	 * @Caching allows multiple nested @Cacheable, @CachePut and @CacheEvict to be used on the same method
	 * {@code 			@Caching(evict = {	@CacheEvict(value = "products", key="#product.name"),
     *   									@CacheEvict(value = "items"   , key = "#product.id") })
	 * }
	 * @see		https://docs.spring.io/spring/docs/current/spring-framework-reference/html/cache.html#cache-annotations-caching
	 */
	@Override  
	@CachePut ( cacheName = "customcache", cacheKeyGenerator = CustomKeyGenerator.class)
	public UserBO updateUser(@CacheValue UserBO userBO) {
		logger.info("End : " + getClass().getName() + " : updateUser()");
		return cacheUserDao.updateUser(userBO);
	}

}

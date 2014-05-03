/***
 *  PersistentTemplate code
 *  @author agun 
 */
package org.flyJenkins.component.persistent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flyJenkins.component.persistent.hsql.HSQLDriver;

/**
 * TODO refactoring
 * @author agun
 *
 */

public class PersistentTemplate{
	
	/**
	 * class name of persistent driver
	 */
	private String driverClassName = null;
	
	private Map<String, QueryDriver> queryMap= null;
	
	private static PersistentDriver pDriver = null;
	
	private static PersistentTemplate instance = null;
	
	private PersistentTemplate(String driverClassName){
		queryMap = new HashMap<String, QueryDriver>();
		queryMap.put("Production", null);	
	}
	
	public static PersistentTemplate getInstance(String driverClassName){
		if(instance == null)
			instance = new PersistentTemplate(driverClassName);
		instance.setDriverClassName(driverClassName);
		instance.getPersistentDriver();		
		return instance;
	}
	
	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	

	/***
	 * obtain persistent driver instance by class name
	 * @return PersistentDriver or null
	 */
	public PersistentDriver getPersistentDriver(){		
		if(driverClassName == null || driverClassName.length() == 0)
			return null;
		
		if(pDriver == null){
			try {
				pDriver =(HSQLDriver) Class.forName(driverClassName).newInstance();
				pDriver.initDevice();
				Package packageVal  = Class.forName(driverClassName).getPackage();
				settingQueryMap(packageVal);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}	
		return pDriver;
	}
	
	private void settingQueryMap(Package packageVal) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String packageName = packageVal.getName();
		for(String key : queryMap.keySet()){
			QueryDriver queryDriver = (QueryDriver) Class.forName(packageName + "." + key + "Driver").newInstance();
			queryDriver.setPersistentDriver(pDriver);
			queryMap.put(key, queryDriver);
		}
	}

	public <T1> void query(T1 t1) throws IllegalArgumentException {
		String key = t1.getClass().getSimpleName();
		if(queryMap.containsKey(key) == false)
			return;
		QueryDriver queryDriver = queryMap.get(key);
		queryDriver.query(t1);
	}

	public <T1> void query(List<T1> t1List) {
		if(t1List == null || t1List.size() == 0)
			return;
		String key = t1List.get(0).getClass().getSimpleName();
		if(queryMap.containsKey(key) == false)
			return;
		QueryDriver queryDriver = queryMap.get(key);
		queryDriver.query(t1List);	
	}

	public <T2> List<T2>  getPageList(int page, int limit, Class cl) {
		String key = cl.getSimpleName();
		if(queryMap.containsKey(key) == false)
			return Collections.EMPTY_LIST;
		if(page  < 1)
			page =1;
		if(limit < 1)
			limit =1;	
		QueryDriver queryDriver = queryMap.get(key);
		return queryDriver.getPageList(page, limit);
	}
	
	public <T3> T3 getRead(T3 t3, Class cl){
		String key = cl.getSimpleName();
		if(queryMap.containsKey(key) == false)
			return null;
		QueryDriver queryDriver = queryMap.get(key);
		return queryDriver.read(t3);
	}
	public <T4> int getTotalPage(T4 t4, int limit, Class cl){
		String key = cl.getSimpleName();
		if(queryMap.containsKey(key) == false)
			return 0;
		QueryDriver queryDriver = queryMap.get(key);
		return queryDriver.getTotalPage(t4, limit);		
	}
}

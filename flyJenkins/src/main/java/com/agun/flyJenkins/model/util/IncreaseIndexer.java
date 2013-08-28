package com.agun.flyJenkins.model.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * make increasing index
 * @author agun
 *
 */
public class IncreaseIndexer {
	
	private static IncreaseIndexer increaseIndex = new IncreaseIndexer();
	
	private Map<String, Integer> indexMap = new ConcurrentHashMap<String, Integer>();

	private IncreaseIndexer(){}
	
	public static IncreaseIndexer getInstance(){
		return increaseIndex;
	}
	
	public synchronized int getIndex(String key){
		if(indexMap.containsKey(key) ==  false){
			indexMap.put(key, 1);
			return 1;
		}
		int value = indexMap.get(key);
		value++;
		indexMap.put(key, value);
		return value;
	}
	
	public synchronized void initIndex(String key, int value){
		indexMap.put(key, value);
	}
	
}

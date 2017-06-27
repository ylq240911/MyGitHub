package com.huiyi.nypos.common.bizflow;

import java.util.HashMap;

public class MemoryControl {
	private HashMap<String, Object> mapContain = new HashMap<String, Object>();
	
	public void clear(){
		mapContain.clear();
	}
	public void remove(String key){
		mapContain.remove(key);
	}
	public void setValue(String key, Object data){
		mapContain.put(key, data);
	}
	
	public Object getValue(String key){
		return mapContain.get(key);
	}
	
	public String getString(String key){
		return mapContain.get(key)==null?"":(String)mapContain.get(key);
	}
	
	public double getDouble(String key){
		return (Double)mapContain.get(key);
	}
	
	public int getInt(String key){
		if(mapContain.get(key) == null) return 0;
		return (Integer)mapContain.get(key);
	}
	
	public byte [] getByteArray(String key){
		return (byte [])mapContain.get(key);
	}
	
	public int [] getIntArray(String key){
		return (int [])mapContain.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getHashMap(String key){
		return (HashMap<String, String>)mapContain.get(key);
	}
	
	public String [] getStrings(String key){
		return (String [])mapContain.get(key);
	}
	
	public boolean getBoolean(String key){
		return (Boolean)mapContain.get(key);
	}
}

package com.agun.flyJenkins.request;

/**
 * flyJenkins 에서 request 를 처리하는 기능의 interface 
 * @author agun
 *
 */
public interface Requester {
	public Object request(String host, Object arg);
}

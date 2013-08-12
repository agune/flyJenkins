package com.agun.flyJenkins.process;

import java.util.Map;

/**
 * flyJenkins 에서 각각의 기능을 담당하는 역할을 하는 process 의 인터페이스 
 * @author agun
 *
 */
public interface FlyProcess {
	public Map<String, Object> run(Object arg1, String operName);
}

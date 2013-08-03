/***
 * jenkins 의 ProcessTree 기능을 편하게 사용할 수 있게 추상화
 * @author agun
 */
package com.agun.jenkins;

import hudson.util.ProcessTree;
import hudson.util.ProcessTree.OSProcess;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agun.system.SystemUtil;



public class ProcessTreeHelper {
	/**
	 * OSProcess 의 iterator 을 편한 List<?>로 변경해 준다
	 * @return List<OSProcess>
	 */
	public static List<OSProcess> getProcessList(){
		ProcessTree processTree = ProcessTree.get();
		Iterator<OSProcess> processIter =    processTree.iterator();
		List<OSProcess> processList = new ArrayList<OSProcess>();
		while(processIter.hasNext()){
			processList.add(processIter.next());
		}
		return processList;
	}
	
	/**
	 * 프로세스에 대한 정보를 구한다.
	 * @return Map<Integer, String>
	 */
	public static Map<Integer, String> getInfoProcess(){
		
		ProcessTree processTree = ProcessTree.get();
		Iterator<OSProcess> processIter =    processTree.iterator();
		Map<Integer, String> processInfoMap = new Hashtable<Integer, String>();
		while(processIter.hasNext()){
			OSProcess osProcess = processIter.next();
			// windowns ? org.jvnet.winp.WinpException: Failed to open process error=5 at .\envvar-cmdline.cpp:53
			if(SystemUtil.isWindows()){
				processInfoMap.put(osProcess.getPid(), osProcess.toString());
			}else{
				List<String> argList = osProcess.getArguments();
				processInfoMap.put(osProcess.getPid(), argList.get(0));
			}
			
			processInfoMap.put(osProcess.getPid(), "");
		}
		return processInfoMap;
	}
	
}

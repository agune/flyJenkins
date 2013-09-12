package com.agun.jenkins;
import java.io.File;
import java.io.IOException;

import hudson.FilePath;
import hudson.remoting.Channel;

/**
 * jenkins 의 filepath 기능을 추상화 한 class 기능 
 * @author agun
 *
 */
public class FilePathHelper{

	CLIHelper cliHelper;
	
	public FilePathHelper(CLIHelper cliHelper){
		
		this.cliHelper = cliHelper;
	}
	
	public void copyTo(String src, String target){
		FilePath filePath = new FilePath(new File(target));
		Channel channel = cliHelper.getChannel();
		FilePath fileRemotePath = new FilePath(channel, src);
		
		try {
			if(fileRemotePath.isDirectory())
				fileRemotePath.copyRecursiveTo(filePath);
			else
				fileRemotePath.copyTo(filePath);
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

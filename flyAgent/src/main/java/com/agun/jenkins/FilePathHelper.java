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
	
	
	
	public void sendCopyTo(String src, String target){
		FilePath filePath = new FilePath(new File(target));
		Channel channel = cliHelper.getChannel();
		FilePath fileRemotePath = new FilePath(channel, src);
		
		try {
			if(filePath.isDirectory())
				filePath.copyRecursiveTo(fileRemotePath);
			else
				filePath.copyTo(fileRemotePath);
					
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void sendCopyTo(FilePath srcFilePath, String target){
		Channel channel = cliHelper.getChannel();
		FilePath fileRemotePath = new FilePath(channel, target);
		try {
			if(srcFilePath.isDirectory())
				srcFilePath.copyRecursiveTo(fileRemotePath);
			else
				srcFilePath.copyTo(fileRemotePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public FilePath unzip(String zipFileSrc, String target){
		Channel channel = cliHelper.getChannel();
		FilePath fileRemotePath = new FilePath(channel, zipFileSrc);
		FilePath targetFilePath = new FilePath(new File(target));
		try {
			fileRemotePath.unzip(targetFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return targetFilePath;
	}
}

package com.agun.jenkins;

import static org.junit.Assert.*;

import hudson.FilePath;
import hudson.remoting.Channel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * jenkins 에 FilePath 기능을 추상화 한 class 테스트 코드
 * 
 * @author agun
 *
 */
public class FilePathHelperTest {
	/**
	 * 원격 파일 전송 테스트 해당 테스트는 시스템에 의존적이기 때문에 ignore 처리 한다.
	 */
	@Ignore
	@Test
	public void readTest() {
		try{
			URL url  = this.getClass().getResource("/id_rsa");
			CLIHelper cliHelper = new CLIHelper("http://192.168.0.127:8080/jenkins", url.getPath());
			FilePathHelper filePathHelper = new FilePathHelper(cliHelper);
			filePathHelper.copyTo("D:\\test.txt", "E:\\업무소스\\test3.txt");
			cliHelper.destory();
		}catch(Exception e){
			assertTrue(e.getMessage(), false);
		}
	}

}

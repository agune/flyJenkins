/**
 * persistent driver test code
 * @author agun
 */

package org.flyJenkins.component.persistent.hsql;

import static org.junit.Assert.*;

import java.util.List;

import org.flyJenkins.component.persistent.PersistentDriver;
import org.flyJenkins.component.persistent.PersistentTemplate;
import org.flyJenkins.model.Production;
import org.junit.Test;

import com.sun.tools.corba.se.idl.InvalidArgument;

/**
 * TODO refactoring
 * @author agun
 *
 */
public class PersistentTemplateTest {

	@Test
	public void test() {
		
		
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		PersistentDriver pv = persistentTemplate.getPersistentDriver();
		pv.initSchema();
		
		Production production = new Production();
		production.setBuildNumber(10);
		production.setJobId(3);
		production.setOutput("/home/tomcat/root.war");
		production.setJobName("테스트프로젝트");
		
		try {
			persistentTemplate.query(production);
			List<Production> productionList = persistentTemplate.getPageList(0, 10, Production.class);
			assertTrue("데이터가 저장 되지 않", productionList.size() > 0);
			assertTrue("빌드 번호가 다름", productionList.get(0).getBuildNumber() == 10);
			assertTrue("job id 가  다름", productionList.get(0).getJobId() == 3);
			assertTrue("output이 다름", productionList.get(0).getOutput().equals("/home/tomcat/root.war"));
			assertTrue("jobName이 다름", productionList.get(0).getJobName().equals("테스트프로젝트"));
									
		} catch (InvalidArgument e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

/**
 * production query driver class
 * @author agun
 */

package org.flyJenkins.component.persistent.hsql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.flyJenkins.component.persistent.PersistentDriver;
import org.flyJenkins.component.persistent.QueryDriver;
import org.flyJenkins.model.Production;


/**
 * TODO refactoring
 * @author agun
 *
 */

public class ProductionDriver implements QueryDriver {
	
	Statement st = null;
	PersistentDriver persistentDriver = null;
	
	public <T1> void query(T1 t1) throws IllegalArgumentException {
		Statement st = persistentDriver.getStatement();
		
		if(!(t1 instanceof Production)){
			throw new IllegalArgumentException("object type is not Production model");
		}
		
		Production production = (Production)t1;
		
		String expression = "INSERT INTO productions(job_id, job_name, build, output) VALUES(";
		expression = expression + production.getJobId() + ",";
		expression = expression + "'" + production.getJobName() + "', ";
		expression = expression + production.getBuildNumber() + ",";
		expression = expression + "'" + production.getOutput() + "'";
		expression = expression + ")";
		System.out.println("===> query : " + expression);
		int i =0;
		
		// run the query
		try {
			i = st.executeUpdate(expression);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}    
		
		if (i == -1) {
			System.out.println("db error : " + expression);
		}

	}

	public <T1> void query(List<T1> t1List) {
	// TODO function : store List
	
	}
	/**
	 * obtain model list
	 */
	public <T2> List<T2> getPageList(int page, int limit) {
		Statement st = persistentDriver.getStatement();
		
		// calc offset by page value
		int offset = limit * (page -1);
		
		List<Production> productionList = new ArrayList<Production>();
		try {
			ResultSet rs = null;
			ResultSetMetaData meta = null;
			String expression = "select limit "+ offset + " " + limit;
			expression = expression  + " id, job_id, job_name, build, output from productions"; 
			rs = st.executeQuery(expression);
			meta = rs.getMetaData();
			int colmax = meta.getColumnCount();
			Object o = null;
			int i = 0;
			
			for (; rs.next(); ) {
	            Production production = new Production();
	            
				for (i = 0; i < colmax; ++i) {
	                o = rs.getObject(i + 1);    
	                if(i == 1)
	                	production.setJobId(rs.getInt(i + 1));
	                else if(i == 2)
	                 	production.setJobName(rs.getObject(i + 1).toString());
	                else if(i == 3)
	                	production.setBuildNumber(rs.getInt(i + 1));
	                else if(i == 4)
	                	production.setOutput(rs.getObject(i + 1).toString());
	            }
				productionList.add(production); 
	        }
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (List<T2>) productionList;
	}

	public void setPersistentDriver(PersistentDriver persistentDriver) {
		this.persistentDriver = persistentDriver;
	}

}

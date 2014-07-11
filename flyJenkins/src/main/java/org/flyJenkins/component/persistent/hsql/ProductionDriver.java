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
		expression = expression + production.getJobID() + ",";
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
			expression = expression  + " id, job_id, job_name, build, output, create_at from productions"; 
			rs = st.executeQuery(expression);
			meta = rs.getMetaData();
			int colmax = meta.getColumnCount();
			Object o = null;
			int i = 0;
			
			for (; rs.next(); ) {
	            Production production = new Production();
	            
				for (i = 0; i < colmax; ++i) {
	                o = rs.getObject(i + 1);    
	                if(i == 0)
	                	production.setProductionID(rs.getInt(i + 1));
	                else if(i == 1)
	                	production.setJobID(rs.getInt(i + 1));
	                else if(i == 2)
	                 	production.setJobName(rs.getObject(i + 1).toString());
	                else if(i == 3)
	                	production.setBuildNumber(rs.getInt(i + 1));
	                else if(i == 4)
	                	production.setOutput(rs.getObject(i + 1).toString());
	                else if(i == 5)
	                	production.setCreateDate(rs.getDate(i + 1));
	             	   
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

	@Override
	public <T3> T3 read(T3 t3) {
		if(!(t3 instanceof Production))
			return null;
		
		Production query = (Production) t3;
		Statement st = persistentDriver.getStatement();
		try {
			ResultSet rs = null;
			ResultSetMetaData meta = null;
			String expression = "SELECT id, job_id, job_name, build, output, create_at from productions where id = " + query.getProductionID(); 
			rs = st.executeQuery(expression);
			meta = rs.getMetaData();
			int colmax = meta.getColumnCount();
			Object o = null;
			int i = 0;
			Production production = new Production();
			if (rs.next()) {
				for (i = 0; i < colmax; ++i) {
	                o = rs.getObject(i + 1);    
	                if(i == 0)
	                	production.setProductionID(rs.getInt(i + 1));
	                else if(i == 1)
	                	production.setJobID(rs.getInt(i + 1));
	                else if(i == 2)
	                 	production.setJobName(rs.getObject(i + 1).toString());
	                else if(i == 3)
	                	production.setBuildNumber(rs.getInt(i + 1));
	                else if(i == 4)
	                	production.setOutput(rs.getObject(i + 1).toString());
	                else if(i == 5)
	                	production.setCreateDate(rs.getDate(i + 1));
	             	   
	            }
	        }
			st.close();
			return (T3)production;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T4> int getTotalPage(T4 t4, int limit) {
		
		if(!(t4 instanceof Production))
			return 0;
		
		Production query = (Production) t4;
		
		
		Statement st = persistentDriver.getStatement();
		
		int totalPage = 0;
		try {
			ResultSet rs = null;
			ResultSetMetaData meta = null;
			String expression = "SELECT count(*) from productions "; 
			rs = st.executeQuery(expression);
			meta = rs.getMetaData();
			if (rs.next()) {
			    int cnt = rs.getInt(1);
			    if(cnt > 0){
			    	totalPage = cnt / limit; 
			    	if((cnt % limit) > 0) totalPage++;
			    }
		    }
			st.close();
			return totalPage;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public <T5> void del(T5 t5) {
		// TODO Auto-generated method stub
		
	}

}

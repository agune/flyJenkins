package org.flyJenkins.component.persistent.hsql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.flyJenkins.component.persistent.PersistentDriver;
import org.flyJenkins.component.persistent.QueryDriver;
import org.flyJenkins.model.ServiceGroup;

public class ServiceGroupDriver implements QueryDriver {

	PersistentDriver persistentDriver = null;

	
	@Override
	public void setPersistentDriver(PersistentDriver persistentDriver) {
		this.persistentDriver = persistentDriver;
	}

	@Override
	public <T1> void query(T1 t1) throws IllegalArgumentException {
		Statement st = persistentDriver.getStatement();
		
		if(!(t1 instanceof ServiceGroup)){
			throw new IllegalArgumentException("object type is not ServiceGroup model");
		}
		
		ServiceGroup serviceGroup = (ServiceGroup)t1;
		String expression = "INSERT INTO serviceGroups(group_name) VALUES(";
		expression = expression + "'" + serviceGroup.getName() + "'";
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

	@Override
	public <T1> void query(List<T1> t1List) {

	}

	@Override
	public <T2> List<T2> getPageList(int page, int limit) {
		Statement st = persistentDriver.getStatement();
		
		// calc offset by page value
		int offset = limit * (page -1);
		
		List<ServiceGroup> serviceGroupList = new ArrayList<ServiceGroup>();
		try {
			ResultSet rs = null;
			ResultSetMetaData meta = null;
			String expression = "select limit "+ offset + " " + limit;
			expression = expression  + " id, group_name, create_at from serviceGroups"; 
			
			System.out.println("serviceGroup ====> " +  expression);
			
			
			rs = st.executeQuery(expression);
			meta = rs.getMetaData();
			int colmax = meta.getColumnCount();
			Object o = null;
			int i = 0;
			
			for (; rs.next(); ) {
	            ServiceGroup serviceGroup = new ServiceGroup();
	            
				for (i = 0; i < colmax; ++i) {
	                o = rs.getObject(i + 1);    
	                if(i == 0)
	                	serviceGroup.setGroupID(rs.getInt(i + 1));
	                else if(i == 1)
	                	serviceGroup.setName(rs.getObject(i + 1).toString());
	                else if(i == 2)
	                	serviceGroup.setCreateDate(rs.getDate(i + 1));
	            }
				serviceGroupList.add(serviceGroup); 
	        }
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (List<T2>) serviceGroupList;
	}

	@Override
	public <T3> T3 read(T3 t3) {
		
		if(!(t3 instanceof ServiceGroup))
			return null;
		
		ServiceGroup query = (ServiceGroup) t3;
		Statement st = persistentDriver.getStatement();
		try {
			ResultSet rs = null;
			ResultSetMetaData meta = null;
			String expression = "SELECT id, group_name, create_at from serviceGroups where id = " + query.getGroupID(); 
			rs = st.executeQuery(expression);
			meta = rs.getMetaData();
			int colmax = meta.getColumnCount();
			int i = 0;
			ServiceGroup serviceGroup = new ServiceGroup();
			if (rs.next()) {
				for (i = 0; i < colmax; ++i) {
	                if(i == 0)
	                	serviceGroup.setGroupID(rs.getInt(i + 1));
	                else if(i == 1)
	                	serviceGroup.setName(rs.getObject(i + 1).toString());
	                else if(i == 2)
	                	serviceGroup.setCreateDate(rs.getDate(i + 1));
	            }
	        }
			st.close();
			return (T3)serviceGroup;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T4> int getTotalPage(T4 t4, int limit) {
		
		if(!(t4 instanceof ServiceGroup))
			return 0;
		
		ServiceGroup query = (ServiceGroup) t4;
		
		
		Statement st = persistentDriver.getStatement();
		
		int totalPage = 0;
		try {
			ResultSet rs = null;
			ResultSetMetaData meta = null;
			String expression = "SELECT count(*) from serviceGroups "; 
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

}

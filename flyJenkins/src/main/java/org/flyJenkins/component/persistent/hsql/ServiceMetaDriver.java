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
import org.flyJenkins.model.ServiceMeta;

public class ServiceMetaDriver implements QueryDriver{
	
	private PersistentDriver persistentDriver = null;

	
	@Override
	public void setPersistentDriver(PersistentDriver persistentDriver) {
		this.persistentDriver = persistentDriver;
	}

	@Override
	public <T1> void query(T1 t1) throws IllegalArgumentException {
		Statement st = persistentDriver.getStatement();
		
		if(!(t1 instanceof ServiceMeta)){
			throw new IllegalArgumentException("object type is not Production model");
		}
		
		ServiceMeta serviceMeta = (ServiceMeta)t1;
		String expression = "INSERT INTO ServiceMetas(group_id, host, destination, service_type, command, weight, test_url) VALUES(";
		expression = expression + serviceMeta.getGroupID() + ",";
		expression = expression + "'" + serviceMeta.getHost() + "', ";
		expression = expression + "'" + serviceMeta.getDestination() + "', ";
		expression = expression + serviceMeta.getType() + ",";
		expression = expression + "'" + serviceMeta.getCommand() + "',";
		expression = expression + serviceMeta.getWeight() + ",";
		expression = expression + "'" + serviceMeta.getTestUrl() + "'";
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
		
		List<ServiceMeta> serviceMetaList = new ArrayList<ServiceMeta>();
		try {
			ResultSet rs = null;
			ResultSetMetaData meta = null;
			String expression = "select limit "+ offset + " " + limit;
			expression = expression  + " id, group_id, host, destination, service_type, command, weight, test_url, create_at from serviceMetas"; 
			rs = st.executeQuery(expression);
			meta = rs.getMetaData();
			int colmax = meta.getColumnCount();
			int i = 0;
			
			for (; rs.next(); ) {
	            ServiceMeta serviceMeta = new ServiceMeta();
	            
				for (i = 0; i < colmax; ++i) {
	                if(i == 0)
	                	serviceMeta.setServiceID(rs.getInt(i + 1));
	                else if(i == 1)
	                	serviceMeta.setGroupID(rs.getInt(i + 1));
	                else if(i == 2)
	                	serviceMeta.setHost(rs.getObject(i + 1).toString());
	                else if(i == 3)
	                	serviceMeta.setDestination(rs.getObject(i + 1).toString());
	                else if(i == 4)
	                	serviceMeta.setType(rs.getInt(i + 1));
	                else if(i == 5)
	                	serviceMeta.setCommand(rs.getObject(i + 1).toString());
	                else if(i == 6)
	                	serviceMeta.setWeight(rs.getInt(i + 1));
	                else if(i == 7)
	                	serviceMeta.setTestUrl(rs.getObject(i + 1).toString());
	                else if(i == 8)
	                	serviceMeta.setCreateDate(rs.getDate(i + 1));
	             	   
	            }
				serviceMetaList.add(serviceMeta); 
	        }
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (List<T2>) serviceMetaList;
	}

	@Override
	public <T3> T3 read(T3 t3) {
		if(!(t3 instanceof ServiceMeta))
			return null;
		
		ServiceMeta query = (ServiceMeta) t3;
	
		
		Statement st = persistentDriver.getStatement();
		try {
			ResultSet rs = null;
			ResultSetMetaData meta = null;
			String expression = "SELECT id, group_id, host, destination, service_type, command, weight, test_url, create_at from serviceMetas where id = " + query.getServiceID(); 
			System.out.println("======> service: " + expression);
			
			rs = st.executeQuery(expression);
			meta = rs.getMetaData();
			int colmax = meta.getColumnCount();
			int i = 0;
			System.out.println("====> " +  colmax);
			if (rs.next()) {
	            ServiceMeta serviceMeta = new ServiceMeta();
	            
				for (i = 0; i < colmax; ++i) {
	                if(i == 0)
	                	serviceMeta.setServiceID(rs.getInt(i + 1));
	                else if(i == 1)
	                	serviceMeta.setGroupID(rs.getInt(i + 1));
	                else if(i == 2)
	                	serviceMeta.setHost(rs.getObject(i + 1).toString());
	                else if(i == 3)
	                	serviceMeta.setDestination(rs.getObject(i + 1).toString());
	                else if(i == 4)
	                	serviceMeta.setType(rs.getInt(i + 1));
	                else if(i == 5)
	                	serviceMeta.setCommand(rs.getObject(i + 1).toString());
	                else if(i == 6)
	                	serviceMeta.setWeight(rs.getInt(i + 1));
	                else if(i == 7)
	                	serviceMeta.setTestUrl(rs.getObject(i + 1).toString());
	                else if(i == 8)
	                	serviceMeta.setCreateDate(rs.getDate(i + 1));
	            }
		        return (T3)serviceMeta;		        
	        }
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <T4> int getTotalPage(T4 t4, int limit) {
		if(!(t4 instanceof ServiceMeta))
			return 0;
		
		ServiceMeta query = (ServiceMeta) t4;
		Statement st = persistentDriver.getStatement();
		int totalPage = 0;
		try {
			ResultSet rs = null;
			ResultSetMetaData meta = null;
			String expression = "SELECT count(*) from serviceMetas "; 
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

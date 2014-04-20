/**
 * query driver interface
 * @author agun
 */
package org.flyJenkins.component.persistent;

import java.util.List;

import com.sun.tools.corba.se.idl.InvalidArgument;

public interface QueryDriver {
	public void setPersistentDriver(PersistentDriver persistentDriver);
	public <T1> void query(T1 t1) throws InvalidArgument;
	public <T1> void query(List<T1> t1List);
	public <T2> List<T2> getPageList(int page , int limit);
}

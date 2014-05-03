/**
 * query driver interface
 * @author agun
 */
package org.flyJenkins.component.persistent;

import java.util.List;


public interface QueryDriver {
	public void setPersistentDriver(PersistentDriver persistentDriver);
	public <T1> void query(T1 t1) throws IllegalArgumentException;
	public <T1> void query(List<T1> t1List);
	public <T2> List<T2> getPageList(int page , int limit);
	public <T3> T3 read(T3 t3);
	public <T4> int getTotalPage(T4 t4, int limit);
}

package org.flyJenkins.component.persistent;

import java.util.List;

public interface PersistentDriver {
	public void initDevice();
	public <T1> void query(T1 t1);
	public <T1> void query(List<T1> t1List);
	public <T2> List<T2> getPageList(int page , int limit);
}

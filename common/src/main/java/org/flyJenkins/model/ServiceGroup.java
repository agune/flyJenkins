/**
 * Service Group model
 * @author agun
 */

package org.flyJenkins.model;

import java.util.Date;
import java.util.List;

public class ServiceGroup {
	
	private int groupID;
	private String name;
	private Date createDate;

	private List<ServiceMeta> serviceMetaList;
	
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public long getCreateDate() {
		if(createDate == null) return 0L;
		return createDate.getTime();
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<ServiceMeta> getServiceMetaList() {
		return serviceMetaList;
	}
	public void setServiceMetaList(List<ServiceMeta> serviceMetaList) {
		this.serviceMetaList = serviceMetaList;
	}
}

//Client customization for WIP#25783 
package com.biperf.core.domain.client;

import com.biperf.core.domain.BaseDomain;

public class TcccQcardBatch extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String batchNumber;
	private String divisionKey;
	private String workCountry;

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getDivisionKey() {
		return divisionKey;
	}

	public void setDivisionKey(String divisionKey) {
		this.divisionKey = divisionKey;
	}

	public String getWorkCountry() {
		return workCountry;
	}

	public void setWorkCountry(String workCountry) {
		this.workCountry = workCountry;
	}

	@Override
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}

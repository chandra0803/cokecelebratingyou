package com.biperf.core.domain.client;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.User;

public class TcccUserPassword extends BaseDomain {

	private Long paxUserId;
	private String password;
	public Long getPaxUserId() {
		return paxUserId;
	}

	public void setPaxUserId(Long paxUserId) {
		this.paxUserId = paxUserId;
	}

	

	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Checks equality of the object parameter to this.
	 * 
	 * @param o
	 * @return boolean
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}

		final User user = (User) o;

		if (getId() != null ? !getId().equals(user.getId()) : user.getId() != null) {
			return false;
		}

		return true;
	}

	/**
	 * Define the hashCode from the id. Overridden from
	 * 
	 * @see com.biperf.core.domain.BaseDomain#hashCode()
	 * @return int
	 */
	public int hashCode() {
		return (getId() != null ? getId().hashCode() : 0);
	}

}

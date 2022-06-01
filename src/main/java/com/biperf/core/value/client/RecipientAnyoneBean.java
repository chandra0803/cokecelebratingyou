
package com.biperf.core.value.client;

import java.io.Serializable;

public class RecipientAnyoneBean implements Serializable {
	
	private String firstName;
	private String lastName;
	private String emailId;
	
	public RecipientAnyoneBean(){
	
	}

	public String getFirstName() 
	{
		return firstName;
	}

	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}

	public String getLastName() 
	{
		return lastName;
	}

	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	

}

/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

/**
 * 
 * @author poddutur
 * @since May 18, 2016
 */
public class NominationsApprovalPageNextLevelApproversValueBean
{
  private String lastName;
  private String firstName;
  private Long approverUserId;

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public Long getApproverUserId()
  {
    return approverUserId;
  }

  public void setApproverUserId( Long approverUserId )
  {
    this.approverUserId = approverUserId;
  }

}

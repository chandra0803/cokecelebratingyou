/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.fileload;

import java.math.BigDecimal;

/**
 * GoalQuestProgressImportRecord.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>meadows</td>
 * <td>Jan 3, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestProgressImportRecord extends ImportRecord
{

  private String loginId;
  private Long userId;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private BigDecimal totalPerformanceToDate;

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getLoginId()
  {
    return loginId;
  }

  public void setLoginId( String loginId )
  {
    this.loginId = loginId;
  }

  public BigDecimal getTotalPerformanceToDate()
  {
    return totalPerformanceToDate;
  }

  public void setTotalPerformanceToDate( BigDecimal totalPerformanceToDate )
  {
    this.totalPerformanceToDate = totalPerformanceToDate;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  /**
   * Ensure equality between this and the object param. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof GoalQuestProgressImportRecord ) )
    {
      return false;
    }

    GoalQuestProgressImportRecord goalQuestProgressImportRecord = (GoalQuestProgressImportRecord)object;

    if ( this.getId() != null && this.getId().equals( goalQuestProgressImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Constructs a <code>String</code> with all attributes
   * in name = value format.
   *
   * @return a <code>String</code> representation 
   * of this object.
   */
  public String toString()
  {
    final String TAB = "    ";

    String retValue = "";

    retValue = "GoalQuestProgressImportRecord ( " + super.toString() + TAB + "loginId = " + this.loginId + TAB + "userId = " + this.userId + TAB + "firstName = " + this.firstName + TAB + "lastName = "
        + this.lastName + TAB + "emailAddress = " + this.emailAddress + TAB + "totalPerformanceToDate = " + this.totalPerformanceToDate + TAB + " )";

    return retValue;
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/fileload/DepositImportRecord.java,v $
 */

package com.biperf.core.domain.fileload;

import java.math.BigDecimal;
import java.util.Date;

/*
 * DepositImportRecord <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 26, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class DepositImportRecord extends ImportRecord
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The user name of the user into whose account the award amount will be deposited.
   */
  private String userName;

  /**
   * The amount to be deposited into the specified user's account.
   */
  private BigDecimal awardAmount;

  private Date awardDate;

  private String comments;

  private String formElement1;
  private String formElement2;
  private String formElement3;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the amount to be deposited in the user's account.
   * 
   * @return the amount to be deposited in the user's account.
   */
  public BigDecimal getAwardAmount()
  {
    return awardAmount;
  }

  /**
   * Returns the user name of the user into whose account the award amount will be deposited.
   * 
   * @return the user name of the user into whose account the award amount will be deposited.
   */
  public String getUserName()
  {
    return userName;
  }

  /**
   * Sets the amount to be deposited in the user's account.
   * 
   * @param awardAmount the amount to be deposited in the user's account.
   */
  public void setAwardAmount( BigDecimal awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  /**
   * Sets the user name of the user into whose account the award amount will be deposited.
   * 
   * @param userName the user name of the user into whose account the award amount will be
   *          deposited.
   */
  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public Date getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( Date awardDate )
  {
    this.awardDate = awardDate;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the given object and this object are equal; returns false otherwise.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object the object to compare to this object.
   * @return true if the given object and this object are equal; false otherwise.
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof DepositImportRecord ) )
    {
      return false;
    }

    DepositImportRecord depositImportRecord = (DepositImportRecord)object;

    if ( this.getId() != null && this.getId().equals( depositImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Returns the hashcode for this object.
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return the hashcode for this object.
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Returns a string representation of this object.
   * 
   * @see java.lang.Object#toString()
   * @return a string representation of this object.
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "DepositImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getFormElement1()
  {
    return formElement1;
  }

  public void setFormElement1( String formElement1 )
  {
    this.formElement1 = formElement1;
  }

  public String getFormElement2()
  {
    return formElement2;
  }

  public void setFormElement2( String formElement2 )
  {
    this.formElement2 = formElement2;
  }

  public String getFormElement3()
  {
    return formElement3;
  }

  public void setFormElement3( String formElement3 )
  {
    this.formElement3 = formElement3;
  }
}

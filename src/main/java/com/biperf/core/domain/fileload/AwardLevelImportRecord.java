
package com.biperf.core.domain.fileload;

import java.util.Date;

/**
 * AwardLevelImportRecord.
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
 * <td>shanmuga</td>
 * <td>Feb 24, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class AwardLevelImportRecord extends ImportRecord
{
  private String userName;
  private String awardLevel;
  private Date awardDate;
  private String comments;

  private String formElement1;
  private String formElement2;
  private String formElement3;

  public String getAwardLevel()
  {
    return awardLevel;
  }

  public void setAwardLevel( String awardLevel )
  {
    this.awardLevel = awardLevel;
  }

  public String getUserName()
  {
    return userName;
  }

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

    if ( ! ( object instanceof AwardLevelImportRecord ) )
    {
      return false;
    }

    AwardLevelImportRecord ImportRecord = (AwardLevelImportRecord)object;

    if ( this.getId() != null && this.getId().equals( ImportRecord.getId() ) )
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
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "AwardLevelImportRecord [" );
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

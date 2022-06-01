/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/UserValueBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.StatusType;
import com.biperf.core.utils.StringUtil;

/**
 * UserValueBean.
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
 * <td>sathish</td>
 * <td>Dec 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserValueBean implements Serializable, Comparable
{
  private Long id;
  private String lastName;
  private String firstName;
  private String middleName;
  private String emailAddress;
  private String emailType;
  private String userName;
  private String status;
  private String awardBanqNumber;
  private Country country;
  private String nodeName;
  private Boolean duplicateEmail;

  /**
   * 
   */
  public UserValueBean()
  {
    super();
  }

  /**
   *
   */
  public UserValueBean( Long id, String lastName, String firstName, String middleName, String emailAddress, PickListItem emailType, String userName )
  {
    super();
    this.id = id;
    this.lastName = lastName;
    this.middleName = middleName;
    this.firstName = firstName;
    this.emailAddress = emailAddress;
    this.emailType = emailType == null ? "" : emailType.getCode();
    this.userName = userName;
  }

  /**
   *
   */
  public UserValueBean( Long id, String lastName, String firstName, String middleName, String emailAddress, PickListItem emailType, String userName, PickListItem status, String awardBanqNumber )
  {
    this( id, lastName, middleName, firstName, emailAddress, emailType, userName );
    this.status = status == null ? StatusType.INACTIVE_CODE : status.getName();
    this.awardBanqNumber = awardBanqNumber;
  }

  public UserValueBean( Long id, String lastName, String firstName, String middleName, String emailAddress, PickListItem emailType, String userName, PickListItem status, String awardBanqNumber, Country country )
  {
    this( id, lastName, middleName, firstName, emailAddress, emailType, userName, status, awardBanqNumber );
    this.country = country;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "UserValueBean [" );
    buf.append( "{id=" + this.getId() + "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public int hashCode()
  {
    return getId() != null ? getId().hashCode() : 0;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof UserValueBean ) )
    {
      return false;
    }

    final UserValueBean valueBean = (UserValueBean)object;

    if ( getId() != null ? !getId().equals( valueBean.getId() ) : valueBean.getId() != null )
    {
      return false;
    }

    return true;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }
  
  public String getEmailType()
  {
    return emailType;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getStatus()
  {
    return status;
  }

  public void setAwardBanqNumber( String awardBanqNumber )
  {
    this.awardBanqNumber = awardBanqNumber;
  }

  public String getAwardBanqNumber()
  {
    return awardBanqNumber;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }
  
  public void setEmailType( String emailType )
  {
    this.emailType = emailType;
  }

  /**
   * Takes a String in the format of something-something and sets the id to what is before the - and
   * the value to what is after the -. Then returns a new UserValueBean.
   * 
   * @param formattedId
   * @return FormattedValueBean
   */
  public static UserValueBean parseFormattedId( String formattedId )
  {
    UserValueBean userValueBean = new UserValueBean();

    int dilimeterIndex = formattedId.indexOf( "-" );

    userValueBean.setId( new Long( formattedId.substring( 0, dilimeterIndex ) ) );

    return userValueBean;
  }

  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof UserValueBean ) )
    {
      throw new ClassCastException( "A UserValueBean was expected." );
    }
    UserValueBean formattedValueBean = (UserValueBean)object;
    return this.getId().compareTo( formattedValueBean.getId() );

  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getNameLFMNoComma()
  {
    StringBuffer fullName = new StringBuffer();
    if ( lastName != null )
    {
      fullName.append( lastName ).append( " " );
    }
    if ( firstName != null )
    {
      fullName.append( firstName );
    }
    if ( middleName != null )
    {
      fullName.append( " " ).append( middleName ).append( " " );
    }

    return fullName.toString();
  }

  /**
   * @return Last name, First name
   */
  public String getLFComma()
  {
    if ( StringUtil.isEmpty( firstName ) && StringUtil.isEmpty( lastName ) )
    {
      return "";
    }
    else if ( StringUtil.isEmpty( firstName ) )
    {
      return lastName;
    }
    else if ( StringUtil.isEmpty( lastName ) )
    {
      return firstName;
    }
    else
    {
      return lastName + ", " + firstName;
    }
  }

  public String getLFCommaWithNodeAndCountry()
  {
    if ( StringUtil.isEmpty( firstName ) && StringUtil.isEmpty( lastName ) )
    {
      return "";
    }
    else if ( StringUtil.isEmpty( firstName ) )
    {
      return lastName;
    }
    else if ( StringUtil.isEmpty( lastName ) )
    {
      return firstName;
    }
    else
    {
      return lastName + ", " + firstName + " - " + nodeName + " - " + country.getAwardbanqAbbrev();
    }
  }

  /**
   * @return Last name - First name
   */
  public String getLFHyphen()
  {
    if ( StringUtil.isEmpty( firstName ) && StringUtil.isEmpty( lastName ) )
    {
      return "";
    }
    else if ( StringUtil.isEmpty( firstName ) )
    {
      return lastName;
    }
    else if ( StringUtil.isEmpty( lastName ) )
    {
      return firstName;
    }
    else
    {
      return lastName + " - " + firstName;
    }
  }

  public Country getCountry()
  {
    return country;
  }

  public void setCountry( Country country )
  {
    this.country = country;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public Boolean getDuplicateEmail()
  {
    return duplicateEmail;
  }

  public void setDuplicateEmail( Boolean duplicateEmail )
  {
    this.duplicateEmail = duplicateEmail;
  }

}

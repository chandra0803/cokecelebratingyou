
package com.biperf.core.ui.user;

import java.io.Serializable;

/**
 * PaxContactType
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
 * <td>mattam</td>
 * <td>June 02, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

@SuppressWarnings( "serial" )
public class PaxContactType implements Serializable
{
  private ContactType contactType;
  private String value;
  private Long contactId;
  private Long userId;
  private Long totalRecords;
  private boolean unique;
  private boolean inputContact;

  public ContactType getContactType()
  {
    return contactType;
  }

  public void setContactType( ContactType contactType )
  {
    this.contactType = contactType;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public Long getContactId()
  {
    return contactId;
  }

  public void setContactId( Long contactId )
  {
    this.contactId = contactId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( Long totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public boolean isUnique()
  {
    return unique;
  }

  public void setUnique( boolean unique )
  {
    this.unique = unique;
  }
  
  public boolean isInputContact()
  {
    return inputContact;
  }

  public void setInputContact( boolean inputContact )
  {
    this.inputContact = inputContact;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( contactId == null ) ? 0 : contactId.hashCode() );
    result = prime * result + ( ( contactType == null ) ? 0 : contactType.hashCode() );
    result = prime * result + ( ( userId == null ) ? 0 : userId.hashCode() );
    result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    PaxContactType other = (PaxContactType)obj;
    if ( contactId == null )
    {
      if ( other.contactId != null )
      {
        return false;
      }
    }
    else if ( !contactId.equals( other.contactId ) )
    {
      return false;
    }
    if ( contactType != other.contactType )
    {
      return false;
    }
    if ( userId == null )
    {
      if ( other.userId != null )
      {
        return false;
      }
    }
    else if ( !userId.equals( other.userId ) )
    {
      return false;
    }
    if ( value == null )
    {
      if ( other.value != null )
      {
        return false;
      }
    }
    else if ( !value.equals( other.value ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "PaxContactType [contactType=" + contactType + ", value=" + value + ", contactId=" + contactId + ", userId=" + userId + ", unique=" + unique + ", inputContact=" + inputContact + "]";
  }

}

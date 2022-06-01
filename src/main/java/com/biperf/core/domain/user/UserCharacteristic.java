/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/user/UserCharacteristic.java,v $
 */

package com.biperf.core.domain.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.DynaPickListType;

/**
 * UserCharacteristic.
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
 * <td>Jason</td>
 * <td>Apr 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserCharacteristic extends BaseDomain
{
  private User user;
  private UserCharacteristicType userCharacteristicType;
  private String characteristicValue;
  private UUID rosterUserCharId;

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "UserCharacteristic [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{user=" + this.getUser() + "}, " );
    buf.append( "{userCharacteristicType=" + this.getUserCharacteristicType() + "}, " );
    buf.append( "{characteristicValue=" + this.getCharacteristicValue() + "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Does equals on the Business Key ( userId and characteristicId ) Overridden from
   * 
   * @see java.lang.Object#toString()
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof UserCharacteristic ) )
    {
      return false;
    }

    final UserCharacteristic userChar = (UserCharacteristic)object;

    if ( getUser() != null ? !getUser().equals( userChar.getUser() ) : userChar.getUser() != null )
    {
      return false;
    }
    if ( getUserCharacteristicType() != null ? !getUserCharacteristicType().equals( userChar.getUserCharacteristicType() ) : userChar.getUserCharacteristicType() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Does hashCode on the Business Key ( userId and characteristicId ) Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    if ( getUser() != null && getUserCharacteristicType() != null )
    {
      return 29 * getUser().hashCode() + getUserCharacteristicType().hashCode();
    }
    return 0;
  }

  /**
   * @return value of user property
   */
  public User getUser()
  {
    return user;
  }

  /**
   * @param user value for user property
   */
  public void setUser( User user )
  {
    this.user = user;
  }

  /**
   * @return value of userCharacteristicType property
   */
  public UserCharacteristicType getUserCharacteristicType()
  {
    return userCharacteristicType;
  }

  /**
   * @param userCharacteristicType value for userCharacteristicType property
   */
  public void setUserCharacteristicType( UserCharacteristicType userCharacteristicType )
  {
    this.userCharacteristicType = userCharacteristicType;
  }

  /**
   * @return value of characteristicValue property
   */
  public String getCharacteristicValue()
  {
    if ( characteristicValue != null )
    {
      characteristicValue = characteristicValue.trim();
    }
    return characteristicValue;
  }

  /**
   * @param characteristicValue for characteristicValue property
   */
  public void setCharacteristicValue( String characteristicValue )
  {
    this.characteristicValue = characteristicValue;
  }

  public UUID getRosterUserCharId()
  {
    return rosterUserCharId;
  }

  public void setRosterUserCharId( UUID rosterUserCharId )
  {
    this.rosterUserCharId = rosterUserCharId;
  }

  /**
   * This is for display pages where the actual name is needed instead of the code stored in the
   * database.
   * 
   * @return list
   */
  public List<DynaPickListType> getCharacteristicDisplayValueList()
  {
    List<DynaPickListType> displayValueList = new ArrayList<>();
    if ( characteristicValue.trim().indexOf( "," ) != -1 )
    {
      StringTokenizer tokens = new StringTokenizer( characteristicValue.trim(), "," );
      int i = 0;
      while ( tokens.hasMoreTokens() )
      {
        displayValueList.add( DynaPickListType.lookup( userCharacteristicType.getPlName(), tokens.nextToken() ) );
        i++;
      }
    }
    else
    {
      displayValueList.add( DynaPickListType.lookup( userCharacteristicType.getPlName(), characteristicValue.trim() ) );
    }

    return displayValueList;
  }

  /**
   * Builds a string for display where an actual name is needed for list type values. 
   * If the value is a selection type, builds a comma separated list with the item name (rather than code)
   * Otherwise, will return the normal value
   */
  public String buildCharacteristicDisplayString()
  {
    if ( getUserCharacteristicType().getCharacteristicDataType().getCode().equals( CharacteristicDataType.SINGLE_SELECT )
        || getUserCharacteristicType().getCharacteristicDataType().getCode().equals( CharacteristicDataType.MULTI_SELECT ) )
    {
      List<DynaPickListType> characteristicDisplayValueList = getCharacteristicDisplayValueList();
      StringBuilder sb = new StringBuilder();
      int i = 0;
      if ( characteristicDisplayValueList != null )
      {
        for ( Iterator<DynaPickListType> it = characteristicDisplayValueList.iterator(); it.hasNext(); )
        {
          i = i + 1;
          DynaPickListType dynaPickListType = it.next();
          if ( dynaPickListType != null )
          {
            sb.append( dynaPickListType.getName() );
            if ( characteristicDisplayValueList.size() != i )
            {
              sb.append( "," );
            }
          }
        }
      }
      return sb.toString();
    }
    else
    {
      return getCharacteristicValue();
    }
  }

}

/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/participant/AudienceCriteriaCharacteristic.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.characteristic.Characteristic;

/**
 * AudienceCriteriaCharacteristic.
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
 * <td>sharma</td>
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AudienceCriteriaCharacteristic extends BaseDomain
{
  private AudienceCriteria audienceCriteria;
  private Characteristic characteristic;
  private String characteristicValue;
  private String charactersticDataType;
  private String searchType;

  public AudienceCriteria getAudienceCriteria()
  {
    return audienceCriteria;
  }

  public void setAudienceCriteria( AudienceCriteria audienceCriteria )
  {
    this.audienceCriteria = audienceCriteria;
  }

  public Characteristic getCharacteristic()
  {
    return characteristic;
  }

  public void setCharacteristic( Characteristic characteristic )
  {
    this.characteristic = characteristic;
  }

  public String getCharacteristicValue()
  {
    return characteristicValue;
  }

  public void setCharacteristicValue( String characteristicValue )
  {
    this.characteristicValue = characteristicValue;
  }

  public String getCharactersticDataType()
  {
    return charactersticDataType;
  }

  public void setCharactersticDataType( String charactersticDataType )
  {
    this.charactersticDataType = charactersticDataType;
  }

  /**
   * Overridden from
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

    if ( ! ( object instanceof AudienceCriteriaCharacteristic ) )
    {
      return false;
    }

    final AudienceCriteriaCharacteristic otherAudienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)object;

    // we need to compare all the fields for equals to work for AudienceCriteriaCharacteristic
    // as there is no other business key in this object.
    if ( !this.getAudienceCriteria().equals( otherAudienceCriteriaCharacteristic.getAudienceCriteria() ) )
    {
      return false;
    }

    if ( !this.getCharacteristic().equals( otherAudienceCriteriaCharacteristic.getCharacteristic() ) )
    {
      return false;
    }

    if ( !this.getCharacteristicValue().equals( otherAudienceCriteriaCharacteristic.getCharacteristicValue() ) )
    {
      return false;
    }

    if ( !this.getSearchType().equals( otherAudienceCriteriaCharacteristic.getSearchType() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;

    result = getAudienceCriteria() != null ? getAudienceCriteria().hashCode() : 0;
    result = result + 29 * ( getCharacteristic() != null ? getCharacteristic().hashCode() : 0 );
    result = result + 29 * ( getCharacteristicValue() != null ? getCharacteristicValue().hashCode() : 0 );
    result = result + 29 * ( getSearchType() != null ? getSearchType().hashCode() : 0 );
    return result;
  }

  public AudienceCriteriaCharacteristic deepCopy()
  {
    AudienceCriteriaCharacteristic clone = new AudienceCriteriaCharacteristic();
    clone.setAudienceCriteria( getAudienceCriteria() );
    clone.setCharacteristic( getCharacteristic() );
    clone.setCharacteristicValue( getCharacteristicValue() );
    clone.setSearchType( getSearchType() );
    clone.setId( getId() );

    return clone;
  }

  public void setSearchType( String searchType )
  {
    this.searchType = searchType;
  }

  public String getSearchType()
  {
    return searchType;
  }

}

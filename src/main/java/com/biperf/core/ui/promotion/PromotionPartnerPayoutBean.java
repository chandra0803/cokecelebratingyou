/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionPartnerPayoutBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;

/**
 * PromotionPartnerPayoutBean.
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
 * <td>reddy</td>
 * <td>Feb 26, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionPartnerPayoutBean implements Serializable, Comparable, Cloneable
{
  private String partnerAwardAmount;
  private Long goalLevelId;
  private int sequenceNumber;
  private String name;
  private String description;
  private String goalLevelcmAssetCode;
  private String nameKey;
  private String descriptionKey;

  public Long getGoalLevelId()
  {
    return goalLevelId;
  }

  public void setGoalLevelId( Long goalLevelId )
  {
    this.goalLevelId = goalLevelId;
  }

  public String getPartnerAwardAmount()
  {
    return partnerAwardAmount;
  }

  public void setPartnerAwardAmount( String partnerAwardAmount )
  {
    this.partnerAwardAmount = partnerAwardAmount;
  }

  public PromotionPartnerPayoutBean()
  {
    partnerAwardAmount = "";

  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public Object clone()
  {
    PromotionPartnerPayoutBean clonedObject = null;

    try
    {
      clonedObject = (PromotionPartnerPayoutBean)super.clone();
    }
    catch( CloneNotSupportedException e )
    {
      // This exception will never be throw because this class implements the interface
      // "Cloneable."
    }

    return clonedObject;
  }

  /**
   * Overridden from @see java.lang.Comparable#compareTo(java.lang.Object)
   * @param arg0
   * @return
   */
  public int compareTo( Object anotherPromoPartnerLevel )
  {
    if ( ! ( anotherPromoPartnerLevel instanceof PromotionPartnerPayoutBean ) )
    {
      throw new ClassCastException( "PromotionPartnerPayoutBean expected" );
    }
    PromotionPartnerPayoutBean comparePromoPartnerLevel = (PromotionPartnerPayoutBean)anotherPromoPartnerLevel;

    return this.sequenceNumber - comparePromoPartnerLevel.getSequenceNumber();
  }

  /**
   * Overridden from @see java.lang.Object#hashCode()
   * @return
   */
  public int hashCode()
  {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ( goalLevelId == null ? 0 : goalLevelId.hashCode() );
    result = PRIME * result + sequenceNumber;
    return result;
  }

  /**
   * Overridden from @see java.lang.Object#equals(java.lang.Object)
   * @param obj
   * @return
   */
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
    final PromotionPartnerPayoutBean other = (PromotionPartnerPayoutBean)obj;
    if ( goalLevelId == null )
    {
      if ( other.goalLevelId != null )
      {
        return false;
      }
    }
    else if ( !goalLevelId.equals( other.goalLevelId ) )
    {
      return false;
    }
    if ( sequenceNumber != other.sequenceNumber )
    {
      return false;
    }
    return true;
  }

  public String getGoalLevelcmAssetCode()
  {
    return goalLevelcmAssetCode;
  }

  public void setGoalLevelcmAssetCode( String goalLevelcmAssetCode )
  {
    this.goalLevelcmAssetCode = goalLevelcmAssetCode;
  }

  public String getNameKey()
  {
    return nameKey;
  }

  public void setNameKey( String nameKey )
  {
    this.nameKey = nameKey;
  }

  public String getDescriptionKey()
  {
    return descriptionKey;
  }

  public void setDescriptionKey( String descriptionKey )
  {
    this.descriptionKey = descriptionKey;
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionGoalPayoutLevelFormBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;

/**
 * PromotionPayoutFormBean.
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
 * <td>December 15, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionGoalPayoutLevelFormBean implements Serializable, Comparable<PromotionGoalPayoutLevelFormBean>, Cloneable
{
  private static final long serialVersionUID = 1L;
  private Long goalLevelId;
  private int sequenceNumber;
  private String name;
  private String description;
  private String nameKey;
  private String descriptionKey;
  private String achievementAmount;
  private String award;
  private String minimumQualifier;
  private String incrementalQuantity;
  private String bonusAward;
  private String maximumPoints;
  private String removeGoal;
  private String managerAward;
  private String partnerAwardAmount;
  private String awardType;
  private String goalLevelcmAssetCode;

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public String getManagerAward()
  {
    return managerAward;
  }

  public void setManagerAward( String managerAward )
  {
    this.managerAward = managerAward;
  }

  public PromotionGoalPayoutLevelFormBean()
  {
    achievementAmount = "";
    award = "";
    minimumQualifier = "";
    incrementalQuantity = "";
    bonusAward = "";
    maximumPoints = "";
  }

  public String getAchievementAmount()
  {
    return achievementAmount;
  }

  public void setAchievementAmount( String achievementAmount )
  {
    this.achievementAmount = achievementAmount;
  }

  public String getAward()
  {
    return award;
  }

  public void setAward( String award )
  {
    this.award = award;
  }

  public String getBonusAward()
  {
    return bonusAward;
  }

  public void setBonusAward( String bonusAward )
  {
    this.bonusAward = bonusAward;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public Long getGoalLevelId()
  {
    return goalLevelId;
  }

  public void setGoalLevelId( Long goalLevelId )
  {
    this.goalLevelId = goalLevelId;
  }

  public String getIncrementalQuantity()
  {
    return incrementalQuantity;
  }

  public void setIncrementalQuantity( String incrementalQuantity )
  {
    this.incrementalQuantity = incrementalQuantity;
  }

  public String getMaximumPoints()
  {
    return maximumPoints;
  }

  public void setMaximumPoints( String maximumPoints )
  {
    this.maximumPoints = maximumPoints;
  }

  public String getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( String minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
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

  public PromotionGoalPayoutLevelFormBean clone()
  {
    PromotionGoalPayoutLevelFormBean clonedObject = null;

    try
    {
      clonedObject = (PromotionGoalPayoutLevelFormBean)super.clone();
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
  public int compareTo( PromotionGoalPayoutLevelFormBean anotherGoalPayoutLevel )
  {
    return this.sequenceNumber - anotherGoalPayoutLevel.getSequenceNumber();
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
    final PromotionGoalPayoutLevelFormBean other = (PromotionGoalPayoutLevelFormBean)obj;
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

  public String getRemoveGoal()
  {
    return removeGoal;
  }

  public void setRemoveGoal( String removeGoal )
  {
    this.removeGoal = removeGoal;
  }

  public String getPartnerAwardAmount()
  {
    return partnerAwardAmount;
  }

  public void setPartnerAwardAmount( String partnerAwardAmount )
  {
    this.partnerAwardAmount = partnerAwardAmount;
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

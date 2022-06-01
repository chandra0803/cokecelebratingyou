/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionManagerOverrideLevelFormBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class PromotionManagerOverrideLevelFormBean implements Serializable, Comparable, Cloneable
{
  private Long overrideLevelId;
  private int sequenceNumber;
  private String name;
  private String description;
  private String award;
  private String teamAchievementPercent;
  private String removeOverrideLevel;
  private String nameKey;
  private String descriptionKey;
  private String goalLevelcmAssetCode;
  private String moStartRank;
  private String moEndRank;
  private String moAwards;
  private List<PromotionManagerOverrideLevelFormBean> managerStackRankPayoutValueList = new ArrayList<PromotionManagerOverrideLevelFormBean>();

  public PromotionManagerOverrideLevelFormBean()
  {
    award = "";
    teamAchievementPercent = "";
  }

  public String getAward()
  {
    return award;
  }

  public void setAward( String award )
  {
    this.award = award;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public Long getOverrideLevelId()
  {
    return overrideLevelId;
  }

  public void setOverrideLevelId( Long overrideLevelId )
  {
    this.overrideLevelId = overrideLevelId;
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

  public String getTeamAchievementPercent()
  {
    return teamAchievementPercent;
  }

  public void setTeamAchievementPercent( String teamAchievementPercent )
  {
    this.teamAchievementPercent = teamAchievementPercent;
  }

  public Object clone()
  {
    PromotionManagerOverrideLevelFormBean clonedObject = null;

    try
    {
      clonedObject = (PromotionManagerOverrideLevelFormBean)super.clone();
      clonedObject.setManagerStackRankPayoutValueList( (List) ( (ArrayList)managerStackRankPayoutValueList ).clone() );
    }
    catch( CloneNotSupportedException e )
    {
      // This exception will never be throw because this class implements the interface
      // "Cloneable."
    }

    return clonedObject;
  }

  public PromotionManagerOverrideLevelFormBean getManagerStackRankPayoutValueList( int index )
  {
    try
    {
      return (PromotionManagerOverrideLevelFormBean)managerStackRankPayoutValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * Overridden from @see java.lang.Comparable#compareTo(java.lang.Object)
   * @param arg0
   * @return
   */
  public int compareTo( Object anotherManagerOverrideLevel )
  {
    if ( ! ( anotherManagerOverrideLevel instanceof PromotionManagerOverrideLevelFormBean ) )
    {
      throw new ClassCastException( "PromotionManagerOverrideLevelFormBean expected" );
    }
    PromotionManagerOverrideLevelFormBean compareManagerOverrideLevel = (PromotionManagerOverrideLevelFormBean)anotherManagerOverrideLevel;

    return this.sequenceNumber - compareManagerOverrideLevel.getSequenceNumber();
  }

  /**
   * Overridden from @see java.lang.Object#hashCode()
   * @return
   */
  public int hashCode()
  {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ( overrideLevelId == null ? 0 : overrideLevelId.hashCode() );
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
    final PromotionManagerOverrideLevelFormBean other = (PromotionManagerOverrideLevelFormBean)obj;
    if ( overrideLevelId == null )
    {
      if ( other.overrideLevelId != null )
      {
        return false;
      }
    }
    else if ( !overrideLevelId.equals( other.overrideLevelId ) )
    {
      return false;
    }
    if ( sequenceNumber != other.sequenceNumber )
    {
      return false;
    }
    return true;
  }

  public String getRemoveOverrideLevel()
  {
    return removeOverrideLevel;
  }

  public void setRemoveOverrideLevel( String removeOverrideLevel )
  {
    this.removeOverrideLevel = removeOverrideLevel;
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

  public String getGoalLevelcmAssetCode()
  {
    return goalLevelcmAssetCode;
  }

  public void setGoalLevelcmAssetCode( String goalLevelcmAssetCode )
  {
    this.goalLevelcmAssetCode = goalLevelcmAssetCode;
  }

  public String getMoStartRank()
  {
    return moStartRank;
  }

  public void setMoStartRank( String moStartRank )
  {
    this.moStartRank = moStartRank;
  }

  public String getMoEndRank()
  {
    return moEndRank;
  }

  public void setMoEndRank( String moEndRank )
  {
    this.moEndRank = moEndRank;
  }

  public String getMoAwards()
  {
    return moAwards;
  }

  public void setMoAwards( String moAwards )
  {
    this.moAwards = moAwards;
  }

  public int getManagerStackRankPayoutValueListCount()
  {
    if ( managerStackRankPayoutValueList == null )
    {
      return 0;
    }

    return managerStackRankPayoutValueList.size();
  }

  public List<PromotionManagerOverrideLevelFormBean> getManagerStackRankPayoutValueList()
  {
    return managerStackRankPayoutValueList;
  }

  public void setManagerStackRankPayoutValueList( List<PromotionManagerOverrideLevelFormBean> managerStackRankPayoutValueList )
  {
    this.managerStackRankPayoutValueList = managerStackRankPayoutValueList;
  }

}

/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/AbstractGoalLevel.java,v $
 */

package com.biperf.core.domain.promotion;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * AbstractGoalLevel.
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
 * <td>Dec 11, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class AbstractGoalLevel extends BaseDomain implements Cloneable
{
  private static final long serialVersionUID = 1L;

  private GoalQuestPromotion promotion;
  private int sequenceNumber;
  private String goalLevelcmAssetCode;
  private String goalLevelNameKey;
  private String goalLevelDescriptionKey;
  private BigDecimal managerAward;

  public String getGoalLevelDescription()
  {
    String goalLevelDescription = CmsResourceBundle.getCmsBundle().getString( getGoalLevelcmAssetCode(), Promotion.CM_GOAL_DESCRIPTION_KEY );
    if ( goalLevelDescription.startsWith( "???" ) )
    {
      goalLevelDescription = "";
    }
    return goalLevelDescription;
  }

  public String getGoalLevelName()
  {
    String goalLevelName = CmsResourceBundle.getCmsBundle().getString( getGoalLevelcmAssetCode(), Promotion.CM_GOALS_KEY );
    if ( goalLevelName.startsWith( "???" ) )
    {
      goalLevelName = "";
    }
    return goalLevelName;
  }

  public GoalQuestPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( GoalQuestPromotion promotion )
  {
    this.promotion = promotion;
  }

  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  /**
   * Overridden from @see java.lang.Object#hashCode()
   * @return
   */
  public int hashCode()
  {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ( goalLevelNameKey == null ? 0 : goalLevelNameKey.hashCode() );
    result = PRIME * result + ( promotion == null ? 0 : promotion.hashCode() );
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
    final AbstractGoalLevel other = (AbstractGoalLevel)obj;
    if ( goalLevelNameKey == null )
    {
      if ( other.goalLevelNameKey != null )
      {
        return false;
      }
    }
    else if ( !goalLevelNameKey.equals( other.goalLevelNameKey ) )
    {
      return false;
    }
    if ( promotion == null )
    {
      if ( other.promotion != null )
      {
        return false;
      }
    }
    else if ( !promotion.equals( other.promotion ) )
    {
      return false;
    }
    if ( sequenceNumber != other.sequenceNumber )
    {
      return false;
    }
    return true;
  }

  public Object clone() throws CloneNotSupportedException
  {
    AbstractGoalLevel clonedGoalLevel = (AbstractGoalLevel)super.clone();
    clonedGoalLevel.setPromotion( null );
    clonedGoalLevel.resetBaseDomain();
    return clonedGoalLevel;
  }

  public BigDecimal getManagerAward()
  {
    return managerAward;
  }

  public void setManagerAward( BigDecimal managerAward )
  {
    this.managerAward = managerAward;
  }

  abstract public boolean isManagerOverrideGoalLevel();

  abstract public boolean isPromotionPartnerPayout();

  public String getGoalLevelcmAssetCode()
  {
    return goalLevelcmAssetCode;
  }

  public void setGoalLevelcmAssetCode( String goalLevelcmAssetCode )
  {
    this.goalLevelcmAssetCode = goalLevelcmAssetCode;
  }

  public String getGoalLevelNameKey()
  {
    return goalLevelNameKey;
  }

  public void setGoalLevelNameKey( String goalLevelNameKey )
  {
    this.goalLevelNameKey = goalLevelNameKey;
  }

  public String getGoalLevelDescriptionKey()
  {
    return goalLevelDescriptionKey;
  }

  public void setGoalLevelDescriptionKey( String goalLevelDescriptionKey )
  {
    this.goalLevelDescriptionKey = goalLevelDescriptionKey;
  }
}

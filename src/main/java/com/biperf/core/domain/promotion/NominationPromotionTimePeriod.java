
package com.biperf.core.domain.promotion;

import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.CmsResourceBundle;

public class NominationPromotionTimePeriod extends BaseDomain implements Cloneable
{
  private static final long serialVersionUID = 80358514083282917L;
  private NominationPromotion nominationPromotion;
  private String timePeriodName;
  private String timePeriodNameAssetCode;
  private Date timePeriodStartDate;
  private Date timePeriodEndDate;
  private Integer maxSubmissionAllowed;
  private Integer maxNominationsAllowed;
  private Integer maxWinsllowed;

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof NominationPromotionTimePeriod ) )
    {
      return false;
    }

    final NominationPromotionTimePeriod other = (NominationPromotionTimePeriod)object;

    if ( getId() == null )
    {
      if ( other.getId() != null )
      {
        return false;
      }
    }
    else if ( !getId().equals( other.getId() ) )
    {
      return false;
    }

    if ( getTimePeriodName() == null )
    {
      if ( other.getTimePeriodName() != null )
      {
        return false;
      }
    }
    else if ( !getTimePeriodName().equals( other.getTimePeriodName() ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 0;
    result = prime * result + ( this.getId() != null ? this.getId().hashCode() : 0 );
    result = prime * result + ( this.getTimePeriodName() != null ? this.getTimePeriodName().hashCode() : 0 );
    return result;
  }

  public NominationPromotionTimePeriod deepCopy()
  {
    NominationPromotionTimePeriod clone = new NominationPromotionTimePeriod();

    clone.setTimePeriodName( this.getTimePeriodName() );
    clone.setTimePeriodNameAssetCode( this.getTimePeriodNameAssetCode() );
    clone.setTimePeriodStartDate( this.getTimePeriodStartDate() );
    clone.setTimePeriodEndDate( this.getTimePeriodEndDate() );
    clone.setMaxSubmissionAllowed( this.getMaxSubmissionAllowed() );
    clone.setMaxNominationsAllowed( this.getMaxNominationsAllowed() );
    clone.setMaxWinsllowed( this.getMaxWinsllowed() );

    return clone;
  }

  public String getTimePeriodName()
  {
    return timePeriodName;
  }

  public void setTimePeriodName( String timePeriodName )
  {
    this.timePeriodName = timePeriodName;
  }

  public Date getTimePeriodStartDate()
  {
    return timePeriodStartDate;
  }

  public void setTimePeriodStartDate( Date timePeriodStartDate )
  {
    this.timePeriodStartDate = timePeriodStartDate;
  }

  public Date getTimePeriodEndDate()
  {
    return timePeriodEndDate;
  }

  public void setTimePeriodEndDate( Date timePeriodEndDate )
  {
    this.timePeriodEndDate = timePeriodEndDate;
  }

  public NominationPromotion getNominationPromotion()
  {
    return nominationPromotion;
  }

  public void setNominationPromotion( NominationPromotion nominationPromotion )
  {
    this.nominationPromotion = nominationPromotion;
  }

  public Integer getMaxSubmissionAllowed()
  {
    return maxSubmissionAllowed;
  }

  public void setMaxSubmissionAllowed( Integer maxSubmissionAllowed )
  {
    this.maxSubmissionAllowed = maxSubmissionAllowed;
  }

  public Integer getMaxNominationsAllowed()
  {
    return maxNominationsAllowed;
  }

  public void setMaxNominationsAllowed( Integer maxNominationsAllowed )
  {
    this.maxNominationsAllowed = maxNominationsAllowed;
  }

  public Integer getMaxWinsllowed()
  {
    return maxWinsllowed;
  }

  public void setMaxWinsllowed( Integer maxWinsllowed )
  {
    this.maxWinsllowed = maxWinsllowed;
  }

  public boolean noStartAndDate()
  {
    return getTimePeriodStartDate() == null && getTimePeriodEndDate() == null;
  }

  public boolean noStartDate()
  {
    return getTimePeriodStartDate() == null;
  }

  public boolean noEndDate()
  {
    return getTimePeriodEndDate() == null;
  }

  public boolean noMaximumLimit()
  {
    return getMaxSubmissionAllowed() == null || getMaxSubmissionAllowed() <= 0;
  }

  public String getTimePeriodNameAssetCode()
  {
    return timePeriodNameAssetCode;
  }

  public void setTimePeriodNameAssetCode( String timePeriodNameAssetCode )
  {
    this.timePeriodNameAssetCode = timePeriodNameAssetCode;
  }

  public String getTimePeriodNameFromCM()
  {
    String timePeriodName = null;
    if ( this.timePeriodNameAssetCode != null )
    {
      timePeriodName = CmsResourceBundle.getCmsBundle().getString( this.timePeriodNameAssetCode, Promotion.PROMOTION_TIME_PERIOD_NAME_KEY_PREFIX );
    }
    return StringEscapeUtils.unescapeHtml4( timePeriodName );
  }

}

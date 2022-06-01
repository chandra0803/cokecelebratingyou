
package com.biperf.core.domain.workhappier;

import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.objectpartners.cms.util.CmsResourceBundle;

public class WorkHappier extends BaseDomain
{
  public static final String HEADLINE_KEY_PREFIX = "HEADLINE";
  public static final String FEELING_KEY_PREFIX = "FEELING";
  public static final String THOUGHTS_KEY_PREFIX = "THOUGHTS";
  public static final String FEELING_WITH_KEY_PREFIX = "FEELING_WITH_PREFIX";

  private String imageName;
  private Long minValue;
  private Long maxValue;
  private String whAssetCode;
  private Set<WorkHappierScore> workHappierScores = new HashSet<WorkHappierScore>();

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof WorkHappier ) )
    {
      return false;
    }

    final WorkHappier other = (WorkHappier)object;

    if ( getId() != null )
    {
      if ( !getId().equals( other.getId() ) )
      {
        return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = 0;
    result += this.getId() != null ? this.getId().hashCode() : 0;
    return result;
  }

  public String getImageName()
  {
    return imageName;
  }

  public void setImageName( String imageName )
  {
    this.imageName = imageName;
  }

  public Long getMinValue()
  {
    return minValue;
  }

  public void setMinValue( Long minValue )
  {
    this.minValue = minValue;
  }

  public Long getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue( Long maxValue )
  {
    this.maxValue = maxValue;
  }

  public String getWhAssetCode()
  {
    return whAssetCode;
  }

  public void setWhAssetCode( String whAssetCode )
  {
    this.whAssetCode = whAssetCode;
  }

  public String getHeadlineFromCM()
  {
    String headline = null;
    if ( this.whAssetCode != null )
    {
      headline = CmsResourceBundle.getCmsBundle().getString( this.whAssetCode, WorkHappier.HEADLINE_KEY_PREFIX );
    }
    return headline;
  }

  public String getFeelingFromCM()
  {
    String feeling = null;
    if ( this.whAssetCode != null )
    {
      feeling = CmsResourceBundle.getCmsBundle().getString( this.whAssetCode, WorkHappier.FEELING_KEY_PREFIX );
    }
    return feeling;
  }

  public String getThoughtsFromCM()
  {
    String thoughts = null;
    if ( this.whAssetCode != null )
    {
      thoughts = CmsResourceBundle.getCmsBundle().getString( this.whAssetCode, WorkHappier.THOUGHTS_KEY_PREFIX );
    }
    return thoughts;
  }

  public String getFeelingWithPrefixFromCM()
  {
    String feelingWithPrefix = null;
    if ( this.whAssetCode != null )
    {
      feelingWithPrefix = CmsResourceBundle.getCmsBundle().getString( this.whAssetCode, WorkHappier.FEELING_WITH_KEY_PREFIX );
    }
    return feelingWithPrefix;
  }

  public Set<WorkHappierScore> getWorkHappierScores()
  {
    return workHappierScores;
  }

  public void setWorkHappierScores( Set<WorkHappierScore> workHappierScores )
  {
    this.workHappierScores = workHappierScores;
  }
}

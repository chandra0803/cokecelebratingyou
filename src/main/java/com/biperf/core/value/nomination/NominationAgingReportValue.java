
package com.biperf.core.value.nomination;

import java.util.ArrayList;
import java.util.List;

/**
 * Value bean for nomination aging report
 */
public class NominationAgingReportValue
{
  private Long promotionId;
  private String promotionName;
  private String timePeriod;
  private List<NominationAgingLevelReportValue> levelData = new ArrayList<>();

  private long totalRecords;
  private long recordSequence;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getTimePeriod()
  {
    return timePeriod;
  }

  public void setTimePeriod( String timePeriod )
  {
    this.timePeriod = timePeriod;
  }

  public List<NominationAgingLevelReportValue> getLevelData()
  {
    return levelData;
  }

  public void setLevelData( List<NominationAgingLevelReportValue> levelData )
  {
    this.levelData = levelData;
  }

  public long getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( long totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public long getRecordSequence()
  {
    return recordSequence;
  }

  public void setRecordSequence( long recordSequence )
  {
    this.recordSequence = recordSequence;
  }

  public static class NominationAgingLevelReportValue
  {
    private Long numberPending;
    private Double averageWait;
    private Long numberWinner;
    private Long numberApproved;
    private Long numberDenied;
    private Long numberMoreInfo;
    private Long numberExpired; // Only used for bar chart

    public Long getNumberPending()
    {
      return numberPending;
    }

    public void setNumberPending( Long numberPending )
    {
      this.numberPending = numberPending;
    }

    public Double getAverageWait()
    {
      return averageWait;
    }

    public void setAverageWait( Double averageWait )
    {
      this.averageWait = averageWait;
    }

    public Long getNumberWinner()
    {
      return numberWinner;
    }

    public void setNumberWinner( Long numberWinner )
    {
      this.numberWinner = numberWinner;
    }

    public Long getNumberApproved()
    {
      return numberApproved;
    }

    public void setNumberApproved( Long numberApproved )
    {
      this.numberApproved = numberApproved;
    }

    public Long getNumberDenied()
    {
      return numberDenied;
    }

    public void setNumberDenied( Long numberDenied )
    {
      this.numberDenied = numberDenied;
    }

    public Long getNumberMoreInfo()
    {
      return numberMoreInfo;
    }

    public void setNumberMoreInfo( Long numberMoreInfo )
    {
      this.numberMoreInfo = numberMoreInfo;
    }

    public Long getNumberExpired()
    {
      return numberExpired;
    }

    public void setNumberExpired( Long numberExpired )
    {
      this.numberExpired = numberExpired;
    }
  }

}

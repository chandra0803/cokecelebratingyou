
package com.biperf.core.ui.nomination.calculator;

import java.util.ArrayList;
import java.util.List;

public class CriteriaInfoBean
{
  private List<String> messages = new ArrayList<String>();
  private List<CriteriaItem> criteria = new ArrayList<CriteriaItem>();
  private AwardRange awardRange;
  private int totalScore;
  private Integer fixedAward;
  private AwardLevel awardLevel;

  public List<String> getMessages()
  {
    return messages;
  }

  public List<CriteriaItem> getCriteria()
  {
    return criteria;
  }

  public void addCriteria( Long criteriaId, int criteriaWeight, int criteriaScore, String criteriaRating, Long criteriaRatingId )
  {
    criteria.add( new CriteriaItem( criteriaId, criteriaWeight, criteriaScore, criteriaRating, criteriaRatingId ) );
  }

  public void setAwardRange( int min, int max )
  {
    awardRange = new AwardRange( min, max );
  }

  public AwardRange getAwardRange()
  {
    return awardRange;
  }

  public int getTotalScore()
  {
    return totalScore;
  }

  public void setTotalScore( int totalScore )
  {
    this.totalScore = totalScore;
  }

  public Integer getFixedAward()
  {
    return fixedAward;
  }

  public void setFixedAward( Integer fixedAward )
  {
    this.fixedAward = fixedAward;
  }

  public void addAwardLevel( Long id, String name, int value )
  {
    awardLevel = new AwardLevel( id, name, value );
  }

  public AwardLevel getAwardLevel()
  {
    return awardLevel;
  }

  public static class CriteriaItem
  {
    private Long criteriaId;
    private int criteriaWeight;
    private int criteriaScore;
    private String criteriaRating;
    private Long criteriaRatingId;

    public CriteriaItem( Long criteriaId, int criteriaWeight, int criteriaScore, String criteriaRating, Long criteriaRatingId )
    {
      this.criteriaId = criteriaId;
      this.criteriaWeight = criteriaWeight;
      this.criteriaScore = criteriaScore;
      this.criteriaRating = criteriaRating;
      this.criteriaRatingId = criteriaRatingId;
    }

    public Long getCriteriaId()
    {
      return criteriaId;
    }

    public String getCriteriaRating()
    {
      return criteriaRating;
    }

    public int getCriteriaScore()
    {
      return criteriaScore;
    }

    public int getCriteriaWeight()
    {
      return criteriaWeight;
    }

    public Long getCriteriaRatingId()
    {
      return criteriaRatingId;
    }
  }

  public static class AwardRange
  {
    private int min;
    private int max;

    public AwardRange( int min, int max )
    {
      this.min = min;
      this.max = max;
    }

    public int getMax()
    {
      return max;
    }

    public int getMin()
    {
      return min;
    }
  }

  public static class AwardLevel
  {
    private Long id;
    private String name;
    private int value;

    public AwardLevel( Long id, String name, int value )
    {
      this.id = id;
      this.name = name;
      this.value = value;
    }

    public Long getId()
    {
      return id;
    }

    public String getName()
    {
      return name;
    }

    public int getValue()
    {
      return value;
    }
  }

}

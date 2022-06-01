/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/sweepstakes/SweepstakesWinnerHistoryForm.java,v $
 */

package com.biperf.core.ui.promotion.sweepstakes;

import java.util.List;

import com.biperf.core.ui.BaseForm;

/**
 * SweepstakesWinnerHistoryForm
 * 
 *
 */
public class SweepstakesWinnerHistoryForm extends BaseForm
{
  public static final String FORM_NAME = "sweepstakesWinnerHistoryForm";

  private String promotionId;
  private String promotionName;
  private String promotionType;
  private String award;
  private String sweepstakesStartDate;
  private String sweepstakesEndDate;
  private String method;
  private List winners;
  private List sweepstakesList;
  private String selectedSweepstakeId;

  public String getSelectedSweepstakeId()
  {
    return selectedSweepstakeId;
  }

  public void setSelectedSweepstakeId( String selectedSweepstakeId )
  {
    this.selectedSweepstakeId = selectedSweepstakeId;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public List getWinners()
  {
    return winners;
  }

  public void setWinners( List winners )
  {
    this.winners = winners;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return A instance of String from list
   */
  public String getWinner( int index )
  {
    try
    {
      return (String)winners.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getSweepstakesEndDate()
  {
    return sweepstakesEndDate;
  }

  public void setSweepstakesEndDate( String sweepstakesEndDate )
  {
    this.sweepstakesEndDate = sweepstakesEndDate;
  }

  public String getSweepstakesStartDate()
  {
    return sweepstakesStartDate;
  }

  public void setSweepstakesStartDate( String sweepstakesStartDate )
  {
    this.sweepstakesStartDate = sweepstakesStartDate;
  }

  public String getAward()
  {
    return award;
  }

  public void setAward( String award )
  {
    this.award = award;
  }

  public List getSweepstakesList()
  {
    return sweepstakesList;
  }

  public void setSweepstakesList( List sweepstakesList )
  {
    this.sweepstakesList = sweepstakesList;
  }

}

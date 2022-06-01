/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.sweepstakes;

import com.biperf.core.ui.BaseFormBean;

/**
 * SweepstakesWinnerFormBean.
 * <p>
 * 
 *
 */
public class SweepstakesWinnerFormBean extends BaseFormBean
{
  String id;
  String description;
  String winnerType;
  String award;
  String sweepstakeId;

  public String getSweepstakeId()
  {
    return sweepstakeId;
  }

  public void setSweepstakeId( String sweepstakeId )
  {
    this.sweepstakeId = sweepstakeId;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getWinnerType()
  {
    return winnerType;
  }

  public void setWinnerType( String winnerType )
  {
    this.winnerType = winnerType;
  }

  public String getAward()
  {
    return award;
  }

  public void setAward( String award )
  {
    this.award = award;
  }
}

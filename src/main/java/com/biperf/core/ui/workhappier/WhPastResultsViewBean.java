/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.workhappier;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author poddutur
 * @since Nov 25, 2015
 */
public class WhPastResultsViewBean
{
  List<WorkHappierScoreViewBean> pastResults = new ArrayList<WorkHappierScoreViewBean>();
  WorkHappinessSliderViewBean happinessSliderOpts = new WorkHappinessSliderViewBean();

  public List<WorkHappierScoreViewBean> getPastResults()
  {
    return pastResults;
  }

  public void setPastResults( List<WorkHappierScoreViewBean> pastResults )
  {
    this.pastResults = pastResults;
  }

  public WorkHappinessSliderViewBean getHappinessSliderOpts()
  {
    return happinessSliderOpts;
  }

  public void setHappinessSliderOpts( WorkHappinessSliderViewBean happinessSliderOpts )
  {
    this.happinessSliderOpts = happinessSliderOpts;
  }
}

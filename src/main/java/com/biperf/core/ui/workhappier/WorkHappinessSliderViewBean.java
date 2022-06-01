/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.workhappier;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author poddutur
 * @since Nov 25, 2015
 */
@JsonInclude( value = Include.NON_NULL )
public class WorkHappinessSliderViewBean
{
  List<WorkHappinessDataViewBean> resultZonesData = new ArrayList<WorkHappinessDataViewBean>();

  public List<WorkHappinessDataViewBean> getResultZonesData()
  {
    return resultZonesData;
  }

  public void setResultZonesData( List<WorkHappinessDataViewBean> resultZonesData )
  {
    this.resultZonesData = resultZonesData;
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.sweepstakes;

import com.biperf.core.ui.BaseFormBean;

/**
 * SweepstakeBean.
 * <p>
 * 
 *
 */
public class SweepstakeBean extends BaseFormBean
{
  String id;
  String description;

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

}

/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.workhappier;

import com.biperf.core.ui.BaseForm;

/**
 * 
 * @author poddutur
 * @since Dec 10, 2015
 */
public class WorkHappierFeedbackForm extends BaseForm
{

  private static final long serialVersionUID = 1L;

  private String message;

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

}

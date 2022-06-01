/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/QuizHistoryForm.java,v $
 */

package com.biperf.core.ui.claim;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;

/*
 * RecognitionHistoryForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Jul
 * 15, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class QuizHistoryForm extends BaseForm
{
  public static final String FORM_NAME = "quizHistoryForm";

  private String method;
  private String promotionId;
  private String submitterId;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private String mode;
  private String chosenPromotionId;
  private String chosenStartDate;
  private String chosenEndDate;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( String submitterId )
  {
    this.submitterId = submitterId;
  }

  public String getMode()
  {
    return mode;
  }

  public void setMode( String mode )
  {
    this.mode = mode;
  }

  public String getChosenEndDate()
  {
    return chosenEndDate;
  }

  public void setChosenEndDate( String chosenEndDate )
  {
    this.chosenEndDate = chosenEndDate;
  }

  public String getChosenPromotionId()
  {
    return chosenPromotionId;
  }

  public void setChosenPromotionId( String chosenPromotionId )
  {
    this.chosenPromotionId = chosenPromotionId;
  }

  public String getChosenStartDate()
  {
    return chosenStartDate;
  }

  public void setChosenStartDate( String chosenStartDate )
  {
    this.chosenStartDate = chosenStartDate;
  }

}

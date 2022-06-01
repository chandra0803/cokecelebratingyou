
package com.biperf.core.ui.profile;

import com.biperf.core.ui.BaseForm;

public class ActivityHistoryForm extends BaseForm
{

  /**
   * The key to the HTTP request attribute that refers to this form.
   */
  public static final String FORM_NAME = "activityHistoryForm";

  private String startDate;

  private String endDate;

  private String mode;

  private String promotionTypeCode;

  private String promotionId;

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getMode()
  {
    return mode;
  }

  public void setMode( String mode )
  {
    this.mode = mode;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
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

}

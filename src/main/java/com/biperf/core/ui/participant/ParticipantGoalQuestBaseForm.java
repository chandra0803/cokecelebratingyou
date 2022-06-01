
package com.biperf.core.ui.participant;

import com.biperf.core.ui.BaseForm;

public class ParticipantGoalQuestBaseForm extends BaseForm
{

  private Long userId;
  private Long promotionBaseId;
  private String method;
  private String newQuantity;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getNewQuantity()
  {
    return newQuantity;
  }

  public void setNewQuantity( String newQuantity )
  {
    this.newQuantity = newQuantity;
  }

  public Long getPromotionBaseId()
  {
    return promotionBaseId;
  }

  public void setPromotionBaseId( Long promotionBaseId )
  {
    this.promotionBaseId = promotionBaseId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public void clearValues()
  {
    this.newQuantity = null;
  }

}

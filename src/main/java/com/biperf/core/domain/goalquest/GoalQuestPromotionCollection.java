
package com.biperf.core.domain.goalquest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class GoalQuestPromotionCollection
{
  private String[] messages = {};
  private java.util.List<PromotionView> promotions;

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public java.util.List<PromotionView> getPromotions()
  {
    return promotions;
  }

  public void setPromotions( java.util.List<PromotionView> promotions )
  {
    this.promotions = promotions;
  }
}

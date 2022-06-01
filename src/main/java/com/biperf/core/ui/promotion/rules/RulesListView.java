
package com.biperf.core.ui.promotion.rules;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author dudam
 * @since Dec 10, 2012
 * @version 1.0
 */
public class RulesListView
{

  private List<RulesView> promotions = new ArrayList<RulesView>();
  private String[] messages = {};

  public List<RulesView> getPromotions()
  {
    return promotions;
  }

  public void setPromotions( List<RulesView> promotions )
  {
    this.promotions = promotions;
  }

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

}

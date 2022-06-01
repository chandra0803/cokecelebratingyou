/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.plateauawards;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author poddutur
 * @since Oct 9, 2015
 */
// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class PlateauAwardsRedeemView
{
  private String[] messages = {};
  private List<PlateauAwardsRedeemBean> promotions;

  public String[] getMessages()
  {
    return messages;
  }

  public void setMessages( String[] messages )
  {
    this.messages = messages;
  }

  public List<PlateauAwardsRedeemBean> getPromotions()
  {
    return promotions;
  }

  public void setPromotions( List<PlateauAwardsRedeemBean> promotions )
  {
    this.promotions = promotions;
  }
}

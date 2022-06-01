/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/Attic/InactivityNotificationForm.java,v $
 */

package com.biperf.core.ui.promotion;

import com.biperf.core.ui.BaseActionForm;

/**
 * PromotionNotificationForm.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class InactivityNotificationForm extends BaseActionForm
{
  private Long promotionId;
  private String messageId;
  private String notificationName;
  private String method;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getMessageId()
  {
    return messageId;
  }

  public void setMessageId( String messageId )
  {
    this.messageId = messageId;
  }

  public String getNotificationName()
  {
    return notificationName;
  }

  public void setNotificationName( String notificationName )
  {
    this.notificationName = notificationName;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

}

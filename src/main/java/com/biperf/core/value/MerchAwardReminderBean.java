/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/MerchAwardReminderBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.util.Date;

import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * MyGoalBean.
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
 * <td>robinsra</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
@SuppressWarnings( "serial" )
public class MerchAwardReminderBean implements Serializable
{
  private Long merchOrderId;
  private Long promotionId;
  private Long participantId;
  private String promotionName;
  private String cmAssetKey;
  private java.util.Date submittedDate;
  private String onlineShoppingUrl;
  private Date expirationDate;
  private Date lastRemindedDate;

  public String getCmAssetKey()
  {
    return cmAssetKey;
  }

  public void setCmAssetKey( String cmAssetKey )
  {
    this.cmAssetKey = cmAssetKey;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public java.util.Date getSubmittedDate()
  {
    return submittedDate;
  }

  public void setSubmittedDate( java.util.Date submittedDate )
  {
    this.submittedDate = submittedDate;
  }

  public Long getMerchOrderId()
  {
    return merchOrderId;
  }

  public void setMerchOrderId( Long merchOrderId )
  {
    this.merchOrderId = merchOrderId;
  }

  public String getOnlineShoppingUrl()
  {
    return onlineShoppingUrl;
  }

  public void setOnlineShoppingUrl( String onlineShoppingUrl )
  {
    this.onlineShoppingUrl = onlineShoppingUrl;
  }

  public String getDisplayLevelName()
  {
    return CmsResourceBundle.getCmsBundle().getString( cmAssetKey, PromoMerchProgramLevel.SPOTLIGHT_LEVEL_NAME_KEY );
  }

  public void setExpirationDate( Date expirationDate )
  {
    this.expirationDate = expirationDate;
  }

  public Date getExpirationDate()
  {
    return expirationDate;
  }

  public Date getLastRemindedDate()
  {
    return lastRemindedDate;
  }

  public void setLastRemindedDate( Date lastRemindedDate )
  {
    this.lastRemindedDate = lastRemindedDate;
  }
}

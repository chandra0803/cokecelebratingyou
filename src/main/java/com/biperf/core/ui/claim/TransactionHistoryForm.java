/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/TransactionHistoryForm.java,v $
 */

package com.biperf.core.ui.claim;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;

/*
 * TransactionHistoryForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Jul 15, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class TransactionHistoryForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The key to the HTTP request attribute that refers to this form.
   */
  public static final String FORM_NAME = "transactionHistoryForm";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String userId;

  private String method;

  private String promotionId;

  private String promotionType;

  private String startDate = DateUtils.displayDateFormatMask;

  private String endDate = DateUtils.displayDateFormatMask;

  private String livePromotionId;

  private String livePromotionType;

  private String liveStartDate;

  private String liveEndDate;

  private String open;

  private String mode;

  // Bug Fix for 17752 add proxyUserId inorder to retrieve the proxyUser's information
  private String proxyUserId;

  /**
   * The prefix of a Content Manager asset code. Used to select nomination and recognition assets
   * from Content Manager.
   */
  private String promotionTypeCode;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getLiveEndDate()
  {
    return liveEndDate;
  }

  public void setLiveEndDate( String liveEndDate )
  {
    this.liveEndDate = liveEndDate;
  }

  public String getLivePromotionId()
  {
    return livePromotionId;
  }

  public void setLivePromotionId( String livePromotionId )
  {
    this.livePromotionId = livePromotionId;
  }

  public String getLivePromotionType()
  {
    return livePromotionType;
  }

  public void setLivePromotionType( String livePromotionType )
  {
    this.livePromotionType = livePromotionType;
  }

  public String getLiveStartDate()
  {
    return liveStartDate;
  }

  public void setLiveStartDate( String liveStartDate )
  {
    this.liveStartDate = liveStartDate;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMode()
  {
    return mode;
  }

  public void setMode( String mode )
  {
    this.mode = mode;
  }

  public String getOpen()
  {
    return open;
  }

  public void setOpen( String open )
  {
    this.open = open;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  // Bug Fix for 17752 both get & set method is defined
  public String getProxyUserId()
  {
    return proxyUserId;
  }

  public void setProxyUserId( String proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
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

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }
}

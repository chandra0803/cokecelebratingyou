/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/PayoutTransactionDetailsForm.java,v $
 */

package com.biperf.core.ui.claim;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PayoutTransactionDetailsForm.
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
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutTransactionDetailsForm extends BaseForm
{
  private Long transactionId;
  private String promotionName;
  private String actionCode;
  private String reasonCode;
  private String comments;
  private String method;
  private Long userId;

  private String claimId;
  private String claimGroupId;
  private String promotionId;
  private String promotionType;
  private String startDate;
  private String endDate;
  private String livePromotionId;
  private String livePromotionType;
  private String liveStartDate;
  private String liveEndDate;
  private boolean open;
  private String mode;
  private String claimNumber;
  private String lastName;
  private String firstName;
  private String middleName;
  private String callingScreen;
  private String dateSubmitted;

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param anActionMapping
   * @param aRequest
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping anActionMapping, HttpServletRequest aRequest )
  {
    ActionErrors actionErrors = super.validate( anActionMapping, aRequest );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( actionCode.equals( JournalStatusType.DENY ) )
    {
      if ( reasonCode == null || reasonCode.equals( "" ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.payout.transaction.detail.REASON_CODE" ) ) );
      }
    }
    return actionErrors;
  } // end validate

  public String getActionCode()
  {
    return actionCode;
  }

  public void setActionCode( String actionCode )
  {
    this.actionCode = actionCode;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getReasonCode()
  {
    return reasonCode;
  }

  public void setReasonCode( String reasonCode )
  {
    this.reasonCode = reasonCode;
  }

  public Long getTransactionId()
  {
    return transactionId;
  }

  public void setTransactionId( Long transactionId )
  {
    this.transactionId = transactionId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public void load( Journal journal, Long userId )
  {
    this.transactionId = journal.getId();
    this.comments = journal.getComments();
    this.userId = userId;

    Promotion promotion = journal.getPromotion();
    if ( promotion != null )
    {
      String promoName = promotion.getName();
      this.promotionName = promoName;
    }
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
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

  public String getpromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public String getClaimId()
  {
    return claimId;
  }

  public void setClaimId( String claimId )
  {
    this.claimId = claimId;
  }

  public String getClaimGroupId()
  {
    return claimGroupId;
  }

  public void setClaimGroupId( String claimGroupId )
  {
    this.claimGroupId = claimGroupId;
  }

  public String getClaimNumber()
  {
    return claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getCallingScreen()
  {
    return callingScreen;
  }

  public void setCallingScreen( String callingScreen )
  {
    this.callingScreen = callingScreen;
  }

  public String getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( String dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
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

  public String getMode()
  {
    return mode;
  }

  public void setMode( String mode )
  {
    this.mode = mode;
  }

  public boolean getOpen()
  {
    return open;
  }

  public void setOpen( boolean open )
  {
    this.open = open;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

}

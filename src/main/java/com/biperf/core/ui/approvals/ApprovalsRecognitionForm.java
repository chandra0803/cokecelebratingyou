/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsRecognitionForm.java,v $
 */

package com.biperf.core.ui.approvals;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * RecognitionApprovalForm.
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
 * <td>zahler</td>
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsRecognitionForm implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Long promotionId;
  private Long claimRecipientId;
  private Long awardAmount;
  private Long awardLevelId;
  private PromoMerchProgramLevel programLevel;
  private String approvalStatusType;
  private String holdPromotionApprovalOptionReasonType;
  private String denyPromotionApprovalOptionReasonType;
  private String page;

  /**
   * 
   */
  public ApprovalsRecognitionForm()
  {
    super();
  }

  public ApprovalsRecognitionForm( Long promotionId,
                                   Long claimRecipientId,
                                   Long awardAmount,
                                   String type,
                                   String holdPromotionApprovalOptionReasonType,
                                   String denyPromotionApprovalOptionReasonType,
                                   PromoMerchProgramLevel programLevel,
                                   String page )
  {
    super();
    this.promotionId = promotionId;
    this.claimRecipientId = claimRecipientId;
    this.awardAmount = awardAmount;
    approvalStatusType = type;
    this.programLevel = programLevel;
    this.holdPromotionApprovalOptionReasonType = holdPromotionApprovalOptionReasonType;
    this.denyPromotionApprovalOptionReasonType = denyPromotionApprovalOptionReasonType;
    this.page = page;
  }

  public String getApprovalStatusTypeName()
  {
    return StringUtils.isBlank( approvalStatusType ) ? null : ApprovalStatusType.lookup( approvalStatusType ).getName();
  }

  /**
   * @return value of approvalStatusType property
   */
  public String getApprovalStatusType()
  {
    return approvalStatusType;
  }

  /**
   * @param approvalStatusType value for approvalStatusType property
   */
  public void setApprovalStatusType( String approvalStatusType )
  {
    this.approvalStatusType = approvalStatusType;
  }

  /**
   * @return value of denyPromotionApprovalOptionReasonType property
   */
  public String getDenyPromotionApprovalOptionReasonType()
  {
    return denyPromotionApprovalOptionReasonType;
  }

  /**
   * @param denyPromotionApprovalOptionReasonType value for denyPromotionApprovalOptionReasonType
   *          property
   */
  public void setDenyPromotionApprovalOptionReasonType( String denyPromotionApprovalOptionReasonType )
  {
    this.denyPromotionApprovalOptionReasonType = denyPromotionApprovalOptionReasonType;
  }

  /**
   * @return value of holdPromotionApprovalOptionReasonType property
   */
  public String getHoldPromotionApprovalOptionReasonType()
  {
    return holdPromotionApprovalOptionReasonType;
  }

  /**
   * @param holdPromotionApprovalOptionReasonType value for holdPromotionApprovalOptionReasonType
   *          property
   */
  public void setHoldPromotionApprovalOptionReasonType( String holdPromotionApprovalOptionReasonType )
  {
    this.holdPromotionApprovalOptionReasonType = holdPromotionApprovalOptionReasonType;
  }

  /**
   * @return Long
   */
  public Long getAwardAmount()
  {
    return awardAmount;
  }

  /**
   * @param awardAmount
   */
  public void setAwardAmount( Long awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public void setAwardLevelId( Long awardLevelId )
  {
    this.awardLevelId = awardLevelId;
  }

  public Long getAwardLevelId()
  {
    return awardLevelId;
  }

  /**
   * @return Long
   */
  public Long getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId
   */
  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setClaimRecipientId( Long claimRecipientId )
  {
    this.claimRecipientId = claimRecipientId;
  }

  public Long getClaimRecipientId()
  {
    return claimRecipientId;
  }

  /**
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();

    String approvalStatusType = getApprovalStatusType();
    if ( approvalStatusType != null && approvalStatusType.equals( ApprovalStatusType.DENIED ) && StringUtils.isBlank( getDenyPromotionApprovalOptionReasonType() ) )
    {
      // deny selected and no reason code given
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "recognition.approval.list", "REASON_CODE_REQUIRED_ERROR" ) ) );
    }
    else if ( approvalStatusType != null && approvalStatusType.equals( ApprovalStatusType.HOLD ) && StringUtils.isBlank( getHoldPromotionApprovalOptionReasonType() ) )
    {
      // hold selected and no reason code given
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "recognition.approval.list", "REASON_CODE_REQUIRED_ERROR" ) ) );
    }
    else if ( approvalStatusType != null && !approvalStatusType.equals( ApprovalStatusType.DENIED ) )
    {
      RecognitionPromotion promotion = (RecognitionPromotion)getPromotionService().getPromotionById( getPromotionId() );
      if ( !promotion.isAwardAmountTypeFixed() && promotion.getAwardAmountMax() != null && promotion.getAwardAmountMin() != null && awardAmount != null )
      {
        long awardAmountMin = promotion.getAwardAmountMin().longValue();
        long awardAmountMax = promotion.getAwardAmountMax().longValue();

        if ( awardAmount.longValue() < awardAmountMin || awardAmount.longValue() > awardAmountMax )
        {
          this.approvalStatusType = ApprovalStatusType.PENDING;
          actionErrors.add( "awardAmountTypeFixed", new ActionMessage( "nomination.approval.list.AMOUNT_RANGES_ERROR", promotion.getAwardAmountMin(), promotion.getAwardAmountMax() ) );
        }
      }

      if ( page != null && page.equals( ApprovalsRecognitionDetailsForm.RECOGNITION_APPROVAL_DETAILS_FORM ) && promotion.getCalculator() != null
          && promotion.getCalculator().getCalculatorAwardType().isRangeAward() && approvalStatusType != null && approvalStatusType.equals( ApprovalStatusType.APPROVED ) )
      {
        long awardAmountMin = Long.parseLong( request.getParameter( "awardAmountMin" ) );
        long awardAmountMax = Long.parseLong( request.getParameter( "awardAmountMax" ) );

        if ( awardAmount.longValue() < awardAmountMin || awardAmount.longValue() > awardAmountMax )
        {
          this.approvalStatusType = ApprovalStatusType.PENDING;
          actionErrors.add( "awardAmountTypeFixed", new ActionMessage( "nomination.approval.list.AMOUNT_RANGES_ERROR", String.valueOf( awardAmountMin ), String.valueOf( awardAmountMax ) ) );
        }
      }
    }

    return actionErrors;
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  public PromoMerchProgramLevel getProgramLevel()
  {
    return programLevel;
  }

  public void setProgramLevel( PromoMerchProgramLevel programLevel )
  {
    this.programLevel = programLevel;
  }

  public String getPage()
  {
    return page;
  }

  public void setPage( String page )
  {
    this.page = page;
  }
}

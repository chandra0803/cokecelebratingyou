/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsRecognitionListForm.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ListPageInfo;

/**
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
 * <td></td>
 * <td></td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class ApprovalsRecognitionListForm extends BaseForm
{
  private static final long serialVersionUID = 1L;
  private static final Log LOG = LogFactory.getLog( ApprovalsRecognitionListForm.class );

  private String method;
  private String promotionId;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private String scoreLabel;
  private static String RECOGNITION_APPROVAL_LIST_FORM = "list";

  public String getFormName()
  {
    return RECOGNITION_APPROVAL_LIST_FORM;
  }

  private Map<String, ApprovalsRecognitionForm> recognitionApprovalFormByClaimRecipientIdString = new HashMap<String, ApprovalsRecognitionForm>();

  private ListPageInfo<AbstractRecognitionClaim> listPageInfo;
  private Long requestedPage;

  /**
   *
   */
  public ApprovalsRecognitionListForm()
  {
    super();
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

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Map<String, ApprovalsRecognitionForm> getRecognitionApprovalFormByClaimRecipientIdString()
  {
    return recognitionApprovalFormByClaimRecipientIdString;
  }

  public void setRecognitionApprovalFormByClaimRecipientIdString( Map<String, ApprovalsRecognitionForm> recognitionApprovalFormByClaimRecipientIdString )
  {
    this.recognitionApprovalFormByClaimRecipientIdString = recognitionApprovalFormByClaimRecipientIdString;
  }

  public void addRecognitionApprovalFormByClaimRecipientIdString( String claimRecipientIdString, ApprovalsRecognitionForm recognitionApprovalForm )
  {
    recognitionApprovalFormByClaimRecipientIdString.put( claimRecipientIdString, recognitionApprovalForm );
  }

  /**
   * @param promotionClaimsValueList
   */
  public void load( List<AbstractRecognitionClaim> approvablesList )
  {
    for ( AbstractRecognitionClaim claim : approvablesList )
    {
      // scoreLabel = ( (RecognitionPromotion) (claim.getPromotion())).getCalculator();
      for ( ClaimRecipient claimRecipientPlaceholder : claim.getClaimRecipients() )
      {
        ApprovalStatusType approvalStatusType = claimRecipientPlaceholder.getApprovalStatusType();
        String approvalStatusTypeCode = approvalStatusType != null ? approvalStatusType.getCode() : null;

        PromotionApprovalOptionReasonType promotionApprovalOptionReasonType = claimRecipientPlaceholder.getPromotionApprovalOptionReasonType();

        String holdPromotionApprovalOptionReasonTypeCode = null;
        String denyPromotionApprovalOptionReasonTypeCode = null;
        if ( approvalStatusTypeCode != null && promotionApprovalOptionReasonType != null )
        {
          if ( approvalStatusTypeCode.equals( ApprovalStatusType.DENIED ) )
          {
            denyPromotionApprovalOptionReasonTypeCode = promotionApprovalOptionReasonType.getCode();
          }
          else
          {
            holdPromotionApprovalOptionReasonTypeCode = promotionApprovalOptionReasonType.getCode();
          }
        }
        PromoMerchProgramLevel level = claimRecipientPlaceholder.getPromoMerchProgramLevel();
        if ( level != null )
        {
          List<PromoMerchCountry> countryList = new ArrayList<PromoMerchCountry>();
          countryList.add( level.getPromoMerchCountry() );
          try
          {
            getMerchLevelService().mergeMerchLevelWithOMList( countryList );
          }
          catch( ServiceErrorException see )
          {
            LOG.error( "Could not able to get max value for level Id " + level.getId(), see );
          }
        }
        ApprovalsRecognitionForm recognitionApprovalForm = new ApprovalsRecognitionForm( claim.getPromotion().getId(),
                                                                                         claimRecipientPlaceholder.getId(),
                                                                                         claimRecipientPlaceholder.getAwardQuantity(),
                                                                                         approvalStatusTypeCode,
                                                                                         holdPromotionApprovalOptionReasonTypeCode,
                                                                                         denyPromotionApprovalOptionReasonTypeCode,
                                                                                         level,
                                                                                         RECOGNITION_APPROVAL_LIST_FORM );

        addRecognitionApprovalFormByClaimRecipientIdString( claimRecipientPlaceholder.getId().toString(), recognitionApprovalForm );
      }
    }

  }

  /**
   * Populate the claim with the recognition approval details.
   *
   * @param promotionClaimsValueList
   * @param approver
   */
  public void populateRecognitionRecipientDomainObjects( List<AbstractRecognitionClaim> approvableList, User approver )
  {
    Map<String, ApprovalsRecognitionForm> recognitionApprovalFormByClaimRecipientIdStringCopy = new HashMap<String, ApprovalsRecognitionForm>( recognitionApprovalFormByClaimRecipientIdString );

    for ( AbstractRecognitionClaim claim : approvableList )
    {
      for ( ClaimRecipient claimRecipientPlaceholder : claim.getClaimRecipients() )
      {
        String claimProductIdString = claimRecipientPlaceholder.getId().toString();
        ApprovalsRecognitionForm recognitionApprovalForm = recognitionApprovalFormByClaimRecipientIdStringCopy.get( claimProductIdString );

        if ( recognitionApprovalForm == null )
        {
          throw new BeaconRuntimeException( "form recognition recipient Id not found. recognition recipient Id: " + claimProductIdString );
        }

        // only fetch the PromoMerchProgramLevel if required
        if ( recognitionApprovalForm.getProgramLevel() != null )
        {
          claimRecipientPlaceholder.setPromoMerchProgramLevel( getMerchLevelService().getPromoMerchProgramLevelById( recognitionApprovalForm.getProgramLevel().getId() ) );
        }

        // Remove so we can confirm all are used up.
        recognitionApprovalFormByClaimRecipientIdStringCopy.remove( claimProductIdString );

        String approvalStatusTypeCode = recognitionApprovalForm.getApprovalStatusType();
        String holdPromotionApprovalOptionReasonTypeCode = recognitionApprovalForm.getHoldPromotionApprovalOptionReasonType();
        String denyPromotionApprovalOptionReasonTypeCode = recognitionApprovalForm.getDenyPromotionApprovalOptionReasonType();

        ClaimApproveUtils.setClaimItemApproverDetails( approver,
                                                       approvalStatusTypeCode,
                                                       holdPromotionApprovalOptionReasonTypeCode,
                                                       denyPromotionApprovalOptionReasonTypeCode,
                                                       claimRecipientPlaceholder );
      }
    }

    if ( !recognitionApprovalFormByClaimRecipientIdStringCopy.isEmpty() )
    {
      throw new BeaconRuntimeException( "form recognition recipient remaining after matching with db entries, already deleted?? Count" + recognitionApprovalFormByClaimRecipientIdStringCopy.size() );
    }

  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors.isEmpty() )
    {
      if ( ApprovalsRecognitionListAction.DISPLAY_METHOD.equals( method ) )
      {
        Date formStartDate = DateUtils.toDate( this.startDate );
        Date formEndDate = DateUtils.toDate( this.endDate );
        if ( formStartDate != null && formEndDate != null )
        {
          if ( formEndDate.before( formStartDate ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "recognition.approval.list.DATE_RANGE_INVALID" ) );
          }
        }
      }
      else
      {
        for ( String claimRecipientIdString : recognitionApprovalFormByClaimRecipientIdString.keySet() )
        {
          ApprovalsRecognitionForm recognitionApprovalForm = recognitionApprovalFormByClaimRecipientIdString.get( claimRecipientIdString );
          ActionErrors subFormErrors = recognitionApprovalForm.validate( mapping, request );
          if ( !subFormErrors.isEmpty() )
          {
            actionErrors = subFormErrors;
            break;
          }
        }
      }
    }

    return actionErrors;
  }

  public String getScoreLabel()
  {
    return scoreLabel;
  }

  public void setScoreLabel( String scoreLabel )
  {
    this.scoreLabel = scoreLabel;
  }

  public MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)BeanLocator.getBean( MerchLevelService.BEAN_NAME );
  }

  public void setListPageInfo( ListPageInfo<AbstractRecognitionClaim> listPageInfo )
  {
    this.listPageInfo = listPageInfo;
  }

  public ListPageInfo<AbstractRecognitionClaim> getListPageInfo()
  {
    return listPageInfo;
  }

  public void setRequestedPage( Long requestedPage )
  {
    this.requestedPage = requestedPage;
  }

  public Long getRequestedPage()
  {
    return requestedPage;
  }
}

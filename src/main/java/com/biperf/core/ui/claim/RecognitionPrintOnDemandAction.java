/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/RecognitionPrintOnDemandAction.java,v $
 */

package com.biperf.core.ui.claim;

import java.awt.Image;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.email.PersonalizationService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.reports.ReportsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.reports.ReportsUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.HtmlUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.objectpartners.cms.domain.Content;

/**
 * RecognitionPrintOnDemandAction
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Todd Meadows</td>
 * <td>Dec 04, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author meadows
 */
public class RecognitionPrintOnDemandAction extends BaseDispatchAction
{
  /**
   * to prepare the recognition print on demand jasper report
   */

  /**
   * Overridden from @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    List claimIdList = (List)request.getAttribute( "claimIds" );
    if ( claimIdList == null )
    {
      Map clientStateMap = ClientStateUtils.getClientStateMap( request );
      claimIdList = (List)ClientStateUtils.getParameterValueAsObject( request, clientStateMap, "claimIds" );
    }
    request.setAttribute( "reportFormat", ReportsUtils.FORMAT_PDF );
    Map reportParameters = new HashMap();
    request.setAttribute( "reportParameters", reportParameters );
    return mapping.findForward( "display_non_html_cert" );
  }

  private String getMessageText( Message message, Map reportMap )
  {
    String localeCode = getSystemVariableService().getDefaultLanguage().getStringVal();
    Map dataMap = new HashMap();
    dataMap.put( "sender", reportMap.get( "GIVER_NAME" ) );
    dataMap.put( "programName", reportMap.get( "PROMOTION_NAME" ) );
    if ( StringUtils.isNotBlank( (String)reportMap.get( "BEHAVIOR" ) ) )
    {
      dataMap.put( "category", PromoRecognitionBehaviorType.lookup( (String)reportMap.get( "BEHAVIOR" ) ).getName() );
    }
    if ( StringUtils.isNotBlank( (String)reportMap.get( "COMMENTS" ) ) )
    {
      dataMap.put( "message", HtmlUtils.removeFormatting( (String)reportMap.get( "COMMENTS" ) ) );
    }
    if ( StringUtils.isNotBlank( (String)reportMap.get( "LEVEL_LABEL" ) ) )
    {
      dataMap.put( "levelLabel", reportMap.get( "LEVEL_LABEL" ) );
    }
    if ( StringUtils.isNotBlank( (String)reportMap.get( "PRODUCT_DESCRIPTION" ) ) )
    {
      dataMap.put( "itemDescription", reportMap.get( "PRODUCT_DESCRIPTION" ) );
    }
    Long claimId = (Long)reportMap.get( "CLAIM_ID" );
    Long claimItemId = (Long)reportMap.get( "CLAIM_ITEM_ID" );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    RecognitionClaim claim = (RecognitionClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
    if ( claim != null )
    {
      dataMap.put( "claimNumber", claim.getClaimNumber() );
      AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)claim.getPromotion();
      // If sweepstakes is active and the Reciever is eligible.
      if ( promotion.isSweepstakesActive() && ( promotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_COMBINED_CODE )
          || promotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE )
          || promotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.RECEIVERS_ONLY_CODE ) ) )
      {
        dataMap.put( "showSweeps", "TRUE" );
      }
      ClaimRecipient claimRecipient = claim.getClaimRecipient( claimItemId );
      if ( claimRecipient != null )
      {
        if ( claimRecipient.getRecipient() != null && claimRecipient.getRecipient().getLanguageType() != null )
        {
          localeCode = claimRecipient.getRecipient().getLanguageType().getCode();
        }
        dataMap.put( "recipientName", claimRecipient.getFirstName() + " " + claimRecipient.getLastName() );

        if ( promotion.isAwardActive() && !promotion.getAwardType().isMerchandiseAwardType() && claimRecipient.getAwardQuantity() != null && claimRecipient.getAwardQuantity().longValue() > 0 )
        {
          Long awardQuantity = claimRecipient.getAwardQuantity();
          if ( awardQuantity.longValue() > 1 )
          {
            dataMap.put( "manyAwardAmount", "TRUE" );
          }
          String tempAwardQuantity = NumberFormatUtil.getUserLocaleBasedNumberFormat( claimRecipient.getAwardQuantity(), LocaleUtils.getLocale( localeCode ) );
          dataMap.put( "awardAmount", String.valueOf( tempAwardQuantity ) );
          dataMap.put( "mediaType", promotion.getAwardType().getAbbr() );
        }

      }
    }
    if ( StringUtils.isNotBlank( (String)reportMap.get( "REFERENCE_NUMBER" ) ) )
    {
      dataMap.put( "referenceNumber", reportMap.get( "REFERENCE_NUMBER" ) );
    }
    if ( StringUtils.isNotBlank( (String)reportMap.get( "ORDER_NUMBER" ) ) )
    {
      dataMap.put( "orderNumber", reportMap.get( "ORDER_NUMBER" ) );
    }
    if ( StringUtils.isNotBlank( (String)reportMap.get( "GIFT_CODE" ) ) )
    {
      dataMap.put( "giftCode", reportMap.get( "GIFT_CODE" ) );
    }

    dataMap.put( "expirationDate", DateUtils.toDisplayString( (Date)reportMap.get( "EXPIRATION_DATE" ), LocaleUtils.getLocale( localeCode ) ) );

    dataMap.put( "merchlinqUrl", getMerchlinqURL() );
    dataMap.put( "phoneNumber", getMerchlinqPhone() );

    String messageTemplateText = message.getI18nPlainTextBody( localeCode );
    return getPersonalizationService().processMessage( dataMap, "personalizing print on demand", messageTemplateText );

  }

  private Image getCertificateImage( HttpServletRequest request, String certificateId ) throws MalformedURLException
  {
    Content certificateContent = ReportsUtils.getCertificateContent( PromotionType.RECOGNITION, certificateId );
    return null;
  }

  private Image getCardImage( HttpServletRequest request, String cardImageName ) throws MalformedURLException
  {
    return null;
  }

  /**
   * Get the Merchlinq ordering URL for the current environment (dev, qa, preprod, prod)
   * 
   * @return String
   */
  private String getMerchlinqURL()
  {
    StringBuffer systemVariableName = new StringBuffer();
    systemVariableName.append( SystemVariableService.MERCHLINQ_ORDER_URL_PREFIX );
    systemVariableName.append( "." );
    systemVariableName.append( Environment.getEnvironment() );

    PropertySetItem merchlinqUrl = getSystemVariableService().getPropertyByName( systemVariableName.toString() );
    if ( merchlinqUrl != null )
    {
      return merchlinqUrl.getStringVal();
    }
    return "";
  }

  /**
   * Get the Merchlinq ordering Phone#
   * 
   * @return String
   */
  private String getMerchlinqPhone()
  {
    PropertySetItem merchlinqPhone = getSystemVariableService().getPropertyByName( SystemVariableService.MERCHLINQ_ORDER_PHONE );
    if ( merchlinqPhone != null )
    {
      return merchlinqPhone.getStringVal();
    }
    return "";
  }

  private ReportsService getReportsService()
  {
    return (ReportsService)getService( ReportsService.BEAN_NAME );
  }

  private PersonalizationService getPersonalizationService()
  {
    return (PersonalizationService)getService( PersonalizationService.BEAN_NAME );
  }

  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }
}

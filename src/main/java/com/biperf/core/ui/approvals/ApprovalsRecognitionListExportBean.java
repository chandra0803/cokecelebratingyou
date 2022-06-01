/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsRecognitionListExportBean.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.ui.BaseExportBean;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class ApprovalsRecognitionListExportBean extends BaseExportBean<AbstractRecognitionClaim>
{
  private static final Log logger = LogFactory.getLog( ApprovalsRecognitionListExportBean.class );

  @Override
  protected String buildCsvFileName()
  {
    return "recognition_approvals.csv";
  }

  @Override
  protected String buildPdfFileName()
  {
    return "recognition_approvals.pdf";
  }

  @Override
  protected String buildCsvHeader( Promotion promotion, AbstractRecognitionClaim exportItem )
  {
    Content content = CmsUtil.getContentFromReaderObject( ContentReaderManager.getContentReader().getContent( "recognition.approval.list" ) );

    StringBuilder csvHeader = new StringBuilder();
    csvHeader.append( content.getContentDataMap().get( "REFERENCE_NUMBER" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "NOMINEE" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "NOMINEE_MANAGER" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "ORG_NAME" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "PROMOTION_NAME" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "AWARD_LBL" ) );
    csvHeader.append( "," );
    // Client customization for WIP #43735 starts
    if ( promotion.getAdihCashOption() )
    {
      csvHeader.append( content.getContentDataMap().get( "RECIPIENT_CUR" ) );
      csvHeader.append( "," );
      csvHeader.append( content.getContentDataMap().get( "US_AWARD_VAL" ) );
      csvHeader.append( "," );
      csvHeader.append( content.getContentDataMap().get( "DIVISION_NUM" ) );
      csvHeader.append( "," );
    }
    // Client customization for WIP #43735 ends
    csvHeader.append( content.getContentDataMap().get( "NOMINATOR" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "DATE_SUBMITTED" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "STATUS" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "COMMENTS" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "CUSTOM_ITEMS" ) );
    return csvHeader.toString();

  }

  @Override
  protected String buildCsvRow( AbstractRecognitionClaim exportItem )
  {
    Content content = CmsUtil.getContentFromReaderObject( ContentReaderManager.getContentReader().getContent( "recognition.approval.list" ) );
    ClaimRecipient claimRecipient = exportItem.getClaimRecipients().iterator().next();

    // Determine award value
    String awardValue = null;
    if ( exportItem.getPromotion().getAwardType().isMerchandiseAwardType() )
    {
      if ( claimRecipient.getPromoMerchProgramLevel() == null )
      {
        awardValue = (String)content.getContentDataMap().get( "NOT_SELECTED" );
      }
      else
      {
        awardValue = claimRecipient.getPromoMerchProgramLevel().getDisplayLevelName();
      }
    }
    else if ( exportItem.getPromotion().getCalculator() != null )
    {
      if ( claimRecipient.getCalculatorScore() == null )
      {
        awardValue = (String)content.getContentDataMap().get( "NOT_SELECTED" );
      }
      else
      {
        awardValue = String.valueOf( claimRecipient.getCalculatorScore() );
      }
    }   // Client customization for WIP #43735 starts
    else if ( exportItem.getPromotion().getAdihCashOption() )
    {
      awardValue = String.valueOf( claimRecipient.getCustomCashAwardQuantity() );
    }
    // Client customization for WIP #43735 ends
    else if ( claimRecipient.getAwardQuantity() != null )
    {
      awardValue = claimRecipient.getAwardQuantity() + " " + exportItem.getPromotion().getAwardType().getName();
    }

    StringBuffer csvRow = new StringBuffer();
    csvRow.append( "\"" );
    csvRow.append( exportItem.getClaimNumber() );
    csvRow.append( "\",\"" );
    csvRow.append( claimRecipient.getLastName() + "," + claimRecipient.getFirstName() );
    csvRow.append( "\",\"" );
    csvRow.append( buildUserDisplayString( claimRecipient.getNode().getNodeManagersForUser( claimRecipient.getClaim().getSubmitter() ) ) );
    csvRow.append( "\",\"" );
    csvRow.append( exportItem.getNode().getName() );
    csvRow.append( "\",\"" );
    csvRow.append( exportItem.getPromotion().getName() );
    csvRow.append( "\",\"" );
    csvRow.append( awardValue == null ? "" : awardValue );
    // Client customization for WIP #43735 starts
    if ( exportItem.getPromotion().getAdihCashOption() )
    {
      csvRow.append( "\",\"" );
      csvRow.append( claimRecipient.getCashCurrencyCode() );
      csvRow.append( "\",\"" );
      csvRow.append( claimRecipient.getDisplayUSDAwardQuantity() );
      csvRow.append( "\",\"" );
      csvRow.append( claimRecipient.getCashPaxDivisionNumber() );
    }
    // Client customization for WIP #43735 ends
    csvRow.append( "\",\"" );
    csvRow.append( exportItem.getSubmitter().getNameLFMWithComma() );
    csvRow.append( "\",\"" );
    String date = DateUtils.toDisplayString( exportItem.getSubmissionDate() );
    csvRow.append( date );
    csvRow.append( "\",\"" );
    csvRow.append( claimRecipient.getApprovalStatusType().getName() );
    csvRow.append( "\",\"" );
    csvRow.append( StringUtil.skipHTML( exportItem.getSubmitterComments() ) );

    for ( ClaimFormStep claimFormStep : exportItem.getPromotion().getClaimForm().getClaimFormSteps() )
    {
      for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
      {
        csvRow.append( "\",\"" );
        String value = claimFormStepElement.getI18nLabel();
        for ( ClaimElement claimElement : exportItem.getClaimElements() )
        {
          if ( claimElement.getClaimFormStepElement().getId().equals( claimFormStepElement.getId() ) )
          {
            value = value + ": " + StringUtils.defaultString( claimElement.getValue() );
            break;
          }
        }
        csvRow.append( value );
      }
    }
    csvRow.append( "\"" );

    return csvRow.toString();
  }

  protected String buildXMLStringRecognition( List<AbstractRecognitionClaim> approvables )
  {
    Content content = CmsUtil.getContentFromReaderObject( ContentReaderManager.getContentReader().getContent( "recognition.approval.list" ) );
    String promotionName = "";
    String startDate = null;
    String endDate = null;

    if ( !approvables.isEmpty() )
    {
      Approvable approvable = approvables.get( 0 );
      if ( approvable != null && approvable.getPromotion() != null )
      {
        promotionName = approvable.getPromotion().getName();
      }
    }

    if ( !StringUtils.isEmpty( getSubmissionStartDate() ) )
    {
      startDate = getSubmissionStartDate();
    }
    if ( !StringUtils.isEmpty( getSubmissionEndDate() ) )
    {
      endDate = getSubmissionEndDate();
    }

    StringBuilder xmlString = new StringBuilder( "" );
    xmlString.append( "<document size=\"A4\" margin-left=\"25\" margin-right=\"25\" margin-top=\"25\" margin-bottom=\"25\"> " );
    xmlString.append( "<font-def name=\"header\" family=\"Helvetica\" size=\"12\" style=\"bold\" />" );
    xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"8\" style=\"normal\" /> " );
    xmlString.append( "<font-def name=\"small\" family=\"Helvetica\" style=\"normal\" size=\"9\"   />" );
    xmlString.append( "<paragraph font=\"header\">" );
    xmlString.append( StringUtil.escapeXml( promotionName ) );

    try
    {
      if ( !StringUtils.isEmpty( startDate ) )
      {
        xmlString.append( " - " + startDate );
      }
      if ( !StringUtils.isEmpty( endDate ) )
      {
        xmlString.append( " - " + endDate );
      }
    }
    catch( Exception e )
    {
      logger.error( "Error while parsing the submission dates:" + e );
    }
    xmlString.append( "\\n----------------------------------------------------------------------------------------------------------------------------------------" );
    xmlString.append( "</paragraph>" );

    for ( AbstractRecognitionClaim approvable : approvables )
    {
      xmlString.append( "<table width=\"100\" widths=\"25,65\" spacing-before=\"20\" padding=\"10\" padding-top=\"5\" padding-bottom=\"5\">" );
      xmlString.append( "<font name=\"normal\">" );
      Map<String, String> paxdetailMap = new LinkedHashMap<String, String>();
      paxdetailMap = buildMapForPax( content, approvable );
      xmlString = buildTableRowsPdf( xmlString, paxdetailMap );
      xmlString.append( "</font>" );
      xmlString.append( "</table>" );
      // xmlString.append("<new-page/>");
      xmlString.append( "<paragraph font=\"header\">" );
      xmlString.append( "\\n----------------------------------------------------------------------------------------------------------------------------------------" );
      xmlString.append( "</paragraph>" );
    }

    xmlString.append( "</document>" );
    return xmlString.toString();
  }

  private Map buildMapForPax( Content content, AbstractRecognitionClaim approvable )
  {
    Map<String, String> paxdetailMap = new LinkedHashMap<String, String>();
    paxdetailMap = buildRecognitionClaimMap( approvable, content );
    return paxdetailMap;

  }

  private Map buildRecognitionClaimMap( AbstractRecognitionClaim recClaim, Content content )
  {
    Map<String, String> paxdetailMap = new LinkedHashMap<String, String>();
    ClaimRecipient claimRecipient = recClaim.getClaimRecipients().iterator().next();
    ApprovableItem approvableItem = recClaim.getApprovableItems().iterator().next();

    Node recipientNode = null;
    Node submitterNode = null;
    recipientNode = claimRecipient.getNode();
    submitterNode = recClaim.getSubmittersNode();
    paxdetailMap.put( content.getContentDataMap().get( "NOMINEE" ) + "",
                      StringUtil.escapeXml( claimRecipient.getLastName() + ", " + claimRecipient.getFirstName() ) + " | " + StringUtil.escapeXml( recipientNode.getName() ) );

    // Determine award value
    String awardValue = null;
    if ( recClaim.getPromotion().getAwardType().isMerchandiseAwardType() )
    {
      if ( claimRecipient.getPromoMerchProgramLevel() == null )
      {
        awardValue = (String)content.getContentDataMap().get( "NOT_SELECTED" );
      }
      else
      {
        awardValue = claimRecipient.getPromoMerchProgramLevel().getDisplayLevelName();
      }
    }
    else if ( recClaim.getPromotion().getCalculator() != null )
    {
      if ( claimRecipient.getCalculatorScore() == null )
      {
        awardValue = (String)content.getContentDataMap().get( "NOT_SELECTED" );
      }
      else
      {
        awardValue = String.valueOf( claimRecipient.getCalculatorScore() );
      }
    }      // Client customization for WIP #43735 starts
    else if( recClaim.getPromotion().getAdihCashOption() )
    {
      awardValue = claimRecipient.getCustomCashAwardQuantity()+" " + claimRecipient.getCashCurrencyCode();
    }
    // Client customization for WIP #43735 ends
    else if ( claimRecipient.getAwardQuantity() != null )
    {
      awardValue = claimRecipient.getAwardQuantity() + " " + recClaim.getPromotion().getAwardType().getName();
    }	
    if ( awardValue == null || awardValue.equalsIgnoreCase( "null" ) )
    {	
      awardValue = "";
    }
    paxdetailMap.put( content.getContentDataMap().get( "AWARD_LBL" ) + "", awardValue );

    paxdetailMap.put( content.getContentDataMap().get( "PROMOTION_NAME" ) + "", StringUtil.escapeXml( recClaim.getPromotion().getName() ) );
    paxdetailMap.put( content.getContentDataMap().get( "NOMINATOR" ) + "",
                      StringUtil.escapeXml( recClaim.getSubmitter().getLastName() + ", " + recClaim.getSubmitter().getFirstName() ) + " | " + StringUtil.escapeXml( submitterNode.getName() ) );
    paxdetailMap.put( content.getContentDataMap().get( "DATE_SUBMITTED" ) + "", StringUtil.escapeXml( DateUtils.toDisplayString( recClaim.getSubmissionDate() ) ) );
    paxdetailMap.put( content.getContentDataMap().get( "STATUS" ) + "", StringUtil.escapeXml( approvableItem.getApprovalStatusType().getName() ) );

    // custom form elements

    for ( ClaimFormStep claimFormStep : recClaim.getPromotion().getClaimForm().getClaimFormSteps() )
    {
      for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
      {
        String value = "";
        for ( ClaimElement claimElement : recClaim.getClaimElements() )
        {
          if ( claimElement.getClaimFormStepElement().getId().equals( claimFormStepElement.getId() ) )
          {
            value = StringUtils.defaultString( claimElement.getValue() );
            break;
          }
        }
        paxdetailMap.put( StringUtil.escapeXml( claimFormStepElement.getI18nLabel() ), StringUtil.escapeXml( value ) );

      }
    }
    // end of custom form elements

    String comments = StringUtil.escapeXml( StringUtil.skipHTML( recClaim.getSubmitterComments() ) + "" );
    paxdetailMap.put( content.getContentDataMap().get( "COMMENTS" ) + "", comments );

    return paxdetailMap;
  }

  protected String buildXMLString( Promotion promotion, List<Approvable> approvables )
  {
    return null;
  }
}

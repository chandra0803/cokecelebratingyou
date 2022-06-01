/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsNominationListExportBean.java,v $
 */

package com.biperf.core.ui.approvals;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.NominationClaimBehaviors;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.hierarchy.NodeToChildrenAssociationRequest;
import com.biperf.core.service.hierarchy.NodeToUserNodesAssociationRequest;
import com.biperf.core.ui.BaseExportBean;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.BeanLocator;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class ApprovalsNominationListExportBean extends BaseExportBean<Approvable>
{
  private static final Log logger = LogFactory.getLog( ApprovalsNominationListExportBean.class );

  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  @Override
  protected String buildCsvFileName()
  {
    return "Nomination_Approvals.csv";
  }

  @Override
  protected String buildPdfFileName()
  {
    return "Nomination_Approvals.pdf";
  }

  @Override
  protected String buildCsvHeader( Promotion promotion, Approvable exportItem )
  {
    Content content = CmsUtil.getContentFromReaderObject( ContentReaderManager.getContentReader().getContent( "nomination.approval.list" ) );

    StringBuilder csvHeader = new StringBuilder();
    csvHeader.append( content.getContentDataMap().get( "REFERENCE_NUMBER" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "NOMINEE" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "NOMINEE_MANAGER" ) );
    csvHeader.append( "," );
    if ( exportItem instanceof NominationClaim )
    {
      NominationClaim nominationClaim = (NominationClaim)exportItem;
      if ( nominationClaim.hasTeamName() )
      {
        csvHeader.append( content.getContentDataMap().get( "TEAM_NAME" ) );
        csvHeader.append( "," );
      }
    }

    csvHeader.append( content.getContentDataMap().get( "NODE_NAME" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "PROMOTION_NAME" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "NOMINATOR" ) );
    csvHeader.append( "," );

    for ( ClaimFormStep claimFormStep : promotion.getClaimForm().getClaimFormSteps() )
    {
      for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
      {
        csvHeader.append( claimFormStepElement.getI18nLabel() );
        csvHeader.append( "," );
      }
    }

    csvHeader.append( content.getContentDataMap().get( "DATE_SUBMITTED" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "STATUS" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "AWARD_PER_PERSON" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "PROCESS_DATE" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "COMMENTS" ) );
    csvHeader.append( "," );
    csvHeader.append( content.getContentDataMap().get( "BEHAVIOR" ) );

    return csvHeader.toString();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected String buildCsvRow( Approvable exportItem )
  {
    if ( exportItem instanceof NominationClaim )
    {
      NominationClaim nominationClaim = (NominationClaim)exportItem;
      ClaimRecipient claimRecipient = nominationClaim.getClaimRecipients().iterator().next();
      ApprovableItem approvableItem = nominationClaim.getApprovableItems().iterator().next();

      Node recipientNode = null;

      StringBuffer csvRow = new StringBuffer();
      if ( nominationClaim.hasTeamName() )
      {
        for ( ClaimRecipient participant : nominationClaim.getClaimRecipients() )
        {
          recipientNode = participant.getNode();
          AssociationRequestCollection reqs = new AssociationRequestCollection();
          reqs.add( new NodeToUserNodesAssociationRequest() );
          reqs.add( new NodeToChildrenAssociationRequest() );
          recipientNode = getNodeService().getNodeWithAssociationsById( recipientNode.getId(), reqs );
          csvRow.append( "\"" );
          csvRow.append( nominationClaim.getClaimNumber() );
          csvRow.append( "\",\"" );
          csvRow.append( participant.getRecipient().getNameFLNoComma() );
          csvRow.append( "\",\"" );
          csvRow.append( buildUserDisplayString( recipientNode.getUsersByRole( HierarchyRoleType.MANAGER ) ) );
          csvRow.append( "\",\"" );
          csvRow.append( nominationClaim.getTeamName() );
          csvRow.append( "\",\"" );
          csvRow.append( recipientNode.getName() );
          csvRow.append( "\",\"" );
          csvRow.append( nominationClaim.getPromotion().getName() );
          csvRow.append( "\",\"" );
          csvRow.append( nominationClaim.getSubmitter().getNameLFMWithComma() );
          csvRow.append( "\",\"" );

          for ( ClaimFormStep claimFormStep : exportItem.getPromotion().getClaimForm().getClaimFormSteps() )
          {
            for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
            {
              String value = "";
              for ( ClaimElement claimElement : nominationClaim.getClaimElements() )
              {
                if ( claimElement.getClaimFormStepElement().getId().equals( claimFormStepElement.getId() ) )
                {
                  if ( claimFormStepElement.getClaimFormElementType().isMultiSelectField() || claimFormStepElement.getClaimFormElementType().isSelectField() )
                  {
                    value = StringUtils.defaultString( claimElement.getValueName() );
                    break;
                  }
                  else
                  {
                    value = StringUtils.defaultString( claimElement.getValue() ).replace( "\"", "\"\"" );
                    break;
                  }
                }
              }
              csvRow.append( value );
              csvRow.append( "\",\"" );
            }
          }

          csvRow.append( DateUtils.toDisplayString( nominationClaim.getSubmissionDate() ) );
          csvRow.append( "\",\"" );
          csvRow.append( approvableItem.getApprovalStatusType().getName() );
          csvRow.append( "\",\"" );
          csvRow.append( getAwardQuantityString( false, participant, null ) );
          csvRow.append( "\",\"" );
          csvRow.append( ( (ClaimRecipient)approvableItem ).getNotificationDate() == null ? "" : DateUtils.toDisplayString( ( (ClaimRecipient)approvableItem ).getNotificationDate() ) );
          csvRow.append( "\",\"" );
          csvRow.append( ( (String)StringUtil.skipHTML( nominationClaim.getSubmitterComments() ) ).replace( "\"", "\"\"" ) );
          csvRow.append( getBehaviorString( nominationClaim ) );
          csvRow.append( "\"" );
          csvRow.append( "\n" );
        }
      }
      else
      {
        recipientNode = claimRecipient.getNode();
        csvRow.append( "\"" );
        csvRow.append( nominationClaim.getClaimNumber() );
        csvRow.append( "\",\"" );
        csvRow.append( claimRecipient.getRecipientDisplayName() );
        csvRow.append( "\",\"" );
        csvRow.append( buildUserDisplayString( recipientNode.getNodeManagersForUser( claimRecipient.getRecipient() ) ) );
        csvRow.append( "\",\"" );
        csvRow.append( recipientNode.getName() );
        csvRow.append( "\",\"" );
        csvRow.append( nominationClaim.getPromotion().getName() );
        csvRow.append( "\",\"" );
        csvRow.append( nominationClaim.getSubmitter().getNameLFMWithComma() );
        csvRow.append( "\",\"" );

        for ( ClaimFormStep claimFormStep : exportItem.getPromotion().getClaimForm().getClaimFormSteps() )
        {
          for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
          {
            String value = "";
            for ( ClaimElement claimElement : nominationClaim.getClaimElements() )
            {
              if ( claimElement.getClaimFormStepElement().getId().equals( claimFormStepElement.getId() ) )
              {
                if ( claimFormStepElement.getClaimFormElementType().isMultiSelectField() || claimFormStepElement.getClaimFormElementType().isSelectField() )
                {
                  value = StringUtils.defaultString( claimElement.getValueName() );
                  break;
                }
                else
                {
                  value = StringUtils.defaultString( claimElement.getValue() ).replace( "\"", "\"\"" );
                  break;
                }
              }
            }
            csvRow.append( value );
            csvRow.append( "\",\"" );
          }
        }

        csvRow.append( DateUtils.toDisplayString( nominationClaim.getSubmissionDate() ) );
        csvRow.append( "\",\"" );
        csvRow.append( approvableItem.getApprovalStatusType().getName() );
        csvRow.append( "\",\"" );
        csvRow.append( getAwardQuantityString( false, (ClaimRecipient)approvableItem, null ) );
        csvRow.append( "\",\"" );
        csvRow.append( ( (ClaimRecipient)approvableItem ).getNotificationDate() == null ? "" : DateUtils.toDisplayString( ( (ClaimRecipient)approvableItem ).getNotificationDate() ) );
        csvRow.append( "\",\"" );
        csvRow.append( ( (String)StringUtil.replaceApostrophe( StringUtil.skipHTML( (String)StringUtil.escapeHTML( nominationClaim.getSubmitterComments() ) ) ) ).replace( "\"", "\"\"" ) );
        csvRow.append( getBehaviorString( nominationClaim ) );
        csvRow.append( "\"" );
      }

      return csvRow.toString();
    }
    else if ( exportItem instanceof ClaimGroup )
    {
      StringBuffer csvRow = new StringBuffer();
      ClaimGroup claimGroup = (ClaimGroup)exportItem;
      Set<NominationClaim> nomClaims = (Set<NominationClaim>)claimGroup.getClaims();
      for ( NominationClaim firstClaim : nomClaims )
      {
        AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)claimGroup.getPromotion();
        csvRow.append( "\"" );
        csvRow.append( firstClaim.getClaimNumber() );
        csvRow.append( "\",\"" );
        csvRow.append( claimGroup.getParticipant().getNameLFMWithComma() );
        csvRow.append( "\",\"" );
        csvRow.append( buildUserDisplayString( claimGroup.getNode().getNodeManagersForUser( claimGroup.getParticipant() ) ) );
        csvRow.append( "\",\"" );
        csvRow.append( claimGroup.getNode().getName() );
        csvRow.append( "\",\"" );
        csvRow.append( claimGroup.getPromotion().getName() );
        csvRow.append( "\",\"" );
        csvRow.append( firstClaim.getSubmitter().getNameLFMWithComma() );
        csvRow.append( "\",\"" );

        for ( ClaimElement claimElement : firstClaim.getClaimElements() )
        {
          csvRow.append( StringUtils.defaultString( claimElement.getValue(), "" ).replace( "\"", "\"\"" ) );
          csvRow.append( "\",\"" );
        }

        csvRow.append( DateUtils.toDisplayString( firstClaim.getSubmissionDate() ) );
        csvRow.append( "\",\"" );
        csvRow.append( claimGroup.getApprovalStatusType() == null ? "pending" : claimGroup.getApprovalStatusType().getName() );
        csvRow.append( "\",\"" );
        csvRow.append( promotion.isAwardAmountTypeFixed() ? promotion.getAwardAmountFixed() : claimGroup.getAwardQuantity() == null ? "" : claimGroup.getAwardQuantity() );
        csvRow.append( "\",\"" );
        csvRow.append( claimGroup.getNotificationDate() == null ? "" : DateUtils.toDisplayString( claimGroup.getNotificationDate() ) );
        csvRow.append( "\",\"" );
        csvRow.append( StringUtil.skipHTML( firstClaim.getSubmitterComments() ) );
        csvRow.append( getBehaviorString( firstClaim ) );
        csvRow.append( "\"" );
        csvRow.append( "\n" );
      }

      return csvRow.toString();
    }
    return null;

  }

  /**
   * Get the award quantity varying by points and cash
   * Assumes 0 is not an allowed award value
   */
  private String getAwardQuantityString( boolean usingClaimGroup, ClaimRecipient claimRecipient, ClaimGroup claimGroup )
  {
    if ( usingClaimGroup )
    {
      return getAwardQuantityString( claimGroup.getAwardQuantity(), claimGroup.getCashAwardQuantity() );
    }
    else
    {
      return getAwardQuantityString( claimRecipient.getAwardQuantity(), claimRecipient.getCashAwardQuantity() );
    }
  }

  /** Helper for above method */
  private String getAwardQuantityString( Long awardQuantity, BigDecimal cashAwardQuantity )
  {
    if ( awardQuantity != null && awardQuantity > 0 )
    {
      return String.valueOf( awardQuantity );
    }
    else if ( cashAwardQuantity != null && cashAwardQuantity.compareTo( BigDecimal.ZERO ) > 0 )
    {
      return "$" + cashAwardQuantity.toPlainString();
    }

    return "";
  }

  /**
   * Helper method to take all of the behaviors and concatenate them into a comma separated list
   */
  private String getBehaviorString( NominationClaim nominationClaim )
  {
    StringBuilder behaviorString = new StringBuilder( "" );

    if ( nominationClaim.getNominationClaimBehaviors() != null && !nominationClaim.getNominationClaimBehaviors().isEmpty() )
    {
      behaviorString.append( "\",\"" );
      Iterator<NominationClaimBehaviors> behaviorIterator = nominationClaim.getNominationClaimBehaviors().iterator();
      while ( behaviorIterator.hasNext() )
      {
        NominationClaimBehaviors behavior = behaviorIterator.next();
        behaviorString.append( behavior.getBehavior().getName() );
        if ( behaviorIterator.hasNext() )
        {
          behaviorString.append( ", " );
        }
      }
    }

    return behaviorString.toString();
  }

  // TODO: finish
  protected String buildXMLString( Promotion promotion, List<Approvable> approvables )
  {
    NominationPromotion nominationPromotion = (NominationPromotion)promotion;
    StringBuilder xmlString = new StringBuilder();
    Content content = CmsUtil.getContentFromReaderObject( ContentReaderManager.getContentReader().getContent( "nomination.approval.list" ) );

    xmlString.append( "<document size=\"A4\" margin-left=\"25\" margin-right=\"25\" margin-top=\"25\" margin-bottom=\"25\"> " );
    xmlString.append( "<font-def name=\"header\" family=\"Helvetica\" size=\"12\" style=\"bold\" />" );
    xmlString.append( "<font-def name=\"label\" family=\"Helvetica\" size=\"8\" style=\"bold\" /> " );
    xmlString.append( "<font-def name=\"normal\" family=\"Helvetica\" size=\"8\" style=\"normal\" /> " );

    // Print a page for each claim
    Iterator<Approvable> approvableIterator = approvables.iterator();
    while ( approvableIterator.hasNext() )
    {
      Approvable approvable = approvableIterator.next();
      NominationClaim nominationClaim = null;

      boolean usingClaimGroup = false;
      if ( approvable instanceof NominationClaim )
      {
        nominationClaim = (NominationClaim)approvable;
        usingClaimGroup = false;
      }
      else if ( approvable instanceof ClaimGroup )
      {
        nominationClaim = (NominationClaim) ( (ClaimGroup)approvable ).getClaims().iterator().next();
        usingClaimGroup = true;
      }

      // Will always have ApprovableItem when it suffices. Otherwise, ClaimRecipient XOR ClaimGroup
      ApprovableItem nomineeApprovableItem = null;
      ClaimRecipient nomineeClaimRecipient = null;
      ClaimGroup nomineeClaimGroup = null;
      if ( usingClaimGroup )
      {
        nomineeClaimGroup = (ClaimGroup)approvable;
        nomineeApprovableItem = nomineeClaimGroup;
      }
      else
      {
        nomineeClaimRecipient = nominationClaim.getClaimRecipients().iterator().next();
        nomineeApprovableItem = nomineeClaimRecipient;
      }

      Participant nominee = usingClaimGroup ? nomineeClaimGroup.getParticipant() : nomineeClaimRecipient.getRecipient();

      xmlString.append( "<table width=\"100\" widths=\"25,65\" spacing-before=\"20\" padding=\"10\" padding-top=\"5\" padding-bottom=\"5\">" );

      String promotionName = approvable.getPromotion() != null ? approvable.getPromotion().getPromoNameFromCM() : "";
      appendPdfRow( content, xmlString, "PROMOTION_NAME", promotionName );

      // Either nominee information or team member information
      if ( nominationClaim.hasTeamName() )
      {
        String teamName = nominationClaim.getTeamName();
        appendPdfRow( content, xmlString, "TEAM_NAME", teamName );

        xmlString.append( "<table-row>" );
        appendCell( xmlString, String.valueOf( content.getContentDataMap().get( "TEAM_MEMBERS" ) ), "label" );
        Iterator<ClaimRecipient> recipients = nominationClaim.getClaimRecipients().iterator();
        for ( int i = 0; recipients.hasNext(); ++i )
        {
          ClaimRecipient teamRecipient = recipients.next();

          // First row has the 'team members' label - other rows need a blank cell
          if ( i != 0 )
          {
            xmlString.append( "<table-row>" );
            appendCell( xmlString, "", "label" );
          }

          String teamMemberInfo = buildPaxDisplayString( teamRecipient.getRecipient() );
          appendCell( xmlString, teamMemberInfo, "normal" );

          xmlString.append( "</table-row>" );
        }
      }
      else
      {
        String nomineeInfo = buildPaxDisplayString( nominee );
        appendPdfRow( content, xmlString, "NOMINEE_PDF_LABEL", nomineeInfo );
      }

      String dateSubmitted = DateUtils.toDisplayString( nominationClaim.getSubmissionDate() );
      appendPdfRow( content, xmlString, "DATE_SUBMITTED", dateSubmitted );

      String nominatorInfo = buildPaxDisplayString( nominationClaim.getSubmitter() );
      appendPdfRow( content, xmlString, "NOMINATOR_PDF_LABEL", nominatorInfo );

      String levelNumber = String.valueOf( nominationClaim.getApprovalRound() );
      appendPdfRow( content, xmlString, "CURRENT_LEVEL", levelNumber );

      // Most recent date this person won. Only display if they were a winner.
      Date mostRecentWinDate = getClaimService().getMostRecentWinDate( promotion.getId(), nominee.getId(), nominationClaim.getApprovalRound() );
      if ( mostRecentWinDate != null )
      {
        appendPdfRow( content, xmlString, "MOST_RECENT_WIN_DATE", DateUtils.toDisplayString( mostRecentWinDate ) );
      }

      // Time period name for the most recent win
      if ( mostRecentWinDate != null )
      {
        Optional<NominationPromotionTimePeriod> timePeriod = nominationPromotion.getNominationTimePeriods().stream()
            .filter( ( tp ) -> DateUtils.isDateBetween( mostRecentWinDate, tp.getTimePeriodStartDate(), tp.getTimePeriodEndDate() ) ).findFirst();
        if ( timePeriod.isPresent() )
        {
          String timePeriodName = timePeriod.get().getTimePeriodNameFromCM();
          appendPdfRow( content, xmlString, "TIME_PERIOD_NAME", timePeriodName );
        }
      }

      // Award. If winner, show the amount they won.
      ApprovalStatusType approvalStatusType = usingClaimGroup ? nomineeClaimGroup.getApprovalStatusType() : nomineeClaimRecipient.getApprovalStatusType();
      if ( ApprovalStatusType.lookup( ApprovalStatusType.WINNER ).equals( approvalStatusType ) )
      {
        String awardAmount = getAwardQuantityString( usingClaimGroup, nomineeClaimRecipient, nomineeClaimGroup );
        appendPdfRow( content, xmlString, "AWARD", awardAmount );
      }

      appendPdfRow( content, xmlString, "STATUS", approvalStatusType.getName() );

      xmlString.append( "</table>" );

      if ( approvableIterator.hasNext() )
      {
        xmlString.append( "<new-page/>" );
      }
    }

    xmlString.append( "</document>" );

    return xmlString.toString();
  }

  private void appendPdfRow( Content content, StringBuilder xmlString, String labelCmKey, String valueText )
  {
    xmlString.append( "<table-row>" );
    appendCell( xmlString, String.valueOf( content.getContentDataMap().get( labelCmKey ) ), "label" );
    appendCell( xmlString, StringUtils.defaultString( valueText ), "normal" );
    xmlString.append( "</table-row>" );
  }

  private void appendCell( StringBuilder xmlString, String cellValue, String fontName )
  {
    xmlString.append( "<cell valign=\"top\">" );
    xmlString.append( "<font name=\"" + fontName + "\">" );
    xmlString.append( cellValue );
    xmlString.append( "</font>" );
    xmlString.append( "</cell>" );
  }

  /** For PDF: LFM, Flag, Org ID, Org Name, Job Title */
  private String buildPaxDisplayString( Participant participant )
  {
    StringBuilder displayString = new StringBuilder();

    displayString.append( participant.getNameLFMWithComma() );
    displayString.append( " " );
    displayString.append( participant.getPaxJobName() );
    displayString.append( " " );
    displayString.append( participant.getPrimaryCountryCode() ); // TODO: Little flag image rather
                                                                 // than code
    displayString.append( " " );
    displayString.append( participant.getPaxDeptName() );

    return displayString.toString();
  }

  protected String buildXMLStringRecognition( List<AbstractRecognitionClaim> approvables )
  {
    return null;
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)BeanLocator.getBean( ClaimService.BEAN_NAME );
  }

  public void extractAsCsv( HttpServletResponse response, Map resultMap )
  {
    try
    {
      prepareHeader( response, buildCsvFileName(), "csv" );
      OutputStream output = response.getOutputStream();

      StringBuilder sBuf = new StringBuilder();
      List resultList = (List)resultMap.get( "p_out_pend_claim_dtl" );

      if ( /* "00".equals( resultMap.get( OUTPUT_RETURN_CODE ) ) && */ null != resultList && resultList.size() > 1 )
      {
        for ( int i = 0; i < resultList.size(); i++ )
        {
          sBuf.append( resultList.get( i ) ).append( "\n" );
        }
      }

      output.write( new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF } );
      output.write( sBuf.toString().getBytes( Charset.forName( "UTF-8" ) ) );
      output.close();
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
  }

}

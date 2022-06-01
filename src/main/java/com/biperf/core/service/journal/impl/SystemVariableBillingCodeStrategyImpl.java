/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/journal/impl/SystemVariableBillingCodeStrategyImpl.java,v $
 */

package com.biperf.core.service.journal.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.BillCodeNominationType;
import com.biperf.core.domain.enums.BillCodeRecognitionType;
import com.biperf.core.domain.enums.BillCodeSSIType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.journal.JournalBillCode;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.merchandise.MerchOrderBillCode;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.promotion.BillCode;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.PromotionBillCode;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.SweepstakesBillCode;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.journal.DepositBillingCodeStrategy;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PickListValueBean;

/**
 * SystemVariableBillingCodeStrategyImpl.
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
 * <td>Brian Repko</td>
 * <td>Sep 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SystemVariableBillingCodeStrategyImpl implements DepositBillingCodeStrategy
{
  private static final Log log = LogFactory.getLog( SystemVariableBillingCodeStrategyImpl.class );

  private ClaimService claimService;

  private UserService userService;

  public static final String CUSTOM_VALUE = "customValue";
  public static final String DEPT_NAME = "department";
  public static final String ORG_UNIT_NAME = "orgUnitName";
  public static final String COUNTRY_CODE = "countryCode";
  public static final String LOGIN_ID = "userName";

  private String getSwpBillingCodeValue( BillCode sweepstakesBillCode, Journal journal )
  {

    String billingCodeValue = "";
    if ( sweepstakesBillCode.getBillCode().equalsIgnoreCase( CUSTOM_VALUE ) )
    {
      billingCodeValue = getBillingCodeValue( sweepstakesBillCode.getCustomValue() );
    }
    else
    {
      String nodeName = journal.getParticipant().getPrimaryUserNode().getNode().getName();
      billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), sweepstakesBillCode.getBillCode(), nodeName );
    }
    return billingCodeValue;
  }

  private String getBillingCodeValue( BillCode promotionBillCode, Journal journal, String node, Claim claim )
  {
    String billingCodeValue = "";
    if ( promotionBillCode.getBillCode().equalsIgnoreCase( CUSTOM_VALUE ) )
    {
      billingCodeValue = promotionBillCode.getCustomValue();
    }
    else
    {
      if ( journal.getPromotion().isRecognitionPromotion() )
      {
        if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeRecognitionType.GIVER ) )
        {
          node = claim.getNode().getName();
          billingCodeValue = getBillCodeCustomValue( claim.getSubmitter(), promotionBillCode.getBillCode(), node );
        }
        if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeRecognitionType.RECEIVER ) )
        {
          billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), node );
        }
        /* WIP 25127 - Truncating PAX characteristics to 25 characters */
        billingCodeValue = getBillingCodeValue( billingCodeValue );
        /* WIP 25127 - Truncating PAX characteristics to 25 characters */
      }
      else if ( journal.getPromotion().isNominationPromotion() )
      {
        if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeNominationType.NOMINATOR ) )
        {
          node = claim.getNode().getName();
          billingCodeValue = getBillCodeCustomValue( claim.getSubmitter(), promotionBillCode.getBillCode(), node );
        }
        else if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeNominationType.NOMINEE ) )
        {
          billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), node );
        }
      }
      else if ( journal.getPromotion().isSSIPromotion() )
      {
        if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeSSIType.CREATOR ) )
        {
          node = claim.getNode().getName();
          billingCodeValue = getBillCodeCustomValue( claim.getSubmitter(), promotionBillCode.getBillCode(), node );
        }
        else if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeSSIType.PARTICIPANT ) )
        {
          billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), node );
        }
      }
      else if ( journal.getPromotion().isQuizPromotion() )
      {
        node = claim.getNode().getName();
        billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), node );
      }
      // FOR GOALQUEST AND CHALLENGE POINT
      else if ( journal.getPromotion().isGoalQuestOrChallengePointPromotion() )
      {
        node = journal.getParticipant().getPrimaryUserNode().getNode().getName();
        billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), node );
        /* WIP 25127 - Truncating PAX characteristics to 25 characters */
        billingCodeValue = getBillingCodeValue( billingCodeValue );
        /* WIP 25127 - Truncating PAX characteristics to 25 characters */
      }
      else if ( journal.getPromotion().isProductClaimPromotion() )
      {
        billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), node );
      }
      else if ( journal.getPromotion().isThrowdownPromotion() )
      {
        node = journal.getParticipant().getPrimaryUserNode().getNode().getName();
        billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), node );
      }
    }
    return billingCodeValue;
  }

  private String getBadgeBillingCodeValue( PromotionBillCode promotionBillCode, Journal journal )
  {
    String nodeName = "";
    String billingCodeValue = "";
    if ( promotionBillCode.getBillCode().equalsIgnoreCase( CUSTOM_VALUE ) )
    {
      billingCodeValue = promotionBillCode.getCustomValue();
    }
    else
    {
      nodeName = journal.getParticipant().getPrimaryUserNode().getNode().getName();
      billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), nodeName );
    }
    return billingCodeValue;
  }

  private boolean ifBillCodesContains( List<PromotionBillCode> billCodes, String string )
  {
    boolean hasString = false;
    for ( PromotionBillCode billCode : billCodes )
    {
      if ( billCode != null && billCode.getBillCode().equalsIgnoreCase( string ) )
      {
        hasString = true;
        break;
      }
    }
    return hasString;
  }

  private boolean isTrackByBillCodes( List<PromotionBillCode> billCodes, String string )
  {
    for ( PromotionBillCode billCode : billCodes )
    {
      if ( billCode != null && billCode.getTrackBillCodeBy() != null )
      {
        if ( billCode.getTrackBillCodeBy().equalsIgnoreCase( string ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.DepositBillingCodeStrategy#setJournalBillingCodes(com.biperf.core.domain.journal.Journal)
   * @param journal
   */
  public void setJournalBillingCodes( Journal journal )
  {
    if ( Journal.SWEEPSTAKES.equals( journal.getJournalType() ) )
    {
      if ( journal.getPromotion().isSwpBillCodesActive() )
      {
        List<SweepstakesBillCode> sweepstakesBillCodeList = journal.getPromotion().getSweepstakesBillCodes();

        JournalBillCode journalBillCode = null;
        if ( journal.getBillCodes().iterator().hasNext() )
        {
          journalBillCode = journal.getBillCodes().iterator().next();
        }
        else
        {
          journalBillCode = new JournalBillCode();
        }

        Iterator<SweepstakesBillCode> iterator = sweepstakesBillCodeList.iterator();
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode1 = iterator.next();
          if ( sweepstakesBillCode1 != null )
          {
            journalBillCode.setBillCode1( getSwpBillingCodeValue( sweepstakesBillCode1, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode2 = iterator.next();
          if ( sweepstakesBillCode2 != null )
          {
            journalBillCode.setBillCode2( getSwpBillingCodeValue( sweepstakesBillCode2, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode3 = iterator.next();
          if ( sweepstakesBillCode3 != null )
          {
            journalBillCode.setBillCode3( getSwpBillingCodeValue( sweepstakesBillCode3, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode4 = iterator.next();
          if ( sweepstakesBillCode4 != null )
          {
            journalBillCode.setBillCode4( getSwpBillingCodeValue( sweepstakesBillCode4, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode5 = iterator.next();
          if ( sweepstakesBillCode5 != null )
          {
            journalBillCode.setBillCode5( getSwpBillingCodeValue( sweepstakesBillCode5, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode6 = iterator.next();
          if ( sweepstakesBillCode6 != null )
          {
            journalBillCode.setBillCode6( getSwpBillingCodeValue( sweepstakesBillCode6, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode7 = iterator.next();
          if ( sweepstakesBillCode7 != null )
          {
            journalBillCode.setBillCode7( getSwpBillingCodeValue( sweepstakesBillCode7, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode8 = iterator.next();
          if ( sweepstakesBillCode8 != null )
          {
            journalBillCode.setBillCode8( getSwpBillingCodeValue( sweepstakesBillCode8, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode9 = iterator.next();
          if ( sweepstakesBillCode9 != null )
          {
            journalBillCode.setBillCode9( getSwpBillingCodeValue( sweepstakesBillCode9, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          SweepstakesBillCode sweepstakesBillCode10 = iterator.next();
          if ( sweepstakesBillCode10 != null )
          {
            journalBillCode.setBillCode10( getSwpBillingCodeValue( sweepstakesBillCode10, journal ) );
          }
        }
        journal.getBillCodes().add( journalBillCode );
      }
    }
    else if ( journal.getPromotion().isBillCodesActive() )
    {
      List<PromotionBillCode> promotionBillCodeList = journal.getPromotion().getPromotionBillCodes();

      if ( journal.getPromotion().isBadgePromotion() )
      {
        Iterator<PromotionBillCode> iterator = promotionBillCodeList.iterator();
        JournalBillCode journalBillCode = null;
        if ( journal.getBillCodes().iterator().hasNext() )
        {
          journalBillCode = journal.getBillCodes().iterator().next();
        }
        else
        {
          journalBillCode = new JournalBillCode();
        }

        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode1 = iterator.next();
          if ( promotionBillCode1 != null )
          {
            journalBillCode.setBillCode1( getBadgeBillingCodeValue( promotionBillCode1, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode2 = iterator.next();
          if ( promotionBillCode2 != null )
          {
            journalBillCode.setBillCode2( getBadgeBillingCodeValue( promotionBillCode2, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode3 = iterator.next();
          if ( promotionBillCode3 != null )
          {
            journalBillCode.setBillCode3( getBadgeBillingCodeValue( promotionBillCode3, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode4 = iterator.next();
          if ( promotionBillCode4 != null )
          {
            journalBillCode.setBillCode4( getBadgeBillingCodeValue( promotionBillCode4, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode5 = iterator.next();
          if ( promotionBillCode5 != null )
          {
            journalBillCode.setBillCode5( getBadgeBillingCodeValue( promotionBillCode5, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode6 = iterator.next();
          if ( promotionBillCode6 != null )
          {
            journalBillCode.setBillCode6( getBadgeBillingCodeValue( promotionBillCode6, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode7 = iterator.next();
          if ( promotionBillCode7 != null )
          {
            journalBillCode.setBillCode7( getBadgeBillingCodeValue( promotionBillCode7, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode8 = iterator.next();
          if ( promotionBillCode8 != null )
          {
            journalBillCode.setBillCode8( getBadgeBillingCodeValue( promotionBillCode8, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode9 = iterator.next();
          if ( promotionBillCode9 != null )
          {
            journalBillCode.setBillCode9( getBadgeBillingCodeValue( promotionBillCode9, journal ) );
          }
        }
        if ( iterator.hasNext() )
        {
          PromotionBillCode promotionBillCode10 = iterator.next();
          if ( promotionBillCode10 != null )
          {
            journalBillCode.setBillCode10( getBadgeBillingCodeValue( promotionBillCode10, journal ) );
          }
        }
        journal.getBillCodes().add( journalBillCode );
      }
      else
      {
        Claim claim = null;
        String nodeName = "";
        boolean trackByBillCodeReceiver = isTrackByBillCodes( promotionBillCodeList, BillCodeRecognitionType.RECEIVER );
        boolean trackByBillCodeNominee = isTrackByBillCodes( promotionBillCodeList, BillCodeNominationType.NOMINEE );
        for ( Iterator iterator = journal.getActivityJournals().iterator(); iterator.hasNext(); )
        {
          ActivityJournal activityJournal = (ActivityJournal)iterator.next();
          if ( activityJournal.getActivity().getClaim() != null )
          {
            claim = activityJournal.getActivity().getClaim();
          }
          else if ( activityJournal.getActivity().getPromotion().isNominationPromotion() )
          {
            NominationActivity nominationActivity = (NominationActivity)activityJournal.getActivity();
            claim = (Claim)nominationActivity.getClaimGroup().getClaims().iterator().next();
          }

          if ( journal.getPromotion().isRecognitionPromotion() || journal.getPromotion().isNominationPromotion() )
          {
            AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
            associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
            claim = claimService.getClaimByIdWithAssociations( claim.getId(), associationRequestCollection );
            claim = unproxy( claim );

            boolean hasOrgUnitName = ifBillCodesContains( promotionBillCodeList, ORG_UNIT_NAME );

            if ( hasOrgUnitName )
            {
              if ( trackByBillCodeReceiver )
              {
                RecognitionClaim recoClaim = (RecognitionClaim)claim;
                ClaimRecipient claimRecipient = (ClaimRecipient)recoClaim.getClaimRecipients().iterator().next();
                nodeName = claimRecipient.getNode().getName();
              }
              else if ( trackByBillCodeNominee )
              {
                NominationClaim nomiClaim = (NominationClaim)claim;
                if ( nomiClaim.getClaimRecipients() != null && nomiClaim.getClaimRecipients().size() > 0 )
                {
                  for ( ClaimRecipient teamMember : nomiClaim.getClaimRecipients() )
                  {
                    if ( teamMember.getRecipient().getId().equals( journal.getParticipant().getId() ) )
                    {
                      nodeName = teamMember.getNode().getName();
                      break;
                    }
                  }
                }
                else
                {
                  ClaimRecipient claimRecipient = (ClaimRecipient)nomiClaim.getClaimRecipients().iterator().next();
                  nodeName = claimRecipient.getNode().getName();
                }
              }
            }
          }

          if ( journal.getPromotion().isProductClaimPromotion() )
          {
            AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
            associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_PARTICIPANTS ) );
            claim = claimService.getClaimByIdWithAssociations( claim.getId(), associationRequestCollection );

            boolean hasOrgUnitName = ifBillCodesContains( promotionBillCodeList, ORG_UNIT_NAME );
            if ( hasOrgUnitName )
            {
              ProductClaim productClaim = (ProductClaim)claim;
              if ( productClaim.getClaimParticipants() != null && productClaim.getClaimParticipants().size() > 0 )
              {
                for ( ProductClaimParticipant claimParticipant : productClaim.getClaimParticipants() )
                {
                  if ( claimParticipant.getParticipant().getId().equals( journal.getParticipant().getId() ) )
                  {
                    nodeName = claimParticipant.getNode().getName();
                    break;
                  }
                }
              }

              if ( StringUtils.isEmpty( nodeName ) )
              {
                nodeName = productClaim.getNode().getName();
              }
            }
          }

          Iterator<PromotionBillCode> itr = promotionBillCodeList.iterator();

          JournalBillCode journalBillCode = null;
          if ( journal.getBillCodes().iterator().hasNext() )
          {
            journalBillCode = journal.getBillCodes().iterator().next();
          }
          else
          {
            journalBillCode = new JournalBillCode();
          }

          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode1 = itr.next();
            if ( promotionBillCode1 != null )
            {
              journalBillCode.setBillCode1( getBillingCodeValue( promotionBillCode1, journal, nodeName, claim ) );
            }
          }
          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode2 = itr.next();
            if ( promotionBillCode2 != null )
            {
              journalBillCode.setBillCode2( getBillingCodeValue( promotionBillCode2, journal, nodeName, claim ) );
            }
          }
          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode3 = itr.next();
            if ( promotionBillCode3 != null )
            {
              journalBillCode.setBillCode3( getBillingCodeValue( promotionBillCode3, journal, nodeName, claim ) );
            }
          }
          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode4 = itr.next();
            if ( promotionBillCode4 != null )
            {
              journalBillCode.setBillCode4( getBillingCodeValue( promotionBillCode4, journal, nodeName, claim ) );
            }
          }
          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode5 = itr.next();
            if ( promotionBillCode5 != null )
            {
              journalBillCode.setBillCode5( getBillingCodeValue( promotionBillCode5, journal, nodeName, claim ) );
            }
          }
          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode6 = itr.next();
            if ( promotionBillCode6 != null )
            {
              journalBillCode.setBillCode6( getBillingCodeValue( promotionBillCode6, journal, nodeName, claim ) );
            }
          }
          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode7 = itr.next();
            if ( promotionBillCode7 != null )
            {
              journalBillCode.setBillCode7( getBillingCodeValue( promotionBillCode7, journal, nodeName, claim ) );
            }
          }
          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode8 = itr.next();
            if ( promotionBillCode8 != null )
            {
              journalBillCode.setBillCode8( getBillingCodeValue( promotionBillCode8, journal, nodeName, claim ) );
            }
          }
          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode9 = itr.next();
            if ( promotionBillCode9 != null )
            {
              journalBillCode.setBillCode9( getBillingCodeValue( promotionBillCode9, journal, nodeName, claim ) );
            }
          }
          if ( itr.hasNext() )
          {
            PromotionBillCode promotionBillCode10 = itr.next();
            if ( promotionBillCode10 != null )
            {
              journalBillCode.setBillCode10( getBillingCodeValue( promotionBillCode10, journal, nodeName, claim ) );
            }
          }
          journal.getBillCodes().add( journalBillCode );
        }
      }
    }
  }

  private String getBillCodeCustomValue( Participant pax, String billCodeValue, String nodeName )
  {
    String customValue = null;

    if ( billCodeValue.equalsIgnoreCase( DEPT_NAME ) )
    {
      for ( Iterator nodeIter = pax.getParticipantEmployers().iterator(); nodeIter.hasNext(); )
      {
        ParticipantEmployer participantEmployer = (ParticipantEmployer)nodeIter.next();
        if ( participantEmployer.getTerminationDate() == null )
        {
          PickListValueBean pickListValueBean = userService.getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                        pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                                                                        participantEmployer.getDepartmentType() );
          customValue = pickListValueBean.getName() != null ? pickListValueBean.getName() : "";
          break;
        }
      }
    }
    else if ( billCodeValue.equalsIgnoreCase( ORG_UNIT_NAME ) )
    {
      customValue = nodeName;
    }
    else if ( billCodeValue.equalsIgnoreCase( COUNTRY_CODE ) )
    {
      customValue = pax.getPrimaryCountryCode();
    }
    else if ( billCodeValue.equalsIgnoreCase( LOGIN_ID ) )
    {
      customValue = pax.getUserName();
    }
    else
    {
      for ( Iterator iter = pax.getUserCharacteristics().iterator(); iter.hasNext(); )
      {
        UserCharacteristic userCharacteristic = (UserCharacteristic)iter.next();
        if ( userCharacteristic.getUserCharacteristicType().getId().equals( new Long( billCodeValue ) ) )
        {
          customValue = userCharacteristic.buildCharacteristicDisplayString();
          break;
        }
        else
        {
          customValue = "";
        }
      }
    }

    return getBillingCodeValue( customValue );
  }

  private String getBillingCodeValue( String name )
  {
    String result = null;
    if ( name != null )
    {
      result = name;
      if ( result != null && result.length() > 25 )
      {
        if ( log.isWarnEnabled() )
        {
          log.warn( "truncating billing code to 25 characters from original value of [" + result + "]" );
        }
        result = result.substring( 0, 25 );
      }
    }
    return result;
  }

  /* WIP# 25130 Start - Recognition Promotion */
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.DepositBillingCodeStrategy#setMerchOrderBillingCodes(com.biperf.core.domain.journal.Journal)
   * @param journal
   */
  public void setMerchOrderBillingCodes( MerchOrder merchOrder, RecognitionPromotion recPromo, Set inputActivities )
  {
    List<PromotionBillCode> promotionBillCodeList = recPromo.getPromotionBillCodes();
    Claim claim = null;
    String nodeName = "";
    boolean trackByBillCodeReceiver = isTrackByBillCodes( promotionBillCodeList, BillCodeRecognitionType.RECEIVER );
    for ( Iterator iterator = inputActivities.iterator(); iterator.hasNext(); )
    {
      Activity activity = (Activity)iterator.next();
      if ( activity.getClaim() != null )
      {
        claim = activity.getClaim();
      }

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
      claim = claimService.getClaimByIdWithAssociations( claim.getId(), associationRequestCollection );
      claim = unproxy( claim );

      boolean hasOrgUnitName = ifBillCodesContains( promotionBillCodeList, ORG_UNIT_NAME );

      if ( hasOrgUnitName )
      {
        if ( trackByBillCodeReceiver )
        {
          RecognitionClaim recoClaim = (RecognitionClaim)claim;
          ClaimRecipient claimRecipient = (ClaimRecipient)recoClaim.getClaimRecipients().iterator().next();
          nodeName = claimRecipient.getNode().getName();
        }
      }

      Iterator<PromotionBillCode> itr = promotionBillCodeList.iterator();

      MerchOrderBillCode merchOrderBillCode = null;
      if ( merchOrder.getBillCodes().iterator().hasNext() )
      {
        merchOrderBillCode = merchOrder.getBillCodes().iterator().next();
      }
      else
      {
        merchOrderBillCode = new MerchOrderBillCode();
      }

      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode1 = itr.next();
        if ( promotionBillCode1 != null )
        {
          merchOrderBillCode.setBillCode1( getBillingCodeValue( promotionBillCode1, merchOrder, nodeName, claim ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode2 = itr.next();
        if ( promotionBillCode2 != null )
        {
          merchOrderBillCode.setBillCode2( getBillingCodeValue( promotionBillCode2, merchOrder, nodeName, claim ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode3 = itr.next();
        if ( promotionBillCode3 != null )
        {
          merchOrderBillCode.setBillCode3( getBillingCodeValue( promotionBillCode3, merchOrder, nodeName, claim ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode4 = itr.next();
        if ( promotionBillCode4 != null )
        {
          merchOrderBillCode.setBillCode4( getBillingCodeValue( promotionBillCode4, merchOrder, nodeName, claim ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode5 = itr.next();
        if ( promotionBillCode5 != null )
        {
          merchOrderBillCode.setBillCode5( getBillingCodeValue( promotionBillCode5, merchOrder, nodeName, claim ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode6 = itr.next();
        if ( promotionBillCode6 != null )
        {
          merchOrderBillCode.setBillCode6( getBillingCodeValue( promotionBillCode6, merchOrder, nodeName, claim ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode7 = itr.next();
        if ( promotionBillCode7 != null )
        {
          merchOrderBillCode.setBillCode7( getBillingCodeValue( promotionBillCode7, merchOrder, nodeName, claim ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode8 = itr.next();
        if ( promotionBillCode8 != null )
        {
          merchOrderBillCode.setBillCode8( getBillingCodeValue( promotionBillCode8, merchOrder, nodeName, claim ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode9 = itr.next();
        if ( promotionBillCode9 != null )
        {
          merchOrderBillCode.setBillCode9( getBillingCodeValue( promotionBillCode9, merchOrder, nodeName, claim ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode10 = itr.next();
        if ( promotionBillCode10 != null )
        {
          merchOrderBillCode.setBillCode10( getBillingCodeValue( promotionBillCode10, merchOrder, nodeName, claim ) );
        }
      }
      merchOrder.getBillCodes().add( merchOrderBillCode );
    }
  }

  private String getBillingCodeValue( BillCode promotionBillCode, MerchOrder merchOrder, String node, Claim claim )
  {
    String billingCodeValue = "";
    if ( promotionBillCode.getBillCode().equalsIgnoreCase( CUSTOM_VALUE ) )
    {
      billingCodeValue = promotionBillCode.getCustomValue();
    }
    else
    {
      if ( claim.getPromotion().isRecognitionPromotion() )
      {
        if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeRecognitionType.GIVER ) )
        {
          node = claim.getNode().getName();
          billingCodeValue = getBillCodeCustomValue( claim.getSubmitter(), promotionBillCode.getBillCode(), node );
        }
        if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeRecognitionType.RECEIVER ) )
        {
          billingCodeValue = getBillCodeCustomValue( merchOrder.getParticipant(), promotionBillCode.getBillCode(), node );
        }
      }
    }
    /* WIP 25127 - Truncating PAX characteristics to 25 characters */
    billingCodeValue = getBillingCodeValue( billingCodeValue );
    /* WIP 25127 - Truncating PAX characteristics to 25 characters */
    return billingCodeValue;
  }

  // Goalquest Promotion
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.DepositBillingCodeStrategy#setMerchOrderBillingCodes(com.biperf.core.domain.journal.Journal)
   * @param journal
   */
  public void setMerchOrderBillingCodes( MerchOrder merchOrder, GoalQuestPromotion goalquestPromo, Set inputActivities )
  {
    List<PromotionBillCode> promotionBillCodeList = goalquestPromo.getPromotionBillCodes();
    Claim claim = null;
    String nodeName = "";
    for ( Iterator iterator = inputActivities.iterator(); iterator.hasNext(); )
    {
      Activity activity = (Activity)iterator.next();

      Iterator<PromotionBillCode> itr = promotionBillCodeList.iterator();

      MerchOrderBillCode merchOrderBillCode = null;
      if ( merchOrder.getBillCodes().iterator().hasNext() )
      {
        merchOrderBillCode = merchOrder.getBillCodes().iterator().next();
      }
      else
      {
        merchOrderBillCode = new MerchOrderBillCode();
      }

      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode1 = itr.next();
        if ( promotionBillCode1 != null )
        {
          merchOrderBillCode.setBillCode1( getBillingCodeValue( promotionBillCode1, merchOrder, nodeName ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode2 = itr.next();
        if ( promotionBillCode2 != null )
        {
          merchOrderBillCode.setBillCode2( getBillingCodeValue( promotionBillCode2, merchOrder, nodeName ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode3 = itr.next();
        if ( promotionBillCode3 != null )
        {
          merchOrderBillCode.setBillCode3( getBillingCodeValue( promotionBillCode3, merchOrder, nodeName ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode4 = itr.next();
        if ( promotionBillCode4 != null )
        {
          merchOrderBillCode.setBillCode4( getBillingCodeValue( promotionBillCode4, merchOrder, nodeName ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode5 = itr.next();
        if ( promotionBillCode5 != null )
        {
          merchOrderBillCode.setBillCode5( getBillingCodeValue( promotionBillCode5, merchOrder, nodeName ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode6 = itr.next();
        if ( promotionBillCode6 != null )
        {
          merchOrderBillCode.setBillCode6( getBillingCodeValue( promotionBillCode6, merchOrder, nodeName ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode7 = itr.next();
        if ( promotionBillCode7 != null )
        {
          merchOrderBillCode.setBillCode7( getBillingCodeValue( promotionBillCode7, merchOrder, nodeName ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode8 = itr.next();
        if ( promotionBillCode8 != null )
        {
          merchOrderBillCode.setBillCode8( getBillingCodeValue( promotionBillCode8, merchOrder, nodeName ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode9 = itr.next();
        if ( promotionBillCode9 != null )
        {
          merchOrderBillCode.setBillCode9( getBillingCodeValue( promotionBillCode9, merchOrder, nodeName ) );
        }
      }
      if ( itr.hasNext() )
      {
        PromotionBillCode promotionBillCode10 = itr.next();
        if ( promotionBillCode10 != null )
        {
          merchOrderBillCode.setBillCode10( getBillingCodeValue( promotionBillCode10, merchOrder, nodeName ) );
        }
      }
      merchOrder.getBillCodes().add( merchOrderBillCode );
    }
  }

  private String getBillingCodeValue( BillCode promotionBillCode, MerchOrder merchOrder, String node )
  {
    String billingCodeValue = "";
    if ( promotionBillCode.getBillCode().equalsIgnoreCase( CUSTOM_VALUE ) )
    {
      billingCodeValue = promotionBillCode.getCustomValue();
    }
    else
    {
      node = merchOrder.getParticipant().getPrimaryUserNode().getNode().getName();
      billingCodeValue = getBillCodeCustomValue( merchOrder.getParticipant(), promotionBillCode.getBillCode(), node );
    }
    /* WIP 25127 - Truncating PAX characteristics to 25 characters */
    billingCodeValue = getBillingCodeValue( billingCodeValue );
    /* WIP 25127 - Truncating PAX characteristics to 25 characters */
    return billingCodeValue;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.DepositBillingCodeStrategy#setJournalBillingCodes(com.biperf.core.domain.journal.Journal)
   * @param journal
   */
  public void setMerchOrderBillingCodesForSweepstakes( MerchOrder merchOrder, RecognitionPromotion recPromo )
  {
    if ( recPromo.isSwpBillCodesActive() )
    {
      List<SweepstakesBillCode> sweepstakesBillCodeList = recPromo.getSweepstakesBillCodes();

      MerchOrderBillCode merchOrderBillCode = null;
      if ( merchOrder.getBillCodes().iterator().hasNext() )
      {
        merchOrderBillCode = merchOrder.getBillCodes().iterator().next();
      }
      else
      {
        merchOrderBillCode = new MerchOrderBillCode();
      }

      Iterator<SweepstakesBillCode> iterator = sweepstakesBillCodeList.iterator();
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode1 = iterator.next();
        if ( sweepstakesBillCode1 != null )
        {
          merchOrderBillCode.setBillCode1( getSwpBillingCodeValue( sweepstakesBillCode1, merchOrder ) );
        }
      }
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode2 = iterator.next();
        if ( sweepstakesBillCode2 != null )
        {
          merchOrderBillCode.setBillCode2( getSwpBillingCodeValue( sweepstakesBillCode2, merchOrder ) );
        }
      }
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode3 = iterator.next();
        if ( sweepstakesBillCode3 != null )
        {
          merchOrderBillCode.setBillCode3( getSwpBillingCodeValue( sweepstakesBillCode3, merchOrder ) );
        }
      }
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode4 = iterator.next();
        if ( sweepstakesBillCode4 != null )
        {
          merchOrderBillCode.setBillCode4( getSwpBillingCodeValue( sweepstakesBillCode4, merchOrder ) );
        }
      }
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode5 = iterator.next();
        if ( sweepstakesBillCode5 != null )
        {
          merchOrderBillCode.setBillCode5( getSwpBillingCodeValue( sweepstakesBillCode5, merchOrder ) );
        }
      }
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode6 = iterator.next();
        if ( sweepstakesBillCode6 != null )
        {
          merchOrderBillCode.setBillCode6( getSwpBillingCodeValue( sweepstakesBillCode6, merchOrder ) );
        }
      }
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode7 = iterator.next();
        if ( sweepstakesBillCode7 != null )
        {
          merchOrderBillCode.setBillCode7( getSwpBillingCodeValue( sweepstakesBillCode7, merchOrder ) );
        }
      }
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode8 = iterator.next();
        if ( sweepstakesBillCode8 != null )
        {
          merchOrderBillCode.setBillCode8( getSwpBillingCodeValue( sweepstakesBillCode8, merchOrder ) );
        }
      }
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode9 = iterator.next();
        if ( sweepstakesBillCode9 != null )
        {
          merchOrderBillCode.setBillCode9( getSwpBillingCodeValue( sweepstakesBillCode9, merchOrder ) );
        }
      }
      if ( iterator.hasNext() )
      {
        SweepstakesBillCode sweepstakesBillCode10 = iterator.next();
        if ( sweepstakesBillCode10 != null )
        {
          merchOrderBillCode.setBillCode10( getSwpBillingCodeValue( sweepstakesBillCode10, merchOrder ) );
        }
      }
      merchOrder.getBillCodes().add( merchOrderBillCode );
    }
  }

  private String getSwpBillingCodeValue( BillCode sweepstakesBillCode, MerchOrder merchOrder )
  {

    String billingCodeValue = "";
    if ( sweepstakesBillCode.getBillCode().equalsIgnoreCase( CUSTOM_VALUE ) )
    {
      billingCodeValue = getBillingCodeValue( sweepstakesBillCode.getCustomValue() );
    }
    else
    {
      String nodeName = merchOrder.getParticipant().getPrimaryUserNode().getNode().getName();
      billingCodeValue = getBillCodeCustomValue( merchOrder.getParticipant(), sweepstakesBillCode.getBillCode(), nodeName );
    }
    return billingCodeValue;
  }
  /* WIP# 25130 End */

  private Claim unproxy( Claim proxied )
  {
    Claim entity = proxied;
    if ( entity != null && entity instanceof HibernateProxy )
    {
      Hibernate.initialize( entity );
      entity = (Claim) ( (HibernateProxy)entity ).getHibernateLazyInitializer().getImplementation();
    }
    return entity;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

}

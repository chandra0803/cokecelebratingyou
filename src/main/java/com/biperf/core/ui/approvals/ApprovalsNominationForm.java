/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsNominationForm.java,v $
 */

package com.biperf.core.ui.approvals;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimItemApprover;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.DateUtils;

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
public class ApprovalsNominationForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private String approvalStatusType;
  private String notificationDate;
  private String awardQuantity;
  private String version;
  private boolean anyPaxNotOptedOut;
  private String level;// Client customization for WIP #56492 
  /**
   * Holds the saved Award value for budget calculation. No Setter exists since only meant to be set
   * internally.
   */
  private Long originalAward;

  /**
   * 
   */
  public ApprovalsNominationForm()
  {
    super();
  }

  /**
   * @param approvable
   */
  public void load( Approvable approvable )
  {
    // Only will ever be one element
    String approvalStatusTypeCode = null;
    ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();
    // Bug Fix 19808. Update the status type from ApprovableItemApprovers as this object holds the
    // Status flags as correct.
    if ( approvable instanceof NominationClaim )
    {
      NominationClaim claim = (NominationClaim)approvable;
      String statusType = null;
      ClaimRecipient teamRecipient = (ClaimRecipient)claim.getApprovableItems().iterator().next();
      if ( teamRecipient.getApprovableItemApprovers() != null && teamRecipient.getApprovableItemApprovers().size() > 0 )
      {
        ClaimItemApprover claimItemApprover = (ClaimItemApprover)teamRecipient.getApprovableItemApprovers().iterator().next();
        if ( claimItemApprover.getApprovalStatusType() != null )
        {
          statusType = claimItemApprover.getApprovalStatusType().getCode();
        }
      }
      approvalStatusTypeCode = statusType != null ? statusType : null;
    }
    else
    {
      ApprovalStatusType approvalStatusType = approvableItem.getApprovalStatusType();
      approvalStatusTypeCode = approvalStatusType != null ? approvalStatusType.getCode() : null;
    }

    if ( approvalStatusTypeCode == null )
    {
      approvalStatusTypeCode = ApprovalStatusType.PENDING;
    }

    version = approvable.getVersion() != null ? approvable.getVersion().toString() : null;

    setApprovalStatusType( approvalStatusTypeCode );

    if ( approvableItem instanceof ClaimGroup )
    {
      ClaimGroup claimGroup = (ClaimGroup)approvableItem;

      for ( Object claimObject : ( (ClaimGroup)approvable ).getClaims() )
      {
        Claim claim = (Claim)claimObject;
      }

      if ( claimGroup.getAwardQuantity() != null )
      {
        setAwardQuantity( claimGroup.getAwardQuantity().toString() );

      }
      if ( claimGroup.getNotificationDate() != null )
      {
        setNotificationDate( DateUtils.toDisplayString( claimGroup.getNotificationDate() ) );
      }
    }
    else if ( approvableItem instanceof ClaimRecipient )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)approvableItem;
      /* coke customization start */
      if (claimRecipient.getOptOut() != null && claimRecipient.getOptOut().equals( "false" ))
      {
        setAnyPaxNotOptedOut(true);
      }
      /* coke customization end */
      if ( claimRecipient.getAwardQuantity() != null )
      {
        setAwardQuantity( claimRecipient.getAwardQuantity().toString() );
      }
      else if ( approvable instanceof NominationClaim && ( (NominationClaim)approvable ).isTeam() )
      {
        NominationClaim nominationClaim = (NominationClaim)approvable;
       /* for ( ClaimRecipient claimTeamRecipient : nominationClaim.getClaimRecipients() )
        {
          if ( claimTeamRecipient.getAwardQuantity() != null )
          {
            setAwardQuantity( claimTeamRecipient.getAwardQuantity().toString() );
          }
        }*/
        for ( ProductClaimParticipant claimParticipant : nominationClaim.getTeamMembers() )
        {
          if ( claimParticipant.getAwardQuantity() != null )
          {
            setAwardQuantity( claimParticipant.getAwardQuantity().toString() );
          }
          /* coke customization start */
          if (claimParticipant.getOptOut() != null && claimParticipant.getOptOut().equals( "false" ))
          {
            setAnyPaxNotOptedOut(true);
          }
          /* coke customization end */
        }
      }
      // Client customization for WIP 58122
      if(approvable instanceof NominationClaim && (( NominationClaim ) approvable ).isTeam() )
      {   
    	  NominationClaim nc = ( NominationClaim ) approvable;
    	  NominationPromotion nominationPromotion = (NominationPromotion)approvable.getPromotion();
    	  if(nominationPromotion.isLevelPayoutByApproverAvailable())
    	  {
    	        for ( ProductClaimParticipant claimParticipant : nc.getTeamMembers() )
    	        {
    	          if ( claimParticipant.getAwardQuantity() != null )
    	          {
    	            setAwardQuantity( claimParticipant.getAwardQuantity().toString() );
    	          }
    	          /* coke customization start */
    	          if (claimParticipant.getOptOut() != null && claimParticipant.getOptOut().equals( "false" ))
    	          {
    	            setAnyPaxNotOptedOut(true);
    	          }
    	          /* coke customization end */
    	        } 
    	  }
      }

      if ( claimRecipient.getNotificationDate() != null )
      {
        setNotificationDate( DateUtils.toDisplayString( claimRecipient.getNotificationDate() ) );
      }
    }
    else
    {
      throw new BeaconRuntimeException( "unknown approvableItem type: " + approvableItem.getClass().getName() );
    }

  }

  public void toDomain( Approvable approvable, User approver )
  {
    if ( !StringUtils.isBlank( version ) )
    {
      approvable.setVersion( Long.valueOf( this.version ) );
    }

    ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();
    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)approvable.getPromotion();

    // Set award and auto-notification time
    // Client customization for WIP 58122
    NominationPromotion nominationpromo=(NominationPromotion)promotion;
    Long awardQuantityValue;
    if(promotion  instanceof NominationPromotion && nominationpromo!=null 
    		  && nominationpromo.isLevelPayoutByApproverAvailable() && getApprovalStatusType().equalsIgnoreCase( ApprovalStatusType.WINNER ))
      {
    	awardQuantityValue=approvableItem.getAwardQuantity();
      }
      	
    else if ( promotion.isAwardAmountTypeFixed() && !getApprovalStatusType().equalsIgnoreCase( ApprovalStatusType.NON_WINNER ) )
    {
      awardQuantityValue = promotion.getAwardAmountFixed();
    }
    else
    {
      awardQuantityValue = !StringUtils.isBlank( getAwardQuantity() ) ? new Long( getAwardQuantity() ) : null;
    }

    if ( approvable instanceof NominationClaim && ( (NominationClaim)approvable ).isTeam() )
    {
      NominationClaim nominationClaim = (NominationClaim)approvable;
      for ( ProductClaimParticipant participant : nominationClaim.getTeamMembers() )
      {
        //-------------------------------
        // coke customization start
        //-------------------------------  
        if ( getParticipantService().isOptedOut( participant.getParticipant().getId() ) )
        {
          participant.setAwardQuantity( null );
        }
        else
        {
          participant.setAwardQuantity( awardQuantityValue );
        }
        //-------------------------------
        // coke customization end --just fyi for CB. I don't think ClaimGroup is ever used below
        //-------------------------------  
      }
      /*for ( ClaimRecipient participant : nominationClaim.getClaimRecipients() )
      {
        participant.setAwardQuantity( awardQuantityValue );
      }*/
    }

    if ( approvableItem instanceof ClaimGroup )
    {
      ClaimGroup claimGroup = (ClaimGroup)approvableItem;
      originalAward = claimGroup.getAwardQuantity();
      claimGroup.setAwardQuantity( awardQuantityValue );
      claimGroup.setNotificationDate( DateUtils.toDate( getNotificationDate() ) );

    }
    else if ( approvableItem instanceof ClaimRecipient )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)approvableItem;
      originalAward = claimRecipient.getAwardQuantity();
      originalAward = originalAward != null ? originalAward : new Long( 0 );
      awardQuantityValue = awardQuantityValue != null ? awardQuantityValue : new Long( 0 );
      claimRecipient.setPreviousAwardQuantity( originalAward );
      //-------------------------------
      // coke customization start
      //------------------------------- 
      if ( claimRecipient.getRecipient() != null && getParticipantService().isOptedOut( claimRecipient.getRecipient().getId() ) )
      {
        claimRecipient.setAwardQuantity( null );
      }
      // Client customization for WIP 58122
      else if (promotion  instanceof NominationPromotion && nominationpromo!=null 
    		  && nominationpromo.isLevelPayoutByApproverAvailable() )
      {
    		  if( claimRecipient.getLevelSelect()!=null && !(claimRecipient.getLevelSelect().isEmpty()))
    		  {
    			  claimRecipient.setAwardQuantity(awardQuantityValue);
    				  
    		  }
    		  else
    		  {
    			  claimRecipient.setAwardQuantity(null);
    		  }
      }		  
      else
      {
        claimRecipient.setAwardQuantity( ( awardQuantityValue + originalAward ) - originalAward );
      }
      //-------------------------------
      // coke customization end
      //------------------------------- 

      claimRecipient.setNotificationDate( DateUtils.toDate( getNotificationDate() ) );
    }
    else
    {
      throw new BeaconRuntimeException( "unknown approvableItem type: " + approvableItem.getClass().getName() );
    }

    String approvalStatusTypeCode = getApprovalStatusType();
    ClaimApproveUtils.setClaimItemApproverDetails( approver, approvalStatusTypeCode, null, null, approvableItem );
  }

  /**
   * Calculate the increase/decrease of total award assigned as compared to the previously saved
   * state. Original Value ignored if this is the first approval round, since budget is not taken
   * from until after the first round is complete. If award is added, the result will be positive.
   * If reward is decrease the result will be negative.
   * @param approvable
   * @param nomineeCount
   * @return int
   */
  public int buildTotalAwardChange( Approvable approvable, int nomineeCount )
  {
    int perNomineeAwardChange;
    ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();

    boolean isNonWinner = approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.NON_WINNER );

    if ( approvable.getApprovalRound() == null || approvable.getApprovalRound().equals( new Long( 1 ) ) )
    {
      if ( !isNonWinner )
      {
        // in the case of fixed award the award qty on the form is null
        // do not calculate perNomineeAwardChange, it is 0
        if ( awardQuantity == null )
        {
          perNomineeAwardChange = 0;
        }
        else
        {
          int formAwardQuantity = new Long( awardQuantity ).intValue();
          perNomineeAwardChange = formAwardQuantity;
        }
      }
      else
      {
        // else first round non-winner so award is zero with no award change
        perNomineeAwardChange = 0;
      }
    }
    else
    {
      // past first round, consider previously saved award.
      if ( !isNonWinner )
      {
        // Bug Fix 19808.If the approval round is greater than one,calculate and update the budget
        // data according to the recent approvers status.
        if ( approvable.getApprovalRound() != null && approvable.getApprovalRound().longValue() > 1 && ( (NominationPromotion)approvable.getPromotion() ).isTeam() )
        {
          if ( awardQuantity == null )
          {
            perNomineeAwardChange = 0;
          }
          else
          {
            int formAwardQuantity = new Long( awardQuantity ).intValue();
            perNomineeAwardChange = formAwardQuantity;
          }

        }
        else
        {
          int formAwardQuantity = new Long( awardQuantity ).intValue();
          perNomineeAwardChange = formAwardQuantity - originalAward.intValue();
        }
      }
      else
      {
        // Non-winner, so subtract the originalAward value
        perNomineeAwardChange = -1 * originalAward.intValue();
      }
    }

    int totalAwardChange = nomineeCount * perNomineeAwardChange;

    return totalAwardChange;
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion( String version )
  {
    this.version = version;
  }

  public Long getOriginalAward()
  {
    return originalAward;
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
   * @return value of awardQuantity property
   */
  public String getAwardQuantity()
  {
    return awardQuantity;
  }

  /**
   * @param awardQuantity value for awardQuantity property
   */
  public void setAwardQuantity( String awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  /**
   * @return value of notificationDate property
   */
  public String getNotificationDate()
  {
    return notificationDate;
  }

  /**
   * @param notificationDate value for notificationDate property
   */
  public void setNotificationDate( String notificationDate )
  {
    this.notificationDate = notificationDate;
  }
   
  //Client customization for WIP #56492 starts
  
  public String getLevel()
  {
    return level;
  }

  /**
 * @return the anyPaxNotOptedOut
 */
public boolean isAnyPaxNotOptedOut() {
	return anyPaxNotOptedOut;
}

/**
 * @param anyPaxNotOptedOut the anyPaxNotOptedOut to set
 */
public void setAnyPaxNotOptedOut(boolean anyPaxNotOptedOut) {
	this.anyPaxNotOptedOut = anyPaxNotOptedOut;
}

public void setLevel( String level )
  {
    this.level = level;
  }
  //Client customization for WIP #56492 ends
  // Client customization for WIP 58122
  private CokeClientService getCokeClientService()
  {
    return (CokeClientService)BeanLocator.getBean( CokeClientService.BEAN_NAME );
  }
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

}

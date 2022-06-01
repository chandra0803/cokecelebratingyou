/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/ClaimApproveUtils.java,v $
 */

package com.biperf.core.utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimGroupApprover;
import com.biperf.core.domain.claim.ClaimItemApprover;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.promotion.NominationPromotion;

/**
 * .
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
 * <td>wadzinsk</td>
 * <td>Feb 10, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimApproveUtils
{

  public static final String READ_ONLY_VALUE = "-read-only-";

  /**
   * protected - we don't want people to make instances since there are only static methods
   */
  protected ClaimApproveUtils()
  {
    super();
  }

  public static int getNodeLevelsRemaining( int approvableItemApproversSize, Promotion promotion, Long approvalRound )
  {
    int nodeLevelsRemaining = 0;

    Integer approvalNodeLevels = promotion.getApprovalNodeLevels();

    if ( promotion.isNominationPromotion() )
    {
      if ( approvalNodeLevels != null && approvableItemApproversSize <= approvalNodeLevels.intValue() )
      {
        nodeLevelsRemaining = approvalNodeLevels.intValue() - approvableItemApproversSize;
      }
    }
    else
    {
      if ( approvalNodeLevels != null )
      {
        nodeLevelsRemaining = approvalNodeLevels.intValue() - approvalRound.intValue() + 1;
      }

      if ( nodeLevelsRemaining < 0 )
      {
        nodeLevelsRemaining = 0;
      }
    }
    return nodeLevelsRemaining;
  }

  public static void markUndeniedApprovableItemsApproved( Approvable approvable, User approver )
  {
    if ( approvable instanceof Claim && ! ( approvable instanceof NominationClaim ) )
    {
      markUndeniedApprovableItemsApprovalStatus( approvable, approver, ApprovalStatusType.lookup( ApprovalStatusType.APPROVED ) );
    }
    else if ( approvable instanceof ClaimGroup || approvable instanceof NominationClaim )
    {
      ClaimGroup claimGroup = new ClaimGroup();
      if ( approvable instanceof ClaimGroup )
      {
        claimGroup = (ClaimGroup)approvable;
      }
      else
      {
        NominationClaim nominationClaim = (NominationClaim)approvable;
        claimGroup = nominationClaim.getClaimGroup();
      }

      if ( claimGroup != null )
      {
        markUndeniedApprovableItemsApprovalStatus( approvable, approver, claimGroup.getApprovalStatusType() );
      }
    }
    else
    {
      throw new BeaconRuntimeException( "unknown approvable class: " + approvable.getClass().getName() );
    }
  }

  public static void markUndeniedApprovableItemsPending( Approvable approvable, User approverUser )
  {
    markUndeniedApprovableItemsApprovalStatus( approvable, approverUser, ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
  }

  private static void markUndeniedApprovableItemsApprovalStatus( Approvable approvable, User approver, ApprovalStatusType approvalStatusType )
  {
    Set approvableItems = approvable.getApprovableItems();
    // Set each claim item's status to APPROVED, if not already denied
    for ( Iterator iter = approvableItems.iterator(); iter.hasNext(); )
    {
      ApprovableItem approvableItem = (ApprovableItem)iter.next();
      if ( approvableItem.getApprovalStatusType() != null && !approvableItem.getApprovalStatusType().isAbstractDenied() )
      {
        approvableItem.setApprovalStatusType( approvalStatusType );
      }
    }
  }

  /**
   * Mark the approval round complete, saving claim item approver data, setting the last approval
   * node if needed setting the new approval round number for the claim, and marking "undenied" the
   * claim items for claims with remaining approval rounds.
   * 
   * @param approverUser
   */
  public static void markApprovalRoundComplete( Approvable approvable, User approverUser, Node sourceNode )
  {

    // set claim item approver data
    Timestamp dateApproved = new Timestamp( new Date().getTime() );

    // Increase approval round number
    Long approvalRound = approvable.getApprovalRound();

    Set approvableItems = approvable.getApprovableItems();

    for ( Iterator iter = approvableItems.iterator(); iter.hasNext(); )
    {
      ApprovableItem approvableItem = (ApprovableItem)iter.next();
      approvableItem.setDateApproved( dateApproved );
      ApprovableItemApprover currentClaimItemApprover = approvableItem.getCurrentClaimItemApprover();
      if ( currentClaimItemApprover != null )
      {
        currentClaimItemApprover.setApproverUser( approverUser );
      }
      else
      {
        approvableItem.addApprover( approverUser, new Date(), approvableItem.getApprovalStatusType(), null, approvableItem.getPromotionApprovalOptionReasonType(), null, null );
      }
    }

    // Save the last approval node
    approvable.setLastApprovalNode( sourceNode );

    if ( approvalRound != null )
    {
      if ( approvable.getPromotion().isNominationPromotion() )
      {
        if ( approvalRound.intValue() == approvable.getPromotion().getApprovalNodeLevels().intValue() )
        {
          if ( approvable instanceof ClaimGroup )
          {
            ClaimGroup claimGroup = (ClaimGroup)approvable;
            Set<NominationClaim> claimSet = claimGroup.getClaims();
            for ( NominationClaim nominationClaim : claimSet )
            {
              nominationClaim.setApprovalRound( approvable.getApprovalRound() );
              nominationClaim.setOpen( false );
              for ( Iterator iter = nominationClaim.getClaimRecipients().iterator(); iter.hasNext(); )
              {
                ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
                claimRecipient.setApprovalStatusType( claimGroup.getApprovalStatusType() );
              }
            }
          }
          else
          {
            approvable.setApprovalRound( approvable.getApprovalRound() );
          }
        }
        else
        {
          if ( approvable instanceof ClaimGroup )
          {
            ClaimGroup claimGroup = (ClaimGroup)approvable;
            Set<Claim> claimSet = claimGroup.getClaims();
            for ( Claim claim : claimSet )
            {
              claim.setApprovalRound( new Long( approvable.getApprovalRound().intValue() + 1 ) );
            }
          }
          else
          {
            approvable.setApprovalRound( new Long( approvable.getApprovalRound().intValue() + 1 ) );
          }
        }
      }
      else
      {
        approvable.setApprovalRound( new Long( approvable.getApprovalRound().intValue() + 1 ) );
      }
    }

    // Reset approval statuses if further approval round required
    if ( approvable.getNodeLevelsRemaining() > 0 )
    {
      ClaimApproveUtils.markUndeniedApprovableItemsPending( approvable, approverUser );
    }
  }

  /**
   * Returns true if the approver type for this claim requires that a claim approver snapshot be
   * taken
   * 
   * @param approvable
   */
  public static boolean isSnapshotKept( Approvable approvable )
  {
    boolean snapshotKept = true;
    NominationPromotion nominationPromotion = null;    
    if ( approvable.getPromotion().isNominationPromotion() )
    {
      nominationPromotion = (NominationPromotion)approvable.getPromotion();
    }
    // Client customizations for WIP #42701 starts
    // String approverTypeCode = approvable.getPromotion().getApproverType().getCode();
    if ( approvable.getPromotion().getApproverType().getCode().equals( ApproverType.SPECIFIC_APPROVERS )
        && ( ( approvable.getPromotion().getAdihCashOption() == null || !approvable.getPromotion().getAdihCashOption().booleanValue() ) &&
            ( approvable.getPromotion().isNominationPromotion() && ( !nominationPromotion.isLevelSelectionByApprover() && !ApprovalType.COKE_CUSTOM.equals( approvable.getPromotion().getApprovalType().getCode() ) ) ) ) )
    {
      // No snapshot for specific approvers since access to their approvers is already trivial.
      snapshotKept = false;
    }
    // Client customizations for WIP #42701 ends
    return snapshotKept;
  }
  /**
   * If all ApprovableItems are not held or pending, the round is considered complete. In the 
   * QuizClaim case, the round is considered complete if the quiz is complete.
   */
  public static boolean isApprovalRoundOver( Approvable approvable )
  {
    boolean complete = true;
    if ( approvable instanceof NominationClaim || approvable instanceof ClaimGroup )
    {
      Set approvableItems = approvable.getApprovableItems();

      for ( Iterator iter = approvableItems.iterator(); iter.hasNext(); )
      {
        ApprovableItem approvableItem = (ApprovableItem)iter.next();
        if ( approvableItem.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) ) && approvableItem.getApprovableItemApprovers().size() < 1 )
        {
          // approval status on at least on claim item is not approved or denied so approval round
          // is incomplete
          complete = false;
          break;
        }
      }
    }
    else if ( ! ( approvable instanceof QuizClaim ) )
    {
      Set approvableItems = approvable.getApprovableItems();

      for ( Iterator iter = approvableItems.iterator(); iter.hasNext(); )
      {
        ApprovableItem approvableItem = (ApprovableItem)iter.next();
        if ( approvableItem.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) )
            || approvableItem.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.HOLD ) ) )
        {
          // approval status on at least on claim item is not approved or denied so approval round
          // is incomplete
          complete = false;
          break;
        }
      }
    }
    else
    {
      QuizClaim quizClaim = (QuizClaim)approvable;
      // mark complete if all questions asked
      if ( !quizClaim.isQuizComplete() )
      {
        complete = false;
      }
    }
    return complete;
  }

  /**
   * Return true if a node level based approval type is used for this approvable.
   * 
   * @param approvable
   */
  public static boolean isNodeLevelUsed( Approvable approvable )
  {
    boolean nodeLevelUsed = false;

    String approverTypeCode = approvable.getPromotion().getApproverType().getCode();
    if ( approverTypeCode.equals( ApproverType.NODE_OWNER_BY_LEVEL ) || approverTypeCode.equals( ApproverType.NODE_OWNER_BY_TYPE )
        || approverTypeCode.equals( ApproverType.NOMINATOR_NODE_OWNER_BY_LEVEL ) || approverTypeCode.equals( ApproverType.NOMINATOR_NODE_OWNER_BY_TYPE )
        || approverTypeCode.equals( ApproverType.NOMINEE_NODE_OWNER_BY_LEVEL ) || approverTypeCode.equals( ApproverType.NOMINEE_NODE_OWNER_BY_TYPE ) )
    {
      nodeLevelUsed = true;
    }

    return nodeLevelUsed;
  }

  public static void setClaimItemApproverDetails( User approver,
                                                  String approvalStatusTypeCode,
                                                  String holdPromotionApprovalOptionReasonTypeCode,
                                                  String denyPromotionApprovalOptionReasonTypeCode,
                                                  ApprovableItem approvableItem )
  {
    if ( approvalStatusTypeCode.equals( READ_ONLY_VALUE ) )
    {
      // Don't reset values for read-only fields (such as when an item has already been approved)
      return;
    }

    approvableItem.setApprovalStatusType( ApprovalStatusType.lookup( approvalStatusTypeCode ) );

    if ( ApprovalStatusType.HOLD.equals( approvalStatusTypeCode ) )
    {

      approvableItem.setPromotionApprovalOptionReasonType( PromotionApprovalOptionReasonType.lookup( holdPromotionApprovalOptionReasonTypeCode ) );
    }
    else if ( ApprovalStatusType.DENIED.equals( approvalStatusTypeCode ) )
    {
      approvableItem.setPromotionApprovalOptionReasonType( PromotionApprovalOptionReasonType.lookup( denyPromotionApprovalOptionReasonTypeCode ) );
    }
    else
    {
      approvableItem.setPromotionApprovalOptionReasonType( null );
    }

    // Set "item approver" details
    ApprovableItemApprover currentClaimItemApprover = approvableItem.getCurrentClaimItemApprover();
    if ( currentClaimItemApprover == null )
    {
      // no "item approver" record for this approver exists, insert one
      approvableItem.addApprover( approver, new Date(), approvableItem.getApprovalStatusType(), null, approvableItem.getPromotionApprovalOptionReasonType(), null, null );
    }
    else
    {
      // update the existing "item approver" record
      currentClaimItemApprover.setDateApproved( new Date() );
      currentClaimItemApprover.setApproverUser( approver );
      currentClaimItemApprover.setApprovalStatusType( approvableItem.getApprovalStatusType() );
      currentClaimItemApprover.setPromotionApprovalOptionReasonType( approvableItem.getPromotionApprovalOptionReasonType() );
    }
    approvableItem.setDateApproved( new Timestamp( new Date().getTime() ) );
  }

  public static void setNominationClaimItemApproverDetails( User approver,
                                                            String approvalStatusTypeCode,
                                                            String approverComment,
                                                            ApprovableItem approvableItem,
                                                            Date notificationDate,
                                                            Long timePeriodId )
  {
    // Set "item approver" details
    ApprovableItemApprover currentClaimItemApprover = approvableItem.getCurrentClaimItemApprover();
    if ( currentClaimItemApprover == null )
    {
      // no "item approver" record for this approver exists, insert one
      approvableItem.addApprover( approver,
                                  new Date(),
                                  ApprovalStatusType.lookup( approvalStatusTypeCode ),
                                  approverComment,
                                  approvableItem.getPromotionApprovalOptionReasonType(),
                                  notificationDate,
                                  timePeriodId );
    }
    else
    {
      // update the existing "item approver" record
      currentClaimItemApprover.setNotificationDate( notificationDate );
      currentClaimItemApprover.setTimePeriodId( timePeriodId );
      currentClaimItemApprover.setDateApproved( new Date() );
      currentClaimItemApprover.setApproverUser( approver );
      currentClaimItemApprover.setApprovalStatusType( ApprovalStatusType.lookup( approvalStatusTypeCode ) );
      currentClaimItemApprover.setApproverComments( approverComment );
    }
    approvableItem.setDateApproved( new Timestamp( new Date().getTime() ) );
  }

  public static boolean isClaimApproved( Approvable approvable )
  {
    boolean approved = false;
    if ( ! ( approvable instanceof QuizClaim || approvable instanceof NominationClaim ) )
    {
      Set approvableItems = approvable.getApprovableItems();

      for ( Iterator iter = approvableItems.iterator(); iter.hasNext(); )
      {
        ApprovableItem approvableItem = (ApprovableItem)iter.next();
        if ( approvableItem.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.APPROVED ) )
            || approvableItem.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.WINNER ) ) )
        {
          approved = true;
          break;
        }
      }
    }
    else if ( approvable instanceof NominationClaim )
    {
      Set<ApprovableItem> approvableItems = approvable.getApprovableItems();
      for ( ApprovableItem approvableItem : approvableItems )
      {
        if ( approvableItem instanceof ClaimRecipient )
        {
          ClaimRecipient claimRecipient = (ClaimRecipient)approvableItem;

          Set<ClaimItemApprover> claimItemApprovers = claimRecipient.getApprovableItemApprovers();

          for ( ClaimItemApprover claimItemApprover : claimItemApprovers )
          {
            if ( claimItemApprovers.size() == claimItemApprover.getApprovalRound().intValue()
                && claimItemApprover.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.WINNER ) ) )
            {
              approved = true;
              break;
            }
          }
        }
        else if ( approvableItem instanceof ClaimGroup )
        {
          ClaimGroup claimGroup = (ClaimGroup)approvableItem;
          Set<ClaimGroupApprover> claimGroupApprovers = claimGroup.getApprovableItemApprovers();
          for ( ClaimGroupApprover claimGroupApprover : claimGroupApprovers )
          {
            if ( claimGroupApprovers.size() == claimGroupApprover.getApprovalRound().intValue()
                && claimGroupApprover.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.WINNER ) ) )
            {
              approved = true;
              break;
            }
          }
        }
      }
    }
    else
    {
      QuizClaim quizClaim = (QuizClaim)approvable;
      if ( quizClaim.getPass() )
      {
        approved = true;
      }
    }
    return approved;
  }

  public static boolean isClaimDenied( Approvable approvable )
  {
    boolean approved = false;
    if ( ! ( approvable instanceof QuizClaim ) )
    {
      Set approvableItems = approvable.getApprovableItems();

      for ( Iterator iter = approvableItems.iterator(); iter.hasNext(); )
      {
        ApprovableItem approvableItem = (ApprovableItem)iter.next();
        if ( approvableItem.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.DENIED ) )
            || approvableItem.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.NON_WINNER ) ) )
        {
          approved = true;
          break;
        }
      }
    }
    return approved;
  }

  public static boolean isNominationApprovalRequestMoreInfo( Approvable approvable )
  {
    boolean moreInfoRequested = false;
    if ( approvable.getPromotion().isNominationPromotion() )
    {
      Set approvableItems = approvable.getApprovableItems();

      for ( Iterator iter = approvableItems.iterator(); iter.hasNext(); )
      {
        ApprovableItem approvableItem = (ApprovableItem)iter.next();
        if ( approvableItem.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.MORE_INFO ) ) )
        {
          moreInfoRequested = true;
          break;
        }
      }
    }
    return moreInfoRequested;
  }
  /**
   * 
   * @param approvable
   * @return
   */
  public static boolean isFinalApprover( Approvable approvable )
  {
    boolean complete = false;
    if (null != approvable && null != approvable.getPromotion()) {
		Claim claim = (Claim) approvable;
 
    if ( approvable instanceof NominationClaim || approvable instanceof ClaimGroup )
    {
      Set approvableItems = approvable.getApprovableItems();

      for ( Iterator iter = approvableItems.iterator(); iter.hasNext(); )
      {
        ApprovableItem approvableItem = (ApprovableItem)iter.next();
        if ( approvableItem.getApprovalStatusType().equals( ApprovalStatusType.lookup( ApprovalStatusType.WINNER ) ) && claim.getApprovableItemApproversSize() == approvableItem.getApprovableItemApprovers().size() )
        {
          // approval status on at least on claim item is not approved or denied so approval round
          // is incomplete
          complete = true;
          break;
        }
      }
    }
    }
    return complete;
  }
  /**
   * 
   * @param approvable
   * @return
   */
	public static boolean isNominationApprovalComplete(Approvable approvable) {
		boolean isApprovalComplete = false;
		if (null != approvable && null != approvable.getPromotion()) {
			Claim claim = (Claim) approvable;
			int promoApporvalRound = approvable.getPromotion().getApprovalNodeLevels();
			
			if (claim instanceof NominationClaim) {
				long exactApprovalRound = null != approvable && null != approvable.getApprovalRound()
						? approvable.getApprovalRound().longValue() : 0;
				if (exactApprovalRound == claim.getApprovableItemApproversSize()) {
					isApprovalComplete = true;
				}
			}
		}
		return isApprovalComplete;
	}
}

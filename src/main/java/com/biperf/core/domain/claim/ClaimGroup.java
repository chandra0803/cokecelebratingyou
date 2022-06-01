/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.claim;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.enums.ApprovableTypeEnum;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.utils.ClaimApproveUtils;

/**
 * Represents a group of claims acting as one Approvable unit. A ClaimGroup itself is also
 * an ApprovableItem so it holds approval status data as well.  ClaimGroup is used for
 * Cumulative Nomination Promotions but could be reused for any situation where claims need to
 * be group for a single approval process.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimGroup extends ApprovableItem implements Approvable
{
  private Set claims = new LinkedHashSet();

  private Participant participant;
  private Node node;

  private Promotion promotion;
  private boolean open;
  private Node lastApprovalNode;
  private String approverComments = "";
  private Long approvalRound;
  private Long awardQuantity;
  private BigDecimal cashAwardQuantity;
  private Date notificationDate;
  private boolean modalWindowViewed;

  public ClaimGroup()
  {
    // default constructor
  }

  public ClaimGroup( String serialId )
  {
    setSerialId( serialId );
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.ApprovableItem#getApprovable()
   */
  public Approvable getApprovable()
  {
    return this;
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.Approvable#getApprovalRound()
   */
  public Long getApprovalRound()
  {
    return approvalRound;
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.Approvable#setApprovalRound(java.lang.Long)
   * @param approvalRound
   */
  public void setApprovalRound( Long approvalRound )
  {
    this.approvalRound = approvalRound;
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.Approvable#getLastApprovalNode()
   */
  public Node getLastApprovalNode()
  {
    return lastApprovalNode;
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.Approvable#setLastApprovalNode(com.biperf.core.domain.hierarchy.Node)
   * @param lastApprovalNode
   */
  public void setLastApprovalNode( Node lastApprovalNode )
  {
    this.lastApprovalNode = lastApprovalNode;
  }

  /**
   * @return value of approvableType property
   */
  public ApprovableTypeEnum getApprovableType()
  {
    return ApprovableTypeEnum.CLAIM_GROUP;
  }

  /**
   * @return value of approverComments property
   */
  public String getApproverComments()
  {
    return approverComments;
  }

  /**
   * @param approverComments value for approverComments property
   */
  public void setApproverComments( String approverComments )
  {
    this.approverComments = approverComments;
  }

  /**
   * @return value of open property
   */
  public boolean isOpen()
  {
    return open;
  }

  /**
   * @param open value for open property
   */
  public void setOpen( boolean open )
  {
    this.open = open;
  }

  /**
   * @return value of promotion property
   */
  public Promotion getPromotion()
  {
    return promotion;
  }

  /**
   * @param promotion value for promotion property
   */
  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  /**
   * @return value of claims property
   */
  public Set getClaims()
  {
    return claims;
  }

  /**
   * @param claims value for nominationClaims property
   */
  public void setClaims( Set claims )
  {
    this.claims = claims;
  }

  public void addClaim( Claim claim )
  {
    claims.add( claim );
    ( (ClaimGroupable)claim ).setClaimGroup( this );
  }

  public void addApprover( User approverUser,
                           Date dateApproved,
                           ApprovalStatusType approvalStatusType,
                           String approverComment,
                           PromotionApprovalOptionReasonType promotionApprovalOptionReasonType,
                           Date notificationDate,
                           Long timePeriodId )
  {
    ApprovableItemApprover approvableItemApprover = new ClaimGroupApprover();
    approvableItemApprover.setApprovableItem( this );
    addApprover( approverUser, dateApproved, approvalStatusType, approverComment, promotionApprovalOptionReasonType, approvableItemApprover, notificationDate, timePeriodId );
  }

  public int getNodeLevelsRemaining()
  {
    return ClaimApproveUtils.getNodeLevelsRemaining( getApprovableItemApproversSize(), promotion, approvalRound );
  }

  public int getApprovableItemApproversSize()
  {
    int approvableItemApproversSize = 0;
    Set<ApprovableItem> approvableItems = getApprovableItems();

    for ( ApprovableItem approvableItem : approvableItems )
    {
      if ( approvableItem instanceof ClaimRecipient )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)approvableItem;

        Set<ClaimItemApprover> claimItemApprovers = claimRecipient.getApprovableItemApprovers();
        for ( ClaimItemApprover claimItemApprover : claimItemApprovers )
        {
          if ( approvableItemApproversSize < claimItemApprover.getApprovalRound() )
          {
            approvableItemApproversSize = claimItemApprover.getApprovalRound().intValue();
          }
        }
      }
      else if ( approvableItem instanceof ClaimGroup )
      {
        ClaimGroup claimGroup = (ClaimGroup)approvableItem;
        Set<ClaimGroupApprover> claimGroupApprovers = claimGroup.getApprovableItemApprovers();
        for ( ClaimGroupApprover claimGroupApprover : claimGroupApprovers )
        {
          if ( approvableItemApproversSize < claimGroupApprover.getApprovalRound() )
          {
            approvableItemApproversSize = claimGroupApprover.getApprovalRound().intValue();
          }
        }
      }
      else if ( approvableItem.getApprovable() instanceof ProductClaim )
      {
        approvableItemApproversSize = approvableItemApproversSize + approvableItem.getApprovableItemApprovers().size();
      }
    }
    return approvableItemApproversSize;
  }

  /**
   * @return value of node property
   */
  public Node getNode()
  {
    return node;
  }

  /**
   * @param node value for node property
   */
  public void setNode( Node node )
  {
    this.node = node;
  }

  /**
   * @return value of participant property
   */
  public Participant getParticipant()
  {
    return participant;
  }

  /**
   * @param participant value for participant property
   */
  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public Date getNotificationDate()
  {
    return notificationDate;
  }

  public void setNotificationDate( Date notificationDate )
  {
    this.notificationDate = notificationDate;
  }

  public boolean equals( Object object )
  {

    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ClaimGroup ) )
    {
      return false;
    }

    // Using guid instead of nominee/node combo since likely mod will base equality on just nominee
    ClaimGroup claimGroup = (ClaimGroup)object;

    if ( this.getSerialId() != null ? !this.getSerialId().equals( claimGroup.getSerialId() ) : claimGroup.getSerialId() != null )
    {
      return false;
    }

    return true;
  } // end equals

  public int hashCode()
  {
    int result = 0;

    result += this.getSerialId() != null ? this.getSerialId().hashCode() : 0;

    return result;
  }

  /**
   * Overridden from @see com.biperf.core.domain.claim.Approvable#getApprovableItems().
   * This claimGroup IS the one and only ApprovableItem
   */
  public Set getApprovableItems()
  {
    LinkedHashSet approvableItems = new LinkedHashSet();
    approvableItems.add( this );
    return approvableItems;
  }

  /**
   * Based on claim claim Numbers, so we know if included claims change during map lookup.
   * 
   */
  public String getApprovableUid()
  {
    String approvableUid = new String();
    for ( Iterator iter = getClaims().iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();
      approvableUid += claim.getClaimNumber();
    }

    return approvableUid;
  }

  public BigDecimal getCashAwardQuantity()
  {
    return cashAwardQuantity;
  }

  public void setCashAwardQuantity( BigDecimal cashAwardQuantity )
  {
    this.cashAwardQuantity = cashAwardQuantity;
  }

  public boolean isModalWindowViewed()
  {
    return modalWindowViewed;
  }

  public void setModalWindowViewed( boolean modalWindowViewed )
  {
    this.modalWindowViewed = modalWindowViewed;
  }
}

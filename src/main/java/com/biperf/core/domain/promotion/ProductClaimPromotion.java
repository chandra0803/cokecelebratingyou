/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/ProductClaimPromotion.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.PromoMgrPayoutFreqType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionProcessingModeType;
import com.biperf.core.domain.enums.StackRankApprovalType;
import com.biperf.core.domain.enums.StackRankFactorType;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.utils.DateUtils;

/**
 * Promotion.
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
 * <td>sathish</td>
 * <td>Oct 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductClaimPromotion extends Promotion
{
  private boolean teamUsed;
  private boolean teamCollectedAsGroup;
  private Integer teamMaxCount;
  private Integer payoutManagerPercent;
  private boolean payoutManager;
  private boolean payoutCarryOver;
  private boolean teamHasMax;

  private ProductClaimPromotion parentPromotion;
  private PromotionProcessingModeType promotionProcessingMode;
  private PromoMgrPayoutFreqType payoutManagerPeriod;
  private PromotionPayoutType payoutType;

  private List promotionPayoutGroups = new ArrayList();
  private Set promotionTeamPositions = new LinkedHashSet();

  private Set childPromotions = new LinkedHashSet();
  private Set stackRankPayoutGroups = new LinkedHashSet();
  private int childrenCount;

  /**
   * Identifies how stack rankings are approved: automatically or manually.
   */
  private StackRankApprovalType stackRankApprovalType;

  /**
   * Identifies the type of stack rank factor used by this promotion: quantity sold or numeric claim
   * element.
   */
  private StackRankFactorType stackRankFactorType;

  /**
   * If the stack rank factor type is "numeric claim element," then this field points to the
   * {@link ClaimFormStepElement} object that defines the characteristics of the claim elements used
   * to create the stack rank.
   */
  private ClaimFormStepElement stackRankClaimFormStepElement;

  /**
   * If true, then show the stack rank factor in stack rank widgets on the home page; if false, then
   * hide the stack rank factor in stack rank widgets on the home page.
   */
  private boolean displayStackRankFactor;

  /**
   * If true, then show the "full list" link in stack rank widgets on the home page; if false, then
   * hide the "full list" link in stack rank widgets on the home page.
   */
  private boolean displayFullListLinkToParticipants;

  private Integer defaultQuantity;

  public ProductClaimPromotion()
  {
    super();
  }

  private void addPromotionTeamPositions( PromotionTeamPosition position )
  {
    position.setPromotion( this );
    this.promotionTeamPositions.add( position );
  }

  /**
   * @return payoutType
   */
  public PromotionPayoutType getPayoutType()
  {
    return payoutType;
  }

  /**
   * @param payoutType
   */
  public void setPayoutType( PromotionPayoutType payoutType )
  {
    this.payoutType = payoutType;
  }

  /**
   * @return payoutCarryOver
   */
  public boolean isPayoutCarryOver()
  {
    return payoutCarryOver;
  }

  /**
   * @param payoutCarryOver
   */
  public void setPayoutCarryOver( boolean payoutCarryOver )
  {
    this.payoutCarryOver = payoutCarryOver;
  }

  /**
   * @return payoutManager
   */
  public boolean isPayoutManager()
  {
    return payoutManager;
  }

  /**
   * @param payoutManager
   */
  public void setPayoutManager( boolean payoutManager )
  {
    this.payoutManager = payoutManager;
  }

  /**
   * @return payoutManagerPercent
   */
  public Integer getPayoutManagerPercent()
  {
    return payoutManagerPercent;
  }

  /**
   * @param payoutManagerPercent
   */
  public void setPayoutManagerPercent( Integer payoutManagerPercent )
  {
    this.payoutManagerPercent = payoutManagerPercent;
  }

  /**
   * @return payoutManagerPeriod
   */
  public PromoMgrPayoutFreqType getPayoutManagerPeriod()
  {
    return payoutManagerPeriod;
  }

  /**
   * @param payoutManagerPeriod
   */
  public void setPayoutManagerPeriod( PromoMgrPayoutFreqType payoutManagerPeriod )
  {
    this.payoutManagerPeriod = payoutManagerPeriod;
  }

  /**
   * Returns true if a team who submits a claim for this promotion is organized as a group; returns
   * false if the team is organized by role.
   * 
   * @return true if a team who submits a claim for this promotion is organized as a group; returns
   *         false if the team is organized by role.
   */
  public boolean isTeamCollectedAsGroup()
  {
    return teamCollectedAsGroup;
  }

  /**
   * Sets the "team collected as group" flag. This flag is true if a team who submits a claim for
   * this promotion is organized as a group; this falg is false if the team is organized by role.
   * 
   * @param teamAsGroup true if a team who submits a claim for this promotion is organized as a
   *          group; false if the team is organized by role.
   */
  public void setTeamCollectedAsGroup( boolean teamAsGroup )
  {
    this.teamCollectedAsGroup = teamAsGroup;
  }

  /**
   * Returns the maximum number of participants on a team that submits a claim for this promotion.
   * 
   * @return the maximum number of participants on a team that submits a claim for this promotion.
   */
  public Integer getTeamMaxCount()
  {
    return teamMaxCount;
  }

  /**
   * Sets the maximum number of participants on a team that submits a claim for this promotion.
   * 
   * @param teamMaxCount the maximum number of participants on a team that submits a claim for this
   *          promotion.
   */
  public void setTeamMaxCount( Integer teamMaxCount )
  {
    this.teamMaxCount = teamMaxCount;
  }

  /**
   * Returns true if a team can submit a claim for this promotion; returns false otherwise.
   * 
   * @return true if a team can submit a claim for this promotion; returns false otherwise.
   */
  public boolean isTeamUsed()
  {
    return teamUsed;
  }

  /**
   * Sets the "team used" flag. If this flag is true, then a team of participants can submit a claim
   * for this promotion; if this flag is false, then only a single participant can submit a claim
   * for this promotion.
   * 
   * @param teamUsed true if a team can submit a claim for this promotion; false otherwise.
   */
  public void setTeamUsed( boolean teamUsed )
  {
    this.teamUsed = teamUsed;
  }

  /**
   * @return promotionProcessingMode
   */
  public PromotionProcessingModeType getPromotionProcessingMode()
  {
    return promotionProcessingMode;
  }

  /**
   * @param promotionProcessMode
   */
  public void setPromotionProcessingMode( PromotionProcessingModeType promotionProcessMode )
  {
    this.promotionProcessingMode = promotionProcessMode;
  }

  /**
   * @return lastUpdateDate in String
   */
  public String getDisplayLastUpdatedDate()
  {
    return DateUtils.toDisplayString( this.getAuditUpdateInfo().getDateModified() );
  }

  /**
   * @return submissionStartDate in String
   */
  public String getDisplayLiveDate()
  {
    return DateUtils.toDisplayString( this.getSubmissionStartDate() );
  }

  /**
   * @return promotionPayoutGroups
   */
  public List getPromotionPayoutGroups()
  {
    return promotionPayoutGroups;
  }

  /**
   * @param promotionPayoutGroups
   */
  public void setPromotionPayoutGroups( List promotionPayoutGroups )
  {
    this.promotionPayoutGroups = promotionPayoutGroups;
  }

  /**
   * Add a promotionPayoutGroup to promotionPayoutGroups
   * 
   * @param promoPayoutGroup
   */
  public void addPromotionPayoutGroup( PromotionPayoutGroup promoPayoutGroup )
  {
    promoPayoutGroup.setPromotion( this );
    this.promotionPayoutGroups.add( promoPayoutGroup );
  }

  /**
   * Returns true if the size of a team that submits a claim on this promotion is limited; returns
   * false otherwise.
   * 
   * @return true if the size of a team that submits a claim on this promotion is limited; returns
   *         false otherwise.
   */
  public boolean isTeamHasMax()
  {
    return teamHasMax;
  }

  /**
   * Sets the "team has max" flag. This flag is true if the size of a team that submits a claim on
   * this promtion is limited; this flag is false otherwise.
   * 
   * @param teamHasMax true if the size of a team that submits a claim on this promtion is limited;
   *          false otherwise.
   */
  public void setTeamHasMax( boolean teamHasMax )
  {
    this.teamHasMax = teamHasMax;
  }

  /**
   * Does a deep copy of the promotion and its children if specified. This is a customized
   * implementation of
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newPromotionName
   * @param newChildPromotionNameHolders
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    ProductClaimPromotion clonedPromotion = (ProductClaimPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );

    // copy the promotionPayoutGroups
    clonedPromotion.setPromotionPayoutGroups( new ArrayList() );
    for ( Iterator iter = this.getPromotionPayoutGroups().iterator(); iter.hasNext(); )
    {
      PromotionPayoutGroup promotionPayoutGroupToCopy = (PromotionPayoutGroup)iter.next();
      clonedPromotion.addPromotionPayoutGroup( (PromotionPayoutGroup)promotionPayoutGroupToCopy.clone() );
    }

    // copy the stackRankPayoutGroups
    clonedPromotion.setStackRankPayoutGroups( new LinkedHashSet() );
    for ( Iterator iter = this.getStackRankPayoutGroups().iterator(); iter.hasNext(); )
    {
      StackRankPayoutGroup promotionStackRankPayoutGroup = (StackRankPayoutGroup)iter.next();
      clonedPromotion.addStackRankPayoutGroup( (StackRankPayoutGroup)promotionStackRankPayoutGroup.clone() );
    }

    // copy the promotionPrimaryAudiences
    clonedPromotion.setPromotionPrimaryAudiences( new LinkedHashSet() );
    for ( Iterator promotionSubmitterAudiencesIter = this.getPromotionPrimaryAudiences().iterator(); promotionSubmitterAudiencesIter.hasNext(); )
    {
      PromotionPrimaryAudience promotionSubmitterAudience = (PromotionPrimaryAudience)promotionSubmitterAudiencesIter.next();
      clonedPromotion.addPromotionPrimaryAudience( (PromotionPrimaryAudience)promotionSubmitterAudience.clone() );
    }

    // copy the promotionSecondaryAudiences
    clonedPromotion.setPromotionSecondaryAudiences( new LinkedHashSet() );
    for ( Iterator promotionSecondaryAudiencesIter = this.getPromotionSecondaryAudiences().iterator(); promotionSecondaryAudiencesIter.hasNext(); )
    {
      PromotionSecondaryAudience promotionSecondaryAudience = (PromotionSecondaryAudience)promotionSecondaryAudiencesIter.next();
      clonedPromotion.addPromotionSecondaryAudience( (PromotionSecondaryAudience)promotionSecondaryAudience.clone() );
    }

    // copy promotionTeamPositions
    clonedPromotion.setPromotionTeamPositions( new LinkedHashSet() );
    for ( Iterator promotionTeamPositionsIter = this.promotionTeamPositions.iterator(); promotionTeamPositionsIter.hasNext(); )
    {
      PromotionTeamPosition promotionTeamPosition = (PromotionTeamPosition)promotionTeamPositionsIter.next();
      clonedPromotion.addPromotionTeamPositions( (PromotionTeamPosition)promotionTeamPosition.clone() );
    }

    if ( cloneWithChildren )
    {
      // copy childPromotions
      clonedPromotion.setChildPromotions( new LinkedHashSet() );
      Iterator childPromotionsIter = this.childPromotions.iterator();
      int childCounter = 0;
      while ( childPromotionsIter.hasNext() )
      {
        ProductClaimPromotion childPromotion = (ProductClaimPromotion)childPromotionsIter.next();
        // Give the child it's new name
        PromotionCopyHolder promotionCopyHolder = (PromotionCopyHolder)newChildPromotionNameHolders.get( childCounter );

        // Copy the child promotion
        ProductClaimPromotion copiedChildPromotion = (ProductClaimPromotion)childPromotion.deepCopy( false, promotionCopyHolder.getNewName(), null );

        // add the child to the parent promotion
        clonedPromotion.addChildPromotion( copiedChildPromotion );
        childCounter++;
      }
    }
    else
    {
      // replace the child promotions with nothing
      clonedPromotion.setChildPromotions( new LinkedHashSet( 0 ) );
      clonedPromotion.setChildrenCount( 0 );
    }

    return clonedPromotion;

  }

  /**
   * @return value of promotionTeamPositions property
   */
  public Set getPromotionTeamPositions()
  {
    return promotionTeamPositions;
  }

  /**
   * @param promotionTeamPositions value for promotionTeamPositions property
   */
  public void setPromotionTeamPositions( Set promotionTeamPositions )
  {
    this.promotionTeamPositions = promotionTeamPositions;
  }

  /**
   * Add a promotionTeamPosition to promotionTeamPositions
   * 
   * @param promotionTeamPosition
   */
  public void addPromotionTeamPosition( PromotionTeamPosition promotionTeamPosition )
  {
    promotionTeamPosition.setPromotion( this );
    this.promotionTeamPositions.add( promotionTeamPosition );
  }

  public boolean hasParent()
  {
    return this.getParentPromotion() != null;
  }

  /**
   * Get parentPromotion
   * 
   * @return parentPromotion
   */
  public ProductClaimPromotion getParentPromotion()
  {
    return parentPromotion;
  }

  /**
   * Set parentPromotion
   * 
   * @param parentPromotion
   */
  public void setParentPromotion( ProductClaimPromotion parentPromotion )
  {
    this.parentPromotion = parentPromotion;
  }

  /**
   * Get childPromotions
   * 
   * @return childPromotions
   */
  public Set getChildPromotions()
  {
    return childPromotions;
  }

  /**
   * Set childPromotions
   * 
   * @param childPromotions
   */
  public void setChildPromotions( Set childPromotions )
  {
    this.childPromotions = childPromotions;
  }

  /**
   * When childPromotion is loaded (lazy=true), retuns the number of child promotions Use
   * getChildrenCount() to get the number of child promotions associated with this promotion from
   * the database directly
   * 
   * @return the number of child promotions this promotion has
   */
  public int getChildPromotionCount()
  {
    if ( getChildPromotions() != null && getChildPromotions().size() > 0 )
    {
      return getChildPromotions().size();
    }

    return 0;
  }

  /**
   * Add a promotion to the set of childr promotions.
   * 
   * @param promotion
   */
  public void addChildPromotion( ProductClaimPromotion promotion )
  {
    if ( childPromotions == null )
    {
      childPromotions = new LinkedHashSet();
    }

    promotion.setParentPromotion( this );
    this.childPromotions.add( promotion );
  }

  /**
   * Get Live Parent and its Live Children promotions
   * 
   * @return livePrimaryAndChildPromotions
   */
  public Set getLivePrimaryAndChildPromotions()
  {
    Set livePrimaryAndChildPromotions = super.getLivePrimaryAndChildPromotions();

    for ( Iterator iter = childPromotions.iterator(); iter.hasNext(); )
    {
      ProductClaimPromotion childPromotion = (ProductClaimPromotion)iter.next();
      if ( childPromotion.isLive() )
      {
        livePrimaryAndChildPromotions.add( childPromotion );
      }
    }

    return livePrimaryAndChildPromotions;
  }

  /**
   * This function can be useful for sorting the promotion list by name. As we have issue of keeping
   * parent/child together with reverse sorting we might need to find a away to keep parent child
   * together. Overridden from
   * 
   * @see com.biperf.core.domain.promotion.Promotion#getSortName()
   * @return String
   */
  public String getSortName()
  {
    String finalSortName = "";
    if ( hasParent() )
    {
      finalSortName = getParentPromotion().getName().concat( getName() );
    }
    else
    {
      finalSortName = getName();
    }

    return finalSortName;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.promotion.Promotion#isClaimFormUsed()
   */
  public boolean isClaimFormUsed()
  {
    return true;
  }

  /**
   * Get the number of child promotion(s) this promotion has the count comes directly from the
   * database query
   * 
   * @return childrenCount
   */
  public int getChildrenCount()
  {
    return childrenCount;
  }

  /**
   * Set the number of child promotion(s) this promotion has the count comes directly from the
   * database query
   * 
   * @param childrenCount
   */
  public void setChildrenCount( int childrenCount )
  {
    this.childrenCount = childrenCount;
  }

  /**
   * Get has sweepstakes to process Note: Promotion needs to be fully hydrated with sweepstakes
   * 
   * @return boolean returns true if there are sweepstakes to process
   */
  public boolean getHasSweepstakesToProcess()
  {
    if ( this.promotionSweepstakes != null )
    {
      Iterator iter = this.promotionSweepstakes.iterator();
      while ( iter.hasNext() )
      {
        PromotionSweepstake sweepstake = (PromotionSweepstake)iter.next();
        if ( !sweepstake.isProcessed() )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Get has sweepstakes history (sweepstakes that have been processed) Note: Promotion needs to be
   * fully hydrated with sweepstakes
   * 
   * @return boolean returns true if there is a sweepstakes history
   */
  public boolean getHasSweepstakesHistory()
  {
    if ( this.promotionSweepstakes != null )
    {
      Iterator iter = this.promotionSweepstakes.iterator();
      while ( iter.hasNext() )
      {
        PromotionSweepstake sweepstake = (PromotionSweepstake)iter.next();
        if ( sweepstake.isProcessed() )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Get the associated set of stackRankPayoutGroups.
   * 
   * @return Set
   */
  public Set getStackRankPayoutGroups()
  {
    return stackRankPayoutGroups;
  }

  /**
   * Set the stackRankPayoutGroups to the promotion
   * 
   * @param stackRankPayoutGroups
   */
  public void setStackRankPayoutGroups( Set stackRankPayoutGroups )
  {
    this.stackRankPayoutGroups = stackRankPayoutGroups;
  }

  /**
   * Add a stackRankPayoutGroup to stackRankPayoutGroups
   * 
   * @param stackRankPayoutGroup
   */
  public void addStackRankPayoutGroup( StackRankPayoutGroup stackRankPayoutGroup )
  {
    stackRankPayoutGroup.setPromotion( this );
    this.stackRankPayoutGroups.add( stackRankPayoutGroup );
  }

  /**
   * Return the StackRankPayoutGroup for the given nodeType, or null if none exists.
   */
  public StackRankPayoutGroup getStackRankPayoutGroup( NodeType nodeType )
  {
    StackRankPayoutGroup matchingStackRankPayoutGroup = null;
    for ( Iterator iter = stackRankPayoutGroups.iterator(); iter.hasNext(); )
    {
      StackRankPayoutGroup stackRankPayoutGroup = (StackRankPayoutGroup)iter.next();
      if ( stackRankPayoutGroup.getNodeType().equals( nodeType ) )
      {
        matchingStackRankPayoutGroup = stackRankPayoutGroup;
        break;
      }
    }

    return matchingStackRankPayoutGroup;
  }

  public boolean isDisplayFullListLinkToParticipants()
  {
    return displayFullListLinkToParticipants;
  }

  public void setDisplayFullListLinkToParticipants( boolean displayFullListLinkToParticipants )
  {
    this.displayFullListLinkToParticipants = displayFullListLinkToParticipants;
  }

  public boolean isDisplayStackRankFactor()
  {
    return displayStackRankFactor;
  }

  public void setDisplayStackRankFactor( boolean displayStackRankFactor )
  {
    this.displayStackRankFactor = displayStackRankFactor;
  }

  public StackRankApprovalType getStackRankApprovalType()
  {
    return stackRankApprovalType;
  }

  public void setStackRankApprovalType( StackRankApprovalType stackRankApprovalType )
  {
    this.stackRankApprovalType = stackRankApprovalType;
  }

  public ClaimFormStepElement getStackRankClaimFormStepElement()
  {
    return stackRankClaimFormStepElement;
  }

  public void setStackRankClaimFormStepElement( ClaimFormStepElement stackRankClaimFormStepElement )
  {
    this.stackRankClaimFormStepElement = stackRankClaimFormStepElement;
  }

  public StackRankFactorType getStackRankFactorType()
  {
    return stackRankFactorType;
  }

  public void setStackRankFactorType( StackRankFactorType stackRankFactorType )
  {
    this.stackRankFactorType = stackRankFactorType;
  }

  public Integer getDefaultQuantity()
  {
    return defaultQuantity;
  }

  public void setDefaultQuantity( Integer defaultQuantity )
  {
    this.defaultQuantity = defaultQuantity;
  }

}

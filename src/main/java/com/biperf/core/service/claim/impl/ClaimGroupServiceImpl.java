/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/impl/ClaimGroupServiceImpl.java,v $
 */

package com.biperf.core.service.claim.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.dao.claim.ClaimGroupDAO;
import com.biperf.core.dao.claim.hibernate.ApproverSeekingClaimGroupQueryConstraint;
import com.biperf.core.dao.claim.hibernate.ApproverSeekingNominationClaimGroupQueryConstraint;
import com.biperf.core.dao.claim.hibernate.ClaimGroupQueryConstraint;
import com.biperf.core.dao.claim.hibernate.JournalClaimGroupQueryConstraint;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionApprovableValue;

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
 * <td>Apr 26, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimGroupServiceImpl implements ClaimGroupService
{

  ClaimGroupDAO claimGroupDAO;

  @Override
  public ClaimGroup saveClaimGroup( ClaimGroup claimGroup )
  {
    return claimGroupDAO.saveClaimGroup( claimGroup );
  }

  /**
   * Returns the claim groups specified by the given query constraint.
   *
   * @param queryConstraint  the query constraint.
   * @param associationRequestCollection  initializes properties of the returned claim groups.
   * @return the specified claim groups, as a <code>List</code> of {@link ClaimGroup} objects.
   */
  public List getClaimGroupList( JournalClaimGroupQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    return claimGroupDAO.getClaimGroupList( queryConstraint, associationRequestCollection );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimGroupService#getClaimGroupById(java.lang.Long)
   * @param claimGroupId
   */
  public ClaimGroup getClaimGroupById( Long claimGroupId )
  {
    return claimGroupDAO.getClaimGroupById( claimGroupId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimGroupService#getClaimGroupByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param claimGroupId
   * @param associationRequestCollection
   */
  public ClaimGroup getClaimGroupByIdWithAssociations( Long claimGroupId, AssociationRequestCollection associationRequestCollection )
  {
    return claimGroupDAO.getClaimGroupByIdWithAssociations( claimGroupId, associationRequestCollection );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimGroupService#getClaimGroupList(com.biperf.core.dao.claim.hibernate.ClaimGroupQueryConstraint)
   * @param claimGroupQueryConstraint
   */
  public List getClaimGroupList( ClaimGroupQueryConstraint claimGroupQueryConstraint )
  {
    return claimGroupDAO.getClaimGroupList( claimGroupQueryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimGroupService#getClaimGroupListCount(com.biperf.core.dao.claim.hibernate.ClaimGroupQueryConstraint)
   * @param claimGroupQueryConstraint
   */
  public int getClaimGroupListCount( ClaimGroupQueryConstraint claimGroupQueryConstraint )
  {
    return claimGroupDAO.getClaimGroupListCount( claimGroupQueryConstraint );
  }

  /**
   * .
   * 
   * @param approverUserId
   * @param includedPromotionIds
   * @param isOpen - if set null, return both open and closed, and return claims already
   * approved by approverUserId
   * @param promotionType
   * @param claimGroupAssociationRequestCollection
   * @param promotionAssociationRequestCollection
   */
  public List getClaimGroupsForApprovalByUser( Long approverUserId,
                                               Long[] includedPromotionIds,
                                               Boolean isOpen,
                                               PromotionType promotionType,
                                               AssociationRequestCollection claimGroupAssociationRequestCollection,
                                               AssociationRequestCollection promotionAssociationRequestCollection,
                                               Boolean expired,
                                               Boolean skipOpenForTile )
  {
    List promotionclaimGroupsValueList = new ArrayList();

    ApproverSeekingClaimGroupQueryConstraint claimGroupQueryConstraint = new ApproverSeekingClaimGroupQueryConstraint();
    if ( BooleanUtils.isFalse( expired ) && BooleanUtils.isTrue( isOpen ) )
    {
      claimGroupQueryConstraint.setApprovableUserId( approverUserId );
    }
    else
    {
      claimGroupQueryConstraint.setApprovedUserId( approverUserId );
    }
    if ( !skipOpenForTile )
    {
      claimGroupQueryConstraint.setOpen( isOpen );
    }
    claimGroupQueryConstraint.setExpired( expired );
    claimGroupQueryConstraint.setClaimGroupPromotionType( promotionType );
    claimGroupQueryConstraint.setIncludedPromotionIds( includedPromotionIds );

    List approvableClaimGroups = getClaimGroupList( claimGroupQueryConstraint );

    if ( approvableClaimGroups.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    // Apply any associations to results

    Map claimGroupsMapByPromotion = new HashMap();
    for ( Iterator iter = approvableClaimGroups.iterator(); iter.hasNext(); )
    {
      ClaimGroup claimGroup = (ClaimGroup)iter.next();

      if ( claimGroupAssociationRequestCollection != null )
      {
        claimGroupAssociationRequestCollection.process( claimGroup );
      }

      Promotion claimGroupPromotion = claimGroup.getPromotion();
      List singlePromotionclaimGroups = (List)claimGroupsMapByPromotion.get( claimGroupPromotion );
      if ( singlePromotionclaimGroups == null )
      {
        singlePromotionclaimGroups = new ArrayList();
        claimGroupsMapByPromotion.put( claimGroupPromotion, singlePromotionclaimGroups );
      }
      singlePromotionclaimGroups.add( claimGroup );
    }

    for ( Iterator iter = claimGroupsMapByPromotion.keySet().iterator(); iter.hasNext(); )
    {
      Promotion claimGroupPromotion = (Promotion)iter.next();
      List singlePromotionClaimGroups = (List)claimGroupsMapByPromotion.get( claimGroupPromotion );
      PromotionApprovableValue promotionApprovableValue = new PromotionApprovableValue();
      promotionApprovableValue.setPromotion( claimGroupPromotion );
      promotionApprovableValue.setApprovables( singlePromotionClaimGroups );
      promotionclaimGroupsValueList.add( promotionApprovableValue );
    }

    if ( promotionAssociationRequestCollection != null )
    {
      for ( Iterator iter = promotionclaimGroupsValueList.iterator(); iter.hasNext(); )
      {
        PromotionApprovableValue promotionApprovableValue = (PromotionApprovableValue)iter.next();

        promotionAssociationRequestCollection.process( promotionApprovableValue.getPromotion() );
      }
    }

    return promotionclaimGroupsValueList;
  }

  public List getNominationClaimGroupsForApprovalByUser( Long approverUserId,
                                                         String claimIds,
                                                         Long[] includedPromotionIds,
                                                         Boolean isOpen,
                                                         PromotionType promotionType,
                                                         AssociationRequestCollection claimGroupAssociationRequestCollection,
                                                         AssociationRequestCollection promotionAssociationRequestCollection,
                                                         Boolean expired,
                                                         String filterApprovalStatusCode,
                                                         Long approvalRound )
  {
    List promotionclaimGroupsValueList = new ArrayList();

    ApproverSeekingNominationClaimGroupQueryConstraint claimGroupQueryConstraint = new ApproverSeekingNominationClaimGroupQueryConstraint();
    if ( BooleanUtils.isFalse( expired ) && BooleanUtils.isTrue( isOpen ) && !UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
    {
      claimGroupQueryConstraint.setUserId( approverUserId );
    }
    else
    {
      if ( !UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
      {
        claimGroupQueryConstraint.setUserId( approverUserId );
      }
      claimGroupQueryConstraint.setApprovalStatusType( filterApprovalStatusCode );
    }

    claimGroupQueryConstraint.setOpen( isOpen );
    claimGroupQueryConstraint.setExpired( expired );
    claimGroupQueryConstraint.setClaimGroupPromotionType( promotionType );
    claimGroupQueryConstraint.setIncludedPromotionIds( includedPromotionIds );
    claimGroupQueryConstraint.setApprovalRound( approvalRound );
    claimGroupQueryConstraint.setClaimIds( claimIds );

    List approvableClaimGroups = getClaimGroupList( claimGroupQueryConstraint );

    if ( approvableClaimGroups.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    // Apply any associations to results

    Map claimGroupsMapByPromotion = new HashMap();
    for ( Iterator iter = approvableClaimGroups.iterator(); iter.hasNext(); )
    {
      ClaimGroup claimGroup = (ClaimGroup)iter.next();

      if ( claimGroupAssociationRequestCollection != null )
      {
        claimGroupAssociationRequestCollection.process( claimGroup );
      }

      Promotion claimGroupPromotion = claimGroup.getPromotion();
      List singlePromotionclaimGroups = (List)claimGroupsMapByPromotion.get( claimGroupPromotion );
      if ( singlePromotionclaimGroups == null )
      {
        singlePromotionclaimGroups = new ArrayList();
        claimGroupsMapByPromotion.put( claimGroupPromotion, singlePromotionclaimGroups );
      }
      singlePromotionclaimGroups.add( claimGroup );
    }

    for ( Iterator iter = claimGroupsMapByPromotion.keySet().iterator(); iter.hasNext(); )
    {
      Promotion claimGroupPromotion = (Promotion)iter.next();
      List singlePromotionClaimGroups = (List)claimGroupsMapByPromotion.get( claimGroupPromotion );
      PromotionApprovableValue promotionApprovableValue = new PromotionApprovableValue();
      promotionApprovableValue.setPromotion( claimGroupPromotion );
      promotionApprovableValue.setApprovables( singlePromotionClaimGroups );
      promotionclaimGroupsValueList.add( promotionApprovableValue );
    }

    if ( promotionAssociationRequestCollection != null )
    {
      for ( Iterator iter = promotionclaimGroupsValueList.iterator(); iter.hasNext(); )
      {
        PromotionApprovableValue promotionApprovableValue = (PromotionApprovableValue)iter.next();

        promotionAssociationRequestCollection.process( promotionApprovableValue.getPromotion() );
      }
    }

    return promotionclaimGroupsValueList;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimGroupService#getClaimGroupsForApprovalByUserCount(java.lang.Long,
   *      com.biperf.core.domain.enums.PromotionType)
   * @param approverUserId
   * @param promotionType
   */
  public int getClaimGroupsForApprovalByUserCount( Long approverUserId, PromotionType promotionType )
  {
    ApproverSeekingClaimGroupQueryConstraint claimGroupQueryConstraint = new ApproverSeekingClaimGroupQueryConstraint();
    claimGroupQueryConstraint.setApprovableUserId( approverUserId );
    claimGroupQueryConstraint.setOpen( Boolean.TRUE );
    claimGroupQueryConstraint.setExpired( Boolean.FALSE );
    claimGroupQueryConstraint.setClaimGroupPromotionType( promotionType );

    return getClaimGroupListCount( claimGroupQueryConstraint );
  }

  public int getNominationClaimGroupsForApprovalByUserCount( Long approverUserId, PromotionType promotionType )
  {
    ApproverSeekingNominationClaimGroupQueryConstraint claimGroupQueryConstraint = new ApproverSeekingNominationClaimGroupQueryConstraint();
    claimGroupQueryConstraint.setUserId( approverUserId );
    claimGroupQueryConstraint.setOpen( Boolean.TRUE );
    claimGroupQueryConstraint.setExpired( Boolean.FALSE );
    claimGroupQueryConstraint.setClaimGroupPromotionType( promotionType );

    return getClaimGroupListCount( claimGroupQueryConstraint );
  }

  public int getUserInClaimGroupsApprovalAudienceCount( Long approverUserId, PromotionType promotionType )
  {
    ApproverSeekingClaimGroupQueryConstraint approverSnapshotClaimQueryConstraint = new ApproverSeekingClaimGroupQueryConstraint();
    approverSnapshotClaimQueryConstraint.setApprovedUserId( approverUserId );
    approverSnapshotClaimQueryConstraint.setExpired( Boolean.FALSE );
    return getClaimGroupListCount( approverSnapshotClaimQueryConstraint );
  }

  public int getUserNominationInClaimGroupsApprovalAudienceCount( Long approverUserId, PromotionType promotionType )
  {
    ApproverSeekingNominationClaimGroupQueryConstraint approverSnapshotClaimQueryConstraint = new ApproverSeekingNominationClaimGroupQueryConstraint();
    approverSnapshotClaimQueryConstraint.setUserId( approverUserId );
    approverSnapshotClaimQueryConstraint.setExpired( Boolean.FALSE );
    return getClaimGroupListCount( approverSnapshotClaimQueryConstraint );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimGroupService#getOpenClaimGroupsWithNoMatchingNodeInApproverHierarchy(com.biperf.core.domain.enums.ApproverType)
   * @param approverType
   */
  public List getOpenClaimGroupsWithNoMatchingNodeInApproverHierarchy( ApproverType approverType )
  {
    return null;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimGroupService#hasPendingJournalForClaimGroup(java.lang.Long,
   *      java.lang.Long)
   * @param nomineeId
   * @param claimGroupId
   */
  public boolean hasPendingJournalForClaimGroup( Long nomineeId, Long claimGroupId )
  {
    return false;
  }

  @Override
  public Journal getJournalForClaimGroup( Long claimGroupId )
  {
    return claimGroupDAO.getJournalForClaimGroup( claimGroupId );
  }

  /**
   * @param claimGroupDAO value for claimGroupDAO property
   */
  public void setClaimGroupDAO( ClaimGroupDAO claimGroupDAO )
  {
    this.claimGroupDAO = claimGroupDAO;
  }

}

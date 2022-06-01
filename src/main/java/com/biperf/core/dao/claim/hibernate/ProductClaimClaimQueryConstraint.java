/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.type.StandardBasicTypes;

import com.biperf.core.domain.claim.ProductClaim;

/**
 * ProductClaimClaimQueryConstraint.
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
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ProductClaimClaimQueryConstraint extends ClaimQueryConstraint
{
  /**
   * constrain by claims submitted by submitterId
   */
  private Long submitterOrTeamMemberParticipantId;

  /**
   * Constrain by manager pax id.
   */
  private Long managerId;

  /**
   * include claims that the participant is the submitter for - ignored if participantId is null.
   */
  private boolean submitter;

  /**
   * include claims where the participant is a team member (i.e., a participant in claimParticipant) -
   * ignored if participantId is null.
   */
  private boolean teamMember;

  /**
   * include closed claims where all line items are approved
   */
  private boolean eligibleClaimsAllApprovedItem;

  /**
   * include closed claims where atlease onr line item is approved
   */
  private boolean eligibleClaimsAtleastOneApprovedItem;

  /*
   * include only manager override claims
   */
  private boolean managerOverrideItem;

  public boolean isManagerOverrideItem()
  {
    return managerOverrideItem;
  }

  public void setManagerOverrideItem( boolean managerOverrideItem )
  {
    this.managerOverrideItem = managerOverrideItem;
  }

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return ProductClaim.class;
  }

  /**
   * Not used since product claim query requires a Junction on submitterId in the case where we are
   * looking for either a submitter or a team member. Instead use
   * {@link #setSubmitterOrTeamMemberParticipantId(Long)}. Overridden from
   * 
   * @see com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint#setSubmitterId(java.lang.Long)
   * @param submitterId
   */
  public void setSubmitterId( Long submitterId )
  {
    // Ignore
    super.setSubmitterId( null );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.QueryConstraint#buildCriteria()
   * @return Criteria
   */
  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    if ( submitterOrTeamMemberParticipantId != null )
    {

      SimpleExpression submitterCriterion = null;
      Criterion userExistsCriterion = null;

      // First create Criterion for submitter and teamMember cases
      if ( teamMember )
      {
        userExistsCriterion = Restrictions.sqlRestriction( "{alias}.claim_id in (select claim_id from CLAIM_PARTICIPANT where PARTICIPANT_ID = ?)",
                                                           submitterOrTeamMemberParticipantId,
                                                           StandardBasicTypes.LONG );

      }

      if ( submitter )
      {
        submitterCriterion = Restrictions.eq( "claim.submitter.id", submitterOrTeamMemberParticipantId );
      }

      // Now add to main criteria
      if ( submitter && !teamMember )
      {
        criteria.add( submitterCriterion );
      }

      if ( !submitter && teamMember )
      {
        criteria.add( userExistsCriterion );
      }

      if ( submitter && teamMember )
      {
        // searching for submitters or team members with the given paxid.
        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add( submitterCriterion );
        disjunction.add( userExistsCriterion );
        criteria.add( disjunction );
      }
    }

    if ( eligibleClaimsAllApprovedItem )
    {
      Criterion eligibleClaimsAllApprovedCriterion = null;
      eligibleClaimsAllApprovedCriterion = Restrictions
          .sqlRestriction( "{alias}.claim_id not in (select claim_id from CLAIM_ITEM ci, CLAIM_PRODUCT cp where ci.APPROVAL_STATUS_TYPE != 'approv'" + " and ci.CLAIM_ITEM_ID = cp.CLAIM_ITEM_ID)" );

      criteria.add( eligibleClaimsAllApprovedCriterion );
    }

    if ( eligibleClaimsAtleastOneApprovedItem )
    {
      Criterion eligibleClaimsAtleastOneApprovedCriterion = null;
      eligibleClaimsAtleastOneApprovedCriterion = Restrictions
          .sqlRestriction( "{alias}.claim_id in (select distinct claim_id from CLAIM_ITEM ci, CLAIM_PRODUCT cp where ci.APPROVAL_STATUS_TYPE = 'approv'"
              + " and ci.CLAIM_ITEM_ID = cp.CLAIM_ITEM_ID)" );
      criteria.add( eligibleClaimsAtleastOneApprovedCriterion );
    }

    if ( managerOverrideItem )
    {
      Criterion eligibleClaimsAtleastOneApprovedCriterion = null;
      eligibleClaimsAtleastOneApprovedCriterion = Restrictions
          .sqlRestriction( "{alias}.claim_id in (select distinct claim_id from ACTIVITY a where a.PRODUCT_ID is null and a.ACTIVITY_DISCRIM = 'mo'" + " and a.USER_ID = '" + managerId + "')" );
      criteria.add( eligibleClaimsAtleastOneApprovedCriterion );
    }

    return criteria;
  }

  /**
   * @return value of submitter property
   */
  public boolean isSubmitter()
  {
    return submitter;
  }

  /**
   * @param submitter value for submitter property
   */
  public void setSubmitter( boolean submitter )
  {
    this.submitter = submitter;
  }

  /**
   * @return value of submitterOrTeamMemberParticipantId property
   */
  public Long getSubmitterOrTeamMemberParticipantId()
  {
    return submitterOrTeamMemberParticipantId;
  }

  /**
   * @param submitterOrTeamMemberParticipantId value for submitterOrTeamMemberParticipantId property
   */
  public void setSubmitterOrTeamMemberParticipantId( Long submitterOrTeamMemberParticipantId )
  {
    this.submitterOrTeamMemberParticipantId = submitterOrTeamMemberParticipantId;
  }

  /**
   * @return value of teamMember property
   */
  public boolean isTeamMember()
  {
    return teamMember;
  }

  /**
   * @param teamMember value for teamMember property
   */
  public void setTeamMember( boolean teamMember )
  {
    this.teamMember = teamMember;
  }

  /**
   * @return boolean
   */
  public boolean isEligibleClaimsAllApprovedItem()
  {
    return eligibleClaimsAllApprovedItem;
  }

  /**
   * @param eligibleClaimsAllApprovedItem
   */
  public void setEligibleClaimsAllApprovedItem( boolean eligibleClaimsAllApprovedItem )
  {
    this.eligibleClaimsAllApprovedItem = eligibleClaimsAllApprovedItem;
  }

  /**
   * @return boolean
   */
  public boolean isEligibleClaimsAtleastOneApprovedItem()
  {
    return eligibleClaimsAtleastOneApprovedItem;
  }

  /**
   * @param eligibleClaimsAtleastOneApprovedItem
   */
  public void setEligibleClaimsAtleastOneApprovedItem( boolean eligibleClaimsAtleastOneApprovedItem )
  {
    this.eligibleClaimsAtleastOneApprovedItem = eligibleClaimsAtleastOneApprovedItem;
  }

  public Long getManagerId()
  {
    return managerId;
  }

  public void setManagerId( Long managerId )
  {
    this.managerId = managerId;
  }

}

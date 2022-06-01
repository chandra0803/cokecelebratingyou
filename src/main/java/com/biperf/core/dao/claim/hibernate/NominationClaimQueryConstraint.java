/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/NominationClaimQueryConstraint.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.NominationEvaluationType;

public class NominationClaimQueryConstraint extends AbstractRecognitionClaimQueryConstraint
{

  private static final long serialVersionUID = 1L;

  /**
   * if true, include claims whose promotion is cumulative. if false, not cumulative. 
   */
  private Boolean cumulative;

  private String approvalStatusType;

  private NominationClaimStatusType nominationStatusType;

  private Long[] includedClaimIds;
  
  /**
   * If present, obtain only claims where the given Participant ID is a recipient of a team based nomination
   */
  private Long teamMemberId;

  @SuppressWarnings( "rawtypes" )
  public Class getResultClass()
  {
    return NominationClaim.class;
  }

  @SuppressWarnings( "unused" )
  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();
    String approvalStatusTypeCode = ApprovalStatusType.NON_WINNER;
    if ( cumulative != null )
    {
      String nominationEvalTypeCode;
      if ( cumulative.booleanValue() )
      {
        nominationEvalTypeCode = NominationEvaluationType.CUMULATIVE;
      }
      else
      {
        nominationEvalTypeCode = NominationEvaluationType.INDEPENDENT;
      }
      criteria.createAlias( "claim.promotion", "promotion" ).add( Restrictions.eq( "promotion.evaluationType", NominationEvaluationType.lookup( nominationEvalTypeCode ) ) );
    }

    if ( nominationStatusType != null )
    {
      criteria.add( Restrictions.eq( "claim.nominationStatusType", nominationStatusType ) ); // currently
                                                                                             // nominationStatusType
                                                                                             // located
                                                                                             // in
                                                                                             // claim
                                                                                             // , it
                                                                                             // has
                                                                                             // to
                                                                                             // be
                                                                                             // moved
                                                                                             // to
                                                                                             // NominationClaim
    }
    // Team member participation
    if ( teamMemberId != null )
    {
      criteria.createAlias( "claim.claimRecipients", "teamMember" ).add( Restrictions.eq( "teamMember.recipient.id", teamMemberId ) );
    }

    /* coke customization start */
    if ( includedClaimIds != null )
    {
      criteria.add( Restrictions.in( "claim.id", includedClaimIds ) );
    }
    /* coke customization end */
    return criteria;
  }

  public Boolean getCumulative()
  {
    return cumulative;
  }

  public void setCumulative( Boolean cumulative )
  {
    this.cumulative = cumulative;
  }

  public String getApprovalStatusType()
  {
    return approvalStatusType;
  }

  public void setApprovalStatusType( String approvalStatusType )
  {
    this.approvalStatusType = approvalStatusType;
  }

  public NominationClaimStatusType getNominationStatusType()
  {
    return nominationStatusType;
  }

  public void setNominationStatusType( NominationClaimStatusType nominationStatusType )
  {
    this.nominationStatusType = nominationStatusType;
  }

  public Long getTeamMemberId()
  {
    return teamMemberId;
  }

  public void setTeamMemberId( Long teamMemberId )
  {
    this.teamMemberId = teamMemberId;
  }
  /* coke customization start */
  public Long[] getIncludedClaimIds()
  {
    return includedClaimIds;
  }

  public void setIncludedClaimIds( Long[] includedClaimIds )
  {
    this.includedClaimIds = includedClaimIds;
  }
  /* coke customization end */
}

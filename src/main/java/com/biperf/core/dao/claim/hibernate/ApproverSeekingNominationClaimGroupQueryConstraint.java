/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/Attic/ApproverSeekingNominationClaimGroupQueryConstraint.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.utils.UserManager;

/**
 * Extracted to pull out approver specific claim query criteria. Also needed, since we can't query
 * by class the normal way due to using native SQL in our constraints (bombs when searching for
 * anything other than the base class)
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
public class ApproverSeekingNominationClaimGroupQueryConstraint extends ClaimGroupQueryConstraint
{

  /**
   * Include only claimGroups that can be approved by the approverUser, based on the current data in
   * the approver claim snapshot.
   */
  private Long userId;

  /**
   * get claimGroups with promotions of a specific promotion type. This is a workaround alternative
   * to setting result type to a specific Claim subclass to avoid a hibernate bug where native sql
   * can't be used in subclass queries.
   */
  private PromotionType claimGroupPromotionType;

  private String approvalStatusType;

  /**
   * If true, only return expired (open past approval end date). If false, only return non-expired .
   * If null return both. if true, forces open to true.
   */
  private Boolean expired;

  private Long approvalRound;

  private String claimIds;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint#buildCriteria()
   */
  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    if ( BooleanUtils.isTrue( getOpen() ) )
    {
      Criterion approvalNonExpiredCriteria = Restrictions
          .sqlRestriction( "{alias}.promotion_id in ( " + "select promotion_id from promotion " + "where trunc(SYSDATE) <= trunc(NVL(approval_end_date, SYSDATE)) ) " );

      Criterion approverNonExpiredCriteria = Restrictions.sqlRestriction( "{alias}.claim_group_id in ( " + " select c.claim_group_id from claim c where c.claim_id in( "
          + " select ci.claim_id from claim_item ci where approval_status_type = 'pend' and claim_id in ( " + claimIds + " ) ) and c.approval_round = ?)", approvalRound, StandardBasicTypes.LONG );

      LogicalExpression orExp = Restrictions.or( approvalNonExpiredCriteria, approverNonExpiredCriteria );
      criteria.add( orExp );

      // Include the claims for approval stored by snapshot and thos not saved by snapshot -
      // specific approvers
      if ( !UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
      {
        Criterion snapshotCriteria = Restrictions.sqlRestriction( "{alias}.claim_group_id in (select claim_group_id from claim_approver_snapshot where approver_user_id=?) ",
                                                                  userId,
                                                                  StandardBasicTypes.LONG );
        criteria.add( snapshotCriteria );
      }
      else
      {
        Criterion snapshotCriteria = Restrictions.sqlRestriction( "{alias}.claim_group_id in (select claim_group_id from claim_approver_snapshot) " );
        criteria.add( snapshotCriteria );
      }
    }

    if ( claimGroupPromotionType != null )
    {
      criteria.createAlias( "claimGroup.promotion", "promotion" ).add( Restrictions.eq( "promotion.promotionType", claimGroupPromotionType ) );
    }

    return criteria;
  }

  /**
   * @return value of claimGroupPromotionType property
   */
  public PromotionType getClaimGroupPromotionType()
  {
    return claimGroupPromotionType;
  }

  /**
   * @param claimGroupPromotionType value for claimGroupPromotionType property
   */
  public void setClaimGroupPromotionType( PromotionType claimGroupPromotionType )
  {
    this.claimGroupPromotionType = claimGroupPromotionType;
  }

  /**
   * @return value of approvableUserId property
   */
  public Long getUserId()
  {
    return userId;
  }

  /**
   * @param approvableUserId value for approvableUserId property
   */
  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Boolean getExpired()
  {
    return expired;
  }

  public void setExpired( Boolean expired )
  {
    this.expired = expired;
  }

  public String getApprovalStatusType()
  {
    return approvalStatusType;
  }

  public void setApprovalStatusType( String approvalStatusType )
  {
    this.approvalStatusType = approvalStatusType;
  }

  public Long getApprovalRound()
  {
    return approvalRound;
  }

  public void setApprovalRound( Long approvalRound )
  {
    this.approvalRound = approvalRound;
  }

  public String getClaimIds()
  {
    return claimIds;
  }

  public void setClaimIds( String claimIds )
  {
    this.claimIds = claimIds;
  }

}

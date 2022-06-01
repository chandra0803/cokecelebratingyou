/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/ApproverSeekingClaimGroupQueryConstraint.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.exception.BeaconRuntimeException;

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
public class ApproverSeekingClaimGroupQueryConstraint extends ClaimGroupQueryConstraint
{

  /**
   * Include only claimGroups that can be approved by the approverUser, based on the current data in
   * the approver claim snapshot.
   */
  private Long approvableUserId;

  /**
   * Include only claimGroups that have been approved by the approverUser.
   */
  private Long approvedUserId;

  /**
   * get claimGroups with promotions of a specific promotion type. This is a workaround alternative
   * to setting result type to a specific Claim subclass to avoid a hibernate bug where native sql
   * can't be used in subclass queries.
   */
  private PromotionType claimGroupPromotionType;

  /**
   * If true, only return expired (open past approval end date). If false, only return non-expired .
   * If null return both. if true, forces open to true.
   */
  private Boolean expired;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint#buildCriteria()
   */
  public Criteria buildCriteria()
  {
    if ( BooleanUtils.isTrue( expired ) )
    {
      // expiring a claim will close the claim, and the claim.is_open will get updated to false.
      // so fetch only closed claims whose status is expired
      setOpen( Boolean.FALSE );
    }

    Criteria criteria = super.buildCriteria();

    if ( expired == null || BooleanUtils.isTrue( expired ) )
    {
      // When included expired, must return entries where the specified user approved.
      if ( approvedUserId == null )
      {
        throw new BeaconRuntimeException( "approvedUserId must be set when expired entries are included" );
      }
    }

    if ( expired != null )
    {
      if ( BooleanUtils.isTrue( expired ) )
      {
        // To fetch normal expired approvals along with the approver expires the claim - bug #54792
        Criterion approvalEndDateCriteria = Restrictions
            .sqlRestriction( "{alias}.promotion_id in ( " + "select promotion_id from promotion where trunc(SYSDATE) > trunc(NVL(approval_end_date, SYSDATE)) ) " );

        Criterion approverExpiredCriteria = Restrictions.sqlRestriction( "{alias}.claim_group_id in ( " + " select c.claim_group_id from claim c where c.claim_id in( "
            + " select ci.claim_id from claim_item ci where approval_status_type = 'expired') )" );

        LogicalExpression orExp = Restrictions.or( approvalEndDateCriteria, approverExpiredCriteria );
        criteria.add( orExp );

      }
      else
      {
        // Non-expired
        Criterion approvalNonExpiredCriteria = Restrictions
            .sqlRestriction( "{alias}.promotion_id in ( " + "select promotion_id from promotion " + "where trunc(SYSDATE) <= trunc(NVL(approval_end_date, SYSDATE)) ) " );

        Criterion approverNonExpiredCriteria = Restrictions.sqlRestriction( "{alias}.claim_group_id in ( " + " select c.claim_group_id from claim c where c.claim_id in( "
            + " select ci.claim_id from claim_item ci where approval_status_type != 'expired') )" );

        LogicalExpression orExp = Restrictions.or( approvalNonExpiredCriteria, approverNonExpiredCriteria );
        criteria.add( orExp );
      }
    }

    if ( approvableUserId != null )
    {
      // Include the claims for approval stored by snapshot and thos not saved by snapshot -
      // specific approvers
      Criterion snapshotCriteria = Restrictions.sqlRestriction( "{alias}.claim_group_id in (select claim_group_id from claim_approver_snapshot where approver_user_id=?) ",
                                                                approvableUserId,
                                                                StandardBasicTypes.LONG );
      Criterion specificApproversCriteria = Restrictions
          .sqlRestriction( "{alias}.promotion_id in (select promotion_id from promo_approval_participant" + " where user_id=?" + " AND PROMO_PARTICIPANT_TYPE='APPROVER')",
                           approvableUserId,
                           StandardBasicTypes.LONG );
      Disjunction either = Restrictions.disjunction();
      either.add( snapshotCriteria );
      either.add( specificApproversCriteria );
      criteria.add( either );

    }

    if ( approvedUserId != null )
    {

      criteria.add( Restrictions.sqlRestriction( "{alias}.claim_group_id in ( " + "select claim_group_id from claim_item_approver where approver_user_id in ?) ",
                                                 approvedUserId,
                                                 StandardBasicTypes.LONG ) );
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
  public Long getApprovableUserId()
  {
    return approvableUserId;
  }

  /**
   * @param approvableUserId value for approvableUserId property
   */
  public void setApprovableUserId( Long approvableUserId )
  {
    this.approvableUserId = approvableUserId;
  }

  /**
   * @return value of approvedUserId property
   */
  public Long getApprovedUserId()
  {
    return approvedUserId;
  }

  /**
   * @param approvedUserId value for approvedUserId property
   */
  public void setApprovedUserId( Long approvedUserId )
  {
    this.approvedUserId = approvedUserId;
  }

  public Boolean getExpired()
  {
    return expired;
  }

  public void setExpired( Boolean expired )
  {
    this.expired = expired;
  }

}

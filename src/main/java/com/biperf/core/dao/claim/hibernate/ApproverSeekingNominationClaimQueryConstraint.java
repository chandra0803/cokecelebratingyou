/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/Attic/ApproverSeekingNominationClaimQueryConstraint.java,v $
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
public class ApproverSeekingNominationClaimQueryConstraint extends ClaimQueryConstraint
{

  /**
   * Include only claims that can be approved by the approverUser, based on the current data in the
   * approver claim snapshot.
   */
  private Long userId;

  private String toDate;

  private String datePattern;

  /**
   * get claims with promotions of a specific promotion type. This is a workaround alternative to
   * setting result type to a specific Claim subclass to avoid a hibernate bug where native sql
   * can't be used in subclass queries.
   */
  private PromotionType claimPromotionType;

  /**
   * If true, only return expired (open past approval end date). If false, only return non-expired
   * . If null return both. if true, forces open to true.
   */
  private Boolean expired;

  private String approvalStatusType;

  /**
   * If false, only return claims since approval start date.  If true, return claims since ever.
   * If null, same as false.
   */
  private Boolean nonRestricted;

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

    if ( approvalRound != null )
    {
      criteria.add( Restrictions.eq( "claim.approvalRound", approvalRound ) );
    }

    if ( nonRestricted == null || BooleanUtils.isFalse( nonRestricted ) )
    {
      criteria.add( Restrictions.sqlRestriction( "{alias}.promotion_id in ( " + "select promotion_id from promotion where  to_date('" + toDate + "', '" + datePattern
          + "') >= trunc(NVL( approval_start_date,  to_date('" + toDate + "', '" + datePattern + "'))) ) " ) );
    }

    if ( BooleanUtils.isTrue( getOpen() ) )
    {
      // pending
      Criterion approvalNonExpiredCriteria = Restrictions.sqlRestriction( "{alias}.promotion_id in ( " + "select promotion_id from promotion " + "where  to_date('" + toDate + "', '" + datePattern
          + "') <= trunc(NVL(approval_end_date,  to_date('" + toDate + "', '" + datePattern + "'))) ) " );

      Criterion approverNonExpiredCriteria = Restrictions
          .sqlRestriction( "{alias}.claim_id in ( " + " select claim_id from claim_item where approval_status_type = 'pend' and claim_id in ( " + claimIds + " ) )" );

      LogicalExpression andExp = Restrictions.and( approvalNonExpiredCriteria, approverNonExpiredCriteria );
      criteria.add( andExp );

      // Include the claims for approval stored by snapshot and thos not saved by snapshot
      if ( UserManager.getUser().getAuthorities() != null && !UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
      {
        Criterion snapshotCriteria = Restrictions.sqlRestriction(
                                                                  "{alias}.claim_id in (select cas.claim_id from claim_approver_snapshot cas,claim c where cas.claim_id = c.claim_id and c.claim_group_id is null and approver_user_id=?) ",
                                                                  userId,
                                                                  StandardBasicTypes.LONG );
        criteria.add( snapshotCriteria );
      }
      else
      {
        Criterion snapshotCriteria = Restrictions
            .sqlRestriction( "{alias}.claim_id in (select cas.claim_id from claim_approver_snapshot cas,claim c where cas.claim_id = c.claim_id and c.claim_group_id is null ) " );
        criteria.add( snapshotCriteria );
      }
    }

    if ( claimPromotionType != null )
    {
      criteria.createAlias( "claim.promotion", "promotion" ).add( Restrictions.eq( "promotion.promotionType", claimPromotionType ) );
    }

    return criteria;
  }

  /**
   * @return value of claimPromotionType property
   */
  public PromotionType getClaimPromotionType()
  {
    return claimPromotionType;
  }

  /**
   * @param claimPromotionType value for claimPromotionType property
   */
  public void setClaimPromotionType( PromotionType claimPromotionType )
  {
    this.claimPromotionType = claimPromotionType;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  /**
   * 
   * @return expired property
   */
  public Boolean getExpired()
  {
    return expired;
  }

  /**
   * Set this property
   * @param expired
   */
  public void setExpired( Boolean expired )
  {
    this.expired = expired;
  }

  /**
   * 
   * @return restricted property
   */
  public Boolean getNonRestricted()
  {
    return nonRestricted;
  }

  /**
   * Set this property
   * @param restricted
   */
  public void setNonRestricted( Boolean restricted )
  {
    this.nonRestricted = restricted;
  }

  public String getToDate()
  {
    return toDate;
  }

  public void setToDate( String toDate )
  {
    this.toDate = toDate;
  }

  public String getDatePattern()
  {
    return datePattern;
  }

  public void setDatePattern( String datePattern )
  {
    this.datePattern = datePattern;
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

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/claim/hibernate/ClaimQueryConstraint.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HibernateSessionManager;

/**
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
public class ClaimQueryConstraint extends BaseQueryConstraint
{

  /**
   * Constrain by submitter pax id.
   */
  private Long submitterId;

  /**
   * Identifies the node_id that the claim is being routed to.
   */
  private Long[] submitterNodeIds;

  /**
   * constrain by claims submitted by proxy
   */
  private Long proxyUserId;

  /**
   * constrain by claims against the given promotion
   */
  private Long[] includedPromotionIds;

  /**
   * only include claims whose promotions don't use the specified excludedPromotionApproverTypes.
   */
  private ApproverType[] excludedPromotionApproverTypes;

  /**
   * only include claims whose promotions use the specified excludedPromotionApproverTypes.
   */
  private ApproverType[] includedPromotionApproverTypes;

  /**
   * include only claims submitted on or after the beginning of day of startDate.
   */
  private Date startDate;

  /**
   * include only claims submitted on or before end of day of startDate.
   */
  private Date endDate;

  /**
   * true to include claims that are open; closed if false.
   */
  private Boolean open;

  /**
   * true to include claims which have one or more posted activity; if false, claims with no 
   * unposted activities.
   */
  private Boolean anyActivitityUnposted;

  private String sortedOn;

  private String sortedBy;

  private int rowNumStart;

  private int rowNumEnd;

  /**
   * Return the result object type - Should be overridden by subclasses. Overridden from
   * 
   * @see com.biperf.core.dao.QueryConstraint#getResultClass()
   * @return Class
   */
  public Class getResultClass()
  {
    return Claim.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "claim" );

    // Constrain by promotion
    if ( includedPromotionIds != null )
    {
      criteria.add( Restrictions.in( "claim.promotion.id", includedPromotionIds ) );
    }

    // Constrain by promotion approver type
    if ( excludedPromotionApproverTypes != null )
    {
      criteria.createAlias( "claim.promotion", "promotion" ).add( Restrictions.not( Restrictions.in( "promotion.approverType", excludedPromotionApproverTypes ) ) );
    }

    // Constrain by promotion approver type
    if ( includedPromotionApproverTypes != null )
    {
      criteria.createAlias( "claim.promotion", "promotion" ).add( Restrictions.in( "promotion.approverType", includedPromotionApproverTypes ) );
    }

    // Constrain by submitter id
    if ( submitterId != null )
    {
      criteria.add( Restrictions.eq( "claim.submitter.id", submitterId ) );
    }

    // Constrain by submitter NodeIds
    if ( submitterNodeIds != null )
    {
      criteria.add( Restrictions.in( "claim.node.id", submitterNodeIds ) );
    }

    // Constrain by proxy participant id
    if ( proxyUserId != null )
    {
      criteria.add( Restrictions.eq( "claim.proxyUser.id", proxyUserId ) );
    }

    // Constrain by Date
    if ( startDate != null )
    {
      // shift date to beginning of day - FUTURE: if exact time check is needed for start and end,
      // could add a boolean param to method to allow choosing between exact time check and "day of"
      // check.
      Date startDateBeginningOfDay = DateUtils.toStartDate( startDate );
      criteria.add( Restrictions.ge( "claim.submissionDate", startDateBeginningOfDay ) );
    }

    if ( endDate != null )
    {
      // shift date to end of day
      Date endDateBeginningOfDay = DateUtils.toEndDate( endDate );
      criteria.add( Restrictions.le( "claim.submissionDate", endDateBeginningOfDay ) );
    }

    // Constrain by open/close
    if ( open != null )
    {
      criteria.add( Restrictions.eq( "claim.open", open ) );
    }

    // Constrain by anyActivitityUnposted
    if ( anyActivitityUnposted != null )
    {
      String postedCode = "1";
      if ( anyActivitityUnposted.booleanValue() )
      {
        postedCode = "0";
      }
      criteria.add( Restrictions.sqlRestriction( "" + "exists (select 1 from ACTIVITY activity " + "where {alias}.claim_id=activity.claim_id and activity.is_posted=" + postedCode + ")" ) );
    }

    // Constrain by sortedOn and sortedBy

    if ( sortedOn != null && sortedBy != null )
    {
      if ( "number".equals( sortedOn ) )
      {
        criteria.addOrder( !"asc".equals( sortedBy ) ? Order.desc( "claim.claimNumber" ) : Order.asc( "claim.claimNumber" ) );
      }
      else if ( "date".equals( sortedOn ) )
      {
        criteria.addOrder( !"asc".equals( sortedBy ) ? Order.desc( "claim.auditCreateInfo" ) : Order.asc( "claim.auditCreateInfo" ) );
      }
      else if ( "submitter".equals( sortedOn ) )
      {
        criteria.createAlias( "claim.submitter", "submitter" ).addOrder( !"asc".equals( sortedBy ) ? Order.desc( "submitter.lastName" ) : Order.asc( "submitter.lastName" ) )
            .addOrder( !sortedBy.equals( "asc" ) ? Order.desc( "submitter.firstName" ) : Order.asc( "submitter.firstName" ) );
      }
    }

    if ( rowNumStart >= 0 && rowNumEnd > 0 )
    {
      criteria.setFirstResult( rowNumStart );
      criteria.setMaxResults( rowNumEnd );
    }

    return criteria;
  }

  /**
   * @return value of endDate property
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * @param endDate value for endDate property
   */
  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  /**
   * @return value of open property
   */
  public Boolean getOpen()
  {
    return open;
  }

  /**
   * @param open value for open property
   */
  public void setOpen( Boolean open )
  {
    this.open = open;
  }

  /**
   * @return value of startDate property
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * @param startDate value for startDate property
   */
  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  /**
   * @return value of includedPromotionIds property
   */
  public Long[] getIncludedPromotionIds()
  {
    return includedPromotionIds;
  }

  /**
   * @param includedPromotionIds value for includedPromotionIds property
   */
  public void setIncludedPromotionIds( Long[] includedPromotionIds )
  {
    this.includedPromotionIds = includedPromotionIds;
  }

  /**
   * @return value of submitterId property
   */
  public Long getSubmitterId()
  {
    return submitterId;
  }

  /**
   * @param submitterId value for submitterId property
   */
  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  public Long getProxyUserId()
  {
    return proxyUserId;
  }

  public void setProxyUserId( Long proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  /**
   * @return value of excludedPromotionApproverTypes property
   */
  public ApproverType[] getExcludedPromotionApproverTypes()
  {
    return excludedPromotionApproverTypes;
  }

  /**
   * @param excludedPromotionApproverTypes value for excludedPromotionApproverTypes property
   */
  public void setExcludedPromotionApproverTypes( ApproverType[] excludedPromotionApproverTypes )
  {
    this.excludedPromotionApproverTypes = excludedPromotionApproverTypes;
  }

  /**
   * @return value of includedPromotionApproverTypes property
   */
  public ApproverType[] getIncludedPromotionApproverTypes()
  {
    return includedPromotionApproverTypes;
  }

  /**
   * @param includedPromotionApproverTypes value for includedPromotionApproverTypes property
   */
  public void setIncludedPromotionApproverTypes( ApproverType[] includedPromotionApproverTypes )
  {
    this.includedPromotionApproverTypes = includedPromotionApproverTypes;
  }

  public Boolean getAnyActivitityUnposted()
  {
    return anyActivitityUnposted;
  }

  public void setAnyActivitityUnposted( Boolean anyActivitityUnposted )
  {
    this.anyActivitityUnposted = anyActivitityUnposted;
  }

  public Long[] getSubmitterNodeIds()
  {
    return submitterNodeIds;
  }

  public void setSubmitterNodeIds( Long[] submitterNodeIds )
  {
    this.submitterNodeIds = submitterNodeIds;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public int getRowNumStart()
  {
    return rowNumStart;
  }

  public void setRowNumStart( int rowNumStart )
  {
    this.rowNumStart = rowNumStart;
  }

  public int getRowNumEnd()
  {
    return rowNumEnd;
  }

  public void setRowNumEnd( int rowNumEnd )
  {
    this.rowNumEnd = rowNumEnd;
  }

}

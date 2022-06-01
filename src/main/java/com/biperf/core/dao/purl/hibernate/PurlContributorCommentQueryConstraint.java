
package com.biperf.core.dao.purl.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.enums.PurlContributorCommentStatus;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.utils.HibernateSessionManager;

public class PurlContributorCommentQueryConstraint extends BaseQueryConstraint
{
  private Long purlRecipientId;

  private Long purlContributorId;

  private PurlContributorCommentStatus[] purlContributorCommentStatusTypesIncluded;

  private PurlContributorMediaStatus[] purlContributorImageStatusTypesIncluded;

  private PurlContributorMediaStatus[] purlContributorVideoStatusTypesIncluded;

  private PurlMediaState[] purlMediaStateIncluded;

  private boolean orderDescending = false;

  private int rowNumStart;

  private int rowNumEnd;

  private Long claimId;

  public Class getResultClass()
  {
    return PurlContributorComment.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "purlContributorComment" );

    // PurlRecipient criterion
    if ( purlRecipientId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "purlContributorComment.purlContributor", "purlContributor" );
      createAliasIfNotAlreadyCreated( criteria, "purlContributor.purlRecipient", "purlRecipient" );
      criteria.add( Restrictions.eq( "purlRecipient.id", purlRecipientId ) );

      /*
       * if ( claimId != null ) { createAliasIfNotAlreadyCreated( criteria, "purlRecipient.claim",
       * "claim" ); criteria.add( Restrictions.eq( "claim.id", claimId ) ); }
       */
    }

    // PurlContributor criterion
    if ( purlContributorId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "purlContributorComment.purlContributor", "purlContributor" );
      criteria.add( Restrictions.eq( "purlContributor.id", purlContributorId ) );
    }

    if ( purlContributorCommentStatusTypesIncluded != null && purlContributorCommentStatusTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "purlContributorComment.status", purlContributorCommentStatusTypesIncluded ) );
    }

    if ( purlContributorImageStatusTypesIncluded != null && purlContributorImageStatusTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "purlContributorComment.imageStatus", purlContributorImageStatusTypesIncluded ) );
    }

    if ( purlContributorVideoStatusTypesIncluded != null && purlContributorVideoStatusTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "purlContributorComment.videoStatus", purlContributorVideoStatusTypesIncluded ) );
    }

    if ( purlMediaStateIncluded != null && purlMediaStateIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "purlContributorComment.mediaState", purlMediaStateIncluded ) );
    }

    criteria.addOrder( orderDescending ? Order.desc( "purlContributorComment.id" ) : Order.asc( "purlContributorComment.id" ) );

    if ( rowNumStart >= 0 && rowNumEnd > 0 )
    {
      criteria.setFirstResult( rowNumStart );
      criteria.setMaxResults( rowNumEnd );
    }

    return criteria;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public Long getPurlContributorId()
  {
    return purlContributorId;
  }

  public void setPurlContributorId( Long purlContributorId )
  {
    this.purlContributorId = purlContributorId;
  }

  public PurlContributorCommentStatus[] getPurlContributorCommentStatusTypesIncluded()
  {
    return purlContributorCommentStatusTypesIncluded;
  }

  public void setPurlContributorCommentStatusTypesIncluded( PurlContributorCommentStatus[] purlContributorCommentStatusTypesIncluded )
  {
    this.purlContributorCommentStatusTypesIncluded = purlContributorCommentStatusTypesIncluded;
  }

  public PurlContributorMediaStatus[] getPurlContributorImageStatusTypesIncluded()
  {
    return purlContributorImageStatusTypesIncluded;
  }

  public void setPurlContributorImageStatusTypesIncluded( PurlContributorMediaStatus[] purlContributorImageStatusTypesIncluded )
  {
    this.purlContributorImageStatusTypesIncluded = purlContributorImageStatusTypesIncluded;
  }

  public PurlContributorMediaStatus[] getPurlContributorVideoStatusTypesIncluded()
  {
    return purlContributorVideoStatusTypesIncluded;
  }

  public void setPurlContributorVideoStatusTypesIncluded( PurlContributorMediaStatus[] purlContributorVideoStatusTypesIncluded )
  {
    this.purlContributorVideoStatusTypesIncluded = purlContributorVideoStatusTypesIncluded;
  }

  public PurlMediaState[] getPurlMediaStateIncluded()
  {
    return purlMediaStateIncluded;
  }

  public void setPurlMediaStateIncluded( PurlMediaState[] purlMediaStateIncluded )
  {
    this.purlMediaStateIncluded = purlMediaStateIncluded;
  }

  public boolean isOrderDescending()
  {
    return orderDescending;
  }

  public void setOrderDescending( boolean orderDescending )
  {
    this.orderDescending = orderDescending;
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

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

}

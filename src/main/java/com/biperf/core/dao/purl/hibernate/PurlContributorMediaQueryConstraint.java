
package com.biperf.core.dao.purl.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorMediaType;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.purl.PurlContributorMedia;
import com.biperf.core.utils.HibernateSessionManager;

public class PurlContributorMediaQueryConstraint extends BaseQueryConstraint
{

  private Long purlRecipientId;

  private Long purlContributorId;

  private PurlContributorMediaStatus[] purlContributorMediaStatusTypesIncluded;

  private PurlContributorMediaType[] purlContributorMediaTypeIncluded;

  private PurlMediaState[] purlMediaStateIncluded;

  public Class getResultClass()
  {
    return PurlContributorMedia.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "purlContributorMedia" );

    // PurlRecipient criterion
    if ( purlRecipientId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "purlContributorMedia.purlContributor", "purlContributor" );
      createAliasIfNotAlreadyCreated( criteria, "purlContributor.purlRecipient", "purlRecipient" );
      criteria.add( Restrictions.eq( "purlRecipient.id", purlRecipientId ) );
    }

    // PurlContributor criterion
    if ( purlContributorId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "purlContributorMedia.purlContributor", "purlContributor" );
      criteria.add( Restrictions.eq( "purlContributor.id", purlContributorId ) );
    }

    if ( purlContributorMediaStatusTypesIncluded != null && purlContributorMediaStatusTypesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "purlContributorMedia.status", purlContributorMediaStatusTypesIncluded ) );
    }

    if ( purlContributorMediaTypeIncluded != null && purlContributorMediaTypeIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "purlContributorMedia.type", purlContributorMediaTypeIncluded ) );
    }

    if ( purlMediaStateIncluded != null && purlMediaStateIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "purlContributorMedia.state", purlMediaStateIncluded ) );
    }

    criteria.addOrder( Order.asc( "purlContributorMedia.id" ) );

    return criteria;

  }

  public PurlContributorMediaStatus[] getPurlContributorMediaStatusTypesIncluded()
  {
    return purlContributorMediaStatusTypesIncluded;
  }

  public void setPurlContributorMediaStatusTypesIncluded( PurlContributorMediaStatus[] purlContributorMediaStatusTypesIncluded )
  {
    this.purlContributorMediaStatusTypesIncluded = purlContributorMediaStatusTypesIncluded;
  }

  public PurlContributorMediaType[] getPurlContributorMediaTypeIncluded()
  {
    return purlContributorMediaTypeIncluded;
  }

  public void setPurlContributorMediaTypeIncluded( PurlContributorMediaType[] purlContributorMediaTypeIncluded )
  {
    this.purlContributorMediaTypeIncluded = purlContributorMediaTypeIncluded;
  }

  public PurlMediaState[] getPurlMediaStateIncluded()
  {
    return purlMediaStateIncluded;
  }

  public void setPurlMediaStateIncluded( PurlMediaState[] purlMediaStateIncluded )
  {
    this.purlMediaStateIncluded = purlMediaStateIncluded;
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
}

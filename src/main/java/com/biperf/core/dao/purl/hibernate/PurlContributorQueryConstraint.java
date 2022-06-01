
package com.biperf.core.dao.purl.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.utils.HibernateSessionManager;

public class PurlContributorQueryConstraint extends BaseQueryConstraint
{
  private Long purlRecipientId;

  private Long contributorUserId;

  private Long promotionId;

  private Long invitedByPurlContributorId;

  private Long createdBy;
  
  private Boolean defaultInvitee;

  public Boolean getDefaultInvitee()
  {
    return defaultInvitee;
  }

  public void setDefaultInvitee( Boolean defaultInvitee )
  {
    this.defaultInvitee = defaultInvitee;
  }

  private PurlContributorState[] purlContributorStates;

  public Class getResultClass()
  {
    return PurlContributor.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "purlContributor" );
    // PurlRecipient criterion
    if ( purlRecipientId != null )
    {
      criteria.add( Restrictions.eq( "purlContributor.purlRecipient.id", purlRecipientId ) );
    }

    if ( contributorUserId != null )
    {
      criteria.add( Restrictions.eq( "purlContributor.user.id", contributorUserId ) );
    }

    if ( invitedByPurlContributorId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "purlContributor.invitedContributor", "invitedContributor" );
      criteria.add( Restrictions.conjunction().add( Restrictions.isNotNull( "purlContributor.invitedContributor" ) ).add( Restrictions.eq( "invitedContributor.id", invitedByPurlContributorId ) ) );
    }

    if ( promotionId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "purlContributor.purlRecipient", "purlRecipient" );
      createAliasIfNotAlreadyCreated( criteria, "purlRecipient.promotion", "promotion" );
      criteria.add( Restrictions.eq( "promotion.id", promotionId ) );
    }

    if ( purlContributorStates != null && purlContributorStates.length > 0 )
    {
      criteria.add( Restrictions.in( "purlContributor.state", purlContributorStates ) );
    }

    if ( createdBy != null && createdBy.longValue() > 0 )
    {
      criteria.add( Restrictions.sqlRestriction( "{alias}.CREATED_BY = " + createdBy + "and {alias}.USER_ID != " + createdBy ) );
    }

    if ( defaultInvitee != null )
    {
      criteria.add( Restrictions.eq( "purlContributor.defaultInvitee", defaultInvitee ) );
    }
    // Through this criteria we always expect to retrieve invited contributors
    criteria.add( Restrictions.eq( "purlContributor.sendLater", Boolean.FALSE ) );

    criteria.addOrder( Order.desc( "purlContributor.id" ) );

    return criteria;
  }

  public PurlContributorState[] getPurlContributorStates()
  {
    return purlContributorStates;
  }

  public void setPurlContributorStates( PurlContributorState[] purlContributorStates )
  {
    this.purlContributorStates = purlContributorStates;
  }

  public Long getContributorUserId()
  {
    return contributorUserId;
  }

  public void setContributorUserId( Long contributorUserId )
  {
    this.contributorUserId = contributorUserId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public Long getInvitedByPurlContributorId()
  {
    return invitedByPurlContributorId;
  }

  public void setInvitedByPurlContributorId( Long invitedByPurlContributorId )
  {
    this.invitedByPurlContributorId = invitedByPurlContributorId;
  }

  public Long getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( Long createdBy )
  {
    this.createdBy = createdBy;
  }

}

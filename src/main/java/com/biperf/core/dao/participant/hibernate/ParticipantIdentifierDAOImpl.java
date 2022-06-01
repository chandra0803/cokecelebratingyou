
package com.biperf.core.dao.participant.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.ParticipantIdentifierDAO;
import com.biperf.core.domain.participant.ParticipantIdentifier;

public class ParticipantIdentifierDAOImpl extends BaseDAO implements ParticipantIdentifierDAO
{
  @SuppressWarnings( "unchecked" )
  public List<ParticipantIdentifier> getAll()
  {
    Criteria criteria = getSession().createCriteria( ParticipantIdentifier.class );
    criteria.setFetchMode( "characteristic", FetchMode.JOIN );
    return criteria.list();
  }

  @Override
  public void save( ParticipantIdentifier pi )
  {
    getSession().saveOrUpdate( pi );
  }

  @Override
  public ParticipantIdentifier getById( Long id )
  {
    Criteria criteria = getSession().createCriteria( ParticipantIdentifier.class );
    criteria.add( Restrictions.eq( "id", id ) );
    return (ParticipantIdentifier)criteria.uniqueResult();
  }

  @Override
  public List<ParticipantIdentifier> getSelected()
  {
    return getBySelectionCriteria( true );
  }

  @Override
  public List<ParticipantIdentifier> getUnSelected()
  {
    return getBySelectionCriteria( false );
  }

  @SuppressWarnings( "unchecked" )
  private List<ParticipantIdentifier> getBySelectionCriteria( boolean selected )
  {
    Criteria criteria = getSession().createCriteria( ParticipantIdentifier.class );
    criteria.add( Restrictions.eq( "selected", selected ) );
    criteria.setFetchMode( "characteristic", FetchMode.JOIN );
    return criteria.list();
  }
}


package com.biperf.core.dao.participant.hibernate;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.ParticipantGroupDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantGroup;
import com.biperf.core.domain.participant.ParticipantGroupDetails;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.participant.ParticipantGroupList;
import com.biperf.core.value.participant.ParticipantGroupList.Group;

public class ParticipantGroupDAOImpl extends BaseDAO implements ParticipantGroupDAO
{

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ParticipantGroup> findGroupsByUserIdAndStartsWith( Long userId, String startsWith )
  {
    Criteria criteria = buildStartsWithCriteria( userId, startsWith );
    criteria.addOrder( Order.asc( "groupName" ).ignoreCase() );
    return criteria.list();
  }

  @Override
  public int findGroupCountByUserIdAndStartsWith( Long userId, String startsWith )
  {
    Criteria criteria = buildStartsWithCriteria( userId, startsWith );
    criteria.setProjection( Projections.rowCount() );
    return ( (Long)criteria.uniqueResult() ).intValue();
  }

  protected Criteria buildStartsWithCriteria( Long userId, String startsWith )
  {
    Criteria criteria = getSession().createCriteria( ParticipantGroup.class, "paxGroup" );
    criteria.add( Restrictions.eq( "paxGroup.groupCreatedBy.id", userId ) );
    criteria.add( Restrictions.ilike( "paxGroup.groupName", scrubInput( startsWith ), MatchMode.START ) );
    return criteria;
  }

  @Override
  public void delete( Long participantGroupId )
  {
    ParticipantGroup pg = find( participantGroupId );
    getSession().delete( pg );
  }

  @Override
  public ParticipantGroup find( Long id )
  {
    return (ParticipantGroup)getSession().get( ParticipantGroup.class, id );
  }

  @Override
  public ParticipantGroup saveOrUpdate( ParticipantGroup group )
  {
    return (ParticipantGroup)HibernateUtil.saveOrUpdateOrShallowMerge( group );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public ParticipantGroupList findGroupsByPaxId( Long userId )
  {
    ParticipantGroupList list = new ParticipantGroupList();
    StringBuilder queryString = new StringBuilder( "from com.biperf.core.domain.participant.ParticipantGroup pg    where pg.groupCreatedBy.id = :paxId " );

    Query query = getSession().createQuery( queryString.toString() );
    query.setParameter( "paxId", userId );

    List<ParticipantGroup> participantGroups = query.list();

    if ( CollectionUtils.isNotEmpty( participantGroups ) )
    {
      java.util.Collections.sort( participantGroups, ( ParticipantGroup pg1, ParticipantGroup pg2 ) -> pg1.getId().compareTo( pg2.getId() ) );

      for ( ParticipantGroup participantGroup : participantGroups )
      {
        Group group = new Group();

        group.setId( participantGroup.getId() );
        group.setName( participantGroup.getGroupName() );
        if ( CollectionUtils.isNotEmpty( participantGroup.getGroupDetails() ) )
        {
          for ( ParticipantGroupDetails participantGroupDetails : participantGroup.getGroupDetails() )
          {
            Participant participant = participantGroupDetails.getParticipant();
            if ( participant.isActive() )
            {
              group.addGroupUserId( participantGroupDetails.getParticipant().getId() );
            }
          }
          if ( CollectionUtils.isNotEmpty( group.getGroupUserIds() ) )
          {
            group.setPaxCount( new Long( group.getGroupUserIds().size() ) );
          }
          else
          {
            group.setPaxCount( new Long( 0 ) );
          }
        }
        list.addGroup( group );
      }
    }

    return list;
  }

  private String scrubInput( String criteria )
  {
    if ( null != criteria && criteria.length() > 0 )
    {
      return criteria.replaceAll( "%", "" );
    }
    return criteria;
  }
}

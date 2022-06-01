
package com.biperf.core.dao.leaderboard.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.leaderboard.LeaderBoardDAO;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.leaderboard.LeaderBoardPaxActivity;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.util.StringUtils;

public class LeaderBoardDAOImpl extends BaseDAO implements LeaderBoardDAO
{

  // LeaderBoard DAO Impl Methods Starts Here
  private static final int MAX_LEADERBOARD_PARTICIPANTS = 23;

  /**
   * if leaderboard name is available then retunrs true else false
   * 
   * @param leaderBoardName, currentLeaderBoardId
   * @return boolean
   */
  public boolean isLeaderBoardNameUnique( String leaderBoardName, Long currentLeaderBoardId )
  {
    boolean isUnique = true;

    if ( currentLeaderBoardId == null )
    {
      currentLeaderBoardId = new Long( 0 );
    }
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.leaderboard.LeaderBoardByNameCount" );
    query.setParameter( "leaderBoardName", leaderBoardName.toLowerCase() );
    query.setParameter( "leaderBoardId", currentLeaderBoardId );

    Integer count = (Integer)query.uniqueResult();
    isUnique = count.intValue() == 0;

    return isUnique;
  }

  /**
   * Gets LeaderBoard using id. 
   * 
   * Overridden from
   * @see com.biperf.core.dao.leaderboard.LeaderBoardDAO#getLeaderBoardById(java.lang.Long)
   * @param id
   * @return LeaderBoard
   */
  public LeaderBoard getLeaderBoardById( Long id )
  {
    return (LeaderBoard)getSession().get( LeaderBoard.class, id );
  }

  /**
   * Gets list of LeaderBoard using userId 
   * 
   * @see com.biperf.core.dao.leaderboard.LeaderBoardDAO#getLeaderBoardById(java.lang.Long)
   * @param UserId
   * @return List
   */
  public List<LeaderBoard> getLeaderBoardsByOwnerUserIdAndStatus( Long userId, String status )
  {
    Criteria criteria = getSession().createCriteria( LeaderBoard.class, "lb" );

    // Live
    if ( status.equalsIgnoreCase( LeaderBoard.LIVE ) )
    {
      criteria.add( Restrictions.eq( "lb.status", LeaderBoard.LEADERBOARD_COMPLETE ) );
      criteria.add( Restrictions.lt( "lb.startDate", new Date() ) );
      Criterion endDateNull = Restrictions.isNull( "lb.endDate" );
      Criterion endDateGreater = Restrictions.ge( "lb.endDate", DateUtils.getCurrentDateTrimmed() );
      criteria.add( Restrictions.or( endDateNull, endDateGreater ) );
      criteria.add( Restrictions.gt( "lb.displayEndDate", new Date() ) );
      criteria.add( Restrictions.eq( "lb.user.id", userId ) );
    }
    // completed but not live or active
    else if ( status.equalsIgnoreCase( LeaderBoard.COMPLETED ) )
    {
      criteria.add( Restrictions.eq( "lb.status", LeaderBoard.LEADERBOARD_COMPLETE ) );
      criteria.add( Restrictions.gt( "lb.startDate", new Date() ) );
      criteria.add( Restrictions.eq( "lb.user.id", userId ) );
    }
    // expired
    else if ( status.equalsIgnoreCase( LeaderBoard.EXPIRED ) )
    {
      criteria.add( Restrictions.eq( "lb.status", LeaderBoard.LEADERBOARD_COMPLETE ) );
      criteria.add( Restrictions.lt( "lb.endDate", new Date() ) );
      criteria.add( Restrictions.eq( "lb.user.id", userId ) );
    }
    else
    {
      criteria.add( Restrictions.eq( "lb.status", status ) );
      criteria.add( Restrictions.eq( "lb.user.id", userId ) );
    }

    return criteria.list();

  }

  /**
   * Gets list of LeaderBoard along with paxs
   * 
   * @param currentUserId and requireDetail(tile page=false, details page=true)
   * @return List of leaderboards with paxs
   */
  public List<LeaderBoard> getLeaderBoardsForTile( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.leaderboard.LeaderBoardsForTile" );
    query.setParameter( "userId", userId );
    return query.list();
  }

  /**
   * @param userId
   * @param requireDetail
   * @return list of leaderboards
   */
  public List<LeaderBoard> getLeaderBoardsForDetailUsingNameId( Long userId, String nameId )
  {
    Session session = getSession();
    Query query = null;
    if ( LeaderBoard.LEADERBOARD_PENDING.equals( nameId ) )
    {
      query = session.getNamedQuery( "com.biperf.core.domain.leaderboard.LeaderBoardsForPendingSet" );
    }
    else if ( LeaderBoard.LEADERBOARD_ARCHIVED.equals( nameId ) )
    {
      query = session.getNamedQuery( "com.biperf.core.domain.leaderboard.LeaderBoardsForArchivedSet" );
    }
    else
    {
      query = session.getNamedQuery( "com.biperf.core.domain.leaderboard.LeaderBoardsForActiveSet" );
    }
    query.setParameter( "userId", userId );
    return query.list();
  }

  /**
   * Gets list of LeaderBoard using  Status
   * 
   * @see com.biperf.core.dao.LeaderBoardDAO#getLeaderBoardStatus()
   * @return List
   */
  public List<LeaderBoard> getLeaderBoardByStatus( String status )
  {
    if ( StringUtils.isEmpty( status ) )
    {
      return null;
    }

    Criteria criteria = getSession().createCriteria( LeaderBoard.class, "lb" );

    // Live
    if ( status.equalsIgnoreCase( LeaderBoard.LIVE ) )
    {
      criteria.add( Restrictions.eq( "lb.status", LeaderBoard.LEADERBOARD_COMPLETE ) );
      criteria.add( Restrictions.le( "lb.startDate", DateUtils.getCurrentDateTrimmed() ) );
      Criterion endDateNull = Restrictions.isNull( "lb.endDate" );
      Criterion endDateGreater = Restrictions.ge( "lb.endDate", DateUtils.getCurrentDateTrimmed() );
      criteria.add( Restrictions.or( endDateNull, endDateGreater ) );
      // criteria.add( Restrictions.gt( "lb.displayEndDate", new Date() ) );
    }
    // completed but not live or active
    else if ( status.equalsIgnoreCase( LeaderBoard.COMPLETED ) )
    {
      criteria.add( Restrictions.eq( "lb.status", LeaderBoard.LEADERBOARD_COMPLETE ) );
      criteria.add( Restrictions.gt( "lb.startDate", DateUtils.getCurrentDateTrimmed() ) );
    }
    // expired
    else if ( status.equalsIgnoreCase( LeaderBoard.EXPIRED ) )
    {
      Disjunction or = Restrictions.disjunction();
      or.add( Restrictions.eq( "lb.status", LeaderBoard.LEADERBOARD_EXPIRED ) );
      or.add( Restrictions.lt( "lb.endDate", DateUtils.getCurrentDateTrimmed() ) );
      criteria.add( or );
    }
    else if ( status.equalsIgnoreCase( LeaderBoard.ALL_NOT_EXPIRED ) )
    {
      criteria.add( Restrictions.ne( "lb.status", LeaderBoard.LEADERBOARD_EXPIRED ) );
    }
    else
    {
      criteria.add( Restrictions.eq( "lb.status", status ) );
    }

    return criteria.list();
  }

  /**
   * Gets list of LeaderBoards that are not expired
   * Leaderboard's which are complete and the end date is greater or equal to current date
   * @see com.biperf.core.dao.LeaderBoardDAO#getLeaderBoardStatus()
   * @return List
   */
  public List<LeaderBoard> getUnexpiredLeaderBoards()
  {
    Criteria criteria = getSession().createCriteria( LeaderBoard.class, "lb" );
    criteria.add( Restrictions.eq( "lb.status", LeaderBoard.LEADERBOARD_COMPLETE ) );
    criteria.add( Restrictions.or( Restrictions.isNull( "lb.endDate" ), Restrictions.ge( "lb.endDate", DateUtils.getCurrentDateTrimmed() ) ) );
    return criteria.list();
  }

  /**
   * Saves  the leaderboard record in the database. Overridden from
   * 
   * @see com.biperf.core.dao.leaderboard.LeaderBoardDAO#saveLeaderBoard(com.biperf.core.domain.leaderboard.LeaderBoard)
   * @param leaderboard
   * @return leaderboard
   */
  public LeaderBoard saveLeaderBoard( LeaderBoard leaderBoard )
  {
    getSession().saveOrUpdate( leaderBoard );
    return leaderBoard;

  }

  // LeaderBoard DAO Impl Methods Ends Here

  // LeaderBoardParticipant DAO Impl Methods Starts Here
  /**
   * Gets LeaderBoardParticipant by the leaderBoardParticipant Id.
   * 
   * @see com.biperf.core.dao.leaderboard.LeaderBoardDAO#getLeaderBoardParticipantById(Long id)
   * @param id
   * @return LeaderBoard
   */
  public LeaderBoardParticipant getLeaderBoardParticipantById( Long id )
  {
    return (LeaderBoardParticipant)getSession().get( LeaderBoardParticipant.class, id );
  }

  /**
   * Gets list of LeaderBoardParticipant by the leaderBoard Id.
   * 
   * @see com.biperf.core.dao.leaderboard.LeaderBoardDAO#getLeaderBoardParticipantById(Long id)
   * @param id
   * @return LeaderBoardParticipant
   */
  public List<LeaderBoardParticipant> getLeaderBoardParticipantsByLeaderBoardId( Long leaderBoardId, String paxStatus )
  {
    Criteria criteria = getSession().createCriteria( LeaderBoardParticipant.class );
    criteria.add( Restrictions.eq( "leaderboard.id", leaderBoardId ) );
    if ( paxStatus.equals( LeaderBoardParticipant.ACTIVE_LB_PAX ) )
    {
      criteria.add( Restrictions.eq( "active", true ) );
    }
    if ( paxStatus.equals( LeaderBoardParticipant.IN_ACTIVE_LB_PAX ) )
    {
      criteria.add( Restrictions.eq( "active", false ) );
    }
    return criteria.list();
  }

  /**
   * Gets list of LeaderBoardParticipant 
   * 
   * @param leaderBoard
   * @return LeaderBoardParticipant
   */
  public List<LeaderBoardParticipant> getLeaderBoardParticipantsByLeaderBoardId( Long leaderBoardId )
  {
    Criteria criteria = getSession().createCriteria( LeaderBoardParticipant.class );
    criteria.add( Restrictions.eq( "leaderboard.id", leaderBoardId ) );
    criteria.add( Restrictions.eq( "active", true ) );
    criteria.setFirstResult( 0 );
    criteria.setMaxResults( MAX_LEADERBOARD_PARTICIPANTS );
    criteria.addOrder( Order.asc( "participantRank" ) );
    return criteria.list();
  }

  /**
   * {@inheritDoc}
   */
  public LeaderBoardParticipant getLeaderBoardParticipantByParticipantIdAndLeaderBoardId( Long participantId, Long leaderBoardId )
  {
    Criteria criteria = getSession().createCriteria( LeaderBoardParticipant.class );
    criteria.add( Restrictions.eq( "user.id", participantId ) );
    criteria.add( Restrictions.eq( "leaderboard.id", leaderBoardId ) );
    criteria.add( Restrictions.eq( "active", true ) );
    LeaderBoardParticipant leaderBoardParticipant = (LeaderBoardParticipant)criteria.uniqueResult();
    return leaderBoardParticipant;
  }

  /**
   * Save the LeaderBoardParticipant.
   * 
   * @see com.biperf.core.dao.leaderboard.LeaderBoardDAO#saveLeaderBoardParticipant(LeaderBoardParticipant leaderBoardParticipant)
   * @param leaderBoardParticipant
   * @return LeaderBoardParticipant
   */
  public LeaderBoardParticipant saveLeaderBoardParticipant( LeaderBoardParticipant leaderBoardParticipant )
  {
    return (LeaderBoardParticipant)HibernateUtil.saveOrUpdateOrShallowMerge( leaderBoardParticipant );
  }

  /**
   * Save the LeaderBoardParticipant list.
   * 
   * @param leaderBoardParticipant
   * @return LeaderBoardParticipant
   */
  public void saveLeaderBoardParticipantsList( List<LeaderBoardParticipant> list )
  {
    Session session = getSession();
    for ( LeaderBoardParticipant lbp : list )
    {
      session.saveOrUpdate( lbp );
    }
    session.flush();
  }

  // LeaderBoardParticipant DAO Impl Methods Ends Here

  // LeaderBoardPaxActivity DAO Impl Methods Starts Here
  /**
   * Get the LeaderBoardPaxActivity by the Id.
   * 
   * @see com.biperf.core.dao.leaderboard.LeaderBoardDAO#getLeaderBoardPaxActivityById(Long id)
   * @param id
   * @return LeaderBoardPaxActivity
   */
  public LeaderBoardPaxActivity getLeaderBoardPaxActivityById( Long id )
  {
    return (LeaderBoardPaxActivity)getSession().get( LeaderBoardPaxActivity.class, id );
  }

  /**
   * Get the LeaderBoardParticipant by UserId.
   * 
   * @see com.biperf.core.dao.leaderboard.LeaderBoardParticipantDAO#getLeaderBoardParticipantByUserId(Long UserId)
   * @param userid
   * @return List
   */
  public List<LeaderBoardPaxActivity> getLeaderBoardPaxActivityByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.leaderboard.getLeaderBoardPaxActivityByUserId" );
    query.setParameter( "userId", userId );
    return query.list();

  }

  /**
   * Gets list of LeaderBoardParticipantActivity by leaderBoardId.
   * 
   * @see com.biperf.core.dao.leaderboard.LeaderBoardParticipantDAO#getLeaderBoardParticipantByUserId(Long UserId)
   * @param userid
   * @return List
   */
  public List<LeaderBoardPaxActivity> getLeaderBoardPaxActivityByLeaderBoardId( Long leaderBoardId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.leaderboard.getLeaderBoardPaxActivityByLeaderBoardId" );
    query.setParameter( "leaderBoardId", leaderBoardId );
    return query.list();
  }

  /**
   * Save the LeaderBoardPaxActivity.
   * 
   * @see com.biperf.core.dao.leaderboard.LeaderBoardDAO#saveLeaderBoardPaxActivity(LeaderBoardPaxActivity leaderBoardPaxActivity)
   * @param leaderBoardPaxActivity
   * @return LeaderBoardPaxActivity
   */
  public LeaderBoardPaxActivity saveLeaderBoardPaxActivity( LeaderBoardPaxActivity leaderBoardPaxActivity )
  {
    Session session = getSession();
    try
    {
      session.save( leaderBoardPaxActivity );
    }
    catch( NonUniqueObjectException e )
    {
      leaderBoardPaxActivity = (LeaderBoardPaxActivity)session.save( leaderBoardPaxActivity );
    }
    // Do a flush to force create of a history record
    session.flush();

    return leaderBoardPaxActivity;
  }

  // LeaderBoardPaxActivity DAO Impl Methods Ends Here

  public Integer isUserHasLiveLeaderBoard( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.leaderboard.isUserHasLiveLeaderboard" );
    query.setParameter( "userId", userId );
    return (Integer)query.uniqueResult();
  }

}

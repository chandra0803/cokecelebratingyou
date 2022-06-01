/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/throwdown/hibernate/SmackTalkDAOImpl.java,v $
 */

package com.biperf.core.dao.throwdown.hibernate;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.throwdown.SmackTalkDAO;
import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.domain.promotion.SmackTalkLike;
import com.biperf.core.utils.HibernateSessionManager;

public class SmackTalkDAOImpl extends BaseDAO implements SmackTalkDAO
{
  @SuppressWarnings( "unused" )
  private JdbcTemplate jdbcTemplate;

  // Use this value to show profile on the pop at once.
  private static final int pageSize = 5;

  /**
   * Save SmackTalk Comment
   * @param saveSmackTalkComment    
   * @return SmackTalkComment
   */
  public SmackTalkComment saveSmackTalkComment( SmackTalkComment saveSmackTalkComment )
  {
    getSession().saveOrUpdate( saveSmackTalkComment );
    return saveSmackTalkComment;
  }

  /**
   * Save SmackTalk Like
   * @param saveSmackTalkLike    
   * @return SmackTalkComment
   */
  public SmackTalkLike saveSmackTalkLike( SmackTalkLike saveSmackTalkLike )
  {
    getSession().saveOrUpdate( saveSmackTalkLike );
    return saveSmackTalkLike;
  }

  /**
   * Get user comments on smacktalk posts 
   * @param matchId   * 
   * @return SmackTalkComment
   */
  @SuppressWarnings( "unchecked" )
  public List<SmackTalkComment> getUserCommentsBySmackTalkPost( Long smackTalkId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( SmackTalkComment.class );
    criteria.add( Restrictions.eq( "parent.id", smackTalkId ) );
    criteria.add( Restrictions.eq( "isHidden", false ) );
    criteria.addOrder( Order.asc( "auditCreateInfo" ) );
    return criteria.list();
  }

  /**
   * Get All User who likes Comments
   * @param smackTalkId    
   * @return SmackTalkLike
   */
  @SuppressWarnings( "unchecked" )
  public List<SmackTalkLike> getUserLikesByComments( Long smackTalkId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.SmackTalkLike.userLikesByComment" );
    query.setParameter( "smackTalkId", smackTalkId );
    return query.list();
  }

  /**
   * Get All SmackTalkPosts per Match
   * @param matchId    
   * @return SmackTalkComment
   */
  @SuppressWarnings( "unchecked" )
  public List<SmackTalkComment> getSmackTalkPostsByMatch( Long matchId )
  {

    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( SmackTalkComment.class );
    criteria.add( Restrictions.eq( "match.id", matchId ) );
    criteria.add( Restrictions.eq( "isHidden", false ) );
    criteria.add( Restrictions.isNull( "parent.id" ) );
    return criteria.list();
  }

  /**
   * 
   * {@inheritDoc}
   */
  public boolean isCurrentUserLikedSmackTalk( Long smackTalkId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.SmackTalkLike.isCurrentUserLikedMatch" );
    query.setParameter( "smackTalkId", smackTalkId );
    query.setParameter( "userId", userId );
    return ( (Long)query.uniqueResult() ).longValue() > 0;
  }

  /**
   * 
   * @param matchId
   * @return SmackTalkComment
   */
  public List<SmackTalkComment> getSmackTalkPostsForMatch( Long[] matchIds )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( SmackTalkComment.class );
    criteria.add( Restrictions.in( "match.id", matchIds ) );
    criteria.add( Restrictions.eq( "isHidden", false ) );
    criteria.add( Restrictions.isNull( "parent.id" ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SmackTalkComment> getSmackTalkByPromotionAndRoundNumber( Long promotionId, Integer roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.promotion.getSmackTalksByPromotionAndRoundNumber" );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundNumber", roundNumber );
    query.setParameter( "isHidden", false );
    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<SmackTalkComment> getSmackTalkByTeam( Set<Long> teamIds, Integer roundNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.promotion.getSmackTalkByTeam" );
    query.setParameterList( "teamIds", teamIds );
    query.setParameter( "roundNumber", roundNumber );
    query.setParameter( "isHidden", false );
    return query.list();
  }

  @Override
  public List<SmackTalkComment> getSmackTalkByPromotionAndRoundIdAndTeam( Long promotionId, Long roundId, Long teamId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.promotion.getSmackTalkByPromotionAndRoundIdAndTeam" );
    query.setParameter( "teamId", teamId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundId", roundId );
    query.setParameter( "isHidden", false );
    return query.list();
  }

  @Override
  public List<SmackTalkComment> getSmackTalkByPromotionAndRoundIdAndUser( Long promotionId, Long roundId, Long userId )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.promotion.getSmackTalkByPromotionAndRoundIdAndUser" );
    query.setParameter( "userId", userId );
    query.setParameter( "promotionId", promotionId );
    query.setParameter( "roundId", roundId );
    query.setParameter( "isHidden", false );
    return query.list();
  }

  /**
   * @param  array of smackTalkIds    
   * @return List<SmackTalkComment>
   */
  @SuppressWarnings( "unchecked" )
  public List<SmackTalkComment> getUserCommentsForSmackTalkPosts( Long[] smackTalkIds )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( SmackTalkComment.class );
    criteria.add( Restrictions.in( "parent.id", smackTalkIds ) );
    criteria.add( Restrictions.eq( "isHidden", false ) );
    return criteria.list();
  }

  /**
   * @param  array of smackTalkIds    
   * @return List<SmackTalkLike>
   */
  @SuppressWarnings( "unchecked" )
  public List<SmackTalkLike> getUserLikesForComments( Long[] smackTalkIds )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( SmackTalkLike.class );
    criteria.add( Restrictions.in( "smackTalkComment.id", smackTalkIds ) );
    return criteria.list();
  }

  @SuppressWarnings( "unchecked" )
  public List<SmackTalkLike> getLikedPaxListBySmackTalkId( Long smackTalkId, int pageNumber )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria cri = session.createCriteria( SmackTalkLike.class );
    cri.add( Restrictions.eq( "smackTalkComment.id", smackTalkId ) );
    cri.setFirstResult( ( pageNumber - 1 ) * pageSize );
    cri.setMaxResults( pageSize );
    return cri.list();
  }

  public int getLikedPaxCount( Long smackTalkId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( SmackTalkLike.class );
    criteria.add( Restrictions.eq( "smackTalkComment.id", smackTalkId ) );
    criteria.setProjection( Projections.rowCount() );
    return ( (Long)criteria.list().get( 0 ) ).intValue();

  }

  /**
   * Get Datasource
   * 
   * @param dataSource    
   * 
   */
  public void setDataSource( DataSource dataSource )
  {
    // TODO what if you want to use a JdbcTemplate by preference,
    // for a native extractor?
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  @Override
  public long getLikeCountBySmackTalk( Long smackTalkId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.SmackTalkLike.likeCount" );
    query.setParameter( "smackTalkId", smackTalkId );
    if ( query.uniqueResult() != null )
    {
      return ( (Long)query.uniqueResult() ).longValue();
    }
    else
    {
      return 0;
    }
  }

  public SmackTalkComment getSmackTalkComment( Long smackTalkId )
  {
    Session session = HibernateSessionManager.getSession();
    Criteria criteria = session.createCriteria( SmackTalkComment.class );
    criteria.add( Restrictions.eq( "id", smackTalkId ) );
    return (SmackTalkComment)criteria.uniqueResult();

  }
}

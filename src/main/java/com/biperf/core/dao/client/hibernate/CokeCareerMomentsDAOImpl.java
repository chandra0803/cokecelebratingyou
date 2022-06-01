/*
 * (c) 2020 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.client.hibernate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.client.CokeCareerMomentsDAO;
import com.biperf.core.domain.client.CareerMomentsView;
import com.biperf.core.domain.client.ClientProfileComment;
import com.biperf.core.domain.client.ClientProfileLike;
import com.biperf.core.domain.client.JobChangesDatum;
import com.biperf.core.domain.client.NewHiresDatum;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.client.CokeCommentsLikes;

/**
 * TODO Javadoc for CokeCareerMomentsDAOImpl.
 *
 * @author yelamanc
 * @since Mar 3, 2020
 * @version 1.0
 */
public class CokeCareerMomentsDAOImpl extends BaseDAO implements CokeCareerMomentsDAO
{
  private static final Log log = LogFactory.getLog( CokeCareerMomentsDAOImpl.class );
  private static String DELETE_LIKE_ABOUTME_RECORD = "DELETE FROM ADIH_USER_PROFILE_LIKE WHERE PROFILE_USER_ID=? AND LIKE_USER_ID=? AND PARTICIPANT_ABOUTME_ID=?";
  private static String DELETE_LIKE_COMMENT_RECORD = "DELETE FROM ADIH_USER_PROFILE_LIKE WHERE PROFILE_USER_ID=? AND LIKE_USER_ID=? AND ADIH_PROFILE_COMMENT_ID=?";
  

  private DataSource dataSource;
  private JdbcTemplate jdbcTemplate;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<NewHiresDatum> getCareerMomentsHireDataForModule()
  {
    CallPrcCokeCareerMomentsModule procedure = new CallPrcCokeCareerMomentsModule( dataSource );
    Map results = procedure.executeProcedure();
    return (List<NewHiresDatum>)results.get( CallPrcCokeCareerMomentsModule.PO_OUT_RESULT_HIRE_SET );
  }

  @Override
  public List<JobChangesDatum> getCareerMomentsJobDataForModule()
  {
    CallPrcCokeCareerMomentsModule procedure = new CallPrcCokeCareerMomentsModule( dataSource );
    Map results = procedure.executeProcedure();
    return (List<JobChangesDatum>)results.get( CallPrcCokeCareerMomentsModule.PO_OUT_RESULT_JOB_SET );
  }
  
  @Override
  public List<CareerMomentsView> getCareerMomentsJobDataForDetail(String tabType, int current, String recType, String listVal, String locale, String username)
  {
    CallPrcCokeCareerMomentsDetail procedure = new CallPrcCokeCareerMomentsDetail( dataSource );
    Map results = procedure.executeProcedure(UserManager.getUserId(), current, recType, tabType, listVal, locale, username);
    return (List<CareerMomentsView>)results.get( "p_out_result_set" );
  }
  
  @Override
  public List<CokeCommentsLikes> getCareerMomentsLikesCommentsCount(String date)
  {
    CallPrdCokeFindLikesComments procedure = new CallPrdCokeFindLikesComments( dataSource );
    Map results = procedure.executeProcedure(date);
    return (List<CokeCommentsLikes>)results.get( "p_out_result_set" );
  }
  
  @Override
  public List<ClientProfileComment> getAllProfileCommentsByUserId(Long userId)
  {
    Criteria criteria = getSession().createCriteria( ClientProfileComment.class );
    criteria.add( Restrictions.eq( "profileUserId", userId ) );

    return criteria.list();

  }
  
  @Override
  public List<ClientProfileComment> getMainLevelProfileCommentsByUserId(Long userId)
  {
    Criteria criteria = getSession().createCriteria( ClientProfileComment.class );
    criteria.add( Restrictions.eq( "profileUserId", userId ) );
    criteria.add( Restrictions.isNull( "profileSubCommentId") );

    return criteria.list();

  }
  
  @Override
  public List<ClientProfileComment> getSubLevelProfileComments(Long profileUserId, Long mainCommentId)
  {
    Criteria criteria = getSession().createCriteria( ClientProfileComment.class );
    criteria.add( Restrictions.eq( "profileUserId", profileUserId ) );
    criteria.add( Restrictions.eq( "profileSubCommentId", mainCommentId) );

    return criteria.list();

  }
  
  @Override
  public int getLikesCountByCommentId(Long commentId)
  {
    Criteria criteria = getSession().createCriteria( ClientProfileLike.class );
    criteria.add( Restrictions.eq( "profileCommentId", commentId ) );

    return criteria.list().size();
  }
  
  @Override
  public int getLikesCountByAboutMeId(Long aboutMeId)
  {
    Criteria criteria = getSession().createCriteria( ClientProfileLike.class );
    criteria.add( Restrictions.eq( "paxAboutMeId", aboutMeId ) );

    return criteria.list().size();
  }
  
  @Override
  public ClientProfileComment saveComment(ClientProfileComment clientComment)
  {
    return (ClientProfileComment)HibernateUtil.saveOrUpdateOrShallowMerge( clientComment );
  }
  
  @Override
  public ClientProfileLike saveLike(ClientProfileLike clientLike)
  {
    return (ClientProfileLike)HibernateUtil.saveOrUpdateOrShallowMerge( clientLike );
  }
  
  @Override
  public void removeAboutMeLike( final Long userId, final Long profileUserId, final Long aboutMeId )
  {
    this.jdbcTemplate.update( DELETE_LIKE_ABOUTME_RECORD, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setLong( 1, profileUserId );
        ps.setLong( 2,  userId);
        ps.setLong( 3, aboutMeId );
      }
    } );
  }
  
  @Override
  public void removeCommentLike( final Long userId, final Long profileUserId, final Long commentId )
  {
    this.jdbcTemplate.update( DELETE_LIKE_COMMENT_RECORD, new PreparedStatementSetter()
    {
      @Override
      public void setValues( PreparedStatement ps ) throws SQLException
      {
        ps.setLong( 1, profileUserId );
        ps.setLong( 2,  userId);
        ps.setLong( 3, commentId );
      }
    } );
  }
  
  @Override
  public Map<Long, Long> getAboutMeLikesByUserId(Long userId, List<AboutMe> aboutMeQuestions)
  {
    Criteria criteria = getSession().createCriteria( ClientProfileLike.class );
    criteria.add( Restrictions.eq( "profileUserId", userId ) );
    
    List<ClientProfileLike> likeList = criteria.list();
    
    if(likeList!=null && likeList.size()>0)
    {
      Map<Long, Long> likeMap = new HashMap<Long, Long>();
      for(AboutMe aboutMe: aboutMeQuestions)
      {
        Long likeCount = 0L;
        for(ClientProfileLike like:likeList)
        {
          if(aboutMe.getId().equals( like.getPaxAboutMeId() ))
          {
            likeCount++;
          }
        }
        likeMap.put( aboutMe.getId(), likeCount );
      }
      return likeMap;
    }
    
    return null;
  }
  
  @Override
  public Map<Long, Boolean> getMyAboutMeLikes(List<AboutMe> aboutMeQuestions, Participant pax)
  {
    Criteria criteria = getSession().createCriteria( ClientProfileLike.class );
    criteria.add( Restrictions.eq( "likedUser", pax ) );
    
    List<ClientProfileLike> likeList = criteria.list();
    
    if(likeList!=null && likeList.size()>0)
    {
      Map<Long, Boolean> likeMap = new HashMap<Long, Boolean>();
      for(AboutMe aboutMe: aboutMeQuestions)
      {
        Boolean liked = false;
        for(ClientProfileLike like:likeList)
        {
          if(aboutMe.getId().equals( like.getPaxAboutMeId() ))
          {
            liked = true;
          }
        }
        likeMap.put( aboutMe.getId(), liked );
      }
      return likeMap;
    }
    
    return null;
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

}

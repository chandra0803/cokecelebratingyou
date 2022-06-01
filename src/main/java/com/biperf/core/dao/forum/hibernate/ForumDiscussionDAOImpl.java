/**
 * 
 */

package com.biperf.core.dao.forum.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.forum.ForumDiscussionDAO;
import com.biperf.core.domain.forum.ForumDiscussion;
import com.biperf.core.domain.forum.ForumDiscussionLike;
import com.biperf.core.domain.forum.ForumDiscussionReply;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;
import com.biperf.core.value.forum.ForumDiscussionValueBean;

/**
 * @author poddutur
 *
 */
public class ForumDiscussionDAOImpl extends BaseDAO implements ForumDiscussionDAO
{
  @SuppressWarnings( "unused" )
  private DataSource dataSource;

  /**
     * gets the sorted list of discussion.
     * 
     * @param topicId
     * @return ForumDiscussionValueBean
     */
  @SuppressWarnings( "unchecked" )
  @Override
  public List<ForumDiscussionValueBean> getSortedDiscussionList( Long topicId )
  {
    String languageCode = UserManager.getLocale().toString();
    String sql = buildAllSortedDiscussionListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( new ForumDiscussionValueBeanMapper() );
    query.setParameter( "topicId", topicId.longValue() );
    query.setParameter( "languageCode", languageCode );
    return query.list();
  }

  private String buildAllSortedDiscussionListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT ft.forum_topic_id, " );
    sql.append( "   (SELECT cms_value " );
    sql.append( "   FROM vw_cms_asset_value " );
    sql.append( "   WHERE locale   = :languageCode " );
    sql.append( "   AND asset_code = ft.topic_name_asset_name " );
    sql.append( "   ) TopicName, " );
    sql.append( "   fd.discussion_id, " );
    sql.append( "   fd.discussion_title, " );
    sql.append( "   NVL(fd2.replies,0) replies, " );
    sql.append( "   fd.date_created AS discussion_created_date, " );
    sql.append( "   fd.created_by AS discussion_created_user_id, " );
    sql.append( "   au.first_name " );
    sql.append( "   ||' ' " );
    sql.append( "   ||au.last_name   AS discussion_creation_user, " );
    sql.append( "   fd3.date_created AS reply_created_date, " );
    sql.append( "   fd3.user_id AS reply_created_user_id, " );
    sql.append( "   CASE " );
    sql.append( "     WHEN fd3.created_by IS NULL " );
    sql.append( "     THEN NULL " );
    sql.append( "     ELSE " );
    sql.append( "       (SELECT au2.first_name " );
    sql.append( "         ||' ' " );
    sql.append( "         ||au2.last_name " );
    sql.append( "       FROM application_user au2 " );
    sql.append( "       WHERE user_id = fd3.user_id " );
    sql.append( "       ) " );
    sql.append( "   END reply_user " );
    sql.append( " FROM forum_topic ft, " );
    sql.append( "   forum_discussion fd, " );
    sql.append( "   (SELECT parent_discussion_id, " );
    sql.append( "     COUNT(discussion_id) replies " );
    sql.append( "   FROM forum_discussion " );
    sql.append( "   WHERE parent_discussion_id IS NOT NULL " );
    sql.append( "   AND forum_topic_id          = :topicId " );
    sql.append( "   AND status = 'A' " );
    sql.append( "   GROUP BY parent_discussion_id " );
    sql.append( "   ) fd2, " );
    sql.append( "   application_user au, " );
    sql.append( "   (SELECT fd.date_created, " );
    sql.append( "     fd.created_by, " );
    sql.append( "     fd.parent_discussion_id, fd.user_id " );
    sql.append( "   FROM " );
    sql.append( "     (SELECT RANK() OVER (PARTITION BY R.parent_discussion_id ORDER BY R.discussion_id DESC) AS rec_rank, " );
    sql.append( "       R.* " );
    sql.append( "     FROM forum_discussion R " );
    sql.append( "     WHERE forum_topic_id      = :topicId " );
    sql.append( "     AND status = 'A' " );
    sql.append( "     AND parent_discussion_id IS NOT NULL " );
    sql.append( "     ) fd " );
    sql.append( "   WHERE rec_rank = 1 " );
    sql.append( "   ) fd3 " );
    sql.append( " WHERE ft.forum_topic_id         = fd.forum_topic_id " );
    sql.append( " AND ft.forum_topic_id           = :topicId " );
    sql.append( " AND fd.parent_discussion_id    IS NULL " );
    sql.append( " AND fd.created_by               = au.user_id " );
    sql.append( " AND fd.discussion_id            = fd2.parent_discussion_id(+) " );
    sql.append( " AND fd3.parent_discussion_id(+) = fd2.parent_discussion_id " );
    sql.append( " AND ft.status = 'A' " );
    sql.append( " AND fd.status = 'A' " );
    sql.append( " ORDER BY NVL(reply_created_date, discussion_created_date) DESC " );

    return sql.toString();

  }

  @SuppressWarnings( "serial" )
  private class ForumDiscussionValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ForumDiscussionValueBean reportValue = new ForumDiscussionValueBean();

      reportValue.setTopicId( extractLong( tuple[0] ) );
      reportValue.setTopicCmAssetCode( extractString( tuple[1] ) );
      reportValue.setDiscussionId( extractLong( tuple[2] ) );
      reportValue.setDiscussionTitle( extractString( tuple[3] ) );
      reportValue.setReplies( extractLong( tuple[4] ) );
      reportValue.setDateCreated( extractDate( tuple[5] ) );
      reportValue.setCreatedUserId( extractLong( tuple[6] ) );
      reportValue.setCreatedBy( extractString( tuple[7] ) );
      reportValue.setDateReplied( extractDate( tuple[8] ) );
      reportValue.setRepliedUserId( extractLong( tuple[9] ) );
      reportValue.setRepliedBy( extractString( tuple[10] ) );

      return reportValue;
    }

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ForumDiscussionDetailValueBean> getDiscussionDetailItems( Long discussionId, Long userId )
  {
    String languageCode = UserManager.getLocale().toString();
    String sql = buildDiscussionDetailItemsListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( new ForumDiscussionDetailValueBeanMapper() );
    query.setParameter( "discussionId", discussionId );
    query.setParameter( "languageCode", languageCode );
    query.setParameter( "userId", userId );

    return query.list();
  }

  private String buildDiscussionDetailItemsListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT fd.user_id, " );
    sql.append( "   au.first_name FirstName, " );
    sql.append( "   au.last_name LastName, " );
    sql.append( "   p.avatar_small avatarURL, " );
    sql.append( "   ft.forum_topic_id topic_id, " );
    sql.append( "   (SELECT cms_value " );
    sql.append( "   FROM vw_cms_asset_value " );
    sql.append( "   WHERE locale   = :languageCode " );
    sql.append( "   AND asset_code = ft.topic_name_asset_name " );
    sql.append( "   ) TopicName, " );
    sql.append( "   (SELECT cms_value " );
    sql.append( "   FROM vw_cms_asset_value " );
    sql.append( "   WHERE locale   = 'en_US'" );
    sql.append( "   AND asset_code = ft.topic_name_asset_name " );
    sql.append( "   ) TopicNameDefault, " );
    sql.append( "   fd.discussion_title discussionName, " );
    sql.append( "   fd.discussion_id, " );
    sql.append( "   fd.discussion_body, " );
    sql.append( "   (SELECT COUNT (discussion_id) " );
    sql.append( "   FROM forum_discussion_like " );
    sql.append( "   WHERE discussion_id = fd.discussion_id " );
    sql.append( "   ) number_of_likes, " );
    sql.append( "   (SELECT listagg(f.user_id, ',') within GROUP ( " );
    sql.append( "   ORDER BY discussion_id) " );
    sql.append( "   FROM forum_discussion_like f " );
    sql.append( "   WHERE discussion_id = fd.discussion_id " );
    sql.append( "   AND f.user_id != :userId " );
    sql.append( "   ) users_liked, " );
    sql.append( "   (SELECT COUNT (discussion_id) " );
    sql.append( "   FROM forum_discussion " );
    sql.append( "   WHERE parent_discussion_id = fd.discussion_id " );
    sql.append( "   AND is_reply               = 1 " );
    sql.append( "   AND status                 = 'A' " );
    sql.append( "   ) number_of_replies, " );
    sql.append( "   TO_CHAR(fd.date_created, 'MM/DD/YYYY') created_date, " );
    sql.append( "   TO_CHAR(fd.date_created, 'HH:MI:SS AM') created_time " );
    sql.append( " FROM forum_discussion fd, " );
    sql.append( "   forum_topic ft, " );
    sql.append( "   application_user au, " );
    sql.append( "   participant p " );
    sql.append( " WHERE fd.forum_topic_id      = ft.forum_topic_id " );
    sql.append( " AND fd.user_id               = au.user_id " );
    sql.append( " AND au.user_id               = p.user_id " );
    sql.append( " AND fd.parent_discussion_id IS NULL " );
    sql.append( " AND fd.discussion_id         = :discussionId " );
    sql.append( " AND ft.status = 'A' " );
    sql.append( " AND fd.status = 'A' " );

    return sql.toString();
  }

  @SuppressWarnings( "serial" )
  private class ForumDiscussionDetailValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ForumDiscussionDetailValueBean reportValue = new ForumDiscussionDetailValueBean();

      reportValue.setUserId( extractLong( tuple[0] ) );
      reportValue.setFirstName( extractString( tuple[1] ) );
      reportValue.setLastName( extractString( tuple[2] ) );
      reportValue.setAvatarUrl( extractString( tuple[3] ) );
      reportValue.setTopicId( extractLong( tuple[4] ) );
      reportValue.setTopicName( extractString( tuple[5] ) );
      reportValue.setTopicNameDefault( extractString( tuple[6] ) );
      reportValue.setDiscussionName( extractString( tuple[7] ) );
      reportValue.setDiscussionId( extractLong( tuple[8] ) );
      reportValue.setDiscussionBody( extractString( tuple[9] ) );
      reportValue.setNumberOfLikes( extractLong( tuple[10] ) );
      reportValue.setCommenterLikedIds( extractString( tuple[11] ) );
      reportValue.setNumberOfReplies( extractInt( tuple[12] ) );
      reportValue.setCreatedDate( extractString( tuple[13] ) );
      reportValue.setCreatedTime( extractString( tuple[14] ) );

      return reportValue;
    }

  }

  @Override
  public List<ForumDiscussionDetailValueBean> getDiscussionReplies( int rowNumStart, int rowNumEnd, Long discussionId, Long userId )
  {
    String sql = buildDiscussionRepliesListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( new ForumDiscussionRepliesValueBeanMapper() );
    query.setParameter( "discussionId", discussionId );
    query.setParameter( "rowNumStart", rowNumStart );
    query.setParameter( "rowNumEnd", rowNumEnd );
    query.setParameter( "userId", userId );
    return query.list();
  }

  private String buildDiscussionRepliesListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT * " );
    sql.append( " FROM " );
    sql.append( "   (SELECT ROWNUM RN, " );
    sql.append( "     rs.* " );
    sql.append( "   FROM " );
    sql.append( "     (SELECT fd.discussion_id commentsId, " );
    sql.append( "       fd.user_id commenterId, " );
    sql.append( "       (SELECT COUNT (discussion_id) " );
    sql.append( "       FROM forum_discussion_like " );
    sql.append( "       WHERE discussion_id = fd.discussion_id " );
    sql.append( "       ) numLikers, " );
    sql.append( "       (SELECT listagg(f.user_id, ',') within GROUP ( " );
    sql.append( "       ORDER BY discussion_id) " );
    sql.append( "       FROM forum_discussion_like f " );
    sql.append( "       WHERE discussion_id = fd.discussion_id " );
    sql.append( "       AND f.user_id != :userId " );
    sql.append( "       ) users_liked, " );
    sql.append( "       au.first_name FirstName, " );
    sql.append( "       au.last_name LastName, " );
    sql.append( "       p.avatar_small avatarURL, " );
    sql.append( "       fd.discussion_body Comment_Body, " );
    sql.append( "       TO_CHAR(fd.date_created, 'MM/DD/YYYY') Commented_date, " );
    sql.append( "       TO_CHAR(fd.date_created, 'HH:MI:SS AM') Commented_Time " );
    sql.append( "     FROM forum_discussion fd, " );
    sql.append( "       application_user au, " );
    sql.append( "       participant p " );
    sql.append( "     WHERE fd.user_id            = au.user_id " );
    sql.append( "     AND au.user_id              = p.user_id " );
    sql.append( "     AND fd.parent_discussion_id = :discussionId " );
    sql.append( "     AND fd.status = 'A' " );
    sql.append( "     ORDER BY fd.date_created ASC " );
    sql.append( "     )rs " );
    sql.append( "   ) " );
    sql.append( " WHERE RN >= :rowNumStart " );
    sql.append( " AND RN   <= :rowNumEnd " );

    return sql.toString();
  }

  @SuppressWarnings( "serial" )
  private class ForumDiscussionRepliesValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ForumDiscussionDetailValueBean reportValue = new ForumDiscussionDetailValueBean();

      reportValue.setCommentId( extractLong( tuple[1] ) );
      reportValue.setCommenterId( extractLong( tuple[2] ) );
      reportValue.setCommentNumOfLikers( extractLong( tuple[3] ) );
      reportValue.setCommenterLikedIds( extractString( tuple[4] ) );
      reportValue.setCommenterFirstName( extractString( tuple[5] ) );
      reportValue.setCommenterLastName( extractString( tuple[6] ) );
      reportValue.setCommenterAvatarUrl( extractString( tuple[7] ) );
      reportValue.setCommentBody( extractString( tuple[8] ) );
      reportValue.setCommentedDate( extractString( tuple[9] ) );
      reportValue.setCommentedTime( extractString( tuple[10] ) );

      return reportValue;
    }

  }

  @Override
  public int getDiscussionRepliesCount( Long discussionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getDiscussionRepliesCount" );
    query.setParameter( "discussionId", discussionId );
    return (Integer)query.uniqueResult();
  }

  @Override
  public ForumDiscussionLike saveForumDiscussionLike( ForumDiscussion forumDiscussion, ForumDiscussionLike forumDiscussionLike )
  {
    ForumDiscussion fd = (ForumDiscussion)getSession().load( ForumDiscussion.class, forumDiscussion.getId() );
    forumDiscussionLike.setForumDiscussion( fd );
    getSession().saveOrUpdate( forumDiscussionLike );
    return forumDiscussionLike;
  }

  @Override
  public Long getDiscussionLikeCountForDiscussion( Long discussionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getDiscussionLikeCount" );
    query.setParameter( "discussionId", discussionId );
    return (Long)query.uniqueResult();
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public List getDiscussionLikedUsersList( Long discussionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getDiscussionLikedUsersList" );
    query.setParameter( "discussionId", discussionId );
    return query.list();
  }

  @Override
  public ForumDiscussionLike saveForumDiscussionCommentLike( ForumDiscussion forumDiscussion, ForumDiscussionLike forumDiscussionLike )
  {
    ForumDiscussion fd = (ForumDiscussion)getSession().load( ForumDiscussion.class, forumDiscussion.getId() );
    forumDiscussionLike.setForumDiscussion( fd );
    getSession().saveOrUpdate( forumDiscussionLike );
    return forumDiscussionLike;
  }

  @Override
  public Long getCommentLikeCountForDiscussion( Long commentId, Long discussionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getCommentLikeCountForDiscussion" );
    query.setParameter( "discussionId", discussionId );
    query.setParameter( "commentId", commentId );
    return (Long)query.uniqueResult();
  }

  @Override
  public List getCommentsLikedUsersList( Long commentId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getCommentsLikedUsersList" );
    query.setParameter( "commentId", commentId );
    return query.list();
  }

  /**
     * Get all forum discussions from the database.
     * 
     * @return List
     */
  @SuppressWarnings( "rawtypes" )
  @Override
  public List getAll()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.AllForumDiscussions" );
    return query.list();
  }

  /**
     * save a forum discussion to the database.
     * 
     * @param ForumDiscussion
     * @return ForumDiscussion
     */
  @Override
  public ForumDiscussion save( User user, ForumTopic forumTopic, ForumDiscussion forumDiscussion )
  {
    ForumTopic ft = (ForumTopic)getSession().load( ForumTopic.class, forumTopic.getId() );
    User us = (User)getSession().load( User.class, user.getId() );
    forumDiscussion.setForumTopic( ft );
    forumDiscussion.setUser( us );
    getSession().saveOrUpdate( forumDiscussion );
    return forumDiscussion;
  }

  @Override
  public ForumDiscussionReply saveReply( User user, ForumTopic forumTopic, ForumDiscussionReply forumDiscussionReply )
  {
    ForumTopic ft = (ForumTopic)getSession().load( ForumTopic.class, forumTopic.getId() );
    User us = (User)getSession().load( User.class, user.getId() );
    forumDiscussionReply.setForumTopic( ft );
    forumDiscussionReply.setUser( us );
    getSession().saveOrUpdate( forumDiscussionReply );
    return forumDiscussionReply;
  }

  /**
   * Save or update the forum discussion to the database.
   * 
   * @param ForumDiscussion
   * @return ForumDiscussion
   */

  @Override
  public ForumDiscussion save( ForumDiscussion forumDiscussion )
  {
    getSession().saveOrUpdate( forumDiscussion );
    return forumDiscussion;
  }

  /**
     * Gets the forum discussion from the database by the discussionId param.
     * 
     * @param discussionId
     * @return ForumDiscussion
     */
  @Override
  public ForumDiscussion getDiscussionById( Long discussionId )
  {
    return (ForumDiscussion)getSession().get( ForumDiscussion.class, discussionId );
  }

  @Override
  public ForumDiscussionReply getDiscussionReplyById( Long discussionId )
  {
    return (ForumDiscussionReply)getSession().get( ForumDiscussionReply.class, discussionId );
  }

  /**
     * Delete a forum discussion.
     * 
     * @param discussionId
     * @throws ServiceErrorException
     */
  @Override
  public void deleteDiscussion( ForumDiscussion discussionToDelete, List<ForumDiscussionReply> repliesToDeleteForDiscussion )
  {
    discussionToDelete.setStatus( "I" );
    if ( repliesToDeleteForDiscussion.size() != 0 )
    {
      for ( ForumDiscussionReply forumDiscussionReply : repliesToDeleteForDiscussion )
      {
        forumDiscussionReply.setStatus( "I" );
        getSession().update( forumDiscussionReply );
      }
    }
    getSession().update( discussionToDelete );
  }

  @Override
  public void deleteDiscussionReply( ForumDiscussionReply repliesToDelete )
  {
    repliesToDelete.setStatus( "I" );
    getSession().update( repliesToDelete );
  }

  @Override
  public List<ForumDiscussionReply> getRepliesByParentDiscussionId( Long discussionId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getRepliesByParentDiscussionId" );
    query.setParameter( "discussionId", discussionId );
    return query.list();
  }

  @Override
  public int getDiscussionCountByName( String discussionName, Long topicId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getDiscussionCountByName" );
    query.setParameter( "discussionName", discussionName );
    query.setParameter( "topicId", topicId );
    return (Integer)query.uniqueResult();
  }

  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }
}

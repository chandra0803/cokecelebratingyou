/**
 * 
 */

package com.biperf.core.dao.forum.hibernate;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.forum.ForumTopicDAO;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.forum.ForumTopicAudience;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.forum.ForumAudienceFormBean;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;
import com.biperf.core.value.forum.ForumTopicValueBean;

/**
 * @author poddutur
 * 
 */
public class ForumTopicDAOImpl extends BaseDAO implements ForumTopicDAO
{
  @SuppressWarnings( "unused" )
  private DataSource dataSource;

  /**
   * Delete the forumTopics
   */
  @Override
  public void deleteTopic( ForumTopic topicToDelete )
  {
    topicToDelete.setStatus( "I" );
    getSession().update( topicToDelete );
  }

  /**
   * get forumTopic by Id
   */
  @Override
  public ForumTopic getTopicById( Long topicId )
  {
    return (ForumTopic)getSession().get( ForumTopic.class, topicId );
  }

  @Override
  public ForumTopicAudience getForumTopicAudienceById( Long audienceId, Long topicId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.forumTopicAudience" );
    query.setParameter( "topicId", topicId );
    query.setParameter( "audienceId", audienceId );
    return (ForumTopicAudience)query.uniqueResult();
  }

  @Override
  public void deleteTopicAudience( ForumTopicAudience topicAudienceToDelete )
  {
    getSession().delete( topicAudienceToDelete );
  }

  @Override
  public List<ForumAudienceFormBean> getAudienceByTopicId( Long topicId )
  {
    String sql = buildAudienceListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "topicId", topicId );
    query.setResultTransformer( new ForumTopicAudienceValueBeanMapper() );
    return query.list();
  }

  private String buildAudienceListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT a.name, a.audience_id, Count(pa.user_id) " );
    sql.append( "   FROM audience a, " );
    sql.append( " forum_topic_audience fta, participant_audience pa " );
    sql.append( " WHERE a.audience_id    = fta.audience_id AND a.audience_id = pa.audience_id " );
    sql.append( " AND fta.forum_topic_id = :topicId " );
    sql.append( " GROUP BY  a.audience_id, a.name " );

    return sql.toString();
  }

  @SuppressWarnings( "serial" )
  private class ForumTopicAudienceValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ForumAudienceFormBean reportValue = new ForumAudienceFormBean();

      reportValue.setName( extractString( tuple[0] ) );
      reportValue.setAudienceId( extractLong( tuple[1] ) );
      reportValue.setSize( extractInt( tuple[2] ) );

      return reportValue;
    }
  }

  /**
     * Get all forum topics from the database.
     * 
     * @return List
     */
  @Override
  public List getAll()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.AllForumTopics" );
    return query.list();
  }

  @Override
  public List<ForumTopicValueBean> getAllTopicNamesForPax()
  {
    Long userId = UserManager.getUserId();
    String languageCode = UserManager.getLocale().toString();
    String sql = buildAllTopicNamesForPaxListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "userId", userId );
    query.setParameter( "languageCode", languageCode );
    query.setResultTransformer( new ForumTopicNamesValueBeanMapper() );
    return query.list();
  }

  private String buildAllTopicNamesForPaxListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT forum_topic_id, " );
    sql.append( "   (SELECT cms_value " );
    sql.append( "   FROM vw_cms_asset_value " );
    sql.append( "   WHERE locale   = DECODE(locale,:languageCode,:languageCode,(select string_val from os_propertyset where entity_name = 'default.language')) " );
    sql.append( "   AND asset_code = topic_name_asset_name " );
    sql.append( "   ) topic_name " );
    sql.append( " FROM forum_topic " );
    sql.append( " WHERE audience_type = 'all active participants' " );
    sql.append( " AND status = 'A' " );
    sql.append( " UNION " );
    sql.append( " SELECT ft.forum_topic_id, " );
    sql.append( "   (SELECT cms_value " );
    sql.append( "   FROM vw_cms_asset_value " );
    sql.append( "   WHERE locale   = DECODE(locale,:languageCode,:languageCode,(select string_val from os_propertyset where entity_name = 'default.language')) " );
    sql.append( "   AND asset_code = ft.topic_name_asset_name " );
    sql.append( "   ) topic_name " );
    sql.append( " FROM participant_audience pa, " );
    sql.append( "   audience a, " );
    sql.append( "   forum_topic_audience fta, " );
    sql.append( "   forum_topic ft " );
    sql.append( " WHERE pa.audience_id   = a.audience_id " );
    sql.append( " AND a.audience_id      = fta.audience_id " );
    sql.append( " AND fta.forum_topic_id = ft.forum_topic_id " );
    sql.append( " AND pa.user_id         = :userId " );
    sql.append( " AND ft.status = 'A' " );

    return sql.toString();
  }

  public List getAllSortedTopicList()
  {
    String sql = buildAllSortedTopicListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setResultTransformer( new ForumTopicValueBeanMapper() );
    return query.list();
  }

  @Override
  public List<ForumTopicValueBean> getAllSortedTopicListByPax( Long userId )
  {
    String sql = buildAllSortedTopicListByPaxQuery( userId );
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "userId", userId );
    query.setResultTransformer( new ForumTopicValueBeanForPaxMapper() );
    return query.list();
  }

  @SuppressWarnings( "serial" )
  private class ForumTopicNamesValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ForumTopicValueBean reportValue = new ForumTopicValueBean();

      reportValue.setSelectedTopicId( extractLong( tuple[0] ) );
      reportValue.setTopicCmAssetCode( extractString( tuple[1] ) );

      return reportValue;
    }
  }

  @SuppressWarnings( "serial" )
  private class ForumTopicValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ForumTopicValueBean reportValue = new ForumTopicValueBean();

      reportValue.setId( extractLong( tuple[0] ) );
      reportValue.setTopicCmAssetCode( extractString( tuple[1] ) );
      reportValue.setDiscussionCount( extractLong( tuple[2] ) );
      reportValue.setAudienceName( extractString( tuple[3] ) );
      reportValue.setStickyStartDate( extractDate( tuple[4] ) );
      reportValue.setStickyEndDate( extractDate( tuple[5] ) );
      reportValue.setSortOrder( extractLong( tuple[6] ) );
      reportValue.setLastActivityDate( extractDate( tuple[7] ) );

      return reportValue;
    }
  }

  @SuppressWarnings( "serial" )
  private class ForumTopicValueBeanForPaxMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ForumTopicValueBean reportValue = new ForumTopicValueBean();

      reportValue.setId( extractLong( tuple[0] ) );
      reportValue.setTopicCmAssetCode( extractString( tuple[1] ) );
      reportValue.setDiscussionCount( extractLong( tuple[2] ) );
      reportValue.setStickyStartDate( extractDate( tuple[3] ) );
      reportValue.setStickyEndDate( extractDate( tuple[4] ) );
      reportValue.setSortOrder( extractLong( tuple[5] ) );
      reportValue.setRepliesCount( extractLong( tuple[6] ) );
      reportValue.setLastActivityDate( extractDate( tuple[7] ) );
      reportValue.setLastPostUserName( extractString( tuple[8] ) );
      reportValue.setLastPostUserId( extractLong( tuple[9] ) );

      return reportValue;
    }
  }

  private String buildAllSortedTopicListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT ft.forum_topic_id   AS topicId, " );
    sql.append( "   ft.topic_name_asset_name AS topicName, " );
    sql.append( "   (SELECT COUNT(discussion_id) " );
    sql.append( "   FROM forum_discussion " );
    sql.append( "   WHERE parent_discussion_id IS NULL " );
    sql.append( "   AND forum_topic_id          = ft.forum_topic_id " );
    sql.append( "   AND status = 'A' " );
    sql.append( "   ) AS Discussion_Count, " );
    sql.append( "   DECODE (ft.audience_type, 'all active participants', ft.audience_type, " );
    sql.append( "   (SELECT listagg(a.name, ',') within GROUP ( " );
    sql.append( "   ORDER BY a.audience_id) AS name " );
    sql.append( "   FROM audience a, " );
    sql.append( "     forum_topic_audience fta " );
    sql.append( "   WHERE a.audience_id    = fta.audience_id " );
    sql.append( "   AND fta.forum_topic_id = ft.forum_topic_id " );
    sql.append( "   ))                    AS Audience_Names, " );
    sql.append( "   ft.sticky_start_date  AS stickyStartDate, " );
    sql.append( "   ft.sticky_end_date    AS stickyEndDate, " );
    sql.append( "   ft.sort_order         AS sortOrder, " );
    sql.append( "   ft.last_activity_date AS lastActivityDate " );
    sql.append( " FROM forum_topic ft " );
    sql.append( " WHERE ft.status = 'A' " );
    sql.append( " ORDER BY topicName " );

    return sql.toString();
  }

  private String buildAllSortedTopicListByPaxQuery( Long userId )
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT forum_topic_id, " );
    sql.append( "   topic_name, " );
    sql.append( "   NVL(discussion_count, 0 ) discussion_count, " );
    sql.append( "   sticky_start_date, " );
    sql.append( "   sticky_end_date, " );
    sql.append( "   sort_order, " );
    sql.append( "   NVL(replies, 0 ) replies, " );
    sql.append( "   date_created last_post_date, " );
    sql.append( "   name last_post_person, " );
    sql.append( "   userId last_post_person_userId " );
    sql.append( " FROM " );
    sql.append( "   (WITH forum AS " );
    sql.append( "   (SELECT forum_topic_id, " );
    sql.append( "     topic_name_asset_name topic_name, " );
    sql.append( "     sticky_start_date, " );
    sql.append( "     sticky_end_date, " );
    sql.append( "     sort_order " );
    sql.append( "   FROM forum_topic " );
    sql.append( "   WHERE audience_type = 'all active participants' " );
    sql.append( "   AND status = 'A' " );
    sql.append( "   UNION " );
    sql.append( "   SELECT ft.forum_topic_id, " );
    sql.append( "     topic_name_asset_name topic_name, " );
    sql.append( "     sticky_start_date, " );
    sql.append( "     sticky_end_date, " );
    sql.append( "     sort_order " );
    sql.append( "   FROM participant_audience pa, " );
    sql.append( "     audience a, " );
    sql.append( "     forum_topic_audience fta, " );
    sql.append( "     forum_topic ft " );
    sql.append( "   WHERE pa.audience_id   = a.audience_id " );
    sql.append( "   AND a.audience_id      = fta.audience_id " );
    sql.append( "   AND fta.forum_topic_id = ft.forum_topic_id " );
    sql.append( "   AND pa.user_id         = :userId " );
    sql.append( "   AND ft.status = 'A' " );
    sql.append( "   ), " );
    sql.append( "   counts AS " );
    sql.append( "   (SELECT f1.forum_topic_id, " );
    sql.append( "     SUM (DECODE (fd1.parent_discussion_id, NULL, 1, 0)) discussion_count, " );
    sql.append( "     SUM (DECODE (fd1.is_reply, 1, 1, 0)) replies " );
    sql.append( "   FROM forum f1, " );
    sql.append( "     forum_discussion fd1 " );
    sql.append( "   WHERE f1.forum_topic_id = fd1.forum_topic_id " );
    sql.append( "   AND fd1.status = 'A' " );
    sql.append( "   GROUP BY f1.forum_topic_id " );
    sql.append( "   ), " );
    sql.append( "   last_post AS " );
    sql.append( "   (SELECT fd.forum_topic_id, " );
    sql.append( "     fd.date_created, " );
    sql.append( "     au.first_name " );
    sql.append( "     ||', ' " );
    sql.append( "     ||au.last_name name, " );
    sql.append( "     au.user_Id userId " );
    sql.append( "   FROM application_user au, " );
    sql.append( "     (SELECT forum_topic_id, " );
    sql.append( "       user_id, " );
    sql.append( "       date_created " );
    sql.append( "     FROM " );
    sql.append( "       (SELECT RANK () OVER (PARTITION BY fd2.forum_topic_id ORDER BY fd2.discussion_id DESC) AS rec_rank, " );
    sql.append( "         fd2.forum_topic_id, " );
    sql.append( "         fd2.user_id, " );
    sql.append( "         fd2.date_created " );
    sql.append( "       FROM forum_discussion fd2, " );
    sql.append( "         forum f2 " );
    sql.append( "       WHERE f2.forum_topic_id       = fd2.forum_topic_id(+) " );
    sql.append( "       AND fd2.status = 'A' " );
    sql.append( "       AND fd2.parent_discussion_id IS NULL " );
    sql.append( "       ) " );
    sql.append( "     WHERE rec_rank = 1 " );
    sql.append( "     ) fd " );
    sql.append( "   WHERE au.user_id = fd.user_id " );
    sql.append( "   ) " );
    sql.append( " SELECT f3.forum_topic_id, " );
    sql.append( "   f3.topic_name, " );
    sql.append( "   f3.sticky_start_date, " );
    sql.append( "   f3.sticky_end_date, " );
    sql.append( "   f3.sort_order, " );
    sql.append( "   c1.discussion_count, " );
    sql.append( "   c1.replies, " );
    sql.append( "   lp.date_created, " );
    sql.append( "   lp.name, " );
    sql.append( "   lp.userId " );
    sql.append( " FROM forum f3, " );
    sql.append( "   counts c1, " );
    sql.append( "   last_post lp " );
    sql.append( " WHERE f3.forum_topic_id = c1.forum_topic_id(+) " );
    sql.append( " AND f3.forum_topic_id   = lp.forum_topic_id(+) " );
    sql.append( "   ) " );
    sql.append( " ORDER BY topic_name " );

    return sql.toString();
  }

  /**
     * Save or update the forum topic to the database.
     * 
     * @param ForumTopic
     * @return ForumTopic
     */
  @Override
  public ForumTopic save( ForumTopic forumTopic )
  {
    getSession().saveOrUpdate( forumTopic );

    return forumTopic;
  }

  @Override
  public ForumTopicAudience save( ForumTopicAudience forumTopicAudience )
  {
    getSession().saveOrUpdate( forumTopicAudience );

    return forumTopicAudience;
  }

  /**
     * gets the list of topics that are eligible for this participant.
     * 
     * @param userId
     * @return ForumTopic
     */
  @SuppressWarnings( "unchecked" )
  @Override
  public List<ForumTopic> getSortedTopicList( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.SortedTopicList" );
    query.setLong( "userId", userId.longValue() );

    return query.list();
  }

  @Override
  public Long getDiscussionCountByTopic( Long topicId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.SortedTopicList" );
    return null;
  }

  @Override
  public List<ForumDiscussionDetailValueBean> getAllSortedTopicsForTile( Long userId )
  {
    String languageCode = UserManager.getLocale().toString();
    String sql = buildAllSortedTopicsForTileQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "userId", userId.longValue() );
    query.setParameter( "languageCode", languageCode );
    query.setResultTransformer( new ForumTopicsValueForTileBeanMapper() );
    return query.list();
  }

  private String buildAllSortedTopicsForTileQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT " );
    sql.append( "   (SELECT cms_value " );
    sql.append( "   FROM vw_cms_asset_value " );
    sql.append( "   WHERE locale = DECODE(locale,:languageCode,:languageCode, " );
    sql.append( "     (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language' " );
    sql.append( "     )) " );
    sql.append( "   AND asset_code = topic_name_asset_name " );
    sql.append( "   ) topic_Name, " );
    sql.append( "   forum_topic_id, " );
    sql.append( "   discussion_title, " );
    sql.append( "   discussion_id, " );
    sql.append( "   parent_discussion_id, " );
    sql.append( "   comment_discussion_body, " );
    sql.append( "   discussion_body, " );
    sql.append( "   NVL(like_count,0) like_count, " );
    sql.append( "   NVL(replies_count,0) replies_count, " );
    sql.append( "   CASE " );
    sql.append( "     WHEN like_count > 0 " );
    sql.append( "     THEN 1 " );
    sql.append( "     ELSE 0 " );
    sql.append( "   END isliked, " );
    sql.append( "   TO_CHAR(date_created, 'MM/DD/YYYY') date_created, " );
    sql.append( "   TO_CHAR(date_created, 'HH:MI:SS AM') time_created, " );
    sql.append( "   user_id, " );
    sql.append( "   first_name, " );
    sql.append( "   last_name, " );
    sql.append( "   avatarURL , " );
    sql.append( "   like_ids, " );
    sql.append( "   NVL( discussion_likes_count, 0 ) discussion_likes_count, " );
    sql.append( "   discussion_like_ids, " );
    sql.append( "   CASE " );
    sql.append( "     WHEN discussion_likes_count > 0 " );
    sql.append( "     THEN 1 " );
    sql.append( "     ELSE 0 " );
    sql.append( "   END is_discussion_liked, " );
    sql.append( "   discussion_creator, " );
    sql.append( "   discussion_creator_first_name, " );
    sql.append( "   discussion_creator_last_name, " );
    sql.append( "   discussion_creator_avatarurl, " );
    sql.append( "   TO_CHAR(discussion_date_created, 'MM/DD/YYYY') discussion_date_created, " );
    sql.append( "   TO_CHAR(discussion_date_created, 'HH:MI:SS AM') discussion_time_created " );
    sql.append( " FROM " );
    sql.append( "   ( WITH topic AS " );
    sql.append( "   (SELECT f.forum_topic_id, " );
    sql.append( "     f.topic_name_asset_name, " );
    sql.append( "     row_order " );
    sql.append( "   FROM " );
    sql.append( "     (SELECT ROWNUM row_order, " );
    sql.append( "       ft.* " );
    sql.append( "     FROM " );
    sql.append( "       (SELECT forum_topic_id, " );
    sql.append( "         topic_name_asset_name " );
    sql.append( "       FROM forum_topic " );
    sql.append( "       WHERE audience_type = 'all active participants' " );
    sql.append( "       AND status          = 'A' " );
    sql.append( "       OR forum_topic_id  IN " );
    sql.append( "         (SELECT fta.forum_topic_id " );
    sql.append( "         FROM forum_topic_audience fta, " );
    sql.append( "           participant_audience pa, " );
    sql.append( "           forum_topic ft " );
    sql.append( "         WHERE fta.audience_id  = pa.audience_id " );
    sql.append( "         AND pa.user_id         = :userId " );
    sql.append( "         AND fta.forum_topic_id = ft.forum_topic_id " );
    sql.append( "         AND status             = 'A' " );
    sql.append( "         ) " );
    sql.append( "       ORDER BY " );
    sql.append( "         CASE " );
    sql.append( "           WHEN sort_order IS NOT NULL " );
    sql.append( "           AND TRUNC(SYSDATE) BETWEEN sticky_start_date AND sticky_end_date " );
    sql.append( "           THEN 1 " );
    sql.append( "           WHEN sort_order IS NULL " );
    sql.append( "           AND TRUNC(SYSDATE) BETWEEN sticky_start_date AND sticky_end_date " );
    sql.append( "           THEN 2 " );
    sql.append( "           WHEN sort_order       IS NOT NULL " );
    sql.append( "           AND sticky_start_date IS NULL " );
    sql.append( "           AND sticky_end_date   IS NULL " );
    sql.append( "           THEN 3 " );
    sql.append( "           WHEN sort_order        IS NULL " );
    sql.append( "           AND sticky_start_date  IS NULL " );
    sql.append( "           AND sticky_end_date    IS NULL " );
    sql.append( "           AND last_activity_date IS NOT NULL " );
    sql.append( "           THEN 4 " );
    sql.append( "           ELSE NULL " );
    sql.append( "         END, " );
    sql.append( "         sort_order, " );
    sql.append( "         last_activity_date, " );
    sql.append( "         date_created " );
    sql.append( "       ) ft " );
    sql.append( "     ) f " );
    sql.append( "   WHERE row_order <= 5 " );
    sql.append( "   ), " );
    sql.append( "   rply_cmnt_dis AS " );
    sql.append( "   (SELECT forum_topic_id, " );
    sql.append( "     DECODE(parent_discussion_id,NULL,discussion_id,parent_discussion_id) parent_discussion_id, " );
    sql.append( "     discussion_title, " );
    sql.append( "     DECODE(parent_discussion_id,NULL,NULL,discussion_id) discussion_id, " );
    sql.append( "     discussion_body, " );
    sql.append( "     user_id, " );
    sql.append( "     date_created " );
    sql.append( "   FROM " );
    sql.append( "     (SELECT RANK () OVER (PARTITION BY fd.forum_topic_id ORDER BY fd.date_created DESC) AS rec_rank, " );
    sql.append( "       t1.forum_topic_id, " );
    sql.append( "       fd.parent_discussion_id, " );
    sql.append( "       fd.discussion_title, " );
    sql.append( "       fd.discussion_id, " );
    sql.append( "       fd.discussion_body, " );
    sql.append( "       fd.user_id, " );
    sql.append( "       fd.date_created " );
    sql.append( "     FROM ( " );
    sql.append( "       (SELECT f.forum_topic_id, " );
    sql.append( "         f.topic_name_asset_name, " );
    sql.append( "         row_order " );
    sql.append( "       FROM " );
    sql.append( "         (SELECT ROWNUM row_order, " );
    sql.append( "           ft.* " );
    sql.append( "         FROM " );
    sql.append( "           (SELECT forum_topic_id, " );
    sql.append( "             topic_name_asset_name " );
    sql.append( "           FROM forum_topic " );
    sql.append( "           WHERE audience_type = 'all active participants' " );
    sql.append( "           AND status          = 'A' " );
    sql.append( "           OR forum_topic_id  IN " );
    sql.append( "             (SELECT fta.forum_topic_id " );
    sql.append( "             FROM forum_topic_audience fta, " );
    sql.append( "               participant_audience pa " );
    sql.append( "             WHERE fta.audience_id = pa.audience_id " );
    sql.append( "             AND pa.user_id        = :userId " );
    sql.append( "             ) " );
    sql.append( "           ORDER BY " );
    sql.append( "             CASE " );
    sql.append( "               WHEN sort_order IS NOT NULL " );
    sql.append( "               AND TRUNC(SYSDATE) BETWEEN sticky_start_date AND sticky_end_date " );
    sql.append( "               THEN 1 " );
    sql.append( "               WHEN sort_order IS NULL " );
    sql.append( "               AND TRUNC(SYSDATE) BETWEEN sticky_start_date AND sticky_end_date " );
    sql.append( "               THEN 2 " );
    sql.append( "               WHEN sort_order       IS NOT NULL " );
    sql.append( "               AND sticky_start_date IS NULL " );
    sql.append( "               AND sticky_end_date   IS NULL " );
    sql.append( "               THEN 3 " );
    sql.append( "               WHEN sort_order        IS NULL " );
    sql.append( "               AND sticky_start_date  IS NULL " );
    sql.append( "               AND sticky_end_date    IS NULL " );
    sql.append( "               AND last_activity_date IS NOT NULL " );
    sql.append( "               THEN 4 " );
    sql.append( "               ELSE NULL " );
    sql.append( "             END, " );
    sql.append( "             sort_order, " );
    sql.append( "             last_activity_date, " );
    sql.append( "             date_created " );
    sql.append( "           ) ft " );
    sql.append( "         ) f " );
    sql.append( "       WHERE row_order <= 5 " );
    sql.append( "       )) t1, " );
    sql.append( "       forum_discussion fd " );
    sql.append( "     WHERE t1.forum_topic_id = fd.forum_topic_id(+) " );
    sql.append( "     AND fd.status           = 'A' " );
    sql.append( "     ) " );
    sql.append( "   WHERE rec_rank = 1 " );
    sql.append( "   ) , " );
    sql.append( "   discussion AS " );
    sql.append( "   (SELECT rcd.forum_topic_id, " );
    sql.append( "     rcd.discussion_title, " );
    sql.append( "     fd1.discussion_id, " );
    sql.append( "     fd1.parent_discussion_id, " );
    sql.append( "     fd1.discussion_body AS comment_discussion_body, " );
    sql.append( "     fd2.discussion_body, " );
    sql.append( "     (SELECT COUNT(discussion_id) " );
    sql.append( "     FROM forum_discussion_like " );
    sql.append( "     WHERE discussion_id =rcd.discussion_id " );
    sql.append( "     ) like_count, " );
    sql.append( "     (SELECT LISTAGG(user_id , ', ') WITHIN GROUP ( " );
    sql.append( "     ORDER BY discussion_id) " );
    sql.append( "     FROM forum_discussion_like " );
    sql.append( "     WHERE discussion_id = rcd.discussion_id " );
    sql.append( "     ) like_ids, " );
    sql.append( "     (SELECT COUNT(fd2.discussion_id) " );
    sql.append( "     FROM forum_discussion fd2 " );
    sql.append( "     WHERE fd2.parent_discussion_id = NVL(rcd.parent_discussion_id, rcd.discussion_id) " );
    sql.append( "     AND fd2.is_reply               = 1 " );
    sql.append( "     AND fd2.status                 = 'A' " );
    sql.append( "     ) replies_count, " );
    sql.append( "     fd1.date_created, " );
    sql.append( "     fd1.user_id, " );
    sql.append( "     au.first_name, " );
    sql.append( "     au.last_name, " );
    sql.append( "     p.avatar_small avatarURL " );
    sql.append( "   FROM rply_cmnt_dis rcd, " );
    sql.append( "     forum_discussion fd1, " );
    sql.append( "     forum_discussion fd2, " );
    sql.append( "     application_user au, " );
    sql.append( "     participant p " );
    sql.append( "   WHERE rcd.forum_topic_id     = fd1.forum_topic_id " );
    sql.append( "   AND fd1.user_id              = au.user_id " );
    sql.append( "   AND au.user_id               = p.user_id " );
    sql.append( "   AND fd1.discussion_id        = rcd.discussion_id " );
    sql.append( "   AND fd1.parent_discussion_id = fd2.discussion_id " );
    sql.append( "   AND fd1.status               = 'A' " );
    sql.append( "   ) , " );
    sql.append( "   discussion_creator AS " );
    sql.append( "   (SELECT rcd.forum_topic_id, " );
    sql.append( "     (SELECT COUNT(discussion_id) " );
    sql.append( "     FROM forum_discussion_like " );
    sql.append( "     WHERE discussion_id = rcd.parent_discussion_id " );
    sql.append( "     ) like_count, " );
    sql.append( "     (SELECT LISTAGG(user_id , ', ') WITHIN GROUP ( " );
    sql.append( "     ORDER BY discussion_id) " );
    sql.append( "     FROM forum_discussion_like " );
    sql.append( "     WHERE discussion_id = rcd.parent_discussion_id " );
    sql.append( "     ) like_ids, " );
    sql.append( "     fd1.date_created, " );
    sql.append( "     fd1.user_id, " );
    sql.append( "     fd1.discussion_body, " );
    sql.append( "     fd1.discussion_id, " );
    sql.append( "     fd1.discussion_title, " );
    sql.append( "     au.first_name, " );
    sql.append( "     au.last_name, " );
    sql.append( "     p.avatar_small avatarURL " );
    sql.append( "   FROM rply_cmnt_dis rcd, " );
    sql.append( "     forum_discussion fd1, " );
    sql.append( "     application_user au, " );
    sql.append( "     participant p " );
    sql.append( "   WHERE rcd.forum_topic_id = fd1.forum_topic_id " );
    sql.append( "   AND fd1.user_id          = au.user_id " );
    sql.append( "   AND au.user_id           = p.user_id " );
    sql.append( "   AND fd1.discussion_id    = NVL(rcd.parent_discussion_id,rcd.discussion_id) " );
    sql.append( "   AND fd1.status           = 'A' " );
    sql.append( "   ) " );
    sql.append( " SELECT t2.topic_name_asset_name, " );
    sql.append( "   t2.forum_topic_id, " );
    sql.append( "   dc.discussion_title, " );
    sql.append( "   dc.discussion_id parent_discussion_id, " );
    sql.append( "   d.discussion_id, " );
    sql.append( "   d.comment_discussion_body, " );
    sql.append( "   dc.discussion_body, " );
    sql.append( "   d.like_count, " );
    sql.append( "   d.replies_count, " );
    sql.append( "   d.date_created, " );
    sql.append( "   d.user_id, " );
    sql.append( "   d.first_name, " );
    sql.append( "   d.last_name, " );
    sql.append( "   d.avatarURL, " );
    sql.append( "   t2.row_order, " );
    sql.append( "   d.like_ids, " );
    sql.append( "   dc.like_count discussion_likes_count, " );
    sql.append( "   dc.like_ids discussion_like_ids, " );
    sql.append( "   dc.user_id discussion_creator, " );
    sql.append( "   dc.first_name discussion_creator_first_name, " );
    sql.append( "   dc.last_name discussion_creator_last_name, " );
    sql.append( "   dc.avatarurl discussion_creator_avatarurl, " );
    sql.append( "   dc.date_created discussion_date_created " );
    sql.append( " FROM topic t2, " );
    sql.append( "   discussion d, " );
    sql.append( "   discussion_creator dc " );
    sql.append( " WHERE t2.forum_topic_id = d.forum_topic_id (+) " );
    sql.append( " AND t2.forum_topic_id   = dc.forum_topic_id(+) " );
    sql.append( " ORDER BY " );
    sql.append( "   CASE " );
    sql.append( "     WHEN date_created IS NULL " );
    sql.append( "     THEN discussion_date_created " );
    sql.append( "     ELSE date_created " );
    sql.append( "   END DESC " );
    sql.append( "   ) " );

    return sql.toString();
  }

  @SuppressWarnings( "serial" )
  private class ForumTopicsValueForTileBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      ForumDiscussionDetailValueBean reportValue = new ForumDiscussionDetailValueBean();

      reportValue.setTopicName( extractString( tuple[0] ) );
      reportValue.setTopicId( extractLong( tuple[1] ) );
      reportValue.setDiscussionName( extractString( tuple[2] ) );
      reportValue.setCommentId( extractLong( tuple[3] ) );
      reportValue.setDiscussionId( extractLong( tuple[4] ) );
      reportValue.setCommentBody( extractString( tuple[5] ) );
      reportValue.setDiscussionBody( extractString( tuple[6] ) );
      reportValue.setCommentNumOfLikers( extractLong( tuple[7] ) );
      reportValue.setNumberOfReplies( extractInt( tuple[8] ) );
      reportValue.setCommentIsLiked( extractBoolean( tuple[9] ) );
      reportValue.setCommentedDate( extractString( tuple[10] ) );
      reportValue.setCommentedTime( extractString( tuple[11] ) );
      reportValue.setCommenterId( extractLong( tuple[12] ) );
      reportValue.setCommenterFirstName( extractString( tuple[13] ) );
      reportValue.setCommenterLastName( extractString( tuple[14] ) );
      reportValue.setCommenterAvatarUrl( extractString( tuple[15] ) );
      reportValue.setCommenterLikedIds( extractString( tuple[16] ) );
      reportValue.setNumberOfLikes( extractLong( tuple[17] ) );
      reportValue.setDiscussionLikedIds( extractString( tuple[18] ) );
      reportValue.setDiscussionIsLiked( extractBoolean( tuple[19] ) );
      reportValue.setUserId( extractLong( tuple[20] ) );
      reportValue.setFirstName( extractString( tuple[21] ) );
      reportValue.setLastName( extractString( tuple[22] ) );
      reportValue.setAvatarUrl( extractString( tuple[23] ) );
      reportValue.setCreatedDate( extractString( tuple[24] ) );
      reportValue.setCreatedTime( extractString( tuple[25] ) );

      return reportValue;
    }
  }

  @Override
  public int getAllActivePaxTypeCountInForumTopics()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getAllActivePaxTypeCountInForumTopics" );
    return (Integer)query.uniqueResult();
  }

  @Override
  public int getPaxExistsCountInAudienceListOfForumTopics( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getPaxExistsCountInAudienceListOfForumTopics" );
    query.setParameter( "userId", userId );
    return (Integer)query.uniqueResult();
  }

  @Override
  public List getAllTopicNames()
  {
    String languageCode = UserManager.getLocale().toString();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.forum.getForumTopicnames" );
    query.setParameter( "languageCode", languageCode );
    return query.list();
  }

  /*
   * @Override public ForumTopic getForumTopicByName( String topicNameFromCM ) {
   * (ForumTopic)getSession().get( ForumTopic.class, topicId ); }
   */
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

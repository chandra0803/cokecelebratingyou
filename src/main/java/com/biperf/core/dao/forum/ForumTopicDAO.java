/**
 * 
 */

package com.biperf.core.dao.forum;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.forum.ForumTopicAudience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.value.forum.ForumAudienceFormBean;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;
import com.biperf.core.value.forum.ForumTopicValueBean;

/**
 * @author poddutur
 *
 */
public interface ForumTopicDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "forumTopicDAO";

  /**
     * Deletes a list of forum topics.
     * 
     * @throws ServiceErrorException
     */
  public void deleteTopic( ForumTopic topicToDelete );

  /**
     * Gets the forum topic from the database by the id param.
     * 
     * @param topicId
     * @return ForumTopic
     */
  public ForumTopic getTopicById( Long topicId );

  /**
     * Get all forum topics from the database.
     * 
     * @return List
     */
  public List getAll();

  /**
     * Save or update the forum topic to the database.
     * 
     * @param ForumTopic
     * @return ForumTopic
     */
  public ForumTopic save( ForumTopic forumTopic );

  public ForumTopicAudience save( ForumTopicAudience forumTopicAudience );

  /**
     * gets the list of topics that are eligible for this participant.
     * 
     * @param userId
     * @return ForumTopic
     */
  public List<ForumTopic> getSortedTopicList( Long userId );

  /**
     * gets the list of topics that are to be sorted.
     * 
     * @return ForumTopic
     */
  public List<ForumTopicValueBean> getAllSortedTopicList();

  /**
   * gets the number of discussions for each topic.
   * 
   * @return discussionCount
   */
  public Long getDiscussionCountByTopic( Long topicId );

  /**
   * @param userId
   * @return
   * @throws ServiceErrorException
   */
  public List<ForumDiscussionDetailValueBean> getAllSortedTopicsForTile( Long userId );

  /**
   * gets the list of topics that are to be sorted by pax.
   * 
   * @return ForumTopic
   */
  public List<ForumTopicValueBean> getAllSortedTopicListByPax( Long userId );

  public List<ForumTopicValueBean> getAllTopicNamesForPax();

  public List<ForumAudienceFormBean> getAudienceByTopicId( Long topicId );

  public int getAllActivePaxTypeCountInForumTopics();

  /**
   * Check the topic title name already exists.
   * 
   * @param new topic name
   * 
   */
  public List<String> getAllTopicNames();

  public ForumTopicAudience getForumTopicAudienceById( Long audienceId, Long topicId );

  public void deleteTopicAudience( ForumTopicAudience topicAudienceToDelete );

  public int getPaxExistsCountInAudienceListOfForumTopics( Long userId );

}

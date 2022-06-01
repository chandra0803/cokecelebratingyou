
package com.biperf.core.service.forum;

import java.util.List;

import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.forum.ForumTopicAudience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.forum.ForumAudienceFormBean;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;
import com.biperf.core.value.forum.ForumTopicValueBean;

public interface ForumTopicService extends SAO
{
  /**
    * BEAN_NAME is for applicationContext.xml reference
    */
  public static String BEAN_NAME = "forumTopicService";

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
   * Gets the forum topic from the database by the id param.
   * 
   * @param id
   * @return ForumTopic
   */
  public ForumTopic getTopicById( Long id );

  /**
   * Deletes a list of forum topics.
   * 
   * @param forumTopicIdList - List of forumTopic.id
   */
  public void deleteTopics( List forumTopicIdList ) throws ServiceErrorException;

  /**
   * Delete a forum topic.
   * 
   * @param topicId
   * @throws ServiceErrorException
   */
  public void deleteTopic( Long topicId ) throws ServiceErrorException;

  /**
   * gets the sorted list of topics that are eligible for this participant.
   * 
   * @param userId, count
   * @return ForumTopic
   */
  public List<ForumTopic> getSortedTopicList( Long userId, int count );

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
   * saves the cmAssetCode for a entered topic Name.
   * 
   * @return string
   */
  public ForumTopic saveTopicCmText( ForumTopic forumTopic, String topicCmAssetCode ) throws ServiceErrorException;

  /**
   * @param userId
   * @return
   * @throws ServiceErrorException
   */
  public List<ForumDiscussionDetailValueBean> getForumTopicsForTile( Long userId );

  public List<ForumTopicValueBean> getAllSortedTopicListByPax( Long userId );

  public List<ForumTopicValueBean> getAllTopicNamesForPax();

  public List<ForumAudienceFormBean> getAudienceByTopicId( Long topicId );

  public boolean isAllActivePaxTypeInForumTopics();

  /**
   * Check the Topic Title name already exists.
   * 
   * @param new topic name
   * 
   */
  public boolean isTopicNameExists( String topicName ) throws ServiceErrorException;

  public void deleteTopicAudience( Long audienceId, Long topicId );

  public boolean isPaxExistsInAudienceListOfForumTopics( Long userId );

}

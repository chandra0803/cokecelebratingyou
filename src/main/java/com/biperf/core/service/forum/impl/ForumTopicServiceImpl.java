/**
 * 
 */

package com.biperf.core.service.forum.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.biperf.core.dao.forum.ForumTopicDAO;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.forum.ForumTopicAudience;
import com.biperf.core.domain.forum.TopicNamesSortComparator;
import com.biperf.core.domain.forum.TopicSortComparator;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.forum.ForumTopicService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.forum.ForumAudienceFormBean;
import com.biperf.core.value.forum.ForumDiscussionDetailValueBean;
import com.biperf.core.value.forum.ForumTopicValueBean;
import com.objectpartners.cms.domain.enums.DataTypeEnum;

/**
 * @author poddutur
 * 
 */
public class ForumTopicServiceImpl implements ForumTopicService
{
  private ForumTopicDAO forumTopicDAO;
  private CMAssetService cmAssetService = null;
  private SystemVariableService systemVariableService;

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  @Override
  public void deleteTopics( List forumTopicIdList ) throws ServiceErrorException
  {
    Iterator idIter = forumTopicIdList.iterator();

    while ( idIter.hasNext() )
    {
      this.deleteTopic( (Long)idIter.next() );
    }
  }

  public void deleteTopic( Long topicId )
  {
    ForumTopic topicToDelete = forumTopicDAO.getTopicById( topicId );

    if ( topicToDelete != null )
    {
      forumTopicDAO.deleteTopic( topicToDelete );
    }
  }

  public void deleteTopicAudience( Long audienceId, Long topicId )
  {
    ForumTopicAudience topicAudienceToDelete = forumTopicDAO.getForumTopicAudienceById( audienceId, topicId );

    if ( topicAudienceToDelete != null )
    {
      forumTopicDAO.deleteTopicAudience( topicAudienceToDelete );
    }
  }

  @Override
  public List getAll()
  {
    return forumTopicDAO.getAll();
  }

  @Override
  public ForumTopic save( ForumTopic forumTopic )
  {
    return forumTopicDAO.save( forumTopic );
  }

  @Override
  public ForumTopicAudience save( ForumTopicAudience forumTopicAudience )
  {
    return forumTopicDAO.save( forumTopicAudience );
  }

  @Override
  public ForumTopic getTopicById( Long topicId )
  {
    return forumTopicDAO.getTopicById( topicId );
  }

  @Override
  public List<ForumAudienceFormBean> getAudienceByTopicId( Long topicId )
  {
    return forumTopicDAO.getAudienceByTopicId( topicId );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ForumTopic> getSortedTopicList( Long userId, int count )
  {
    List sortedTopicList = new ArrayList();
    List<ForumTopic> forumTopicList = forumTopicDAO.getSortedTopicList( userId );

    sortedTopicList.addAll( forumTopicList );

    Collections.sort( sortedTopicList, new TopicSortComparator() );

    if ( count > 0 )
    {
      sortedTopicList.subList( 0, count );
    }

    return sortedTopicList;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<ForumTopicValueBean> getAllSortedTopicList()
  {
    List<ForumTopicValueBean> sortedTopicList = new ArrayList<ForumTopicValueBean>();
    List<ForumTopicValueBean> forumTopicList = forumTopicDAO.getAllSortedTopicList();

    sortedTopicList.addAll( forumTopicList );

    Collections.sort( sortedTopicList, new TopicSortComparator() );

    return sortedTopicList;
  }

  @Override
  public Long getDiscussionCountByTopic( Long topicId )
  {
    Long discussionCount = forumTopicDAO.getDiscussionCountByTopic( topicId );
    return discussionCount;
  }

  @Override
  public ForumTopic saveTopicCmText( ForumTopic forumTopic, String topicName ) throws ServiceErrorException
  {
    try
    {
      if ( forumTopic.getTopicscmAsset() == null )
      {
        // Create and set asset to forum topic
        String newAssetName = cmAssetService.getUniqueAssetCode( ForumTopic.FORUMTOPIC_NAME_CMASSET_PREFIX );
        forumTopic.setTopicscmAsset( newAssetName );
      }

      CMDataElement cmDataElement = new CMDataElement( ForumTopic.FORUMTOPIC_NAME_CMASSET_NAME, ForumTopic.FORUMTOPIC_NAME_CMASSET_TYPE_KEY, topicName, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      cmAssetService.createOrUpdateAsset( ForumTopic.FORUMTOPIC_NAME_SECTION_CODE,
                                          ForumTopic.FORUMTOPIC_NAME_CMASSET_TYPE_NAME,
                                          ForumTopic.FORUMTOPIC_NAME_CMASSET_NAME,
                                          forumTopic.getTopicscmAsset(),
                                          elements,
                                          UserManager.getLocale(),
                                          null );
      if ( !systemVariableService.getDefaultLanguage().getStringVal().equals( UserManager.getLocale().toString() ) )
      {
        cmAssetService.createOrUpdateAsset( ForumTopic.FORUMTOPIC_NAME_SECTION_CODE,
                                            ForumTopic.FORUMTOPIC_NAME_CMASSET_TYPE_NAME,
                                            ForumTopic.FORUMTOPIC_NAME_CMASSET_NAME,
                                            forumTopic.getTopicscmAsset(),
                                            elements,
                                            Locale.US,
                                            null );
      }
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return forumTopic;
  }

  @Override
  public List<ForumDiscussionDetailValueBean> getForumTopicsForTile( Long userId )
  {
    List<ForumDiscussionDetailValueBean> forumTopicsTileList = forumTopicDAO.getAllSortedTopicsForTile( userId );

    return forumTopicsTileList;
  }

  @Override
  public List<ForumTopicValueBean> getAllSortedTopicListByPax( Long userId )
  {
    List<ForumTopicValueBean> sortedTopicList = new ArrayList<ForumTopicValueBean>();
    List<ForumTopicValueBean> forumTopicList = forumTopicDAO.getAllSortedTopicListByPax( userId );

    sortedTopicList.addAll( forumTopicList );

    Collections.sort( sortedTopicList, new TopicSortComparator() );

    return sortedTopicList;
  }

  @Override
  public boolean isAllActivePaxTypeInForumTopics()
  {
    int allActivePaxCnt = forumTopicDAO.getAllActivePaxTypeCountInForumTopics();
    boolean allActivePax = allActivePaxCnt > 0 ? true : false;

    return allActivePax;
  }

  @Override
  public boolean isTopicNameExists( String topicName ) throws ServiceErrorException
  {
    boolean isTopicNameExists = false;
    List topicNames = this.forumTopicDAO.getAllTopicNames();
    if ( topicNames.contains( topicName ) )
    {
      isTopicNameExists = true;
    }
    return isTopicNameExists;
  }

  @Override
  public boolean isPaxExistsInAudienceListOfForumTopics( Long userId )
  {
    int count = this.forumTopicDAO.getPaxExistsCountInAudienceListOfForumTopics( userId );

    boolean inAudience = count > 0 ? true : false;

    return inAudience;
  }

  @Override
  public List<ForumTopicValueBean> getAllTopicNamesForPax()
  {
    List<ForumTopicValueBean> topicsList = new ArrayList<ForumTopicValueBean>();
    topicsList = forumTopicDAO.getAllTopicNamesForPax();

    Collections.sort( topicsList, new TopicNamesSortComparator() );

    return topicsList;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setForumTopicDAO( ForumTopicDAO forumTopicDAO )
  {
    this.forumTopicDAO = forumTopicDAO;
  }

}

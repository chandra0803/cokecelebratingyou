/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/impl/ListBuilderServiceImpl.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.dao.participant.ListBuilderDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.value.FormattedValueBean;

/**
 * ListBuilderServiceImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Sathish</td>
 * <td>June 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ListBuilderServiceImpl implements ListBuilderService
{

  /** ListBuilderDAO */
  protected ListBuilderDAO listBuilderDAO;

  /** AudienceDAO */
  private AudienceDAO audienceDAO;

  /** HierarchyService */
  private HierarchyService hierarchyService;

  /** NodeDAO */
  private NodeDAO nodeDAO;

  /** ParticipantDAO */
  private ParticipantDAO participantDAO;

  /**
   * Overridden from
   * 
   * @param criteriaAudience
   * @param hierarchyId
   * @param produceUserIdOnly
   * @param filterAudiences
   * @param loadUsers
   * @return List
   */
  public List searchParticipants( CriteriaAudience criteriaAudience, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean loadUsers, boolean preCalculated )
  {

    List userIdList = listBuilderDAO.searchParticipants( criteriaAudience, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated, false );

    if ( loadUsers )
    {

      List userList = new ArrayList();
      for ( Iterator userIdListIterator = userIdList.iterator(); userIdListIterator.hasNext(); )
      {
        FormattedValueBean fvb = (FormattedValueBean)userIdListIterator.next();
        userList.add( this.participantDAO.getParticipantById( fvb.getId() ) );
      }

      return userList;
    }

    return userIdList;

  }

  public List getCriteriaAudienceParticipants( Long audienceId, boolean loadUsers )
  {

    List userIdList = listBuilderDAO.getParticipantsUserIdListByAudienceId( audienceId );

    if ( loadUsers )
    {

      List userList = new ArrayList();
      for ( Iterator userIdListIterator = userIdList.iterator(); userIdListIterator.hasNext(); )
      {
        BigDecimal userId = (BigDecimal)userIdListIterator.next();
        userList.add( this.participantDAO.getParticipantById( userId.longValue() ) );
      }

      return userList;
    }

    return userIdList;

  }

  /**
   * Search the participants for the audienceCriteria and hierarchyId params. Overridden from
   * 
   * @see com.biperf.core.service.participant.ListBuilderService#searchParticipants(com.biperf.core.domain.participant.AudienceCriteria,
   *      java.lang.Long)
   * @param audienceCriteria
   * @param hierarchyId
   * @return List
   */
  public List searchParticipants( AudienceCriteria audienceCriteria, Long hierarchyId, boolean preCalculated )
  {
    return listBuilderDAO.searchParticipants( audienceCriteria, hierarchyId, preCalculated );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.participant.ListBuilderService#searchParticipants(java.util.Set,
   *      java.lang.Long, boolean, java.util.Set)
   * @param audiences
   * @param hierarchyId
   * @param produceUserIdOnly
   * @param filterAudiences
   * @return List
   */
  public List searchParticipants( Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated )
  {
    return listBuilderDAO.searchParticipants( audiences, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.participant.ListBuilderService#searchParticipants(com.biperf.core.domain.participant.AudienceCriteria,
   *      java.lang.Long, boolean, java.util.Set)
   * @param audienceCriteria
   * @param hierarchyId
   * @param produceUserIdOnly
   * @param filterAudiences
   * @return List
   */
  public List searchParticipants( AudienceCriteria audienceCriteria, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated, boolean isStandardClientAdmin )
  {
    return listBuilderDAO.searchParticipants( audienceCriteria, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated, isStandardClientAdmin );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.participant.ListBuilderService#searchParticipants(com.biperf.core.domain.participant.AudienceCriteria,
   *      java.lang.Long, boolean, java.util.Set)
   * @param parentNodeId
   * @param includeDescendantNodes
   * @param audiences
   * @param hierarchyId
   * @param produceUserIdOnly
   * @param filterAudiences
   * @return List
   */
  public List searchParticipants( Long parentNodeId, boolean includeDescendantNodes, Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated )
  {
    if ( parentNodeId == null )
    {
      return searchParticipants( audiences, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated );
    }

    Node parentNode = this.nodeDAO.getNodeById( parentNodeId );

    addNodeAndDescendants( parentNode, filterAudiences, includeDescendantNodes );

    return listBuilderDAO.searchParticipants( audiences, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated );
  }

  /**
   * Recurses down through the <code>unit</code>s adding all child units to
   * <code>collection</code> (unless they are already present).
   */
  private void addNodeAndDescendants( Node node, Collection filterAudiences, boolean includeDescendantNodes )
  {
    if ( filterAudiences.contains( node ) )
    {
      return;
    }

    CriteriaAudience criteriaAudience = new CriteriaAudience();
    AudienceCriteria currentAudienceCriteria = new AudienceCriteria();

    currentAudienceCriteria.setNodeId( node.getId() );
    criteriaAudience.addAudienceCriteria( currentAudienceCriteria );
    filterAudiences.add( criteriaAudience );

    if ( includeDescendantNodes )
    {

      List children = this.nodeDAO.getChildNodesByParent( node.getId() );

      for ( Iterator childIter = children.iterator(); childIter.hasNext(); )
      {
        Node childNode = (Node)childIter.next();
        addNodeAndDescendants( childNode, filterAudiences, includeDescendantNodes );
      }
    }
  }

  /**
   * @param listBuilderDAO value for listBuilderDAO property
   */
  public void setListBuilderDAO( ListBuilderDAO listBuilderDAO )
  {
    this.listBuilderDAO = listBuilderDAO;
  }

  /**
   * @param hierarchyService
   */
  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  /**
   * @param audienceDAO value for audienceDAO property
   */
  public void setAudienceDAO( AudienceDAO audienceDAO )
  {
    this.audienceDAO = audienceDAO;
  }

  /**
   * @param participantDAO
   */
  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  /**
   * @param nodeDAO value for audienceDAO property
   */
  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  /**
   * Gets the size of the audience associated to the audienceId param.
   * 
   * @param audienceId
   * @return int
   */
  public Audience getAudienceWithSize( Long audienceId, boolean preCalculated )
  {

    Audience audience = this.audienceDAO.getAudienceById( audienceId );

    if ( audience instanceof CriteriaAudience )
    {

      Long hierarchyId = this.hierarchyService.getPrimaryHierarchy().getId();

      CriteriaAudience criteriaAudience = (CriteriaAudience)this.audienceDAO.getAudienceById( audienceId );

      criteriaAudience.setSize( this.listBuilderDAO.searchParticipants( criteriaAudience, hierarchyId, true, null, preCalculated, false ).size() );

    }

    return audience;
  }

  @Override
  public List getParticipantsListByAudienceId( Long audienceId )
  {
    return listBuilderDAO.getParticipantsListByAudienceId( audienceId );
  }

  public List searchParticipantsInactivePaxIncluded( Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated )
  {
    return listBuilderDAO.searchParticipantsInactivePaxIncluded( audiences, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated );
  }

  public List rosterSearch( AudienceCriteria audienceCriteria,
                            Long hierarchyId,
                            boolean produceUserIdOnly,
                            Set filterAudiences,
                            boolean preCalculated,
                            boolean isStandardClientAdmin,
                            boolean emailSearch,
                            String sortedOn,
                            String sortedBy,
                            int pageNbr,
                            int pageSize )
  {
    return listBuilderDAO.rosterSearch( audienceCriteria, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated, isStandardClientAdmin, emailSearch, sortedOn, sortedBy, pageNbr, pageSize );
  }
}

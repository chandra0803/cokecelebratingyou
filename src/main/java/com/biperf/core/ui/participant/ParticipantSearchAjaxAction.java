
package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeSearchCriteria;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;

/**
 * ParticipantSearchAjaxAction implements participant searches invoked through ajax calls. Results
 * are returned to the browser in XML format. This class can be used as-is, however it's common to
 * use derived classes that override one or more protected methods.
 * 
 *
 */
public class ParticipantSearchAjaxAction extends BaseDispatchAction
{
  protected static final String FORWARD_SUCCESS = ActionConstants.SUCCESS_FORWARD;
  protected static final String FORWARD_SUCCESS_NODES = "success_nodes";
  protected static final String FORWARD_SUCCESS_PICKLIST_ITEMS = "success_picklist_items";

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = null;
    String searchBy = request.getParameter( "criteria" );
    String query = request.getParameter( "query" );
    request.setAttribute( "criteria", searchBy );
    request.setAttribute( "query", query );
    boolean secondLevel = Boolean.valueOf( request.getParameter( "secondLevel" ) ).booleanValue();
    request.setAttribute( "secondLevel", new Boolean( secondLevel ) );

    if ( "lastName".equals( searchBy ) )
    {
      ParticipantSearchCriteria criteria = new ParticipantSearchCriteria();
      criteria.setLastName( query );
      forward = doParticipantSearch( criteria, request );
    }

    if ( "firstName".equals( searchBy ) )
    {
      ParticipantSearchCriteria criteria = new ParticipantSearchCriteria();
      criteria.setFirstName( query );
      forward = doParticipantSearch( criteria, request );
    }

    if ( "location".equals( searchBy ) )
    {
      forward = doLocationSearch( query, secondLevel, request );
    }

    if ( "jobTitle".equals( searchBy ) )
    {
      forward = doJobPositionSearch( query, secondLevel, request );
    }

    if ( "department".equals( searchBy ) )
    {
      forward = doDepartmentSearch( query, secondLevel, request );
    }

    return forward != null ? mapping.findForward( forward ) : null;
  }

  //
  // Search by department
  //

  protected String doDepartmentSearch( String query, boolean isSecondLevelSearch, HttpServletRequest request )
  {
    if ( isSecondLevelSearch )
    {
      ParticipantSearchCriteria criteria = new ParticipantSearchCriteria();
      criteria.setDepartment( query );
      return doParticipantSearch( criteria, request );
    }
    else
    {
      List departments = DepartmentType.getList();
      departments = filterPickList( query, departments );
      request.setAttribute( "pickListItems", departments );
      return FORWARD_SUCCESS_PICKLIST_ITEMS;
    }
  }

  //
  // Search by location
  //

  /**
   * @param query
   * @param isSecondLevelSearch
   * @param request
   * @return
   */
  protected String doLocationSearch( String query, boolean isSecondLevelSearch, HttpServletRequest request )
  {
    return doLocationSearch( query, null, isSecondLevelSearch, request );
  }

  /**
   * @param query
   * @param nodeSearchCriteria
   * @param isSecondLevelSearch
   * @param request
   * @return
   */
  protected String doLocationSearch( String query, NodeSearchCriteria nodeSearchCriteria, boolean isSecondLevelSearch, HttpServletRequest request )
  {
    if ( isSecondLevelSearch )
    {
      Long nodeId = new Long( query );
      ParticipantSearchCriteria criteria = new ParticipantSearchCriteria();
      request.setAttribute( "selectedNode", nodeId );
      return doParticipantSearch( criteria, request, nodeId, true );
    }
    else
    {
      List nodes = getNodeList( query, nodeSearchCriteria, request );
      request.setAttribute( "nodes", nodes );
      return FORWARD_SUCCESS_NODES;
    }
  }

  /**
   * @param query
   * @param criteria
   * @param request
   * @return
   */
  protected List getNodeList( String query, NodeSearchCriteria criteria, HttpServletRequest request )
  {
    if ( criteria == null )
    {
      criteria = new NodeSearchCriteria();
    }

    // name filter
    criteria.setNodeName( query.trim() );

    // node filter
    if ( applyMainUserNodeFilter( request ) )
    {
      // simple node scope filter (for e.g. Add Proxy)
      criteria.setNodeId( getMainUserNode( request ) );
      criteria.setNodeIdAndBelow( true );
    }
    else
    {
      Promotion promotion = getPromotion( request );
      if ( promotion != null && promotion.getSecondaryAudienceType() != null )
      {
        // promotion secondary audience filter (pre-query)

        if ( promotion.getSecondaryAudienceType().isSpecificNodeType() )
        {
          criteria.setNodeId( getMainUserNode( request ) );
          criteria.setNodeIdAndBelow( true );
        }

        if ( promotion.getSecondaryAudienceType().isSpecificNodeAndBelowType() )
        {
          criteria.setNodeId( getMainUserNode( request ) );
          criteria.setNodeIdAndBelow( true );
        }
      }
    }

    // search
    List nodes = getNodeService().searchNode( criteria );

    // filter
    return filterNodes( nodes, request );
  }

  private boolean isNodeBasedCriteria( AudienceCriteria audienceCriteria )
  {
    return !StringUtils.isEmpty( audienceCriteria.getNodeName() ) || null != audienceCriteria.getNodeId() || null != audienceCriteria.getNodeTypeId() || null != audienceCriteria.getNodeRole()
        || !audienceCriteria.getNodeTypeCharacteristicCriterias().isEmpty();
  }

  protected List filterNodes( List nodes, HttpServletRequest request )
  {
    Promotion promotion = getPromotion( request );
    if ( promotion != null && promotion.getSecondaryAudienceType() != null )
    {
      // promotion secondary audience filter (post-query)

      if ( promotion.getSecondaryAudienceType().isSpecifyAudienceType() )
      {
        nodes = getAudienceService().filterNodesByAudiences( nodes, promotion.getSecondaryAudiences() );
      }

      if ( promotion.getSecondaryAudienceType().isSameAsPrimaryType() && promotion.getPrimaryAudienceType() != null && promotion.getPrimaryAudienceType().isSpecifyAudienceType() )
      {
        nodes = getAudienceService().filterNodesByAudiences( nodes, promotion.getPrimaryAudiences() );
      }
    }

    return nodes;
  }

  //
  // Search by job position
  //

  protected String doJobPositionSearch( String query, boolean isSecondLevelSearch, HttpServletRequest request )
  {
    if ( isSecondLevelSearch )
    {
      ParticipantSearchCriteria criteria = new ParticipantSearchCriteria();
      criteria.setJobPosition( query );
      return doParticipantSearch( criteria, request );
    }
    else
    {
      List jobPositions = PositionType.getList();
      jobPositions = filterPickList( query, jobPositions );
      request.setAttribute( "pickListItems", jobPositions );
      return FORWARD_SUCCESS_PICKLIST_ITEMS;
    }
  }

  //
  // Search by participant
  //

  protected String doParticipantSearch( ParticipantSearchCriteria criteria, HttpServletRequest request )
  {
    Long nodeId = null;
    boolean recursively = false;

    // node filter
    if ( applyMainUserNodeFilter( request ) )
    {
      // simple node scope filter (for e.g. Add Proxy)
      nodeId = getMainUserNode( request );
      recursively = true;
    }
    else
    {
      Promotion promotion = getPromotion( request );
      if ( promotion != null && promotion.getSecondaryAudienceType() != null )
      {
        // promotion secondary audience filter (pre-query)

        if ( promotion.getSecondaryAudienceType().isSpecificNodeType() )
        {
          nodeId = getMainUserNode( request );
          recursively = false;
        }

        if ( promotion.getSecondaryAudienceType().isSpecificNodeAndBelowType() )
        {
          nodeId = getMainUserNode( request );
          recursively = true;
        }
      }
    }

    return doParticipantSearch( criteria, request, nodeId, recursively );
  }

  protected String doParticipantSearch( ParticipantSearchCriteria criteria, HttpServletRequest request, Long nodeId, boolean recursively )
  {
    // node filter
    criteria.setNodeId( nodeId );
    criteria.setNodeIdAndBelow( recursively );

    // sort order
    criteria.setSortByLastNameFirstName( true );

    // search
    List paxes = null;
    AssociationRequestCollection associations = getPaxAssociations();

    Boolean countryRequired = Boolean.valueOf( request.getParameter( "countryRequired" ) );
    Boolean emailRequired = new Boolean( false );
    if ( request.getParameter( "emailRequired" ) != null )
    {
      emailRequired = Boolean.valueOf( request.getParameter( "emailRequired" ) );
    }
    if ( countryRequired.booleanValue() )
    {
      if ( associations == null )
      {
        associations = new AssociationRequestCollection();
      }
      associations.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
    }

    if ( emailRequired.booleanValue() )
    {
      if ( associations == null )
      {
        associations = new AssociationRequestCollection();
      }
      associations.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    }

    if ( associations != null )
    {
      paxes = getParticipantService().searchParticipantWithAssociations( criteria, true, associations );
    }
    else
    {
      paxes = getParticipantService().searchParticipant( criteria, true );
    }

    // filter
    filterUsers( paxes, request );

    // done
    request.setAttribute( "countryRequired", countryRequired );
    request.setAttribute( "paxes", paxes );
    return FORWARD_SUCCESS;
  }

  protected List filterUsers( List participants, HttpServletRequest request )
  {
    Promotion promotion = getPromotion( request );
    Long userId = getUserId( request );
    Long proxyUserId = getProxyUserId( request );
    // load main user node
    Node mainUserNode = null;
    if ( promotion != null )
    {
      mainUserNode = getNodeService().getNodeById( getMainUserNode( request ) );
    }
    // filter paxes
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      Participant pax = (Participant)paxIter.next();
      if ( mustRemove( pax, userId, proxyUserId, mainUserNode, promotion, request ) )
      {
        paxIter.remove();
      }
    }
    // filter by node
    if ( shouldFilterNodes( promotion ) )
    {
      participants = filterByNode( participants, promotion, mainUserNode );
    }
    else
    {
      List nodes = getNodeList( "", null, request );
      // filter participants that are in removed nodes
      for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
      {
        User currentPax = (User)paxIter.next();
        for ( Iterator userNodeIter = currentPax.getUserNodes().iterator(); userNodeIter.hasNext(); )
        {
          UserNode userNode = (UserNode)userNodeIter.next();
          if ( !nodes.contains( userNode.getNode() ) )
          {
            userNodeIter.remove();
          }
        }
      }
    }

    if ( promotion != null && promotion.getSecondaryAudiences() != null && !promotion.getSecondaryAudiences().isEmpty() )
    {
      participants = getAudienceService().filterParticipantNodesByAudienceNodeRole( participants, promotion.getSecondaryAudiences() );
    }
    return participants;
  }

  protected boolean mustRemove( Participant pax, Long userId, Long proxyUserId, Node mainUserNode, Promotion promotion, HttpServletRequest request )
  {
    // remove self
    if ( promotion == null || excludeSelf( request ) )
    {
      if ( userId != null && userId.equals( pax.getId() ) )
      {
        return true;
      }
      if ( proxyUserId != null && proxyUserId.equals( pax.getId() ) )
      {
        return true;
      }
    }

    // remove if not valid for this audience
    if ( promotion != null )
    {
      boolean isSubmitter = false;
      if ( request.getSession().getAttribute( "paxIds" ) == null )
      {
        if ( promotion.getSecondaryAudienceType() == null || promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
            || promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) )
                && promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
            || promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) )
            || promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) ) )
        {
          return false;
        }
        getEligibleParticipantsForPromotion( promotion, request );
        List paxIds = (List)request.getSession().getAttribute( "paxIds" );
        return !paxIds.contains( pax.getId() );
      }
      else
      {
        List paxIds = (List)request.getSession().getAttribute( "paxIds" );
        return !paxIds.contains( pax.getId() );
      }
      /*
       * if ( !getPromotionService().isParticipantMemberOfPromotionAudience( pax, promotion,
       * isSubmitter, mainUserNode ) ) { return true; }
       */
    }
    return false;
  }

  protected List filterByNode( List participants, Promotion promotion, Node mainUserNode )
  {
    // collect user nodes
    List filteredNodes = new ArrayList();
    Set nodeSet = new HashSet();
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      User currentPax = (User)paxIter.next();
      nodeSet.addAll( currentPax.getUserNodesAsNodes() );
    }
    if ( promotion.getSecondaryAudienceType() != null )
    {
      if ( !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
      {
        // filter nodes
        if ( promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
            && promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) ) )
        {
          filteredNodes.addAll( nodeSet );
        }
        else
        {
          if ( promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) )
              && promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) ) )
          {

            if ( isNodeBasedCriteria( promotion.getPromotionPrimaryAudiences() ) )
            {
              filteredNodes = getAudienceService().filterNodeListBySecondaryAudience( promotion, nodeSet, mainUserNode );
            }
            else
            {
              filteredNodes.addAll( nodeSet );
            }

          }
          else if ( !promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) ) )
          {
            if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) ) )
            {
              filteredNodes.add( mainUserNode );
            }
            else if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_BELOW_CODE ) ) )
            {
              filteredNodes = getNodeService().allChildNodesUnderParentNodes( mainUserNode.getId() );

            }
            else
            {
              if ( isNodeBasedCriteria( promotion.getPromotionSecondaryAudiences() ) )
              {
                filteredNodes = getAudienceService().filterNodeListBySecondaryAudience( promotion, nodeSet, mainUserNode );
              }
              else
              {
                filteredNodes.addAll( nodeSet );
              }
            }
          }

          else
          {

            filteredNodes = getAudienceService().filterNodeListBySecondaryAudience( promotion, nodeSet, mainUserNode );

          }
        }
      }
      else
      {
        filteredNodes.addAll( nodeSet );
      }
    }
    else
    {
      filteredNodes.addAll( nodeSet );
    }

    // filter participants that are in removed nodes
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      User currentPax = (User)paxIter.next();
      for ( Iterator userNodeIter = currentPax.getUserNodes().iterator(); userNodeIter.hasNext(); )
      {
        UserNode userNode = (UserNode)userNodeIter.next();
        if ( !filteredNodes.contains( userNode.getNode() ) )
        {
          userNodeIter.remove();

        }
      }
    }

    return participants;
  }

  public boolean isNodeBasedCriteria( Set promotionAudiences )
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    Iterator iter = promotionAudiences.iterator();
    while ( iter.hasNext() )
    {
      PromotionAudience promotionAudience = (PromotionAudience)iter.next();
      Audience audience = promotionAudience.getAudience();

      if ( audience instanceof CriteriaAudience )
      {
        CriteriaAudience criteriaAudience = (CriteriaAudience)audience;

        for ( Iterator iter1 = criteriaAudience.getAudienceCriterias().iterator(); iter1.hasNext(); )
        {
          // node is in crit audience if in any Audience Criteria
          audienceCriteria = (AudienceCriteria)iter1.next();
          if ( isNodeBasedCriteria( audienceCriteria ) )
          {
            return true;
          }
        }

      }
      else if ( ! ( audience instanceof PaxAudience ) )
      {
        throw new BeaconRuntimeException( "Unknown audience class type: " + audience.getClass().getName() );
      }

    }

    return false;
  }

  protected boolean shouldFilterNodes( Promotion promotion )
  {
    if ( promotion == null || promotion.getSecondaryAudienceType() == null )
    {
      return false;
    }

    if ( promotion.getSecondaryAudienceType().isSpecificNodeType() )
    {
      return true;
    }

    if ( promotion.getSecondaryAudienceType().isSpecificNodeAndBelowType() )
    {
      return true;
    }

    if ( promotion.getSecondaryAudienceType().isSpecifyAudienceType() )
    {
      return true;
    }

    if ( promotion.getSecondaryAudienceType().isSameAsPrimaryType() && promotion.getPrimaryAudienceType() != null && promotion.getPrimaryAudienceType().isSpecifyAudienceType() )
    {
      return true;
    }

    return false;
  }

  //
  // Overridables
  //

  protected boolean applyMainUserNodeFilter( HttpServletRequest request )
  {
    return false;
  }

  protected Promotion getPromotion( HttpServletRequest request )
  {
    return null;
  }

  protected Long getUserId( HttpServletRequest request )
  {
    return UserManager.getUserId();
  }

  protected Long getProxyUserId( HttpServletRequest request )
  {
    return null;
  }

  protected boolean excludeSelf( HttpServletRequest request )
  {
    return false;
  }

  protected Long getMainUserNode( HttpServletRequest request )
  {
    return null;
  }

  protected AssociationRequestCollection getPaxAssociations()
  {
    return null;
  }

  //
  // Helpers
  //

  protected List filterPickList( String query, List picklistItems )
  {
    List results = new ArrayList();
    if ( picklistItems != null )
    {
      for ( Iterator i = picklistItems.iterator(); i.hasNext(); )
      {
        PickListItem next = (PickListItem)i.next();
        if ( next.getName() != null && next.getName().toLowerCase().startsWith( query.toLowerCase() ) )
        {
          results.add( next );
        }
      }
    }

    return results;
  }

  private List<Long> getEligibleParticipantsForPromotion( Promotion promotion, HttpServletRequest request )
  {
    List<Long> paxIds = new ArrayList<Long>();
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();

    if ( promotion.getSecondaryAudienceType().equals( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) )
        && promotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) ) )
    {
      List<FormattedValueBean> fvbPaxList = getListBuilderService().searchParticipants( promotion.getPrimaryAudiences(), primaryHierarchy.getId(), true, null, true );

      if ( null != fvbPaxList )
      {
        for ( FormattedValueBean fmv : fvbPaxList )
        {
          paxIds.add( fmv.getId() );
        }
      }
    }
    else
    {

      List<FormattedValueBean> fvbPaxSecondList = getListBuilderService().searchParticipants( promotion.getSecondaryAudiences(), primaryHierarchy.getId(), true, null, true );
      if ( null != fvbPaxSecondList )
      {
        for ( FormattedValueBean fmv : fvbPaxSecondList )
        {
          paxIds.add( fmv.getId() );
        }
      }
    }

    request.getSession().setAttribute( "paxIds", paxIds );
    return paxIds;
  }

  protected static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  protected static NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected static AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  protected static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected static HierarchyService getHierarchyService()
  {
    return (HierarchyService)BeanLocator.getBean( HierarchyService.BEAN_NAME );
  }

  protected static ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)BeanLocator.getBean( ListBuilderService.BEAN_NAME );
  }

}

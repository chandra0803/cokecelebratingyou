/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ListBuilderAction.java,v $ */

package com.biperf.core.ui.participant;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.AudienceCriteriaCharacteristic;
import com.biperf.core.domain.participant.AudienceCriteriaUtility;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.node.NodeSearchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.FormattedValueBean;

/**
 * Action class for List Builder operations.
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
 * <td>sedey</td>
 * <td>June 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ListBuilderAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( ListBuilderAction.class );

  private static final String SESSION_LIST_BUILDER_FORM = "sessionListBuilderForm";
  public static final String SESSION_FILTER_AUDIENCE_SET = "SESSION_FILTER_AUDIENCE_SET";
  public static final String SESSION_RESULT_PARTICIPANT_ID_LIST = "SESSION_AUDIENCE_MEMBER_LIST";
  public static final String SESSION_CRITERIA_AUDIENCE_VALUE = "criteriaAudienceValue";

  public static final String AUDIENCE_MEMBERS_LOOKUP_RETURN_URL_PARAM = "audienceMembersLookupReturnUrl";

  /**
   * Prepare the display for creating a product.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // get the actionForward to display the udpate pages.
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  /**
   * Prepare the display for a listBuilder Search.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward search( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ListBuilderForm searchListBuilderForm = (ListBuilderForm)form;
    log.debug( "****SearchListBuilderForm Elelments***: " + searchListBuilderForm.toString() );
    // For role patch, client admin removed. But new view_participants role takes its place, here.
    // To keep changeset minimal for ease of patching, leaving variable name...
    boolean isStandardClientAdmin = false;
    if ( getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_VIEW_PARTICIPANTS ) )
    {
      isStandardClientAdmin = true;
    }

    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      searchListBuilderForm.setSelectedBox( null ); // EARLY EXIT
    }

    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    // TODO This should move to the form, once we are ready to make the service call.
    if ( searchListBuilderForm.getLookedUpNodeName() != null && !searchListBuilderForm.getLookedUpNodeName().equals( searchListBuilderForm.getNameOfNode() ) )
    {
      // User changed node name substring after a node id lookup.
      searchListBuilderForm.setNodeId( null );
    }

    Set filterAudiences = null;
    if ( searchListBuilderForm.isFilterAudienceIncluded() )
    {
      filterAudiences = (Set)request.getSession().getAttribute( SESSION_FILTER_AUDIENCE_SET );
    }

    String searchType = searchListBuilderForm.getSearchType();
    if ( searchType.equals( "criteria" ) )
    {

      // Get the "form" back out of the Session to redisplay.
      CriteriaAudienceValue criteriaAudienceValue = (CriteriaAudienceValue)request.getSession().getAttribute( SESSION_CRITERIA_AUDIENCE_VALUE );

      if ( criteriaAudienceValue == null )
      {
        criteriaAudienceValue = new CriteriaAudienceValue();
      }

      AudienceCriteriaValue audienceCriteriaValue = new AudienceCriteriaValue();
      audienceCriteriaValue.setAudienceCriteria( searchListBuilderForm.toSearchDomainObject() );

      AudienceCriteria audienceCriteria = audienceCriteriaValue.getAudienceCriteria();

      if ( audienceCriteria.hasExcludeConstraints() )
      {
        if ( !audienceCriteria.hasConstraints() )
        {
          errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "participant.list.builder.details.ENTER_SEARCH_CRITERIA" ) );
          saveErrors( request, errors );
          return mapping.findForward( ActionConstants.FAIL_FORWARD );
        }
      }

      // retrieve from dao using form objects
      for ( Iterator iter = audienceCriteria.getCharacteristicCriterias().iterator(); iter.hasNext(); )
      {

        AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

        Characteristic characteristic = audienceCriteriaCharacteristic.getCharacteristic();
        Characteristic characteristicType = null;

        if ( characteristic instanceof UserCharacteristicType )
        {
          characteristicType = getUserCharacteristicService().getCharacteristicById( audienceCriteriaCharacteristic.getCharacteristic().getId() );

        }
        else if ( characteristic instanceof NodeTypeCharacteristicType )
        {
          characteristicType = getNodeTypeCharacteristicService().getCharacteristicById( audienceCriteriaCharacteristic.getCharacteristic().getId() );
        }
        else
        {
          throw new BeaconRuntimeException( "characteristicTypeClass must be UserCharacteristicType or NodeTypeCharacteristicType" );
        }

        if ( characteristicType != null )
        {
          audienceCriteriaCharacteristic.setCharacteristic( characteristicType );
        }
        else
        {
          iter.remove();
        }
      }

      audienceCriteriaValue.setCriteriaList( AudienceCriteriaUtility.getCriteria( audienceCriteriaValue.getAudienceCriteria() ) );

      audienceCriteriaValue.setExclusionCriteriaList( AudienceCriteriaUtility.getExclusionCriteria( audienceCriteriaValue.getAudienceCriteria() ) );

      audienceCriteriaValue.setViewPaxList( false );
      criteriaAudienceValue.setCurrentAudienceCriteriaValue( audienceCriteriaValue );

      request.getSession().setAttribute( SESSION_CRITERIA_AUDIENCE_VALUE, criteriaAudienceValue );

    }
    else
    {
      // Specific Particpant Query
      AudienceCriteria audienceCriteria = searchListBuilderForm.toSearchDomainObject();

      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      if ( primaryHierarchy != null )
      {
        Long primaryHierarchyId = primaryHierarchy.getId();

        // Service returns a FormattedValueBean object, which
        // is hack to get it working for now. Service/DAO formatting for view. It's bad, refactor
        // it...
        List searchResultFormattedValueBeans = getListBuilderService().searchParticipants( audienceCriteria, primaryHierarchyId, false, filterAudiences, false, isStandardClientAdmin );

        // Remove already selected values from search results.
        if ( searchListBuilderForm.getSelectedBox() != null && searchListBuilderForm.getSelectedBox().length > 0 )
        {
          List selectedFormattedValueBeans = ListBuilderController.buildSelectedResultList( searchListBuilderForm.getSelectedBox() );
          searchResultFormattedValueBeans.removeAll( selectedFormattedValueBeans );
        }

        // Prep resultBox with formatted values as if we are the results of a submit.
        // Controller will handle building other related view pieces.
        String[] resultsBox = new String[searchResultFormattedValueBeans.size()];
        for ( int i = 0; i < resultsBox.length; i++ )
        {
          resultsBox[i] = ( (FormattedValueBean)searchResultFormattedValueBeans.get( i ) ).getFormattedId();

        }
        searchListBuilderForm.setResultsBox( resultsBox );
      }

      searchListBuilderForm.setMethod( "search" );
    }
    searchListBuilderForm.reset( mapping, request );

    return mapping.findForward( forwardTo );
  }

  public ActionForward displayAudienceExclusion( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.CREATE_FORWARD;

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    ListBuilderForm searchListBuilderForm = (ListBuilderForm)form;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_LIST_BUILDER_FORM, searchListBuilderForm );

    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * Calling List Builder for retrieving a list of selected participant Ids <br />
   * <br />
   * In your action, create a prepareAudienceMembersLookup() and returnAudienceMembersLookup()
   * dispatch methods. (See ProductClaimSubmissionAction for an example)<br />
   * <br />
   * <b>prepareAudienceMembersLookup()</b> tasks: <br />
   * <ol>
   * <li>save your local form into the session (unless it already is a session form)</li>
   * <li>Build return url string to tell the List Builder action where to go upon return</li>
   * <li>perform a redirect to
   * /participant/listBuilderPaxDisplay.do?audienceMembersLookupReturnUrl=(url from step 2)</li>
   * <li>return null (since the redirect is handling the view)<br />
   * </li>
   * </ol>
   * <br />
   * <br />
   * <b>returnAudienceMembersLookup() </b>tasks:<br />
   * <ol>
   * <li>Set local form to the session version of the local form, then remove the session object
   * (Unless local form is a session form).<br />
   * </li>
   * <li>Retrieve Set&lt;Long&gt; of participantIds with request.getSession().getAttribute(
   * ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_SET );</li>
   * <li>Use the Set of participantIds as needed.</li>
   * <li>3. Remove the session attribute ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_SET.</li>
   * </ol>
   * <br />
   * Note: If the user selects the cancel button in List Builder, the same return Url will be called
   * but the ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_SET attribute will be null. <br />
   * In your jsp:<br />
   * <ol>
   * <li>On the button or link that you want to perform a list builder lookup, set the dispatch to
   * 'prepareAudienceMembersLookup' (Note: This refers to your Action's dispatch method)</li>
   * </ol>
   * <br />
   * <br />
   * TODO: Explain how to apply a filter audience.<br />
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward sendAudienceMembers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ListBuilderForm listBuilderForm = (ListBuilderForm)form;
    String returnUrl = listBuilderForm.getAudienceMembersLookupReturnUrl();

    if ( RequestUtils.getOptionalParamBoolean( request, "cancelAudienceMembersLookup" ).booleanValue() )
    {
      request.getSession().removeAttribute( SESSION_RESULT_PARTICIPANT_ID_LIST );
    }
    else
    {
      List selectedFormattedValueBeans = ListBuilderController.buildSelectedResultList( listBuilderForm.getSelectedBox() );
      request.getSession().setAttribute( SESSION_RESULT_PARTICIPANT_ID_LIST, selectedFormattedValueBeans );
    }
    // redirect to return url.
    response.sendRedirect( RequestUtils.getBaseURI( request ) + returnUrl );

    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward testReceiveAudienceMembers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    log.warn( "\n\n\nLogging list of returned Audience Members\n-----------------------------------------" );
    Set participantIds = (Set)request.getSession().getAttribute( SESSION_RESULT_PARTICIPANT_ID_LIST );
    for ( Iterator iter = participantIds.iterator(); iter.hasNext(); )
    {
      Long participantId = (Long)iter.next();
      log.warn( participantId );
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward prepareNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ListBuilderForm searchListBuilderForm = (ListBuilderForm)form;

    String returnUrl = RequestUtils.getRequiredParamString( request, "returnUrl" );

    String nodeSearchType = RequestUtils.getRequiredParamString( request, "nodeSearchType" );

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_LIST_BUILDER_FORM, searchListBuilderForm );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/hierarchy/nodeLookup.do?" + "method=displaySearchWithinPrimary&" + NodeSearchAction.RETURN_ACTION_URL_PARAM + returnUrl
        + "&nodeSearchType=" + nodeSearchType );

    return null;
  }

  public ActionForward returnAudienceExclusion( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Boolean excludeCountry = Boolean.FALSE;
    Boolean excludeNode = Boolean.FALSE;
    Boolean excludeNodeRole = Boolean.FALSE;
    Boolean excludeNodeChar = Boolean.FALSE;
    Boolean excludeJobPosition = Boolean.FALSE;
    Boolean excludeDepartment = Boolean.FALSE;
    Boolean excludePaxChar = Boolean.FALSE;

    ListBuilderForm searchListBuilderForm = (ListBuilderForm)form;

    excludeCountry = searchListBuilderForm.getExcludeCountry();
    excludeNode = searchListBuilderForm.getExcludeNodeName();
    excludeNodeRole = searchListBuilderForm.getExcludeNodeRole();
    excludeNodeChar = searchListBuilderForm.getExcludeNodeCharacteristic();
    excludeJobPosition = searchListBuilderForm.getExcludeJobPosition();
    excludeDepartment = searchListBuilderForm.getExcludeDepartment();
    excludePaxChar = searchListBuilderForm.getExcludePaxCharacteristic();

    // Get the form back out of the Session to redisplay.
    ListBuilderForm sessionListBuilderForm = (ListBuilderForm)request.getSession().getAttribute( SESSION_LIST_BUILDER_FORM );

    if ( sessionListBuilderForm != null )
    {
      try
      {
        BeanUtils.copyProperties( searchListBuilderForm, sessionListBuilderForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    searchListBuilderForm.setExcludeCountry( excludeCountry );
    searchListBuilderForm.setExcludeNodeName( excludeNode );
    searchListBuilderForm.setExcludeNodeRole( excludeNodeRole );
    searchListBuilderForm.setExcludeNodeCharacteristic( excludeNodeChar );
    searchListBuilderForm.setExcludeJobPosition( excludeJobPosition );
    searchListBuilderForm.setExcludeDepartment( excludeDepartment );
    searchListBuilderForm.setExcludePaxCharacteristic( excludePaxChar );

    // clean up the session
    request.getSession().removeAttribute( SESSION_LIST_BUILDER_FORM );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );

  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ListBuilderForm searchListBuilderForm = (ListBuilderForm)form;

    // Get the form back out of the Session to redisplay.
    ListBuilderForm sessionListBuilderForm = (ListBuilderForm)request.getSession().getAttribute( SESSION_LIST_BUILDER_FORM );

    if ( sessionListBuilderForm != null )
    {
      try
      {
        BeanUtils.copyProperties( searchListBuilderForm, sessionListBuilderForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }
    String nodeId = null;
    String nodeName = null;
    String nodeSearchType = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        nodeId = (String)clientStateMap.get( "nodeId" );
      }
      catch( ClassCastException cce )
      {
        nodeId = ( (Long)clientStateMap.get( "nodeId" ) ).toString();
      }
      nodeName = (String)clientStateMap.get( "nodeName" );
      nodeSearchType = (String)clientStateMap.get( "nodeSearchType" );
    }
    catch( InvalidClientStateException e )
    {
      // do nothing, we just might not have anything at this point and that is okay
    }

    if ( nodeSearchType != null && nodeSearchType.equals( "include" ) )
    {
      // Set the nodeId, nodeName, and lookedUpNodeName on the form.
      if ( nodeId != null )
      {
        searchListBuilderForm.setNodeId( new Long( nodeId ) );
      }
      searchListBuilderForm.setNameOfNode( nodeName );
      searchListBuilderForm.setLookedUpNodeName( nodeName );
    }
    else if ( nodeSearchType != null && nodeSearchType.equals( "exclude" ) )
    {
      // Set the nodeId, nodeName, and lookedUpNodeName on the form.
      if ( nodeId != null )
      {
        searchListBuilderForm.setExcludeNodeId( new Long( nodeId ) );
      }
      searchListBuilderForm.setExcludeNameOfNode( nodeName );
      searchListBuilderForm.setExcludeLookedUpNodeName( nodeName );
    }

    // clean up the session
    request.getSession().removeAttribute( SESSION_LIST_BUILDER_FORM );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Builds a list of ListBuilderResult objects from a String[] of id's
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addAudienceCriteriaToList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    CriteriaAudienceValue criteriaAudienceValue = (CriteriaAudienceValue)request.getSession().getAttribute( SESSION_CRITERIA_AUDIENCE_VALUE );
    criteriaAudienceValue.getAudienceCriteriaValueList().add( 0, criteriaAudienceValue.getCurrentAudienceCriteriaValue() );
    criteriaAudienceValue.getCurrentAudienceCriteriaValue().setViewPaxList( false );
    criteriaAudienceValue.setCurrentAudienceCriteriaValue( null );

    ListBuilderForm listBuilderForm = (ListBuilderForm)form;
    listBuilderForm.reset( mapping, request );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Builds a list of ListBuilderResult objects from a String[] of id's
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removeAudienceCriteriaFromList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    CriteriaAudienceValue criteriaAudienceValue = (CriteriaAudienceValue)request.getSession().getAttribute( SESSION_CRITERIA_AUDIENCE_VALUE );

    for ( Iterator removeIter = criteriaAudienceValue.getAudienceCriteriaValueList().iterator(); removeIter.hasNext(); )
    {

      AudienceCriteriaValue audienceCriteriaValue = (AudienceCriteriaValue)removeIter.next();

      if ( audienceCriteriaValue.getRemove() )
      {
        removeIter.remove();
      }
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Builds a list of ListBuilderResult objects from a String[] of id's
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward viewList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ListBuilderForm searchListBuilderForm = (ListBuilderForm)form;

    CriteriaAudienceValue criteriaAudienceValue = (CriteriaAudienceValue)request.getSession().getAttribute( SESSION_CRITERIA_AUDIENCE_VALUE );

    criteriaAudienceValue.getAudienceCriteriaValueByIndex( searchListBuilderForm.getAudienceCriteriaValueViewIndex() ).setViewPaxList( true );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Builds a list of ListBuilderResult objects from a String[] of id's
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward hideList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ListBuilderForm searchListBuilderForm = (ListBuilderForm)form;

    CriteriaAudienceValue criteriaAudienceValue = (CriteriaAudienceValue)request.getSession().getAttribute( SESSION_CRITERIA_AUDIENCE_VALUE );

    criteriaAudienceValue.getAudienceCriteriaValueByIndex( searchListBuilderForm.getAudienceCriteriaValueViewIndex() ).setViewPaxList( false );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Occurs when a new search is selected.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displaySearch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // Clear any previous CriteriaAudienceValue from session on new search

    request.getSession().removeAttribute( SESSION_CRITERIA_AUDIENCE_VALUE );
    request.getSession().removeAttribute( "audienceId" );
    request.getSession().removeAttribute( "outputErrorDeleteMsg" );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Gets an AudienceService
   * 
   * @return AudienceService
   */
  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  /**
   * Gets a HierarchyService
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  /**
   * Gets a CharacteristicService
   * 
   * @return CharacteristicService
   */
  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  /**
   * Gets a CharacteristicService
   * 
   * @return CharacteristicService
   */
  private NodeTypeCharacteristicService getNodeTypeCharacteristicService()
  {
    return (NodeTypeCharacteristicService)getService( NodeTypeCharacteristicService.BEAN_NAME );
  }

  public static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  public static AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)BeanLocator.getBean( AuthorizationService.BEAN_NAME );
  }
}

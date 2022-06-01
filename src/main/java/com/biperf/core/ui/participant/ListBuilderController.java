/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/ListBuilderController.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.NodeIncludeType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.employer.EmployerService;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.value.FormattedValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Implements the controller for the ListBuilder page.
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
public class ListBuilderController extends BaseController
{
  private static final String LIST_BUILDER_ASSET = "participant.list.builder.details";
  private boolean isPax = false;

  /**
   * Tiles controller for the ListBuilder page
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) Overridden from
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ListBuilderForm listBuilderForm = (ListBuilderForm)request.getAttribute( "listBuilderForm" );

    if ( StringUtils.equals( listBuilderForm.getSearchType(), ListBuilderForm.PARTICIPANT_SEARCH_TYPE ) )
    {
      isPax = true;
    }

    populateTilesAttributes( listBuilderForm, context );

    populateInstructionalCopyAttribute( listBuilderForm, request );

    if ( listBuilderForm.isAdmin() || isPax )
    {
      prepareAdminSearch( request, listBuilderForm );
    }

    if ( StringUtils.equals( listBuilderForm.getSearchType(), listBuilderForm.CRITERIA_SEARCH_TYPE ) )
    {
      request.setAttribute( "isCriteria", true );
      if ( listBuilderForm.getExcludeCountry() )
      {
        List countryList = getCountryService().getAllActive();
        request.setAttribute( "countryList", countryList );
        request.setAttribute( "showCountry", true );
      }
      if ( listBuilderForm.getExcludeNodeName() )
      {
        request.setAttribute( "showNodeName", true );
      }
      if ( listBuilderForm.getExcludeNodeRole() )
      {
        request.setAttribute( "showNodeRole", true );
      }
      if ( listBuilderForm.getExcludeNodeCharacteristic() )
      {
        Long excludeNodeTypeId = listBuilderForm.getExcludeNodeTypeId();
        if ( excludeNodeTypeId != null )
        {
          // Node type was selected
          prepareExcludeNodeTypeCharacteristics( request, listBuilderForm );
        }
        request.setAttribute( "showNodeCharacteristic", true );
      }
      if ( listBuilderForm.getExcludeJobPosition() )
      {
        // request.setAttribute( "jobPositions", PositionType.getList() );
        request.setAttribute( "jobPositions", getUserService().getPickListValuesFromCM( PositionType.PICKLIST_ASSET + ".items", "en_US" ) );
        request.setAttribute( "showJobPosition", true );
      }
      if ( listBuilderForm.getExcludeDepartment() )
      {
        request.setAttribute( "departments", DepartmentType.getList() );
        request.setAttribute( "showDepartment", true );
      }
      if ( listBuilderForm.getExcludePaxCharacteristic() )
      {
        prepareExcludeUserCharacteristics( request, listBuilderForm );
        request.setAttribute( "showPaxCharacteristic", true );
      }
    }

    if ( StringUtils.equals( listBuilderForm.getSearchType(), ListBuilderForm.PARTICIPANT_SEARCH_TYPE ) )
    {
      prepareParticipantResultBoxes( request, listBuilderForm );
    }

    if ( request.getParameter( "singleResult" ) != null )
    {
      if ( request.getParameter( "singleResult" ).equals( "true" ) )
      {
        listBuilderForm.setNeedSingleResult( true );
      }
    }

    // set the list of Positions in the request
    // request.setAttribute( "jobPositions", PositionType.getList() );
    request.setAttribute( "jobPositions", getUserService().getPickListValuesFromCM( PositionType.PICKLIST_ASSET + ".items", "en_US" ) );

    // set the list of Departments in the request
    request.setAttribute( "departments", DepartmentType.getList() );

    // set the list of Languages in the request
    request.setAttribute( "languages", LanguageType.getList() );

    // set the list of Employers in the request
    request.setAttribute( "employers", getEmployerService().getAll() );
    List countryList = getCountryService().getAllActive();
    request.setAttribute( "countryList", countryList );
    request.setAttribute( "rosterAudienceId", listBuilderForm.getRosterAudienceId() );
  }

  /**
   * @param listBuilderForm
   * @param request
   */
  private void populateInstructionalCopyAttribute( ListBuilderForm listBuilderForm, HttpServletRequest request )
  {
    boolean isAdmin = listBuilderForm.isAdmin();
    String searchType = listBuilderForm.getSearchType();
    String pageType = listBuilderForm.getPageType();

    String instructionalCopyKey;
    if ( !isAdmin && StringUtils.equals( pageType, "listBuilder" ) )
    {
      instructionalCopyKey = "INSTRUCTIONAL_COPY_PAX_LIST_BUILDER";
    }
    else if ( isAdmin && StringUtils.equals( pageType, "listBuilder" ) && StringUtils.equals( searchType, ListBuilderForm.PARTICIPANT_SEARCH_TYPE ) )
    {
      instructionalCopyKey = "INSTRUCTIONAL_COPY_ADMIN_LB_PAX";
    }
    else if ( isAdmin && StringUtils.equals( pageType, "listBuilder" ) && StringUtils.equals( searchType, ListBuilderForm.CRITERIA_SEARCH_TYPE ) )
    {
      instructionalCopyKey = "INSTRUCTIONAL_COPY_ADMIN_LB_CRIT";
    }
    else if ( isAdmin && StringUtils.equals( pageType, "addAudience" ) && StringUtils.equals( searchType, ListBuilderForm.PARTICIPANT_SEARCH_TYPE ) )
    {
      instructionalCopyKey = "INSTRUCTIONAL_COPY_ADD_AUD_PAX";
    }
    else if ( isAdmin && StringUtils.equals( pageType, "addAudience" ) && StringUtils.equals( searchType, ListBuilderForm.CRITERIA_SEARCH_TYPE ) )
    {
      instructionalCopyKey = "INSTRUCTIONAL_COPY_ADD_AUD_CRIT";
    }
    else if ( isAdmin && StringUtils.equals( pageType, "editAudience" ) && StringUtils.equals( searchType, ListBuilderForm.PARTICIPANT_SEARCH_TYPE ) )
    {
      instructionalCopyKey = "INSTRUCTIONAL_COPY_ADD_AUD_PAX";
    }
    else if ( isAdmin && StringUtils.equals( pageType, "editAudience" ) && StringUtils.equals( searchType, ListBuilderForm.CRITERIA_SEARCH_TYPE ) )
    {
      instructionalCopyKey = "INSTRUCTIONAL_COPY_ADD_AUD_CRIT";
    }
    else
    {
      throw new BeaconRuntimeException( "Unsupported List Builder variant: isAdmin: " + isAdmin + " searchType: " + searchType + " pageType: " + pageType );
    }

    request.setAttribute( "INSTRUCTIONAL_COPY_KEY", instructionalCopyKey );
  }

  private void prepareParticipantResultBoxes( HttpServletRequest request, ListBuilderForm listBuilderForm )
  {
    // Get the selected/results box items from the form and place that list in the request for
    // redisplay.

    List searchResultList = null;
    List selectedList = null;
    if ( listBuilderForm.getSelectedBox() != null && listBuilderForm.getSelectedBox().length > 0 )
    {
      selectedList = buildSelectedResultList( listBuilderForm.getSelectedBox() );
    }
    request.setAttribute( "selectedResults", selectedList );
    // once the selected list is build, we no longer want those values in the in the form's
    // String[] because when the page displays, they will show up as selected.
    listBuilderForm.setSelectedBox( null );
    // Get any remaining non selected search results from the form and place that list
    // in the request for redisplay.
    if ( listBuilderForm.getResultsBox() != null && listBuilderForm.getResultsBox().length > 0 )
    {
      searchResultList = buildSelectedResultList( listBuilderForm.getResultsBox() );
    }
    request.setAttribute( "searchResults", searchResultList );
    // once the search result list is build, we no longer want those values in the in the form's
    // String[] because when the page displays, they will show up as selected.
    listBuilderForm.setResultsBox( null );
  }

  private void prepareAdminSearch( HttpServletRequest request, ListBuilderForm listBuilderForm )
  {
    prepareUserCharacteristics( request, listBuilderForm );
    request.setAttribute( "nodeTypeList", getNodeTypeService().getAll() );
    request.setAttribute( "nodeRelationshipList", HierarchyRoleType.getList() );
    request.setAttribute( "nodeIncludeList", NodeIncludeType.getList() );
    Long nodeTypeId = listBuilderForm.getNodeTypeId();
    if ( nodeTypeId != null )
    {
      // Node type was selected
      prepareNodeTypeCharacteristics( request, listBuilderForm );
    }
  }

  private void populateTilesAttributes( ListBuilderForm listBuilderForm, ComponentContext context )
  {
    populateHeaderTilesAttribute( context, listBuilderForm.getPageType() );

    populateSearchAndResultsTilesAttribute( context, listBuilderForm.getSearchType(), listBuilderForm.isAdmin() );

    populateSaveTilesAttribute( context, listBuilderForm.getPageType(), listBuilderForm.isAdmin() );

  }

  private void populateHeaderTilesAttribute( ComponentContext context, String pageType )
  {
    if ( StringUtils.equals( pageType, "listBuilder" ) )
    {
      context.putAttribute( "header", ContentReaderManager.getText( LIST_BUILDER_ASSET, "HEADER_LIST_BUILDER" ) );
    }
    else if ( StringUtils.equals( pageType, "addAudience" ) )
    {
      context.putAttribute( "header", ContentReaderManager.getText( LIST_BUILDER_ASSET, "HEADER_ADD_AUDIENCE" ) );
    }
    else if ( StringUtils.equals( pageType, "editAudience" ) )
    {
      context.putAttribute( "header", ContentReaderManager.getText( LIST_BUILDER_ASSET, "HEADER_EDIT_AUDIENCE" ) );
    }
    else
    {
      throw new BeaconRuntimeException( "unknown pageType: " + pageType );
    }
  }

  private void populateSearchAndResultsTilesAttribute( ComponentContext context, String searchType, boolean isAdmin )
  {
    if ( searchType.equals( ListBuilderForm.CRITERIA_SEARCH_TYPE ) )
    {
      context.putAttribute( "results", "/participant/listBuilderCriteriaResults.jsp" );
    }
    else if ( searchType.equals( ListBuilderForm.PARTICIPANT_SEARCH_TYPE ) )
    {
      context.putAttribute( "results", "/participant/listBuilderResultsParticipant.jsp" );
    }
    else
    {
      throw new BeaconRuntimeException( "unknown searchType: " + searchType );
    }

    if ( isAdmin || isPax )
    {
      context.putAttribute( "search", "/participant/listBuilderSearchAdmin.jsp" );
    }
    else
    {
      context.putAttribute( "search", "/participant/listBuilderSearchNonAdmin.jsp" );
    }

  }

  private void populateSaveTilesAttribute( ComponentContext context, String pageType, boolean isAdmin )
  {
    if ( pageType.equals( "listBuilder" ) && isAdmin )
    {
      context.putAttribute( "save", "/participant/listBuilderSaveAdminListBuilder.jsp" );
    }
    else if ( pageType.equals( "listBuilder" ) && !isAdmin )
    {
      context.putAttribute( "save", "/participant/listBuilderSaveNonAdminListBuilder.jsp" );
    }
    else if ( pageType.equals( "addAudience" ) )
    {
      context.putAttribute( "save", "/participant/listBuilderSaveAddAudience.jsp" );
    }
    else if ( pageType.equals( "editAudience" ) )
    {
      context.putAttribute( "save", "/participant/listBuilderSaveEditAudience.jsp" );
    }
    else
    {
      throw new BeaconRuntimeException( "unknown pageType: " + pageType );
    }
  }

  /**
   * @param request
   * @param listBuilderForm
   */
  private void prepareNodeTypeCharacteristics( HttpServletRequest request, ListBuilderForm listBuilderForm )
  {
    Set nodeCharacteristics = new LinkedHashSet();

    List nodeTypeCharacteristicTypes = getNodeTypeCharacteristicService().getAllNodeTypeCharacteristicTypesByNodeTypeId( listBuilderForm.getNodeTypeId() );

    List characteristicList = CharacteristicUtils.getNodeCharacteristicValueList( nodeCharacteristics, nodeTypeCharacteristicTypes );

    if ( listBuilderForm.getNodeTypeCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.loadExistingValues( characteristicList, listBuilderForm.getNodeTypeCharacteristicValueList() );
    }
    listBuilderForm.setNodeTypeCharacteristicValueList( characteristicList );

    Iterator availableIt = nodeTypeCharacteristicTypes.iterator();
    while ( availableIt.hasNext() )
    {
      Characteristic characteristic = (Characteristic)availableIt.next();
      if ( characteristic.getPlName() != null && !characteristic.getPlName().equals( "" ) )
      {
        request.setAttribute( characteristic.getPlName(), DynaPickListType.getList( characteristic.getPlName() ) );
      }
    }

  }

  private void prepareUserCharacteristics( HttpServletRequest request, ListBuilderForm listBuilderForm )
  {
    Set userCharacteristics = new LinkedHashSet();
    // This is the set of all UserCharacteristic objects
    List availableCharacteristics = getUserCharacteristicService().getAllCharacteristics();
    List characteristicList = CharacteristicUtils.getUserCharacteristicValueList( userCharacteristics, availableCharacteristics );
    if ( listBuilderForm.getUserCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.loadExistingValues( characteristicList, listBuilderForm.getUserCharacteristicValueList() );
    }
    listBuilderForm.setUserCharacteristicValueList( characteristicList );
    // This is a little different than the above Iteration because this iteration is
    // dealing with Characteristic PickList Objects rather than UserCharacteristic Objects
    Iterator availableIt = availableCharacteristics.iterator();
    while ( availableIt.hasNext() )
    {
      Characteristic characteristic = (Characteristic)availableIt.next();
      if ( characteristic.getPlName() != null && !characteristic.getPlName().equals( "" ) )
      {
        request.setAttribute( characteristic.getPlName(), addAllOption( DynaPickListType.getList( characteristic.getPlName() ) ) );
      }
    }

  }

  /**
   * @param request
   * @param listBuilderForm
   */
  private void prepareExcludeNodeTypeCharacteristics( HttpServletRequest request, ListBuilderForm listBuilderForm )
  {
    Set nodeCharacteristics = new LinkedHashSet();

    List nodeTypeCharacteristicTypes = getNodeTypeCharacteristicService().getAllNodeTypeCharacteristicTypesByNodeTypeId( listBuilderForm.getExcludeNodeTypeId() );

    List characteristicList = CharacteristicUtils.getNodeCharacteristicValueList( nodeCharacteristics, nodeTypeCharacteristicTypes );

    if ( listBuilderForm.getExcludeNodeTypeCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.loadExistingValues( characteristicList, listBuilderForm.getExcludeNodeTypeCharacteristicValueList() );
    }
    listBuilderForm.setExcludeNodeTypeCharacteristicValueList( characteristicList );

    Iterator availableIt = nodeTypeCharacteristicTypes.iterator();
    while ( availableIt.hasNext() )
    {
      Characteristic characteristic = (Characteristic)availableIt.next();
      if ( characteristic.getPlName() != null && !characteristic.getPlName().equals( "" ) )
      {
        request.setAttribute( characteristic.getPlName(), DynaPickListType.getList( characteristic.getPlName() ) );
      }
    }

  }

  private void prepareExcludeUserCharacteristics( HttpServletRequest request, ListBuilderForm listBuilderForm )
  {
    Set userCharacteristics = new LinkedHashSet();
    // This is the set of all UserCharacteristic objects
    List availableCharacteristics = getUserCharacteristicService().getAllCharacteristics();
    List characteristicList = CharacteristicUtils.getUserCharacteristicValueList( userCharacteristics, availableCharacteristics );
    if ( listBuilderForm.getExcludeUserCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.loadExistingValues( characteristicList, listBuilderForm.getExcludeUserCharacteristicValueList() );
    }
    listBuilderForm.setExcludeUserCharacteristicValueList( characteristicList );
    // This is a little different than the above Iteration because this iteration is
    // dealing with Characteristic PickList Objects rather than UserCharacteristic Objects
    Iterator availableIt = availableCharacteristics.iterator();
    while ( availableIt.hasNext() )
    {
      Characteristic characteristic = (Characteristic)availableIt.next();
      if ( characteristic.getPlName() != null && !characteristic.getPlName().equals( "" ) )
      {
        request.setAttribute( characteristic.getPlName(), addAllOption( DynaPickListType.getList( characteristic.getPlName() ) ) );
      }
    }

  }

  private List addAllOption( List pickList )
  {
    // TODO: Can we make our own picklistitem
    // pickList.add(0, new PickListItem());
    return pickList;
  }

  /**
   * Builds a list of FormattedValueBean objects from a String[] of id's
   * 
   * @param selectedResults
   * @return List
   */
  public static List buildSelectedResultList( String[] selectedResults )
  {
    List selectedResultList = new ArrayList();

    if ( selectedResults != null )
    {
      for ( int i = 0; i < selectedResults.length; i++ )
      {
        String idValue = selectedResults[i];

        FormattedValueBean selectedResult = FormattedValueBean.parseFormattedId( idValue );

        selectedResultList.add( selectedResult );
      }
    }

    Collections.sort( selectedResultList );
    return selectedResultList;
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  private NodeTypeService getNodeTypeService()
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }

  private NodeTypeCharacteristicService getNodeTypeCharacteristicService()
  {
    return (NodeTypeCharacteristicService)getService( NodeTypeCharacteristicService.BEAN_NAME );
  }

  private EmployerService getEmployerService()
  {
    return (EmployerService)getService( EmployerService.BEAN_NAME );
  }

  /**
   * Get the CountryService from the beanLocator.
   * 
   * @return CountryService
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }
}

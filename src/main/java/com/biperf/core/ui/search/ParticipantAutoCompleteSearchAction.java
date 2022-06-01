
package com.biperf.core.ui.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantSearchListView;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.FormFieldConstants;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.TcccClientUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.CountryValueBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PickListValueBean;
import com.biperf.core.value.nomination.NominationsParticipantDataValueBean;
import com.biperf.core.value.nomination.NominationsParticipantDataValueBean.NodeValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Class for implementing Auto complete and search participants for G5 Search
 * 
 */
public class ParticipantAutoCompleteSearchAction extends BaseDispatchAction
{

  public ActionForward doAutoComplete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String searchBy = request.getParameter( "type" );
    String query = request.getParameter( "query" );
    ParticipantSearchCriteria criteria = new ParticipantSearchCriteria();
    AutoCompleteView autoCompleteViewObj = new AutoCompleteView();
    List objects = null;

    if ( FormFieldConstants.PAX_SEARCH_CRITERIA_LASTNAME.equals( searchBy ) )
    {
      criteria.setLastName( query );
      objects = getParticipantService().searchCriteriaAutoComplete( criteria );
    }
    else if ( FormFieldConstants.PAX_SEARCH_CRITERIA_FIRSTNAME.equals( searchBy ) )
    {
      criteria.setFirstName( query );
      objects = getParticipantService().searchCriteriaAutoComplete( criteria );
    }
    else if ( FormFieldConstants.PAX_SEARCH_CRITERIA_DEPT.equals( searchBy ) )
    {
      /* customization start for performance tuning */
      //List<PickListValueBean> departments = getUserService().getPickListValuesFromCM( DepartmentType.PICKLIST_ASSET + ".items", UserManager.getUserLocale() );
      //objects = filterPickList( query, departments );
      objects = getParticipantService().getByDepartmentTypeForAutoComplete( query );
      /*customization end */
    }
    else if ( FormFieldConstants.PAX_SEARCH_CRITERIA_LOCATION.equals( searchBy ) )
    {
      criteria.setCountry( query );
      objects = getParticipantService().searchCriteriaAutoComplete( criteria );
    }
    else if ( FormFieldConstants.PAX_SEARCH_CRITERIA_JOB_TITLE.equals( searchBy ) )
    {
      /*customization start for performance tuning */
      //List<PickListValueBean> jobPositions = getUserService().getPickListValuesFromCM( PositionType.PICKLIST_ASSET + ".items", UserManager.getUserLocale() );
      //objects = filterPickList( query, jobPositions );
      objects = getParticipantService().getByPositionTypeForAutoComplete( query );
      /*customization end */
    }
    else if ( FormFieldConstants.PAX_SEARCH_CRITERIA_COUNTRY.equals( searchBy ) )
    {
      criteria.setPostalCode( query );
      objects = getParticipantService().searchCriteriaAutoComplete( criteria );
    }
    else if ( FormFieldConstants.PAX_SEARCH_CRITERIA_GROUP_NAME.equals( searchBy ) )
    {
      criteria.setGroupName( query );
      objects = getParticipantService().searchCriteriaAutoComplete( criteria );
    }

    if ( objects != null )
    {
      for ( Object object : objects )
      {
        AutoCompleteNameIdView nameIdView = new AutoCompleteNameIdView();
        if ( object instanceof String )
        {
          nameIdView.setId( object.toString() );
          nameIdView.setName( object.toString() );
        }
        else if ( object instanceof NameIdBean )
        {
          nameIdView.setId( ( (NameIdBean)object ).getId().toString() );
          nameIdView.setName( ( (NameIdBean)object ).getName() );
        }
        else if ( object instanceof DepartmentType )
        {
          nameIdView.setId( ( (DepartmentType)object ).getCode() );
          nameIdView.setName( ( (DepartmentType)object ).getName() );
        }
        else if ( object instanceof PositionType )
        {
          nameIdView.setId( ( (PositionType)object ).getCode() );
          nameIdView.setName( ( (PositionType)object ).getName() );
        }
        else if ( object instanceof PickListValueBean )
        {
          nameIdView.setId( ( (PickListValueBean)object ).getCode() );
          nameIdView.setName( ( (PickListValueBean)object ).getName() );
        }
        autoCompleteViewObj.getNameIdView().add( nameIdView );
      }
    }
    super.writeAsJsonToResponse( autoCompleteViewObj, response );
    return null;
  }

  public ActionForward generatePaxSearchView( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // build main view
    ParticipantSearchListView listViewObj = new ParticipantSearchListView();
    List<ParticipantSearchView> participants;
    List<Participant> paxs = null;
    if ( isSearchEmpty( request ) )
    {
      // Bug 3219 - Treat spaces as empty search and display message
      addMessageToListView( listViewObj, "participant.search.TYPE_ATLEAST_TWO_CHARS" );
    }
    else
    {
      // build criteria and get pax count using criteria
      ParticipantSearchCriteria criteria = getSearchCriteria( request );

      Long paxCount = getPaxCountBasedOnCriteria( criteria );

      // get system variable Pax Search Limit and compare with returned pax count
      int paxSearchLimit = getSystemVariableService().getPropertyByName( "pax.search.limit" ).getIntVal();
      if ( paxCount > paxSearchLimit )
      {
        addMessageToListView( listViewObj, "participant.search.TOO_MANY_RESULTS" );
      }
      else
      {
        paxs = searchParticipants( request );

        // Search CountryValueBean from Filtered Participants
        List<Long> paxIds = new ArrayList<Long>();
        for ( Participant pax : paxs )
        {
          populatePaxSourceType( pax, criteria );
          paxIds.add( pax.getId() );
        }
        Long[] ids = paxIds.toArray( new Long[paxIds.size()] );

        // Populate country only when there are pax available
        // otherwise will get ORA-00936: missing expression exception
        // Client customizations for wip #26532 starts
        if ( ids.length > 0 )
        {
          List<Map<Long, CountryValueBean>> countryResults = getParticipantService().populateCountriesForUsers( ids );
          Promotion promotion = getPromotion( request );
          listViewObj = buildParticipantSearchListView( promotion, paxs, getPromotionId( request ), countryResults );
          flagInvalidCountries( paxs, promotion, listViewObj );
          // Client customizations for wip #42701 starts
          //flagInvalidPayrolls( promotion, listViewObj );
          participants = listViewObj.getParticipants();
          CokeClientService cokeClientService = getCokeClientService();
          // Client customizations for wip #42701 ends
          for ( ParticipantSearchView participantSearchView : participants )
          {
            // Client customizations for wip #42701 starts
            if ( promotion != null && promotion.getAdihCashOption() && !Objects.isNull( participantSearchView.getCurrency() ) )
            {
              participantSearchView.setAwardMin( 1L );
              participantSearchView.setAwardMax( TcccClientUtils.getMaxAllowedAward( cokeClientService, promotion.getAdihCashMaxAward(), participantSearchView.getCurrency() ) );
            }
            // Client customizations for wip #42701 ends
            if ( participantSearchView.getIsPublic() && needParticipantProfileLink() )
            {
              participantSearchView.setParticipantUrl( getParticipantUrl( request, participantSearchView ) );
            }
            participantSearchView.setPurlAllowOutsideDomains( getParticipantService().isAllowePurlOutsideInvites( participantSearchView.getId() ) );
          }
        }
        else
        {
          addMessageToListView( listViewObj, "participant.search.NO_RESULTS" );
        }
        // Client customizations for wip #26532 ends
      }
    }
    super.writeAsJsonToResponse( listViewObj, response );
    return null;
  }

  public ActionForward generatePaxSearchViewForNomination( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    ParticipantSearchListView listViewObj = new ParticipantSearchListView();
    List<Participant> paxs = null;

    if ( isSearchEmpty( request ) )
    {
      addMessageToListView( listViewObj, "participant.search.TYPE_ATLEAST_TWO_CHARS" );
    }
    else
    {

      ParticipantSearchCriteria criteria = getSearchCriteria( request );
      Long paxCount = getPaxCountBasedOnCriteria( criteria );

      int paxSearchLimit = getSystemVariableService().getPropertyByName( "pax.search.limit" ).getIntVal();
      if ( paxCount > paxSearchLimit )
      {
        addMessageToListView( listViewObj, "participant.search.TOO_MANY_RESULTS" );
      }
      else
      {
        paxs = searchParticipants( request );
        Promotion promotion = getPromotion( request );
        List<Long> paxIds = new ArrayList<Long>();
        for ( Participant pax : paxs )
        {
          paxIds.add( pax.getId() );
        }
        Long[] ids = paxIds.toArray( new Long[paxIds.size()] );

        if ( ids.length > 0 )
        {
          List<Map<Long, CountryValueBean>> countryResults = getParticipantService().populateCountriesForUsers( ids );
          listViewObj = buildParticipantSearchListView( promotion, paxs, getPromotionId( request ), countryResults );
          flagInvalidCountries( paxs, getPromotion( request ), listViewObj );
        }
        else
        {
          addMessageToListView( listViewObj, "participant.search.NO_RESULTS" );
        }
      }
    }

    if ( listViewObj.getMessages() != null && listViewObj.getMessages().size() > 0 )
    {
      listViewObj.getParticipants().clear();
      writeAsJsonToResponse( listViewObj, response );
    }
    else
    {
      writeAsJsonToResponse( getNomPax( listViewObj.getParticipants() ), response );
    }

    return null;
  }

  private NominationsParticipantDataValueBean getNomPax( List<ParticipantSearchView> pax )
  {
    NominationsParticipantDataValueBean nomPaxList = new NominationsParticipantDataValueBean();
    NominationsParticipantDataValueBean.ParticipantValueBean nomPax = null;
    NominationsParticipantDataValueBean.NodeValueBean node = null;

    for ( ParticipantSearchView p : pax )
    {
      nomPax = new NominationsParticipantDataValueBean.ParticipantValueBean();
      nomPax.setId( p.getId() );
      nomPax.setLastName( p.getLastName() );
      nomPax.setFirstName( p.getFirstName() );
      nomPax.setCountryName( p.getCountryName() );
      nomPax.setCountryCode( p.getCountryCode() );
      nomPax.setCountryRatio( p.getCountryRatio() );
      nomPax.setJobName( p.getJobName() );
      nomPax.setDepartmentName( p.getDepartmentName() );
      nomPax.setOrgName( p.getOrgName() );

      List<NodeValueBean> nomPaxNodes = new ArrayList<NominationsParticipantDataValueBean.NodeValueBean>();

      List<NameableBean> nodes = p.getNodes();
      for ( NameableBean n : nodes )
      {
        node = new NodeValueBean();
        node.setId( n.getId().intValue() );
        node.setName( n.getName() );
        nomPaxNodes.add( node );
      }
      nomPax.setNodes( nomPaxNodes );
      nomPaxList.addParticiapnt( nomPax );
    }
    return nomPaxList;
  }

  // bug fix #59069
  protected boolean needParticipantProfileLink()
  {
    return true;
  }

  protected Integer getPaxSearchResultCount()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.PAX_SEARCH_ROWS_LIMIT ).getIntVal();
  }

  protected Long getPaxCountBasedOnCriteria( ParticipantSearchCriteria criteria )
  {
    return getParticipantService().getPaxCountBasedOnCriteria( criteria );
  }

  protected void flagInvalidCountries( List<Participant> participants, Promotion promotion, ParticipantSearchListView listViewObj )
  {
  }

  protected Promotion getPromotion( HttpServletRequest request )
  {
    return null;
  }

  protected void addMessageToListView( ParticipantSearchListView listViewObj, String messageCMAsset )
  {
    String refineSearchMessage = CmsResourceBundle.getCmsBundle().getString( messageCMAsset );
    WebErrorMessage messages = new WebErrorMessage();
    List<WebErrorMessage> messageList = new ArrayList<WebErrorMessage>();
    messages.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    messages.setText( refineSearchMessage );
    messages.setCode( "" );
    messages.setName( "" );
    messageList.add( messages );
    listViewObj.setMessages( messageList );
  }

  protected boolean isSearchEmpty( HttpServletRequest request )
  {
    String lastName = request.getParameter( "lastName" );
    String firstName = request.getParameter( "firstName" );
    String location = request.getParameter( "location" );
    String jobTitle = request.getParameter( "jobTitle" );
    String department = request.getParameter( "department" );
    String country = request.getParameter( "country" );
    if ( StringUtil.isNullOrEmpty( lastName ) && StringUtil.isNullOrEmpty( firstName ) && StringUtil.isNullOrEmpty( location ) && StringUtil.isNullOrEmpty( jobTitle )
        && StringUtil.isNullOrEmpty( department ) && StringUtil.isNullOrEmpty( country ) )
    {
      return true;
    }
    return false;
  }

  protected List<Participant> searchParticipants( HttpServletRequest request )
  {
    // build criteria
    ParticipantSearchCriteria criteria = getSearchCriteria( request );
    // search
    List<Participant> searchResults = getParticipantService().searchParticipants( criteria );
    // filter
    List<Participant> filteredResults = filterUsers( searchResults, request );

    return filteredResults;
  }

  protected ParticipantSearchCriteria getSearchCriteria( HttpServletRequest request )
  {
    String lastName = request.getParameter( "lastName" );
    String firstName = request.getParameter( "firstName" );
    String location = request.getParameter( "location" );
    String jobTitle = request.getParameter( "jobTitle" );
    String department = request.getParameter( "department" );
    String country = request.getParameter( "country" );

    ParticipantSearchCriteria criteria = new ParticipantSearchCriteria();
    if ( !StringUtil.isEmpty( lastName ) )
    {
      criteria.setLastName( lastName.trim() );
    }
    if ( !StringUtil.isEmpty( firstName ) )
    {
      criteria.setFirstName( firstName.trim() );
      criteria.setSortByLastNameFirstName( true );
    }
    if ( !StringUtil.isEmpty( location ) )
    {
      criteria.setNodeId( Long.parseLong( location.trim() ) );
    }
    if ( !StringUtil.isEmpty( jobTitle ) )
    {
      criteria.setJobPosition( jobTitle.trim() );
    }
    if ( !StringUtil.isEmpty( department ) )
    {
      criteria.setDepartment( department.trim() );
    }
    if ( !StringUtil.isEmpty( country ) )
    {
      criteria.setCountry( country.trim() );
    }
    criteria.setSortByLastNameFirstName( true );

    return criteria;
  }

  protected Long getPromotionId( HttpServletRequest request )
  {
    String promotionId = request.getParameter( "promotionId" );
    return StringUtils.isBlank( promotionId ) ? null : Long.valueOf( promotionId );
  }

  protected ParticipantSearchListView buildParticipantSearchListView( Promotion promotion, List<Participant> paxResults, Long promotionId, List<Map<Long, CountryValueBean>> countryResults )
  {    
    ParticipantSearchListView paxSearchListView = new ParticipantSearchListView();
    WebErrorMessage messages = new WebErrorMessage();
    List<WebErrorMessage> messageList = new ArrayList<WebErrorMessage>();
    String refineSearchMessage = CmsResourceBundle.getCmsBundle().getString( "participant.search.NO_PARTICIPANTS" );
    if ( paxResults == null || paxResults.isEmpty() )
    {
      messages.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      messages.setText( refineSearchMessage );
      messageList.add( messages );
      paxSearchListView.setMessages( messageList );
    }

    Map<String, String> codes = new HashMap<String, String>();
    if ( paxResults != null )
    {
      for ( Object object : paxResults )
      {
        if ( object instanceof Participant )
        {
          Participant pax = (Participant)object;
          ParticipantSearchView bean = new ParticipantSearchView( pax );

          // override properties, customizable to each search
          bean.setSelected( getIsSelected() );
          bean.setLocked( getIsLocked( pax ) );
          bean.setUrlEdit( getUrlEdit() );
          bean.setCountryRatio( getBudgetConversionRatio( pax.getId(), UserManager.getUserId(), promotionId ).doubleValue() );

          for ( Map<Long, CountryValueBean> countryInfo : countryResults )
          {
            if ( countryInfo != null )
            {
              CountryValueBean country = countryInfo.get( pax.getId() );

              // Add country names to Map to avoid multiple CM Calls
              String countryName = "";
              if ( country != null )
              {
                String assetCode = country.getCmAssetCode();
                if ( codes.containsKey( assetCode ) )
                {
                  countryName = codes.get( assetCode );
                }
                else
                {
                  countryName = ContentReaderManager.getText( country.getCmAssetCode(), "COUNTRY_NAME" );
                  codes.put( assetCode, countryName );
                }

                bean.setCountryName( countryName );
                bean.setCountryCode( country.getCountryCode() );
              }
            }
          }

          paxSearchListView.getParticipants().add( bean );
        }
      }
    }

    return paxSearchListView;
  }

  //
  // Overridables
  //
  protected boolean getIsSelected()
  {
    return false;
  }

  protected boolean getIsLocked( Participant pax )
  {
    return false;
  }

  protected String getUrlEdit()
  {
    return "";
  }

  protected BigDecimal getBudgetConversionRatio( Long receiverId, Long submitterId, Long promotionId )
  {
    return BigDecimal.ONE;
  }

  protected List<Participant> filterUsers( List<Participant> participants, HttpServletRequest request )
  {
    return participants;
  }

  /*
   * protected static ParticipantService getParticipantService() { return
   * (ParticipantService)getService( ParticipantService.BEAN_NAME ); }
   */
  protected static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private List<PickListValueBean> filterPickList( String query, List<PickListValueBean> picklistItems )
  {
    List results = new ArrayList();
    if ( !StringUtil.isEmpty( query ) )
    {
      if ( picklistItems != null )
      {
        for ( PickListValueBean i : picklistItems )
        {
          if ( i.getName() != null && i.getName().toLowerCase().startsWith( query.toLowerCase() ) )
          {
            results.add( i );
          }
        }
      }
    }

    return results;
  }

  public String getParticipantUrl( HttpServletRequest request, ParticipantSearchView participantSearchView )
  {
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "paxId", participantSearchView.getId() );
    parameterMap.put( "isFullPage", "true" );
    return ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, parameterMap );
  }

  protected ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  // Client customizations for wip #42701 starts
  protected CokeClientService getCokeClientService()
  {
    return (CokeClientService)getService( CokeClientService.BEAN_NAME );
  }
  // Client customizations for wip #42701 ends
  
  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected void populatePaxSourceType( Participant pax, ParticipantSearchCriteria searchCriteria )
  {
  }

}

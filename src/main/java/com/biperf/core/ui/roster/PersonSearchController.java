
package com.biperf.core.ui.roster;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.ui.roster.enums.RosterRoleTypeEnum;
import com.biperf.core.ui.search.AutoCompleteControllerHelper;
import com.biperf.core.ui.search.PaxSearchQueryModel;
import com.biperf.core.value.indexing.IndexSearchResult;
import com.biperf.core.value.participant.PaxIndexData;
import com.biw.digs.rest.enums.RoleTypeEnum;
import com.biw.digs.rest.request.PersonSearchRequest;
import com.biw.digs.rest.response.PersonFullView;
import com.biw.digs.rest.response.PersonSearch;
import com.biw.digs.rest.service.PersonSearchService;

@Controller
public class PersonSearchController extends RosterBaseController implements PersonSearchService
{
  private @Autowired AutoCompleteService autoCompleteService;

  private static final Log log = LogFactory.getLog( PersonSearchController.class );

  @Override
  public PersonSearch search( UUID companyId, PersonSearchRequest request )
  {
    PaxSearchQueryModel model = new PaxSearchQueryModel();

    if ( Objects.nonNull( request.getPageNumber() ) && Objects.nonNull( request.getPageSize() ) )
    {
      model = getPaginationDetails( request, model );
    }
    else
    {
      model = getDefaultPaginationDetails( request, model );
    }

    if ( Objects.nonNull( request.getPersonId() ) )
    {
      model.setRosterUserId( request.getPersonId() );
    }

    if ( Objects.nonNull( request.getUsername() ) && !StringUtils.isEmpty( request.getUsername() ) )
    {
      model.setUserName( request.getUsername().trim().toUpperCase() );
    }

    if ( Objects.nonNull( request.getFirstName() ) && !StringUtils.isEmpty( request.getFirstName() ) )
    {
      model.setName( request.getFirstName().trim() );
    }

    if ( Objects.nonNull( request.getLastName() ) && !StringUtils.isEmpty( request.getLastName() ) )
    {
      model.setName( request.getLastName().trim() );
    }

    if ( Objects.nonNull( request.getPersonCountry() ) && !StringUtils.isEmpty( request.getPersonCountry() ) )
    {
      model.setPersonCountry( request.getPersonCountry().trim() );
    }

    if ( Objects.nonNull( request.getLanguagePreference() ) && !StringUtils.isEmpty( request.getLanguagePreference() ) )
    {
      model.setLanguagePreference( request.getLanguagePreference().trim() );
    }

    if ( Objects.nonNull( request.getHierarchyNodeId() ) )
    {
      Long nodeId = nodeService.getNodeIdByRosterNodeId( request.getHierarchyNodeId() );

      if ( Objects.nonNull( nodeId ) )
      {
        model.setNodeId( nodeId );
      }
      else
      {
        log.error( "Hierarchy Node ID Not Found, input ID : " + request.getHierarchyNodeId().toString() );
        return getEmptyPersonSearchView();
      }
    }

    if ( !StringUtils.isEmpty( request.getRoleType() ) )
    {
      String ipRoleType = request.getRoleType().trim().toLowerCase();
      List<String> roleTypes = Arrays.asList( RoleTypeEnum.MEMBER.toString().toLowerCase(), RoleTypeEnum.MANAGER.toString().toLowerCase(), RoleTypeEnum.OWNER.toString().toLowerCase() );

      if ( roleTypes.contains( ipRoleType ) )
      {
        model.setRoleType( RosterRoleTypeEnum.findTypeByCode( ipRoleType ).getValue() );
      }
      else
      {
        log.error( "Invalid participant role type, input ID : " + ipRoleType );
        return getEmptyPersonSearchView();
      }
    }

    if ( Objects.nonNull( request.getEmailAddress() ) && !StringUtils.isEmpty( request.getEmailAddress() ) )
    {
      model.setEmailAddress( request.getEmailAddress().trim().toLowerCase() );
    }

    if ( Objects.nonNull( request.getPhoneNumber() ) && !StringUtils.isEmpty( request.getPhoneNumber() ) )
    {
      model.setPhoneNumber( request.getPhoneNumber().replaceAll( "-", "" ).trim() );
    }

    if ( Objects.nonNull( request.getPersonAddressCountry() ) && !StringUtils.isEmpty( request.getPersonAddressCountry() ) )
    {
      model.setPersonAddressCountry( request.getPersonAddressCountry().trim() );
    }

    if ( Objects.nonNull( request.getState() ) && !StringUtils.isEmpty( request.getState() ) )
    {
      model.setState( request.getState().trim() );
    }

    return getParticipantsDetails( model, request.getPersonProperties() );

  }

  private PersonSearch getParticipantsDetails( PaxSearchQueryModel model, List<String> personProperties )
  {
    List<PaxIndexData> paxIndexList = getParticipants( model ).getSearchResults();

    return buildResponseView( paxIndexList, personProperties );
  }

  private IndexSearchResult<PaxIndexData> getParticipants( PaxSearchQueryModel model )
  {
    AutoCompleteControllerHelper helper = new AutoCompleteControllerHelper();
    IndexSearchResult<PaxIndexData> paxIndexData = null;

    try
    {
      paxIndexData = autoCompleteService.search( helper.buildPaxSearchCriteria( model ) );
    }
    catch( Exception e )
    {
      log.error( e.getMessage() );
    }

    return paxIndexData;
  }

  private PersonSearch buildResponseView( List<PaxIndexData> paxList, List<String> personProperties )
  {
    PersonSearch response = new PersonSearch();

    if ( CollectionUtils.isNotEmpty( paxList ) )
    {
      response.setRecordCount( paxList.size() );
      paxList.stream().forEach( obj -> response.getPersons().add( buildPersonSearcView( obj, personProperties ) ) );
    }
    else
    {
      response.setRecordCount( 0 );
    }

    return response;
  }

  private PersonFullView buildPersonSearcView( PaxIndexData paxIndexData, List<String> personProperties )
  {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "MM/dd/yyyy" );

    PersonFullView view = new PersonFullView();

    view.setId( paxIndexData.getRosterUserId() );
    view.setExternalId( paxIndexData.getUserName() );

    if ( Objects.nonNull( paxIndexData.getLanguagePreference() ) )
    {
      view.setLocale( Locale.forLanguageTag( paxIndexData.getLanguagePreference() ) );
    }
    view.setTitle( paxIndexData.getTitle() );
    view.setMiddleName( paxIndexData.getMiddleName() );
    view.setGivenName( paxIndexData.getFirstname() );
    view.setSurname( paxIndexData.getLastname() );
    view.setSuffix( paxIndexData.getSuffix() );
    view.setCountry( paxIndexData.getPersonCountry() );
    view.setUsername( paxIndexData.getUserName() );

    if ( Objects.nonNull( paxIndexData.getHireDate() ) )
    {
      view.setHireDate( LocalDate.parse( paxIndexData.getHireDate(), formatter ) );
    }

    if ( Objects.nonNull( paxIndexData.getTerminationDate() ) )
    {
      view.setTerminationDate( LocalDate.parse( paxIndexData.getTerminationDate(), formatter ) );
    }

    view.setPronouns( paxIndexData.getPronouns() );

    /*
     * TODO : In future, all the person properties will be taken from ES index instead of DB.
     */

    if ( Objects.nonNull( personProperties ) && !personProperties.isEmpty() )
    {
      view = buildPersonPropsView( paxIndexData.getUserId(), personProperties, view );
    }

    return view;
  }

  private PaxSearchQueryModel getPaginationDetails( PersonSearchRequest request, PaxSearchQueryModel model )
  {
    try
    {
      if ( request.getPageNumber() > 0 && request.getPageSize() > 0 )
      {
        Integer endRow = request.getPageSize() * request.getPageNumber();
        Integer startRow = endRow + 1 - request.getPageSize();
        model.setPaginationRowStart( startRow );
        model.setPaginationRowEnd( request.getPageSize() );
      }
      else
      {
        model = getDefaultPaginationDetails( request, model );
      }

    }
    catch( Exception ex )
    {
      model = getDefaultPaginationDetails( request, model );
    }

    return model;
  }

  private PaxSearchQueryModel getDefaultPaginationDetails( PersonSearchRequest request, PaxSearchQueryModel model )
  {
    model.setPaginationRowStart( 0 );
    model.setPaginationRowEnd( 9900 );
    return model;
  }

}

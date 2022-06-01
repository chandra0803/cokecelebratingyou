
package com.biperf.core.ui.search;

import static com.biperf.core.service.system.SystemVariableService.AUTOCOMPLETE_ELASTIC_SEARCH_MAX_ALLOWED_TO_RECOGNIZE;

import java.util.Arrays;

import com.biperf.core.domain.enums.SortByType;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.value.indexing.PaxIndexMappingEnum;
import com.biperf.core.value.participant.PaxIndexSearchCriteria;
import com.biperf.core.value.participant.PaxIndexSortOrder;

public class AutoCompleteControllerHelper
{

  public PaxIndexSearchCriteria buildPaxSearchCriteria( PaxSearchQueryModel model )
  {
    PaxIndexSearchCriteria criteria = new PaxIndexSearchCriteria();
    criteria.setName( model.getName() );
    criteria.setDepartmentTypeCode( model.getDepartment() );
    if ( model.getLocation() != null )
    {
      criteria.setAllNodeIds( Arrays.asList( model.getLocation() ) );
    }
    criteria.setPositionTypeCode( model.getJobTitle() );
    criteria.setCountryId( model.getCountry() );
    criteria.setFromIndex( model.getFromIndex() );
    criteria.setAudienceIds( model.getAudienceId() );
    criteria.setPath( model.getPath() );
    criteria.setExcludeUserId( model.getExcludeUserIds() );
    criteria.setIncludeUserIds( model.getIncludeUserIds() );
    criteria.getFieldSortOrders().add( new PaxIndexSortOrder( SortByType.ASC, PaxIndexMappingEnum.LAST_NAME ) );
    criteria.getFieldSortOrders().add( new PaxIndexSortOrder( SortByType.ASC, PaxIndexMappingEnum.FIRST_NAME ) );

    SystemVariableService systemvariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );

    if ( model.isAll() )
    {
      criteria.setRecordsMaxSize( systemvariableService.getPropertyByName( AUTOCOMPLETE_ELASTIC_SEARCH_MAX_ALLOWED_TO_RECOGNIZE ).getIntVal() );
    }
    else
    {
      criteria.setRecordsMaxSize( systemvariableService.getPropertyByName( SystemVariableService.AUTOCOMPLETE_ELASTIC_SEARCH_MAX_RESULT_SIZE ).getIntVal() );
    }

    // Roster Related Attributes
    criteria.setRosterUserId( model.getRosterUserId() );
    criteria.setUserName( model.getUserName() );
    criteria.setPersonCountry( model.getPersonCountry() );
    criteria.setLanguagePreference( model.getLanguagePreference() );
    criteria.setRoleType( model.getRoleType() );
    criteria.setEmailAddress( model.getEmailAddress() );
    criteria.setPhoneNumber( model.getPhoneNumber() );
    criteria.setPersonAddressCountry( model.getPersonAddressCountry() );
    criteria.setState( model.getState() );

    // Pagination Attributes
    criteria.setPaginationRowStart( model.getPaginationRowStart() );
    criteria.setPaginationRowEnd( model.getPaginationRowEnd() );

    return criteria;
  }
}

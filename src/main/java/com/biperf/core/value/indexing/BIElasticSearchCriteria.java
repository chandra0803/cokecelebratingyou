
package com.biperf.core.value.indexing;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;

public interface BIElasticSearchCriteria
{

  default int getRecordsMaxSize()
  {
    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    PropertySetItem propertyByName = systemVariableService.getPropertyByName( SystemVariableService.AUTOCOMPLETE_ELASTIC_SEARCH_MAX_RESULT_SIZE );
    Objects.requireNonNull( propertyByName );

    return propertyByName.getIntVal();
  }

  default long getFromIndex()
  {
    return 0;
  }

  /**
   *  list of fields to be retrieved from index  document
   */
  default List<?> getFields()
  {
    return Arrays.asList();
  }

}

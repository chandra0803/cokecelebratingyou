
package com.biperf.core.ui.homepage;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.service.home.ModuleAppFilterMap;
import com.biperf.core.ui.BaseDispatchAction;

public abstract class BaseXPromotionalAction extends BaseDispatchAction
{
  @SuppressWarnings( "unchecked" )
  protected boolean isXpromotionEnabled( HttpServletRequest request )
  {
    List<ModuleAppFilterMap> moduleAppFilters = (List<ModuleAppFilterMap>)request.getSession().getAttribute( "filteredModuleList" );
    for ( ModuleAppFilterMap moduleApp : moduleAppFilters )
    {
      if ( moduleApp.getModule().getTileMappingType().getCode().equals( TileMappingType.X_PROMOTIONAL ) )
      {
        return true;
      }
    }
    return false;
  }

  protected List<?> shiftCollection( List<?> collection )
  {
    if ( collection.size() < 2 )
    {
      return collection;
    }
    Collections.swap( collection, 0, collection.size() - 1 );

    return collection;
  }
}

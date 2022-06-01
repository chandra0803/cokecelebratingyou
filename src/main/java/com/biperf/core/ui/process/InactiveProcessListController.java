
package com.biperf.core.ui.process;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

public class InactiveProcessListController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Inactive Process List page.
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "pageType", ProcessStatusType.INACTIVE );

    // For New Service Anniversary Module From Nackle
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      request.setAttribute( "processList", NewServiceAnniversaryUtil.removePurlAndCelebrationProcess( getProcessList() ) );
    }
    else
    {
      request.setAttribute( "processList", getProcessList() );

    }

  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of inactive processes.
   * 
   * @return a list of inactive processes, as a <code>List</code> of {@link Process} objects.
   */
  private List getProcessList()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BaseAssociationRequest()
    {
      public void execute( Object domainObject )
      {
        com.biperf.core.domain.process.Process process = (Process)domainObject;
        initialize( process.getEditRoles() );
        initialize( process.getLaunchRoles() );
        initialize( process.getViewLogRoles() );
      }
    } );

    return getProcessService().getProcessListByStatus( ProcessStatusType.INACTIVE, associationRequestCollection, "all" );
  }

  /**
   * Returns the process service.
   * 
   * @return a reference to the process service.
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

}

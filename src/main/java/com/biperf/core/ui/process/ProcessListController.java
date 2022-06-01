
package com.biperf.core.ui.process;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ProcessListType;
import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.ActionFormUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

public class ProcessListController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Active Process List page.
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    ProcessListForm processListForm = (ProcessListForm)ActionFormUtils.getActionForm( request, servletContext, "processListForm" );

    request.setAttribute( "pageType", ProcessStatusType.ACTIVE );

    // For New Service Anniversary Module From Nackle
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      request.setAttribute( "processList", NewServiceAnniversaryUtil.removePurlAndCelebrationProcess( getProcessList( processListForm.getProcessType() ) ) );
    }
    else
    {
      request.setAttribute( "processList", getProcessList( processListForm.getProcessType() ) );
    }

    request.setAttribute( "processListType", ProcessListType.getList() );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of active processes.
   * 
   * @return a list of active processes, as a <code>List</code> of {@link Process} objects.
   */
  @SuppressWarnings( "unchecked" )
  private List getProcessList( String processType )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BaseAssociationRequest()
    {
      public void execute( Object domainObject )
      {
        Process process = (Process)domainObject;
        initialize( process.getEditRoles() );
        initialize( process.getLaunchRoles() );
        initialize( process.getViewLogRoles() );
      }
    } );

    return getProcessService().getProcessListByStatus( ProcessStatusType.ACTIVE, associationRequestCollection, processType );
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

  public static <T> Predicate<T> distinctByKey( Function<? super T, Object> keyExtractor )
  {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent( keyExtractor.apply( t ), Boolean.TRUE ) == null;
  }
}

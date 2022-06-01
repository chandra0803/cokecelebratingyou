
package com.biperf.core.ui.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.domain.process.ProcessInvocationComment;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.process.ProcessInvocationService;
import com.biperf.core.service.process.impl.ProcessInvocationAssociationRequest;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

public class ProcessCommentLogController extends BaseController
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // Get selected ProcessInvocation
    ProcessInvocation processInvocation = null;

    Long processInvocationId = null;

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
        processInvocationId = new Long( (String)clientStateMap.get( "processInvocationId" ) );
      }
      catch( ClassCastException cce )
      {
        processInvocationId = (Long)clientStateMap.get( "processInvocationId" );
      }

      if ( processInvocationId != null )
      {
        // Get a list of ProcessInvocation object(s) for this Process
        AssociationRequestCollection assocReqs = new AssociationRequestCollection();
        assocReqs.add( new ProcessInvocationAssociationRequest( ProcessInvocationAssociationRequest.ALL ) );
        processInvocation = getProcessInvocationService().getProcessInvocationById( processInvocationId, assocReqs );
        request.setAttribute( "process", processInvocation.getProcess() );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // Set it to the request object
    request.setAttribute( "processInvocation", processInvocation );

    // Add non-null non-empty string comment(s)
    List comments = new ArrayList();
    if ( processInvocation != null && processInvocation.getProcessInvocationComments() != null )
    {
      Iterator iter = processInvocation.getProcessInvocationComments().iterator();
      while ( iter.hasNext() )
      {
        ProcessInvocationComment pic = (ProcessInvocationComment)iter.next();
        if ( pic != null && pic.getComments() != null && !pic.getComments().equals( "" ) )
        {
          comments.add( pic );
        }
      }
    }

    request.setAttribute( "processComments", comments );

  }

  /**
   * Get the ProcessInvocationService from the beanLocator.
   * 
   * @return ProcessInvocationService
   */
  private ProcessInvocationService getProcessInvocationService()
  {
    return (ProcessInvocationService)getService( ProcessInvocationService.BEAN_NAME );
  }
}

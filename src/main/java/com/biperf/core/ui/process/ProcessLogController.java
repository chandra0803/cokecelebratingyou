
package com.biperf.core.ui.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.domain.process.ProcessInvocationParameter;
import com.biperf.core.domain.process.ProcessInvocationParameterValue;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.process.ProcessInvocationService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.process.impl.ProcessInvocationAssociationRequest;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.util.StringUtils;

public class ProcessLogController extends BaseController
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
    // Get selected Process and ProcessInvocation(s)
    List processInvocations = new ArrayList();
    List removedProcessInvocations = new ArrayList();
    Long processId = null;
    int size = 0;
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
        String processIdString = (String)clientStateMap.get( "processId" );
        if ( processIdString != null && !processIdString.equals( "" ) )
        {
          processId = new Long( processIdString );
        }
      }
      catch( ClassCastException cce )
      {
        processId = (Long)clientStateMap.get( "processId" );
      }

      if ( processId != null )
      {
        Process process = getProcessService().getProcessById( processId );
        request.setAttribute( "process", process );

        ParamEncoder paramEncoder = new ParamEncoder( "processInvocation" );
        String pageParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_PAGE );
        String sortParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_SORT );
        String orderParameterName = paramEncoder.encodeParameterName( TableTagParameters.PARAMETER_ORDER );

        String pageNumString = request.getParameter( pageParameterName );
        int pageSize = 20;

        // Get a list of ProcessInvocation object(s) for this Process
        AssociationRequestCollection assocReqs = new AssociationRequestCollection();
        assocReqs.add( new ProcessInvocationAssociationRequest( ProcessInvocationAssociationRequest.ALL ) );
        int pageNumber = 1;
        if ( pageNumString != null )
        {
          pageNumber = Integer.parseInt( pageNumString );
        }
        processInvocations = getProcessInvocationService().getProcessInvocationsByProcessWithAssociations( process, assocReqs, pageNumber, pageSize );

        size = getProcessInvocationService().getProcessInvocationCountByProcess( process );
        // iterate the processInvocationParameters and add file name associated to file id
        for ( Iterator iter = processInvocations.iterator(); iter.hasNext(); )
        {
          ProcessInvocation invoc = (ProcessInvocation)iter.next();
          for ( Iterator iterator = invoc.getProcessInvocationParameters().iterator(); iterator.hasNext(); )
          {
            ProcessInvocationParameter param = (ProcessInvocationParameter)iterator.next();
            if ( param.getProcessParameterName().equals( "importFileId" ) )
            {
              ProcessInvocationParameterValue paramValue = (ProcessInvocationParameterValue)param.getProcessInvocationParameterValues().iterator().next();
              String fileId = paramValue.getValue();
              if ( StringUtils.isEmpty( fileId ) )
              {
                removedProcessInvocations.add( invoc );
                continue;
              }
              ImportFile file = getImportService().getImportFile( new Long( fileId ) );
              // BugFix 18443.Skip those process Invocations whose File refferences are deleted
              // abruptly!
              if ( file == null )
              {
                removedProcessInvocations.add( invoc );
                continue;
              }
              String fileName = file.getFileName();
              ProcessInvocationParameterValue value = new ProcessInvocationParameterValue();
              value.setValue( fileName );
              ProcessInvocationParameter parameter = new ProcessInvocationParameter();
              parameter.setProcessParameterName( "Import File Name" );
              parameter.addProcessInvocationParameterValue( value );
              invoc.addProcessInvocationParameter( parameter );
              break;
            }
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    for ( int i = 0; i < removedProcessInvocations.size(); i++ )
    {
      for ( int j = 0; j < processInvocations.size(); j++ )
      {
        if ( ( (ProcessInvocation)removedProcessInvocations.get( i ) ).getId().longValue() == ( (ProcessInvocation)processInvocations.get( j ) ).getId().longValue() )
        {
          processInvocations.remove( j );
          break;
        }
      }

    }
    // Set it to the request object
    request.setAttribute( "processLog", processInvocations );
    request.setAttribute( "size", size );
  }

  /**
   * Get the ProcessService from the beanLocator.
   * 
   * @return ProcessService
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
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

  /**
   * Get the ImportService from the beanLocator.
   * 
   * @return ImportService
   */
  private ImportService getImportService()
  {
    return (ImportService)getService( ImportService.BEAN_NAME );
  }
}

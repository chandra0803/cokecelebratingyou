
package com.biperf.core.ui.homepage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ResourceCenterAction. purpose of this class is used to download excel file
 * from Resource center
 *
 */

public class ResourceCenterAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ResourceCenterAction.class );

  @SuppressWarnings( "rawtypes" )
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String password = ClientStatePasswordManager.getGlobalPassword();
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      String fileNamePath = (String)clientStateMap.get( "filePath" );
      logger.error( "ResourceCenterAction - fileNamePath :" + fileNamePath );
      String fileName = new File( fileNamePath ).getName();
      File file = new File( getAppDataDir(), fileName );
      try
      {
        FileUtils.copyURLToFile( new URL( fileNamePath ), file );
        response.setContentType( "application/vnd.ms-excel" );
        response.setCharacterEncoding( "UTF-8" );
        response.setHeader( "Pragma", "public" );
        response.setHeader( "Cache-Control", "max-age=0" );
        response.setHeader( "Content-disposition", "attachment; filename=" + file.getName() );
        FileInputStream inStream = new FileInputStream( file );
        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ( ( bytesRead = inStream.read( buffer ) ) != -1 )
        {
          outStream.write( buffer, 0, bytesRead );
        }
        inStream.close();
        outStream.close();
      }
      catch( IOException ex )
      {
        file.delete();
        logger.error( ex.getMessage() );
      }
      file.delete();
    }
    catch( InvalidClientStateException ex )
    {
      logger.error( ex.getMessage() );
    }
    return null;
  }

  private static String getAppDataDir()
  {
    StringBuilder builder = new StringBuilder();
    builder.append( System.getProperty( ActionConstants.SYS_APPDATADIR ) );
    builder.append( File.separator );
    String path = builder.toString();
    if ( path != null && !new File( path ).exists() )
    {
      new File( path ).mkdirs();
    }
    return path;
  }

  public static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}


package com.biperf.core.ui.reports.file;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.biperf.core.domain.enums.FileStoreType;
import com.biperf.core.domain.filestore.FileStore;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.filestore.FileStoreService;
import com.biperf.core.strategy.AWSReportDownloadStrategy;
import com.biperf.core.strategy.ReportDownloadStrategy;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;

public class ReportFileDownloadAction extends DispatchAction
{
  private static final Log logger = LogFactory.getLog( ReportFileDownloadAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    FileStore file = getFileStore( request );

    writeContent( response, file );

    return null;
  }

  private void writeContent( HttpServletResponse response, FileStore file )
  {
    try
    {
      writeHeader( response, file );
      response.getOutputStream().write( new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF } );
      logger.error( "****writeFileData start userId=" + UserManager.getUserId() + " internalFileName=" + file.getInternalFileName() );
      if ( AwsUtils.isAws() )
      {
        getAwsReportDownloadStrategy().awsWriteFileData( file.getInternalFileName(), response );
      }
      else
      {
        getWebDavReportDownloadStrategy().writeFileData( file.getInternalFileName(), response );
      }
      logger.error( "****writeFileData finish userId=" + UserManager.getUserId() + " internalFileName=" + file.getInternalFileName() );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e );
    }
  }

  private void writeHeader( HttpServletResponse response, FileStore file ) throws IOException
  {
    String contentType = null;
    if ( file.getFileStoreType().getCode().equals( FileStoreType.CSV ) )
    {
      contentType = "text/csv; charset=UTF-8";
    }
    else
    { // default
      contentType = "text/csv; charset=UTF-8";
    }
    response.setContentType( contentType );
    response.setCharacterEncoding( "UTF-8" );
    response.setHeader( "Pragma", "public" );
    response.setHeader( "Cache-Control", "max-age=0" );
    response.setHeader( "Content-disposition", "attachment; filename=" + file.getUserFileName() );

    response.flushBuffer();
  }

  private FileStore getFileStore( HttpServletRequest request )
  {
    Long fileStoreId = (Long)ClientStateUtils.getParameterValueAsObject( request, ClientStateUtils.getClientStateMap( request ), "fileStoreId" );
    return getFileStoreService().get( fileStoreId );
  }

  private ReportDownloadStrategy getWebDavReportDownloadStrategy()
  {
    return (ReportDownloadStrategy)BeanLocator.getBean( ReportDownloadStrategy.WEBDAV );
  }

  private AWSReportDownloadStrategy getAwsReportDownloadStrategy()
  {
    return (AWSReportDownloadStrategy)BeanLocator.getBean( AWSReportDownloadStrategy.AWS );
  }

  private FileStoreService getFileStoreService()
  {
    return (FileStoreService)BeanLocator.getBean( FileStoreService.BEAN_NAME );
  }
}

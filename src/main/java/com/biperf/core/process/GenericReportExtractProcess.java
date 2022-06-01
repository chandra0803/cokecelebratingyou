
package com.biperf.core.process;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.FileStoreType;
import com.biperf.core.domain.filestore.FileStore;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.message.Message;
import com.biperf.core.service.SAO;
import com.biperf.core.service.filestore.FileStoreService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.utils.BeanLocator;

public class GenericReportExtractProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "genericReportExtractProcess";
  private static final Log log = LogFactory.getLog( GenericReportExtractProcess.class );
  private FileStoreService fileStoreService;

  // properties set from jobDataMap
  private String beanName = null;
  private String methodName = null;
  private Long userId = null;
  private String userFilename = null;
  private String internalFilename = null;
  private Object reportParameters = null;

  public GenericReportExtractProcess()
  {
    super();
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void onExecute()
  {
    try
    {
      // generate the file type and internal name
      FileStoreType csv = FileStoreType.lookup( FileStoreType.CSV );
      internalFilename = getFileStoreService().generateInternalFileName( userId, csv );
      ( (Map)reportParameters ).put( "internalFilename", internalFilename );
      // call report service method
      // proc should return a 'success' message of some type
      invokeExtractMethod();
      // record the FileStore
      FileStore fileStore = fileStoreService.save( userId, buildFileStore() );
      // send email alert to the user
      sendEmail( fileStore );
    }
    catch( Throwable e )
    {
      log.error( "Error processing report extraction for userId: " + userId + " with parms [ " + reportParameters + " ] ", e );
    }
  }

  @SuppressWarnings( { "unchecked" } )
  private void invokeExtractMethod() throws Exception
  {
    Method method = getInvocationMethod();
    Map<String, Object> parameters = (Map<String, Object>)reportParameters;
    method.invoke( getService(), parameters );
  }

  private Method getInvocationMethod() throws NoSuchMethodException, SecurityException
  {
    return getService().getClass().getDeclaredMethod( methodName, Map.class );
  }

  private SAO getService()
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  private FileStore buildFileStore()
  {
    FileStore file = new FileStore();

    file.setDateGenerated( new java.util.Date() );
    file.setDownloaded( false );
    file.setFileStoreType( FileStoreType.lookup( FileStoreType.CSV ) );
    file.setInternalFileName( internalFilename );
    file.setUserFileName( userFilename );
    return file;
  }

  private void sendEmail( FileStore fileStore )
  {
    Message message = messageService.getMessageByCMAssetCode( MessageService.REPORT_EXTRACT_MESSAGE_CM_ASSET_CODE );
    Mailing mailing = mailingService.buildReportExtractNotificationMailing( fileStore, message );
    mailingService.submitMailing( mailing, null );
  }

  public FileStoreService getFileStoreService()
  {
    return fileStoreService;
  }

  public void setFileStoreService( FileStoreService fileStoreService )
  {
    this.fileStoreService = fileStoreService;
  }

  public String getBeanName()
  {
    return beanName;
  }

  public void setBeanName( String beanName )
  {
    this.beanName = beanName;
  }

  public String getMethodName()
  {
    return methodName;
  }

  public void setMethodName( String methodName )
  {
    this.methodName = methodName;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getUserFilename()
  {
    return userFilename;
  }

  public void setUserFilename( String userFilename )
  {
    this.userFilename = userFilename;
  }

  public Object getReportParameters()
  {
    return reportParameters;
  }

  public void setReportParameters( Object reportParameters )
  {
    this.reportParameters = reportParameters;
  }
}

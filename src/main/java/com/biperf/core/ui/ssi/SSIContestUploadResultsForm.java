
package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.upload.FormFile;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestUploadResultsForm extends BaseForm
{

  private static final String FILE_TYPE_XLS = ".xls";
  private static final String FILE_TYPE_CSV = ".csv";
  private static final String FILE_TYPE_XLSX = ".xlsx";
  private FormFile ssiHiddenUpload;
  private String filename;
  private String ssiUploadActivityDate;
  private String saveAndSendProgressUpdate;
  private static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;

  public FormFile getSsiHiddenUpload()
  {
    return ssiHiddenUpload;
  }

  public void setSsiHiddenUpload( FormFile ssiHiddenUpload )
  {
    this.ssiHiddenUpload = ssiHiddenUpload;
  }

  public String getFilename()
  {
    return filename;
  }

  public void setFilename( String filename )
  {
    this.filename = filename;
  }

  public String getSsiUploadActivityDate()
  {
    return ssiUploadActivityDate;
  }

  public void setSsiUploadActivityDate( String ssiUploadActivityDate )
  {
    this.ssiUploadActivityDate = ssiUploadActivityDate;
  }

  public String getSaveAndSendProgressUpdate()
  {
    return saveAndSendProgressUpdate;
  }

  public void setSaveAndSendProgressUpdate( String saveAndSendProgressUpdate )
  {
    this.saveAndSendProgressUpdate = saveAndSendProgressUpdate;
  }

  public List<String> validate()
  {

    List<String> validationErrors = new ArrayList<String>();

    // only allow csv/xls/xlsx files to upload
    if ( ! ( ssiHiddenUpload.getFileName().endsWith( FILE_TYPE_CSV ) || ssiHiddenUpload.getFileName().endsWith( FILE_TYPE_XLS ) || ssiHiddenUpload.getFileName().endsWith( FILE_TYPE_XLSX ) ) )
    {
      validationErrors.add( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.UPLOAD_RESULTS_FILE_TYPE" ) );
    }

    if ( ssiHiddenUpload.getFileSize() == 0 )
    {
      validationErrors.add( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.EMPTY_FILE_UPLOAD_ERR" ) );
    }

    int fileSizeLimitInMB = getSystemVariableService().getPropertyByName( SystemVariableService.SSI_PROGRESS_UPLOAD_SIZE_LIMIT ).getIntVal();
    int fileSizeLimitInBytes = MEGABYTES_TO_BYTES_MULTIPLIER * fileSizeLimitInMB;
    if ( ssiHiddenUpload.getFileSize() > fileSizeLimitInBytes )
    {
      validationErrors.add( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.UPLOAD_FILE_SIZE" ) + fileSizeLimitInMB );
    }

    return validationErrors;
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}

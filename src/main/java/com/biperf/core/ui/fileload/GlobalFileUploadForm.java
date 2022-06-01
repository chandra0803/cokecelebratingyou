/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/fileload/GlobalFileUploadForm.java,v $
 */

package com.biperf.core.ui.fileload;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;

/*
 * ImportFileListCriteriaForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug
 * 30, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class GlobalFileUploadForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final Log log = LogFactory.getLog( GlobalFileUploadForm.class );

  /**
   * A prefix of the import file name.
   */
  private String fileNameCriteria;

  /**
   * The status of the import file; for example, "staged."
   */
  private String statusCriteria;

  /**
   * The type of the import file; for example, "participant import file."
   */
  private String fileTypeCriteria;

  /**
   * The earliest date on which information about the import files in the import file list was
   * loaded into this application's staging tables.
   */
  private String startDateCriteria = DateUtils.displayDateFormatMask;

  /**
   * The latest date on which information about the import files in the import file list was loaded
   * into this application's staging tables.
   */
  private String endDateCriteria = DateUtils.displayDateFormatMask;

  // this is to hold checked files for delete
  private String[] deleteFiles;

  private FormFile theFile;

  private String method;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public FormFile getTheFile()
  {
    return theFile;
  }

  public void setTheFile( FormFile theFile )
  {
    this.theFile = theFile;
  }

  /**
   * @return String[]
   */
  public String[] getDeleteFiles()
  {
    return deleteFiles;
  }

  /**
   * @param deleteFiles
   */
  public void setDeleteFiles( String[] deleteFiles )
  {
    this.deleteFiles = deleteFiles;
  }

  /**
   * Returns the latest date on which information about the import files in the import file list was
   * loaded into this application's staging tables.
   * 
   * @return the latest date on which information about the import files in the import file list was
   *         loaded into this application's staging tables.
   */
  public String getEndDateCriteria()
  {
    return endDateCriteria;
  }

  /**
   * Returns a user-specified prefix of the import file name.
   * 
   * @return a user-specified prefix of the import file name.
   */
  public String getFileNameCriteria()
  {
    return fileNameCriteria;
  }

  /**
   * Returns the import file status.
   * 
   * @return the import file status code.
   */
  public String getStatusCriteria()
  {
    return statusCriteria;
  }

  /**
   * Returns the import file type.
   * 
   * @return the import file type code.
   */
  public String getFileTypeCriteria()
  {
    return fileTypeCriteria;
  }

  /**
   * Returns the earliest date on which information about the import files in the import file list
   * was loaded into this application's staging tables.
   * 
   * @return the earliest date on which information about the import files in the import file list
   *         was loaded into this application's staging tables.
   */
  public String getStartDateCriteria()
  {
    return startDateCriteria;
  }

  /**
   * Sets the latest date on which information about the import files in the import file list was
   * loaded into this application's staging tables.
   * 
   * @param endDateCriteria the latest date on which information about the import files in the
   *          import file list was loaded into this application's staging tables.
   */
  public void setEndDateCriteria( String endDateCriteria )
  {
    this.endDateCriteria = endDateCriteria;
  }

  /**
   * Sets a user-specified prefix of the import file name.
   * 
   * @param fileNameCriteria a user-specified prefix of the import file name.
   */
  public void setFileNameCriteria( String fileNameCriteria )
  {
    this.fileNameCriteria = fileNameCriteria;
  }

  /**
   * Sets the import file status.
   * 
   * @param statusCriteria an import file status code.
   */
  public void setStatusCriteria( String statusCriteria )
  {
    this.statusCriteria = statusCriteria;
  }

  /**
   * Sets the import file type.
   * 
   * @param fileTypeCriteria an import file type code.
   */
  public void setFileTypeCriteria( String fileTypeCriteria )
  {
    this.fileTypeCriteria = fileTypeCriteria;
  }

  /**
   * Sets the earliest date on which information about the import files in the import file list was
   * loaded into this application's staging tables.
   * 
   * @param startDateCriteria the earliest date on which information about the import files in the
   *          import file list was loaded into this application's staging tables.
   */
  public void setStartDateCriteria( String startDateCriteria )
  {
    this.startDateCriteria = startDateCriteria;
  }

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * Validates the data in this form.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   * @return <code>ActionErrors</code> object that encapsulates any validation errors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( startDateCriteria != null && startDateCriteria.trim().length() > 0 && endDateCriteria != null && endDateCriteria.trim().length() > 0 )
    {
      try
      {
        Date startDate = DateUtils.toStartDate( startDateCriteria );
        Date endDate = DateUtils.toEndDate( endDateCriteria );

        if ( startDate.after( endDate ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.DATE_RANGE" ) );
        }
      }
      catch( ParseException e )
      {
        // This exception should never occur because if the user enters a start
        // date or an end date, then the call to super.validate above will
        // ensure that the entered string represents a valid date.
        if ( log.isErrorEnabled() )
        {
          log.error( "Either startDateCriteria or endDateCriteria does not represent a valid date.  " + "startDateCriteria = \"" + startDateCriteria + "\", " + "endDateCriteria = \"" + endDateCriteria
              + "\"" );
        }
      }
    }

    return actionErrors;
  }
}

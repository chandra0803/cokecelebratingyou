/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/fileload/ImportFileListSearchController.java,v $
 */

package com.biperf.core.ui.fileload;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.util.StringUtils;

/*
 * ImportFileListSearchController <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug
 * 31, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ImportFileListSearchController extends BaseController
{
  private SystemVariableService systemVariableService;
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the View File Loads page.
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    // By default, select the "staged" item from the import file status picklist
    // and also "budget" from the type list because everyone can do budget fileloads.
    String servletPath = request.getServletPath();
    if ( servletPath.indexOf( "displayImportFileList" ) != -1 )
    {
      ImportFileListCriteriaForm form = getForm( request );
      if ( StringUtils.isEmpty( form.getStatusCriteria() ) )
      {
        form.setStatusCriteria( ImportFileStatusType.STAGED );
      }
    }

    // Get the picklist items.
    List fileTypes = new ArrayList( ImportFileTypeType.getList() );
    fileTypes.remove( ImportFileTypeType.lookup( ImportFileTypeType.PRODUCT_CLAIM ) );
    // remove certain file types based on role
    Set roles = new HashSet();
    roles.add( AuthorizationService.ROLE_CODE_BI_ADMIN );
    roles.add( AuthorizationService.ROLE_CODE_PROCESS_TEAM );
    roles.add( AuthorizationService.ROLE_CODE_PROJ_MGR );

    request.setAttribute( "importFileTypeList", fileTypes );
    request.setAttribute( "importFileStatusList", ImportFileStatusType.getList() );

    // Get the import file list.
    List importFileList = getImportFileList( request );
    request.setAttribute( "importFileList", importFileList );
    request.setAttribute( "pageSize", new Integer( PresentationUtils.getDisplayTablePageSize( importFileList.size() ) ) );

    PropertySetItem globalStandalone = getSystemVariableService().getPropertyByName( SystemVariableService.ENABLE_GLOBAL_STANDALONE );
    request.setAttribute( "allowGlobalFileUpload", Boolean.valueOf( globalStandalone.getBooleanVal() ) );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private ImportFileListCriteriaForm getForm( HttpServletRequest request )
  {
    return (ImportFileListCriteriaForm)request.getAttribute( "importFileListCriteriaForm" );
  }

  /**
   * Returns the specified import file list.
   * 
   * @param request an HTTP request.
   * @return the specified import file list.
   */
  private List getImportFileList( HttpServletRequest request )
  {
    FormAdapter form = new FormAdapter( getForm( request ) );

    String fileNamePrefix = form.getFileNamePrefix();
    ImportFileTypeType importFileType = form.getImportFileType();
    ImportFileStatusType importFileStatus = form.getImportFileStatus();
    Date startDate = form.getStartDate();
    Date endDate = form.getEndDate();

    return getImportService().getImportFiles( fileNamePrefix, importFileType, importFileStatus, startDate, endDate );
  }

  /**
   * Returns the import service.
   * 
   * @return a reference to the import service.
   */
  private ImportService getImportService()
  {
    return (ImportService)getService( ImportService.BEAN_NAME );
  }

  /**
   * Returns the authorization service.
   * 
   * @return a reference to the authorization service.
   */
  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Adapts an {@link ImportFileListCriteriaForm} object to the method
   * <code>ImportService.getImportFiles(String, ImportFileTypeType,
   * ImportFileStatusType, Date, Date)</code>.
   */
  private static class FormAdapter
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private static final Log logger = LogFactory.getLog( FormAdapter.class );

    /**
     * The import file list criteria form that is adapted by this form adapter.
     */
    private ImportFileListCriteriaForm form;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------

    /**
     * Constructs a <code>FormAdapter</code> object.
     * 
     * @param form the form to adapt.
     */
    FormAdapter( ImportFileListCriteriaForm form )
    {
      if ( form == null )
      {
        throw new IllegalArgumentException( "Parameter \"form\" is null." );
      }

      this.form = form;
    }

    /**
     * Returns the end date entered by the user. Returns null if the user the user did not enter an
     * end date or if the user entered an invalid end date.
     * <p>
     * The end date is the latest date on which information about an import file was loaded into the
     * application's staging tables.
     * </p>
     * 
     * @return the end date entered by the user.
     */
    public Date getEndDate()
    {
      Date endDate = null;

      String endDateString = form.getEndDateCriteria();
      if ( endDateString != null && endDateString.length() > 0 && !endDateString.equals( DateUtils.displayDateFormatMask ) )
      {
        try
        {
          endDate = DateUtils.toEndDate( endDateString );
        }
        catch( ParseException e )
        {
          logger.warn( "Invalid end date." );
        }
      }

      return endDate;
    }

    /**
     * Returns a prefix of the import file name or null if the user did not enter a file name
     * prefix.
     * 
     * @return a prefix of the import file name.
     */
    public String getFileNamePrefix()
    {
      return form.getFileNameCriteria();
    }

    /**
     * Returns the import file status or null if the user did not select an import file status.
     * 
     * @return the import file type.
     */
    public ImportFileStatusType getImportFileStatus()
    {
      ImportFileStatusType importFileStatus = null;

      String importFileStatusCode = form.getStatusCriteria();
      if ( importFileStatusCode != null && importFileStatusCode.length() > 0 && !"ALL".equals( importFileStatusCode ) )
      {
        importFileStatus = ImportFileStatusType.lookup( importFileStatusCode );
      }

      return importFileStatus;
    }

    /**
     * Returns the import file type or null if the user did not select an import file type.
     * 
     * @return the import file type.
     */
    public ImportFileTypeType getImportFileType()
    {
      ImportFileTypeType importFileType = null;

      String importFileTypeCode = form.getFileTypeCriteria();
      if ( importFileTypeCode != null && importFileTypeCode.length() > 0 && !"ALL".equals( importFileTypeCode ) )
      {
        importFileType = ImportFileTypeType.lookup( importFileTypeCode );
      }

      return importFileType;
    }

    /**
     * Returns the start date entered by the user. Returns null if the user did not enter a start
     * date of the user entered an invalid start date.
     * <p>
     * The start date is the earliest date on which information about an import file was loaded into
     * the application's staging tables.
     * </p>
     * 
     * @return the start date entered by the user.
     */
    public Date getStartDate()
    {
      Date startDate = null;

      String startDateString = form.getStartDateCriteria();
      if ( startDateString != null && startDateString.length() > 0 && !startDateString.equals( DateUtils.displayDateFormatMask ) )
      {
        try
        {
          startDate = DateUtils.toStartDate( startDateString );
        }
        catch( ParseException e )
        {
          logger.warn( "Invalid start date." );
        }
      }

      return startDate;
    }
  }

  private SystemVariableService getSystemVariableService()
  {
    if ( systemVariableService == null )
    {
      systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    }
    return systemVariableService;
  }
}

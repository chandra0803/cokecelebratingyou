/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/fileload/ImportRecordListController.java,v $
 */

package com.biperf.core.ui.fileload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.displaytag.tags.TableTagParameters;

import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecord;
import com.biperf.core.domain.fileload.ProductClaimImportRecord;
import com.biperf.core.domain.fileload.QuizImportRecord;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/*
 * ImportRecordListController <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 21, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ImportRecordListController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private ImportService importService = (ImportService)getService( ImportService.BEAN_NAME );

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Import Record List page.
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    // Get the import file.
    Long importFileId = null;
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
        String id = (String)clientStateMap.get( "importFileId" );
        importFileId = new Long( id );
      }
      catch( ClassCastException cce )
      {
        importFileId = (Long)clientStateMap.get( "importFileId" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    ImportFile importFile = importService.getImportFile( importFileId );
    String fileType = importFile.getFileType().getCode();
    Enumeration paramNames = request.getParameterNames();
    int pageNum = 1;
    String pageValue = "";
    while ( paramNames.hasMoreElements() )
    {
      String name = (String)paramNames.nextElement();
      if ( name != null && name.startsWith( "d-" ) && name.endsWith( "-p" ) )
      {
        pageValue = request.getParameter( name );
        if ( pageValue != null )
        {
          pageNum = Integer.parseInt( pageValue );
        }
      }
    }
    // To fix Bug # 14891
    String export = request.getParameter( "export" );
    int pageSize = 0;

    if ( request.getParameter( TableTagParameters.PARAMETER_EXPORTING ) != null )
    {
      int recordcount = importFile.getImportRecordCount();
      pageSize = recordcount;
      pageNum = 1;
    }
    else
    {
      pageSize = 40;
    }

    /*
     * if(export!=null && export.equals( "false" )) { pageSize=40; }else if (pageValue!=null) {
     * pageSize=40; }else { int recordcount = importFile.getImportRecordCount(); pageSize =
     * recordcount; }
     */
    // Lists the records with errors by page
    if ( request.getParameter( "showErrors" ) != null && request.getParameter( "showErrors" ).equals( "true" ) )
    {
      List recordsWithErrorsbyPage = importService.getRecordsWithErrorByPage( importFileId, pageNum, pageSize, fileType, new BaseAssociationRequest()
      {
        public void execute( Object domainObject )
        {
          List importRecords = (List)domainObject;

          // Hydrate each import record's import record errors.
          Iterator iter = importRecords.iterator();
          while ( iter.hasNext() )
          {
            ImportRecord importRecord = (ImportRecord)iter.next();
            initialize( importRecord.getImportRecordErrors() );

            if ( importRecord instanceof ProductClaimImportRecord )
            {
              initialProductClaimRecords( (ProductClaimImportRecord)importRecord );
            }
          }
        }

        private void initialProductClaimRecords( ProductClaimImportRecord importRecord )
        {

          initialize( importRecord.getProductClaimImportFieldRecords() );
          initialize( importRecord.getProductClaimImportProductRecords() );

        }
      } );
      importFile.setRecordsWithErrorsByPage( recordsWithErrorsbyPage );

      // Sort the quiz error records by order of insertion which is
      // supposed to be in the order of Header/Question/Answer(s) records
      if ( recordsWithErrorsbyPage.size() > 0 && importFile.getFileType().isQuiz() )
      {
        List quizErrorList = new ArrayList( recordsWithErrorsbyPage );
        Collections.sort( quizErrorList, new Comparator()
        {
          public int compare( Object object, Object object1 )
          {
            QuizImportRecord quizImportRecord1 = (QuizImportRecord)object;
            QuizImportRecord quizImportRecord2 = (QuizImportRecord)object1;

            return quizImportRecord1.getId().compareTo( quizImportRecord2.getId() );
          }
        } );
        importFile.setRecordsWithErrorsByPage( new ArrayList( quizErrorList ) );
      }
    }
    else
    // Lists the records without errors by page
    {
      List recordsbyPage = importService.getRecordsbyPage( importFileId, pageNum, pageSize, fileType, new BaseAssociationRequest()
      {
        public void execute( Object domainObject )
        {
          List importRecords = (List)domainObject;

          // Hydrate each import record's import record errors.
          Iterator iter = importRecords.iterator();
          while ( iter.hasNext() )
          {
            ImportRecord importRecord = (ImportRecord)iter.next();
            initialize( importRecord.getImportRecordErrors() );
            if ( importRecord instanceof ProductClaimImportRecord )
            {
              initialProductClaimRecords( (ProductClaimImportRecord)importRecord );
            }
          }
        }

        private void initialProductClaimRecords( ProductClaimImportRecord importRecord )
        {

          initialize( importRecord.getProductClaimImportFieldRecords() );
          initialize( importRecord.getProductClaimImportProductRecords() );

        }
      } );
      importFile.setRecordsWithoutErrorsByPage( recordsbyPage );
    }

    // Sort the quiz records by order of insertion which is
    // supposed to be in the order of Header/Question/Answer(s) records
    if ( importFile.getFileType().isQuiz() )
    {
      List quizList = new ArrayList( importFile.getQuizImportRecords() );

      Collections.sort( quizList, new Comparator()
      {
        public int compare( Object object, Object object1 )
        {
          QuizImportRecord quizImportRecord1 = (QuizImportRecord)object;
          QuizImportRecord quizImportRecord2 = (QuizImportRecord)object1;

          return quizImportRecord1.getId().compareTo( quizImportRecord2.getId() );
        }
      } );
      importFile.setQuizImportRecords( new LinkedHashSet( quizList ) );
    }

    request.setAttribute( "importFile", importFile );

    String showErrors = request.getParameter( "showErrors" );
    if ( null != showErrors && new Boolean( showErrors ).booleanValue() )
    {
      request.setAttribute( "importRecords", importFile.getRecordsWithErrorsByPage() );
      request.setAttribute( "importRecordCount", new Integer( importFile.getImportRecordErrorCount() ) );
      request.setAttribute( "showOnlyImportRecordsWithErrors", "true" );
      request.setAttribute( "pageSize", pageSize );
    }
    else
    {
      request.setAttribute( "importRecords", importFile.getRecordsWithoutErrorsByPage() );
      request.setAttribute( "importRecordCount", new Integer( importFile.getImportRecordWithoutErrorCount() ) );
      request.setAttribute( "pageSize", pageSize );
    }

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.SSO_UNIQUE_ID ).getStringVal().equalsIgnoreCase( "SSO ID" ) )
    {
      request.setAttribute( "showSSOId", true );
    }
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}

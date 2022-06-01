/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$ */

package com.biperf.core.ui.fileload;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ArrayUtil;

/**
 * Action class for file operations.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>attada</td>
 * <td>April 17, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ImportFileListAction extends BaseDispatchAction
{
  /**
   * Deletes the specified import file.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward deleteImportFiles( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    if ( isTokenValid( request, true ) )
    {
      ImportFileListCriteriaForm importFileListCriteriaForm = (ImportFileListCriteriaForm)form;

      if ( importFileListCriteriaForm.getDeleteFiles() != null && importFileListCriteriaForm.getDeleteFiles().length > 0 )
      {
        deleteFiles( importFileListCriteriaForm.getDeleteFiles() );
      }

    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * delete a list of files
   * 
   * @param fileIds
   */
  private void deleteFiles( String[] fileIds )
  {
    // Convert String[] of fileIds to Long[]
    List fileIdList = ArrayUtil.convertStringArrayToListOfLongObjects( fileIds );
    getImportService().deleteImportFiles( fileIdList );
  }

  /**
   * Returns a reference to the Import service.
   * 
   * @return a reference to the Import service.
   */
  private ImportService getImportService()
  {
    return (ImportService)getService( ImportService.BEAN_NAME );
  }
}

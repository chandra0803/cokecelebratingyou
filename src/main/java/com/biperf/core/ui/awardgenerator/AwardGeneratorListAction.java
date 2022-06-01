
package com.biperf.core.ui.awardgenerator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardgenerator.AwardGeneratorService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ArrayUtil;

/**
 * Action class for Award Generator List operations.
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
 * <td>chowdhur</td>
 * <td>Jul 08, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class AwardGeneratorListAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( AwardGeneratorListAction.class );

  /**
   *
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteAwardGenerators( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    AwardGeneratorListForm awardGeneratorListForm = (AwardGeneratorListForm)form;

    if ( awardGeneratorListForm.getDeleteAwardGeneratorIds() != null && awardGeneratorListForm.getDeleteAwardGeneratorIds().length > 0 )
    {
      deleteAwardGenerators( awardGeneratorListForm.getDeleteAwardGeneratorIds() );
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * delete a list of awardgenerators
   *
   * @param awardGeneratorIds
   */
  private void deleteAwardGenerators( String[] awardGeneratorIds )
  {
    List awardGeneratorIdList = ArrayUtil.convertStringArrayToListOfLongObjects( awardGeneratorIds );
    try
    {
      getAwardGeneratorService().deleteAwardGenerators( awardGeneratorIdList );
    }
    catch( ServiceErrorException e )
    {
      // Exception thrown if the awardgenerator to be deleted is assigned
      logger.error( e.getMessage(), e );
    }
  }

  private AwardGeneratorService getAwardGeneratorService()
  {
    return (AwardGeneratorService)getService( AwardGeneratorService.BEAN_NAME );
  }
}

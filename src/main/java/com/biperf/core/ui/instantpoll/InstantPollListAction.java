/**
 * 
 */

package com.biperf.core.ui.instantpoll;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.instantpoll.InstantPollService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ArrayUtil;

/**
 * @author poddutur
 *
 */
public class InstantPollListAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( InstantPollListAction.class );

  /**
    * RETURN_ACTION_URL_PARAM
    */
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  /**
   * Delete instant polls
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward remove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    InstantPollForm instantPollForm = (InstantPollForm)form;
    @SuppressWarnings( "rawtypes" )
    List instantPollIdList = new ArrayList();

    if ( instantPollForm.getDeleteInstantPolls() != null )
    {
      instantPollIdList = ArrayUtil.convertStringArrayToListOfLongObjects( instantPollForm.getDeleteInstantPolls() );
    }
    else
    {
      return mapping.findForward( forwardTo );
    }

    try
    {
      getInstantPollService().deleteInstantPolls( instantPollIdList );
    }
    catch( ServiceErrorException e )
    {
      logger.error( e.getMessage(), e );
    }

    return mapping.findForward( forwardTo );
  }

  private InstantPollService getInstantPollService()
  {
    return (InstantPollService)getService( InstantPollService.BEAN_NAME );
  }

}

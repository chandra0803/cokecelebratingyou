/**
 * 
 */

package com.biperf.core.ui.forum;

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
import com.biperf.core.service.forum.ForumTopicService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ArrayUtil;

/**
 * @author poddutur
 * 
 */
public class ForumTopicListAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( ForumTopicListAction.class );

  /**
    * RETURN_ACTION_URL_PARAM
    */
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  /**
   * Delete forum topics
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

    ForumTopicListForm forumTopicListForm = (ForumTopicListForm)form;
    @SuppressWarnings( "rawtypes" )
    List forumTopicIdList = new ArrayList();

    if ( forumTopicListForm.getDeleteForumTopics() != null )
    {
      forumTopicIdList = ArrayUtil.convertStringArrayToListOfLongObjects( forumTopicListForm.getDeleteForumTopics() );
    }
    else
    {
      return mapping.findForward( forwardTo );
    }

    try
    {
      getForumTopicService().deleteTopics( forumTopicIdList );
    }
    catch( ServiceErrorException e )
    {
      logger.error( e.getMessage(), e );
    }

    return mapping.findForward( forwardTo );
  }

  private ForumTopicService getForumTopicService()
  {
    return (ForumTopicService)getService( ForumTopicService.BEAN_NAME );
  }

}

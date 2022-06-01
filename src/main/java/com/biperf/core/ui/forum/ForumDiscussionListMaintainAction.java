/**
 * 
 */

package com.biperf.core.ui.forum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.forum.ForumRedirectToEditView;
import com.biperf.core.domain.forum.ForumRedirectToEditView.Message;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.forum.ForumDiscussionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.value.forum.ForumDiscussionValueBean;

/**
 * @author poddutur
 *
 */
public class ForumDiscussionListMaintainAction extends BaseDispatchAction
{
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException
  {
    HttpSession session = request.getSession();
    Long topicId = null;
    List<ForumDiscussionValueBean> forumDiscussionList = new ArrayList<ForumDiscussionValueBean>();

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      @SuppressWarnings( "unchecked" )
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      topicId = (Long)clientStateMap.get( "id" );
    }
    else
    {
      topicId = (Long)session.getAttribute( "topicId" );
    }

    forumDiscussionList = getForumDiscussionService().getSortedDiscussionList( topicId );

    for ( ForumDiscussionValueBean discussionList : forumDiscussionList )
    {
      if ( discussionList.getDateCreated() != null )
      {
        String createdDate = DateUtils.toRelativeTimeLapsed( discussionList.getDateCreated() );

        if ( createdDate.equals( "About 1 day ago" ) )
        {
          createdDate = "Yesterday";
        }
        discussionList.setCreatedDate( createdDate );
      }
      if ( discussionList.getDateReplied() != null )
      {
        String repliedDate = DateUtils.toRelativeTimeLapsed( discussionList.getDateReplied() );
        if ( repliedDate.equals( "About 1 day ago" ) )
        {
          repliedDate = "Yesterday";
        }
        discussionList.setRepliedDate( repliedDate );
      }
    }

    request.setAttribute( "forumDiscussionList", forumDiscussionList );

    return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  public ActionForward removeDiscussionReplies( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException, IOException
  {
    HttpSession session = request.getSession();

    Long commentId = Long.parseLong( request.getParameter( "commentId" ) );
    Long discussionId = (Long)session.getAttribute( "discussionId" );
    Long topicId = (Long)session.getAttribute( "topicId" );
    String topicName = (String)session.getAttribute( "topicName" );
    String discussionName = (String)session.getAttribute( "discussionName" );

    getForumDiscussionService().deleteDiscussionReply( commentId );

    session.setAttribute( "discussionId", discussionId );
    session.setAttribute( "topicId", topicId );
    session.setAttribute( "topicName", topicName );
    session.setAttribute( "discussionName", discussionName );

    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward removeDiscussion( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
      throws ServiceErrorException, InvalidClientStateException, IOException
  {
    Long discussionId = Long.parseLong( request.getParameter( "discussionId" ) );
    Long topicId = (Long)request.getSession().getAttribute( "topicId" );
    ForumRedirectToEditView forumRedirectToEditView = new ForumRedirectToEditView();
    Message message = new Message();

    getForumDiscussionService().deleteDiscussion( discussionId );

    Map parameterMap = new HashMap();
    parameterMap.put( "id", topicId );
    parameterMap.put( "discussionId", discussionId );

    String forumRedirectUrl = RequestUtils.getBaseURI( request ) + ClientStateUtils.generateEncodedLink( "", PageConstants.FORUM_PAGE_URL, parameterMap );

    message.setRedirectUrl( forumRedirectUrl );
    forumRedirectToEditView.setMessage( message );

    super.writeAsJsonToResponse( forumRedirectToEditView, response );

    return null;
  }

  private ForumDiscussionService getForumDiscussionService()
  {
    return (ForumDiscussionService)getService( ForumDiscussionService.BEAN_NAME );
  }

}

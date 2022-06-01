/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.forum.ForumDiscussionReply;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.forum.ForumDiscussionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.UserManager;

/**
 * @author poddutur
 *
 */
public class ForumCommentReplyMaintainAction extends BaseDispatchAction
{
  @SuppressWarnings( "unchecked" )
  @Override
  public ActionForward execute( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ForumDiscussionReplyListForm forumDiscussionReplyListForm = (ForumDiscussionReplyListForm)actionForm;
    HttpSession session = request.getSession();
    Long topicId = null;
    String topicName = null;
    Long discussionId = null;
    String discussionName = null;
    String submitReply;

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      topicId = (Long)clientStateMap.get( "topicId" );
      topicName = (String)clientStateMap.get( "topicName" );
      discussionId = (Long)clientStateMap.get( "discussionId" );
      discussionName = (String)clientStateMap.get( "discussionName" );
    }

    else
    {
      topicId = (Long)session.getAttribute( "topicId" );
      topicName = (String)session.getAttribute( "topicName" );
      discussionId = (Long)session.getAttribute( "discussionId" );
      discussionName = (String)session.getAttribute( "discussionName" );
    }

    ForumDiscussionReply forumDiscussionReply = new ForumDiscussionReply();
    forumDiscussionReply.setParentDiscussionId( discussionId );
    forumDiscussionReply.setDiscussionTitle( discussionName );
    forumDiscussionReply.setDiscussionBody( forumDiscussionReplyListForm.getNotifyMessage() );
    forumDiscussionReply.setStatus( "A" );

    ForumTopic forumTopic = new ForumTopic();
    User user = new User();
    user.setId( UserManager.getUserId() );
    forumTopic.setId( topicId );
    try
    {
      getForumDiscussionService().saveReply( user, forumTopic, forumDiscussionReply );
      submitReply = "true";
    }
    catch( Exception e )
    {
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    session.setAttribute( "topicId", topicId );
    session.setAttribute( "topicName", topicName );
    session.setAttribute( "discussionId", discussionId );
    session.setAttribute( "discussionName", discussionName );
    session.setAttribute( "submitReply", submitReply );

    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private ForumDiscussionService getForumDiscussionService()
  {
    return (ForumDiscussionService)getService( ForumDiscussionService.BEAN_NAME );
  }

}

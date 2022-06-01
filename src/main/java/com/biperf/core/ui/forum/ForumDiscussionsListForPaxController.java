/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.forum.ForumDiscussionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.forum.ForumDiscussionValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author poddutur
 *
 */
public class ForumDiscussionsListForPaxController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long topicId = null;
    String topicName = null;

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
      topicId = (Long)clientStateMap.get( "topicId" );
      topicName = (String)clientStateMap.get( "topicName" );
    }

    List<ForumDiscussionValueBean> forumDiscussionList = new ArrayList<ForumDiscussionValueBean>();

    forumDiscussionList = getForumDiscussionService().getSortedDiscussionList( topicId );

    for ( ForumDiscussionValueBean discussionList : forumDiscussionList )
    {
      if ( discussionList.getDateCreated() != null )
      {
        String createdDate = DateUtils.toRelativeTimeLapsed( discussionList.getDateCreated() );

        if ( createdDate.equals( "About 1 day ago" ) )
        {
          createdDate = CmsResourceBundle.getCmsBundle().getString( "relative.time.elapsed.YESTERDAY" );
        }
        discussionList.setCreatedDate( createdDate );
      }
      if ( discussionList.getDateReplied() != null )
      {
        String repliedDate = DateUtils.toRelativeTimeLapsed( discussionList.getDateReplied() );
        if ( repliedDate.equals( "About 1 day ago" ) )
        {
          repliedDate = CmsResourceBundle.getCmsBundle().getString( "relative.time.elapsed.YESTERDAY" );
        }
        discussionList.setRepliedDate( repliedDate );
      }
    }

    request.setAttribute( "topicId", topicId );
    request.setAttribute( "topicName", topicName );
    request.setAttribute( "forumDiscussionList", forumDiscussionList );
  }

  private ForumDiscussionService getForumDiscussionService()
  {
    return (ForumDiscussionService)getService( ForumDiscussionService.BEAN_NAME );
  }

}

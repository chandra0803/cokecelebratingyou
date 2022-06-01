/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;

/**
 * @author poddutur
 *
 */
public class ForumPageDiscussionsFromTileController extends BaseController
{
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long topicId = null;
    String topicName = null;

    topicId = Long.parseLong( request.getParameter( "topicId" ) );
    topicName = request.getParameter( "topicName" );

    Map<String, Object> paramMapExpand = new HashMap<String, Object>();
    paramMapExpand.put( "topicId", topicId );
    paramMapExpand.put( "topicName", topicName );
    String startDiscussionUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.FORUM_START_DISCUSSION, paramMapExpand );
    String discussionsAjaxUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.FORUM_DISCUSSION_AJAX_URL, paramMapExpand );
    request.setAttribute( "startDiscussionUrl", startDiscussionUrl );
    request.setAttribute( "discussionsAjaxUrl", discussionsAjaxUrl );
    request.setAttribute( "topicId", topicId );
    request.setAttribute( "topicName", topicName );
  }

}

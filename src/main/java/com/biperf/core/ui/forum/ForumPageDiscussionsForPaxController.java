/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.value.forum.ForumTopicValueBean;

/**
 * @author poddutur
 *
 */
public class ForumPageDiscussionsForPaxController extends BaseController
{
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long topicId = null;
    String topicCmAssetCode = null;
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
      if ( (String)clientStateMap.get( "topicName" ) != null )
      {
        topicName = (String)clientStateMap.get( "topicName" );
      }
      else
      {
        topicCmAssetCode = (String)clientStateMap.get( "topicCmAssetCode" );
        ForumTopicValueBean forumTopicValueBean = new ForumTopicValueBean();
        forumTopicValueBean.setTopicCmAssetCode( topicCmAssetCode );
        topicName = forumTopicValueBean.getTopicNameFromCM();
      }
    }
    else
    {
      topicId = Long.parseLong( request.getParameter( "topicId" ) );
      topicName = request.getParameter( "topicName" );
    }

    Map paramMapExpand = new HashMap();
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

/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.forum.ForumTopicService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.forum.ForumTopicValueBean;

/**
 * @author poddutur
 *
 */
public class ForumTopicsListForPaxController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List<ForumTopicValueBean> forumTopicList = new ArrayList<ForumTopicValueBean>();

    Long userId = UserManager.getUser().getUserId();

    forumTopicList = getForumTopicService().getAllSortedTopicListByPax( userId );

    for ( ForumTopicValueBean topicList : forumTopicList )
    {
      if ( topicList.getLastActivityDate() != null )
      {
        String lastActivityDate = DateUtils.toRelativeTimeLapsed( topicList.getLastActivityDate() );

        if ( lastActivityDate.equals( "About 1 day ago" ) )
        {
          lastActivityDate = "Yesterday";
        }
        topicList.setLastActivityDateString( lastActivityDate );
      }
    }

    request.setAttribute( "forumTopicList", forumTopicList );
  }

  /**
   * Get the ForumTopicService from the beanLocator.
   * 
   * @return ForumTopicService
   */
  private ForumTopicService getForumTopicService()
  {
    return (ForumTopicService)getService( ForumTopicService.BEAN_NAME );
  }

}

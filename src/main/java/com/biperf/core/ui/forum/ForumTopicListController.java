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
import com.biperf.core.value.forum.ForumTopicValueBean;

/**
 * @author poddutur
 *
 */
public class ForumTopicListController extends BaseController
{
  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List<ForumTopicValueBean> forumTopicList = new ArrayList<ForumTopicValueBean>();

    forumTopicList = getForumTopicService().getAllSortedTopicList();

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

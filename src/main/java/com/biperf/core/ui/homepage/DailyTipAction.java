
package com.biperf.core.ui.homepage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.service.diycommunications.ParticipantDIYCommunicationsService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.value.DailyTipValueBean;
import com.objectpartners.cms.domain.Content;

public class DailyTipAction extends BaseDispatchAction
{
  /**
  * Dispatcher.  Default to home page display.  Too much work to append 'method=display'
  * to all the paths that lead to the home page.  
  */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return this.display( mapping, form, request, response );
    }
  }

  /**
   * Method to list the active badges in the system.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<DailyTipValueBean> dailyTipsList = new ArrayList<DailyTipValueBean>();
    List<DailyTipValueBean> activeDIYDailyTipsList = new ArrayList<DailyTipValueBean>();

    List<DailyTipValueBean> homedailyTipsList = getMainContentService().getDailyTips();
    int tipsSize = 0;
    if ( homedailyTipsList != null && homedailyTipsList.size() > 0 )
    {
      dailyTipsList.addAll( homedailyTipsList );
      tipsSize = homedailyTipsList.size() + 1;
    }

    List<Content> tipsList = getParticipantDIYCommunicationsService().getDiyCommunications( DIYCommunications.COMMUNICATION_TYPE_TIPS, DIYCommunications.DIY_TIPS_SECTION_CODE );

    for ( int i = 0; i < tipsList.size(); i++ )
    {
      Content content1 = (Content)tipsList.get( i );
      String text = (String)content1.getContentDataMap().get( "TEXT" );
      DailyTipValueBean dailyTip = new DailyTipValueBean();
      dailyTip.setText( text );
      dailyTip.setCode( "home.dailyTips" );
      dailyTip.setId( new Long( tipsSize++ ) );
      activeDIYDailyTipsList.add( dailyTip );
    }

    if ( activeDIYDailyTipsList != null && activeDIYDailyTipsList.size() > 0 )
    {
      dailyTipsList.addAll( activeDIYDailyTipsList );
    }

    DailyTipView dailyTipView = new DailyTipView();
    dailyTipView.setTipCollections( dailyTipsList );
    super.writeAsJsonToResponse( dailyTipView, response );
    return null;
  }

  /**
   * Returns the claim service.
   * 
   * @return a reference to the claim service.
   */
  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }

  public static ParticipantDIYCommunicationsService getParticipantDIYCommunicationsService()
  {
    return (ParticipantDIYCommunicationsService)getService( ParticipantDIYCommunicationsService.BEAN_NAME );
  }

}

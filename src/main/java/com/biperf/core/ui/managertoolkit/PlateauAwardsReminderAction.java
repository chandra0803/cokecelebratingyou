
package com.biperf.core.ui.managertoolkit;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.plateauawards.PlateauAwardsService;
import com.biperf.core.service.plateauawards.PreviewMessage;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;

public class PlateauAwardsReminderAction extends BaseDispatchAction
{
  private static final String AWARD_REMINDERS_SENT_KEY = "awardRemindersSent";

  @Override
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return prepareDisplay( actionMapping, actionForm, request, response );
  }

  public ActionForward prepareDisplay( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    PlateauAwardsReminderForm form = (PlateauAwardsReminderForm)actionForm;

    form.setReminders( getPlateauAwardsService().findPlateauAwardRemindersFor( UserManager.getUserId() ) );
    form.setCurrentUserId( UserManager.getUserId() );

    getPreviewMessage( form );

    form.setHomePageFilter( request.getContextPath() + "/homePage.do" + RequestUtils.getHomePageFilterToken( request ) );

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward send( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    PlateauAwardsReminderForm form = (PlateauAwardsReminderForm)actionForm;
    getPlateauAwardsService().sendPlateauAwardReminders( form.getReminderRecipientIds(), UserManager.getUserId(), form.getNotifyMessage() );

    // add the number of award reminders sent to session
    request.setAttribute( AWARD_REMINDERS_SENT_KEY, form.getReminderRecipientIds().size() );
    request.setAttribute( "displayPopup", true );

    return prepareDisplay( mapping, actionForm, request, response );
  }

  public static void moveToRequest( HttpServletRequest request )
  {
    // get it from session....
    Integer awardRemindersSent = (Integer)request.getSession().getAttribute( AWARD_REMINDERS_SENT_KEY );

    if ( awardRemindersSent != null )
    {
      // remove it from session....
      request.getSession().removeAttribute( AWARD_REMINDERS_SENT_KEY );

      // ... and add it to the request
      request.setAttribute( AWARD_REMINDERS_SENT_KEY, awardRemindersSent );
    }
  }

  private void getPreviewMessage( PlateauAwardsReminderForm form )
  {
    PreviewMessage previewMessage = getPlateauAwardsService().getPreviewMessage( UserManager.getUserId() );
    form.setPreviewMessage( previewMessage );
  }

  private PlateauAwardsService getPlateauAwardsService()
  {
    return (PlateauAwardsService)getService( PlateauAwardsService.BEAN_NAME );
  }
}

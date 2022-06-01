
package com.biperf.core.ui.managertoolkit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.service.plateauawards.PlateauAwardReminderBean;
import com.biperf.core.service.plateauawards.PreviewMessage;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.MethodMap;
import com.biperf.core.utils.DateUtils;

public class PlateauAwardsReminderForm extends BaseForm
{
  private List<PlateauAwardReminderBean> reminders = new ArrayList<PlateauAwardReminderBean>();
  private Map<Integer, PlateauAwardReminderBean> sendReminderBeans = new HashMap<Integer, PlateauAwardReminderBean>();
  private Long currentUserId = new Long( 0 );
  private MethodMap dateFormatter;
  private PreviewMessage previewMessage;
  private String notifyMessage;

  private String homePageFilter;

  public List<PlateauAwardReminderBean> getReminders()
  {
    return reminders;
  }

  public void setReminders( List<PlateauAwardReminderBean> reminders )
  {
    this.reminders = reminders;
  }

  public PlateauAwardReminderBean getSendReminderBeans( int index )
  {
    PlateauAwardReminderBean bean = sendReminderBeans.get( index );
    if ( bean == null )
    {
      bean = new PlateauAwardReminderBean();
      sendReminderBeans.put( index, bean );
    }
    return bean;
  }

  public List<Long> getReminderRecipientIds()
  {
    List<Long> recipients = new ArrayList<Long>();

    for ( PlateauAwardReminderBean bean : sendReminderBeans.values() )
    {
      recipients.add( bean.getMerchOrderId() );
    }

    return recipients;
  }

  public Long getCurrentUserId()
  {
    return currentUserId;
  }

  public void setCurrentUserId( Long currentUserId )
  {
    this.currentUserId = currentUserId;
  }

  public MethodMap getFormattedDate()
  {
    if ( dateFormatter == null )
    {
      dateFormatter = new MethodMap()
      {

        public Object get( Object key )
        {
          Date date = (Date)key;
          return DateUtils.toDisplayString( date );
        }

      };
    }
    return dateFormatter;
  }

  public PreviewMessage getPreviewMessage()
  {
    return previewMessage;
  }

  public void setPreviewMessage( PreviewMessage previewMessage )
  {
    this.previewMessage = previewMessage;
  }

  public String getNotifyMessage()
  {
    return notifyMessage;
  }

  public void setNotifyMessage( String notifyMessage )
  {
    this.notifyMessage = notifyMessage;
  }

  public String getHomePageFilter()
  {
    return homePageFilter;
  }

  public void setHomePageFilter( String homePageFilter )
  {
    this.homePageFilter = homePageFilter;
  }

}


package com.biperf.core.ui.diycommunication;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.value.diycommunication.MessageUploadBean;

public class DIYOwnImageUploadBean
{
  private List<MessageUploadBean> messages = new ArrayList<MessageUploadBean>();

  public DIYOwnImageUploadBean()
  {
  }

  public DIYOwnImageUploadBean( MessageUploadBean message )
  {
    messages.add( message );
  }

  public List<MessageUploadBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<MessageUploadBean> messages )
  {
    this.messages = messages;
  }

}

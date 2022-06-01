
package com.biperf.core.value.diycommunication;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.value.BaseJsonView;

public class CommunicationsUploadDoc extends BaseJsonView
{
  private static final long serialVersionUID = 1L;

  List<MessageUploadBean> messages = new ArrayList<MessageUploadBean>();

  public CommunicationsUploadDoc()
  {
    messages.add( new MessageUploadBean() );
  }

  public CommunicationsUploadDoc( MessageUploadBean uploadBean )
  {
    messages.add( new MessageUploadBean( uploadBean ) );
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


package com.biperf.core.ui;

import java.util.ArrayList;
import java.util.List;

public class JsonResponseMessageList<T>
{

  private List<T> messages = new ArrayList<T>();

  public List<T> getMessages()
  {
    return messages;
  }

  public void setMessages( List<T> messages )
  {
    this.messages = messages;
  }

  public void addMessage( T e )
  {
    this.messages.add( e );
  }

}

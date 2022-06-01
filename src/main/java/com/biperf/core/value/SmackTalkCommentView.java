
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class SmackTalkCommentView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private SmackTalkCommentViewBean comment = new SmackTalkCommentViewBean();

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public SmackTalkCommentViewBean getComment()
  {
    return comment;
  }

  public void setComment( SmackTalkCommentViewBean comment )
  {
    this.comment = comment;
  }

}

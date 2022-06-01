
package com.biperf.core.ui.publicrecognition;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.publicrecognition.PublicRecognitionCommentViewBean;

/**
 * 
 * @author dudam
 * @since Dec 11, 2012
 * @version 1.0
 */
public class PublicRecognitionCommentView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private PublicRecognitionCommentViewBean comment = new PublicRecognitionCommentViewBean();

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public PublicRecognitionCommentViewBean getComment()
  {
    return comment;
  }

  public void setComment( PublicRecognitionCommentViewBean comment )
  {
    this.comment = comment;
  }

}

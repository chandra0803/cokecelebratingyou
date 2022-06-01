/**
 * 
 */

package com.biperf.core.ui.forum;

import com.biperf.core.ui.BaseForm;

/**
 * @author poddutur
 *
 */
public class ForumDiscussionReplyListForm extends BaseForm
{

  /**
   * 
   */
  private static final long serialVersionUID = 4564267432554558022L;

  public static final String FORM_NAME = "forumDiscussionReplyListForm";

  private String notifyMessage;

  public String getNotifyMessage()
  {
    return notifyMessage;
  }

  public void setNotifyMessage( String notifyMessage )
  {
    this.notifyMessage = notifyMessage;
  }

}

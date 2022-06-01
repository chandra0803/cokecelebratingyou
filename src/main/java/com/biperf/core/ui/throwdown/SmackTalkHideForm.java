
package com.biperf.core.ui.throwdown;

import com.biperf.core.ui.BaseForm;

@SuppressWarnings( "serial" )
public class SmackTalkHideForm extends BaseForm
{
  private Long smackTalkId;
  private Long commentId;

  public void setSmackTalkId( Long smackTalkId )
  {
    this.smackTalkId = smackTalkId;
  }

  public Long getSmackTalkId()
  {
    return smackTalkId;
  }

  public void setCommentId( Long commentId )
  {
    this.commentId = commentId;
  }

  public Long getCommentId()
  {
    return commentId;
  }
}

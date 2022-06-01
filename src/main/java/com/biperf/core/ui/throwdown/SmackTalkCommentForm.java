
package com.biperf.core.ui.throwdown;

import com.biperf.core.ui.BaseForm;

public class SmackTalkCommentForm extends BaseForm
{
  // Promotion display information
  private String matchId;
  private String smackTalkId;
  private String comment;

  public String getMatchId()
  {
    return matchId;
  }

  public void setMatchId( String matchId )
  {
    this.matchId = matchId;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public void setSmackTalkId( String smackTalkId )
  {
    this.smackTalkId = smackTalkId;
  }

  public String getSmackTalkId()
  {
    return smackTalkId;
  }
}

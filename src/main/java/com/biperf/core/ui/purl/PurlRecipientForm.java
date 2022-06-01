
package com.biperf.core.ui.purl;

import com.biperf.core.ui.BaseActionForm;

public class PurlRecipientForm extends BaseActionForm
{
  private Long purlRecipientId;
  private String subject;
  private String comments;
  private boolean commentOrderDescending;

  private String method;
  private String data;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public void setCommentOrderDescending( boolean commentOrderDescending )
  {
    this.commentOrderDescending = commentOrderDescending;
  }

  public boolean isCommentOrderDescending()
  {
    return commentOrderDescending;
  }

  public void setData( String data )
  {
    this.data = data;
  }

  public String getData()
  {
    return data;
  }

}

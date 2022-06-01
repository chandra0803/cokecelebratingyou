
package com.biperf.core.service.plateauawards;

public class PreviewMessage
{
  private final String subject;
  private final String text;

  public PreviewMessage( String subject, String text )
  {
    this.subject = subject;
    this.text = text;
  }

  public String getSubject()
  {
    return subject;
  }

  public String getText()
  {
    return text;
  }
}

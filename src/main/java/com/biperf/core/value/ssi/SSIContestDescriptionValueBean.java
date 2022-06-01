
package com.biperf.core.value.ssi;

/**
 * 
 * SSIContestDescriptionView.
 * 
 * @author kandhi
 * @since Nov 10, 2014
 * @version 1.0
 */
public class SSIContestDescriptionValueBean
{
  private String language;
  private String text;

  public SSIContestDescriptionValueBean()
  {
    super();
  }

  public SSIContestDescriptionValueBean( String language, String text )
  {
    super();
    this.language = language;
    this.text = text;
  }

  public String getLanguage()
  {
    return language;
  }

  public void setLanguage( String language )
  {
    this.language = language;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

}

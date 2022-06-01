
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementUniqueRecognitionSentView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementUniqueRecognitionSentView
{
  private int actual;
  private int target;
  private String type;
  private String title;
  private String helpText;

  public EngagementUniqueRecognitionSentView( int actual, int target, String type )
  {
    super();
    this.actual = actual;
    this.target = target;
    this.type = type;
  }

  public EngagementUniqueRecognitionSentView( int actual, int target )
  {
    super();
    this.actual = actual;
    this.target = target;
  }

  public EngagementUniqueRecognitionSentView( int actual, int target, String type, String title )
  {
    super();
    this.actual = actual;
    this.target = target;
    this.type = type;
    this.title = title;
  }

  public EngagementUniqueRecognitionSentView( int actual, int target, String type, String title, String helpText )
  {
    super();
    this.actual = actual;
    this.target = target;
    this.type = type;
    this.title = title;
    this.helpText = helpText;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public int getActual()
  {
    return actual;
  }

  public void setActual( int actual )
  {
    this.actual = actual;
  }

  public int getTarget()
  {
    return target;
  }

  public void setTarget( int target )
  {
    this.target = target;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getHelpText()
  {
    return helpText;
  }

  public void setHelpText( String helpText )
  {
    this.helpText = helpText;
  }

}

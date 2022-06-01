
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementRecognitionSentView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementRecognitionSentView
{
  private int actual;
  private int target;
  private String type;
  private String title;

  public EngagementRecognitionSentView( int actual, int target, String type )
  {
    super();
    this.actual = actual;
    this.target = target;
    this.type = type;
  }

  public EngagementRecognitionSentView( int actual, int target )
  {
    this.actual = actual;
    this.target = target;
  }

  public EngagementRecognitionSentView( int actual, int target, String type, String title )
  {
    super();
    this.actual = actual;
    this.target = target;
    this.type = type;
    this.title = title;
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

}

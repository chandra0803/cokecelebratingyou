/**
 * 
 */

package com.biperf.core.ui.publicrecognition;

import com.biperf.core.ui.BaseForm;

/**
 * 
 *
 */
public class PublicRecognitionAddPointsForm extends BaseForm
{

  private String recognitionId;
  private String comment;
  private String points;

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public String getPoints()
  {
    return points;
  }

  public void setPoints( String points )
  {
    this.points = points;
  }

  public String getRecognitionId()
  {
    return recognitionId;
  }

  public void setRecognitionId( String recognitionId )
  {
    this.recognitionId = recognitionId;
  }

}


package com.biperf.core.ui.publicrecognition;

import com.biperf.core.ui.BaseForm;

public class PublicRecognitionCheersForm extends BaseForm
{
  private String recognitionId;
  private String points;
  private String recipientId;
  private String comments;

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getRecognitionId()
  {
    return recognitionId;
  }

  public void setRecognitionId( String recognitionId )
  {
    this.recognitionId = recognitionId;
  }

  public String getPoints()
  {
    return points;
  }

  public void setPoints( String points )
  {
    this.points = points;
  }

  public String getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( String recipientId )
  {
    this.recipientId = recipientId;
  }

}

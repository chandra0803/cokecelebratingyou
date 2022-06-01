/**
 * 
 */

package com.biperf.core.ui.publicrecognition;

import com.biperf.core.ui.BaseForm;

/**
 * 
 *
 */
public class PublicRecognitionCommentForm extends BaseForm
{
  // Promotion display information
  private String recognitionId;
  private String comment;

  public String getRecognitionId()
  {
    return recognitionId;
  }

  public void setRecognitionId( String recognitionId )
  {
    this.recognitionId = recognitionId;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

}

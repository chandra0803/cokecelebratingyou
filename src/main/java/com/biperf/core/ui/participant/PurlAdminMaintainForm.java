
package com.biperf.core.ui.participant;

import com.biperf.core.ui.BaseForm;

public class PurlAdminMaintainForm extends BaseForm
{
  /**
   * The key to the HTTP request attribute that refers to this form.
   */
  public static final String FORM_NAME = "purlAdminMaintainForm";

  private String method;
  private String purlRecipientId;
  private String[] deleteComments;
  private String[] deletePhotos;
  private String[] deleteVideos;

  public String getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( String purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMethod()
  {
    return method;
  }

  public void setDeleteComments( String[] deleteComments )
  {
    this.deleteComments = deleteComments;
  }

  public String[] getDeleteComments()
  {
    return deleteComments;
  }

  public void setDeletePhotos( String[] deletePhotos )
  {
    this.deletePhotos = deletePhotos;
  }

  public String[] getDeletePhotos()
  {
    return deletePhotos;
  }

  public void setDeleteVideos( String[] deleteVideos )
  {
    this.deleteVideos = deleteVideos;
  }

  public String[] getDeleteVideos()
  {
    return deleteVideos;
  }
}

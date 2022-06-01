
package com.biperf.core.ui.recognition;

import org.apache.struts.upload.FormFile;

import com.biperf.core.ui.BaseForm;

public class EcardUploadForm extends BaseForm
{
  private FormFile uploadAnImage;

  public FormFile getUploadAnImage()
  {
    return uploadAnImage;
  }

  public void setUploadAnImage( FormFile uploadAnImage )
  {
    this.uploadAnImage = uploadAnImage;
  }
}


package com.biperf.core.ui.client;

import org.apache.struts.upload.FormFile;

import com.biperf.core.ui.BaseForm;

/**
 * TcccClaimFileUploadForm.
 * 
 * This class is created as part of Client Customization for WIP #39189
 * 
 * @author dudam
 * @since Nov 17, 2017
 * @version 1.0
 */
public class TcccClaimFileUploadForm extends BaseForm
{

  private static final long serialVersionUID = 1L;
  private FormFile claimFile;
  private Long promotionId;

  public FormFile getClaimFile()
  {
    return claimFile;
  }

  public void setClaimFile( FormFile claimFile )
  {
    this.claimFile = claimFile;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

}

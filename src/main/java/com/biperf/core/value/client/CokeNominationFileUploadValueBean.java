
package com.biperf.core.value.client;

import java.io.Serializable;

/**
 * CokeNominationFileUploadValueBean.
 * 
 * @author dudam
 * @since Sep 13, 2019
 * @version 1.0
 */
public class CokeNominationFileUploadValueBean implements Serializable
{

  private static final long serialVersionUID = 1L;
  private Long promotionId;
  private Integer fileMinNumber;
  private Integer fileMaxNumber;
  private String allowedFileTypes;

  public CokeNominationFileUploadValueBean()
  {
    // empty constructor
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Integer getFileMinNumber()
  {
    return fileMinNumber;
  }

  public void setFileMinNumber( Integer fileMinNumber )
  {
    this.fileMinNumber = fileMinNumber;
  }

  public Integer getFileMaxNumber()
  {
    return fileMaxNumber;
  }

  public void setFileMaxNumber( Integer fileMaxNumber )
  {
    this.fileMaxNumber = fileMaxNumber;
  }

  public String getAllowedFileTypes()
  {
    return allowedFileTypes;
  }

  public void setAllowedFileTypes( String allowedFileTypes )
  {
    this.allowedFileTypes = allowedFileTypes;
  }

}
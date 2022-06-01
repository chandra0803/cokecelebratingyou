/*
 * (c) 20057 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionCertificateFormBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;

/**
 * PromotionCertificateFormBean.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>meadows</td>
 * <td>Sep 06, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionCertificateFormBean extends PromotionECardCertificateAbstractFormBean implements Serializable
{
  private String certificateId;
  private String disable;
  private String orderNumber;
  private String textColor;
  private String position;
  private String certName;

  public String getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( String certificateId )
  {
    this.certificateId = certificateId;
  }

  public void setDisable( String disable )
  {
    this.disable = disable;
  }

  public String getDisable()
  {
    return disable;
  }

  public String getOrderNumber()
  {
    return orderNumber;
  }

  public void setOrderNumber( String orderNumber )
  {
    this.orderNumber = orderNumber;
  }

  public String getTextColor()
  {
    return textColor;
  }

  public void setTextColor( String textColor )
  {
    this.textColor = textColor;
  }

  public String getPosition()
  {
    return position;
  }

  public void setPosition( String position )
  {
    this.position = position;
  }

  public String getCertName()
  {
    return certName;
  }

  public void setCertName( String certName )
  {
    this.certName = certName;
  }
}

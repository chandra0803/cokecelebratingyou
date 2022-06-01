/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ConvertCertificateForm.java,v $
 */

package com.biperf.core.ui.participant;

import com.biperf.core.ui.BaseForm;

/**
 * ConvertCertificateForm.
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
 * <td>zahler</td>
 * <td>Sep 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ConvertCertificateForm extends BaseForm
{
  private static final long serialVersionUID = 1L;
  private String method;
  private String certNumber;

  public String getCertNumber()
  {
    return certNumber;
  }

  public void setCertNumber( String certNumber )
  {
    this.certNumber = certNumber;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/proxy/ProxyListForm.java,v $
 */

package com.biperf.core.ui.proxy;

import com.biperf.core.ui.BaseForm;

/**
 * Proxy List ActionForm transfer object.
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
 * <td>sedey</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyListForm extends BaseForm
{
  private String mainUserId;
  private String method;
  private String proxyId;
  private String[] deleteProxy;
  private boolean showCancel;

  public String[] getDeleteProxy()
  {
    return deleteProxy;
  }

  public void setDeleteProxy( String[] deleteProxy )
  {
    this.deleteProxy = deleteProxy;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getMainUserId()
  {
    return mainUserId;
  }

  public void setMainUserId( String userId )
  {
    this.mainUserId = userId;
  }

  public String getProxyId()
  {
    return proxyId;
  }

  public void setProxyId( String proxyId )
  {
    this.proxyId = proxyId;
  }

  public boolean isShowCancel()
  {
    return showCancel;
  }

  public void setShowCancel( boolean showCancel )
  {
    this.showCancel = showCancel;
  }

}

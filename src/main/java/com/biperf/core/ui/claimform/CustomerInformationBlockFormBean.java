/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claimform/CustomerInformationBlockFormBean.java,v $
 */

package com.biperf.core.ui.claimform;

import com.biperf.core.ui.BaseFormBean;

/**
 * CustomerInformationBlockFormBean.
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
 * <td>Jun 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CustomerInformationBlockFormBean extends BaseFormBean
{
  String claimFormStepElementId;
  String name;
  boolean display;
  boolean required;
  boolean hideRequired;

  public boolean isDisplay()
  {
    return display;
  }

  public void setDisplay( boolean display )
  {
    this.display = display;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired( boolean required )
  {
    this.required = required;
  }

  public String getClaimFormStepElementId()
  {
    return claimFormStepElementId;
  }

  public void setClaimFormStepElementId( String claimFormStepElementId )
  {
    this.claimFormStepElementId = claimFormStepElementId;
  }

  public boolean isHideRequired()
  {
    return hideRequired;
  }

  public void setHideRequired( boolean showRequired )
  {
    this.hideRequired = showRequired;
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claimform/ClaimFormStepViewForm.java,v $
 */

package com.biperf.core.ui.claimform;

import com.biperf.core.ui.BaseForm;

/**
 * ClaimFormStepViewForm.
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
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepViewForm extends BaseForm
{
  public static final String FORM_NAME = "claimFormStepViewForm";

  private String claimFormId;
  private String claimFormStepId;
  private String method;
  private String[] delete;
  private int claimFormStepElementsSize;
  private String claimFormStepElementId;
  private String newElementSequenceNum;
  private String claimFormStepElementTypeCode;
  private String moduleType;

  public String getClaimFormStepId()
  {
    return claimFormStepId;
  }

  public void setClaimFormStepId( String claimFormStepId )
  {
    this.claimFormStepId = claimFormStepId;
  }

  public String[] getDelete()
  {
    return delete;
  }

  public void setDelete( String[] delete )
  {
    this.delete = delete;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public int getClaimFormStepElementsSize()
  {
    return claimFormStepElementsSize;
  }

  public void setClaimFormStepElementsSize( int claimFormStepElementsSize )
  {
    this.claimFormStepElementsSize = claimFormStepElementsSize;
  }

  public String getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( String claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public String getClaimFormStepElementId()
  {
    return claimFormStepElementId;
  }

  public void setClaimFormStepElementId( String claimFormStepElementId )
  {
    this.claimFormStepElementId = claimFormStepElementId;
  }

  public String getNewElementSequenceNum()
  {
    return newElementSequenceNum;
  }

  public void setNewElementSequenceNum( String newElementSequenceNum )
  {
    this.newElementSequenceNum = newElementSequenceNum;
  }

  public String getClaimFormStepElementTypeCode()
  {
    return claimFormStepElementTypeCode;
  }

  public void setClaimFormStepElementTypeCode( String claimFormStepElementTypeCode )
  {
    this.claimFormStepElementTypeCode = claimFormStepElementTypeCode;
  }

  public String getModuleType()
  {
    return moduleType;
  }

  public void setModuleType( String moduleType )
  {
    this.moduleType = moduleType;
  }
}

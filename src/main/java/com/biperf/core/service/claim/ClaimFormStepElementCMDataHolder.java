/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/claim/ClaimFormStepElementCMDataHolder.java,v $
 */

package com.biperf.core.service.claim;

import java.util.HashMap;
import java.util.Map;

import com.biperf.core.domain.claim.ClaimFormStepElement;

/**
 * This class is a data holder for CM data needed to create a ClaimFormStepElement. <p/> <b>Change
 * History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class ClaimFormStepElementCMDataHolder
{
  public ClaimFormStepElementCMDataHolder()
  {
    // empty default constructor
  }

  public ClaimFormStepElementCMDataHolder( String elementLabel, String heading, String copyBlock, String linkName, String labelTrue, String labelFalse )
  {
    this.elementLabel = elementLabel;
    this.heading = heading;
    this.copyBlock = copyBlock;
    this.linkName = linkName;
    this.labelTrue = labelTrue;
    this.labelFalse = labelFalse;
  }

  String elementLabel;
  String heading;
  String copyBlock;
  String linkName;
  String labelTrue;
  String labelFalse;
  String buttonLabel;

  public String getButtonLabel()
  {
    return buttonLabel;
  }

  public void setButtonLabel( String buttonLabel )
  {
    this.buttonLabel = buttonLabel;
  }

  public String getElementLabel()
  {
    return elementLabel;
  }

  public void setElementLabel( String elementLabel )
  {
    this.elementLabel = elementLabel;
  }

  public String getHeading()
  {
    return heading;
  }

  public void setHeading( String heading )
  {
    this.heading = heading;
  }

  public String getCopyBlock()
  {
    return copyBlock;
  }

  public void setCopyBlock( String copyBlock )
  {
    this.copyBlock = copyBlock;
  }

  public String getLinkName()
  {
    return linkName;
  }

  public void setLinkName( String linkName )
  {
    this.linkName = linkName;
  }

  public String getLabelTrue()
  {
    return labelTrue;
  }

  public void setLabelTrue( String labelTrue )
  {
    this.labelTrue = labelTrue;
  }

  public String getLabelFalse()
  {
    return labelFalse;
  }

  public void setLabelFalse( String labelFalse )
  {
    this.labelFalse = labelFalse;
  }

  public Map getCMDataMap( ClaimFormStepElement element )
  {
    Map dataMap = new HashMap();
    if ( this.elementLabel != null )
    {
      dataMap.put( element.getCmKeyForElementLabel(), this.elementLabel );
    }
    if ( this.heading != null )
    {
      dataMap.put( element.getCmKeyForHeading(), this.heading );
    }
    if ( this.copyBlock != null )
    {
      dataMap.put( element.getCmKeyForCopyBlock(), this.copyBlock );
    }
    if ( this.linkName != null )
    {
      dataMap.put( element.getCmKeyForLinkName(), this.linkName );
    }
    if ( this.labelTrue != null )
    {
      dataMap.put( element.getCmKeyForLabelTrue(), this.labelTrue );
    }
    if ( this.labelFalse != null )
    {
      dataMap.put( element.getCmKeyForLabelFalse(), this.labelFalse );
    }
    if ( this.buttonLabel != null )
    {
      dataMap.put( element.getCmKeyForButton(), this.buttonLabel );
    }
    return dataMap;
  }
}

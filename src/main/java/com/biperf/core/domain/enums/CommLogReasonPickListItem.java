/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CommLogReasonPickListItem.java,v $
 *
 */

package com.biperf.core.domain.enums;

/**
 * CommLogReasonPickListItem <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public abstract class CommLogReasonPickListItem extends PickListItem
{

  private static final String ESCALATION_CODE = "ESCALATION_HOURS";
  private static final String URGENCY_TYPE_CODE = "URGENCY_CODE";

  public Integer getEscalationHours()
  {
    if ( getContentMap().get( ESCALATION_CODE ) == null )
    {
      return null;
    }
    try
    {
      return new Integer( (String)getContentMap().get( ESCALATION_CODE ) );
    }
    catch( NumberFormatException e )
    {
      return null;
    }
  }

  public CommLogUrgencyType getUrgencyTypeCode()
  {
    if ( getContentMap().get( URGENCY_TYPE_CODE ) == null )
    {
      return null;
    }
    return CommLogUrgencyType.lookup( (String)getContentMap().get( URGENCY_TYPE_CODE ) );
  }

}

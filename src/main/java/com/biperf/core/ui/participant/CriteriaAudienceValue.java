/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/CriteriaAudienceValue.java,v $
 */

package com.biperf.core.ui.participant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * CriteriaAudienceValue.
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
 * <td>leep</td>
 * <td>Apr 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CriteriaAudienceValue implements Serializable
{
  private AudienceCriteriaValue currentAudienceCriteriaValue = null;

  private ArrayList<AudienceCriteriaValue> audienceCriteriaValueList = new ArrayList();

  /**
   * Get the size of audienceCriteriaValueList.
   * 
   * @return int
   */
  public int getAudienceCriteriaValueListSize()
  {
    return this.audienceCriteriaValueList.size();
  }

  /**
   * Get the isActive. Hibernate required method.
   * 
   * @return Boolean
   */
  public AudienceCriteriaValue getCurrentAudienceCriteriaValue()
  {
    return this.currentAudienceCriteriaValue;
  }

  /**
   * Set the isActive.
   * 
   * @param audienceCriteriaValue
   */
  public void setCurrentAudienceCriteriaValue( AudienceCriteriaValue audienceCriteriaValue )
  {
    this.currentAudienceCriteriaValue = audienceCriteriaValue;
  }

  /**
   * Get the Description.
   * 
   * @return description
   */
  public ArrayList<AudienceCriteriaValue> getAudienceCriteriaValueList()
  {
    return this.audienceCriteriaValueList;
  }

  /**
   * Set the List.
   * 
   * @param list
   */
  public void setAudienceCriteriaValueList( ArrayList<AudienceCriteriaValue> list )
  {
    this.audienceCriteriaValueList = list;
  }

  /**
   * returns the
   * 
   * @param index
   * @return AudienceCriteriaValue
   */
  public AudienceCriteriaValue getAudienceCriteriaValueByIndex( Long index )
  {
    if ( index.equals( new Long( -1 ) ) )
    {
      return this.getCurrentAudienceCriteriaValue();
    }

    return (AudienceCriteriaValue)this.getAudienceCriteriaValueList().get( index.intValue() );
  }

  /**
   * returns the
   * 
   * @return long
   */
  public long getTotalPaxSize()
  {
    int total = 0;

    for ( Iterator iter = this.getAudienceCriteriaValueList().iterator(); iter.hasNext(); )
    {
      AudienceCriteriaValue audienceCriteriaValue = (AudienceCriteriaValue)iter.next();
      total = total + audienceCriteriaValue.getPaxList().size();
    }

    return total;
  }

}

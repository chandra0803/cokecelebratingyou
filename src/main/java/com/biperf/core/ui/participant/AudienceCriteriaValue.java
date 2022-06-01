/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/AudienceCriteriaValue.java,v $
 */

package com.biperf.core.ui.participant;

import java.io.Serializable;
import java.util.ArrayList;

import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.value.FormattedValueBean;

/**
 * AudienceCriteriaValue.
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
public class AudienceCriteriaValue implements Serializable
{
  int index = 0;

  private AudienceCriteria audienceCriteria = new AudienceCriteria();

  private ArrayList criteriaList = new ArrayList();

  private ArrayList exclusionCriteriaList = new ArrayList();

  private ArrayList<FormattedValueBean> paxList = new ArrayList<FormattedValueBean>();

  private boolean viewPaxList = false;

  private boolean remove = false;

  /**
   * Get the isActive. Hibernate required method.
   * 
   * @return Boolean
   */
  public AudienceCriteria getAudienceCriteria()
  {
    return this.audienceCriteria;
  }

  /**
   * Set the isActive.
   * 
   * @param audience
   */
  public void setAudienceCriteria( AudienceCriteria audience )
  {
    this.audienceCriteria = audience;
  }

  /**
   * Get the Description.
   * 
   * @return description
   */
  public ArrayList getCriteriaList()
  {
    return this.criteriaList;
  }

  /**
   * Set the Description.
   * 
   * @param list
   */
  public void setCriteriaList( ArrayList list )
  {
    this.criteriaList = list;
  }

  /**
   * Get the Description.
   * 
   * @return description
   */
  public ArrayList getAudienceCriteriaValueList()
  {
    return this.paxList;
  }

  /**
   * Set the Description.
   * 
   * @param list
   */
  public void setAudienceCriteriaValueList( ArrayList list )
  {
    this.paxList = list;
  }

  /**
   * Get the Description.
   * 
   * @return description
   */
  public boolean getViewPaxList()
  {
    return this.viewPaxList;
  }

  /**
   * Set the Description.
   * 
   * @param view
   */
  public void setViewPaxList( boolean view )
  {
    this.viewPaxList = view;
  }

  /**
   * Get the Description.
   * 
   * @return description
   */
  public boolean getRemove()
  {
    return this.remove;
  }

  /**
   * Set the Description.
   * 
   * @param delete
   */
  public void setRemove( boolean delete )
  {
    this.remove = delete;
  }

  /**
   * Get the Description.
   * 
   * @return paxList
   */
  public ArrayList<FormattedValueBean> getPaxList()
  {
    return paxList;
  }

  /**
   * Set the Description.
   * 
   * @param list
   */
  public void setPaxList( ArrayList<FormattedValueBean> list )
  {
    this.paxList = list;
  }

  public int getPaxListSize()
  {
    return this.paxList.size();
  }

  public void setExclusionCriteriaList( ArrayList exclusionCriteriaList )
  {
    this.exclusionCriteriaList = exclusionCriteriaList;
  }

  public ArrayList<FormattedValueBean> getExclusionCriteriaList()
  {
    return exclusionCriteriaList;
  }

}

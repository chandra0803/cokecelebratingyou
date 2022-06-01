/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/ManagerOverrideFacts.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Date;

import com.biperf.core.domain.participant.Participant;

/*
 * ManagerOverrideFacts <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Aug
 * 24, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ManagerOverrideFacts implements PayoutCalculationFacts
{
  private Participant manager;
  private Date startDate;
  private Date endDate;

  /**
   * Constructs a <code>ManagerOverrideFacts</code> object.
   * 
   * @param manager
   * @param startDate
   * @param endDate
   */
  public ManagerOverrideFacts( Participant manager, Date startDate, Date endDate )
  {
    super();

    this.manager = manager;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Participant getManager()
  {
    return manager;
  }

  public void setManager( Participant manager )
  {
    this.manager = manager;
  }

  /**
   * Returns the start date.
   * 
   * @return Date value of the startDate property
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * Sets the start date.
   * 
   * @param startDate
   */
  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  /**
   * Returns the end date.
   * 
   * @return Date value of the endDate property
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * Sets the end date.
   * 
   * @param endDate
   */
  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }
}

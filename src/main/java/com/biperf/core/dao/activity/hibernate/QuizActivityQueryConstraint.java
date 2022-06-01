/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.activity.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.activity.QuizActivity;

/**
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
 * <td>wadzinsk</td>
 * <td>Nov 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class QuizActivityQueryConstraint extends ActivityQueryConstraint
{

  /**
   * If true, return activities for passing quiz claims, if false, for failing quiz claims, if null,
   * don't constrain (i.e., both).
   */
  private Boolean pass;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return QuizActivity.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    // Constrain by giver/receiver - submitter boolean true represents giver
    if ( pass != null )
    {
      criteria.createAlias( "activity.claim", "claim" ).add( Restrictions.eq( "claim.pass", pass ) );
    }

    return criteria;
  }

  /**
   * @return value of pass property
   */
  public Boolean getPass()
  {
    return pass;
  }

  /**
   * @param pass value for pass property
   */
  public void setPass( Boolean pass )
  {
    this.pass = pass;
  }

}

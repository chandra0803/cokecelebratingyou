/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/hibernate/GenericPostInsertEventListener.java,v $
 */

package com.biperf.core.utils.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.service.budget.BudgetMasterService;

/**
 * GenericPostDeleteEventListener.
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
 * <td>Dec 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GenericPostInsertEventListener extends AbstractEventListener implements PostInsertEventListener
{

  private static final Log logger = LogFactory.getLog( GenericPostInsertEventListener.class );
  private BudgetMasterService budgetMasterService;

  @Override
  /*
   * If we add mover events, this method should change to a filter-chain like structure so that we
   * don't keep adding more and more stuff here.
   */
  public void onPostInsert( PostInsertEvent event )
  {
    // check for promotion updates for caching purposes
    processPromotionChangeEvent( event, event.getEntity() );
    // check for Budget Inserts
    processBudgetInsertEvent( event );
  }

  /*
   * This processes the creation of a Budget - it adds the original insert into the BudgetHistory
   * table.
   */
  private void processBudgetInsertEvent( PostInsertEvent event )
  {
    if ( event.getEntity() instanceof Budget )
    {
      try
      {
        budgetMasterService.createBudgetHistory( (Budget)event.getEntity() );
      }
      catch( ConstraintViolationException e )
      {
        logger.debug( "Constraint Violation: " + e.getConstraintName() );
        // Do nothing..We are trying to insert duplicate row here.
      }
    }
  }

  public BudgetMasterService getBudgetMasterService()
  {
    return budgetMasterService;
  }

  public void setBudgetMasterService( BudgetMasterService budgetMasterService )
  {
    this.budgetMasterService = budgetMasterService;
  }
}

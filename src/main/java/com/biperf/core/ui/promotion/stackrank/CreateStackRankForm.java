/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.stackrank;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.dao.promotion.hibernate.StackRankQueryConstraint;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/*
 * CreateStackRankForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 14, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class CreateStackRankForm extends BaseForm
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The ID of the promotion whose sales activity is used to create this stack ranking.
   */
  private Long promotionId;

  /**
   * The date of the first day of the stack rank period.
   */
  private String startDate;

  /**
   * The date of the last day of the stack rank period.
   */
  private String endDate;

  /**
   * If true, calculate payout after calculating stack rank; if false, do not calculate payout.
   */
  private String calculatePayout;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public String getCalculatePayout()
  {
    return calculatePayout;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setCalculatePayout( String calculatePayout )
  {
    this.calculatePayout = calculatePayout;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------

  /**
   * Validates the data in this form.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   * @return <code>ActionErrors</code> object that encapsulates any validation errors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    Date formatDate = null;
    if ( startDate != null && startDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( startDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( startDate ) )
      {
        actionErrors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.stackrank.errors.START_DATE" ) ) );

      }
    }
    if ( endDate != null && endDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( endDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( endDate ) )
      {
        actionErrors.add( "endDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.stackrank.errors.END_DATE" ) ) );

      }
    }

    if ( actionErrors.size() == 0 )
    {
      // Constraint: The start date must be earlier than or equal to the end date.
      if ( DateUtils.toDate( startDate ).after( DateUtils.toDate( endDate ) ) )
      {
        actionErrors.add( "startDate", new ActionMessage( "system.errors.DATE_RANGE" ) );
      }

      // Constraint: You cannot create a stack rank if the application is in the
      // process of creating a stack rank for the same promotion.
      StackRankQueryConstraint queryConstraint = new StackRankQueryConstraint();
      queryConstraint.setPromotionIdsIncluded( new Long[] { promotionId } );
      queryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.BEFORE_CREATE_STACK_RANK_LISTS ),
                                                                         StackRankState.lookup( StackRankState.CREATING_STACK_RANK_LISTS ) } );

      List stackRankList = getStackRankService().getStackRankList( queryConstraint );
      if ( !stackRankList.isEmpty() )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.stackrank.errors.CREATING_STACK_RANK_LISTS" ) );
      }
    }

    return actionErrors;
  }

  // ---------------------------------------------------------------------------
  // Service Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the Stack Rank service.
   * 
   * @return a reference to the Stack Rank service.
   */
  private StackRankService getStackRankService()
  {
    return (StackRankService)BeanLocator.getBean( StackRankService.BEAN_NAME );
  }
}

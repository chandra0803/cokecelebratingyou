/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/discretionary/AwardForm.java,v $
 */

package com.biperf.core.ui.promotion.discretionary;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * AwardForm.
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
 * <td>jenniget</td>
 * <td>Sep 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardForm extends BaseForm
{

  private Long userId;
  private String promotion;
  private String amount;
  private String comments;
  private long discretionaryAwardMax;
  private long discretionaryAwardMin;

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    // validate required fields
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    // Verify Award Amount
    if ( amount != null && !amount.equals( "" ) )
    {
      try
      {
        long awardAmount = Long.parseLong( amount );

        if ( ! ( awardAmount <= this.discretionaryAwardMax && awardAmount >= discretionaryAwardMin ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( "system.errors.PROMOTION_AWARD_AMOUNT_NOT_IN_RANGE", new Long( this.discretionaryAwardMin ), new Long( this.discretionaryAwardMax ) ) );
        }
      }
      catch( NumberFormatException e )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.LONG", CmsResourceBundle.getCmsBundle().getString( "promotion.discretionary.award", "AWARD_AMOUNT" ) ) );
      }
    }

    return actionErrors;
  } // end validate

  public String getAmount()
  {
    return amount;
  }

  public void setAmount( String amount )
  {
    this.amount = amount;
  }

  public String getPromotion()
  {
    return promotion;
  }

  public void setPromotion( String promotion )
  {
    this.promotion = promotion;
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public long getDiscretionaryAwardMax()
  {
    return discretionaryAwardMax;
  }

  public void setDiscretionaryAwardMax( long discretionaryAwardMax )
  {
    this.discretionaryAwardMax = discretionaryAwardMax;
  }

  public long getDiscretionaryAwardMin()
  {
    return discretionaryAwardMin;
  }

  public void setDiscretionaryAwardMin( long discretionaryAwardMin )
  {
    this.discretionaryAwardMin = discretionaryAwardMin;
  }
}

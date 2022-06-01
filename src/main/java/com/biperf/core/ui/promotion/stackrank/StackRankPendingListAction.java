/*
 * $Source$
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.promotion.stackrank;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.process.StackRankCreationProcess;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.StackRankService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;

/**
 * StackRankPendingListAction.
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
 * <td>sedey</td>
 * <td>March 09, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StackRankPendingListAction extends BaseDispatchAction
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Approves the specified stack ranking.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward approveStackRank( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forward = ActionConstants.SUCCESS_FORWARD;
    ActionMessages errors = new ActionMessages();

    if ( isTokenValid( request, true ) )
    {
      StackRankPendingListForm approveStackRankForm = (StackRankPendingListForm)form;

      if ( approveStackRankForm.isApproved() )
      {
        getStackRankService().approveStackRank( approveStackRankForm.getStackRankId() );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forward );
  }

  /**
   * Creates a stack ranking.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward createStackRank( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forward = ActionConstants.SUCCESS_FORWARD;
    ActionMessages errors = new ActionMessages();

    if ( isTokenValid( request, true ) )
    {
      if ( !isCancelled( request ) )
      {
        CreateStackRankFormWrapper createStackRankForm = new CreateStackRankFormWrapper( (CreateStackRankForm)form );

        // Create the stack rank.
        StackRank stackRank = new StackRank();

        stackRank.setGuid( GuidUtils.generateGuid() );
        stackRank.setState( StackRankState.lookup( StackRankState.BEFORE_CREATE_STACK_RANK_LISTS ) );
        stackRank.setStartDate( createStackRankForm.getStartDate() );
        stackRank.setEndDate( createStackRankForm.getEndDate() );
        stackRank.setCalculatePayout( createStackRankForm.isCalculatePayout() );
        stackRank.setPromotion( (ProductClaimPromotion)getPromotionService().getPromotionById( createStackRankForm.getPromotionId() ) );

        stackRank = getStackRankService().saveStackRank( stackRank );

        // Create the stack rank lists.
        LinkedHashMap parameterValueMap = new LinkedHashMap();
        parameterValueMap.put( "stackRankId", new String[] { stackRank.getId().toString() } );

        ProcessService processService = getProcessService();
        Process process = processService.createOrLoadSystemProcess( "stackRankCreationProcess", StackRankCreationProcess.BEAN_NAME );
        processService.launchProcess( process, parameterValueMap, UserManager.getUserId() );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
    }

    return mapping.findForward( forward );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the Process service.
   * 
   * @return a reference to the Process service.
   */
  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Promotion service.
   * 
   * @return a reference to the Promotion service.
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Gets a StackRank Service
   * 
   * @return StackRankService
   */
  private StackRankService getStackRankService()
  {
    return (StackRankService)getService( StackRankService.BEAN_NAME );
  } // end

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  private static class CreateStackRankFormWrapper
  {
    private static final Log logger = LogFactory.getLog( CreateStackRankFormWrapper.class );
    private CreateStackRankForm form;

    /**
     * Constructs a <code>CreateStackRankFormWrapper</code> object given a non-null
     * {@link CreateStackRankForm} object.
     * 
     * @param form the {@link CreateStackRankForm} object to be wrapped.
     * @throws IllegalArgumentException if the argument "form" is null.
     */
    public CreateStackRankFormWrapper( CreateStackRankForm form )
    {
      if ( form == null )
      {
        throw new IllegalArgumentException( "Argument \"form\" is null." );
      }
      this.form = form;
    }

    /**
     * Returns the stack rank start date specified by the user, as a date.
     * 
     * @return the stack rank start date specified by the user.
     */
    public Date getStartDate()
    {
      Date startDate = null;

      try
      {
        startDate = DateUtils.toStartDate( form.getStartDate() );
      }
      catch( ParseException e )
      {
        // This exception should never occur because the contents of the Create Stack Rank form
        // have been validated and the stack rank start date is required and must be a date.
        logger.error( "Error parsing stack rank start date.", e );
      }

      return startDate;
    }

    /**
     * Returns the stack rank end date specified by the user, as a date.
     * 
     * @return the stack rank end date specified by the user.
     */
    public Date getEndDate()
    {
      Date endDate = null;

      try
      {
        endDate = DateUtils.toEndDate( form.getEndDate() );
      }
      catch( ParseException e )
      {
        // This exception should never occur because the contents of the Create Stack Rank form
        // have been validated and the stack rank end date is required and must be a date.
        logger.error( "Error parsing stack rank end date.", e );
      }

      return endDate;
    }

    /**
     * Returns the ID of the promotion whose sales activity is used to create the stack rank.
     * 
     * @return a promotion ID.
     */
    public Long getPromotionId()
    {
      return form.getPromotionId();
    }

    /**
     * Returns the calculate payout flag specified by the user, as a boolean value.
     * 
     * @return the calculate payout flag specified by the user.
     */
    public boolean isCalculatePayout()
    {
      boolean isCalculatePayout = false;

      if ( form.getCalculatePayout().equalsIgnoreCase( "true" ) )
      {
        isCalculatePayout = true;
      }

      return isCalculatePayout;
    }
  }
}

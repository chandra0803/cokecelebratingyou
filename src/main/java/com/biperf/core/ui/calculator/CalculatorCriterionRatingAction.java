/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/calculator/CalculatorCriterionRatingAction.java,v $
 */

package com.biperf.core.ui.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.calculator.CalculatorCriterionAssociationRequest;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStateUtils;

/**
 * CalculatorCriterionAction.
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
 * <td>attada</td>
 * <td>May 29, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorCriterionRatingAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( CalculatorCriterionRatingAction.class );

  /**
   * Display CalculatorCriterion
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "display";
    logger.debug( ">>> " + METHOD_NAME );

    String forward = ActionConstants.SUCCESS_FORWARD;

    CalculatorCriterionRatingForm criterionRatingForm = (CalculatorCriterionRatingForm)form;

    criterionRatingForm.setMethod( "save" );
    Long criterionRatingId = criterionRatingForm.getCriterionRatingId();

    Long calculatorCriterionId = criterionRatingForm.getCalculatorCriterionId();

    if ( criterionRatingId != null && criterionRatingId.longValue() > 0 )
    {

      criterionRatingForm.load( getCalculatorService().getCriterionRatingById( criterionRatingId ) );
    }
    else if ( calculatorCriterionId != null && calculatorCriterionId.longValue() > 0 )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new CalculatorCriterionAssociationRequest( CalculatorCriterionAssociationRequest.ALL ) );

      CalculatorCriterion calculatorCriterion = getCalculatorService().getCalculatorCriterionByIdWithAssociations( calculatorCriterionId, associationRequestCollection );
      criterionRatingForm.setCriterionText( calculatorCriterion.getCriterionText() );
      criterionRatingForm.setCriterionStatus( calculatorCriterion.getCriterionStatus().getName() );
      criterionRatingForm.setWeightedScore( calculatorCriterion.getCalculator().isWeightedScore() );
      criterionRatingForm.setWeightValue( calculatorCriterion.getWeightValue() );
    }

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * Creates or updates a CalculatorCriterionRating
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "save";

    String forwardAction = ActionConstants.SUCCESS_FORWARD;

    ActionMessages errors = new ActionMessages();
    CalculatorCriterionRatingForm criterionRatingForm = (CalculatorCriterionRatingForm)actionForm;

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      forwardAction = ActionConstants.CANCEL_FORWARD;
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "calculatorCriterionId", criterionRatingForm.getCalculatorCriterionId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=display" } );
    }
    // TODO check text
    try
    {
      getCalculatorService().saveCriterionRating( criterionRatingForm.getCalculatorCriterionId(), criterionRatingForm.toDomainObject(), criterionRatingForm.getRatingLabel() );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "calculatorCriterionId", criterionRatingForm.getCalculatorCriterionId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=display" } );

  } // end create

  private CalculatorService getCalculatorService()
  {
    return (CalculatorService)getService( CalculatorService.BEAN_NAME );
  }

} // end QuizFormAction

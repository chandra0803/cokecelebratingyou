/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/calculator/CalculatorLibraryListAction.java,v $ */

package com.biperf.core.ui.calculator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ArrayUtil;

/**
 * Action class for Calculator Form List operations.
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
 * <td>May 23, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorLibraryListAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( CalculatorLibraryListAction.class );

  /**
   * Delete under construction quizes
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteUnderConstructionQuizes( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    CalculatorLibraryListForm calculatorLibraryListForm = (CalculatorLibraryListForm)form;

    if ( calculatorLibraryListForm.getDeleteUnderConstructionIds() != null && calculatorLibraryListForm.getDeleteUnderConstructionIds().length > 0 )
    {
      deleteCalculators( calculatorLibraryListForm.getDeleteUnderConstructionIds() );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Delete complete quizes
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteCompleteQuizes( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    CalculatorLibraryListForm calculatorLibraryListForm = (CalculatorLibraryListForm)form;
    if ( calculatorLibraryListForm.getDeleteCompletedIds() != null && calculatorLibraryListForm.getDeleteCompletedIds().length > 0 )
    {
      deleteCalculators( calculatorLibraryListForm.getDeleteCompletedIds() );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * delete a list of calculators
   * 
   * @param calculatorIds
   */
  private void deleteCalculators( String[] calculatorIds )
  {
    // Convert String[] of calculatorIdIds to Long[]
    List calculatorIdList = ArrayUtil.convertStringArrayToListOfLongObjects( calculatorIds );
    try
    {
      getCalculatorService().deleteCalculators( calculatorIdList );
    }
    catch( ServiceErrorException e )
    {
      // Exception thrown if the calculator to be deleted is assigned
      logger.error( e.getMessage(), e );
    }
  }

  private CalculatorService getCalculatorService()
  {
    return (CalculatorService)getService( CalculatorService.BEAN_NAME );
  }
}

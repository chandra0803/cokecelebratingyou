/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claimform/CustomerInformationBlockAction.java,v $
 */

package com.biperf.core.ui.claimform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.CustomerInformationBlock;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;

/**
 * CustomerInformationBlockAction.
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
 * <td>Jun 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CustomerInformationBlockAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( CustomerInformationBlockAction.class );

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "update";

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String queryString = "clientState=" + clientState + "cryptoPass" + cryptoPass;
      return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString } );
    }

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    CustomerInformationBlockForm customerInformationBlockForm = (CustomerInformationBlockForm)form;

    List cibFormBeans = customerInformationBlockForm.getCibFormBeans();

    Map customerInformationBlockElements = CustomerInformationBlock.getCustomerInformationBlockElements();

    List elementsToSave = new ArrayList();
    List elementsToDelete = new ArrayList();

    Iterator iter = cibFormBeans.iterator();
    while ( iter.hasNext() )
    {
      CustomerInformationBlockFormBean formBean = (CustomerInformationBlockFormBean)iter.next();

      ClaimFormStepElement claimFormStepElementToSave = new ClaimFormStepElement();

      if ( formBean.getClaimFormStepElementId() == null || formBean.getClaimFormStepElementId().equals( "" ) )
      {
        ClaimFormStepElement cibElement = (ClaimFormStepElement)customerInformationBlockElements.get( new Long( formBean.getId() ) );

        try
        {
          claimFormStepElementToSave = (ClaimFormStepElement)cibElement.clone();
        }
        catch( CloneNotSupportedException e )
        {
          log.error( e.getMessage(), e );
        }
      }
      else
      {
        claimFormStepElementToSave = getClaimFormDefinitionService().getClaimFormStepElementById( new Long( formBean.getClaimFormStepElementId() ) );
      }

      claimFormStepElementToSave.setRequired( formBean.isRequired() );
      claimFormStepElementToSave.setCustomerInformationBlockId( new Long( formBean.getId() ) );

      // If display is true, save no matter what.
      // If display is false, need to delete if record already in the DB. (id exists)
      if ( formBean.isDisplay() )
      {
        elementsToSave.add( claimFormStepElementToSave );
      }
      else if ( claimFormStepElementToSave.getId() != null )
      {
        elementsToDelete.add( claimFormStepElementToSave );
      }
    }

    getClaimFormDefinitionService().saveCustomerInformationBlockElements( new Long( customerInformationBlockForm.getClaimFormStepId() ), elementsToSave, elementsToDelete );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "claimFormStepId", customerInformationBlockForm.getClaimFormStepId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String method = "method=display";
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_UPDATE, new String[] { queryString, method } );
  }

  /**
   * Get the claimFormDefinitionService from the beanFactory.
   * 
   * @return ClaimFormDefinitionService
   */
  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }
}

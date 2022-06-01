/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claimform/CustomerInformationBlockController.java,v $
 */

package com.biperf.core.ui.claimform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.CustomerInformationBlock;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimFormStepAssociationRequest;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * CustomerInformationBlockController.
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
 * <td>Jun 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CustomerInformationBlockController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( CustomerInformationBlockController.class );

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    CustomerInformationBlockForm customerInformationBlockForm = (CustomerInformationBlockForm)request.getAttribute( CustomerInformationBlockForm.FORM_NAME );
    if ( customerInformationBlockForm == null )
    {
      customerInformationBlockForm = new CustomerInformationBlockForm();
    }
    if ( customerInformationBlockForm.getCibFormBeans() == null || customerInformationBlockForm.getCibFormBeans().isEmpty() )
    { // if we already have these beans then we are probably going back to the page to show an error
      // so don't process the lines below and overwrite selections made by the user
      Map elements = CustomerInformationBlock.getCustomerInformationBlockElements();
      List formBeans = loadElementsIntoFormBeans( elements );

      try
      {
        String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        String s = (String)clientStateMap.get( "claimFormStepId" );
        Long claimFormStepId = new Long( s );
        if ( claimFormStepId != null )
        {
          AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
          associationRequestCollection.add( new ClaimFormStepAssociationRequest() );

          ClaimFormStep claimFormStep = getClaimFormDefinitionService().getClaimFormStepWithAssociations( claimFormStepId, associationRequestCollection );

          List claimFormStepElements = claimFormStep.getClaimFormStepElements();

          loadValuesOnFormBeans( formBeans, claimFormStepElements );

          customerInformationBlockForm.setClaimFormStepId( String.valueOf( claimFormStepId ) );
          customerInformationBlockForm.setCibFormBeans( formBeans );
        }
        else
        {
          LOG.error( "promotionId not found in client state" );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }

      request.setAttribute( CustomerInformationBlockForm.FORM_NAME, customerInformationBlockForm );
    }
  }

  /**
   * @param elements
   * @return List of FormBeans
   */
  private List loadElementsIntoFormBeans( Map elements )
  {
    List formBeans = new ArrayList();
    Iterator cibElementIterator = elements.values().iterator();
    while ( cibElementIterator.hasNext() )
    {
      ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)cibElementIterator.next();

      CustomerInformationBlockFormBean formBean = new CustomerInformationBlockFormBean();
      formBean.setId( String.valueOf( claimFormStepElement.getId() ) );
      formBean.setName( claimFormStepElement.getDescription() );
      formBean.setDisplay( false );
      formBean.setRequired( claimFormStepElement.isRequired() );

      // String code = claimFormStepElement.getClaimFormElementType().getCode();
      if ( claimFormStepElement.getClaimFormElementType().isAddressBookSelect() || claimFormStepElement.getClaimFormElementType().isBooleanCheckbox() )
      {
        formBean.setHideRequired( true );
      }

      formBeans.add( formBean );
    }
    return formBeans;
  }

  /**
   * @param formBeans
   * @param claimFormStepElements
   */
  private void loadValuesOnFormBeans( List formBeans, List claimFormStepElements )
  {
    Iterator elementIter = claimFormStepElements.iterator();
    while ( elementIter.hasNext() )
    {
      ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)elementIter.next();

      if ( claimFormStepElement.getCustomerInformationBlockId() != null )
      {
        Iterator formBeanIterator = formBeans.iterator();
        while ( formBeanIterator.hasNext() )
        {
          CustomerInformationBlockFormBean formBean = (CustomerInformationBlockFormBean)formBeanIterator.next();

          if ( String.valueOf( claimFormStepElement.getCustomerInformationBlockId() ).equals( formBean.getId() ) )
          {
            formBean.setDisplay( true );
            formBean.setRequired( claimFormStepElement.isRequired() );
            formBean.setClaimFormStepElementId( String.valueOf( claimFormStepElement.getId() ) );
          } // if this is the record
        } // while
      } // if customerInformationBlockId is not null
    } // while
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

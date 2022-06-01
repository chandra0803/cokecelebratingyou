/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claimform/ClaimFormStepElementViewController.java,v $
 *
 */

package com.biperf.core.ui.claimform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimFormStepAssociationRequest;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ClaimFormStepElementViewController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ClaimFormStepElementViewController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( ClaimFormStepElementViewController.class );

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
      String claimFormStepId = null;
      String claimFormStepElementId = null;
      try
      {
        claimFormStepId = (String)clientStateMap.get( "claimFormStepId" );

      }
      catch( ClassCastException cce )
      {
        Long stepId = (Long)clientStateMap.get( "claimFormStepId" );
        if ( stepId != null )
        {
          claimFormStepId = stepId.toString();
        }
      }
      try
      {
        claimFormStepElementId = (String)clientStateMap.get( "claimFormStepElementId" );
      }
      catch( ClassCastException cce )
      {
        Long stepElementId = (Long)clientStateMap.get( "claimFormStepElementId" );
        if ( stepElementId != null )
        {
          claimFormStepElementId = stepElementId.toString();
        }
      }
      if ( claimFormStepId != null && claimFormStepElementId != null )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ClaimFormStepAssociationRequest() );

        ClaimFormStep claimFormStep = getClaimFormDefinitionService().getClaimFormStepWithAssociations( new Long( claimFormStepId ), associationRequestCollection );

        // Put the form in the request for the view page. Allows us to re-use some display code.
        ClaimFormStepElementForm form = new ClaimFormStepElementForm();
        ClaimFormStepElement element = getClaimFormDefinitionService().getClaimFormStepElementById( new Long( claimFormStepElementId ) );
        form.load( element );
        form.setCmData( getClaimFormDefinitionService().getClaimFormStepElementCMDataHolder( claimFormStep.getClaimForm().getCmAssetCode(), element ) );
        form.setClaimFormId( claimFormStep.getClaimForm().getId() );

        request.setAttribute( ClaimFormStepElementForm.FORM_NAME, form );
        request.setAttribute( "claimFormStep", claimFormStep );

        ClaimFormElementType elementType = ClaimFormElementType.lookup( form.getClaimFormStepElementTypeCode() );
        form.setClaimFormStepElementTypeDesc( elementType.getName() );

        // Now put all of the items and values in the request.
        List elementItems = ClaimFormStepElementItem.getItemListByType( elementType );
        List elementValues = new ArrayList();
        Iterator iterator = elementItems.iterator();
        while ( iterator.hasNext() )
        {
          ClaimFormStepElementItem claimFormStepElementItem = (ClaimFormStepElementItem)iterator.next();
          if ( claimFormStepElementItem.isSelectField() )
          {
            String pickListItem = (String)PropertyUtils.getNestedProperty( form, claimFormStepElementItem.getFormProperty() );
            elementValues.add( DynaPickListType.lookup( claimFormStepElementItem.getPickListAsset(), pickListItem ) );
          }
          else if ( claimFormStepElementItem.isSelectPickListField() )
          {
            String pickListAsset = (String)PropertyUtils.getNestedProperty( form, claimFormStepElementItem.getFormProperty() );
            List pickList = DynaPickListType.getList( pickListAsset );
            elementValues.add( ( (DynaPickListType)pickList.get( 0 ) ).getListName() );
          }
          else
          {
            elementValues.add( PropertyUtils.getNestedProperty( form, claimFormStepElementItem.getFormProperty() ) );
          }
        }
        request.setAttribute( "elementItems", elementItems );
        request.setAttribute( "elementValues", elementValues );

        // Need the claim form module for text box's "is why field" section
        ClaimForm claimForm = getClaimFormDefinitionService().getClaimFormById( form.getClaimFormId() );
        request.setAttribute( "formModuleType", claimForm.getClaimFormModuleType().getCode() );
      }
      else
      {
        LOG.error( "claimFormStepId or claimFormStepElementId not found in client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  /**
   * Get the CharacteristicService from the beanLocator.
   * 
   * @return CharacteristicService
   */
  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

}

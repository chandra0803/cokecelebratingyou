
package com.biperf.core.ui.productclaim;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.value.ProductClaimCharacteristicsPreviewBean;
import com.biperf.core.value.ProductClaimPreviewBean;

public class ProductClaimPreviewRedirectAction extends BaseDispatchAction
{
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    HttpSession session = request.getSession();
    ProductClaimSubmissionForm productClaimSubmissionForm = (ProductClaimSubmissionForm)session.getAttribute( "productClaimSubmissionForm" );

    String[] tempMultiValues1;
    String[] tempMultiValues2 = null;
    String tempBooleanValue1;
    String tempBooleanValue2 = null;

    ArrayList<ProductClaimPreviewBean> products = productClaimSubmissionForm.getProducts();

    for ( ProductClaimPreviewBean productClaimPreviewBean : products )
    {
      for ( ProductClaimCharacteristicsPreviewBean productClaimCharacteristicsPreviewBean : productClaimPreviewBean.getCharacteristicsValues() )
      {
        if ( productClaimCharacteristicsPreviewBean.getType() != null )
        {
          if ( productClaimCharacteristicsPreviewBean.getType().equals( "boolean" ) )
          {
            tempBooleanValue1 = productClaimCharacteristicsPreviewBean.getValue();

            if ( tempBooleanValue1.contains( "[" ) || tempBooleanValue1.contains( "]" ) || tempBooleanValue1.contains( "'" ) )
            {
              tempBooleanValue2 = tempBooleanValue1.replace( "[", "" ).replace( "]", "" ).replace( "'", "" );
            }
            if ( tempBooleanValue2 != null )
            {
              productClaimCharacteristicsPreviewBean.setValue( tempBooleanValue2 );
            }
          }

          if ( productClaimCharacteristicsPreviewBean.getType().equals( "multi_select" ) )
          {
            tempMultiValues1 = productClaimCharacteristicsPreviewBean.getValues();
            if ( tempMultiValues1 != null )
            {
              for ( String str : tempMultiValues1 )
              {
                if ( str.contains( "[" ) || str.contains( "]" ) || str.contains( "'" ) )
                {
                  tempMultiValues2 = str.replace( "[", "" ).replace( "]", "" ).replace( "'", "" ).split( "," );
                }
              }
              if ( tempMultiValues2 != null )
              {
                productClaimCharacteristicsPreviewBean.setValues( tempMultiValues2 );
              }
            }
          }
        }
      }
    }

    request.setAttribute( "productClaimSubmissionForm", productClaimSubmissionForm );

    ProductClaimStateManager.store( productClaimSubmissionForm, request );
    ProductClaimStateManager.addToRequest( productClaimSubmissionForm, request );

    session.removeAttribute( "productClaimSubmissionForm" );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

}

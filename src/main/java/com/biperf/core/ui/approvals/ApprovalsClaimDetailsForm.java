/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsClaimDetailsForm.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClaimApproveUtils;

/**
 * ApprovalsClaimDetailsForm.
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
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsClaimDetailsForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private static final Log LOG = LogFactory.getLog( ApprovalsClaimDetailsForm.class );
  private Map claimProductApprovalFormByClaimProductIdString = new HashMap();
  private String method;
  private String claimId;

  /**
   * @return value of claimProductApprovalFormByClaimProductIdString property
   */
  public Map getClaimProductApprovalFormByClaimProductIdString()
  {
    return claimProductApprovalFormByClaimProductIdString;
  }

  /**
   * @param claimProductApprovalFormByClaimProductIdString value for
   *          claimProductApprovalFormByClaimProductIdString property
   */
  public void setClaimProductApprovalFormByClaimProductIdString( Map claimProductApprovalFormByClaimProductIdString )
  {
    this.claimProductApprovalFormByClaimProductIdString = claimProductApprovalFormByClaimProductIdString;
  }

  public void addClaimProductApprovalFormByClaimProductIdString( String claimProductIdString, ApprovalsClaimsForm claimProductApprovalForm )
  {
    claimProductApprovalFormByClaimProductIdString.put( claimProductIdString, claimProductApprovalForm );
  }

  /**
   * @param claim
   */
  public void load( ProductClaim claim )
  {
    // Add all claim products to map by id
    for ( Iterator iterator1 = claim.getClaimProducts().iterator(); iterator1.hasNext(); )
    {
      ClaimProduct claimProduct = (ClaimProduct)iterator1.next();

      ApprovalStatusType approvalStatusType = claimProduct.getApprovalStatusType();
      String approvalStatusTypeCode = approvalStatusType != null ? approvalStatusType.getCode() : null;

      PromotionApprovalOptionReasonType promotionApprovalOptionReasonType = claimProduct.getPromotionApprovalOptionReasonType();

      String holdPromotionApprovalOptionReasonTypeCode = null;
      String denyPromotionApprovalOptionReasonTypeCode = null;
      if ( approvalStatusTypeCode != null && promotionApprovalOptionReasonType != null )
      {
        if ( approvalStatusTypeCode.equals( ApprovalStatusType.DENIED ) )
        {
          denyPromotionApprovalOptionReasonTypeCode = promotionApprovalOptionReasonType.getCode();
        }
        else
        {
          holdPromotionApprovalOptionReasonTypeCode = promotionApprovalOptionReasonType.getCode();
        }
      }

      ApprovalsClaimsForm claimProductApprovalForm = new ApprovalsClaimsForm( approvalStatusTypeCode, holdPromotionApprovalOptionReasonTypeCode, denyPromotionApprovalOptionReasonTypeCode );

      addClaimProductApprovalFormByClaimProductIdString( claimProduct.getId().toString(), claimProductApprovalForm );
    }

  }

  /**
   * Populate the claim with the claim product approval details and the approver comments.
   * 
   * @param claim
   * @param approver
   */
  public void populateClaimProductDomainObjects( ProductClaim claim, User approver )
  {
    Map claimProductApprovalFormByClaimProductIdStringCopy = new HashMap( claimProductApprovalFormByClaimProductIdString );

    for ( Iterator iterator1 = claim.getClaimProducts().iterator(); iterator1.hasNext(); )
    {
      ClaimProduct claimProduct = (ClaimProduct)iterator1.next();

      String claimProductIdString = claimProduct.getId().toString();
      ApprovalsClaimsForm claimProductApprovalForm = (ApprovalsClaimsForm)claimProductApprovalFormByClaimProductIdStringCopy.get( claimProductIdString );

      if ( claimProductApprovalForm == null )
      {
        throw new BeaconRuntimeException( "form claimProduct Id not found. claimProduct Id: " + claimProductIdString );
      }

      // Remove so we can confirm all are used up.
      claimProductApprovalFormByClaimProductIdStringCopy.remove( claimProductIdString );
      String approvalStatusTypeCode = null;

      if ( claimProductApprovalForm.getApprovalStatusType() != null )
      {
        approvalStatusTypeCode = claimProductApprovalForm.getApprovalStatusType();
      }
      else
      {
        approvalStatusTypeCode = claimProduct.getApprovalStatusType().getCode();
      }

      String holdPromotionApprovalOptionReasonTypeCode = claimProductApprovalForm.getHoldPromotionApprovalOptionReasonType();
      String denyPromotionApprovalOptionReasonTypeCode = null;

      if ( claimProductApprovalForm.getDenyPromotionApprovalOptionReasonType() != null )
      {
        denyPromotionApprovalOptionReasonTypeCode = claimProductApprovalForm.getDenyPromotionApprovalOptionReasonType();
      }
      else
      {
        if ( claimProduct.getPromotionApprovalOptionReasonType() != null && approvalStatusTypeCode.equals( ApprovalStatusType.DENIED ) )
        {
          denyPromotionApprovalOptionReasonTypeCode = claimProduct.getPromotionApprovalOptionReasonType().getCode();
        }
      }

      ClaimApproveUtils.setClaimItemApproverDetails( approver, approvalStatusTypeCode, holdPromotionApprovalOptionReasonTypeCode, denyPromotionApprovalOptionReasonTypeCode, claimProduct );

    }

    if ( !claimProductApprovalFormByClaimProductIdStringCopy.isEmpty() )
    {
      throw new BeaconRuntimeException( "form claimProduct remaining after matching with db entries, already deleted?? Count" + claimProductApprovalFormByClaimProductIdStringCopy.size() );
    }

  }

  /**
   * @return value of method property
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method value for method property
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return value of claimId property
   */
  public String getClaimId()
  {
    return claimId;
  }

  /**
   * @param claimId value for claimId property
   */
  public void setClaimId( String claimId )
  {
    this.claimId = claimId;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.validator.ValidatorForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // super.reset( mapping, request );

    // Must recreate map form object, so we need claimproduct ids.
    Long claimId = null;

    if ( request.getParameter( "claimId" ) != null )
    {
      claimId = Long.parseLong( request.getParameter( "claimId" ) );
    }
    else
    {
      claimId = (Long)request.getSession().getAttribute( "claimId" );
    }

    if ( claimId != null )
    {
      ProductClaim claim = (ProductClaim)getClaimService().getClaimById( claimId );
      for ( Iterator iter = claim.getClaimProducts().iterator(); iter.hasNext(); )
      {
        ClaimProduct claimProduct = (ClaimProduct)iter.next();
        addClaimProductApprovalFormByClaimProductIdString( claimProduct.getId().toString(), new ApprovalsClaimsForm() );

      }
    }
    else
    {
      LOG.error( "claimId not found in request" );
    }
    request.getSession().setAttribute( "claimId", claimId );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)BeanLocator.getBean( ClaimService.BEAN_NAME );
  }

}

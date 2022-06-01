/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsClaimsListForm.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.ClaimItem;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ListPageInfo;
import com.biperf.core.value.PromotionApprovableValue;

/**
 * ApprovalsClaimsListForm.
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
public class ApprovalsClaimsListForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String promotionId;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private boolean open = true;
  private Map claimProductApprovalFormByClaimProductIdString = new HashMap();

  private String initializationJson = "";

  private ListPageInfo<PromotionApprovableValue> listPageInfo;
  private Long requestedPage;
  private String sortedOn;
  private String sortedBy;
  private int rowNumStart;
  private int rowNumEnd;
  private String claimStatus;

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getInitializationJson()
  {
    return initializationJson;
  }

  public void setInitializationJson( String initializationJson )
  {
    this.initializationJson = initializationJson;
  }

  public String getClaimStatus()
  {
    return claimStatus;
  }

  public void setClaimStatus( String claimStatus )
  {
    this.claimStatus = claimStatus;
  }

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
   * @param promotionClaimsValueList
   */
  public void load( List promotionClaimsValueList )
  {
    // Add all claim products to map by id
    for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
    {
      PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next();
      for ( Iterator iterator = promotionClaimsValue.getApprovables().iterator(); iterator.hasNext(); )
      {
        ProductClaim claim = (ProductClaim)iterator.next();
        for ( Iterator iterator1 = claim.getClaimProducts().iterator(); iterator1.hasNext(); )
        {
          ClaimItem claimItem = (ClaimItem)iterator1.next();

          ApprovalStatusType approvalStatusType = claimItem.getApprovalStatusType();
          String approvalStatusTypeCode = approvalStatusType != null ? approvalStatusType.getCode() : null;

          PromotionApprovalOptionReasonType promotionApprovalOptionReasonType = claimItem.getPromotionApprovalOptionReasonType();

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
          addClaimProductApprovalFormByClaimProductIdString( claimItem.getId().toString(), claimProductApprovalForm );
        }
      }

    }

  }

  /**
   * Populate the claim with the claim product approval details.
   * 
   * @param promotionClaimsValueList
   * @param approver
   */
  public void populateClaimProductDomainObjects( List promotionClaimsValueList, User approver )
  {
    Map claimProductApprovalFormByClaimProductIdStringCopy = new HashMap( claimProductApprovalFormByClaimProductIdString );

    for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
    {
      PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next();
      for ( Iterator iterator = promotionClaimsValue.getApprovables().iterator(); iterator.hasNext(); )
      {
        ProductClaim claim = (ProductClaim)iterator.next();
        for ( Iterator iterator1 = claim.getClaimProducts().iterator(); iterator1.hasNext(); )
        {
          ClaimItem claimProduct = (ClaimProduct)iterator1.next();

          String claimProductIdString = claimProduct.getId().toString();
          ApprovalsClaimsForm claimProductApprovalForm = (ApprovalsClaimsForm)claimProductApprovalFormByClaimProductIdStringCopy.get( claimProductIdString );

          if ( claimProductApprovalForm == null )
          {
            throw new BeaconRuntimeException( "form claimProduct Id not found. claimProduct Id: " + claimProductIdString );
          }

          // Remove so we can confirm all are used up.
          claimProductApprovalFormByClaimProductIdStringCopy.remove( claimProductIdString );

          String approvalStatusTypeCode = claimProductApprovalForm.getApprovalStatusType();
          String holdPromotionApprovalOptionReasonTypeCode = claimProductApprovalForm.getHoldPromotionApprovalOptionReasonType();
          String denyPromotionApprovalOptionReasonTypeCode = claimProductApprovalForm.getDenyPromotionApprovalOptionReasonType();

          ClaimApproveUtils.setClaimItemApproverDetails( approver, approvalStatusTypeCode, holdPromotionApprovalOptionReasonTypeCode, denyPromotionApprovalOptionReasonTypeCode, claimProduct );
        }
      }
    }

    if ( !claimProductApprovalFormByClaimProductIdStringCopy.isEmpty() )
    {
      throw new BeaconRuntimeException( "form claimProduct remaining after matching with db entries, already deleted?? Count" + claimProductApprovalFormByClaimProductIdStringCopy.size() );
    }

  }

  /**
   * @return value of open property
   */
  public boolean isOpen()
  {
    return open;
  }

  /**
   * @param open value for open property
   */
  public void setOpen( boolean open )
  {
    this.open = open;
  }

  public ListPageInfo<PromotionApprovableValue> getListPageInfo()
  {
    return listPageInfo;
  }

  public void setListPageInfo( ListPageInfo<PromotionApprovableValue> listPageInfo )
  {
    this.listPageInfo = listPageInfo;
  }

  public Long getRequestedPage()
  {
    return requestedPage;
  }

  public void setRequestedPage( Long requestedPage )
  {
    this.requestedPage = requestedPage;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public int getRowNumStart()
  {
    return rowNumStart;
  }

  public void setRowNumStart( int rowNumStart )
  {
    this.rowNumStart = rowNumStart;
  }

  public int getRowNumEnd()
  {
    return rowNumEnd;
  }

  public void setRowNumEnd( int rowNumEnd )
  {
    this.rowNumEnd = rowNumEnd;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    List<ApprovalsClaimsBean> hiddenAttributeList = new ArrayList<ApprovalsClaimsBean>();
    ActionErrors actionErrors = super.validate( mapping, request );
    if ( actionErrors.isEmpty() )
    {
      for ( Iterator iter = claimProductApprovalFormByClaimProductIdString.keySet().iterator(); iter.hasNext(); )
      {
        String claimProductIdString = (String)iter.next();
        ApprovalsClaimsForm claimProductApprovalForm = (ApprovalsClaimsForm)claimProductApprovalFormByClaimProductIdString.get( claimProductIdString );

        if ( claimProductApprovalForm.getApprovalStatusType() != null && claimProductApprovalForm.getApprovalStatusType().equals( ApprovalStatusType.DENIED ) )
        {
          ApprovalsClaimsBean approvalsClaimsBean = new ApprovalsClaimsBean();
          approvalsClaimsBean.setApprovalStatusType( claimProductApprovalForm.getApprovalStatusType() );
          approvalsClaimsBean.setClaimProductIdString( claimProductIdString );
          approvalsClaimsBean.setDenyPromotionApprovalOptionReasonType( claimProductApprovalForm.getDenyPromotionApprovalOptionReasonType() );
          hiddenAttributeList.add( approvalsClaimsBean );
        }

        ActionErrors subFormErrors = claimProductApprovalForm.validate( mapping, request );
        if ( !subFormErrors.isEmpty() )
        {
          request.setAttribute( "hiddenAttributeSelectList", hiddenAttributeList );
          actionErrors = subFormErrors;
        }
      }
    }
    return actionErrors;
  }

}

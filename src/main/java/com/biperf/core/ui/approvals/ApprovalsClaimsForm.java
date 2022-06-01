/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/ClaimProductApprovalForm.java,v $
 */

package com.biperf.core.ui.approvals;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ApprovalsClaimsForm.
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
public class ApprovalsClaimsForm extends BaseForm
{
  private String approvalStatusType;
  private String holdPromotionApprovalOptionReasonType;
  private String denyPromotionApprovalOptionReasonType;

  /**
   * 
   */
  public ApprovalsClaimsForm()
  {
    super();
  }

  public ApprovalsClaimsForm( String type, String holdPromotionApprovalOptionReasonType, String denyPromotionApprovalOptionReasonType )
  {
    super();
    approvalStatusType = type;
    this.holdPromotionApprovalOptionReasonType = holdPromotionApprovalOptionReasonType;
    this.denyPromotionApprovalOptionReasonType = denyPromotionApprovalOptionReasonType;
  }

  /**
   * @return value of approvalStatusType property
   */
  public String getApprovalStatusType()
  {
    return approvalStatusType;
  }

  /**
   * @param approvalStatusType value for approvalStatusType property
   */
  public void setApprovalStatusType( String approvalStatusType )
  {
    this.approvalStatusType = approvalStatusType;
  }

  /**
   * @return value of denyPromotionApprovalOptionReasonType property
   */
  public String getDenyPromotionApprovalOptionReasonType()
  {
    return denyPromotionApprovalOptionReasonType;
  }

  /**
   * @param denyPromotionApprovalOptionReasonType value for denyPromotionApprovalOptionReasonType
   *          property
   */
  public void setDenyPromotionApprovalOptionReasonType( String denyPromotionApprovalOptionReasonType )
  {
    this.denyPromotionApprovalOptionReasonType = denyPromotionApprovalOptionReasonType;
  }

  /**
   * @return value of holdPromotionApprovalOptionReasonType property
   */
  public String getHoldPromotionApprovalOptionReasonType()
  {
    return holdPromotionApprovalOptionReasonType;
  }

  /**
   * @param holdPromotionApprovalOptionReasonType value for holdPromotionApprovalOptionReasonType
   *          property
   */
  public void setHoldPromotionApprovalOptionReasonType( String holdPromotionApprovalOptionReasonType )
  {
    this.holdPromotionApprovalOptionReasonType = holdPromotionApprovalOptionReasonType;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = new ActionErrors();

    String approvalStatusType = getApprovalStatusType();
    if ( approvalStatusType != null && approvalStatusType.equals( ApprovalStatusType.DENIED ) && StringUtils.isBlank( getDenyPromotionApprovalOptionReasonType() ) )
    {
      // deny selected and no reason code given
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "recognition.approval.list", "REASON_CODE_REQUIRED_ERROR" ) ) );
    }
    else if ( approvalStatusType != null && approvalStatusType.equals( ApprovalStatusType.HOLD ) && StringUtils.isBlank( getHoldPromotionApprovalOptionReasonType() ) )
    {
      // hold selected and no reason code given
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "recognition.approval.list", "REASON_CODE_REQUIRED_ERROR" ) ) );
    }

    return actionErrors;
  }
}

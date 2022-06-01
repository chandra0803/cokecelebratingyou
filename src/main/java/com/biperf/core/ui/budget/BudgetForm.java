/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/BudgetForm.java,v $
 */

package com.biperf.core.ui.budget;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * BudgetForm.
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
 * <td>robinsra</td>
 * <td>May 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetForm extends BaseActionForm
{
  public static final String UPDATE_METHOD_NO_CHANGE = "noChange";
  public static final String ADD_TO_BUDGET = "addToBudget";
  public static final String TRANSFER_TO_BUDGET = "transfer";

  private String budgetMasterId;
  private String budgetName;
  private String userId;
  private String nodeId;
  private String paxOrNode;
  private String method;

  private String ownerName;
  private String budgetStatusType;
  private String budgetStatusTypeDesc;
  private String originalValue;
  private String availableValue;
  private String qtyToAdd;
  private String calculatedOriginalValue;
  private String calculatedAvailableValue;

  private long budgetId;
  private long version;
  private long dateCreated;
  private String createdBy;

  private String budgetType;
  private Long selectedParticipantId;
  private Long selectedNodeId;
  private String budgetsToShow;
  private String searchQuery;

  private String updateMethod = UPDATE_METHOD_NO_CHANGE;
  private String amountToTransfer;
  private String searchQuery2;
  private Long selectedTransferId;

  private Long transferBudgetOriginalAmount;
  private Long transferBudgetCurrentAmount;

  private Long budgetSegmentId;
  private String budgetSegmentName;

  private List budgetSegmentList = new ArrayList();

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( paxOrNode == null || !paxOrNode.equals( "NODE" ) && !paxOrNode.equals( "PAX" ) )
    {
      // Verify that is says PAX or NODE
      // - should be set on the prepares when entering the screen

      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) ) );
    }

    // Verify that at least one or the other is filled in (a node id or user id)
    if ( ( nodeId == null || nodeId.equals( "" ) ) && ( userId == null || userId.equals( "" ) ) )
    {
      actionErrors.add( "owner", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.budgetmaster.details.OWNER" ) ) );

    }

    if ( actionMapping.getPath().equals( "/budgetMaintainCreate" ) )
    {
      try
      {
        if ( originalValue != null && !originalValue.isEmpty() && Integer.parseInt( originalValue ) < 1 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budget.errors.AMOUNT_IS_NEGATIVE" ) );
        }
      }
      catch( NumberFormatException ne )
      {
        actionErrors.add( "originalValue", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "admin.budget.details.AMOUNT" ) ) );
      }
    }

    // Verify amounts are not too large
    if ( originalValue != null && !originalValue.isEmpty() && originalValue.length() > 8 || calculatedOriginalValue != null && calculatedOriginalValue.length() > 8
        || calculatedAvailableValue != null && calculatedAvailableValue.length() > 8 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budget.errors.AMOUNT_TOO_LARGE" ) );
    }

    if ( ADD_TO_BUDGET.equalsIgnoreCase( getUpdateMethod() ) )
    {
      if ( StringUtils.isBlank( getQtyToAdd() ) )
      {
        actionErrors.add( "qtyToAdd", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.budget.details.AMOUNT_TO_ADD" ) ) );
      }
      else
      {
        // BugFix 20993,Allow inputiing negative budget amount values.
        try
        {
          // make sure the data is actually valid for Numeric of Integers
          Integer.parseInt( getQtyToAdd() );

        }
        catch( NumberFormatException e )
        {

          actionErrors.add( "qtyToAdd", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INTEGER, CmsResourceBundle.getCmsBundle().getString( "admin.budget.details.AMOUNT_TO_ADD" ) ) );
        }
      }
    }

    if ( TRANSFER_TO_BUDGET.equalsIgnoreCase( getUpdateMethod() ) )
    {
      if ( getSelectedTransferId() == null || getSelectedTransferId().longValue() == 0 )
      {
        actionErrors.add( "searchQuery2", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.budget.details.RECEIVING_OWNER" ) ) );
      }
      if ( StringUtils.isBlank( getAmountToTransfer() ) )
      {
        actionErrors.add( "amountToTransfer",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.budget.details.AMOUNT_TO_TRANSFER" ) ) );
      }
      else if ( !StringUtils.isNumeric( getAmountToTransfer() ) )
      {
        actionErrors.add( "amountToTransfer",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INTEGER, CmsResourceBundle.getCmsBundle().getString( "admin.budget.details.AMOUNT_TO_TRANSFER" ) ) );
      }
      else
      {
        BigDecimal amountToTransfer = NumberUtils.createBigDecimal( getAmountToTransfer() );
        BigDecimal availableBudget = NumberUtils.createBigDecimal( getAvailableValue() );
        if ( amountToTransfer.compareTo( availableBudget ) > 0 )
        {
          actionErrors.add( "amountToTransfer", new ActionMessage( "admin.budget.errors.TRANSFER_AMOUNT_TOO_LARGE" ) );

        }
      }
    }

    return actionErrors;
  } // end validate

  /**
   * Load the Budget to the form
   * 
   * @param budget
   */
  public void load( Budget budget )
  {
    BudgetSegment budgetSegment = budget.getBudgetSegment();
    this.budgetSegmentId = budgetSegment.getId();
    this.budgetSegmentName = budgetSegment.getDisplaySegmentName();

    BudgetMaster budgetMaster = budgetSegment.getBudgetMaster();
    this.budgetMasterId = String.valueOf( budgetMaster.getId() );
    this.budgetName = String.valueOf( CmsResourceBundle.getCmsBundle().getString( budgetMaster.getCmAssetCode(), budgetMaster.getNameCmKey() ) );

    User user = budget.getUser();

    if ( user != null )
    {
      this.userId = String.valueOf( user.getId() );
      this.ownerName = user.getFirstName() + " " + user.getLastName();
      this.paxOrNode = "PAX";
    }

    Node node = budget.getNode();
    if ( node != null )
    {
      this.nodeId = String.valueOf( node.getId() );
      this.ownerName = node.getName();
      this.paxOrNode = "NODE";
    }

    if ( budget.getStatus() != null )
    {
      this.budgetStatusType = budget.getStatus().getCode();
      this.budgetStatusTypeDesc = budget.getStatus().getName();
    }
    this.originalValue = String.valueOf( budget.getOriginalValue() );
    this.availableValue = String.valueOf( budget.getCurrentValue() );

    this.calculatedOriginalValue = "";
    this.calculatedAvailableValue = "";

    this.qtyToAdd = "";
    this.amountToTransfer = "";

    this.budgetId = budget.getId().longValue();
    this.createdBy = budget.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = budget.getAuditCreateInfo().getDateCreated().getTime();
    this.version = budget.getVersion().longValue();

  } // end load

  /**
   * Builds a domain object from the form.
   * 
   * @return Budget
   */
  public Budget toInsertedDomainObject()
  {
    Budget budget = new Budget();
    // NOTE - all association ids will be passed, not set (ex - budgetMasterId, budgetSegmentId,
    // userId,nodeId)

    budget.setStatus( BudgetStatusType.lookup( this.budgetStatusType ) );
    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

    BigDecimal budgetValue = new BigDecimal( this.originalValue );
    budget.setOriginalValue( budgetValue );
    budget.setCurrentValue( budgetValue );

    return budget;
  } // end toInsertedDomainObject

  /**
   * Builds a full domain object from the form.
   * 
   * @return Budget
   */
  public Budget toFullDomainObject()
  {
    Budget budget = new Budget();
    // NOTE - all association ids will be passed, not set (ex - budgetMasterId,userId,nodeId)

    budget.setStatus( BudgetStatusType.lookup( this.budgetStatusType ) );

    // Set the Values with the newly calculated ones.
    BigDecimal calcOrigValue = new BigDecimal( this.calculatedOriginalValue );
    BigDecimal calcAvailValue = new BigDecimal( this.calculatedAvailableValue );
    budget.setOriginalValue( calcOrigValue );
    budget.setCurrentValue( calcAvailValue );
    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

    budget.setId( new Long( this.budgetId ) );
    budget.setVersion( new Long( this.version ) );
    budget.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    budget.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );

    return budget;
  } // end toInsertedDomainObject

  public String getAvailableValue()
  {
    return availableValue;
  }

  public void setAvailableValue( String availableValue )
  {
    this.availableValue = availableValue;
  }

  public String getBudgetStatusType()
  {
    return budgetStatusType;
  }

  public void setBudgetStatusType( String budgetStatusType )
  {
    this.budgetStatusType = budgetStatusType;
  }

  public String getBudgetStatusTypeDesc()
  {
    return budgetStatusTypeDesc;
  }

  public void setBudgetStatusTypeDesc( String budgetStatusTypeDesc )
  {
    this.budgetStatusTypeDesc = budgetStatusTypeDesc;
  }

  public String getCalculatedAvailableValue()
  {
    return calculatedAvailableValue;
  }

  public void setCalculatedAvailableValue( String calculatedAvailableValue )
  {
    this.calculatedAvailableValue = calculatedAvailableValue;
  }

  public String getCalculatedOriginalValue()
  {
    return calculatedOriginalValue;
  }

  public void setCalculatedOriginalValue( String calculatedOriginalValue )
  {
    this.calculatedOriginalValue = calculatedOriginalValue;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getOriginalValue()
  {
    return originalValue;
  }

  public void setOriginalValue( String originalValue )
  {
    this.originalValue = originalValue;
  }

  public String getOwnerName()
  {
    return ownerName;
  }

  public void setOwnerName( String ownerName )
  {
    this.ownerName = ownerName;
  }

  public String getQtyToAdd()
  {
    return qtyToAdd;
  }

  public void setQtyToAdd( String qtyToAdd )
  {
    this.qtyToAdd = qtyToAdd;
  }

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public String getPaxOrNode()
  {
    return paxOrNode;
  }

  public void setPaxOrNode( String paxOrNode )
  {
    this.paxOrNode = paxOrNode;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getBudgetMasterId()
  {
    return budgetMasterId;
  }

  public void setBudgetMasterId( String budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public String getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( String nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public long getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( long budgetId )
  {
    this.budgetId = budgetId;
  }

  public String getBudgetName()
  {
    return budgetName;
  }

  public void setBudgetName( String budgetName )
  {
    this.budgetName = budgetName;
  }

  public String getBudgetType()
  {
    return budgetType;
  }

  public void setBudgetType( String budgetType )
  {
    this.budgetType = budgetType;
  }

  public String getBudgetTypeForDisplay()
  {
    if ( StringUtils.isNotBlank( getBudgetType() ) )
    {
      BudgetType type = BudgetType.lookup( getBudgetType() );
      if ( type != null )
      {
        return type.getName().toLowerCase();
      }
    }
    return "";
  }

  public Long getSelectedParticipantId()
  {
    return selectedParticipantId;
  }

  public void setSelectedParticipantId( Long selectedParticipantId )
  {
    this.selectedParticipantId = selectedParticipantId;
  }

  public Long getSelectedNodeId()
  {
    return selectedNodeId;
  }

  public void setSelectedNodeId( Long selectedNodeId )
  {
    this.selectedNodeId = selectedNodeId;
  }

  public String getBudgetsToShow()
  {
    return budgetsToShow;
  }

  public void setBudgetsToShow( String budgetsToShow )
  {
    this.budgetsToShow = budgetsToShow;
  }

  public String getSearchQuery()
  {
    return searchQuery;
  }

  public void setSearchQuery( String searchQuery )
  {
    this.searchQuery = searchQuery;
  }

  public String getUpdateMethod()
  {
    return updateMethod;
  }

  public void setUpdateMethod( String updateMethod )
  {
    this.updateMethod = updateMethod;
  }

  public String getAmountToTransfer()
  {
    return amountToTransfer;
  }

  public void setAmountToTransfer( String amountToTransfer )
  {
    this.amountToTransfer = amountToTransfer;
  }

  public String getSearchQuery2()
  {
    return searchQuery2;
  }

  public void setSearchQuery2( String searchQuery2 )
  {
    this.searchQuery2 = searchQuery2;
  }

  public Long getSelectedTransferId()
  {
    return selectedTransferId;
  }

  public void setSelectedTransferId( Long selectedTransferId )
  {
    this.selectedTransferId = selectedTransferId;
  }

  public Long getTransferBudgetOriginalAmount()
  {
    return transferBudgetOriginalAmount;
  }

  public void setTransferBudgetOriginalAmount( Long transferBudgetOriginalAmount )
  {
    this.transferBudgetOriginalAmount = transferBudgetOriginalAmount;
  }

  public Long getTransferBudgetCurrentAmount()
  {
    return transferBudgetCurrentAmount;
  }

  public void setTransferBudgetCurrentAmount( Long transferBudgetCurrentAmount )
  {
    this.transferBudgetCurrentAmount = transferBudgetCurrentAmount;
  }

  public String getReceiverFullName()
  {
    return searchQuery2;
  }

  public Long getBudgetSegmentId()
  {
    return budgetSegmentId;
  }

  public void setBudgetSegmentId( Long budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public List getBudgetSegmentList()
  {
    return budgetSegmentList;
  }

  public void setBudgetSegmentList( List budgetSegmentList )
  {
    this.budgetSegmentList = budgetSegmentList;
  }

  public String getBudgetSegmentName()
  {
    return budgetSegmentName;
  }

  public void setBudgetSegmentName( String budgetSegmentName )
  {
    this.budgetSegmentName = budgetSegmentName;
  }

} // end class BudgetForm

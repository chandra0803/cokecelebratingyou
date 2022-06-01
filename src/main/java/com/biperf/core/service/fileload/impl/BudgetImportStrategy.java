/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/BudgetImportStrategy.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.fileload.BudgetImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/*
 * BudgetImportStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 13, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class BudgetImportStrategy extends ImportStrategy
{
  public static final String BUDGET_USER_OR_NODE_ID = "budget.label.USER_OR_NODE_ID";
  public static final String BUDGET_AMOUNT = "budget.label.AMOUNT";

  private UserService userService;
  private ParticipantService paxService;
  private NodeService nodeService;
  private BudgetMasterService budgetService;
  private PromotionService promotionService;
  private CountryService countryService;

  /**
   * Verifies the specified import file.
   * @param records the import records to import.
   * @param importFile the import file to import.
   */
  public void verifyImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    validateBudgetImportRecord( importFile, records );

  }

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the records to import.
   * @param justForPaxRightNow 
   * @throws ServiceErrorException
   */
  public void verifyImportFile( ImportFile importFile, List records, boolean justForPaxRightNow ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    verifyImportFile( importFile, records );
  }

  /**
   * Validates the given import record.
   * 
   * @param budgetRecord the import record to validate.
   * @param basePromotion the promotion to validate against.
   * @return the number of import records with errors in the import file.
   */
  protected void validateBudgetImportRecord( ImportFile importFile, List records )
  {
    Map<String, Object> result = getBudgetService().verifyImportFile( importFile.getId(), importFile.getPromotion().getId() );

    int errorCount = ( (BigDecimal)result.get( "p_out_total_error_rec" ) ).intValue();
    importFile.setImportRecordErrorCount( errorCount );
    importFile.setDateVerified( DateUtils.getCurrentDate() );
    importFile.setVerifiedBy( UserManager.getUserName() );

    int resultCode = ( (BigDecimal)result.get( "p_out_returncode" ) ).intValue();
    if ( resultCode == 0 || resultCode == 1 )
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFIED ) );
    }
    else
    {
      importFile.setHierarchy( null );
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_FAILED ) );
    }
  }

  private Budget getBudgetforBudgetRecord( BudgetImportRecord budgetRecord, BudgetMaster budgetMaster, Long budgetSegmentId )
  {
    Budget budget = null;

    if ( budgetMaster.getBudgetType().equals( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) ) )
    {
      budget = getBudgetService().getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegmentId, budgetRecord.getUserId() );
    }
    else
    {
      budget = getBudgetService().getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegmentId, budgetRecord.getNodeId() );
    }

    return budget;
  }

  private Budget buildBudget( BudgetImportRecord budgetRecord, BudgetMaster budgetMaster, BudgetSegment budgetSegment, boolean replaceValue, BigDecimal countryRatio ) throws ServiceErrorException
  {
    Budget budget = null;

    // Instead of iterating through each budget, go get the one with the budgetMasterId and value
    // from the budgetRecord
    if ( budgetMaster.getBudgetType().equals( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) ) )
    {
      budget = budgetService.getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegment.getId(), budgetRecord.getUserId() );
    }
    else
    {
      budget = budgetService.getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegment.getId(), budgetRecord.getNodeId() );
    }

    if ( budget == null )
    {
      budget = new Budget();
      budget.setBudgetSegment( budgetSegment );
      budget.setOriginalValue( BigDecimal.ZERO );
      budget.setCurrentValue( BigDecimal.ZERO );

      if ( budgetMaster.getBudgetType().equals( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) ) )
      {
        budget.setUser( getUserService().getUserById( budgetRecord.getUserId() ) );
      }
      else
      {
        budget.setNode( getNodeService().getNodeById( budgetRecord.getNodeId() ) );
      }
    }
    else
    {
      if ( replaceValue )
      {
        budget.setOriginalValue( BigDecimal.ZERO );
        budget.setCurrentValue( BigDecimal.ZERO );

        budget.setEffectiveDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
        if ( budgetMaster.getBudgetType().equals( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) ) )
        {
          if ( budget.getUser().isActive().booleanValue() && budget.getStatus().getCode().equals( BudgetStatusType.ACTIVE ) )
          {
            getBudgetService().updateUserBudget( budgetSegment, budget.getUser().getId(), budget );
          }
        }
        else
        {
          if ( budget.getStatus().getCode().equals( BudgetStatusType.ACTIVE ) )
          {
            getBudgetService().updateNodeBudget( budgetSegment, budget.getNode().getId(), budget );
          }
        }
      }
    }

    BigDecimal convertedBudgetAmount = BudgetUtils.applyMediaConversion( budgetRecord.getBudgetAmount(), countryRatio );
    budget.setCurrentValue( budget.getCurrentValue().add( convertedBudgetAmount ) );
    budget.setOriginalValue( budget.getOriginalValue().add( convertedBudgetAmount ) );
    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );
    return budget;
  }

  public NodeService getNodeService()
  {
    return nodeService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public BudgetMasterService getBudgetService()
  {
    return budgetService;
  }

  public void setBudgetService( BudgetMasterService budgetService )
  {
    this.budgetService = budgetService;
  }

  public ParticipantService getPaxService()
  {
    return paxService;
  }

  public void setPaxService( ParticipantService paxService )
  {
    this.paxService = paxService;
  }

  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    Map<String, Object> result = getBudgetService().importImportFile( importFile.getId(), UserManager.getUserId() );

    int errorCount = ( (BigDecimal)result.get( "p_total_error_rec" ) ).intValue();
    importFile.setDateImported( DateUtils.getCurrentDate() );
    importFile.setImportedBy( UserManager.getUserName() );
    importFile.setImportRecordErrorCount( errorCount );

    int resultCode = ( (BigDecimal)result.get( "p_out_returncode" ) ).intValue();
    if ( resultCode == 0 || resultCode == 1 )
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    }
    else
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORT_FAILED ) );
    }
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public CountryService getCountryService()
  {
    return countryService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }
}


package com.biperf.core.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetToNodeOwnersAddressAssociationRequest;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class BudgetTransferMailingProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( BudgetTransferMailingProcess.class );

  public static final String PROCESS_NAME = "Budget Transfer Mailing Process";
  public static final String BEAN_NAME = "budgetTransferMailingProcess";

  private MailingService mailingService;
  private MessageService messageService;
  private BudgetMasterService budgetMasterService;
  private String budgetMasterId;
  private String budgetSegmentId;
  private String[] childBudgetIds;
  private String[] adjustmentAmounts;
  private String adjustmentAmountString;
  private String childBudgetIdString;

  @SuppressWarnings( "unchecked" )
  protected void onExecute()
  {
    AssociationRequestCollection budgetAssociationRequestCollection = new AssociationRequestCollection();
    budgetAssociationRequestCollection.add( new BudgetToNodeOwnersAddressAssociationRequest() );

    if ( !StringUtil.isEmpty( budgetMasterId ) && !StringUtil.isEmpty( budgetSegmentId ) )
    {
      try
      {
        childBudgetIds = com.biperf.core.service.util.StringUtil.parseCommaDelimitedList( childBudgetIdString );
        adjustmentAmounts = com.biperf.core.service.util.StringUtil.parseCommaDelimitedList( adjustmentAmountString );

        for ( int index = 0; index < childBudgetIds.length; index++ )
        {
          Long budgetId = Long.parseLong( childBudgetIds[index] );
          BigDecimal adjustedValue = new BigDecimal( adjustmentAmounts[index] );
          Budget childNodeOwnerBudget = budgetMasterService.getBudgetbyId( budgetId, budgetAssociationRequestCollection );

          Long nodeOwnerId = getBudgetOwner( childNodeOwnerBudget );

          sendBudgetReallocationRecipientEmail( nodeOwnerId, childNodeOwnerBudget, adjustedValue );

          if ( childNodeOwnerBudget.getNode() != null )
          {
            List<User> users = getManagers( childNodeOwnerBudget.getNode().getId() );
            for ( User user : users )
            {
              sendBudgetReallocationRecipientEmail( user.getId(), childNodeOwnerBudget, adjustedValue );
            }
          }
        }
      }
      catch( Exception e )
      {
        log.error( "Cannot process Budget Transfer for budget master id: " + budgetMasterId + " and budgetSegmentId: " + budgetSegmentId + e );
      }
    }
  }

  /**
   * @param recipientNodeOwnerBudget
   * @param adjustmentValue
   */
  private void sendBudgetReallocationRecipientEmail( Long nodeOwnerId, Budget recipientNodeOwnerBudget, BigDecimal adjustmentValue )
  {
    final BigDecimal US_MEDIA_VALUE = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
    final BigDecimal RECIPIENT_MEDIA_VALUE = getBudgetMediaValueForBudget( recipientNodeOwnerBudget );
    MailingRecipient mr = new MailingRecipient();
    BudgetSegment budgetSegment = recipientNodeOwnerBudget.getBudgetSegment();
    BudgetMaster budgetMaster = budgetSegment.getBudgetMaster();

    // Long recipientNodeOwnerId = getBudgetOwnerUserId( recipientNodeOwnerBudget );
    if ( nodeOwnerId != null )
    {
      User recipientNodeOwner = getUserService().getUserById( nodeOwnerId );

      mr.setUser( recipientNodeOwner );
      if ( recipientNodeOwner.getLanguageType() != null )
      {
        mr.setLocale( recipientNodeOwner.getLanguageType().getCode() );
      }
      else
      {
        mr.setLocale( getSystemVariableService().getDefaultLanguage().getStringVal() );
      }

      mr.setGuid( GuidUtils.generateGuid() );

      SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );
      String timePeriodStartDate = sdf.format( budgetSegment.getStartDate() );
      String timePeriodEndDate = null;
      if ( budgetSegment.getEndDate() != null )
      {
        timePeriodEndDate = sdf.format( budgetSegment.getEndDate() );
      }

      Map<String, Object> dataMap = new HashMap<String, Object>();
      dataMap.put( "firstName", recipientNodeOwner.getFirstName() );
      dataMap.put( "lastName", recipientNodeOwner.getLastName() );
      dataMap.put( "budgetMasterName", budgetMaster.getBudgetMasterName() );
      dataMap.put( "promotionName", budgetMaster.getBudgetMasterName() );
      dataMap.put( "timePeriodStartDate", timePeriodStartDate );
      dataMap.put( "timePeriodEndDate", timePeriodEndDate );
      if ( adjustmentValue.doubleValue() > 0 )
      {
        dataMap.put( "increased", "true" );
      }
      BigDecimal recipientAdjustmentValue = BudgetUtils.applyMediaConversion( adjustmentValue, US_MEDIA_VALUE, RECIPIENT_MEDIA_VALUE );
      String adjstmtValue = NumberFormatUtil.getUserLocaleBasedNumberFormat( recipientAdjustmentValue.doubleValue() > 0
          ? BudgetUtils.getBudgetDisplayValue( recipientAdjustmentValue )
          : BudgetUtils.getBudgetDisplayValue( recipientAdjustmentValue.negate() ), LocaleUtils.getLocale( mr.getLocale() ) );
      dataMap.put( "changedBudgetAmount", String.valueOf( adjstmtValue ) );
      if ( ( adjustmentValue.doubleValue() > 0 ? adjustmentValue.doubleValue() : adjustmentValue.negate().doubleValue() ) > 1 )
      {
        dataMap.put( "multipleCBA", "true" );
      }
      // String crntValue = NumberFormatUtil.getUserLocaleBasedNumberFormat(
      // BudgetUtils.getBudgetDisplayValue( recipientNodeOwnerBudget.getCurrentValue() ),
      // LocaleUtils.getLocale( mr.getLocale() ) );
      // Bug Fix - 65033
      BigDecimal convertedCurrentValue = BudgetUtils.applyMediaConversion( recipientNodeOwnerBudget.getCurrentValue(), US_MEDIA_VALUE, RECIPIENT_MEDIA_VALUE );
      // End - 65033
      dataMap.put( "totalBudgetAmount", String.valueOf( convertedCurrentValue.longValue() ) );
      if ( recipientNodeOwnerBudget.getCurrentValue().doubleValue() > 1 )
      {
        dataMap.put( "multipleTBA", "true" );
      }

      ContentReader contentReader = ContentReaderManager.getContentReader();
      List promotionAwardList = new ArrayList();
      if ( contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) ) instanceof java.util.List )
      {
        promotionAwardList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) );
        for ( Object content : promotionAwardList )
        {
          Content contentData = (Content)content;
          Map m = contentData.getContentDataMapList();
          Translations nameObject = (Translations)m.get( "CODE" );

          if ( nameObject.getValue().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
          {
            Translations valueObject = (Translations)m.get( "NAME" );
            dataMap.put( "awardMedia", valueObject.getValue() );
            break;
          }
        }
      }

      String systemUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      dataMap.put( "siteUrl", systemUrl );

      mr.addMailingRecipientDataFromMap( dataMap );

      Message message = messageService.getMessageByCMAssetCode( MessageService.BUDGET_REALLOCATION_RECIPIENT_NOTIFICATION_MESSAGE_CM_ASSET_CODE );
      // Create mailing object
      Mailing mailing = new Mailing();
      mailing.setMessage( message );
      mailing.addMailingRecipient( mr );
      mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
      mailing.setSender( "Incentive System Mailbox" );
      mailing.setMailingType( MailingType.lookup( MailingType.CLAIM_FORM_STEP ) );
      mailing.setGuid( GuidUtils.generateGuid() );

      mailingService.submitMailing( mailing, null );
    }
  }

  private BigDecimal getBudgetMediaValueForBudget( Budget budget )
  {
    Long budgetOwnerId = getBudgetOwnerUserId( budget );
    return budgetOwnerId == null ? null : getUserService().getBudgetMediaValueForUser( budgetOwnerId );
  }

  private Long getBudgetOwnerUserId( Budget budget )
  {
    Long budgetOwnerId = null;
    BudgetMaster budgetMaster = budget.getBudgetSegment().getBudgetMaster();
    if ( budgetMaster.isNodeBudget() )
    {
      budgetOwnerId = budget.getNode().getNodeOwner() == null ? null : budget.getNode().getNodeOwner().getId();
    }
    else if ( budgetMaster.isParticipantBudget() )
    {
      budgetOwnerId = budget.getUser().getId();
    }

    return budgetOwnerId;
  }

  public void setBudgetMasterId( String budgetMasterId )
  {
    this.budgetMasterId = budgetMasterId;
  }

  public void setBudgetSegmentId( String budgetSegmentId )
  {
    this.budgetSegmentId = budgetSegmentId;
  }

  public void setChildBudgetIdString( String childBudgetIdString )
  {
    this.childBudgetIdString = childBudgetIdString;
  }

  public void setAdjustmentAmountString( String adjustmentAmountString )
  {
    this.adjustmentAmountString = adjustmentAmountString;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setBudgetMasterService( BudgetMasterService budgetMasterService )
  {
    this.budgetMasterService = budgetMasterService;
  }

  private List<User> getManagers( Long nodeId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    List<User> managers = getUserService().getAllUsersOnNodeHavingRole( nodeId, HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ), associationRequestCollection );
    return managers;

  }

  private Long getBudgetOwner( Budget budget )
  {
    User budgetOwner = null;
    BudgetMaster budgetMaster = budget.getBudgetSegment().getBudgetMaster();
    if ( budgetMaster.isNodeBudget() )
    {
      budgetOwner = budget.getNode().getNodeOwner() == null ? null : budget.getNode().getNodeOwner();
    }
    else if ( budgetMaster.isParticipantBudget() )
    {
      budgetOwner = budget.getUser();
    }

    if ( budgetOwner != null )
    {
      return budgetOwner.getId();
    }

    return null;
  }

}

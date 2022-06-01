
package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;

public class MessageSMSGroupType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.message.smsgroup";

  public static final String RECOGNITION_RECEIVED = "recognitionReceived";
  public static final String BUDGET_END_ALERT = "budgetEndAlerts";
  public static final String GOAL_REMINDERS = "goalReminders";
  public static final String DEPOSIT_NOTIFICATION = "depositNotifications";
  public static final String PROMOTIONAL_MESSAGE = "promotionalMessages";
  public static final String INTEGER = "int";
  private static SystemVariableService systemVariableService;

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected MessageSMSGroupType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( MessageSMSGroupType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static MessageSMSGroupType lookup( String code )
  {
    return (MessageSMSGroupType)getPickListFactory().getPickListItem( MessageSMSGroupType.class, code );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  public static List<MessageSMSGroupType> getPlateuOnlyList()
  {
    @SuppressWarnings( "unchecked" )
    List<MessageSMSGroupType> list = new ArrayList<MessageSMSGroupType>( getPickListFactory().getPickList( MessageSMSGroupType.class ) );
    Boolean isDrive = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_PLATFORM_ONLY ).getBooleanVal();
    if ( isDrive )
    {
      Iterator<MessageSMSGroupType> iterator = list.iterator();
      while ( iterator.hasNext() )
      {
        MessageSMSGroupType smsGroupType = iterator.next();
        if ( smsGroupType.getCode().equals( MessageSMSGroupType.BUDGET_END_ALERT ) || smsGroupType.getCode().equals( MessageSMSGroupType.DEPOSIT_NOTIFICATION ) )
        {
          iterator.remove();
        }
      }
    }
    return list;
  }

  private static SystemVariableService getSystemVariableService()
  {
    if ( systemVariableService == null )
    {
      systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    }
    return systemVariableService;
  }

}

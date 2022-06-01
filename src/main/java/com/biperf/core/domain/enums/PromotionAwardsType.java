/**
 *
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PromotionAwardsType.
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
 * <td>asondgeroth</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class PromotionAwardsType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.promotion.awardstype";

  public static final String POINTS = "points"; // Renamed AwardPerQs to Points
  public static final String MERCHANDISE = "merchandise";
  public static final String TRAVEL_AWARD = "travelaward";
  public static final String CASH = "cash";
  public static final String OTHER = "other";
  public static final String NONE = "none";
  public static final String MERCHTRAVEL = "merchTra";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionAwardsType()
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
    List awardTypes = getPickListFactory().getPickList( PromotionAwardsType.class );
    List filteredList = new ArrayList();
    // Filter out new goalquest award types
    for ( Iterator iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PickListItem pickListItem = (PickListItem)iter.next();
      if ( !pickListItem.getCode().equals( MERCHANDISE ) && !pickListItem.getCode().equals( TRAVEL_AWARD ) && !pickListItem.getCode().equals( CASH ) && !pickListItem.getCode().equals( OTHER ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return filteredList;
  }

  /**
   * Get the pick list from content manager.
   *
   * @return List
   */
  /*
   * public static List getNonGoalQuestList() { List awardTypes = getPickListFactory().getPickList(
   * PromotionAwardsType.class ); List filteredList = new ArrayList(); for ( Iterator iter =
   * awardTypes.iterator(); iter.hasNext(); ) { PickListItem pickListItem =
   * (PickListItem)iter.next(); if ( !pickListItem.getCode().equals( MERCHANDISE ) &&
   * !pickListItem.getCode().equals( TRAVEL_AWARD ) ) { filteredList.add( pickListItem ); } } return
   * filteredList; }
   */
  /**
   * Get the pick list from content manager.
   *
   * @return List
   */
  public static List getOtherList()
  {
    List awardTypes = getPickListFactory().getPickList( PromotionAwardsType.class );
    List filteredList = new ArrayList();
    for ( Iterator iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PickListItem pickListItem = (PickListItem)iter.next();
      if ( !pickListItem.getCode().equals( MERCHANDISE ) && !pickListItem.getCode().equals( TRAVEL_AWARD ) && !pickListItem.getCode().equals( CASH ) && !pickListItem.getCode().equals( OTHER )
          && !pickListItem.getCode().equals( NONE ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return filteredList;
  }

  /**
   * Get the pick list from content manager.
   *
   * @return List
   */
  public static List getRecognitionList()
  {
    List awardTypes = getPickListFactory().getPickList( PromotionAwardsType.class );
    List filteredList = new ArrayList();
    for ( Iterator iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PickListItem pickListItem = (PickListItem)iter.next();
      if ( !pickListItem.getCode().equals( TRAVEL_AWARD ) && !pickListItem.getCode().equals( CASH ) && !pickListItem.getCode().equals( OTHER ) && !pickListItem.getCode().equals( NONE ) )
      {
        filteredList.add( pickListItem );
      }

    }
    return filteredList;
  }

  /**
   * Get the pick list from content manager.
   *
   * @return List
   */
  public static List getGoalQuestList( boolean isSalesMakerEnabled )
  {
    List awardTypes = getPickListFactory().getPickList( PromotionAwardsType.class );
    List filteredList = new ArrayList();
    for ( Iterator iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PickListItem pickListItem = (PickListItem)iter.next();
      if ( pickListItem.getCode().equals( POINTS ) || pickListItem.getCode().equals( MERCHANDISE )
          || !pickListItem.getCode().equals( TRAVEL_AWARD ) && !pickListItem.getCode().equals( CASH ) && !pickListItem.getCode().equals( OTHER ) && !pickListItem.getCode().equals( NONE ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return !isSalesMakerEnabled ? filteredList : alterListIfSalesMakerEnabled( filteredList );
  }

  public static List getChallengePointList( boolean isSalesMakerEnabled )
  {
    List awardTypes = ChallengePointAwardType.getList();

    List filteredList = new ArrayList();
    for ( Iterator iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PickListItem pickListItem = (PickListItem)iter.next();
      if ( pickListItem.getCode().equals( POINTS ) || pickListItem.getCode().equals( ChallengePointAwardType.MERCHTRAVEL ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return !isSalesMakerEnabled ? filteredList : alterChallengePointListIfSalesMakerEnabled( filteredList );
  }

  public static List getWellnessList()
  {
    List awardTypes = getPickListFactory().getPickList( PromotionAwardsType.class );
    List filteredList = new ArrayList();
    for ( Iterator iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PickListItem pickListItem = (PickListItem)iter.next();
      if ( pickListItem.getCode().equals( POINTS ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return filteredList;
  }

  public static List<PromotionAwardsType> getNominationList()
  {
    List<PromotionAwardsType> awardTypes = getPickListFactory().getPickList( PromotionAwardsType.class );
    List<PromotionAwardsType> filteredList = new ArrayList<>();
    for ( Iterator<PromotionAwardsType> iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PromotionAwardsType pickListItem = iter.next();
      if ( pickListItem.getCode().equals( POINTS ) || pickListItem.getCode().equals( CASH ) || pickListItem.getCode().equals( OTHER ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return filteredList;
  }

  public static List<PromotionAwardsType> getInactiveAwardTypeList()
  {
    List<PromotionAwardsType> awardTypes = getPickListFactory().getPickList( PromotionAwardsType.class );
    List<PromotionAwardsType> filteredList = new ArrayList<>();
    for ( Iterator<PromotionAwardsType> iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PromotionAwardsType pickListItem = iter.next();
      if ( pickListItem.getCode().equals( NONE ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return filteredList;
  }

  public static List<PromotionAwardsType> getCustomAwardTypeList()
  {
    List<PromotionAwardsType> awardTypes = getPickListFactory().getPickList( PromotionAwardsType.class );
    List<PromotionAwardsType> filteredList = new ArrayList<>();
    for ( Iterator<PromotionAwardsType> iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PromotionAwardsType pickListItem = iter.next();
      if ( pickListItem.getCode().equals( POINTS ) || pickListItem.getCode().equals( CASH ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return filteredList;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   *
   * @param code
   * @return PickListItem
   */
  public static PromotionAwardsType lookup( String code )
  {
    return (PromotionAwardsType)getPickListFactory().getPickListItem( PromotionAwardsType.class, code );
  }

  public static List<PromotionAwardsType> alterListIfSalesMakerEnabled( List<PromotionAwardsType> promotionAwardsTypeList )
  {
    List<PromotionAwardsType> awardTypes = promotionAwardsTypeList;
    List<PromotionAwardsType> filteredList = new ArrayList<>();
    for ( Iterator<PromotionAwardsType> iter = awardTypes.iterator(); iter.hasNext(); )
    {
      PromotionAwardsType pickListItem = iter.next();
      if ( !pickListItem.getCode().equals( MERCHANDISE ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return filteredList;
  }

  public static List<ChallengePointAwardType> alterChallengePointListIfSalesMakerEnabled( List<ChallengePointAwardType> promotionAwardsTypeList )
  {
    List<ChallengePointAwardType> awardTypes = promotionAwardsTypeList;
    List<ChallengePointAwardType> filteredList = new ArrayList<>();
    for ( Iterator<ChallengePointAwardType> iter = awardTypes.iterator(); iter.hasNext(); )
    {
      ChallengePointAwardType pickListItem = iter.next();
      if ( !pickListItem.getCode().equals( ChallengePointAwardType.MERCHTRAVEL ) )
      {
        filteredList.add( pickListItem );
      }
    }
    return filteredList;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  @Override
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  public boolean isPointsAwardType()
  {
    return this.getCode().equals( POINTS );
  }

  public boolean isMerchandiseAwardType()
  {
    return this.getCode().equals( MERCHANDISE );
  }

  public boolean isTravelAwardType()
  {
    return this.getCode().equals( TRAVEL_AWARD );
  }

  public boolean isCashAwardType()
  {
    return this.getCode().equals( CASH );
  }

  public boolean isOtherAwardType()
  {
    return this.getCode().equals( OTHER );
  }

  public boolean isNoneAwardType()
  {
    return this.getCode().equals( NONE );
  }

}

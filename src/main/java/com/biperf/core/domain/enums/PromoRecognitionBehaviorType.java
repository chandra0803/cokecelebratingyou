/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/PromoRecognitionBehaviorType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

import com.objectpartners.cms.util.ContentReaderManager;

/**
 * PromoRecognitionBehaviorType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class PromoRecognitionBehaviorType extends PromotionBehaviorType
{
  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.promo.recognition.behavior";

  public static final String NONE_CODE = "none";
  public static final String INNOVATION_CODE = "innovation";
  public static final String GREAT_IDEA_CODE = "greatidea";
  public static final String ABOVE_BEYOND_CODE = "abovebeyond";
  public static final String LEADERSHIP_CODE = "leadership";
  public static final String OUTSTANDING_CODE = "outstanding";
  public static final String SUPPORTIVE_CODE = "supportive";
  public static final String TEAM_PLAYER_CODE = "teamplayer";

  private static final PromoRecognitionBehaviorType NONE = createNoneItem();

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromoRecognitionBehaviorType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PromoRecognitionBehaviorType
   */
  public static List getList()
  {
    List list = getPickListFactory().getPickList( PromoRecognitionBehaviorType.class, new PickListItemNameComparator() );
    return list;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromoRecognitionBehaviorType
   */
  public static PromoRecognitionBehaviorType lookup( String code )
  {
    if ( NONE_CODE.equals( code ) )
    {
      return NONE;
    }

    return (PromoRecognitionBehaviorType)getPickListFactory().getPickListItem( PromoRecognitionBehaviorType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PromoRecognitionBehaviorType
   */
  public static PromoRecognitionBehaviorType getDefaultItem()
  {
    return (PromoRecognitionBehaviorType)getPickListFactory().getDefaultPickListItem( PromoRecognitionBehaviorType.class );
  }

  public static PromoRecognitionBehaviorType getNoneItem()
  {
    return NONE;
  }

  private static PromoRecognitionBehaviorType createNoneItem()
  {
    PromoRecognitionBehaviorType none = new PromoRecognitionBehaviorType();
    none.setCode( NONE_CODE );
    none.setActive( true );

    none.setName( ContentReaderManager.getText( "NO_BEHAVIOR", "recognition.submit" ) );
    none.setDesc( ContentReaderManager.getText( "NO_BEHAVIOR_SELECTED", "recognition.submit" ) );

    return none;
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
}

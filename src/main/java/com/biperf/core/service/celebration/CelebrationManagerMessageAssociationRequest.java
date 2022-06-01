
package com.biperf.core.service.celebration;

import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.service.BaseAssociationRequest;

public class CelebrationManagerMessageAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Hyrate Level: Promotion
   */
  public static final int CELEBRATION_PROMOTION = 2;

  /**
   * Hyrate Level: Participant
   */

  public static final int CELEBRATION_RECIPIENT = 3;

  /**
   * Hyrate Level: User
   */

  public static final int CELEBRATION_MANAGER = 4;

  /**
   * Hyrate Level: User
   */

  public static final int CELEBRATION_MANAGER_ABOVE = 5;

  public CelebrationManagerMessageAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    CelebrationManagerMessage celebrationManagerMessage = (CelebrationManagerMessage)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateCelebrationPromotion( celebrationManagerMessage );
        hydrateCelebrationRecipient( celebrationManagerMessage );
        hydrateCelebrationManager( celebrationManagerMessage );
        hydrateCelebrationManagerAbove( celebrationManagerMessage );
        break;

      case CELEBRATION_PROMOTION:
        hydrateCelebrationPromotion( celebrationManagerMessage );
        break;

      case CELEBRATION_RECIPIENT:
        hydrateCelebrationRecipient( celebrationManagerMessage );
        break;

      case CELEBRATION_MANAGER:
        hydrateCelebrationManager( celebrationManagerMessage );
        break;

      case CELEBRATION_MANAGER_ABOVE:
        hydrateCelebrationManagerAbove( celebrationManagerMessage );
        break;

      default:
        break;
    } // switch
  }

  private void hydrateCelebrationPromotion( CelebrationManagerMessage celebrationManagerMessage )
  {
    initialize( celebrationManagerMessage.getPromotion() );
  }

  private void hydrateCelebrationRecipient( CelebrationManagerMessage celebrationManagerMessage )
  {
    initialize( celebrationManagerMessage.getRecipient() );
  }

  private void hydrateCelebrationManager( CelebrationManagerMessage celebrationManagerMessage )
  {
    initialize( celebrationManagerMessage.getManager() );
  }

  private void hydrateCelebrationManagerAbove( CelebrationManagerMessage celebrationManagerMessage )
  {
    initialize( celebrationManagerMessage.getManagerAbove() );
  }

}

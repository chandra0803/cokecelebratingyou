/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/Attic/AdminTestGoalQuestLoginPasswordEmailProcess.java,v $
 */

package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.crypto.SHA256Hash;

/**
 * AdminTestGoalQuestLoginPasswordEmailProcess is the process to generate the Login ID email & Password email for goal quest promo.
 * This process created for "EmailTestChangeRequestMay042015.doc"
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
 * <td>Venkatesh Dudam</td>
 * <td>Jun 24, 2015</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class AdminTestGoalQuestLoginPasswordEmailProcess extends GoalQuestWelcomeProcess
{

  private static final Log log = LogFactory.getLog( AdminTestGoalQuestLoginPasswordEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Goal Quest Login Password Email Process";
  public static final String BEAN_NAME = "adminTestGoalQuestLoginPwdEmailProcess";

  private String userName;
  private Long promotionId;
  private String recipientLocale;

  public AdminTestGoalQuestLoginPasswordEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    // Set it here for password validation - This sets it in thread local that MD5Hash uses
    // MD5Hash.setDefaultUpperCase( !getSystemVariableService().getPropertyByName(
    // SystemVariableService.PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() );
    SHA256Hash.setDefaultUpperCase( !getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() );
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */

    log.debug( "Starting Admin Test GoalQuest Login Password Email Process with User Name: " + userName );
    try
    {
      Participant participant = participantService.getParticipantByUserName( userName );
      if ( participant != null )
      {
        User runByUser = userService.getUserById( UserManager.getUserId() );
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
        Promotion promotion = promotionService.getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
        Message message = getGoalquestEmailMessage();
        participant.setLanguageType( LanguageType.lookup( recipientLocale ) );
        sendLaunchMessage( message, (GoalQuestPromotion)promotion, participant );
      }
      else
      {
        addComment( "User name " + userName + " not available in the system to launch Admin Test Goal Quest Login Password Email Process." );
      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing Admin Test Goal Quest Login Password Email Process with Username: " + userName );
    }
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getRecipientLocale()
  {
    return recipientLocale;
  }

  public void setRecipientLocale( String recipientLocale )
  {
    this.recipientLocale = recipientLocale;
  }

}

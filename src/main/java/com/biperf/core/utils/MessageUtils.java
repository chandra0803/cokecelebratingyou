
package com.biperf.core.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.domain.enums.InsertFieldType;
import com.biperf.core.domain.enums.MessageType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;

public class MessageUtils
{
  public static Map emailNotificationTypeMessageTypeMapping = new HashMap();

  static
  {
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PROGRAM_LAUNCH, MessageType.PROMOTION_LAUNCH );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PROGRAM_END, MessageType.PROMOTION_END );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY, MessageType.PARTICIPANT_INACTIVITY );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_RECOGNITION, MessageType.PARTICIPANT_INACTIVITY );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_NOMINATION, MessageType.PARTICIPANT_INACTIVITY );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.APPROVER_REMINDER, MessageType.APPROVER_REMINDER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.GOAL_SELECTED, MessageType.PARTICIPANT_GOAL_SELECTED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.GOAL_NOT_SELECTED, MessageType.PARTICIPANT_GOAL_NOT_SELECTED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PROGRESS_UPDATED, MessageType.PARTICIPANT_PROGRESS_UPDATED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.GOAL_ACHIEVED, MessageType.PARTICIPANT_GOAL_ACHIEVED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.GOAL_NOT_ACHIEVED, MessageType.PARTICIPANT_GOAL_NOT_ACHIEVED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PARTNER_GOAL_ACHIEVED, MessageType.PARTNER_GOAL_ACHIEVED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PARTNER_GOAL_NOT_ACHIEVED, MessageType.PARTNER_GOAL_NOT_ACHIEVED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PARTNER_SELECTED, MessageType.PARTNER_GOAL_SELECTED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PARTNER_PROGRESS_UPDATED, MessageType.PARTNER_PROGRESS_UPDATED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.NON_REDEMPTION_REMINDER, MessageType.NON_REDEMPTION_REMINDER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.GQ_NON_REDEMPTION_REMINDER, MessageType.GQ_NON_REDEMPTION_REMINDER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.BUDGET_SWEEP, MessageType.BUDGET_SWEEP );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.BUDGET_END, MessageType.BUDGET_END );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.BUDGET_REMINDER, MessageType.BUDGET_REMINDER );

    // SK - Promotion Type for GQ Survey

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.RECOGNITION_RECEIVED, MessageType.RECOGNITION_RECEIVED );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PURL_MANAGER_NOTIFICATION, MessageType.PURL_MANAGER_NOTIFICATION );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION, MessageType.PURL_CONTRIBUTOR_INVITATION );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION, MessageType.PURL_RECIPIENT_INVITATION );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE, MessageType.PURL_MANAGER_NONRESPONSE );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE, MessageType.PURL_CONTRIBUTOR_NONRESPONSE );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CHALLENGEPOINT_SELECTED, MessageType.PARTICIPANT_CHALLENGEPOINT_SELECTED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_SELECTED, MessageType.PARTICIPANT_CHALLENGEPOINT_NOT_SELECTED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CHALLENGEPOINT_ACHIEVED, MessageType.PARTICIPANT_CHALLENGEPOINT_ACHIEVED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_ACHIEVED, MessageType.PARTICIPANT_CHALLENGEPOINT_NOT_ACHIEVED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CHALLENGEPOINT_INTERIM_PAYOUT_PROCESSED, MessageType.PARTICIPANT_CHALLENGEPOINT_INTERIM_PAYOUT_PROCESSED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CHALLENGEPOINT_PROGRESS_UPDATED, MessageType.PARTICIPANT_CHALLENGEPOINT_PROGRESS_UPDATED );

    emailNotificationTypeMessageTypeMapping.put( ClaimFormStepEmailNotificationType.CLAIM_SUBMITTED, MessageType.APPROVER_CLAIM_SUBMITTED );
    emailNotificationTypeMessageTypeMapping.put( ClaimFormStepEmailNotificationType.CLAIM_APPROVED, MessageType.PARTICIPANT_CLAIM_REVIEWED );
    emailNotificationTypeMessageTypeMapping.put( ClaimFormStepEmailNotificationType.CLAIM_DENIED, MessageType.PARTICIPANT_CLAIM_REVIEWED );
    emailNotificationTypeMessageTypeMapping.put( ClaimFormStepEmailNotificationType.RECOGNITION_SUBMITTED, MessageType.APPROVER_RECOGNITION_SUBMITTED );
    emailNotificationTypeMessageTypeMapping.put( ClaimFormStepEmailNotificationType.RECOGNITION_APPROVED_EMAIL, MessageType.GIVER_RECOGNITION_APPROVAL_DECISION );
    /*Customization for WIP 32479 starts here*/
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PURL_EXTERNAL_CONTRIBUTOR_INVITATION , MessageType.PURL_EXTERNAL_CONTRIBUTOR_INVITATION );
    /*Customization for WIP 32479 ends here*/
    /*Customization for WIP 46293 starts here*/
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.COKE_PURL_MANAGER_CONTRIBUTOR_INVITATION , MessageType.COKE_PURL_MANAGER_CONTRIBUTOR_INVITATION);
    /*Customization for WIP 46293 ends here*/
    // nominations
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TO_NOMINEE_WHEN_CLAIM_SUBMITTED, MessageType.NOMINEE_NOMINATION_SUBMITTED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TO_NOMINEES_MANAGER_WHEN_CLAIM_SUBMITTED, MessageType.NOMINEE_MGR_NOMINATION_SUBMITTED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TO_APPROVER_WHEN_CLAIM_SUBMITTED, MessageType.APPROVER_NOMINATION_SUBMITTED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TO_NOMINEE_WHEN_WINNER, MessageType.NOMINEE_WINNER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TO_NOMINEES_MANAGER_WHEN_WINNER, MessageType.NOMINEE_MGR_WINNER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TO_NOMINATOR_WHEN_WINNER, MessageType.NOMINATOR_WINNER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TO_NOMINATOR_WHEN_NON_WINNER, MessageType.NOMINATOR_NON_WINNER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.APPROVER_REMINDER_TIME_PERIOD_EXPIRED, MessageType.APPROVER_REMINDER_TIME_PERIOD_EXPIRED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.APPROVER_REMINDER_APPROVAL_END_DATE, MessageType.APPROVER_REMINDER_APPROVAL_END_DATE );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.APPROVER_REQUEST_MORE_INFORMATION, MessageType.APPROVER_REQUEST_MORE_INFORMATION );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.NOMINATOR_REQUEST_MORE_INFORMATION, MessageType.NOMINATOR_REQUEST_MORE_INFORMATION );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PUBLIC_RECOG_ADD_POINTS_FOR_NOMINATION, MessageType.PUBLIC_RECOG_ADD_POINTS_FOR_NOMINATION );

    // badge

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.BADGE_RECEIVED, MessageType.BADGE_RECEIVED );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CP_PARTNER_GOAL_ACHIEVED, MessageType.CP_PARTNER_GOAL_ACHIEVED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CP_PARTNER_GOAL_NOT_ACHIEVED, MessageType.CP_PARTNER_GOAL_NOT_ACHIEVED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CP_PARTNER_GOAL_SELECTED, MessageType.CP_PARTNER_GOAL_SELECTED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CP_PARTNER_PROGRESS_UPDATED, MessageType.CP_PARTNER_PROGRESS_UPDATED );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CP_NON_REDEMPTION_REMINDER, MessageType.CP_NON_REDEMPTION_REMINDER );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TD_MATCH_OUTCOME, MessageType.TD_MATCH_OUTCOME );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TD_NEXT_ROUND, MessageType.TD_NEXT_ROUND );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.TD_PROGRESS_UPDATED, MessageType.TD_PROGRESS_UPDATED );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.PUBLIC_RECOGNITION_ADD_POINTS, MessageType.PUBLIC_RECOGNITION_ADD_POINTS );

    // KPM
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.KPM_MANAGERS_UPDATE, MessageType.KPM_MANAGERS_UPDATE );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.KPM_PARTICIPANT_UPDATE, MessageType.KPM_PARTICIPANT_UPDATE );

    // Celebration
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CELEBRATION_MANAGER_NOTIFICATION, MessageType.CELEBRATION_MANAGER_NOTIFICATION );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CELEBRATION_MANAGER_NONRESPONSE, MessageType.CELEBRATION_MANAGER_NONRESPONSE );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED, MessageType.CELEBRATION_RECOGNITION_RECEIVED );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION, MessageType.CELEBRATION_PURL_RECIPIENT_INVITATION );

    // SSI

    // Common Creator Notifications For All Contest Types
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR, MessageType.CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR, MessageType.CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR, MessageType.CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR, MessageType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR );

    // Common Approver Notifications For All Contest Types
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_APPROVAL_NOTIFY_APPROVER, MessageType.CONTEST_APPROVAL_NOTIFY_APPROVER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER, MessageType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_UPDATE_AFTER_APPROVAL_STATUS_NOTIFY_APPROVER,
                                                 MessageType.CONTEST_UPDATE_AFTER_APPROVAL_STATUS_NOTIFY_APPROVER );

    // SSI - Contest Type: Award Them Now
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW, MessageType.CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW, MessageType.CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW, MessageType.CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_NOTIFY_APPROVER_AWARD_THEM_NOW, MessageType.CONTEST_NOTIFY_APPROVER_AWARD_THEM_NOW );

    // SSI - Contest Type: Do This Get That
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT, MessageType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT, MessageType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT, MessageType.CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT, MessageType.CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT, MessageType.CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT, MessageType.CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT, MessageType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT, MessageType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT, MessageType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT,
                                                 MessageType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT, MessageType.CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT, MessageType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT );

    // SSI - Contest Type: Objectives
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES, MessageType.CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES, MessageType.CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES, MessageType.CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES, MessageType.CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES, MessageType.CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES, MessageType.CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES, MessageType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES, MessageType.CONTEST_END_NOTIFY_MGR_OBJECTIVES );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES, MessageType.CONTEST_END_NOTIFY_PAX_OBJECTIVES );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES, MessageType.CONTEST_FINAL_RESULT_CREATOR_NOTIFY_OBJECTIVES );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_OBJECTIVES, MessageType.CONTEST_FINAL_RESULT_MGR_NOTIFY_OBJECTIVES );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES, MessageType.CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES );

    // SSI - Contest Type: Stack Rank
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK, MessageType.CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK, MessageType.CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK, MessageType.CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK, MessageType.CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK, MessageType.CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK, MessageType.CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK, MessageType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK, MessageType.CONTEST_END_NOTIFY_MGR_STACK_RANK );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK, MessageType.CONTEST_END_NOTIFY_PAX_STACK_RANK );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK, MessageType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK, MessageType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK, MessageType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK );

    // SSI - Contest Type: Step It Up
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP, MessageType.CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP, MessageType.CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP, MessageType.CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP, MessageType.CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP, MessageType.CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP, MessageType.CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP, MessageType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP, MessageType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP, MessageType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP );

    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP, MessageType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP, MessageType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP, MessageType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP );

    // SSI - Contest Claim Notifications
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_CLAIM_APPROVAL_NOTIFY_APPROVER, MessageType.CONTEST_CLAIM_APPROVAL_NOTIFY_APPROVER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.CONTEST_CLAIM_ACTION_NOTIFY_SUBMITTER, MessageType.CONTEST_CLAIM_ACTION_NOTIFY_SUBMITTER );
    emailNotificationTypeMessageTypeMapping.put( PromotionEmailNotificationType.NOMINATION_APPROVER_NOTIFICATION, MessageType.NOMINATION_APPROVER_NOTIFICATION );
    
    
    //Client customizations for WIP #59461
    emailNotificationTypeMessageTypeMapping.put( ClaimFormStepEmailNotificationType.TO_NOMINATOR_WHEN_SUBMITTED , MessageType.NOMINATOR_SUBMITTED );
  }

  public static String getMessageTypeCode( String emailNotificationTypeCode )
  {
    String messageTypeCode = (String)emailNotificationTypeMessageTypeMapping.get( emailNotificationTypeCode );

    if ( messageTypeCode == null )
    {
      throw new RuntimeException( "Associate MessageType to EmailNotificationType (" + emailNotificationTypeCode + ") " );
    }
    return messageTypeCode;
  }

  public static boolean isMessageTextWizardSendable( String textToCheck )
  {
    return isMessageTextWizardSendable( textToCheck, true );
  }

  public static boolean isMessageTextWizardSendable( String textToCheck, boolean allowPromotionName )
  {
    boolean isSendable = true;
    // copy to new String so we don't screw up the original with our check below
    String tempString = new String( textToCheck );
    // replace all valid occurrences of tokens with blank space so we can check for invalid tokens
    for ( Iterator iter = InsertFieldType.getSpecificPatternList().iterator(); iter.hasNext(); )
    {
      Pattern temp = (Pattern)iter.next();
      if ( allowPromotionName || !temp.equals( InsertFieldType.PROMOTION_NAME_PATTERN ) )
      {
        tempString = temp.matcher( tempString ).replaceAll( " " );
      }
    }
    // all valid tokens have been replaced, now check for general pattern matches which are invalid
    // Matcher generalMatcher = InsertFieldType.GENERAL_PATTERN.matcher( tempString );
    // isSendable = !generalMatcher.find();
    isSendable = true;
    return isSendable;
  }

  /**
   * For personalized url strip off absolute url path tinymce inserted
   * 
   * Messages in the library with href="$personalizedURL" are replaced with 
   * href="http://.../.../$personalizedURL" by Tiny MCE, this piece
   * of code is trying to strip off [http://.../.../] inserted by Tiny MCE
   * 
   * @param htmlMsg
   * @return stripped htmlMsg rid of absolute url path inserted by tinymce
   */
  public static String removeUrlFromPersonalizedToken( String htmlMsg )
  {
    String parsed = "";

    String[] hrefs = htmlMsg.split( "href" );

    for ( int i = 0; i < hrefs.length; i++ )
    {
      String href = hrefs[i];

      // Skip the first token stream
      if ( i == 0 )
      {
        parsed = parsed + href;
        continue;
      }

      int idx1 = href.indexOf( "\"" );
      if ( idx1 > 0 )
      {
        int idx2 = href.indexOf( "\"", idx1 + 1 );
        if ( idx2 > 0 )
        {
          String preHrefValue = href.substring( 0, idx1 + 1 );
          String hrefValue = href.substring( idx1 + 1, idx2 );
          String postHrefValue = href.substring( idx2 );

          // if contains absolute path url
          if ( hrefValue.indexOf( "http" ) >= 0 )
          {
            // if contains a "personalization-token" e.g. $certificateLink, get the position
            int pos = hrefValue.indexOf( "$" );

            // strip off the "http://.../.../" part so only the url token remains (e.g.
            // $certificateLink)
            // This is needed because the url will be assembled by code that sends the email later
            // on and
            // should not be saved to the message library
            if ( pos > 0 )
            {
              href = preHrefValue + hrefValue.substring( pos, hrefValue.length() ) + postHrefValue;
            }
          }
        }
      }
      parsed = parsed + "href" + href;
    }
    return parsed.equals( "" ) ? htmlMsg : parsed;
  }
}

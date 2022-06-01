
package com.biperf.core.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.ssi.SSIAdminContestActions;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.domain.ssi.SSIContestPaxClaimField;
import com.biperf.core.service.SAO;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestUtil
{

  private static final int CONTEST_NAME_MAX_LENGTH = 50;
  public static final String CONTEST_ID = "contestId";
  public static final String USER_ID = "userId";
  public static final String SSI_CLIENTSTATE_PARAM_ID = "id";
  protected static final String AWARD_ISSUANCE_NUMBER = "awardIssuanceNumber";
  protected static final String LEVEL_APPROVED = "levelApproved";
  public static final String GET_METHOD = "GET";
  private static final String SPACE_CHAR = " ";
  public static final String SHOW_MODAL = "showModal";
  public static final String MODAL_MESSAGE = "modalMessage";
  public static final String PERCENTAGE = "%";
  public static final String HASHTAG = "#";
  public static final String DEFAULT_ZERO = "0";
  public static final String SELECTION_YES = "yes";

  public static final String ACTIVITY_UPLOAD = "activityUpload";
  public static final String CLAIM_SUBMISSION = "claimSubmission";

  public static final String CLAIM_FIELD_GROUP_ADDRESS = "address";
  public static final String CLAIM_FIELD_SUB_TYPE_ADDRESS1 = "address1";
  public static final String CLAIM_FIELD_SUB_TYPE_ADDRESS2 = "address2";
  public static final String CLAIM_FIELD_SUB_TYPE_ADDRESS3 = "address3";
  public static final String CLAIM_FIELD_SUB_TYPE_CITY = "city";
  public static final String CLAIM_FIELD_SUB_TYPE_STATE = "state";
  public static final String CLAIM_FIELD_SUB_TYPE_COUNTRY = "country";
  public static final String CLAIM_FIELD_SUB_TYPE_POSTAL_CODE = "postalCode";

  public static final String CLAIM_ID = "claimId";
  public static final String CLAIM_NUMBER = "claimNumber";
  public static final String CLAIM_FIELD_TYPE_NUMBER = "number";
  public static final String CLAIM_FIELD_TYPE_DATE = "date";
  public static final String CLAIM_FIELD_TYPE_SELECT = "select";
  public static final String CLAIM_FIELD_TYPE_TEXT_AREA = "textarea";

  public static final String CLAIM_FIELD_QUANTITY = "Quantity";
  public static final String CLAIM_FIELD_AMOUNT = "Amount";

  // all page sizes
  public static final int PAX_MGR_PER_PAGE = 25;
  public static final int PAX_SUPERVIEWER_PER_PAGE = 25;
  public static final int CLAIM_APPROVALS_PER_PAGE = 50;
  public static final int STACK_RANK_PER_PAGE = 100;
  public static final int PAYOUTS_PER_PAGE = 25;
  public static final int CONTEST_DETAIL_PER_PAGE = 50;
  public static final int CLAIM_HISTORY_PER_PAGE = 25;
  public static final int PAX_RECORDS_PER_PAGE = 25;
  public static final int SSTCONTENTAPPROVAL = 1;

  public static final String SORT_BY_LAST_NAME = "lastName";

  public static final String DEFAULT_SORT_BY_STACK_RANK = "stack_rank";
  public static final String DEFAULT_SORT_BY_LAST_NAME = "last_name";

  // pax claim history/ activity history
  public static final String CLAIMS_DEFAULT_SORT_ON = "submissionDate";
  public static final String DEFAULT_SORT_ON = "dateSubmitted";

  public static final String DEFAULT_SORT_BY = "asc";

  public static final int ACTIVITY_CURRENCY_DECIMAL_PRECISION = 2;
  public static final int ACTIVITY_UNITS_DECIMAL_PRECISION = 4;
  public static final int ACTIVITY_UNITS_DECIMAL_MIN_PRECISION = 0;
  public static final int PAYOUT_DECIMAL_PRECISION = 0;

  public static final Integer FIRST_PAGE_NUMBER = 1;

  public static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;

  public static final String ASC = "asc";
  public static final String DESC = "desc";

  public static final int PAX_SEARCH_SIZE = 300;

  public static final String SSI_ADMIN_CC = "ccSSIAdminEmailID";

  public static final String REGEX_DOUBLE_QUOTE = "\"";

  public static int getPrecision( Double object )
  {
    BigDecimal bigDecimalAmount = BigDecimal.valueOf( object );
    return bigDecimalAmount.scale();
  }

  public static String getContestProgressUpdateDate( SSIContest contest )
  {
    if ( contest.getStatus().isFinalizeResults() )
    {
      return DateUtils.toDisplayString( contest.getPayoutIssuedDate() );
    }
    else if ( contest.getLastProgressUpdateDate() != null )
    {
      return DateUtils.toDisplayString( contest.getLastProgressUpdateDate() );
    }
    else if ( SSIContestStatus.LIVE.equals( contest.getStatus().getCode() ) )
    {
      return DateUtils.toDisplayString( contest.getStartDate() );
    }
    else
    {
      return ""; // blank
    }
  }

  /**
   * Create client state with claim Number
   * @param claimNumber 
   * @param ssiContest
   * @return
   */

  public static String createClaimClientState( HttpServletRequest request, Map<String, Object> clientStateParamMap, boolean enCode )
  {
    return getClientState( request, clientStateParamMap, enCode );
  }

  /**
   *fileUrlStringTokenizer
   * @param fileNameAndDocUrl 
   * @return String[]
   */

  public static String[] fileUrlStringTokenizer( String fileNameAndDocUrl )
  {
    return StringUtils.split( fileNameAndDocUrl, "|" );
  }

  /**
   * get claim id from client State
   * @param ssiContest
   * @return
   */

  public static Long getClaimIdFromClientState( HttpServletRequest request, String clientState, boolean decode ) throws InvalidClientStateException
  {
    return Long.valueOf( getParameterFromClientState( request, clientState, decode, CLAIM_ID ) );
  }

  /**
   * get claim Number from client State
   * @param ssiContest
   * @return
   */

  public static String getClaimNumberFromClientState( HttpServletRequest request, String clientState, boolean decode ) throws InvalidClientStateException
  {
    return getParameterFromClientState( request, clientState, decode, CLAIM_NUMBER );
  }

  public static String getClientState( Long contestId )
  {
    // put the client state instead of direct id
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    clientStateParamMap.put( "contestId", contestId );
    String password = ClientStatePasswordManager.getPassword();
    try
    {
      return URLEncoder.encode( ClientStateSerializer.serialize( clientStateParamMap, password ), "UTF-8" );
    }
    catch( UnsupportedEncodingException e )
    {
      throw new RuntimeException( "Unable Encode Client State: " + e );
    }
  }

  public static String getClientState( Long contestId, Long userId )
  {
    // put the client state instead of direct id
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    clientStateParamMap.put( CONTEST_ID, contestId );
    clientStateParamMap.put( USER_ID, userId );
    String password = ClientStatePasswordManager.getPassword();
    try
    {
      return URLEncoder.encode( ClientStateSerializer.serialize( clientStateParamMap, password ), "UTF-8" );
    }
    catch( UnsupportedEncodingException e )
    {
      throw new RuntimeException( "Unable Encode Client State: " + e );
    }
  }

  public static String getClientState( HttpServletRequest request, Long contestId, boolean enCode )
  {
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    clientStateParamMap.put( CONTEST_ID, contestId );

    return getClientState( request, clientStateParamMap, enCode );
  }

  public static String getClientState( HttpServletRequest request, Long contestId, Long userId, boolean enCode )
  {
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    clientStateParamMap.put( CONTEST_ID, contestId );
    clientStateParamMap.put( USER_ID, userId );

    return getClientState( request, clientStateParamMap, enCode );
  }

  public static String getClientState( HttpServletRequest request, Long contestId, Short issuanceNumber, Integer approvalLevelActiontaken, boolean enCode )
  {
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    clientStateParamMap.put( CONTEST_ID, contestId );
    clientStateParamMap.put( AWARD_ISSUANCE_NUMBER, issuanceNumber );
    clientStateParamMap.put( LEVEL_APPROVED, approvalLevelActiontaken );

    return getClientState( request, clientStateParamMap, enCode );
  }

  public static String getClientState( HttpServletRequest request, Map<String, Object> clientStateParams, boolean enCode )
  {
    String password = ClientStatePasswordManager.getPassword();

    String cryptoPass = request.getParameter( "cryptoPass" );
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    try
    {
      String clientState = ClientStateSerializer.serialize( clientStateParams, password );
      if ( enCode )
      {
        return URLEncoder.encode( clientState, "UTF-8" );
      }
      else
      {
        return clientState;
      }
    }
    catch( UnsupportedEncodingException e )
    {
      throw new RuntimeException( "Unable Encode Client State: " + e );
    }
  }

  public static Long getContestIdFromClientState( HttpServletRequest request, String clientState, boolean decode ) throws InvalidClientStateException
  {
    return Long.valueOf( getParameterFromClientState( request, clientState, decode, CONTEST_ID ) );
  }

  public static Long getUserIdFromClientState( HttpServletRequest request, String clientState, boolean decode ) throws InvalidClientStateException
  {
    return Long.valueOf( getParameterFromClientState( request, clientState, decode, USER_ID ) );
  }

  public static Short getIssuanceNumberFromClientState( HttpServletRequest request, String clientState, boolean decode ) throws InvalidClientStateException
  {
    return Short.valueOf( getParameterFromClientState( request, clientState, decode, AWARD_ISSUANCE_NUMBER ) );
  }

  public static Integer getLevelApprovedFromClientState( HttpServletRequest request, String clientState, boolean decode ) throws InvalidClientStateException
  {
    return Integer.valueOf( getParameterFromClientState( request, clientState, decode, LEVEL_APPROVED ) );
  }

  public static String getParameterFromClientState( HttpServletRequest request, String clientState, boolean decode, String paramName ) throws InvalidClientStateException
  {
    try
    {
      if ( decode )
      {
        clientState = URLDecoder.decode( clientState, "UTF-8" );
      }
      String cryptoPass = request.getParameter( "cryptoPass" );

      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      return clientStateMap.get( paramName ).toString();
    }
    catch( UnsupportedEncodingException e )
    {
      throw new RuntimeException( "Unable Decode Client State: " + e );
    }
  }

  public static String getFormattedValue( Double value, int precision, int minPrecision )
  {
    if ( value != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( BigDecimal.valueOf( value ), precision, UserManager.getLocale(), minPrecision );
    }
    else
    {
      return null;
    }
  }

  public static String getFormattedValue( Double value, int precision )
  {
    if ( value != null )
    {
      int minPrecision = precision;
      if ( precision == ACTIVITY_UNITS_DECIMAL_PRECISION )
      {
        minPrecision = ACTIVITY_UNITS_DECIMAL_MIN_PRECISION;
      }
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( BigDecimal.valueOf( value ), precision, UserManager.getLocale(), minPrecision );
    }
    else
    {
      return null;
    }
  }

  public static String getFormattedValue( Long value, int precision )
  {
    if ( value != null )
    {
      int minPrecision = precision;
      if ( precision == ACTIVITY_UNITS_DECIMAL_PRECISION )
      {
        minPrecision = ACTIVITY_UNITS_DECIMAL_MIN_PRECISION;
      }
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( BigDecimal.valueOf( value ), precision, UserManager.getLocale(), minPrecision );
    }
    else
    {
      return null;
    }
  }

  public static int getPrecision( String measureType )
  {
    if ( SSIActivityMeasureType.CURRENCY_CODE.equals( measureType ) )
    {
      return 2;
    }
    else
    {
      return 4;
    }
  }

  public static String getBaselineTypeLabel( SSIContest contest )
  {
    String label = null;
    if ( contest.getActivityMeasureType().isCurrency() )
    {
      if ( contest.getIndividualBaselineType().isNo() )
      {
        label = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_dtgt.CURRENCY" );
      }
      else if ( contest.getIndividualBaselineType().isPercentageOverBaseline() )
      {
        label = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.PERCENTAGE_OVER_BASELINE" );
      }
      else
      {
        label = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.CURRENCY_OVER_BASELINE" );
      }
    }
    else if ( contest.getActivityMeasureType().isUnit() )
    {
      if ( contest.getIndividualBaselineType().isNo() )
      {
        label = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.AMOUNT" );
      }
      else if ( contest.getIndividualBaselineType().isPercentageOverBaseline() )
      {
        label = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.PERCENTAGE_OVER_BASELINE" );
      }
      else
      {
        label = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.AMOUNT_OVER_BASELINE" );
      }
    }
    return label;
  }

  public static int getLiveContestSize( List<SSIContest> contests, String contestType )
  {
    int contestSizeAwardThemNow = 0;
    int contestSizeObjectives = 0;
    int contestSizeDoThisGetThat = 0;
    int contestSizeStepItUp = 0;
    int contestSizeStackRank = 0;
    if ( contests != null && contests.size() > 0 )
    {
      for ( SSIContest contest : contests )
      {
        if ( SSIContestType.OBJECTIVES.equals( contestType ) && contest.getContestType().isObjectives() )
        {
          contestSizeObjectives++;
        }
        else if ( SSIContestType.DO_THIS_GET_THAT.equals( contestType ) && contest.getContestType().isDoThisGetThat() )
        {
          contestSizeDoThisGetThat++;
        }
        else if ( SSIContestType.STEP_IT_UP.equals( contestType ) && contest.getContestType().isStepItUp() )
        {
          contestSizeStepItUp++;
        }
        else if ( SSIContestType.STACK_RANK.equals( contestType ) && contest.getContestType().isStackRank() )
        {
          contestSizeStackRank++;
        }
        else
        {
          contestSizeAwardThemNow++;
        }
      }
      if ( SSIContestType.OBJECTIVES.equals( contestType ) )
      {
        return contestSizeObjectives;
      }
      else if ( SSIContestType.DO_THIS_GET_THAT.equals( contestType ) )
      {
        return contestSizeDoThisGetThat;
      }
      else if ( SSIContestType.STEP_IT_UP.equals( contestType ) )
      {
        return contestSizeStepItUp;
      }
      else if ( SSIContestType.STACK_RANK.equals( contestType ) )
      {
        return contestSizeStackRank;
      }
      else
      {
        return contestSizeAwardThemNow;
      }
    }
    else
    {
      return 0;
    }
  }

  public static String getActivityPrefix( SSIContestValueBean valueBean )
  {
    if ( SSIActivityMeasureType.CURRENCY_CODE.equals( valueBean.getActivityMeasureType().getCode() ) )
    {
      return valueBean.getActivityMeasureCurrency().getCurrencySymbol();
    }
    else
    {
      return "";
    }
  }

  public static String getPayoutPrefix( SSIContestValueBean valueBean )
  {
    if ( SSIPayoutType.OTHER_CODE.equals( valueBean.getPayoutType().getCode() ) )
    {
      return valueBean.getPayoutOtherCurrency() != null ? valueBean.getPayoutOtherCurrency().getCurrencySymbol() : "";
    }
    else
    {
      return "";
    }
  }

  public static String getPayoutSuffix( SSIContestValueBean valueBean )
  {
    if ( SSIPayoutType.POINTS_CODE.equals( valueBean.getPayoutType().getCode() ) )
    {
      return SPACE_CHAR + valueBean.getPayoutType().getName();
    }
    else
    {
      return "";
    }
  }

  public static List<String> validateContestName( String contestName )
  {
    List<String> validationErrors = new ArrayList<String>();
    if ( StringUtil.isNullOrEmpty( contestName ) )
    {
      validationErrors.add( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.CONTEST_NAME_REQUIRED" ) );
    }
    else if ( contestName.length() > CONTEST_NAME_MAX_LENGTH )
    {
      validationErrors.add( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.CONTEST_NAME_MAX_LENGTH" ) + CONTEST_NAME_MAX_LENGTH );
    }
    return validationErrors;
  }

  public static String populateCreatorDetailPageUrl( Long contestId, Long userId, String contestCode )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( CONTEST_ID, contestId.toString() );
    paramMap.put( USER_ID, userId.toString() );
    String detailPageUrl = ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                 PageConstants.SSI_CREATOR_DETAIL_ATN_URL,
                                                                 paramMap,
                                                                 false,
                                                                 CONTEST_ID );
    return detailPageUrl;
  }

  public static String populateCreatorDetailPageUrl( Long contestId, Long userId )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( CONTEST_ID, contestId.toString() );
    paramMap.put( USER_ID, userId.toString() );
    String detailPageUrl = ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                 PageConstants.SSI_CREATOR_DETAIL_REDIRECT_URL,
                                                                 paramMap,
                                                                 false,
                                                                 SSI_CLIENTSTATE_PARAM_ID );
    return detailPageUrl;
  }

  public static String populateManagerDetailPageUrl( Long contestId, Long userId )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( CONTEST_ID, contestId.toString() );
    paramMap.put( USER_ID, userId.toString() );
    String detailPageUrl = ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                 PageConstants.SSI_MGR_DETAIL_REDIRECT_URL,
                                                                 paramMap,
                                                                 false,
                                                                 SSI_CLIENTSTATE_PARAM_ID );
    return detailPageUrl;
  }

  public static String populateParticipantDetailPageUrl( Long contestId, Long userId )
  {
    if ( contestId != null && userId != null )
    {
      Map<String, String> paramMap = new HashMap<String, String>();
      paramMap.put( CONTEST_ID, contestId.toString() );
      paramMap.put( USER_ID, userId.toString() );
      String detailPageUrl = ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                   PageConstants.SSI_PAX_DETAIL_REDIRECT_URL,
                                                                   paramMap,
                                                                   false,
                                                                   SSI_CLIENTSTATE_PARAM_ID );
      return detailPageUrl;
    }
    else
    {
      return "";
    }
  }

  public static Long getHighestLevelPayoutAmount( List<SSIContestLevel> levels )
  {
    long highestLevelPayoutAmt = 0;
    if ( levels != null )
    {
      SSIContestLevel highestLevel = levels.get( 0 );
      for ( SSIContestLevel level : levels )
      {
        if ( highestLevel.getSequenceNumber().intValue() < level.getSequenceNumber().intValue() )
        {
          highestLevel = level;
        }
      }
      highestLevelPayoutAmt = highestLevel.getPayoutAmount();
    }
    return highestLevelPayoutAmt;
  }

  public static Double getHighestLevelGoalAmount( List<SSIContestLevel> levels )
  {
    Double highestLevelGoalAmt = null;
    if ( levels != null )
    {
      SSIContestLevel highestLevel = levels.get( 0 );
      for ( SSIContestLevel level : levels )
      {
        if ( highestLevel.getSequenceNumber().intValue() < level.getSequenceNumber().intValue() )
        {
          highestLevel = level;
        }
      }
      highestLevelGoalAmt = highestLevel.getGoalAmount();
    }
    return highestLevelGoalAmt;
  }

  public static String getActivityDescription( SSIContestPaxClaim paxClaim, SSIContestParticipant contestParticipant, SSIContest contest )
  {
    String activityDescription = null;
    if ( contest.getContestType().isDoThisGetThat() )
    {
      String fieldValue = null;
      for ( SSIContestPaxClaimField paxClaimField : paxClaim.getPaxClaimFields() )
      {
        if ( paxClaimField.getClaimField().getFormElement().getI18nLabel().equalsIgnoreCase( SSIContestUtil.CLAIM_FIELD_AMOUNT )
            || paxClaimField.getClaimField().getFormElement().getI18nLabel().equalsIgnoreCase( SSIContestUtil.CLAIM_FIELD_QUANTITY ) )
        {
          fieldValue = paxClaimField.getFieldValue();
          break;
        }
      }
      if ( fieldValue != null )
      {
        String[] activityAmts = fieldValue.split( ",", -1 );
        List<String> activities = new ArrayList<String>();
        int i = 0;
        for ( SSIContestActivity activity : contest.getContestActivities() )
        {
          if ( !StringUtil.isNullOrEmpty( activityAmts[i] ) )
          {
            activities.add( activity.getDescription() );
          }
          i++;
        }
        activityDescription = StringUtils.join( activities, "," );
      }
    }
    else
    {
      activityDescription = contestParticipant == null ? contest.getActivityDescription() : contestParticipant.getActivityDescription();
    }
    return activityDescription;
  }

  public static SSIAdminContestActions canAddSSIAdminInEMailNotificxations( Long contestID )
  {
    SSIAdminContestActions adminContestActions = getSSIContestService().getAdminActionByEditCreator( contestID );
    if ( adminContestActions != null )
    {
      return adminContestActions;
    }
    else
    {
      return null;
    }
  }

  public static String getCm3damBaseUrl()
  {
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrl = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    return siteUrl + "-cm/cm3dam" + '/';
  }

  public static String removeComma( String inputString )
  {
    return inputString.replaceAll( "[$,]", "" );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private static SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

}

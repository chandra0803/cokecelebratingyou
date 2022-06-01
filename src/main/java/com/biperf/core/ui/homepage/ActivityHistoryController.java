
package com.biperf.core.ui.homepage;

import static com.biperf.core.utils.Environment.ENV_DEV;
import static com.biperf.core.utils.Environment.getEnvironment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.tiles.ComponentContext;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.diyquiz.DIYQuizService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.profile.ActivityHistoryForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.util.StringUtils;

public class ActivityHistoryController extends BaseController
{

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && !ENV_DEV.equalsIgnoreCase( getEnvironment() ) )
    {
      request.setAttribute( "isNewSAEnabled", true );
    }

    String navselected = (String)tileContext.getAttribute( "navselected" );
    request.getSession().setAttribute( "selectedTabId", navselected );

    ActivityHistoryForm activityHistoryForm = (ActivityHistoryForm)request.getAttribute( ActivityHistoryForm.FORM_NAME );

    // this is required while return from 'Detail pages'
    String promotionId = null;
    Date startDate = null;
    Date endDate = null;
    String mode = null;

    String fromPaginationValue = request.getParameter( "d-8131178-p" );

    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( clientState != null && clientState.length() > 0 )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        mode = (String)clientStateMap.get( "mode" );
        startDate = getDateParameter( request, clientStateMap, "startDate" );
        if ( startDate != null )
        {
          startDate = DateUtils.toStartDate( startDate );
        }
        endDate = getDateParameter( request, clientStateMap, "endDate" );
        if ( endDate != null )
        {
          endDate = DateUtils.toEndDate( endDate );
        }
        try
        {
          promotionId = (String)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          promotionId = (String)clientStateMap.get( "promotionId" );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( request.getParameter( "promotionId" ) != null )
    {
      promotionId = request.getParameter( "promotionId" );
    }
    if ( promotionId != null )
    {
      activityHistoryForm.setPromotionId( promotionId );
    }
    if ( startDate != null )
    {
      activityHistoryForm.setStartDate( DateUtils.toDisplayString( startDate ) );
    }
    if ( endDate != null )
    {
      activityHistoryForm.setEndDate( DateUtils.toDisplayString( endDate ) );
    }

    // 1. Set the default dates if dates are not specified.
    if ( activityHistoryForm.getStartDate() == null || activityHistoryForm.getStartDate().trim().equals( "" ) )
    {
      activityHistoryForm.setStartDate( DateUtils.toDisplayString( getDefaultStartDate() ) );
    }

    if ( activityHistoryForm.getEndDate() == null || activityHistoryForm.getEndDate().trim().equals( "" ) )
    {
      activityHistoryForm.setEndDate( DateUtils.toDisplayString( getDefaultEndDate() ) );
    }

    // 2. get All eligiblePromos - to display in search

    List<PromotionMenuBean> eligiblePromoList = null;
    List<PromotionMenuBean> promotionsList = new ArrayList<PromotionMenuBean>();
    if ( UserManager.getUser().isParticipant() )
    {
      eligiblePromoList = getEligiblePromotions( request );

      for ( PromotionMenuBean promotionMenuBean : eligiblePromoList )
      {
        Promotion promotion = promotionMenuBean.getPromotion();
        if ( !promotion.isEngagementPromotion() && !promotion.isSurveyPromotion() && !promotion.isGoalQuestPromotion() && !promotion.isChallengePointPromotion() && !promotion.isThrowdownPromotion()
            && !promotion.isBadgePromotion() && !promotion.isSSIPromotion() )
        {
          if ( promotion.isDIYQuizPromotion() )
          {
            // For DIY - quiz name should be displayed instead of the promotion name
            // Also the promotion id property is a combination of promotion id and quiz id
            List<DIYQuiz> diyQuizzes = getDIYQuizService().getEligibleQuizzesForParticipantByPromotion( promotion.getId(), UserManager.getUser().getUserId() );
            for ( DIYQuiz diyQuiz : diyQuizzes )
            {
              PromotionMenuBean newPromotionMenuBean = new PromotionMenuBean();
              try
              {
                BeanUtils.copyProperties( newPromotionMenuBean, promotionMenuBean );
              }
              catch( Exception e )
              {
              }
              newPromotionMenuBean.setFormPromotionId( String.valueOf( promotionMenuBean.getPromotion().getId() ) + "|" + String.valueOf( diyQuiz.getId() ) );
              newPromotionMenuBean.setFormPromotionName( diyQuiz.getName() );
              promotionsList.add( newPromotionMenuBean );
            }
          }
          else
          {
            promotionMenuBean.setFormPromotionId( String.valueOf( promotionMenuBean.getPromotion().getId() ) );
            promotionMenuBean.setFormPromotionName( promotionMenuBean.getPromotion().getName() );
            promotionsList.add( promotionMenuBean );
          }
        }
      }
      PropertyComparator.sort( promotionsList, new MutableSortDefinition( "formPromotionName", false, true ) );
    }

    request.setAttribute( "promotionList", promotionsList );

    // 3. set the search param - promotion ID. Set the Tile Name as well here.
    String oldtileName = "";
    String tileName = "";
    if ( activityHistoryForm.getPromotionId() == null || activityHistoryForm.getPromotionId().trim().equals( "" ) )
    {
      /*
       * activityHistoryForm.setPromotionId(((PromotionMenuBean)
       * eligiblePromoList.get(0)).getPromotion().getId().toString()); if(((PromotionMenuBean)
       * eligiblePromoList.get(0)).getPromotion().isQuizPromotion()) {
       * request.setAttribute("promotionId", activityHistoryForm.getPromotionId()); }else {
       * request.setAttribute("promotionId", new Long(activityHistoryForm.getPromotionId())); }
       * oldtileName = getOldTileName( (PromotionMenuBean) eligiblePromoList.get(0),
       * activityHistoryForm); tileName = getTileName( (PromotionMenuBean) eligiblePromoList.get(0),
       * activityHistoryForm);
       */

      activityHistoryForm.setPromotionId( "allRecognitions" );
      oldtileName = getOldTilenameByPromotionType( activityHistoryForm );
      tileName = getTilenameByPromotionType( activityHistoryForm );
      request.setAttribute( "promotionId", activityHistoryForm.getPromotionId() );
      request.setAttribute( "activityHistoryTile", "activity.history." + oldtileName );
      request.setAttribute( "profileactivityHistoryTile", "profile.activity.history." + tileName );
    }
    else
    {
      if ( activityHistoryForm.getPromotionId().startsWith( "all" ) )
      {
        // set the tile name
        oldtileName = getOldTilenameByPromotionType( activityHistoryForm );
        tileName = getTilenameByPromotionType( activityHistoryForm );
        request.setAttribute( "activityHistoryTile", "activity.history." + oldtileName );
        request.setAttribute( "profileactivityHistoryTile", "profile.activity.history." + tileName );
      }
      else
      {
        // get the selected Program by using promotion ID.
        Promotion promotion = (Promotion)getPromotionService().getPromotionById( new Long( activityHistoryForm.getPromotionId() ) );
        PromotionMenuBean promotionMenuBean = new PromotionMenuBean();
        promotionMenuBean.setPromotion( promotion );
        if ( promotion.isQuizPromotion() )
        {
          request.setAttribute( "promotionId", activityHistoryForm.getPromotionId() );
        }
        else
        {
          request.setAttribute( "promotionId", new Long( activityHistoryForm.getPromotionId() ) );
        }
        oldtileName = getOldTileName( promotionMenuBean, activityHistoryForm );
        tileName = getTileName( promotionMenuBean, activityHistoryForm );
        request.setAttribute( "activityHistoryTile", "activity.history." + oldtileName );
        request.setAttribute( "profileactivityHistoryTile", "profile.activity.history." + tileName );
        request.setAttribute( "isShowGraph", Boolean.TRUE );
      }
    }
    // Set all extra required params
    request.setAttribute( "mode", activityHistoryForm.getMode() );
    request.setAttribute( "promotionTypeCode", activityHistoryForm.getPromotionTypeCode() );
    if ( activityHistoryForm.getPromotionTypeCode() != null && activityHistoryForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) )
    {
      request.setAttribute( "startDate", (Date)DateUtils.toStartDate( activityHistoryForm.getStartDate() ) );
      request.setAttribute( "endDate", (Date)DateUtils.toStartDate( activityHistoryForm.getEndDate() ) );

    }
    else
    {
      request.setAttribute( "startDate", activityHistoryForm.getStartDate() );
      request.setAttribute( "endDate", activityHistoryForm.getEndDate() );
    }
    // check for modules installed
    checkAndSetModulesInstalled( request );

    String subNavSelected = (String)tileContext.getAttribute( "subNavSelected" );
    request.setAttribute( "subNavSelected", subNavSelected );

    if ( StringUtils.isEmpty( fromPaginationValue ) )
    {
      request.setAttribute( "showTabView", "true" );
    }
    else
    {
      request.setAttribute( "showTabView", "false" );
    }

    String tabClicked = request.getParameter( "tabClicked" );
    if ( StringUtils.isEmpty( tabClicked ) )
    {
      request.setAttribute( "tabClicked", "both" );
    }
    else
    {
      if ( tabClicked.equalsIgnoreCase( "0" ) )
      {
        request.setAttribute( "tabClicked", "sent" );
      }
      else
      {
        request.setAttribute( "tabClicked", "received" );
      }
    }

  }

  private void checkAndSetModulesInstalled( HttpServletRequest request )
  {
    SystemVariableService systemVariableService = (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
    if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_PRODUCTCLAIMS ).getBooleanVal() )
    {
      request.setAttribute( "productClaimInstalled", "true" );
    }
    if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_QUIZZES ).getBooleanVal() )
    {
      request.setAttribute( "quizzesInstalled", "true" );
    }
    if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_RECOGNITION ).getBooleanVal() )
    {
      request.setAttribute( "recognitionInstalled", "true" );
    }
    if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_NOMINATIONS ).getBooleanVal() )
    {
      request.setAttribute( "nominationsInstalled", "true" );
    }
  }

  private String getOldTilenameByPromotionType( ActivityHistoryForm activityHistoryForm )
  {
    String tileName = null;
    if ( activityHistoryForm.getPromotionId().equals( "allRecognitions" ) )
    {
      activityHistoryForm.setPromotionTypeCode( PromotionType.RECOGNITION );
      if ( activityHistoryForm.getMode() == null || activityHistoryForm.getMode().trim().equals( "" ) )
      {
        activityHistoryForm.setMode( "received" );
      }
      // get tile Name
      if ( activityHistoryForm.getMode().equals( "sent" ) )
      {
        tileName = "recognition.sent";
      }
      else
      {
        tileName = "recognition.received";
      }
    }
    else
    {
      if ( activityHistoryForm.getPromotionId().equals( "allNominations" ) )
      {
        activityHistoryForm.setPromotionTypeCode( PromotionType.NOMINATION );
        if ( activityHistoryForm.getMode() == null || activityHistoryForm.getMode().trim().equals( "" ) )
        {
          activityHistoryForm.setMode( "sent" );
        }
        // get tile Name
        if ( activityHistoryForm.getMode().equals( "sent" ) )
        {
          tileName = "nomination.sent";
        }
        else
        {
          tileName = "nomination.received";
        }
      }
      else
      {
        if ( activityHistoryForm.getPromotionId().equals( "allProductClaims" ) )
        {
          activityHistoryForm.setPromotionTypeCode( PromotionType.PRODUCT_CLAIM );
          if ( activityHistoryForm.getMode() == null || activityHistoryForm.getMode().trim().equals( "" ) )
          {
            activityHistoryForm.setMode( "sent" );
          }
          // get tile Name
          if ( activityHistoryForm.getMode().equals( "sent" ) )
          {
            tileName = "product.claim.open";
          }
          else
          {
            tileName = "product.claim.closed";
          }

        }
        else
        {
          if ( activityHistoryForm.getPromotionId().equals( "allQuizzes" ) )
          {
            activityHistoryForm.setPromotionTypeCode( PromotionType.QUIZ );
            activityHistoryForm.setMode( "sent" );// required to enable subNav tab selection
            tileName = "quiz";
          }
        }
      }
    }
    return tileName;
  }

  private String getTilenameByPromotionType( ActivityHistoryForm activityHistoryForm )
  {
    String tileName = null;
    if ( activityHistoryForm.getPromotionId().equals( "allRecognitions" ) )
    {
      activityHistoryForm.setPromotionTypeCode( PromotionType.RECOGNITION );
      tileName = "recognition";
    }
    else
    {
      if ( activityHistoryForm.getPromotionId().equals( "allNominations" ) )
      {
        activityHistoryForm.setPromotionTypeCode( PromotionType.NOMINATION );
        tileName = "nomination";

      }
      else
      {
        if ( activityHistoryForm.getPromotionId().equals( "allProductClaims" ) )
        {
          activityHistoryForm.setPromotionTypeCode( PromotionType.PRODUCT_CLAIM );
          tileName = "product.claim";
        }
        else
        {
          if ( activityHistoryForm.getPromotionId().equals( "allQuizzes" ) )
          {
            activityHistoryForm.setPromotionTypeCode( PromotionType.QUIZ );
            tileName = "quiz";
          }
        }
      }
    }
    return tileName;
  }

  private String getTileName( PromotionMenuBean promotionMenuBean, ActivityHistoryForm activityHistoryForm )
  {
    String tileName = null;
    if ( promotionMenuBean.getPromotion().isRecognitionPromotion() )
    {
      activityHistoryForm.setPromotionTypeCode( PromotionType.RECOGNITION );
      tileName = "recognition";
    }
    else
    {
      if ( promotionMenuBean.getPromotion().isNominationPromotion() )
      {
        activityHistoryForm.setPromotionTypeCode( PromotionType.NOMINATION );
        // set the default Mode
        tileName = "nomination";
      }
      else
      {
        if ( promotionMenuBean.getPromotion().isProductClaimPromotion() )
        {
          activityHistoryForm.setPromotionTypeCode( PromotionType.PRODUCT_CLAIM );
          tileName = "product.claim";
        }
        else
        {
          if ( promotionMenuBean.getPromotion().isQuizPromotion() )
          {
            activityHistoryForm.setPromotionTypeCode( PromotionType.QUIZ );
            tileName = "quiz";
          }
        }
      }
    }
    return tileName;
  }

  private String getOldTileName( PromotionMenuBean promotionMenuBean, ActivityHistoryForm activityHistoryForm )
  {
    String tileName = null;
    if ( promotionMenuBean.getPromotion().isRecognitionPromotion() )
    {
      activityHistoryForm.setPromotionTypeCode( PromotionType.RECOGNITION );
      // set the default Mode
      if ( activityHistoryForm.getMode() == null || activityHistoryForm.getMode().trim().equals( "" ) )
      {
        activityHistoryForm.setMode( "sent" );
      }
      // get tile Name
      if ( activityHistoryForm.getMode().equals( "sent" ) )
      {
        tileName = "recognition.sent";
      }
      else
      {
        tileName = "recognition.received";
      }
    }
    else
    {
      if ( promotionMenuBean.getPromotion().isNominationPromotion() )
      {
        activityHistoryForm.setPromotionTypeCode( PromotionType.NOMINATION );
        // set the default Mode
        if ( activityHistoryForm.getMode() == null || activityHistoryForm.getMode().trim().equals( "" ) )
        {
          activityHistoryForm.setMode( "sent" );
        }
        // get tile Name
        if ( activityHistoryForm.getMode().equals( "sent" ) )
        {
          tileName = "nomination.sent";
        }
        else
        {
          tileName = "nomination.received";
        }
      }
      else
      {
        if ( promotionMenuBean.getPromotion().isProductClaimPromotion() )
        {
          activityHistoryForm.setPromotionTypeCode( PromotionType.PRODUCT_CLAIM );
          if ( activityHistoryForm.getMode() == null || activityHistoryForm.getMode().trim().equals( "" ) )
          {
            activityHistoryForm.setMode( "sent" );
          }
          // get tile Name
          if ( activityHistoryForm.getMode().equals( "sent" ) )
          {
            tileName = "product.claim.open";
          }
          else
          {
            tileName = "product.claim.closed";
          }
        }
        else
        {
          if ( promotionMenuBean.getPromotion().isQuizPromotion() )
          {
            activityHistoryForm.setPromotionTypeCode( PromotionType.QUIZ );
            activityHistoryForm.setMode( "sent" );// required to enable subNav tab selection
            tileName = "quiz";
          }
        }
      }
    }
    return tileName;
  }

  private List getFilteredEligiblePromotions( HttpServletRequest request )
  {
    // TODO for 1.1, replace this with getEligiblePromotions( request ), but need to make changes in
    // maincontentService to do some cleanup for the activityFlag
    List eligiblePromotions = (List)getMainContentService().buildEligiblePromoList( UserManager.getUser(), false );
    return filterEligiblePromotions( eligiblePromotions );
  }

  private List filterEligiblePromotions( List eligiblePromotions )
  {
    PromotionMenuBean promotionMenuBean = null;
    List<PromotionMenuBean> promotionsList = new ArrayList<PromotionMenuBean>();
    for ( int i = 0; i < eligiblePromotions.size(); i++ )
    {
      promotionMenuBean = (PromotionMenuBean)eligiblePromotions.get( i );
      if ( promotionMenuBean.getPromotion().isDIYQuizPromotion() )
      {
        // For DIY - quiz name should be displayed instead of the promotion name
        // Also the promotion id property is a combination of promotion id and quiz id
        List<DIYQuiz> diyQuizzes = getDIYQuizService().getEligibleQuizzesForParticipantByPromotion( promotionMenuBean.getPromotion().getId(), UserManager.getUser().getUserId() );
        for ( DIYQuiz diyQuiz : diyQuizzes )
        {
          PromotionMenuBean newPromotionMenuBean = new PromotionMenuBean();
          try
          {
            BeanUtils.copyProperties( newPromotionMenuBean, promotionMenuBean );
          }
          catch( Exception e )
          {
          }
          newPromotionMenuBean.setFormPromotionId( String.valueOf( promotionMenuBean.getPromotion().getId() ) + "|" + String.valueOf( diyQuiz.getId() ) );
          newPromotionMenuBean.setFormPromotionName( diyQuiz.getName() );
          promotionsList.add( newPromotionMenuBean );
        }
      }
      else if ( !promotionMenuBean.getPromotion().isSurveyPromotion() && !promotionMenuBean.getPromotion().isGoalQuestPromotion() && !promotionMenuBean.getPromotion().isChallengePointPromotion() )
      {
        promotionMenuBean.setFormPromotionId( String.valueOf( promotionMenuBean.getPromotion().getId() ) );
        promotionMenuBean.setFormPromotionName( promotionMenuBean.getPromotion().getName() );
        promotionsList.add( promotionMenuBean );
      }
    }
    return promotionsList;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------
  private Date getDateParameter( HttpServletRequest request, Map clientStateMap, String parameterName )
  {
    Date dateValue = null;
    String dateString = RequestUtils.getOptionalParamString( request, parameterName );
    if ( dateString == null )
    {
      if ( clientStateMap != null )
      {
        try
        {
          dateValue = (Date)clientStateMap.get( parameterName );
        }
        catch( ClassCastException cce )
        {
          dateString = (String)clientStateMap.get( parameterName );
        }
      }
    }
    if ( dateString != null )
    {
      dateValue = DateUtils.toDate( dateString );
    }
    return dateValue;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  private DIYQuizService getDIYQuizService()
  {
    return (DIYQuizService)BeanLocator.getBean( DIYQuizService.BEAN_NAME );
  }

  /**
   * Returns the default start date.
   * 
   * @return the default start date.
   */
  private Date getDefaultStartDate()
  {
    String timeZoneID = getUserService().getUserTimeZone( UserManager.getUserId() );
    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    Date launchDate = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_LAUNCH_DATE ).getDateVal();
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.add( GregorianCalendar.MONTH, -1 );
    Date todayMinusMonth = DateUtils.applyTimeZone( calendar.getTime(), timeZoneID );

    return todayMinusMonth.after( launchDate ) ? DateUtils.toStartDate( todayMinusMonth ) : launchDate;
  }

  private Date getDefaultEndDate()
  {
    String timeZoneID = getUserService().getUserTimeZone( UserManager.getUserId() );
    return DateUtils.applyTimeZone( DateUtils.toEndDate( DateUtils.getCurrentDate() ), timeZoneID );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }
}

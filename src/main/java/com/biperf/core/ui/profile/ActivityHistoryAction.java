
package com.biperf.core.ui.profile;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.claim.hibernate.JournalClaimGroupQueryConstraint;
import com.biperf.core.dao.claim.hibernate.JournalClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.NominationClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.ProductClaimClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.RecognitionClaimQueryConstraint;
import com.biperf.core.dao.journal.hibernate.JournalQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.NominationPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.ProductClaimPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.QuizPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.RecognitionPromotionQueryConstraint;
import com.biperf.core.domain.activity.MerchandiseActivity;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.process.NominationAutoNotificationProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.diyquiz.DIYQuizService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.journal.impl.JournalAssociationRequest;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PublicRecognitionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.reports.ReportsService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.ActivityHistoryRecognitionListExportBean;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.claim.ProductClaimValueObject;
import com.biperf.core.ui.claim.QuizHistoryValueObject;
import com.biperf.core.ui.claim.RecognitionDetailBean;
import com.biperf.core.ui.claim.RecognitionHistoryForm;
import com.biperf.core.ui.claim.RecognitionHistoryValueObject;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ParticipantQuizClaimHistory;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ActivityHistoryAction extends BaseDispatchAction
{
  /**
   * If <code>mode</code> is SENT_MODE, then the Nomination/Recognition
   * History page shows nominations submitted/recognitions sent.
   */
  public static final String SENT_MODE = "sent";

  /**
   * If <code>mode</code> is RECEIVED_MODE, then the Nomination/Recognition
   * History page shows nominations won/recognitions received.
   */
  public static final String RECEIVED_MODE = "received";

  /**
   * Dispatcher.  Default to home page display.  Too much work to append 'method=display'
   * to all the paths that lead to the home page.  
   */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return this.display( mapping, form, request, response );
    }
  }

  /**
   * Method to list the active badges in the system.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActivityHistoryForm activityHistoryForm = (ActivityHistoryForm)request.getAttribute( ActivityHistoryForm.FORM_NAME );

    // this is required while return from 'Detail pages'
    String promotionId = null;
    Date startDate = null;
    Date endDate = null;
    String forward = "activityHistoryRecognition";

    String fromPaginationValue = request.getParameter( "d-7243690-p" );
    promotionId = request.getParameter( "promotionId" );

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

    if ( !StringUtil.isEmpty( promotionId ) )
    {
      // For DIY promotion promotion id is a combination of promotion and DIY quiz id's
      String[] inputPromoQuizId = promotionId.split( "\\|" );
      promotionId = inputPromoQuizId[0];
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
    List eligiblePromoList = null;
    if ( UserManager.getUser().isParticipant() )
    {
      eligiblePromoList = getFilteredEligiblePromotions( request );
      PropertyComparator.sort( eligiblePromoList, new MutableSortDefinition( "formPromotionName", false, true ) );
    }

    request.setAttribute( "promotionList", eligiblePromoList );

    if ( eligiblePromoList != null && eligiblePromoList.size() > 0 )
    {
      // 3. set the search param - promotion ID. Set the Tile Name as well here.
      String oldtileName = "";
      String tileName = "";
      if ( activityHistoryForm.getPromotionId() == null || activityHistoryForm.getPromotionId().trim().equals( "" ) )
      {
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
          if ( promotion.isQuizPromotion() || promotion.isDIYQuizPromotion() )
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
        if ( tileName.equalsIgnoreCase( "recognition" ) )
        {
          forward = "activityHistoryRecognition";
        }
        else if ( tileName.equalsIgnoreCase( "nomination" ) )
        {
          forward = "activityHistoryNomination";
        }
        else if ( tileName.equalsIgnoreCase( "product.claim" ) )
        {
          forward = "activityHistoryProductClaim";
        }
        else if ( tileName.equalsIgnoreCase( "quiz" ) )
        {
          forward = "activityHistoryQuiz";
        }
        else
        {
          forward = "activityHistory";
        }

        request.setAttribute( "tabClicked", "both" );
      }
      else
      {
        // TODO: The below code assumes for product claim, product claim closed is going to be the
        // default tab. If it is going to change modify the below code accordingly
        if ( tabClicked.equalsIgnoreCase( "1" ) )
        {
          if ( tileName.equalsIgnoreCase( "recognition" ) )
          {
            forward = "recognitionSent";
            request.setAttribute( "tabClicked", "sent" );
          }
          else if ( tileName.equalsIgnoreCase( "nomination" ) )
          {
            forward = "nominationReceived";
            request.setAttribute( "tabClicked", "received" );
          }
          else if ( tileName.equalsIgnoreCase( "product.claim" ) )
          {
            forward = "productClaimClosed";
            request.setAttribute( "tabClicked", "closed" );
          }
        }
        else
        {
          if ( tileName.equalsIgnoreCase( "recognition" ) )
          {
            forward = "recognitionReceived";
            request.setAttribute( "tabClicked", "received" );
          }
          else if ( tileName.equalsIgnoreCase( "nomination" ) )
          {
            forward = "nominationSent";
            request.setAttribute( "tabClicked", "sent" );
          }
          else if ( tileName.equalsIgnoreCase( "product.claim" ) )
          {
            forward = "productClaimOpen";
            request.setAttribute( "tabClicked", "open" );
          }
          else if ( tileName.equalsIgnoreCase( "quiz" ) )
          {
            forward = "quiz";
          }
        }
      }

      if ( tileName.equalsIgnoreCase( "recognition" ) || tileName.equalsIgnoreCase( "nomination" ) )
      {
        buildRecNominationValueObjects( request );
      }
      else if ( tileName.equalsIgnoreCase( "quiz" ) )
      {
        buildQuizValueObject( request );
      }
      else if ( tileName.equalsIgnoreCase( "product.claim" ) )
      {
        buildProductClaimValueObject( request, activityHistoryForm );
      }
    }

    return mapping.findForward( forward );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward printAsPdf( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Long claimId = null;
    Long journalId = null;
    List<RecognitionDetailBean> recognitionDetails = new ArrayList<RecognitionDetailBean>();
    List<RecognitionHistoryValueObject> sentValueObjects = null;
    List<RecognitionHistoryValueObject> receiverValueObjects = null;
    ValueObjectBuilder sentbuilder = null;
    ValueObjectBuilder receivedbuilder = null;

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String recog = RequestUtils.getRequiredParamString( request, "recog" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      Long submitterId = null;
      Long promotionId = null;
      Long proxyUserId = null;
      String startDate = "";
      String promotionTypeCode = "";
      String endDateMaxTime = "";

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      if ( clientStateMap.get( "submitterId" ) instanceof Long )
      {
        submitterId = (Long)clientStateMap.get( "submitterId" );
      }
      if ( clientStateMap.get( "promotionId" ) instanceof Long )
      {
        promotionId = (Long)clientStateMap.get( "promotionId" );
      }
      if ( clientStateMap.get( "proxyUserId" ) instanceof Long )
      {
        proxyUserId = (Long)clientStateMap.get( "proxyUserId" );
      }
      if ( clientStateMap.get( "startDate" ) instanceof String )
      {
        startDate = (String)clientStateMap.get( "startDate" );
      }
      if ( clientStateMap.get( "promotionTypeCode" ) instanceof String )
      {
        promotionTypeCode = (String)clientStateMap.get( "promotionTypeCode" );
      }
      if ( clientStateMap.get( "endDate" ) instanceof String )
      {
        endDateMaxTime = (String)clientStateMap.get( "endDate" );
      }

      if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
      {
        receivedbuilder = new NominationReceiveBuilder( submitterId, promotionId, DateUtils.toDate( startDate ), DateUtils.toDate( endDateMaxTime ) );
        sentbuilder = new NominationSentBuilder( submitterId, proxyUserId, promotionId, DateUtils.toDate( startDate ), DateUtils.toDate( endDateMaxTime ) );
      }
      else if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
      {
        receivedbuilder = new RecognitionReceiveBuilder( submitterId, promotionId, DateUtils.toDate( startDate ), DateUtils.toDate( endDateMaxTime ) );
        sentbuilder = new RecognitionSentBuilder( submitterId, proxyUserId, promotionId, DateUtils.toDate( startDate ), DateUtils.toDate( endDateMaxTime ) );
      }
      if ( receivedbuilder != null )
      {
        receiverValueObjects = receivedbuilder.buildValueObjects();
      }
      if ( sentbuilder != null )
      {
        sentValueObjects = sentbuilder.buildValueObjects();
      }
      if ( "sent".equals( recog ) && null != sentValueObjects )
      {
        Collections.sort( sentValueObjects, new Comparator<RecognitionHistoryValueObject>()
        {
          public int compare( RecognitionHistoryValueObject s1, RecognitionHistoryValueObject s2 )
          {
            return s1.getSubmissionDate().compareTo( s2.getSubmissionDate() );
          }
        } );

        for ( RecognitionHistoryValueObject test : sentValueObjects )
        {
          claimId = test.getClaim().getId();
          if ( null != claimId )
          {
            RecognitionDetailBean bean = getRecognitionDetailBean( request, claimId );
            if ( null != bean )
            {
              recognitionDetails.add( bean );
            }
          }
        }
      }
      if ( "received".equals( recog ) && null != receiverValueObjects )
      {
        Collections.sort( receiverValueObjects, new Comparator<RecognitionHistoryValueObject>()
        {
          public int compare( RecognitionHistoryValueObject s1, RecognitionHistoryValueObject s2 )
          {
            return s1.getSubmissionDate().compareTo( s2.getSubmissionDate() );
          }
        } );
        for ( RecognitionHistoryValueObject test : receiverValueObjects )
        {
          if ( test.getClaim() != null )
          {
            claimId = test.getClaim().getId();
            if ( null != claimId )
            {
              RecognitionDetailBean bean = getRecognitionDetailBean( request, claimId );
              if ( null != bean )
              {
                recognitionDetails.add( bean );
              }
            }
          }
          if ( test.isSweepstakes() )
          {
            journalId = test.getJournalId();
            if ( null != journalId )
            {
              RecognitionDetailBean beanForSweepAward = getRecognitionDetailBeanForSweepAward( request, journalId );
              if ( null != beanForSweepAward )
              {
                recognitionDetails.add( beanForSweepAward );
              }
            }
          }
          if ( test.getClaim() == null && test.getIsBadgePromotion() )
          {
            // Added logic to set badge informations in detail bean to display badge info in pdf
            RecognitionDetailBean beanForParticipantBadge = getRecognitionDetailBeanForParticipantBadge( request, test );
            if ( null != beanForParticipantBadge )
            {
              recognitionDetails.add( beanForParticipantBadge );
            }
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    ActivityHistoryRecognitionListExportBean exportBean = new ActivityHistoryRecognitionListExportBean();
    exportBean.setExportPdfListRecognition( new ArrayList<AbstractRecognitionClaim>() );
    exportBean.extractAsPdf( recognitionDetails, response, "" );

    return null;
  }

  protected RecognitionDetailBean getRecognitionDetailBeanForParticipantBadge( HttpServletRequest request, RecognitionHistoryValueObject valueObject )
  {
    RecognitionDetailBean bean = null;
    String userLang = UserManager.getUserLanguage();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant pax = getParticipantService().getParticipantByIdWithAssociations( valueObject.getRecipient().getId(), associationRequestCollection );
    bean = new RecognitionDetailBean( userLang, request.getContextPath(), valueObject, pax );
    return bean;
  }

  protected RecognitionDetailBean getRecognitionDetailBeanForSweepAward( HttpServletRequest request, Long journalId )
  {
    RecognitionDetailBean bean = null;

    String userLang = UserManager.getUserLanguage();
    Journal journal = getJournalService().getJournalById( journalId );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant pax = getParticipantService().getParticipantByIdWithAssociations( journal.getParticipant().getId(), associationRequestCollection );

    bean = new RecognitionDetailBean( userLang, request.getContextPath(), journal, pax );
    bean.getRecognition().setSweepAward( true );

    return bean;
  }

  protected RecognitionDetailBean getRecognitionDetailBean( HttpServletRequest request, Long claimId )
  {
    RecognitionDetailBean bean = null;
    AbstractRecognitionClaim abstractRecognitionClaim = getClaim( claimId, RequestUtils.getBaseURI( request ) );

    // could also use UserService#getPreferredLanguageFor to determine the user's language; the rest
    // of
    // translation uses it
    String userLang = UserManager.getUserLanguage();
    Long userId;
    String participantId = request.getParameter( "participantId" );
    if ( participantId != null )
    {
      userId = Long.parseLong( participantId );
    }
    else
    {
      userId = UserManager.getUserId();
    }

    if ( ( (AbstractRecognitionPromotion)abstractRecognitionClaim.getPromotion() ).isAllowPublicRecognition() )
    {
      List<PublicRecognitionFormattedValueBean> publicRecognitionClaims = getPublicRecognitionService().getPublicRecognitionClaimsByClaimId( claimId, userId );
      if ( !publicRecognitionClaims.isEmpty() )
      {
        PublicRecognitionFormattedValueBean publicRecognitionClaim = publicRecognitionClaims.iterator().next();
        publicRecognitionClaim.setAbstractRecognitionClaim( abstractRecognitionClaim );
        bean = new RecognitionDetailBean( userLang, request.getContextPath(), publicRecognitionClaim, true, true );
        bean.mergeTeamClaims( publicRecognitionClaims );
      }
    }
    else
    {
      bean = new RecognitionDetailBean( userLang, request.getContextPath(), abstractRecognitionClaim, true, true );
    }

    return bean;
  }

  private AbstractRecognitionClaim getClaim( Long claimId, String baseContextPath )
  {
    AbstractRecognitionClaim claim = getClaimService().getRecognitionDetail( claimId, true );
    getClaimElementDomainObjects( claim );

    if ( claim instanceof RecognitionClaim )
    {
      String purlURL = getPurlService().createPurlRecipientUrlFromClaimId( claim.getId() );
      if ( purlURL != null )
      {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put( "purlUrl", purlURL );
        ( (RecognitionClaim)claim ).setPurlUrl( ClientStateUtils.generateEncodedLink( baseContextPath, "/purl/purlRecipient.do?method=showPurlInPublicRecognition", paramMap ) );
      }
    }
    return claim;
  }

  private AbstractRecognitionClaim getClaimElementDomainObjects( AbstractRecognitionClaim claim )
  {
    for ( Iterator iter = claim.getClaimElements().iterator(); iter.hasNext(); )
    {
      ClaimElement claimElement = (ClaimElement)iter.next();
      if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isMultiSelectField() || claimElement.getClaimFormStepElement().getClaimFormElementType().isSelectField() )
      {
        List pickListItems = new ArrayList();
        // convert the comma delimited list of selected pickListItems to a list of strings
        Iterator pickListCodes = ArrayUtil.convertDelimitedStringToList( claimElement.getValue(), "," ).iterator();
        while ( pickListCodes.hasNext() )
        {
          String code = (String)pickListCodes.next();
          pickListItems.add( DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), code ) );
        }
        claimElement.setPickListItems( pickListItems );
      }
    }
    return claim;
  }

  protected static PublicRecognitionService getPublicRecognitionService()
  {
    return (PublicRecognitionService)getService( PublicRecognitionService.BEAN_NAME );
  }

  protected static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
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
          if ( promotionMenuBean.getPromotion().isQuizPromotion() || promotionMenuBean.getPromotion().isDIYQuizPromotion() )
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
          if ( promotionMenuBean.getPromotion().isQuizPromotion() || promotionMenuBean.getPromotion().isDIYQuizPromotion() )
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

  private List getFilteredEligiblePromotions( HttpServletRequest request )
  {
    // TODO for 1.1, replace this with getEligiblePromotions( request ), but need to make changes in
    // maincontentService to do some cleanup for the activityFlag
    List eligiblePromotions = (List)getMainContentService().buildEligiblePromoList( UserManager.getUser(), false );
    return filterEligiblePromotions( eligiblePromotions );
  }

  private List<PromotionMenuBean> filterEligiblePromotions( List<PromotionMenuBean> eligiblePromotions )
  {
    PromotionMenuBean promotionMenuBean = null;
    List<PromotionMenuBean> promotionsList = new ArrayList<PromotionMenuBean>();
    for ( int i = 0; i < eligiblePromotions.size(); i++ )
    {
      promotionMenuBean = (PromotionMenuBean)eligiblePromotions.get( i );
      if ( !promotionMenuBean.getPromotion().isSurveyPromotion() && !promotionMenuBean.getPromotion().isGoalQuestPromotion() && !promotionMenuBean.getPromotion().isChallengePointPromotion()
          && !promotionMenuBean.getPromotion().isThrowdownPromotion() )
      {
        // For DIY - quiz name should be displayed instead of the promotion name
        // Also the promotion id property is a combination of promotion id and quiz id
        if ( promotionMenuBean.getPromotion().isDIYQuizPromotion() )
        {
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
        else
        {
          promotionMenuBean.setFormPromotionId( String.valueOf( promotionMenuBean.getPromotion().getId() ) );
          promotionMenuBean.setFormPromotionName( promotionMenuBean.getPromotion().getName() );
          promotionsList.add( promotionMenuBean );
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

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
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

  public void buildQuizValueObject( HttpServletRequest request )
  {
    Long promotionId = null;
    Long diyQuizId = null;
    Date startDate = null;
    Date endDate = null;
    List promotionList = null;
    String timeZoneID = "";
    boolean isAccountStatusOnHold = false;

    if ( request.getAttribute( "promotionId" ) != null && !request.getAttribute( "promotionId" ).equals( "" ) )
    {
      promotionId = new Long( (String)request.getAttribute( "promotionId" ) );
    }

    if ( !StringUtils.isEmpty( request.getParameter( "promotionId" ) ) )
    {
      // For DIY promotion promotion id is a combination of promotion and DIY quiz id's
      String requestPromotionId = request.getParameter( "promotionId" );
      String[] inputPromoQuizId = requestPromotionId.split( "\\|" );
      if ( inputPromoQuizId.length > 1 )
      {
        diyQuizId = Long.valueOf( inputPromoQuizId[1] );
      }
    }

    startDate = (Date)request.getAttribute( "startDate" );
    endDate = DateUtils.toEndDate( (Date)request.getAttribute( "endDate" ) ); // get the latest time
                                                                              // on the specified
                                                                              // date

    QuizPromotionQueryConstraint quizPromotionQueryConstraint = new QuizPromotionQueryConstraint();
    promotionList = getPromotionService().getPromotionList( quizPromotionQueryConstraint );

    QuizClaimQueryConstraint quizClaimQueryConstraint = new QuizClaimQueryConstraint();
    quizClaimQueryConstraint.setIncludedPromotionIds( promotionId != null ? new Long[] { promotionId } : null );
    if ( diyQuizId != null )
    {
      quizClaimQueryConstraint.setDiyQuizId( diyQuizId );
    }
    quizClaimQueryConstraint.setStartDate( startDate );
    quizClaimQueryConstraint.setEndDate( endDate );

    if ( request.getAttribute( "submitterId" ) != null )
    {
      quizClaimQueryConstraint.setSubmitterId( (Long)request.getAttribute( "submitterId" ) );
      timeZoneID = getUserService().getUserTimeZone( (Long)request.getAttribute( "submitterId" ) );
    }
    else
    {
      quizClaimQueryConstraint.setSubmitterId( UserManager.getUser().getUserId() );
      timeZoneID = UserManager.getUser().getTimeZoneId();
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.QUIZ_RESPONSES ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    Map quizClaimGroupings = getClaimService().getParticipantQuizClaimHistoryByPromotionMap( quizClaimQueryConstraint, associationRequestCollection );

    Set keySet = quizClaimGroupings.keySet();
    ArrayList quizValueAttemptedList = new ArrayList();

    for ( Iterator quizClaimsIter = keySet.iterator(); quizClaimsIter.hasNext(); )
    {
      ParticipantQuizClaimHistory participantQuizClaimHistory = (ParticipantQuizClaimHistory)quizClaimGroupings.get( quizClaimsIter.next() );

      ArrayList quizAttemptedList = (ArrayList)participantQuizClaimHistory.getQuizClaimsBySubmissionDate();

      boolean resumeQuiz = false;

      for ( Iterator quizAttemptedListIter = quizAttemptedList.iterator(); quizAttemptedListIter.hasNext(); )
      {
        QuizClaim quizClaim = (QuizClaim)quizAttemptedListIter.next();

        if ( participantQuizClaimHistory.getMostRecentClaim().equals( quizClaim ) && !quizClaim.isQuizComplete() && DateUtils
            .isDateBetween( new Date(), participantQuizClaimHistory.getPromotion().getSubmissionStartDate(), participantQuizClaimHistory.getPromotion().getSubmissionEndDate(), timeZoneID ) )
        {
          resumeQuiz = true;
        }
      }

      int count = 0;
      Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
      for ( Iterator quizAttemptedListIter = quizAttemptedList.iterator(); quizAttemptedListIter.hasNext(); )
      {
        count++;

        QuizClaim quizClaim = (QuizClaim)quizAttemptedListIter.next();
        QuizHistoryValueObject quizHistoryValueObject = new QuizHistoryValueObject();
        quizHistoryValueObject.setId( quizClaim.getId() );
        quizHistoryValueObject.setQuizAttempt( count );
        if ( quizClaim.getPromotion().isDIYQuizPromotion() )
        {
          quizHistoryValueObject.setDiyQuizName( quizClaim.getQuiz().getName() );
        }
        quizHistoryValueObject.setPromotionId( quizClaim.getPromotion().getId() );
        quizHistoryValueObject.setPromotionName( quizClaim.getPromotion().getName() );
        quizHistoryValueObject.setQuizComplete( quizClaim.isQuizComplete() );

        // Need to display Award amount if there is one on the journal for this quiz
        quizHistoryValueObject.setAwardQuantity( getAwardForQuiz( quizClaim ) );
        quizHistoryValueObject.setAwardTypeName( PromotionAwardsType.lookup( quizClaim.getPromotion().getAwardType().getCode() ).getName() );

        if ( quizClaim.isQuizComplete() )
        {
          quizHistoryValueObject.setDateCompleted( quizClaim.getSubmissionDate() );
        }

        if ( quizClaim.isQuizComplete() )
        {
          quizHistoryValueObject.setQuizComplete( true );
          if ( quizClaim.getPass() != null && quizClaim.getPass().booleanValue() )
          {
            quizHistoryValueObject.setPassed( true );
          }
          else
          {
            quizHistoryValueObject.setPassed( false );
          }
        }
        else
        {
          quizHistoryValueObject.setQuizComplete( false );
        }

        quizHistoryValueObject.setResumeQuiz( resumeQuiz );

        quizHistoryValueObject.setRetakeQuiz( participantQuizClaimHistory.isRetakeable( timeZoneID ) );

        if ( quizClaim.isQuizComplete() )
        {
          quizValueAttemptedList.add( quizHistoryValueObject );
        }
      }
    }
    isAccountStatusOnHold = getMainContentService().checkShowShopORTravel();
    if ( !isAccountStatusOnHold )
    {
      request.setAttribute( "status", "hold" );
    }
    // Add the non-quiz related Deposits eg. file load, discretionary, etc to the quiz list.
    quizValueAttemptedList.addAll( getNonQuizRelatedDeposits( UserManager.getUser().getUserId(), promotionId ) );
    request.setAttribute( "quizValueAttemptedList", quizValueAttemptedList );
    request.setAttribute( "startDate", DateUtils.toDisplayString( startDate ) );
    request.setAttribute( "endDate", DateUtils.toDisplayString( endDate ) );
    request.setAttribute( "promotionList", promotionList );
    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
  }

  /*
   * @param participantId
   * @return List of 'faked' quiz history value object that are really non-quiz related deposits
   * such as file load, discretionary awards. It was decided to display these items in the Quiz list
   * in order to show the Pax their award
   */
  private List getNonQuizRelatedDeposits( Long participantId, Long promotionId )
  {
    List depositList = new ArrayList();

    // Get any deposits for this promotion not linked to any quiz taking.
    // eg. file load, discretionary
    JournalQueryConstraint journalQueryConstraint = new JournalQueryConstraint();
    journalQueryConstraint.setParticipantId( participantId );
    journalQueryConstraint.setNotResultOfClaim( true );
    journalQueryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );
    journalQueryConstraint.setPromotionId( promotionId ); // Fix 21341

    // need to do the association to read the Promotion obj from Journal obj in jsp
    AssociationRequestCollection journalAssociationRequestCollection = new AssociationRequestCollection();
    journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

    List journalList = getJournalService().getJournalList( journalQueryConstraint, journalAssociationRequestCollection );
    for ( Iterator journalIter = journalList.iterator(); journalIter.hasNext(); )
    {
      Journal journal = (Journal)journalIter.next();
      // only want quiz promotion type deposits
      if ( journal.getPromotion() != null && journal.getPromotion().isQuizPromotion() )
      {
        QuizHistoryValueObject valueObject = new QuizHistoryValueObject();
        valueObject.setNonClaimRelatedDeposits( true );
        valueObject.setPromotionName( journal.getPromotion().getName() );
        valueObject.setDateCompleted( new Timestamp( journal.getTransactionDate().getTime() ) );
        if ( journal.getTransactionAmount() != null )
        {
          valueObject.setJournalType( journal.getJournalType() );
          valueObject.setAwardQuantity( journal.getTransactionAmount().intValue() );
          valueObject.setAwardTypeName( PromotionAwardsType.lookup( journal.getPromotion().getAwardType().getCode() ).getName() );
        }
        // For Fileload, should be false here
        valueObject.setDiscretionary( Journal.DISCRETIONARY.equals( journal.getJournalType() ) );
        valueObject.setSweepstakes( Journal.SWEEPSTAKES.equals( journal.getJournalType() ) );
        if ( JournalTransactionType.REVERSE.equals( journal.getTransactionType().getCode() ) )
        {
          valueObject.setReversalDescription( journal.getTransactionDescription() );
        }
        depositList.add( valueObject );
      }
    }
    return depositList;
  }

  /**
   * @param quizClaim
   * @return the award amount posted on the journal for a specific quiz, maybe zero, if quiz was not
   *         passed/completed etc.
   */
  private int getAwardForQuiz( QuizClaim quizClaim )
  {
    int awardAmount = 0;

    AssociationRequestCollection journalAssociationRequest = new AssociationRequestCollection();
    journalAssociationRequest.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION_DEPROXY ) );
    journalAssociationRequest.add( new JournalAssociationRequest( JournalAssociationRequest.ACTIVITY_JOURNALS ) );

    List journals = getJournalService().getJournalsByClaimIdAndUserId( quizClaim.getId(), quizClaim.getSubmitter().getId(), journalAssociationRequest );

    if ( !quizClaim.isOpen() && journals != null && journals.size() > 0 )
    {
      Journal journal = (Journal)journals.get( 0 );
      awardAmount = journal.getTransactionAmount().intValue();
    }

    return awardAmount;
  }

  public void buildRecNominationValueObjects( HttpServletRequest request )
  {
    RequestWrapper requestWrapper = new RequestWrapper( request );

    // Get parameters.
    RecognitionHistoryForm form = requestWrapper.getRecognitionHistoryForm();

    String promotionTypeCode = "";
    String mode = "";
    Long submitterId = null;
    Long proxyUserId = null;
    Long promotionId = null;
    boolean isAccountStatusOnHold = false;
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
        promotionTypeCode = (String)clientStateMap.get( "promotionTypeCode" );
        mode = (String)clientStateMap.get( "mode" );
        try
        {
          submitterId = (Long)clientStateMap.get( "submitterId" );
        }
        catch( ClassCastException cce )
        {
          submitterId = new Long( (String)clientStateMap.get( "submitterId" ) );
        }
        try
        {
          proxyUserId = (Long)clientStateMap.get( "proxyUserId" );
        }
        catch( ClassCastException cce )
        {
          String temp = (String)clientStateMap.get( "proxyUserId" );
          if ( !StringUtils.isEmpty( temp ) )
          {
            proxyUserId = new Long( temp );
          }
        }
        try
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          if ( ! ( (String)clientStateMap.get( "promotionId" ) ).startsWith( "all" ) )
          {
            promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( submitterId == null )
    {
      submitterId = UserManager.getUser().getUserId();
    }
    if ( StringUtils.isEmpty( promotionTypeCode ) )
    {
      promotionTypeCode = requestWrapper.getPromotionTypeCode();
    }
    if ( StringUtils.isEmpty( mode ) )
    {
      mode = requestWrapper.getMode();
    }
    Date startDate = requestWrapper.getStartDate();
    Date endDate = requestWrapper.getEndDate();

    // Setup the page.
    form.setSubmitterId( submitterId );
    form.setProxyUserId( proxyUserId );
    form.setPromotionTypeCode( promotionTypeCode );
    if ( request.getAttribute( "promotionId" ) != null )
    {
      promotionId = (Long)request.getAttribute( "promotionId" );
    }
    mode = (String)request.getAttribute( "mode" );
    if ( request.getAttribute( "promotionTypeCode" ) != null )
    {
      promotionTypeCode = (String)request.getAttribute( "promotionTypeCode" );
    }
    form.setMode( mode );

    request.setAttribute( "submitterName", getParticipantName( submitterId ) );

    // Setup the search criteria form.
    Date formStartDate = startDate != null ? startDate : getDefaultStartDate();
    form.setStartDate( DateUtils.toDisplayString( formStartDate ) );

    Date formEndDate = endDate != null ? endDate : getDefaultEndDate();
    // make sure end date has the maximum time of the day 23:59:59
    Date endDateMaxTime = formEndDate;
    endDateMaxTime = DateUtils.toEndDate( formEndDate );

    form.setEndDate( DateUtils.toDisplayString( formEndDate ) );
    // Bug fix # 22831
    List promotionList = getPromotionList( promotionTypeCode );
    PropertyComparator.sort( promotionList, new MutableSortDefinition( "name", false, true ) );
    requestWrapper.setAttribute( "promotionList", promotionList );

    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    if ( promotionId == null && form.getPromotionId() != null && form.getPromotionId().longValue() != 0 )
    { // promotionId might come from the DDL on the form that was submitted
      promotionId = form.getPromotionId();
    }
    else
    {
      form.setPromotionId( promotionId );
    }

    request.setAttribute( "refreshDate", getRefreshDate( "promonodeactivity" ) );

    // Obtain data for bar graphs. These do not show if an 'all promotion' option is selected
    try // TODO for 1.1: needs rework, look into combining 6 separate calls into one
    {
      if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
      {
        // recognition received
        Long recogntionReceivedCount = getActivityService().getMyRecognitionReceived( promotionId, form.getSubmitterId(), form.getStartDate(), form.getEndDate() );
        request.setAttribute( "recogntionReceivedCount", recogntionReceivedCount );

        BigDecimal recogntionReceivedAverageForMyTeamCount = getActivityService().getRecognitionReceivedAveargeForMyTeam( promotionId, form.getSubmitterId(), form.getStartDate(), form.getEndDate() );
        request.setAttribute( "recogntionReceivedAverageForMyTeamCount", recogntionReceivedAverageForMyTeamCount );

        BigDecimal recogntionReceviedAverageForAllPromotions = getActivityService().getRecognitionReceivedAveargeForPromotions( promotionId, form.getStartDate(), form.getEndDate() );
        request.setAttribute( "recogntionReceviedAverageForAllPromotions", recogntionReceviedAverageForAllPromotions );
        // recognition sent
        Long recogntionSentCount = getActivityService().getMyRecognitionSent( promotionId, form.getSubmitterId(), form.getStartDate(), form.getEndDate() );
        request.setAttribute( "recogntionSentCount", recogntionSentCount );

        BigDecimal recogntionSentAverageForMyTeamCount = getActivityService().getRecognitionSentAveargeForMyTeam( promotionId, form.getSubmitterId(), form.getStartDate(), form.getEndDate() );
        request.setAttribute( "recogntionSentAverageForMyTeamCount", recogntionSentAverageForMyTeamCount );

        BigDecimal recogntionSentAverageForAllPromotions = getActivityService().getRecognitionSentAveargeForPromotions( promotionId, form.getStartDate(), form.getEndDate() );
        request.setAttribute( "recogntionSentAverageForAllPromotions", recogntionSentAverageForAllPromotions );
      }
      if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
      {
        // Nomination Submitted
        Long nominationSubmittedCount = getActivityService().getMySumittedNominations( promotionId, form.getSubmitterId(), form.getStartDate(), form.getEndDate() );
        request.setAttribute( "nominationSubmittedCount", nominationSubmittedCount );

        BigDecimal nominationSubmittedAveargeForMyTeamCount = getActivityService().getSumittedNominationsAveargeForMyTeam( promotionId, form.getSubmitterId(), form.getStartDate(), form.getEndDate() );
        request.setAttribute( "nominationSubmittedAveargeForMyTeamCount", nominationSubmittedAveargeForMyTeamCount );

        BigDecimal nominationSubmittedAveargeForMyPromotionsCount = getActivityService().getSumittedNominationsAveargeForPromotions( promotionId, form.getStartDate(), form.getEndDate() );
        request.setAttribute( "nominationSubmittedAveargeForMyPromotionsCount", nominationSubmittedAveargeForMyPromotionsCount );
        // Nomination received
        Long nominationReceivedCount = getActivityService().getMyNominationsReceived( promotionId, form.getSubmitterId(), form.getStartDate(), form.getEndDate() );
        request.setAttribute( "nominationReceivedCount", nominationReceivedCount );

        BigDecimal nominationReceivedCountAveargeForMyTeamCount = getActivityService().getNominationsReceivedAveargeForMyTeam( promotionId,
                                                                                                                               form.getSubmitterId(),
                                                                                                                               form.getStartDate(),
                                                                                                                               form.getEndDate() );
        request.setAttribute( "nominationReceivedCountAveargeForMyTeamCount", nominationReceivedCountAveargeForMyTeamCount );

        BigDecimal nominationReceivedAveargeForMyCompanyCount = getActivityService().getNominationsReceivedAveargeForCompany( promotionId, form.getStartDate(), form.getEndDate() );
        request.setAttribute( "nominationReceivedAveargeForMyCompanyCount", nominationReceivedAveargeForMyCompanyCount );
      }
    }
    catch( Exception e )
    {
      // Do nothing, as date is already validated
    }

    // Setup the history list.
    ValueObjectBuilder sentbuilder = null;
    ValueObjectBuilder receivedbuilder = null;
    Map<String, Object> receivedSentValue = new HashMap<String, Object>();
    receivedSentValue.put( "submitterId", form.getSubmitterId() );
    receivedSentValue.put( "proxyUserId", form.getProxyUserId() );
    receivedSentValue.put( "promotionId", form.getPromotionId() );
    receivedSentValue.put( "startDate", form.getStartDate() );
    receivedSentValue.put( "endDate", endDateMaxTime.toString() );
    receivedSentValue.put( "promotionTypeCode", promotionTypeCode );

    request.setAttribute( "receiveSentValueObj", receivedSentValue );

    if ( promotionTypeCode.equals( PromotionType.NOMINATION ) )
    {
      receivedbuilder = new NominationReceiveBuilder( form.getSubmitterId(), form.getPromotionId(), DateUtils.toDate( form.getStartDate() ), endDateMaxTime );

      sentbuilder = new NominationSentBuilder( form.getSubmitterId(), form.getProxyUserId(), form.getPromotionId(), DateUtils.toDate( form.getStartDate() ), endDateMaxTime );
    }
    else if ( promotionTypeCode.equals( PromotionType.RECOGNITION ) )
    {
      receivedbuilder = new RecognitionReceiveBuilder( form.getSubmitterId(), form.getPromotionId(), DateUtils.toDate( form.getStartDate() ), endDateMaxTime );

      sentbuilder = new RecognitionSentBuilder( form.getSubmitterId(), form.getProxyUserId(), form.getPromotionId(), DateUtils.toDate( form.getStartDate() ), endDateMaxTime );
    }
    if ( receivedbuilder != null )
    {
      List<RecognitionHistoryValueObject> receiverValueObjects = receivedbuilder.buildValueObjects();
      if ( receiverValueObjects != null && !receiverValueObjects.isEmpty() )
      {
        for ( RecognitionHistoryValueObject historyValueObject : receiverValueObjects )
        {
          if ( historyValueObject.isReversal() )
          {
            request.setAttribute( "toShowRCopy", "Yes" );
            break;
          }
        }
      }
      else
      {
        isAccountStatusOnHold = getMainContentService().checkShowShopORTravel();
        if ( !isAccountStatusOnHold )
        {
          request.setAttribute( "status", "hold" );
        }
      }
      request.setAttribute( "receivedvalueObjects", receiverValueObjects );

    }

    if ( sentbuilder != null )
    {
      List<RecognitionHistoryValueObject> sentValuObjects = sentbuilder.buildValueObjects();
      if ( sentValuObjects != null && !sentValuObjects.isEmpty() )
      {
        for ( RecognitionHistoryValueObject historyValueObject : sentValuObjects )
        {
          if ( historyValueObject.isReversal() )
          {
            request.setAttribute( "toShowRCopy", "Yes" );
            break;
          }
        }
      }

      request.setAttribute( "sentvalueObjects", sentValuObjects );
    }
  }

  private String getRefreshDate( String categoryCode )
  {
    return DateUtils.toDisplayTimeWithMeridiemString( getReportsService().getReportDate( categoryCode ) );
  }

  /**
   * Returns the name of the specified participant, or null if the participant
   * ID is null or specifies no participant.
   * 
   * @param participantId
   *            identifies a participant.
   * @return the name of the specified participant, or null if the participant
   *         ID is null or specifies no participant.
   */
  private String getParticipantName( Long participantId )
  {
    String participantName = null;

    Participant participant = getParticipantService().getParticipantById( participantId );
    if ( participant != null )
    {
      participantName = participant.getNameLFMWithComma();
    }

    return participantName;
  }

  /**
   * Returns a list of live and expired promotions.
   * 
   * @param promotionTypeCode
   *            the abstract recognition type: nomination or recognition.
   * @return a list of live and expired promotions.
   */
  private List getPromotionList( String promotionTypeCode )
  {
    PromotionQueryConstraint promotionQueryConstraint = promotionTypeCode.equals( PromotionType.NOMINATION )
        ? (PromotionQueryConstraint)new NominationPromotionQueryConstraint()
        : (PromotionQueryConstraint)new RecognitionPromotionQueryConstraint();

    promotionQueryConstraint
        .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );

    return getPromotionService().getPromotionList( promotionQueryConstraint );
  }

  /**
   * Returns the participant service.
   * 
   * @return a reference to the participant service.
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the ActivityService from the beanLocator.
   * 
   * @return ActivityService
   */
  private static ActivityService getActivityService()
  {
    return (ActivityService)getService( ActivityService.BEAN_NAME );
  }

  private static ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private ReportsService getReportsService()
  {
    return (ReportsService)getService( ReportsService.BEAN_NAME );
  }

  /**
   * Get the JournalService from the beanLocator.
   * 
   * @return JournalService
   */
  private JournalService getJournalService()
  {
    return (JournalService)getService( JournalService.BEAN_NAME );
  }

  /**
   * Returns the diy quiz service.
   * 
   * @return a reference to the claim service.
   */
  private DIYQuizService getDIYQuizService()
  {
    return (DIYQuizService)getService( DIYQuizService.BEAN_NAME );
  }

  /**
   * Builds value objects used by the claim list on the Nomination History
   * page and the Recognition History page.
   */
  public static interface ValueObjectBuilder
  {
    /**
     * Returns information about the specified recognition claims.
     * 
     * @return information about the specified recognition claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects();
  }

  /**
   * Adds behavior specific to this controller to an
   * <code>HttpServletRequest</code> object.
   */
  private static class RequestWrapper extends HttpServletRequestWrapper
  {
    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------
    /**
     * Keys to request attributes and parameters.
     */
    private static final String PROMOTION_TYPE_CODE = "promotionTypeCode";

    private static final String MODE = "mode";

    private static final String START_DATE = "startDate";

    private static final String END_DATE = "endDate";

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private static final Log logger = LogFactory.getLog( RequestWrapper.class );

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------
    /**
     * Constructs a <code>RequestWrapper</code> object.
     * 
     * @param request
     */
    public RequestWrapper( HttpServletRequest request )
    {
      super( request );
    }

    /**
     * Returns the latest date on which claims in the claim list were
     * submitted. Returns now if the user did not specify an end date;
     * returns null if the user enterred an invalid date.
     * 
     * @return the latest date on which claims in the claim list are
     *         submitted.
     */
    public Date getEndDate()
    {
      Date endDate = null;

      String endDateString = getParameter( END_DATE );
      if ( endDateString != null && endDateString.length() > 0 )
      {
        try
        {
          endDate = DateUtils.toEndDate( endDateString );
        }
        catch( ParseException e )
        {
          logger.warn( "Invalid end date." );
        }
      }

      return endDate;
    }

    /**
     * Returns the mode.
     * 
     * @return the mode.
     */
    public String getMode()
    {
      String mode = getParameter( MODE );
      if ( mode == null || mode.length() == 0 )
      {
        mode = SENT_MODE;
      }

      return mode;
    }

    /**
     * Returns the Recognition History form.
     * 
     * @return the Recognition History form.
     */
    public RecognitionHistoryForm getRecognitionHistoryForm()
    {
      RecognitionHistoryForm form = (RecognitionHistoryForm)getAttribute( RecognitionHistoryForm.FORM_NAME );
      if ( form == null )
      {
        form = new RecognitionHistoryForm();
        setAttribute( RecognitionHistoryForm.FORM_NAME, form );
      }

      return form;
    }

    /**
     * Returns the earliest date on which claims in the claim list were
     * submitted. Returns null if the user enterred an invalid start date.
     * 
     * @return the earliest date on which claims in the claim list are
     *         submitted.
     */
    public Date getStartDate()
    {
      Date startDate = null;

      String startDateString = getParameter( START_DATE );
      if ( startDateString != null && startDateString.length() > 0 )
      {
        try
        {
          startDate = DateUtils.toStartDate( startDateString );
        }
        catch( ParseException e )
        {
          logger.warn( "Invalid start date." );
        }
      }

      return startDate;
    }

    /**
     * Returns "nomination" if the user is viewing nomination history pages
     * and "recognition" if the user is viewing recognition history pages.
     * 
     * @return "nomination" if the user is viewing nomination history pages
     *         and "recognition" if the user is viewing recognition history
     *         pages.
     */
    public String getPromotionTypeCode()
    {
      String promotionTypeCode = getParameter( PROMOTION_TYPE_CODE );
      if ( promotionTypeCode == null || promotionTypeCode.length() == 0 )
      {
        promotionTypeCode = PromotionType.RECOGNITION;
      }

      return promotionTypeCode;
    }
  }

  /**
   * Builds value objects used by the Recognitions Received list on the
   * Recognition History page.
   */
  private static class RecognitionReceiveBuilder implements ValueObjectBuilder
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private Long recipientId;

    private Long promotionId;

    private Date startDate;

    private Date endDate;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------
    /**
     * Constructs a <code>RecognitionReceiveBuilder</code> object.
     * 
     * @param recipientId
     *            the ID of the user who is the recognition recipient.
     * @param promotionId
     *            if specified, return claims for only this promotion.
     * @param startDate
     *            if specified, return claims that were submitted on or
     *            after this date.
     * @param endDate
     *            if specified, return claims that were submitted on or
     *            before this date.
     */
    public RecognitionReceiveBuilder( Long recipientId, Long promotionId, Date startDate, Date endDate )
    {
      this.recipientId = recipientId;
      this.promotionId = promotionId;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    /**
     * Returns information about the specified recognition claims.
     * 
     * @return information about the specified recognition claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects()
    {
      List valueObjects = new ArrayList();

      List claimRecipientList = getClaimRecipientList( getClaimList() );
      for ( Iterator iter = claimRecipientList.iterator(); iter.hasNext(); )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
        String timeZoneID = getUserService().getUserTimeZone( claimRecipient.getRecipient().getId() );
        Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
        RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( claimRecipient );
        if ( ( (RecognitionClaim)claimRecipient.getClaim() ).isReversal() )
        {
          valueObject.setReversal( true );
        }
        List activities = null;
        boolean flag = false;
        if ( claimRecipient.getClaim().getPromotion().isRecognitionPromotion() )
        {
          RecognitionPromotion recPromotion = (RecognitionPromotion)claimRecipient.getClaim().getPromotion();
          if ( recPromotion.isSelfRecognitionEnabled() )
          {
            if ( claimRecipient.getRecipient().getId().longValue() == recipientId.longValue() )
            {
              flag = true;
            }
          }
        }
        if ( flag )
        {
          activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId(), null );
        }
        else
        {
          activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId() );
        }
        valueObject.setMerchGiftCodeActivityList( activities );
        valueObjects.add( valueObject );
      }
      // Get information about non claim related recognitions
      valueObjects.addAll( getNonClaimRelatedDeposits( this.recipientId ) );
      Long cuurentUserId = UserManager.getUser().getUserId();
      List<ParticipantBadge> participantBadges = getGamificationService().getBadgeByParticipantId( cuurentUserId );

      valueObjects.addAll( buildRecActHisValObj( participantBadges, startDate, endDate ) );
      return valueObjects;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------
    /**
     * Returns the claims specified by the state of this object.
     * 
     * @return the claims specified by the state of this object, as a
     *         <code>List</code> of {@link RecognitionClaim} objects.
     */
    private List getClaimList()
    {
      // Setup the query constraint.
      RecognitionClaimQueryConstraint queryConstraint = new RecognitionClaimQueryConstraint();

      queryConstraint.setRecipientId( recipientId );
      queryConstraint.setOpen( Boolean.FALSE );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setIncludedPromotionIds( new Long[] { promotionId } );
      }

      queryConstraint.setStartDate( startDate );
      queryConstraint.setEndDate( endDate );

      // Setup the association request collection.
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

      // Get the claim list.
      return getClaimService().getClaimListWithAssociations( queryConstraint, associationRequestCollection );
    }

    /**
     * Returns a list of the recipients from the given claims.
     * 
     * @return a list of the recipients from the given claims.
     */
    private List getClaimRecipientList( List claimList )
    {
      ArrayList claimRecipientList = new ArrayList();

      for ( Iterator claimIter = claimList.iterator(); claimIter.hasNext(); )
      {
        RecognitionClaim claim = (RecognitionClaim)claimIter.next();
        for ( Iterator claimRecipientIter = claim.getClaimRecipients().iterator(); claimRecipientIter.hasNext(); )
        {
          ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIter.next();

          Long localRecipientId = claimRecipient.getRecipient().getId();
          Long claimId = claimRecipient.getClaim().getId();

          if ( claimRecipient.getApprovalStatusType().isAbstractApproved() && localRecipientId.equals( recipientId ) && !getClaimService().hasPendingJournalForClaim( localRecipientId, claimId ) )
          {
            claimRecipientList.add( claimRecipient );
          }
        }
      }

      return claimRecipientList;
    }

    /**
     * Returns the claim service.
     * 
     * @return a reference to the claim service.
     */
    private ClaimService getClaimService()
    {
      return (ClaimService)getService( ClaimService.BEAN_NAME );
    }

    /**
     * @param participantId
     * @return List of 'faked' value object that are really non-claim
     *         related deposits such as file load, discretionary awards
     */
    private List getNonClaimRelatedDeposits( Long participantId )
    {
      List depositList = new ArrayList();

      // Get any deposits for this promotion not linked to any quiz taking.
      // eg. file load, discretionary
      JournalQueryConstraint journalQueryConstraint = new JournalQueryConstraint();
      journalQueryConstraint.setParticipantId( participantId );
      journalQueryConstraint.setNotResultOfClaim( true );
      journalQueryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );
      journalQueryConstraint.setPromotionId( promotionId );
      journalQueryConstraint.setStartDate( startDate );
      journalQueryConstraint.setEndDate( endDate );

      // need to do the association to read the Promotion obj from Journal
      // obj in jsp
      AssociationRequestCollection journalAssociationRequestCollection = new AssociationRequestCollection();
      journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION_DEPROXY ) );
      journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.ACTIVITY_JOURNALS ) );

      List journalList = getJournalService().getJournalList( journalQueryConstraint, journalAssociationRequestCollection );
      for ( Iterator journalIter = journalList.iterator(); journalIter.hasNext(); )
      {
        Journal journal = (Journal)journalIter.next();
        // only want recognition promotion type deposits
        if ( journal.getPromotion() != null && journal.getPromotion().isRecognitionPromotion() )
        {
          RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( journal );
          valueObject.setMerchGiftCodeActivityList( getMerchOrderActivityList( journal ) );
          depositList.add( valueObject );
        }
      }
      return depositList;
    }

    private List getMerchOrderActivityList( Journal journal )
    {
      List merchOrderActivityList = new ArrayList();
      AbstractRecognitionPromotion recognitionPromotion = (AbstractRecognitionPromotion)journal.getPromotion();
      if ( recognitionPromotion.isAwardActive() && recognitionPromotion.getAwardType().isMerchandiseAwardType()
          && RecognitionPromotion.AWARD_STRUCTURE_LEVEL.equals( recognitionPromotion.getAwardStructure() ) )
      {
        for ( Iterator activityJournalIter = journal.getActivityJournals().iterator(); activityJournalIter.hasNext(); )
        {
          ActivityJournal currentActivityJournal = (ActivityJournal)activityJournalIter.next();
          if ( currentActivityJournal.getActivity() instanceof MerchandiseActivity )
          {
            merchOrderActivityList.add( currentActivityJournal.getActivity() );
          }
        }
      }
      return merchOrderActivityList;
    }

    /**
     * Returns the journal service.
     * 
     * @return a reference to the journal service.
     */
    private JournalService getJournalService()
    {
      return (JournalService)getService( JournalService.BEAN_NAME );
    }
  }

  /**
   * Builds value objects used by the Recognitions Sent list on the
   * Recognition History page.
   */
  private static class RecognitionSentBuilder implements ValueObjectBuilder
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private Long submitterId;

    private Long proxyUserId;

    private Long promotionId;

    private Date startDate;

    private Date endDate;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------
    /**
     * Constructs a <code>RecognitionSentBuilder</code> object.
     * 
     * @param submitterId
     *            the ID of the user who submitted the claims.
     * @param proxyUserId
     *            the ID of the user who submitted the claims on behalf of
     *            the submitter.
     * @param promotionId
     *            if specified, return claims for only this promotion.
     * @param startDate
     *            if specified, return claims that were submitted on or
     *            after this date.
     * @param endDate
     *            if specified, return claims that were submitted on or
     *            before this date.
     */
    public RecognitionSentBuilder( Long submitterId, Long proxyUserId, Long promotionId, Date startDate, Date endDate )
    {
      this.submitterId = submitterId;
      this.proxyUserId = proxyUserId;
      this.promotionId = promotionId;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    /**
     * Returns information about the specified recognition claims.
     * 
     * @return information about the specified recognition claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects()
    {
      List valueObjects = new ArrayList();

      List claimRecipientList = getClaimRecipientList( getClaimList() );
      for ( Iterator iter = claimRecipientList.iterator(); iter.hasNext(); )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
        RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( claimRecipient );
        Long recipientUserId = null;
        if ( ( (RecognitionClaim)claimRecipient.getClaim() ).isReversal() )
        {
          valueObject.setReversal( true );
        }
        if ( claimRecipient.getRecipient() != null )
        {
          recipientUserId = claimRecipient.getRecipient().getId();
        }
        List activities = null;
        if ( claimRecipient.getRecipient() != null )
        {
          boolean flag = false;
          if ( claimRecipient.getClaim().getPromotion().isRecognitionPromotion() )
          {
            RecognitionPromotion recPromotion = (RecognitionPromotion)claimRecipient.getClaim().getPromotion();
            if ( recPromotion.isSelfRecognitionEnabled() )
            {
              if ( claimRecipient.getRecipient().getId().longValue() == submitterId.longValue() )
              {
                flag = true;

              }
            }
          }
          if ( flag )
          {
            activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId(), null );
          }
          else
          {
            activities = getActivityService().getMerchOrderActivitiesByClaimIdAndUserId( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId() );
          }
        }
        valueObject.setMerchGiftCodeActivityList( activities );
        valueObjects.add( valueObject );
      }

      return valueObjects;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------
    /**
     * Returns the claims specified by the state of this object.
     * 
     * @return the claims specified by the state of this object, as a
     *         <code>List</code> of {@link RecognitionClaim} objects.
     */
    private List getClaimList()
    {
      // Setup the query constraint.
      RecognitionClaimQueryConstraint queryConstraint = new RecognitionClaimQueryConstraint();

      queryConstraint.setSubmitterId( submitterId );
      queryConstraint.setProxyUserId( proxyUserId );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setIncludedPromotionIds( new Long[] { promotionId } );
      }

      queryConstraint.setStartDate( startDate );
      queryConstraint.setEndDate( endDate );

      // Setup the association request collection.
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

      // Get the claim list.
      return getClaimService().getClaimListWithAssociations( queryConstraint, associationRequestCollection );
    }

    /**
     * Returns a list of the recipients from the given claims.
     * 
     * @return a list of the recipients from the given claims.
     */
    private List getClaimRecipientList( List claimList )
    {
      ArrayList claimRecipientList = new ArrayList();

      for ( Iterator claimIter = claimList.iterator(); claimIter.hasNext(); )
      {
        RecognitionClaim claim = (RecognitionClaim)claimIter.next();
        claimRecipientList.addAll( claim.getClaimRecipients() );
      }

      return claimRecipientList;
    }

    /**
     * Returns the claim service.
     * 
     * @return a reference to the claim service.
     */
    private ClaimService getClaimService()
    {
      return (ClaimService)getService( ClaimService.BEAN_NAME );
    }
  }

  /**
   * Builds value objects used by the Nominations Sent list on the Nomination
   * History page.
   */
  public static class NominationSentBuilder implements ValueObjectBuilder
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private Long submitterId;

    private Long proxyUserId;

    private Long promotionId;

    private Date startDate;

    private Date endDate;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------
    /**
     * Constructs a <code>NominationSentBuilder</code> object.
     * 
     * @param submitterId
     *            the ID of the user who submitted the claims.
     * @param proxyUserId
     *            the ID of the user who submitted the claims on behalf of
     *            the submitter.
     * @param promotionId
     *            if specified, return claims for only this promotion.
     * @param startDate
     *            if specified, return claims that were submitted on or
     *            after this date.
     * @param endDate
     *            if specified, return claims that were submitted on or
     *            before this date.
     */
    public NominationSentBuilder( Long submitterId, Long proxyUserId, Long promotionId, Date startDate, Date endDate )
    {
      this.submitterId = submitterId;
      this.proxyUserId = proxyUserId;
      this.promotionId = promotionId;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    /**
     * Returns information about the specified nomination claims.
     * 
     * @return information about the specified nomination claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects()
    {
      List valueObjects = new ArrayList();
      List claimList = getClaimList();
      for ( Iterator iter = claimList.iterator(); iter.hasNext(); )
      {
        NominationClaim claim = (NominationClaim)iter.next();
        if ( null != claim.getNominationStatusType() && claim.getNominationStatusType().getCode().equals( NominationClaimStatusType.COMPLETE ) )
        {
          valueObjects.add( new RecognitionHistoryValueObject( claim ) );
        }
      }

      return valueObjects;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------
    /**
     * Returns the claims specified by the state of this object.
     * 
     * @return the claims specified by the state of this object, as a
     *         <code>List</code> of {@link NominationClaim} objects.
     */
    private List getClaimList()
    {
      // Setup the query constraint.
      NominationClaimQueryConstraint queryConstraint = new NominationClaimQueryConstraint();

      queryConstraint.setSubmitterId( submitterId );
      queryConstraint.setProxyUserId( proxyUserId );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setIncludedPromotionIds( new Long[] { promotionId } );
      }

      queryConstraint.setStartDate( startDate );
      queryConstraint.setEndDate( endDate );

      // Setup the association request collection.
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

      // Get the claim list.
      return getClaimService().getClaimListWithAssociations( queryConstraint, associationRequestCollection );
    }

    /**
     * Returns the claim service.
     * 
     * @return a reference to the claim service.
     */
    private ClaimService getClaimService()
    {
      return (ClaimService)getService( ClaimService.BEAN_NAME );
    }
  }

  // ---------------------------------------------------------------------------
  /**
   * Builds value objects used by the Nominations Received list on the
   * Nomination History page.
   */
  public static class NominationReceiveBuilder implements ValueObjectBuilder
  {
    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------
    private Long recipientId;

    private Long promotionId;

    private Date startDate;

    private Date endDate;

    // -------------------------------------------------------------------------
    // Public Methods
    // -------------------------------------------------------------------------
    /**
     * Constructs a <code>NominationReceiveBuilder</code> object.
     * 
     * @param recipientId
     *            the ID of the user who is the nomination recipient.
     * @param promotionId
     *            if specified, return claims for only this promotion.
     * @param startDate
     *            if specified, return claims that were submitted on or
     *            after this date.
     * @param endDate
     *            if specified, return claims that were submitted on or
     *            before this date.
     */
    public NominationReceiveBuilder( Long recipientId, Long promotionId, Date startDate, Date endDate )
    {
      this.recipientId = recipientId;
      this.promotionId = promotionId;
      this.startDate = startDate;
      this.endDate = endDate;
    }

    /**
     * Returns information about the specified nomination claims.
     * 
     * @return information about the specified nomination claims, as a
     *         <code>List</code> of {@link RecognitionHistoryValueObject}
     *         objects.
     */
    public List buildValueObjects()
    {
      List valueObjects = new ArrayList();
      boolean canNotify = true;
      boolean isWinner = false;
      boolean isPublic = false;
      List<Long> claimGroupIdsList = new ArrayList<Long>();

      // Get information about nominations received where claims are
      // approved cumulatively.
      Boolean isAwardActive = null;
      if ( promotionId != null )
      {
        NominationPromotion nominationPromotion = (NominationPromotion)getPromotionService().getPromotionById( promotionId );
        isAwardActive = nominationPromotion.isAwardActive();
      }
      List claimGroupList = getClaimGroupList( JournalStatusType.POST, isAwardActive );
      for ( Iterator iter = claimGroupList.iterator(); iter.hasNext(); )
      {
        ClaimGroup claimGroup = (ClaimGroup)iter.next();

        if ( claimGroup.getNotificationDate() != null )
        {
          canNotify = canNotify( claimGroup.getNotificationDate() );
        }
        if ( canNotify )
        {
          if ( claimGroup != null && claimGroup.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER ) )
          {
            valueObjects.add( new RecognitionHistoryValueObject( claimGroup ) );
          }
        }
        claimGroupIdsList.add( claimGroup.getId() );
      }
      // Get information about nominations received where claims are
      // approved independently.
      List claimList = getClaimList( JournalStatusType.POST );
      for ( Iterator iter = claimList.iterator(); iter.hasNext(); )
      {
        NominationClaim claim = (NominationClaim)iter.next();
        if ( claim.getClaimGroup() == null || claim.getClaimGroup() != null && !claimGroupIdsList.contains( claim.getClaimGroup().getId() ) )
        {
          // Individual based claims will have recipients
          if ( claim.getClaimRecipients() != null && !claim.getClaimRecipients().isEmpty() )
          {
            for ( Iterator claimRecipientIter = claim.getClaimRecipients().iterator(); claimRecipientIter.hasNext(); )
            {
              ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIter.next();

              if ( claimRecipient != null )
              {
                if ( claimRecipient.getRecipient().getId().longValue() == UserManager.getUserId().longValue() )
                {
                  Set<ApprovableItemApprover> approvableItemApprovers = claimRecipient.getApprovableItemApprovers();
                  isPublic = claim.getPromotion() != null && ( (NominationPromotion)claim.getPromotion() ).isAllowPublicRecognition();
                  for ( ApprovableItemApprover item : approvableItemApprovers )
                  {
                    canNotify = canNotify( item.getNotificationDate() );
                    isWinner = item.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER );
                    // Public nominations and private winning nominations will show up
                    if ( canNotify && ( isWinner || isPublic ) )
                    {
                      RecognitionHistoryValueObject recognitionHistoryValueObject = new RecognitionHistoryValueObject( claim, item, claimRecipient );
                      if ( recognitionHistoryValueObject != null && recognitionHistoryValueObject.getClaim() != null )
                      {
                        valueObjects.add( recognitionHistoryValueObject );
                      }
                    }
                  }
                }
              }
            }
          }
          // Team based claims have team members
          else if ( claim.getClaimRecipients() != null && !claim.getClaimRecipients().isEmpty() )
          {
            Iterator<ClaimRecipient> teamMemberIterator = claim.getClaimRecipients().iterator();
            while ( teamMemberIterator.hasNext() )
            {
              ClaimRecipient teamMember = teamMemberIterator.next();

              if ( teamMember != null )
              {
                canNotify = true;
                isWinner = false;
                isPublic = claim.getPromotion() != null && ( (NominationPromotion)claim.getPromotion() ).isAllowPublicRecognition();
              }
            }
          }
        }
      }

      // Get information about non claim related nominations
      valueObjects.addAll( getNonClaimRelatedDeposits( this.recipientId ) );
      return valueObjects;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------
    private boolean canNotify( Date notificationDate )
    {
      if ( notificationDate != null )
      {
        // Add Notification process time if any so that if today is the notification day, we don't
        // send
        // until the time of day of the process is passed.
        Date now = new Date();
        Long timeOfDayMillis = getProcessService().getTimeOfDayMillisOfFirstSchedule( NominationAutoNotificationProcess.BEAN_NAME );
        if ( timeOfDayMillis == null )
        {
          timeOfDayMillis = new Long( 0 );
        }

        Date processingDateWithTime = new Date( notificationDate.getTime() + timeOfDayMillis.longValue() );
        if ( now.before( processingDateWithTime ) )
        {
          return false;
        }
      }

      return true;
    }

    /**
     * Builds a {@link JournalClaimQueryConstraint} object that selects
     * {@link Claim} objects.
     * 
     * @param awardGroupType
     *            either "individual" or "team."
     * @return a {@link JournalClaimQueryConstraint} object that selects
     *         {@link Claim} objects.
     */
    private JournalClaimQueryConstraint buildJournalClaimQueryConstraint( NominationAwardGroupType awardGroupType, String journalStatusType )
    {
    if(null != awardGroupType){
    	JournalClaimQueryConstraint queryConstraint = new JournalClaimQueryConstraint( awardGroupType );
    
      queryConstraint.setRecipientId( recipientId );
      queryConstraint.setJournalStatusType( journalStatusType );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setPromotionId( promotionId );
      }

      if ( startDate != null )
      {
        queryConstraint.setApprovalStartDate( startDate );
      }

      if ( endDate != null )
      {
        queryConstraint.setApprovalEndDate( endDate );
      }
    
      return queryConstraint;
    }else {
    	return null;
    }
    }
    /**
     * Builds a {@link JournalClaimQueryConstraint} object that selects
     * {@link Claim} objects.
     * 
     * @param awardGroupType
     *            either "individual" or "team."
     * @return a {@link JournalClaimQueryConstraint} object that selects
     *         {@link Claim} objects.
     */
    private JournalClaimQueryConstraint buildJournalClaimQueryConstraint( NominationAwardGroupType awardGroupType, String journalStatusType, NominationAwardGroupSizeType awardGroupSizeType )
    {
      JournalClaimQueryConstraint queryConstraint = new JournalClaimQueryConstraint( awardGroupType, awardGroupSizeType );

      queryConstraint.setRecipientId( recipientId );
      queryConstraint.setJournalStatusType( journalStatusType );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setPromotionId( promotionId );
      }

      if ( startDate != null )
      {
        queryConstraint.setApprovalStartDate( startDate );
      }

      if ( endDate != null )
      {
        queryConstraint.setApprovalEndDate( endDate );
      }

      return queryConstraint;
    }

    /**
     * Returns the claim groups specified by the state of this object.
     * 
     * @return the claim groups specified by the state of this object, as a
     *         <code>List</code> of {@link ClaimGroup} objects.
     */
    private List getClaimGroupList( String journalStatusType, Boolean isAwardActive )
    {
      // Setup the query constraint.
      JournalClaimGroupQueryConstraint queryConstraint = new JournalClaimGroupQueryConstraint();

      queryConstraint.setParticipantId( recipientId );
      queryConstraint.setJournalStatusType( journalStatusType );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setPromotionId( promotionId );
        queryConstraint.setAwardActive( isAwardActive );
      }

      if ( startDate != null )
      {
        queryConstraint.setApprovalStartDate( startDate );
      }

      if ( endDate != null )
      {
        queryConstraint.setApprovalEndDate( endDate );
      }

      // Setup the association request collection.
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.CLAIMS ) );

      // Get the claim group list.
      return getClaimGroupService().getClaimGroupList( queryConstraint, associationRequestCollection );
    }

    /**
     * Returns the claims specified by the state of this object.
     * 
     * @return the claims specified by the state of this object, as a
     *         <code>List</code> of {@link NominationClaim} objects.
     */
    private List getClaimList( String journalStatusType )
    {
      List claimList = new ArrayList();
 
      // Setup the query constraint to get claim list with no points.
      NominationClaimQueryConstraint queryConstraint = new NominationClaimQueryConstraint();

      queryConstraint.setRecipientId( recipientId );

      // This is to make sure that we do not get duplicate records. The query above is getting all
      // the claims that have points. The below one gets every thing from the nomination claim. This
      // is displaying duplicates in the page. The flag will exclude claims that have award quantity
      // not null in the claim recipient as these claims are getting added in the top query. 
      queryConstraint.setExcludeClaimsWithPoints( true );

      if ( promotionId != null && promotionId.longValue() != 0 )
      {
        queryConstraint.setIncludedPromotionIds( new Long[] { promotionId } );
      }

      if ( startDate != null )
      {
        queryConstraint.setStartDate( startDate );
      }

      if ( endDate != null )
      {
        queryConstraint.setEndDate( endDate );
      }
      
      queryConstraint.setOpen( false );
      
      // Setup the association request collection.
      AssociationRequestCollection associationRequestCollection1 = new AssociationRequestCollection();
      associationRequestCollection1.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
      associationRequestCollection1.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
      associationRequestCollection1.add( new ClaimAssociationRequest( ClaimAssociationRequest.TEAM_MEMBERS ) );
      
      claimList.addAll( getClaimService().getClaimListWithAssociations( queryConstraint, associationRequestCollection1 ) );

      /* coke customization start 
       * Needed another query to pickup team nominations where no journal for the whole team was
       * created because they are all opted out
       * */
      List cokeClaimIdList = getClaimService().getNomTeamClaimsWithoutJournalForActivityHistoryReceivedTab( startDate, endDate, promotionId, recipientId );
      if (cokeClaimIdList != null && cokeClaimIdList.size() > 0)
      {
        NominationClaimQueryConstraint queryConstraintCoke = new NominationClaimQueryConstraint();
        queryConstraintCoke.setIncludedClaimIds( getPromotionsArray(cokeClaimIdList) );
        claimList.addAll( getClaimService().getClaimListWithAssociations( queryConstraintCoke, associationRequestCollection1 ) );
      }
      /* coke customization end */
      
      return claimList;
    }

    /* coke customization start */
    private Long[] getPromotionsArray( List promotionIds )
    {
      List<Long> newPromotionIds = new ArrayList<Long>();
      for ( int i = 0; i < promotionIds.size(); i++ )
      {
        Long promoId = (Long)promotionIds.get( i );
        newPromotionIds.add( promoId );

      }
      if ( !promotionIds.isEmpty() )
        return newPromotionIds.toArray( new Long[0] );
      else
        return null;
    }
    /* coke customization end */


    /**
     * @param participantId
     * @return List of 'faked' value object that are really non-claim
     *         related deposits such as file load, discretionary awards
     */
    private List getNonClaimRelatedDeposits( Long participantId )
    {
      List depositList = new ArrayList();

      // Get any deposits for this promotion not linked to any quiz taking.
      // eg. file load, discretionary
      JournalQueryConstraint journalQueryConstraint = new JournalQueryConstraint();
      journalQueryConstraint.setParticipantId( participantId );
      journalQueryConstraint.setNotResultOfClaim( true );
      journalQueryConstraint.setJournalStatusTypesIncluded( new JournalStatusType[] { JournalStatusType.lookup( JournalStatusType.POST ) } );
      journalQueryConstraint.setPromotionId( promotionId );
      journalQueryConstraint.setStartDate( startDate );
      journalQueryConstraint.setEndDate( endDate );

      // need to do the association to read the Promotion obj from Journal
      // obj in jsp
      AssociationRequestCollection journalAssociationRequestCollection = new AssociationRequestCollection();
      journalAssociationRequestCollection.add( new JournalAssociationRequest( JournalAssociationRequest.PROMOTION ) );

      List journalList = getJournalService().getJournalList( journalQueryConstraint, journalAssociationRequestCollection );
      for ( Iterator journalIter = journalList.iterator(); journalIter.hasNext(); )
      {
        Journal journal = (Journal)journalIter.next();
        // only want nomination promotion type deposits
        if ( journal.getPromotion() != null && journal.getPromotion().isNominationPromotion() )
        {
          RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject( journal );
          depositList.add( valueObject );
        }
      }
      return depositList;
    }

    /**
     * Returns the claim group service.
     * 
     * @return a reference to the claim group service.
     */
    private ClaimGroupService getClaimGroupService()
    {
      return (ClaimGroupService)getService( ClaimGroupService.BEAN_NAME );
    }

    /**
     * Returns the claim service.
     * 
     * @return a reference to the claim service.
     */
    private ClaimService getClaimService()
    {
      return (ClaimService)getService( ClaimService.BEAN_NAME );
    }

    /**
     * Returns the journal service.
     * 
     * @return a reference to the journal service.
     */
    private JournalService getJournalService()
    {
      return (JournalService)getService( JournalService.BEAN_NAME );
    }
  }

  public void buildProductClaimValueObject( HttpServletRequest request, ActivityHistoryForm form )
  {
    RequestWrapper requestWrapper = new RequestWrapper( request );

    // Get parameters.
    Long submitterId = null;
    Long proxyUserId = null;
    Long promotionId = null;
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
        try
        {
          submitterId = (Long)clientStateMap.get( "submitterId" );
        }
        catch( ClassCastException cce )
        {
          submitterId = new Long( (String)clientStateMap.get( "submitterId" ) );
        }
        try
        {
          proxyUserId = (Long)clientStateMap.get( "proxyUserId" );
        }
        catch( ClassCastException cce )
        {
          String temp = (String)clientStateMap.get( "proxyUserId" );
          if ( !StringUtils.isEmpty( temp ) )
          {
            proxyUserId = new Long( temp );
          }
        }
        try
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        catch( ClassCastException cce )
        {
          if ( ! ( (String)clientStateMap.get( "promotionId" ) ).startsWith( "all" ) )
          {
            promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( submitterId == null )
    {
      submitterId = UserManager.getUser().getUserId();
    }
    Date startDate = requestWrapper.getStartDate();
    Date endDate = requestWrapper.getEndDate();

    // Setup the page.
    if ( request.getAttribute( "promotionId" ) != null )
    {
      promotionId = (Long)request.getAttribute( "promotionId" );
    }

    request.setAttribute( "submitterName", getParticipantName( submitterId ) );

    // Setup the search criteria form.
    Date formStartDate = startDate != null ? startDate : getDefaultStartDate();

    Date formEndDate = endDate != null ? endDate : getDefaultEndDate();
    // make sure end date has the maximum time of the day 23:59:59
    Date endDateMaxTime = formEndDate;
    endDateMaxTime = DateUtils.toEndDate( formEndDate );

    List promotionList = getClaimsPromotionList();
    PropertyComparator.sort( promotionList, new MutableSortDefinition( "name", false, true ) );
    requestWrapper.setAttribute( "promotionList", promotionList );

    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

    request.setAttribute( "refreshDate", getRefreshDate( "promonodeactivity" ) );

    if ( promotionId != null )
    {
      try
      {

        // Open Claims
        Long openClaimsSubmittedCount = getActivityService().getMyProductClaimsSubmitted( promotionId, submitterId, "open", form.getStartDate(), form.getEndDate() );
        request.setAttribute( "openClaimsSubmittedCount", openClaimsSubmittedCount );

        BigDecimal openClaimsSubmittedAverageForMyTeamCount = getActivityService().getProductClaimsSubmittedAverageForMyTeam( promotionId,
                                                                                                                              submitterId,
                                                                                                                              "open",
                                                                                                                              form.getStartDate(),
                                                                                                                              form.getEndDate() );
        request.setAttribute( "openClaimsSubmittedAverageForMyTeamCount", openClaimsSubmittedAverageForMyTeamCount );

        BigDecimal openClaimsSubmittedAverageForMyCompanyCount = getActivityService().getProductClaimsSubmittedAverageForCompany( promotionId, "open", form.getStartDate(), form.getEndDate() );
        request.setAttribute( "openClaimsSubmittedAverageForMyCompanyCount", openClaimsSubmittedAverageForMyCompanyCount );

        // Closed Claims
        Long closedClaimsSubmittedCount = getActivityService().getMyProductClaimsSubmitted( promotionId, submitterId, "closed", form.getStartDate(), form.getEndDate() );
        request.setAttribute( "closedClaimsSubmittedCount", closedClaimsSubmittedCount );

        BigDecimal closedClaimsSubmittedAverageForMyTeamCount = getActivityService().getProductClaimsSubmittedAverageForMyTeam( promotionId,
                                                                                                                                submitterId,
                                                                                                                                "closed",
                                                                                                                                form.getStartDate(),
                                                                                                                                form.getEndDate() );
        request.setAttribute( "closedClaimsSubmittedAverageForMyTeamCount", closedClaimsSubmittedAverageForMyTeamCount );

        BigDecimal closedClaimsSubmittedAverageForMyCompanyCount = getActivityService().getProductClaimsSubmittedAverageForCompany( promotionId, "closed", form.getStartDate(), form.getEndDate() );
        request.setAttribute( "closedClaimsSubmittedAverageForMyCompanyCount", closedClaimsSubmittedAverageForMyCompanyCount );
      }
      catch( Exception e )
      {
        // Do nothing, as date is already validated
      }
    }

    // Get the open claim list.
    List openClaimList = getClaimList( submitterId, proxyUserId, promotionId, startDate, endDate, true );
    request.setAttribute( "openClaimList", openClaimList );

    // Get the size of the open claim list.
    request.setAttribute( "openClaimListSize", new Integer( openClaimList.size() ) );

    // Get the closed claim list.
    List closedClaimList = getClaimList( submitterId, proxyUserId, promotionId, startDate, endDate, false );
    request.setAttribute( "closedClaimList", closedClaimList );
    boolean isAccountStatusOnHold = getMainContentService().checkShowShopORTravel();
    if ( !isAccountStatusOnHold )
    {
      request.setAttribute( "status", "hold" );
    }
  }

  /**
   * Get the promotion list.
   *
   * @return List of product claim master promotions that are live or expired
   */
  private List getClaimsPromotionList()
  {
    List promotions = new ArrayList();

    ProductClaimPromotionQueryConstraint masterOnlyProductClaimPromoQueryConstraint = new ProductClaimPromotionQueryConstraint();
    masterOnlyProductClaimPromoQueryConstraint.setMasterOrChildConstraint( Boolean.TRUE );
    masterOnlyProductClaimPromoQueryConstraint
        .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );

    promotions = getPromotionService().getPromotionList( masterOnlyProductClaimPromoQueryConstraint );
    return promotions;
  }

  /**
   * Returns the specified claim list.
   * 
   * @param submitterId the ID of the user who submitted the claims in the claim list. Must not be null.
   * @param promotionId the ID of the promotion for which claims in the claim list are submitted. Can be null.
   * @param startDate the earliest date on which claims in the claim list are submitted. Must not be null.
   * @param endDate the latest date on which claims in the claim list are submitted. Must not be null.
   * @param isOpen true if the claim list contains only open claims; false if the claim list contains only closed claims.
   * @return the specified claim list, as a <code>List</code> of {@link ProductClaimValueObject} objects.
   */
  private List getClaimList( Long submitterId, Long proxyUserId, Long promotionId, Date startDate, Date endDate, boolean isOpen )
  {
    List valueObjectList = new ArrayList();

    // Get all the claims for which this person was a submitter or a team member.
    ProductClaimClaimQueryConstraint queryConstraint = new ProductClaimClaimQueryConstraint();

    queryConstraint.setSubmitterOrTeamMemberParticipantId( submitterId );
    queryConstraint.setProxyUserId( proxyUserId );
    queryConstraint.setSubmitter( true );
    queryConstraint.setTeamMember( true );
    queryConstraint.setIncludedPromotionIds( promotionId != null ? new Long[] { promotionId } : null );
    queryConstraint.setStartDate( startDate );
    queryConstraint.setEndDate( endDate );
    queryConstraint.setOpen( Boolean.valueOf( isOpen ) );

    List claimList = getClaimService().getClaimList( queryConstraint );
    String timeZoneID = getUserService().getUserTimeZone( submitterId );

    // Look up the earnings. Maybe move this down to the service layer as a refactoring.
    for ( int i = 0; i < claimList.size(); i++ )
    {
      Claim claim = (Claim)claimList.get( i );
      claim.setTimeZoneID( timeZoneID );
      claim.setEarnings( getClaimService().getEarningsForClaim( claim.getId(), submitterId ) );
      ProductClaimValueObject valueObject = new ProductClaimValueObject( claim );
      valueObjectList.add( valueObject );
    }

    return valueObjectList;
  }

  private static GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  private static List<RecognitionHistoryValueObject> buildRecActHisValObj( List<ParticipantBadge> participantBadges, Date startDate, Date endDate )
  {
    List<RecognitionHistoryValueObject> valueObjects = new ArrayList<RecognitionHistoryValueObject>( participantBadges.size() );

    Participant participant = new Participant();
    participant.setLastName( CmsResourceBundle.getCmsBundle().getString( "home.navmenu.admin", "SYSTEM" ) );

    for ( ParticipantBadge participantBadge : participantBadges )
    {
      Long createdBy = participantBadge.getAuditCreateInfo().getCreatedBy();
      if ( createdBy == 1 && DateUtils.isDateBetween( participantBadge.getEarnedDate(), startDate, endDate ) )
      {
        BadgeRule badgeRule = participantBadge.getBadgeRule();
        RecognitionHistoryValueObject valueObject = new RecognitionHistoryValueObject();
        Promotion promotion = new RecognitionPromotion();
        promotion.setPromotionName( badgeRule.getBadgeNameTextFromCM() );

        valueObject.setPromotion( promotion );
        valueObject.setSubmissionDate( new Timestamp( participantBadge.getEarnedDate().getTime() ) );
        valueObject.setSubmitter( participant );
        valueObject.setAwardQuantity( badgeRule.getBadgePoints() );
        valueObject.setAwardTypeName( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getName() );
        valueObject.setIsBadgePromotion( true );
        valueObject.setRecipient( (Participant)participantBadge.getParticipant() );
        valueObjects.add( valueObject );
      }
    }
    return valueObjects;
  }
}

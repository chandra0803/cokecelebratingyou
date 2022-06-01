
package com.biperf.core.ui.diyquiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.DIYQuizStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.diyquiz.DIYQuizService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.quiz.QuizAssociationRequest;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.QuizFileUploadValue;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 *
 * DIYQuizAdminAction.
 *
 * @author kandhi
 * @since Jun 28, 2013
 * @version 1.0
 */
public class DIYQuizAdminAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( DIYQuizAdminAction.class );
  public static final String PAGE_ADMIN = "admin";
  public static final String PAGE_PAX = "pax";

  /**
   * Dispatcher.  Default to home page display.
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
   * Displays the admin quiz list page by status
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "showDIYQuizList";

    DIYQuizForm diyQuizForm = (DIYQuizForm)form;
    QuizPromotion quizPromotion = getPromotionService().getLiveDIYQuizPromotion();
    if ( quizPromotion != null )
    {
      addQuizzesToForm( request, diyQuizForm, quizPromotion );
    }
    else
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.diy.errors.NO_ACTIVE_DIY_PROMO_FOUND" ) );
      saveErrors( request, errors );
    }
    return mapping.findForward( forward );
  }

  protected void addQuizzesToForm( HttpServletRequest request, DIYQuizForm diyQuizForm, QuizPromotion quizPromotion )
  {
    List<DIYQuiz> incompleteQuizzes = getDIYQuizService().getQuizByPromotionAndStatus( DIYQuizStatusType.INCOMPLETE, quizPromotion.getId() );
    List<DIYQuiz> pendingQuizzes = getDIYQuizService().getQuizByPromotionAndStatus( DIYQuizStatusType.PENDING, quizPromotion.getId() );
    List<DIYQuiz> activeQuizzes = getDIYQuizService().getQuizByPromotionAndStatus( DIYQuizStatusType.ACTIVE, quizPromotion.getId() );

    diyQuizForm.setPromotionId( quizPromotion.getId() );
    request.setAttribute( "incompleteQuizzes", incompleteQuizzes );
    request.setAttribute( "pendingQuizzes", pendingQuizzes );
    request.setAttribute( "activeQuizzes", activeQuizzes );
    request.setAttribute( "hasPromotion", true );
  }

  /**
   * Displays the admin diy quiz list page for inactive status
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings( "rawtypes" )
  public ActionForward viewInactive( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "showInactiveDIYQuizList";
    DIYQuizForm diyQuizForm = (DIYQuizForm)form;

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    Long promotionId = (Long)clientStateMap.get( "promotionId" );
    diyQuizForm.setPromotionId( promotionId );

    List<DIYQuiz> inactiveQuizzes = getDIYQuizService().getQuizByPromotionAndStatus( DIYQuizStatusType.INACTIVE, promotionId );
    request.setAttribute( "inactiveQuizzes", inactiveQuizzes );
    return mapping.findForward( forward );
  }

  /**
   * Delete the rows selected
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings( "rawtypes" )
  public ActionForward prepareRemove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final String forwardTo = "showDIYQuizList";
    String quizStatusType = request.getParameter( "quizType" );
    DIYQuizForm diyQuizForm = (DIYQuizForm)form;
    String[] quizzesToRemove = {};
    if ( quizStatusType != null && quizStatusType.equalsIgnoreCase( DIYQuizStatusType.ACTIVE ) )
    {
      quizzesToRemove = diyQuizForm.getDeleteQuizzesActive();
    }
    else if ( quizStatusType != null && quizStatusType.equalsIgnoreCase( DIYQuizStatusType.INCOMPLETE ) )
    {
      quizzesToRemove = diyQuizForm.getDeleteQuizzesIncomplete();
    }
    else if ( quizStatusType != null && quizStatusType.equalsIgnoreCase( DIYQuizStatusType.PENDING ) )
    {
      quizzesToRemove = diyQuizForm.getDeleteQuizzesPending();
    }
    List quizIdsList = ArrayUtil.convertStringArrayToListOfLongObjects( quizzesToRemove );
    ActionMessages errors = new ActionMessages();
    try
    {
      getDIYQuizService().deleteQuizes( quizIdsList );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
    }
    QuizPromotion quizPromotion = getPromotionService().getLiveDIYQuizPromotion();
    addQuizzesToForm( request, diyQuizForm, quizPromotion );
    return mapping.findForward( forwardTo );
  }

  /**
   * Load an empty quiz page for create
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "createDIYQuiz";
    DIYQuizForm diyQuizForm = (DIYQuizForm)form;

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    String source = (String)clientStateMap.get( "source" );
    Long promotionId = (Long)clientStateMap.get( "promotionId" );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
    QuizPromotion quizPromotion = (QuizPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

    Set<Participant> userNodesPaxList = null;
    Set<Participant> paxList = new HashSet<Participant>();

    if ( UserManager.getUser().isParticipant() )
    {
      AssociationRequestCollection requestCollection = new AssociationRequestCollection();
      requestCollection.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
      User loggedInUser = getUserService().getUserByIdWithAssociations( UserManager.getUserId(), requestCollection );
      Collection<Node> nodes = new ArrayList<Node>();
      nodes.addAll( loggedInUser.getUserNodesAsNodes() );

      // Get list of participant in logged in user nodes
      userNodesPaxList = getParticipantService().getPaxInNodes( nodes, false );

      for ( Participant pax : userNodesPaxList )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        Participant partner = getParticipantService().getParticipantByIdWithAssociations( pax.getId(), associationRequestCollection );
        paxList.add( partner );
      }
    }

    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    DIYQuizPageView pageViewBean;
    pageViewBean = new DIYQuizPageView( quizPromotion.getPromotionCertificates(), quizPromotion.getBadge(), paxList, siteUrlPrefix );
    pageViewBean.setStatus( ClaimFormStatusType.UNDER_CONSTRUCTION );

    // Set client state variables to JSON
    pageViewBean.setClientState( clientState );
    pageViewBean.setCryptoPassword( cryptoPass );
    pageViewBean.setSource( source );
    pageViewBean.setPromotionId( quizPromotion.getId() );
    diyQuizForm.setPromotionId( promotionId );
    diyQuizForm.setInitializationJson( toJson( pageViewBean ) );

    diyQuizForm.setStartDate( DateUtils.toDisplayString( DateUtils.getCurrentDateTrimmed() ) );
    if ( StringUtils.isEmpty( diyQuizForm.getIntroText() ) )
    {
      diyQuizForm.setIntroText( CmsResourceBundle.getCmsBundle().getString( "quiz.diy.form.DEFAULT_INTRO_TEXT" ) );
    }

    request.setAttribute( "source", source );
    request.setAttribute( "mode", "create" );
    return mapping.findForward( forward );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward loadManagerParticipants( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long ownerId = RequestUtils.getRequiredParamLong( request, "participantId" );
    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
    User loggedInUser = getUserService().getUserByIdWithAssociations( ownerId, requestCollection );
    Collection<Node> nodes = new ArrayList<Node>();
    nodes.addAll( loggedInUser.getUserNodesAsNodes() );

    // Get list of participants for logged in user nodes
    Set<Participant> userNodesPaxList = getParticipantService().getPaxInNodes( nodes, false );

    Set<Participant> paxList = new HashSet<Participant>();
    for ( Iterator<Participant> iter = userNodesPaxList.iterator(); iter.hasNext(); )
    {
      Participant pax = iter.next();
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
      Participant partner = getParticipantService().getParticipantByIdWithAssociations( pax.getId(), associationRequestCollection );
      paxList.add( partner );
    }

    DIYQuizManagerParticipantView managerParticipantView = new DIYQuizManagerParticipantView( paxList );
    super.writeAsJsonToResponse( managerParticipantView, response );
    return null;
  }

  /**
   * Load the quiz page for edit
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYQuizForm diyQuizForm = (DIYQuizForm)form;

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    String source = (String)clientStateMap.get( "source" );
    Long quizId = (Long)clientStateMap.get( "quizId" );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    associationRequestCollection.add( new QuizAssociationRequest( QuizAssociationRequest.QUIZ_QUESTION_ANSWER ) );
    DIYQuiz diyQuiz = getDIYQuizService().getQuizByIdWithAssociations( quizId, associationRequestCollection );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
    QuizPromotion quizPromotion = (QuizPromotion)getPromotionService().getPromotionByIdWithAssociations( diyQuiz.getPromotion().getId(), promoAssociationRequestCollection );

    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    DIYQuizPageView pageViewBean = new DIYQuizPageView( quizPromotion.getPromotionCertificates(), quizPromotion.getBadge(), diyQuiz, siteUrlPrefix, false );

    pageViewBean.setStatus( diyQuiz.getClaimFormStatusType().getCode() );

    // Set client state variables to JSON
    pageViewBean.setClientState( clientState );
    pageViewBean.setCryptoPassword( cryptoPass );
    pageViewBean.setSource( source );
    pageViewBean.setPromotionId( quizPromotion.getId() );

    diyQuizForm.setInitializationJson( toJson( pageViewBean ) );
    request.setAttribute( "source", source );
    request.setAttribute( "mode", "edit" );
    return mapping.findForward( "updateDIYQuiz" );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public ActionForward prepareCopy( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "createDIYQuiz";
    DIYQuizForm diyQuizForm = (DIYQuizForm)form;

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    String source = (String)clientStateMap.get( "source" );
    Long quizId = (Long)clientStateMap.get( "quizId" );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    associationRequestCollection.add( new QuizAssociationRequest( QuizAssociationRequest.QUIZ_QUESTION_ANSWER ) );
    DIYQuiz diyQuiz = getDIYQuizService().getQuizByIdWithAssociations( quizId, associationRequestCollection );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
    QuizPromotion quizPromotion = (QuizPromotion)getPromotionService().getPromotionByIdWithAssociations( diyQuiz.getPromotion().getId(), promoAssociationRequestCollection );

    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    DIYQuizPageView pageViewBean = new DIYQuizPageView( quizPromotion.getPromotionCertificates(), quizPromotion.getBadge(), diyQuiz, siteUrlPrefix, true );

    pageViewBean.setStatus( ClaimFormStatusType.UNDER_CONSTRUCTION );

    // Set client state variables to JSON
    pageViewBean.setClientState( clientState );
    pageViewBean.setCryptoPassword( cryptoPass );
    pageViewBean.setSource( source );
    pageViewBean.setPromotionId( quizPromotion.getId() );

    diyQuizForm.setInitializationJson( toJson( pageViewBean ) );
    diyQuizForm.setStartDate( DateUtils.toDisplayString( DateUtils.getCurrentDateTrimmed() ) );
    request.setAttribute( "mode", "create" );
    return mapping.findForward( forward );
  }

  /**
   * Saves the incomplete quiz
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings( "rawtypes" )
  public ActionForward saveDraft( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYQuizForm diyQuizForm = (DIYQuizForm)form;
    String source = diyQuizForm.getSource();

    DIYQuiz diyQuiz = new DIYQuiz();
    diyQuizForm.toDomainObj( diyQuiz );

    diyQuiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    populateLearningObjects( diyQuizForm, diyQuiz );

    DIYQuizResponseView responseView = new DIYQuizResponseView();
    try
    {
      diyQuiz = getDIYQuizService().saveDIYQuiz( diyQuiz, diyQuizForm.getPromotionId() );
      responseView.getQuiz().setId( String.valueOf( diyQuiz.getId() ) );
      if ( source.equalsIgnoreCase( "pax" ) )
      {
        responseView.setForwardUrl( RequestUtils.getBaseURI( request ) + mapping.findForward( PAGE_PAX ).getPath() + "&isSave=true" );
      }
      else
      {
        responseView.setForwardUrl( RequestUtils.getBaseURI( request ) + mapping.findForward( PAGE_ADMIN ).getPath() );
      }
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        WebErrorMessage message = new WebErrorMessage();
        message.setType( "error" );
        message.setCode( "validationError" );
        message.setName( "quizNameInput" );
        message.setText( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
        responseView.getMessages().add( message );
      }
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Used to save a new quiz or update an existing quiz
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings( "rawtypes" )
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYQuizForm diyQuizForm = (DIYQuizForm)form;
    String source = diyQuizForm.getSource();

    DIYQuiz diyQuiz = new DIYQuiz();
    diyQuizForm.toDomainObj( diyQuiz );

    diyQuiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );

    populateLearningObjects( diyQuizForm, diyQuiz );
    DIYQuizResponseView responseView = new DIYQuizResponseView();
    try
    {
      getDIYQuizService().saveDIYQuiz( diyQuiz, diyQuizForm.getPromotionId() );
      if ( source.equalsIgnoreCase( "pax" ) )
      {
        responseView.setForwardUrl( RequestUtils.getBaseURI( request ) + mapping.findForward( PAGE_PAX ).getPath() + "&isSave=true" );
      }
      else
      {
        responseView.setForwardUrl( RequestUtils.getBaseURI( request ) + mapping.findForward( PAGE_ADMIN ).getPath() );
      }
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        WebErrorMessage message = new WebErrorMessage();
        message.setType( "error" );
        message.setCode( "validationError" );
        message.setName( "quizNameInput" );
        message.setText( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
        responseView.getMessages().add( message );
      }
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Checks whether the quiz name is unique or not
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   * @throws Exception
   */
  @SuppressWarnings( "rawtypes" )
  public ActionForward checkQuizName( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String quizName = request.getParameter( "quizName" );
    String quizId = request.getParameter( "quizId" );
    Long id = null;

    if ( quizName != null )
    {
      quizName = quizName.trim();
    }
    if ( quizId != null && quizId.length() > 0 )
    {
      id = Long.valueOf( quizId );
    }
    DIYQuizResponseView responseView = new DIYQuizResponseView();
    try
    {
      getDIYQuizService().checkDuplicateQuizName( quizName, id );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        WebErrorMessage message = new WebErrorMessage();
        message.setType( "error" );
        message.setCode( "validationError" );
        message.setName( "quizNameInput" );
        message.setText( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
        responseView.getMessages().add( message );
      }
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Quiz page for create and display based on Status
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward manage( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "manageDIYQuiz";

    QuizPromotion quizPromotion = getPromotionService().getLiveDIYQuizPromotion();
    if ( quizPromotion != null )
    {
      request.setAttribute( "promotionId", quizPromotion.getId() );
    }

    if ( request.getParameter( "isSave" ) != null )
    {
      request.setAttribute( "displayPopup", true );
    }

    return mapping.findForward( forward );
  }

  /**
   * Load the manage quizzes display table page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward manageTable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "success";

    String quizStatusType = request.getParameter( "quizFilter" );

    List<DIYQuiz> quizzes = new ArrayList<DIYQuiz>();
    QuizPromotion quizPromotion = getPromotionService().getLiveDIYQuizPromotion();

    if ( quizPromotion != null )
    {
      if ( quizStatusType.equalsIgnoreCase( DIYQuizStatusType.INACTIVE ) )
      {
        List<DIYQuiz> inactiveQuizzes = getDIYQuizService().getQuizByPromotionAndStatus( DIYQuizStatusType.INACTIVE, quizPromotion.getId() );
        quizzes.addAll( getUserOwnedQuizzes( inactiveQuizzes, DIYQuizStatusType.INACTIVE ) );
      }
      else
      {
        List<DIYQuiz> activeQuizzes = getDIYQuizService().getQuizByPromotionAndStatus( DIYQuizStatusType.ACTIVE, quizPromotion.getId() );
        List<DIYQuiz> pendingQuizzes = getDIYQuizService().getQuizByPromotionAndStatus( DIYQuizStatusType.PENDING, quizPromotion.getId() );
        List<DIYQuiz> incompleteQuizzes = getDIYQuizService().getQuizByPromotionAndStatus( DIYQuizStatusType.INCOMPLETE, quizPromotion.getId() );

        quizzes.addAll( getUserOwnedQuizzes( activeQuizzes, DIYQuizStatusType.ACTIVE ) );
        quizzes.addAll( getUserOwnedQuizzes( pendingQuizzes, DIYQuizStatusType.PENDING ) );
        quizzes.addAll( getUserOwnedQuizzes( incompleteQuizzes, DIYQuizStatusType.INCOMPLETE ) );
      }
    }
    request.setAttribute( "quizzes", quizzes );

    return mapping.findForward( forward );
  }

  private List<DIYQuiz> getUserOwnedQuizzes( List<DIYQuiz> quizzes, String quizStatus )
  {

    List<DIYQuiz> userOwnedQuizzes = new ArrayList<DIYQuiz>();
    for ( DIYQuiz quiz : quizzes )
    {
      if ( quiz.getOwner().getId().equals( UserManager.getUserId() ) )
      {
        quiz.setQuizStatus( DIYQuizStatusType.lookup( quizStatus ).getName() );
        userOwnedQuizzes.add( quiz );
      }
    }
    return userOwnedQuizzes;
  }

  protected void populateLearningObjects( DIYQuizForm diyQuizForm, DIYQuiz diyQuiz ) throws Exception
  {
    if ( diyQuizForm.getMaterialsAsList() != null )
    {
      for ( DIYQuizMaterialView materialView : diyQuizForm.getMaterialsAsList() )
      {
        QuizLearningObject learningObject = new QuizLearningObject();
        learningObject.setContentResourceCMCode( materialView.getCmContentResource() );
        if ( materialView.getId() != null && materialView.getId() > 0 )
        {
          learningObject.setId( materialView.getId() );
        }
        learningObject.setStatus( QuizLearningObject.ACTIVE_STATUS );
        learningObject.setSlideNumber( materialView.getPageNumber() );

        if ( materialView.getFiles() != null )
        {
          StringBuilder htmlStringBuilder = new StringBuilder();
          String quizmediaDesc = materialView.getText();
          StringBuilder filePathStringBuilder = new StringBuilder();
          for ( DIYQuizMaterialFileView file : materialView.getFiles() )
          {
            String mediaHtmlString = "";
            if ( StringUtils.isNotBlank( file.getUrl() ) )
            {
              if ( QuizFileUploadValue.TYPE_IMAGE.equalsIgnoreCase( materialView.getType() ) )
              {
                mediaHtmlString = getHtmlImageString( file.getUrl() );
              }
              else if ( QuizFileUploadValue.TYPE_PDF.equalsIgnoreCase( materialView.getType() ) )
              {
                if ( filePathStringBuilder.length() > 0 )
                {
                  filePathStringBuilder.append( "||" );
                }
                mediaHtmlString = getHtmlPdfString( file.getUrl(), file.getName() );
              }
              else if ( QuizFileUploadValue.TYPE_VIDEO.equalsIgnoreCase( materialView.getType() ) )
              {
                mediaHtmlString = getHtmlVideoString( file.getUrl() );
              }
              else
              {
                mediaHtmlString = "";
              }
              filePathStringBuilder.append( file.getUrl() );
              htmlStringBuilder.append( mediaHtmlString );
            }
          }

          if ( QuizFileUploadValue.TYPE_VIDEO.equalsIgnoreCase( materialView.getType() ) )
          {
            learningObject = getQuizService().saveQuizLearningVideo( learningObject,
                                                                     filePathStringBuilder.toString(),
                                                                     filePathStringBuilder.toString(),
                                                                     filePathStringBuilder.toString(),
                                                                     filePathStringBuilder.toString(),
                                                                     quizmediaDesc );
          }
          else
          {
            getQuizService().saveQuizLearningResources( learningObject, htmlStringBuilder.toString(), quizmediaDesc, filePathStringBuilder.toString() );
          }
        }
        diyQuiz.addQuizLearningObject( learningObject );
      }
    }
  }

  private String getHtmlVideoString( String url )
  {
    StringBuilder htmlVideoString = new StringBuilder();
    String globalUniqueId = String.valueOf( new Date().getTime() );
    htmlVideoString.append( "<video id='example_video_'" + globalUniqueId + " class='video-js vjs-default-skin' controls width='250' height='186' preload='auto'>" );
    htmlVideoString.append( "<source type='video/mp4' src='" + url + "'>" );
    htmlVideoString.append( "</video><script>var myPlayer = _V_('example_video_'" + globalUniqueId + ");</script>" );

    return htmlVideoString.toString();
  }

  private String getHtmlPdfString( String url, String pdfDisplayName )
  {
    StringBuilder htmlPdfString = new StringBuilder();
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    htmlPdfString.append( "<a target='_blank' href='" + url + "'>" );
    htmlPdfString.append( pdfDisplayName );
    htmlPdfString.append( "<img src='" + siteUrlPrefix + "/assets/img/reports/reports_exportPdf.png' alt='pdf image'/>" );
    htmlPdfString.append( "</a>" );
    htmlPdfString.append( "<br>" );
    return htmlPdfString.toString();
  }

  private String getHtmlImageString( String url )
  {
    StringBuilder htmlImageString = new StringBuilder();
    htmlImageString.append( "<p><img src=\"" + url + "\" alt=\"Photo\" class=\"thumb\"/></p>" );
    return htmlImageString.toString();
  }

  public ActionForward uploadLearningObject( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYQuizForm diyQuizForm = (DIYQuizForm)form;
    FormFile formFile = diyQuizForm.getMaterialFile();

    String orginalfilename = formFile.getFileName();
    String fileFormat = FilenameUtils.getExtension( orginalfilename );
    String extension = "." + fileFormat;
    String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
    if ( filename != null )
    {
      filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
      filename = ImageUtils.getValidFileName( filename );
    }
    filename = filename + extension;
    int filesize = formFile.getFileSize();
    byte[] imageInByte = formFile.getFileData();

    QuizFileUploadValue data = new QuizFileUploadValue();
    data.setData( imageInByte );
    data.setName( filename );
    data.setSize( filesize );

    if ( QuizFileUploadValue.TYPE_PDF.equalsIgnoreCase( fileFormat ) && QuizFileUploadValue.TYPE_PDF.equalsIgnoreCase( diyQuizForm.getFileType() ) )
    {
      uploadPDF( response, filename, data, orginalfilename );
    }
    else if ( getDIYQuizService().isValidImageFormat( fileFormat ) && QuizFileUploadValue.TYPE_IMAGE.equalsIgnoreCase( diyQuizForm.getFileType() ) )
    {
      uploadImage( response, filename, data, orginalfilename );
    }
    else
    {
      DIYQuizFileUploadView fileUploadView = new DIYQuizFileUploadView();
      DIYQuizFileUploadPropertiesView propertiesView = new DIYQuizFileUploadPropertiesView();
      propertiesView.setIsSuccess( false );
      String pdfSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.diy.errors.INVALID_FILE_FORMAT" );
      propertiesView.setErrorText( pdfSizeValidMessage );
      propertiesView.setOriginalFilename( orginalfilename );
      fileUploadView.setProperties( propertiesView );
      writeUploadJsonToResponse( fileUploadView, response );
      logger.error( "Invalid file format supplied:" + fileFormat );
    }
    return null;
  }

  private void uploadPDF( HttpServletResponse response, String filename, QuizFileUploadValue data, String orginalfilename ) throws ServiceErrorException, IOException
  {
    try
    {
      data.setType( QuizFileUploadValue.TYPE_PDF );
      boolean validPdfSize = getQuizService().validFileData( data );
      if ( validPdfSize )
      {
        data.setQuizFormName( PromotionType.lookup( PromotionType.DIY_QUIZ ).getCode() );
        // There won't be an Id when creating a new learning object
        Random r = new Random();
        data.setId( Long.parseLong( String.valueOf( r.nextInt( 10000 ) ) ) );
        data = getQuizService().uploadPdfForQuizLibrary( data );
        getQuizService().moveFileToWebdav( data.getFull() );
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        if ( !Environment.isCtech() )
        {
          siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
        }

        String imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + data.getFull();

        DIYQuizFileUploadView fileUploadView = new DIYQuizFileUploadView();
        DIYQuizFileUploadPropertiesView propertiesView = new DIYQuizFileUploadPropertiesView();
        propertiesView.setIsSuccess( true );
        propertiesView.setFileUrl( imageUrlPath );
        propertiesView.setOriginalFilename( filename );
        fileUploadView.setProperties( propertiesView );
        writeUploadJsonToResponse( fileUploadView, response );
      }
      else
      {
        DIYQuizFileUploadView fileUploadView = new DIYQuizFileUploadView();
        DIYQuizFileUploadPropertiesView propertiesView = new DIYQuizFileUploadPropertiesView();
        propertiesView.setIsSuccess( false );
        String pdfSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.diy.errors.VALID_PDF_SIZE" );
        propertiesView.setErrorText( pdfSizeValidMessage );
        propertiesView.setOriginalFilename( filename );
        fileUploadView.setProperties( propertiesView );
        writeUploadJsonToResponse( fileUploadView, response );
      }
    }
    catch( Throwable e )
    {
      DIYQuizFileUploadView fileUploadView = new DIYQuizFileUploadView();
      DIYQuizFileUploadPropertiesView propertiesView = new DIYQuizFileUploadPropertiesView();
      propertiesView.setIsSuccess( false );
      String pdfSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.diy.errors.PDF_UPLOAD_FAILED" );
      propertiesView.setErrorText( pdfSizeValidMessage );
      propertiesView.setOriginalFilename( orginalfilename );
      fileUploadView.setProperties( propertiesView );
      writeUploadJsonToResponse( fileUploadView, response );
      logger.error( "Error during quiz upload pdf:" + e );
    }
  }

  private void uploadImage( HttpServletResponse response, String filename, QuizFileUploadValue data, String orginalfilename ) throws ServiceErrorException, IOException
  {
    try
    {
      data.setType( QuizFileUploadValue.TYPE_PICTURE );
      boolean validImageSize = getQuizService().validFileData( data );
      if ( validImageSize )
      {
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        if ( !Environment.isCtech() )
        {
          siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
        }
        data.setQuizFormName( PromotionType.lookup( PromotionType.DIY_QUIZ ).getCode() );
        // There won't be an Id when creating a new learning object
        Random r = new Random();
        data.setId( Long.parseLong( String.valueOf( r.nextInt( 10000 ) ) ) );
        data.setFileFullPath( siteUrlPrefix + "-cm/cm3dam" + '/' );
        data = getQuizService().uploadPhotoForQuizLibrary( data );
        getQuizService().moveFileToWebdav( data.getFull() );
        getQuizService().moveFileToWebdav( data.getThumb() );

        String imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + data.getThumb();

        DIYQuizFileUploadView fileUploadView = new DIYQuizFileUploadView();
        DIYQuizFileUploadPropertiesView propertiesView = new DIYQuizFileUploadPropertiesView();
        propertiesView.setIsSuccess( true );
        propertiesView.setFileUrl( imageUrlPath );
        propertiesView.setOriginalFilename( orginalfilename );
        fileUploadView.setProperties( propertiesView );
        writeUploadJsonToResponse( fileUploadView, response );
      }
      else
      {
        DIYQuizFileUploadView fileUploadView = new DIYQuizFileUploadView();
        DIYQuizFileUploadPropertiesView propertiesView = new DIYQuizFileUploadPropertiesView();
        double lowerSizeLimit = 1024 * 1024 * .001;
        if ( data.getSize() <= lowerSizeLimit )
        {
          String imageSizeLowerLimitMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.diy.errors.VALID_IMAGE_LOWER_LIMIT" );
          propertiesView.setErrorText( imageSizeLowerLimitMessage );
        }
        else
        {
          String imageSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.diy.errors.VALID_IMAGE_SIZE" );
          propertiesView.setErrorText( imageSizeValidMessage );
        }
        propertiesView.setIsSuccess( false );
        propertiesView.setOriginalFilename( orginalfilename );
        fileUploadView.setProperties( propertiesView );
        writeUploadJsonToResponse( fileUploadView, response );
      }
    }
    catch( Throwable e )
    {
      DIYQuizFileUploadView fileUploadView = new DIYQuizFileUploadView();
      DIYQuizFileUploadPropertiesView propertiesView = new DIYQuizFileUploadPropertiesView();
      propertiesView.setIsSuccess( false );
      propertiesView.setOriginalFilename( orginalfilename );
      String imageSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.diy.errors.IMAGE_UPLOAD_FAILED" );
      propertiesView.setErrorText( imageSizeValidMessage );
      fileUploadView.setProperties( propertiesView );
      writeUploadJsonToResponse( fileUploadView, response );
      logger.error( "Error during quiz upload image:" + e );
      e.printStackTrace();
    }
  }

  /**
   * We need to set the content type as text/html for upload.
   * Default is text/html
   */
  private void writeUploadJsonToResponse( Object bean, HttpServletResponse response ) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.print( toJson( bean ) );
    out.flush();
    out.close();
  }

  private DIYQuizService getDIYQuizService()
  {
    return (DIYQuizService)getService( DIYQuizService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }
}

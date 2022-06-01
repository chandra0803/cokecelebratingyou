/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/quiz/QuizLearningObjectAction.java,v $
 */

package com.biperf.core.ui.quiz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.quiz.QuizLearningObjectView;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.JsonResponse;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.QuizFileUploadValue;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * QuizLearningObjectAction.
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
 * <td>sharafud</td>
 * <td>Oct 3, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizLearningObjectAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( QuizLearningObjectAction.class );

  /**
   * Display Quiz learning Object form
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "display";
    logger.debug( ">>> " + METHOD_NAME );

    // QuizFormForm quizForm=(QuizFormForm) form;
    Long quizFormId = 0L;
    String strQuizFormName = "";
    String uploadType = "";

    try
    {
      String clientState = request.getParameter( "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      if ( clientState != null )
      {
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        strQuizFormName = (String)clientStateMap.get( "quizFormName" );
        uploadType = (String)clientStateMap.get( "uploadType" );
        try
        {
          String strQuizFormId = (String)clientStateMap.get( "quizFormId" );
          if ( quizFormId.intValue() == 0 && !StringUtils.isEmpty( strQuizFormId ) )
          {
            quizFormId = new Long( strQuizFormId );
          }
        }
        catch( ClassCastException cce )
        {
          Long longQuizFormId = (Long)clientStateMap.get( "quizFormId" );
          if ( quizFormId.intValue() == 0 && longQuizFormId != null )
          {
            quizFormId = longQuizFormId;
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    QuizLearningObjectForm quizLearningForm = new QuizLearningObjectForm();
    request.setAttribute( "quizFormId", quizFormId );
    request.setAttribute( "quizFormName", strQuizFormName );
    request.setAttribute( "uploadType", uploadType );
    String forward = ActionConstants.SUCCESS_FORWARD;
    quizLearningForm.setQuizFormId( quizFormId );
    quizLearningForm.setQuizFormName( strQuizFormName );
    quizLearningForm.setUploadType( uploadType );
    return mapping.findForward( forward );
  }

  /**
   * Display edit Quiz learning Object form
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareEdit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "prepareEdit";
    int numberofColumns = 2;
    String uploadType = "image";
    List<QuizLearningDetails> pdfList = new ArrayList<QuizLearningDetails>();
    int numberofPdfs = 0;
    logger.debug( ">>> " + METHOD_NAME );

    // QuizFormForm quizForm=(QuizFormForm) form;
    Long quizFormId = 0L;
    int slideNumber = 0;
    String strQuizFormName = "";
    QuizLearningObjectForm quizLearningForm = (QuizLearningObjectForm)form;
    try
    {
      String clientState = request.getParameter( "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      if ( clientState != null )
      {
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        slideNumber = Integer.parseInt( clientStateMap.get( "slideId" ).toString() );

        // strQuizFormName= (String)clientStateMap.get( "quizFormName" );
        // strQuizFormName=quizLearningForm.getQuizFormName();
        try
        {
          String strQuizFormId = (String)clientStateMap.get( "quizFormId" );
          if ( quizFormId.intValue() == 0 && !StringUtils.isEmpty( strQuizFormId ) )
          {
            quizFormId = new Long( strQuizFormId );
          }
        }
        catch( ClassCastException cce )
        {
          Long longQuizFormId = (Long)clientStateMap.get( "quizFormId" );
          if ( quizFormId.intValue() == 0 && longQuizFormId != null )
          {
            quizFormId = longQuizFormId;
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Quiz quizLib = getQuizService().getQuizById( quizFormId );
    strQuizFormName = quizLib.getName();
    List<QuizLearningDetails> quizLearningDetails = getQuizService().getQuizLearningObjectforSlide( quizLib.getLearningObjects(), slideNumber );
    Iterator quizLearningItr = quizLearningDetails.iterator();
    String imageUrl = "";
    String videoUrl = "";
    String videoUrlMp4 = "";
    String videoUrlWebm = "";
    String videoUrl3gp = "";
    String videoUrlOgg = "";
    String mediaPath = "";
    String leftColumn = "";
    while ( quizLearningItr.hasNext() )
    {
      QuizLearningDetails quizLearningDetail = (QuizLearningDetails)quizLearningItr.next();
      if ( StringUtil.isEmpty( quizLearningDetail.getLeftColumn() ) )
      {
        numberofColumns = 1;
      }
      else
      {
        numberofColumns = 2;
        mediaPath = quizLearningDetail.getFilePath();
        leftColumn = quizLearningDetail.getLeftColumn();
        if ( leftColumn.contains( "<p>" ) )
        {
          uploadType = "image";
          imageUrl = leftColumn;

        }
        else if ( leftColumn.contains( "</a>" ) )
        {
          uploadType = "pdf";
          numberofPdfs = org.apache.commons.lang3.StringUtils.countMatches( leftColumn, "</a>" );
          String pdfString = leftColumn;
          try
          {
            for ( int i = 0; i < numberofPdfs; i++ )
            {
              int indexOccurence = pdfString.indexOf( "</a>" );
              String pdfIndividual = pdfString.substring( 0, indexOccurence + 4 );
              int imageIndex = pdfIndividual.indexOf( "<img src=" );
              // String pdfDisplayName=pdfIndividual.substring( pdfIndividual.indexOf(".pdf"
              // )+6,pdfIndividual.length()-4 );
              String pdfDisplayName = pdfIndividual.substring( pdfIndividual.indexOf( ".pdf" ) + 6, imageIndex );
              if ( numberofPdfs > i + 1 )
              {
                pdfString = pdfString.substring( indexOccurence + 8, pdfString.length() );
              }

              QuizLearningDetails quizLearningPdfs = new QuizLearningDetails();
              quizLearningPdfs.setLeftColumn( pdfIndividual );
              quizLearningPdfs.setRightColumn( pdfDisplayName );
              quizLearningPdfs.setFilePath( mediaPath );
              pdfList.add( quizLearningPdfs );
            }
            java.util.Collections.sort( pdfList );
          }
          catch( Exception e )
          {
            logger.error( "Error while parsing PDFs on prepare edit:" + e );
          }

        }
        else
        {
          uploadType = "video";
          videoUrl = leftColumn;
          videoUrlMp4 = quizLearningDetail.getVideoUrlMp4();
          videoUrlWebm = quizLearningDetail.getVideoUrlWebm();
          videoUrl3gp = quizLearningDetail.getVideoUrl3gp();
          videoUrlOgg = quizLearningDetail.getVideoUrlOgg();
        }

      }
      String rightColumn = quizLearningDetail.getRightColumn();
      quizLearningForm.load( leftColumn, rightColumn, uploadType, numberofColumns, slideNumber, mediaPath, videoUrlMp4, videoUrlWebm, videoUrl3gp, videoUrlOgg );

    }
    request.setAttribute( "numberofPdfs", numberofPdfs );
    request.setAttribute( "imageUrl", imageUrl );
    request.setAttribute( "videoUrl", videoUrl );
    request.setAttribute( "videoUrlMp4", videoUrlMp4 );
    request.setAttribute( "videoUrlWebm", videoUrlWebm );
    request.setAttribute( "videoUrl3gp", videoUrl3gp );
    request.setAttribute( "videoUrlOgg", videoUrlOgg );
    request.setAttribute( "pdfList", pdfList );
    request.setAttribute( "uploadType", uploadType );
    request.setAttribute( "numberOfColumns", numberofColumns );
    request.setAttribute( "quizFormId", quizFormId );
    request.setAttribute( "quizFormName", strQuizFormName );
    String forward = ActionConstants.SUCCESS_FORWARD;
    // quizLearningForm.setQuizFormId( quizFormId );
    // quizLearningForm
    // quizLearningForm.setQuizFormName(strQuizFormName);
    return mapping.findForward( forward );
  }

  public ActionForward saveLearningObject( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, String> clientStateParameterMap = new HashMap<String, String>();
    String forward = "showQuizLibrary";

    String nextPage = request.getParameter( "nextSlide" );
    String cancelled = request.getParameter( "cancel" );
    int numberOfColumns = Integer.parseInt( request.getParameter( "numberOfColumns" ) );
    String quizLabel = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.QUIZ_LABEL" );
    String mediaUrl = "";
    String mediaHtmlString = "";
    String quizmediaDesc = "";

    if ( !StringUtils.isEmpty( nextPage ) )
    {
      forward = "showNextSlide";
    }

    QuizLearningObjectForm quizLearningForm = (QuizLearningObjectForm)form;

    // if ( isCancelled( request ) )
    if ( cancelled != null && cancelled.equalsIgnoreCase( "true" ) )
    {
      // EARLY EXIT
      String forwardAction = ActionConstants.CANCEL_FORWARD;
      Map clientStateParameterCancelMap = new HashMap();
      clientStateParameterCancelMap.put( "quizFormId", quizLearningForm.getQuizFormId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterCancelMap );
      return ActionUtils.forwardWithParameters( mapping, forwardAction, new String[] { queryString, "method=view" } );
    }

    QuizLearningObject quizObj = new QuizLearningObject();
    quizObj.setQuiz( getQuizService().getQuizById( quizLearningForm.getQuizFormId() ) );
    String mediaFilePath = quizLearningForm.getMediaFilePath();
    if ( numberOfColumns == 2 )
    {
      if ( quizLearningForm.getUploadType().equalsIgnoreCase( "image" ) )
      {
        mediaUrl = quizLearningForm.getImageUrl();
        mediaHtmlString = getHtmlImageString( mediaUrl );
        quizmediaDesc = quizLearningForm.getQuizLearningText();
      }
      else if ( quizLearningForm.getUploadType().equalsIgnoreCase( "pdf" ) )
      {
        // mediaUrl=quizLearningForm.getPdfUrl();
        String[] pdfUploadUrlString = quizLearningForm.getPdfUploadStringRow();
        String[] pdfDisplayNames = quizLearningForm.getPdfQuizTextString();
        mediaHtmlString = getHtmlPdfString( pdfUploadUrlString, pdfDisplayNames );
        quizmediaDesc = quizLearningForm.getQuizLearningText();
        mediaFilePath = quizLabel + "/" + quizLearningForm.getQuizFormName() + "/";
      }
      else if ( quizLearningForm.getUploadType().equalsIgnoreCase( "video" ) )
      {
        mediaUrl = quizLearningForm.getVideoUrl();
        mediaHtmlString = getHtmlVideoString( mediaUrl );
        quizmediaDesc = quizLearningForm.getQuizLearningText();
      }
    }
    else
    {
      mediaHtmlString = "";
      quizmediaDesc = quizLearningForm.getQuizLearningTextFull();
    }

    Long quizFormId = quizLearningForm.getQuizFormId();

    int slideNumber = getQuizService().getNextSlideIdForQuiz( quizFormId );

    quizObj.setSlideNumber( slideNumber );
    quizObj.setStatus( QuizLearningObject.ACTIVE_STATUS );

    if ( quizLearningForm.getUploadType().equalsIgnoreCase( "video" ) )
    {
      quizObj = getQuizService()
          .saveQuizLearningVideo( quizObj, quizLearningForm.getVideoUrlMp4(), quizLearningForm.getVideoUrlWebm(), quizLearningForm.getVideoUrl3gp(), quizLearningForm.getVideoUrlOgg(), quizmediaDesc );
    }
    else
    {
      quizObj = getQuizService().saveQuizLearningResources( quizObj, mediaHtmlString, quizmediaDesc, mediaFilePath );
    }

    quizObj = getQuizService().saveQuizLearning( quizObj );

    clientStateParameterMap.put( "quizFormId", quizFormId + "" );
    clientStateParameterMap.put( "quizFormName", quizLearningForm.getQuizFormName() );
    clientStateParameterMap.put( "uploadType", quizLearningForm.getUploadType() );

    // return mapping.findForward( forward );

    if ( !StringUtils.isEmpty( nextPage ) )
    {
      return ActionUtils.forwardWithParameters( mapping, forward, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ), "method=display" } );
    }
    else
    {
      return ActionUtils.forwardWithParameters( mapping, forward, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ), "method=view" } );
    }

  }

  public ActionForward updateLearningObject( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, String> clientStateParameterMap = new HashMap<String, String>();
    String forward = "showQuizLibrary";

    String nextPage = request.getParameter( "nextSlide" );
    String cancelled = request.getParameter( "cancel" );
    int numberOfColumns = Integer.parseInt( request.getParameter( "numberOfColumns" ) );
    String mediaUrl = "";
    String mediaHtmlString = "";
    String quizmediaDesc = "";
    String quizLabel = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.QUIZ_LABEL" );
    if ( !StringUtils.isEmpty( nextPage ) )
    {
      forward = "showNextSlide";
    }

    QuizLearningObjectForm quizLearningForm = (QuizLearningObjectForm)form;

    // if ( isCancelled( request ) )
    if ( cancelled != null && cancelled.equalsIgnoreCase( "true" ) )
    {
      // EARLY EXIT
      String forwardAction = ActionConstants.CANCEL_FORWARD;
      Map clientStateParameterCancelMap = new HashMap();
      clientStateParameterCancelMap.put( "quizFormId", quizLearningForm.getQuizFormId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterCancelMap );
      return ActionUtils.forwardWithParameters( mapping, forwardAction, new String[] { queryString, "method=view" } );
    }

    QuizLearningObject quizObj = new QuizLearningObject();
    quizObj.setQuiz( getQuizService().getQuizById( quizLearningForm.getQuizFormId() ) );
    String mediaFilePath = quizLearningForm.getMediaFilePath();
    if ( numberOfColumns == 2 )
    {
      if ( quizLearningForm.getUploadType().equalsIgnoreCase( "image" ) )
      {
        mediaUrl = quizLearningForm.getImageUrl();
        if ( mediaUrl != null && mediaUrl.contains( "<p>" ) )
        {
          mediaHtmlString = mediaUrl;
        }
        else
        {
          mediaHtmlString = getHtmlImageString( mediaUrl );
        }
        quizmediaDesc = quizLearningForm.getQuizLearningText();
      }
      else if ( quizLearningForm.getUploadType().equalsIgnoreCase( "pdf" ) )
      {
        // mediaUrl=quizLearningForm.getPdfUrl();
        String[] pdfUploadUrlString = quizLearningForm.getPdfUploadStringRow();
        String[] pdfDisplayNames = quizLearningForm.getPdfQuizTextString();
        mediaHtmlString = getHtmlPdfString( pdfUploadUrlString, pdfDisplayNames );
        quizmediaDesc = quizLearningForm.getQuizLearningText();
        mediaFilePath = quizLabel + "/" + quizLearningForm.getQuizFormName() + "/";
      }
      else if ( quizLearningForm.getUploadType().equalsIgnoreCase( "video" ) )
      {
        mediaUrl = quizLearningForm.getVideoUrl();
        if ( mediaUrl != null && mediaUrl.contains( "</video>" ) )
        {
          mediaHtmlString = mediaUrl;
        }
        else
        {
          mediaHtmlString = getHtmlVideoString( mediaUrl );
        }
        // mediaHtmlString=mediaUrl;
        quizmediaDesc = quizLearningForm.getQuizLearningText();
      }
    }
    else
    {
      mediaHtmlString = "";
      quizmediaDesc = quizLearningForm.getQuizLearningTextFull();
    }

    Long quizFormId = quizLearningForm.getQuizFormId();

    int slideNumber = quizLearningForm.getSlideNumber();

    quizObj.setSlideNumber( slideNumber );
    quizObj.setStatus( QuizLearningObject.ACTIVE_STATUS );

    quizObj = getQuizService().getQuizLearningObjectForSlide( quizFormId, slideNumber );

    if ( quizLearningForm.getUploadType().equalsIgnoreCase( "video" ) )
    {
      quizObj = getQuizService()
          .saveQuizLearningVideo( quizObj, quizLearningForm.getVideoUrlMp4(), quizLearningForm.getVideoUrlWebm(), quizLearningForm.getVideoUrl3gp(), quizLearningForm.getVideoUrlOgg(), quizmediaDesc );
    }
    else
    {
      quizObj = getQuizService().saveQuizLearningResources( quizObj, mediaHtmlString, quizmediaDesc, mediaFilePath );
    }

    quizObj = getQuizService().saveQuizLearning( quizObj );

    clientStateParameterMap.put( "quizFormId", quizFormId + "" );
    clientStateParameterMap.put( "quizFormName", quizLearningForm.getQuizFormName() );
    clientStateParameterMap.put( "uploadType", quizLearningForm.getUploadType() );

    // return mapping.findForward( forward );

    if ( !StringUtils.isEmpty( nextPage ) )
    {
      return ActionUtils.forwardWithParameters( mapping, forward, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ), "method=display" } );
    }
    else
    {
      return ActionUtils.forwardWithParameters( mapping, forward, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ), "method=view" } );
    }

  }

  public ActionForward removeSlides( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, String> clientStateParameterMap = new HashMap<String, String>();
    String forward = "showQuizLibrary";

    QuizLearningObjectForm quizLearningForm = (QuizLearningObjectForm)form;
    String[] slidesToDelete = quizLearningForm.getDeleteSlides();

    QuizLearningObject quizObj = new QuizLearningObject();

    Long quizFormId = quizLearningForm.getQuizFormId();

    for ( int i = 0; i < slidesToDelete.length; i++ )
    {
      int slideNumber = Integer.parseInt( slidesToDelete[i] );
      quizObj = getQuizService().getQuizLearningObjectForSlide( quizFormId, slideNumber );
      quizObj.setSlideNumber( slideNumber );
      quizObj.setStatus( QuizLearningObject.INACTIVE_STATUS );
      quizObj = getQuizService().saveQuizLearning( quizObj );
    }

    List<QuizLearningObject> quizActiveLearningObjects = getQuizService().getQuizLearningObjectById( quizFormId );

    for ( int i = 0; i < quizActiveLearningObjects.size() - 1; i++ )
    {
      QuizLearningObject quizLearningObj1 = quizActiveLearningObjects.get( i );
      QuizLearningObject quizLearningObj2 = quizActiveLearningObjects.get( i + 1 );

      if ( i == 0 && quizLearningObj1.getSlideNumber() != 1 )
      {
        quizLearningObj1.setSlideNumber( 1 );
      }
      if ( quizLearningObj2.getSlideNumber() - quizLearningObj1.getSlideNumber() != 1 )
      {
        quizLearningObj2.setSlideNumber( quizLearningObj1.getSlideNumber() + 1 );
      }
    }

    for ( int i = 0; i < quizActiveLearningObjects.size(); i++ )
    {
      getQuizService().mergeQuizLearning( quizActiveLearningObjects.get( i ) );
    }

    clientStateParameterMap.put( "quizFormId", quizFormId + "" );
    clientStateParameterMap.put( "quizFormName", quizLearningForm.getQuizFormName() );

    // return mapping.findForward( forward );

    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ), "method=view" } );

  }

  public ActionForward processPhoto( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    String status = JsonResponse.STATUS_UNKNOWN;
    boolean detailImgmovetoWebDav = false;
    boolean thumbImgmovetoWebDav = false;
    String globalUniqueId = "";
    Long quizFormId = 0L;
    String imageSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_IMAGE_SIZE" );
    String imageSizeLowerLimitMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_IMAGE_LOWER_LIMIT" );
    try
    {
      QuizLearningObjectForm quizLearningForm = (QuizLearningObjectForm)form;

      quizFormId = Long.parseLong( RequestUtils.getRequiredParam( request, "quizFormId" ).toString() );
      String quizFormName = RequestUtils.getRequiredParam( request, "quizFormName" ).toString();
      String orginalfilename = quizLearningForm.getFileAsset().getFileName();
      String extension = "." + getFileExtension( orginalfilename );
      String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
      if ( filename != null )
      {
        filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );

      }
      filename = filename + extension;
      int filesize = quizLearningForm.getFileAsset().getFileSize();
      byte[] imageInByte = quizLearningForm.getFileAsset().getFileData();

      // Not getting the parameter through AJAX file upload, so get it from the session
      globalUniqueId = String.valueOf( new Date().getTime() );

      QuizFileUploadValue data = new QuizFileUploadValue();
      // data.setId( quizLearningForm.getQuizFormId() );
      data.setQuizFormName( quizFormName );
      data.setId( quizFormId );
      data.setData( imageInByte );
      data.setType( QuizFileUploadValue.TYPE_PICTURE );
      data.setName( filename );
      data.setSize( filesize );
      boolean validImageSize = getQuizService().validFileData( data );
      if ( validImageSize )
      {
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        if ( !Environment.isCtech() )
        {
          siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
        }
        data.setFileFullPath( siteUrlPrefix + "-cm/cm3dam" + '/' );
        data = getQuizService().uploadPhotoForQuizLibrary( data );
        detailImgmovetoWebDav = getQuizService().moveFileToWebdav( data.getFull() );
        thumbImgmovetoWebDav = getQuizService().moveFileToWebdav( data.getThumb() );
        status = JsonResponse.STATUS_SUCCESS;

        String imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + data.getThumb();

        QuizLearningObjectView quizLearningView = new QuizLearningObjectView();
        quizLearningView.setFull( data.getFull() );
        quizLearningView.setFilename( data.getName() );
        quizLearningView.setQuizFormId( data.getId() );
        quizLearningView.setId( globalUniqueId );
        quizLearningView.setImageurl( imageUrlPath );
        quizLearningView.setMedia( "picture" );
        quizLearningView.setStatus( status );
        quizLearningView.setThumb( data.getThumb() );
        writeAsJsonToResponse( quizLearningView, response );
        /*
         * PrintWriter out = response.getWriter(); out.print( toJson(quizLearningView));
         * out.flush(); out.close();
         */
      }
      else
      {
        status = JsonResponse.STATUS_FAILED;
        QuizLearningObjectView quizLearningView = new QuizLearningObjectView();
        double lowerSizeLimit = 1024 * 1024 * .001;
        if ( data.getSize() <= lowerSizeLimit )
        {
          quizLearningView.setFail( imageSizeLowerLimitMessage );
        }
        else
        {
          quizLearningView.setFail( imageSizeValidMessage );
        }
        quizLearningView.setStatus( status );
        writeAsJsonToResponse( quizLearningView, response );
      }

    }
    catch( Throwable e )
    {
      status = JsonResponse.STATUS_FAILED;
      QuizLearningObjectView quizLearningView = new QuizLearningObjectView();
      quizLearningView.setStatus( status );
      quizLearningView.setQuizFormId( quizFormId );
      quizLearningView.setId( globalUniqueId );
      quizLearningView.setFail( imageSizeValidMessage );
      logger.error( "Error during quiz process photo:" + e );
      e.printStackTrace();
    }

    return null;

  }

  public ActionForward processVideo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String videoSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_VIDEO_SIZE" );

    String status = JsonResponse.STATUS_UNKNOWN;
    boolean detailImgmovetoWebDav = false;
    boolean thumbImgmovetoWebDav = false;
    String globalUniqueId = "";
    Long quizFormId = 0L;
    try
    {
      QuizLearningObjectForm quizLearningForm = (QuizLearningObjectForm)form;

      quizFormId = Long.parseLong( RequestUtils.getRequiredParam( request, "quizFormId" ).toString() );
      String quizFormName = RequestUtils.getRequiredParam( request, "quizFormName" ).toString();
      String filename = quizLearningForm.getFileAssetVideo().getFileName();
      int filesize = quizLearningForm.getFileAssetVideo().getFileSize();
      byte[] imageInByte = quizLearningForm.getFileAssetVideo().getFileData();
      // Not getting the parameter through AJAX file upload, so get it from the session
      globalUniqueId = String.valueOf( new Date().getTime() );

      QuizFileUploadValue data = new QuizFileUploadValue();
      // data.setId( quizLearningForm.getQuizFormId() );
      data.setQuizFormName( quizFormName );
      data.setId( quizFormId );
      data.setData( imageInByte );
      data.setType( QuizFileUploadValue.TYPE_VIDEO );
      data.setName( filename );
      data.setSize( filesize );

      boolean validVideoSize = getQuizService().validFileData( data );
      if ( validVideoSize )
      {
        data = getQuizService().uploadVideoForQuizLibrary( data );
        detailImgmovetoWebDav = getQuizService().moveFileToWebdav( data.getFull() );

        // thumbImgmovetoWebDav = getQuizService().moveFileToWebdav( data.getThumb() );
        status = JsonResponse.STATUS_SUCCESS;
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        if ( !Environment.isCtech() )
        {
          siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
        }

        String imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + data.getFull();

        // imageUrlPath=imageUrlPath.substring( 0, imageUrlPath.lastIndexOf( "." ) );
        String fileExtension = data.getFull().substring( data.getFull().lastIndexOf( "." ) + 1, data.getFull().length() );

        QuizLearningObjectView quizLearningView = new QuizLearningObjectView();
        if ( !StringUtils.isEmpty( fileExtension ) && fileExtension.equalsIgnoreCase( "mp4" ) )
        {
          quizLearningView.setVideoUrlMp4( imageUrlPath );
        }
        else if ( !StringUtils.isEmpty( fileExtension ) && fileExtension.equalsIgnoreCase( "webm" ) )
        {
          quizLearningView.setVideoUrlWebm( imageUrlPath );
        }
        else if ( !StringUtils.isEmpty( fileExtension ) && fileExtension.equalsIgnoreCase( "3gp" ) )
        {
          quizLearningView.setVideoUrl3gp( imageUrlPath );
        }
        else if ( !StringUtils.isEmpty( fileExtension ) && fileExtension.equalsIgnoreCase( "ogg" ) )
        {
          quizLearningView.setVideoUrlOgg( imageUrlPath );
        }

        quizLearningView.setFull( data.getFull() );
        quizLearningView.setFilename( data.getName() );
        quizLearningView.setQuizFormId( data.getId() );
        quizLearningView.setId( globalUniqueId );
        quizLearningView.setImageurl( imageUrlPath );
        quizLearningView.setMedia( "video" );
        quizLearningView.setStatus( status );
        quizLearningView.setThumb( data.getThumb() );
        writeAsJsonToResponse( quizLearningView, response );
      }
      else
      {
        status = JsonResponse.STATUS_FAILED;
        QuizLearningObjectView quizLearningView = new QuizLearningObjectView();
        quizLearningView.setFail( videoSizeValidMessage );
        quizLearningView.setStatus( status );
        writeAsJsonToResponse( quizLearningView, response );
      }

    }
    catch( Throwable e )
    {
      status = JsonResponse.STATUS_FAILED;
      QuizLearningObjectView quizLearningView = new QuizLearningObjectView();
      quizLearningView.setStatus( status );
      quizLearningView.setQuizFormId( quizFormId );
      quizLearningView.setId( globalUniqueId );
      quizLearningView.setFail( videoSizeValidMessage );

      logger.error( "Error during quiz process video:" + e );

    }

    return null;

  }

  public ActionForward processPDF( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String pdfSizeValidMessage = CmsResourceBundle.getCmsBundle().getString( "quiz.learningForm.VALID_PDF_SIZE" );

    String status = JsonResponse.STATUS_UNKNOWN;
    boolean detailImgmovetoWebDav = false;
    boolean thumbImgmovetoWebDav = false;
    String globalUniqueId = "";
    Long quizFormId = 0L;
    try
    {
      QuizLearningObjectForm quizLearningForm = (QuizLearningObjectForm)form;

      quizFormId = Long.parseLong( RequestUtils.getRequiredParam( request, "quizFormId" ).toString() );
      String quizFormName = RequestUtils.getRequiredParam( request, "quizFormName" ).toString();
      // String filename = quizLearningForm.getFileAsset().getFileName();
      // int filesize = quizLearningForm.getFileAsset().getFileSize();
      // byte[] imageInByte = quizLearningForm.getFileAsset().getFileData();

      String filename = quizLearningForm.getFileAssetPdf().getFileName();
      int filesize = quizLearningForm.getFileAssetPdf().getFileSize();
      byte[] imageInByte = quizLearningForm.getFileAssetPdf().getFileData();

      // Not getting the parameter through AJAX file upload, so get it from the session
      globalUniqueId = String.valueOf( new Date().getTime() );

      QuizFileUploadValue data = new QuizFileUploadValue();
      // data.setId( quizLearningForm.getQuizFormId() );
      data.setQuizFormName( quizFormName );
      data.setId( quizFormId );
      data.setData( imageInByte );
      data.setType( QuizFileUploadValue.TYPE_PDF );
      data.setName( filename );
      data.setSize( filesize );

      boolean validPdfSize = getQuizService().validFileData( data );
      if ( validPdfSize )
      {

        data = getQuizService().uploadPdfForQuizLibrary( data );
        detailImgmovetoWebDav = getQuizService().moveFileToWebdav( data.getFull() );
        // thumbImgmovetoWebDav = getQuizService().moveFileToWebdav( data.getThumb() );
        status = JsonResponse.STATUS_SUCCESS;
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        if ( !Environment.isCtech() )
        {
          siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
        }

        String imageUrlPath = siteUrlPrefix + "-cm/cm3dam" + '/' + data.getFull();

        QuizLearningObjectView quizLearningView = new QuizLearningObjectView();
        quizLearningView.setFull( data.getFull() );
        quizLearningView.setFilename( data.getName() );
        quizLearningView.setQuizFormId( data.getId() );
        quizLearningView.setId( globalUniqueId );
        quizLearningView.setImageurl( imageUrlPath );
        quizLearningView.setMedia( "pdf" );
        quizLearningView.setStatus( status );
        quizLearningView.setThumb( data.getThumb() );
        writeAsJsonToResponse( quizLearningView, response );
      }
      else
      {
        status = JsonResponse.STATUS_FAILED;
        QuizLearningObjectView quizLearningView = new QuizLearningObjectView();
        quizLearningView.setFail( pdfSizeValidMessage );
        quizLearningView.setStatus( status );
        writeAsJsonToResponse( quizLearningView, response );
      }

    }
    catch( Throwable e )
    {
      status = JsonResponse.STATUS_FAILED;
      QuizLearningObjectView quizLearningView = new QuizLearningObjectView();
      quizLearningView.setStatus( status );
      quizLearningView.setQuizFormId( quizFormId );
      quizLearningView.setId( globalUniqueId );
      quizLearningView.setFail( pdfSizeValidMessage );

      logger.error( "Error during quiz process pdf:" + e );

    }

    return null;

  }

  private String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  public String getHtmlImageString( String url )
  {
    StringBuilder htmlImageString = new StringBuilder();
    htmlImageString.append( "<p><img src=\"" + url + "\" alt=\"Photo\" class=\"thumb\"/></p>" );

    return htmlImageString.toString();
  }

  public String getHtmlPdfString( String[] urls, String[] pdfDisplayNames )
  {
    StringBuilder htmlPdfString = new StringBuilder();
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    for ( int i = 0; i < urls.length; i++ )
    {
      if ( urls[i].contains( "</a>" ) )
      {
        htmlPdfString.append( urls[i] );
      }
      else
      {
        htmlPdfString.append( "<a target='_blank' href='" + urls[i] + "'>" );
        htmlPdfString.append( pdfDisplayNames[i] );
        htmlPdfString.append( "<img src='" + siteUrlPrefix + "/assets/img/reports/reports_exportPdf.png' alt='pdf image'/>" );
        htmlPdfString.append( "</a>" );
      }
      htmlPdfString.append( "<br>" );

    }

    return htmlPdfString.toString();
  }

  // Just for Quiz File upload ajax responses, overriding the method in BaseDispatch since the
  // content type application/json is not working
  public void writeAsJsonToResponse( Object bean, HttpServletResponse response ) throws IOException
  {
    PrintWriter out = response.getWriter();
    out.print( toJson( bean ) );
    out.flush();
    out.close();
  }

  public String getHtmlVideoString( String url )
  {
    StringBuilder htmlVideoString = new StringBuilder();
    String globalUniqueId = String.valueOf( new Date().getTime() );
    htmlVideoString.append( "<video id='example_video_'" + globalUniqueId + " class='video-js vjs-default-skin' controls width='250' height='186' preload='auto'>" );
    htmlVideoString.append( "<source type='video/mp4' src='" + url + "'>" );
    htmlVideoString.append( "</video><script>var myPlayer = _V_('example_video_'" + globalUniqueId + ");</script>" );

    return htmlVideoString.toString();
  }

  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }

  protected static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

} // end QuizFormAction

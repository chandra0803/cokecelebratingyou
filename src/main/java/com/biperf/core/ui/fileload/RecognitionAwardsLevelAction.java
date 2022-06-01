
package com.biperf.core.ui.fileload;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.util.StringUtils;

/**
 * RecognitionAwardsLevelAction.
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
 * <td>shanmuga</td>
 * <td>Mar 3, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class RecognitionAwardsLevelAction extends BaseDispatchAction
{

  private static final String CARD_PREFIX = "card:";

  /**
   * Navigate to  the Recognition Options Page.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.CREATE_FORWARD );
    RecognitionAwardsLevelForm recAwardForm = (RecognitionAwardsLevelForm)request.getAttribute( "recognitionAwardsLevelForm" );
    // ActionMessages errors = new ActionMessages();
    ImportFile importFile = getImportFile( getImportFileId( request ) );
    if ( !StringUtils.isEmpty( recAwardForm.getPromotionId() ) )
    {
      if ( request.getSession().getAttribute( "recAwardsImportFile" ) != null )
      {
        ImportFile importOptionsFile = (ImportFile)request.getSession().getAttribute( "recAwardsImportFile" );
        if ( importOptionsFile.getPromotion() != null && !String.valueOf( importOptionsFile.getPromotion().getId() ).equals( recAwardForm.getPromotionId() ) )
        {
          request.getSession().setAttribute( "recognitionAwardsLevelForm", null );
        }
      }

    }
    recAwardForm.setImportFileId( String.valueOf( importFile.getId() ) );
    recAwardForm.setImportFileType( importFile.getFileType().getCode() );
    return forward;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return forward to importFil;e details page.
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ImportFile importFile = null;
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    RecognitionAwardsLevelForm recAwardForm = (RecognitionAwardsLevelForm)form;

    if ( isCancelled( request ) )
    {
      if ( !StringUtils.isEmpty( recAwardForm.getImportFileId() ) )
      {

        importFile = getImportService().getImportFile( new Long( recAwardForm.getImportFileId() ) );
      }
      if ( request.getSession().getAttribute( "recAwardsImportFile" ) == null )
      {
        request.getSession().setAttribute( "recAwardsImportFile", importFile );
      }
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }

    if ( !StringUtils.isEmpty( recAwardForm.getImportFileId() ) )
    {

      importFile = getImportService().getImportFile( new Long( recAwardForm.getImportFileId() ) );
    }
    populateDomainWithCardOrCertificate( importFile, recAwardForm );
    if ( !StringUtils.isEmpty( recAwardForm.getSubmitterId() ) )
    {
      importFile.setSubmitter( getParticipantService().getParticipantById( new Long( recAwardForm.getSubmitterId() ) ) );
    }
    if ( !StringUtils.isEmpty( recAwardForm.getSubmitterNodeId() ) )
    {
      importFile.setSubmitterNode( getNodeService().getNodeById( new Long( recAwardForm.getSubmitterNodeId() ) ) );
    }
    if ( !StringUtils.isEmpty( recAwardForm.getPromotionId() ) )
    {
      Promotion promotion = getPromotionService().getPromotionById( new Long( recAwardForm.getPromotionId() ) );
      /** if( promotion.isRecognitionPromotion() )
      {
        importFile.setRecognitionDeposit( new Boolean(true) );
      }else{
        importFile.setRecognitionDeposit( new Boolean(false) );
      }**/
      importFile.setPromotion( promotion );
      if ( promotion.isRecognitionPromotion() )
      {
        RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
        if ( recognitionPromotion.isCopyRecipientManager() )
        {
          importFile.setCopyManager( recognitionPromotion.isCopyRecipientManager() );
        }
        else
        {
          importFile.setCopyManager( recAwardForm.isCopyManager() );
        }
      }
    }
    if ( !StringUtils.isEmpty( recAwardForm.getComments() ) )
    {
      importFile.setSubmitterComments( recAwardForm.getComments() );
    }
    if ( !StringUtils.isEmpty( recAwardForm.getSelectedBehavior() ) )
    {
      importFile.setBehavior( recAwardForm.getSelectedBehavior() );
    }
    try
    {
      if ( request.getSession().getAttribute( "recAwardsImportFile" ) == null )
      {
        request.getSession().setAttribute( "recAwardsImportFile", importFile );
      }
      else
      {
        request.getSession().setAttribute( "recAwardsImportFile", importFile );
      }

    }
    catch( Exception e )
    {
      log.warn( e.getMessage() );
    }
    getImportService().saveRecognitionAwardsLevel( importFile );
    request.setAttribute( "importFile", importFile );

    return forward;
  }

  /**
   * Returns the specified import file.
   * 
   * @param importFileId the HTTP request we are handling.
   * @return the specified import file.
   */
  private ImportFile getImportFile( Long importFileId )
  {
    ImportService importService = (ImportService)getService( ImportService.BEAN_NAME );
    // ImportFileAssociationRequest importFileAssociationRequest = new
    // ImportFileAssociationRequest();
    return importService.getImportFile( importFileId );
  }

  private Long getImportFileId( HttpServletRequest request )
  {
    Long importFileId = null;

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
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
        String claimFormIdString = (String)clientStateMap.get( "importFileId" );
        importFileId = new Long( claimFormIdString );
      }
      catch( ClassCastException cce )
      {
        importFileId = (Long)clientStateMap.get( "importFileId" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    return importFileId;
  }

  /**
   * @param importFile 
   * 
   * @param recognitionAwardsLevelForm
   * @return ImportFile
   */
  public ImportFile populateDomainWithCardOrCertificate( ImportFile importFile, RecognitionAwardsLevelForm recAwardForm )
  {
    if ( StringUtils.isEmpty( recAwardForm.getCardId() ) )
    {
      return importFile;
    }
    if ( recAwardForm.getCardId().startsWith( CARD_PREFIX ) )
    {
      long selectedCardId = Long.parseLong( recAwardForm.getCardId().substring( CARD_PREFIX.length() ) );
      if ( selectedCardId > -1 )
      {
        importFile.setCard( new Long( selectedCardId ) );
      }
    }

    /*
     * else if ( recognitionOptionsForm.getCardId().startsWith( CERTIFICATE_PREFIX ) ) { String
     * selectedCertificateId = recognitionOptionsForm.getCardId() .substring(
     * CERTIFICATE_PREFIX.length() ); PromotionCertificateFormBean certificate = new
     * PromotionCertificateFormBean(); if( ! StringUtils.isEmpty( selectedCertificateId )) {
     * certificate.setId( new Long( selectedCertificateId ) ); impFile.set } }
     */
    return importFile;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Returns the SystemVariableService service.
   * 
   * @return a reference to the SystemVariableService service.
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private ImportService getImportService()
  {
    return (ImportService)getService( ImportService.BEAN_NAME );
  }

}

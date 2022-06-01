
package com.biperf.core.ui.fileload;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.DepositImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.participant.Participant;
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
 * RecognitionOptionsAction.
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
 * <td>reddy</td>
 * <td>Dec 11, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionOptionsAction extends BaseDispatchAction
{
  private static final String CARD_PREFIX = "card:";

  private static final String CERTIFICATE_PREFIX = "certificate:";

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
    RecognitionOptionsForm recOptionsForm = (RecognitionOptionsForm)request.getAttribute( "recognitionOptionsForm" );
    // ActionMessages errors = new ActionMessages();
    ImportFile importFile = getImportFile( getImportFileId( request ) );
    if ( !StringUtils.isEmpty( recOptionsForm.getPromotionId() ) )
    {
      if ( request.getSession().getAttribute( "recOptionsImportFile" ) != null )
      {
        ImportFile importOptionsFile = (ImportFile)request.getSession().getAttribute( "recOptionsImportFile" );
        if ( importOptionsFile.getPromotion() != null && !String.valueOf( importOptionsFile.getPromotion().getId() ).equals( recOptionsForm.getPromotionId() ) )
        {
          request.getSession().setAttribute( "recognitionOptionsForm", null );
        }
      }

    }
    recOptionsForm.setImportFileId( String.valueOf( importFile.getId() ) );
    recOptionsForm.setImportFileType( importFile.getFileType().getCode() );
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
    RecognitionOptionsForm recoOptionsForm = (RecognitionOptionsForm)form;

    if ( isCancelled( request ) )
    {
      if ( !StringUtils.isEmpty( recoOptionsForm.getImportFileId() ) )
      {

        importFile = getImportService().getImportFile( new Long( recoOptionsForm.getImportFileId() ), new ImportFileAssociationRequest() );
      }
      if ( request.getSession().getAttribute( "recOptionsImportFile" ) == null )
      {
        request.getSession().setAttribute( "recOptionsImportFile", importFile );
      }
      return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    }

    if ( !StringUtils.isEmpty( recoOptionsForm.getImportFileId() ) )
    {

      importFile = getImportService().getImportFile( new Long( recoOptionsForm.getImportFileId() ), new ImportFileAssociationRequest() );
    }
    populateDomainWithCardOrCertificate( importFile, recoOptionsForm );
    if ( !StringUtils.isEmpty( recoOptionsForm.getSubmitterId() ) )
    {
      importFile.setSubmitter( getParticipantService().getParticipantById( new Long( recoOptionsForm.getSubmitterId() ) ) );

      List<DepositImportRecord> records = getImportService().getAllRecords( importFile.getId(), ImportFileTypeType.DEPOSIT, null );
      for ( DepositImportRecord importRecord : records )
      {
        if ( importRecord != null && !StringUtils.isEmpty( importRecord.getUserName() ) )
        {
          Participant pax = getParticipantService().getParticipantByUserName( importRecord.getUserName() );
          Promotion promotion = getPromotionService().getPromotionById( new Long( recoOptionsForm.getPromotionId() ) );
          if ( pax != null && pax.getId().equals( Long.valueOf( recoOptionsForm.getSubmitterId() ) )
              && ( promotion.isRecognitionPromotion() && ! ( (RecognitionPromotion)promotion ).isSelfRecognitionEnabled() ) )
          {
            ActionMessages errors = new ActionMessages();
            errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.fileload.common.SENDER_AND_GIVER_CANNOT_BE_SAME" ) );
            saveErrors( request, errors );
            recoOptionsForm.setImportFileId( String.valueOf( importFile.getId() ) );
            if ( !StringUtils.isEmpty( recoOptionsForm.getSubmitterNodeId() ) )
            {
              importFile.setSubmitterNode( getNodeService().getNodeById( new Long( recoOptionsForm.getSubmitterNodeId() ) ) );
            }
            recoOptionsForm.setImportFileType( importFile.getFileType().getCode() );
            request.getSession().setAttribute( "recOptionsImportFile", importFile );
            return mapping.findForward( "create" );
          }
        }
      }
    }
    if ( !StringUtils.isEmpty( recoOptionsForm.getSubmitterNodeId() ) )
    {
      importFile.setSubmitterNode( getNodeService().getNodeById( new Long( recoOptionsForm.getSubmitterNodeId() ) ) );
    }
    if ( !StringUtils.isEmpty( recoOptionsForm.getPromotionId() ) )
    {
      Promotion promotion = getPromotionService().getPromotionById( new Long( recoOptionsForm.getPromotionId() ) );
      if ( promotion.isRecognitionPromotion() )
      {
        importFile.setRecognitionDeposit( new Boolean( true ) );
      }
      else
      {
        importFile.setRecognitionDeposit( new Boolean( false ) );
      }
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
          importFile.setCopyManager( recoOptionsForm.isCopyManager() );
        }
      }
    }
    if ( !StringUtils.isEmpty( recoOptionsForm.getComments() ) )
    {
      importFile.setSubmitterComments( recoOptionsForm.getComments() );
    }

    if ( !StringUtils.isEmpty( recoOptionsForm.getSelectedBehavior() ) )
    {
      importFile.setBehavior( recoOptionsForm.getSelectedBehavior() );
    }

    try
    {
      request.getSession().setAttribute( "recOptionsImportFile", importFile );
    }
    catch( Exception e )
    {
      log.warn( e.getMessage() );
    }
    getImportService().saveRecognitionOptions( importFile );
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
    ImportFileAssociationRequest importFileAssociationRequest = new ImportFileAssociationRequest();
    return importService.getImportFile( importFileId, importFileAssociationRequest );
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
   * @param recognitionOptionsForm
   * @return ImportFile
   */
  public ImportFile populateDomainWithCardOrCertificate( ImportFile importFile, RecognitionOptionsForm recognitionOptionsForm )
  {
    if ( StringUtils.isEmpty( recognitionOptionsForm.getCardId() ) )
    {
      return importFile;
    }
    if ( recognitionOptionsForm.getCardId().startsWith( CARD_PREFIX ) )
    {
      long selectedCardId = Long.parseLong( recognitionOptionsForm.getCardId().substring( CARD_PREFIX.length() ) );
      if ( selectedCardId > -1 )
      {
        importFile.setCard( new Long( selectedCardId ) );
      }
    }

    else if ( recognitionOptionsForm.getCardId().startsWith( CERTIFICATE_PREFIX ) )
    {
      String selectedCertificateId = recognitionOptionsForm.getCardId().substring( CERTIFICATE_PREFIX.length() );
      // PromotionCertificateFormBean certificate = new PromotionCertificateFormBean();
      if ( !StringUtils.isEmpty( selectedCertificateId ) )
      {
        importFile.setCertificateId( new Long( selectedCertificateId ) );
      }

    }
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

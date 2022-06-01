
package com.biperf.core.ui.recognition;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.json.JSONArray;
import org.json.JSONObject;

import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.CustomerInformationBlock;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.claim.ClaimElementForm;
import com.biperf.core.ui.recognition.purl.PresetSearchFiltersBean;
import com.biperf.core.ui.recognition.state.RecipientBean;
import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.utils.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ValidateAction extends BaseRecognitionAction
{
  private static final Log logger = LogFactory.getLog( ValidateAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SendRecognitionForm form = (SendRecognitionForm)actionForm;
    Promotion promotion = getPromotionService().getPromotionById( form.getPromotionId() );
    if ( StringUtils.isEmpty( form.getPromotionType() ) )
    {
      form.setPromotionType( promotion.getPromotionType().getCode() );
    }

    RecognitionClaimSubmission submission = RecognitionClaimSubmissionFactory.buildFrom( RecognitionClaimSource.WEB, form );
    form.setNewClaimElementsList( submission.getClaimElements() );

    try
    {
      RecognitionClaimSubmissionResponse validation = getClaimService().validate( submission );
      ActionErrors actionErrors = new ActionErrors();
      validateAddressBlock( actionErrors, form );
      if ( !validation.isSuccess() )
      {
        if ( promotion.isRecognitionPromotion() )
        {
          RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
          if ( recognitionPromotion.isIncludePurl() )
          {
            RecipientBean recipientBean = buildRecipientBean( form );
            form.setClaimRecipientFormBeansForPurl( 0, recipientBean );
            JSONArray nodesArray = new JSONArray( recipientBean.getNodes() );
            JSONObject nodeObject = nodesArray.getJSONObject( 0 );
            List<Node> childNodes = getNodeService().getNodeAndNodesBelow( nodeObject.getLong( "id" ) );
            PresetSearchFiltersBean bean = new PresetSearchFiltersBean( childNodes,
                                                                        CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.ADD_TEAM_MEMBERS" ),
                                                                        CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.SELECT_TEAM" ) );
            ObjectMapper mapper = new ObjectMapper();
            Writer writer = new StringWriter();
            try
            {
              mapper.writeValue( writer, bean );
            }
            catch( Throwable t )
            {
              log.error( "\n\n\nERROR!!!\n\n\n" + t.getMessage() );
            }
            form.setContributorTeamsSearchFilters( writer.toString() );
          }
        }
        List<ServiceError> serviceErrors = validation.getErrors();
        for ( Object obj : serviceErrors )
        {
          ServiceError error = (ServiceError)obj;
          String errorMessage = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
          if ( StringUtils.isNotEmpty( error.getArg1() ) )
          {
            errorMessage = errorMessage.replace( "{0}", error.getArg1() );
          }
          if ( StringUtils.isNotEmpty( error.getArg2() ) )
          {
            errorMessage = errorMessage.replace( "{1}", error.getArg2() );
          }
          if ( StringUtils.isNotEmpty( error.getArg3() ) )
          {
            errorMessage = errorMessage.replace( "{2}", error.getArg3() );
          }
          errorMessage = errorMessage.replace( "???", "" );
          errorMessage = errorMessage.replace( "???", "" );
          error.setArg1( errorMessage );
        }
        request.setAttribute( "submitRecognitionValidationErrors", serviceErrors );
        RecognitionStateManager.store( form, request );
        return mapping.findForward( "failure" );
      }
    }
    catch( ServiceErrorException t )
    {
      logger.error( "\n\n****************************\nERROR in " + this.getClass().getName() + "#execute\n****************************\n\n", t );
      ActionMessages errors = new ActionMessages();
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) );
      saveErrors( request, errors );

      return mapping.findForward( "failure" );
    }
    catch( Throwable e )
    {
      logger.error( "\n\n****************\nERROR in " + getClass().getName() + "#execute\n****************************\n\n", e );

      ActionMessages errors = new ActionMessages();
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION ) );
      saveErrors( request, errors );

      return mapping.findForward( "failure" );
    }

    // put the state in session
    RecognitionStateManager.store( form, request );

    // if there are no errors, store the edited card (what the user drew)
    storeUserEditedCard( form );

    // PURL promotions don't have a preview page, so if it's preview,
    // go to submit it and bypass the preview page.
    if ( promotion instanceof RecognitionPromotion )
    {
      RecognitionPromotion rp = (RecognitionPromotion)promotion;
      if ( rp.isIncludePurl() )
      {
        return mapping.findForward( "submit" );
      }
    }

    ActionRedirect redirect = new ActionRedirect( mapping.findForward( "success" ) );
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() && !Objects.isNull( request.getParameter( "isRARecognitionFlow" ) )
        && request.getParameter( "isRARecognitionFlow" ).toString().equals( "yes" ) )
    {
      String reporteeId = request.getParameter( "reporteeId" );
      redirect.addParameter( "isRARecognitionFlow", "yes" );
      redirect.addParameter( "reporteeId", reporteeId );

    }

    return redirect;
  }

  private void validateAddressBlock( ActionErrors actionErrors, SendRecognitionForm form )
  {
    List<ClaimElementForm> sortedList = form.getSortedClaimElementsList( form.getClaimElementsList() );
    for ( Iterator<ClaimElementForm> elementsIterator = sortedList.iterator(); elementsIterator.hasNext(); )
    {
      ClaimElementForm claimElementForm = elementsIterator.next();
      ClaimFormStepElement claimFormStepElement = claimElementForm.getClaimFormStepElement();

      // validate address fields
      if ( claimFormStepElement.getClaimFormElementType().isAddressBlock() )
      {
        if ( CustomerInformationBlock.MAIN_ADDRESS_1_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() )
            || CustomerInformationBlock.ADDITIONAL_ADDRESS_2_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() ) )
        {
          claimElementForm.getMainAddressFormBean().validateAddress( actionErrors );
        }
      }
    }
  }

  private RecipientBean buildRecipientBean( SendRecognitionForm recognitionForm )
  {
    RecipientBean recipientBean = null;
    for ( int currentIndex = 0; currentIndex < recognitionForm.getClaimRecipientFormBeansCount(); currentIndex++ )
    {
      recipientBean = recognitionForm.getClaimRecipientFormBeans( currentIndex );
      if ( recipientBean != null && !StringUtil.isNullOrEmpty( recipientBean.getNodes() ) )
      {
        return recipientBean;
      }
    }
    return recipientBean;
  }

}

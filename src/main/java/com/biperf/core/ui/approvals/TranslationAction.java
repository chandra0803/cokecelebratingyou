
package com.biperf.core.ui.approvals;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.TranslatedContent;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.translation.UnexpectedTranslationException;
import com.biperf.core.service.translation.UnsupportedTranslationException;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.ContentReaderManager;

public class TranslationAction extends BaseDispatchAction
{

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final Long RECOGNITION_ID = getClaimIdFromClientState( request );
    TranslateResponse bean = null;
    try
    {
      TranslatedContent tc = getClaimService().getTranslatedRecognitionClaimSubmitterCommentFor( RECOGNITION_ID, UserManager.getUserId() );
      bean = new TranslateResponse( tc.getTranslatedContent() );
    }
    catch( UnsupportedTranslationException ute )
    {
      bean = new TranslateResponse( ContentReaderManager.getText( "recognition.approval.details", "TRANSLATION_UNAVAILABLE" ) );
    }
    catch( UnexpectedTranslationException ex )
    {
      bean = new TranslateResponse( ContentReaderManager.getText( "recognition.approval.details", "TRANSLATION_UNAVAILABLE" ) );
    }

    super.writeAsJsonToResponse( bean, response );
    return null;
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)BeanLocator.getBean( ClaimService.BEAN_NAME );
  }

  @SuppressWarnings( "unchecked" )
  private static Long getClaimIdFromClientState( HttpServletRequest request )
  {
    Long claimId = null;
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
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      // Get the claim.
      // nomination uses approvableId while recognition uses claimId
      Object claimIdParam = clientStateMap.containsKey( "approvableId" ) ? clientStateMap.get( "approvableId" ) : clientStateMap.get( "claimId" );
      if ( claimIdParam != null )
      {
        if ( claimIdParam instanceof String )
        {
          claimId = Long.valueOf( (String)claimIdParam );
        }
        else
        {
          claimId = (Long)claimIdParam;
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    return claimId;
  }

  private static final class TranslateResponse
  {
    private final String comment;

    public TranslateResponse( String comment )
    {
      this.comment = comment;
    }

    public String getComment()
    {
      return comment;
    }
  }
}

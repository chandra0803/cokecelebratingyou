
package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.ParticipantIdentifierType;
import com.biperf.core.domain.participant.ParticipantIdentifier;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.ParticipantActivationService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;

public class ParticipantActivationAction extends BaseDispatchAction
{
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantActivationForm paxActivationForm = (ParticipantActivationForm)form;
    paxActivationForm.setParticipantIdentifierBeans( buildParticipantIdentifierBeans() );
    paxActivationForm.setUserCharacteristics( getUserCharacteristicService().getAvailbleParticipantIdentifierCharacteristics() );

    return mapping.findForward( "success" );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    ParticipantActivationForm paxActivationForm = (ParticipantActivationForm)form;
    ActionMessages errors = validateSave( paxActivationForm );
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    getParticipantActivationService().save( buildParticipantIdentifiers( paxActivationForm ) );
    return display( mapping, form, request, response );
  }

  private ActionMessages validateSave( ParticipantActivationForm paxActivationForm )
  {
    ActionMessages errors = new ActionMessages();
    int count = 0;
    for ( ParticipantIdentifierBean bean : paxActivationForm.getParticipantIdentifierBeans() )
    {
      if ( bean.isSelected() )
      {
        count++;
      }
      if ( StringUtils.isEmpty( bean.getLabel() ) )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "Label for " + bean.getName() ) );
      }
    }
    if ( count == 0 )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_EMPTY, "Must have at least one Identifier active" ) );
    }
    return errors;
  }

  public ActionForward addCharacteristic( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    ParticipantActivationForm paxActivationForm = (ParticipantActivationForm)form;
    ParticipantIdentifier pi = new ParticipantIdentifier();
    pi.setCharacteristic( getUserCharacteristicService().getCharacteristicById( paxActivationForm.getAddUserCharacteristic() ) );
    getParticipantActivationService().save( pi );
    paxActivationForm.getParticipantIdentifierBeans().add( new ParticipantIdentifierBean( pi ) );
    return display( mapping, form, request, response );
  }

  private List<ParticipantIdentifierBean> buildParticipantIdentifierBeans()
  {
    List<ParticipantIdentifierBean> paxIdentBeans = new ArrayList<ParticipantIdentifierBean>();
    List<ParticipantIdentifier> paxIdents = getParticipantActivationService().getParticipantIdentifiers();
    paxIdents.forEach( p -> paxIdentBeans.add( new ParticipantIdentifierBean( p ) ) );
    paxIdentBeans.sort( ( o1, o2 ) -> o1.getName().toLowerCase().compareTo( o2.getName().toLowerCase() ) );
    return paxIdentBeans;
  }

  public List<ParticipantIdentifier> buildParticipantIdentifiers( ParticipantActivationForm paxActivationForm )
  {
    List<ParticipantIdentifier> pis = new ArrayList<ParticipantIdentifier>();
    paxActivationForm.getParticipantIdentifierBeans().stream().forEach( p -> pis.add( buildParticipantIdentifier( p ) ) );
    return pis;
  }

  private ParticipantIdentifier buildParticipantIdentifier( ParticipantIdentifierBean p )
  {
    ParticipantIdentifier pi = new ParticipantIdentifier();
    pi.setId( p.getParticipantIdentifierId() );
    pi.setSelected( p.isSelected() );
    pi.setDescription( p.getDescription() );
    pi.setLabel( p.getLabel() );
    if ( p.isUserCharacteristic() )
    {
      pi.setCharacteristic( getUserCharacteristicService().getCharacteristicById( p.getCharacteristicId() ) );
    }
    else
    {
      pi.setParticipantIdentifierType( ParticipantIdentifierType.lookup( p.getType() ) );
    }
    return pi;
  }

  private ParticipantActivationService getParticipantActivationService()
  {
    return (ParticipantActivationService)getService( ParticipantActivationService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }
}

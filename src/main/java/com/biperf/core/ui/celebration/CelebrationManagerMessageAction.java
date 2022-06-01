
package com.biperf.core.ui.celebration;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.celebration.CelebrationManagerMessageAssociationRequest;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PickListValueBean;

public class CelebrationManagerMessageAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( CelebrationRecognitionPurlAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    CelebrationManagerMessageForm managerMessageForm = (CelebrationManagerMessageForm)form;
    try
    {

      Long managerMessageId = getManagerMessageId( request );
      if ( managerMessageId != null )
      {
        // managerMessage
        CelebrationManagerMessage managerMessage = loadManagerMessage( managerMessageId, managerMessageForm );

        // recipient
        Participant recipient = managerMessage.getRecipient();
        if ( recipient != null )
        {
          loadRecipientInfo( managerMessageForm, recipient, managerMessage );
        }

        // manager
        User manager = loadManagerInfo( managerMessageForm, managerMessage );

        // Bug Fix 65398 is addressed by Bug Fix 66698
        // Removing some part of Bug fix 65398 for Bug fix 66760 in this class

        /*
         * Date messageCollectExpirationDate = managerMessage.getMsgCollectExpireDate(); if(
         * (manager != null && UserManager.getUser().getUserId().equals(manager.getId())) &&
         * (messageCollectExpirationDate.after(new Date())))
         */

        if ( manager != null && UserManager.getUser().getUserId().equals( manager.getId() ) )
        {
          return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
        }
        else
        {
          return mapping.findForward( ActionConstants.CANCEL_TO_HOMEPAGE );
        }
        // End Bug Fix

      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    catch( Exception e )
    {
      logger.error( "Error displaying celebration manager message screen : " + e );
    }
    // if errors not empty
    return mapping.findForward( ActionConstants.FAIL_FORWARD );
  }

  public ActionForward saveManagerMessage( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    CelebrationManagerMessageForm managerMessageForm = (CelebrationManagerMessageForm)form;
    Long managerMessageId = getManagerMessageId( request );
    if ( managerMessageId != null )
    {
      CelebrationManagerMessage managerMessage = getCelebrationService().getCelebrationManagerMessageById( managerMessageId );
      // find out who is this manager, manager or manageAbove
      if ( isLoggedInUserManagerAbove( managerMessage ) )
      {
        managerMessage.setManagerAboveMessage( managerMessageForm.getComments() );
      }
      else
      {
        managerMessage.setManagerMessage( managerMessageForm.getComments() );
      }
      try
      {
        getCelebrationService().saveCelebrationManagerMessage( managerMessage );
      }
      catch( Exception e )
      {
        log.error( e );
      }
    }

    return mapping.findForward( "save_forward" );
  }

  private CelebrationManagerMessage loadManagerMessage( Long managerMessageId, CelebrationManagerMessageForm managerMessageForm )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new CelebrationManagerMessageAssociationRequest( CelebrationManagerMessageAssociationRequest.CELEBRATION_MANAGER ) );
    associationRequestCollection.add( new CelebrationManagerMessageAssociationRequest( CelebrationManagerMessageAssociationRequest.CELEBRATION_MANAGER_ABOVE ) );
    CelebrationManagerMessage managerMessage = getCelebrationService().getCelebrationManagerMessageById( managerMessageId, associationRequestCollection );
    managerMessageForm.setManagerMessageId( managerMessage.getId() );
    return managerMessage;
  }

  private void loadRecipientInfo( CelebrationManagerMessageForm managerMessageForm, Participant recipient, CelebrationManagerMessage managerMessage )
  {
    AssociationRequestCollection paxAssociationRequestCollection = new AssociationRequestCollection();
    paxAssociationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    paxAssociationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant recipientParticipant = getParticipantService().getParticipantByIdWithAssociations( recipient.getId(), paxAssociationRequestCollection );

    String primaryCountryCode = recipientParticipant.getPrimaryCountryCode();
    managerMessageForm.setPrimaryCountryCode( primaryCountryCode );
    String primaryCountryName = recipientParticipant.getPrimaryCountryName();
    managerMessageForm.setPrimaryCountryName( primaryCountryName );

    ParticipantEmployer currentEmployer = getParticipantService().getCurrentParticipantEmployer( recipientParticipant );
    PickListValueBean pickListDeptValueBean = getUserService().getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                           currentEmployer.getParticipant().getLanguageType() == null
                                                                                               ? UserManager.getDefaultLocale().toString()
                                                                                               : currentEmployer.getParticipant().getLanguageType().getCode(),
                                                                                           currentEmployer.getDepartmentType() );
    String departmentName = currentEmployer.getDepartmentType() != null ? pickListDeptValueBean.getName() : "";
    managerMessageForm.setDepartment( departmentName );
    PickListValueBean pickListPositionValueBean = getUserService().getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                                                                               currentEmployer.getParticipant().getLanguageType() == null
                                                                                                   ? UserManager.getDefaultLocale().toString()
                                                                                                   : currentEmployer.getParticipant().getLanguageType().getCode(),
                                                                                               currentEmployer.getPositionType() );
    String jobTitle = currentEmployer.getPositionType() != null ? pickListPositionValueBean.getName() : "";
    managerMessageForm.setJobTitle( jobTitle );

    managerMessageForm.setFirstName( recipient.getFirstName() );
    managerMessageForm.setLastName( recipient.getLastName() );

    RecognitionPromotion promotion = managerMessage.getPromotion();
    managerMessageForm.setPromotionName( promotion.getName() );

    if ( promotion.isServiceAnniversary() )
    {
      managerMessageForm.setServiceAnniversaryEnabed( true );
      if ( promotion.getAnniversaryInYears() )
      {
        managerMessageForm.setAnniversaryInYears( true );
        managerMessageForm.setAnniversaryNumberOfYearsOrDays( managerMessage.getAnniversaryNumberOfYears() );
      }
      else
      {
        managerMessageForm.setAnniversaryInYears( false );
        managerMessageForm.setAnniversaryNumberOfYearsOrDays( managerMessage.getAnniversaryNumberOfDays() );
      }
    }
    else
    {
      managerMessageForm.setServiceAnniversaryEnabed( false );
    }
    managerMessageForm.setAvatarUrl( recipient.getAvatarSmallFullPath( null ) );
    UserNode userNode = getUserService().getPrimaryUserNode( recipient.getId() );
    managerMessageForm.setOrgName( userNode.getNode().getName() );
  }

  private User loadManagerInfo( CelebrationManagerMessageForm managerMessageForm, CelebrationManagerMessage managerMessage )
  {
    User manager = null;
    if ( isLoggedInUserManagerAbove( managerMessage ) )
    {
      manager = managerMessage.getManagerAbove();
    }
    else
    {
      manager = managerMessage.getManager();
    }
    // Bug Fix 65398
    if ( manager != null && UserManager.getUser().getUserId().equals( manager.getId() ) )
    {
      String managerName = manager.getFirstName() + " " + manager.getLastName();
      managerMessageForm.setManagerName( managerName );
      managerMessageForm.setManagerFirstName( manager.getFirstName() );
      managerMessageForm.setManagerLastName( manager.getLastName() );
      Participant managerPax = getParticipantService().getParticipantById( manager.getId() );
      String managerAvatarUrl = managerPax.getAvatarSmallFullPath( null );
      managerMessageForm.setManagerAvatarUrl( managerAvatarUrl );
    }

    return manager;
  }

  private boolean isLoggedInUserManagerAbove( CelebrationManagerMessage managerMessage )
  {
    if ( managerMessage.getManagerAbove() != null && UserManager.getUserId().equals( managerMessage.getManagerAbove().getId() ) )
    {
      return true;
    }
    return false;
  }

  public Long getManagerMessageId( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsLong( request, "managerMessageId" );
  }

  public Long getClientStateParameterValueAsLong( HttpServletRequest request, String parameter ) throws InvalidClientStateException
  {
    Object paramValue = getClientStateParameterValue( request, parameter );
    Long parameterValue = null;
    try
    {
      parameterValue = (Long)paramValue;
    }
    catch( ClassCastException cce )
    {
      parameterValue = new Long( (String)paramValue );
    }
    return parameterValue;
  }

  public Object getClientStateParameterValue( HttpServletRequest request, String parameter ) throws InvalidClientStateException
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
    return clientStateMap.get( parameter );
  }

  private CelebrationService getCelebrationService()
  {
    return (CelebrationService)getService( CelebrationService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}

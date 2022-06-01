
package com.biperf.core.ui.claim;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.recognition.PromotionNodeCheckAction;
import com.biperf.core.ui.recognition.PromotionNodeCheckBean;
import com.biperf.core.ui.recognition.PromotionNodeCheckBean.Node;
import com.biperf.core.ui.recognition.state.RecipientBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.RecognitionBean;

public class ManagerAddPointsAction extends RecognitionDetailAction
{
  private static final String DISPLAY_INACTIVE_PAGE = "inactive";

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    final ManagerAddPointsForm FORM = (ManagerAddPointsForm)actionForm;

    ManagerAddPointsState params = getClientStateParameters( request, FORM );

    final Long CLAIM_ID = params.getClaimId();
    final Long RECIPIENT_ID = params.getRecipientId();
    final Long SUBMITTER_ID = UserManager.getUserId();
    final Long PROMOTION_ID = params.getPromotionId();
    final Long SUBMITTER_NODE_ID = params.getManagerNodeId();
    final Long PROMOTION_ID1 = params.getManagerPromotionId();

    Promotion promotion = getPromotionInfo( PROMOTION_ID, PROMOTION_ID1, FORM );

    Participant recipient = getRecipientInfo( FORM, PROMOTION_ID, SUBMITTER_ID, RECIPIENT_ID, SUBMITTER_NODE_ID );

    boolean isParticipantInPromotionAudience = getAudienceService().isParticipantInSecondaryAudience( promotion, recipient );

    if ( !isParticipantInPromotionAudience || !recipient.isActive() )
    {
      if ( !recipient.isActive() )
      {
        request.setAttribute( "isRecipientInActive", Boolean.TRUE );
      }
      else if ( !isParticipantInPromotionAudience )
      {
        request.setAttribute( "participantNotInPromotion", Boolean.TRUE );
      }
      return mapping.findForward( DISPLAY_INACTIVE_PAGE );
    }

    if ( isParticipantInPromotionAudience && recipient.isActive() )
    {
      RecognitionDetailBean bean = getRecognitionDetailBean( request, CLAIM_ID, true );
      FORM.setDetailBean( bean );

      getSubmitterInfo( SUBMITTER_ID, FORM );

      List<NameableBean> nodes = FORM.getSubmitterInfo().getNodes();
      getNodesBudgetInfo( nodes, PROMOTION_ID1, SUBMITTER_ID, FORM );

      FORM.setSubmitterNodeId( SUBMITTER_NODE_ID );

      // save that info in session
      request.getSession().setAttribute( ManagerAddPointsForm.SESSION_KEY, FORM );
    }
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private Participant getRecipientInfo( ManagerAddPointsForm form, Long promotionId, Long submitterId, Long recipientId, Long recipientNodeId )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );

    Participant recipient = getParticipantService().getParticipantByIdWithAssociations( recipientId, arc );

    // create the bean
    ParticipantInfoView view = new ParticipantInfoView( recipient );
    view.setCountryCode( recipient.getPrimaryCountryCode() );
    view.setCountryName( recipient.getPrimaryCountryName() );

    // calculate the "real" country ratio
    view.setCountryRatio( BudgetUtils.getBudgetConversionRatio( getUserService(), getPromotionService(), promotionId, recipientId, submitterId ).doubleValue() );

    // set the view on the form
    form.setRecipientInfo( view );

    // populate the claimRecipientFormBean from the view
    RecipientBean bean = form.getClaimRecipientFormBeans( 0 );
    bean.setUserId( view.getId() );
    bean.setAwardQuantity( new Long( 0 ) );
    bean.setCountryCode( view.getCountryCode() );
    bean.setCountryRatio( view.getCountryRatio() );
    bean.setCountryName( view.getCountryName() );
    bean.setFirstName( view.getFirstName() );
    bean.setLastName( view.getLastName() );
    bean.setDepartmentName( view.getDepartmentName() );
    bean.setJobName( view.getJobName() );
    bean.setNodeId( recipientNodeId );
    bean.setOptOutAwards( recipient.getOptOutAwards() );

    // email
    arc = new AssociationRequestCollection();
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
    Participant pax = getParticipantService().getParticipantByIdWithAssociations( recipientId, arc );

    if ( pax.getPrimaryEmailAddress() != null )
    {
      bean.setEmailAddr( pax.getPrimaryEmailAddress().getEmailAddr() );
    }
    return recipient;
  }

  private void getSubmitterInfo( Long submitterId, ManagerAddPointsForm form )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );

    Participant submitter = getParticipantService().getParticipantByIdWithAssociations( submitterId, arc );

    // create the bean
    ParticipantInfoView view = new ParticipantInfoView( submitter );
    view.setCountryCode( submitter.getPrimaryCountryCode() );
    view.setCountryName( submitter.getPrimaryCountryName() );

    // set it on the form
    form.setSubmitterInfo( view );
  }

  private void getNodeBudgetInfo( Long nodeId, Long promotionId, Long submitterId, ManagerAddPointsForm form )
  {
    PromotionNodeCheckBean bean = new PromotionNodeCheckBean();

    PromotionNodeCheckAction.getAvailableBudget( promotionId, submitterId, nodeId, bean );

    form.setNodeBudgetJson( toJson( bean.getNode() ) );
  }

  private void getNodesBudgetInfo( List<NameableBean> submitterNodes, Long promotionId, Long submitterId, ManagerAddPointsForm form )
  {
    List<Node> nodes = new ArrayList<Node>();
    for ( NameableBean node : submitterNodes )
    {
      PromotionNodeCheckBean bean = new PromotionNodeCheckBean();

      PromotionNodeCheckAction.getAvailableBudget( promotionId, submitterId, node.getId(), bean );
      nodes.add( bean.getNode() );
    }
    form.setNodeBudgetJson( toJson( nodes ) );
  }

  private Promotion getPromotionInfo( Long promotionId, Long promotionId1, ManagerAddPointsForm form )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL ) );

    AbstractRecognitionPromotion arp = (AbstractRecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, arc );
    AbstractRecognitionPromotion arp1 = (AbstractRecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId1, arc );
    RecognitionBean rb = new RecognitionBean( arp );
    RecognitionBean rb1 = new RecognitionBean( arp1 );

    // populate the form
    form.setPromotionId( promotionId );
    form.setAwardType( rb1.getAwardType() );
    form.setAwardFixed( rb1.getAwardFixed() );
    form.setAwardMin( rb1.getAwardMin() );
    form.setAwardMax( rb1.getAwardMax() );
    form.setPromotionName( rb.getName() );

    form.setMgrPromotionId( promotionId1 );
    form.setMgrPromotionName( rb1.getName() );
    return arp;
  }

  private ManagerAddPointsState getClientStateParameters( HttpServletRequest request, ManagerAddPointsForm form )
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );

    ManagerAddPointsState params = new ManagerAddPointsState();
    form.setClientState( clientState, params );

    return params;
  }

  protected ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  protected UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  public static class ManagerAddPointsState implements Serializable
  {
    private Long promotionId;
    private Long managerPromotionId;
    private Long recipientId;
    private Long claimId;
    private Long managerNodeId;

    public Long getPromotionId()
    {
      return promotionId;
    }

    public void setPromotionId( Long promotionId )
    {
      this.promotionId = promotionId;
    }

    public Long getManagerPromotionId()
    {
      return managerPromotionId;
    }

    public void setManagerPromotionId( Long managerPromotionId )
    {
      this.managerPromotionId = managerPromotionId;
    }

    public Long getRecipientId()
    {
      return recipientId;
    }

    public void setRecipientId( Long recipientId )
    {
      this.recipientId = recipientId;
    }

    public Long getClaimId()
    {
      return claimId;
    }

    public void setClaimId( Long claimId )
    {
      this.claimId = claimId;
    }

    public Long getManagerNodeId()
    {
      return managerNodeId;
    }

    public void setManagerNodeId( Long managerNodeId )
    {
      this.managerNodeId = managerNodeId;
    }
  }
}

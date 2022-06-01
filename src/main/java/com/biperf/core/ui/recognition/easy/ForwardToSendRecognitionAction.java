
package com.biperf.core.ui.recognition.easy;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.recognition.SendRecognitionForm;
import com.biperf.core.ui.recognition.purl.PresetSearchFiltersBean;
import com.biperf.core.ui.recognition.state.RecipientBean;
import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.NameableBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ForwardToSendRecognitionAction extends BaseRecognitionAction
{
  private static final Log logger = LogFactory.getLog( ForwardToSendRecognitionAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SendRecognitionForm state = (SendRecognitionForm)actionForm;

    Long promotionId = state.getPromotionId();
    Promotion promotion = getPromotionService().getPromotionById( promotionId );
    state.setPromotionType( promotion.getPromotionType().getCode() );

    // populate the recipient information
    populateRecipientInfo( state, promotion );

    // populate purl contributors
    populatePurlContributorSearchFilter( state, promotion );

    RecognitionStateManager.store( state, request );

    return mapping.findForward( "success" );
  }

  private void populateRecipientInfo( SendRecognitionForm state, Promotion promotion )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
    arc.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
    arc.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );

    Long recipientId = state.getClaimRecipientFormBeans( 0 ).getUserId();
    Long recipientNodeId = state.getClaimRecipientFormBeans( 0 ).getNodeId();
    Participant pax = getParticipantService().getParticipantByIdWithAssociations( recipientId, arc );

    String recipientNodeName = "";
    String nodes = "";
    if ( recipientNodeId != null && null != pax.getUserNodeByNodeId( recipientNodeId ) )
    {
      recipientNodeName = pax.getUserNodeByNodeId( recipientNodeId ).getNode().getName();
      List<NameableBean> beans = new ArrayList<NameableBean>();
      beans.add( new NameableBean( recipientNodeId, recipientNodeName ) );
      nodes = toJson( beans ).toString();
    }

    RecipientBean recipientBean = state.getClaimRecipientFormBeans( 0 );

    recipientBean.setUserId( pax.getId() );
    recipientBean.setFirstName( pax.getFirstName() );
    recipientBean.setLastName( pax.getLastName() );
    recipientBean.setCountryCode( pax.getPrimaryCountryCode() );
    recipientBean.setCountryName( pax.getPrimaryCountryName() );
    recipientBean.setNodeId( recipientNodeId );
    recipientBean.setJobName( pax.getPaxJobName() );
    recipientBean.setDepartmentName( pax.getPaxDeptName() );
    recipientBean.setNodes( nodes );
    boolean calculateBudgetRatio = promotion.getBudgetMaster() != null && !promotion.getBudgetMaster().isCentralBudget();
    recipientBean.setCountryRatio( calculateBudgetRatio ? BudgetUtils.getBudgetConversionRatio( getUserService(), recipientId, UserManager.getUserId() ).doubleValue() : 1 );
    recipientBean.setOptOutAwards( pax.getOptOutAwards() );

    if ( pax.getPrimaryEmailAddress() != null )
    {
      recipientBean.setEmailAddr( pax.getPrimaryEmailAddress().getEmailAddr() );
    }
    // Client customizations for wip #26532 starts
    recipientBean.setPurlAllowOutsideDomains( pax.isAllowSharePurlToOutsiders() );
    // Client customizations for wip #26532 ends

    // Client customizations for wip #42701 starts
    recipientBean.setCurrency( getUserService().getUserCurrencyCharValue( pax.getId() ) );
    // Client customizations for wip #42701 ends
    
    // if there is an award and it's fixed award, populate the award quantity
    if ( promotion.isAbstractRecognitionPromotion() )
    {
      AbstractRecognitionPromotion arp = (AbstractRecognitionPromotion)promotion;
      if ( arp.isAwardActive() && arp.isAwardAmountTypeFixed() )
      {
        if ( pax.getOptOutAwards() )
        {
          recipientBean.setAwardQuantity( 0L );
        }
        else
        {
          recipientBean.setAwardQuantity( arp.getAwardAmountFixed() );
        }
      }
    }
  }

  private void populatePurlContributorSearchFilter( SendRecognitionForm state, Promotion promotion )
  {
    if ( promotion instanceof RecognitionPromotion )
    {
      RecognitionPromotion rp = (RecognitionPromotion)promotion;
      if ( rp.isIncludePurl() )
      {
        List<Node> childNodes = getNodeService().getNodeAndNodesBelow( state.getClaimRecipientFormBeans( 0 ).getNodeId() );
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
          if ( logger.isDebugEnabled() )
          {
            logger.debug( "\n\n\nERROR!!!\n\n\n" + t.getMessage() );
          }
        }
        state.setContributorTeamsSearchFilters( writer.toString() );
      }
    }
  }
}

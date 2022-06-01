
package com.biperf.core.ui.recognition.easy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.threads.CallableFactory;
import com.biperf.core.value.RecognitionBean;

public class DisplayAction extends BaseRecognitionAction
{
  private static final Log logger = LogFactory.getLog( DisplayAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final Long RECIPIENT_ID = RequestUtils.getRequiredParamLong( request, REQUEST_PARAM_RECIPIENT_ID );
    final Long USER_ID = UserManager.getUserId();
    final boolean IS_USER_A_PARTICIPANT = UserManager.getUser().isParticipant();
    final EasyRecognitionBean bean = new EasyRecognitionBean();

    List<Callable<Object>> callables = new ArrayList<Callable<Object>>();

    callables.add( CallableFactory.createCallable( new Callable<Object>()
    {
      public Object call() throws Exception
      {
        getUserNodes( USER_ID, bean );
        return null;
      }
    } ) );

    callables.add( CallableFactory.createCallable( new Callable<Object>()
    {
      public Object call() throws Exception
      {
        getPromotionList( USER_ID, RECIPIENT_ID, IS_USER_A_PARTICIPANT, bean );
        return null;
      }
    } ) );

    callables.add( CallableFactory.createCallable( new Callable<Object>()
    {
      public Object call() throws Exception
      {
        getRecipientInfo( RECIPIENT_ID, bean );
        return null;
      }
    } ) );

    try
    {
      getExecutorService().invokeAll( callables );
    }
    catch( InterruptedException ie )
    {
      logger.error( "\n\nERROR in " + getClass().getName() + " when calling executorService.invokeAll: " + ie.toString() );
    }

    writeAsJsonToResponse( bean, response );

    return null;
  }

  private void getRecipientInfo( Long recipientId, EasyRecognitionBean bean )
  {
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
    arc.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant pax = getParticipantService().getParticipantByIdWithAssociations( recipientId, arc );
    bean.setRecipientInfo( pax );
    if ( bean.getRecipientInfo().size() > 0 )
    {
      bean.getRecipientInfo().get( 0 ).setCountryCode( pax.getPrimaryCountryCode() );
      bean.getRecipientInfo().get( 0 ).setCountryName( pax.getPrimaryCountryName() );
    }
  }

  private void getPromotionList( Long submitterId, Long recipientId, boolean isSubmitterAParticipant, EasyRecognitionBean bean )
  {
    List<RecognitionBean> easyPromotions = getPromotionService().getEasyRecognitionSubmissionList( submitterId, isSubmitterAParticipant, recipientId );

    // sort alphabetically...
    Collections.sort( easyPromotions, new Comparator<RecognitionBean>()
    {

      public int compare( RecognitionBean one, RecognitionBean two )
      {
        return one.getName().compareTo( two.getName() );
      }
    } );

    bean.setPromotions( easyPromotions );
  }

  private void getUserNodes( Long userId, EasyRecognitionBean bean )
  {
    Set<UserNode> userNodes = getUserService().getUserNodes( userId );
    List<UserNode> userNodesList = null;
    if ( userNodes != null && !userNodes.isEmpty() )
    {
      userNodesList = new ArrayList<UserNode>( userNodes );
    }
    bean.setUserNodes( userNodesList );
  }

}

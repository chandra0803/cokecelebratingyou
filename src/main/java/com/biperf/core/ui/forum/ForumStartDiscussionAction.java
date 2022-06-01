/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.forum.ForumDiscussion;
import com.biperf.core.domain.forum.ForumDiscussionTitleCheckView;
import com.biperf.core.domain.forum.ForumDiscussionTitleCheckView.Fields;
import com.biperf.core.domain.forum.ForumDiscussionTitleCheckView.Messages;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.forum.ForumDiscussionService;
import com.biperf.core.service.forum.ForumTopicService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.forum.ForumTopicValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author poddutur
 *
 */
public class ForumStartDiscussionAction extends BaseDispatchAction
{

  @SuppressWarnings( "unchecked" )
  public ActionForward createDiscussion( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws InvalidClientStateException
  {
    ForumStartDiscussionForm forumStartDiscussionForm = (ForumStartDiscussionForm)form;

    List<ForumTopicValueBean> forumTopicsList = new ArrayList<ForumTopicValueBean>();

    forumTopicsList = getForumTopicService().getAllTopicNamesForPax();

    for ( ForumTopicValueBean forumTopicValueBean : forumTopicsList )
    {
      forumTopicValueBean.setSelectedTopic( forumTopicValueBean.getTopicCmAssetCode() );
    }

    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
    Participant sessionUser = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), requestCollection );

    forumStartDiscussionForm.setAvatarURL( sessionUser.getAvatarSmallFullPath() );
    request.setAttribute( "forumTopicsList", forumTopicsList );

    return actionMapping.findForward( "display_start_discussion" );

  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancelled( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  public ActionForward submit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ForumStartDiscussionForm forumStartDiscussionForm = (ForumStartDiscussionForm)form;

    ForumDiscussion forumDiscussion = new ForumDiscussion();

    ForumTopic forumTopic = new ForumTopic();
    User user = new User();
    user.setId( UserManager.getUserId() );
    forumTopic.setId( forumStartDiscussionForm.getTopicId() );
    forumDiscussion.setDiscussionTitle( forumStartDiscussionForm.getTitle() );
    forumDiscussion.setDiscussionBody( forumStartDiscussionForm.getText() );
    forumDiscussion.setStatus( "A" );

    try
    {
      getForumDiscussionService().save( user, forumTopic, forumDiscussion );
      forumTopic = getForumTopicService().getTopicById( forumStartDiscussionForm.getTopicId() );
    }

    catch( Exception e )
    {
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    forumTopic.setTopicscmAsset( forumTopic.getTopicCmAssetCode() );

    Map paramMapExpand = new HashMap();
    paramMapExpand.put( "topicId", forumStartDiscussionForm.getTopicId() );
    paramMapExpand.put( "topicName", forumTopic.getTopicNameFromCM() );
    response.sendRedirect( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.FORUM_DISCUSSIONS_URL, paramMapExpand ) );

    return null;
  }

  public ActionForward isDiscussionNameExists( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String discussionName = request.getParameter( "selectedTitle" );
    Long topicId = null;

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    if ( StringUtils.isNotBlank( clientState ) )
    {
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      @SuppressWarnings( "unchecked" )
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      topicId = (Long)clientStateMap.get( "topicId" );
    }

    ForumDiscussionTitleCheckView forumDiscussionTitleCheckView = new ForumDiscussionTitleCheckView();
    Messages messages = new Messages();
    Fields fields = new Fields();

    boolean isDiscussionExists = getForumDiscussionService().isDiscussionNameExists( discussionName, topicId );
    if ( isDiscussionExists )
    {
      List<Messages> messagesList = new ArrayList<Messages>();
      List<Fields> fieldsList = new ArrayList<Fields>();

      fields.setName( "title" );
      fields.setText( CmsResourceBundle.getCmsBundle().getString( "forum.library.TITLE_TAKEN" ) );
      fieldsList.add( fields );

      messages.setType( "error" );
      messages.setCode( "validationError" );
      messages.setFields( fieldsList );
      messagesList.add( messages );

      forumDiscussionTitleCheckView.setMessages( messagesList );

      super.writeAsJsonToResponse( forumDiscussionTitleCheckView, response );
      return null;
    }
    super.writeAsJsonToResponse( forumDiscussionTitleCheckView, response );
    return null;
  }

  /**
   * Get the ForumTopicService from the beanLocator.
   * 
   * @return ForumTopicService
   */
  private ForumTopicService getForumTopicService()
  {
    return (ForumTopicService)getService( ForumTopicService.BEAN_NAME );
  }

  /**
   * Get the ForumDiscussionService from the beanLocator.
   * 
   * @return ForumDiscussionService
   */
  private ForumDiscussionService getForumDiscussionService()
  {
    return (ForumDiscussionService)getService( ForumDiscussionService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}

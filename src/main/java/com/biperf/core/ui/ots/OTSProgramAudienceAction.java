
package com.biperf.core.ui.ots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.enums.OTSProgramStatusType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.ots.ProgramAudience;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.ots.OTSProgramAssociationRequest;
import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ots.OTSProgramAudienceForm;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.promotion.PromotionAudienceForm;
import com.biperf.core.ui.promotion.PromotionBaseDispatchAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

public class OTSProgramAudienceAction extends PromotionBaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( OTSProgramAudienceAction.class );

  public static String SESSION_PROMO_AUDIENCE_FORM = "sessionPromoAudienceForm";

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    OTSProgramAudienceForm promoAudienceForm = (OTSProgramAudienceForm)form;
    Long programNumber = promoAudienceForm.getProgramNumber();
    if ( !Objects.isNull( programNumber ) )
    {
      OTSProgram program = getOTSProgramService().getOTSProgramByProgramNumber( programNumber );
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      OTSProgramAssociationRequest req = new OTSProgramAssociationRequest( OTSProgramAssociationRequest.PRIMARY_AUDIENCES );
      if ( !Objects.isNull( req ) )
      {
        associationRequestCollection.add( req );
        program = getOTSProgramService().getOTSProgramByIdWithAssociations( program.getId(), associationRequestCollection );
        promoAudienceForm.load( setPrimaryAndSecondaryAudiences( program ) );

      }
      else
      {
        forwardTo = "save";

      }

    }
    return mapping.findForward( forwardTo );
  }

  public ActionForward addActualAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    OTSProgramAudienceForm promoAudienceForm = (OTSProgramAudienceForm)form;

    // If there was no audience selected, then return an error
    if ( promoAudienceForm.getProgramAudienceId() == null || promoAudienceForm.getProgramAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    addProgramAudience( promoAudienceForm, request, OTSProgramAudienceForm.PRIMARY, new Long( promoAudienceForm.getProgramAudienceId() ) );

    request.setAttribute( "otsProgramAudienceForm", promoAudienceForm );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private int getNbrOfPaxsInCriteriaAudience( Audience audience )
  {
    /* Fix 24956 start */
    /*
     * original code return getPaxsInCriteriaAudience( audience ).size();
     */

    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    return getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true ).size();
  }

  private OTSProgram setPrimaryAndSecondaryAudiences( OTSProgram program )
  {
    // iterate audiences and set nbr of pax for criteria based audience
    if ( program.getAudiences() != null )
    {
      Set programAudienceSetWithPaxSize = new LinkedHashSet();

      Iterator programAudienceIterator = program.getAudiences().iterator();
      while ( programAudienceIterator.hasNext() )
      {
        Audience audience = (Audience)programAudienceIterator.next();
        audience.setSize( getNbrOfPaxsInCriteriaAudience( audience ) );

        ProgramAudience programAudience = new ProgramAudience();
        programAudience.setOtsProgram( program );
        programAudience.setAudience( audience );
        programAudienceSetWithPaxSize.add( programAudience );
      }
      program.setProgramAudience( programAudienceSetWithPaxSize );
    }

    return program;
  }

  private void addProgramAudience( OTSProgramAudienceForm form, HttpServletRequest request, String promoAudienceType, Long audienceId )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    if ( AudienceType.SPECIFIC_PAX_TYPE.equals( form.getAudienceType() ) || audience instanceof CriteriaAudience )
    {
      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      Long primaryHierarchyId = primaryHierarchy.getId();

      Set audiences = new LinkedHashSet();
      audiences.add( audience );
      List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );
      audience.setSize( paxFormattedValueList.size() );
    }

    // add the audience to the list
    form.addAudience( audience, promoAudienceType );

  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    ActionForward forward = null;
    ActionMessages errors = new ActionMessages();
    ActionMessages warnings = new ActionMessages();
    OTSProgramAudienceForm promoAudienceForm = (OTSProgramAudienceForm)form;
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new OTSProgramAssociationRequest( OTSProgramAssociationRequest.PRIMARY_AUDIENCES ) );
    OTSProgram program = getOTSProgramService().getOTSProgramByProgramNumber( new Long( promoAudienceForm.getProgramNumber() ) );
    if ( Objects.isNull( program ) )
    {
      program = new OTSProgram();

    }
    else
    {
      program.setId( program.getId() );
      program.setVersion( program.getVersion() );
    }
    program = getOTSProgramService().getOTSProgramByIdWithAssociations( program.getId(), associationRequestCollection );
    List<Audience> dbAudienceList = new ArrayList<Audience>( program.getAudiences() );
    List<Audience> formAudienceList = new ArrayList<Audience>( promoAudienceForm.getProgramAudienceAsList() );

    program = promoAudienceForm.toDomainObject( program );

    program.setProgramStatus( OTSProgramStatusType.lookup( OTSProgramStatusType.COMPLETED ) );

    getOTSProgramService().save( program );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward prepareSubmitterAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    OTSProgramAudienceForm promotionAudienceForm = (OTSProgramAudienceForm)form;

    String promotionId = promotionAudienceForm.getProgramNumber().toString();

    ActionForward returnForward = mapping.findForward( "submitterAudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "programNumber", promotionId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );
    Map parmMap = new HashMap();
    parmMap.put( "saveAudienceReturnUrl", returnUrl );
    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;
    queryString2 = ClientStateUtils.generateEncodedLink( "", "", parmMap );
    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PROMO_AUDIENCE_FORM, promotionAudienceForm );

    return ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );
  }

  public ActionForward returnSubmitterAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    OTSProgramAudienceForm promotionAudienceForm = (OTSProgramAudienceForm)form;

    // Get the form back out of the Session to redisplay.
    OTSProgramAudienceForm sessionPromoAudienceForm = (OTSProgramAudienceForm)request.getSession().getAttribute( SESSION_PROMO_AUDIENCE_FORM );

    if ( sessionPromoAudienceForm != null )
    {
      String formAudienceType = PromotionAudienceForm.PRIMARY;
      extractAndAddPromotionAudience( request, promotionAudienceForm, sessionPromoAudienceForm, formAudienceType );
    }

    request.getSession().removeAttribute( SESSION_PROMO_AUDIENCE_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void extractAndAddPromotionAudience( HttpServletRequest request, OTSProgramAudienceForm promotionAudienceForm, OTSProgramAudienceForm sessionPromoAudienceForm, String formAudienceType )
  {
    try
    {
      BeanUtils.copyProperties( promotionAudienceForm, sessionPromoAudienceForm );
    }
    catch( Exception e )
    {
      logger.info( "returnCategoryLookup: Copy Properties failed." );
    }
    Long audienceId = null;
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
      audienceId = (Long)clientStateMap.get( "audienceId" );
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since this is an option parameter
    }

    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addProgramAudience( promotionAudienceForm, request, formAudienceType, audienceId );
    }
  }

  public ActionForward removeSubmitterAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorExceptionWithRollback
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    OTSProgramAudienceForm promotionAudienceForm = (OTSProgramAudienceForm)form;

    // Save a copy of the parent promotion's primary audience list.
    List primaryAudienceList = new ArrayList();
    primaryAudienceList.addAll( promotionAudienceForm.getProgramAudienceAsList() );

    // Remove audiences from the primary audience list.
    promotionAudienceForm.removeItems( PromotionAudienceForm.PRIMARY );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  private boolean isAudienceUpdated( List<Audience> dbAudienceList, List<Audience> formAudienceList, String dbAudienceType, String formAudienceType )
  {
    // If the audience type is changed
    if ( !dbAudienceType.equals( formAudienceType ) )
    {
      return true;
    }

    List<Long> dbAudienceIds = new ArrayList<Long>();
    List<Long> formAudienceIds = new ArrayList<Long>();
    boolean audienceUpdated = false;

    for ( Iterator<Audience> dbIter = dbAudienceList.iterator(); dbIter.hasNext(); )
    {
      Audience dbAudience = dbIter.next();
      dbAudienceIds.add( dbAudience.getId() );
    }

    for ( Iterator<Audience> formIter = formAudienceList.iterator(); formIter.hasNext(); )
    {
      Audience formAudience = formIter.next();
      formAudienceIds.add( formAudience.getId() );
    }

    // If the audience counts do not match
    if ( dbAudienceIds.size() != formAudienceIds.size() )
    {
      return true;
    }

    // If the audience counts match but the audiences are different
    for ( Iterator<Audience> formAudIter = formAudienceList.iterator(); formAudIter.hasNext(); )
    {
      if ( !dbAudienceIds.contains( formAudIter.next().getId() ) )
      {
        audienceUpdated = true;
      }
    }

    for ( Iterator<Audience> dbAudIter = dbAudienceList.iterator(); dbAudIter.hasNext(); )
    {
      if ( !formAudienceIds.contains( dbAudIter.next().getId() ) )
      {
        audienceUpdated = true;
      }
    }
    return audienceUpdated;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  /**
   * Gets an AudienceService
   * 
   * @return AudienceService
   */
  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

  private OTSProgramService getOTSProgramService()
  {
    return (OTSProgramService)getService( OTSProgramService.BEAN_NAME );
  }

  /**
   * Gets a HierarchyService
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

}

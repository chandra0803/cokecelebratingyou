
package com.biperf.core.service.leaderboard.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.dao.leaderboard.LeaderBoardDAO;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardComparator;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.leaderboard.LeaderBoardPaxActivity;
import com.biperf.core.domain.leaderboard.LeaderBoardSet;
import com.biperf.core.domain.leaderboard.LeaderBoardView;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.LeaderBoardMailingProcess;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.leaderboard.LeaderBoardService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.util.CmsResourceBundle;

public class LeaderBoardServiceImpl implements LeaderBoardService
{

  private CMAssetService cmAssetService = null;
  private MailingService mailingService;
  private MessageService messageService;
  private ParticipantService participantService;
  private SystemVariableService systemVariableService;
  private ProcessService processService;

  private static Comparator<LeaderBoardParticipant> ASCE_COMPARATOR = new Comparator<LeaderBoardParticipant>()
  {
    public int compare( LeaderBoardParticipant lbp1, LeaderBoardParticipant lbp2 )
    {
      return Integer.parseInt( lbp1.getScore() ) - Integer.parseInt( lbp2.getScore() );
    }
  };

  private static Comparator<LeaderBoardParticipant> DESC_COMPARATOR = new Comparator<LeaderBoardParticipant>()
  {
    public int compare( LeaderBoardParticipant lbp1, LeaderBoardParticipant lbp2 )
    {
      return Integer.parseInt( lbp2.getScore() ) - Integer.parseInt( lbp1.getScore() );
    }
  };

  private LeaderBoardDAO leaderBoardDAO;

  public void setLeaderBoardDAO( LeaderBoardDAO leaderBoardDAO )
  {
    this.leaderBoardDAO = leaderBoardDAO;
  }

  public boolean isLeaderBoardNameUnique( String leaderBoardName, Long currentLeaderBoardId )
  {
    return this.leaderBoardDAO.isLeaderBoardNameUnique( leaderBoardName, currentLeaderBoardId );
  }

  public LeaderBoard saveRulesCmText( LeaderBoard leaderBoard, String rulesText ) throws ServiceErrorException
  {

    try
    {
      if ( leaderBoard.getRulescmAsset() == null )
      {
        // Create and set asset to leaderboard
        String newAssetName = cmAssetService.getUniqueAssetCode( LeaderBoard.LEADERBOARD_RULES_CMASSET_PREFIX );
        leaderBoard.setRulescmAsset( newAssetName );
      }

      CMDataElement cmDataElement = new CMDataElement( LeaderBoard.LEADERBOARD_RULES_CMASSET_NAME, LeaderBoard.LEADERBOARD_RULES_CMASSET_TYPE_KEY, rulesText, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      cmAssetService.createOrUpdateAsset( LeaderBoard.LEADERBOARD_RULES_SECTION_CODE,
                                          LeaderBoard.LEADERBOARD_RULES_CMASSET_TYPE_NAME,
                                          LeaderBoard.LEADERBOARD_RULES_CMASSET_NAME,
                                          leaderBoard.getRulescmAsset(),
                                          elements,
                                          UserManager.getLocale(),
                                          null );
      if ( !systemVariableService.getDefaultLanguage().getStringVal().equals( UserManager.getLocale().toString() ) )
      {
        cmAssetService.createOrUpdateAsset( LeaderBoard.LEADERBOARD_RULES_SECTION_CODE,
                                            LeaderBoard.LEADERBOARD_RULES_CMASSET_TYPE_NAME,
                                            LeaderBoard.LEADERBOARD_RULES_CMASSET_NAME,
                                            leaderBoard.getRulescmAsset(),
                                            elements,
                                            Locale.US,
                                            null );
      }
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return leaderBoard;
  }

  // LeaderBoard Service Methods Impl Starts Here
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#getgetLeaderBoardById(java.lang.Long)
   * 
   * @param id
   * @return LeaderBoard
   * @throws ServiceErrorException
   */
  public LeaderBoard getLeaderBoardById( Long id )
  {
    LeaderBoard leaderBoard = this.leaderBoardDAO.getLeaderBoardById( id );
    return leaderBoard;
  }

  /**
   * {@inheritDoc}
   */
  public LeaderBoard getLeaderBoardByIdWithAssociations( Long leaderBoardId, AssociationRequestCollection associationRequestCollection ) throws ServiceErrorException
  {
    LeaderBoard leaderBoard = this.leaderBoardDAO.getLeaderBoardById( leaderBoardId );
    if ( associationRequestCollection != null )
    {
      for ( Iterator iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
      {
        AssociationRequest req = (AssociationRequest)iterator.next();
        req.execute( leaderBoard );
      }
    }
    return leaderBoard;
  }

  public LeaderBoard getLeaderBoardJsonForDetail( Long leaderBoardId, AssociationRequestCollection associationRequestCollection ) throws ServiceErrorException
  {
    LeaderBoard leaderBoard = getLeaderBoardByIdWithAssociations( leaderBoardId, associationRequestCollection );

    Long userId = UserManager.getUserId();
    String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    for ( LeaderBoardParticipant lbp : leaderBoard.getParticipants() )
    {
      leaderBoard.getSortParticipants().add( lbp );
    }

    // check the sort_order of that leaderboard and sort the participants.
    if ( leaderBoard.getSortOrder().equals( SORT_DESCENDING ) )
    {
      Collections.sort( leaderBoard.getSortParticipants(), DESC_COMPARATOR );
    }
    else
    {
      Collections.sort( leaderBoard.getSortParticipants(), ASCE_COMPARATOR );
    }
    for ( LeaderBoardParticipant lbp : leaderBoard.getSortParticipants() )
    {
      // check if logged in user is a participant, also lb as of date
      if ( userId.equals( lbp.getUser().getId() ) )
      {
        leaderBoard.setAsOfDate( lbp.getAsOfDate() );
        lbp.setCurrentUser( true );
      }
      else
      {
        lbp.setCurrentUser( false );
      }
    }
    // if lb as of date is null then set the first top user as of date to lb.
    if ( leaderBoard.getAsOfDate() == null && leaderBoard.getSortParticipants().size() > 0 )
    {
      leaderBoard.setAsOfDate( leaderBoard.getSortParticipants().get( 0 ).getAsOfDate() );
    }

    // if current user is the owner of the leaderboard then lb can be editable by user.
    if ( userId.equals( leaderBoard.getUser().getId() ) )
    {
      leaderBoard.setEditableByUser( true );
      Map leaderBoardParamMap = new HashMap();
      leaderBoardParamMap.put( "boardId", leaderBoard.getId() );
      leaderBoardParamMap.put( "source", LeaderBoardService.PAGE_DETAIL );
      leaderBoard.getLeaderBoardActionUrls().setEditLeaderBoardUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.LEADERBOARD_EDIT_ACTION, leaderBoardParamMap ) );
      leaderBoardParamMap.put( "type", LeaderBoardService.TYPE_COPY );
      leaderBoard.getLeaderBoardActionUrls().setCopyLeaderBoardUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.LEADERBOARD_COPY_ACTION, leaderBoardParamMap ) );
    }
    else
    {
      leaderBoard.setEditableByUser( false );
    }
    return leaderBoard;
  }

  public LeaderBoard getLeaderBoardJsonForTile( Long leaderBoardId ) throws ServiceErrorException
  {
    LeaderBoard leaderBoard = new LeaderBoard();
    Long participantId = UserManager.getUserId();
    boolean paxFoundInList = false;
    List<LeaderBoardParticipant> lbPaxs = this.leaderBoardDAO.getLeaderBoardParticipantsByLeaderBoardId( leaderBoardId );
    for ( LeaderBoardParticipant lbp : lbPaxs )
    {
      if ( lbp.getUser().getId().equals( participantId ) )
      {
        paxFoundInList = true;
      }
    }
    if ( !paxFoundInList )
    {
      LeaderBoardParticipant participant = this.leaderBoardDAO.getLeaderBoardParticipantByParticipantIdAndLeaderBoardId( participantId, leaderBoardId );
      if ( participant != null )
      {
        lbPaxs.add( participant );
      }
    }
    leaderBoard = mergeLeaderBoardPaxDataInTOLeaderBoard( leaderBoard, lbPaxs, participantId );
    return leaderBoard;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#getgetLeaderBoardByUserId(java.lang.Long)
   * 
   * @param userId
   * @return List
   * @throws ServiceErrorException
   */
  public List getLeaderBoardsByOwnerUserIdAndStatus( Long userId, String status ) throws ServiceErrorException
  {
    return this.leaderBoardDAO.getLeaderBoardsByOwnerUserIdAndStatus( userId, status );
  }

  public LeaderBoardView getLeaderBoardsForDetailUsingNameId( Long userId, String nameId ) throws ServiceErrorException
  {
    List<LeaderBoard> leaderBoards = leaderBoards = this.leaderBoardDAO.getLeaderBoardsForDetailUsingNameId( userId, nameId );
    Collections.sort( leaderBoards, new LeaderBoardComparator() );

    LeaderBoardSet pendingLBD = new LeaderBoardSet();
    LeaderBoardSet archivedLBD = new LeaderBoardSet();
    LeaderBoardSet activeLBD = new LeaderBoardSet();

    String activeLeaderBoardEmptyMessage = CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.EMPTY_MESSAGE_ACTIVE" );
    String pendingLeaderBoardEmptyMessage = CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.EMPTY_MESSAGE_PENDING" );
    String archiveLeaderBoardEmptyMessage = CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.EMPTY_MESSAGE_ARCHIVE" );

    LeaderBoardView leaderBoardDetailPageView = new LeaderBoardView();

    // JSON object need this even though the lb size is zero
    activeLBD.setName( CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.ACTIVE_SET" ) );
    activeLBD.setNameId( LEADERBOARD_ACTIVE );
    pendingLBD.setName( CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.PENDING_SET" ) );
    pendingLBD.setNameId( LEADERBOARD_PENDING );
    archivedLBD.setName( CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.ARCHIVED_SET" ) );
    archivedLBD.setNameId( LEADERBOARD_ARCHIVED );

    if ( nameId != null && nameId.equals( LEADERBOARD_PENDING ) )
    {
      pendingLBD.setLeaderBoardsList( leaderBoards );
    }
    else if ( nameId != null && nameId.equals( LEADERBOARD_ARCHIVED ) )
    {
      archivedLBD.setLeaderBoardsList( leaderBoards );
    }
    else
    {
      activeLBD.setLeaderBoardsList( leaderBoards );
    }

    if ( activeLBD.getLeaderBoardsList() == null || activeLBD.getLeaderBoardsList().size() == 0 )
    {
      activeLBD.setEmptyMessage( activeLeaderBoardEmptyMessage );
    }
    if ( pendingLBD.getLeaderBoardsList() == null || pendingLBD.getLeaderBoardsList().size() == 0 )
    {
      pendingLBD.setEmptyMessage( pendingLeaderBoardEmptyMessage );
    }
    if ( archivedLBD.getLeaderBoardsList() == null || archivedLBD.getLeaderBoardsList().size() == 0 )
    {
      archivedLBD.setEmptyMessage( archiveLeaderBoardEmptyMessage );
    }

    leaderBoardDetailPageView.getLeaderBoardSets().add( activeLBD );
    leaderBoardDetailPageView.getLeaderBoardSets().add( pendingLBD );
    leaderBoardDetailPageView.getLeaderBoardSets().add( archivedLBD );

    return leaderBoardDetailPageView;
  }

  public LeaderBoardView getLeaderBoardsForTile( Long userId ) throws ServiceErrorException
  {
    List<LeaderBoard> leaderBoards = this.leaderBoardDAO.getLeaderBoardsForTile( userId );
    LeaderBoardSet leaderBoardSet = new LeaderBoardSet();
    LeaderBoardView leaderBoardTileView = new LeaderBoardView();
    leaderBoardSet.setNameId( LEADERBOARD_ACTIVE );
    leaderBoardSet.setName( CmsResourceBundle.getCmsBundle().getString( "leaderboard.label.ACTIVE_SET" ) );
    leaderBoardSet.setLeaderBoardsList( leaderBoards );
    leaderBoardTileView.getLeaderBoardSets().add( leaderBoardSet );
    return leaderBoardTileView;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#getLeaderBoardByStatus
   * @param status
   * @return List
   * @throws ServiceErrorException
   */
  public List getLeaderBoardByStatus( String status )
  {
    return this.leaderBoardDAO.getLeaderBoardByStatus( status );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#getLeaderBoardByStatus
   * @param status
   * @return List
   * @throws ServiceErrorException
   */
  public List<LeaderBoard> getUnexpiredLeaderBoards()
  {
    return this.leaderBoardDAO.getUnexpiredLeaderBoards();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#saveBLeaderBoard(com.biperf.core.domain.leaderboard.LeaderBoard)
   * @param LeaderBoardToSave
   * @return LeaderBoard
   * @throws ServiceErrorException
   */
  public LeaderBoard saveLeaderBoard( LeaderBoard leaderBoardToSave ) throws ServiceErrorException
  {
    LeaderBoard leaderBoardToReturn = null;
    Boolean createNewLB = (Boolean) ( leaderBoardToSave.getId() == null ? true : false );
    leaderBoardToReturn = this.leaderBoardDAO.saveLeaderBoard( leaderBoardToSave );
    String notifyMessage = leaderBoardToSave.getNotifyMessage();
    Long leaderBoardId = leaderBoardToReturn.getId();

    if ( !StringUtils.isEmpty( notifyMessage ) )
    {
      if ( leaderBoardId != null )
      {
        Process process = processService.createOrLoadSystemProcess( LeaderBoardMailingProcess.PROCESS_NAME, LeaderBoardMailingProcess.BEAN_NAME );

        LinkedHashMap parameterValueMap = new LinkedHashMap();

        parameterValueMap.put( "leaderBoardId", new String[] { leaderBoardId.toString() } );
        parameterValueMap.put( "createLb", new String[] { createNewLB.toString() } );
        parameterValueMap.put( "notifyMessage", new String[] { notifyMessage } );

        ProcessSchedule processSchedule = new ProcessSchedule();
        processSchedule.setStartDate( new Date() );
        processSchedule.setTimeOfDayMillis( new Long( 0 ) );
        processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

        processService.scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );
      }
    }

    return leaderBoardToReturn;
  }

  public LeaderBoard save( LeaderBoard leaderboard ) throws ServiceErrorException
  {
    this.leaderBoardDAO.saveLeaderBoard( leaderboard );
    return leaderboard;
  }

  // LeaderBoard Service Methods Impl Ends Here

  // LeaderBoardParticipant Service Methods Impl Starts Here
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#getLeaderBoardParticipantById(java.lang.Long)
   * 
   * @param id
   * @return LeaderBoardParticipant
   * @throws ServiceErrorException
   */
  public LeaderBoardParticipant getLeaderBoardParticipantById( Long id ) throws ServiceErrorException
  {
    return this.leaderBoardDAO.getLeaderBoardParticipantById( id );
  }

  /**
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#getLeaderBoardParticipantsByLeaderBoardId(java.lang.Long)
   * 
   * @param leaderBoardId
   * @return List
   * @throws ServiceErrorException
   */
  public List getLeaderBoardParticipantsByLeaderBoardId( Long leaderBoardId, String paxStatus ) throws ServiceErrorException
  {
    List lbPaxs = this.leaderBoardDAO.getLeaderBoardParticipantsByLeaderBoardId( leaderBoardId, paxStatus );
    return lbPaxs;
  }

  /**
   * Overridden from 
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#saveLeaderBoardParticipant(leaderBoardparticipanttosave)
   * 
   * @param leaderBoardParticipantToSave
   * @return LeaderBoardParticipant
   * @throws ServiceErrorException
   */
  public LeaderBoardParticipant saveLeaderBoardParticipant( LeaderBoardParticipant leaderBoardParticipantToSave ) throws ServiceErrorException
  {
    return this.leaderBoardDAO.saveLeaderBoardParticipant( leaderBoardParticipantToSave );
  }

  /**
   * Overridden from 
   * 
   * Save leaderBoardParticipants list
   * 
   * @param LeaderBoardParticipant
   * @return LeaderBoardParticipant
   * @throws ServiceErrorException
   */
  public void saveLeaderBoardParticipantsList( List<LeaderBoardParticipant> leaderBoardParticipants ) throws ServiceErrorException
  {

    this.leaderBoardDAO.saveLeaderBoardParticipantsList( leaderBoardParticipants );
  }

  // LeaderBoardParticipant Service Methods Impl Ends Here

  // LeaderBoardPaxActivity Service Methods Impl Starts Here
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#getLeaderBoardPaxActivityById(java.lang.Long)
   * 
   * @param id
   * @return LeaderBoardPaxActivity
   * @throws ServiceErrorException
   */
  public LeaderBoardPaxActivity getLeaderBoardPaxActivityById( Long id )
  {
    LeaderBoardPaxActivity leaderBoardPaxActivity = this.leaderBoardDAO.getLeaderBoardPaxActivityById( id );
    return leaderBoardPaxActivity;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#getLeaderBoardPaxActivityByUserId(java.lang.Long)
   * 
   * @param userId
   * @return List
   * @throws ServiceErrorException
   */
  public List getLeaderBoardPaxActivityByUserId( Long userId ) throws ServiceErrorException
  {
    return this.leaderBoardDAO.getLeaderBoardPaxActivityByUserId( userId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#getLeaderBoardPaxActivityByLeaderBoardId(java.lang.Long)
   * 
   * @param leaderBoardId
   * @return List
   * @throws ServiceErrorException
   */
  public List getLeaderBoardPaxActivityByLeaderBoardId( Long leaderBoardId ) throws ServiceErrorException
  {
    return this.leaderBoardDAO.getLeaderBoardPaxActivityByLeaderBoardId( leaderBoardId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.leaderboard.LeaderBoardService#saveLeaderBoardPaxActivity(leaderBoardPaxActivityTosave)
   * 
   * @param leaderBoardPaxActivityToSave
   * @return LeaderBoardPaxActivity
   * @throws ServiceErrorException
   */
  public LeaderBoardPaxActivity saveLeaderBoardPaxActivity( LeaderBoardPaxActivity leaderBoardPaxActivityTosave ) throws ServiceErrorException
  {
    LeaderBoardPaxActivity dbLeaderBoardPaxActivity = null;

    // if inserting, need to process child objects after save.
    if ( leaderBoardPaxActivityTosave.getId() == null )
    {
      // get the children and hold onto them.
      leaderBoardPaxActivityTosave = this.leaderBoardDAO.saveLeaderBoardPaxActivity( leaderBoardPaxActivityTosave );
    }
    dbLeaderBoardPaxActivity = this.leaderBoardDAO.saveLeaderBoardPaxActivity( leaderBoardPaxActivityTosave );

    return dbLeaderBoardPaxActivity;
  }

  // LeaderBoardPaxActivity Service Methods Impl Ends Here

  private LeaderBoard mergeLeaderBoardPaxDataInTOLeaderBoard( LeaderBoard leaderBoard, List<LeaderBoardParticipant> lbPaxs, Long participantId )
  {
    if ( lbPaxs != null && lbPaxs.size() > 0 )
    {
      leaderBoard = lbPaxs.get( 0 ).getLeaderboard();
    }
    if ( lbPaxs != null )
    {
      leaderBoard.getSortParticipants().addAll( lbPaxs );
    }
    if ( leaderBoard.getSortOrder().equals( SORT_DESCENDING ) )
    {
      Collections.sort( leaderBoard.getSortParticipants(), DESC_COMPARATOR );
    }
    else
    {
      Collections.sort( leaderBoard.getSortParticipants(), ASCE_COMPARATOR );
    }
    for ( LeaderBoardParticipant lbp : leaderBoard.getSortParticipants() )
    {
      if ( participantId.equals( lbp.getUser().getId() ) )
      {
        leaderBoard.setAsOfDate( lbp.getAsOfDate() );
        lbp.setCurrentUser( true );
      }
      else
      {
        lbp.setCurrentUser( false );
      }
    }
    if ( leaderBoard.getAsOfDate() == null && leaderBoard.getSortParticipants().size() > 0 )
    {
      leaderBoard.setAsOfDate( leaderBoard.getSortParticipants().get( 0 ).getAsOfDate() );
    }
    return leaderBoard;
  }

  /**
   * Gets the LeaderBoardParticipant by the leaderBoardId and userId.
   * 
   * @param leaderBoardId
   * * @param userId
   * @return LeaderBoardParticipant
   */
  public LeaderBoardParticipant getLeaderBoardParticipantByParticipantIdAndLeaderBoardId( Long leaderBoardId, Long userId )
  {
    return leaderBoardDAO.getLeaderBoardParticipantByParticipantIdAndLeaderBoardId( userId, leaderBoardId );
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public MessageService getMessageService()
  {
    return messageService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public Integer isUserHasLiveLeaderBoard( Long userId )
  {
    return this.leaderBoardDAO.isUserHasLiveLeaderBoard( userId );
  }

  public ProcessService getProcessService()
  {
    return processService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

}

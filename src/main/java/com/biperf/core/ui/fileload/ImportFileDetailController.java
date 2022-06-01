/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/fileload/ImportFileDetailController.java,v $
 */

package com.biperf.core.ui.fileload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.promotion.hibernate.ChallengepointPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.GoalQuestPromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.RecognitionPromotionQueryConstraint;
import com.biperf.core.domain.budget.BudgetMasterComparator;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.BudgetSegmentComparator;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.FileImportActionType;
import com.biperf.core.domain.enums.FileImportApprovalType;
import com.biperf.core.domain.enums.FileImportTransactionType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MessageType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.leaderboard.LeaderBoardService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PromotionRoundValue;
import com.objectpartners.cms.util.ContentReaderManager;

/*
 * ImportFileDetailController <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 20, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ImportFileDetailController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the File Load Details page.
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws ServiceErrorException
  {

    ImportFileForm importFileForm = (ImportFileForm)request.getAttribute( "importFileForm" );

    // Get the import file.
    ImportFile importFile = (ImportFile)request.getAttribute( "importFile" );
    if ( importFile == null )
    {
      importFile = getImportFile( getImportFileId( request ) );
    }
    if ( importFile.getIsLeaderBoardImportFile() && importFile.getLeaderboardId() != null )
    {
      LeaderBoard lb = getLeaderBoardService().getLeaderBoardById( importFile.getLeaderboardId() );
      importFile.setLeaderBoardName( lb.getName() );
    }

    if ( importFile.getIsBadgeImportFile() && importFile.getBadgepromotionId() != null )
    {
      Badge b = getGamificationService().getBadgeById( importFile.getBadgepromotionId() );
      importFile.setBadgeName( b.getName() );
    }

    if ( importFile.getIsBudgetImportFile() && importFile.getBudgetSegmentId() != null )
    {
      BudgetSegment b = getBudgetMasterService().getBudgetSegmentById( importFile.getBudgetSegmentId() );
      importFile.setBudgetSegmentName( b.getDisplaySegmentName() );
    }

    if ( importFile.getIsBudgetDistributionImportFile() && importFile.getBudgetSegmentId() != null )
    {
      BudgetSegment b = getBudgetMasterService().getBudgetSegmentById( importFile.getBudgetSegmentId() );
      importFile.setBudgetSegmentName( b.getDisplaySegmentName() );
      importFile.setBudgetMasterName( b.getBudgetMaster().getBudgetMasterName() );
    }

    request.setAttribute( "importFile", importFile );
    request.setAttribute( "fileNameCriteria", RequestUtils.getOptionalParamString( request, "fileNameCriteria" ) );
    request.setAttribute( "statusCriteria", RequestUtils.getOptionalParamString( request, "statusCriteria" ) );
    request.setAttribute( "fileTypeCriteria", RequestUtils.getOptionalParamString( request, "fileTypeCriteria" ) );
    request.setAttribute( "startDateCriteria", RequestUtils.getOptionalParamString( request, "startDateCriteria" ) );
    request.setAttribute( "endDateCriteria", RequestUtils.getOptionalParamString( request, "endDateCriteria" ) );

    ImportFileTypeType importFileType = importFile.getFileType();
    if ( importFile != null )
    {
      if ( importFile.getIsStaged() )
      {
        // Get the hierarchy list.

        if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.HIERARCHY ) )
        {
          List hierarchyList = getHierarchyList();
          request.setAttribute( "hierarchyList", hierarchyList );
          request.setAttribute( "hierarchyListSize", new Integer( hierarchyList.size() ) );
        }

        if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.PARTICIPANT ) )
        {
          List hierarchyList = getHierarchyList();
          request.setAttribute( "hierarchyList", hierarchyList );
          request.setAttribute( "hierarchyListSize", new Integer( hierarchyList.size() ) );
        }

        // Get the LeaderBoard List
        if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.LEADERBOARD ) )
        {
          List leaderBoardList = getLeaderBoardList();
          request.setAttribute( "leaderBoardList", leaderBoardList );
          request.setAttribute( "leaderBoardListSize", new Integer( leaderBoardList.size() ) );
          List sortedActionList = new ArrayList( FileImportActionType.getList() );
          Collections.reverse( sortedActionList );
          request.setAttribute( "actionList", sortedActionList );
        }
        // get badge list
        if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.BADGE ) )
        {
          List badgeList = getBadgeList();
          request.setAttribute( "badgeList", badgeList );
          request.setAttribute( "badgeListSize", new Integer( badgeList.size() ) );
          request.setAttribute( "actionList", FileImportActionType.getList() );
        }

        // Get the promotion list.
        if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.BUDGET ) )
        {
          List promotionList = getPromotionListForBudget();
          request.setAttribute( "promotionList", promotionList );
          request.setAttribute( "promotionListSize", new Integer( promotionList.size() ) );

          String promotionId = (String)request.getAttribute( "promotionId" );
          if ( promotionId == null && promotionList.size() == 1 )
          {
            ListIterator iter = promotionList.listIterator();
            while ( iter.hasNext() )
            {
              Promotion promotion = (Promotion)iter.next();
              promotionId = promotion.getId().toString();
            }
          }

          if ( StringUtils.isNotEmpty( promotionId ) )
          {
            List budgetSegmentList = getBudgetSegmentListForBudget( new Long( promotionId ) );
            request.setAttribute( "budgetSegmentList", budgetSegmentList );
            request.setAttribute( "budgetSegmentListSize", new Integer( budgetSegmentList.size() ) );
          }

          List countryList = getCountryList();
          request.setAttribute( "countryList", countryList );
          request.setAttribute( "countryListSize", new Integer( countryList.size() ) );

          request.setAttribute( "countryId", getCountryService().getCountryByCode( Country.UNITED_STATES ).getId() );
        }
        else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.DEPOSIT ) )
        {
          String promoId = request.getParameter( "promotionId" ) != null ? request.getParameter( "promotionId" ) : null;
          List promotionList = getPromotionListForDeposit();
          request.setAttribute( "promotionList", promotionList );
          request.setAttribute( "promotionListSize", new Integer( promotionList.size() ) );

          if ( promotionList.size() == 1 )
          {
            Promotion promotion = (Promotion)promotionList.get( 0 );
            if ( promotion.isFileLoadEntry() && promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isAllowRecognitionSendDate() )
            {
              request.setAttribute( "showDelayRecognition", Boolean.TRUE );
              request.setAttribute( "maxDaysDelayed", ( (RecognitionPromotion)promotion ).getMaxDaysDelayed() );
            }
          }
          else if ( promotionList.size() > 1 && promoId != null && !promoId.isEmpty() )
          {
            Promotion promotion = getPromotionService().getPromotionById( new Long( promoId ) );
            if ( promotion.isFileLoadEntry() && promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isAllowRecognitionSendDate() )
            {
              request.setAttribute( "showDelayRecognition", Boolean.TRUE );
              request.setAttribute( "maxDaysDelayed", ( (RecognitionPromotion)promotion ).getMaxDaysDelayed() );
            }
          }
        }
        else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.AWARD_LEVEL ) )
        {
          List promotionList = getPromotionListForMerchandiseLevel();
          request.setAttribute( "promotionList", promotionList );
          request.setAttribute( "promotionListSize", new Integer( promotionList.size() ) );
        }
        else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.PRODUCT_CLAIM ) )
        {
          List promotionList = getPromotionListForProductClaims();
          request.setAttribute( "promotionList", promotionList );
          request.setAttribute( "promotionListSize", new Integer( promotionList.size() ) );
          request.setAttribute( "fileImportApprovalTypeList", FileImportApprovalType.getList() );
        }
        else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.GQ_BASE_DATA_LOAD ) || importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.GQ_GOAL_DATA_LOAD )
            || importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) || importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.GQ_VIN_LOAD ) )
        {
          List promotionList = getPromotionListForGoalQuest( importFileType.getCode() );
          request.setAttribute( "promotionList", promotionList );
          request.setAttribute( "promotionListSize", new Integer( promotionList.size() ) );
        }
        else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.CP_BASE_DATA_LOAD ) || importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.CP_LEVEL_DATA_LOAD )
            || importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.CP_PROGRESS_DATA_LOAD ) )
        {
          List promotionList = getPromotionListForChallengepoint( importFileType.getCode() );
          request.setAttribute( "promotionList", promotionList );
          request.setAttribute( "promotionListSize", new Integer( promotionList.size() ) );
        }
        else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.TD_PROGRESS_DATA_LOAD ) )
        {
          Set<PromotionRoundValue> promoRounds = getThrowdownService().getPromotionsForProgressLoad();
          List<ThrowdownPromotion> promotionList = new ArrayList<ThrowdownPromotion>();
          Map<Long, List<Integer>> promoRoundsMap = new HashMap<Long, List<Integer>>();
          Set<ThrowdownPromotion> promotions = new HashSet<ThrowdownPromotion>();
          for ( PromotionRoundValue promoRound : promoRounds )
          {
            promotions.add( promoRound.getPromotion() );
            if ( promoRoundsMap.get( promoRound.getPromotion().getId() ) != null )
            {
              promoRoundsMap.get( promoRound.getPromotion().getId() ).add( promoRound.getRoundNumber() );
            }
            else
            {
              List<Integer> roundNumbers = new ArrayList<Integer>();
              roundNumbers.add( promoRound.getRoundNumber() );
              promoRoundsMap.put( promoRound.getPromotion().getId(), roundNumbers );
            }
          }
          promotionList.addAll( promotions );
          Collections.sort( promotionList, ThrowdownPromotion.UpperCaseNameComparator );
          request.setAttribute( "promotionList", promotionList );
          request.setAttribute( "promoRoundsMap", promoRoundsMap );
          request.setAttribute( "promotionListSize", new Integer( promotionList.size() ) );
        }
        else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.BUDGET_DISTRIBUTION ) )
        {
          Long budgetMasterId = null;

          List budgetMasterList = getBudgetMasterService().getAllActive();
          Collections.sort( budgetMasterList, new BudgetMasterComparator() );
          request.setAttribute( "budgetMasterList", budgetMasterList );
          request.setAttribute( "budgetMasterListSize", new Integer( budgetMasterList.size() ) );

          if ( importFileForm != null && importFileForm.getBudgetMasterId() != null )
          {
            budgetMasterId = importFileForm.getBudgetMasterId();
            List budgetSegmentList = getBudgetMasterService().getBudgetSegmentsForDistribution( budgetMasterId );
            Collections.sort( budgetSegmentList, new BudgetSegmentComparator() );
            request.setAttribute( "budgetSegmentList", budgetSegmentList );
            request.setAttribute( "budgetSegmentListSize", budgetSegmentList.size() );
          }
        }
        else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.NOMINATION_APPROVER ) )
        {
          List<NameableBean> nominationPromotionList = getNominationPromotionList();
          request.setAttribute( "promotionList", nominationPromotionList );
          request.setAttribute( "promotionListSize", nominationPromotionList.size() );

        }
        else if ( StringUtils.isNotEmpty( importFileType.getCode() ) && StringUtils.contains( importFileType.getCode(), "ssicontest" ) )
        {
          String contestType = null;
          if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_DTGT ) )
          {
            contestType = SSIContestType.DO_THIS_GET_THAT;
          }
          else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_OBJ ) )
          {
            contestType = SSIContestType.OBJECTIVES;
          }
          else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_SR ) )
          {
            contestType = SSIContestType.STACK_RANK;
          }
          else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_SIU ) )
          {
            contestType = SSIContestType.STEP_IT_UP;
          }
          else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.SSI_CONTEST_ATN ) )
          {
            contestType = SSIContestType.AWARD_THEM_NOW;
          }

          if ( !StringUtils.isEmpty( contestType ) )
          {
            List<NameableBean> contestList = getSSIContestList( contestType );
            request.setAttribute( "contestList", contestList );
            request.setAttribute( "contestListSize", contestList.size() );
          }
        }
      }
      else if ( importFile.getIsVerified() )
      {
        if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.DEPOSIT ) )
        {
          List messageList = getMessageList();
          request.setAttribute( "messageList", messageList );
          request.setAttribute( "messageListSize", new Integer( messageList.size() ) );

          List transactionTypeList = FileImportTransactionType.getList();
          request.setAttribute( "transactionTypeList", transactionTypeList );
          request.setAttribute( "transactionTypeListSize", new Integer( transactionTypeList.size() ) );

        }
        else if ( importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.PRODUCT_CLAIM ) )
        {
          List pcApprovalList = FileImportApprovalType.getList();
          request.setAttribute( "approvalTypeList", pcApprovalList );
          request.setAttribute( "approvalTypeListSize", new Integer( pcApprovalList.size() ) );
        }
      }
      if ( !importFile.getIsStaged() && importFileType.getCode().equalsIgnoreCase( ImportFileTypeType.PARTICIPANT ) )
      {
        if ( importFile.getHierarchy() != null )
        {
          Hierarchy hierarchy = importFile.getHierarchy();

          String cmAssetCode = hierarchy.getCmAssetCode();
          String cmItemKey = hierarchy.getNameCmKey();
          String name = ContentReaderManager.getText( cmAssetCode, cmItemKey );

          hierarchy.setName( name );

        }

      }

    }
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private List<NameableBean> getNominationPromotionList()
  {
    return getPromotionService().getNominationPromotionListForApproverFileLoad();

  }

  private List<NameableBean> getSSIContestList( String contestType )
  {
    return getSSIContestService().getSSIContestList( contestType );
  }

  /**
   * Returns a list of active, not deleted hierarchies, sorted by hierarchy name.
   * 
   * @return a list of active, not deleted hierarchies, as a <code>List</code> of
   *         {@link com.biperf.core.domain.hierarchy.Hierarchy} objects.
   */
  private List getHierarchyList()
  {
    List hierarchyList = getHierarchyService().getActiveHierarchies();

    // Set the hierarchy name.
    Iterator iter = hierarchyList.iterator();
    while ( iter.hasNext() )
    {
      Hierarchy hierarchy = (Hierarchy)iter.next();

      String cmAssetCode = hierarchy.getCmAssetCode();
      String cmItemKey = hierarchy.getNameCmKey();
      String name = ContentReaderManager.getText( cmAssetCode, cmItemKey );

      hierarchy.setName( name );
    }

    // Sort the hierarchy list.
    Collections.sort( hierarchyList, new Comparator()
    {
      public int compare( Object object, Object object1 )
      {
        Hierarchy hierarchy1 = (Hierarchy)object;
        Hierarchy hierarchy2 = (Hierarchy)object1;

        return hierarchy1.getName().compareTo( hierarchy2.getName() );
      }
    } );

    return hierarchyList;
  }

  /**
   * Returns the specified import file.
   * 
   * @param importFileId the HTTP request we are handling.
   * @return the specified import file.
   */
  private ImportFile getImportFile( Long importFileId )
  {
    ImportService importService = (ImportService)getService( ImportService.BEAN_NAME );
    ImportFileAssociationRequest importFileAssociationRequest = new ImportFileAssociationRequest();
    return importService.getImportFile( importFileId, importFileAssociationRequest );
  }

  /**
   * Returns the import file ID.
   * 
   * @param request the HTTP request from which the import file ID is retrieved.
   * @return the import file ID.
   */
  private Long getImportFileId( HttpServletRequest request )
  {
    Long importFileId = null;

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
      try
      {
        String claimFormIdString = (String)clientStateMap.get( "importFileId" );
        importFileId = new Long( claimFormIdString );
      }
      catch( ClassCastException cce )
      {
        importFileId = (Long)clientStateMap.get( "importFileId" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    return importFileId;
  }

  /**
   * Returns a list of live master promotions sorted by promotion name.
   * 
   * @return a list of live master promotions, as a <code>List</code> of {@link Promotion}
   *         objects.
   */
  private List getPromotionListForDeposit()
  {
    List promotionList = null;

    PromotionQueryConstraint liveMasterPromotionQueryConstraint = new PromotionQueryConstraint();
    // to fix bug where if you have installed only recognition module, then the deposit file load
    // 'verify' fail
    // on parent promotion not being there
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.INSTALL_PRODUCTCLAIMS ).getBooleanVal() )
    {
      liveMasterPromotionQueryConstraint.setMasterOrChildConstraint( Boolean.TRUE );
    }
    liveMasterPromotionQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    liveMasterPromotionQueryConstraint.setPromotionTypesExcluded( new PromotionType[] { PromotionType.lookup( PromotionType.SURVEY ),
                                                                                        PromotionType.lookup( PromotionType.NOMINATION ),
                                                                                        PromotionType.lookup( PromotionType.QUIZ ),
                                                                                        PromotionType.lookup( PromotionType.DIY_QUIZ ),
                                                                                        PromotionType.lookup( PromotionType.BADGE ),
                                                                                        PromotionType.lookup( PromotionType.INSTANTPOLL ),
                                                                                        PromotionType.lookup( PromotionType.SELF_SERV_INCENTIVES ) } );
    promotionList = getPromotionService().getPromotionList( liveMasterPromotionQueryConstraint );

    if ( promotionList != null && promotionList.size() > 0 )
    {
      for ( int i = 0; i < promotionList.size(); i++ )
      {
        Promotion promotion = (Promotion)promotionList.get( i );
        if ( promotion.isRecognitionPromotion() )
        {
          RecognitionPromotion recoPromo = (RecognitionPromotion)promotion;
          if ( recoPromo.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
          {
            promotionList.remove( i );
          }
        }

      }
    }
    if ( promotionList != null )
    {
      Collections.sort( promotionList, new Comparator()
      {
        public int compare( Object object, Object object1 )
        {
          Promotion promotion1 = (Promotion)object;
          Promotion promotion2 = (Promotion)object1;

          return promotion1.getName().compareTo( promotion2.getName() );
        }
      } );
    }

    return promotionList;
  }

  // get recognition merchandise level promotion only
  private List getPromotionListForMerchandiseLevel()
  {
    List promotionList = null;

    PromotionQueryConstraint liveMasterPromotionQueryConstraint = new PromotionQueryConstraint();
    liveMasterPromotionQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.RECOGNITION ) } );
    liveMasterPromotionQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    promotionList = getPromotionService().getPromotionList( liveMasterPromotionQueryConstraint );

    ListIterator iter = promotionList.listIterator();
    while ( iter.hasNext() )
    {
      Promotion promotion = (Promotion)iter.next();
      if ( promotion.isRecognitionPromotion() )
      {
        RecognitionPromotion recoPromo = (RecognitionPromotion)promotion;
        if ( recoPromo.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
        {
          iter.remove();
        }
        // Bug # 28300
        else if ( !recoPromo.isAwardActive() )
        {
          iter.remove();
        }

      }
    }
    PropertyComparator.sort( promotionList, new MutableSortDefinition( "name", false, true ) );

    return promotionList;
  }

  /**
   * Returns a list of live master product claim promotions sorted by promotion name.
   * 
   * @return a list of live master promotions, as a <code>List</code> of {@link Promotion}
   *         objects.
   */
  private List getPromotionListForProductClaims()
  {
    List promotionList = null;

    PromotionQueryConstraint liveMasterPromotionQueryConstraint = new PromotionQueryConstraint();
    liveMasterPromotionQueryConstraint.setMasterOrChildConstraint( Boolean.TRUE );
    liveMasterPromotionQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) } );
    liveMasterPromotionQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    promotionList = getPromotionService().getPromotionList( liveMasterPromotionQueryConstraint );

    ListIterator iter = promotionList.listIterator();
    while ( iter.hasNext() )
    {
      Promotion promotion = (Promotion)iter.next();
      if ( promotion.isRecognitionPromotion() && !promotion.getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) ) )
      {
        iter.remove();
      }
    }
    PropertyComparator.sort( promotionList, new MutableSortDefinition( "name", false, true ) );

    return promotionList;
  }

  /**
   * Returns a list of live goalquest promotions sorted by promotion name.
   * 
   * @return a list of live goalquest promotions sorted by promotion name, as a
   *         <code>List</code> of {@link GoalQuestPromotion} objects.
   */
  private List getPromotionListForGoalQuest( String importFileType )
  {
    List promotionList = null;

    GoalQuestPromotionQueryConstraint queryConstraint = new GoalQuestPromotionQueryConstraint();
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.GOALQUEST ) } );
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );

    queryConstraint.setHasIssueAwardsRun( Boolean.FALSE );

    promotionList = getPromotionService().getPromotionList( queryConstraint );

    PropertyComparator.sort( promotionList, new MutableSortDefinition( "name", false, true ) );

    return promotionList;
  }

  /**
   * Returns a list of live Challengepoint promotions sorted by promotion name.
   * 
   * @return a list of live Challengepoint promotions sorted by promotion name, as a
   *         <code>List</code> of {@link ChallengePointPromotion} objects.
   */
  private List getPromotionListForChallengepoint( String importFileType )
  {
    List promotionList = null;

    ChallengepointPromotionQueryConstraint queryConstraint = new ChallengepointPromotionQueryConstraint();
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.CHALLENGE_POINT ) } );
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );

    queryConstraint.setHasIssueAwardsRun( Boolean.FALSE );

    promotionList = getPromotionService().getPromotionList( queryConstraint );

    PropertyComparator.sort( promotionList, new MutableSortDefinition( "name", false, true ) );

    return promotionList;
  }

  /**
   * Returns a list of live master promotions with budgets sorted by promotion name.
   * 
   * @return a list of live master promotions with budgets sorted by promotion name, as a
   *         <code>List</code> of {@link Promotion} objects.
   */
  private List getPromotionListForBudget()
  {
    List promotionList = null;

    RecognitionPromotionQueryConstraint promotionQueryConstraint = new RecognitionPromotionQueryConstraint();
    promotionQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ),
                                                                                          PromotionStatusType.lookup( PromotionStatusType.COMPLETE ),
                                                                                          PromotionStatusType.lookup( PromotionStatusType.EXPIRED ),
                                                                                          PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) } );
    promotionQueryConstraint.setHasBudgets( Boolean.TRUE );
    promotionList = getPromotionService().getPromotionList( promotionQueryConstraint );

    ListIterator iter = promotionList.listIterator();
    while ( iter.hasNext() )
    {
      Promotion promotion = (Promotion)iter.next();
      if ( promotion.getBudgetMaster().isCentralBudget() || !promotion.getBudgetMaster().isActive() )
      {
        iter.remove();
      }
    }
    Collections.sort( promotionList, new Comparator()
    {
      public int compare( Object object, Object object1 )
      {
        Promotion promotion1 = (Promotion)object;
        Promotion promotion2 = (Promotion)object1;

        return promotion1.getName().compareTo( promotion2.getName() );
      }
    } );

    return promotionList;
  }

  private List getBudgetSegmentListForBudget( Long promotionId )
  {
    Promotion promotion = getPromotionService().getPromotionById( promotionId );
    Long budgetMasterId = promotion.getBudgetMaster() != null ? promotion.getBudgetMaster().getId() : null;
    List budgetSegmentList = null;
    if ( budgetMasterId != null )
    {
      budgetSegmentList = getBudgetMasterService().getBudgetSegmentsByBudgetMasterId( budgetMasterId );
      Collections.sort( budgetSegmentList, new BudgetSegmentComparator() );
    }
    return budgetSegmentList;
  }

  /**
   * Returns List of LeaderBoards.
   * 
   * @return list of leaderBoards.
   */
  private List getLeaderBoardList()
  {
    List leaderBoardList = getLeaderBoardService().getUnexpiredLeaderBoards();
    // Set the leaderBoard name.
    Iterator iter = leaderBoardList.iterator();
    while ( iter.hasNext() )
    {
      LeaderBoard leaderboard = (LeaderBoard)iter.next();

      String name = leaderboard.getName();
      leaderboard.setName( name );
    }
    // Sort the leaderBoard list.
    Collections.sort( leaderBoardList, new Comparator()
    {
      public int compare( Object object, Object object1 )
      {
        LeaderBoard leaderboard1 = (LeaderBoard)object;
        LeaderBoard leaderboard2 = (LeaderBoard)object1;

        return leaderboard1.getName().compareTo( leaderboard2.getName() );
      }
    } );

    return leaderBoardList;

  }

  /**
   * Returns List of Badges.
   * 
   * @return list of badges.
   */
  private List getBadgeList()
  {
    List badgeList = getGamificationService().getBadgeByTypeAndStatus( BadgeType.FILELOAD, Badge.BADGE_ACTIVE );
    // Set the leaderBoard name.
    Iterator iter = badgeList.iterator();
    while ( iter.hasNext() )
    {
      Badge badge = (Badge)iter.next();

      String name = badge.getName();
      badge.setId( badge.getId() );
      badge.setName( name );
    }
    // Sort the leaderBoard list.
    Collections.sort( badgeList, new Comparator()
    {
      public int compare( Object object, Object object1 )
      {
        Badge badge1 = (Badge)object;
        Badge badge2 = (Badge)object1;

        return badge1.getName().compareTo( badge2.getName() );
      }
    } );

    return badgeList;

  }

  /**
   * Returns List of Countries.
   * 
   * @return list of countries.
   */
  private List getCountryList()
  {
    return getCountryService().getAllActive();
  }

  /**
   * Returns a list of messages sorted by message name.
   * 
   * @return a list of messages, as a <code>List</code> of {@link Message} objects.
   */
  private List getMessageList()
  {
    List messageList = getMessageService().getAllActiveMessagesByTypecode( MessageType.DEPOSIT );
    // Add the "No Notification" message for fix 18956
    Message noMessage = new Message();
    noMessage.setId( new Long( -1 ) );
    noMessage.setName( "No Notification" );

    messageList.add( noMessage );

    Collections.sort( messageList, new Comparator()
    {
      public int compare( Object object, Object object1 )
      {
        Message message1 = (Message)object;
        Message message2 = (Message)object1;

        return message1.getName().compareTo( message2.getName() );
      }
    } );

    return messageList;
  }

  /**
   * Returns the hierarchy service.
   * 
   * @return a reference to the hierarchy service.
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  /**
   * Returns the message service.
   * 
   * @return a reference to the message service.
   */
  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  /**
   * Returns the LeaderBoard
   * 
   * @return a reference to LeaderBoard.
   */
  private LeaderBoardService getLeaderBoardService()
  {
    return (LeaderBoardService)getService( LeaderBoardService.BEAN_NAME );
  }

  /**
   * Returns the Gamification
   * 
   * @return a reference to Gamification.
   */
  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  /**
   * Returns the CountryService
   * 
   * @return a reference to CountryService.
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  /**
   * Returns the promotion service.
   * 
   * @return a reference to the promotion service.
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Returns the ssicontest service.
   * 
   * @return a reference to the ssicontest service.
   */
  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  /**
   * Returns the SystemVariableService service.
   * 
   * @return a reference to the SystemVariableService service.
   */
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }

}

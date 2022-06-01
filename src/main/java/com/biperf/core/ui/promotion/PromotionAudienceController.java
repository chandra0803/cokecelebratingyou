/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionJobPositionType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * PromotionAudienceController.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>asondgeroth</td>
 * <td>Jul 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionAudienceController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    PromotionAudienceForm audienceForm = (PromotionAudienceForm)request.getAttribute( "promotionAudienceForm" );

    List availableAudiences = getAudienceService().getAll();
    List availableAudiencesCopy = new ArrayList( availableAudiences );

    List<Audience> availablePrimaryAudiences = getAvailableAudiences( audienceForm.getPrimaryAudienceAsList(), new ArrayList( availableAudiences ) );
    Collections.sort( availablePrimaryAudiences, new AudienceComparator() );
    request.setAttribute( "availablePrimaryAudiences", availablePrimaryAudiences );

    boolean isGoalQuest = audienceForm.getPromotionTypeCode().equals( PromotionType.lookup( PromotionType.GOALQUEST ).getCode() );
    boolean isChallengepoint = audienceForm.getPromotionTypeCode().equals( PromotionType.lookup( PromotionType.CHALLENGE_POINT ).getCode() );
    if ( !isGoalQuest && !isChallengepoint )
    {
      List<Audience> availableSecondaryAudiences = getAvailableAudiences( audienceForm.getSecondaryAudienceAsList(), new ArrayList( availableAudiences ) );
      Collections.sort( availableSecondaryAudiences, new AudienceComparator() );
      request.setAttribute( "availableSecondaryAudiences", availableSecondaryAudiences );
    }

    if ( audienceForm.getPromotionTypeCode().equals( PromotionType.lookup( PromotionType.THROWDOWN ).getCode() ) )
    {
      List<Audience> availableDivisionAudiences = getAvailableDivisionAudiences( audienceForm.getDivisionList(), new ArrayList( availableAudiences ) );
      Collections.sort( availableDivisionAudiences, new AudienceComparator() );
      request.setAttribute( "availableDivisionAudiences", availableDivisionAudiences );
    }

    Promotion promotion = getPromotionService().getPromotionById( new Long( audienceForm.getPromotionId() ) );
    if ( promotion != null && promotion.isRecognitionPromotion() )
    {
      if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
      {
        request.setAttribute( "isPurlIncluded", Boolean.TRUE );
      }
      if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
      {
        request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
      }
    }

    if ( promotion != null && promotion.isGoalQuestOrChallengePointPromotion() )
    {
      if ( promotion.isGoalQuestPromotion() && promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS )
          || promotion.isChallengePointPromotion() && ( (ChallengePointPromotion)promotion ).getChallengePointAwardType().getCode().equals( ChallengePointAwardType.POINTS ) )
      {
        // During setup, partners will always be available
        audienceForm.setGqpartnersEnabled( "true" );
        List primaryAudList = new ArrayList( audienceForm.getPrimaryAudienceAsList() );
        List partnerAudList = new ArrayList( audienceForm.getPartnerAudienceList() );
        partnerAudList.addAll( primaryAudList );

        List<Audience> availablePartnerAudiences = getAvailableAudiences( partnerAudList, availableAudiencesCopy );
        Collections.sort( availablePartnerAudiences, new AudienceComparator() );
        request.setAttribute( "availablePartnerAudiences", availablePartnerAudiences );

        request.removeAttribute( "availablePrimaryAudiences" );

        List<Audience> availableGQCPPrimaryAudiences = getAvailableAudiences( partnerAudList, availableAudiencesCopy );
        Collections.sort( availableGQCPPrimaryAudiences, new AudienceComparator() );
        request.setAttribute( "availablePrimaryAudiences", availableGQCPPrimaryAudiences );
      }
    }

    String domainId = null;
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( StringUtils.isNotEmpty( clientState ) )
      {
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
          domainId = (String)clientStateMap.get( "domainId" );
        }
        catch( ClassCastException cce )
        {
          Long id = (Long)clientStateMap.get( "domainId" );
          domainId = id.toString();
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Long domainIdLong = null;
    if ( StringUtils.isNotEmpty( domainId ) )
    {
      domainIdLong = new Long( domainId );
    }

    List userCharList = makeGetAllCharacteristicServiceCall( domainIdLong );

    request.setAttribute( "typeList", getAvailableTeamPositions( new ArrayList( PromotionJobPositionType.getList() ), audienceForm.getPromotionTeamPositionAsList() ) );
    request.setAttribute( "hasParent", Boolean.valueOf( audienceForm.isHasParent() ) );
    request.setAttribute( "hasChildren", Boolean.valueOf( audienceForm.isHasChildren() ) );
    request.setAttribute( "promotionStatus", audienceForm.getPromotionStatus() );
    request.setAttribute( "promotionTypeCode", audienceForm.getPromotionTypeCode() );
    request.setAttribute( "userCharList", userCharList );

    if ( ObjectUtils.equals( audienceForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    if ( audienceForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) || audienceForm.getPromotionTypeCode().equals( PromotionType.DIY_QUIZ )
        || audienceForm.getPromotionTypeCode().equals( PromotionType.SURVEY ) || audienceForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST )
        || audienceForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) || audienceForm.getPromotionTypeCode().equals( PromotionType.WELLNESS )
        || audienceForm.getPromotionTypeCode().equals( PromotionType.THROWDOWN ) || audienceForm.getPromotionTypeCode().equals( PromotionType.SELF_SERV_INCENTIVES ) )
    {
      request.setAttribute( "pageNumber", "2" );
    }
    else
    {
      request.setAttribute( "pageNumber", "3" );
    }

    boolean isRecognition = audienceForm.getPromotionTypeCode().equals( PromotionType.lookup( PromotionType.RECOGNITION ).getCode() );

    request.setAttribute( "isRecognition", String.valueOf( isRecognition ) );
    populateTilesAttribute( tileContext, audienceForm.isHasParent() );

  }

  public class AudienceComparator implements Comparator<Audience>
  {
    public int compare( Audience o1, Audience o2 )
    {
      return o1.getName().toLowerCase().compareTo( o2.getName().toLowerCase() );
    }
  }

  private List<Audience> getAvailableAudiences( List<PromotionAudienceFormBean> audiences, List<Audience> availableAudiences )
  {
    if ( audiences != null )
    {
      Iterator<Audience> audienceIterator = availableAudiences.iterator();
      while ( audienceIterator.hasNext() )
      {
        Audience audience = (Audience)audienceIterator.next();
        Iterator<PromotionAudienceFormBean> assignedIterator = audiences.iterator();
        while ( assignedIterator.hasNext() )
        {
          PromotionAudienceFormBean audienceBean = assignedIterator.next();
          if ( audienceBean.getAudienceId().equals( audience.getId() ) )
          {
            audienceIterator.remove();
          }
        }
      }
    }
    return availableAudiences;
  }

  private List<Audience> getAvailableDivisionAudiences( List<ThrowdownDivisionBean> divisionAudienceBeans, List<Audience> availableAudiences )
  {
    List<Long> assignedAudienceIds = new ArrayList<Long>();
    if ( divisionAudienceBeans != null )
    {
      for ( ThrowdownDivisionBean divisionAudienceBean : divisionAudienceBeans )
      {
        for ( PromotionAudienceFormBean promotionAudienceFormBean : divisionAudienceBean.getDivisionAudiences() )
        {
          assignedAudienceIds.add( promotionAudienceFormBean.getAudienceId() );
        }
      }
    }
    if ( divisionAudienceBeans != null )
    {
      Iterator<Audience> audienceIterator = availableAudiences.iterator();
      while ( audienceIterator.hasNext() )
      {
        Audience audience = audienceIterator.next();
        if ( assignedAudienceIds.contains( audience.getId() ) )
        {
          audienceIterator.remove();
        }
      }
    }
    return availableAudiences;
  }

  private List getAvailableTeamPositions( List jobPositions, List promotionTeamPositions )
  {
    Iterator it = jobPositions.iterator();
    while ( it.hasNext() )
    {
      PromotionJobPositionType currentJobPosition = (PromotionJobPositionType)it.next();
      if ( promotionTeamPositions != null && promotionTeamPositions.size() > 0 )
      {
        Iterator selectedJobPositions = promotionTeamPositions.iterator();
        while ( selectedJobPositions.hasNext() )
        {
          PromotionAudienceFormBean positionBean = (PromotionAudienceFormBean)selectedJobPositions.next();
          if ( positionBean.getName().equals( currentJobPosition.getName() ) )
          {
            it.remove();
          }
        }
      }
    }

    return jobPositions;

  }

  private void populateTilesAttribute( ComponentContext context, boolean hasParent )
  {
    if ( hasParent )
    {
      context.putAttribute( "audiencePageTop", "/promotion/productclaim/promotionAudienceChild.jsp" );
      // no bottom needed because Child is both top and bottom section
      context.putAttribute( "audiencePageBottom", "empty.jsp" );
    }
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  public List makeGetAllCharacteristicServiceCall( Long domainId )
  {
    return getUserCharacteristicService().getAllCharacteristics();
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }
}

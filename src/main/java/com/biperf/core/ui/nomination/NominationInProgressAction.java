
package com.biperf.core.ui.nomination;

import static com.biperf.core.domain.enums.nomination.NominationsInProgressConstants.CLAIM_ID;
import static com.biperf.core.domain.enums.nomination.NominationsInProgressConstants.DATE_STARTED;
import static com.biperf.core.domain.enums.nomination.NominationsInProgressConstants.NOMINEE;
import static com.biperf.core.domain.enums.nomination.NominationsInProgressConstants.NOM_PROMO_NAME;
import static com.biperf.core.domain.enums.nomination.NominationsInProgressConstants.PROMO_ID;
import static com.biperf.core.domain.enums.nomination.NominationsInProgressConstants.REMOVE_PARAM_NAME;
import static com.biperf.core.domain.enums.nomination.NominationsPaginationConstants.EDIT;
import static com.biperf.core.domain.enums.nomination.NominationsPaginationConstants.REMOVE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.nomination.NominationClaimsInProgressSortColumn;
import com.biperf.core.domain.enums.nomination.NominationsInProgressConstants;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.common.MessagesArray;
import com.biperf.core.value.nomination.NominationsInProgressListValueObject;
import com.biperf.core.value.nomination.NominationsInProgressValueObject;
import com.objectpartners.cms.util.CmsResourceBundle;

public class NominationInProgressAction extends BaseRecognitionAction
{

  public ActionForward display( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( "display" );
  }

  public ActionForward getInProgressNoms( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long userId = UserManager.getUserId();
    NomInProgressForm form = (NomInProgressForm)actionForm;

    NominationClaimsInProgressSortColumn sortColumn = NominationClaimsInProgressSortColumn.getByDisplayValue( request.getParameter( "sortedOn" ) );

    int pageNumber = 1;
    if ( request.getParameter( "currentPage" ) != null )
    {
      pageNumber = Integer.parseInt( request.getParameter( "currentPage" ) );
    }

    form.setCurrentPage( pageNumber );
    form.setStartIndex( ( pageNumber - 1 ) * NominationsInProgressListValueObject.PAGE_SIZE );
    form.setEndIndex( pageNumber * NominationsInProgressListValueObject.PAGE_SIZE );

    String sortBy = "desc";
    if ( request.getParameter( "sortedBy" ) != null )
    {
      sortBy = request.getParameter( "sortedBy" );
    }
    form.setSortBy( sortBy );

    String sortOn = sortColumn.getDbColumnName();
    form.setSortOn( sortOn );

    List<NominationsInProgressValueObject> progressClaims = new ArrayList<NominationsInProgressValueObject>();
    int inProgCount = getNominationClaimService().getInProgressNominationClaimsCount( userId );
    if ( inProgCount > 0 )
    {
      Map<String, Object> prcParameters = new HashMap<>();
      prcParameters.put( "submitterId", userId );
      prcParameters.put( "rowNumStart", form.getStartIndex() );
      prcParameters.put( "rowNumEnd", form.getEndIndex() + 1 );
      prcParameters.put( "sortedBy", sortBy );
      prcParameters.put( "sortedOn", sortOn );

      Map<String, Object> prcResults = getNominationClaimService().getNominationClaimsInProgress( prcParameters );
      progressClaims = (List<NominationsInProgressValueObject>)prcResults.get( "p_out_data" );
    }

    NominationsInProgressListViewObject view = new NominationsInProgressListViewObject( inProgCount,
                                                                                        form.getCurrentPage(),
                                                                                        NominationsInProgressListValueObject.PAGE_SIZE,
                                                                                        getTabularData( request, progressClaims ) );
    view.setStartIndex( form.getStartIndex() );
    view.setEndIndex( form.getEndIndex() );
    view.setSortedOnIndex( sortColumn.getColumnIndex() );
    view.setSortBy( sortBy );

    writeAsJsonToResponse( view, response );
    return null;
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward remove( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> params;

    try
    {
      params = ClientStateSerializer.deserialize( request.getParameter( REMOVE_PARAM_NAME ), ClientStatePasswordManager.getPassword() );
      Long claimId = (Long)params.get( CLAIM_ID );
      ClaimService claimService = getClaimService();
      claimService.deleteClaim( claimId.longValue() );
    }
    catch( Exception e )
    {
      writeAppErrorAsJsonResponse( response, e );
    }

    MessagesArray messagesArray = new MessagesArray();
    messagesArray.getMessages().add( "Success" );
    writeAsJsonToResponse( messagesArray, response, ContentType.JSON );

    return null;
  }

  private NominationsInProgressListTabularDataViewObject getTabularData( HttpServletRequest request, List<NominationsInProgressValueObject> progressClaims )
  {
    NominationsInProgressListTabularDataViewObject tabularData = new NominationsInProgressListTabularDataViewObject();

    int i = 1;
    for ( NominationsInProgressValueObject nomClaimInProgress : progressClaims )
    {
      Map<String, Long> clientStateParams = new HashMap<String, Long>( 2 );
      clientStateParams.put( CLAIM_ID, nomClaimInProgress.getClaimId() );
      clientStateParams.put( PROMO_ID, nomClaimInProgress.getPromotionId() );

      String dateCreated = DateUtils.toDisplayString( nomClaimInProgress.getDateCreated() );
      String editUrl = getEditUrl( request, clientStateParams );
      String removeParams = ClientStateSerializer.serialize( clientStateParams, ClientStatePasswordManager.getPassword() );

      tabularData.getResults()
          .add( new NominationsInProgressViewObject( nomClaimInProgress.getClaimId(), i, dateCreated, nomClaimInProgress.getName(), nomClaimInProgress.getPromotionName(), editUrl, removeParams ) );
      i++;
    }
    tabularData.setMeta( getMetadata() );

    return tabularData;
  }

  private NominationsInProgressListMetaViewObject getMetadata()
  {
    NominationsInProgressListMetaViewObject meta = new NominationsInProgressListMetaViewObject();

    NominationsInProgressListColumnViewObject column1 = new NominationsInProgressListColumnViewObject();
    column1.setId( 1 );
    column1.setSortable( true );
    column1.setName( DATE_STARTED );
    column1.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.inprogress.DATE_STARTED" ) );
    meta.getColumns().add( column1 );

    NominationsInProgressListColumnViewObject column2 = new NominationsInProgressListColumnViewObject();
    column2.setId( 2 );
    column2.setSortable( true );
    column2.setName( NOMINEE );
    column2.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.inprogress.NOMINEE_NAME" ) );
    meta.getColumns().add( column2 );

    NominationsInProgressListColumnViewObject column3 = new NominationsInProgressListColumnViewObject();
    column3.setId( 3 );
    column3.setSortable( true );
    column3.setName( NOM_PROMO_NAME );
    column3.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.inprogress.PROMOTION_NAME" ) );
    meta.getColumns().add( column3 );

    NominationsInProgressListColumnViewObject column4 = new NominationsInProgressListColumnViewObject();
    column4.setId( 4 );
    column4.setSortable( false );
    column4.setName( EDIT );
    column4.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.inprogress.EDIT" ) );
    meta.getColumns().add( column4 );

    NominationsInProgressListColumnViewObject column5 = new NominationsInProgressListColumnViewObject();
    column5.setId( 5 );
    column5.setSortable( false );
    column5.setName( REMOVE );
    column5.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "nomination.inprogress.REMOVE" ) );
    meta.getColumns().add( column5 );

    return meta;
  }

  protected String getEditUrl( HttpServletRequest request, Map<String, Long> clientStateParams )
  {
    Long promotionId = null;
    Long claimId = null;
    if ( clientStateParams.get( PROMO_ID ) != null )
    {
      promotionId = Long.valueOf( clientStateParams.get( PROMO_ID ) );
    }
    if ( clientStateParams.get( CLAIM_ID ) != null )
    {
      claimId = Long.valueOf( clientStateParams.get( CLAIM_ID ) );
    }
    Map paramMap = new HashMap();
    paramMap.put( "promotionId", promotionId );
    paramMap.put( "claimId", claimId );

    String url = ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                       "/" + NominationsInProgressConstants.MODIFY_INPROGRESS_NOM_CLAIM_URL,
                                                       paramMap );
    return url;
  }

  public ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );

  }

  public CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

}

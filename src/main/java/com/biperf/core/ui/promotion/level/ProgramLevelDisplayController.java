
package com.biperf.core.ui.promotion.level;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.awardslinqDataRetriever.client.MerchLevelProduct;
import com.biperf.awardslinqDataRetriever.client.MerchlinqLevelData;
import com.biperf.awardslinqDataRetriever.client.ProductGroupDescription;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromoMerchCountryAssociationRequest;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.UserManager;

/**
 * ProgramLevelDisplayController.
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
 * <td>siemback</td>
 * <td>July 23, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class ProgramLevelDisplayController extends BaseController
{

  /**
   * Overridden from
   *
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
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
    String programId = (String)clientStateMap.get( "programId" );
    Long promoMerchCountryId = (Long)clientStateMap.get( "promoMerchCountryId" );
    String lastName = (String)clientStateMap.get( "lastName" );
    String firstName = (String)clientStateMap.get( "firstName" );

    if ( promoMerchCountryId != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
      PromoMerchCountry promoMerchCountry = getPromoMerchCountryService().getPromoMerchCountryByIdWithAssociations( promoMerchCountryId, associationRequestCollection );
      request.setAttribute( "country", promoMerchCountry.getCountry() );
      request.setAttribute( "promoMerchCountryLevels", promoMerchCountry.getLevels() );
    }

    AwardBanqMerchResponseValueObject data = getMerchLevelService().getMerchlinqLevelDataWebService( programId, true, true );
    MerchlinqLevelData merchData = getMerchLevelService().buildMerchLinqLevelData( data );
    request.setAttribute( "merchLevelData", getMerchlinqLevelData( merchData ) );
    // following code added to fix Bug 24737 -- START
    if ( UserManager.getUser().isParticipant() )
    {
      Long promotionId = null;
      try
      {
        promotionId = (Long)clientStateMap.get( "promotionIdToDisplayLevelNames" );
      }
      catch( ClassCastException e )
      {
        promotionId = new Long( (String)clientStateMap.get( "promotionIdToDisplayLevelNames" ) );
      }
      request.setAttribute( "promotionIdToDisplayLevelNames", promotionId );
      // get the User Country
      UserAddress userAddress = getUserService().getPrimaryUserAddress( UserManager.getUserId() );
      // get Promotion's Country List along with levels details
      AssociationRequestCollection arCollection = new AssociationRequestCollection();
      arCollection.add( new PromoMerchCountryAssociationRequest( PromoMerchCountryAssociationRequest.ALL_HYDRATION_LEVEL ) );
      List countryList = getPromoMerchCountryService().getPromoMerchCountriesByPromotionId( promotionId, arCollection );
      getMerchLevelService().mergeMerchLevelWithOMList( countryList );

      // Pick the right Country data from CountryList based on user country
      PromoMerchCountry promoMerchCountry = null;
      boolean isPAXCountryIsInPromoCountriesList = false;
      for ( int i = 0; i < countryList.size(); i++ )
      {
        promoMerchCountry = (PromoMerchCountry)countryList.get( i );
        if ( userAddress.getAddress().getCountry().getCountryCode().equals( promoMerchCountry.getCountry().getCountryCode() ) )
        {
          isPAXCountryIsInPromoCountriesList = true;
          break;
        }
      }
      // set the required Levels data in request.
      // Note:- if PAX Country is in Promo Countries then display corresponding Level names.
      // Example 'silver' , 'Gold' etc..,
      // If PAX country is not in Promo countries then display 'Level1', 'Level2','Level3' etc..,
      if ( isPAXCountryIsInPromoCountriesList )
      {
        request.setAttribute( "promoMerchCountryLevels", promoMerchCountry.getLevels() );
      }
    }
    // above code added to fix Bug 24737 -- END

    request.setAttribute( "programId", programId );
    request.setAttribute( "lastName", lastName );
    request.setAttribute( "firstName", firstName );
  }

  private String escapeColumnValue( Object rawValue )
  {
    String stringValue = StringUtils.trim( rawValue.toString() );

    if ( stringValue == null )
    {
      return null;
    }
    stringValue = StringUtils.replace( stringValue, "\"", "&quot" );

    return stringValue;

  }

  private MerchlinqLevelData getMerchlinqLevelData( MerchlinqLevelData data )
  {

    List list = (List)data.getLevels();
    Iterator listItr = null;
    if ( list != null )
    {
      listItr = list.iterator();
    }
    if ( listItr != null )
    {
      while ( listItr.hasNext() )
      {
        MerchLevel merchLevel = (MerchLevel)listItr.next();
        Collection col = (Collection)merchLevel.getProducts();
        if ( col != null )
        {
          Iterator itr = col.iterator();
          while ( itr.hasNext() )
          {
            MerchLevelProduct mlp = (MerchLevelProduct)itr.next();
            if ( !mlp.getProductGroupDescriptions().isEmpty() )
            {
              ProductGroupDescription productDescription = mlp.getProductGroupDescriptions().get( UserManager.getLocale() );
              productDescription.setDescription( escapeColumnValue( productDescription.getDescription() ) );

              String copy = "";
              if ( !StringUtils.isEmpty( productDescription.getCopy() ) )
              {
                copy = escapeColumnValue( copy.length() > 300 ? copy.substring( 0, 300 ) + "......" : copy );
              }
              productDescription.setCopy( copy );
            }
          }
        }
      }
    }
    return data;

  }

  private MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private PromoMerchCountryService getPromoMerchCountryService()
  {
    return (PromoMerchCountryService)getService( PromoMerchCountryService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}

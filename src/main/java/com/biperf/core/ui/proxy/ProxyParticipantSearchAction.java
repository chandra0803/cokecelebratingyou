
package com.biperf.core.ui.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantSearchListView;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.ui.search.ParticipantAutoCompleteSearchAction;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.CountryValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class ProxyParticipantSearchAction extends ParticipantAutoCompleteSearchAction
{

  @Override
  protected boolean getIsLocked( Participant pax )
  {

    Long userId = UserManager.getUserId();
    if ( userId.equals( pax.getId() ) )
    {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

  @Override
  protected String getUrlEdit()
  {
    return PageConstants.PARTICIPANT_PROXY_NEW;
  }

  @Override
  protected ParticipantSearchListView buildParticipantSearchListView( Promotion promotion,  List<Participant> paxResults, Long promotionId, List<Map<Long, CountryValueBean>> countryResults )
  {
    ParticipantSearchListView paxSearchListView = new ParticipantSearchListView();
    WebErrorMessage messages = new WebErrorMessage();
    List<WebErrorMessage> messageList = new ArrayList<WebErrorMessage>();
    String refineSearchMessage = CmsResourceBundle.getCmsBundle().getString( "participant.search.NO_PARTICIPANTS" );
    List<Proxy> proxies = getProxyService().getProxiesByUserId( UserManager.getUserId() );
    if ( paxResults == null || paxResults.isEmpty() )
    {
      messages.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      messages.setText( refineSearchMessage );
      messageList.add( messages );
      paxSearchListView.setMessages( messageList );
    }
    Map<String, String> codes = new HashMap<String, String>();
    if ( paxResults != null )
    {
      for ( Object object : paxResults )
      {
        if ( object instanceof Participant )
        {
          Participant pax = (Participant)object;
          ParticipantSearchView bean = new ParticipantSearchView( pax );

          // override properties, customizable to each search
          bean.setSelected( getIsSelected( proxies, pax ) );
          bean.setLocked( getIsLocked( pax ) );
          bean.setUrlEdit( getUrlEdit() );
          bean.setCountryRatio( getBudgetConversionRatio( pax.getId(), UserManager.getUserId(), promotionId ).doubleValue() );

          for ( Map<Long, CountryValueBean> countryInfo : countryResults )
          {
            if ( countryInfo != null )
            {
              CountryValueBean country = countryInfo.get( pax.getId() );

              // Add country names to Map to avoid multiple CM Calls
              String countryName = "";
              if ( country != null )
              {
                String assetCode = country.getCmAssetCode();
                if ( codes.containsKey( assetCode ) )
                {
                  countryName = codes.get( assetCode );
                }
                else
                {
                  countryName = ContentReaderManager.getText( country.getCmAssetCode(), "COUNTRY_NAME" );
                  codes.put( assetCode, countryName );
                }

                bean.setCountryName( countryName );
                bean.setCountryCode( country.getCountryCode() );
              }
            }
          }
          paxSearchListView.getParticipants().add( bean );
        }
      }
    }
    return paxSearchListView;
  }

  protected boolean getIsSelected( List<Proxy> proxies, Participant pax )
  {
    boolean alreadyAddedAsProxy = false;
    for ( Proxy proxy : proxies )
    {
      if ( proxy.getProxyUser().getId().equals( pax.getId() ) )
      {
        alreadyAddedAsProxy = true;
      }
    }
    return alreadyAddedAsProxy;
  }

  protected static ProxyService getProxyService()
  {
    return (ProxyService)getService( ProxyService.BEAN_NAME );
  }

}

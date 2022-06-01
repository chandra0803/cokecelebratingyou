
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.utils.UserManager;

/**
 * 
 * @author dudam
 * @since Apr 29, 2013
 * @version 1.0
 */
public class ShopInternationalMerchModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private CountryService countryService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( participant.isParticipant() )
    {
      if ( !canShowShopTile( participant, parameterMap ) )
      {
        return false;
      }
      return checkCurrentUserSupplier();
    }
    return false;
  }

  private boolean checkCurrentUserSupplier()
  {
    Country country = getUserService().getPrimaryUserAddressCountry( UserManager.getUser().getUserId() );
    return this.countryService.checkUserSupplier( country.getCountryCode(), Supplier.BII );
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  private boolean canShowShopTile( Participant participant, Map<String, Object> parameterMap )
  {
    return getMainContentService().checkShowShopORTravel();
  }

}

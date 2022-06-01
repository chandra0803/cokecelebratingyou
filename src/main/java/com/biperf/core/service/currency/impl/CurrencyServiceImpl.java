/**
 * 
 */

package com.biperf.core.service.currency.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.biperf.core.dao.currency.CurrencyDAO;
import com.biperf.core.domain.currency.Currency;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.currency.CurrencyService;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.domain.enums.DataTypeEnum;

/**
 * @author dudam
 * @since Dec 19, 2014
 * @version 1.0
 */
public class CurrencyServiceImpl implements CurrencyService
{
  private CurrencyDAO currencyDAO;
  private CMAssetService cmAssetService;

  @Override
  public Currency save( Currency currency ) throws ServiceErrorException
  {
    String currencyName = currency.getCurrencyName();
    if ( StringUtil.isNullOrEmpty( currency.getCmAssetName() ) )
    {
      // Create and set asset to currency
      String assetName = this.cmAssetService.getUniqueAssetCode( Currency.CURRENCY_CMASSET_PREFIX );
      currency.setCmAssetName( assetName );
    }
    CMDataElement cmDataName = new CMDataElement( Currency.CURRENCY_CMASSET_CURRENCY_NAME, Currency.CURRENCY_CMASSET_NAME, currencyName, false, DataTypeEnum.STRING );
    List<CMDataElement> elements = new ArrayList<CMDataElement>();
    elements.add( cmDataName );
    cmAssetService.createOrUpdateAsset( Currency.CURRENCY_SECTION_CODE,
                                        Currency.CURRENCY_CMASSET_TYPE_NAME,
                                        Currency.CURRENCY_CMASSET_CURRENCY_NAME,
                                        currency.getCmAssetName(),
                                        elements,
                                        Locale.US,
                                        null );
    return this.currencyDAO.save( currency );
  }

  @Override
  public Currency getCurrencyById( Long id )
  {
    return this.currencyDAO.getCurrencyById( id );
  }

  @Override
  public Currency getCurrencyByCode( String code )
  {
    return this.currencyDAO.getCurrencyByCode( code );
  }

  @Override
  public List getAllActiveCurrency()
  {
    return this.currencyDAO.getAllActiveCurrency();
  }

  @Override
  public List getAllCurrency()
  {
    return this.currencyDAO.getAllCurrency();
  }

  @Override
  public boolean isCurrencyNameUnique( String currencyName, Long currentCurrencyId, String locale )
  {

    return this.currencyDAO.isCurrencyNameUnique( currencyName, currentCurrencyId, locale );
  }

  public void setCurrencyDAO( CurrencyDAO currencyDAO )
  {
    this.currencyDAO = currencyDAO;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

}

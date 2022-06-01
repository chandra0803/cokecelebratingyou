/**
 * 
 */

package com.biperf.core.dao.cashcurrency.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.biperf.core.dao.cashcurrency.CashCurrencyDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.domain.cashcurrency.CashCurrencyCurrent;
import com.biperf.core.domain.cashcurrency.CashCurrencyHistory;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.CashCurrencyBean;

/**
 * @author poddutur
 * 
 */
public class CashCurrencyDAOImplTest extends BaseDAOTest
{
  /**
   * Uses the ApplicationContextFactory to look up the CashCurrencyDAO
   * implementation.
   * 
   * @return CashCurrencyDAO
   */
  private CashCurrencyDAO getCashCurrencyDAO()
  {
    return (CashCurrencyDAO)ApplicationContextFactory.getApplicationContext().getBean( "cashCurrencyDAO" );
  }

  /**
   * Test getting all CashCurrencyCurrent in the database.
   */
  public void testGetAllOldCashCurrencyList()
  {
    CashCurrencyDAO cashCurrencyDAO = getCashCurrencyDAO();

    List<CashCurrencyCurrent> expectedList = new ArrayList<CashCurrencyCurrent>();

    CashCurrencyCurrent cashCurrencyCurrent1 = buildStaticCashCurrencyCurrentDomainObject();
    cashCurrencyDAO.saveCashCurrencyCurrent( cashCurrencyCurrent1 );

    CashCurrencyCurrent cashCurrencyCurrent2 = buildStaticCashCurrencyCurrentDomainObject();
    cashCurrencyDAO.saveCashCurrencyCurrent( cashCurrencyCurrent2 );

    expectedList.add( cashCurrencyCurrent1 );
    expectedList.add( cashCurrencyCurrent2 );

    flushAndClearSession();

    List<CashCurrencyCurrent> actualList = cashCurrencyDAO.getAllOldCashCurrencyList();

    assertTrue( "The list of CashCurrencyCurrent from the database doesn't contain the expected set", actualList.containsAll( expectedList ) );

  }

  public void testgetCashCurrencyFromCurrentAndHistory()
  {
    CashCurrencyDAO cashCurrencyDAO = getCashCurrencyDAO();
    CashCurrencyCurrent expectedCashCurrencyCurrent = buildStaticCashCurrencyCurrentDomainObject();
    expectedCashCurrencyCurrent.setEffectiveDate( new Date() );
    cashCurrencyDAO.saveCashCurrencyCurrent( expectedCashCurrencyCurrent );
    flushAndClearSession();
    CashCurrencyBean bean = cashCurrencyDAO.getCashCurrencyFromCurrentAndHistory( "USD", "USD", DateUtils.getDateAfterNumberOfDays( new Date(), 10 ) );
    assertTrue( bean != null );
  }

  /**
   * Tests to save CashCurrencyHistory
   */
  public void testSaveCashCurrencyHistory()
  {
    // save a new CashCurrencyCurrent
    CashCurrencyDAO cashCurrencyDAO = getCashCurrencyDAO();

    CashCurrencyHistory expectedCashCurrencyHistory = buildStaticCashCurrencyHistoryDomainObject();
    cashCurrencyDAO.saveCashCurrencyHistory( expectedCashCurrencyHistory );

    assertEquals( "Actual CashCurrencyHistory doesn't match with expected", expectedCashCurrencyHistory, cashCurrencyDAO.getCashCurrencyHistoryById( expectedCashCurrencyHistory.getId() ) );

    cashCurrencyDAO.saveCashCurrencyHistory( expectedCashCurrencyHistory );

    flushAndClearSession();
  }

  /**
   * creates a CashCurrencyHistory domain object
   * 
   * @param suffix
   * @return CashCurrencyHistory
   */
  public static CashCurrencyHistory buildStaticCashCurrencyHistoryDomainObject()
  {
    CashCurrencyHistory cashCurrencyHistory = new CashCurrencyHistory();
    cashCurrencyHistory.setbPomEnteredRate( 1 );
    cashCurrencyHistory.setEffectiveDate( DateUtils.toDate( "31-DEC-00", Locale.US ) );
    cashCurrencyHistory.setFromCurrency( "USD" );
    cashCurrencyHistory.setRateDiv( 1 );
    cashCurrencyHistory.setRateMult( 1 );
    cashCurrencyHistory.setToCurrency( "USD" );
    cashCurrencyHistory.setRtType( "CRRNT" );
    return cashCurrencyHistory;
  }

  /**
   * creates a CashCurrencyCurrent domain object
   * 
   * @param suffix
   * @return CashCurrencyCurrent
   */
  public static CashCurrencyCurrent buildStaticCashCurrencyCurrentDomainObject()
  {
    CashCurrencyCurrent cashCurrencyCurrent = new CashCurrencyCurrent();
    cashCurrencyCurrent.setbPomEnteredRate( 1 );
    cashCurrencyCurrent.setEffectiveDate( DateUtils.toDate( "31-DEC-00", Locale.US ) );
    cashCurrencyCurrent.setFromCurrency( "USD" );
    cashCurrencyCurrent.setRateDiv( 1 );
    cashCurrencyCurrent.setRateMult( 1 );
    cashCurrencyCurrent.setToCurrency( "USD" );
    cashCurrencyCurrent.setRtType( "CRRNT" );

    return cashCurrencyCurrent;
  }

}

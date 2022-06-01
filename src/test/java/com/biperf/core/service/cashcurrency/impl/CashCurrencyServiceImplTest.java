/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.cashcurrency.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Mock;

import com.biperf.core.dao.cashcurrency.CashCurrencyDAO;
import com.biperf.core.domain.cashcurrency.CashCurrencyCurrent;
import com.biperf.core.domain.cashcurrency.CashCurrencyHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.value.CashCurrencyBean;

/**
 * 
 * @author poddutur
 * @since Mar 31, 2016
 */
public class CashCurrencyServiceImplTest extends BaseServiceTest
{
  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public CashCurrencyServiceImplTest( String test )
  {
    super( test );
  }

  /** cashCurrencyServiceImplementation */
  private CashCurrencyServiceImpl cashCurrencyService = new CashCurrencyServiceImpl();

  /** mocks */
  private Mock mockCashCurrencyDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockCashCurrencyDAO = new Mock( CashCurrencyDAO.class );
    cashCurrencyService.setCashCurrencyDAO( (CashCurrencyDAO)mockCashCurrencyDAO.proxy() );
  }

  public void testConvertCurrencyUSDToINR()
  {
    CashCurrencyBean bean = new CashCurrencyBean();
    bean.setbPomEnteredRate( new BigDecimal( "65.1234" ) );
    mockCashCurrencyDAO.expects( once() ).method( "getCashCurrencyFromCurrentAndHistory" ).will( returnValue( bean ) );

    // USD to INR
    BigDecimal convertdCurrency = cashCurrencyService.convertCurrency( "USD", "INR", new BigDecimal( "2" ), null );
    assertTrue( convertdCurrency != null );
    assertEquals( new BigDecimal( 130.25 ), convertdCurrency );
    mockCashCurrencyDAO.verify();
  }

  public void testConvertCurrencyUSDToUSD()
  {
    CashCurrencyBean bean = new CashCurrencyBean();
    bean.setRateMult( new BigDecimal( "65.1234" ) );
    bean.setRateDiv( new BigDecimal( "1.0000" ) );

    // USD to INR
    BigDecimal convertdCurrency = cashCurrencyService.convertCurrency( "USD", "USD", new BigDecimal( "2" ), null );
    assertTrue( convertdCurrency != null );
    final BigDecimal expected = new BigDecimal( 2.00 );
    assertEquals( expected.floatValue(), convertdCurrency.floatValue() );
  }

  public void testConvertCurrencyINRToUSD()
  {
    CashCurrencyBean bean = new CashCurrencyBean();
    bean.setbPomEnteredRate( new BigDecimal( "65.1234" ) );
    mockCashCurrencyDAO.expects( once() ).method( "getCashCurrencyFromCurrentAndHistory" ).will( returnValue( bean ) );

    // USD to INR
    BigDecimal convertdCurrency = cashCurrencyService.convertCurrency( "INR", "USD", new BigDecimal( "130.25" ), null );
    assertTrue( convertdCurrency != null );
    assertEquals( new BigDecimal( 2.00 ).floatValue(), convertdCurrency.floatValue() );
    mockCashCurrencyDAO.verify();
  }

  public static void main( String[] args )
  {
    BigDecimal a = new BigDecimal( "50.8595" );
    // a.setScale( 2 , RoundingMode.HALF_UP);
    // System.out.println(a.divide( new BigDecimal( "1" )) );

    System.out.println( a.divide( new BigDecimal( "1" ) ).setScale( 2, BigDecimal.ROUND_HALF_UP ).toPlainString() );

  }

  /**
   * Test deleting the Cash Currency Current through the service.
   * 
   * @throws ServiceErrorException
   * @throws ParseException 
   */
  public void testDeleteCashCurrencyCurrent() throws ServiceErrorException, ParseException
  {
    // Create the test CashCurrencyCurrent.
    CashCurrencyCurrent cashCurrencyCurrent = new CashCurrencyCurrent();
    cashCurrencyCurrent.setId( new Long( 6746 ) );
    cashCurrencyCurrent.setbPomEnteredRate( 1 );
    SimpleDateFormat formatter1 = new SimpleDateFormat( "dd-MMM-yy" );
    Date date1 = formatter1.parse( "31-DEC-00" );
    cashCurrencyCurrent.setEffectiveDate( date1 );
    cashCurrencyCurrent.setFromCurrency( "USD" );
    cashCurrencyCurrent.setRateDiv( 1 );
    cashCurrencyCurrent.setRateMult( 1 );
    cashCurrencyCurrent.setToCurrency( "USD" );
    cashCurrencyCurrent.setRtType( "CRRNT" );

    mockCashCurrencyDAO.expects( once() ).method( "deleteOldCashCurrency" ).with( same( cashCurrencyCurrent ) );

    // test the CashCurrencyService.deleteOldCashCurrency
    cashCurrencyService.deleteOldCashCurrency( cashCurrencyCurrent );

    // Verify the mockDAO deletes.
    mockCashCurrencyDAO.verify();
  }

  /**
   * Test adding the CashCurrencyCurrent through the service.
   * 
   * @throws ServiceErrorException
   * @throws ParseException 
   */
  public void testSaveCashCurrencyCurrent() throws ServiceErrorException, ParseException
  {
    CashCurrencyCurrent cashCurrencyCurrent = new CashCurrencyCurrent();
    cashCurrencyCurrent.setbPomEnteredRate( 1 );
    SimpleDateFormat formatter1 = new SimpleDateFormat( "dd-MMM-yy" );
    Date date1 = formatter1.parse( "31-DEC-00" );
    cashCurrencyCurrent.setEffectiveDate( date1 );
    cashCurrencyCurrent.setFromCurrency( "USD" );
    cashCurrencyCurrent.setRateDiv( 1 );
    cashCurrencyCurrent.setRateMult( 1 );
    cashCurrencyCurrent.setToCurrency( "USD" );
    cashCurrencyCurrent.setRtType( "CRRNT" );

    CashCurrencyCurrent savedCashCurrencyCurrent = new CashCurrencyCurrent();
    savedCashCurrencyCurrent.setId( new Long( 1 ) );
    savedCashCurrencyCurrent.setbPomEnteredRate( 1 );
    SimpleDateFormat formatter2 = new SimpleDateFormat( "dd-MMM-yy" );
    Date date2 = formatter2.parse( "31-DEC-00" );
    savedCashCurrencyCurrent.setEffectiveDate( date2 );
    savedCashCurrencyCurrent.setFromCurrency( "USD" );
    savedCashCurrencyCurrent.setRateDiv( 1 );
    savedCashCurrencyCurrent.setRateMult( 1 );
    savedCashCurrencyCurrent.setToCurrency( "USD" );
    savedCashCurrencyCurrent.setRtType( "CRRNT" );

    // insert a new cashCurrencyCurrent
    mockCashCurrencyDAO.expects( once() ).method( "saveCashCurrencyCurrent" ).with( same( cashCurrencyCurrent ) ).will( returnValue( savedCashCurrencyCurrent ) );

    // test Adding with the cashCurrencyService.saveCashCurrencyCurrent
    CashCurrencyCurrent returnedCashCurrencyCurrent = cashCurrencyService.saveCashCurrencyCurrent( cashCurrencyCurrent );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    assertEquals( "Actual returned CashCurrencyCurrent wasn't equal to what was expected", savedCashCurrencyCurrent, returnedCashCurrencyCurrent );
    mockCashCurrencyDAO.verify();
  }

  /**
   * Test adding the CashCurrencyHistory through the service.
   * 
   * @throws ServiceErrorException
   * @throws ParseException 
   */
  public void testSaveCashCurrencyHistory() throws ServiceErrorException, ParseException
  {
    CashCurrencyHistory cashCurrencyHistory = new CashCurrencyHistory();
    cashCurrencyHistory.setbPomEnteredRate( 1 );
    SimpleDateFormat formatter1 = new SimpleDateFormat( "dd-MMM-yy" );
    Date date1 = formatter1.parse( "31-DEC-00" );
    cashCurrencyHistory.setEffectiveDate( date1 );
    cashCurrencyHistory.setFromCurrency( "USD" );
    cashCurrencyHistory.setRateDiv( 1 );
    cashCurrencyHistory.setRateMult( 1 );
    cashCurrencyHistory.setToCurrency( "USD" );
    cashCurrencyHistory.setRtType( "CRRNT" );

    CashCurrencyHistory savedcashCurrencyHistory = new CashCurrencyHistory();
    savedcashCurrencyHistory.setId( new Long( 1 ) );
    savedcashCurrencyHistory.setbPomEnteredRate( 1 );
    SimpleDateFormat formatter2 = new SimpleDateFormat( "dd-MMM-yy" );
    Date date2 = formatter2.parse( "31-DEC-00" );
    savedcashCurrencyHistory.setEffectiveDate( date2 );
    savedcashCurrencyHistory.setFromCurrency( "USD" );
    savedcashCurrencyHistory.setRateDiv( 1 );
    savedcashCurrencyHistory.setRateMult( 1 );
    savedcashCurrencyHistory.setToCurrency( "USD" );
    savedcashCurrencyHistory.setRtType( "CRRNT" );

    // insert a new cashCurrencyHistory
    mockCashCurrencyDAO.expects( once() ).method( "saveCashCurrencyHistory" ).with( same( cashCurrencyHistory ) ).will( returnValue( savedcashCurrencyHistory ) );

    // test Adding with the cashCurrencyService.saveCashCurrencyHistory
    CashCurrencyHistory returnedCashCurrencyHistory = cashCurrencyService.saveCashCurrencyHistory( cashCurrencyHistory );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    assertEquals( "Actual returned CashCurrencyHistory wasn't equal to what was expected", savedcashCurrencyHistory, returnedCashCurrencyHistory );
    mockCashCurrencyDAO.verify();
  }

  public void testGetAllOldCashCurrencyList() throws ParseException
  {

    List<CashCurrencyCurrent> expectedList = getAllExpectedOldCashCurrencyList();

    mockCashCurrencyDAO.expects( once() ).method( "getAllOldCashCurrencyList" ).will( returnValue( expectedList ) );

    List<CashCurrencyCurrent> actualList = cashCurrencyService.getAllOldCashCurrencyList();

    assertTrue( "Actual set didn't contain expected set for getAllOldCashCurrencyList.", actualList.containsAll( expectedList ) );

    mockCashCurrencyDAO.verify();
  }

  public static List<CashCurrencyCurrent> getAllExpectedOldCashCurrencyList() throws ParseException
  {
    List<CashCurrencyCurrent> allOldCashCurrencyList = new ArrayList<CashCurrencyCurrent>();

    CashCurrencyCurrent valueBean = new CashCurrencyCurrent();
    valueBean.setId( new Long( 6747 ) );
    valueBean.setbPomEnteredRate( 1 );
    SimpleDateFormat formatter1 = new SimpleDateFormat( "dd-MMM-yy" );
    Date date1 = formatter1.parse( "15-JAN-08" );
    valueBean.setEffectiveDate( date1 );
    valueBean.setFromCurrency( "AUD" );
    valueBean.setRateDiv( 1 );
    valueBean.setRateMult( 1 );
    valueBean.setToCurrency( "AUD" );
    valueBean.setRtType( "CRRNT" );

    allOldCashCurrencyList.add( valueBean );

    CashCurrencyCurrent valueBean2 = new CashCurrencyCurrent();
    valueBean2.setId( new Long( 6748 ) );
    valueBean2.setbPomEnteredRate( 1 );
    SimpleDateFormat formatter2 = new SimpleDateFormat( "dd-MMM-yy" );
    Date date2 = formatter2.parse( "25-JAN-11" );
    valueBean.setEffectiveDate( date2 );
    valueBean2.setFromCurrency( "CAD" );
    valueBean2.setRateDiv( 1 );
    valueBean2.setRateMult( 1 );
    valueBean2.setToCurrency( "CAD" );
    valueBean2.setRtType( "CRRNT" );

    allOldCashCurrencyList.add( valueBean2 );

    CashCurrencyCurrent valueBean3 = new CashCurrencyCurrent();
    valueBean3.setId( new Long( 6749 ) );
    valueBean3.setbPomEnteredRate( (float)0.9953 );
    SimpleDateFormat formatter3 = new SimpleDateFormat( "dd-MMM-yy" );
    Date date3 = formatter3.parse( "25-JAN-11" );
    valueBean.setEffectiveDate( date3 );
    valueBean3.setFromCurrency( "USD" );
    valueBean3.setRateDiv( 1 );
    valueBean3.setRateMult( 0 );
    valueBean3.setToCurrency( "CAD" );
    valueBean3.setRtType( "CRRNT" );

    allOldCashCurrencyList.add( valueBean3 );

    return allOldCashCurrencyList;
  }
}

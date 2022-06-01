
package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biperf.core.dao.client.CokeClientDAO;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * TcccClientUtils
 * 
 * @author dudam
 * @since Feb 21, 2018
 * @version 1.0
 * 
 * This util is created as part of WIP #42701
 */
public class TcccClientUtils
{

  public static boolean isValidAwardAmount( CokeClientService cokeClientService, Long awardAmount, String currencyCode, Long maxUsdAwardAmount )
  {
    Double exchangeRate = cokeClientService.getCurrencyExchangeRate( currencyCode );
    Double convertedAmount = exchangeRate * awardAmount;
    return convertedAmount.longValue() <= maxUsdAwardAmount.longValue();
  }

  public static String convertToUSDString( CokeClientService cokeClientService, Long awardAmount, String currencyCode )
  {
    Double exchangeRate = cokeClientService.getCurrencyExchangeRate( currencyCode );
    Double convertedAmount = exchangeRate * awardAmount;
    return convertedAmount.longValue() + " " + "USD";
  }

  public static Long convertToUSD( CokeClientService cokeClientService, Long awardAmount, String currencyCode )
  {
    Double exchangeRate = cokeClientService.getCurrencyExchangeRate( currencyCode );
    Double convertedAmount = exchangeRate * awardAmount;
    return convertedAmount.longValue();
  }

  public static Long convertToUSD( CokeClientDAO cokeClientDao, Long awardAmount, String currencyCode )
  {
    Double exchangeRate = cokeClientDao.getCurrencyExchangeRate( currencyCode );
    Double convertedAmount = exchangeRate * awardAmount;
    return convertedAmount.longValue();
  }
  
  public static Long convertToPoints( CokeClientService cokeClientService, Long awardAmountUsd, Long userId )
  {
    Double userMediaValue = cokeClientService.getUserBudgetMediaValue( userId );
    Double convertedPoints = awardAmountUsd != 0 ? ( awardAmountUsd / userMediaValue ) : 0;
    return convertedPoints.longValue();
  }

  public static Long convertToPoints( CokeClientDAO cokeClientDAO, Long awardAmountUsd, Long userId )
  {
    Double userMediaValue = cokeClientDAO.getUserBudgetMediaValue( userId );
    Double convertedPoints = awardAmountUsd != 0 ? ( awardAmountUsd / userMediaValue ) : 0;
    return convertedPoints.longValue();
  }

  public static Long getMaxAllowedAward( CokeClientService cokeClientService, Long maxAwardAmount, String currencyCode )
  {
    Double exchangeRate = cokeClientService.getCurrencyExchangeRate( currencyCode );
    Double maxAmt = new Double( maxAwardAmount ) / exchangeRate;
    return maxAmt.longValue();
  }

  public static Long getMaxAllowedAward( CokeClientDAO cokeClientDAO, Long maxAwardAmount, String currencyCode )
  {
    Double exchangeRate = cokeClientDAO.getCurrencyExchangeRate( currencyCode );
    Double maxAmt = new Double( maxAwardAmount ) / exchangeRate;
    return maxAmt.longValue();
  }
  
  public static Long convertToPercentPoints( SystemVariableService systemVariableService, CokeClientService cokeClientService, Long awardAmount, Long userId )
  {
    Integer cashPercentage = systemVariableService.getPropertyByName( SystemVariableService.COKE_CASH_PERCENT_SPLIT ).getIntVal();
    Long cashPercentageValue = ( awardAmount * cashPercentage ) / 100;
    Long cashPointsPercentageValue = awardAmount - cashPercentageValue;
    Double userMediaValue = cokeClientService.getUserBudgetMediaValue( userId );
    Double convertedPoints = cashPointsPercentageValue != 0 ? ( cashPointsPercentageValue / userMediaValue ) : 0;
    return convertedPoints.longValue();
  }

  public static Long convertToPercentPoints( SystemVariableService systemVariableService, CokeClientDAO cokeClientDAO, Long awardAmount, Long userId )
  {
    Integer cashPercentage = systemVariableService.getPropertyByName( SystemVariableService.COKE_CASH_PERCENT_SPLIT ).getIntVal();
    Long cashPercentageValue = ( awardAmount * cashPercentage ) / 100;
    Long cashPointsPercentageValue = awardAmount - cashPercentageValue;
    Double userMediaValue = cokeClientDAO.getUserBudgetMediaValue( userId );
    Double convertedPoints = cashPointsPercentageValue != 0 ? ( cashPointsPercentageValue / userMediaValue ) : 0;
    return convertedPoints.longValue();
  }

  public static Long convertToPercentCash( SystemVariableService systemVariableService, Long awardAmount )
  {
    Integer cashPercentage = systemVariableService.getPropertyByName( SystemVariableService.COKE_CASH_PERCENT_SPLIT ).getIntVal();
    return ( awardAmount * cashPercentage ) / 100;
  }

  public static boolean cashAllowed( SystemVariableService systemVariableService, UserService userService, Long participantId )
  {
    String jobGrades = systemVariableService.getPropertyByName( SystemVariableService.COKE_CASH_JOB_GRADE_AND_BELOW ).getStringVal();
    String[] jobGradesArray = jobGrades.split( "," );
    List<String> jobGradesList = new ArrayList<>( Arrays.asList( jobGradesArray ) );
    String userJobGrade = userService.getUserJobGradeCharValue( participantId );
    return !StringUtil.isNullOrEmpty( userJobGrade ) && ( jobGradesList.contains( userJobGrade ) );
  }

}

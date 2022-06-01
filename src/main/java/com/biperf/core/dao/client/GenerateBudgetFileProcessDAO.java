package com.biperf.core.dao.client;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * 
 * MCProcessDAO.
 * 
 * @author bethke
 * @since Dec 29, 2011
 */
public interface GenerateBudgetFileProcessDAO extends DAO
{
  public static final String BEAN_NAME = "generateBudgetFileProcessDAO";

  @SuppressWarnings("rawtypes")
public Map callGenerateBudgetFileProc( String orgUnits, Integer level );
  
  @SuppressWarnings("rawtypes")
public Map callGenerateBudgetFileWDProc( String orgUnits, Integer level );  
 
}

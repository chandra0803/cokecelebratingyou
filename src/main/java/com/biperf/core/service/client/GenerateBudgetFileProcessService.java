package com.biperf.core.service.client;

import java.util.Map;

import com.biperf.core.service.SAO;

public interface GenerateBudgetFileProcessService extends SAO
{
	  /**
	   * BEAN_NAME for referencing in tests and spring config files.
	   */
	  public final String BEAN_NAME = "generateBudgetFileProcessService";

	   /**
	   */
	  @SuppressWarnings("rawtypes")
	  public Map callGenerateBudgetFileProc( String orgUnits, Integer level );
	  
	  public Map callGenerateBudgetFileWDProc( String orgUnits, Integer level );	  
}
package com.biperf.core.service.client.impl;

import java.util.Map;





import com.biperf.core.dao.client.GenerateBudgetFileProcessDAO;
import com.biperf.core.service.client.GenerateBudgetFileProcessService;


public class GenerateBudgetFileProcessServiceImpl implements GenerateBudgetFileProcessService
{
  
  private GenerateBudgetFileProcessDAO generateBudgetFileProcessDAO;
  

  public GenerateBudgetFileProcessDAO getGenerateBudgetFileProcessDAO() 
  {
	return generateBudgetFileProcessDAO;
  }

  public void setGenerateBudgetFileProcessDAO(
		GenerateBudgetFileProcessDAO generateBudgetFileProcessDAO) 
  {
	this.generateBudgetFileProcessDAO = generateBudgetFileProcessDAO;
  }


@SuppressWarnings("rawtypes")
public Map callGenerateBudgetFileProc( String orgUnits, Integer level )
  {
	  return generateBudgetFileProcessDAO.callGenerateBudgetFileProc( orgUnits, level );
  }

@SuppressWarnings("rawtypes")
public Map callGenerateBudgetFileWDProc( String orgUnits, Integer level )
  {
	  return generateBudgetFileProcessDAO.callGenerateBudgetFileWDProc( orgUnits, level );
  }
    
}

package com.biperf.core.service.termsandcondition.impl;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.termsandcondition.TermsAndConditionService;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 *  AutoCompleteService implementation 
 */

public class TermsAndConditionServiceImpl implements TermsAndConditionService
{
  
  private static final Log logger = LogFactory.getLog( TermsAndConditionServiceImpl.class );

  private SystemVariableService systemVariableService; 
  
  public String getTermsAndConditionText()
  {
    
    return MessageFormat.format( ContentReaderManager.getText( "participant.preference.edit", "TERMS_AND_CONDITIONS_INSTRUCTIONS" ),
                                 new Object[] { getSystemVariableService().getPropertyByName( SystemVariableService.PRODUCT_CLIENT_NAME ).getStringVal().toString() } );
  }
  

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }
  
}

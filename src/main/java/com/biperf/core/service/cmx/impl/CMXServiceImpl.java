
package com.biperf.core.service.cmx.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.exception.DataException;
import com.biperf.core.service.cmx.CMXRepositoryFactory;
import com.biperf.core.service.cmx.CMXService;
import com.biperf.core.value.cmx.v1.cmx.CMXTranslateRequest;

@Service( "cmxServiceImpl" )
public class CMXServiceImpl implements CMXService
{

  @Autowired
  private CMXRepositoryFactory cmxRepo;

  @Override
  public Map<String, String> getTranslation( String locale, List<String> keys ) throws DataException
  {
    String cmxFormatLocale = locale.replace( "_", "-" );
    CMXTranslateRequest cmxTranslateRequest = new CMXTranslateRequest();
    cmxTranslateRequest.setKeys( keys );
    List<String> localeList = new LinkedList<String>();
    localeList.add( cmxFormatLocale );
    cmxTranslateRequest.setLocales( localeList );
    Map<String, Map<String, String>> response = cmxRepo.getRepo().getTranslation( cmxTranslateRequest );

    if ( response != null && response.get( cmxFormatLocale ) != null )
    {
      Map<String, String> content = response.get( cmxFormatLocale );
      return content;
    }
    else
    {
      return null;
    }

  }
}

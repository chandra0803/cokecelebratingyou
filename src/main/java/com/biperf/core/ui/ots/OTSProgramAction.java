/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.ots;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.ui.BaseAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.vo.ots.OTSProgramVO;

/**
 * TODO Javadoc for OTSAdministrationAction.
 * 
 * @author rajadura
 * @since Nov 20, 2017
 * 
 */
public class OTSProgramAction extends BaseAction
{

  private static final Log logger = LogFactory.getLog( OTSProgramAction.class );

  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    List<OTSProgramVO> otsProgramList = getOTSProgramService().getOTSProgram();
    List<OTSProgramVO> completedPrograms = otsProgramList.stream().filter( p -> "completed".equals( p.getProgramStatus() ) ).collect( Collectors.toList() );
    List<OTSProgramVO> inCompletedPrograms = otsProgramList.stream().filter( p -> "incompleted".equals( p.getProgramStatus() ) ).collect( Collectors.toList() );
    Collections.sort( completedPrograms, ASCE_COMPARATOR );
    Collections.sort( inCompletedPrograms, ASCE_COMPARATOR );
    request.setAttribute( "complete", completedPrograms );
    request.setAttribute( "incomplete", inCompletedPrograms );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private static Comparator<OTSProgramVO> ASCE_COMPARATOR = new Comparator<OTSProgramVO>()
  {
    public int compare( OTSProgramVO c1, OTSProgramVO c2 )
    {
      return c1.getProgramNumber().compareTo( c2.getProgramNumber() );
    }
  };

  protected OTSProgramService getOTSProgramService()
  {
    return (OTSProgramService)getService( OTSProgramService.BEAN_NAME );
  }

}

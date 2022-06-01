/*
 Copyright (c) 2002, Matthias Bauer, LIVINGLOGIC AG, BAYREUTH
 All rights reserved.

 Redistribution and use in source and binary forms, with or without 
 modification, are permitted provided that the following conditions are 
 met:

 1. Redistributions of source code must retain the above copyright 
 notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright 
 notice, this list of conditions and the following disclaimer in the 
 documentation and/or other materials provided with the distribution.
 3. The end-user documentation included with the redistribution, if
 any, must include the following acknowlegement:
 "This product includes software developed by 
 Matthias Bauer, LIVINGLOGIC AG, BAYREUTH 
 (http://www.livinglogic.de/)."
 Alternately, this acknowlegement may appear in the software itself,
 if and wherever such third-party acknowlegements normally appear.
 4. Neither the name of Matthias Bauer, nor the LIVINGLOGIC AG nor the 
 names of the other contributors may be used to endorse or promote 
 products derived from this software without specific prior written
 permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.
 */

package com.biperf.core.ui.tiles;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.InvalidCancelException;
import org.apache.struts.config.ForwardConfig;

import com.livinglogic.struts.workflow.WorkflowRequestProcessorLogic;
import com.livinglogic.struts.workflow.WorkflowRequestProcessorLogicAdapter;

/**
 * The workflow request processor through which any action request is run in order to get the chance
 * to do the workflow state checks. The logic is not implemented here, but in
 * WorkflowRequestProcessorLogic.
 * 
 * @author M. Bauer
 *
 */

public class BeaconWorkflowTilesRequestProcessor extends BeaconTilesRequestProcessor implements WorkflowRequestProcessorLogicAdapter
{
  /**
   * The WorkflowRequestProcessingLogic instance we are using
   */
  WorkflowRequestProcessorLogic logic;

  public void processForwardConfig( HttpServletRequest request, HttpServletResponse response, ForwardConfig forward ) throws IOException, ServletException
  {
    super.processForwardConfig( request, response, forward );
  }

  public HttpServletRequest processMultipart( HttpServletRequest request )
  {
    return super.processMultipart( request );
  }

  public String processPath( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    return super.processPath( request, response );
  }

  public void processLocale( HttpServletRequest request, HttpServletResponse response )
  {
    super.processLocale( request, response );
  }

  public void processContent( HttpServletRequest request, HttpServletResponse response )
  {
    super.processContent( request, response );
  }

  public void processNoCache( HttpServletRequest request, HttpServletResponse response )
  {
    super.processNoCache( request, response );
  }

  public boolean processPreprocess( HttpServletRequest request, HttpServletResponse response )
  {
    return super.processPreprocess( request, response );
  }

  public ActionMapping processMapping( HttpServletRequest request, HttpServletResponse response, String path ) throws IOException
  {
    return super.processMapping( request, response, path );
  }

  public boolean processRoles( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping ) throws IOException, ServletException
  {
    return super.processRoles( request, response, mapping );
  }

  public ActionForm processActionForm( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping )
  {
    return super.processActionForm( request, response, mapping );
  }

  public void processPopulate( HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping ) throws ServletException
  {
    super.processPopulate( request, response, form, mapping );
  }

  public boolean processValidate( HttpServletRequest request, HttpServletResponse response, ActionForm form, ActionMapping mapping ) throws IOException, ServletException
  {
    try
    {
      return super.processValidate( request, response, form, mapping );
    }
    catch( InvalidCancelException e )
    {
      return false;
    }
  }

  public boolean processForward( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping ) throws IOException, ServletException
  {
    return super.processForward( request, response, mapping );
  }

  public boolean processInclude( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping ) throws IOException, ServletException
  {
    return super.processInclude( request, response, mapping );
  }

  public Action processActionCreate( HttpServletRequest request, HttpServletResponse response, ActionMapping mapping ) throws IOException
  {
    return super.processActionCreate( request, response, mapping );
  }

  public ActionForward processActionPerform( HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping ) throws IOException, ServletException
  {
    return super.processActionPerform( request, response, action, form, mapping );
  }

  /**
   * <p>
   * Process an <code>HttpServletRequest</code> and create the corresponding
   * <code>HttpServletResponse</code>.
   * </p>
   * 
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a processing exception occurs
   */
  public void process( HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    if ( null == logic )
    {
      logic = new WorkflowRequestProcessorLogic( this );
    }
    logic.process( request, response );
  }
}

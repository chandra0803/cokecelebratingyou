package com.biperf.core.ui.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.client.TcccQcardBatch;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.client.TcccQcardBatchService;

import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class TccQcardBatchSaveAction extends BaseDispatchAction {
	private static final Log logger = LogFactory.getLog(TccQcardBatchSaveAction.class);

	public ActionForward unspecified(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		return display(actionMapping, actionForm, request, response);
	}

	public ActionForward display(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		String forwardTo = ActionConstants.DISPLAY_FORWARD;
		TcccQcardBatch tcccQcardBatch;

		TcccQcardBtachForm tcccQcardBtachForm = (TcccQcardBtachForm) actionForm;

		Long qcardBatchId = tcccQcardBtachForm.getQcardBatchId();

		tcccQcardBtachForm.setMethod("save");
		if (qcardBatchId != null) {
			tcccQcardBatch = getTcccQcardBatchService().getQcardBatchById(qcardBatchId);
			tcccQcardBtachForm.setMode("edit");
			tcccQcardBtachForm.load(tcccQcardBatch);
		} else
			tcccQcardBtachForm.setMode("add");

		return mapping.findForward(forwardTo);
	}

	private TcccQcardBatchService getTcccQcardBatchService() {
		return (TcccQcardBatchService) getService(TcccQcardBatchService.BEAN_NAME);
	}

	public ActionForward save(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		ActionMessages errors = new ActionMessages();
		String forwardTo = ActionConstants.SUCCESS_FORWARD;
		TcccQcardBatch tcccQcardBatch;
		TcccQcardBtachForm tcccQcardBtachForm = (TcccQcardBtachForm) actionForm;

		if (tcccQcardBtachForm.getQcardBatchId() != null && tcccQcardBtachForm.getQcardBatchId() > 0) {
			tcccQcardBatch = getTcccQcardBatchService().getQcardBatchById(tcccQcardBtachForm.getQcardBatchId());
		} else {
			tcccQcardBatch = new TcccQcardBatch();
			List batchList = getTcccQcardBatchService().getQCardBatchByBatchNbr(tcccQcardBtachForm.getBatchNumber());
		      if (batchList.size() > 0)
		      {
		        errors.add( ActionMessages.GLOBAL_MESSAGE,
		                    new ActionMessage( "OnTheSpot.error.validation.BATCH_NUMBER_EXISTS" ) );
		        saveErrors( request, errors );
		        return mapping.findForward( ActionConstants.FAIL_FORWARD ); 
		      }
		}
		
		

		tcccQcardBatch.setId(tcccQcardBtachForm.getQcardBatchId());
		tcccQcardBatch.setBatchNumber(tcccQcardBtachForm.getBatchNumber());
		tcccQcardBatch.setDivisionKey(tcccQcardBtachForm.getDivisionKey());
		tcccQcardBatch.setWorkCountry(tcccQcardBtachForm.getWorkCountry());
		tcccQcardBatch.setVersion(tcccQcardBtachForm.getVersion());
		try {
			getTcccQcardBatchService().saveQcardBatch(tcccQcardBatch);
		}

		catch (ServiceErrorException e) {
			logger.debug(e);
			List serviceErrors = e.getServiceErrors();
			ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors(serviceErrors, errors);
		}

		return mapping.findForward(forwardTo);

	}
}

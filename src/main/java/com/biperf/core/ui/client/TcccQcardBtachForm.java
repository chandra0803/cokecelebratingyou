
package com.biperf.core.ui.client;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.client.TcccQcardBatch;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.claimform.ClaimFormStepElementForm;
import com.biperf.core.ui.constants.ActionConstants;
import com.objectpartners.cms.util.ContentReaderManager;

public class TcccQcardBtachForm extends BaseForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1331663132072910880L;
	private Long qcardBatchId;
	private static final Log logger = LogFactory.getLog(TcccQcardBtachForm.class);

	private String method;
	private String batchNumber;
	private String divisionKey;
	private String workCountry;
	private long version;
	private String mode;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Long getQcardBatchId() {
		return qcardBatchId;
	}

	public void setQcardBatchId(Long qcardBatchId) {
		this.qcardBatchId = qcardBatchId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getDivisionKey() {
		return divisionKey;
	}

	public void setDivisionKey(String divisionKey) {
		this.divisionKey = divisionKey;
	}

	public String getWorkCountry() {
		return workCountry;
	}

	public void setWorkCountry(String workCountry) {
		this.workCountry = workCountry;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		batchNumber = "";
		divisionKey = "";
		workCountry = "";
		qcardBatchId = null;

	}

	public void load(TcccQcardBatch tcccQcardBatch) {
		this.batchNumber = tcccQcardBatch.getBatchNumber();
		this.workCountry = tcccQcardBatch.getWorkCountry();
		this.divisionKey = tcccQcardBatch.getDivisionKey();
		this.version = tcccQcardBatch.getVersion();
		this.qcardBatchId = tcccQcardBatch.getId();
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public ActionErrors validate(ActionMapping anActionMapping, HttpServletRequest aRequest) {
		final String METHOD_NAME = "validate";
		logger.info(">>> " + METHOD_NAME);
		//String regex = "[a-zA-Z0-9]+";

		ActionErrors actionErrors = actionErrors = new ActionErrors();

		if (this.batchNumber == null || this.batchNumber.equals(""))

			actionErrors.add(ActionConstants.ERROR_MESSAGE_PROPERTY,
					new ActionMessage("OnTheSpot.error.validation.BATCH_NUMBER_REQUIRED"));
		if (this.divisionKey == null || this.divisionKey.equals(""))

			actionErrors.add(ActionConstants.ERROR_MESSAGE_PROPERTY,
					new ActionMessage("OnTheSpot.error.validation.DIVISION_KEY_REQUIRED"));
		if (this.workCountry == null || this.workCountry.equals(""))

			actionErrors.add(ActionConstants.ERROR_MESSAGE_PROPERTY,
					new ActionMessage("OnTheSpot.error.validation.WORK_COUNTRY_REQUIRED"));
		if (( this.divisionKey.length() > 20 )
				&& (this.divisionKey != null && !this.divisionKey.equals("")))

			actionErrors.add(ActionConstants.ERROR_MESSAGE_PROPERTY,
					new ActionMessage("OnTheSpot.error.validation.INVALID_DIVISION_KEY"));
		if ((!(this.workCountry.matches("[a-zA-Z]+")) || !(this.workCountry.length() == 2) )&& (this.workCountry != null && !this.workCountry.equals("")))

			actionErrors.add(ActionConstants.ERROR_MESSAGE_PROPERTY,
					new ActionMessage("OnTheSpot.error.validation.INVALID_WORK_COUNTRY"));
		return actionErrors;
	}

}

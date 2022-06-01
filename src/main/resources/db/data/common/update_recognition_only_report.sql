BEGIN
	UPDATE report
	set IS_ACTIVE = 0
	 WHERE CM_ASSET_CODE in ('report.cash.budget.balance','report.recognition.purlactivity','report.awarditemselection.byorg','report.awardlevelactivity.byorg');
END;

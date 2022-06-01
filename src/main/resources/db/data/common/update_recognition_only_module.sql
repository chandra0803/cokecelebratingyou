BEGIN
	UPDATE MODULE_APP
	set IS_ACTIVE = 0
	WHERE NAME not in ('Billboard','News','On The Spot','Public Recognition','Banner Ads','Custom Display','RPM Manager');
END;

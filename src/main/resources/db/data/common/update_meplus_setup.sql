BEGIN
	UPDATE MODULE_APP
	set IS_ACTIVE = 0
	WHERE NAME not in ('Badges','Browse Awards','Celebration','DIY Travel','Events','Great Deal','International Catalog','Merchandise','News','PURL (Managers only)','Plateau Awards Redeem','Purl Celebrate','Real Deal','Resource Center');
END;

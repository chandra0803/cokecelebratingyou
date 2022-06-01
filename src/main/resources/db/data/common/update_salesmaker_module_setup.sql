BEGIN
	UPDATE MODULE_APP
	set IS_ACTIVE = 0
	WHERE APP_AUDIENCE_TYPE not in ('onTheSpotCardModule','newsModule','banner','challengepointModule','SSICreatorContestsModule','SSIManagerModule','SSIParticipantModule','SSICreatorModule','goalquestModule','challengepointManagerModule','goalquestManagerModule','newsModule','managertoolkit','approvalsModule','heroModule');
END;
BEGIN
	UPDATE os_propertyset
	set BOOLEAN_VAL = 0
	WHERE ENTITY_NAME in ('install.productclaims','install.quizzes','install.nominations','install.survey','install.wellness','install.goalquest','install.challengepoint','install.ssi','install.leaderboard','install.throwdown');
END;

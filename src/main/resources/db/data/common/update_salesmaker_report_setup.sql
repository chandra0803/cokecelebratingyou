BEGIN
	UPDATE report
    set IS_ACTIVE = 0
    WHERE REPORT_CODE not in ('cpSelection','cpManager','cpAchievement','cpProgress','cpSummary','awardsByPax','awardsByOrg','badgeActivityByOrg','enrollment',
    'hierarchyExport','participantExport','individualActivity','loginActivityPax','loginActivityOrg','gcSelection','gcManager','gcAchievement','gcProgress',
    'gcSummary','quizActivity','quizAnalysis','throwdownActivityByPax');
END;
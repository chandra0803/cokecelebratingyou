BEGIN
    UPDATE os_propertyset
    set BOOLEAN_VAL = 0
    WHERE ENTITY_NAME in ('install.recognition','purl.available','self.enrollment.enabled','install.survey','roster.management.available','install.nominations','public.rec.wall.feed.enabled','instantpoll','celebration','install.quizzes','install.productclaims','install.engagement','install.leaderboard','work.happier');
END;
BEGIN

   rk_all; -- This will kill all the existing database connection for current user. This is needed as the seed data for key is cached for already looged in sessions.
   -- For local databases we do not have this procedure, hence this step will fail, just ignore this message and make sure you create new database session.

END;
/
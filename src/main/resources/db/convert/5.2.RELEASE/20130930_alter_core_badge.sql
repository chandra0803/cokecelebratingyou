ALTER TABLE promotion ADD CONSTRAINT badge_setup_id_fk
  FOREIGN KEY (badge_setup_id) REFERENCES badge (badge_id)
/
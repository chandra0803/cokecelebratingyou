ALTER TABLE activity ADD CONSTRAINT activity_product_fk
  FOREIGN KEY (product_id) REFERENCES product(product_id)
/
ALTER TABLE payout_calculation_audit
  ADD CONSTRAINT payout_calculation_audit_fk_4
  FOREIGN KEY (promo_payout_group_id) REFERENCES promo_payout_group(promo_payout_group_id)
/

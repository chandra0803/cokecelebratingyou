DELETE FROM process_role
      WHERE role_id IN (SELECT role_id
                          FROM role
                         WHERE CODE IN ('ContentApprover',
                                        'ContentEditor',
                                        'CustomContentManager',
                                        'CSR',
                                        'CSR_MGR',
                                        'FileApprover',
                                        'FileManager',
                                        'SecurityAdministrator',
                                        'TRANSLATOR',
                                        'CLIENT_ADMIN',
                                        'STANDARD_CLIENT_ADMIN',
                                        'STANDARD_CLIENT_ADMIN_PAX',
                                        'STANDARD_CLIENT_ADMIN_PROMO',
                                        'STANDARD_CLIENT_ADMIN_COMM',
                                        'STANDARD_CLIENT_ADMIN_RPT',
                                        'SPECIAL_REPORT_ACCESS'))
/

DELETE FROM user_type_role
      WHERE role_id IN (SELECT role_id
                          FROM role
                         WHERE code IN ('ContentApprover',
                                        'ContentEditor',
                                        'CustomContentManager',
                                        'CSR',
                                        'CSR_MGR',
                                        'FileApprover',
                                        'FileManager',
                                        'SecurityAdministrator',
                                        'TRANSLATOR',
                                        'CLIENT_ADMIN',
                                        'CustomContentManager',
                                        'STANDARD_CLIENT_ADMIN',
                                        'STANDARD_CLIENT_ADMIN_PAX',
                                        'STANDARD_CLIENT_ADMIN_PROMO',
                                        'STANDARD_CLIENT_ADMIN_COMM',
                                        'STANDARD_CLIENT_ADMIN_RPT',
                                        'SPECIAL_REPORT_ACCESS'))
/

DELETE FROM user_role
      WHERE role_id IN (SELECT role_id
                          FROM role
                         WHERE code IN ('ContentApprover',
                                        'ContentEditor',
                                        'CustomContentManager',
                                        'CSR',
                                        'CSR_MGR',
                                        'FileApprover',
                                        'FileManager',
                                        'SecurityAdministrator',
                                        'TRANSLATOR',
                                        'CLIENT_ADMIN',
                                        'STANDARD_CLIENT_ADMIN',
                                        'STANDARD_CLIENT_ADMIN_PAX',
                                        'STANDARD_CLIENT_ADMIN_PROMO',
                                        'STANDARD_CLIENT_ADMIN_COMM',
                                        'STANDARD_CLIENT_ADMIN_RPT',
                                        'SPECIAL_REPORT_ACCESS'))
/

DELETE FROM role
      WHERE code IN ('ContentApprover',
                                        'ContentEditor',
                                        'CustomContentManager',
                                        'CSR',
                                        'CSR_MGR',
                                        'FileApprover',
                                        'FileManager',
                                        'SecurityAdministrator',
                                        'TRANSLATOR',
                                        'CLIENT_ADMIN',
                                        'STANDARD_CLIENT_ADMIN',
                                        'STANDARD_CLIENT_ADMIN_PAX',
                                        'STANDARD_CLIENT_ADMIN_PROMO',
                                        'STANDARD_CLIENT_ADMIN_COMM',
                                        'STANDARD_CLIENT_ADMIN_RPT',
                                        'SPECIAL_REPORT_ACCESS')
/
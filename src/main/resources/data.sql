-- ── Projects ────────────────────────────────────────────────────────────────
INSERT IGNORE INTO projects (name, client, fy, status, quote_gross, collection_received, exp_material, exp_labour, exp_subcontract, exp_consultants, exp_miscellaneous) VALUES
('Padma Shyam res Perungudi',           'Padma Shyam Suryodev',      '2025-2026', 'active',    1215840.00,  610000.00,  399363.00, 157761.00,      0.00, 39600.00, 35730.00),
('Ravi Kumar Villa — Adyar',            'Ravi Kumar Sharma',         '2025-2026', 'active',    2450000.00, 1837500.00,  875200.00, 298500.00, 162000.00, 68000.00, 38750.00),
('Meenakshi Apt Interiors — Velachery', 'Meenakshi Rajendran',       '2025-2026', 'completed',  875000.00,  875000.00,  294500.00, 112800.00,      0.00, 33000.00, 17640.00),
('Sundar Tech Office Fit-out — Nungambakkam', 'Sundar Pichai Enterprises', '2025-2026', 'active', 3820000.00, 1910000.00, 648000.00, 196000.00, 524000.00, 88000.00, 49500.00),
('Lakshmi Narayan res — Besant Nagar',  'Lakshmi Narayan Iyer',      '2025-2026', 'active',    1680000.00,  840000.00,  312500.00, 118400.00,      0.00, 56000.00, 22800.00),
('Priya Boutique Showroom Refit — T Nagar', 'Priya Fashion House',   '2025-2026', 'completed',  990000.00,  990000.00,  318400.00, 129600.00,  96000.00, 27500.00, 16200.00),
('Anand Residence — Porur',             'Anand Krishnamurthy',       '2025-2026', 'active',    1540000.00,  770000.00,  198600.00,  64200.00,      0.00, 52000.00, 14800.00),
('Kavitha Dental Clinic — Chromepet',   'Dr. Kavitha Subramanian',   '2025-2026', 'onhold',     620000.00,  310000.00,  148200.00,  58400.00,      0.00, 24000.00, 12600.00);

-- ── Expense Items — Padma Shyam res Perungudi (project_id = 1) ──────────────
INSERT IGNORE INTO expense_items (project_id, category, description, party_name, month_year, ref_no, pwj_gross, gst_percent, pwj_gst_amount, pwj_total_payable, vendor_gross, vendor_gst_percent, vendor_gst_amount, vendor_total_payable, payment_date, payment_against, paid_amount, paid_to, remarks) VALUES
-- MATERIAL
(1,'MATERIAL','Carpentry material','Jayam Enterprises','Mar-26','PSR-001',77424.61,18,13936.43,91361.04,77424.61,18,13936.43,91361.04,'2026-04-28','PO',91361.00,'DBS',NULL),
(1,'MATERIAL','Hardware materials','Royal Hardware collection','Mar-26','PSR-005',10045.00,18,1808.10,11853.10,10045.00,18,1808.10,11853.10,'2026-04-04','PO',11853.00,'Royal Hardware collection','100% advance'),
(1,'MATERIAL','Main panel door & frame','MSJ Door Palace','Mar-26','PSR-009',25000.00,0,0.00,25000.00,25000.00,0,0.00,25000.00,'2026-04-16','PO',25000.00,'MSJ Door Palace',NULL),
(1,'MATERIAL','Carpentry material','KPS Traders','Apr-26','PSR-007',23578.81,18,4244.19,27823.00,23578.81,18,4244.19,27823.00,NULL,NULL,0.00,NULL,NULL),
(1,'MATERIAL','Carpentry material','KPS Traders','Apr-26','PSR-010',118317.79,18,21297.20,139614.99,118317.79,18,21297.20,139614.99,NULL,NULL,0.00,NULL,NULL),
(1,'MATERIAL','Sliding glass door','Jay hardwares','Apr-26','PSRP-015',177000.00,18,31860.00,208860.00,177000.00,18,31860.00,208860.00,'2026-04-29','PO',208860.00,'Jay hardwares','Split: JW 1,00,000 + PO 1,08,860'),
(1,'MATERIAL','Hettich hardware materials','Royal Hardware collection','Apr-26','PSR-012',30646.34,18,5516.34,36162.68,30646.34,18,5516.34,36162.68,'2026-04-27','PO',36163.00,'Royal Hardware collection',NULL),
(1,'MATERIAL','Toughened glass with polish','Mountain Safety Glass','Apr-26','PSR-013',22142.72,18,3985.69,26128.41,22142.72,18,3985.69,26128.41,'2026-04-28','PO',26126.00,'Mountain Safety Glass','Split payments'),
(1,'MATERIAL','Catch laminate','KPS Traders','Apr-26','PSR-014',10254.20,18,1845.76,12099.96,10254.20,18,1845.76,12099.96,NULL,NULL,0.00,NULL,NULL),
-- LABOUR
(1,'LABOUR','Demolition - existing shutter removal','Viswalingam','Mar-26','0-60',3000.00,0,0.00,3000.00,3000.00,0,0.00,3000.00,'2026-03-24','JW',3000.00,'Viswalingam',NULL),
(1,'LABOUR','Civil touch work','Market labour','Mar-26','PSRP-003',2400.00,0,0.00,2400.00,2400.00,0,0.00,2400.00,'2026-03-28','JW',2400.00,'Jagan',NULL),
(1,'LABOUR','Putty touch up work','Local labour','Mar-26','0-87',1200.00,0,0.00,1200.00,1200.00,0,0.00,1200.00,'2026-03-28','JW',1200.00,'Jagan',NULL),
(1,'LABOUR','Fixing and installation work','Vishwa interiors','Mar-26','PSRP-002',197015.00,0,0.00,197015.00,197015.00,0,0.00,197015.00,'2026-04-27','WO',98506.00,'Vishwa','25% advance + 2nd installment'),
(1,'LABOUR','Door removal, plastering, demolition','Surendhar','Apr-26','PSRP-006',5000.00,0,0.00,5000.00,5000.00,0,0.00,5000.00,'2026-04-09','JW',5000.00,'Jagan',NULL),
(1,'LABOUR','Painting work','Murugesan','Apr-26','PSRP-008',98850.00,0,0.00,98850.00,98850.00,0,0.00,98850.00,'2026-04-16','WO',29655.00,'Murugesan','Balance 69,195 pending'),
(1,'LABOUR','Plastering work','Sivakumar','Apr-26','PSRP-011',18000.00,0,0.00,18000.00,18000.00,0,0.00,18000.00,'2026-04-21','JW',18000.00,'Siva',NULL),
-- CONSULTANTS
(1,'CONSULTANTS','Designing of interiors','Rk Architects','Apr-26','PSRP-004',40000.00,18,7200.00,47200.00,40000.00,18,7200.00,47200.00,'2026-04-02','WO',39600.00,'Rk Architects','1% TDS deducted'),
-- MISCELLANEOUS
(1,'MISCELLANEOUS','Pooja items transport','Aakash','Mar-26',NULL,300.00,0,0.00,300.00,300.00,0,0.00,300.00,'2026-03-14',NULL,300.00,'Aakash',NULL),
(1,'MISCELLANEOUS','Site Pooja','Engn Jagan','Mar-26',NULL,2000.00,0,0.00,2000.00,2000.00,0,0.00,2000.00,'2026-03-14',NULL,2000.00,'Engn Jagan',NULL),
(1,'MISCELLANEOUS','Carpentry material transport','Gunasekar','Mar-26','0-63',2000.00,0,0.00,2000.00,2000.00,0,0.00,2000.00,'2026-03-24','JW',2000.00,'Aakash','Jayam Enterprise to site'),
(1,'MISCELLANEOUS','Petty cash','Engn Jagan','Mar-26','0-65',5000.00,0,0.00,5000.00,5000.00,0,0.00,5000.00,'2026-03-24','JW',5000.00,'Engn Jagan',NULL),
(1,'MISCELLANEOUS','Petty cash','Engn Jagan','Mar-26',NULL,5000.00,0,0.00,5000.00,5000.00,0,0.00,5000.00,'2026-03-28',NULL,5000.00,'Engn Jagan',NULL),
(1,'MISCELLANEOUS','Carpentry material transport charges','Aakash','Apr-26',NULL,1430.00,0,0.00,1430.00,1430.00,0,0.00,1430.00,'2026-04-16',NULL,1430.00,'Akash',NULL),
(1,'MISCELLANEOUS','Petty cash','Engn Jagan','Apr-26','123',5000.00,0,0.00,5000.00,5000.00,0,0.00,5000.00,'2026-04-18','JW',5000.00,'Engn Jagan',NULL),
(1,'MISCELLANEOUS','Petty cash','Engn Jagan','Apr-26','139',5000.00,0,0.00,5000.00,5000.00,0,0.00,5000.00,'2026-04-27','JW',5000.00,'Engn Jagan',NULL),
(1,'MISCELLANEOUS','Petty cash','Engn Jagan','Apr-26','145',5000.00,0,0.00,5000.00,5000.00,0,0.00,5000.00,'2026-04-28','JW',5000.00,'Engn Jagan',NULL),
(1,'MISCELLANEOUS','Glass transport - Mountain Safety','Engn Jagan','Apr-26','143',2200.00,0,0.00,2200.00,2200.00,0,0.00,2200.00,'2026-04-28','JW',2200.00,'Engn Jagan',NULL),
(1,'MISCELLANEOUS','Loading & unloading charge glass','Engn Jagan','Apr-26','148',2800.00,0,0.00,2800.00,2800.00,0,0.00,2800.00,'2026-04-28','JW',2800.00,'Engn Jagan',NULL);

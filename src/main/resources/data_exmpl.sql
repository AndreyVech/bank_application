insert into users (id, name, password)
values (1,'user_atm_1','user_atm_1'),
(2, 'user_atm_2','user_atm_2'),
(3, 'user_web_1','user_web_1'),
(4, 'user_web_2','user_web_2'),
(5, 'user_app_1','user_app_1'),
(6, 'user_app_2','user_app_2');

insert into accounts (id, acc_num, saldo, user_id)
values
(1, '40817810100010000001', 50.00, 1),
(2, '40817810100020000001', 50.00, 2),
(3, '40817810100030000001', 53330.00, 3),
(4, '40817810100010000001', 0, 4),
(5, '40817810100010000001', 223450.40, 5),
(6, '40817810100010000001', 1150.00, 6),
(7, '40817810100010000002', 50000.00, 1);



select * from users

select * from accounts order by id
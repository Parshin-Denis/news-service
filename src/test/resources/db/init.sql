insert into categories (id, name) values (1, 'Politic'), (2, 'Culture'), (3, 'Economy');
insert into users (id, name, password) values (1, 'Admin', 'pass'), (2, 'User', 'pass');
insert into authorities (role_type, user_id) values ('ROLE_ADMIN', 1), ('ROLE_USER', 2);
insert into news (id, content, category_id, user_id) values
(1, 'first news', 1, 1), (2, 'second news', 2, 1), (3, 'third news', 3, 1), (4, 'forth news', 1, 2),
(5, 'fifth news', 2, 2);
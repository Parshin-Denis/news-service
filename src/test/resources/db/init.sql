insert into categories (name) values ('Politic'), ('Culture'), ('Economy');
insert into users (name, password) values ('Admin', 'pass'), ('User', 'pass');
insert into authorities (role_type, user_id) values ('ROLE_ADMIN', 1), ('ROLE_USER', 2);
insert into news (content, category_id, user_id) values
('first news', 1, 1), ('second news', 2, 1), ('third news', 3, 1), ('forth news', 1, 2), ('fifth news', 2, 2);
insert into comments (content, news_id, user_id) values ('comment1', 1, 1), ('comment2', 1, 2), ('comment3', 2, 1);
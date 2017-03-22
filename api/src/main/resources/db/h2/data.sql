INSERT INTO roles (id, name, description)
VALUES
  (1, 'ROLE_ADMIN', 'admin'),
  (2, 'ROLE_USER', 'user');

INSERT INTO persons (id, first_name, last_name, short_name, email, password, phone, birth_date, gender, created)
VALUES
  (1, 'Alex', 'Saunin', 'maniac', 'alsaunin@gmail.com', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '79211234567', '1984-03-23', 1, '2000-01-01'),
  (2, 'Vincent', 'Gigante', '', 'vinc@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1928-03-29', 1, '2000-01-11'),
  (3, 'Манька', 'Облигация', '', 'manyka@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1955-04-29', 2, '2000-01-08'),
  (5, 'Витька', 'Антибиотик', 'vitamin', 'vitya@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '79219999999', '1933-12-08', 1, '2000-01-12'),
  (6, 'Michael', 'Corleone', 'mikkey', 'm_korleone@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1920-10-15', 1, '2000-01-02'),
  (7, 'Vito', 'Corleone', 'vito', 'v_korleone@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1891-12-07', 1, '2000-01-03'),
  (8, 'Tony', 'Soprano', 'tony', 'tony@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1959-08-24', 1, '2000-01-04'),
  (9, 'Al', 'Capone', 'alkapone', 'alkapone@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1899-01-17', 1, '2000-01-05'),
  (10, 'Pablo', 'Escobar', 'pablo', 'pablo@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1949-12-01', 1, '2000-01-06'),
  (11, 'Johnie', 'Dillinger', 'jonny', 'jonny@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1903-06-22', 1, '2000-01-07'),
  (12, 'Mickey', 'Cohen', 'koen', 'koen@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1913-09-04', 1, '2000-01-09'),
  (13, 'Carlito', 'Brigante', 'carlito', 'brigante@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1930-01-01', 1, '2000-01-10'),
  (14, 'Шарко', 'Молодой', '', 'sharko@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1953-03-20', 1, '2000-01-13'),
  (15, 'Luky', 'Luciano', 'lucky', 'lucky@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1897-11-24', 1, '2000-01-14'),
  (16, 'Donnie', 'Brasco', 'donny', 'donny@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1940-04-25', 1, '2000-01-15'),
  (17, 'Vega', 'Vincent', 'delavega', 'vega@mail.ru', '$2a$10$CUrSYLzTe/zBeLTMMbvLluvMqykTmufwHYqiNQP6uVC0.PyMc/H8m', '76545465465', '1954-02-18', 1, '2000-01-01');

INSERT INTO user_roles (person_id, role_id) VALUES
  (1, 1), (1, 2), (2, 2), (3, 2), (5, 2), (6, 2), (7, 2), (8, 2), (9, 2), (10, 2), (11, 2), (12, 2), (13, 2), (14, 2), (15, 2), (16, 2), (17, 2);

INSERT INTO friends (person_id, friend_id) VALUES
  (1, 8), (2, 1), (1, 9), (1, 3), (1, 12), (1, 10), (1, 13), (1, 2), (5, 1), (1, 5), (15, 2), (15, 5), (15, 3), (16, 1),
  (16, 8), (16, 14), (1, 16), (17, 3), (17, 5), (17, 1), (1, 17);

INSERT INTO messages (id, posted, sender_id, recipient_id, body) VALUES
  (1, {ts '2016-07-20 00:00:00.0'}, 1, 5, 'Привет'),
  (2, {ts '2016-07-20 00:00:01.0'}, 5, 1, 'Привет'),
  (3, {ts '2016-07-20 12:02:43.0'}, 1, 5, 'Как дела?'),
  (4, {ts '2016-07-20 12:05:34.0'}, 5, 1, 'Нормально!\nА у тебя?'),
  (5, {ts '2016-07-20 12:06:11.0'}, 1, 5, 'Тоже вроде неплохо'),
  (6, {ts '2016-07-20 19:03:19.0'}, 5, 1, 'Сорри...\nМне пора бежать, борщ кипит'),
  (7, {ts '2016-07-20 19:51:11.0'}, 1, 5, 'Проверка\nСвязи'),
  (8, {ts '2016-08-06 16:49:58.0'}, 1, 5, 'Привет, Витамин!'),
  (9, {ts '2016-08-07 15:34:12.0'}, 5, 1, 'И тебе привет, коли не шутишь!'),
  (10, {ts '2016-12-18 10:10:35.0'}, 1, 5, 'Ты зачем Адвоката хлопнул?\nУ него же жена, ребенок...\nТаких как ты, редиской называли'),
  (11, {ts '2016-07-21 15:01:13.0'}, 1, 7, 'Hello Vito!\nHow''s your family?'),
  (12, {ts '2016-08-04 01:33:47.0'}, 7, 1, 'It''s all right. Though it seems, that I''m like an old tree stump!\nYears take their toll...'),
  (13, {ts '2016-07-26 00:40:52.0'}, 6, 1, 'Hi geek!'),
  (14, {ts '2016-07-26 01:13:42.0'}, 1, 6, 'Hi, man!'),
  (15, {ts '2016-07-26 01:14:54.0'}, 6, 1, 'How''s old socks?'),
  (16, {ts '2016-07-26 01:15:19.0'}, 1, 6, 'I''m Ok, and you?\nHow are you getting on?'),
  (17, {ts '2016-07-26 01:15:45.0'}, 6, 1, 'Seems good enougth.\nWhat are you up to?\nHow do you in T-Systems?'),
  (18, {ts '2016-08-14 14:07:43.0'}, 8, 1, 'Hi Alex, glad to meet you here!'),
  (19, {ts '2016-08-14 14:09:30.0'}, 1, 8, 'Howdy Antony, long time no seen you!'),
  (20, {ts '2016-08-14 14:20:08.0'}, 17, 1, 'Buddy, can you add me in your friend list? Thx');
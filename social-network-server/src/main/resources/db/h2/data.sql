INSERT INTO persons (id, first_name, last_name, short_name, email, phone, birth_date, gender, created)
VALUES
  (1, 'Alex', 'Saunin', 'maniac', 'alsaunin@gmail.com', '79211234567', '1984-03-23', 1, '2000-01-01'),
  (2, 'Vincent', 'Gigante', '', 'vinc@mail.ru', '76545465465', '1928-03-29', 1, '2000-01-11'),
  (3, 'Манька', 'Облигация', '', 'manyka@mail.ru', '76545465465', '1955-04-29', 2, '2000-01-08'),
  (5, 'Витька', 'Антибиотик', 'vitamin', 'vitya@mail.ru', '79219999999', '1933-12-08', 1, '2000-01-12'),
  (6, 'Michael', 'Corleone', 'mikkey', 'm_korleone@mail.ru', '76545465465', '1920-10-15', 1, '2000-01-02'),
  (7, 'Vito', 'Corleone', 'vito', 'v_korleone@mail.ru', '76545465465', '1891-12-07', 1, '2000-01-03'),
  (8, 'Tony', 'Soprano', 'tony', 'tony@mail.ru', '76545465465', '1959-08-24', 1, '2000-01-04'),
  (9, 'Al', 'Capone', 'alkapone', 'alkapone@mail.ru', '76545465465', '1899-01-17', 1, '2000-01-05'),
  (10, 'Pablo', 'Escobar', 'pablo', 'pablo@mail.ru', '76545465465', '1949-12-01', 1, '2000-01-06'),
  (11, 'Johnie', 'Dillinger', 'jonny', 'jonny@mail.ru', '76545465465', '1903-06-22', 1, '2000-01-07'),
  (12, 'Mickey', 'Cohen', 'koen', 'koen@mail.ru', '76545465465', '1913-09-04', 1, '2000-01-09'),
  (13, 'Carlito', 'Brigante', 'carlito', 'brigante@mail.ru', '76545465465', '1930-01-01', 1, '2000-01-10'),
  (14, 'Шарко', 'Молодой', '', 'sharko@mail.ru', '76545465465', '1953-03-20', 1, '2000-01-13'),
  (15, 'Luky', 'Luciano', 'lucky', 'lucky@mail.ru', '76545465465', '1897-11-24', 1, '2000-01-14'),
  (16, 'Donnie', 'Brasco', 'donny', 'donny@mail.ru', '76545465465', '1940-04-25', 1, '2000-01-15'),
  (17, 'Vega', 'Vincent', 'delavega', 'vega@mail.ru', '76545465465', '1954-02-18', 1, '2000-01-01');

INSERT INTO friends (person_id, friend_id) VALUES
  (1, 8), (2, 1), (1, 9), (1, 3), (1, 12), (1, 10), (1, 13), (1, 2), (5, 1), (1, 5), (15, 2), (15, 5), (15, 3), (16, 1),
  (16, 8), (16, 14), (1, 16), (17, 3), (17, 5), (17, 1), (1, 17);

INSERT INTO messages (id, posted, sender_id, recipient_id, body) VALUES
  (1, {ts '2012-09-17 00:00:00.0'}, 1, 5, 'Привет')

--   (, {ts '2012-09-17 00:00:00.0'}, , , ''),

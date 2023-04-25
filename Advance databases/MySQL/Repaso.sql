
/*Ejercicio 1*/
CREATE TABLE estudiantes (
  id INT PRIMARY KEY,
  nombre VARCHAR(255),
  apellido VARCHAR(255),
  edad INT,
  carrera VARCHAR(255)
);

/*Ejercicio 2*/
INSERT INTO estudiantes (id, nombre, apellido, edad, carrera)
VALUES (1, 'Juan', 'Pérez', 20, 'Informática'),
       (2, 'Ana', 'Sánchez', 22, 'Matemáticas'),
       (3, 'Pedro', 'García', 19, 'Informática'),
       (4, 'Laura', 'Rodríguez', 24, 'Física');

/*Ejercicio 3*/
CREATE TABLE cursos (
  id INT PRIMARY KEY,
  nombre VARCHAR(255),
  creditos INT,
  area VARCHAR(255)
);

/*Ejercicio 4*/
INSERT INTO cursos (id, nombre, creditos, area)
VALUES (1, 'Programación', 4, 'Informática'),
       (2, 'Cálculo', 3, 'Matemáticas'),
       (3, 'Álgebra', 3, 'Matemáticas'),
       (4, 'Física I', 4, 'Física');

/*Ejercicio 5*/
CREATE TABLE inscripciones (
  id INT PRIMARY KEY,
  estudiante_id INT,
  curso_id INT,
  nota DECIMAL(3,1),
  FOREIGN KEY (estudiante_id) REFERENCES estudiantes (id),
  FOREIGN KEY (curso_id) REFERENCES cursos (id)
);

/*Ejercicio 6*/
INSERT INTO inscripciones (id, estudiante_id, curso_id, nota)
VALUES (1, 1, 1, 8.5),
       (2, 1, 2, 7.0),
       (3, 2, 3, 9.5),
       (4, 3, 1, 6.0),
       (5, 4, 4, 8.0);

/*Ejercicio 7*/
SELECT nombre FROM estudiantes;

/*Ejercicio 8*/
SELECT nombre FROM estudiantes WHERE edad < 20;

/*Ejercicio 9*/
SELECT nombre FROM estudiantes WHERE carrera = 'Desarrollo de software';

/*Ejercicio 10*/
SELECT e.nombre, c.nombre
FROM estudiantes e
JOIN inscripciones i ON e.id = i.estudiante_id
JOIN cursos c ON c.id = i.curso_id;

/*Ejercicio 11*/
SELECT e.nombre, c.nombre, i.nota
FROM estudiantes e
JOIN inscripciones i ON e.id = i.estudiante_id
JOIN cursos c ON c.id = i.curso_id;

/*Ejercicio 12*/
SELECT nombre
FROM cursos
ORDER BY creditos ASC
LIMIT 2;

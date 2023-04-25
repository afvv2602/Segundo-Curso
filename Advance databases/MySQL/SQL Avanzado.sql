
/*Tabla estudiantes*/
CREATE TABLE estudiantes (
  id INT PRIMARY KEY,
  nombre VARCHAR(255),
  apellido VARCHAR(255),
  edad INT,
  carrera VARCHAR(255)
);

INSERT INTO estudiantes (id, nombre, apellido, edad, carrera)
VALUES (1, 'Juan', 'Pérez', 20, 'Informática'),
       (2, 'Ana', 'Sánchez', 22, 'Matemáticas'),
       (3, 'Pedro', 'García', 19, 'Informática'),
       (4, 'Laura', 'Rodríguez', 24, 'Física');

/*Tabla cursos*/
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

/*Tabla inscripciones*/
CREATE TABLE inscripciones (
  id INT PRIMARY KEY,
  estudiante_id INT,
  curso_id INT,
  nota DECIMAL(3,1),
  FOREIGN KEY (estudiante_id) REFERENCES estudiantes (id),
  FOREIGN KEY (curso_id) REFERENCES cursos (id)
);

INSERT INTO inscripciones (id, estudiante_id, curso_id, nota)
VALUES (1, 1, 1, 8.5),
       (2, 1, 2, 7.0),
       (3, 2, 3, 9.5),
       (4, 3, 1, 6.0),
       (5, 4, 4, 8.0);

/*Consulta 1*/
SELECT carrera, COUNT(*) AS num_estudiantes
FROM estudiantes
GROUP BY carrera;

/*Consulta 2*/
SELECT AVG(nota) AS media_notas
FROM inscripciones;

/*Consulta 3*/
SELECT curso_id, COUNT(*) AS num_inscritos
FROM inscripciones
GROUP BY curso_id;

/*Consulta 4*/
SELECT estudiante_id, COUNT(*) AS num_cursos
FROM inscripciones
GROUP BY estudiante_id
HAVING COUNT(*) > 1;

/*Consulta 5*/
SELECT curso_id, AVG(nota) AS media_notas
FROM inscripciones
GROUP BY curso_id;

/*Consulta 6*/
SELECT e.nombre, AVG(i.nota) AS media_notas
FROM estudiantes e
INNER JOIN inscripciones i ON e.id = i.estudiante_id
GROUP BY e.id
HAVING AVG(i.nota) > x;

/*Consulta 7*/
SELECT c.nombre AS Curso, COUNT(*) AS "Cantidad de estudiantes"
FROM cursos c
INNER JOIN inscripciones i ON c.id = i.curso_id
GROUP BY c.id
ORDER BY COUNT(*) DESC;

/*Consulta 8*/
SELECT e.nombre
FROM estudiantes e
INNER JOIN inscripciones i ON e.id = i.estudiante_id
WHERE i.curso_id = x;

/*Consulta 9*/
/*Esta consulta muestra la cantidad de estudiantes inscritos en cursos cuyo nombre contiene la palabra 
"matemáticas" (no sensible a mayúsculas y minúsculas),
 ordenados por la cantidad de estudiantes de forma descendente:*/
SELECT c.nombre AS Curso, COUNT(*) AS "Cantidad de estudiantes"
FROM cursos c
INNER JOIN inscripciones i ON c.id = i.curso_id
WHERE LOWER(c.nombre) LIKE '%matematicas%'
GROUP BY c.id
HAVING COUNT(*) >= 1
ORDER BY COUNT(*) DESC;

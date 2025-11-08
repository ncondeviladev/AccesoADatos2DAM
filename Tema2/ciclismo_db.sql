
-- Base de datos de ciclismo (nombres en español) - PostgreSQL (todo en esquema 'public')
-- 7 equipos x 7 ciclistas, 14 etapas, SIN vistas. Distancias y tiempos coherentes.
-- Fecha: 2025-10-29

-- Limpieza opcional (descomenta si quieres rehacer desde cero)
-- DROP TABLE IF EXISTS resultados_puerto CASCADE;
-- DROP TABLE IF EXISTS puertos CASCADE;
-- DROP TABLE IF EXISTS puntos_meta CASCADE;
-- DROP TABLE IF EXISTS resultados_sprint CASCADE;
-- DROP TABLE IF EXISTS sprints CASCADE;
-- DROP TABLE IF EXISTS resultados_etapa CASCADE;
-- DROP TABLE IF EXISTS etapas CASCADE;
-- DROP TABLE IF EXISTS carreras CASCADE;
-- DROP TABLE IF EXISTS ciclistas CASCADE;
-- DROP TABLE IF EXISTS equipos CASCADE;
-- DROP TYPE IF EXISTS categoria_puerto;
-- DROP TYPE IF EXISTS estado_resultado;
-- DROP TYPE IF EXISTS tipo_etapa;

-- =========================
-- Tipos enumerados
-- =========================
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tipo_etapa') THEN
        CREATE TYPE tipo_etapa AS ENUM ('Llana','Quebrada','Montaña','CRI','CRE');
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'estado_resultado') THEN
        CREATE TYPE estado_resultado AS ENUM ('FINALIZADO','DNF','DNS','DSQ');
    END IF;
END $$;

DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'categoria_puerto') THEN
        CREATE TYPE categoria_puerto AS ENUM ('HC','1','2','3','4');
    END IF;
END $$;

-- =========================
-- Tablas
-- =========================

CREATE TABLE equipos (
    id_equipo SERIAL PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE,
    pais   TEXT NOT NULL
);

CREATE TABLE ciclistas (
    id_ciclista SERIAL PRIMARY KEY,
    id_equipo   INT NOT NULL REFERENCES equipos(id_equipo) ON DELETE RESTRICT,
    nombre      TEXT NOT NULL,
    pais        TEXT NOT NULL,
    fecha_nac   DATE NOT NULL,
    UNIQUE (nombre, fecha_nac)
);

CREATE TABLE carreras (
    id_carrera  SERIAL PRIMARY KEY,
    nombre      TEXT NOT NULL,
    anio        INT NOT NULL CHECK (anio BETWEEN 1990 AND 2100),
    fecha_inicio DATE NOT NULL,
    fecha_fin    DATE NOT NULL,
    UNIQUE (nombre, anio),
    CHECK (fecha_fin >= fecha_inicio)
);

CREATE TABLE etapas (
    id_etapa   SERIAL PRIMARY KEY,
    id_carrera INT NOT NULL REFERENCES carreras(id_carrera) ON DELETE CASCADE,
    num_etapa  INT NOT NULL CHECK (num_etapa >= 1),
    fecha      DATE NOT NULL,
    salida     TEXT NOT NULL,
    llegada    TEXT NOT NULL,
    distancia_km NUMERIC(6,2) NOT NULL CHECK (distancia_km > 0),
    tipo       tipo_etapa NOT NULL,
    UNIQUE (id_carrera, num_etapa)
);

-- Trigger: valida que la fecha de la etapa está dentro de la carrera
CREATE OR REPLACE FUNCTION trg_valida_fecha_etapa_fn() RETURNS TRIGGER AS $$
DECLARE
  f_ini DATE;
  f_fin DATE;
BEGIN
  SELECT fecha_inicio, fecha_fin INTO f_ini, f_fin
  FROM carreras WHERE id_carrera = NEW.id_carrera;
  IF f_ini IS NULL THEN
    RAISE EXCEPTION 'Carrera % no existe', NEW.id_carrera USING ERRCODE = '23503';
  END IF;
  IF NOT (NEW.fecha BETWEEN f_ini AND f_fin) THEN
    RAISE EXCEPTION 'Fecha de etapa % fuera del rango [% - %] para carrera %',
      NEW.fecha, f_ini, f_fin, NEW.id_carrera USING ERRCODE = '23514';
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_valida_fecha_etapa
BEFORE INSERT OR UPDATE ON etapas
FOR EACH ROW
EXECUTE FUNCTION trg_valida_fecha_etapa_fn();

-- Resultados de etapa
CREATE TABLE resultados_etapa (
    id_etapa    INT NOT NULL REFERENCES etapas(id_etapa) ON DELETE CASCADE,
    id_ciclista INT NOT NULL REFERENCES ciclistas(id_ciclista) ON DELETE CASCADE,
    posicion    INT CHECK (posicion IS NULL OR posicion >= 1),
    tiempo      INTERVAL,
    diferencia  INTERVAL,
    estado      estado_resultado NOT NULL DEFAULT 'FINALIZADO',
    PRIMARY KEY (id_etapa, id_ciclista),
    CHECK ( (estado <> 'FINALIZADO' AND tiempo IS NULL AND posicion IS NULL)
         OR (estado = 'FINALIZADO' AND tiempo IS NOT NULL AND posicion IS NOT NULL) )
);

-- Sprints intermedios
CREATE TABLE sprints (
    id_sprint  SERIAL PRIMARY KEY,
    id_etapa   INT NOT NULL REFERENCES etapas(id_etapa) ON DELETE CASCADE,
    km         NUMERIC(6,2) NOT NULL CHECK (km >= 0),
    UNIQUE (id_etapa, km)
);

CREATE TABLE resultados_sprint (
    id_sprint   INT NOT NULL REFERENCES sprints(id_sprint) ON DELETE CASCADE,
    id_ciclista INT NOT NULL REFERENCES ciclistas(id_ciclista) ON DELETE CASCADE,
    posicion    INT NOT NULL CHECK (posicion >= 1),
    puntos      INT NOT NULL CHECK (puntos >= 0),
    PRIMARY KEY (id_sprint, id_ciclista),
    UNIQUE (id_sprint, posicion)
);

-- Puntos en meta (verde)
CREATE TABLE puntos_meta (
    id_etapa    INT NOT NULL REFERENCES etapas(id_etapa) ON DELETE CASCADE,
    id_ciclista INT NOT NULL REFERENCES ciclistas(id_ciclista) ON DELETE CASCADE,
    posicion    INT NOT NULL CHECK (posicion >= 1),
    puntos      INT NOT NULL CHECK (puntos >= 0),
    PRIMARY KEY (id_etapa, id_ciclista),
    UNIQUE (id_etapa, posicion)
);

-- Puertos y puntos de montaña
CREATE TABLE puertos (
    id_puerto  SERIAL PRIMARY KEY,
    id_etapa   INT NOT NULL REFERENCES etapas(id_etapa) ON DELETE CASCADE,
    nombre     TEXT NOT NULL,
    km         NUMERIC(6,2) NOT NULL CHECK (km >= 0),
    categoria  categoria_puerto NOT NULL,
    UNIQUE (id_etapa, nombre)
);

CREATE TABLE resultados_puerto (
    id_puerto   INT NOT NULL REFERENCES puertos(id_puerto) ON DELETE CASCADE,
    id_ciclista INT NOT NULL REFERENCES ciclistas(id_ciclista) ON DELETE CASCADE,
    posicion    INT NOT NULL CHECK (posicion >= 1),
    puntos      INT NOT NULL CHECK (puntos >= 0),
    PRIMARY KEY (id_puerto, id_ciclista),
    UNIQUE (id_puerto, posicion)
);

-- Índices
CREATE INDEX idx_ciclistas_equipo ON ciclistas(id_equipo);
CREATE INDEX idx_resultados_etapa_etapa ON resultados_etapa(id_etapa);
CREATE INDEX idx_resultados_etapa_ciclista ON resultados_etapa(id_ciclista);
CREATE INDEX idx_resultados_sprint_ciclista ON resultados_sprint(id_ciclista);
CREATE INDEX idx_puntos_meta_ciclista ON puntos_meta(id_ciclista);
CREATE INDEX idx_resultados_puerto_ciclista ON resultados_puerto(id_ciclista);

-- Equipos
INSERT INTO equipos (nombre, pais) VALUES
('Iberia Pro Cycling', 'ESP'),
('AlpenStars', 'SUI'),
('Nordic Hammer', 'NOR'),
('Blue Coast Racing', 'POR'),
('Gaul Vélocité', 'FRA'),
('Teutonic Wheels', 'GER'),
('Azzurri Sprint', 'ITA');

-- Ciclistas
INSERT INTO ciclistas (id_equipo, nombre, pais, fecha_nac) VALUES
(1, 'Carlos Ruiz', 'ESP', '1994-01-01'),
(1, 'Miguel Santos', 'ESP', '1995-02-02'),
(1, 'Javier León', 'ESP', '1996-03-03'),
(1, 'Óscar Vega', 'ESP', '1997-04-04'),
(1, 'Adrián Flores', 'ESP', '1998-05-05'),
(1, 'Pablo Núñez', 'ESP', '1999-06-06'),
(1, 'Sergio Arias', 'ESP', '1994-07-07'),
(2, 'Lukas Meier', 'SUI', '1994-01-01'),
(2, 'Noah Schmid', 'SUI', '1995-02-02'),
(2, 'Fabian Keller', 'SUI', '1996-03-03'),
(2, 'Timo Vogel', 'GER', '1997-04-04'),
(2, 'Jonas Ritter', 'SUI', '1998-05-05'),
(2, 'Marco Lüthi', 'SUI', '1999-06-06'),
(2, 'Sven Albrecht', 'GER', '1994-07-07'),
(3, 'Erik Johansen', 'NOR', '1994-01-01'),
(3, 'Mats Solberg', 'NOR', '1995-02-02'),
(3, 'Henrik Nilsen', 'NOR', '1996-03-03'),
(3, 'Sindre Aas', 'NOR', '1997-04-04'),
(3, 'Kasper Lunde', 'NOR', '1998-05-05'),
(3, 'Tobias Hauge', 'NOR', '1999-06-06'),
(3, 'Lars Eik', 'NOR', '1994-07-07'),
(4, 'Tiago Mendes', 'POR', '1994-01-01'),
(4, 'Rui Carvalho', 'POR', '1995-02-02'),
(4, 'Diogo Rocha', 'POR', '1996-03-03'),
(4, 'Pedro Faria', 'POR', '1997-04-04'),
(4, 'André Reis', 'POR', '1998-05-05'),
(4, 'Hugo Teixeira', 'POR', '1999-06-06'),
(4, 'Nuno Duarte', 'POR', '1994-07-07'),
(5, 'Louis Martin', 'FRA', '1994-01-01'),
(5, 'Thomas Dubois', 'FRA', '1995-02-02'),
(5, 'Antoine Leroy', 'FRA', '1996-03-03'),
(5, 'Julien Garnier', 'FRA', '1997-04-04'),
(5, 'Hugo Robert', 'FRA', '1998-05-05'),
(5, 'Nicolas Bernard', 'FRA', '1999-06-06'),
(5, 'Paul Fontaine', 'FRA', '1994-07-07'),
(6, 'Felix Weber', 'GER', '1994-01-01'),
(6, 'Moritz Braun', 'GER', '1995-02-02'),
(6, 'Leon Fischer', 'GER', '1996-03-03'),
(6, 'Jan Richter', 'GER', '1997-04-04'),
(6, 'Klaus Hofmann', 'GER', '1998-05-05'),
(6, 'Timo Berg', 'GER', '1999-06-06'),
(6, 'Arne Beck', 'GER', '1994-07-07'),
(7, 'Marco Bianchi', 'ITA', '1994-01-01'),
(7, 'Luca Moretti', 'ITA', '1995-02-02'),
(7, 'Giovanni Conti', 'ITA', '1996-03-03'),
(7, 'Andrea Rizzi', 'ITA', '1997-04-04'),
(7, 'Riccardo Sala', 'ITA', '1998-05-05'),
(7, 'Davide Neri', 'ITA', '1999-06-06'),
(7, 'Tommaso Galli', 'ITA', '1994-07-07');

INSERT INTO carreras (nombre, anio, fecha_inicio, fecha_fin) VALUES
('Vuelta Atlántica', 2025, '2025-05-01', '2025-05-14');

-- Etapas
INSERT INTO etapas (id_carrera, num_etapa, fecha, salida, llegada, distancia_km, tipo) VALUES
(1, 1, '2025-05-01', 'Lisboa', 'Setúbal', 168.00, 'Llana'),
(1, 2, '2025-05-02', 'Setúbal', 'Évora', 182.00, 'Quebrada'),
(1, 3, '2025-05-03', 'Évora', 'Beja', 160.00, 'Llana'),
(1, 4, '2025-05-04', 'Beja', 'Faro', 175.00, 'Quebrada'),
(1, 5, '2025-05-05', 'Faro', 'Portimão', 190.00, 'Montaña'),
(1, 6, '2025-05-06', 'Portimão', 'Lagos', 155.00, 'Llana'),
(1, 7, '2025-05-07', 'Lagos', 'Sagres', 40.00, 'CRI'),
(1, 8, '2025-05-08', 'Sagres', 'Sines', 170.00, 'Quebrada'),
(1, 9, '2025-05-09', 'Sines', 'Setúbal', 165.00, 'Llana'),
(1, 10, '2025-05-10', 'Setúbal', 'Lisboa', 192.00, 'Montaña'),
(1, 11, '2025-05-11', 'Lisboa', 'Sintra', 178.00, 'Quebrada'),
(1, 12, '2025-05-12', 'Sintra', 'Mafra', 150.00, 'Llana'),
(1, 13, '2025-05-13', 'Mafra', 'Torres Vedras', 158.00, 'Llana'),
(1, 14, '2025-05-14', 'Torres Vedras', 'Caldas da Rainha', 184.00, 'Quebrada');

-- Resultados de etapa y puntos de meta

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(1, 1, 1, '03:50:00', '00:00:00', 'FINALIZADO'),
(1, 2, 2, '03:50:01', '00:00:01', 'FINALIZADO'),
(1, 3, 3, '03:50:01', '00:00:01', 'FINALIZADO'),
(1, 4, 4, '03:50:04', '00:00:04', 'FINALIZADO'),
(1, 5, 5, '03:50:05', '00:00:05', 'FINALIZADO'),
(1, 6, 6, '03:50:06', '00:00:06', 'FINALIZADO'),
(1, 7, 7, '03:50:07', '00:00:07', 'FINALIZADO'),
(1, 8, 8, '03:50:00', '00:00:00', 'FINALIZADO'),
(1, 9, 9, '03:50:01', '00:00:01', 'FINALIZADO'),
(1, 10, 10, '03:50:02', '00:00:02', 'FINALIZADO'),
(1, 11, 11, '03:50:03', '00:00:03', 'FINALIZADO'),
(1, 12, 12, '03:50:04', '00:00:04', 'FINALIZADO'),
(1, 13, 13, '03:50:05', '00:00:05', 'FINALIZADO'),
(1, 14, 14, '03:50:06', '00:00:06', 'FINALIZADO'),
(1, 15, 15, '03:50:07', '00:00:07', 'FINALIZADO'),
(1, 16, 16, '03:50:00', '00:00:00', 'FINALIZADO'),
(1, 17, 17, '03:50:01', '00:00:01', 'FINALIZADO'),
(1, 18, 18, '03:50:02', '00:00:02', 'FINALIZADO'),
(1, 19, 19, '03:50:03', '00:00:03', 'FINALIZADO'),
(1, 20, 20, '03:50:04', '00:00:04', 'FINALIZADO'),
(1, 21, 21, '03:50:05', '00:00:05', 'FINALIZADO'),
(1, 22, 22, '03:50:06', '00:00:06', 'FINALIZADO'),
(1, 23, 23, '03:50:07', '00:00:07', 'FINALIZADO'),
(1, 24, 24, '03:50:00', '00:00:00', 'FINALIZADO'),
(1, 25, 25, '03:50:01', '00:00:01', 'FINALIZADO'),
(1, 26, 26, '03:50:02', '00:00:02', 'FINALIZADO'),
(1, 27, 27, '03:50:03', '00:00:03', 'FINALIZADO'),
(1, 28, 28, '03:50:04', '00:00:04', 'FINALIZADO'),
(1, 29, 29, '03:50:05', '00:00:05', 'FINALIZADO'),
(1, 30, 30, '03:50:06', '00:00:06', 'FINALIZADO'),
(1, 31, 31, '03:50:07', '00:00:07', 'FINALIZADO'),
(1, 32, 32, '03:50:00', '00:00:00', 'FINALIZADO'),
(1, 33, 33, '03:50:01', '00:00:01', 'FINALIZADO'),
(1, 34, 34, '03:50:02', '00:00:02', 'FINALIZADO'),
(1, 35, 35, '03:50:03', '00:00:03', 'FINALIZADO'),
(1, 36, 36, '03:50:04', '00:00:04', 'FINALIZADO'),
(1, 37, 37, '03:50:05', '00:00:05', 'FINALIZADO'),
(1, 38, 38, '03:50:06', '00:00:06', 'FINALIZADO'),
(1, 39, 39, '03:50:07', '00:00:07', 'FINALIZADO'),
(1, 40, 40, '03:50:00', '00:00:00', 'FINALIZADO'),
(1, 41, 41, '03:50:01', '00:00:01', 'FINALIZADO'),
(1, 42, 42, '03:50:02', '00:00:02', 'FINALIZADO'),
(1, 43, 43, '03:50:03', '00:00:03', 'FINALIZADO'),
(1, 44, 44, '03:50:04', '00:00:04', 'FINALIZADO'),
(1, 45, 45, '03:50:05', '00:00:05', 'FINALIZADO'),
(1, 46, 46, '03:50:06', '00:00:06', 'FINALIZADO'),
(1, 47, 47, '03:50:07', '00:00:07', 'FINALIZADO'),
(1, 48, 48, '03:50:00', '00:00:00', 'FINALIZADO'),
(1, 49, 49, '03:50:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(1, 1, 1, 50),
(1, 2, 2, 30),
(1, 3, 3, 20),
(1, 4, 4, 12),
(1, 5, 5, 10),
(1, 6, 6, 8),
(1, 7, 7, 6),
(1, 8, 8, 4),
(1, 9, 9, 2),
(1, 10, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(2, 6, 1, '04:25:00', '00:00:00', 'FINALIZADO'),
(2, 7, 2, '04:25:01', '00:00:01', 'FINALIZADO'),
(2, 8, 3, '04:25:01', '00:00:01', 'FINALIZADO'),
(2, 9, 4, '04:25:04', '00:00:04', 'FINALIZADO'),
(2, 10, 5, '04:25:05', '00:00:05', 'FINALIZADO'),
(2, 11, 6, '04:25:06', '00:00:06', 'FINALIZADO'),
(2, 12, 7, '04:25:07', '00:00:07', 'FINALIZADO'),
(2, 13, 8, '04:25:00', '00:00:00', 'FINALIZADO'),
(2, 14, 9, '04:25:01', '00:00:01', 'FINALIZADO'),
(2, 15, 10, '04:25:02', '00:00:02', 'FINALIZADO'),
(2, 16, 11, '04:25:03', '00:00:03', 'FINALIZADO'),
(2, 17, 12, '04:25:04', '00:00:04', 'FINALIZADO'),
(2, 18, 13, '04:25:05', '00:00:05', 'FINALIZADO'),
(2, 19, 14, '04:25:06', '00:00:06', 'FINALIZADO'),
(2, 20, 15, '04:25:07', '00:00:07', 'FINALIZADO'),
(2, 21, 16, '04:25:00', '00:00:00', 'FINALIZADO'),
(2, 22, 17, '04:25:01', '00:00:01', 'FINALIZADO'),
(2, 23, 18, '04:25:02', '00:00:02', 'FINALIZADO'),
(2, 24, 19, '04:25:03', '00:00:03', 'FINALIZADO'),
(2, 25, 20, '04:25:04', '00:00:04', 'FINALIZADO'),
(2, 26, 21, '04:25:05', '00:00:05', 'FINALIZADO'),
(2, 27, 22, '04:25:06', '00:00:06', 'FINALIZADO'),
(2, 28, 23, '04:25:07', '00:00:07', 'FINALIZADO'),
(2, 29, 24, '04:25:00', '00:00:00', 'FINALIZADO'),
(2, 30, 25, '04:25:01', '00:00:01', 'FINALIZADO'),
(2, 31, 26, '04:25:02', '00:00:02', 'FINALIZADO'),
(2, 32, 27, '04:25:03', '00:00:03', 'FINALIZADO'),
(2, 33, 28, '04:25:04', '00:00:04', 'FINALIZADO'),
(2, 34, 29, '04:25:05', '00:00:05', 'FINALIZADO'),
(2, 35, 30, '04:25:06', '00:00:06', 'FINALIZADO'),
(2, 36, 31, '04:25:07', '00:00:07', 'FINALIZADO'),
(2, 37, 32, '04:25:00', '00:00:00', 'FINALIZADO'),
(2, 38, 33, '04:25:01', '00:00:01', 'FINALIZADO'),
(2, 39, 34, '04:25:02', '00:00:02', 'FINALIZADO'),
(2, 40, 35, '04:25:03', '00:00:03', 'FINALIZADO'),
(2, 41, 36, '04:25:04', '00:00:04', 'FINALIZADO'),
(2, 42, 37, '04:25:05', '00:00:05', 'FINALIZADO'),
(2, 43, 38, '04:25:06', '00:00:06', 'FINALIZADO'),
(2, 44, 39, '04:25:07', '00:00:07', 'FINALIZADO'),
(2, 45, 40, '04:25:00', '00:00:00', 'FINALIZADO'),
(2, 46, 41, '04:25:01', '00:00:01', 'FINALIZADO'),
(2, 47, 42, '04:25:02', '00:00:02', 'FINALIZADO'),
(2, 48, 43, '04:25:03', '00:00:03', 'FINALIZADO'),
(2, 49, 44, '04:25:04', '00:00:04', 'FINALIZADO'),
(2, 1, 45, '04:25:05', '00:00:05', 'FINALIZADO'),
(2, 2, 46, '04:25:06', '00:00:06', 'FINALIZADO'),
(2, 3, 47, '04:25:07', '00:00:07', 'FINALIZADO'),
(2, 4, 48, '04:25:00', '00:00:00', 'FINALIZADO'),
(2, 5, 49, '04:25:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(2, 6, 1, 50),
(2, 7, 2, 30),
(2, 8, 3, 20),
(2, 9, 4, 12),
(2, 10, 5, 10),
(2, 11, 6, 8),
(2, 12, 7, 6),
(2, 13, 8, 4),
(2, 14, 9, 2),
(2, 15, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(3, 11, 1, '03:55:00', '00:00:00', 'FINALIZADO'),
(3, 12, 2, '03:55:01', '00:00:01', 'FINALIZADO'),
(3, 13, 3, '03:55:01', '00:00:01', 'FINALIZADO'),
(3, 14, 4, '03:55:04', '00:00:04', 'FINALIZADO'),
(3, 15, 5, '03:55:05', '00:00:05', 'FINALIZADO'),
(3, 16, 6, '03:55:06', '00:00:06', 'FINALIZADO'),
(3, 17, 7, '03:55:07', '00:00:07', 'FINALIZADO'),
(3, 18, 8, '03:55:00', '00:00:00', 'FINALIZADO'),
(3, 19, 9, '03:55:01', '00:00:01', 'FINALIZADO'),
(3, 20, 10, '03:55:02', '00:00:02', 'FINALIZADO'),
(3, 21, 11, '03:55:03', '00:00:03', 'FINALIZADO'),
(3, 22, 12, '03:55:04', '00:00:04', 'FINALIZADO'),
(3, 23, 13, '03:55:05', '00:00:05', 'FINALIZADO'),
(3, 24, 14, '03:55:06', '00:00:06', 'FINALIZADO'),
(3, 25, 15, '03:55:07', '00:00:07', 'FINALIZADO'),
(3, 26, 16, '03:55:00', '00:00:00', 'FINALIZADO'),
(3, 27, 17, '03:55:01', '00:00:01', 'FINALIZADO'),
(3, 28, 18, '03:55:02', '00:00:02', 'FINALIZADO'),
(3, 29, 19, '03:55:03', '00:00:03', 'FINALIZADO'),
(3, 30, 20, '03:55:04', '00:00:04', 'FINALIZADO'),
(3, 31, 21, '03:55:05', '00:00:05', 'FINALIZADO'),
(3, 32, 22, '03:55:06', '00:00:06', 'FINALIZADO'),
(3, 33, 23, '03:55:07', '00:00:07', 'FINALIZADO'),
(3, 34, 24, '03:55:00', '00:00:00', 'FINALIZADO'),
(3, 35, 25, '03:55:01', '00:00:01', 'FINALIZADO'),
(3, 36, 26, '03:55:02', '00:00:02', 'FINALIZADO'),
(3, 37, 27, '03:55:03', '00:00:03', 'FINALIZADO'),
(3, 38, 28, '03:55:04', '00:00:04', 'FINALIZADO'),
(3, 39, 29, '03:55:05', '00:00:05', 'FINALIZADO'),
(3, 40, 30, '03:55:06', '00:00:06', 'FINALIZADO'),
(3, 41, 31, '03:55:07', '00:00:07', 'FINALIZADO'),
(3, 42, 32, '03:55:00', '00:00:00', 'FINALIZADO'),
(3, 43, 33, '03:55:01', '00:00:01', 'FINALIZADO'),
(3, 44, 34, '03:55:02', '00:00:02', 'FINALIZADO'),
(3, 45, 35, '03:55:03', '00:00:03', 'FINALIZADO'),
(3, 46, 36, '03:55:04', '00:00:04', 'FINALIZADO'),
(3, 47, 37, '03:55:05', '00:00:05', 'FINALIZADO'),
(3, 48, 38, '03:55:06', '00:00:06', 'FINALIZADO'),
(3, 49, 39, '03:55:07', '00:00:07', 'FINALIZADO'),
(3, 1, 40, '03:55:00', '00:00:00', 'FINALIZADO'),
(3, 2, 41, '03:55:01', '00:00:01', 'FINALIZADO'),
(3, 3, 42, '03:55:02', '00:00:02', 'FINALIZADO'),
(3, 4, 43, '03:55:03', '00:00:03', 'FINALIZADO'),
(3, 5, 44, '03:55:04', '00:00:04', 'FINALIZADO'),
(3, 6, 45, '03:55:05', '00:00:05', 'FINALIZADO'),
(3, 7, 46, '03:55:06', '00:00:06', 'FINALIZADO'),
(3, 8, 47, '03:55:07', '00:00:07', 'FINALIZADO'),
(3, 9, 48, '03:55:00', '00:00:00', 'FINALIZADO'),
(3, 10, 49, '03:55:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(3, 11, 1, 50),
(3, 12, 2, 30),
(3, 13, 3, 20),
(3, 14, 4, 12),
(3, 15, 5, 10),
(3, 16, 6, 8),
(3, 17, 7, 6),
(3, 18, 8, 4),
(3, 19, 9, 2),
(3, 20, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(4, 16, 1, '04:20:00', '00:00:00', 'FINALIZADO'),
(4, 17, 2, '04:20:01', '00:00:01', 'FINALIZADO'),
(4, 18, 3, '04:20:01', '00:00:01', 'FINALIZADO'),
(4, 19, 4, '04:20:04', '00:00:04', 'FINALIZADO'),
(4, 20, 5, '04:20:05', '00:00:05', 'FINALIZADO'),
(4, 21, 6, '04:20:06', '00:00:06', 'FINALIZADO'),
(4, 22, 7, '04:20:07', '00:00:07', 'FINALIZADO'),
(4, 23, 8, '04:20:00', '00:00:00', 'FINALIZADO'),
(4, 24, 9, '04:20:01', '00:00:01', 'FINALIZADO'),
(4, 25, 10, '04:20:02', '00:00:02', 'FINALIZADO'),
(4, 26, 11, '04:20:03', '00:00:03', 'FINALIZADO'),
(4, 27, 12, '04:20:04', '00:00:04', 'FINALIZADO'),
(4, 28, 13, '04:20:05', '00:00:05', 'FINALIZADO'),
(4, 29, 14, '04:20:06', '00:00:06', 'FINALIZADO'),
(4, 30, 15, '04:20:07', '00:00:07', 'FINALIZADO'),
(4, 31, 16, '04:20:00', '00:00:00', 'FINALIZADO'),
(4, 32, 17, '04:20:01', '00:00:01', 'FINALIZADO'),
(4, 33, 18, '04:20:02', '00:00:02', 'FINALIZADO'),
(4, 34, 19, '04:20:03', '00:00:03', 'FINALIZADO'),
(4, 35, 20, '04:20:04', '00:00:04', 'FINALIZADO'),
(4, 36, 21, '04:20:05', '00:00:05', 'FINALIZADO'),
(4, 37, 22, '04:20:06', '00:00:06', 'FINALIZADO'),
(4, 38, 23, '04:20:07', '00:00:07', 'FINALIZADO'),
(4, 39, 24, '04:20:00', '00:00:00', 'FINALIZADO'),
(4, 40, 25, '04:20:01', '00:00:01', 'FINALIZADO'),
(4, 41, 26, '04:20:02', '00:00:02', 'FINALIZADO'),
(4, 42, 27, '04:20:03', '00:00:03', 'FINALIZADO'),
(4, 43, 28, '04:20:04', '00:00:04', 'FINALIZADO'),
(4, 44, 29, '04:20:05', '00:00:05', 'FINALIZADO'),
(4, 45, 30, '04:20:06', '00:00:06', 'FINALIZADO'),
(4, 46, 31, '04:20:07', '00:00:07', 'FINALIZADO'),
(4, 47, 32, '04:20:00', '00:00:00', 'FINALIZADO'),
(4, 48, 33, '04:20:01', '00:00:01', 'FINALIZADO'),
(4, 49, 34, '04:20:02', '00:00:02', 'FINALIZADO'),
(4, 1, 35, '04:20:03', '00:00:03', 'FINALIZADO'),
(4, 2, 36, '04:20:04', '00:00:04', 'FINALIZADO'),
(4, 3, 37, '04:20:05', '00:00:05', 'FINALIZADO'),
(4, 4, 38, '04:20:06', '00:00:06', 'FINALIZADO'),
(4, 5, 39, '04:20:07', '00:00:07', 'FINALIZADO'),
(4, 6, 40, '04:20:00', '00:00:00', 'FINALIZADO'),
(4, 7, 41, '04:20:01', '00:00:01', 'FINALIZADO'),
(4, 8, 42, '04:20:02', '00:00:02', 'FINALIZADO'),
(4, 9, 43, '04:20:03', '00:00:03', 'FINALIZADO'),
(4, 10, 44, '04:20:04', '00:00:04', 'FINALIZADO'),
(4, 11, 45, '04:20:05', '00:00:05', 'FINALIZADO'),
(4, 12, 46, '04:20:06', '00:00:06', 'FINALIZADO'),
(4, 13, 47, '04:20:07', '00:00:07', 'FINALIZADO'),
(4, 14, 48, '04:20:00', '00:00:00', 'FINALIZADO'),
(4, 15, 49, '04:20:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(4, 16, 1, 50),
(4, 17, 2, 30),
(4, 18, 3, 20),
(4, 19, 4, 12),
(4, 20, 5, 10),
(4, 21, 6, 8),
(4, 22, 7, 6),
(4, 23, 8, 4),
(4, 24, 9, 2),
(4, 25, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(5, 21, 1, '04:40:00', '00:00:00', 'FINALIZADO'),
(5, 22, 2, '04:40:01', '00:00:01', 'FINALIZADO'),
(5, 23, 3, '04:40:01', '00:00:01', 'FINALIZADO'),
(5, 24, 4, '04:40:04', '00:00:04', 'FINALIZADO'),
(5, 25, 5, '04:40:05', '00:00:05', 'FINALIZADO'),
(5, 26, 6, '04:40:06', '00:00:06', 'FINALIZADO'),
(5, 27, 7, '04:40:07', '00:00:07', 'FINALIZADO'),
(5, 28, 8, '04:40:00', '00:00:00', 'FINALIZADO'),
(5, 29, 9, '04:40:01', '00:00:01', 'FINALIZADO'),
(5, 30, 10, '04:40:02', '00:00:02', 'FINALIZADO'),
(5, 31, 11, '04:40:03', '00:00:03', 'FINALIZADO'),
(5, 32, 12, '04:40:04', '00:00:04', 'FINALIZADO'),
(5, 33, 13, '04:40:05', '00:00:05', 'FINALIZADO'),
(5, 34, 14, '04:40:06', '00:00:06', 'FINALIZADO'),
(5, 35, 15, '04:40:07', '00:00:07', 'FINALIZADO'),
(5, 36, 16, '04:40:00', '00:00:00', 'FINALIZADO'),
(5, 37, 17, '04:40:01', '00:00:01', 'FINALIZADO'),
(5, 38, 18, '04:40:02', '00:00:02', 'FINALIZADO'),
(5, 39, 19, '04:40:03', '00:00:03', 'FINALIZADO'),
(5, 40, 20, '04:40:04', '00:00:04', 'FINALIZADO'),
(5, 41, 21, '04:40:05', '00:00:05', 'FINALIZADO'),
(5, 42, 22, '04:40:06', '00:00:06', 'FINALIZADO'),
(5, 43, 23, '04:40:07', '00:00:07', 'FINALIZADO'),
(5, 44, 24, '04:40:00', '00:00:00', 'FINALIZADO'),
(5, 45, 25, '04:40:01', '00:00:01', 'FINALIZADO'),
(5, 46, 26, '04:40:02', '00:00:02', 'FINALIZADO'),
(5, 47, 27, '04:40:03', '00:00:03', 'FINALIZADO'),
(5, 48, 28, '04:40:04', '00:00:04', 'FINALIZADO'),
(5, 49, 29, '04:40:05', '00:00:05', 'FINALIZADO'),
(5, 1, 30, '04:40:06', '00:00:06', 'FINALIZADO'),
(5, 2, 31, '04:40:07', '00:00:07', 'FINALIZADO'),
(5, 3, 32, '04:40:00', '00:00:00', 'FINALIZADO'),
(5, 4, 33, '04:40:01', '00:00:01', 'FINALIZADO'),
(5, 5, 34, '04:40:02', '00:00:02', 'FINALIZADO'),
(5, 6, 35, '04:40:03', '00:00:03', 'FINALIZADO'),
(5, 7, 36, '04:40:04', '00:00:04', 'FINALIZADO'),
(5, 8, 37, '04:40:05', '00:00:05', 'FINALIZADO'),
(5, 9, 38, '04:40:06', '00:00:06', 'FINALIZADO'),
(5, 10, 39, '04:40:07', '00:00:07', 'FINALIZADO'),
(5, 11, 40, '04:40:00', '00:00:00', 'FINALIZADO'),
(5, 12, 41, '04:40:01', '00:00:01', 'FINALIZADO'),
(5, 13, 42, '04:40:02', '00:00:02', 'FINALIZADO'),
(5, 14, 43, '04:40:03', '00:00:03', 'FINALIZADO'),
(5, 15, 44, '04:40:04', '00:00:04', 'FINALIZADO'),
(5, 16, 45, '04:40:05', '00:00:05', 'FINALIZADO'),
(5, 17, 46, '04:40:06', '00:00:06', 'FINALIZADO'),
(5, 18, 47, '04:40:07', '00:00:07', 'FINALIZADO'),
(5, 19, 48, '04:40:00', '00:00:00', 'FINALIZADO'),
(5, 20, 49, '04:40:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(5, 21, 1, 50),
(5, 22, 2, 30),
(5, 23, 3, 20),
(5, 24, 4, 12),
(5, 25, 5, 10),
(5, 26, 6, 8),
(5, 27, 7, 6),
(5, 28, 8, 4),
(5, 29, 9, 2),
(5, 30, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(6, 26, 1, '03:48:00', '00:00:00', 'FINALIZADO'),
(6, 27, 2, '03:48:01', '00:00:01', 'FINALIZADO'),
(6, 28, 3, '03:48:01', '00:00:01', 'FINALIZADO'),
(6, 29, 4, '03:48:04', '00:00:04', 'FINALIZADO'),
(6, 30, 5, '03:48:05', '00:00:05', 'FINALIZADO'),
(6, 31, 6, '03:48:06', '00:00:06', 'FINALIZADO'),
(6, 32, 7, '03:48:07', '00:00:07', 'FINALIZADO'),
(6, 33, 8, '03:48:00', '00:00:00', 'FINALIZADO'),
(6, 34, 9, '03:48:01', '00:00:01', 'FINALIZADO'),
(6, 35, 10, '03:48:02', '00:00:02', 'FINALIZADO'),
(6, 36, 11, '03:48:03', '00:00:03', 'FINALIZADO'),
(6, 37, 12, '03:48:04', '00:00:04', 'FINALIZADO'),
(6, 38, 13, '03:48:05', '00:00:05', 'FINALIZADO'),
(6, 39, 14, '03:48:06', '00:00:06', 'FINALIZADO'),
(6, 40, 15, '03:48:07', '00:00:07', 'FINALIZADO'),
(6, 41, 16, '03:48:00', '00:00:00', 'FINALIZADO'),
(6, 42, 17, '03:48:01', '00:00:01', 'FINALIZADO'),
(6, 43, 18, '03:48:02', '00:00:02', 'FINALIZADO'),
(6, 44, 19, '03:48:03', '00:00:03', 'FINALIZADO'),
(6, 45, 20, '03:48:04', '00:00:04', 'FINALIZADO'),
(6, 46, 21, '03:48:05', '00:00:05', 'FINALIZADO'),
(6, 47, 22, '03:48:06', '00:00:06', 'FINALIZADO'),
(6, 48, 23, '03:48:07', '00:00:07', 'FINALIZADO'),
(6, 49, 24, '03:48:00', '00:00:00', 'FINALIZADO'),
(6, 1, 25, '03:48:01', '00:00:01', 'FINALIZADO'),
(6, 2, 26, '03:48:02', '00:00:02', 'FINALIZADO'),
(6, 3, 27, '03:48:03', '00:00:03', 'FINALIZADO'),
(6, 4, 28, '03:48:04', '00:00:04', 'FINALIZADO'),
(6, 5, 29, '03:48:05', '00:00:05', 'FINALIZADO'),
(6, 6, 30, '03:48:06', '00:00:06', 'FINALIZADO'),
(6, 7, 31, '03:48:07', '00:00:07', 'FINALIZADO'),
(6, 8, 32, '03:48:00', '00:00:00', 'FINALIZADO'),
(6, 9, 33, '03:48:01', '00:00:01', 'FINALIZADO'),
(6, 10, 34, '03:48:02', '00:00:02', 'FINALIZADO'),
(6, 11, 35, '03:48:03', '00:00:03', 'FINALIZADO'),
(6, 12, 36, '03:48:04', '00:00:04', 'FINALIZADO'),
(6, 13, 37, '03:48:05', '00:00:05', 'FINALIZADO'),
(6, 14, 38, '03:48:06', '00:00:06', 'FINALIZADO'),
(6, 15, 39, '03:48:07', '00:00:07', 'FINALIZADO'),
(6, 16, 40, '03:48:00', '00:00:00', 'FINALIZADO'),
(6, 17, 41, '03:48:01', '00:00:01', 'FINALIZADO'),
(6, 18, 42, '03:48:02', '00:00:02', 'FINALIZADO'),
(6, 19, 43, '03:48:03', '00:00:03', 'FINALIZADO'),
(6, 20, 44, '03:48:04', '00:00:04', 'FINALIZADO'),
(6, 21, 45, '03:48:05', '00:00:05', 'FINALIZADO'),
(6, 22, 46, '03:48:06', '00:00:06', 'FINALIZADO'),
(6, 23, 47, '03:48:07', '00:00:07', 'FINALIZADO'),
(6, 24, 48, '03:48:00', '00:00:00', 'FINALIZADO'),
(6, 25, 49, '03:48:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(6, 26, 1, 50),
(6, 27, 2, 30),
(6, 28, 3, 20),
(6, 29, 4, 12),
(6, 30, 5, 10),
(6, 31, 6, 8),
(6, 32, 7, 6),
(6, 33, 8, 4),
(6, 34, 9, 2),
(6, 35, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(7, 31, 1, '00:42:00', '00:00:00', 'FINALIZADO'),
(7, 32, 2, '00:42:01', '00:00:01', 'FINALIZADO'),
(7, 33, 3, '00:42:01', '00:00:01', 'FINALIZADO'),
(7, 34, 4, '00:42:04', '00:00:04', 'FINALIZADO'),
(7, 35, 5, '00:42:05', '00:00:05', 'FINALIZADO'),
(7, 36, 6, '00:42:06', '00:00:06', 'FINALIZADO'),
(7, 37, 7, '00:42:07', '00:00:07', 'FINALIZADO'),
(7, 38, 8, '00:42:00', '00:00:00', 'FINALIZADO'),
(7, 39, 9, '00:42:01', '00:00:01', 'FINALIZADO'),
(7, 40, 10, '00:42:02', '00:00:02', 'FINALIZADO'),
(7, 41, 11, '00:42:03', '00:00:03', 'FINALIZADO'),
(7, 42, 12, '00:42:04', '00:00:04', 'FINALIZADO'),
(7, 43, 13, '00:42:05', '00:00:05', 'FINALIZADO'),
(7, 44, 14, '00:42:06', '00:00:06', 'FINALIZADO'),
(7, 45, 15, '00:42:07', '00:00:07', 'FINALIZADO'),
(7, 46, 16, '00:42:00', '00:00:00', 'FINALIZADO'),
(7, 47, 17, '00:42:01', '00:00:01', 'FINALIZADO'),
(7, 48, 18, '00:42:02', '00:00:02', 'FINALIZADO'),
(7, 49, 19, '00:42:03', '00:00:03', 'FINALIZADO'),
(7, 1, 20, '00:42:04', '00:00:04', 'FINALIZADO'),
(7, 2, 21, '00:42:05', '00:00:05', 'FINALIZADO'),
(7, 3, 22, '00:42:06', '00:00:06', 'FINALIZADO'),
(7, 4, 23, '00:42:07', '00:00:07', 'FINALIZADO'),
(7, 5, 24, '00:42:00', '00:00:00', 'FINALIZADO'),
(7, 6, 25, '00:42:01', '00:00:01', 'FINALIZADO'),
(7, 7, 26, '00:42:02', '00:00:02', 'FINALIZADO'),
(7, 8, 27, '00:42:03', '00:00:03', 'FINALIZADO'),
(7, 9, 28, '00:42:04', '00:00:04', 'FINALIZADO'),
(7, 10, 29, '00:42:05', '00:00:05', 'FINALIZADO'),
(7, 11, 30, '00:42:06', '00:00:06', 'FINALIZADO'),
(7, 12, 31, '00:42:07', '00:00:07', 'FINALIZADO'),
(7, 13, 32, '00:42:00', '00:00:00', 'FINALIZADO'),
(7, 14, 33, '00:42:01', '00:00:01', 'FINALIZADO'),
(7, 15, 34, '00:42:02', '00:00:02', 'FINALIZADO'),
(7, 16, 35, '00:42:03', '00:00:03', 'FINALIZADO'),
(7, 17, 36, '00:42:04', '00:00:04', 'FINALIZADO'),
(7, 18, 37, '00:42:05', '00:00:05', 'FINALIZADO'),
(7, 19, 38, '00:42:06', '00:00:06', 'FINALIZADO'),
(7, 20, 39, '00:42:07', '00:00:07', 'FINALIZADO'),
(7, 21, 40, '00:42:00', '00:00:00', 'FINALIZADO'),
(7, 22, 41, '00:42:01', '00:00:01', 'FINALIZADO'),
(7, 23, 42, '00:42:02', '00:00:02', 'FINALIZADO'),
(7, 24, 43, '00:42:03', '00:00:03', 'FINALIZADO'),
(7, 25, 44, '00:42:04', '00:00:04', 'FINALIZADO'),
(7, 26, 45, '00:42:05', '00:00:05', 'FINALIZADO'),
(7, 27, 46, '00:42:06', '00:00:06', 'FINALIZADO'),
(7, 28, 47, '00:42:07', '00:00:07', 'FINALIZADO'),
(7, 29, 48, '00:42:00', '00:00:00', 'FINALIZADO'),
(7, 30, 49, '00:42:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(7, 31, 1, 50),
(7, 32, 2, 30),
(7, 33, 3, 20),
(7, 34, 4, 12),
(7, 35, 5, 10),
(7, 36, 6, 8),
(7, 37, 7, 6),
(7, 38, 8, 4),
(7, 39, 9, 2),
(7, 40, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(8, 36, 1, '04:15:00', '00:00:00', 'FINALIZADO'),
(8, 37, 2, '04:15:01', '00:00:01', 'FINALIZADO'),
(8, 38, 3, '04:15:01', '00:00:01', 'FINALIZADO'),
(8, 39, 4, '04:15:04', '00:00:04', 'FINALIZADO'),
(8, 40, 5, '04:15:05', '00:00:05', 'FINALIZADO'),
(8, 41, 6, '04:15:06', '00:00:06', 'FINALIZADO'),
(8, 42, 7, '04:15:07', '00:00:07', 'FINALIZADO'),
(8, 43, 8, '04:15:00', '00:00:00', 'FINALIZADO'),
(8, 44, 9, '04:15:01', '00:00:01', 'FINALIZADO'),
(8, 45, 10, '04:15:02', '00:00:02', 'FINALIZADO'),
(8, 46, 11, '04:15:03', '00:00:03', 'FINALIZADO'),
(8, 47, 12, '04:15:04', '00:00:04', 'FINALIZADO'),
(8, 48, 13, '04:15:05', '00:00:05', 'FINALIZADO'),
(8, 49, 14, '04:15:06', '00:00:06', 'FINALIZADO'),
(8, 1, 15, '04:15:07', '00:00:07', 'FINALIZADO'),
(8, 2, 16, '04:15:00', '00:00:00', 'FINALIZADO'),
(8, 3, 17, '04:15:01', '00:00:01', 'FINALIZADO'),
(8, 4, 18, '04:15:02', '00:00:02', 'FINALIZADO'),
(8, 5, 19, '04:15:03', '00:00:03', 'FINALIZADO'),
(8, 6, 20, '04:15:04', '00:00:04', 'FINALIZADO'),
(8, 7, 21, '04:15:05', '00:00:05', 'FINALIZADO'),
(8, 8, 22, '04:15:06', '00:00:06', 'FINALIZADO'),
(8, 9, 23, '04:15:07', '00:00:07', 'FINALIZADO'),
(8, 10, 24, '04:15:00', '00:00:00', 'FINALIZADO'),
(8, 11, 25, '04:15:01', '00:00:01', 'FINALIZADO'),
(8, 12, 26, '04:15:02', '00:00:02', 'FINALIZADO'),
(8, 13, 27, '04:15:03', '00:00:03', 'FINALIZADO'),
(8, 14, 28, '04:15:04', '00:00:04', 'FINALIZADO'),
(8, 15, 29, '04:15:05', '00:00:05', 'FINALIZADO'),
(8, 16, 30, '04:15:06', '00:00:06', 'FINALIZADO'),
(8, 17, 31, '04:15:07', '00:00:07', 'FINALIZADO'),
(8, 18, 32, '04:15:00', '00:00:00', 'FINALIZADO'),
(8, 19, 33, '04:15:01', '00:00:01', 'FINALIZADO'),
(8, 20, 34, '04:15:02', '00:00:02', 'FINALIZADO'),
(8, 21, 35, '04:15:03', '00:00:03', 'FINALIZADO'),
(8, 22, 36, '04:15:04', '00:00:04', 'FINALIZADO'),
(8, 23, 37, '04:15:05', '00:00:05', 'FINALIZADO'),
(8, 24, 38, '04:15:06', '00:00:06', 'FINALIZADO'),
(8, 25, 39, '04:15:07', '00:00:07', 'FINALIZADO'),
(8, 26, 40, '04:15:00', '00:00:00', 'FINALIZADO'),
(8, 27, 41, '04:15:01', '00:00:01', 'FINALIZADO'),
(8, 28, 42, '04:15:02', '00:00:02', 'FINALIZADO'),
(8, 29, 43, '04:15:03', '00:00:03', 'FINALIZADO'),
(8, 30, 44, '04:15:04', '00:00:04', 'FINALIZADO'),
(8, 31, 45, '04:15:05', '00:00:05', 'FINALIZADO'),
(8, 32, 46, '04:15:06', '00:00:06', 'FINALIZADO'),
(8, 33, 47, '04:15:07', '00:00:07', 'FINALIZADO'),
(8, 34, 48, '04:15:00', '00:00:00', 'FINALIZADO'),
(8, 35, 49, '04:15:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(8, 36, 1, 50),
(8, 37, 2, 30),
(8, 38, 3, 20),
(8, 39, 4, 12),
(8, 40, 5, 10),
(8, 41, 6, 8),
(8, 42, 7, 6),
(8, 43, 8, 4),
(8, 44, 9, 2),
(8, 45, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(9, 41, 1, '03:45:00', '00:00:00', 'FINALIZADO'),
(9, 42, 2, '03:45:01', '00:00:01', 'FINALIZADO'),
(9, 43, 3, '03:45:01', '00:00:01', 'FINALIZADO'),
(9, 44, 4, '03:45:04', '00:00:04', 'FINALIZADO'),
(9, 45, 5, '03:45:05', '00:00:05', 'FINALIZADO'),
(9, 46, 6, '03:45:06', '00:00:06', 'FINALIZADO'),
(9, 47, 7, '03:45:07', '00:00:07', 'FINALIZADO'),
(9, 48, 8, '03:45:00', '00:00:00', 'FINALIZADO'),
(9, 49, 9, '03:45:01', '00:00:01', 'FINALIZADO'),
(9, 1, 10, '03:45:02', '00:00:02', 'FINALIZADO'),
(9, 2, 11, '03:45:03', '00:00:03', 'FINALIZADO'),
(9, 3, 12, '03:45:04', '00:00:04', 'FINALIZADO'),
(9, 4, 13, '03:45:05', '00:00:05', 'FINALIZADO'),
(9, 5, 14, '03:45:06', '00:00:06', 'FINALIZADO'),
(9, 6, 15, '03:45:07', '00:00:07', 'FINALIZADO'),
(9, 7, 16, '03:45:00', '00:00:00', 'FINALIZADO'),
(9, 8, 17, '03:45:01', '00:00:01', 'FINALIZADO'),
(9, 9, 18, '03:45:02', '00:00:02', 'FINALIZADO'),
(9, 10, 19, '03:45:03', '00:00:03', 'FINALIZADO'),
(9, 11, 20, '03:45:04', '00:00:04', 'FINALIZADO'),
(9, 12, 21, '03:45:05', '00:00:05', 'FINALIZADO'),
(9, 13, 22, '03:45:06', '00:00:06', 'FINALIZADO'),
(9, 14, 23, '03:45:07', '00:00:07', 'FINALIZADO'),
(9, 15, 24, '03:45:00', '00:00:00', 'FINALIZADO'),
(9, 16, 25, '03:45:01', '00:00:01', 'FINALIZADO'),
(9, 17, 26, '03:45:02', '00:00:02', 'FINALIZADO'),
(9, 18, 27, '03:45:03', '00:00:03', 'FINALIZADO'),
(9, 19, 28, '03:45:04', '00:00:04', 'FINALIZADO'),
(9, 20, 29, '03:45:05', '00:00:05', 'FINALIZADO'),
(9, 21, 30, '03:45:06', '00:00:06', 'FINALIZADO'),
(9, 22, 31, '03:45:07', '00:00:07', 'FINALIZADO'),
(9, 23, 32, '03:45:00', '00:00:00', 'FINALIZADO'),
(9, 24, 33, '03:45:01', '00:00:01', 'FINALIZADO'),
(9, 25, 34, '03:45:02', '00:00:02', 'FINALIZADO'),
(9, 26, 35, '03:45:03', '00:00:03', 'FINALIZADO'),
(9, 27, 36, '03:45:04', '00:00:04', 'FINALIZADO'),
(9, 28, 37, '03:45:05', '00:00:05', 'FINALIZADO'),
(9, 29, 38, '03:45:06', '00:00:06', 'FINALIZADO'),
(9, 30, 39, '03:45:07', '00:00:07', 'FINALIZADO'),
(9, 31, 40, '03:45:00', '00:00:00', 'FINALIZADO'),
(9, 32, 41, '03:45:01', '00:00:01', 'FINALIZADO'),
(9, 33, 42, '03:45:02', '00:00:02', 'FINALIZADO'),
(9, 34, 43, '03:45:03', '00:00:03', 'FINALIZADO'),
(9, 35, 44, '03:45:04', '00:00:04', 'FINALIZADO'),
(9, 36, 45, '03:45:05', '00:00:05', 'FINALIZADO'),
(9, 37, 46, '03:45:06', '00:00:06', 'FINALIZADO'),
(9, 38, 47, '03:45:07', '00:00:07', 'FINALIZADO'),
(9, 39, 48, '03:45:00', '00:00:00', 'FINALIZADO'),
(9, 40, 49, '03:45:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(9, 41, 1, 50),
(9, 42, 2, 30),
(9, 43, 3, 20),
(9, 44, 4, 12),
(9, 45, 5, 10),
(9, 46, 6, 8),
(9, 47, 7, 6),
(9, 48, 8, 4),
(9, 49, 9, 2),
(9, 1, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(10, 46, 1, '04:35:00', '00:00:00', 'FINALIZADO'),
(10, 47, 2, '04:35:01', '00:00:01', 'FINALIZADO'),
(10, 48, 3, '04:35:01', '00:00:01', 'FINALIZADO'),
(10, 49, 4, '04:35:04', '00:00:04', 'FINALIZADO'),
(10, 1, 5, '04:35:05', '00:00:05', 'FINALIZADO'),
(10, 2, 6, '04:35:06', '00:00:06', 'FINALIZADO'),
(10, 3, 7, '04:35:07', '00:00:07', 'FINALIZADO'),
(10, 4, 8, '04:35:00', '00:00:00', 'FINALIZADO'),
(10, 5, 9, '04:35:01', '00:00:01', 'FINALIZADO'),
(10, 6, 10, '04:35:02', '00:00:02', 'FINALIZADO'),
(10, 7, 11, '04:35:03', '00:00:03', 'FINALIZADO'),
(10, 8, 12, '04:35:04', '00:00:04', 'FINALIZADO'),
(10, 9, 13, '04:35:05', '00:00:05', 'FINALIZADO'),
(10, 10, 14, '04:35:06', '00:00:06', 'FINALIZADO'),
(10, 11, 15, '04:35:07', '00:00:07', 'FINALIZADO'),
(10, 12, 16, '04:35:00', '00:00:00', 'FINALIZADO'),
(10, 13, 17, '04:35:01', '00:00:01', 'FINALIZADO'),
(10, 14, 18, '04:35:02', '00:00:02', 'FINALIZADO'),
(10, 15, 19, '04:35:03', '00:00:03', 'FINALIZADO'),
(10, 16, 20, '04:35:04', '00:00:04', 'FINALIZADO'),
(10, 17, 21, '04:35:05', '00:00:05', 'FINALIZADO'),
(10, 18, 22, '04:35:06', '00:00:06', 'FINALIZADO'),
(10, 19, 23, '04:35:07', '00:00:07', 'FINALIZADO'),
(10, 20, 24, '04:35:00', '00:00:00', 'FINALIZADO'),
(10, 21, 25, '04:35:01', '00:00:01', 'FINALIZADO'),
(10, 22, 26, '04:35:02', '00:00:02', 'FINALIZADO'),
(10, 23, 27, '04:35:03', '00:00:03', 'FINALIZADO'),
(10, 24, 28, '04:35:04', '00:00:04', 'FINALIZADO'),
(10, 25, 29, '04:35:05', '00:00:05', 'FINALIZADO'),
(10, 26, 30, '04:35:06', '00:00:06', 'FINALIZADO'),
(10, 27, 31, '04:35:07', '00:00:07', 'FINALIZADO'),
(10, 28, 32, '04:35:00', '00:00:00', 'FINALIZADO'),
(10, 29, 33, '04:35:01', '00:00:01', 'FINALIZADO'),
(10, 30, 34, '04:35:02', '00:00:02', 'FINALIZADO'),
(10, 31, 35, '04:35:03', '00:00:03', 'FINALIZADO'),
(10, 32, 36, '04:35:04', '00:00:04', 'FINALIZADO'),
(10, 33, 37, '04:35:05', '00:00:05', 'FINALIZADO'),
(10, 34, 38, '04:35:06', '00:00:06', 'FINALIZADO'),
(10, 35, 39, '04:35:07', '00:00:07', 'FINALIZADO'),
(10, 36, 40, '04:35:00', '00:00:00', 'FINALIZADO'),
(10, 37, 41, '04:35:01', '00:00:01', 'FINALIZADO'),
(10, 38, 42, '04:35:02', '00:00:02', 'FINALIZADO'),
(10, 39, 43, '04:35:03', '00:00:03', 'FINALIZADO'),
(10, 40, 44, '04:35:04', '00:00:04', 'FINALIZADO'),
(10, 41, 45, '04:35:05', '00:00:05', 'FINALIZADO'),
(10, 42, 46, '04:35:06', '00:00:06', 'FINALIZADO'),
(10, 43, 47, '04:35:07', '00:00:07', 'FINALIZADO'),
(10, 44, 48, '04:35:00', '00:00:00', 'FINALIZADO'),
(10, 45, 49, '04:35:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(10, 46, 1, 50),
(10, 47, 2, 30),
(10, 48, 3, 20),
(10, 49, 4, 12),
(10, 1, 5, 10),
(10, 2, 6, 8),
(10, 3, 7, 6),
(10, 4, 8, 4),
(10, 5, 9, 2),
(10, 6, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(11, 2, 1, '04:10:00', '00:00:00', 'FINALIZADO'),
(11, 3, 2, '04:10:01', '00:00:01', 'FINALIZADO'),
(11, 4, 3, '04:10:01', '00:00:01', 'FINALIZADO'),
(11, 5, 4, '04:10:04', '00:00:04', 'FINALIZADO'),
(11, 6, 5, '04:10:05', '00:00:05', 'FINALIZADO'),
(11, 7, 6, '04:10:06', '00:00:06', 'FINALIZADO'),
(11, 8, 7, '04:10:07', '00:00:07', 'FINALIZADO'),
(11, 9, 8, '04:10:00', '00:00:00', 'FINALIZADO'),
(11, 10, 9, '04:10:01', '00:00:01', 'FINALIZADO'),
(11, 11, 10, '04:10:02', '00:00:02', 'FINALIZADO'),
(11, 12, 11, '04:10:03', '00:00:03', 'FINALIZADO'),
(11, 13, 12, '04:10:04', '00:00:04', 'FINALIZADO'),
(11, 14, 13, '04:10:05', '00:00:05', 'FINALIZADO'),
(11, 15, 14, '04:10:06', '00:00:06', 'FINALIZADO'),
(11, 16, 15, '04:10:07', '00:00:07', 'FINALIZADO'),
(11, 17, 16, '04:10:00', '00:00:00', 'FINALIZADO'),
(11, 18, 17, '04:10:01', '00:00:01', 'FINALIZADO'),
(11, 19, 18, '04:10:02', '00:00:02', 'FINALIZADO'),
(11, 20, 19, '04:10:03', '00:00:03', 'FINALIZADO'),
(11, 21, 20, '04:10:04', '00:00:04', 'FINALIZADO'),
(11, 22, 21, '04:10:05', '00:00:05', 'FINALIZADO'),
(11, 23, 22, '04:10:06', '00:00:06', 'FINALIZADO'),
(11, 24, 23, '04:10:07', '00:00:07', 'FINALIZADO'),
(11, 25, 24, '04:10:00', '00:00:00', 'FINALIZADO'),
(11, 26, 25, '04:10:01', '00:00:01', 'FINALIZADO'),
(11, 27, 26, '04:10:02', '00:00:02', 'FINALIZADO'),
(11, 28, 27, '04:10:03', '00:00:03', 'FINALIZADO'),
(11, 29, 28, '04:10:04', '00:00:04', 'FINALIZADO'),
(11, 30, 29, '04:10:05', '00:00:05', 'FINALIZADO'),
(11, 31, 30, '04:10:06', '00:00:06', 'FINALIZADO'),
(11, 32, 31, '04:10:07', '00:00:07', 'FINALIZADO'),
(11, 33, 32, '04:10:00', '00:00:00', 'FINALIZADO'),
(11, 34, 33, '04:10:01', '00:00:01', 'FINALIZADO'),
(11, 35, 34, '04:10:02', '00:00:02', 'FINALIZADO'),
(11, 36, 35, '04:10:03', '00:00:03', 'FINALIZADO'),
(11, 37, 36, '04:10:04', '00:00:04', 'FINALIZADO'),
(11, 38, 37, '04:10:05', '00:00:05', 'FINALIZADO'),
(11, 39, 38, '04:10:06', '00:00:06', 'FINALIZADO'),
(11, 40, 39, '04:10:07', '00:00:07', 'FINALIZADO'),
(11, 41, 40, '04:10:00', '00:00:00', 'FINALIZADO'),
(11, 42, 41, '04:10:01', '00:00:01', 'FINALIZADO'),
(11, 43, 42, '04:10:02', '00:00:02', 'FINALIZADO'),
(11, 44, 43, '04:10:03', '00:00:03', 'FINALIZADO'),
(11, 45, 44, '04:10:04', '00:00:04', 'FINALIZADO'),
(11, 46, 45, '04:10:05', '00:00:05', 'FINALIZADO'),
(11, 47, 46, '04:10:06', '00:00:06', 'FINALIZADO'),
(11, 48, 47, '04:10:07', '00:00:07', 'FINALIZADO'),
(11, 49, 48, '04:10:00', '00:00:00', 'FINALIZADO'),
(11, 1, 49, '04:10:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(11, 2, 1, 50),
(11, 3, 2, 30),
(11, 4, 3, 20),
(11, 5, 4, 12),
(11, 6, 5, 10),
(11, 7, 6, 8),
(11, 8, 7, 6),
(11, 9, 8, 4),
(11, 10, 9, 2),
(11, 11, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(12, 7, 1, '03:40:00', '00:00:00', 'FINALIZADO'),
(12, 8, 2, '03:40:01', '00:00:01', 'FINALIZADO'),
(12, 9, 3, '03:40:01', '00:00:01', 'FINALIZADO'),
(12, 10, 4, '03:40:04', '00:00:04', 'FINALIZADO'),
(12, 11, 5, '03:40:05', '00:00:05', 'FINALIZADO'),
(12, 12, 6, '03:40:06', '00:00:06', 'FINALIZADO'),
(12, 13, 7, '03:40:07', '00:00:07', 'FINALIZADO'),
(12, 14, 8, '03:40:00', '00:00:00', 'FINALIZADO'),
(12, 15, 9, '03:40:01', '00:00:01', 'FINALIZADO'),
(12, 16, 10, '03:40:02', '00:00:02', 'FINALIZADO'),
(12, 17, 11, '03:40:03', '00:00:03', 'FINALIZADO'),
(12, 18, 12, '03:40:04', '00:00:04', 'FINALIZADO'),
(12, 19, 13, '03:40:05', '00:00:05', 'FINALIZADO'),
(12, 20, 14, '03:40:06', '00:00:06', 'FINALIZADO'),
(12, 21, 15, '03:40:07', '00:00:07', 'FINALIZADO'),
(12, 22, 16, '03:40:00', '00:00:00', 'FINALIZADO'),
(12, 23, 17, '03:40:01', '00:00:01', 'FINALIZADO'),
(12, 24, 18, '03:40:02', '00:00:02', 'FINALIZADO'),
(12, 25, 19, '03:40:03', '00:00:03', 'FINALIZADO'),
(12, 26, 20, '03:40:04', '00:00:04', 'FINALIZADO'),
(12, 27, 21, '03:40:05', '00:00:05', 'FINALIZADO'),
(12, 28, 22, '03:40:06', '00:00:06', 'FINALIZADO'),
(12, 29, 23, '03:40:07', '00:00:07', 'FINALIZADO'),
(12, 30, 24, '03:40:00', '00:00:00', 'FINALIZADO'),
(12, 31, 25, '03:40:01', '00:00:01', 'FINALIZADO'),
(12, 32, 26, '03:40:02', '00:00:02', 'FINALIZADO'),
(12, 33, 27, '03:40:03', '00:00:03', 'FINALIZADO'),
(12, 34, 28, '03:40:04', '00:00:04', 'FINALIZADO'),
(12, 35, 29, '03:40:05', '00:00:05', 'FINALIZADO'),
(12, 36, 30, '03:40:06', '00:00:06', 'FINALIZADO'),
(12, 37, 31, '03:40:07', '00:00:07', 'FINALIZADO'),
(12, 38, 32, '03:40:00', '00:00:00', 'FINALIZADO'),
(12, 39, 33, '03:40:01', '00:00:01', 'FINALIZADO'),
(12, 40, 34, '03:40:02', '00:00:02', 'FINALIZADO'),
(12, 41, 35, '03:40:03', '00:00:03', 'FINALIZADO'),
(12, 42, 36, '03:40:04', '00:00:04', 'FINALIZADO'),
(12, 43, 37, '03:40:05', '00:00:05', 'FINALIZADO'),
(12, 44, 38, '03:40:06', '00:00:06', 'FINALIZADO'),
(12, 45, 39, '03:40:07', '00:00:07', 'FINALIZADO'),
(12, 46, 40, '03:40:00', '00:00:00', 'FINALIZADO'),
(12, 47, 41, '03:40:01', '00:00:01', 'FINALIZADO'),
(12, 48, 42, '03:40:02', '00:00:02', 'FINALIZADO'),
(12, 49, 43, '03:40:03', '00:00:03', 'FINALIZADO'),
(12, 1, 44, '03:40:04', '00:00:04', 'FINALIZADO'),
(12, 2, 45, '03:40:05', '00:00:05', 'FINALIZADO'),
(12, 3, 46, '03:40:06', '00:00:06', 'FINALIZADO'),
(12, 4, 47, '03:40:07', '00:00:07', 'FINALIZADO'),
(12, 5, 48, '03:40:00', '00:00:00', 'FINALIZADO'),
(12, 6, 49, '03:40:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(12, 7, 1, 50),
(12, 8, 2, 30),
(12, 9, 3, 20),
(12, 10, 4, 12),
(12, 11, 5, 10),
(12, 12, 6, 8),
(12, 13, 7, 6),
(12, 14, 8, 4),
(12, 15, 9, 2),
(12, 16, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(13, 12, 1, '03:38:00', '00:00:00', 'FINALIZADO'),
(13, 13, 2, '03:38:01', '00:00:01', 'FINALIZADO'),
(13, 14, 3, '03:38:01', '00:00:01', 'FINALIZADO'),
(13, 15, 4, '03:38:04', '00:00:04', 'FINALIZADO'),
(13, 16, 5, '03:38:05', '00:00:05', 'FINALIZADO'),
(13, 17, 6, '03:38:06', '00:00:06', 'FINALIZADO'),
(13, 18, 7, '03:38:07', '00:00:07', 'FINALIZADO'),
(13, 19, 8, '03:38:00', '00:00:00', 'FINALIZADO'),
(13, 20, 9, '03:38:01', '00:00:01', 'FINALIZADO'),
(13, 21, 10, '03:38:02', '00:00:02', 'FINALIZADO'),
(13, 22, 11, '03:38:03', '00:00:03', 'FINALIZADO'),
(13, 23, 12, '03:38:04', '00:00:04', 'FINALIZADO'),
(13, 24, 13, '03:38:05', '00:00:05', 'FINALIZADO'),
(13, 25, 14, '03:38:06', '00:00:06', 'FINALIZADO'),
(13, 26, 15, '03:38:07', '00:00:07', 'FINALIZADO'),
(13, 27, 16, '03:38:00', '00:00:00', 'FINALIZADO'),
(13, 28, 17, '03:38:01', '00:00:01', 'FINALIZADO'),
(13, 29, 18, '03:38:02', '00:00:02', 'FINALIZADO'),
(13, 30, 19, '03:38:03', '00:00:03', 'FINALIZADO'),
(13, 31, 20, '03:38:04', '00:00:04', 'FINALIZADO'),
(13, 32, 21, '03:38:05', '00:00:05', 'FINALIZADO'),
(13, 33, 22, '03:38:06', '00:00:06', 'FINALIZADO'),
(13, 34, 23, '03:38:07', '00:00:07', 'FINALIZADO'),
(13, 35, 24, '03:38:00', '00:00:00', 'FINALIZADO'),
(13, 36, 25, '03:38:01', '00:00:01', 'FINALIZADO'),
(13, 37, 26, '03:38:02', '00:00:02', 'FINALIZADO'),
(13, 38, 27, '03:38:03', '00:00:03', 'FINALIZADO'),
(13, 39, 28, '03:38:04', '00:00:04', 'FINALIZADO'),
(13, 40, 29, '03:38:05', '00:00:05', 'FINALIZADO'),
(13, 41, 30, '03:38:06', '00:00:06', 'FINALIZADO'),
(13, 42, 31, '03:38:07', '00:00:07', 'FINALIZADO'),
(13, 43, 32, '03:38:00', '00:00:00', 'FINALIZADO'),
(13, 44, 33, '03:38:01', '00:00:01', 'FINALIZADO'),
(13, 45, 34, '03:38:02', '00:00:02', 'FINALIZADO'),
(13, 46, 35, '03:38:03', '00:00:03', 'FINALIZADO'),
(13, 47, 36, '03:38:04', '00:00:04', 'FINALIZADO'),
(13, 48, 37, '03:38:05', '00:00:05', 'FINALIZADO'),
(13, 49, 38, '03:38:06', '00:00:06', 'FINALIZADO'),
(13, 1, 39, '03:38:07', '00:00:07', 'FINALIZADO'),
(13, 2, 40, '03:38:00', '00:00:00', 'FINALIZADO'),
(13, 3, 41, '03:38:01', '00:00:01', 'FINALIZADO'),
(13, 4, 42, '03:38:02', '00:00:02', 'FINALIZADO'),
(13, 5, 43, '03:38:03', '00:00:03', 'FINALIZADO'),
(13, 6, 44, '03:38:04', '00:00:04', 'FINALIZADO'),
(13, 7, 45, '03:38:05', '00:00:05', 'FINALIZADO'),
(13, 8, 46, '03:38:06', '00:00:06', 'FINALIZADO'),
(13, 9, 47, '03:38:07', '00:00:07', 'FINALIZADO'),
(13, 10, 48, '03:38:00', '00:00:00', 'FINALIZADO'),
(13, 11, 49, '03:38:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(13, 12, 1, 50),
(13, 13, 2, 30),
(13, 14, 3, 20),
(13, 15, 4, 12),
(13, 16, 5, 10),
(13, 17, 6, 8),
(13, 18, 7, 6),
(13, 19, 8, 4),
(13, 20, 9, 2),
(13, 21, 10, 1);

INSERT INTO resultados_etapa (id_etapa, id_ciclista, posicion, tiempo, diferencia, estado) VALUES
(14, 17, 1, '04:05:00', '00:00:00', 'FINALIZADO'),
(14, 18, 2, '04:05:01', '00:00:01', 'FINALIZADO'),
(14, 19, 3, '04:05:01', '00:00:01', 'FINALIZADO'),
(14, 20, 4, '04:05:04', '00:00:04', 'FINALIZADO'),
(14, 21, 5, '04:05:05', '00:00:05', 'FINALIZADO'),
(14, 22, 6, '04:05:06', '00:00:06', 'FINALIZADO'),
(14, 23, 7, '04:05:07', '00:00:07', 'FINALIZADO'),
(14, 24, 8, '04:05:00', '00:00:00', 'FINALIZADO'),
(14, 25, 9, '04:05:01', '00:00:01', 'FINALIZADO'),
(14, 26, 10, '04:05:02', '00:00:02', 'FINALIZADO'),
(14, 27, 11, '04:05:03', '00:00:03', 'FINALIZADO'),
(14, 28, 12, '04:05:04', '00:00:04', 'FINALIZADO'),
(14, 29, 13, '04:05:05', '00:00:05', 'FINALIZADO'),
(14, 30, 14, '04:05:06', '00:00:06', 'FINALIZADO'),
(14, 31, 15, '04:05:07', '00:00:07', 'FINALIZADO'),
(14, 32, 16, '04:05:00', '00:00:00', 'FINALIZADO'),
(14, 33, 17, '04:05:01', '00:00:01', 'FINALIZADO'),
(14, 34, 18, '04:05:02', '00:00:02', 'FINALIZADO'),
(14, 35, 19, '04:05:03', '00:00:03', 'FINALIZADO'),
(14, 36, 20, '04:05:04', '00:00:04', 'FINALIZADO'),
(14, 37, 21, '04:05:05', '00:00:05', 'FINALIZADO'),
(14, 38, 22, '04:05:06', '00:00:06', 'FINALIZADO'),
(14, 39, 23, '04:05:07', '00:00:07', 'FINALIZADO'),
(14, 40, 24, '04:05:00', '00:00:00', 'FINALIZADO'),
(14, 41, 25, '04:05:01', '00:00:01', 'FINALIZADO'),
(14, 42, 26, '04:05:02', '00:00:02', 'FINALIZADO'),
(14, 43, 27, '04:05:03', '00:00:03', 'FINALIZADO'),
(14, 44, 28, '04:05:04', '00:00:04', 'FINALIZADO'),
(14, 45, 29, '04:05:05', '00:00:05', 'FINALIZADO'),
(14, 46, 30, '04:05:06', '00:00:06', 'FINALIZADO'),
(14, 47, 31, '04:05:07', '00:00:07', 'FINALIZADO'),
(14, 48, 32, '04:05:00', '00:00:00', 'FINALIZADO'),
(14, 49, 33, '04:05:01', '00:00:01', 'FINALIZADO'),
(14, 1, 34, '04:05:02', '00:00:02', 'FINALIZADO'),
(14, 2, 35, '04:05:03', '00:00:03', 'FINALIZADO'),
(14, 3, 36, '04:05:04', '00:00:04', 'FINALIZADO'),
(14, 4, 37, '04:05:05', '00:00:05', 'FINALIZADO'),
(14, 5, 38, '04:05:06', '00:00:06', 'FINALIZADO'),
(14, 6, 39, '04:05:07', '00:00:07', 'FINALIZADO'),
(14, 7, 40, '04:05:00', '00:00:00', 'FINALIZADO'),
(14, 8, 41, '04:05:01', '00:00:01', 'FINALIZADO'),
(14, 9, 42, '04:05:02', '00:00:02', 'FINALIZADO'),
(14, 10, 43, '04:05:03', '00:00:03', 'FINALIZADO'),
(14, 11, 44, '04:05:04', '00:00:04', 'FINALIZADO'),
(14, 12, 45, '04:05:05', '00:00:05', 'FINALIZADO'),
(14, 13, 46, '04:05:06', '00:00:06', 'FINALIZADO'),
(14, 14, 47, '04:05:07', '00:00:07', 'FINALIZADO'),
(14, 15, 48, '04:05:00', '00:00:00', 'FINALIZADO'),
(14, 16, 49, '04:05:01', '00:00:01', 'FINALIZADO');

INSERT INTO puntos_meta (id_etapa, id_ciclista, posicion, puntos) VALUES
(14, 17, 1, 50),
(14, 18, 2, 30),
(14, 19, 3, 20),
(14, 20, 4, 12),
(14, 21, 5, 10),
(14, 22, 6, 8),
(14, 23, 7, 6),
(14, 24, 8, 4),
(14, 25, 9, 2),
(14, 26, 10, 1);

-- Sprints intermedios y resultados

INSERT INTO sprints (id_etapa, km) VALUES (1, 56.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(1, 1, 1, 10),
(1, 2, 2, 6),
(1, 3, 3, 4),
(1, 4, 4, 2),
(1, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (1, 112.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(2, 2, 1, 10),
(2, 3, 2, 6),
(2, 4, 3, 4),
(2, 5, 4, 2),
(2, 6, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (2, 91.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(3, 1, 1, 10),
(3, 2, 2, 6),
(3, 3, 3, 4),
(3, 4, 4, 2),
(3, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (3, 80.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(4, 1, 1, 10),
(4, 2, 2, 6),
(4, 3, 3, 4),
(4, 4, 4, 2),
(4, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (4, 87.50);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(5, 1, 1, 10),
(5, 2, 2, 6),
(5, 3, 3, 4),
(5, 4, 4, 2),
(5, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (6, 51.67);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(6, 1, 1, 10),
(6, 2, 2, 6),
(6, 3, 3, 4),
(6, 4, 4, 2),
(6, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (6, 103.33);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(7, 2, 1, 10),
(7, 3, 2, 6),
(7, 4, 3, 4),
(7, 5, 4, 2),
(7, 6, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (8, 85.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(8, 1, 1, 10),
(8, 2, 2, 6),
(8, 3, 3, 4),
(8, 4, 4, 2),
(8, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (9, 55.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(9, 1, 1, 10),
(9, 2, 2, 6),
(9, 3, 3, 4),
(9, 4, 4, 2),
(9, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (9, 110.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(10, 2, 1, 10),
(10, 3, 2, 6),
(10, 4, 3, 4),
(10, 5, 4, 2),
(10, 6, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (11, 89.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(11, 1, 1, 10),
(11, 2, 2, 6),
(11, 3, 3, 4),
(11, 4, 4, 2),
(11, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (12, 50.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(12, 1, 1, 10),
(12, 2, 2, 6),
(12, 3, 3, 4),
(12, 4, 4, 2),
(12, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (12, 100.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(13, 2, 1, 10),
(13, 3, 2, 6),
(13, 4, 3, 4),
(13, 5, 4, 2),
(13, 6, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (13, 52.67);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(14, 1, 1, 10),
(14, 2, 2, 6),
(14, 3, 3, 4),
(14, 4, 4, 2),
(14, 5, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (13, 105.33);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(15, 2, 1, 10),
(15, 3, 2, 6),
(15, 4, 3, 4),
(15, 5, 4, 2),
(15, 6, 5, 1);

INSERT INTO sprints (id_etapa, km) VALUES (14, 92.00);

INSERT INTO resultados_sprint (id_sprint, id_ciclista, posicion, puntos) VALUES
(16, 1, 1, 10),
(16, 2, 2, 6),
(16, 3, 3, 4),
(16, 4, 4, 2),
(16, 5, 5, 1);

-- Puertos y resultados de montaña

INSERT INTO puertos (id_etapa, nombre, km, categoria) VALUES (2, 'Alto do Pego', 120.0, '3');

INSERT INTO resultados_puerto (id_puerto, id_ciclista, posicion, puntos) VALUES
(1, 4, 1, 5),
(1, 5, 2, 3),
(1, 6, 3, 2),
(1, 7, 4, 1);

INSERT INTO puertos (id_etapa, nombre, km, categoria) VALUES (4, 'Serra de Monchique', 150.0, '2');

INSERT INTO resultados_puerto (id_puerto, id_ciclista, posicion, puntos) VALUES
(2, 10, 1, 10),
(2, 11, 2, 6),
(2, 12, 3, 4),
(2, 13, 4, 2),
(2, 14, 5, 1);

INSERT INTO puertos (id_etapa, nombre, km, categoria) VALUES (5, 'Foia', 155.0, 'HC');

INSERT INTO resultados_puerto (id_puerto, id_ciclista, posicion, puntos) VALUES
(3, 13, 1, 20),
(3, 14, 2, 15),
(3, 15, 3, 10),
(3, 16, 4, 6),
(3, 17, 5, 4),
(3, 18, 6, 2);

INSERT INTO puertos (id_etapa, nombre, km, categoria) VALUES (5, 'Picota', 60.0, '2');

INSERT INTO resultados_puerto (id_puerto, id_ciclista, posicion, puntos) VALUES
(4, 13, 1, 10),
(4, 14, 2, 6),
(4, 15, 3, 4),
(4, 16, 4, 2),
(4, 17, 5, 1);

INSERT INTO puertos (id_etapa, nombre, km, categoria) VALUES (8, 'Cercal', 100.0, '3');

INSERT INTO resultados_puerto (id_puerto, id_ciclista, posicion, puntos) VALUES
(5, 22, 1, 5),
(5, 23, 2, 3),
(5, 24, 3, 2),
(5, 25, 4, 1);

INSERT INTO puertos (id_etapa, nombre, km, categoria) VALUES (10, 'Arrábida', 130.0, '2');

INSERT INTO resultados_puerto (id_puerto, id_ciclista, posicion, puntos) VALUES
(6, 28, 1, 10),
(6, 29, 2, 6),
(6, 30, 3, 4),
(6, 31, 4, 2),
(6, 32, 5, 1);

INSERT INTO puertos (id_etapa, nombre, km, categoria) VALUES (10, 'Alto da Caparica', 80.0, '3');

INSERT INTO resultados_puerto (id_puerto, id_ciclista, posicion, puntos) VALUES
(7, 28, 1, 5),
(7, 29, 2, 3),
(7, 30, 3, 2),
(7, 31, 4, 1);

INSERT INTO puertos (id_etapa, nombre, km, categoria) VALUES (11, 'Peninha', 90.0, '2');

INSERT INTO resultados_puerto (id_puerto, id_ciclista, posicion, puntos) VALUES
(8, 31, 1, 10),
(8, 32, 2, 6),
(8, 33, 3, 4),
(8, 34, 4, 2),
(8, 35, 5, 1);

INSERT INTO puertos (id_etapa, nombre, km, categoria) VALUES (14, 'Serra do Montejunto', 95.0, '2');

INSERT INTO resultados_puerto (id_puerto, id_ciclista, posicion, puntos) VALUES
(9, 40, 1, 10),
(9, 41, 2, 6),
(9, 42, 3, 4),
(9, 43, 4, 2),
(9, 44, 5, 1);

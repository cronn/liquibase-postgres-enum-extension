--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3 (Debian 16.3-1.pgdg120+1)
-- Dumped by pg_dump version 16.3 (Debian 16.3-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: color; Type: TYPE; Schema: public; Owner: test
--

CREATE TYPE public.color AS ENUM (
    'BLACK',
    'GREEN'
);


ALTER TYPE public.color OWNER TO test;

--
-- Name: language; Type: TYPE; Schema: public; Owner: test
--

CREATE TYPE public.language AS ENUM (
    'EN',
    'FR',
    'CH',
    'DE'
);


ALTER TYPE public.language OWNER TO test;

--
-- Name: should_be_dropped; Type: TYPE; Schema: public; Owner: test
--

CREATE TYPE public.should_be_dropped AS ENUM (
    'YELLOW',
    'BLACK'
);


ALTER TYPE public.should_be_dropped OWNER TO test;

--
-- Name: CAST (public.color AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.color AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (public.language AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.language AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (public.should_be_dropped AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.should_be_dropped AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS public.color); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.color) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS public.language); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.language) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS public.should_be_dropped); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.should_be_dropped) WITH INOUT AS IMPLICIT;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: test
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO test;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: test
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO test;

--
-- Name: entity_using_language1; Type: TABLE; Schema: public; Owner: test
--

CREATE TABLE public.entity_using_language1 (
    id bigint NOT NULL,
    language public.language
);


ALTER TABLE public.entity_using_language1 OWNER TO test;

--
-- Name: entity_using_language2; Type: TABLE; Schema: public; Owner: test
--

CREATE TABLE public.entity_using_language2 (
    id bigint NOT NULL,
    language public.language
);


ALTER TABLE public.entity_using_language2 OWNER TO test;

--
-- Name: simple_entity_with_enum; Type: TABLE; Schema: public; Owner: test
--

CREATE TABLE public.simple_entity_with_enum (
    id bigint NOT NULL,
    color public.color
);


ALTER TABLE public.simple_entity_with_enum OWNER TO test;

--
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: test
--

INSERT INTO public.databasechangelog VALUES ('change1', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 1, 'EXECUTED', '9:4b6982478dc5d873e2a5a2df4915c8c6', 'createPostgresEnumType name=colour', '', NULL, '4.27.0', NULL, NULL, 'test');
INSERT INTO public.databasechangelog VALUES ('change2', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 2, 'EXECUTED', '9:a89705da785e9daeffc3dc2e6adaa0e2', 'renamePostgresEnumType newName=color, oldName=colour', '', NULL, '4.27.0', NULL, NULL, 'test');
INSERT INTO public.databasechangelog VALUES ('change3', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 3, 'EXECUTED', '9:b9b7da0bc9ccdd850d013312f9fe97a6', 'addPostgresEnumValues enumTypeName=color', '', NULL, '4.27.0', NULL, NULL, 'test');
INSERT INTO public.databasechangelog VALUES ('change4', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 4, 'EXECUTED', '9:d77b821d2b89b652b1fb2f9124b81dd2', 'createTable tableName=simple_entity_with_enum; sql', '', NULL, '4.27.0', NULL, NULL, 'test');
INSERT INTO public.databasechangelog VALUES ('change5', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 5, 'EXECUTED', '9:ba0514d74b0eb7ccf7be91dd4511fc4b', 'renamePostgresEnumValue enumTypeName=color', '', NULL, '4.27.0', NULL, NULL, 'test');
INSERT INTO public.databasechangelog VALUES ('change6', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 6, 'EXECUTED', '9:5bda148ff2c03a342874b4979deaf392', 'createPostgresEnumType name=drop_me', '', NULL, '4.27.0', NULL, NULL, 'test');
INSERT INTO public.databasechangelog VALUES ('change7', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 7, 'EXECUTED', '9:44940c358b1483b3b6d0c8e3d072a818', 'dropPostgresEnumType name=drop_me', '', NULL, '4.27.0', NULL, NULL, 'test');
INSERT INTO public.databasechangelog VALUES ('change8', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 8, 'EXECUTED', '9:bb221ce19ea090295391f26eefbf5e61', 'createPostgresEnumType name=should_be_dropped', '', NULL, '4.27.0', NULL, NULL, 'test');
INSERT INTO public.databasechangelog VALUES ('change9', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 9, 'EXECUTED', '9:adac3bd97125e2a14931ae4befb926bb', 'createPostgresEnumType name=language; createTable tableName=entity_using_language1; createTable tableName=entity_using_language2; sql; sql', '', NULL, '4.27.0', NULL, NULL, 'test');
INSERT INTO public.databasechangelog VALUES ('change10', 'Jane Doe', 'migrations/full-changelog.xml', '2024-01-02 12:00:00', 10, 'EXECUTED', '9:abb4f59debeedd6c7a77b611d81043d3', 'modifyPostgresEnumType name=language', '', NULL, '4.27.0', NULL, NULL, 'test');


--
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: test
--

INSERT INTO public.databasechangeloglock VALUES (1, false, NULL, NULL);


--
-- Data for Name: entity_using_language1; Type: TABLE DATA; Schema: public; Owner: test
--

INSERT INTO public.entity_using_language1 VALUES (1, 'EN');
INSERT INTO public.entity_using_language1 VALUES (2, 'DE');


--
-- Data for Name: entity_using_language2; Type: TABLE DATA; Schema: public; Owner: test
--



--
-- Data for Name: simple_entity_with_enum; Type: TABLE DATA; Schema: public; Owner: test
--

INSERT INTO public.simple_entity_with_enum VALUES (1, 'GREEN');


--
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: test
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- Name: entity_using_language1 pk_entity_using_language1; Type: CONSTRAINT; Schema: public; Owner: test
--

ALTER TABLE ONLY public.entity_using_language1
    ADD CONSTRAINT pk_entity_using_language1 PRIMARY KEY (id);


--
-- Name: entity_using_language2 pk_entity_using_language2; Type: CONSTRAINT; Schema: public; Owner: test
--

ALTER TABLE ONLY public.entity_using_language2
    ADD CONSTRAINT pk_entity_using_language2 PRIMARY KEY (id);


--
-- Name: simple_entity_with_enum pk_simple_entity_with_enum; Type: CONSTRAINT; Schema: public; Owner: test
--

ALTER TABLE ONLY public.simple_entity_with_enum
    ADD CONSTRAINT pk_simple_entity_with_enum PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

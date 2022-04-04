--
-- PostgreSQL database dump
--

-- Dumped from database version 10.20
-- Dumped by pg_dump version 14.2

-- Started on 2022-04-03 22:55:42

--SET statement_timeout = 0;
--SET lock_timeout = 0;
--SET idle_in_transaction_session_timeout = 0;
--SET client_encoding = 'UTF8';
--SET standard_conforming_strings = on;
--SELECT pg_catalog.set_config('search_path', '', false);
--SET check_function_bodies = false;
--SET xmloption = content;
--SET client_min_messages = warning;
--SET row_security = off;

DROP SCHEMA PUBLIC CASCADE;
CREATE SCHEMA public;
CREATE USER postgres SUPERUSER;

--
-- TOC entry 201 (class 1259 OID 17115)
-- Name: balance_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.balance_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.balance_sequence OWNER TO postgres;

SET default_tablespace = '';

--
-- TOC entry 203 (class 1259 OID 17119)
-- Name: balance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.balance (
    blc_id integer DEFAULT nextval('public.balance_sequence'::regclass) NOT NULL,
    blc_code character varying(100),
    blc_amount numeric(15,2)
);


ALTER TABLE public.balance OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 17113)
-- Name: company_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.company_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.company_sequence OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 17145)
-- Name: company; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.company (
    cpn_id integer DEFAULT nextval('public.company_sequence'::regclass) NOT NULL,
    cpn_public_name character varying(100),
    cpn_corporate_name character varying(100),
    cpn_ptr_balance_id integer NOT NULL,
    cpn_siren character varying(100),
    cpn_siret character varying(100)
);


ALTER TABLE public.company OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 17103)
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: postgres
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


ALTER TABLE public.databasechangelog OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 17098)
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 17111)
-- Name: deposit_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.deposit_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.deposit_sequence OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 17158)
-- Name: deposit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.deposit (
    dps_id integer DEFAULT nextval('public.deposit_sequence'::regclass) NOT NULL,
    dps_amount numeric(15,2),
    dps_remaining_amount numeric(15,2),
    dps_begin_date timestamp without time zone,
    dps_end_date timestamp without time zone,
    dps_type character varying(100),
    dps_ptr_user_id integer NOT NULL,
    dps_ptr_company_id integer NOT NULL
);


ALTER TABLE public.deposit OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 17117)
-- Name: payment_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.payment_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.payment_sequence OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 17174)
-- Name: payment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment (
    pay_id integer DEFAULT nextval('public.payment_sequence'::regclass) NOT NULL,
    pay_amount numeric(15,2),
    pay_receiver character varying(60),
    pay_object character varying(60),
    pay_ptr_user_id integer NOT NULL
);


ALTER TABLE public.payment OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 17109)
-- Name: user_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_sequence OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 17127)
-- Name: user_challenge; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_challenge (
    usr_id integer DEFAULT nextval('public.user_sequence'::regclass) NOT NULL,
    usr_firstname character varying(40),
    usr_lastname character varying(40),
    usr_email character varying(40),
    usr_ptr_gift_balance_id integer NOT NULL,
    usr_ptr_meal_balance_id integer NOT NULL
);


ALTER TABLE public.user_challenge OWNER TO postgres;

--
-- TOC entry 2860 (class 0 OID 17119)
-- Dependencies: 203
-- Data for Name: balance; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.balance VALUES (4, 'elodie.rivalin@gmail.com_GIFT', 0.00);
INSERT INTO public.balance VALUES (2, 'aurore.melie@gmail.com_GIFT', 100.00);
INSERT INTO public.balance VALUES (5, 'Apple FRANCE', 99900.00);
INSERT INTO public.balance VALUES (3, 'elodie.rivalin@gmail.com_MEAL', 50.00);
INSERT INTO public.balance VALUES (1, 'aurore.melie@gmail.com_MEAL', 30.00);
INSERT INTO public.balance VALUES (6, 'BOULANGERIES PAUL', 420.00);


--
-- TOC entry 2862 (class 0 OID 17145)
-- Dependencies: 205
-- Data for Name: company; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.company VALUES (1, 'Apple', 'Apple FRANCE', 5, '322120916', '32212091600208');
INSERT INTO public.company VALUES (2, 'Paul', 'BOULANGERIES PAUL', 6, '403052111', '40305211102616');


--
-- TOC entry 2854 (class 0 OID 17103)
-- Dependencies: 197
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.databasechangelog VALUES ('seq_01_add_user_sequence', 'kvu', 'db/changelog/sequence.xml', '2022-04-04 01:49:38.893502', 1, 'EXECUTED', '8:05dcd87c02dce418ef9b5ce216239337', 'createSequence sequenceName=user_sequence', '', NULL, '4.9.0', NULL, NULL, '9029778829');
INSERT INTO public.databasechangelog VALUES ('seq_02_add_deposit_sequence', 'kvu', 'db/changelog/sequence.xml', '2022-04-04 01:49:38.897664', 2, 'EXECUTED', '8:684b021c7d5acc0512286efdbcb58431', 'createSequence sequenceName=deposit_sequence', '', NULL, '4.9.0', NULL, NULL, '9029778829');
INSERT INTO public.databasechangelog VALUES ('seq_03_add_company_sequence', 'kvu', 'db/changelog/sequence.xml', '2022-04-04 01:49:38.900939', 3, 'EXECUTED', '8:367f4c44b9c8d01e2176e0322bcbd58c', 'createSequence sequenceName=company_sequence', '', NULL, '4.9.0', NULL, NULL, '9029778829');
INSERT INTO public.databasechangelog VALUES ('seq_04_add_balance_sequence', 'kvu', 'db/changelog/sequence.xml', '2022-04-04 01:49:38.903688', 4, 'EXECUTED', '8:207e6788e29355b4a3293aff1d9e4d0c', 'createSequence sequenceName=balance_sequence', '', NULL, '4.9.0', NULL, NULL, '9029778829');
INSERT INTO public.databasechangelog VALUES ('seq_05_add_payment_sequence', 'kvu', 'db/changelog/sequence.xml', '2022-04-04 01:49:38.905858', 5, 'EXECUTED', '8:050be3c40237cede9ddf878fe21559c8', 'createSequence sequenceName=payment_sequence', '', NULL, '4.9.0', NULL, NULL, '9029778829');
INSERT INTO public.databasechangelog VALUES ('s001_01_balance', 'kvu', 'db/changelog/schema-001.xml', '2022-04-04 01:49:38.913466', 6, 'EXECUTED', '8:b31abc3af7d1333f579939825896fced', 'createTable tableName=balance', '', NULL, '4.9.0', NULL, NULL, '9029778829');
INSERT INTO public.databasechangelog VALUES ('s001_02_user', 'kvu', 'db/changelog/schema-001.xml', '2022-04-04 01:49:38.921614', 7, 'EXECUTED', '8:e779faae63b4641f0aa251b714d39d7b', 'createTable tableName=user_challenge', '', NULL, '4.9.0', NULL, NULL, '9029778829');
INSERT INTO public.databasechangelog VALUES ('s001_03_company', 'kvu', 'db/changelog/schema-001.xml', '2022-04-04 01:49:38.928197', 8, 'EXECUTED', '8:421c8b67c5b50ef6576ebf88bfdfbfdf', 'createTable tableName=company', '', NULL, '4.9.0', NULL, NULL, '9029778829');
INSERT INTO public.databasechangelog VALUES ('s001_04_deposit', 'kvu', 'db/changelog/schema-001.xml', '2022-04-04 01:49:38.933947', 9, 'EXECUTED', '8:a9714cbaa56864bef57ba048b14b32a8', 'createTable tableName=deposit', '', NULL, '4.9.0', NULL, NULL, '9029778829');
INSERT INTO public.databasechangelog VALUES ('s001_05_payment', 'kvu', 'db/changelog/schema-001.xml', '2022-04-04 01:49:38.938655', 10, 'EXECUTED', '8:f609d6b95712dca0a527d17ed6782fdd', 'createTable tableName=payment', '', NULL, '4.9.0', NULL, NULL, '9029778829');


--
-- TOC entry 2853 (class 0 OID 17098)
-- Dependencies: 196
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.databasechangeloglock VALUES (1, false, NULL, NULL);


--
-- TOC entry 2863 (class 0 OID 17158)
-- Dependencies: 206
-- Data for Name: deposit; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.deposit VALUES (1, 100.00, 100.00, '2022-02-03 23:55:13.554', '2023-02-03 23:55:13.554', 'GIFT', 1, 1);
INSERT INTO public.deposit VALUES (2, 50.00, 50.00, '2022-02-03 23:55:13.554', '2023-02-28 00:00:00', 'MEAL', 2, 2);
INSERT INTO public.deposit VALUES (3, 30.00, 30.00, '2022-02-03 23:55:13.554', '2023-02-28 00:00:00', 'MEAL', 1, 2);


--
-- TOC entry 2864 (class 0 OID 17174)
-- Dependencies: 207
-- Data for Name: payment; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2861 (class 0 OID 17127)
-- Dependencies: 204
-- Data for Name: user_challenge; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.user_challenge VALUES (1, 'Aurore', 'Mélie', 'aurore.melie@gmail.com', 2, 1);
INSERT INTO public.user_challenge VALUES (2, 'Elodie', 'Rivalin', 'elodie.rivalin@gmail.com', 4, 3);


--
-- TOC entry 2870 (class 0 OID 0)
-- Dependencies: 201
-- Name: balance_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.balance_sequence', 6, true);


--
-- TOC entry 2871 (class 0 OID 0)
-- Dependencies: 200
-- Name: company_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.company_sequence', 2, true);


--
-- TOC entry 2872 (class 0 OID 0)
-- Dependencies: 199
-- Name: deposit_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.deposit_sequence', 3, true);


--
-- TOC entry 2873 (class 0 OID 0)
-- Dependencies: 202
-- Name: payment_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.payment_sequence', 1, false);


--
-- TOC entry 2874 (class 0 OID 0)
-- Dependencies: 198
-- Name: user_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_sequence', 2, true);


--
-- TOC entry 2711 (class 2606 OID 17126)
-- Name: balance balance_blc_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.balance
    ADD CONSTRAINT balance_blc_code_key UNIQUE (blc_code);


--
-- TOC entry 2713 (class 2606 OID 17124)
-- Name: balance balance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.balance
    ADD CONSTRAINT balance_pkey PRIMARY KEY (blc_id);


--
-- TOC entry 2719 (class 2606 OID 17152)
-- Name: company company_cpn_corporate_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company
    ADD CONSTRAINT company_cpn_corporate_name_key UNIQUE (cpn_corporate_name);


--
-- TOC entry 2721 (class 2606 OID 17150)
-- Name: company company_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company
    ADD CONSTRAINT company_pkey PRIMARY KEY (cpn_id);


--
-- TOC entry 2709 (class 2606 OID 17102)
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- TOC entry 2723 (class 2606 OID 17163)
-- Name: deposit deposit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deposit
    ADD CONSTRAINT deposit_pkey PRIMARY KEY (dps_id);


--
-- TOC entry 2725 (class 2606 OID 17179)
-- Name: payment payment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT payment_pkey PRIMARY KEY (pay_id);


--
-- TOC entry 2715 (class 2606 OID 17132)
-- Name: user_challenge user_challenge_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_challenge
    ADD CONSTRAINT user_challenge_pkey PRIMARY KEY (usr_id);


--
-- TOC entry 2717 (class 2606 OID 17134)
-- Name: user_challenge user_challenge_usr_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_challenge
    ADD CONSTRAINT user_challenge_usr_email_key UNIQUE (usr_email);


--
-- TOC entry 2728 (class 2606 OID 17153)
-- Name: company fk_cpn_blc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.company
    ADD CONSTRAINT fk_cpn_blc FOREIGN KEY (cpn_ptr_balance_id) REFERENCES public.balance(blc_id);


--
-- TOC entry 2729 (class 2606 OID 17164)
-- Name: deposit fk_dps_cpn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deposit
    ADD CONSTRAINT fk_dps_cpn FOREIGN KEY (dps_ptr_company_id) REFERENCES public.company(cpn_id);


--
-- TOC entry 2730 (class 2606 OID 17169)
-- Name: deposit fk_dps_usr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.deposit
    ADD CONSTRAINT fk_dps_usr FOREIGN KEY (dps_ptr_user_id) REFERENCES public.user_challenge(usr_id);


--
-- TOC entry 2731 (class 2606 OID 17180)
-- Name: payment fk_pay_usr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment
    ADD CONSTRAINT fk_pay_usr FOREIGN KEY (pay_ptr_user_id) REFERENCES public.user_challenge(usr_id);


--
-- TOC entry 2726 (class 2606 OID 17135)
-- Name: user_challenge fk_usr_blc_gift; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_challenge
    ADD CONSTRAINT fk_usr_blc_gift FOREIGN KEY (usr_ptr_gift_balance_id) REFERENCES public.balance(blc_id);


--
-- TOC entry 2727 (class 2606 OID 17140)
-- Name: user_challenge fk_usr_blc_meal; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_challenge
    ADD CONSTRAINT fk_usr_blc_meal FOREIGN KEY (usr_ptr_meal_balance_id) REFERENCES public.balance(blc_id);


-- Completed on 2022-04-04 01:59:55

--
-- PostgreSQL database dump complete
--


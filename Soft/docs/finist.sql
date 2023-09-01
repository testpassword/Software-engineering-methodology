--
-- PostgreSQL database dump
--

-- Dumped from database version 15.4
-- Dumped by pg_dump version 15.4

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
-- Name: comp_status; Type: TYPE; Schema: public; Owner: pugalol
--

CREATE TYPE public.comp_status AS ENUM (
    'IN_PROGRESS',
    'VOTING',
    'WAITING_AGREEMENT',
    'MARRIAGE'
);


ALTER TYPE public.comp_status OWNER TO pugalol;

--
-- Name: education; Type: TYPE; Schema: public; Owner: pugalol
--

CREATE TYPE public.education AS ENUM (
    'Церковно-приходское',
    'Среднее общее',
    'Среднее профессиональное',
    'Высшее'
);


ALTER TYPE public.education OWNER TO pugalol;

--
-- Name: role; Type: TYPE; Schema: public; Owner: pugalol
--

CREATE TYPE public.role AS ENUM (
    'matchmaker',
    'groom',
    'bride',
    'guest',
    'assistant',
    'enemy'
);


ALTER TYPE public.role OWNER TO pugalol;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: arrow_price; Type: TABLE; Schema: public; Owner: pugalol
--

CREATE TABLE public.arrow_price (
    price integer DEFAULT 100 NOT NULL,
    CONSTRAINT arrow_price_price_check CHECK ((price > 0))
);


ALTER TABLE public.arrow_price OWNER TO pugalol;

--
-- Name: bride_votes; Type: TABLE; Schema: public; Owner: pugalol
--

CREATE TABLE public.bride_votes (
    id integer NOT NULL,
    "competitionId" integer NOT NULL,
    "brideId" integer NOT NULL,
    points integer DEFAULT 0 NOT NULL,
    "endTime" timestamp without time zone DEFAULT (CURRENT_TIMESTAMP + '30 days'::interval) NOT NULL
);


ALTER TABLE public.bride_votes OWNER TO pugalol;

--
-- Name: competitions; Type: TABLE; Schema: public; Owner: pugalol
--

CREATE TABLE public.competitions (
    id integer NOT NULL,
    name text DEFAULT ''::text NOT NULL,
    city text DEFAULT ''::text NOT NULL,
    status public.comp_status DEFAULT 'IN_PROGRESS'::public.comp_status NOT NULL
);


ALTER TABLE public.competitions OWNER TO pugalol;

--
-- Name: follows; Type: TABLE; Schema: public; Owner: pugalol
--

CREATE TABLE public.follows (
    id integer NOT NULL,
    "userId" integer NOT NULL,
    "competitionId" integer NOT NULL
);


ALTER TABLE public.follows OWNER TO pugalol;

--
-- Name: marriages; Type: TABLE; Schema: public; Owner: pugalol
--

CREATE TABLE public.marriages (
    id integer NOT NULL,
    "competitionId" integer NOT NULL,
    "groomId" integer NOT NULL,
    "brideId" integer NOT NULL,
    "groomAgreement" boolean DEFAULT false NOT NULL,
    "brideAgreement" boolean DEFAULT false NOT NULL,
    report text DEFAULT ''::text NOT NULL
);


ALTER TABLE public.marriages OWNER TO pugalol;

--
-- Name: tasks; Type: TABLE; Schema: public; Owner: pugalol
--

CREATE TABLE public.tasks (
    id integer NOT NULL,
    text text DEFAULT ''::text NOT NULL,
    "executorId" integer NOT NULL,
    report text DEFAULT ''::text NOT NULL,
    completed boolean DEFAULT false NOT NULL,
    "competitionId" integer NOT NULL
);


ALTER TABLE public.tasks OWNER TO pugalol;

--
-- Name: users; Type: TABLE; Schema: public; Owner: pugalol
--

CREATE TABLE public.users (
    role public.role,
    id integer NOT NULL,
    phone integer NOT NULL,
    name text DEFAULT ''::text NOT NULL,
    "dateOfBirth" timestamp without time zone,
    city text DEFAULT ''::text NOT NULL,
    password text DEFAULT ''::text NOT NULL,
    education public.education,
    "aboutSelf" text DEFAULT ''::text NOT NULL,
    "aboutPartner" text DEFAULT ''::text NOT NULL,
    "isPairing" boolean DEFAULT false NOT NULL,
    arrows_amount integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.users OWNER TO pugalol;

--
-- Data for Name: arrow_price; Type: TABLE DATA; Schema: public; Owner: pugalol
--

COPY public.arrow_price (price) FROM stdin;
\.


--
-- Data for Name: bride_votes; Type: TABLE DATA; Schema: public; Owner: pugalol
--

COPY public.bride_votes (id, "competitionId", "brideId", points, "endTime") FROM stdin;
\.


--
-- Data for Name: competitions; Type: TABLE DATA; Schema: public; Owner: pugalol
--

COPY public.competitions (id, name, city, status) FROM stdin;
\.


--
-- Data for Name: follows; Type: TABLE DATA; Schema: public; Owner: pugalol
--

COPY public.follows (id, "userId", "competitionId") FROM stdin;
\.


--
-- Data for Name: marriages; Type: TABLE DATA; Schema: public; Owner: pugalol
--

COPY public.marriages (id, "competitionId", "groomId", "brideId", "groomAgreement", "brideAgreement", report) FROM stdin;
\.


--
-- Data for Name: tasks; Type: TABLE DATA; Schema: public; Owner: pugalol
--

COPY public.tasks (id, text, "executorId", report, completed, "competitionId") FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: pugalol
--

COPY public.users (role, id, phone, name, "dateOfBirth", city, password, education, "aboutSelf", "aboutPartner", "isPairing", arrows_amount) FROM stdin;
\.


--
-- Name: bride_votes bride_votes_pkey; Type: CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.bride_votes
    ADD CONSTRAINT bride_votes_pkey PRIMARY KEY (id);


--
-- Name: competitions competitions_pkey; Type: CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.competitions
    ADD CONSTRAINT competitions_pkey PRIMARY KEY (id);


--
-- Name: marriages marriages_pkey; Type: CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.marriages
    ADD CONSTRAINT marriages_pkey PRIMARY KEY (id);


--
-- Name: tasks tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: bride_votes bride_votes_brideId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.bride_votes
    ADD CONSTRAINT "bride_votes_brideId_fkey" FOREIGN KEY ("brideId") REFERENCES public.users(id);


--
-- Name: bride_votes bride_votes_competitionId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.bride_votes
    ADD CONSTRAINT "bride_votes_competitionId_fkey" FOREIGN KEY ("competitionId") REFERENCES public.competitions(id);


--
-- Name: follows follows_competitionId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.follows
    ADD CONSTRAINT "follows_competitionId_fkey" FOREIGN KEY ("competitionId") REFERENCES public.competitions(id);


--
-- Name: follows follows_userId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.follows
    ADD CONSTRAINT "follows_userId_fkey" FOREIGN KEY ("userId") REFERENCES public.users(id);


--
-- Name: marriages marriages_brideId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.marriages
    ADD CONSTRAINT "marriages_brideId_fkey" FOREIGN KEY ("brideId") REFERENCES public.users(id);


--
-- Name: marriages marriages_competitionId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.marriages
    ADD CONSTRAINT "marriages_competitionId_fkey" FOREIGN KEY ("competitionId") REFERENCES public.competitions(id);


--
-- Name: marriages marriages_groomId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.marriages
    ADD CONSTRAINT "marriages_groomId_fkey" FOREIGN KEY ("groomId") REFERENCES public.users(id);


--
-- Name: tasks tasks_competitionId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT "tasks_competitionId_fkey" FOREIGN KEY ("competitionId") REFERENCES public.competitions(id);


--
-- Name: tasks tasks_executorId_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pugalol
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT "tasks_executorId_fkey" FOREIGN KEY ("executorId") REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--


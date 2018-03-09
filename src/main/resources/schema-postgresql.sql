CREATE SCHEMA public
    AUTHORIZATION gdauser;

CREATE TABLE public.absence (
    id integer NOT NULL,
    date_debut bytea NOT NULL,
    date_fin bytea NOT NULL,
    motif character varying(255),
    statut integer,
    type integer NOT NULL,
    collaborateur_matricule character varying(255)
);


ALTER TABLE public.absence OWNER TO gdauser;

CREATE SEQUENCE public.absence_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.absence_id_seq OWNER TO gdauser;

ALTER SEQUENCE public.absence_id_seq OWNED BY public.absence.id;

CREATE TABLE public.collaborateur (
    matricule character varying(255) NOT NULL,
    actif boolean,
    conges integer,
    departement character varying(255),
    email character varying(255),
    nom character varying(255),
    password character varying(255),
    prenom character varying(255),
    rtt integer,
    rtt_employeur integer
);


ALTER TABLE public.collaborateur OWNER TO gdauser;

CREATE TABLE public.collaborateur_roles (
    collaborateur_matricule character varying(255) NOT NULL,
    roles character varying(255)
);


ALTER TABLE public.collaborateur_roles OWNER TO gdauser;


CREATE TABLE public.collaborateur_subalternes (
    collaborateur_matricule character varying(255) NOT NULL,
    subalternes_matricule character varying(255) NOT NULL
);


ALTER TABLE public.collaborateur_subalternes OWNER TO gdauser;


CREATE TABLE public.jour_ferie (
    id integer NOT NULL,
    commentaire character varying(255),
    date bytea NOT NULL,
    type integer NOT NULL
);


ALTER TABLE public.jour_ferie OWNER TO gdauser;


CREATE SEQUENCE public.jour_ferie_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.jour_ferie_id_seq OWNER TO gdauser;


ALTER SEQUENCE public.jour_ferie_id_seq OWNED BY public.jour_ferie.id;


ALTER TABLE ONLY public.absence ALTER COLUMN id SET DEFAULT nextval('public.absence_id_seq'::regclass);


ALTER TABLE ONLY public.jour_ferie ALTER COLUMN id SET DEFAULT nextval('public.jour_ferie_id_seq'::regclass);
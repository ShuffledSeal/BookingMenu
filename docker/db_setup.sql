DROP TABLE IF EXISTS patient CASCADE;
DROP TABLE IF EXISTS doctor CASCADE;
DROP TABLE IF EXISTS receptionist CASCADE;
DROP TABLE IF EXISTS booking CASCADE;

CREATE TABLE doctor (
	dID SERIAL PRIMARY KEY,
	firstname VARCHAR(20) NOT NULL check (length(firstname) > 3),
	lastname VARCHAR(20) NOT NULL check (length(lastname) > 3),
	gender VARCHAR(1) NOT NULL check (gender in ('M','F','O','P')),
	phoneNum VARCHAR(13) NOT NULL check (length(phoneNum) = 13) check (phoneNum ~ '^[0-9]*$'),
	email VARCHAR(40) NOT NULL,
	address VARCHAR
);

CREATE TABLE patient (
	pID SERIAL PRIMARY KEY,
	dID INTEGER,
	firstname VARCHAR(20) NOT NULL check (length(firstname) > 3),
	middlename VARCHAR(20) NOT NULL check (length(middlename) > 3),
	lastname VARCHAR(20) NOT NULL check (length(lastname) > 3),
    gender VARCHAR(1) NOT NULL check (gender in ('M','F','O','P')),
	isMarried BOOLEAN NOT NULL,
	hasChildren BOOLEAN NOT NULL,
	phoneNum VARCHAR(13) NOT NULL check (length(phoneNum) = 13) check (phoneNum ~ '^[0-9]*$'),
	email VARCHAR(40) NOT NULL,
	address VARCHAR NOT NULL,
	disabilities VARCHAR,
	currentJob VARCHAR,
	FOREIGN KEY(dID) REFERENCES doctor (dID)
);

CREATE TABLE receptionist (
	rID SERIAL PRIMARY KEY,
	login VARCHAR(50) NOT NULL UNIQUE,
	pwd VARCHAR(255) NOT NULL,
	salt VARCHAR NOT NULL
);

CREATE TABLE booking (
    pID INTEGER,
    dID INTEGER,
    date DATE NOT NULL,
    time TIME NOT NULL,
    PRIMARY KEY (pID, dID),
    FOREIGN KEY(pID) REFERENCES patient (pID),
    FOREIGN KEY(dID) REFERENCES doctor (dID)
);
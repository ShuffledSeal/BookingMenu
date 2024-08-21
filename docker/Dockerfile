FROM postgres
ENV POSTGRES_PASSWORD password
ENV POSTGRES_DB master
COPY ./db_setup.sql /docker-entrypoint-initdb.d/
COPY ./pg_hba.conf /etc/postgresql/13/main/pg_hba.conf

COPY ./postgresql.conf /etc/postgresql/13/main/postgresql.conf
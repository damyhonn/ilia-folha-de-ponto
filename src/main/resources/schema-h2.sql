create table alocacao (
    id integer primary key,
    dia varchar(50),
    tempo varchar(50),
    nome_projeto varchar(256)
);

create table mensagem (
    id integer primary key,
    mensagem varchar(256)
);

create table momento (
    id integer primary key,
    data_hora varchar(50)
);

create table registro (
    id integer primary key,
    dia varchar(50)
);

create table relatorio (
    id integer primary key,
    mes varchar(7),
    horas_trabalhadas varchar(50),
    horas_excedentes varchar(50),
    horas_devidas varchar(50),
    tempo varchar(50),
    nome_projeto varchar(256)
);

create table registro_horarios (
    registro_id integer primary key,
    horario varchar(50)
);

create table relatorio_registro (
    relatorio_id integer,
    registro_id integer
);

create table relatorio_alocacao (
    relatorio_id integer,
    alocacao_id integer
);
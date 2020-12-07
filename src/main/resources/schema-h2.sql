create table alocacao (
    id IDENTITY primary key,
    dia varchar(50),
    tempo varchar(50),
    nome_projeto varchar(256)
);

create table mensagem (
    id integer primary key,
    mensagem varchar(256)
);

create table momento (
    id IDENTITY primary key,
    data_hora varchar(50)
);

create table registro (
    id IDENTITY primary key,
    dia varchar(50)
);

create table relatorio (
    id IDENTITY primary key,
    mes varchar(7),
    horas_trabalhadas varchar(50),
    horas_excedentes varchar(50),
    horas_devidas varchar(50),
    tempo varchar(50),
    nome_projeto varchar(256)
);

create table registro_horarios (
	id IDENTITY primary key,
    registro_id integer not null,
    horario timestamp,
    foreign key (registro_id) references registro(id)
);

create table relatorio_registro (
    relatorio_id integer,
    registro_id integer
);

create table alocacao_relatorio (
	id IDENTITY primary key,
	relatorio_id integer,
	nome_projeto varchar(256),
    tempo varchar(50),
    foreign key (relatorio_id) references relatorio(id)
);

create table relatorio_alocacoes (
	alocacoes_id integer,
	relatorios_id integer
);
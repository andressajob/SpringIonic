--CATEGORY
INSERT INTO categoria(id, nome) VALUES
  (1001, 'Informática'), 
  (1002, 'Escritório'), 
  (1003, 'Cama'), 
  (1004, 'Mesa'), 
  (1005, 'Banho'), 
  (1006, 'Decoração'),
  (1007, 'Perfumaria');

--AUTHENTICATION
INSERT INTO users(id, username, password, name, email, active) VALUES
(1001, 'user', '$2a$10$Qji2/icFWIGGQEAv8bbwNuKGrSZyiUUPqE/0SNO2M.BpU.NA6xPhW', 'Vendedor','vendedor@outlook.com', true);
INSERT INTO users(id, username, password, name, email, active) VALUES
(1002, 'admin', '$2a$10$m/3j6fZltLuJVwUM9vEtBe4BWJByEPwn7AsB8TF5KVFin6mpEg20O', 'Administrador','admin@gmail.com', true);
--ROLES
INSERT INTO roles(id, role) values
(1001, 'ROLE_USER'), (1002, 'ROLE_ADMIN');

-- USER_ROLES
INSERT INTO users_roles (user_id, roles_id) values
(1001, 1001), (1002, 1002);

-- CLIENTS
INSERT INTO cliente (id, nome, email, cpf_ou_cnpj, senha, tipo) VALUES
(1,'Usuário Comum', 'vicktor.junior@gmail.com', '11111111111', 'senha', 1);

-- STATES
INSERT INTO estado (id, nome) VALUES
(1001,'Rio Grande do Sul'),(1002, 'Santa Catarina');

-- CITIES
INSERT INTO cidade (id, nome, estado_id) VALUES
(1001, 'Porto Alegre', 1001), 
(1002,'Florianópolis',1002), 
(1003,'Canoas',1001);

-- ADDRESSES
INSERT INTO endereco (id, logradouro, numero, complemento, bairro, cep, cidade_id, cliente_id) VALUES
(1, 'Endereço Padrão', '0', '', '', '00000000', 1001, 1);
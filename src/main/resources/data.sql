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
(1001, 'user', '$2a$10$Qji2/icFWIGGQEAv8bbwNuKGrSZyiUUPqE/0SNO2M.BpU.NA6xPhW', 'Tony Stark','tony@stark.com', true);
INSERT INTO users(id, username, password, name, email, active) VALUES
(1002, 'admin', '$2a$10$m/3j6fZltLuJVwUM9vEtBe4BWJByEPwn7AsB8TF5KVFin6mpEg20O', 'Administrador','admin@gmail.com', true);
--ROLES
INSERT INTO roles(id, role) values
(1001, 'ROLE_USER'), (1002, 'ROLE_ADMIN');

-- USER_ROLES
INSERT INTO users_roles (user_id, roles_id) values
(1001, 1001), (1002, 1002);

--PRODUCTS
INSERT INTO produto (id, nome, preco) values
(1001,'Computador', 2000.00),
(1002,'Impressora', 800.00),
(1003,'Mouse', 80.00),
(1004,'Mesa',300.00),
(1005,'Toalha',50.00),
(1006,'Colcha',200.00),
(1007,'TV True Color',1200.00),
(1008,'Roçadeira',800.00),
(1009,'Abajour',100.00),
(1010,'Shampoo',90.00);

--PRODUCT _ CATEGORY
INSERT INTO produto_categoria(produto_id, categoria_id) values
(1001,1001),
(1001,1004),
(1002,1001),
(1002,1002),
(1002,1004),
(1003,1001),
(1003,1004),
(1004,1002),
(1005,1003),
(1006,1003),
(1007,1004),
(1008,1005),
(1009,1006),
(1010,1007);

-- CLIENTS
INSERT INTO cliente (id, nome, email, cpf_ou_cnpj, senha, tipo) VALUES
(1001,'Usuário Comum', 'vicktor.junior@gmail.com', '11111111111', 'senha', 1);

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
(1001, 'Endereço Padrão', '0', '', '', '00000000', 1001, 1001);


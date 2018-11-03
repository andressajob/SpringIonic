--AUTHENTICATION
INSERT INTO users(id, username, password, name, email, active) VALUES
  (1001, 'user', '$2a$10$Qji2/icFWIGGQEAv8bbwNuKGrSZyiUUPqE/0SNO2M.BpU.NA6xPhW', 'Vendedor','vicktor.junior@gmail.com', true);
INSERT INTO users(id, username, password, name, email, active) VALUES
  (1002, 'admin', '$2a$10$m/3j6fZltLuJVwUM9vEtBe4BWJByEPwn7AsB8TF5KVFin6mpEg20O', 'Administrador','vicktor.junior@gmail.com', true);
--ROLES
INSERT INTO roles(id, role) values
  (1001, 'ROLE_USER'), (1002, 'ROLE_ADMIN');
-- USER_ROLES
INSERT INTO users_roles (user_id, roles_id) values
  (1001, 1001), (1002, 1002);
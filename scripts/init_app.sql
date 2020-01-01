

use netmon_app;
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
INSERT INTO roles(name) VALUES('ROLE_CUSTOMER');
INSERT INTO roles(name) VALUES('ROLE_OFFICE');
INSERT INTO roles(name) VALUES('ROLE_TECHNICAL');
INSERT INTO roles(name) VALUES('ROLE_MANAGER');

insert INTO users(created_at, updated_at, activation_code, active, email, forgotten_code, mobile, name, password, tel_number, username, nationalid) VALUES ( CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,"00000",'Y',"admin@netmon.com","99999","09120339307","admin","$2a$10$ImL2J9O5IqTlRfnV7oxbx.LjH9/a0YvW6DQMMR61QEjiZnBfuEAvC","02166535215", "admin", 1234567890);
insert INTO user_roles(user_id,role_id) VALUES (1,1);


insert INTO users(created_at, updated_at, activation_code, active, email, forgotten_code, mobile, name, password, tel_number, username, nationalid) VALUES ( CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,"00000",'Y',"mollaahmadi@gmail.com","99999","09120339307","mollaahmadi","$2a$10$ImL2J9O5IqTlRfnV7oxbx.LjH9/a0YvW6DQMMR61QEjiZnBfuEAvC","02166535215", "mollaahmadi", 1234567890);
insert INTO user_roles(user_id,role_id) VALUES (2,2);

insert INTO users(created_at, updated_at, activation_code, active, email, forgotten_code, mobile, name, password, tel_number, username, nationalid) VALUES ( CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,"00000",'Y',"office@netmon.com","99999","09120339307","mollaahmadi","$2a$10$ImL2J9O5IqTlRfnV7oxbx.LjH9/a0YvW6DQMMR61QEjiZnBfuEAvC","02166535215", "office1", 1234567890);
insert INTO user_roles(user_id,role_id) VALUES (3,3);

insert INTO users(created_at, updated_at, activation_code, active, email, forgotten_code, mobile, name, password, tel_number, username, nationalid) VALUES ( CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,"00000",'Y',"office@netmon.com","99999","09120339307","mollaahmadi","$2a$10$ImL2J9O5IqTlRfnV7oxbx.LjH9/a0YvW6DQMMR61QEjiZnBfuEAvC","02166535215", "technical", 1234567890);
insert INTO user_roles(user_id,role_id) VALUES (4,4);

insert INTO users(created_at, updated_at, activation_code, active, email, forgotten_code, mobile, name, password, tel_number, username, nationalid) VALUES ( CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,"00000",'Y',"manager@netmon.com","99999","09120339307","mollaahmadi","$2a$10$ImL2J9O5IqTlRfnV7oxbx.LjH9/a0YvW6DQMMR61QEjiZnBfuEAvC","02166535215", "manager", 1234567890);
insert INTO user_roles(user_id,role_id) VALUES (5, 5);


-- insert INTO users(created_at, updated_at, activation_code, active, email, forgotten_code, mobile, name, password, tel_number, username) VALUES ( CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,"00000",'Y',"mollaahmadi@gmail.com","99999","09120339307","mollaahmadi","$2a$10$ImL2J9O5IqTlRfnV7oxbx.LjH9/a0YvW6DQMMR61QEjiZnBfuEAvC","02166535215", "mollaahmadi2");
-- insert INTO user_roles(user_id,role_id) VALUES (7,2);



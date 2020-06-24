use jdbc_authentication;

CREATE TABLE users (
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (username)
);

CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username
  on authorities (username,authority);

INSERT INTO users (username, password, enabled)
  values ('admin',
    '$2a$10$CNPpPTLGLNvO.O4t/Io5S.JZYpI7/X/5kiORP0VcKnKLnIRb3CLSe',
    1);

INSERT INTO users (username, password, enabled)
  values ('user',
    '$2a$10$mIb/bICwTrcZNgn0wB565ev3lM7mOePDS/mliTYc0lepext6n7F.a',
    1);
INSERT INTO users (username, password, enabled)
  values ('user2',
    '$2a$10$mIb/bICwTrcZNgn0wB565ev3lM7mOePDS/mliTYc0lepext6n7F.a',
    1);
INSERT INTO authorities (username, authority)
  values ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority)
  values ('admin', 'ADMIN_USER');
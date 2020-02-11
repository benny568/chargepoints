CREATE USER  IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'appuser';
GRANT ALL PRIVILEGES ON session.* TO 'appuser'@'localhost';
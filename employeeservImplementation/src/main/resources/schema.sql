DROP TABLE IF EXISTS employees;

CREATE TABLE employees (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  date_of_birth DATE NOT NULL,
  address_line_1 VARCHAR(255) NOT NULL,
  address_line_2 VARCHAR(255) DEFAULT NULL,
  city VARCHAR(20) NOT NULL,
  state VARCHAR(20) NOT NULL,
  country VARCHAR(20) NOT NULL,
  zip_code INT NOT NULL,
  uuid VARCHAR(64) NOT NULL
)
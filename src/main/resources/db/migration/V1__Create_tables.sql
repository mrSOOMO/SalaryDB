-- Таблиця: Відділ
CREATE TABLE IF NOT EXISTS department (
                                          id SERIAL PRIMARY KEY,
                                          name VARCHAR(100) NOT NULL,
                                          manager VARCHAR(100)
);

-- Таблиця: Посада
CREATE TABLE IF NOT EXISTS position (
                                        id SERIAL PRIMARY KEY,
                                        title VARCHAR(100) NOT NULL,
                                        salary DECIMAL(10, 2) NOT NULL
);

-- Таблиця: Працівник
CREATE TABLE IF NOT EXISTS employee (
                                        id SERIAL PRIMARY KEY,
                                        first_name VARCHAR(100) NOT NULL,
                                        last_name VARCHAR(100) NOT NULL,
                                        department_id INT REFERENCES department(id),
                                        position_id INT REFERENCES position(id)
);

-- Таблиця: Табель
CREATE TABLE IF NOT EXISTS timesheet (
                                         id SERIAL PRIMARY KEY,
                                         date VARCHAR(100) NOT NULL,
                                         hours INT NOT NULL,
                                         employee_id INT REFERENCES employee(id)
);

-- Таблиця: Заробітна Плата
CREATE TABLE IF NOT EXISTS payroll (
                                       id SERIAL PRIMARY KEY,
                                       amount DECIMAL(10, 2) NOT NULL,
                                       payment_date VARCHAR(100) NOT NULL,
                                       employee_id INT REFERENCES employee(id)
);

-- Таблиця: Податкові Відрахування
CREATE TABLE IF NOT EXISTS tax_deduction (
                                             id SERIAL PRIMARY KEY,
                                             deduction_type VARCHAR(100) NOT NULL,
                                             rate DECIMAL(10, 2) NOT NULL,
                                             payroll_id INT REFERENCES payroll(id)
);

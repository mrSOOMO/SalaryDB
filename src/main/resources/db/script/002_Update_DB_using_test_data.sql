-- Оновлення даних у таблиці Відділ
INSERT INTO department (id, name, manager)
VALUES (1, 'HR', 'Іван Іванов'),
       (2, 'Бухгалтерія', 'Петро Петров')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, manager = EXCLUDED.manager;

-- Оновлення даних у таблиці Посада
INSERT INTO position (id, title, salary)
VALUES (1, 'Менеджер', 50000.00),
       (2, 'Бухгалтер', 40000.00)
ON CONFLICT (id) DO UPDATE
    SET title = EXCLUDED.title, salary = EXCLUDED.salary;

-- Оновлення даних у таблиці Працівник
INSERT INTO employee (id, first_name, last_name, department_id, position_id)
VALUES (1, 'Андрій', 'Андрієнко', 1, 1),
       (2, 'Марія', 'Марієнко', 2, 2)
ON CONFLICT (id) DO UPDATE
    SET first_name = EXCLUDED.first_name, last_name = EXCLUDED.last_name,
        department_id = EXCLUDED.department_id, position_id = EXCLUDED.position_id;

-- Оновлення даних у таблиці Табель
INSERT INTO timesheet (id, date, hours, employee_id)
VALUES (1, '2023-01-01', 8, 1),
       (2, '2023-01-02', 8, 1)
ON CONFLICT (id) DO UPDATE
    SET date = EXCLUDED.date, hours = EXCLUDED.hours, employee_id = EXCLUDED.employee_id;

-- Оновлення даних у таблиці Заробітна Плата
INSERT INTO payroll (id, amount, payment_date, employee_id)
VALUES (1, 8000.00, '2023-01-15', 1),
       (2, 8500.00, '2023-01-15', 2)
ON CONFLICT (id) DO UPDATE
    SET amount = EXCLUDED.amount, payment_date = EXCLUDED.payment_date, employee_id = EXCLUDED.employee_id;

-- Оновлення даних у таблиці Податкові Відрахування
INSERT INTO tax_deduction (id, deduction_type, rate, payroll_id)
VALUES (1, 'Податок на доходи', 18.00, 1),
       (2, 'Військовий збір', 1.50, 1)
ON CONFLICT (id) DO UPDATE
    SET deduction_type = EXCLUDED.deduction_type, rate = EXCLUDED.rate, payroll_id = EXCLUDED.payroll_id;

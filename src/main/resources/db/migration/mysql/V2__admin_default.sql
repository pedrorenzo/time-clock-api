INSERT INTO `company` (`id`, `cnpj`, `update_date`, `create_date`, `company_name`) 
VALUES (NULL, '82198127000121', CURRENT_DATE(), CURRENT_DATE(), 'Time Clock IT');

INSERT INTO `employee` (`id`, `cpf`, `update_date`, `create_date`, `email`, `name`, 
`profile`, `hours_lunch_per_day`, `hours_worked_per_day`, `password`, `value_per_hour`, `company_id`) 
VALUES (NULL, '16248890935', CURRENT_DATE(), CURRENT_DATE(), 'admin@timeclock.com', 'ADMIN', 'ROLE_ADMIN', NULL, NULL, 
'$2a$06$xIvBeNRfS65L1N17I7JzgefzxEuLAL0Xk0wFAgIkoNqu9WD6rmp4m', NULL, 
(SELECT `id` FROM `company` WHERE `cnpj` = '82198127000121'));

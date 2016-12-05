/*5. Find the minimum, maximum, and average faculty salary 
for each department. Include the department name in the result.*/

SELECT department_name, MIN(salary) AS minimum_salary, MAX(salary) AS maximum_salary, AVG(salary) AS average_salary
FROM University.Instructor
GROUP BY department_name
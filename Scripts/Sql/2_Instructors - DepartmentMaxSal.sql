/*2. Find the ID, name, department and salary 
for those instructors 
whose salary is within 10% of the largest salary in their department.*/

SELECT i.ID, i.name, i.department_name AS department, i.salary
FROM University.Instructor i
JOIN (
	SELECT i2.department_name AS dept, (MAX(i2.salary) - MAX(i2.salary) * 0.1) AS min_sal
    FROM University.Instructor i2
    GROUP BY i2.department_name
    ) AS i2 
		ON (i.department_name = i2.dept)
WHERE i.salary >= i2.min_sal

/*7. Find the names and budgets 
for those departments 
having an average salary greater than $145k.*/

SELECT d.name, d.budget
FROM University.Department d
JOIN (
	SELECT i.department_name
	FROM University.Instructor i
	GROUP BY i.department_name
	HAVING AVG(i.salary) > 145000
	) AS i 
	ON (d.name = i.department_name)

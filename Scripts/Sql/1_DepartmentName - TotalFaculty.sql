/*1.	Find the department name and total number of faculty 
for each department.*/

SELECT d.name AS department_name, COUNT(i.name) AS total_faculty
FROM University.Department d
JOIN University.Instructor i
	ON (d.name = i.department_name)
GROUP BY d.name
ORDER BY COUNT(i.name) DESC
/*3. Find a count 
of the number of instructors 
whose department is in the same building as the math department.*/

SELECT COUNT(*) AS number_of_instructors
FROM University.Instructor i
WHERE i.department_name IN (
	SELECT d.name
	FROM University.Department d
	WHERE d.building = (
		SELECT d2.building
		FROM University.Department d2
		WHERE d2.name = 'MTH')
	)

    

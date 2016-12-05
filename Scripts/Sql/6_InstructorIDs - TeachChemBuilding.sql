/*6. Find the IDs 
for those faculty members 
who teach a class in the building where the chemistry department is located.*/

SELECT DISTINCT t.instructor_ID
FROM University.Teaches t
JOIN University.Section s
	ON (t.course_ID = s.course_ID AND t.section_number = s.number)
WHERE s.building = (
	SELECT d.building
	FROM University.Department d
	WHERE d.name = 'CHM')
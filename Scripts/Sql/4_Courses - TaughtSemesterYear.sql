/*4. Find the ID numbers 
for those courses 
that were taught in the Fall 2009 semester and in the Spring 2010 semester.*/

SELECT s.course_ID
FROM University.Section s
WHERE
	(s.semester = 'Fall' AND s.year = 2009)
		OR
	(s.semester = 'Spring' AND s.year = 2010)
GROUP BY s.course_ID
HAVING COUNT(DISTINCT s.course_ID, s.semester, s.year) = 2
select instructor_ID as Faculty_ID
from university.teaches x, university.section y
where (x.year = '2016' and x.year = y.year)
and (x.semester = 'Fall' and x.semester = y.semester)
and x.course_ID = y.course_ID
and y.building = some(select building
					  from university.department
                      where name = 'CHM');
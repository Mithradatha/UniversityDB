Select course_ID
from university.section
where (semester = 'Fall' and year = '2009')
union
select course_ID
from university.section
where (semester = 'Spring' and year = '2010');
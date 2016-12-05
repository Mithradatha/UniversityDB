select department_name, count(name) as Faculty
from university.instructor
group by department_name;

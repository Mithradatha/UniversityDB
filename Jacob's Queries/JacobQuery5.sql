select department_name as Department, max(salary) as Maximum,  min(salary) as Minimum,  avg(salary) as Average
from university.instructor
group by department_name;
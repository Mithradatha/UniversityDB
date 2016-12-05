select name as department, budget
from university.department x
where 100000 < some(select avg(salary)
						from university.instructor
                        where x.name = department_name);
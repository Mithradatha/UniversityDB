select h.ID, h.name, h.department_name, h.salary
from university.instructor h
where h.salary >= some
	(select (max(salary) - (max(salary) * 0.1))
     from university.instructor x
	 where h.department_name = x.department_name);
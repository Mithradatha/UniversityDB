select count(h.ID) as Count
from university.instructor h, university.department x
where h.department_name = x.name
and x.building = some(select building
					  from university.department
                      where name = 'MTH');

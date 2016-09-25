class Instructor:
    count = 0

    def __init__(self, instructor_id, name, department_name, salary):
        self.ID = instructor_id
        self.name = name
        self.department_name = department_name
        self.salary = salary
        Instructor.count += 1

    def headers(self):
        return 'ID,name,department_name,salary\n'
    
    def printLine(self):
        return '{0},{1},{2},{3}\n'.format(self.ID, self.name, self.department_name, self.salary)
class Student:
    count = 0

    def __init__(self, student_id, name, department_name, credits):
        self.ID = student_id
        self.name = name
        self.department_name = department_name
        self.credits = credits
        Student.count += 1

    def headers(self):
        return 'ID,name,department_name,credits\n'
    
    def printLine(self):
        return '{0},{1},{2},{3}\n'.format(self.ID, self.name, self.department_name, self.credits)
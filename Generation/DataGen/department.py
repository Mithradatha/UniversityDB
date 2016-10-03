class Department:
    count = 0

    def __init__(self, name, building, budget):
        self.name = name
        self.building = building
        self.budget = budget
        Department.count += 1

    def headers(self):
        return 'name,building,budget\n'
    
    def printLine(self):
        return '{0},{1},{2}\n'.format(self.name, self.building, self.budget)


department_names = ['CSE','MTH','CHM','BIO','HUM','ECE','SWE','PSY']
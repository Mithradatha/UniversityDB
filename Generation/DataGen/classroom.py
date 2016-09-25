class Classroom:
    count = 0

    def __init__(self, building, number, capacity):
        self.building = building
        self.number = number
        self.capacity = capacity
        Classroom.count += 1

    def headers(self):
        return 'building,number,capacity\n'
    
    def printLine(self):
        return '{0},{1},{2}\n'.format(self.building, self.number, self.capacity)
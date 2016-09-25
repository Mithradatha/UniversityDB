class Section:
    count = 0

    def __init__(self, course_id, section_number, department_name, semester, year, building, classroom_number, time_slot_id):
        self.course_ID = course_id
        self.number = section_number
        self.department_name = department_name
        self.semester = semester
        self.year = year
        self.building = building
        self.classroom_number = classroom_number
        self.time_slot_ID = time_slot_id
        Section.count += 1

    def headers(self):
        return 'course_ID,number,semester,year,building,classroom_number,time_slot_ID\n'
    
    def printLine(self):
        return '{0},{1},{2},{3},{4},{5},{6}\n'.format(self.course_ID, self.number, self.semester,
        self.year, self.building, self.classroom_number, self.time_slot_ID)
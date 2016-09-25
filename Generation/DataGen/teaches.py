class Teaches:
    count = 0

    def __init__(self, instructor_id, course_id, section_number, semester, year):
        self.instructor_ID = instructor_id
        self.course_ID = course_id
        self.section_number = section_number
        self.semester = semester
        self.year = year
        Teaches.count += 1

    def headers(self):
        return 'instructor_ID,course_ID,section_number,semester,year\n'
    
    def printLine(self):
        return '{0},{1},{2},{3},{4}\n'.format(self.instructor_ID, self.course_ID, self.section_number, self.semester, self.year)
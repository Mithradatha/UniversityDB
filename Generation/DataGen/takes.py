class Takes:
    count = 0

    def __init__(self, student_id, course_id, section_number, semester, year, grade):
        self.student_ID = student_id
        self.course_ID = course_id
        self.section_number = section_number
        self.semester = semester
        self.year = year
        self.grade = grade
        Takes.count += 1

    def headers(self):
        return 'student_ID,course_ID,section_number,semester,year,grade\n'
    
    def printLine(self):
        return '{0},{1},{2},{3},{4},{5}\n'.format(self.student_ID, self.course_ID, self.section_number, 
        self.semester, self.year, self.grade)
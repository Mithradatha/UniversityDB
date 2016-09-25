class Advisor:
    count = 0

    def __init__(self, student_id, instructor_id):
        self.student_ID = student_id
        self.instructor_ID = instructor_id
        Advisor.count += 1

    def headers(self):
        return 'student_ID,instructor_ID\n'
    
    def printLine(self):
        return '{0},{1}\n'.format(self.student_ID, self.instructor_ID)
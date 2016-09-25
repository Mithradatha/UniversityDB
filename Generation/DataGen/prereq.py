class Prereq:
    count = 0

    def __init__(self, course_id, prereq_id):
        self.course_ID = course_id
        self.prereq_ID = prereq_id
        Prereq.count += 1

    def headers(self):
        return 'course_ID,prereq_ID\n'
    
    def printLine(self):
        return '{0},{1}\n'.format(self.course_ID, self.prereq_ID)
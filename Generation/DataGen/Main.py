from time_slot import Time_Slot, time_slots
from building import Building
from classroom import Classroom
from department import Department, department_names
from instructor import Instructor
from student import Student
from advisor import Advisor
from course import Course, course_catalog
from prereq import Prereq
from section import Section
from teaches import Teaches
from takes import Takes
from random import Random
from faker import Factory
from datetime import datetime
from copy import deepcopy


start = datetime.now()

# Throttles
SF = 500
base_students_per_instructor = 25
base_instructors = 100
base_courses_per_instructor = 2.5
base_prereqs_per_course = 0.9
base_advisors_per_student = 0.9
base_instructors_per_section = 1.1

# Fake Data Generator 
fake = Factory.create('en_US')

# Random Number Generator 
rng = Random()
rng.seed()

# Generate CSV Files

def printToFile(fileName, headers, records):
    dataFile = open(".\\Data\\"+fileName, "w")
    dataFile.write(headers)
    for record in records:
        dataFile.write(record.printLine())
    dataFile.close()

def printToTerminal(title, count):
    print "{0}\n{1}".format(title, count)

# Generate Time_Slots
printToTerminal("Time_Slots",Time_Slot.count)
#printToFile('Time_Slot.csv', time_slots[0].headers(), time_slots)

# Generate Buildings
buildings = []
for i in xrange(1,21):
    buildings.append(Building(rng.randint(1,30)))

# Generate Classrooms
classrooms = []
for building in buildings:
    for room in xrange(1,building.rooms+1):
        classrooms.append(Classroom(building.number, room, rng.randint(15,200)))
printToTerminal("Classrooms",Classroom.count)
#printToFile('Classroom.csv', classrooms[0].headers(), classrooms)

# Generate Departments
departments = []
building_stack = range(1,21)
rng.shuffle(building_stack)
for name in department_names:
    departments.append(Department(name, building_stack.pop(), rng.randint(500000, 2000000)))
printToTerminal("Departments",Department.count)
#printToFile('Department.csv', departments[0].headers(), departments)

# Generate Instructors
instructors = []
for instructor_id in xrange(1, SF*base_instructors+1):
    instructors.append(Instructor(instructor_id, fake.name(), rng.choice(department_names), rng.randint(30000,150000)))
printToTerminal("Instructors",Instructor.count)
##printToFile('Instructor.csv', instructors[0].headers(), instructors)

# Generate Students
students = []
for student_id in xrange(1, int(len(instructors)*base_students_per_instructor+1)):
    students.append(Student(student_id, fake.name(), rng.choice(department_names), rng.randint(0,130)))
printToTerminal("Students",Student.count)
##printToFile('Student.csv', students[0].headers(), students)

# Generate Advisors
advisors = []
students_dup = deepcopy(students)
rng.shuffle(students_dup)
for i in xrange(1, int(len(students_dup)*base_advisors_per_student+1)):
    student = students_dup.pop()
    advisor_id = rng.choice([instructor.ID for instructor in instructors if instructor.department_name == student.department_name])
    advisors.append(Advisor(student.ID, advisor_id))
printToTerminal("Advisors",Advisor.count)
##printToFile('Advisor.csv', advisors[0].headers(), advisors)

# Generate Courses
courses = []
for course_id in xrange(1, int(len(instructors)*base_courses_per_instructor+1)):
    department_name = rng.choice(course_catalog.keys())
    title = rng.choice(course_catalog[department_name])
    if title[-1:].isdigit():
        course_catalog[department_name][course_catalog[department_name].index(title)] = title[0:-1] + str(int(title[-1:]) + 1)
    else:
        course_catalog[department_name][course_catalog[department_name].index(title)] = title + ' 2'
    credits = rng.randint(1,4)
    courses.append(Course(course_id, title, department_name, credits))
printToTerminal("Courses",Course.count)
#printToFile('Course.csv', courses[0].headers(), courses)

# Generate Prereqs
prereqs = []
rng.shuffle(courses)
for i in xrange(1, int(len(courses)*base_prereqs_per_course+1)):
    course_id = rng.choice(courses).ID
    prereq_id = rng.choice(courses).ID
    while prereq_id == course_id:
        prereq_id = rng.choice(courses).ID
    prereqs.append(Prereq(course_id, prereq_id))
printToTerminal("Prereqs",Prereq.count)
#printToFile('Prereq.csv', prereqs[0].headers(), prereqs)

# Generate Sections
sections = []
semesters = ['Spring','Fall']
for course in courses:
    nSections = rng.randint(1,10)
    for section_number in xrange(1,nSections+1):
        semester = rng.choice(semesters)
        year = rng.randint(2000, 2016)
        classroom = rng.choice(classrooms)
        time_slot = rng.choice(time_slots)
        sections.append(Section(course.ID, section_number, course.department_name, semester, year, classroom.building, classroom.number, time_slot.ID))
printToTerminal("Sections",Section.count)
#printToFile('Section.csv', sections[0].headers(), sections)


# Generate Teaches
teaches = []
for section in sections:
    instructor_id = rng.choice([instructor.ID for instructor in instructors if instructor.department_name == section.department_name])
    teaches.append(Teaches(instructor_id, section.course_ID, section.number, section.semester, section.year))
if base_instructors_per_section > 1.0:
    to = int(len(sections)*(base_instructors_per_section - 1.0))
    for i in xrange(1, to+1):
        section = rng.choice(sections)
        instructor_id = rng.choice([instructor.ID for instructor in instructors if instructor.department_name == section.department_name])
        teaches.append(Teaches(instructor_id, section.course_ID, section.number, section.semester, section.year))
printToTerminal("Teaches",Teaches.count)
#printToFile('Teaches.csv', teaches[0].headers(), teaches)

# Generate Takes
takes = []
grades = ['A','B','C','D','F']
sections_dup = deepcopy(sections)
rng.shuffle(sections_dup)
for student in students:
    nCourses = rng.randint(1,7)
    for i in xrange(1, nCourses+1):
        if len(sections_dup) > 0:
            section = sections_dup.pop()
        else:
            section = rng.choice(sections)
        grade = rng.choice(grades)
        takes.append(Takes(student.ID, section.course_ID, section.number, section.semester, section.year, grade))
printToTerminal("Takes",Takes.count)
#printToFile('Takes.csv', takes[0].headers(), takes)

end = datetime.now()
print 'Start Time: ',str(start.time())
print 'End Time: ',str(end.time())
print 'Duration: {x}'.format(x=end-start)

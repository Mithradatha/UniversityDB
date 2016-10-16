from random import randint, shuffle, choice
from datetime import datetime
from collections import namedtuple
from sys import argv
from misc import time_slots, course_catalog, fake_names

if __name__ == "__main__":

	initStart = datetime.now()
	start = initStart

	# Throttle
	SF = 10050
	try:
		SF = int(argv[1])
	except IndexError:
		print 'SF should be provided as a command line argument.'

	# Multipliers
	base_instructors = 100
	base_instructors_per_section = 1.1
	base_students_per_instructor = 25
	base_advisors_per_student = 0.9
	base_courses_per_instructor = 2.5
	base_prereqs_per_course = 0.9

	# Devations
	deviation_students = 30
	deviation_courses = 20
	deviation_advisors = int(base_advisors_per_student * 100)
	deviation_prereqs = int(base_prereqs_per_course * 100)

	# Ranges
	min_rooms_per_buiding = 1
	max_rooms_per_building = 30

	min_courses_per_student = 1
	max_courses_per_student = 7

	min_room_capacity = 15
	max_room_capacity = 200

	min_department_budget = 500000 
	max_department_budget = 2000000

	min_student_credits = 0
	max_student_credits = 130

	min_instructor_salary = 30000
	max_instructor_salary = 150000

	min_course_credits = 1
	max_course_credits = 4

	min_sections_per_course = 1
	max_sections_per_course = 10

	min_section_year = 2000
	max_section_year = 2016

	# Custom 
	department_names = ['CSE','MTH','CHM','BIO','HUM','ECE','SWE','PSY']

	semesters = ('Spring','Fall')

	grades = ['A','B','C','D','F']

	# Totals
	number_of_buildings = 20
	number_of_departments = len(department_names)
	number_of_instructors = int(SF * base_instructors)
	number_of_students = int(number_of_instructors * base_students_per_instructor)
	number_of_advisors = int(number_of_students * base_advisors_per_student)
	number_of_courses = int(number_of_instructors * base_courses_per_instructor)


	def printDuration(component):
		print
		print "Component: ",component
		print "Start: ",start.time()
		print "End: ",end.time()
		print "Duration: ",end-start

	def partition(m, n, deviation):
		mean = int(m / n)
		epsilon = int((m * (float(deviation) / 100)) / n)
		min = mean - epsilon
		max = mean + epsilon
		ls = []
		while n > 1:
			size = randint(min, max)
			ls.append(size)
			n -= 1
		ls.append(m - sum(ls))
		return ls

	end = datetime.now()
	printDuration('Initialization')

	# Master Storage
	master = dict((department_name, {}) for department_name in department_names)

	# Time Slots
	start = datetime.now()

	timeslotFile = open('.\\Data\\' + 'Time_Slot.csv', "w")
	timeslotFile.write('ID,days,start_time,end_time\n')

	for time_slot_id in time_slots:
		time_slot = time_slots[time_slot_id]
		timeslotFile.write('{0},{1},{2},{3}\n'.format(time_slot_id, time_slot.days, time_slot.start_time, time_slot.end_time))

	timeslotFile.close()
	end = datetime.now()
	printDuration('Time Slots')

	# Classrooms
	start = datetime.now()

	classrooms = {}
	classroomFile = open('.\\Data\\' + 'Classroom.csv', "w")
	classroomFile.write('building,number,capacity\n')

	# classrooms { key: building_id, value: [room_numbers...] }
	for building_id in xrange(1, number_of_buildings + 1):
		classrooms[building_id] = range(1, randint(min_rooms_per_buiding, max_rooms_per_building) + 1)
		for room_number in classrooms[building_id]:
			classroomFile.write('{0},{1},{2}\n'.format(building_id, room_number, randint(min_room_capacity, max_room_capacity)))
		
	classroomFile.close()
	end = datetime.now()
	printDuration('Classrooms')

	# Departments
	start = datetime.now()

	shuffle(department_names)

	buildings = classrooms.keys()
	shuffle(buildings)

	departmentFile = open('.\\Data\\' + 'Department.csv', "w")
	departmentFile.write('name,building,budget\n')

	for department_name in department_names:
		departmentFile.write('{0},{1},{2}\n'.format(department_name, buildings.pop(), randint(min_department_budget, max_department_budget)))
		master[department_name]['Students'] = []
		master[department_name]['Instructors'] = []

	departmentFile.close()
	end = datetime.now()
	printDuration('Departments')

	# Students
	start = datetime.now()

	studentFile = open('.\\Data\\' + 'Student.csv', "w")
	studentFile.write('ID,name,department_name,credits\n')

	for student_id in xrange(1, number_of_students + 1):
		department = choice(department_names)
		studentFile.write('{0},{1},{2},{3}\n'.format(student_id, choice(fake_names), department, randint(min_student_credits, max_student_credits)))
		master[department]['Students'].append(student_id)

	studentFile.close()
	end = datetime.now()
	printDuration('Students')

	# Instructors
	start = datetime.now()

	instructorFile = open('.\\Data\\' + 'Instructor.csv', "w")
	instructorFile.write('ID,name,department_name,salary\n')

	for instructor_id in xrange(1, number_of_instructors + 1):
		department = choice(department_names)
		instructorFile.write('{0},{1},{2},{3}\n'.format(instructor_id, choice(fake_names), department, randint(min_instructor_salary, max_instructor_salary)))
		master[department]['Instructors'].append(instructor_id)

	instructorFile.close()
	end = datetime.now()
	printDuration('Instructors')

	# Advisors
	start = datetime.now()

	advisorFile = open('.\\Data\\' + 'Advisor.csv', "w")
	advisorFile.write('student_ID,instructor_ID\n')

	for department in master:
		instructors = master[department]['Instructors']
		for student_id in master[department]['Students']:
			if randint(0, 100) <= deviation_advisors:
				advisorFile.write('{0},{1}\n'.format(student_id, choice(instructors)))

	advisorFile.close()    
	end = datetime.now()
	printDuration('Advisors')

	# Courses
	start = datetime.now()

	courses = {}
	buildings = classrooms.keys()
	Section = namedtuple('Section', ['semester', 'year'])

	courseFile = open('.\\Data\\' + 'Course.csv', "w")
	courseFile.write('ID,name,department_name,credits\n')

	prereqFile = open('.\\Data\\' + 'Prereq.csv', "w")
	prereqFile.write('course_ID,prereq_ID\n')

	sectionFile = open('.\\Data\\' + 'Section.csv', "w")
	sectionFile.write('course_ID,number,semester,year,building,classroom_number,time_slot_ID\n')

	teachesFile = open('.\\Data\\' + 'Teaches.csv', "w")
	teachesFile.write('instructor_ID,course_ID,section_number,semester,year\n')

	for course_id in xrange(1, number_of_courses + 1):
		courses[course_id] = {}
		department = choice(department_names)
		instructors = master[department]['Instructors']
		index = randint(0, len(course_catalog[department]) - 1)
		title = course_catalog[department][index]
		digit = [s for s in title.split() if s.isdigit()]
		if len(digit) > 0:
			course_catalog[department][index] = title[0:-(len(digit[0]))] + str(int(digit[0]) + 1)
		else:
			course_catalog[department][index] = title + ' 2'
		courseFile.write('{0},{1},{2},{3}\n'.format(course_id, title, department, randint(min_course_credits, max_course_credits)))
		# Prereqs
		if randint(0, 100) <= deviation_prereqs:
			prereq_id = course_id
			while prereq_id == course_id:
				prereq_id = randint(1, number_of_courses)
			prereqFile.write('{0},{1}\n'.format(course_id, prereq_id))
		# Sections
		for section_number in xrange(1, randint(min_sections_per_course, max_sections_per_course) + 1):
			building = choice(buildings)
			semester = choice(semesters)
			year = randint(min_section_year, max_section_year)
			courses[course_id][section_number] = Section(semester=semester, year=year)
			sectionFile.write('{0},{1},{2},{3},{4},{5},{6}\n'.format(course_id, section_number, semester, year, building, choice(classrooms[building]), choice(time_slots.keys())))
			# Teaches
			teachesFile.write('{0},{1},{2},{3},{4}\n'.format(choice(instructors), course_id, section_number, semester, year))

	prereqFile.close()
	teachesFile.close()
	sectionFile.close()
	courseFile.close()
	end = datetime.now()
	printDuration('Courses')

	# Takes
	start = datetime.now()

	course_ids = courses.keys()

	takesFile = open('.\\Data\\' + 'Takes.csv', "w")
	takesFile.write('student_ID,course_ID,section_number,semester,year,grade\n')

	for department in master:
		for student_id in master[department]['Students']:
			for i in xrange(1, randint(min_courses_per_student, max_courses_per_student) + 1):
				course_id = choice(course_ids)
				section_number = choice(courses[course_id].keys())
				section = courses[course_id][section_number]
				takesFile.write('{0},{1},{2},{3},{4},{5}\n'.format(student_id, course_id, section_number, section.semester, section.year, choice(grades)))

	takesFile.close()
	end = datetime.now()
	printDuration('Takes')

	start = initStart
	end = datetime.now()
	printDuration('Total')
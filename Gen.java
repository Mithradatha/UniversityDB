import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;

public class Gen {
	private static int SF = -1;

	private static FileWriter writer;
	private static Random random = new Random();
	
	private final static String folderStructure = ".\\Generation\\DataGen\\Data\\";
	private final static String fileExtention = ".csv";

	//use the id of the course, minus one for these
	//TODO: instantiate!!
	private static String[] courseToSemester;
	private static String[] courseToYear;
	private static int[] courseToNumSections;

	private static final String[] Semesters = new String[]{"Spring", "Fall"};

	private static final String[] grades = new String[]{"A", "B", "C", "D", "F"};

	private static String[] ClassPrefixes = new String[]{"CSE", "CHM", "MTH", "BIO", "HUM", "ECE", "SWE", "PSY"};
	//index 0 is id of the last instructor that belongs to CSE, index 1 for CHM, etc...
	//in other words, all instructor with an id from 1->InstructorBounds[0] are CSE
	private static int[] InstructorBounds;
	//same thing, but for students
	private static int[] StudentBounds;
	//same thin, but for CourseBounds
	private static int[] CourseBounds;

	//times are represented as ints (unit: minutes)
	private static int[]    MWFtimes = new int[]{8 * 60 , 9 * 60, 10* 60, 11* 60, 12 * 60, 13 * 60, 14 * 60, 15 * 60, 16 * 60, 17 * 60};
	private static int      MWFduration = 50;
	private static int[]    TRtimes = new int[]{8 * 60, 9 * 60 + 30, 11 * 60, 12 * 60 + 30, 14 * 60, 15 * 60 + 30, 17 * 60};
	private static int      TRduration = 75;

	private static String[] alphabet = new String[]{"A", "B", "C", "D", "E", "F",
	                                                "G", "H", "I", "J", "K", "L",
																	"M", "N", "O", "P", "Q", "R",
																	"S", "T", "U", "V", "W", "X",
																	"Y", "Z"};

   //given index (building - 1), value is number of rooms in that building
	private static int[] RoomsInBuilding;

	public static void main (final String[] args) {
		//get scale factor from user
		while (SF <= 0 ) {
			System.out.println("Hey there fella give me a scale Factor > 0");
			Scanner in = new Scanner(System.in);
			SF = in.nextInt();
		}
		System.out.println("Great! I'll get right on that");

		///////////////  DEPARTMENT ///////////////////////////
		System.out.println("Generating Departments");

		try {
			//generate data
			ArrayList<Integer> buildings = new ArrayList<Integer>(ClassPrefixes.length);
			while (buildings.size() < ClassPrefixes.length) {
                int temp = randIntInRange(1,20);
				buildings.add(temp);
			}
			ArrayList<Integer> budgets = new ArrayList<Integer>(ClassPrefixes.length);
			for (int i = 0; i < ClassPrefixes.length; i++) {
				budgets.add(randIntInRange(500000, 2000000));
			}

			//write this info
			writer = generateFileWriter("Department");
			for (int i = 0; i < ClassPrefixes.length; i++) {
				writeCsvLine(writer, ClassPrefixes[i], Integer.toString(buildings.get(i)), Integer.toString(budgets.get(i)));
			}
			writer.close();
		}
		catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////// CLASSROOM /////////////////////////////
		System.out.println("Generating Classrooms");

		try {
			writer = generateFileWriter("Classroom");

			RoomsInBuilding = new int[20];
			for (int building = 1; building <= 20; building++) {
				int roomsInBuilding = randIntInRange(1, 30);
				RoomsInBuilding[building - 1] = roomsInBuilding;
				for (int classroom = roomsInBuilding; classroom > 0; classroom--) {
					int capacity = randIntInRange(15, 200);
					writeCsvLine(writer, Integer.toString(building), Integer.toString(classroom), Integer.toString(capacity));
				}
			}

			writer.close();
		}
		catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}


		//////////////// Time Slot ///////////////////
		System.out.println("Generating Time Slot");

        try {
            writer = generateFileWriter("Time_Slot");
            for (int id = 1; id <= 17; id++) {
                if (id <= 10) { // MWF
                    int startTime = MWFtimes[id-1];
                    int endTime = startTime + MWFduration;
                    writeCsvLine(writer, Integer.toString(id), "MWF", formatTime(startTime), formatTime(endTime));
                } else {    // TR
                    int startTime = TRtimes[id - 11];
                    int endTime = startTime + TRduration;
                    writeCsvLine(writer, Integer.toString(id), "TR", formatTime(startTime), formatTime(endTime));
                }
            }
            writer.close();

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

		///////////////// STUDENT ////////////////////////////
		System.out.println("Generating Students");
		int numStudents = SF * 2500;
		try {
			writer = generateFileWriter("Student");

			int studentsPerDept = numStudents / ClassPrefixes.length;
			//initialize StudentBounds
			StudentBounds = new int[ClassPrefixes.length];
			for (int i = 0; i < StudentBounds.length - 1; i++) {
				StudentBounds[i] = studentsPerDept * (i+1);
			}
			StudentBounds[StudentBounds.length - 1] = numStudents;

			//actually make students
			int currentDept = 0;
			for (int id = 1; id <= numStudents; id++) {
				if (id > StudentBounds[currentDept]) {
					currentDept++;
				}
				String name = randomName();
				String deptName = ClassPrefixes[currentDept];
				int credits = randIntInRange(0, 130);
				writeCsvLine(writer, Integer.toString(id), name, deptName, Integer.toString(credits));
			}

			writer.close();
		}
		catch( Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		////////////////// Instructor //////////////////////////////
		System.out.println("Generating Instructors");
		int numInstructors = SF * 100;
		try {
			writer = generateFileWriter("Instructor");


			InstructorBounds = new int[ClassPrefixes.length];
			//instantiate instructorBounds
			int instructorsPerDept = numInstructors / ClassPrefixes.length;
			for (int i = 0; i < InstructorBounds.length - 1; i++) {
				InstructorBounds[i] = instructorsPerDept * (i + 1);
			}
			InstructorBounds[InstructorBounds.length - 1] = numInstructors;

			//now actually create and write the instructors
			int currentDept = 0;
			for (int id = 1; id <= numInstructors; id++) {
				if (id > InstructorBounds[currentDept])
					currentDept++;
				int salary = randIntInRange(30000, 150000);
				writeCsvLine(writer, Integer.toString(id), randomName(), ClassPrefixes[currentDept], Integer.toString(salary));
			}

			writer.close();
		}
		catch( Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}


		////////////////// ADVISOR ////////////////////////////
		System.out.println("Generating Advisors");
		try {
			writer = generateFileWriter("Advisor");

			int currentDept = 0;
			for (int s_id = 1; s_id <= numStudents; s_id++) {
				if (s_id % 10 == 0) {
					continue; //this is the one in ten without an advisor
				}
				if (s_id > StudentBounds[currentDept]) {
					currentDept++;
				}
				int i_id = ( currentDept == 0 ) ? randIntInRange(1, InstructorBounds[0]) : randIntInRange(InstructorBounds[currentDept-1]+1, InstructorBounds[currentDept]);

				writeCsvLine(writer, Integer.toString(s_id), Integer.toString(i_id));
			}

			writer.close();
		}
		catch( Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		/////////////////// COURSE //////////////////////////
		System.out.println("Generating Courses");
		int numCourses = (int) (numInstructors * 2.5);
		try {
			writer = generateFileWriter("Course");

			//instantiate CourseBounds
			int coursesPerDept = numCourses / ClassPrefixes.length;
			CourseBounds = new int[ClassPrefixes.length];
			for (int i = 0; i < CourseBounds.length - 1; i++) {
				CourseBounds[i] = (i+1) * coursesPerDept;
			}
			CourseBounds[CourseBounds.length - 1] = numCourses;

			//generate courses
			int currentDept = 0;
			for (int c_id = 1; c_id <= numCourses; c_id++) {
				if (c_id > CourseBounds[currentDept]) {
					currentDept++;
				}
				String title = randomName();
				String dept = ClassPrefixes[currentDept];
				int credits = randIntInRange(1,4);

				writeCsvLine(writer, Integer.toString(c_id), title, dept, Integer.toString(credits));
			}

			writer.close();
		}
		catch( Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////////// PREREQ /////////////////////
		System.out.println("Generating Prereqs");
		try {
			writer = generateFileWriter("Prereq");

			for (int c_id = 1; c_id < numCourses; c_id++) {
				if (c_id % 10 == 0) continue; //one in ten courses doesn't have a prereq
				int p_id = c_id;
				while (c_id == p_id) p_id = randIntInRange(1, numCourses); //not guaranteed to terminate, but I'm not
							                                                  // concerned with correctness
		      writeCsvLine(writer, Integer.toString(c_id), Integer.toString(p_id));
			}

			writer.close();
		}
		catch( Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////////// SECTION/////////////////////
		System.out.println("Generating Sections");
		courseToYear = new String[numCourses];
		courseToSemester = new String[numCourses];
		courseToNumSections = new int[numCourses];
		try {
			writer = generateFileWriter("Section");

			for (int c_id = 1; c_id < numCourses; c_id++) {
				//to simplify things, all sections of a given course will happen during the same semester
				String semester = Semesters[randIntInRange(0, Semesters.length - 1)];
				String year = Integer.toString(randIntInRange(2000, 2015));
				int tempNumSections = randIntInRange(1,10);
				//file this info away for future reference
				courseToYear[c_id - 1] = year;
				courseToSemester[c_id - 1] = semester;
				courseToNumSections[c_id - 1] = tempNumSections;

				for (int section = tempNumSections; section >= 1; section --) { //doesn't ensure mean is 3 sections per class
					int building = randIntInRange(1,20);
					String room = String.format("%02d", randIntInRange(1, RoomsInBuilding[building - 1]));
					String timeSlot = Integer.toString(randIntInRange(1, 17));

					writeCsvLine(writer, Integer.toString(c_id), String.format("%02d",section), semester, year, Integer.toString(building), room, timeSlot);
				}
			}

			writer.close();
		}
		catch( Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////////// TEACHES /////////////////////
		System.out.println("Generating Teaches");
		try {
			writer = generateFileWriter("Teaches");

			//TODO;
			//for each course number
				//for each section of courses
					//get  the semester, year, and 1.1 teachers from that department
					//instructors are easy, but the semester and year have to be consistent
			int currentDept = 0;
			for (int c_id = 1; c_id <= numCourses; c_id++) {
				if (c_id > CourseBounds[currentDept]) currentDept++;
				int numSections = courseToNumSections[c_id - 1];
				String year = courseToYear[c_id - 1];
				String semester = courseToSemester[c_id - 1];
				for (int section = 1; section <= numSections; section++) {
					int randomInstructorInDept = randIntInRange( (currentDept == 0) ? 0 : InstructorBounds[currentDept - 1], InstructorBounds[currentDept]);
					writeCsvLine(writer, Integer.toString(randomInstructorInDept), Integer.toString(c_id), Integer.toString(section), semester, year);
				}
			}

			writer.close();
		}
		catch( Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////////// TAKES /////////////////////
		System.out.println("Generating Takes");
		try {
			int[] coursesTakenByStudent = new int[numStudents];

			writer = generateFileWriter("Takes");

			//first, put a student in each section of each class:
			int currentStudentCounter = 1;
			for (int course = 1; course < numCourses; course++) {
				int sectionsInThisCourse = courseToNumSections[course - 1];
				String semester = courseToSemester[course - 1];
				String year = courseToYear[course - 1];
				for (int section = 1; section <= sectionsInThisCourse; section++) {
					//add obligatory first student to section
					writeCsvLine(writer, Integer.toString(currentStudentCounter), Integer.toString(course), Integer.toString(section), semester, year, randomGrade());
					coursesTakenByStudent[currentStudentCounter - 1]++;
					currentStudentCounter++;
					if (coursesTakenByStudent[currentStudentCounter - 1] >= 7) currentStudentCounter++;

					//determine how many more students to add. number between 0 and 12.
					int numToAdd = randIntInRange(0,12);
					ArrayList<Integer> studentsInThisClass = new ArrayList<Integer>();
					studentsInThisClass.add(currentStudentCounter);
					while(numToAdd > 0) {
						int randomStudent = randIntInRange(1, numStudents);
						while (! (coursesTakenByStudent[randomStudent-1] < 7 && !studentsInThisClass.contains(randomStudent)) ) {
							randomStudent = randIntInRange(1, numStudents);
						}
						writeCsvLine(writer, Integer.toString(randomStudent), Integer.toString(course), Integer.toString(section), semester, year, randomGrade());
						coursesTakenByStudent[randomStudent-1]++;

						numToAdd--;
					}


				}
			}

			writer.close();
		}
		catch( Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}


	private static FileWriter generateFileWriter(final String fileName) {
		return new FileWriter(folderStructure + fileName + fileExtention);
	}
	
	private static int randIntInRange(int lo, int hi) {
		assert hi >= lo;
		return random.nextInt(hi + 1 - lo) + lo;
	}
	
	private static void writeCsvLine(FileWriter w, String... values) throws Exception{
		try {
			for (int i = 0; i < values.length; i++) {
				w.write(values[i]);
				if (i != values.length - 1) {
					w.write(",");
				}
			}
			w.write("\n");
		}
		catch (Exception e) {
			throw e;
		}
	}
	//imperfect, but works for our application
	private static String formatTime(int minutes) {
		int m = minutes % 60;
		minutes = minutes - m; //now it's an even multiple of 60
		int h = minutes / 60;
        int s = 0;
		if (h > 24) {
			h -= 24;
		}
		return String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
	}

	private static String randomName() {
		int numChars = randIntInRange(1, 32);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numChars; i++) {
			sb.append(alphabet[randIntInRange(0, alphabet.length - 1)]);
		}
		return sb.toString();
	}

	private static String randomGrade() {
		return grades[randIntInRange(0, grades.length - 1)];
	}
}

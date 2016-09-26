import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;

public class Gen {
	private static int SF = -1;

	private static FileWriter writer;
	private static Random random = new Random();

	private final static String DepartmentFileName = "DEPARTMENT.txt";
	private final static String ClassroomFileName = "CLASSROOM.txt";
	private final static String TimeslotFileName = "TIMESLOT.txt";
	private final static String TimeslotdetailFileName = "TIMESLOTDETAIL.txt";
	private final static String StudentFileName = "STUDENT.txt";
	private final static String AdvisorFileName = "ADVISOR.txt";
	private final static String CourseFileName = "COURSE.txt";
	private final static String PrereqFileName = "PREREQ.txt";
	private final static String SectionFileName = "SECTION.txt";
	private final static String TeachesFileName = "TEACHES.txt";
	private final static String TakesFileName = "TAKES.txt";

	private static final String Semesters = new String[]{"SPRING", "FALL"};

	private static String[] ClassPrefixes = new String[]{"CSE", "CHM", "MTH", "BIO", "HUM", "ECE", "SWE", "PSY"};
	//index 0 is id of the last instructor that belongs to CSE, index 1 for CHM, etc...
	//in other words, all instructor with an id from 1->InstructorBounds[0] are CSE
	private static int[] InstructorBounds;
	//same thing, but for students
	private static int[] StudentBounds;
	//same thin, but for CourseBounds
	private static int[] CourseBounds;

	//times are represented as ints (unit: minutes)
	private static int[]    MWFtimes = new int[]{8 * 60 , 9 * 60, 10* 60, 11* 60, 12* 60, 1* 60, 2* 60, 3* 60, 4* 60, 5* 60};
	private static int[]    MWFids   = new int[]{      1,      2,      3,      4,      5,     6,     7,     8,     9,    10};
	private static int      MWFduration = 60;
	private static String[] MWFstrings = new String[] {"M", "W", "F"};
	private static int[]    TRtimes = new int[]{8 * 60, 9 * 60 + 30, 11 * 60, 12 * 60 + 30, 2 * 60, 3 * 60 + 30, 5 * 60};
	private static int[]    TRids   = new int[]{    11,          12,      13, 1          4,     15,          16,     17};
	private static double   TRduration = 75;
	private static String[] TRstrings = new String[] {"T", "R"};

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
			while (buildings.length < ClassPrefixes.length) {
				int temp = randIntInRange(1,20);
				buildings.add(temp);
			}
			ArrayList<Integer> budgets = new ArrayList<Integer>(ClassPrefixes.length);
			for (int i = 0; i < ClassPrefixes.length; i++) {
				budgets.add(randIntInRange(500000, 2000000));
			}

			//write this info
			writer = new FileWriter(DepartmentFileName);
			for (int i = 0; i < ClassPrefixes.length; i++) {
				printCsvLine(writer, ClassPrefixes[i], Integer.toString(buildings.get(i)), Integer.toString(budgets.get(i)));
			}
			writer.close();
		}
		catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////// CLASSROOM /////////////////////////////
		System.out.println("Generating Classrooms");

		try {
			writer = new FileWriter(ClassroomFileName);

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

		////////////////// Time Slot ///////////////////////////
		System.out.println("Generating Time Slots!");
		try {
			writer = new FileWriter(TimeslotdetailFileName);
			for (int id = 1; id <= 17; id++) {
				writeCsvLine(writer, Integer.toString(id));
			}
		}

		//////////////// Time Slot Details ///////////////////
		System.out.println("Generating Time Slot Details");
		try {
			writer = new FileWriter(TimeslotdetailFileName);
			//mon, wed, fri time slots
			for (int i = 0; i < MWFtimes.length; i++) {
				int id = MWFids[i];
				int startTime = MWFtimes[i];
				int endTime = startTime + MWFduration;
				for (int j = 0; j < MWFstrings.length; i++) {
					String day = MWFstrings[j];
					writeCsvLine(Integer.toString(id), day, formatTime(startTime), formatTime(endTime));
				}
			}
			// tues, thurs time slots
			for (int i = 0; i < TRtimes.length; i++) {
				int id = TRids[i];
				int startTime = TRtimes[i];
				int endTime = startTime + TRduration;
				for (int j = 0; j < TRstrings.length; i++) {
					String day = TRstrings[j];
					writeCsvLine(Integer.toString(id), day, formatTime(startTime), formatTime(endTime));
				}
			}

			writer.close();
		}
		catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		///////////////// STUDENT ////////////////////////////
		System.out.println("Generating Students");
		int numStudents = SF * 2500;
		try {
			writer = new FileWriter(StudentFileName);

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
				string name = randomName();
				string deptName = ClassPrefixes[currentDept];
				int credits = randIntInRange(0, 130);
				printCsvLine(writer, Integer.toString(id), name, deptName, Integer.toString(credits));
			}

			writer.close();
		}
		catch( exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		////////////////// Instructor //////////////////////////////
		System.out.println("Generating Instructors");
		int numInstructors = SF * 100;
		try {
			writer = new FileWriter(InstructorFileName);


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
				printCsvLine(writer, Integer.toString(id), randomName(), ClassPrefixes[currentDept], Integer.toString(salary);
			}

			writer.close();
		}
		catch( exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}


		////////////////// ADVISOR ////////////////////////////
		System.out.println("Generating Advisors");
		try {
			writer = new FileWriter(AdvisorFileName);

			int currentDept = 0;
			for (int s_id = 1; s_id <= numStudents; s_id++) {
				if (s_id % 10 == 0) {
					continue; //this is the one in ten without an advisor
				}
				if (s_id > StudentBounds[currentDept]) {
					currentDept++;
				}
				i_id = ( currentDept == 0 ) ? randIntInRange(1, InstructorBounds[0]) : randIntInRange(InstructorBounds[currentDept-1]+1, InstructorBounds[currentDept]);

				printCsvLine(writer, Integer.toString(s_id), Integer.toString(i_id));
			}

			writer.close();
		}
		catch( exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		/////////////////// COURSE //////////////////////////
		System.out.println("Generating courses...");
		int numCourses = numInstructors * 2.5;
		try {
			writer = new FileWriter(CourseFileName);

			//instantiate CourseBounds
			int coursesPerDept = numCourses / ClassPrefixes.length;
			CourseBounds = new int[ClassPrefixes.length];
			for (int i = 0; i < CourseBounds.length - 1; i++) {
				CourseBounds[i] = (i+1) * coursesPerDept;
			}
			Coursebounds[CourseBounds.length - 1] = numCourses;

			//generate courses
			int currentDept = 0;
			for (int c_id = 1; c_id <= numCourses; numCourses++) {
				if (c_id > CourseBounds[currentDept]) {
					currentDept++;
				}
				String title = randomName();
				String dept = ClassPrefixes[currentDept];
				int credits = randIntInRange(1,4);

				printCsvLine(writer, Integer.toString(c_id), title, dept, Integer.toString(credits));
			}

			writer.close();
		}
		catch( exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////////// PREREQ /////////////////////
		System.out.println("Generating Prereqs");
		try {
			writer = new FileWriter(PrereqFileName);

			for (int c_id = 1, c_id < numCourses; c_id++) {
				if (c_id % 10 == 0) continue; //one in ten courses doesn't have a prereq
				int p_id = c_id;
				while (c_id == p_id) p_id = randIntInRange(1, numCourses); //not guaranteed to terminate, but I'm not
							                                                  // concerned with correctness
		      printCsvLine(writer, Integer.toString(c_id), Integer.toString(p_id));
			}

			writer.close();
		}
		catch( exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////////// SECTION/////////////////////
		System.out.println("Generating Sections");
		try {
			writer = new FileWriter(SectionFileName);

			for (int c_id = 1; c_id < numCourses; c_id++) {
				for (int section = randIntInRange(1,10); section >= 1; section --) { //doesn't ensure mean is 3 sections per class
					String semester = Semesters[randIntInRange(0, Semesters.length - 1)];
					String year = Integer.parseInt(randIntInRange(2000, 2015));
					int building = randIntInRange(1,20);
					String room = String.format("%02d", randIntInRange(1, RoomsInBuilding[building - 1]));
					String timeSlot = Integer.parseInt(randIntInRange(1, 17));

					printCsvLine(Integer.toString(c_id), String.format("%02d",section), semester, year, Integer.toString(building), room, timeSlot);
				}
			}

			writer.close();
		}
		catch( exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////////// TEACHES /////////////////////
		System.out.println("Generating Teaches");
		try {
			writer = new FileWriter(TeachesFileName);

			//TODO;

			writer.close();
		}
		catch( exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}

		//////////////////// TAKES /////////////////////
		System.out.println("Generating Takes");
		try {
			writer = new FileWriter(TakesFileName);

			//TODO

			writer.close();
		}
		catch( exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}



	private static randIntInRange(int lo, int hi) {
		assert hi >= lo;
		return random.nextInt(hi + 1 - lo) + lo;
	}
	private static writeCsvLine(FileWriter w, String... values) throws Exception{
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
		if (h > 12) {
			h -= 12;
		}
		return String.format("%02d", h) + ":" + String.format("%02d", m);
	}

	private static String randomName() {
		int numChars = randIntInRange(1, 32);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numChars; i++) {
			sb.append(alphabet[randIntInRange(0, alphabet.length - 1)])
		}
		return sb.toString();
	}
}

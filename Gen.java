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

	private static String[] ClassPrefixes = new String[]{"CSE", "CHM", "MTH", "BIO", "HUM", "ECE", "SWE", "PSY"};

	public static void main (final String[] args) {
		while (SF <= 0 ) {
			System.out.println("Hey there fella give me a scale Factor > 0");
			Scanner in = new Scanner(System.in);
			SF = in.nextInt();
		}
		System.out.println("Great! I'll get right on that");

		//DEPARTMENT
		System.out.println("Generating Departments");

		try {
			//generate data
			ArrayList<Integer> buildings = new ArrayList<Integer>(ClassPrefixes.length);
			while (buildings.length < ClassPrefixes.length) {
				int temp = randIntInRange(1,20);
				if (!buildings.contains(temp)) { //two departments can't have the same building?
					buildings.add(temp);
					counter++;
				}
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

		//CLASSROOM
		System.out.println("Generating Classrooms");
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
}

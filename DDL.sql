DROP DATABASE IF EXISTS University;
CREATE DATABASE University;

USE University;

CREATE TABLE Time_Slot (
	ID int AUTO_INCREMENT,
    days varchar(3),
    start_time time,
    end_time time,
    PRIMARY KEY (ID)
);

CREATE TABLE Classroom (
	building int,
    number int,
    capacity int,
    PRIMARY KEY (building, number)
);

CREATE TABLE Department (
	name char(3),
    building int UNIQUE,
    budget int,
    PRIMARY KEY (name)
);

CREATE TABLE Student (
	ID int AUTO_INCREMENT,
    name varchar(32),
    department_name char(3),
    credits int,
    PRIMARY KEY (ID),
    FOREIGN KEY (department_name) REFERENCES Department(name)
);

CREATE TABLE Instructor (
	ID int AUTO_INCREMENT,
    name varchar(32),
    department_name char(3),
    salary int,
    PRIMARY KEY (ID),
    FOREIGN KEY (department_name) REFERENCES Department(name)
);

CREATE TABLE Course (
	ID int AUTO_INCREMENT,
    name varchar(32) UNIQUE,
    department_name char(3),
    credits int,
    PRIMARY KEY (ID),
    FOREIGN KEY (department_name) REFERENCES Department(name)
);

CREATE TABLE Advisor (
	student_ID int,
    instructor_ID int,
    PRIMARY KEY (student_ID),
    FOREIGN KEY (student_ID) REFERENCES Student(ID),
    FOREIGN KEY (instructor_ID) REFERENCES Instructor(ID)
);

CREATE TABLE Prereq (
	course_ID int,
    prereq_ID int,
    PRIMARY KEY (course_ID, prereq_ID),
    FOREIGN KEY (course_ID) REFERENCES Course(ID),
    FOREIGN KEY (prereq_ID) REFERENCES Course(ID)
);

CREATE TABLE Section (
	course_ID int,
    number int,
    semester varchar(6),
    year int,
    building int,
    classroom_number int,
    time_slot_ID int,
    PRIMARY KEY (course_ID, number, semester, year),
    FOREIGN KEY (course_ID) REFERENCES Course(ID),
    FOREIGN KEY (building, classroom_number) REFERENCES Classroom(building, number),
    FOREIGN KEY (time_slot_ID) REFERENCES Time_Slot(ID)
);

CREATE TABLE Teaches (
	instructor_ID int,
    course_ID int,
    section_number int,
    semester varchar(6),
    year int,
    PRIMARY KEY (instructor_ID, course_ID, section_number, semester, year),
    FOREIGN KEY (instructor_ID) REFERENCES Instructor(ID),
    FOREIGN KEY (course_ID, section_number, semester, year) REFERENCES Section(course_ID, number, semester, year)
);

CREATE TABLE Takes (
	student_ID int,
    course_ID int,
    section_number int,
    semester varchar(6),
    year int,
    grade char(1),
    PRIMARY KEY (student_ID, course_ID, section_number, semester, year),
    FOREIGN KEY (student_ID) REFERENCES Student(ID),
    FOREIGN KEY (course_ID, section_number, semester, year) REFERENCES Section(course_ID, number, semester, year)
);
package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.Course;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO: generate, getAll, print, delete methods for: Question class

public class App
{
    private static Session session;


    static SessionFactory getSessionFactory() throws HibernateException{
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Pupil.class);
        configuration.addAnnotatedClass(Teacher.class);
        configuration.addAnnotatedClass(Principal.class);
        configuration.addAnnotatedClass(Question.class);
        configuration.addAnnotatedClass(Course.class);
        configuration.addAnnotatedClass(Subject.class);
        configuration.addAnnotatedClass(Answer.class);
        configuration.addAnnotatedClass(Grade.class);
        configuration.addAnnotatedClass(Exam.class);
        configuration.addAnnotatedClass(Exam_Question_points.class);
        configuration.addAnnotatedClass(ReadyExam.class);
        configuration.addAnnotatedClass(WordSubmission.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void generateStudentsWithGrades() throws Exception{
        Pupil p1 = new Pupil("Michael", "213468951", "Michael_password", false); // new pupil
        session.save(p1);
        Pupil p2 = new Pupil("noam","313468951", "1", false);
        session.save(p2);
        Pupil p3 = new Pupil("a","231468951" ,"2", false);
        session.save(p3);
        Pupil p4 = new Pupil("b","323468153" ,"3", false);
        session.save(p4);
        session.flush();

            /*
             * The call to session.flush() updates the DB immediately without ending the transaction.
             * Recommended to do after an arbitrary unit of work.
             * MANDATORY to do if you are saving a large amount of data - otherwise you may get
            cache errors.
             */
        session.flush();
    }

    public static void generateExam(Question q1,Question q2,Course speeds,Pupil p1,Teacher t1) throws Exception{

        Exam ex1 = new Exam("first exam", speeds, 90, "hello students!", "hello teacher!", t1);
        session.save(ex1);
        session.flush();
        ex1.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
            /*
            This part is important and tricky. normally you would think that adding a question to the exam would look like:
            ex1.addQuestion(q1, 70);
            (BTW: now adding a question also requires to give that question points- 70 points in this case)
            BUT YOU SHOULD NOT ADD QUESTIONS LIKE THIS!!!!!!!!!!
            the points of this question on this exam are loaded into a different entity called Exam_question_points.
            this entity is created automatically each time you add a question, but in order for it to be saved in the tables
            I made the addQuestion function return this entity- and then it has to be saved by the session.
            SO THE OVERALL COMMAND TO ADD A QUESTION TO AN EXAM IS:
            session.save(ex1.addQuestion(q1, 70));
             */
        session.save(ex1.addQuestion(q1, 70));
        session.save(ex1.addQuestion(q2, 30));
        session.save(ex1);
        session.flush();

        Exam ex2 = new Exam("second exam", speeds, 90, "hello students!", "hello teacher!", t1);
        session.save(ex2);
        session.flush();
        ex1.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
            /*
            This part is important and tricky. normally you would think that adding a question to the exam would look like:
            ex1.addQuestion(q1, 70);
            (BTW: now adding a question also requires to give that question points- 70 points in this case)
            BUT YOU SHOULD NOT ADD QUESTIONS LIKE THIS!!!!!!!!!!
            the points of this question on this exam are loaded into a different entity called Exam_question_points.
            this entity is created automatically each time you add a question, but in order for it to be saved in the tables
            I made the addQuestion function return this entity- and then it has to be saved by the session.
            SO THE OVERALL COMMAND TO ADD A QUESTION TO AN EXAM IS:
            session.save(ex1.addQuestion(q1, 70));
             */
        session.save(ex2.addQuestion(q1, 50));
        session.save(ex2.addQuestion(q2, 50));
        session.save(ex2);
        session.flush();

        Exam ex3 = new Exam("third exam", speeds, 90, "hello students!", "hello teacher!", t1);
        session.save(ex3);
        session.flush();
        ex1.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
            /*
            This part is important and tricky. normally you would think that adding a question to the exam would look like:
            ex1.addQuestion(q1, 70);
            (BTW: now adding a question also requires to give that question points- 70 points in this case)
            BUT YOU SHOULD NOT ADD QUESTIONS LIKE THIS!!!!!!!!!!
            the points of this question on this exam are loaded into a different entity called Exam_question_points.
            this entity is created automatically each time you add a question, but in order for it to be saved in the tables
            I made the addQuestion function return this entity- and then it has to be saved by the session.
            SO THE OVERALL COMMAND TO ADD A QUESTION TO AN EXAM IS:
            session.save(ex1.addQuestion(q1, 70));
             */
        session.save(ex3.addQuestion(q1, 70));
        session.save(ex3.addQuestion(q2, 30));
        session.save(ex3);
        session.flush();

        ReadyExam r1 = new ReadyExam(ex1, "10a4", true, "14/6/2023 13:30");  // new "Out of the drawer" exam
        session.save(r1);
        r1.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
        session.flush();
        List<Question> correct = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
        correct.add(q1);                                          // p1 answered only question q1 correctly
        Grade gr1 = new Grade(/*98, */r1, p1, correct, "Michael, nice exam!"); // grade- note that given the List "correct" it computes the grade itself
        session.save(gr1);
        session.flush();
            /*
             * The call to session.flush() updates the DB immediately without ending the transaction.
             * Recommended to do after an arbitrary unit of work.
             * MANDATORY to do if you are saving a large amount of data - otherwise you may get
            cache errors.
*/
    }

    public static void generateTeachers() throws Exception{
        Teacher t1 = new Teacher("Malki", "Malki_password", true);       // new teacher
        session.save(t1);
        Teacher t2 = new Teacher("v", "1", true);
        session.save(t2);
        Teacher t3 = new Teacher("a", "2", true);
        session.save(t3);
            /*
             * The call to session.flush() updates the DB immediately without ending the transaction.
             * Recommended to do after an arbitrary unit of work.
             * MANDATORY to do if you are saving a large amount of data - otherwise you may get
            cache errors.
             */
        session.flush();
    }

    public static void generatePrincipal() throws Exception{
//        session.beginTransaction();

        Principal pr1 = new Principal("Dani Keren", "Dani_Keren_password", false);
        session.save(pr1);
        Principal pr0 = new Principal("a", "1", false);
        session.save(pr0);
        Principal pr2 = new Principal("b", "2", false);
        session.save(pr2);
        Principal pr3 = new Principal("c", "3", false);
        session.save(pr3);
        Principal pr4 = new Principal("d", "4", false);
        session.save(pr4);
        Principal pr5 = new Principal("e", "5", false);
        session.save(pr5);
        Principal pr6 = new Principal("f", "6", false);
        session.save(pr6);
        Principal pr7 = new Principal("g", "7", false);
        session.save(pr7);
        Principal pr8 = new Principal("h", "8", false);
        session.save(pr8);
        Principal pr9 = new Principal("i", "9", false);
        session.save(pr9);
        Principal pr10 = new Principal("j", "10", false);
        session.save(pr10);
        Principal pr11= new Principal("k", "11", false);
        session.save(pr11);
        Principal pr12= new Principal("l", "12", false);
        session.save(pr12);
        Principal pr13= new Principal("m", "13", false);
        session.save(pr13);
        Principal pr14= new Principal("n", "14", false);
        session.save(pr14);
        session.flush();
    }

    public static void generateSubject() throws Exception
    {
        Subject biology = new Subject("Biology");
        session.save(biology);

        session.flush();
    }

    public static void generateCourse() throws Exception
    {
        Subject astro = new Subject("Astrophysics");
        session.save(astro);
        session.flush();
        Course speeds = new Course("Velocity in space", astro);
        session.save(speeds);
        session.flush();

        session.flush();
    }

    public static void generateQuestion() throws Exception
    {
        Subject astro = new Subject("Astrophysics");                  // new subject
        session.save(astro);
        session.flush();
        Course speeds = new Course("Velocity in space", astro);       // new course
        session.save(speeds);
        session.flush();
        // TODO: javafx
        Answer ans1 = new Answer("1 km/h", false);          // new answers
        Answer ans2 = new Answer("2 km/h", false);
        Answer ans3 = new Answer("3 km/h", false);
        Answer ans4 = new Answer("4 km/h", true);
        session.save(ans1);
        session.save(ans2);
        session.save(ans3);
        session.save(ans4);
        session.flush();

        Answer ans5 = new Answer("laptop", false);          // more new answers
        Answer ans6 = new Answer("table", false);
        Answer ans7 = new Answer("Cruise ship", true);
        Answer ans8 = new Answer("tent", false);
        session.save(ans5);
        session.save(ans6);
        session.save(ans7);
        session.save(ans8);
        session.flush();
        Question q1 = new Question("which number is bigger?", ans1, ans2, ans3, ans4, astro, speeds);// new questions
        session.save(q1);
            /*session.save(ans1);
            session.save(ans2);
            session.save(ans3);
            session.save(ans4);*/
        session.flush();
        q1.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
        session.save(q1);
        session.flush();
        Question q2 = new Question("which is bigger?", ans5, ans6, ans7, ans8, astro, speeds); // another question
        session.save(q2);
        session.flush();
        q2.updateCode();
        session.save(q2);
        session.flush();

    }
    public static List<Pupil> getAllPupils() throws Exception{
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Pupil> query = builder.createQuery(Pupil.class);
        query.from(Pupil.class);
        List<Pupil> pupils = session.createQuery(query).getResultList();
        return pupils;
    }

    private static void printAllPupils() throws Exception{
        List<Pupil> pupils=getAllPupils();
        System.out.println("All pupils are: ");
        if (pupils.isEmpty())
        {
            System.out.println("There are no pupils");
        }
        for(Pupil pupil: pupils)
        {
            System.out.println("Name: "+ pupil.getName());
            System.out.println("Password: "+ pupil.getPassword());
            System.out.println("Grades: "+ pupil.getGrades().toString());
            System.out.println("is logged in: "+ pupil.getIsLoggedIn());
        }
        System.out.println();
    }

    private static void printAllExams() throws Exception{
        List<Exam> exams=getAllExams();
        System.out.println("All pupils are: ");
        if (exams.isEmpty())
        {
            System.out.println("There are no pupils");
        }
        for(Exam exam: exams)
        {
            System.out.println("Name: "+ exam.getName());
            System.out.println("Password: "+ exam.getExam_code_number());
        }
        System.out.println();
    }

    private static void deleteAllPupils() throws Exception {

        String hql = "DELETE FROM Pupil";
        Query query = session.createQuery(hql);
        int rowCount = query.executeUpdate();
        System.out.println("Deleted " + rowCount + " students.");

        session.getTransaction().commit();
    }




    public static List<Teacher> getAllTeachers() throws Exception{
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
        query.from(Teacher.class);
        List<Teacher> teachers = session.createQuery(query).getResultList();
        return teachers;
    }

    public static List<Exam> getAllExams() throws Exception{
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Exam> query = builder.createQuery(Exam.class);
        query.from(Exam.class);
        List<Exam> exams = session.createQuery(query).getResultList();
        return exams;
    }




    private static void printAllTeachers() throws Exception{
        List<Teacher> teachers=getAllTeachers();
        System.out.println("All teachers are: ");
        if (teachers.isEmpty())
        {
            System.out.println("There are no teachers");
        }
        for(Teacher teacher: teachers)
        {
            System.out.println("Name: "+ teacher.getName());
            System.out.println("Password: "+ teacher.getPassword());
            System.out.println("is logged in: "+ teacher.getIsLoggedIn());
        }
        System.out.println();
    }

    private static void deleteAllTeachers() throws Exception {
        //  session.beginTransaction();

        String hql = "DELETE FROM Teacher";
        Query query = session.createQuery(hql);
        int rowCount = query.executeUpdate();
        System.out.println("Deleted " + rowCount + " teachers.");

        session.getTransaction().commit();
    }

    public static List<Principal> getAllPrincipals() throws Exception{
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Principal> query = builder.createQuery(Principal.class);
        query.from(Principal.class);
        List<Principal> principals = session.createQuery(query).getResultList();
        return principals;
    }

    private static void printAllPrincipals() throws Exception{
        List<Principal> principals=getAllPrincipals();
        System.out.println("All principals are: ");
        if (principals.isEmpty())
        {
            System.out.println("There are no principals");
        }
        for(Principal principal: principals)
        {
            System.out.println("Name: "+ principal.getName());
            System.out.println("Password: "+ principal.getPassword());
            System.out.println("is logged in: "+ principal.getIsLoggedIn());
        }
        System.out.println();
    }

    private static void deleteAllPrincipals() throws Exception {
        //session.beginTransaction();

        String hql = "DELETE FROM Principal";
        Query query = session.createQuery(hql);
        int rowCount = query.executeUpdate();
        System.out.println("Deleted " + rowCount + " principals.");

        session.getTransaction().commit();
    }

    public static List<Subject> getAllSubjects() throws Exception{
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
        query.from(Subject.class);
        List<Subject> subjects = session.createQuery(query).getResultList();
        return subjects;
    }

    private static void printAllSubject() throws Exception{
        List<Subject> subjects=getAllSubjects();
        System.out.println("All subjects are: ");
        if (subjects.isEmpty())
        {
            System.out.println("There are no subjects");
        }
        for(Subject subject: subjects)
        {
            System.out.println("Name: "+ subject.getName());
        }
        System.out.println();
    }

    private static void deleteAllSubjects() throws Exception {

        String hql = "DELETE FROM Subject";
        Query query = session.createQuery(hql);
        int rowCount = query.executeUpdate();
        System.out.println("Deleted " + rowCount + " subjects.");

    }

    public static List<Course> getAllCourses() throws Exception{
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Course> query = builder.createQuery(Course.class);
        query.from(Course.class);
        List<Course> courses = session.createQuery(query).getResultList();
        return courses;
    }

    private static void printAllCourses() throws Exception{
        List<Course> courses=getAllCourses();
        System.out.println("All courses are: ");
        if (courses.isEmpty())
        {
            System.out.println("There are no courses");
        }
        for(Course course: courses)
        {
            System.out.println("Name: "+ course.getName());
        }
        System.out.println();
    }

    private static void deleteAllCourses() throws Exception {

        String hql = "DELETE FROM Course";
        Query query = session.createQuery(hql);
        int rowCount = query.executeUpdate();
        System.out.println("Deleted " + rowCount + " courses.");

    }

    public static List<Question> getAllQuestions() throws Exception{
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);
        query.from(Question.class);
        List<Question> questions = session.createQuery(query).getResultList();
        return questions;
    }

    private static void printAllQuestions() throws Exception{
        List<Question> questions=getAllQuestions();
        System.out.println("All questions are: ");
        if (questions.isEmpty())
        {
            System.out.println("There are no questions");
        }
        for(Question question: questions)
        {
            System.out.println(question);
        }
        System.out.println();
    }

    private static void deleteAllQuestions() throws Exception {

        String hql = "DELETE FROM Question";
        Query query = session.createQuery(hql);
        int rowCount = query.executeUpdate();
        System.out.println("Deleted " + rowCount + " questions.");

    }

    private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3100); // for local use (in LAN)

//        server = new SimpleServer(13010);
        server.listen();

        try {
            SessionFactory sessionFactory=getSessionFactory();
            session = sessionFactory.openSession();

            session.beginTransaction();

//            Subject sub=new Subject("Rocket Engineering");
//            session.save(sub);
//            session.flush();
//            Subject physics=new Subject("Physics");
//            session.save(physics);
//            session.flush();
//            Subject computer_science=new Subject("Computer Science");
//            session.save(computer_science);
//            session.flush();
//            Subject mathematics=new Subject("Mathematics");
//            session.save(mathematics);
//            session.flush();
//            Subject biology=new Subject("Biology");
//            session.save(biology);
//            session.flush();
//            Subject chemestry=new Subject("Chemestry");
//            session.save(chemestry);
//            session.flush();
//            Subject history=new Subject("History");
//            session.save(history);
//            session.flush();
//            Subject literature=new Subject("Literature");
//            session.save(literature);
//            session.flush();
//            Subject geography=new Subject("Geography");
//            session.save(geography);
//            session.flush();
//            //--------------------------------------------courses--------------------------------------------//
//            Course football = new Course("Football", sports);       // new course
//            session.save(football);
//            session.flush();
//            Course basketball = new Course("Baseketball", sports);       // new course
//            session.save(basketball);
//            session.flush();
//            Course volleyball = new Course("Volleyball", sports);       // new course
//            session.save(volleyball);
//            session.flush();
//            Course Introductory_To_Physics = new Course("Introductory To Physics", physics);       // new course
//            session.save(Introductory_To_Physics);
//            session.flush();
//            Course Optics = new Course("Velocity in space", physics);       // new course
//            session.save(Optics);
//            session.flush();
//            Course Electricity_Magnetism_physics = new Course("Electricity & Magnetism-physics", physics);       // new course
//            session.save(Electricity_Magnetism_physics);
//            session.flush();
//            Course  Algorithms_Data_Structures = new Course("Algorithms & Data Structures", computer_science);       // new course
//            session.save(Algorithms_Data_Structures);
//            session.flush();
//            Course Machine_Learning = new Course("Machine Learning", computer_science);       // new course
//            session.save(Machine_Learning);
//            session.flush();
//            Course Software_Development = new Course("Software Development", computer_science);       // new course
//            session.save(Software_Development);
//            session.flush();
//            Course Linear_algebra = new Course("Linear Algebra", mathematics);       // new course
//            session.save(Linear_algebra);
//            session.flush();
//            Course calculus1 = new Course("calculus1", mathematics);       // new course
//            session.save(calculus1);
//            session.flush();
//            Course probability = new Course("Probability", mathematics);       // new course
//            session.save(probability);
//            session.flush();
//            Course Biotechnology = new Course("Biotechnology", chemestry);       // new course
//            session.save(Biotechnology);
//            session.flush();
//            Course Molecular_Chemistry = new Course("Molecular Chemistry", chemestry);       // new course
//            session.save(Molecular_Chemistry);
//            session.flush();
//            Course Theoretical_Chemistry = new Course("Theoretical Chemistry", chemestry);       // new course
//            session.save(Theoretical_Chemistry);
//            session.flush();
//            Course anatomy = new Course("Anatomy", biology);       // new course
//            session.save(anatomy);
//            session.flush();
//            Course environmental_biology = new Course("Environmental Biology", biology);       // new course
//            session.save(environmental_biology);
//            session.flush();
//            Course microbiology = new Course("Microbiology", biology);       // new course
//            session.save(microbiology);
//            session.flush();
//            Course world_war_1 = new Course("World War I", history);       // new course
//            session.save(world_war_1);
//            session.flush();
//            Course world_war_2 = new Course("World War II", history);       // new course
//            session.save(world_war_2);
//            session.flush();
//            Course The_Industrial_Revolution = new Course("The Industrial Revolution", history);       // new course
//            session.save(The_Industrial_Revolution);
//            session.flush();
//            Course poetry = new Course("Poetry", literature);       // new course
//            session.save(poetry);
//            session.flush();
//            Course classical_literature = new Course("Classical Literature", literature);       // new course
//            session.save(classical_literature);
//            session.flush();
//            Course Reading_Fiction = new Course("Reading Fiction", literature);       // new course
//            session.save(Reading_Fiction);
//            session.flush();
//            Course Human_Geography = new Course("Human Geography", geography);       // new course
//            session.save(Human_Geography);
//            session.flush();
//            Course Physical_Geography = new Course("Physical Geography", geography);       // new course
//            session.save(Physical_Geography);
//            session.flush();
//            Course language_geography = new Course("Language Geography", geography);       // new course
//            session.save(language_geography);
//            session.flush();
//
//            Answer ans1 = new Answer("9", false);          // new answers
//            Answer ans2 = new Answer("10", false);
//            Answer ans3 = new Answer("11", true);
//            Answer ans4 = new Answer("12", false);
//            session.save(ans1);
//            session.save(ans2);
//            session.save(ans3);
//            session.save(ans4);
//            session.flush();
//            Answer ans5 = new Answer("pele", false);          // new answers
//            Answer ans6 = new Answer("ronaldo", false);
//            Answer ans7 = new Answer("messi", true);
//            Answer ans8 = new Answer("maradona", false);
//            session.save(ans5);
//            session.save(ans6);
//            session.save(ans7);
//            session.save(ans8);
//            session.flush();
//            Answer ans9 = new Answer("manchester city", true);          // new answers
//            Answer ans10 = new Answer("barcelona", false);
//            Answer ans11 = new Answer("real madrid", false);
//            Answer ans12 = new Answer("lyon", false);
//            session.save(ans9);
//            session.save(ans10);
//            session.save(ans11);
//            session.save(ans12);
//            session.flush();
//            Answer ans13 = new Answer("roll back", false);          // new answers
//            Answer ans14 = new Answer("right back", true);
//            Answer ans15 = new Answer("roll right", false);
//            Answer ans16= new Answer("right ball", false);
//            session.save(ans13);
//            session.save(ans14);
//            session.save(ans15);
//            session.save(ans16);
//            session.flush();
//            Answer ans17 = new Answer("3", false);          // new answers
//            Answer ans18 = new Answer("4", false);
//            Answer ans19= new Answer("5", true);
//            Answer ans20 = new Answer("6", false);
//            session.save(ans17);
//            session.save(ans18);
//            session.save(ans19);
//            session.save(ans20);
//            session.flush();
//            Answer ans21 = new Answer("Mikel Jordan", true);          // new answers
//            Answer ans22 = new Answer("Lebron James", false);
//            Answer ans23 = new Answer("Kyrie Irving", false);
//            Answer ans24 = new Answer("Omri Caspi", false);
//            session.save(ans21);
//            session.save(ans22);
//            session.save(ans23);
//            session.save(ans24);
//            session.flush();
//            Answer ans25 = new Answer("Detroit", false);          // new answers
//            Answer ans26 = new Answer("Lakers", false);
//            Answer ans27 = new Answer("Real Madrid", false);
//            Answer ans28 = new Answer("Denver", true);
//            session.save(ans25);
//            session.save(ans26);
//            session.save(ans27);
//            session.save(ans28);
//            session.flush();
//            Answer ans29 = new Answer("Power Forward", true);          // new answers
//            Answer ans30 = new Answer("Power False", false);
//            Answer ans31 = new Answer("Power Fake", false);
//            Answer ans32 = new Answer("Panic Forward", false);
//            session.save(ans29);
//            session.save(ans30);
//            session.save(ans31);
//            session.save(ans32);
//            session.flush();
//            Answer ans33 = new Answer("5", false);          // new answers
//            Answer ans34 = new Answer("6", true);
//            Answer ans35 = new Answer("7", false);
//            Answer ans36 = new Answer("4", false);
//            session.save(ans33);
//            session.save(ans34);
//            session.save(ans35);
//            session.save(ans36);
//            session.flush();
//            Answer ans37 = new Answer("no", true);          // new answers
//            Answer ans38= new Answer("yes", false);
//            Answer ans39= new Answer("only in first set", false);
//            Answer ans40 = new Answer("when judge permites", false);
//            session.save(ans37);
//            session.save(ans38);
//            session.save(ans39);
//            session.save(ans40);
//            session.flush();
//            Answer ans41 = new Answer("15", false);          // new answers
//            Answer ans42 = new Answer("20", false);
//            Answer ans43 = new Answer("25", true);
//            Answer ans44 = new Answer("30", false);
//            session.save(ans41);
//            session.save(ans42);
//            session.save(ans43);
//            session.save(ans44);
//            session.flush();
//            Answer ans45 = new Answer("15", true);          // new answers
//            Answer ans46= new Answer("20", false);
//            Answer ans47= new Answer("25", false);
//            Answer ans48 = new Answer("30", false);
//            session.save(ans45);
//            session.save(ans46);
//            session.save(ans47);
//            session.save(ans48);
//            session.flush();
//            Answer ans49 = new Answer("Conductive metal wires", false);          // new answers
//            Answer ans50 = new Answer("Silicon solar cells", true);
//            Answer ans51 = new Answer("Reflective mirrors", false);
//            Answer ans52 = new Answer("Non of the above", false);
//            session.save(ans49);
//            session.save(ans50);
//            session.save(ans51);
//            session.save(ans52);
//            session.flush();
//            Answer ans53 = new Answer("Ampere", true);          // new answers
//            Answer ans54 = new Answer("Volt", false);
//            Answer ans55= new Answer("Ohm", false);
//            Answer ans56= new Answer("Watt", false);
//            session.save(ans53);
//            session.save(ans54);
//            session.save(ans55);
//            session.save(ans56);
//            session.flush();
//            Answer ans57 = new Answer("Melting ice", false);          // new answers
//            Answer ans58 = new Answer("Boiling water", true);
//            Answer ans59= new Answer("Dissolving sugar in water ans", false);
//            Answer ans60 = new Answer("None of the above", false);
//            session.save(ans57);
//            session.save(ans58);
//            session.save(ans59);
//            session.save(ans60);
//            session.flush();
//            Answer ans61 = new Answer("Condensation", false);          // new answers
//            Answer ans62 = new Answer("Evaporation ", false);
//            Answer ans63 = new Answer("Sublimation ", true);
//            Answer ans64 = new Answer("Freezing ", false);
//            session.save(ans61);
//            session.save(ans62);
//            session.save(ans63);
//            session.save(ans64);
//            session.flush();
//            Answer ans65 = new Answer("Reflection", false);          // new answers
//            Answer ans66 = new Answer("Refraction", true);
//            Answer ans67= new Answer("Diffraction", false);
//            Answer ans68 = new Answer("Dispersion", false);
//            session.save(ans65);
//            session.save(ans66);
//            session.save(ans67);
//            session.save(ans68);
//            session.flush();
//            Answer ans69 = new Answer("Convex lens", true);          // new answers
//            Answer ans70 = new Answer("Concave lens", false);
//            Answer ans71= new Answer("Plano-convex lens", false);
//            Answer ans72 = new Answer("Plano-concave lens", false);
//            session.save(ans69);
//            session.save(ans70);
//            session.save(ans71);
//            session.save(ans72);
//            session.flush();
//            Answer ans73 = new Answer("Concave mirror", true);          // new answers
//            Answer ans74= new Answer("Convex mirror", false);
//            Answer ans75= new Answer("Plane mirror", false);
//            Answer ans76= new Answer("Spherical mirror", false);
//            session.save(ans73);
//            session.save(ans74);
//            session.save(ans75);
//            session.save(ans76);
//            session.flush();
//            Answer ans77 = new Answer("Blue", false);          // new answers
//            Answer ans78 = new Answer("Green", false);
//            Answer ans79 = new Answer("Red", true);
//            Answer ans80= new Answer("Violet", false);
//            session.save(ans77);
//            session.save(ans78);
//            session.save(ans79);
//            session.save(ans80);
//            session.flush();
//            Answer ans81 = new Answer("Ampere", false);          // new answers
//            Answer ans82 = new Answer("Coulomb", true);
//            Answer ans83 = new Answer("Ohm", false);
//            Answer ans84= new Answer("Volt", false);
//            session.save(ans81);
//            session.save(ans82);
//            session.save(ans83);
//            session.save(ans84);
//            session.flush();
//            Answer ans85 = new Answer("Ohm's law: Current (I) = Voltage (V) / Resistance (R)", true);          // new answers
//            Answer ans86= new Answer("Kirchhoff's law: Voltage (V) = Current (I) * Resistance", false);
//            Answer ans87= new Answer("Faraday's law: Resistance (R) = Voltage (V) / Current (I)", false);
//            Answer ans88= new Answer("Ampere's law: Voltage (V) = Current (I) + Resistance (R)", false);
//            session.save(ans85);
//            session.save(ans86);
//            session.save(ans87);
//            session.save(ans88);
//            session.flush();
//            Answer ans89 = new Answer("The flow of electric current through a conductor", false);          // new answers
//            Answer ans90 = new Answer("The interaction between a magnetic field and an electric field", false);
//            Answer ans91 = new Answer("The motion of a conductor through a magnetic field", true);
//            Answer ans92 = new Answer("The polarization of light in a magnetic medium", false);
//            session.save(ans89);
//            session.save(ans90);
//            session.save(ans91);
//            session.save(ans92);
//            session.flush();
//            Answer ans93 = new Answer("Copper", false);          // new answers
//            Answer ans94 = new Answer("Silver", false);
//            Answer ans95= new Answer("Aluminum", false);
//            Answer ans96= new Answer("Rubber", true);
//            session.save(ans93);
//            session.save(ans94);
//            session.save(ans95);
//            session.save(ans96);
//            session.flush();
//            Answer ans97 = new Answer("Stack", false);          // new answers
//            Answer ans98=new Answer("Queue", true);
//            Answer ans99 = new Answer("Linked list", false);
//            Answer ans100 = new Answer("Binary tree", false);
//            session.save(ans97);
//            session.save(ans98);
//            session.save(ans99);
//            session.save(ans100);
//            session.flush();
//            Answer ans101 = new Answer("O(1)", false);          // new answers
//            Answer ans102 = new Answer("O(log n)", true);
//            Answer ans103 = new Answer("O(n)", false);
//            Answer ans104= new Answer("O(n log n)", false);
//            session.save(ans101);
//            session.save(ans102);
//            session.save(ans103);
//            session.save(ans104);
//            session.flush();
//            Answer ans105 = new Answer("Merge sort", false);          // new answers
//            Answer ans106 = new Answer("Quick sort", false);
//            Answer ans107= new Answer("Insertion sort", true);
//            Answer ans108= new Answer("Radix sort", false);
//            session.save(ans105);
//            session.save(ans106);
//            session.save(ans107);
//            session.save(ans108);
//            session.flush();
//
//            Answer ans109 = new Answer("Stack", false);          // new answers
//            Answer ans110 = new Answer("Queue", false);
//            Answer ans111 = new Answer("Linked list", true);
//            Answer ans112 = new Answer("Hash table", false);
//            session.save(ans109);
//            session.save(ans110);
//            session.save(ans111);
//            session.save(ans112);
//            session.flush();
//
//            Answer ans113 = new Answer("Classification", false);          // new answers
//            Answer ans114 = new Answer("Clustering", false);
//            Answer ans115 = new Answer("Regression", true);
//            Answer ans116= new Answer("Reinforcement Learning", false);
//            session.save(ans113);
//            session.save(ans114);
//            session.save(ans115);
//            session.save(ans116);
//            session.flush();
//
//            Answer ans117 = new Answer("Mean Squared Error (MSE)", false);          // new answers
//            Answer ans118 = new Answer("rea Under the Curve (AUC)", true);
//            Answer ans119 = new Answer("F1 score", false);
//            Answer ans120 = new Answer("R-squared", false);
//            session.save(ans117);
//            session.save(ans118);
//            session.save(ans119);
//            session.save(ans120);
//            session.flush();
//            Answer ans121 = new Answer("To normalize the data distribution", true);          // new answers
//            Answer ans122 = new Answer("To remove outliers from the dataset", false);
//            Answer ans123 = new Answer("To increase model complexity", false);
//            Answer ans124 = new Answer("To improve model interpretability", false);
//            session.save(ans121);
//            session.save(ans122);
//            session.save(ans123);
//            session.save(ans124);
//            session.flush();
//            Answer ans125 = new Answer("Regularization", true);          // new answers
//            Answer ans126 = new Answer("Cross-validation", false);
//            Answer ans127 = new Answer("Dimensionality reduction", false);
//            Answer ans128 = new Answer("Ensemble learning", false);
//            session.save(ans125);
//            session.save(ans126);
//            session.save(ans127);
//            session.save(ans128);
//            session.flush();
//            Answer ans129 = new Answer("To track and manage changes to source code over time-right", true);          // new answers
//            Answer ans130 = new Answer("To document software requirements and specifications", false);
//            Answer ans131 = new Answer("To automate software testing processes", false);
//            Answer ans132 = new Answer("To deploy software applications to production servers", false);
//            session.save(ans129);
//            session.save(ans130);
//            session.save(ans131);
//            session.save(ans132);
//            session.flush();
//            Answer ans133 = new Answer("A sequential and linear approach to software development", false);          // new answers
//            Answer ans134 = new Answer("Emphasizing comprehensive documentation and planning upfront", false);
//            Answer ans135 = new Answer("Adapting to changing requirements through iterative development", true);
//            Answer ans136 = new Answer("Focusing on individual coding tasks and minimizing collaboration", false);
//            session.save(ans133);
//            session.save(ans134);
//            session.save(ans135);
//            session.save(ans136);
//            session.flush();
//            Answer ans137 = new Answer("Object-Oriented Programming (OOP)", true);          // new answers
//            Answer ans138 = new Answer("Functional Programming (FP)", false);
//            Answer ans139 = new Answer("Procedural Programming", false);
//            Answer ans140 = new Answer("Event-Driven Programming", false);
//            session.save(ans137);
//            session.save(ans138);
//            session.save(ans139);
//            session.save(ans140);
//            session.flush();
//            Answer ans141 = new Answer("Version control", false);          // new answers
//            Answer ans142 = new Answer("Unit testing", false);
//            Answer ans143 = new Answer("Code refactoring", false);
//            Answer ans144 = new Answer("Debugging", true);
//            session.save(ans141);
//            session.save(ans142);
//            session.save(ans143);
//            session.save(ans144);
//            session.flush();
//            Answer ans145 = new Answer("ad - bc", true);          // new answers
//            Answer ans146 = new Answer("a + b + c + d", false);
//            Answer ans147 = new Answer("(a + b)(c + d)", false);
//            Answer ans148 = new Answer("abcd", false);
//            session.save(ans145);
//            session.save(ans146);
//            session.save(ans147);
//            session.save(ans148);
//            session.flush();
//            Answer ans149 = new Answer("The matrix has a determinant of zero", false);          // new answers
//            Answer ans150 = new Answer("The matrix has all zero entries", false);
//            Answer ans151 = new Answer("The matrix is square and its determinant is nonzero", true);
//            Answer ans152 = new Answer("The matrix is symmetric", false);
//            session.save(ans149);
//            session.save(ans150);
//            session.save(ans151);
//            session.save(ans152);
//            session.flush();
//            Answer ans153 = new Answer("Row reduction", false);          // new answers
//            Answer ans154 = new Answer("Transposition", false);
//            Answer ans155 = new Answer("Permutation", true);
//            Answer ans156 = new Answer("Scaling", false);
//            session.save(ans153);
//            session.save(ans154);
//            session.save(ans155);
//            session.save(ans156);
//            session.flush();
//            Answer ans157 = new Answer("The sum of the products of their corresponding components", true);          // new answers
//            Answer ans158 = new Answer("The difference between their magnitudes", false);
//            Answer ans159 = new Answer("The product of their magnitudes", false);
//            Answer ans160 = new Answer("The cross product of their magnitudes", false);
//            session.save(ans157);
//            session.save(ans158);
//            session.save(ans159);
//            session.save(ans160);
//            session.flush();
//            Answer ans161 = new Answer("6x + 2", true);          // new answers
//            Answer ans162 = new Answer("6x - 2", false);
//            Answer ans163 = new Answer("3x^2 + 2x", false);
//            Answer ans164= new Answer("3x^2 - 2x", false);
//            session.save(ans161);
//            session.save(ans162);
//            session.save(ans163);
//            session.save(ans164);
//            session.flush();
//            Answer ans165 = new Answer("x^4 - x^3 + (5/2)x^2 - x + C", false);          // new answers
//            Answer ans166 = new Answer("4x^4 - 2x^3 + 5x^2 - x + C", false);
//            Answer ans167= new Answer("(4/3)x^4 - (2/3)x^3 + (5/2)x^2 - x + C", true);
//            Answer ans168 = new Answer("(4/3)x^4 - (2/3)x^3 + (5/2)x^2", false);
//            session.save(ans165);
//            session.save(ans166);
//            session.save(ans167);
//            session.save(ans168);
//            session.flush();
//            Answer ans169 = new Answer("Infinity (∞)", true);          // new answers
//            Answer ans170 = new Answer("-Infinity (-∞)", false);
//            Answer ans171 = new Answer("1", false);
//            Answer ans172 = new Answer("Does not exist", false);
//            session.save(ans169);
//            session.save(ans170);
//            session.save(ans171);
//            session.save(ans172);
//            session.flush();
//
//            Answer ans173 = new Answer("cos(x)", true);          // new answers
//            Answer ans174 = new Answer("-cos(x)", false);
//            Answer ans175= new Answer("sin(x)", false);
//            Answer ans176= new Answer("-sin(x)", false);
//            session.save(ans173);
//            session.save(ans174);
//            session.save(ans175);
//            session.save(ans176);
//            session.flush();
//
//            Answer ans177 = new Answer("1/2", false);          // new answers
//            Answer ans178 = new Answer("1/3", false);
//            Answer ans179 = new Answer("2/3", true);
//            Answer ans180 = new Answer("1/6", false);
//            session.save(ans177);
//            session.save(ans178);
//            session.save(ans179);
//            session.save(ans180);
//            session.flush();
//
//            Answer ans181 = new Answer("1/13", false);          // new answers
//            Answer ans182 = new Answer("1/26", true);
//            Answer ans183 = new Answer("1/52", false);
//            Answer ans184 = new Answer("13/51", false);
//            session.save(ans181);
//            session.save(ans182);
//            session.save(ans183);
//            session.save(ans184);
//            session.flush();
//
//            Answer ans185 = new Answer("1/10", false);          // new answers
//            Answer ans186 = new Answer("3/10", true);
//            Answer ans187 = new Answer("1/3", false);
//            Answer ans188 = new Answer("3/5", false);
//            session.save(ans185);
//            session.save(ans186);
//            session.save(ans187);
//            session.save(ans188);
//            session.flush();
//            Answer ans189 = new Answer("0.2", false);          // new answers
//            Answer ans190 = new Answer("0.4", false);
//            Answer ans191 = new Answer("0.6", false);
//            Answer ans192 = new Answer("1.0", true);
//            session.save(ans189);
//            session.save(ans190);
//            session.save(ans191);
//            session.save(ans192);
//            session.flush();
//            Answer ans193 = new Answer("DNA synthesis", false);          // new answers
//            Answer ans194 = new Answer("DNA sequencing", false);
//            Answer ans195 = new Answer("DNA cloning", false);
//            Answer ans196 = new Answer("Polymerase chain reaction (PCR)", true);
//            session.save(ans193);
//            session.save(ans194);
//            session.save(ans195);
//            session.save(ans196);
//            session.flush();
//            Answer ans197 = new Answer("They facilitate DNA replication", false);          // new answers
//            Answer ans198 = new Answer("They produce complementary RNA strands", false);
//            Answer ans199 = new Answer("They cut DNA at specific recognition sequences", true);
//            Answer ans200 = new Answer("They bind to DNA to activate gene expression", false);
//            session.save(ans197);
//            session.save(ans198);
//            session.save(ans199);
//            session.save(ans200);
//            session.flush();
//            Answer ans201 = new Answer("To create genetically modified organisms (GMOs)", true);          // new answers
//            Answer ans202 = new Answer("To study the inheritance patterns of genetic traits", false);
//            Answer ans203 = new Answer("To develop new drug molecules", false);
//            Answer ans204 = new Answer("To improve crop yields through selective breeding", false);
//            session.save(ans201);
//            session.save(ans202);
//            session.save(ans203);
//            session.save(ans204);
//            session.flush();
//            Answer ans205 = new Answer("DNA sequencing", false);          // new answers
//            Answer ans206 = new Answer("DNA hybridization", false);
//            Answer ans207 = new Answer("Gel electrophoresis", true);
//            Answer ans208 = new Answer("Southern blotting", false);
//            session.save(ans205);
//            session.save(ans206);
//            session.save(ans207);
//            session.save(ans208);
//            session.flush();
//            Answer ans209 = new Answer("Isomer", true);          // new answers
//            Answer ans210 = new Answer("Homolog", false);
//            Answer ans211 = new Answer("Enantiomer", false);
//            Answer ans212 = new Answer("Polymorph", false);
//            session.save(ans209);
//            session.save(ans210);
//            session.save(ans211);
//            session.save(ans212);
//            session.flush();
//            Answer ans213 = new Answer("Ionic bond", false);          // new answers
//            Answer ans214 = new Answer("Covalent bond", true);
//            Answer ans215 = new Answer("Metallic bond", false);
//            Answer ans216 = new Answer("Hydrogen bond", false);
//            session.save(ans213);
//            session.save(ans214);
//            session.save(ans215);
//            session.save(ans216);
//            session.flush();
//            Answer ans217 = new Answer("Condensation", false);          // new answers
//            Answer ans218 = new Answer("Sublimation", false);
//            Answer ans219 = new Answer("Vaporization", true);
//            Answer ans220 = new Answer("Deposition", false);
//            session.save(ans217);
//            session.save(ans218);
//            session.save(ans219);
//            session.save(ans220);
//            session.flush();
//            Answer ans221 = new Answer("Decomposition reaction", false);          // new answers
//            Answer ans222 = new Answer("Redox reaction", false);
//            Answer ans223 = new Answer("Synthesis reaction", true);
//            Answer ans224 = new Answer("Acid-base reaction", false);
//            session.save(ans221);
//            session.save(ans222);
//            session.save(ans223);
//            session.save(ans224);
//            session.flush();
//            Answer ans225 = new Answer("To calculate molecular orbitals and electron distributions", true);          // new answers
//            Answer ans226 = new Answer("To determine reaction kinetics and thermodynamics", false);
//            Answer ans227 = new Answer("To predict the three-dimensional structure of molecules", false);
//            Answer ans228 = new Answer("To study the properties of bulk materials", false);
//            session.save(ans225);
//            session.save(ans226);
//            session.save(ans227);
//            session.save(ans228);
//            session.flush();
//            Answer ans229 = new Answer("An approximation that allows for the separation of nuclear and electronic motions", true);          // new answers
//            Answer ans230 = new Answer("A method to predict the energy changes during a chemical reaction", false);
//            Answer ans231 = new Answer("A technique to determine the electron density of a molecule", false);
//            Answer ans232 = new Answer("A model for describing the behavior of superconducting materials", false);
//            session.save(ans229);
//            session.save(ans230);
//            session.save(ans231);
//            session.save(ans232);
//            session.flush();
//            Answer ans233 = new Answer("Density functional theory (DFT)", false);          // new answers
//            Answer ans234 = new Answer("Hartree-Fock theory", false);
//            Answer ans235 = new Answer("Molecular mechanics", true);
//            Answer ans236= new Answer("Coupled cluster theory", false);
//            session.save(ans233);
//            session.save(ans234);
//            session.save(ans235);
//            session.save(ans236);
//            session.flush();
//            Answer ans237 = new Answer("To develop new experimental techniques for chemical analysis", false);          // new answers
//            Answer ans238 = new Answer("To study chemical reactions under controlled laboratory conditions", false);
//            Answer ans239 = new Answer("To understand and explain chemical phenomena using mathematical models and simulations", true);
//            Answer ans240 = new Answer("To synthesize new compounds with unique properties", false);
//            session.save(ans237);
//            session.save(ans238);
//            session.save(ans239);
//            session.save(ans240);
//            session.flush();
//            Answer ans241 = new Answer("Femur", true);          // new answers
//            Answer ans242 = new Answer("Humerus", false);
//            Answer ans243 = new Answer("Tibia", false);
//            Answer ans244 = new Answer("Ulna", false);
//            session.save(ans241);
//            session.save(ans242);
//            session.save(ans243);
//            session.save(ans244);
//            session.flush();
//            Answer ans245 = new Answer("Pumping blood throughout the body", false);          // new answers
//            Answer ans246 = new Answer("Filtration and excretion of waste products", false);
//            Answer ans247 = new Answer("Regulating body temperature", false);
//            Answer ans248 = new Answer("Facilitating the exchange of oxygen and carbon dioxide", true);
//            session.save(ans245);
//            session.save(ans246);
//            session.save(ans247);
//            session.save(ans248);
//            session.flush();
//
//            Answer ans249 = new Answer("Liver", false);          // new answers
//            Answer ans250 = new Answer("Kidneys", true);
//            Answer ans251 = new Answer("Pancreas", false);
//            Answer ans252 = new Answer("Spleen", false);
//            session.save(ans249);
//            session.save(ans250);
//            session.save(ans251);
//            session.save(ans252);
//            session.flush();
//            Answer ans253 = new Answer("Skeletal muscle", false);          // new answers
//            Answer ans254 = new Answer("Cardiac muscle", false);
//            Answer ans255 = new Answer("Smooth muscle", false);
//            Answer ans256 = new Answer("Epithelial muscle", true);
//            session.save(ans253);
//            session.save(ans254);
//            session.save(ans255);
//            session.save(ans256);
//            session.flush();
//            Answer ans257 = new Answer("Ecology", false);          // new answers
//            Answer ans258 = new Answer("Biodiversity", true);
//            Answer ans259 = new Answer("Evolution", false);
//            Answer ans260 = new Answer("Symbiosis", false);
//            session.save(ans257);
//            session.save(ans258);
//            session.save(ans259);
//            session.save(ans260);
//            session.flush();
//            Answer ans261 = new Answer("Sunlight", true);          // new answers
//            Answer ans262 = new Answer("Wind", false);
//            Answer ans263 = new Answer("Geothermal heat", false);
//            Answer ans264 = new Answer("Chemical reactions", false);
//            session.save(ans261);
//            session.save(ans262);
//            session.save(ans263);
//            session.save(ans264);
//            session.flush();
//            Answer ans265 = new Answer("Oxygen (O2)", false);          // new answers
//            Answer ans266 = new Answer("Nitrogen (N2)", false);
//            Answer ans267 = new Answer("Carbon dioxide (CO2)", true);
//            Answer ans268 = new Answer("Argon (Ar)", false);
//            session.save(ans265);
//            session.save(ans266);
//            session.save(ans267);
//            session.save(ans268);
//            session.flush();
//            Answer ans269 = new Answer("Respiration", false);          // new answers
//            Answer ans270 = new Answer("Fermentation", false);
//            Answer ans271 = new Answer("Photosynthesis", true);
//            Answer ans272 = new Answer("Transpiration", false);
//            session.save(ans269);
//            session.save(ans270);
//            session.save(ans271);
//            session.save(ans272);
//            session.flush();
//            Answer ans273 = new Answer("Virology", false);          // new answers
//            Answer ans274 = new Answer("Mycology", false);
//            Answer ans275 = new Answer("Microbiology", true);
//            Answer ans276 = new Answer("Parasitology", false);
//            session.save(ans273);
//            session.save(ans274);
//            session.save(ans275);
//            session.save(ans276);
//            session.flush();
//
//            Answer ans277 = new Answer("Staphylococcus aureus", false);          // new answers
//            Answer ans278 = new Answer("Escherichia coli", true);
//            Answer ans279 = new Answer("Streptococcus pyogenes", false);
//            Answer ans280 = new Answer("Bacillus anthracis", false);
//            session.save(ans277);
//            session.save(ans278);
//            session.save(ans279);
//            session.save(ans280);
//            session.flush();
//
//            Answer ans281 = new Answer("Stimulating the immune system", false);          // new answers
//            Answer ans282 = new Answer("Treating viral infections", false);
//            Answer ans283 = new Answer("Killing or inhibiting the growth of bacteria", true);
//            Answer ans284 = new Answer("Preventing fungal growth", false);
//            session.save(ans281);
//            session.save(ans282);
//            session.save(ans283);
//            session.save(ans284);
//            session.flush();
//            Answer ans285 = new Answer("Malaria", false);          // new answers
//            Answer ans286 = new Answer("Tuberculosis", false);
//            Answer ans287 = new Answer("Influenza", true);
//            Answer ans288 = new Answer("Lyme disease", false);
//            session.save(ans285);
//            session.save(ans286);
//            session.save(ans287);
//            session.save(ans288);
//            session.flush();
//
//            Answer ans289 = new Answer("1914", true);          // new answers
//            Answer ans290 = new Answer("1916", false);
//            Answer ans291 = new Answer("1918", false);
//            Answer ans292 = new Answer("1920", false);
//            session.save(ans289);
//            session.save(ans290);
//            session.save(ans291);
//            session.save(ans292);
//            session.flush();
//            Answer ans293 = new Answer("The assassination of Archduke Franz Ferdinand", true);          // new answers
//            Answer ans294 = new Answer("The signing of the Treaty of Versailles", false);
//            Answer ans295 = new Answer("The sinking of the Lusitania", false);
//            Answer ans296 = new Answer("The Zimmerman Telegram", false);
//            session.save(ans293);
//            session.save(ans294);
//            session.save(ans295);
//            session.save(ans296);
//            session.flush();
//            Answer ans297 = new Answer("Germany, Austria-Hungary, and the Ottoman Empire", true);          // new answers
//            Answer ans298 = new Answer("France, Russia, and the United Kingdom", false);
//            Answer ans299 = new Answer("Italy, Japan, and the United States", false);
//            Answer ans300 = new Answer("Serbia, Bulgaria, and Greece", false);
//            session.save(ans297);
//            session.save(ans298);
//            session.save(ans299);
//            session.save(ans300);
//            session.flush();
//            Answer ans301 = new Answer("1916", false);          // new answers
//            Answer ans302 = new Answer("1917", false);
//            Answer ans303 = new Answer("1918", true);
//            Answer ans304 = new Answer("1919", false);
//            session.save(ans301);
//            session.save(ans302);
//            session.save(ans303);
//            session.save(ans304);
//            session.flush();
//
//            Answer ans305 = new Answer("1937", false);          // new answers
//            Answer ans306 = new Answer("1939", true);
//            Answer ans307 = new Answer("1941", false);
//            Answer ans308 = new Answer("1945", false);
//            session.save(ans305);
//            session.save(ans306);
//            session.save(ans307);
//            session.save(ans308);
//            session.flush();
//            Answer ans309 = new Answer("The bombing of Pearl Harbor", false);          // new answers
//            Answer ans310 = new Answer("The invasion of Poland", true);
//            Answer ans311 = new Answer("The signing of the Munich Agreement", false);
//            Answer ans312 = new Answer("The invasion of France", false);
//            session.save(ans309);
//            session.save(ans310);
//            session.save(ans311);
//            session.save(ans312);
//            session.flush();
//            Answer ans313 = new Answer("Germany, Italy, and Japan", true);          // new answers
//            Answer ans314 = new Answer("United States, United Kingdom, and Soviet Union", false);
//            Answer ans315 = new Answer("France, Poland, and Australia", false);
//            Answer ans316 = new Answer("China, India, and Canada", false);
//            session.save(ans313);
//            session.save(ans314);
//            session.save(ans315);
//            session.save(ans316);
//            session.flush();
//            Answer ans317 = new Answer("1943", false);          // new answers
//            Answer ans318 = new Answer("1945", true);
//            Answer ans319 = new Answer("1947", false);
//            Answer ans320 = new Answer("1950", false);
//            session.save(ans317);
//            session.save(ans318);
//            session.save(ans319);
//            session.save(ans320);
//            session.flush();
//            Answer ans321 = new Answer("17th century", false);          // new answers
//            Answer ans322 = new Answer("18th century", true);
//            Answer ans323 = new Answer("19th century", false);
//            Answer ans324 = new Answer("20th century", false);
//            session.save(ans321);
//            session.save(ans322);
//            session.save(ans323);
//            session.save(ans324);
//            session.flush();
//            Answer ans325 = new Answer("Steam engine", true);          // new answers
//            Answer ans326 = new Answer("Telegraph", false);
//            Answer ans327 = new Answer("Printing press", false);
//            Answer ans328 = new Answer("Cotton gin", false);
//            session.save(ans325);
//            session.save(ans326);
//            session.save(ans327);
//            session.save(ans328);
//            session.flush();
//            Answer ans329 = new Answer("Agriculture", false);          // new answers
//            Answer ans330 = new Answer("Textile", true);
//            Answer ans331 = new Answer("Mining", false);
//            Answer ans332 = new Answer("Fishing", false);
//            session.save(ans329);
//            session.save(ans330);
//            session.save(ans331);
//            session.save(ans332);
//            session.flush();
//            Answer ans333 = new Answer("United States", false);          // new answers
//            Answer ans334 = new Answer("Germany", false);
//            Answer ans335 = new Answer("France", false);
//            Answer ans336 = new Answer("United Kingdom", true);
//            session.save(ans333);
//            session.save(ans334);
//            session.save(ans335);
//            session.save(ans336);
//            session.flush();
//            Answer ans337 = new Answer("The title of a poem", false);          // new answers
//            Answer ans338 = new Answer("A type of rhyme scheme", false);
//            Answer ans339 = new Answer("A group of lines within a poem", true);
//            Answer ans340 = new Answer("The author's signature", false);
//            session.save(ans337);
//            session.save(ans338);
//            session.save(ans339);
//            session.save(ans340);
//            session.flush();
//            Answer ans341 = new Answer("Rhyme", false);          // new answers
//            Answer ans342 = new Answer("Alliteration", true);
//            Answer ans343 = new Answer("Metaphor", false);
//            Answer ans344 = new Answer("Simile", false);
//            session.save(ans341);
//            session.save(ans342);
//            session.save(ans343);
//            session.save(ans344);
//            session.flush();
//            Answer ans345 = new Answer("Metaphor", false);          // new answers
//            Answer ans346 = new Answer("Personification", false);
//            Answer ans347 = new Answer("Onomatopoeia-", true);
//            Answer ans348 = new Answer("Hyperbole", false);
//            session.save(ans345);
//            session.save(ans346);
//            session.save(ans347);
//            session.save(ans348);
//            session.flush();
//            Answer ans349 = new Answer("Sonnet", true);          // new answers
//            Answer ans350 = new Answer("Haiku", false);
//            Answer ans351 = new Answer("Ballad", false);
//            Answer ans352 = new Answer("Epic", false);
//            session.save(ans349);
//            session.save(ans350);
//            session.save(ans351);
//            session.save(ans352);
//            session.flush();
//            Answer ans353 = new Answer("Homer", true);          // new answers
//            Answer ans354 = new Answer("Virgil", false);
//            Answer ans355 = new Answer("Ovid", false);
//            Answer ans356 = new Answer("Sophocles", false);
//            session.save(ans353);
//            session.save(ans354);
//            session.save(ans355);
//            session.save(ans356);
//            session.flush();
//            Answer ans357 = new Answer("Romeo and Juliet", false);          // new answers
//            Answer ans358 = new Answer("Hamlet", false);
//            Answer ans359 = new Answer("Macbeth", true);
//            Answer ans360 = new Answer("Othello", false);
//            session.save(ans357);
//            session.save(ans358);
//            session.save(ans359);
//            session.save(ans360);
//            session.flush();
//            Answer ans361 = new Answer("Jane Austen", true);          // new answers
//            Answer ans362 = new Answer("Emily Brontë", false);
//            Answer ans363 = new Answer("Charlotte Brontë", false);
//            Answer ans364 = new Answer("Mary Shelley", false);
//            session.save(ans361);
//            session.save(ans362);
//            session.save(ans363);
//            session.save(ans364);
//            session.flush();
//            Answer ans365 = new Answer("Sophocles", true);          // new answers
//            Answer ans366 = new Answer("Euripides", false);
//            Answer ans367 = new Answer("Aeschylus", false);
//            Answer ans368 = new Answer("Aristophanes", false);
//            session.save(ans365);
//            session.save(ans366);
//            session.save(ans367);
//            session.save(ans368);
//            session.flush();
//            Answer ans369 = new Answer("Plot", true);          // new answers
//            Answer ans370 = new Answer("Theme", false);
//            Answer ans371 = new Answer("Setting", false);
//            Answer ans372 = new Answer("Character", false);
//            session.save(ans369);
//            session.save(ans370);
//            session.save(ans371);
//            session.save(ans372);
//            session.flush();
//            Answer ans373 = new Answer("Plot", false);          // new answers
//            Answer ans374 = new Answer("Theme", false);
//            Answer ans375 = new Answer("Setting", false);
//            Answer ans376 = new Answer("Point of view", true);
//            session.save(ans373);
//            session.save(ans374);
//            session.save(ans375);
//            session.save(ans376);
//            session.flush();
//            Answer ans377 = new Answer("Simile", true);          // new answers
//            Answer ans378 = new Answer("Metaphor", false);
//            Answer ans379 = new Answer("Personification", false);
//            Answer ans380 = new Answer("Hyperbole", false);
//            session.save(ans377);
//            session.save(ans378);
//            session.save(ans379);
//            session.save(ans380);
//            session.flush();
//            Answer ans381 = new Answer("Plot", false);          // new answers
//            Answer ans382 = new Answer("Theme", true);
//            Answer ans383 = new Answer("Setting", false);
//            Answer ans384 = new Answer("Conflict", false);
//            session.save(ans381);
//            session.save(ans382);
//            session.save(ans383);
//            session.save(ans384);
//            session.flush();
//            Answer ans385 = new Answer("Erosion", false);          // new answers
//            Answer ans386 = new Answer("Weathering", true);
//            Answer ans387 = new Answer("Deposition", false);
//            Answer ans388 = new Answer("Subduction", false);
//            session.save(ans385);
//            session.save(ans386);
//            session.save(ans387);
//            session.save(ans388);
//            session.flush();
//            Answer ans389 = new Answer("Glacier", true);          // new answers
//            Answer ans390 = new Answer("Tundra", false);
//            Answer ans391 = new Answer("Steppe", false);
//            Answer ans392 = new Answer("Delta", false);
//            session.save(ans389);
//            session.save(ans390);
//            session.save(ans391);
//            session.save(ans392);
//            session.flush();
//            Answer ans393 = new Answer("Convergent boundary", false);          // new answers
//            Answer ans394 = new Answer("Transform boundary", false);
//            Answer ans395 = new Answer("Divergent boundary", true);
//            Answer ans396 = new Answer("Subduction zone", false);
//            session.save(ans393);
//            session.save(ans394);
//            session.save(ans395);
//            session.save(ans396);
//            session.flush();
//            Answer ans397 = new Answer("El Niño", true);          // new answers
//            Answer ans398 = new Answer("La Niña", false);
//            Answer ans399 = new Answer("Monsoon", false);
//            Answer ans400 = new Answer("Coriolis effect", false);
//            session.save(ans397);
//            session.save(ans398);
//            session.save(ans399);
//            session.save(ans400);
//            session.flush();
//            Answer ans401 = new Answer("Indo-European", false);          // new answers
//            Answer ans402 = new Answer("Sino-Tibetan", true);
//            Answer ans403 = new Answer("Afro-Asiatic", false);
//            Answer ans404 = new Answer("Austroasiatic", false);
//            session.save(ans401);
//            session.save(ans402);
//            session.save(ans403);
//            session.save(ans404);
//            session.flush();
//            Answer ans405 = new Answer("Portuguese", true);          // new answers
//            Answer ans406 = new Answer("Spanish", false);
//            Answer ans407 = new Answer("English", false);
//            Answer ans408 = new Answer("French", false);
//            session.save(ans405);
//            session.save(ans406);
//            session.save(ans407);
//            session.save(ans408);
//            session.flush();
//            Answer ans409 = new Answer("Southwest Asia", false);          // new answers
//            Answer ans410 = new Answer("Sub-Saharan Africa", false);
//            Answer ans411 = new Answer("Pacific Islands", false);
//            Answer ans412 = new Answer("Arctic and Subarctic", true);
//            session.save(ans409);
//            session.save(ans410);
//            session.save(ans411);
//            session.save(ans412);
//            session.flush();
//            Answer ans413 = new Answer("English", true);          // new answers
//            Answer ans414 = new Answer("Mandarin Chinese", false);
//            Answer ans415 = new Answer("Spanish", false);
//            Answer ans416 = new Answer("French", false);
//            session.save(ans413);
//            session.save(ans414);
//            session.save(ans415);
//            session.save(ans416);
//            session.flush();
//            Answer ans417 = new Answer("Migration", true);          // new answers
//            Answer ans418 = new Answer("Urban sprawl", false);
//            Answer ans419 = new Answer("Industrialization", false);
//            Answer ans420 = new Answer("Suburbanization", false);
//            session.save(ans417);
//            session.save(ans418);
//            session.save(ans419);
//            session.save(ans420);
//            session.flush();
//            Answer ans421 = new Answer("Ethnicity", false);          // new answers
//            Answer ans422 = new Answer("Nation", true);
//            Answer ans423 = new Answer("Society", false);
//            Answer ans424 = new Answer("Culture", false);
//            session.save(ans421);
//            session.save(ans422);
//            session.save(ans423);
//            session.save(ans424);
//            session.flush();
//            Answer ans425 = new Answer("Los Angeles", false);          // new answers
//            Answer ans426 = new Answer("Chicago", false);
//            Answer ans427 = new Answer("New York City", true);
//            Answer ans428 = new Answer("San Francisco", false);
//            session.save(ans425);
//            session.save(ans426);
//            session.save(ans427);
//            session.save(ans428);
//            session.flush();
//            Answer ans429 = new Answer("Zoning", true);          // new answers
//            Answer ans430 = new Answer("Gentrification", false);
//            Answer ans431 = new Answer("Urbanization", false);
//            Answer ans432 = new Answer("Segregation", false);
//            session.save(ans429);
//            session.save(ans430);
//            session.save(ans431);
//            session.save(ans432);
//
//
//            Question q1 = new Question("How many players are on the pitch in football game in one team?", ans1, ans2, ans3, ans4,sports, football);// new questions
//            session.save(q1);
//            session.flush();
//            q1.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q1);
//            session.flush();
//            Question q2 = new Question("Who is the best player in the history?", ans5, ans6, ans7, ans8, sports, football);// new questions
//            session.save(q2);
//            session.flush();
//            q2.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q2);
//            session.flush();
//            Question q3 = new Question("who is the current champions league winner?", ans9, ans10, ans11, ans12, sports, football);// new questions
//            session.save(q3);
//            session.flush();
//            q3.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q3);
//            session.flush();
//            Question q4 = new Question("what is the position \"rb\"?", ans13, ans14, ans15, ans16, sports, football);// new questions
//            session.save(q4);
//            session.flush();
//            q4.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q4);
//            session.flush();
//            Question q5 = new Question("How many players are on the pitch in basketball game in one team?", ans17, ans18, ans19, ans20, sports, basketball);// new questions
//            session.save(q5);
//            session.flush();
//            q5.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q5);
//            session.flush();
//            Question q6 = new Question("who is the best player in the history?", ans21, ans22, ans23, ans24, sports, basketball);// new questions
//            session.save(q6);
//            session.flush();
//            q6.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q6);
//            session.flush();
//            Question q7 = new Question("who is the current NBA league winner?", ans25, ans26, ans27, ans28, sports, basketball);// new questions
//            session.save(q7);
//            session.flush();
//            q7.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q7);
//            session.flush();
//            Question q8 = new Question("What is the position \"PF\"?", ans29, ans30, ans31, ans32, sports, basketball);// new questions
//            session.save(q8);
//            session.flush();
//            q8.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q8);
//            session.flush();
//            Question q9 = new Question("How many players are on the pitch in football game in one team?", ans33, ans34, ans35, ans36, sports, volleyball);// new questions
//            session.save(q9);
//            session.flush();
//            q9.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q9);
//            session.flush();
//            Question q10 = new Question("Can you hold the ball during volleyball  game??", ans37, ans38, ans39, ans40, sports, volleyball);// new questions
//            session.save(q10);
//            session.flush();
//            q10.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q10);
//            session.flush();
//            Question q11 = new Question("How many points team need to earn to win a set??", ans41, ans42, ans43, ans44, sports, volleyball);// new questions
//            session.save(q11);
//            session.flush();
//            q11.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q11);
//            session.flush();
//            Question q12 = new Question("In case of tie how many points need team to earn in fifth set??", ans45, ans46, ans47, ans48, sports, volleyball);// new questions
//            session.save(q12);
//            session.flush();
//            q12.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q12);
//            session.flush();
//            Question q13 = new Question("What is the main component responsible for generating electricity in a solar panel?", ans49, ans50, ans51, ans52, physics, Introductory_To_Physics);// new questions
//            session.save(q13);
//            session.flush();
//            q13.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q13);
//            session.flush();
//            Question q14 = new Question("What is the SI unit of electric current?", ans53, ans54, ans55, ans56, physics, Introductory_To_Physics);// new questions
//            session.save(q14);
//            session.flush();
//            q14.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q14);
//            session.flush();
//            Question q15 = new Question("Which of the following is an example of a chemical change?", ans57, ans58, ans59, ans60, physics, Introductory_To_Physics);// new questions
//            session.save(q15);
//            session.flush();
//            q15.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q15);
//            session.flush();
//            Question q16 = new Question("What is the process by which a gas changes directly into a solid without passing through the liquid state called?", ans61, ans62, ans63, ans64, physics, Introductory_To_Physics);// new questions
//            session.save(q16);
//            session.flush();
//            q16.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q16);
//            session.flush();
//            Question q17 = new Question("What is the phenomenon that causes light to change direction when passing from one medium to another?", ans65, ans66, ans67, ans68, physics, Optics);// new questions
//            session.save(q17);
//            session.flush();
//            q17.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q17);
//            session.flush();
//            Question q18 = new Question("What type of lens is thicker at the center and thinner at the edges?", ans69, ans70, ans71, ans72, physics, Optics);// new questions
//            session.save(q18);
//            session.flush();
//            q18.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q18);
//            session.flush();
//            Question q19 = new Question("What type of mirror has a reflecting surface that curves inward, causing light rays to converge?", ans73, ans74, ans75, ans76, physics, Optics);// new questions
//            session.save(q19);
//            session.flush();
//            q19.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q19);
//            session.flush();
//            Question q20 = new Question("Which color of light has the longest wavelength?", ans77, ans78, ans79, ans80, physics, Optics);// new questions
//            session.save(q20);
//            session.flush();
//            q20.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q20);
//            session.flush();
//            Question q21 = new Question("What is the SI unit of electric charge?", ans81, ans82, ans83, ans84, physics, Electricity_Magnetism_physics);// new questions
//            session.save(q21);
//            session.flush();
//            q21.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q21);
//            session.flush();
//            Question q22 = new Question("What is the relationship between current, voltage, and resistance in an electrical circuit?", ans85, ans86, ans87, ans88, physics, Electricity_Magnetism_physics);// new questions
//            session.save(q22);
//            session.flush();
//            q22.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q22);
//            session.flush();
//            Question q23 = new Question("What causes the phenomenon of electromagnetic induction??", ans89, ans90, ans91, ans92, physics, Electricity_Magnetism_physics);// new questions
//            session.save(q23);
//            session.flush();
//            q23.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q23);
//            session.flush();
//            Question q24 = new Question("Which of the following materials is a good electrical insulator?", ans93, ans94, ans95, ans96, physics, Electricity_Magnetism_physics);// new questions
//            session.save(q24);
//            session.flush();
//            q24.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q24);
//            session.flush();
//            Question q25 = new Question("Which data structure follows the \"First-In-First-Out\" (FIFO) principal?", ans97, ans98, ans99, ans100, computer_science, Algorithms_Data_Structures);// new questions
//            session.save(q25);
//            session.flush();
//            q25.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q25);
//            session.flush();
//            Question q26 = new Question("What is the time complexity of searching for an element in a balanced binary search tree (BST)?", ans101, ans102, ans103, ans104, computer_science, Algorithms_Data_Structures);// new questions
//            session.save(q26);
//            session.flush();
//            q26.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q26);
//            session.flush();
//            Question q27 = new Question("Which sorting algorithm has a worst-case time complexity of O(n^2)?", ans105, ans106, ans107, ans108, computer_science, Algorithms_Data_Structures);// new questions
//            session.save(q27);
//            session.flush();
//            q27.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q27);
//            session.flush();
//            Question q28= new Question("Which data structure allows efficient insertion and deletion at both ends?", ans109, ans110, ans111, ans112, computer_science, Algorithms_Data_Structures);// new questions
//            session.save(q28);
//            session.flush();
//            q28.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q28);
//            session.flush();
//            Question q29 = new Question("Which type of machine learning algorithm is suitable for predicting a continuous value, such as house prices?", ans113, ans114, ans115, ans116, computer_science,Machine_Learning);// new questions
//            session.save(q29);
//            session.flush();
//            q29.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q29);
//            session.flush();
//            Question q30 = new Question("Which evaluation metric is commonly used for binary classification problems?", ans117, ans118, ans119, ans120, computer_science, Machine_Learning);// new questions
//            session.save(q30);
//            session.flush();
//            q30.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q30);
//            session.flush();
//            Question q31 = new Question("What is the purpose of feature scaling in machine learning?", ans121, ans122, ans123, ans124, computer_science, Machine_Learning);// new questions
//            session.save(q31);
//            session.flush();
//            q31.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q31);
//            session.flush();
//            Question q32 = new Question("Which technique is used to address overfitting in machine learning models?", ans125, ans126, ans127, ans128, computer_science, Machine_Learning);// new questions
//            session.save(q32);
//            session.flush();
//            q32.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q32);
//            session.flush();
//            Question q33 = new Question("What is the purpose of version control systems (VCS) in software development?", ans129, ans130, ans131, ans132, computer_science, Software_Development);// new questions
//            session.save(q33);
//            session.flush();
//            q33.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q33);
//            session.flush();
//            Question q34 = new Question("What is the Agile software development methodology known for?", ans133, ans134, ans135, ans136, computer_science, Software_Development);// new questions
//            session.save(q34);
//            session.flush();
//            q34.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q34);
//            session.flush();
//            Question q35 = new Question("Which programming paradigm focuses on defining data structures and the operations that can be performed on them?", ans137, ans138, ans139, ans140, computer_science, Software_Development);// new questions
//            session.save(q35);
//            session.flush();
//            q35.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q35);
//            session.flush();
//            Question q36 = new Question("Which software development practice involves systematically finding and fixing code defects?", ans141, ans142, ans143, ans144, computer_science, Software_Development);// new questions
//            session.save(q36);
//            session.flush();
//            q36.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q36);
//            session.flush();
//            Question q37 = new Question("What is the determinant of a 2x2 matrix [a b; c d]?", ans145, ans146, ans147, ans148, mathematics, Linear_algebra);// new questions
//            session.save(q37);
//            session.flush();
//            q37.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q37);
//            session.flush();
//            Question q38 = new Question("What does it mean for a matrix to be invertible??", ans149, ans150, ans151, ans152, mathematics, Linear_algebra);// new questions
//            session.save(q38);
//            session.flush();
//            q38.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q38);
//            session.flush();
//            Question q39 = new Question("Which operation in linear algebra involves swapping two rows or columns of a matrix?", ans153, ans154, ans155, ans156, mathematics, Linear_algebra);// new questions
//            session.save(q39);
//            session.flush();
//            q39.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q39);
//            session.flush();
//            Question q40 = new Question("What is the dot product of two vectors a and b??", ans157, ans158, ans159, ans160, mathematics, Linear_algebra);// new questions
//            session.save(q40);
//            session.flush();
//            q40.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q40);
//            session.flush();
//            Question q41 = new Question("What is the derivative of f(x) = 3x^2 + 2x - 1 with respect to x?", ans161, ans162, ans163, ans164, mathematics, calculus1);// new questions
//            session.save(q41);
//            session.flush();
//            q41.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q41);
//            session.flush();
//            Question q42 = new Question("What is the integral of f(x) = 4x^3 - 2x^2 + 5x - 1 with respect to x?", ans165, ans166, ans167, ans168, mathematics, calculus1);// new questions
//            session.save(q42);
//            session.flush();
//            q42.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q42);
//            session.flush();
//            Question q43 = new Question("What is the limit of f(x) as x approaches infinity for the function f(x) = 3x^2 - 2x + 1?", ans169, ans170, ans171, ans172, mathematics, calculus1);// new questions
//            session.save(q43);
//            session.flush();
//            q43.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q43);
//            session.flush();
//            Question q44 = new Question("What is the derivative of sin(x) with respect to x?", ans173, ans174, ans175, ans176, mathematics, calculus1);// new questions
//            session.save(q44);
//            session.flush();
//            q44.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q44);
//            session.flush();
//            Question q45 = new Question("What is the probability of rolling an even number on a fair six-sided cube?", ans177, ans178, ans179, ans180, mathematics, probability);// new questions
//            session.save(q45);
//            session.flush();
//            q45.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q45);
//            session.flush();
//            Question q46 = new Question("Two cards are drawn successively without replacement from a standard deck of 52 cards. What is the probability of drawing a spade and then drawing a heart?", ans181, ans182, ans183, ans184, mathematics, probability);// new questions
//            session.save(q46);
//            session.flush();
//            q46.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q46);
//            session.flush();
//            Question q47 = new Question("A bag contains 5 red marbles, 3 blue marbles, and 2 green marbles. If one marble is randomly drawn from the bag, what is the probability of selecting a blue marble?", ans185, ans186, ans187, ans188, mathematics, probability);// new questions
//            session.save(q47);
//            session.flush();
//            q47.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q47);
//            session.flush();
//            Question q48 = new Question("The probability of an event A is 0.4 and the probability of an event B is 0.6. If events A and B are mutually exclusive, what is the probability of either event A or event B occurring??", ans189, ans190, ans191, ans192, mathematics, probability);// new questions
//            session.save(q48);
//            session.flush();
//            q48.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q48);
//            session.flush();
//            Question q49 = new Question("What is the process of amplifying a specific DNA sequence in the laboratory called?", ans193, ans194, ans195, ans196, chemestry, Biotechnology);// new questions
//            session.save(q49);
//            session.flush();
//            q49.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q49);
//            session.flush();
//            Question q50 = new Question("What is the role of restriction enzymes in biotechnology?", ans197, ans198, ans199, ans200, chemestry, Biotechnology);// new questions
//            session.save(q50);
//            session.flush();
//            q50.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q50);
//            session.flush();
//            Question q51 = new Question("What is the purpose of genetic engineering in biotechnology?", ans201, ans202, ans203, ans204, chemestry, Biotechnology);// new questions
//            session.save(q51);
//            session.flush();
//            q51.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q51);
//            session.flush();
//            Question q52 = new Question("What is the technique used to separate and analyze DNA fragments based on their size and charge??", ans205, ans206, ans207, ans208, chemestry, Biotechnology);// new questions
//            session.save(q52);
//            session.flush();
//            q52.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q52);
//            session.flush();
//            Question q53 = new Question("What is the term for a molecule that has the same molecular formula but different structural arrangements?", ans209, ans210, ans211, ans212, chemestry, Molecular_Chemistry);// new questions
//            session.save(q53);
//            session.flush();
//            q53.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q53);
//            session.flush();
//            Question q55 = new Question("What is the process by which a liquid substance is converted into a gaseous state?", ans217, ans218, ans219, ans220, chemestry, Molecular_Chemistry);// new questions
//            session.save(q55);
//            session.flush();
//            q55.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q55);
//            session.flush();
//            Question q56 = new Question("Which type of chemical reaction involves the combination of two or more substances to form a new compound?", ans221, ans222, ans223, ans224, chemestry, Molecular_Chemistry);// new questions
//            session.save(q56);
//            session.flush();
//            q56.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q56);
//            session.flush();
//            Question q57 = new Question("What is the Schrödinger equation used for in theoretical chemistry?", ans225, ans226, ans227, ans228, chemestry, Theoretical_Chemistry);// new questions
//            session.save(q57);
//            session.flush();
//            q57.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q57);
//            session.flush();
//            Question q58 = new Question("What is the Born-Oppenheimer approximation in theoretical chemistry?", ans229, ans230, ans231, ans232, chemestry, Theoretical_Chemistry);// new questions
//            session.save(q58);
//            session.flush();
//            q58.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q58);
//            session.flush();
//            Question q59 = new Question("Which theoretical approach is commonly used to study molecular dynamics and interactions?", ans233, ans234, ans235, ans236, chemestry, Theoretical_Chemistry);// new questions
//            session.save(q59);
//            session.flush();
//            q59.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q59);
//            session.flush();
//            Question q60 = new Question("What is the main goal of theoretical chemistry?", ans237, ans238, ans239, ans240, chemestry, Theoretical_Chemistry);// new questions
//            session.save(q60);
//            session.flush();
//            q60.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q60);
//            session.flush();
//            Question q61 = new Question("Which of the following is the largest bone in the human body?", ans241, ans242, ans243, ans244, biology, anatomy);// new questions
//            session.save(q61);
//            session.flush();
//            q61.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q61);
//            session.flush();
//            Question q62 = new Question("What is the primary function of the respiratory system?", ans245, ans246, ans247, ans248, biology, anatomy);// new questions
//            session.save(q62);
//            session.flush();
//            q62.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q62);
//            session.flush();
//            Question q63 = new Question("Which organ is responsible for filtering and removing waste products from the blood?", ans249, ans250, ans251, ans252, biology, anatomy);// new questions
//            session.save(q63);
//            session.flush();
//            q63.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q63);
//            session.flush();
//            Question q64 = new Question("Which of the following is not one of the primary types of muscle tissue in the human body?", ans253, ans254, ans255, ans256, biology, anatomy);// new questions  Question q44 = new Question("which is bigger?", ans1, ans2, ans3, ans4, astro, speeds);// new questions
//            session.save(q64);
//            session.flush();
//            q64.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q64);
//            session.flush();
//            Question q65 = new Question("What is the term for the variety of life on Earth and the interactions between living organisms and their environment?", ans257, ans258, ans259, ans260, biology, environmental_biology);// new questions
//            session.save(q65);
//            session.flush();
//            q65.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q65);
//            session.flush();
//            Question q66 = new Question("What is the primary source of energy in most ecosystems?", ans261, ans262, ans263, ans264, biology, environmental_biology);// new questions
//            session.save(q66);
//            session.flush();
//            q66.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q66);
//            session.flush();
//            Question q67 = new Question("Which of the following is a greenhouse gas that contributes to global warming?", ans265, ans266, ans267, ans268, biology, environmental_biology);// new questions
//            session.save(q67);
//            session.flush();
//            q67.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q67);
//            session.flush();
//            Question q68 = new Question("What is the process by which plants convert carbon dioxide and sunlight into glucose and oxygen?", ans269, ans270, ans271, ans272, biology, environmental_biology);// new questions
//            session.save(q68);
//            session.flush();
//            q68.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q68);
//            session.flush();
//            Question q69 = new Question("What is the study of microorganisms, including bacteria, viruses, fungi, and protozoa, called?", ans273, ans274, ans275, ans276, biology, microbiology);// new questions
//            session.save(q69);
//            session.flush();
//            q69.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q69);
//            session.flush();
//            Question q70 = new Question("Which of the following is an example of a Gram-negative bacterium?", ans277, ans278, ans279, ans280, biology, microbiology);// new questions
//            session.save(q70);
//            session.flush();
//            q70.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q70);
//            session.flush();
//            Question q71 = new Question("What is the primary function of antibiotics?", ans281, ans282, ans283, ans284, biology, microbiology);// new questions
//            session.save(q71);
//            session.flush();
//            q71.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q71);
//            session.flush();
//            Question q72 = new Question("Which of the following is a viral disease that affects the respiratory system??", ans285, ans286, ans287, ans288, biology, microbiology);// new questions
//            session.save(q72);
//            session.flush();
//            q72.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q72);
//            session.flush();
//            Question q73 = new Question("When did World War I begin?", ans289, ans290, ans291, ans292, history, world_war_1);// new questions
//            session.save(q73);
//            session.flush();
//            q73.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q73);
//            session.flush();
//            Question q74 = new Question("Which event sparked the outbreak of World War I?", ans293, ans294, ans295, ans296, history, world_war_1);// new questions
//            session.save(q74);
//            session.flush();
//            q74.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q74);
//            session.flush();
//            Question q75 = new Question("Which countries formed the alliance known as the Central Powers during World War I?", ans297, ans298, ans299, ans300, history, world_war_1);// new questions
//            session.save(q75);
//            session.flush();
//            q75.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q75);
//            session.flush();
//            Question q76 = new Question("When did World War I end?", ans301, ans302, ans303, ans304, history, world_war_1);// new questions
//            session.save(q76);
//            session.flush();
//            q76.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q76);
//            session.flush();
//            Question q77 = new Question("When did World War II begin?", ans305, ans306, ans307, ans308, history, world_war_2);// new questions
//            session.save(q77);
//            session.flush();
//            q77.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q77);
//            session.flush();
//            Question q78 = new Question("Which event is considered the immediate trigger for the start of World War II?", ans309, ans310, ans311, ans312, history, world_war_2);// new questions
//            session.save(q78);
//            session.flush();
//            q78.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q78);
//            session.flush();
//            Question q79 = new Question("Which countries formed the alliance known as the Axis Powers during World War II?", ans313, ans314, ans315, ans316, history, world_war_2);// new questions
//            session.save(q79);
//            session.flush();
//            q79.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q79);
//            session.flush();
//            Question q80 = new Question("When did World War II end in Europe?", ans317, ans318, ans319, ans320, history, world_war_2);// new questions
//            session.save(q80);
//            session.flush();
//            q80.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q80);
//            session.flush();
//            Question q81 = new Question("When did the Industrial Revolution begin?", ans321, ans322, ans323, ans324, history, The_Industrial_Revolution);// new questions
//            session.save(q81);
//            session.flush();
//            q81.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q81);
//            session.flush();
//            Question q82 = new Question("What was one of the key advancements in transportation during the Industrial Revolution?", ans325, ans326, ans327, ans328, history, The_Industrial_Revolution);// new questions
//            session.save(q82);
//            session.flush();
//            q82.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q82);
//            session.flush();
//            Question q83 = new Question("Which industry experienced significant growth and transformation during the Industrial Revolution?", ans329, ans330, ans331, ans332, history, The_Industrial_Revolution);// new questions
//            session.save(q83);
//            session.flush();
//            q83.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q83);
//            session.flush();
//            Question q84 = new Question("Where did the Industrial Revolution first take root?", ans333, ans334, ans335, ans336, history, The_Industrial_Revolution);// new questions
//            session.save(q84);
//            session.flush();
//            q84.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q84);
//            session.flush();
//            Question q85 = new Question("What is a stanza in poetry?", ans337, ans338, ans339, ans340, literature, poetry);// new questions
//            session.save(q85);
//            session.flush();
//            q85.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q85);
//            session.flush();
//            Question q86 = new Question("What is the term for the repetition of consonant sounds at the beginning of words in a line of poetry?", ans341, ans342, ans343, ans344, literature, poetry);// new questions
//            session.save(q86);
//            session.flush();
//            q86.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q86);
//            session.flush();
//            Question q87 = new Question("Which poetic device involves the use of words that imitate the sounds they represent?", ans345, ans346, ans347, ans348, literature, poetry);// new questions
//            session.save(q87);
//            session.flush();
//            q87.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q87);
//            session.flush();
//            Question q88 = new Question("What is the term for a fourteen-line poem with a specific rhyme scheme, often expressing deep emotions?", ans349, ans350, ans351, ans352, literature, poetry);// new questions
//            session.save(q88);
//            session.flush();
//            q88.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q88);
//            session.flush();
//            Question q89 = new Question("Who wrote the famous epic poem \"The Odyssey\"?", ans353, ans354, ans355, ans356, literature, classical_literature);// new questions
//            session.save(q89);
//            session.flush();
//            q89.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q89);
//            session.flush();
//            Question q90 = new Question("Which tragedy by William Shakespeare features the characters Macbeth and Lady Macbeth?", ans357, ans358, ans359, ans360, literature, classical_literature);// new questions
//            session.save(q90);
//            session.flush();
//            q90.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q90);
//            session.flush();
//            Question q91 = new Question("Who is the author of the novel \"Pride and Prejudice\"?", ans361, ans362, ans363, ans364, literature, classical_literature);// new questions
//            session.save(q91);
//            session.flush();
//            q91.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q91);
//            session.flush();
//            Question q92 = new Question("Which ancient Greek playwright wrote the famous tragedy \"Oedipus Rex\"?", ans365, ans366, ans367, ans368, literature, classical_literature);// new questions
//            session.save(q92);
//            session.flush();
//            q92.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q92);
//            session.flush();
//            Question q93 = new Question("What is the term for the sequence of events that make up a story in a work of fiction?", ans369, ans370, ans371, ans372, literature, Reading_Fiction);// new questions
//            session.save(q93);
//            session.flush();
//            q93.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q93);
//            session.flush();
//            Question q94 = new Question("What is the term for the perspective from which a story is told?", ans373, ans374, ans375, ans376, literature, Reading_Fiction);// new questions
//            session.save(q94);
//            session.flush();
//            q94.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q94);
//            session.flush();
//            Question q95 = new Question("Which literary device involves a figure of speech that makes a comparison between two unlike things using \"like\" or \"as\"?", ans377, ans378, ans379, ans380, literature, Reading_Fiction);// new questions
//            session.save(q95);
//            session.flush();
//            q95.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q95);
//            session.flush();
//            Question q96 = new Question("What is the term for the overall message or insight about life conveyed in a work of fiction?", ans381, ans382, ans383, ans384, literature, Reading_Fiction);// new questions
//            session.save(q96);
//            session.flush();
//            q96.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q96);
//            session.flush();
//            Question q97 = new Question("What is the process by which rocks are broken down into smaller fragments by physical forces such as wind, water, or ice?", ans385, ans386, ans387, ans388, geography, Physical_Geography);// new questions
//            session.save(q97);
//            session.flush();
//            q97.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q97);
//            session.flush();
//            Question q98 = new Question("What is the term for a large, slow-moving mass of ice formed from compacted snow in areas where snowfall exceeds melting?", ans389, ans390, ans391, ans392, geography, Physical_Geography);// new questions
//            session.save(q98);
//            session.flush();
//            q98.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q98);
//            session.flush();
//            Question q99 = new Question("What is the term for the boundary between two tectonic plates where they are moving apart from each other?", ans393, ans394, ans395, ans396, geography, Physical_Geography);// new questions
//            session.save(q99);
//            session.flush();
//            q99.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q99);
//            session.flush();
//            Question q100 = new Question("What is the term for the natural phenomenon characterized by the periodic warming of the surface waters in the eastern Pacific Ocean?", ans397, ans398, ans399, ans400, geography, Physical_Geography);// new questions
//            session.save(q100);
//            session.flush();
//            q100.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q100);
//            session.flush();
//            Question q101 = new Question("Which language family does Mandarin Chinese belong to?", ans401, ans402, ans403, ans404, geography, language_geography);// new questions
//            session.save(q101);
//            session.flush();
//            q101.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q101);
//            session.flush();
//            Question q102 = new Question("Which language is predominantly spoken in Brazil?", ans405, ans406, ans407, ans408, geography, language_geography);// new questions
//            session.save(q102);
//            session.flush();
//            q102.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q102);
//            session.flush();
//            Question q103 = new Question("Which region is known for the linguistic diversity of Indigenous languages in North America?", ans409, ans410, ans411, ans412, geography, language_geography);// new questions
//            session.save(q103);
//            session.flush();
//            q103.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q103);
//            session.flush();
//            Question q104 = new Question("Which language is the most widely spoken as a second language in the world?", ans413, ans414, ans415, ans416, geography, language_geography);// new questions
//            session.save(q104);
//            session.flush();
//            q104.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q104);
//            session.flush();
//            Question q105 = new Question("What is the term for the movement of people from rural areas to cities, resulting in urbanization?", ans417, ans418, ans419, ans420, geography, Human_Geography);// new questions
//            session.save(q105);
//            session.flush();
//            q105.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q105);
//            session.flush();
//            Question q106 = new Question("What is the term for a group of people with shared cultural characteristics and a sense of unity?", ans421, ans422, ans423, ans424, geography, Human_Geography);// new questions
//            session.save(q106);
//            session.flush();
//            q106.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q106);
//            session.flush();
//            Question q107 = new Question("Which city is considered the financial capital of the United States and has a significant global influence?", ans425, ans426, ans427, ans428, geography, Human_Geography);// new questions
//            session.save(q107);
//            session.flush();
//            q107.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q107);
//            session.flush();
//            Question q108 = new Question("What is the term for the division of a city into different zones for specific land uses, such as residential, commercial, and industrial areas?", ans429, ans430, ans431, ans432, geography, Human_Geography);// new questions
//            session.save(q108);
//            session.flush();
//            q108.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q108);
//            session.flush();
//
//            Teacher t1 = new Teacher("Malki", "Malki_password", true);       // new teacher
//            t1.addSubject(computer_science);
//            session.save(t1);
//            session.flush();
//
//            Teacher t2 = new Teacher("Avi Cohen", "avi", true);       // new teacher
//            t2.addSubject(sports);
//            session.save(t2);
//            session.flush();
//
//            Teacher t3 = new Teacher("Snel Lechtman", "Boris_password", true);       // new teacher
//            t3.addSubject(physics);
//            session.save(t3);
//            session.flush();
//
//            Teacher t4 = new Teacher("Amir Smith", "Amir_password", true);       // new teacher
//            t4.addSubject(mathematics);
//            session.save(t4);
//            session.flush();
//
//            Teacher t5 = new Teacher("Roey Catz", "Roey_password", true);       // new teacher
//            t5.addSubject(biology);
//            session.save(t5);
//            session.flush();
//
//            Teacher t6 = new Teacher("Noam Levi", "Noam_password", true);       // new teacher
//            t6.addSubject(chemestry);
//            session.save(t6);
//            session.flush();
//
//            Teacher t7 = new Teacher("Roti Menashe", "Roti_password", true);       // new teacher
//            t7.addSubject(history);
//            session.save(t7);
//            session.flush();
//
//            Teacher t8 = new Teacher("Ben Mizrahi", "Ben_password", true);       // new teacher
//            t8.addSubject(literature);
//            session.save(t8);
//            session.flush();
//
//            Teacher t9 = new Teacher("Amnon Harel", "Amnon_password", true);       // new teacher
//            t9.addSubject(geography);
//            session.save(t9);
//            session.flush();
//
//
//            Exam ex1 = new Exam("mid term football exam", football, 90, "hello students!", "hello teacher!", t2);
//            session.save(ex1);
//            session.flush();
//            ex1.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex1.addQuestion(q1, 70));
//            session.save(ex1.addQuestion(q2, 30));
//            session.save(ex1);
//            session.flush();
//
//
//            Exam ex2 = new Exam("Final exam football", football, 90, "hello students!", "hello teacher!", t2);
//            session.save(ex2);
//            session.flush();
//            ex2.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex2.addQuestion(q3, 70));
//            session.save(ex2.addQuestion(q4, 30));
//            session.save(ex2);
//            session.flush();
//
//
//            Exam ex3 = new Exam("Mid term exam basketball", basketball, 90, "hello students!", "hello teacher!", t2);
//            session.save(ex3);
//            session.flush();
//            ex3.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex3.addQuestion(q5, 70));
//            session.save(ex3.addQuestion(q6, 30));
//            session.save(ex3);
//            session.flush();
//
//
//            Exam ex4 = new Exam("Sem A final basketball", basketball, 90, "hello students!", "hello teacher!", t2);
//            session.save(ex4);
//            session.flush();
//            ex4.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex4.addQuestion(q7, 70));
//            session.save(ex4.addQuestion(q8, 30));
//            session.save(ex4);
//            session.flush();
//
//            Exam ex5 = new Exam("Volleball prinicipals", volleyball, 90, "hello students!", "hello teacher!", t2);
//            session.save(ex5);
//            session.flush();
//            ex5.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex5.addQuestion(q9, 85));
//            session.save(ex5.addQuestion(q10, 15));
//            session.save(ex5);
//            session.flush();
//
//            Exam ex6 = new Exam("Volleball summary", volleyball, 90, "hello students!", "hello teacher!", t2);
//            session.save(ex6);
//            session.flush();
//            ex6.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex6.addQuestion(q10, 50));
//            session.save(ex6.addQuestion(q11, 20));
//            session.save(ex6.addQuestion(q12, 30));
//            session.save(ex6);
//            session.flush();
//
//            Exam ex7 = new Exam("Sem A physics", Introductory_To_Physics, 90, "hello students!", "hello teacher!", t3);
//            session.save(ex7);
//            session.flush();
//            ex7.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex7.addQuestion(q13, 50));
//            session.save(ex7.addQuestion(q14, 50));
//            session.save(ex7);
//            session.flush();
//
//
//            Exam ex8 = new Exam("Final Intro to Physics", Introductory_To_Physics, 90, "hello students!", "hello teacher!", t3);
//            session.save(ex8);
//            session.flush();
//            ex8.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex8.addQuestion(q15, 50));
//            session.save(ex8.addQuestion(q16, 20));
//            session.save(ex8.addQuestion(q14, 30));
//            session.save(ex8);
//            session.flush();
//
//
//
//            Exam ex9 = new Exam("Optics Sem A mid exam", Optics, 90, "hello students!", "hello teacher!", t3);
//            session.save(ex9);
//            session.flush();
//            ex9.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex9.addQuestion(q17, 50));
//            session.save(ex9.addQuestion(q18, 50));
//            session.save(ex9);
//            session.flush();
//
//
//
//            Exam ex10 = new Exam("Optics Final", Optics, 90, "hello students!", "hello teacher!", t3);
//            session.save(ex10);
//            session.flush();
//            ex10.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex10.addQuestion(q19, 50));
//            session.save(ex10.addQuestion(q20, 50));
//            session.save(ex10);
//            session.flush();
//
//
//
//            Exam ex11 = new Exam("Final Electricity&Magnetism", Electricity_Magnetism_physics, 90, "hello students!", "hello teacher!", t3);
//            session.save(ex11);
//            session.flush();
//            ex11.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex11.addQuestion(q21, 30));
//            session.save(ex11.addQuestion(q22, 20));
//            session.save(ex11.addQuestion(q23, 30));
//            session.save(ex11.addQuestion(q24, 20));
//            session.save(ex11);
//            session.flush();
//
//
//
//
//            Exam ex12 = new Exam("Final Algorithms", Algorithms_Data_Structures, 90, "hello students!", "hello teacher!", t1);
//            session.save(ex12);
//            session.flush();
//            ex12.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex12.addQuestion(q25, 50));
//            session.save(ex12.addQuestion(q26, 20));
//            session.save(ex12.addQuestion(q27, 10));
//            session.save(ex12.addQuestion(q28, 20));
//            session.save(ex12);
//            session.flush();
//
//
//
//
//            Exam ex13 = new Exam("ML Final Exam Sem A", Machine_Learning, 90, "hello students!", "hello teacher!", t1);
//            session.save(ex13);
//            session.flush();
//            ex13.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex13.addQuestion(q29, 50));
//            session.save(ex13.addQuestion(q30, 20));
//            session.save(ex13.addQuestion(q31, 15));
//            session.save(ex13.addQuestion(q32, 15));
//            session.save(ex13);
//            session.flush();
//
//            Exam ex14 = new Exam("Software_Development Final Exam", Software_Development, 90, "hello students!", "hello teacher!", t1);
//            session.save(ex14);
//            session.flush();
//            ex14.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex14.addQuestion(q33, 50));
//            session.save(ex14.addQuestion(q34, 20));
//            session.save(ex14.addQuestion(q35, 15));
//            session.save(ex14.addQuestion(q36, 15));
//            session.save(ex14);
//            session.flush();
//
//
//            Exam ex15 = new Exam("Linear Algebra", Linear_algebra, 90, "hello students!", "hello teacher!", t4);
//            session.save(ex15);
//            session.flush();
//            ex15.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex15.addQuestion(q37, 50));
//            session.save(ex15.addQuestion(q38, 20));
//            session.save(ex15.addQuestion(q39, 15));
//            session.save(ex15.addQuestion(q40, 15));
//            session.save(ex15);
//            session.flush();
//
//
//            Exam ex16 = new Exam("Calculus I Final", calculus1, 90, "hello students!", "hello teacher!", t4);
//            session.save(ex16);
//            session.flush();
//            ex16.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex16.addQuestion(q41, 50));
//            session.save(ex16.addQuestion(q42, 20));
//            session.save(ex16.addQuestion(q43, 15));
//            session.save(ex16.addQuestion(q44, 15));
//            session.save(ex16);
//            session.flush();
//
//
//
//            Exam ex17 = new Exam("Probability Final Exam Sem A", probability, 90, "hello students!", "hello teacher!", t4);
//            session.save(ex17);
//            session.flush();
//            ex17.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex17.addQuestion(q45, 50));
//            session.save(ex17.addQuestion(q46, 20));
//            session.save(ex17.addQuestion(q47, 15));
//            session.save(ex17.addQuestion(q48, 15));
//            session.save(ex17);
//            session.flush();
//
//
//            Exam ex18 = new Exam("Biotechnology Final Exam", Biotechnology, 90, "hello students!", "hello teacher!", t6);
//            session.save(ex18);
//            session.flush();
//            ex18.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex18.addQuestion(q49, 50));
//            session.save(ex18.addQuestion(q50, 20));
//            session.save(ex18.addQuestion(q51, 15));
//            session.save(ex18.addQuestion(q52, 15));
//            session.save(ex18);
//            session.flush();
//
//
//            Exam ex19 = new Exam("Molecular Chemistry Final",Molecular_Chemistry, 90, "hello students!", "hello teacher!", t6);
//            session.save(ex19);
//            session.flush();
//            ex19.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex19.addQuestion(q53, 50));
//            session.save(ex19.addQuestion(q55, 35));
//            session.save(ex19.addQuestion(q56, 15));
//            session.save(ex19);
//            session.flush();
//
//            Exam ex20 = new Exam("Theoretical Chemistry Final Exam Sem A", Theoretical_Chemistry, 90, "hello students!", "hello teacher!", t6);
//            session.save(ex20);
//            session.flush();
//            ex20.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex20.addQuestion(q57, 60));
//            session.save(ex20.addQuestion(q58, 10));
//            session.save(ex20.addQuestion(q59, 15));
//            session.save(ex20.addQuestion(q60, 15));
//            session.save(ex13);
//            session.flush();
//
//
//            Exam ex21 = new Exam("Anatomy Final Exam Sem A",anatomy, 90, "hello students!", "hello teacher!", t5);
//            session.save(ex21);
//            session.flush();
//            ex21.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex21.addQuestion(q61, 50));
//            session.save(ex21.addQuestion(q62, 20));
//            session.save(ex21.addQuestion(q63, 15));
//            session.save(ex21.addQuestion(q64, 15));
//            session.save(ex21);
//            session.flush();
//
//            Exam ex22 = new Exam("Environmental Biology Final", environmental_biology, 90, "hello students!", "hello teacher!", t5);
//            session.save(ex22);
//            session.flush();
//            ex22.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex22.addQuestion(q65, 50));
//            session.save(ex22.addQuestion(q66, 20));
//            session.save(ex22.addQuestion(q67, 15));
//            session.save(ex22.addQuestion(q68, 15));
//            session.save(ex22);
//            session.flush();
//
//            Exam ex23 = new Exam("Microbiology Final Exam Sem A", microbiology, 90, "hello students!", "hello teacher!", t5);
//            session.save(ex23);
//            session.flush();
//            ex23.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex23.addQuestion(q69, 50));
//            session.save(ex23.addQuestion(q70, 20));
//            session.save(ex23.addQuestion(q71, 15));
//            session.save(ex23.addQuestion(q72, 15));
//            session.save(ex23);
//            session.flush();
//
//            Exam ex24 = new Exam("history Exam Sem A", world_war_1, 90, "hello students!", "hello teacher!", t7);
//            session.save(ex24);
//            session.flush();
//            ex24.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex24.addQuestion(q73, 50));
//            session.save(ex24.addQuestion(q74, 20));
//            session.save(ex24.addQuestion(q75, 15));
//            session.save(ex24.addQuestion(q76, 15));
//            session.save(ex24);
//            session.flush();
//
//            Exam ex25 = new Exam("world_war_2 Exam Sem A", world_war_2, 90, "hello students!", "hello teacher!", t7);
//            session.save(ex25);
//            session.flush();
//            ex25.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex25.addQuestion(q77, 50));
//            session.save(ex25.addQuestion(q78, 20));
//            session.save(ex25.addQuestion(q79, 15));
//            session.save(ex25.addQuestion(q80, 15));
//            session.save(ex25);
//            session.flush();
//
//            Exam ex26 = new Exam("The IndustrialRevolution Final Exam Sem A", The_Industrial_Revolution, 90, "hello students!", "hello teacher!", t7);
//            session.save(ex26);
//            session.flush();
//            ex26.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex26.addQuestion(q81, 50));
//            session.save(ex26.addQuestion(q82, 20));
//            session.save(ex26.addQuestion(q83, 15));
//            session.save(ex26.addQuestion(q84, 15));
//            session.save(ex26);
//            session.flush();
//
//            Exam ex27 = new Exam("Poetry Final Exam Sem B",poetry, 90, "hello students!", "hello teacher!", t8);
//            session.save(ex27);
//            session.flush();
//            ex27.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex27.addQuestion(q85, 50));
//            session.save(ex27.addQuestion(q86, 20));
//            session.save(ex27.addQuestion(q87, 15));
//            session.save(ex27.addQuestion(q88, 15));
//            session.save(ex27);
//            session.flush();
//
//            Exam ex28 = new Exam("Classical Literature Final ", classical_literature, 90, "hello students!", "hello teacher!", t8);
//            session.save(ex28);
//            session.flush();
//            ex28.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex28.addQuestion(q89, 50));
//            session.save(ex28.addQuestion(q90, 20));
//            session.save(ex28.addQuestion(q91, 15));
//            session.save(ex28.addQuestion(q92, 15));
//            session.save(ex28);
//            session.flush();
//
//            Exam ex29 = new Exam("Reading Fiction Final Exam Sem B", Reading_Fiction, 90, "hello students!", "hello teacher!", t8);
//            session.save(ex29);
//            session.flush();
//            ex29.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex29.addQuestion(q93, 50));
//            session.save(ex29.addQuestion(q94, 20));
//            session.save(ex29.addQuestion(q95, 15));
//            session.save(ex29.addQuestion(q96, 15));
//            session.save(ex29);
//            session.flush();
//
//            Exam ex30 = new Exam("Physical Geography Exam Sem B", Physical_Geography, 90, "hello students!", "hello teacher!", t9);
//            session.save(ex30);
//            session.flush();
//            ex30.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex30.addQuestion(q97, 50));
//            session.save(ex30.addQuestion(q98, 20));
//            session.save(ex30.addQuestion(q99, 15));
//            session.save(ex30.addQuestion(q100, 15));
//            session.save(ex30);
//            session.flush();
//
//            Exam ex31 = new Exam("Human Geography Exam Sem B", Human_Geography, 90, "hello students!", "hello teacher!", t9);
//            session.save(ex31);
//            session.flush();
//            ex31.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex31.addQuestion(q101, 50));
//            session.save(ex31.addQuestion(q102, 20));
//            session.save(ex31.addQuestion(q103, 15));
//            session.save(ex31.addQuestion(q104, 15));
//            session.save(ex31);
//            session.flush();
//
//            Exam ex32 = new Exam("Language Geography Exam Sem B", language_geography, 90, "hello students!", "hello teacher!", t9);
//            session.save(ex32);
//            session.flush();
//            ex32.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            session.save(ex32.addQuestion(q105, 50));
//            session.save(ex32.addQuestion(q106, 20));
//            session.save(ex32.addQuestion(q107, 15));
//            session.save(ex32.addQuestion(q108, 15));
//            session.save(ex32);
//            session.flush();
//
//
//            Pupil p1 = new Pupil("Alon", "213468951", "Michael_password", false); // new pupil
//            session.save(p1);
//            session.flush();
//            Pupil p2 = new Pupil("David", "223468951", "David_password", false); // new pupil
//            session.save(p2);
//            session.flush();
//            Pupil p3 = new Pupil("Lian", "213498351", "Lian_password", false); // new pupil
//            session.save(p3);
//            session.flush();
//            Pupil p4 = new Pupil("Noa", "134468951", "Noa_password", false); // new pupil
//            session.save(p4);
//            session.flush();
//            Pupil p5 = new Pupil("Noga", "313448651", "Noga_password", false); // new pupil
//            session.save(p5);
//            session.flush();
//            Pupil p6 = new Pupil("Ariel", "323364951", "Ariel_password", false); // new pupil
//            session.save(p6);
//            session.flush();
//            Pupil p7 = new Pupil("Moli", "214498991", "Moli_password", false); // new pupil
//            session.save(p7);
//            session.flush();
//            Pupil p8 = new Pupil("Aviv", "273477951", "Aviv_password", false); // new pupil
//            session.save(p8);
//            session.flush();
//            Pupil p9 = new Pupil("Oliver", "263466861", "Oliver_password", false); // new pupil
//            session.save(p9);
//            session.flush();
//            Pupil p10 = new Pupil("Neria", "214468441", "Neria_password", false); // new pupil
//            session.save(p10);
//            session.flush();
//            Pupil p11 = new Pupil("Amnon", "273468554", "Amnon_password", false); // new pupil
//            session.save(p11);
//            session.flush();
//            Pupil p12 = new Pupil("Elia", "253458951", "Elia_password", false); // new pupil
//            session.save(p12);
//            session.flush();
//            Pupil p13 = new Pupil("Dor", "273767957", "Dor_password", false); // new pupil
//            session.save(p13);
//            session.flush();
//            Pupil p14 = new Pupil("Dorit", "233466951", "Dorit_password", false); // new pupil
//            session.save(p14);
//            session.flush();
//            Pupil p15 = new Pupil("Elena", "243667981", "Elena_password", false); // new pupil
//            session.save(p15);
//            session.flush();
//            Pupil p16 = new Pupil("Alona", "213666661", "Alona_password", false); // new pupil
//            session.save(p16);
//            session.flush();
//            Pupil p17 = new Pupil("Raviv", "215465951", "Raviv_password", false); // new pupil
//            session.save(p17);
//            session.flush();
//            Pupil p18 = new Pupil("Nicholas", "213468998", "Nicholas_password", false); // new pupil
//            session.save(p18);
//            session.flush();
//            Pupil p19 = new Pupil("Avner", "245468657", "Avner_password", false); // new pupil
//            session.save(p19);
//            session.flush();
//            Pupil p20 = new Pupil("Shay", "263468891", "Shay_password", false); // new pupil
//            session.save(p20);
//            session.flush();
//            Pupil p21 = new Pupil("Steve", "288468651", "Steve_password", false); // new pupil
//            session.save(p21);
//            session.flush();
//            Pupil p22 = new Pupil("Stav", "293068051", "Stav_password", false); // new pupil
//            session.save(p22);
//            session.flush();
//            Pupil p23 = new Pupil("Ori", "210408051", "Ori_password", false); // new pupil
//            session.save(p23);
//            session.flush();
//            Pupil p24 = new Pupil("Boris", "203068900", "Boris_password", false); // new pupil
//            session.save(p24);
//            session.flush();
//            Pupil p25 = new Pupil("Ohad", "273488059", "Ohad_password", false); // new pupil
//            session.save(p25);
//            session.flush();
//            Pupil p26 = new Pupil("Gilda", "163808961", "Gilda_password", false); // new pupil
//            session.save(p26);
//            session.flush();
//            Pupil p27 = new Pupil("Moria", "234898921", "Moria_password", false); // new pupil
//            session.save(p27);
//            session.flush();
//            Pupil p28 = new Pupil("Daniel", "213234251", "Daniel_password", false); // new pupil
//            session.save(p28);
//            session.flush();
//            Pupil p29 = new Pupil("Daniela", "253332251", "Daniela_password", false); // new pupil
//            session.save(p29);
//            session.flush();
//            Pupil p30= new Pupil("Guy", "213468252", "Guy_password", false); // new pupil
//            session.save(p30);
//            session.flush();
//            Pupil p31 = new Pupil("Debi", "313468953", "Debi_password", false); // new pupil
//            session.save(p31);
//            session.flush();
//            Pupil p32 = new Pupil("Dvora", "215466951", "Dvora_password", false); // new pupil
//            session.save(p32);
//            session.flush();
//            Pupil p33 = new Pupil("Dikla", "213467771", "Dikla_password", false); // new pupil
//            session.save(p33);
//            session.flush();
//            Pupil p34 = new Pupil("Maria", "285448971", "Maria_password", false); // new pupil
//            session.save(p34);
//            session.flush();
//            Pupil p35 = new Pupil("Meni", "267098951", "Meni_password", false); // new pupil
//            session.save(p35);
//            session.flush();
//
//
//            ReadyExam r1 = new ReadyExam(ex1, "10a4", true, "14/6/2023 13:30");  // new "Out of the drawer" exam
//            session.save(r1);
//            r1.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct.add(q1);                                          // p1 answered only question q1 correctly
//            Grade gr2 = new Grade(/*98, */r1, p2, correct, "Michael, nice exam!"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr2);
//            session.flush();
//            Grade gr3 = new Grade(/*98, */r1, p3, correct, "Wow"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr3);
//            session.flush();
//            Grade gr4 = new Grade(/*98, */r1, p4, correct, "AMAZING"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr4);
//            session.flush();
//            Grade gr5 = new Grade(/*98, */r1, p5, correct, "Great improvment"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr5);
//            session.flush();
//            Grade gr6 = new Grade(/*98, */r1, p6, correct, "You can do better"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr6);
//            session.flush();
//
//            ReadyExam r2 = new ReadyExam(ex2, "1va2", true, "14/6/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r2);
//            r2.setActual_solving_time(130); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_1 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_1.add(q4);
//            Grade gr7 = new Grade(/*98, */r2, p5, correct_1, "Expected more from you"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr7);
//            session.flush();
//            Grade gr8 = new Grade(/*98, */r2, p6, correct_1, "Not enough"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr8);
//            session.flush();
//            Grade gr9 = new Grade(/*98, */r2, p7, correct_1, "Opposite smiley face"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr9);
//            session.flush();
//            Grade gr10 = new Grade(/*98, */r2, p8, correct_1, "You can do better"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr10);
//            session.flush();
//
//            ReadyExam r3 = new ReadyExam(ex3, "1mkl", true, "14/6/2023 17:30");  // new "Out of the drawer" exam
//            session.save(r3);
//            r3.setActual_solving_time(130); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_2 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_2.add(q5);
//            Grade gr11 = new Grade(/*98, */r3, p9, correct_1, "Great"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr11);
//            session.flush();
//            Grade gr12 = new Grade(/*98, */r3, p10, correct_1, ""); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr12);
//            session.flush();
//            Grade gr13 = new Grade(/*98, */r3, p11, correct_1, "More next time"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr13);
//            session.flush();
//            Grade gr14 = new Grade(/*98, */r3, p12, correct_1, ""); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr14);
//            session.flush();
//
//            ReadyExam r4 = new ReadyExam(ex4, "72ag", true, "15/6/2023 13:30");  // new "Out of the drawer" exam
//            session.save(r4);
//            r4.setActual_solving_time(130); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_3 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_3.add(q8);
//            Grade gr15 = new Grade(/*98, */r4, p5, correct_3, "Not bad from you"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr15);
//            session.flush();
//            Grade gr16 = new Grade(/*98, */r4, p8, correct_3, ""); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr16);
//            session.flush();
//            Grade gr17 = new Grade(/*98, */r4, p14, correct_3, "Need to practice more"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr17);
//            session.flush();
//            Grade gr18 = new Grade(/*98, */r4, p15, correct_3, ""); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr18);
//            session.flush();
//            Grade gr19 = new Grade(/*98, */r4, p22, correct_3, ""); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr19);
//            session.flush();
//            Grade gr20 = new Grade(/*98, */r4, p20, correct_3, ""); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr20);
//            session.flush();
//            Grade gr21 = new Grade(/*98, */r4, p21, correct_3, ""); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr21);
//            session.flush();
//
//
//            ReadyExam r5 = new ReadyExam(ex5, "4lkq", true, "14/6/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r5);
//            r5.setActual_solving_time(100); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_4 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_4.add(q9);
//            Grade gr22 = new Grade(/*98, */r5, p9, correct_4, "Amazing"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr22);
//            session.flush();
//            Grade gr23 = new Grade(/*98, */r5, p13, correct_4, "Amazing"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr23);
//            session.flush();
//            Grade gr24 = new Grade(/*98, */r5, p4, correct_4, "Amazing"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr24);
//            session.flush();
//            Grade gr25 = new Grade(/*98, */r5, p10, correct_4, "Amazing"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr25);
//            session.flush();
//            Grade gr26 = new Grade(/*98, */r5, p16, correct_4, "Amazing"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr26);
//            session.flush();
//            Grade gr27 = new Grade(/*98, */r5, p12, correct_4, "Amazing"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr27);
//            session.flush();
//            Grade gr28 = new Grade(/*98, */r5, p17, correct_4, "Amazing"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr28);
//            session.flush();
//
//
//            ReadyExam r6 = new ReadyExam(ex6, "qr3n", true, "15/6/2023 17:30");  // new "Out of the drawer" exam
//            session.save(r6);
//            r6.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_5 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_5.add(q10);
//            correct_5.add(q12);
//            Grade gr29 = new Grade(/*98, */r6, p11, correct_5, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr29);
//            session.flush();
//            Grade gr30 = new Grade(/*98, */r6, p15, correct_5, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr30);
//            session.flush();
//            Grade gr31 = new Grade(/*98, */r6, p18, correct_5, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr31);
//            session.flush();
//            Grade gr32 = new Grade(/*98, */r6, p20, correct_5, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr32);
//            session.flush();
//            Grade gr33 = new Grade(/*98, */r6, p22, correct_5, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr33);
//            session.flush();
//            Grade gr34 = new Grade(/*98, */r6, p23, correct_5, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr34);
//            session.flush();
//            Grade gr35 = new Grade(/*98, */r6, p19, correct_5, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr35);
//            session.flush();
//            Grade gr36 = new Grade(/*98, */r6, p24, correct_5, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr36);
//            session.flush();
//
//
//            ReadyExam r7 = new ReadyExam(ex7, "123m", true, "16/6/2023 13:30");  // new "Out of the drawer" exam
//            session.save(r7);
//            r7.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_6 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_6.add(q13);
//            Grade gr37 = new Grade(/*98, */r7, p11, correct_6, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr37);
//            session.flush();
//            Grade gr38 = new Grade(/*98, */r7, p15, correct_6, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr38);
//            session.flush();
//            Grade gr39 = new Grade(/*98, */r7, p18, correct_6, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr39);
//            session.flush();
//            Grade gr40 = new Grade(/*98, */r7, p20, correct_6, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr40);
//            session.flush();
//            Grade gr41 = new Grade(/*98, */r7, p22, correct_6, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr41);
//            session.flush();
//            Grade gr42 = new Grade(/*98, */r7, p23, correct_6, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr42);
//            session.flush();
//            Grade gr43 = new Grade(/*98, */r7, p19, correct_6, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr43);
//            session.flush();
//            Grade gr44 = new Grade(/*98, */r7, p24, correct_6, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr44);
//            session.flush();
//
//
//            ReadyExam r8 = new ReadyExam(ex8, "mo18", true, "16/6/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r8);
//            r8.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_7 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_7.add(q15);
//            Grade gr45 = new Grade(/*98, */r8, p10, correct_7, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr45);
//            session.flush();
//            Grade gr46 = new Grade(/*98, */r8, p13, correct_7, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr46);
//            session.flush();
//            Grade gr47 = new Grade(/*98, */r8, p16, correct_7, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr47);
//            session.flush();
//            Grade gr48 = new Grade(/*98, */r8, p21, correct_7, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr48);
//            session.flush();
//            Grade gr49 = new Grade(/*98, */r8, p24, correct_7, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr49);
//            session.flush();
//            Grade gr50 = new Grade(/*98, */r8, p26, correct_7, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr50);
//            session.flush();
//            Grade gr51= new Grade(/*98, */r8, p28, correct_7, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr51);
//            session.flush();
//            Grade gr52 = new Grade(/*98, */r8, p6, correct_7, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr52);
//            session.flush();
//
//
//
//
//            ReadyExam r9 = new ReadyExam(ex9, "lm10", true, "16/6/2023 17:30");  // new "Out of the drawer" exam
//            session.save(r9);
//            r9.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_8 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_8.add(q17);
//            correct_8.add(q18);
//            Grade gr53 = new Grade(/*98, */r9, p1, correct_8, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr53);
//            session.flush();
//            Grade gr54 = new Grade(/*98, */r9, p2, correct_8, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr54);
//            session.flush();
//            Grade gr55 = new Grade(/*98, */r9, p3, correct_8, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr55);
//            session.flush();
//            Grade gr56 = new Grade(/*98, */r9, p4, correct_8, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr56);
//            session.flush();
//            Grade gr57 = new Grade(/*98, */r9, p5, correct_8, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr57);
//            session.flush();
//            Grade gr58 = new Grade(/*98, */r9, p6, correct_8, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr58);
//            session.flush();
//            Grade gr59 = new Grade(/*98, */r9, p9, correct_8, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr59);
//            session.flush();
//            Grade gr60 = new Grade(/*98, */r9, p10, correct_8, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr60);
//            session.flush();
//
//
//
//
//            ReadyExam r10 = new ReadyExam(ex10, "cr17", true, "19/6/2023 13:30");  // new "Out of the drawer" exam
//            session.save(r10);
//            r7.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_9 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_9.add(q19);
//            Grade gr61 = new Grade(/*98, */r10, p12, correct_9, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr61);
//            session.flush();
//            Grade gr62 = new Grade(/*98, */r10, p13, correct_9, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr62);
//            session.flush();
//            Grade gr63 = new Grade(/*98, */r10, p14, correct_9, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr63);
//            session.flush();
//            Grade gr64 = new Grade(/*98, */r10, p15, correct_9, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr64);
//            session.flush();
//            Grade gr65 = new Grade(/*98, */r10, p16, correct_9, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr65);
//            session.flush();
//            Grade gr66 = new Grade(/*98, */r10, p17, correct_9, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr66);
//            session.flush();
//            Grade gr67 = new Grade(/*98, */r10, p18, correct_9, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr67);
//            session.flush();
//            Grade gr68 = new Grade(/*98, */r10, p29, correct_9, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr68);
//            session.flush();
//
//
//
//
//            ReadyExam r11 = new ReadyExam(ex11, "k45l", true, "19/6/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r11);
//            r11.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_10 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_10.add(q24);
//            Grade gr69 = new Grade(/*98, */r11, p22, correct_10, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr69);
//            session.flush();
//            Grade gr70 = new Grade(/*98, */r11, p23, correct_10, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr70);
//            session.flush();
//            Grade gr71 = new Grade(/*98, */r11, p24, correct_10, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr71);
//            session.flush();
//            Grade gr72 = new Grade(/*98, */r11, p25, correct_10, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr72);
//            session.flush();
//            Grade gr73 = new Grade(/*98, */r11, p26, correct_10, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr73);
//            session.flush();
//            Grade gr74 = new Grade(/*98, */r11, p27, correct_10, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr74);
//            session.flush();
//            Grade gr75 = new Grade(/*98, */r11, p28, correct_10, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr75);
//            session.flush();
//            Grade gr76 = new Grade(/*98, */r11, p29, correct_10, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr76);
//            session.flush();
//
//
//
//
//            ReadyExam r12 = new ReadyExam(ex12, "orm2", true, "20/6/2023 13:30");  // new "Out of the drawer" exam
//            session.save(r12);
//            r12.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_11 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_11.add(q25);
//            Grade gr77 = new Grade(/*98, */r12, p9, correct_11, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr77);
//            session.flush();
//            Grade gr78 = new Grade(/*98, */r12, p12, correct_11, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr78);
//            session.flush();
//            Grade gr79 = new Grade(/*98, */r12, p14, correct_11, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr79);
//            session.flush();
//            Grade gr80 = new Grade(/*98, */r12, p16, correct_11, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr80);
//            session.flush();
//            Grade gr81 = new Grade(/*98, */r12, p17, correct_11, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr81);
//            session.flush();
//            Grade gr82 = new Grade(/*98, */r12, p19, correct_11, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr82);
//            session.flush();
//            Grade gr83 = new Grade(/*98, */r12, p21, correct_11, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr83);
//            session.flush();
//            Grade gr84 = new Grade(/*98, */r12, p25, correct_11, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr84);
//            session.flush();
//
//
//
//
//            ReadyExam r13 = new ReadyExam(ex13, "308u", true, "21/6/2023 13:30");  // new "Out of the drawer" exam
//            session.save(r13);
//            r7.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_12 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_12.add(q31);
//            Grade gr85 = new Grade(/*98, */r13, p8, correct_12, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr85);
//            session.flush();
//            Grade gr86 = new Grade(/*98, */r13, p11, correct_12, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr86);
//            session.flush();
//            Grade gr87 = new Grade(/*98, */r13, p13, correct_12, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr87);
//            session.flush();
//            Grade gr88 = new Grade(/*98, */r13, p15, correct_12, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr88);
//            session.flush();
//            Grade gr89 = new Grade(/*98, */r13, p16, correct_12, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr89);
//            session.flush();
//            Grade gr90 = new Grade(/*98, */r13, p17, correct_12, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr90);
//            session.flush();
//            Grade gr91 = new Grade(/*98, */r13, p19, correct_12, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr91);
//            session.flush();
//            Grade gr92 = new Grade(/*98, */r13, p23, correct_12, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr92);
//            session.flush();
//
//
//
//
//
//
//            ReadyExam r14 = new ReadyExam(ex14, "20qq", true, "22/6/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r14);
//            r14.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_13 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_13.add(q35);
//            Grade gr93 = new Grade(/*98, */r14, p11, correct_13, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr93);
//            session.flush();
//            Grade gr94 = new Grade(/*98, */r14, p15, correct_13, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr94);
//            session.flush();
//            Grade gr95 = new Grade(/*98, */r14, p18, correct_13, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr95);
//            session.flush();
//            Grade gr96 = new Grade(/*98, */r14, p20, correct_13, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr96);
//            session.flush();
//            Grade gr97 = new Grade(/*98, */r14, p22, correct_13, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr97);
//            session.flush();
//            Grade gr98 = new Grade(/*98, */r14, p23, correct_13, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr98);
//            session.flush();
//            Grade gr99 = new Grade(/*98, */r14, p19, correct_13, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr99);
//            session.flush();
//            Grade gr100 = new Grade(/*98, */r14, p24, correct_13, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr100);
//            session.flush();
//
//            ReadyExam r15 = new ReadyExam(ex15, "mza9", true, "23/6/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r15);
//            r15.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_14 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_14.add(q40);
//            Grade gr101 = new Grade(/*98, */r15, p12, correct_14, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr101);
//            session.flush();
//            Grade gr102 = new Grade(/*98, */r15, p16, correct_14, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr102);
//            session.flush();
//            Grade gr103 = new Grade(/*98, */r15, p4, correct_14, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr103);
//            session.flush();
//            Grade gr104 = new Grade(/*98, */r15, p5, correct_14, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr104);
//            session.flush();
//            Grade gr105 = new Grade(/*98, */r15, p6, correct_14, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr105);
//            session.flush();
//            Grade gr106 = new Grade(/*98, */r15, p7, correct_14, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr106);
//            session.flush();
//            Grade gr107 = new Grade(/*98, */r15, p8, correct_14, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr107);
//            session.flush();
//            Grade gr108 = new Grade(/*98, */r15, p9, correct_14, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr108);
//            session.flush();
//
//
//            ReadyExam r16 = new ReadyExam(ex16, "t64u", true, "24/6/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r16);
//            r16.setActual_solving_time(100); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_15 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_15.add(q41);
//            Grade gr109 = new Grade(/*98, */r16, p1, correct_15, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr109);
//            session.flush();
//            Grade gr110 = new Grade(/*98, */r16, p2, correct_15, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr110);
//            session.flush();
//            Grade gr111 = new Grade(/*98, */r16, p3, correct_15, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr111);
//            session.flush();
//            Grade gr112 = new Grade(/*98, */r16, p4, correct_15, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr112);
//            session.flush();
//            Grade gr113 = new Grade(/*98, */r16, p5, correct_15, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr113);
//            session.flush();
//            Grade gr114 = new Grade(/*98, */r16, p11, correct_15, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr114);
//            session.flush();
//            Grade gr115 = new Grade(/*98, */r16, p13, correct_15, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr115);
//            session.flush();
//            Grade gr116 = new Grade(/*98, */r16, p14, correct_15, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr116);
//            session.flush();
//
//
//
//            ReadyExam r17 = new ReadyExam(ex17, "pwe2", true, "27/6/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r17);
//            r17.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_16 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_16.add(q48);
//            Grade gr117 = new Grade(/*98, */r17, p20, correct_16, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr117);
//            session.flush();
//            Grade gr118 = new Grade(/*98, */r17, p21, correct_16, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr118);
//            session.flush();
//            Grade gr119 = new Grade(/*98, */r17, p22, correct_16, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr119);
//            session.flush();
//            Grade gr120 = new Grade(/*98, */r17, p23, correct_16, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr120);
//            session.flush();
//            Grade gr121 = new Grade(/*98, */r17, p24, correct_16, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr121);
//            session.flush();
//            Grade gr122 = new Grade(/*98, */r17, p25, correct_16, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr122);
//            session.flush();
//            Grade gr123 = new Grade(/*98, */r17, p26, correct_16, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr123);
//            session.flush();
//            Grade gr124 = new Grade(/*98, */r17, p27, correct_16, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr124);
//            session.flush();
//
//            ReadyExam r18 = new ReadyExam(ex18, "pwe2", true, "28/6/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r18);
//            r18.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_17 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_17.add(q49);
//            Grade gr125 = new Grade(/*98, */r18, p16, correct_17, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr125);
//            session.flush();
//            Grade gr126 = new Grade(/*98, */r18, p17, correct_17, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr126);
//            session.flush();
//            Grade gr127 = new Grade(/*98, */r18, p18, correct_17, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr127);
//            session.flush();
//            Grade gr128 = new Grade(/*98, */r18, p19, correct_17, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr128);
//            session.flush();
//            Grade gr129 = new Grade(/*98, */r18, p24, correct_17, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr129);
//            session.flush();
//            Grade gr130 = new Grade(/*98, */r18, p23, correct_17, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr130);
//            session.flush();
//            Grade gr131 = new Grade(/*98, */r18, p28, correct_17, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr131);
//            session.flush();
//            Grade gr132 = new Grade(/*98, */r18, p29, correct_17, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr132);
//            session.flush();
//
//
//            ReadyExam r19 = new ReadyExam(ex19, "pwe2", true, "1/7/2023 15:30");  // new "Out of the drawer" exam
//            session.save(r19);
//            r19.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_18 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_18.add(q53);
//            Grade gr133 = new Grade(/*98, */r19, p10, correct_18, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr133);
//            session.flush();
//            Grade gr134 = new Grade(/*98, */r19, p11, correct_18, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr134);
//            session.flush();
//            Grade gr135 = new Grade(/*98, */r19, p12, correct_18, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr135);
//            session.flush();
//            Grade gr136 = new Grade(/*98, */r19, p13, correct_18, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr136);
//            session.flush();
//            Grade gr137 = new Grade(/*98, */r19, p9, correct_18, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr137);
//            session.flush();
//            Grade gr138 = new Grade(/*98, */r19, p8, correct_18, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr138);
//            session.flush();
//            Grade gr139 = new Grade(/*98, */r19, p7, correct_18, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr139);
//            session.flush();
//            Grade gr140 = new Grade(/*98, */r19, p6, correct_18, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr140);
//            session.flush();
//
//            ReadyExam r20 = new ReadyExam(ex20, "l122", true, "1/7/2023 17:30");  // new "Out of the drawer" exam
//            session.save(r20);
//            r20.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_19 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_19.add(q57);
//            Grade gr141 = new Grade(/*98, */r20, p1, correct_19, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr141);
//            session.flush();
//            Grade gr142 = new Grade(/*98, */r20, p2, correct_19, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr142);
//            session.flush();
//            Grade gr143 = new Grade(/*98, */r20, p3, correct_19, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr143);
//            session.flush();
//            Grade gr144 = new Grade(/*98, */r20, p4, correct_19, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr144);
//            session.flush();
//            Grade gr145 = new Grade(/*98, */r20, p5, correct_19, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr145);
//            session.flush();
//            Grade gr146 = new Grade(/*98, */r20, p6, correct_19, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr146);
//            session.flush();
//            Grade gr147 = new Grade(/*98, */r20, p7, correct_19, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr147);
//            session.flush();
//            Grade gr148 = new Grade(/*98, */r20, p8, correct_19, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr148);
//            session.flush();
//
//
//            ReadyExam r21 = new ReadyExam(ex21, "llk2", true, "2/7/2023 08:30");  // new "Out of the drawer" exam
//            session.save(r21);
//            r21.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_20 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_20.add(q63);
//            Grade gr149 = new Grade(/*98, */r21, p9, correct_20, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr149);
//            session.flush();
//            Grade gr150 = new Grade(/*98, */r21, p10, correct_20, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr150);
//            session.flush();
//            Grade gr151 = new Grade(/*98, */r21, p11, correct_20, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr151);
//            session.flush();
//            Grade gr152 = new Grade(/*98, */r21, p12, correct_20, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr152);
//            session.flush();
//            Grade gr153 = new Grade(/*98, */r21, p13, correct_20, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr153);
//            session.flush();
//            Grade gr154 = new Grade(/*98, */r21, p14, correct_20, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr154);
//            session.flush();
//            Grade gr155 = new Grade(/*98, */r21, p15, correct_20, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr155);
//            session.flush();
//            Grade gr156 = new Grade(/*98, */r21, p16, correct_20, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr156);
//            session.flush();
//
//            ReadyExam r22 = new ReadyExam(ex22, "m122", true, "2/7/2023 08:30");  // new "Out of the drawer" exam
//            session.save(r22);
//            r22.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_21 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_21.add(q67);
//            Grade gr157 = new Grade(/*98, */r22, p17, correct_21, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr157);
//            session.flush();
//            Grade gr158 = new Grade(/*98, */r22, p18, correct_21, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr158);
//            session.flush();
//            Grade gr159 = new Grade(/*98, */r22, p19, correct_21, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr159);
//            session.flush();
//            Grade gr160 = new Grade(/*98, */r22, p20, correct_21, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr160);
//            session.flush();
//            Grade gr162 = new Grade(/*98, */r22, p21, correct_21, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr162);
//            session.flush();
//            Grade gr500 = new Grade(/*98, */r22, p22, correct_21, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr500);
//            session.flush();
//            Grade gr163 = new Grade(/*98, */r22, p23, correct_21, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr163);
//            session.flush();
//            Grade gr164 = new Grade(/*98, */r22, p24, correct_21, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr164);
//            session.flush();
//
//            ReadyExam r23 = new ReadyExam(ex23, "o2pe", true, "2/7/2023 12:00");  // new "Out of the drawer" exam
//            session.save(r23);
//            r23.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_22 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_22.add(q70);
//            Grade gr165 = new Grade(/*98, */r23, p25, correct_22, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr165);
//            session.flush();
//            Grade gr166 = new Grade(/*98, */r23, p26, correct_22, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr166);
//            session.flush();
//            Grade gr167 = new Grade(/*98, */r23, p27, correct_22, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr167);
//            session.flush();
//            Grade gr168 = new Grade(/*98, */r23, p28, correct_22, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr168);
//            session.flush();
//            Grade gr169 = new Grade(/*98, */r23, p29, correct_22, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr169);
//            session.flush();
//            Grade gr170 = new Grade(/*98, */r23, p1, correct_22, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr170);
//            session.flush();
//            Grade gr171 = new Grade(/*98, */r23, p2, correct_22, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr171);
//            session.flush();
//            Grade gr172 = new Grade(/*98, */r23, p3, correct_22, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr172);
//            session.flush();


//            ReadyExam r24 = new ReadyExam(ex24, "ok88", true, "2/7/2023 12:00");  // new "Out of the drawer" exam
//            session.save(r24);
//            r24.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_23 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_23.add(q74);
//            Grade gr173 = new Grade(/*98, */r24, p4, correct_23, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr173);
//            session.flush();
//            Grade gr174 = new Grade(/*98, */r24, p5, correct_23, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr174);
//            session.flush();
//            Grade gr175 = new Grade(/*98, */r24, p6, correct_23, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr175);
//            session.flush();
//            Grade gr176 = new Grade(/*98, */r24, p7, correct_23, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr176);
//            session.flush();
//            Grade gr177= new Grade(/*98, */r24, p8, correct_23, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr177);
//            session.flush();
//            Grade gr178 = new Grade(/*98, */r24, p9, correct_23, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr178);
//            session.flush();
//            Grade gr179 = new Grade(/*98, */r24, p10, correct_23, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr179);
//            session.flush();
//            Grade gr180 = new Grade(/*98, */r24, p11, correct_23, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr180);
//            session.flush();
//
//
//            ReadyExam r25 = new ReadyExam(ex25, "oqq8", true, "2/7/2023 14:00");  // new "Out of the drawer" exam
//            session.save(r25);
//            r25.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_24 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_24.add(q80);
//            Grade gr181 = new Grade(/*98, */r25, p12, correct_24, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr181);
//            session.flush();
//            Grade gr182 = new Grade(/*98, */r25, p13, correct_24, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr182);
//            session.flush();
//            Grade gr183 = new Grade(/*98, */r25, p14, correct_24, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr183);
//            session.flush();
//            Grade gr184 = new Grade(/*98, */r25, p15, correct_24, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr184);
//            session.flush();
//            Grade gr185= new Grade(/*98, */r25, p16, correct_24, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr185);
//            session.flush();
//            Grade gr186 = new Grade(/*98, */r25, p17, correct_24, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr186);
//            session.flush();
//            Grade gr187 = new Grade(/*98, */r25, p18, correct_24, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr187);
//            session.flush();
//            Grade gr188 = new Grade(/*98, */r25, p19, correct_24, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr188);
//            session.flush();
//
//
//            ReadyExam r26 = new ReadyExam(ex26, "pok4", true, "2/7/2023 16:00");  // new "Out of the drawer" exam
//            session.save(r26);
//            r26.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_25 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_25.add(q81);
//            Grade gr189 = new Grade(/*98, */r26, p13, correct_25, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr189);
//            session.flush();
//            Grade gr190 = new Grade(/*98, */r26, p14, correct_25, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr190);
//            session.flush();
//            Grade gr191 = new Grade(/*98, */r26, p15, correct_25, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr191);
//            session.flush();
//            Grade gr192 = new Grade(/*98, */r26, p16, correct_25, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr192);
//            session.flush();
//            Grade gr193= new Grade(/*98, */r26, p17, correct_25, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr193);
//            session.flush();
//            Grade gr194 = new Grade(/*98, */r26, p18, correct_25, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr194);
//            session.flush();
//            Grade gr195 = new Grade(/*98, */r26, p19, correct_25, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr195);
//            session.flush();
//            Grade gr196 = new Grade(/*98, */r26, p20, correct_25, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr196);
//            session.flush();
//
//
//
//
//            ReadyExam r27 = new ReadyExam(ex27, "pok4", true, "3/7/2023 08:30");  // new "Out of the drawer" exam
//            session.save(r27);
//            r27.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_26 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_26.add(q87);
//            Grade gr197 = new Grade(/*98, */r27, p14, correct_26, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr197);
//            session.flush();
//            Grade gr198 = new Grade(/*98, */r27, p15, correct_26, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr198);
//            session.flush();
//            Grade gr199 = new Grade(/*98, */r27, p16, correct_26, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr199);
//            session.flush();
//            Grade gr200 = new Grade(/*98, */r27, p17, correct_26, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr200);
//            session.flush();
//            Grade gr201= new Grade(/*98, */r27, p20, correct_26, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr201);
//            session.flush();
//            Grade gr202 = new Grade(/*98, */r27, p21, correct_26, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr202);
//            session.flush();
//            Grade gr203 = new Grade(/*98, */r27, p22, correct_26, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr203);
//            session.flush();
//            Grade gr204 = new Grade(/*98, */r27, p23, correct_26, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr204);
//            session.flush();
//
//
//            ReadyExam r28 = new ReadyExam(ex28, "ne8v", true, "3/7/2023 18:00");  // new "Out of the drawer" exam
//            session.save(r28);
//            r28.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_27 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_27.add(q92);
//            Grade gr205 = new Grade(/*98, */r28, p18, correct_27, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr205);
//            session.flush();
//            Grade gr206 = new Grade(/*98, */r28, p19, correct_27, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr206);
//            session.flush();
//            Grade gr207 = new Grade(/*98, */r28, p24, correct_27, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr207);
//            session.flush();
//            Grade gr208 = new Grade(/*98, */r28, p25, correct_27, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr208);
//            session.flush();
//            Grade gr209= new Grade(/*98, */r28, p26, correct_27, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr209);
//            session.flush();
//            Grade gr210 = new Grade(/*98, */r28, p27, correct_27, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr210);
//            session.flush();
//            Grade gr211 = new Grade(/*98, */r28, p28, correct_27, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr211);
//            session.flush();
//            Grade gr212 = new Grade(/*98, */r28, p29, correct_27, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr212);
//            session.flush();
//
//            ReadyExam r29 = new ReadyExam(ex29, "ae8v", true, "4/7/2023 08:00");  // new "Out of the drawer" exam
//            session.save(r29);
//            r29.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_28 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_28.add(q96);
//            Grade gr213 = new Grade(/*98, */r29, p18, correct_28, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr213);
//            session.flush();
//            Grade gr214 = new Grade(/*98, */r29, p19, correct_28, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr214);
//            session.flush();
//            Grade gr215 = new Grade(/*98, */r29, p24, correct_28, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr215);
//            session.flush();
//            Grade gr216 = new Grade(/*98, */r29, p25, correct_28, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr216);
//            session.flush();
//            Grade gr217= new Grade(/*98, */r29, p26, correct_28, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr217);
//            session.flush();
//            Grade gr218 = new Grade(/*98, */r29, p27, correct_28, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr218);
//            session.flush();
//            Grade gr219 = new Grade(/*98, */r29, p28, correct_28, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr219);
//            session.flush();
//            Grade gr220 = new Grade(/*98, */r29, p29, correct_28, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr220);
//            session.flush();
//
//
//            ReadyExam r30 = new ReadyExam(ex30, "da8v", true, "4/7/2023 12:00");  // new "Out of the drawer" exam
//            session.save(r30);
//            r30.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_29 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_29.add(q100);
//            Grade gr221 = new Grade(/*98, */r30, p1, correct_29, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr221);
//            session.flush();
//            Grade gr222 = new Grade(/*98, */r30, p2, correct_29, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr222);
//            session.flush();
//            Grade gr223 = new Grade(/*98, */r30, p3, correct_29, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr223);
//            session.flush();
//            Grade gr224 = new Grade(/*98, */r30, p4, correct_29, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr224);
//            session.flush();
//            Grade gr225= new Grade(/*98, */r30, p5, correct_29, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr225);
//            session.flush();
//            Grade gr226 = new Grade(/*98, */r30, p6, correct_29, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr226);
//            session.flush();
//            Grade gr227 = new Grade(/*98, */r30, p7, correct_29, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr227);
//            session.flush();
//            Grade gr228 = new Grade(/*98, */r30, p8, correct_29, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr228);
//            session.flush();
//
//
//            ReadyExam r31 = new ReadyExam(ex31, "139a", true, "4/7/2023 16:00");  // new "Out of the drawer" exam
//            session.save(r31);
//            r31.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_30 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_30.add(q104);
//            Grade gr229 = new Grade(/*98, */r31, p9, correct_30, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr229);
//            session.flush();
//            Grade gr230 = new Grade(/*98, */r31 ,p10, correct_30, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr230);
//            session.flush();
//            Grade gr231 = new Grade(/*98, */r31, p11, correct_30, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr231);
//            session.flush();
//            Grade gr232 = new Grade(/*98, */r31, p12, correct_30, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr232);
//            session.flush();
//            Grade gr233= new Grade(/*98, */r31, p13, correct_30, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr233);
//            session.flush();
//            Grade gr234 = new Grade(/*98, */r31, p14, correct_30, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr234);
//            session.flush();
//            Grade gr235 = new Grade(/*98, */r31, p15, correct_30, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr235);
//            session.flush();
//            Grade gr236 = new Grade(/*98, */r31, p16, correct_30, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr236);
//            session.flush();
//
//
//            ReadyExam r32 = new ReadyExam(ex32, "1daa", true, "4/7/2023 18:00");  // new "Out of the drawer" exam
//            session.save(r32);
//            r32.setActual_solving_time(120); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct_31 = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct_31.add(q107);
//            Grade gr237 = new Grade(/*98, */r32, p17, correct_31, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr237);
//            session.flush();
//            Grade gr238 = new Grade(/*98, */r32 ,p18, correct_31, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr238);
//            session.flush();
//            Grade gr239 = new Grade(/*98, */r32, p19, correct_31, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr239);
//            session.flush();
//            Grade gr240 = new Grade(/*98, */r32, p20, correct_31, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr240);
//            session.flush();
//            Grade gr241= new Grade(/*98, */r32, p21, correct_31, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr241);
//            session.flush();
//            Grade gr242 = new Grade(/*98, */r32, p22, correct_31, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr242);
//            session.flush();
//            Grade gr243 = new Grade(/*98, */r32, p23, correct_31, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr243);
//            session.flush();
//            Grade gr244 = new Grade(/*98, */r32, p24, correct_31, "WOW"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr244);
//            session.flush();

//            Subject astro = new Subject("Astrophysics");                  // new subject
//            session.save(astro);
//           session.flush();
//           Course speeds = new Course("Velocity in space", astro);       // new course
//           session.save(speeds);
//           session.flush();
//            // TODO: javafx
//            Answer ans1 = new Answer("1 km/h", false);          // new answers
//            Answer ans2 = new Answer("2 km/h", false);
//            Answer ans3 = new Answer("3 km/h", false);
//            Answer ans4 = new Answer("4 km/h", true);
//            session.save(ans1);
//            session.save(ans2);
//            session.save(ans3);
//            session.save(ans4);
//            session.flush();
//
//            Answer ans5 = new Answer("laptop", false);          // more new answers
//            Answer ans6 = new Answer("table", false);
//            Answer ans7 = new Answer("Cruise ship", true);
//            Answer ans8 = new Answer("tent", false);
//            session.save(ans5);
//            session.save(ans6);
//            session.save(ans7);
//            session.save(ans8);
//            session.flush();
//            Question q1 = new Question("which is bigger?", ans1, ans2, ans3, ans4, astro, speeds);// new questions
//            session.save(q1);
//            /*session.save(ans1);
//            session.save(ans2);
//            session.save(ans3);
//            session.save(ans4);*/
//            session.flush();
//            q1.updateCode();  // Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
//            session.save(q1);
//            session.flush();
//
//            Question q2 = new Question("which number is bigger?", ans5, ans6, ans7, ans8, astro, speeds); // another question
//            session.save(q2);
//            session.flush();
//            q2.updateCode();
//            session.save(q2);
//            session.flush();
//
//            Teacher t1 = new Teacher("Malki", "Malki_password", true);       // new teacher
//            t1.addSubject(astro);
//            session.save(t1);
//            session.flush();
//
//            Pupil p1 = new Pupil("Michael", "213468951", "Michael_password", false); // new pupil
//            session.save(p1);
//            session.flush();
//
//
//
//            // new exam
//            Exam ex1 = new Exam("first exam", speeds, 90, "hello students!", "hello teacher!", t1);
//            session.save(ex1);
//            session.flush();
//            ex1.updateCode(); // Important!!! after creating + saving + flushing the exam, you have to call this function and then save + flush again
//            /*
//            This part is important and tricky. normally you would think that adding a question to the exam would look like:
//            ex1.addQuestion(q1, 70);
//            (BTW: now adding a question also requires to give that question points- 70 points in this case)
//            BUT YOU SHOULD NOT ADD QUESTIONS LIKE THIS!!!!!!!!!!
//            the points of this question on this exam are loaded into a different entity called Exam_question_points.
//            this entity is created automatically each time you add a question, but in order for it to be saved in the tables
//            I made the addQuestion function return this entity- and then it has to be saved by the session.
//            SO THE OVERALL COMMAND TO ADD A QUESTION TO AN EXAM IS:
//            session.save(ex1.addQuestion(q1, 70));
//             */
//            session.save(ex1.addQuestion(q1, 70));
//            session.save(ex1.addQuestion(q2, 30));
//            session.save(ex1);
//            session.flush();
//
//            ReadyExam r1 = new ReadyExam(ex1, "10a4", true, "14/6/2023 13:30");  // new "Out of the drawer" exam
//            session.save(r1);
//            r1.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given- no need)
//            session.flush();
//            List<Question> correct = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
//            correct.add(q1);                                          // p1 answered only question q1 correctly
//            Grade gr1 = new Grade(/*98, */r1, p1, correct, "Michael, nice exam!"); // grade- note that given the List "correct" it computes the grade itself
//            session.save(gr1);
//            session.flush();



//            deleteAllPupils();
            //  deleteAllTeachers();
            //deleteAllPrincipals();

            //deleteAllSubjects();
            //   deleteAllCourses();
            // deleteAllQuestions();

            //generatePrincipal();
            // generateTeachers();

//            generateStudentsWithGrades();

            // generateSubject();
            // generateCourse();
            //   generateQuestion();
            //  generateQuestion();

            printAllPrincipals();
            printAllTeachers();

            // printAllQuestions();
            //  printAllPupils();

         /*   List<Question>questions=getAllQuestions();
            for(Question question:questions)
            {
                System.out.println(question.GetAnswer());
            }*/

            //  printAllSubject();
            //   printAllCourses();
            //   printAllQuestions();
            //   printAllExams();

            session.getTransaction().commit();//Save everything

        } catch (Exception e) {
            if(session!=null)
            {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back");
            e.printStackTrace();
        }
        finally {
            if (session != null) {
                session.close();
                session.getSessionFactory().close();
            }
        }
    }
}

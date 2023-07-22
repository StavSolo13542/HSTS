//package org.example;
package il.cshaifasweng.OCSFMediatorExample.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
public class App
{

    private static Session session;
    private static SimpleServer server;

    public static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Answer.class);
        configuration.addAnnotatedClass(Course.class);
        configuration.addAnnotatedClass(Exam.class);
        configuration.addAnnotatedClass(Grade.class);
        configuration.addAnnotatedClass(Principal.class);
        configuration.addAnnotatedClass(Pupil.class);
        configuration.addAnnotatedClass(Question.class);
        configuration.addAnnotatedClass(Subject.class);
        configuration.addAnnotatedClass(Teacher.class);
        configuration.addAnnotatedClass(ReadyExam.class);
        configuration.addAnnotatedClass(Exam_Question_points.class);


        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    /*private static void generateCars() throws Exception {
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Car car = new Car("MOO-" + random.nextInt(), 100000, 2000 + random.nextInt(19));
            session.save(car);

 * The call to session.flush() updates the DB immediately without ending the transaction.
 * Recommended to do after an arbitrary unit of work.
 * MANDATORY to do if you are saving a large amount of data - otherwise you may get
cache errors.

            session.flush();
        }
    }*/

    /*private static List<Car> getAllCars() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Car> query = builder.createQuery(Car.class);
        query.from(Car.class);
        List<Car> data = session.createQuery(query).getResultList();
        return data;
    }*/

    /*private static void printAllCars() throws Exception {
        List<Car> cars = getAllCars();
        for (Car car : cars) {
            System.out.print("Id: ");
            System.out.print(car.getId());
            System.out.print(", License plate: ");
            System.out.print(car.getLicensePlate());
            System.out.print(", Price: ");
            System.out.print(car.getPrice());
            System.out.print(", Year: ");
            System.out.print(car.getYear());
            System.out.print('\n');
        }
    }*/

    public static void main( String[] args ) {
        try {
            server = new SimpleServer(3100); // for local use (in LAN)

//        server = new SimpleServer(13010);
            server.listen();

            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            String queryString = "UPDATE Pupil SET isLoggedIn = 0 WHERE name = 'Michael'";
            Query query = session.createQuery(queryString);
            int rowCount = query.executeUpdate();

            Subject astro = new Subject("Astrophysics");                  // new subject
            session.save(astro);
            session.flush();
            Course speeds = new Course("Velocity in space", astro);       // new course
            session.save(speeds);
            session.flush();
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
            Question q1 = new Question("which is bigger?", ans1, ans2, ans3, ans4, astro, speeds);// new questions
            session.save(q1);
            session.flush();
            q1.updateCode();  //Important!!! after creating + saving + flushing the question, you have to call this function and then save + flush again
            session.save(q1);
            session.flush();

            Question q2 = new Question("which number is bigger?", ans5, ans6, ans7, ans8, astro, speeds); // another question
            session.save(q2);
            session.flush();
            q2.updateCode();
            session.save(q2);
            session.flush();

            Teacher t1 = new Teacher("Malki", "Malki_password", true);       // new teacher
            t1.addSubject(astro);
            session.save(t1);
            session.flush();

            Pupil p1 = new Pupil("Michael", "213468951", "Michael_password", false); // new pupil
            session.save(p1);
            session.flush();

            Principal pr1 = new Principal("Dani Keren", "Dani_Keren_password", true);  // new principle
            session.save(pr1);
            session.flush();

            // new exam
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

            ReadyExam r1 = new ReadyExam(ex1, "10a4", true, "14/6/2023 13:30");  // new "Out of the drawer" exam
            session.save(r1);
            r1.setActual_solving_time(150); // need to update actual solving time at the end of an exam (if no extra time was given - no need)
            session.flush();
            List<Question> correct = new ArrayList<Question>();       // Questions answered correctly by pupil p1 during exam ex1
            correct.add(q1);                                          // p1 answered only question q1 correctly
            Grade gr1 = new Grade(/*98, */r1, p1, correct, "Michael, nice exam!"); // grade - note that given the List "correct" it computes the grade itself
            session.save(gr1);
            session.flush();


            session.getTransaction().commit(); // Save everything.

        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
            System.out.println("App(Server) was launched successfully");
        }
    }
}

//public class App


//    public static List<Pupil> getAllPupils() throws Exception{
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Pupil> query = builder.createQuery(Pupil.class);
//        query.from(Pupil.class);
//        List<Pupil> pupils = session.createQuery(query).getResultList();
//        return pupils;
//    }
//
//    private static void printAllPupils() throws Exception{
//        List<Pupil> pupils=getAllPupils();
//        System.out.println("All pupils are: ");
//        if (pupils.isEmpty())
//        {
//            System.out.println("There are no pupils");
//        }
//        for(Pupil pupil: pupils)
//        {
//            System.out.println("Id: "+ pupil.getId());
//            System.out.println("Name: "+ pupil.getName());
//            System.out.println("Password: "+ pupil.getPassword());
//            System.out.println("Grades: "+ pupil.getGrades().toString());
//            System.out.println("is logged in: "+ pupil.getIsLoggedIn());
//        }
//        System.out.println();
//    }
//
//    private static void deleteAllPupils() throws Exception {
//
//        String hql = "DELETE FROM Pupil";
//        Query query = session.createQuery(hql);
//        int rowCount = query.executeUpdate();
//        System.out.println("Deleted " + rowCount + " students.");
//
//        session.getTransaction().commit();
//    }
//
//    public static List<Teacher> getAllTeachers() throws Exception{
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
//        query.from(Teacher.class);
//        List<Teacher> teachers = session.createQuery(query).getResultList();
//        return teachers;
//    }
//
//    private static void printAllTeachers() throws Exception{
//        List<Teacher> teachers=getAllTeachers();
//        System.out.println("All teachers are: ");
//        if (teachers.isEmpty())
//        {
//            System.out.println("There are no teachers");
//        }
//        for(Teacher teacher: teachers)
//        {
//            System.out.println("Name: "+ teacher.getName());
//            System.out.println("Password: "+ teacher.getPassword());
//            System.out.println("is logged in: "+ teacher.getIsLoggedIn());
//        }
//        System.out.println();
//    }
//
//    private static void deleteAllTeachers() throws Exception {
//        session.beginTransaction();
//
//        String hql = "DELETE FROM Teacher";
//        Query query = session.createQuery(hql);
//        int rowCount = query.executeUpdate();
//        System.out.println("Deleted " + rowCount + " teachers.");
//
//        session.getTransaction().commit();
//    }
//
//    public static List<Principal> getAllPrincipals() throws Exception{
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Principal> query = builder.createQuery(Principal.class);
//        query.from(Principal.class);
//        List<Principal> principals = session.createQuery(query).getResultList();
//        return principals;
//    }
//
//    private static void printAllPrincipals() throws Exception{
//        List<Principal> principals=getAllPrincipals();
//        System.out.println("All principals are: ");
//        if (principals.isEmpty())
//        {
//            System.out.println("There are no principals");
//        }
//        for(Principal principal: principals)
//        {
//            System.out.println("Name: "+ principal.getName());
//            System.out.println("Password: "+ principal.getPassword());
//            System.out.println("is logged in: "+ principal.getIsLoggedIn());
//        }
//        System.out.println();
//    }
//
//    private static void deleteAllPrincipals() throws Exception {
//        session.beginTransaction();
//
//        String hql = "DELETE FROM Principal";
//        Query query = session.createQuery(hql);
//        int rowCount = query.executeUpdate();
//        System.out.println("Deleted " + rowCount + " principals.");
//
//        session.getTransaction().commit();
//    }
//
//    public static List<Subject> getAllSubjects() throws Exception{
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Subject> query = builder.createQuery(Subject.class);
//        query.from(Subject.class);
//        List<Subject> subjects = session.createQuery(query).getResultList();
//        return subjects;
//    }
//
//    private static void printAllSubject() throws Exception{
//        List<Subject> subjects=getAllSubjects();
//        System.out.println("All subjects are: ");
//        if (subjects.isEmpty())
//        {
//            System.out.println("There are no subjects");
//        }
//        for(Subject subject: subjects)
//        {
//            System.out.println("Name: "+ subject.getName());
//        }
//        System.out.println();
//    }
//
//    private static void deleteAllSubjects() throws Exception {
//
//        String hql = "DELETE FROM Subject";
//        Query query = session.createQuery(hql);
//        int rowCount = query.executeUpdate();
//        System.out.println("Deleted " + rowCount + " subjects.");
//
//    }
//
//    public static List<Course> getAllCourses() throws Exception{
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Course> query = builder.createQuery(Course.class);
//        query.from(Course.class);
//        List<Course> courses = session.createQuery(query).getResultList();
//        return courses;
//    }
//
//    private static void printAllCourses() throws Exception{
//        List<Course> courses=getAllCourses();
//        System.out.println("All courses are: ");
//        if (courses.isEmpty())
//        {
//            System.out.println("There are no courses");
//        }
//        for(Course course: courses)
//        {
//            System.out.println("Name: "+ course.getName());
//        }
//        System.out.println();
//    }
//
//    private static void deleteAllCourses() throws Exception {
//
//        String hql = "DELETE FROM Course";
//        Query query = session.createQuery(hql);
//        int rowCount = query.executeUpdate();
//        System.out.println("Deleted " + rowCount + " courses.");
//
//    }
//
//    public static List<Question> getAllQuestions() throws Exception{
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Question> query = builder.createQuery(Question.class);
//        query.from(Question.class);
//        List<Question> questions = session.createQuery(query).getResultList();
//        return questions;
//    }
//
//    private static void printAllQuestions() throws Exception{
//        List<Question> questions=getAllQuestions();
//        System.out.println("All questions are: ");
//        if (questions.isEmpty())
//        {
//            System.out.println("There are no questions");
//        }
//        for(Question question: questions)
//        {
//            System.out.println(question);
//        }
//        System.out.println();
//    }
//
//    private static void deleteAllQuestions() throws Exception {
//
//        String hql = "DELETE FROM Question";
//        Query query = session.createQuery(hql);
//        int rowCount = query.executeUpdate();
//        System.out.println("Deleted " + rowCount + " questions.");
//
//    }
//    static Exam ex1;
//    private static SimpleServer server;
//    public static void main( String[] args ) throws IOException
//    {
//        server = new SimpleServer(3100); // for local use (in LAN)
//
////        server = new SimpleServer(13010);
//        server.listen();
//
//        try {
//            SessionFactory sessionFactory = getSessionFactory();
//            session = sessionFactory.openSession();
//            session.beginTransaction();
////            deleteAllPupils();
////            deleteAllTeachers();
////            deleteAllPrincipals();
//
////            deleteAllSubjects();
////            deleteAllCourses();
////            deleteAllQuestions();
//
////            generatePrincipal();
////            generateTeachers();
////            generateStudentsWithGrades();
//
////            generateSubject();
////            generateCourse();
////            generateQuestion();
//
////            printAllPrincipals();
////            printAllTeachers();
//            printAllPupils();
////            printAllSubject();
////            printAllCourses();
//            printAllQuestions();
//
//            session.getTransaction().commit();//Save everything
//
//        } catch (Exception e) {
//            if(session!=null)
//            {
//                session.getTransaction().rollback();
//            }
//            System.err.println("An error occurred, changes have been rolled back");
//            e.printStackTrace();
//        }
//        finally {
//            if (session != null) {
//                session.close();
//                session.getSessionFactory().close();
//            }
//        }
//    }
//}
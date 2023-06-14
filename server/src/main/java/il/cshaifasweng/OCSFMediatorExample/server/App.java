package il.cshaifasweng.OCSFMediatorExample.server;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
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
    public static Exam exam;
    static SessionFactory getSessionFactory() throws HibernateException {
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

    public static void generateStudentsWithGrades() throws Exception{
    }

    public static void generateTeachers() throws Exception{
    }

    public static void generatePrincipal() throws Exception{
//        session.beginTransaction();

    }

    public static void generateSubject() throws Exception
    {
        Subject biology = new Subject("Biology");
        session.save(biology);

        session.flush();
    }

    public static void generateCourse() throws Exception
    {
    }

    public static void generateQuestion() throws Exception
    {
        List<String> answers = new ArrayList<String>();
        answers.add("Lion");
        answers.add("Hippo");
        answers.add("Bird");
        answers.add("Snake");

        String subjects_name = "Biology";

        Query query = session.createQuery("SELECT s FROM Subject s WHERE s.name = :subjectName");
        query.setParameter("subjectName", subjects_name);
        Subject subject = (Subject) ((org.hibernate.query.Query<?>) query).uniqueResult();
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
            System.out.println("Id: "+ pupil.getId());
            System.out.println("Name: "+ pupil.getName());
            System.out.println("Password: "+ pupil.getPassword());
            System.out.println("Grades: "+ pupil.getGrades().toString());
            System.out.println("is logged in: "+ pupil.getIsLoggedIn());
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
        session.beginTransaction();

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
        session.beginTransaction();

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
    static Exam ex1;
    private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3100); // for local use (in LAN)

//        server = new SimpleServer(13010);
        server.listen();

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
//            deleteAllPupils();
//            deleteAllTeachers();
//            deleteAllPrincipals();

//            deleteAllSubjects();
//            deleteAllCourses();
//            deleteAllQuestions();

//            generatePrincipal();
//            generateTeachers();
//            generateStudentsWithGrades();

//            generateSubject();
//            generateCourse();
//            generateQuestion();

//            printAllPrincipals();
//            printAllTeachers();
            printAllPupils();
//            printAllSubject();
//            printAllCourses();
            printAllQuestions();

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
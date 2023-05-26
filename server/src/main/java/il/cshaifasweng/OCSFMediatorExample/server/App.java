package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import il.cshaifasweng.OCSFMediatorExample.server.entities.Principal;
import il.cshaifasweng.OCSFMediatorExample.server.entities.Pupil;
import il.cshaifasweng.OCSFMediatorExample.server.entities.Teacher;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Session session;

    static SessionFactory getSessionFactory() throws HibernateException{
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Pupil.class);
        configuration.addAnnotatedClass(Teacher.class);
        configuration.addAnnotatedClass(Principal.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static void generateStudentsWithGrades() throws Exception{
        Pupil pupil1 = new Pupil(1, "Alon", "100 95 100");
        session.save(pupil1);
        Pupil pupil2 = new Pupil(2, "Tomer", "100 95 80");
        session.save(pupil2);
        Pupil pupil3 = new Pupil(3, "Stav", "95 95 100");
        session.save(pupil3);
        Pupil pupil4 = new Pupil(4, "Shahar", "100 100 100");
        session.save(pupil4);
        Pupil pupil5 = new Pupil(5, "Michael", "90 95 100");
        session.save(pupil5);
        Pupil pupil6 = new Pupil(6, "Sassi", "90 90 90");
        session.save(pupil6);
        Pupil pupil7 = new Pupil(7, "Noam", "80 95 100");
        session.save(pupil7);
        Pupil pupil8 = new Pupil(8, "Avi", "60 70 80");
        session.save(pupil8);
        Pupil pupil9 = new Pupil(9, "Moshe", "100 80 100");
        session.save(pupil9);
        Pupil pupil0 = new Pupil(10, "Ya'akov", "100 95 100");
        session.save(pupil0);
        Pupil pupil11 = new Pupil(11, "David", "100 100 100");
        session.save(pupil11);
            /*
             * The call to session.flush() updates the DB immediately without ending the transaction.
             * Recommended to do after an arbitrary unit of work.
             * MANDATORY to do if you are saving a large amount of data - otherwise you may get
            cache errors.
             */
        session.flush();
    }

    public static void generateTeachers() throws Exception{
        Teacher teacher1 = new Teacher(1, "Tova");
        session.save(teacher1);
        Teacher teacher2 = new Teacher(2, "Sara");
        session.save(teacher2);
        Teacher teacher3 = new Teacher(3, "Bina");
        session.save(teacher3);
        Teacher teacher4 = new Teacher(4, "Limor");
        session.save(teacher4);
        Teacher teacher5 = new Teacher(5, "Michal");
        session.save(teacher5);
            /*
             * The call to session.flush() updates the DB immediately without ending the transaction.
             * Recommended to do after an arbitrary unit of work.
             * MANDATORY to do if you are saving a large amount of data - otherwise you may get
            cache errors.
             */
        session.flush();
    }

    public static void generatePrincipal() throws Exception{
        Principal principal = new Principal(1, "Anat");
        session.save(principal);

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
            System.out.println("Grades: "+ pupil.getGrades().toString());
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
        }
        System.out.println();
    }

    private static void deleteAllTeachers() throws Exception {

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
        }
        System.out.println();
    }

    private static void deleteAllPrincipals() throws Exception {

        String hql = "DELETE FROM Principal";
        Query query = session.createQuery(hql);
        int rowCount = query.executeUpdate();
        System.out.println("Deleted " + rowCount + " principals.");

        session.getTransaction().commit();
    }

    private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3100);
        server.listen();

        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();

            session.beginTransaction();
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

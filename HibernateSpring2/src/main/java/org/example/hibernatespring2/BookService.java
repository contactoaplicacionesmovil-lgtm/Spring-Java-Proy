package org.example.hibernatespring2;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    public void saveBook(Book book) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction trans = session.beginTransaction();
        session.save(book);
        trans.commit();

    }

    public Book getBookById(Long id) {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction trans = sesion.beginTransaction();
        Book book = sesion.get(Book.class, id);
        trans.commit();
        return book;
    }

    public List<Book> getAllBooks() {
        Session sesion = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction trans = sesion.beginTransaction();
        List<Book> books = sesion.createQuery("FROM Book", Book.class).getResultList();
        trans.commit();
        return books;
    }

    public void updateBook(Book book) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction trans = session.beginTransaction();
        session.update(book);
        trans.commit();
    }

    public void deleteBook(Long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction trans = session.beginTransaction();
        Book b = session.get(Book.class, id);
        if (b != null) {
            session.delete(b);
        }
        trans.commit();
    }

}

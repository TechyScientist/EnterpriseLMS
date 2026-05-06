package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.persistence.Book;
import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("CatalogSearchServlet")
public class CatalogSearchServlet extends HttpServlet {

    @EJB
    private BookDao bookDao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(request.getParameter("search-submit") != null) {
            String searchBy = request.getParameter("searchBy");
            switch (searchBy) {
                case "copyBarcode": {
                    String barcode = request.getParameter("copyBarcode");
                    Book book = bookDao.findByCopyCode(barcode);
                    if (book == null) {
                        session.setAttribute("status", SC_NOT_FOUND);
                        response.sendRedirect("/library/catalog-search.jsp");
                    } else {
                        session.setAttribute("status", SC_ACCEPTED);
                        session.setAttribute("book", book);
                        response.sendRedirect("/library/catalog-search.jsp");
                    }
                }
                break;
                case "titleBarcode": {
                    String barcode = request.getParameter("titleBarcode");
                    List<Book> books = bookDao.findByTitleBarcode(barcode);
                    if (books.isEmpty()) {
                        session.setAttribute("status", SC_NOT_FOUND);
                        response.sendRedirect("/library/catalog-search.jsp");
                    } else {
                        session.setAttribute("status", SC_ACCEPTED);
                        session.setAttribute("booklist", books);
                        response.sendRedirect("/library/catalog-search.jsp");
                    }
                }
                break;
                case "title": {
                    String title = request.getParameter("title");
                    List<Book> books = bookDao.findByTitle(title);
                    if (books.isEmpty()) {
                        session.setAttribute("status", SC_NOT_FOUND);
                        response.sendRedirect("/library/catalog-search.jsp");
                    } else {
                        session.setAttribute("status", SC_ACCEPTED);
                        session.setAttribute("booklist", books);
                        response.sendRedirect("/library/catalog-search.jsp");
                    }
                }
                break;
                default:
                    String author = request.getParameter("author");
                    List<Book> books = bookDao.findByAuthor(author);
                    if (books.isEmpty()) {
                        session.setAttribute("status", SC_NOT_FOUND);
                        response.sendRedirect("/library/catalog-search.jsp");
                    } else {
                        session.setAttribute("status", SC_ACCEPTED);
                        session.setAttribute("booklist", books);
                        response.sendRedirect("/library/catalog-search.jsp");
                    }
            }
        } else {
            session.setAttribute("status", SC_BAD_REQUEST);
            response.sendRedirect("/library/catalog-search.jsp");
        }
    }
}

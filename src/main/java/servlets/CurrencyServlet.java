package servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrencyDao;
import daoImp.CurrencyDaoImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Currency;
import java.util.List;


public class CurrencyServlet extends HttpServlet {
    //private CurrencyDao currencyDao = new CurrencyDaoImp();
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("doGet");
//        String action = request.getParameter("action");
//        switch (action) {
//            case "getAll":
//                getAllCurrencies(request, response);
//                break;
//            default:
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
//        }
    }

//    private void getAllCurrencies(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        List<Currency> currencies = currencyDao.getAllCurrency();
//        String json = new ObjectMapper().writeValueAsString(currencies);
//        response.setContentType("application/json");
//        response.getWriter().write(json);
//    }
}

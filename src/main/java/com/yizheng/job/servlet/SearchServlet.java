package com.yizheng.job.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yizheng.job.database.MySQLConnection;
import com.yizheng.job.entity.Item;
import com.yizheng.job.entity.ResultResponse;
import com.yizheng.job.external.GitHubClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            mapper.writeValue(response.getWriter(), new ResultResponse("Session Invalid"));
            return;
        }

        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));
        String userId = request.getParameter("user_id");

        //first we get a set of item IDs that are liked by this user
        MySQLConnection connection = new MySQLConnection();
        Set<String> favoriteItemIds = connection.getFavoriteItemIds(userId);
        connection.close();

        //then we do the search
        GitHubClient client = new GitHubClient();
        List<Item> items = client.search(lat, lon, null);

        //then for the search results, if the item was previously already liked by the user, we set the favorite field to true
        for (Item item : items) {
            item.setFavorite(favoriteItemIds.contains(item.getId()));
        }

        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), items);
    }
}

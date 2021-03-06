package com.google.sps.servlets;

import com.google.sps.data.Room;
import java.util.Queue;
import com.google.sps.data.Message;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import java.util.LinkedList;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;
import org.owasp.html.PolicyFactory;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private static final String REDIRECT_HTML = "/index.html";
    private static final String ROOM_QUERY = "roomId";
    private static final String EMAIL_QUERY = "userEmail";
    public static final PolicyFactory POLICY_DEFINITION = new HtmlPolicyBuilder().toFactory();

    // Retrieve messages from datastore  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {    
        String roomIdQuery = request.getParameter(ROOM_QUERY);       
        Gson gson = ServletUtil.PARSER;
        // Retrieves the entity with matching ID and its corresponding messages property as a JSON string          
        String jsonMessages = gson.toJson(Room.fromRoomId(Long.parseLong(roomIdQuery)).getMessages());          
        response.setContentType("application/json;");    
        response.getWriter().println(jsonMessages);  
    }   
    // Update messages in datastore  
    @Override  
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {    
        String message = getParameter(request, "text-input", "");    
        long timestamp = System.currentTimeMillis();
        // TODO: get sender information based on their login    
        String sender = request.getParameter(EMAIL_QUERY); 
        // Get room ID from URL request    
        String roomIdQuery = request.getParameter(ROOM_QUERY);   
        String sanitizedMessage = POLICY_DEFINITION.sanitize(message);    
        Message chatMessage = Message.createNewMessage(sender, sanitizedMessage, timestamp);

        Room room = Room.fromRoomId(Long.parseLong(roomIdQuery));
        room.addMessage(chatMessage);
        room.toDatastore();          
        // TODO: Correct redirect    
        response.sendRedirect(REDIRECT_HTML);  
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {    
        String value = request.getParameter(name);   
        return value == null ? defaultValue : value;
    }
}

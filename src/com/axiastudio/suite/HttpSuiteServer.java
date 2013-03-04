/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.JsonUtil;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.Store;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiziano
 */
public class HttpSuiteServer {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        start();
    }
    
    public static void start(){
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(9000), 0);
        } catch (IOException ex) {
            Logger.getLogger(HttpSuiteServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if( server != null ){
            HttpContext context = server.createContext("/json", new Handler());
            context.getFilters().add(new EntityFilter()); // POST attributes
            server.setExecutor(null);
            server.start();
        } else {
            Logger.getLogger(HttpSuiteServer.class.getName()).log(Level.SEVERE, "Unable to start server.");
        }
        
    }
    
    static class Handler implements HttpHandler {
        
        @Override
        public void handle(HttpExchange t) throws IOException {
            
            System.out.println(t.getRequestMethod() + ": "+t.getRequestURI());

            
            // token
            String[] tokens = t.getRequestURI().toString().split("/");
            String className = tokens[2];
            String entityId = tokens[3];
            IController controller = (IController) Register.queryUtility(IController.class, className);
            switch( t.getRequestMethod().toUpperCase() ) {
                case "POST":
            //System.out.println(t.getHttpContext().getAttributes());
                    HashMap map = (HashMap) t.getAttribute("entity");
                    Object entity = controller.get(((Integer) map.get("id")).longValue());
                    JsonUtil.jsonToPojo(entity, map);
                    controller.commit(entity);
                    break;
                case "GET":
                    //String response = ")]}',"; // Angular JSONP vulnerability strategy
                    String response = "";
                    if( entityId.equals("full") ){
                        /* full store */
                        Store store = controller.createFullStore();
                        response += "[";
                        for( Object obj: store ){
                            if( response.length()>1 ){
                                response += ",";
                            }
                            // XXX: basterebbero solo gli id...
                            response += JsonUtil.pojoToJson(obj, true);
                        }
                        response += "]";
                    } else {
                        /* single entity
                         * XXX: per ora faccio una cosa molto brutta, ovvero carico lo
                         * store completo e cerco l'entità giusta...
                         */
                        Store store = controller.createFullStore();
                        for( Object obj: store ){
                            Method getId=null;
                            try {
                                getId = obj.getClass().getMethod("getId");
                            } catch (NoSuchMethodException | SecurityException ex) {
                                Logger.getLogger(HttpSuiteServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Long i=null;
                            if( getId != null ){
                                try {
                                    i = (Long) getId.invoke(obj);
                                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                    Logger.getLogger(HttpSuiteServer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if( i.toString().equals(entityId) ){
                                response += JsonUtil.pojoToJson(obj, true);
                            }
                        }

                    }
                    //System.out.println(response);
                    Headers headers = t.getResponseHeaders();
                    headers.add("Content-Type", "application/json");
                    //headers.add("Connection", "keep-alive");
                    //headers.add("Transfer-Encoding", "chunked");
                    t.sendResponseHeaders(200, response.length());
                    try (OutputStream os = t.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    break;
            }
        }
    }
    
}


/*
 * Nota: questo filtro imposta come attributo direttamente il map
 */
class EntityFilter extends Filter {

    @Override
    public void doFilter(HttpExchange he, Chain chain) throws IOException {
        if( he.getRequestMethod().equalsIgnoreCase("POST") ) {
            InputStreamReader isr = new InputStreamReader(he.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);
            String json = br.readLine();
            Map<String, Object> map = JsonUtil.jsonToMap(json);
            he.setAttribute("entity", map.get("entity"));
        }
        chain.doFilter(he);
    }

    @Override
    public String description() {
        return "Parses the requested POST for entity attribute";
    }
    
}


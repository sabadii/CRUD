package servlet;

import controller.PostController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import repository.PostRepository;
import service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@ComponentScan
public class MainServlet extends HttpServlet {
    private PostController controller;
    @Override
    public void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(MainServlet.class);
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            if (method.equals("GET") && path.equals("/api/posts")) {
                handleGetAllPosts(resp);
            } else if (method.equals("GET") && path.matches("/api/posts/\\d+")) {
                handleGetPostById(req, resp);
            } else if (method.equals("POST") && path.equals("/api/posts")) {
                handleSavePost(req, resp);
            } else if (method.equals("DELETE") && path.matches("/api/posts/\\d+")) {
                handleDeletePostById(req, resp);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGetAllPosts(HttpServletResponse resp) throws IOException {
        controller.all(resp);
    }

    private void handleGetPostById(HttpServletRequest req, HttpServletResponse resp){
        final var id = extractIdFromPath(req.getRequestURI());
        controller.getById(id, resp);
    }

    private void handleSavePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        controller.save(req.getReader(), resp);
    }

    private  void handleDeletePostById(HttpServletRequest req, HttpServletResponse resp) {
        final  var id = extractIdFromPath(req.getRequestURI());
        controller.removeById(id, resp);
    }

    private long extractIdFromPath(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/")));
    }
}

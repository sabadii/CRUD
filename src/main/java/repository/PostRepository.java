package repository;

import model.Post;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Stub
public class PostRepository {

    private final List<Post> posts = new ArrayList<>();
    private long postIdCounter = 1;
    public  synchronized List<Post> getAll() {
        return new ArrayList<>(posts);
    }
    public synchronized Post getById(long id) {
        return posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized void save(Post post) {
        if (post.getId() == 0) {
            post.setId(postIdCounter++);
            posts.add(post);
        } else {
            posts.stream()
                    .filter(existingPost -> existingPost.getId() == post.getId())
                    .findFirst()
                    .ifPresent(existingPost ->  {
                        existingPost.setContent(post.getContent());
                    });
        }
    }

    public synchronized void removeById(long id) {
        posts.removeIf(post -> post.getId() == id);
    }

}
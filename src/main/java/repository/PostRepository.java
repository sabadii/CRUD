package repository;

import model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Stub
public interface PostRepository {
    List<Post> all();

    Optional<Post> getById(long id);

    Post save(Post post);

    void removeById(long id);
}
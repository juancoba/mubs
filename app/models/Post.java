package models;


import java.util.*;
import java.lang.Long;
import javax.persistence.*;

import com.avaje.ebean.*;
import play.db.ebean.Model;

@Entity
public class Post extends Model {

    public String title;
    public String postedAt;
    public Date postedDate = new Date();




    @Id
    public Long id;

    @Lob
    public String content;


    public Post(String title, String content, String postedAt) {
        this.title = title;
        this.content = content;
        this.postedAt =  postedAt;

        postedDate = Calendar.getInstance().getTime();

        System.out.println(postedDate);

    }

    public static com.avaje.ebean.Query<Post> byId(){
        return find.where().orderBy("id desc");
    }

    public static List<Post> all(){
        return find.all();
    }

    public static void create(Post post){
        post.save();
    }

    public static void delete(Long id) {
        find.ref(id).delete();
    }

    public static Finder<Long,Post> find = new Finder<>(
            Long.class, Post.class
    );
}
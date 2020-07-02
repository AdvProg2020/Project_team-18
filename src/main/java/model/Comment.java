package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.ArrayList;
public class Comment implements Idable<Comment> {
    private int id;
    private String user;
    private Product product;
    private String commentTitle;
    private String commentBody;
    private transient static ArrayList<Comment>allComments = new ArrayList<>();

    public static ArrayList<Comment> getAllComments() {
        return allComments;
    }

    public Comment() {
    }

    public Comment(String user, Product product, String commentTitle, String commentBody) {
        this.user = user;
        this.product = product;
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.id = idSetter();
    }
    private int idSetter() {
        if (allComments.size() == 0) {
            return 1;
        }
        int max = 0;
        for (Comment comment : allComments) {
            if (comment.id > max)
                max = comment.id;
        }
        return max + 1;
    }

    public Product getProduct() {
        return product;
    }

    public String getCommentTitle() {
        return commentTitle;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object obj) {
        Comment first = (Comment) this;
        Comment second = (Comment) obj;
        boolean user = this.user.equals(((Comment) obj).user);
        boolean title = this.commentTitle.equals(((Comment) obj).commentTitle);
        boolean content = this.commentBody.equals(((Comment) obj).commentBody);
        boolean product = this.product.equals(((Comment) obj).product);
        if (user && title && content && product)
            return true;
        else return false;
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public  Comment getById(int id) {
        for (Comment comment : allComments){
            if (comment.getId()==id)
                return comment;
        }
        return null;
    }
}

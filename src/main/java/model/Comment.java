package model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;

public class Comment {
    private int id;
    private String user;
    private Product product;
    private String commentTitle;
    private String commentBody;
    private static ArrayList<Comment>allComments = new ArrayList<>();

    public static ArrayList<Comment> getAllComments() {
        return allComments;
    }
    @JsonCreator
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
}

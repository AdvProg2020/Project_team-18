package model;

import java.util.ArrayList;

public class Comment {
    private String user;
    private Product product;
    private String commentTitle;
    private String commentBody;
    private static ArrayList<Comment>allComents = new ArrayList<>();

    public static ArrayList<Comment> getAllComents() {
        return allComents;
    }

    public Comment(String user, Product product, String commentTitle, String commentBody) {
        this.user = user;
        this.product = product;
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
    }

    public String getUser() {
        return user;
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

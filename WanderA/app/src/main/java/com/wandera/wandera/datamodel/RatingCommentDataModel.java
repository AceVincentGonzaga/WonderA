package com.wandera.wandera.datamodel;

public class RatingCommentDataModel {
    private String accountId;
    private float rating;
    private String comment;
    private String businessId;

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getComment() {
        return comment;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
    }
}

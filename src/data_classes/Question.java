package data_classes;

public class Question {
    private String auctionId;
    private String userId;
    private String questionId;
    private String questionBeingRespondedId;
    private String text;

    public Question(String auctionId, String userId, String questionId, String questionBeingRespondedId, String text) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.questionId = questionId;
        this.questionBeingRespondedId = questionBeingRespondedId;
        this.text = text;
    }
    public void setAuctionId(String auctionId) {this.auctionId = auctionId;}
    public void setUserId(String userId) {this.userId = userId;}
    public void setQuestionId(String questionId) {this.questionId = questionId;}
    public void setQuestionBeingRespondedId(String questionBeingRespondedId) {this.questionBeingRespondedId = questionBeingRespondedId;}
    public void setText(String text) {this.text = text;}

    public String getAuctionId() {return this.auctionId;}
    public String getUserId() {return this.userId;}
    public String getQuestionId() {return this.questionId;}
    public String getQuestionBeingRespondedId() {return this.questionBeingRespondedId;}
    public String getText() {return this.text;}
}

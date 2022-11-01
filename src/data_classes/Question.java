package data_classes;

public class Question {
    private String userId;
    private String questionId;
    private String questionBeingRespondedId;
    private String text;

    public Question() {}
    public Question(String userId, String questionId, String questionBeingRespondedId, String text) {
        this.userId = userId;
        this.questionId = questionId;
        this.questionBeingRespondedId = questionBeingRespondedId;
        this.text = text;
    }
    public void setUserId(String userId) {this.userId = userId;}
    public void setQuestionId(String questionId) {this.questionId = questionId;}
    public void setQuestionBeingRespondedId(String questionBeingRespondedId) {this.questionBeingRespondedId = questionBeingRespondedId;}
    public void setText(String text) {this.text = text;}

    public String getUserId() {return this.userId;}
    public String getQuestionId() {return this.questionId;}
    public String getQuestionBeingRespondedId() {return this.questionBeingRespondedId;}
    public String getText() {return this.text;}

    @Override
    public String toString() {
        return "Question [Question ID: " + getQuestionId() + " User ID:" + getUserId() + ", Reply to question ID:" + getQuestionBeingRespondedId() + ", Text:" + getText() + "]";
    }
}

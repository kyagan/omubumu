package com.kaanyagan.omubumu.models;

public class QuestionModel {
    private String questionId,questionSubCategoryId,questionRowId,questionChooseId,questionA;
    private Integer questionACount;

    public QuestionModel() {
        this.questionId = questionId;
        this.questionSubCategoryId = questionSubCategoryId;
        this.questionRowId = questionRowId;
        this.questionChooseId = questionChooseId;
        this.questionA = questionA;
        this.questionACount = questionACount;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionSubCategoryId() {
        return questionSubCategoryId;
    }

    public void setQuestionSubCategoryId(String questionSubCategoryId) {
        this.questionSubCategoryId = questionSubCategoryId;
    }

    public String getQuestionRowId() {
        return questionRowId;
    }

    public void setQuestionRowId(String questionRowId) {
        this.questionRowId = questionRowId;
    }

    public String getQuestionChooseId() {
        return questionChooseId;
    }

    public void setQuestionChooseId(String questionChooseId) {
        this.questionChooseId = questionChooseId;
    }

    public String getQuestionA() {
        return questionA;
    }

    public void setQuestionA(String questionA) {
        this.questionA = questionA;
    }

    public Integer getQuestionACount() {
        return questionACount;
    }

    public void setQuestionACount(Integer questionACount) {
        this.questionACount = questionACount;
    }
}

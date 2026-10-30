package com.example.rungta.data.model;

public class StudentRiskPrediction {
    private Student student;
    private float currentPercentage;
    private float lastMonthPercentage;
    private float previousMonthPercentage;
    private float predictedPercentage;
    private String riskLevel; // "LOW RISK", "MEDIUM RISK", "HIGH RISK"
    private String recommendation;
    private String explanationReason;

    public StudentRiskPrediction(Student student, float currentPercentage, float lastMonthPercentage, float previousMonthPercentage, float predictedPercentage, String riskLevel, String recommendation, String explanationReason) {
        this.student = student;
        this.currentPercentage = currentPercentage;
        this.lastMonthPercentage = lastMonthPercentage;
        this.previousMonthPercentage = previousMonthPercentage;
        this.predictedPercentage = predictedPercentage;
        this.riskLevel = riskLevel;
        this.recommendation = recommendation;
        this.explanationReason = explanationReason;
    }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public float getCurrentPercentage() { return currentPercentage; }
    public void setCurrentPercentage(float currentPercentage) { this.currentPercentage = currentPercentage; }

    public float getLastMonthPercentage() { return lastMonthPercentage; }
    public void setLastMonthPercentage(float lastMonthPercentage) { this.lastMonthPercentage = lastMonthPercentage; }

    public float getPreviousMonthPercentage() { return previousMonthPercentage; }
    public void setPreviousMonthPercentage(float previousMonthPercentage) { this.previousMonthPercentage = previousMonthPercentage; }

    public float getPredictedPercentage() { return predictedPercentage; }
    public void setPredictedPercentage(float predictedPercentage) { this.predictedPercentage = predictedPercentage; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public String getExplanationReason() { return explanationReason; }
    public void setExplanationReason(String explanationReason) { this.explanationReason = explanationReason; }
}

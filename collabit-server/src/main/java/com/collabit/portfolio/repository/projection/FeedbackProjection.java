package com.collabit.portfolio.repository.projection;

public interface FeedbackProjection {
    String getCode();
    String getFeedback();
    boolean getIsPositive();
    String getName();
}

package com.skillsync.Exception;

public class FeedbackNotFoundException extends RuntimeException {
    public FeedbackNotFoundException(Long id) {
        super("⚠️ Feedback not found with ID: " + id);
    }
}

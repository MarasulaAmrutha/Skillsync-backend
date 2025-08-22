package com.skillsync.feedback.service;

import com.skillsync.model.Feedback; // Corrected import path
import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackService {
    // Existing CRUD methods
    Feedback saveFeedback(Feedback feedback);
    List<Feedback> getAllFeedback();
    Feedback getFeedbackById(Long id);
    void deleteFeedback(Long id);
    Feedback updateFeedback(Long id, Feedback updatedFeedback);

    // NEW: For Comment Management & Tagging
    void updateFeedbackStatus(Long id, String status);
    void addTagsToFeedback(Long id, String tagsToAdd);

    // NEW: Retrieval methods for Centralized Feedback Dashboard filtering
    List<Feedback> getFeedbackByCourse(Long courseId);
    List<Feedback> getFeedbackByUser(Long userId);
    List<Feedback> getFeedbackByTrainer(Long trainerId);
    List<Feedback> getFeedbackByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Feedback> getFeedbackByOverallRatingGreaterThanEqual(Integer rating);
    List<Feedback> getFeedbackByStatus(String status);
    List<Feedback> getFeedbackByTag(String tag);
    List<Feedback> getFeedbackByCourseAndStatus(Long courseId, String status); // Added for completeness if needed

    // NEW: Analytics methods for Centralized Feedback Dashboard
    Double getAverageOverallRatingForCourse(Long courseId);
    Long getFeedbackCountForCourse(Long courseId);
    Double getAverageContentRelevanceRatingForCourse(Long courseId);
    Double getAverageTrainerEffectivenessRatingForCourse(Long courseId);
}
package com.skillsync.feedback.service;

import com.skillsync.model.Feedback; // Corrected import path
import com.skillsync.repository.FeedbackRepository; // Keep original package path
import com.skillsync.Exception.FeedbackNotFoundException; // Ensure this exception class exists
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        // @PrePersist in Feedback entity handles submissionTimestamp
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));
    }

    @Override
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new FeedbackNotFoundException(id);
        }
        feedbackRepository.deleteById(id);
    }

    @Override
    public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
        // Retrieve existing feedback by ID, will throw FeedbackNotFoundException if not found
        Feedback existingFeedback = getFeedbackById(id);

        // Update all relevant fields from the incoming updatedFeedback object
        // The @Data annotation in Feedback.java generates these setters
        existingFeedback.setComment(updatedFeedback.getComment());
        existingFeedback.setRating(updatedFeedback.getRating());
        // For these fields (userId, courseId, trainerId), typically you wouldn't update them
        // in a PUT request after initial submission, but if your design requires it, keep them.
        existingFeedback.setUserId(updatedFeedback.getUserId());
        existingFeedback.setCourseId(updatedFeedback.getCourseId());
        existingFeedback.setTrainerId(updatedFeedback.getTrainerId());

        // Update the new detailed ratings
        existingFeedback.setContentRelevanceRating(updatedFeedback.getContentRelevanceRating());
        existingFeedback.setTrainerEffectivenessRating(updatedFeedback.getTrainerEffectivenessRating());
        existingFeedback.setWouldRecommend(updatedFeedback.getWouldRecommend());
        existingFeedback.setIsAnonymous(updatedFeedback.getIsAnonymous());

        // Update management fields
        existingFeedback.setTags(updatedFeedback.getTags());
        existingFeedback.setStatus(updatedFeedback.getStatus());
        existingFeedback.setAdminNotes(updatedFeedback.getAdminNotes());

        // @PreUpdate in Feedback entity handles lastUpdatedTimestamp
        return feedbackRepository.save(existingFeedback);
    }

    // --- New Methods for Comment Management & Tagging ---

    @Override
    public void updateFeedbackStatus(Long id, String status) {
        Feedback feedback = getFeedbackById(id); // Throws if not found
        feedback.setStatus(status);
        feedbackRepository.save(feedback);
    }

    @Override
    public void addTagsToFeedback(Long id, String tagsToAdd) {
        Feedback feedback = getFeedbackById(id); // Throws if not found

        // Get existing tags, split by comma, trim whitespace, collect into a Set to manage uniqueness
        Set<String> existingTags = (feedback.getTags() != null && !feedback.getTags().isEmpty())
                                 ? Arrays.stream(feedback.getTags().split(","))
                                         .map(String::trim)
                                         .collect(Collectors.toSet())
                                 : new java.util.HashSet<>();

        // Split new tags, trim, and collect into a Set
        Set<String> newTags = Arrays.stream(tagsToAdd.split(","))
                                     .map(String::trim)
                                     .collect(Collectors.toSet());

        // Add all new tags to the existing set; Set automatically handles duplicates
        existingTags.addAll(newTags);

        // Convert the combined set back into a comma-separated string
        feedback.setTags(String.join(",", existingTags));
        feedbackRepository.save(feedback);
    }

    // --- New Retrieval Methods for Centralized Feedback Dashboard Filtering ---

    @Override
    public List<Feedback> getFeedbackByCourse(Long courseId) {
        return feedbackRepository.findByCourseId(courseId);
    }

    @Override
    public List<Feedback> getFeedbackByUser(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }

    @Override
    public List<Feedback> getFeedbackByTrainer(Long trainerId) {
        return feedbackRepository.findByTrainerId(trainerId);
    }

    @Override
    public List<Feedback> getFeedbackByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return feedbackRepository.findBySubmissionTimestampBetween(startDate, endDate);
    }

    @Override
    public List<Feedback> getFeedbackByOverallRatingGreaterThanEqual(Integer rating) {
        return feedbackRepository.findByRatingGreaterThanEqual(rating);
    }

    @Override
    public List<Feedback> getFeedbackByStatus(String status) {
        return feedbackRepository.findByStatus(status);
    }

    @Override
    public List<Feedback> getFeedbackByTag(String tag) {
        // This performs a 'LIKE %tag%' search on the tags column.
        return feedbackRepository.findByTagsContaining(tag);
    }

    @Override
    public List<Feedback> getFeedbackByCourseAndStatus(Long courseId, String status) {
        return feedbackRepository.findByCourseIdAndStatus(courseId, status);
    }

    // --- New Analytics Methods for Centralized Feedback Dashboard ---

    @Override
    public Double getAverageOverallRatingForCourse(Long courseId) {
        List<Feedback> feedbackList = feedbackRepository.findByCourseId(courseId);
        OptionalDouble average = feedbackList.stream()
                .filter(f -> f.getRating() != null)
                .mapToInt(Feedback::getRating)
                .average();
        return average.isPresent() ? average.getAsDouble() : null;
    }

    @Override
    public Long getFeedbackCountForCourse(Long courseId) {
        return feedbackRepository.countByCourseId(courseId);
    }

    @Override
    public Double getAverageContentRelevanceRatingForCourse(Long courseId) {
        List<Feedback> feedbackList = feedbackRepository.findByCourseId(courseId);
        OptionalDouble average = feedbackList.stream()
                .filter(f -> f.getContentRelevanceRating() != null)
                .mapToInt(Feedback::getContentRelevanceRating)
                .average();
        return average.isPresent() ? average.getAsDouble() : null;
    }

    @Override
    public Double getAverageTrainerEffectivenessRatingForCourse(Long courseId) {
        List<Feedback> feedbackList = feedbackRepository.findByCourseId(courseId);
        OptionalDouble average = feedbackList.stream()
                .filter(f -> f.getTrainerEffectivenessRating() != null)
                .mapToInt(Feedback::getTrainerEffectivenessRating)
                .average();
        return average.isPresent() ? average.getAsDouble() : null;
    }
}
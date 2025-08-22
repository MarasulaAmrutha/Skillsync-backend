package com.skillsync.repository; // Keep original package path

import com.skillsync.model.Feedback; // Corrected import path for Feedback model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByCourseId(Long courseId);
    List<Feedback> findByUserId(Long userId);
    List<Feedback> findByTrainerId(Long trainerId);
    List<Feedback> findBySubmissionTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Feedback> findByRatingGreaterThanEqual(Integer rating);

    // New queries for management and filtering
    List<Feedback> findByStatus(String status);
    List<Feedback> findByTagsContaining(String tag); // For simple tag search
    List<Feedback> findByCourseIdAndStatus(Long courseId, String status);

    long countByCourseId(Long courseId);
}



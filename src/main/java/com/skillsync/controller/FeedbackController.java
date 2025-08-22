package com.skillsync.controller;

import com.skillsync.model.Feedback;
import com.skillsync.feedback.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3009", "*"}) // Allow all origins for testing
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<Feedback> submitFeedback(@Valid @RequestBody Feedback feedback) {
        log.info("Received feedback submission: {}", feedback);
        Feedback saved = feedbackService.saveFeedback(feedback);
        return ResponseEntity
                .created(URI.create("/api/feedback/" + saved.getId()))
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(feedback);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @Valid @RequestBody Feedback updatedFeedback) {
        Feedback feedback = feedbackService.updateFeedback(id, updatedFeedback);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Feedback>> getFeedbackByCourse(@PathVariable Long courseId) {
        List<Feedback> feedback = feedbackService.getFeedbackByCourse(courseId);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbackByUser(@PathVariable Long userId) {
        List<Feedback> feedback = feedbackService.getFeedbackByUser(userId);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<List<Feedback>> getFeedbackByTrainer(@PathVariable Long trainerId) {
        List<Feedback> feedback = feedbackService.getFeedbackByTrainer(trainerId);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Feedback>> getFeedbackByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Feedback> feedback = feedbackService.getFeedbackByDateRange(startDate, endDate);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Feedback>> getFeedbackByStatus(@PathVariable String status) {
        List<Feedback> feedback = feedbackService.getFeedbackByStatus(status);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Feedback>> getFeedbackByTag(@PathVariable String tag) {
        List<Feedback> feedback = feedbackService.getFeedbackByTag(tag);
        return ResponseEntity.ok(feedback);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateFeedbackStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        if (status == null || status.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        feedbackService.updateFeedbackStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/tags")
    public ResponseEntity<Void> addTagsToFeedback(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String tagsToAdd = payload.get("tags");
        if (tagsToAdd == null || tagsToAdd.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        feedbackService.addTagsToFeedback(id, tagsToAdd);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/course/{courseId}/average-rating")
    public ResponseEntity<Double> getAverageOverallRatingForCourse(@PathVariable Long courseId) {
        Double avg = feedbackService.getAverageOverallRatingForCourse(courseId);
        return ResponseEntity.ok(avg != null ? avg : 0.0);
    }

    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<Long> getFeedbackCountForCourse(@PathVariable Long courseId) {
        Long count = feedbackService.getFeedbackCountForCourse(courseId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/course/{courseId}/average-content-relevance")
    public ResponseEntity<Double> getAverageContentRelevanceRatingForCourse(@PathVariable Long courseId) {
        Double avg = feedbackService.getAverageContentRelevanceRatingForCourse(courseId);
        return ResponseEntity.ok(avg != null ? avg : 0.0);
    }

    @GetMapping("/course/{courseId}/average-trainer-effectiveness")
    public ResponseEntity<Double> getAverageTrainerEffectivenessRatingForCourse(@PathVariable Long courseId) {
        Double avg = feedbackService.getAverageTrainerEffectivenessRatingForCourse(courseId);
        return ResponseEntity.ok(avg != null ? avg : 0.0);
    }
}
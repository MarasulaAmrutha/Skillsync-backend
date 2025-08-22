package com.skillsync.model; // Corrected package name as per your structure

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data; // Ensure Lombok is correctly set up in your project
import java.time.LocalDateTime; // Import for timestamp

@Data // THIS ANNOTATION IS CRUCIAL for auto-generating getters and setters for ALL fields
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Core Feedback Content ---
    @NotBlank(message = "Comment cannot be blank")
    @Size(max = 2000, message = "Comment cannot exceed 2000 characters")
    @Column(name = "comment_text", length = 2000) // Renamed for clarity, using 'comment_text'
    private String comment;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    @Column(name = "overall_rating")
    private Integer rating; // Renamed to 'rating' for consistency with UI

    // --- Linking Fields ---
    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "Course ID cannot be null")
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "trainer_id")
    private Long trainerId; // Optional: can be null

    // --- Additional Structured Feedback ---
    @Min(value = 1, message = "Content relevance rating must be at least 1")
    @Max(value = 5, message = "Content relevance rating cannot be more than 5")
    @Column(name = "content_relevance_rating")
    private Integer contentRelevanceRating;

    @Min(value = 1, message = "Trainer effectiveness rating must be at least 1")
    @Max(value = 5, message = "Trainer effectiveness rating cannot be more than 5")
    @Column(name = "trainer_effectiveness_rating")
    private Integer trainerEffectivenessRating;

    @Column(name = "would_recommend")
    private Boolean wouldRecommend; // Yes/No question

    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false; // Default to false

    // --- Management & Tagging ---
    @Column(name = "tags")
    private String tags; // Comma-separated tags (e.g., "improvement, ui_bug")

    @Column(name = "status")
    private String status = "New"; // e.g., "New", "Reviewed", "Actioned", "Closed"

    @Column(name = "admin_notes", length = 1000)
    private String adminNotes; // For internal admin notes

    // --- Timestamp ---
    @Column(name = "submission_timestamp", nullable = false)
    private LocalDateTime submissionTimestamp;

    @Column(name = "last_updated_timestamp")
    private LocalDateTime lastUpdatedTimestamp; // For tracking admin updates

    // --- Constructors ---
    public Feedback() {
        this.submissionTimestamp = LocalDateTime.now(); // Set on creation
    }

    // Constructor for initial submission (without all details)
    public Feedback(String comment, Integer rating, Long userId, Long courseId) {
        this.comment = comment;
        this.rating = rating;
        this.userId = userId;
        this.courseId = courseId;
        this.submissionTimestamp = LocalDateTime.now();
    }

    // --- Lifecycle Callbacks ---
    @PrePersist
    protected void onCreate() {
        if (submissionTimestamp == null) {
            submissionTimestamp = LocalDateTime.now();
        }
        if (lastUpdatedTimestamp == null) {
            lastUpdatedTimestamp = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedTimestamp = LocalDateTime.now();
    }

    // Lombok's @Data handles getters/setters automatically.
    // You do NOT need to write explicit getters/setters if @Data is used and Lombok is set up.
}
package com.example.gourmet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "店名は必須です")
    @Size(max = 100, message = "店名は100文字以内で入力してください")
    private String name;

    @NotBlank(message = "カテゴリは必須です")
    private String category;

    @NotNull(message = "評価は必須です")
    @Min(value = 1, message = "評価は1以上で入力してください")
    @Max(value = 5, message = "評価は5以下で入力してください")
    private Integer rating;

    @NotBlank(message = "予算感は必須です")
    private String budget;

    private String hideawayLevel;

    @NotBlank(message = "コメントは必須です")
    @Size(max = 500, message = "コメントは500文字以内で入力してください")
    private String comment;

    private Integer reactionCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getBudget() { return budget; }
    public void setBudget(String budget) { this.budget = budget; }

    public String getHideawayLevel() { return hideawayLevel; }
    public void setHideawayLevel(String hideawayLevel) { this.hideawayLevel = hideawayLevel; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getReactionCount() { return reactionCount; }
    public void setReactionCount(Integer reactionCount) { this.reactionCount = reactionCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

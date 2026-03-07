package gov.idrd.sports.domain.athlete;

import gov.idrd.sports.domain.exception.DomainValidationException;

public class Athlete {

    private Long id;
    private String name;
    private Integer age;
    private String category;
    private Long trainerId;

    private Athlete(Long id, String name, Integer age, String category, Long trainerId) {
        this.id = id;
        this.name = validateName(name);
        this.age = validateAge(age);
        this.category = validateCategory(category);
        this.trainerId = trainerId;
    }

    public static Athlete create(String name, Integer age, String category, Long trainerId) {
        return new Athlete(null, name, age, category, trainerId);
    }

    public static Athlete restore(Long id, String name, Integer age, String category, Long trainerId) {
        if (id == null || id <= 0) {
            throw new DomainValidationException("Athlete id must be a positive value");
        }
        return new Athlete(id, name, age, category, trainerId);
    }

    public void updateProfile(String name, Integer age, String category, Long trainerId) {
        this.name = validateName(name);
        this.age = validateAge(age);
        this.category = validateCategory(category);
        this.trainerId = trainerId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getCategory() {
        return category;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    private String validateName(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Athlete name is required");
        }
        return value.trim();
    }

    private Integer validateAge(Integer value) {
        if (value == null || value <= 0) {
            throw new DomainValidationException("Athlete age must be greater than zero");
        }
        return value;
    }

    private String validateCategory(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Athlete category is required");
        }
        return value.trim();
    }
}
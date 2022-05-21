package model;

import java.io.Serializable;
import java.util.Date;

public class HistoryEntity implements Serializable {

    private Integer id;
    private Integer categoryId;
    private String categoryName;
    private Long payment;
    private String date;
    private String description;
    private Integer profileId;

    @Override
    public String toString() {
        return "HistoryEntity{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", payment=" + payment +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", profileId=" + profileId +
                '}';
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public HistoryEntity() {
    }

    public HistoryEntity(String categoryName, Long payment, String date) {
        this.categoryName = categoryName;
        this.payment = payment;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getPayment() {
        return payment;
    }

    public void setPayment(Long payment) {
        this.payment = payment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

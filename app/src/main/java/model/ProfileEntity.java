package model;

import java.io.Serializable;

public class ProfileEntity implements Serializable {
    private Integer id;
    private String name;

    @Override
    public String toString() {
        return "ProfileEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", address='" + address + '\'' +
                ", job='" + job + '\'' +
                ", account=" + account +
                '}';
    }

    private String phone;
    private String password;
    private String gender;
    private String birthDate;
    private String address;
    private String job;
    private Long account;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public ProfileEntity() {
    }

    public ProfileEntity(Integer id, String name, String phone, String password, String gender, String birthDate, String address, String job, Long account) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
        this.job = job;
        this.account = account;
    }
}

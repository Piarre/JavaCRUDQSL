package fr.piarre.Models;

public class User {
    public Integer id;
    public String name;
    public String surname;
    public String email;
    public String password;
    public String actif;
    public Integer age;
    public String createdAt;
    public String updatedAt;

    public User(String email) {
        this.email = email;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String surname, String email, String password, String actif, Integer age) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.actif = actif;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getActif() {
        return actif;
    }

    public Integer getAge() {
        return age;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", actif='" + actif + '\'' +
                ", age=" + age +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public void PrintValues() {
        System.out.println(id + " | " + name + " | " + surname + " | " + email + " | " + password + " | " + actif + " | " + age);
    }

    public void Print() {
        System.out.println("Id: " + id + " | name: " + name + " | surname: " + surname + " | email: " + email + " | password: " + password + " | actif: " + actif + " | age: " + age);
    }
}

package com.example.tutoforreal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Task {
    private int id;
    private String title;
    private TaskStatus taskStatus;
    private String description;
    private LocalDate publishedDate;
    private int projectId;
    private LocalDate deadline;
    private int taskDuration;
    private String author;
    private String asignedTo;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setTaskDuration(int taskDuration) {
        this.taskDuration = taskDuration;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAsignedTo(String asignedTo) {
        this.asignedTo = asignedTo;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public LocalDate getDeadLine() {
        return deadline;
    }

    public int getTaskDuration() {
        return taskDuration;
    }

    public String getAuthor() {
        return author;
    }

    public String getAssignedTo() {
        return asignedTo;
    }

    public Task(String title, TaskStatus taskStatus, String description, int projectId, LocalDate deadline, int taskDuration, String author, String asignedTo) {
        this.title = title;
        this.taskStatus = taskStatus;
        this.description = description;
        this.publishedDate = LocalDate.from(LocalDateTime.now());
        this.projectId = projectId;
        this.deadline = deadline;
        this.taskDuration = taskDuration;
        this.author = author;
        this.asignedTo = asignedTo;
    }

    public Task(int id, String title, TaskStatus taskStatus, String description, LocalDate publishedDate,  int projectId, LocalDate deadline, int taskDuration, String author, String asignedTo) {
        this.id = id;
        this.title = title;
        this.taskStatus = taskStatus;
        this.description = description;
        this.publishedDate = publishedDate;
        this.projectId = projectId;
        this.deadline = deadline;
        this.taskDuration = taskDuration;
        this.author = author;
        this.asignedTo = asignedTo;
    }

    public static LocalDate getDate(){
        Scanner time = new Scanner(System.in);
        System.out.println("annee : ");
        int year = time.nextInt();
        System.out.println("mois");
        int month = time.nextInt();
        System.out.println("jour");
        int day = time.nextInt();
        LocalDate date = LocalDate.of( year , month , day );
        return date;
    }
/*
    static Task createtask(int idProject){
        Scanner scan = new Scanner(System.in);
        System.out.println("title");
        String title = scan.nextLine();
        System.out.println("task status");
        TaskStatus taskStatus = TaskStatus.valueOf(scan.nextLine());
        System.out.println("description");
        String description = scan.nextLine();
        int projectId = idProject;
        System.out.println("deadline");
        LocalDate deadline = getDate();
        System.out.println("taskduration");
        int taskDuration = scan.nextInt();
        System.out.println("author");
        scan.nextLine();
        String author = scan.nextLine();
        System.out.println("assignedto");
        String assignedTo = scan.nextLine();
        Task newTask = new Task(title, taskStatus, description, projectId, deadline, taskDuration, author, assignedTo);
        return newTask;
    }
*/
    @Override
    public String toString() {
        return "Tâche : " +
                "\n" +
                "* Id : " + id +
                "\n" +
                "* Titre de la tâche : '" + title +
                "\n" +
                "* Status de la tâche : " + taskStatus +
                "\n" +
                "* Description : '" + description +
                "\n" +
                "* Date de publication : " + publishedDate +
                "\n" +
                "* Id du projet auquel appartient la tâche : " + projectId +
                "\n" +
                "* Deadline : " + deadline +
                "\n" +
                "* Durée estimée de la tâche : " + taskDuration +
                "\n" +
                "* Auteur : " + author +
                "\n" +
                "* Assignée à : " + asignedTo +
                "\n" + "\n";
    }
}

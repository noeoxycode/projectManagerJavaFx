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
    private LocalDate deadLine;
    private int taskDuration;
    private String author;
    private String asignedTo;

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
        return deadLine;
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

    public Task(String title, TaskStatus taskStatus, String description, int projectId, LocalDate deadLine, int taskDuration, String author, String asignedTo) {
        this.title = title;
        this.taskStatus = taskStatus;
        this.description = description;
        this.publishedDate = LocalDate.from(LocalDateTime.now());
        this.projectId = projectId;
        this.projectId = projectId;
        this.deadLine = deadLine;
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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", description='" + description + '\'' +
                ", publishedDate=" + publishedDate +
                ", projectId=" + projectId +
                ", deadLine=" + deadLine +
                ", taskDuration=" + taskDuration +
                ", author=" + author +
                ", asignedTo=" + asignedTo +
                '}';
    }
}

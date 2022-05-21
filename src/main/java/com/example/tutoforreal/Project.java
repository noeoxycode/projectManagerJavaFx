package com.example.tutoforreal;

import java.util.ArrayList;
import java.util.Scanner;

public class Project {
    private int id;
    private String title;
    private String description;
    private ArrayList<Task> projectList = new ArrayList();

    public Project(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Project(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() { return description; }

    public int getId() {return id;}

    public void setDescription(String description) { this.description = description; }

    public ArrayList<Task> getProjectList() {
        return projectList;
    }

    public void setProjectList(ArrayList<Task> projectList) {
        this.projectList = projectList;
    }

    static Project createProject() {
        System.out.println("Saisisez le titre de votre projet : ");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        System.out.println("Saisisez la description de votre projet : ");
        String description = scan.nextLine();
        System.out.println("Votre projet s'appelle " + name);
        System.out.println("La description de votre projet est : \n" + description);
        Project newProject = new Project(name, description);
        return newProject;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", projectList=" + projectList +
                '}';
    }
}

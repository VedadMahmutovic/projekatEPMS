package model;

public class Employee {
    private int id;
    private String ime;
    private String prezime;
    private String pozicija;
    private double plata;
    private int radiSati;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return ime; }
    public void setName(String name) { this.ime = name; }
    public String getSurname() { return prezime; }
    public void setSurname(String surname) { this.prezime = surname; }
    public String getPosition() { return pozicija; }
    public void setPosition(String position) { this.pozicija = position; }
    public double getSalary() { return plata; }
    public void setSalary(double salary) { this.plata = salary; }
    public int getHoursWorked() { return radiSati; }
    public void setHoursWorked(int hoursWorked) { this.radiSati = hoursWorked; }
}

package com.example.demo.assignmentofice.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "weekly_planner")
@Data
public class WeeklyPlanModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project")
    private Long project;

    @Column(name = "meeting_date")
    private LocalDate meetingDate;

    @Column(name = "duration")
    private String duration;

    @Column(name = "meeting_title")
    private String meetingTitle;

    @Column(name = "objective")
    private String objective;

    @Column(name = "status")
    private Long status;
}
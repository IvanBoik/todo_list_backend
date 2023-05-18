package com.boiko.todo_list.entity;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private String id;

    private String name;

    private String description;

    private boolean isDone;

    private Date date;
}
